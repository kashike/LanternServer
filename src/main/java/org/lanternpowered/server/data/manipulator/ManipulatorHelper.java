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
package org.lanternpowered.server.data.manipulator;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.reflect.TypeToken;
import org.lanternpowered.server.data.LanternDataManager;
import org.lanternpowered.server.data.persistence.DataTypeSerializer;
import org.lanternpowered.server.data.persistence.DataTypeSerializerContext;
import org.lanternpowered.server.data.value.AbstractValueContainer;
import org.lanternpowered.server.data.value.ElementHolder;
import org.lanternpowered.server.data.value.KeyRegistration;
import org.lanternpowered.server.game.Lantern;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public final class ManipulatorHelper {

    @SuppressWarnings("unchecked")
    public static DataContainer toContainer(AbstractValueContainer valueContainer) {
        final DataContainer dataContainer = DataContainer.createNew();
        final Map<Key<?>, KeyRegistration> map = valueContainer.getRawValueMap();
        final LanternDataManager dataManager = Lantern.getGame().getDataManager();
        for (Map.Entry<Key<?>, KeyRegistration> entry : map.entrySet()) {
            if (!(entry.getValue() instanceof ElementHolder)) {
                continue;
            }
            final Key<?> key = entry.getKey();
            final DataQuery dataQuery = key.getQuery();
            final TypeToken<?> typeToken = key.getElementToken();
            final DataTypeSerializer typeSerializer = dataManager.getTypeSerializer(typeToken)
                    .orElseThrow(() -> new IllegalStateException("Wasn't able to find a type serializer for the element type: " + typeToken.toString()));
            final DataTypeSerializerContext context = dataManager.getTypeSerializerContext();
            // The value's shouldn't be null inside a data manipulator,
            // since it doesn't support removal of values
            dataContainer.set(dataQuery, typeSerializer.serialize(typeToken, context,
                    checkNotNull(((ElementHolder) entry.getValue()).get(), "element")));
        }
        return dataContainer;
    }

    @SuppressWarnings("unchecked")
    public static <T extends AbstractValueContainer> Optional<T> buildContent(DataView container, Supplier<T> manipulatorSupplier)
            throws InvalidDataException {
        final T manipulator = manipulatorSupplier.get();
        final Map<Key<?>, KeyRegistration> map = manipulator.getRawValueMap();
        final LanternDataManager dataManager = Lantern.getGame().getDataManager();
        for (Map.Entry<Key<?>, KeyRegistration> entry : map.entrySet()) {
            if (!(entry.getValue() instanceof ElementHolder)) {
                continue;
            }
            final Key<?> key = entry.getKey();
            final DataQuery dataQuery = key.getQuery();
            final TypeToken<?> typeToken = key.getElementToken();
            final Object data = container.get(dataQuery).orElseThrow(
                    () -> new InvalidDataException("Key query (" + dataQuery.toString() + ") is missing."));
            final DataTypeSerializer typeSerializer = dataManager.getTypeSerializer(typeToken)
                    .orElseThrow(() -> new IllegalStateException("Wasn't able to find a type serializer for the element type: " + typeToken.toString()));
            final DataTypeSerializerContext context = dataManager.getTypeSerializerContext();
            ((ElementHolder) map.get(key)).set(typeSerializer.deserialize(typeToken, context, data));
        }
        return Optional.of(manipulator);
    }
}
