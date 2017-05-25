/*
 * This file is part of LanternServer, licensed under the MIT License (MIT).
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the Software), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED AS IS, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.lanternpowered.server.data.manipulator.gen;

import static org.lanternpowered.server.data.manipulator.gen.TypeGenerator.newInternalName;

import com.google.common.collect.ImmutableList;
import org.lanternpowered.server.data.manipulator.DataManipulatorRegistration;
import org.lanternpowered.server.data.value.IValueContainer;
import org.lanternpowered.server.util.DefineableClassLoader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.manipulator.immutable.ImmutableListData;
import org.spongepowered.api.data.manipulator.immutable.ImmutableVariantData;
import org.spongepowered.api.data.manipulator.mutable.ListData;
import org.spongepowered.api.data.manipulator.mutable.VariantData;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.util.Tuple;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
        addTypeGenerator(new AbstractListDataTypeGenerator(), ListData.class::isAssignableFrom);
    }

    public void addTypeGenerator(TypeGenerator typeGenerator, Predicate<Class<?>> predicate) {
        this.typeGenerators.add(0, new Tuple<>(typeGenerator, predicate));
    }

    @SuppressWarnings("unchecked")
    public <M extends ListData<E, M, I>, I extends ImmutableListData<E, I, M>, E> DataManipulatorRegistration<M, I> newListRegistrationFor(
            PluginContainer pluginContainer, String id, String name, Class<M> manipulatorType, Class<I> immutableManipulatorType,
            Key<ListValue<E>> key, Supplier<List<E>> listSupplier) {
        final Base<M, I> base = generateBase(pluginContainer, id, name,
                manipulatorType, immutableManipulatorType, null, null);
        try {
            base.mutableManipulatorTypeImpl.getField("key").set(null, key);
            base.mutableManipulatorTypeImpl.getField("listSupplier").set(null, listSupplier);
            base.immutableManipulatorTypeImpl.getField("key").set(null, key);
            base.immutableManipulatorTypeImpl.getField("listSupplier").set(null, (Supplier<List>) () -> ImmutableList.copyOf(listSupplier.get()));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        return base.supplier.get();
    }

    @SuppressWarnings("unchecked")
    public <M extends DataManipulator<M, I>, I extends ImmutableDataManipulator<I, M>> DataManipulatorRegistration<M, I> newRegistrationFor(
            PluginContainer pluginContainer, String id, String name,
            Class<M> manipulatorType, Class<I> immutableManipulatorType,
            @Nullable Class<? extends M> mutableExpansion, @Nullable Class<? extends I> immutableExpansion,
            @Nullable Consumer<IValueContainer<?>> registrationConsumer) {
        final Base<M, I> base = generateBase(pluginContainer, id, name,
                manipulatorType, immutableManipulatorType, mutableExpansion, immutableExpansion);
        try {
            base.mutableManipulatorTypeImpl.getField("registrationConsumer").set(null, registrationConsumer);
            base.immutableManipulatorTypeImpl.getField("registrationConsumer").set(null, registrationConsumer);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        return base.supplier.get();
    }

    private static final class Base<M extends DataManipulator<M, I>, I extends ImmutableDataManipulator<I, M>> {

        private final Supplier<DataManipulatorRegistration<M, I>> supplier;
        private final Class<? extends M> mutableManipulatorTypeImpl;
        private final Class<? extends M> immutableManipulatorTypeImpl;

        private Base(Supplier<DataManipulatorRegistration<M, I>> supplier, Class<? extends M> mutableManipulatorTypeImpl,
                Class<? extends M> immutableManipulatorTypeImpl) {
            this.immutableManipulatorTypeImpl = immutableManipulatorTypeImpl;
            this.mutableManipulatorTypeImpl = mutableManipulatorTypeImpl;
            this.supplier = supplier;
        }
    }

    @SuppressWarnings("unchecked")
    private <M extends DataManipulator<M, I>, I extends ImmutableDataManipulator<I, M>> Base<M, I> generateBase(
            PluginContainer pluginContainer, String id, String name,
            Class<M> manipulatorType, Class<I> immutableManipulatorType,
            @Nullable Class<? extends M> mutableExpansion, @Nullable Class<? extends I> immutableExpansion) {
        final ClassWriter cwM = new ClassWriter(Opcodes.V1_8);
        final ClassWriter cwI = new ClassWriter(Opcodes.V1_8);

        final String mutableImplTypeName = newInternalName(manipulatorType);
        final String immutableImplTypeName = newInternalName(immutableManipulatorType);

        final String mutableImplClassName = mutableImplTypeName.replace('/', '.');
        final String immutableImplClassName = immutableImplTypeName.replace('/', '.');

        for (Tuple<TypeGenerator, Predicate<Class<?>>> tuple : this.typeGenerators) {
            if (tuple.getSecond().test(manipulatorType)) {
                tuple.getFirst().generateClasses(cwM, cwI, mutableImplTypeName, immutableImplTypeName,
                        manipulatorType, immutableManipulatorType, mutableExpansion, immutableExpansion);
                break;
            }
        }
        byte[] bytes = cwM.toByteArray();
        final Class<?> manipulatorTypeImpl = this.classLoader.defineClass(mutableImplClassName, bytes);
        bytes = cwI.toByteArray();
        final Class<?> immutableManipulatorTypeImpl = this.classLoader.defineClass(immutableImplClassName, bytes);

        final ClassWriter cw = new ClassWriter(Opcodes.V1_8);
        final String className = this.registrationGenerator.generate(cw,
                (Class) manipulatorType, (Class) manipulatorTypeImpl, (Class) immutableManipulatorType, (Class) immutableManipulatorTypeImpl);
        bytes = cw.toByteArray();
        final Class<?> registrationClass = this.classLoader.defineClass(className, bytes);
        return new Base(() -> {
            try {
                return registrationClass
                        .getConstructor(PluginContainer.class, String.class, String.class)
                        .newInstance(pluginContainer, id, name);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }, manipulatorTypeImpl, immutableManipulatorTypeImpl);
    }
}
