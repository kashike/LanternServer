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

    abstract <M extends DataManipulator<M, I>, I extends ImmutableDataManipulator<I, M>> void generateClasses(
            ClassWriter cwM, ClassWriter cwI,
            String mutableClassName, String immutableClassName,
            Class<M> manipulatorType, Class<I> immutableManipulatorType,
            @Nullable Class<? extends M> mutableExpansion,
            @Nullable Class<? extends I> immutableExpansion);
}
