package org.lanternpowered.server.data.manipulator.gen;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nullable;

public abstract class TypeGenerator {

    private static final AtomicInteger counter = new AtomicInteger();

    static String newName(Class<?> manipulatorType) {
        return newName(manipulatorType.getName());
    }

    static String newName(String name) {
        return name + "ImplXYZ" + counter.getAndIncrement();
    }

    static String newInternalName(Class<?> manipulatorType) {
        return newName(Type.getInternalName(manipulatorType));
    }

    static String newInternalName(String name) {
        return newName(name);
    }

    abstract <M extends DataManipulator<M, I>, I extends ImmutableDataManipulator<I, M>> void generateClasses(String[] classNames,
            ClassWriter cwM, ClassWriter cwI, Class<M> manipulatorType, Class<I> immutableManipulatorType,
            @Nullable Class<? extends M> mutableExpansion,
            @Nullable Class<? extends I> immutableExpansion);
}
