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
package org.lanternpowered.server.data.value;

import java.util.function.BiConsumer;

import javax.annotation.Nullable;

public interface ElementHolder<E> {

    /**
     * Sets the new element and retrieves the old one if present.
     *
     * @param element The element
     * @return The old element if present, otherwise {@code null}
     */
    @Nullable E set(@Nullable E element);

    /**
     * Gets the current value if present.
     *
     * @return The current element if present, otherwise {@code null}
     */
    @Nullable E get();

    /**
     * Adds a listener that tracks the changes of the internal value.
     *
     * @param listener The listener
     */
    void addListener(Listener<E> listener);

    @FunctionalInterface
    interface Listener<E> extends BiConsumer<E, E> {

        @Override
        void accept(E oldElement, E newElement);
    }
}
