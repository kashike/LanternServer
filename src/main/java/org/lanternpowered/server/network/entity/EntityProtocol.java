/*
 * This file is part of LanternServer, licensed under the MIT License (MIT).
 *
 * Copyright (c) LanternPowered <https://github.com/LanternPowered>
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) Contributors
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
package org.lanternpowered.server.network.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import org.spongepowered.api.entity.Entity;

public abstract class EntityProtocol<E extends Entity> {

    private int parameterIndex;

    /**
     * Initializes the {@link EntityProtocol}.
     */
    protected void initialize() {
    }

    /**
     * Creates a new {@link ParameterType}.
     *
     * @param valueType The parameter value type
     * @param <T> The value type
     * @return The parameter type
     */
    protected final <T> ParameterType<T> newParameterType(ParameterValueType<T> valueType) {
        return new ParameterType<>(checkNotNull(valueType, "valueType"), this.parameterIndex++);
    }

    /**
     * Fills the {@link ParameterList} with parameters to update the {@link Entity} on
     * the client. If {@code initial} is {@code true} then is being initially spawned
     * on the client.
     *
     * @param entity The entity
     * @param parameterList The parameter list to fill
     * @param initial Whether the entity is being spawned
     */
    public void fill(E entity, ParameterList parameterList, boolean initial) {
    }
}
