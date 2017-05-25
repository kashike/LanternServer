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

import com.flowpowered.math.vector.Vector3d;
import org.lanternpowered.server.data.manipulator.gen.DataManipulatorGenerator;
import org.lanternpowered.server.data.manipulator.immutable.LanternImmutableColoredData;
import org.lanternpowered.server.data.manipulator.immutable.LanternImmutableCommandData;
import org.lanternpowered.server.data.manipulator.immutable.LanternImmutableDisplayNameData;
import org.lanternpowered.server.data.manipulator.immutable.LanternImmutableDyeableData;
import org.lanternpowered.server.data.manipulator.immutable.LanternImmutableFireworkRocketData;
import org.lanternpowered.server.data.manipulator.immutable.LanternImmutableRepresentedItemData;
import org.lanternpowered.server.data.manipulator.immutable.LanternImmutableRepresentedPlayerData;
import org.lanternpowered.server.data.manipulator.immutable.LanternImmutableRotationalData;
import org.lanternpowered.server.data.manipulator.immutable.LanternImmutableSkullData;
import org.lanternpowered.server.data.manipulator.immutable.LanternImmutableTargetedLocationData;
import org.lanternpowered.server.data.manipulator.immutable.LanternImmutableWetData;
import org.lanternpowered.server.data.manipulator.immutable.block.LanternImmutableAttachedData;
import org.lanternpowered.server.data.manipulator.immutable.item.LanternImmutableAuthorData;
import org.lanternpowered.server.data.manipulator.immutable.item.LanternImmutableBlockItemData;
import org.lanternpowered.server.data.manipulator.immutable.item.LanternImmutableBreakableData;
import org.lanternpowered.server.data.manipulator.immutable.item.LanternImmutableCoalData;
import org.lanternpowered.server.data.manipulator.immutable.item.LanternImmutableCookedFishData;
import org.lanternpowered.server.data.manipulator.immutable.item.LanternImmutableDurabilityData;
import org.lanternpowered.server.data.manipulator.immutable.item.LanternImmutableFishData;
import org.lanternpowered.server.data.manipulator.immutable.item.LanternImmutableGenerationData;
import org.lanternpowered.server.data.manipulator.immutable.item.LanternImmutableGoldenAppleData;
import org.lanternpowered.server.data.manipulator.immutable.item.LanternImmutableHideData;
import org.lanternpowered.server.data.manipulator.immutable.item.LanternImmutableInventoryItemData;
import org.lanternpowered.server.data.manipulator.immutable.item.LanternImmutableMapItemData;
import org.lanternpowered.server.data.manipulator.immutable.item.LanternImmutablePlaceableData;
import org.lanternpowered.server.data.manipulator.immutable.item.LanternImmutableSpawnableData;
import org.lanternpowered.server.data.manipulator.mutable.LanternColoredData;
import org.lanternpowered.server.data.manipulator.mutable.LanternCommandData;
import org.lanternpowered.server.data.manipulator.mutable.LanternDisplayNameData;
import org.lanternpowered.server.data.manipulator.mutable.LanternDyeableData;
import org.lanternpowered.server.data.manipulator.mutable.LanternFireworkRocketData;
import org.lanternpowered.server.data.manipulator.mutable.LanternRepresentedItemData;
import org.lanternpowered.server.data.manipulator.mutable.LanternRepresentedPlayerData;
import org.lanternpowered.server.data.manipulator.mutable.LanternRotationalData;
import org.lanternpowered.server.data.manipulator.mutable.LanternSkullData;
import org.lanternpowered.server.data.manipulator.mutable.LanternTargetedLocationData;
import org.lanternpowered.server.data.manipulator.mutable.LanternWetData;
import org.lanternpowered.server.data.manipulator.mutable.block.LanternAttachedData;
import org.lanternpowered.server.data.manipulator.mutable.item.LanternAuthorData;
import org.lanternpowered.server.data.manipulator.mutable.item.LanternBlockItemData;
import org.lanternpowered.server.data.manipulator.mutable.item.LanternBreakableData;
import org.lanternpowered.server.data.manipulator.mutable.item.LanternCoalData;
import org.lanternpowered.server.data.manipulator.mutable.item.LanternCookedFishData;
import org.lanternpowered.server.data.manipulator.mutable.item.LanternDurabilityData;
import org.lanternpowered.server.data.manipulator.mutable.item.LanternFishData;
import org.lanternpowered.server.data.manipulator.mutable.item.LanternGenerationData;
import org.lanternpowered.server.data.manipulator.mutable.item.LanternGoldenAppleData;
import org.lanternpowered.server.data.manipulator.mutable.item.LanternHideData;
import org.lanternpowered.server.data.manipulator.mutable.item.LanternInventoryItemData;
import org.lanternpowered.server.data.manipulator.mutable.item.LanternMapItemData;
import org.lanternpowered.server.data.manipulator.mutable.item.LanternPlaceableData;
import org.lanternpowered.server.data.manipulator.mutable.item.LanternSpawnableData;
import org.lanternpowered.server.data.value.IValueContainer;
import org.lanternpowered.server.game.Lantern;
import org.lanternpowered.server.plugin.InternalPluginsInfo;
import org.lanternpowered.server.profile.LanternGameProfile;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.DataManager;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.manipulator.immutable.ImmutableColoredData;
import org.spongepowered.api.data.manipulator.immutable.ImmutableCommandData;
import org.spongepowered.api.data.manipulator.immutable.ImmutableDisplayNameData;
import org.spongepowered.api.data.manipulator.immutable.ImmutableDyeableData;
import org.spongepowered.api.data.manipulator.immutable.ImmutableFireworkEffectData;
import org.spongepowered.api.data.manipulator.immutable.ImmutableFireworkRocketData;
import org.spongepowered.api.data.manipulator.immutable.ImmutableListData;
import org.spongepowered.api.data.manipulator.immutable.ImmutablePotionEffectData;
import org.spongepowered.api.data.manipulator.immutable.ImmutableRepresentedItemData;
import org.spongepowered.api.data.manipulator.immutable.ImmutableRepresentedPlayerData;
import org.spongepowered.api.data.manipulator.immutable.ImmutableRotationalData;
import org.spongepowered.api.data.manipulator.immutable.ImmutableSkullData;
import org.spongepowered.api.data.manipulator.immutable.ImmutableTargetedLocationData;
import org.spongepowered.api.data.manipulator.immutable.ImmutableWetData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableAttachedData;
import org.spongepowered.api.data.manipulator.immutable.item.ImmutableAuthorData;
import org.spongepowered.api.data.manipulator.immutable.item.ImmutableBlockItemData;
import org.spongepowered.api.data.manipulator.immutable.item.ImmutableBreakableData;
import org.spongepowered.api.data.manipulator.immutable.item.ImmutableCoalData;
import org.spongepowered.api.data.manipulator.immutable.item.ImmutableCookedFishData;
import org.spongepowered.api.data.manipulator.immutable.item.ImmutableDurabilityData;
import org.spongepowered.api.data.manipulator.immutable.item.ImmutableEnchantmentData;
import org.spongepowered.api.data.manipulator.immutable.item.ImmutableFishData;
import org.spongepowered.api.data.manipulator.immutable.item.ImmutableGenerationData;
import org.spongepowered.api.data.manipulator.immutable.item.ImmutableGoldenAppleData;
import org.spongepowered.api.data.manipulator.immutable.item.ImmutableHideData;
import org.spongepowered.api.data.manipulator.immutable.item.ImmutableInventoryItemData;
import org.spongepowered.api.data.manipulator.immutable.item.ImmutableLoreData;
import org.spongepowered.api.data.manipulator.immutable.item.ImmutableMapItemData;
import org.spongepowered.api.data.manipulator.immutable.item.ImmutablePagedData;
import org.spongepowered.api.data.manipulator.immutable.item.ImmutablePlaceableData;
import org.spongepowered.api.data.manipulator.immutable.item.ImmutableSpawnableData;
import org.spongepowered.api.data.manipulator.immutable.item.ImmutableStoredEnchantmentData;
import org.spongepowered.api.data.manipulator.mutable.ColoredData;
import org.spongepowered.api.data.manipulator.mutable.CommandData;
import org.spongepowered.api.data.manipulator.mutable.DisplayNameData;
import org.spongepowered.api.data.manipulator.mutable.DyeableData;
import org.spongepowered.api.data.manipulator.mutable.FireworkEffectData;
import org.spongepowered.api.data.manipulator.mutable.FireworkRocketData;
import org.spongepowered.api.data.manipulator.mutable.ListData;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.data.manipulator.mutable.RepresentedItemData;
import org.spongepowered.api.data.manipulator.mutable.RepresentedPlayerData;
import org.spongepowered.api.data.manipulator.mutable.RotationalData;
import org.spongepowered.api.data.manipulator.mutable.SkullData;
import org.spongepowered.api.data.manipulator.mutable.TargetedLocationData;
import org.spongepowered.api.data.manipulator.mutable.WetData;
import org.spongepowered.api.data.manipulator.mutable.block.AttachedData;
import org.spongepowered.api.data.manipulator.mutable.item.AuthorData;
import org.spongepowered.api.data.manipulator.mutable.item.BlockItemData;
import org.spongepowered.api.data.manipulator.mutable.item.BreakableData;
import org.spongepowered.api.data.manipulator.mutable.item.CoalData;
import org.spongepowered.api.data.manipulator.mutable.item.CookedFishData;
import org.spongepowered.api.data.manipulator.mutable.item.DurabilityData;
import org.spongepowered.api.data.manipulator.mutable.item.EnchantmentData;
import org.spongepowered.api.data.manipulator.mutable.item.FishData;
import org.spongepowered.api.data.manipulator.mutable.item.GenerationData;
import org.spongepowered.api.data.manipulator.mutable.item.GoldenAppleData;
import org.spongepowered.api.data.manipulator.mutable.item.HideData;
import org.spongepowered.api.data.manipulator.mutable.item.InventoryItemData;
import org.spongepowered.api.data.manipulator.mutable.item.LoreData;
import org.spongepowered.api.data.manipulator.mutable.item.MapItemData;
import org.spongepowered.api.data.manipulator.mutable.item.PagedData;
import org.spongepowered.api.data.manipulator.mutable.item.PlaceableData;
import org.spongepowered.api.data.manipulator.mutable.item.SpawnableData;
import org.spongepowered.api.data.manipulator.mutable.item.StoredEnchantmentData;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nullable;

public class DataManipulatorRegistry {

    private static final DataManipulatorRegistry INSTANCE = new DataManipulatorRegistry();

    public static DataManipulatorRegistry get() {
        return INSTANCE;
    }

    private final Map<Class, DataManipulatorRegistration> registrationByClass = new HashMap<>();
    private final DataManipulatorGenerator dataManipulatorGenerator = new DataManipulatorGenerator();

    {
        // general package
        /// normal containers
        register(ColoredData.class, ImmutableColoredData.class, LanternColoredData.class, LanternImmutableColoredData.class,
                c -> c.registerKey(Keys.COLOR, Color.WHITE));
        register(CommandData.class, ImmutableCommandData.class, LanternCommandData.class, LanternImmutableCommandData.class,
                c -> {
                    c.registerKey(Keys.COMMAND, "");
                    c.registerKey(Keys.SUCCESS_COUNT, 0);
                    c.registerKey(Keys.TRACKS_OUTPUT, true);
                    c.registerKey(Keys.LAST_COMMAND_OUTPUT, Optional.empty());
                });
        register(DisplayNameData.class, ImmutableDisplayNameData.class, LanternDisplayNameData.class, LanternImmutableDisplayNameData.class,
                c -> c.registerKey(Keys.DISPLAY_NAME, Text.EMPTY));
        register(FireworkRocketData.class, ImmutableFireworkRocketData.class, LanternFireworkRocketData.class, LanternImmutableFireworkRocketData.class,
                c -> c.registerKey(Keys.FIREWORK_FLIGHT_MODIFIER, 0));
        register(RepresentedItemData.class, ImmutableRepresentedItemData.class, LanternRepresentedItemData.class, LanternImmutableRepresentedItemData.class,
                c -> c.registerKey(Keys.REPRESENTED_ITEM, ItemStackSnapshot.NONE));
        register(RepresentedPlayerData.class, ImmutableRepresentedPlayerData.class, LanternRepresentedPlayerData.class, LanternImmutableRepresentedPlayerData.class,
                c -> c.registerKey(Keys.REPRESENTED_PLAYER, LanternGameProfile.UNKNOWN));
        register(TargetedLocationData.class, ImmutableTargetedLocationData.class, LanternTargetedLocationData.class, LanternImmutableTargetedLocationData.class,
                c -> c.registerKey(Keys.TARGETED_LOCATION, Vector3d.ZERO));
        register(WetData.class, ImmutableWetData.class, LanternWetData.class, LanternImmutableWetData.class,
                c -> c.registerKey(Keys.IS_WET, false));

        /// variant containers: TODO
        register(DyeableData.class, LanternDyeableData::new, LanternDyeableData::new, LanternDyeableData::new,
                ImmutableDyeableData.class, LanternImmutableDyeableData::new, LanternImmutableDyeableData::new);
        register(RotationalData.class, LanternRotationalData::new, LanternRotationalData::new, LanternRotationalData::new,
                ImmutableRotationalData.class, LanternImmutableRotationalData::new, LanternImmutableRotationalData::new);
        register(SkullData.class, LanternSkullData::new, LanternSkullData::new, LanternSkullData::new,
                ImmutableSkullData.class, LanternImmutableSkullData::new, LanternImmutableSkullData::new);

        /// list containers
        registerList(FireworkEffectData.class, ImmutableFireworkEffectData.class, Keys.FIREWORK_EFFECTS);
        registerList(PotionEffectData.class, ImmutablePotionEffectData.class, Keys.POTION_EFFECTS);

        // block package
        register(AttachedData.class, ImmutableAttachedData.class, LanternAttachedData.class, LanternImmutableAttachedData.class,
                valueContainer -> valueContainer.registerKey(Keys.ATTACHED, false));

        // item package
        /// normal containers
        register(AuthorData.class, ImmutableAuthorData.class, LanternAuthorData.class, LanternImmutableAuthorData.class,
                c -> c.registerKey(Keys.BOOK_AUTHOR, Text.EMPTY));
        register(BlockItemData.class, ImmutableBlockItemData.class, LanternBlockItemData.class, LanternImmutableBlockItemData.class,
                c -> c.registerKey(Keys.ITEM_BLOCKSTATE, BlockTypes.AIR.getDefaultState()));
        register(BreakableData.class, ImmutableBreakableData.class, LanternBreakableData.class, LanternImmutableBreakableData.class,
                c -> c.registerKey(Keys.BREAKABLE_BLOCK_TYPES, new HashSet<>()));
        register(DurabilityData.class, ImmutableDurabilityData.class, LanternDurabilityData.class, LanternImmutableDurabilityData.class,
                c -> {
                    c.registerKey(Keys.ITEM_DURABILITY, 100);
                    c.registerKey(Keys.UNBREAKABLE, false);
                });
        register(GenerationData.class, ImmutableGenerationData.class, LanternGenerationData.class, LanternImmutableGenerationData.class,
                c -> c.registerKey(Keys.GENERATION, 0, 0, Integer.MAX_VALUE));
        register(HideData.class, ImmutableHideData.class, LanternHideData.class, LanternImmutableHideData.class,
                c -> {
                    c.registerKey(Keys.HIDE_ENCHANTMENTS, false);
                    c.registerKey(Keys.HIDE_ATTRIBUTES, false);
                    c.registerKey(Keys.HIDE_UNBREAKABLE, false);
                    c.registerKey(Keys.HIDE_CAN_DESTROY, false);
                    c.registerKey(Keys.HIDE_CAN_PLACE, false);
                    c.registerKey(Keys.HIDE_MISCELLANEOUS, false);
                });
        register(MapItemData.class, ImmutableMapItemData.class, LanternMapItemData.class, LanternImmutableMapItemData.class,
                c -> {});
        register(PlaceableData.class, ImmutablePlaceableData.class, LanternPlaceableData.class, LanternImmutablePlaceableData.class,
                c -> c.registerKey(Keys.PLACEABLE_BLOCKS, new HashSet<>()));

        /// variant containers: TODO
        register(CoalData.class, LanternCoalData::new, LanternCoalData::new, LanternCoalData::new,
                ImmutableCoalData.class, LanternImmutableCoalData::new, LanternImmutableCoalData::new);
        register(CookedFishData.class, LanternCookedFishData::new, LanternCookedFishData::new, LanternCookedFishData::new,
                ImmutableCookedFishData.class, LanternImmutableCookedFishData::new, LanternImmutableCookedFishData::new);
        register(FishData.class, LanternFishData::new, LanternFishData::new, LanternFishData::new,
                ImmutableFishData.class, LanternImmutableFishData::new, LanternImmutableFishData::new);
        register(GoldenAppleData.class, LanternGoldenAppleData::new, LanternGoldenAppleData::new, LanternGoldenAppleData::new,
                ImmutableGoldenAppleData.class, LanternImmutableGoldenAppleData::new, LanternImmutableGoldenAppleData::new);
        register(SpawnableData.class, LanternSpawnableData::new, LanternSpawnableData::new, LanternSpawnableData::new,
                ImmutableSpawnableData.class, LanternImmutableSpawnableData::new, LanternImmutableSpawnableData::new);

        /// list containers
        registerList(EnchantmentData.class, ImmutableEnchantmentData.class, Keys.ITEM_ENCHANTMENTS);
        registerList(LoreData.class, ImmutableLoreData.class, Keys.ITEM_LORE);
        registerList(PagedData.class, ImmutablePagedData.class, Keys.BOOK_PAGES);
        registerList(StoredEnchantmentData.class, ImmutableStoredEnchantmentData.class, Keys.STORED_ENCHANTMENTS);

        /// containers with special behavior
        register(InventoryItemData.class, LanternInventoryItemData::new, LanternInventoryItemData::new, LanternInventoryItemData::new,
                ImmutableInventoryItemData.class, LanternImmutableInventoryItemData::new, LanternImmutableInventoryItemData::new);
    }

    private static final class RegistrationInfo {

        public static RegistrationInfo build(Class<?> manipulatorType) {
            final char[] name = manipulatorType.getCanonicalName().toCharArray();
            final StringBuilder builder = new StringBuilder();

            for (int i = 0; i < name.length; i++) {
                if (Character.isUpperCase(name[i]) || name[i] == '.') {
                    if (i != 0) {
                        builder.append('_');
                    }
                    builder.append(Character.toLowerCase(name[i]));
                } else {
                    builder.append(name[i]);
                }
            }

            final String fullName = manipulatorType.getName();
            final String plugin;
            if (fullName.startsWith("org.spongepowered.")) {
                plugin = InternalPluginsInfo.SpongePlatform.IDENTIFIER;
            } else if (fullName.startsWith("org.lanternpowered.")) {
                plugin = InternalPluginsInfo.Implementation.IDENTIFIER;
            } else {
                final String[] parts = fullName.split(".");
                if (parts.length > 1) {
                    plugin = parts[1];
                } else if (parts.length == 1) {
                    plugin = parts[0];
                } else {
                    plugin = "unknown";
                }
            }

            final PluginContainer pluginContainer = Lantern.getGame().getPluginManager().getPlugin(plugin)
                    .orElseThrow(() -> new IllegalStateException("The plugin " + plugin + " does not exist!"));
            return new RegistrationInfo(pluginContainer, builder.toString(), manipulatorType.getCanonicalName());
        }

        private final PluginContainer pluginContainer;
        private final String id;
        private final String name;

        private RegistrationInfo(PluginContainer pluginContainer, String id, String name) {
            this.pluginContainer = pluginContainer;
            this.name = name;
            this.id = id;
        }
    }

    @SuppressWarnings("unchecked")
    public <M extends DataManipulator<M, I>, I extends ImmutableDataManipulator<I, M>> DataManipulatorRegistration<M, I> register(
            Class<M> manipulatorType, Supplier<M> manipulatorSupplier, Function<M, M> manipulatorCopyFunction, Function<I, M> immutableToMutableFunction,
            Class<I> immutableManipulatorType, Supplier<I> immutableManipulatorSupplier, Function<M, I> mutableToImmutableFunction) {
        final RegistrationInfo registrationInfo = RegistrationInfo.build(manipulatorType);
        return register(registrationInfo.pluginContainer, registrationInfo.id, registrationInfo.name, manipulatorType, manipulatorSupplier,
                manipulatorCopyFunction, immutableToMutableFunction, immutableManipulatorType, immutableManipulatorSupplier,
                mutableToImmutableFunction);
    }

    @SuppressWarnings("unchecked")
    public <M extends DataManipulator<M, I>, I extends ImmutableDataManipulator<I, M>> DataManipulatorRegistration<M, I> register(
            PluginContainer pluginContainer, String id, String name,
            Class<M> manipulatorType, Supplier<M> manipulatorSupplier, Function<M, M> manipulatorCopyFunction, Function<I, M> immutableToMutableFunction,
            Class<I> immutableManipulatorType, Supplier<I> immutableManipulatorSupplier, Function<M, I> mutableToImmutableFunction) {
        checkNotNull(manipulatorType, "manipulatorType");
        checkNotNull(manipulatorSupplier, "manipulatorSupplier");
        checkNotNull(immutableManipulatorType, "immutableManipulatorType");
        checkNotNull(immutableManipulatorSupplier, "immutableManipulatorSupplier");
        final DataManipulatorRegistration<M, I> registration = new SimpleDataManipulatorRegistration<>(pluginContainer, id, name,
                manipulatorType, manipulatorSupplier, manipulatorCopyFunction, immutableToMutableFunction,
                immutableManipulatorType, immutableManipulatorSupplier, mutableToImmutableFunction, manipulatorSupplier.get().getKeys());
        return register(registration);
    }

    public <M extends ListData<E, M, I>, I extends ImmutableListData<E, I, M>, E> DataManipulatorRegistration<M, I> registerList(
            Class<M> manipulatorType, Class<I> immutableManipulatorType, Key<ListValue<E>> key) {
        return registerList(manipulatorType, immutableManipulatorType, key, ArrayList::new);
    }

    public <M extends ListData<E, M, I>, I extends ImmutableListData<E, I, M>, E> DataManipulatorRegistration<M, I> registerList(
            Class<M> manipulatorType, Class<I> immutableManipulatorType,
            Key<ListValue<E>> key, Supplier<List<E>> listSupplier) {
        checkNotNull(manipulatorType, "manipulatorType");
        checkNotNull(immutableManipulatorType, "immutableManipulatorType");
        final RegistrationInfo registrationInfo = RegistrationInfo.build(manipulatorType);
        return registerList(registrationInfo.pluginContainer, registrationInfo.id, registrationInfo.name,
                manipulatorType, immutableManipulatorType, key, listSupplier);
    }

    public <M extends ListData<E, M, I>, I extends ImmutableListData<E, I, M>, E> DataManipulatorRegistration<M, I> registerList(
            PluginContainer pluginContainer, String id, String name, Class<M> manipulatorType, Class<I> immutableManipulatorType, Key<ListValue<E>> key) {
        return registerList(pluginContainer, id, name, manipulatorType, immutableManipulatorType, key, ArrayList::new);
    }

    public <M extends ListData<E, M, I>, I extends ImmutableListData<E, I, M>, E> DataManipulatorRegistration<M, I> registerList(
            PluginContainer pluginContainer, String id, String name, Class<M> manipulatorType, Class<I> immutableManipulatorType,
            Key<ListValue<E>> key, Supplier<List<E>> listSupplier) {
        checkNotNull(manipulatorType, "manipulatorType");
        checkNotNull(immutableManipulatorType, "immutableManipulatorType");
        final DataManipulatorRegistration<M, I> registration = this.dataManipulatorGenerator.newListRegistrationFor(
                pluginContainer, id, name, manipulatorType, immutableManipulatorType, key, listSupplier);
        return register(registration);
    }

    public <M extends DataManipulator<M, I>, I extends ImmutableDataManipulator<I, M>> DataManipulatorRegistration<M, I> register(
            Class<M> manipulatorType, Class<I> immutableManipulatorType,
            @Nullable Class<? extends M> mutableExpansion, @Nullable Class<? extends I> immutableExpansion,
            @Nullable Consumer<IValueContainer<?>> registrationConsumer) {
        final RegistrationInfo registrationInfo = RegistrationInfo.build(manipulatorType);
        return register(registrationInfo.pluginContainer, registrationInfo.id, registrationInfo.name,
                manipulatorType, immutableManipulatorType, mutableExpansion, immutableExpansion, registrationConsumer);
    }

    public <M extends DataManipulator<M, I>, I extends ImmutableDataManipulator<I, M>> DataManipulatorRegistration<M, I> register(
            PluginContainer pluginContainer, String id, String name, Class<M> manipulatorType, Class<I> immutableManipulatorType) {
        return register(pluginContainer, id, name, manipulatorType, immutableManipulatorType, null, null, null);
    }

    public <M extends DataManipulator<M, I>, I extends ImmutableDataManipulator<I, M>> DataManipulatorRegistration<M, I> register(
            PluginContainer pluginContainer, String id, String name, Class<M> manipulatorType, Class<I> immutableManipulatorType,
            @Nullable Class<? extends M> mutableExpansion, @Nullable Class<? extends I> immutableExpansion,
            @Nullable Consumer<IValueContainer<?>> registrationConsumer) {
        checkNotNull(manipulatorType, "manipulatorType");
        checkNotNull(immutableManipulatorType, "immutableManipulatorType");
        final DataManipulatorRegistration<M, I> registration = this.dataManipulatorGenerator.newRegistrationFor(
                pluginContainer, id, name, manipulatorType, immutableManipulatorType, mutableExpansion, immutableExpansion, registrationConsumer);
        return register(registration);
    }

    @SuppressWarnings("unchecked")
    private <M extends DataManipulator<M, I>, I extends ImmutableDataManipulator<I, M>> DataManipulatorRegistration<M, I> register(
            DataManipulatorRegistration<M, I> registration) {
        checkNotNull(registration, "registration");
        this.registrationByClass.put(registration.getManipulatorClass(), registration);
        this.registrationByClass.put(registration.getImmutableManipulatorClass(), registration);
        this.registrationByClass.put(registration.createMutable().getClass(), registration);
        this.registrationByClass.put(registration.createImmutable().getClass(), registration);
        final DataManager dataManager = Lantern.getGame().getDataManager();
        dataManager.registerBuilder(registration.getImmutableManipulatorClass(), registration.getImmutableDataBuilder());
        dataManager.registerBuilder(registration.getManipulatorClass(), registration.getDataManipulatorBuilder());
        return registration;
    }

    public Collection<DataManipulatorRegistration> getAll() {
        return Collections.unmodifiableCollection(this.registrationByClass.values());
    }

    public <M extends DataManipulator<M, I>, I extends ImmutableDataManipulator<I, M>> Optional<DataManipulatorRegistration<M, I>> getByMutable(
            Class<M> manipulatorType) {
        //noinspection unchecked
        return Optional.ofNullable(this.registrationByClass.get(checkNotNull(manipulatorType, "manipulatorType")));
    }

    public <M extends DataManipulator<M, I>, I extends ImmutableDataManipulator<I, M>> Optional<DataManipulatorRegistration<M, I>> getByImmutable(
            Class<I> immutableManipulatorType) {
        //noinspection unchecked
        return Optional.ofNullable(this.registrationByClass.get(checkNotNull(immutableManipulatorType, "immutableManipulatorType")));
    }
}
