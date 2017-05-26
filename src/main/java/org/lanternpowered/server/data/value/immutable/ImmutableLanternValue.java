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
package org.lanternpowered.server.data.value.immutable;

import static com.google.common.base.Preconditions.checkNotNull;

import org.lanternpowered.server.data.ImmutableDataCachingUtil;
import org.lanternpowered.server.data.value.AbstractBaseValue;
import org.lanternpowered.server.data.value.mutable.LanternValue;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.function.Function;

import javax.annotation.Nullable;

public class ImmutableLanternValue<E> extends AbstractBaseValue<E> implements ImmutableValue<E> {

    /**
     * Gets a cached {@link ImmutableValue} of the default value and the actual value.
     *
     * @param key The key for the value
     * @param defaultValue The default value
     * @param actualValue The actual value
     * @param <T> The type of value
     * @return The cached immutable value
     */
    public static <T> ImmutableValue<T> cachedOf(Key<? extends BaseValue<T>> key, T defaultValue, @Nullable T actualValue) {
        return ImmutableDataCachingUtil.getValue(ImmutableLanternValue.class, key, defaultValue, actualValue);
    }

    public ImmutableLanternValue(Key<? extends BaseValue<E>> key, E defaultValue) {
        super(key, defaultValue, defaultValue);
    }

    public ImmutableLanternValue(Key<? extends BaseValue<E>> key, E defaultValue, @Nullable E actualValue) {
        super(key, defaultValue, actualValue);
    }

    @Override
    public ImmutableValue<E> with(E value) {
        return new ImmutableLanternValue<>(getKey(),getDefault(), value);
    }

    @Override
    public ImmutableValue<E> transform(Function<E, E> function) {
        final E value = checkNotNull(function).apply(get());
        return new ImmutableLanternValue<>(getKey(), getDefault(), value);
    }

    @Override
    public Value<E> asMutable() {
        return new LanternValue<>(getKey(), getDefault(), get());
    }
}
