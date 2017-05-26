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
package org.lanternpowered.server.data;

import org.lanternpowered.server.data.manipulator.DataManipulatorRegistry;
import org.lanternpowered.server.data.persistence.DataTranslators;
import org.lanternpowered.server.data.persistence.DataTypeSerializers;
import org.lanternpowered.server.data.property.block.GroundLuminancePropertyStore;
import org.lanternpowered.server.data.property.block.SkyLuminancePropertyStore;
import org.lanternpowered.server.data.value.LanternValueFactory;
import org.lanternpowered.server.effect.potion.LanternPotionEffectBuilder;
import org.lanternpowered.server.game.LanternGame;
import org.lanternpowered.server.item.enchantment.ItemEnchantmentDataBuilder;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.meta.ItemEnchantment;
import org.spongepowered.api.data.property.PropertyRegistry;
import org.spongepowered.api.data.property.block.GroundLuminanceProperty;
import org.spongepowered.api.data.property.block.SkyLuminanceProperty;
import org.spongepowered.api.data.type.WireAttachmentType;
import org.spongepowered.api.data.type.WireAttachmentTypes;
import org.spongepowered.api.data.value.mutable.CompositeValueStore;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.text.BookView;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.BookViewDataBuilder;
import org.spongepowered.api.text.serializer.TextConfigSerializer;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.util.RespawnLocation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DataRegistrar {

    public static void setupRegistrations(LanternGame game) {
        final PropertyRegistry propertyRegistry = game.getPropertyRegistry();
        propertyRegistry.register(SkyLuminanceProperty.class, new SkyLuminancePropertyStore());
        propertyRegistry.register(GroundLuminanceProperty.class, new GroundLuminancePropertyStore());

        final LanternDataManager dataManager = game.getDataManager();
        // Register the data type serializers
        DataTypeSerializers.registerSerializers(dataManager);
        // Register the data serializers
        DataTranslators.registerSerializers(dataManager);

        // Register the data builders
        dataManager.registerBuilder(Text.class, new TextConfigSerializer());
        dataManager.registerBuilder(BookView.class, new BookViewDataBuilder());
        dataManager.registerBuilder(PotionEffect.class, new LanternPotionEffectBuilder());
        dataManager.registerBuilder(RespawnLocation.class, new RespawnLocation.Builder());
        dataManager.registerBuilder(ItemEnchantment.class, new ItemEnchantmentDataBuilder());

        final LanternValueFactory valueFactory = LanternValueFactory.getInstance();
        valueFactory.registerKey(Keys.CONNECTED_DIRECTIONS).applyValueProcessor(builder -> builder
                .applicableTester((key, valueContainer) ->
                        valueContainer.supports(Keys.CONNECTED_WEST) || valueContainer.supports(Keys.CONNECTED_EAST) ||
                        valueContainer.supports(Keys.CONNECTED_NORTH) || valueContainer.supports(Keys.CONNECTED_SOUTH))
                .retrieveHandler(((key, valueContainer) -> {
                    final Set<Direction> directions = new HashSet<>();
                    if (valueContainer.get(Keys.CONNECTED_WEST).orElse(false)) {
                        directions.add(Direction.WEST);
                    }
                    if (valueContainer.get(Keys.CONNECTED_EAST).orElse(false)) {
                        directions.add(Direction.EAST);
                    }
                    if (valueContainer.get(Keys.CONNECTED_SOUTH).orElse(false)) {
                        directions.add(Direction.SOUTH);
                    }
                    if (valueContainer.get(Keys.CONNECTED_NORTH).orElse(false)) {
                        directions.add(Direction.NORTH);
                    }
                    return Optional.of(directions);
                }))
                .offerHandler((key, valueContainer, directions) -> {
                    if (valueContainer instanceof CompositeValueStore) {
                        final CompositeValueStore store = (CompositeValueStore) valueContainer;
                        final DataTransactionResult.Builder resultBuilder = DataTransactionResult.builder();
                        resultBuilder.absorbResult(store.offer(Keys.CONNECTED_WEST, directions.contains(Direction.WEST)));
                        resultBuilder.absorbResult(store.offer(Keys.CONNECTED_EAST, directions.contains(Direction.EAST)));
                        resultBuilder.absorbResult(store.offer(Keys.CONNECTED_SOUTH, directions.contains(Direction.SOUTH)));
                        resultBuilder.absorbResult(store.offer(Keys.CONNECTED_NORTH, directions.contains(Direction.NORTH)));
                        return resultBuilder.result(DataTransactionResult.Type.SUCCESS).build();
                    }
                    return DataTransactionResult.successNoData();
                })
                .failAlwaysRemoveHandler());
        valueFactory.registerKey(Keys.WIRE_ATTACHMENTS).applyValueProcessor(builder -> builder
                .applicableTester((key, valueContainer) ->
                        valueContainer.supports(Keys.WIRE_ATTACHMENT_WEST) || valueContainer.supports(Keys.WIRE_ATTACHMENT_EAST) ||
                                valueContainer.supports(Keys.WIRE_ATTACHMENT_NORTH) || valueContainer.supports(Keys.WIRE_ATTACHMENT_SOUTH))
                .retrieveHandler(((key, valueContainer) -> {
                    final Map<Direction, WireAttachmentType> attachments = new HashMap<>();
                    valueContainer.get(Keys.WIRE_ATTACHMENT_WEST).ifPresent(type -> attachments.put(Direction.WEST, type));
                    valueContainer.get(Keys.WIRE_ATTACHMENT_EAST).ifPresent(type -> attachments.put(Direction.EAST, type));
                    valueContainer.get(Keys.WIRE_ATTACHMENT_SOUTH).ifPresent(type -> attachments.put(Direction.SOUTH, type));
                    valueContainer.get(Keys.WIRE_ATTACHMENT_NORTH).ifPresent(type -> attachments.put(Direction.NORTH, type));
                    return Optional.of(attachments);
                }))
                .offerHandler((key, valueContainer, attachments) -> {
                    if (valueContainer instanceof CompositeValueStore) {
                        final CompositeValueStore store = (CompositeValueStore) valueContainer;
                        final DataTransactionResult.Builder resultBuilder = DataTransactionResult.builder();
                        WireAttachmentType type = attachments.get(Direction.WEST);
                        resultBuilder.absorbResult(store.offer(Keys.CONNECTED_WEST,
                                type == null ? WireAttachmentTypes.NONE : type));
                        type = attachments.get(Direction.EAST);
                        resultBuilder.absorbResult(store.offer(Keys.CONNECTED_EAST,
                                type == null ? WireAttachmentTypes.NONE : type));
                        type = attachments.get(Direction.SOUTH);
                        resultBuilder.absorbResult(store.offer(Keys.CONNECTED_SOUTH,
                                type == null ? WireAttachmentTypes.NONE : type));
                        type = attachments.get(Direction.NORTH);
                        resultBuilder.absorbResult(store.offer(Keys.CONNECTED_NORTH,
                                type == null ? WireAttachmentTypes.NONE : type));
                        return resultBuilder.result(DataTransactionResult.Type.SUCCESS).build();
                    }
                    return DataTransactionResult.successNoData();
                })
                .failAlwaysRemoveHandler());

        DataManipulatorRegistry.get();
    }

    public static void finalizeRegistrations(LanternGame game) {
        game.getPropertyRegistry().completeRegistration();
    }
}
