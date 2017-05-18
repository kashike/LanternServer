package org.lanternpowered.server.data.manipulator.gen;

import org.lanternpowered.server.data.manipulator.DataManipulatorRegistration;
import org.lanternpowered.server.data.value.IValueContainer;
import org.lanternpowered.server.util.DefineableClassLoader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.util.Tuple;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;

/**
 * This generator will attempt to auto generate {@link DataManipulator} classes
 * with the proper constructors, methods, etc. So that they can be registered
 * as a {@link DataManipulatorRegistration}.
 */
public final class DataManipulatorGenerator {

    private final DefineableClassLoader classLoader = new DefineableClassLoader();
    private final List<Tuple<TypeGenerator, Predicate<Class<?>>>> typeGenerators = new ArrayList<>();
    private final DataManipulatorRegistrationGenerator registrationGenerator = new DataManipulatorRegistrationGenerator();

    {
        addTypeGenerator(new AbstractDataTypeGenerator(), type -> true);

    }

    public void addTypeGenerator(TypeGenerator typeGenerator, Predicate<Class<?>> predicate) {
        this.typeGenerators.add(0, new Tuple<>(typeGenerator, predicate));
    }

    @SuppressWarnings("unchecked")
    public <M extends DataManipulator<M, I>, I extends ImmutableDataManipulator<I, M>> DataManipulatorRegistration<M, I> newRegistrationFor(
            PluginContainer pluginContainer, String id, String name,
            Class<M> manipulatorType, Class<I> immutableManipulatorType,
            @Nullable Class<? extends M> mutableExpansion, @Nullable Class<? extends I> immutableExpansion,
            @Nullable Consumer<IValueContainer<?>> registrationConsumer) {
        final ClassWriter cwM = new ClassWriter(Opcodes.V1_8);
        final ClassWriter cwI = new ClassWriter(Opcodes.V1_8);
        final String[] classNames = new String[2];
        for (Tuple<TypeGenerator, Predicate<Class<?>>> tuple : this.typeGenerators) {
            if (tuple.getSecond().test(manipulatorType)) {
                tuple.getFirst().generateClasses(classNames, cwM, cwI, manipulatorType, immutableManipulatorType, mutableExpansion,
                        immutableExpansion);
                break;
            }
        }
        byte[] bytes = cwM.toByteArray();
        final Class<?> manipulatorTypeImpl = this.classLoader.defineClass(classNames[0], bytes);
        bytes = cwI.toByteArray();
        final Class<?> immutableManipulatorTypeImpl = this.classLoader.defineClass(classNames[1], bytes);

        try {
            manipulatorTypeImpl.getField("registrationConsumer").set(null, registrationConsumer);
            immutableManipulatorTypeImpl.getField("registrationConsumer").set(null, registrationConsumer);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        final ClassWriter cw = new ClassWriter(Opcodes.V1_8);
        final String className = this.registrationGenerator.generate(cw,
                (Class) manipulatorType, (Class) manipulatorTypeImpl, (Class) immutableManipulatorType, (Class) immutableManipulatorTypeImpl);
        bytes = cw.toByteArray();
        final Class<?> registrationClass = this.classLoader.defineClass(className, bytes);
        try {
            return (DataManipulatorRegistration) registrationClass
                    .getConstructor(PluginContainer.class, String.class, String.class)
                    .newInstance(pluginContainer, id, name);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
