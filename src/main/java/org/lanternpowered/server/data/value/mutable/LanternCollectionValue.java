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
package org.lanternpowered.server.data.value.mutable;

import static com.google.common.base.Preconditions.checkNotNull;

import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableCollectionValue;
import org.spongepowered.api.data.value.mutable.CollectionValue;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
abstract class LanternCollectionValue<E, V extends Collection<E>, I extends CollectionValue<E, V, I, L>,
        L extends ImmutableCollectionValue<E, V, L, I>> extends LanternValue<V> implements CollectionValue<E, V, I, L> {

    LanternCollectionValue(Key<? extends BaseValue<V>> key, V defaultValue) {
        super(key, defaultValue);
    }

    LanternCollectionValue(Key<? extends BaseValue<V>> key, V defaultValue, V actualValue) {
        super(key, defaultValue, actualValue);
    }

    @Override
    public I set(V value) {
        this.actualValue = checkNotNull(value);
        return (I) this;
    }

    @Override
    public I transform(Function<V, V> function) {
        this.actualValue = checkNotNull(checkNotNull(function).apply(this.actualValue));
        return (I) this;
    }

    @Override
    public int size() {
        return getActualValue().size();
    }

    @Override
    public boolean isEmpty() {
        return getActualValue().isEmpty();
    }

    @Override
    public I add(E element) {
        getActualValue().add(checkNotNull(element));
        return (I) this;
    }

    @Override
    public I addAll(Iterable<E> elements) {
        for (E element : checkNotNull(elements)) {
            getActualValue().add(checkNotNull(element));
        }
        return (I) this;
    }

    @Override
    public I remove(E element) {
        getActualValue().remove(checkNotNull(element));
        return (I) this;
    }

    @Override
    public I removeAll(Iterable<E> elements) {
        for (E element : elements) {
            getActualValue().remove(checkNotNull(element));
        }
        return (I) this;
    }

    @Override
    public I removeAll(Predicate<E> predicate) {
        for (Iterator<E> iterator = getActualValue().iterator(); iterator.hasNext(); ) {
            if (checkNotNull(predicate).test(iterator.next())) {
                iterator.remove();
            }
        }
        return (I) this;
    }

    @Override
    public boolean contains(E element) {
        return getActualValue().contains(checkNotNull(element));
    }

    @Override
    public boolean containsAll(Collection<E> iterable) {
        return getActualValue().containsAll(iterable);
    }

    @Override
    public boolean exists() {
        return this.actualValue != null;
    }

    @Override
    public abstract L asImmutable();

    @Override
    public Optional<V> getDirect() {
        return Optional.of(getActualValue());
    }

    @Override
    public Iterator<E> iterator() {
        return getActualValue().iterator();
    }
}
