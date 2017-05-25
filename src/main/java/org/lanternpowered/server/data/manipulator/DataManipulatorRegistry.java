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
import org.lanternpowered.server.data.manipulator.immutable.LanternImmutableFireworkRocketData;
import org.lanternpowered.server.data.manipulator.immutable.LanternImmutableRepresentedItemData;
import org.lanternpowered.server.data.manipulator.immutable.LanternImmutableRepresentedPlayerData;
import org.lanternpowered.server.data.manipulator.immutable.LanternImmutableTargetedLocationData;
import org.lanternpowered.server.data.manipulator.immutable.LanternImmutableWetData;
import org.lanternpowered.server.data.manipulator.immutable.block.LanternImmutableAttachedData;
import org.lanternpowered.server.data.manipulator.immutable.item.LanternImmutableAuthorData;
import org.lanternpowered.server.data.manipulator.immutable.item.LanternImmutableBlockItemData;
import org.lanternpowered.server.data.manipulator.immutable.item.LanternImmutableBreakableData;
import org.lanternpowered.server.data.manipulator.immutable.item.LanternImmutableDurabilityData;
import org.lanternpowered.server.data.manipulator.immutable.item.LanternImmutableGenerationData;
import org.lanternpowered.server.data.manipulator.immutable.item.LanternImmutableHideData;
import org.lanternpowered.server.data.manipulator.immutable.item.LanternImmutableInventoryItemData;
import org.lanternpowered.server.data.manipulator.immutable.item.LanternImmutableMapItemData;
import org.lanternpowered.server.data.manipulator.immutable.item.LanternImmutablePlaceableData;
import org.lanternpowered.server.data.manipulator.mutable.LanternColoredData;
import org.lanternpowered.server.data.manipulator.mutable.LanternCommandData;
import org.lanternpowered.server.data.manipulator.mutable.LanternDisplayNameData;
import org.lanternpowered.server.data.manipulator.mutable.LanternFireworkRocketData;
import org.lanternpowered.server.data.manipulator.mutable.LanternRepresentedItemData;
import org.lanternpowered.server.data.manipulator.mutable.LanternRepresentedPlayerData;
import org.lanternpowered.server.data.manipulator.mutable.LanternTargetedLocationData;
import org.lanternpowered.server.data.manipulator.mutable.LanternWetData;
import org.lanternpowered.server.data.manipulator.mutable.block.LanternAttachedData;
import org.lanternpowered.server.data.manipulator.mutable.item.LanternAuthorData;
import org.lanternpowered.server.data.manipulator.mutable.item.LanternBlockItemData;
import org.lanternpowered.server.data.manipulator.mutable.item.LanternBreakableData;
import org.lanternpowered.server.data.manipulator.mutable.item.LanternDurabilityData;
import org.lanternpowered.server.data.manipulator.mutable.item.LanternGenerationData;
import org.lanternpowered.server.data.manipulator.mutable.item.LanternHideData;
import org.lanternpowered.server.data.manipulator.mutable.item.LanternInventoryItemData;
import org.lanternpowered.server.data.manipulator.mutable.item.LanternMapItemData;
import org.lanternpowered.server.data.manipulator.mutable.item.LanternPlaceableData;
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
import org.spongepowered.api.data.manipulator.immutable.ImmutableVariantData;
import org.spongepowered.api.data.manipulator.immutable.ImmutableWetData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableAttachedData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableAxisData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableBigMushroomData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableBrickData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableComparatorData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableDirtData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableDisguisedBlockData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableDoublePlantData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableHingeData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableLogAxisData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutablePistonData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutablePlantData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutablePortionData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutablePrismarineData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableQuartzData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableRailDirectionData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableSandData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableSandstoneData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableShrubData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableSlabData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableStairShapeData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableStoneData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableTreeData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableWallData;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableArtData;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableCareerData;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableDominantHandData;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableGameModeData;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableOcelotData;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableParrotData;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableRabbitData;
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
import org.spongepowered.api.data.manipulator.immutable.tileentity.ImmutableSignData;
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
import org.spongepowered.api.data.manipulator.mutable.VariantData;
import org.spongepowered.api.data.manipulator.mutable.WetData;
import org.spongepowered.api.data.manipulator.mutable.block.AttachedData;
import org.spongepowered.api.data.manipulator.mutable.block.AxisData;
import org.spongepowered.api.data.manipulator.mutable.block.BigMushroomData;
import org.spongepowered.api.data.manipulator.mutable.block.BrickData;
import org.spongepowered.api.data.manipulator.mutable.block.ComparatorData;
import org.spongepowered.api.data.manipulator.mutable.block.DirtData;
import org.spongepowered.api.data.manipulator.mutable.block.DisguisedBlockData;
import org.spongepowered.api.data.manipulator.mutable.block.DoublePlantData;
import org.spongepowered.api.data.manipulator.mutable.block.HingeData;
import org.spongepowered.api.data.manipulator.mutable.block.LogAxisData;
import org.spongepowered.api.data.manipulator.mutable.block.PistonData;
import org.spongepowered.api.data.manipulator.mutable.block.PlantData;
import org.spongepowered.api.data.manipulator.mutable.block.PortionData;
import org.spongepowered.api.data.manipulator.mutable.block.PrismarineData;
import org.spongepowered.api.data.manipulator.mutable.block.QuartzData;
import org.spongepowered.api.data.manipulator.mutable.block.RailDirectionData;
import org.spongepowered.api.data.manipulator.mutable.block.SandData;
import org.spongepowered.api.data.manipulator.mutable.block.SandstoneData;
import org.spongepowered.api.data.manipulator.mutable.block.ShrubData;
import org.spongepowered.api.data.manipulator.mutable.block.SlabData;
import org.spongepowered.api.data.manipulator.mutable.block.StairShapeData;
import org.spongepowered.api.data.manipulator.mutable.block.StoneData;
import org.spongepowered.api.data.manipulator.mutable.block.TreeData;
import org.spongepowered.api.data.manipulator.mutable.block.WallData;
import org.spongepowered.api.data.manipulator.mutable.entity.ArtData;
import org.spongepowered.api.data.manipulator.mutable.entity.CareerData;
import org.spongepowered.api.data.manipulator.mutable.entity.DominantHandData;
import org.spongepowered.api.data.manipulator.mutable.entity.GameModeData;
import org.spongepowered.api.data.manipulator.mutable.entity.OcelotData;
import org.spongepowered.api.data.manipulator.mutable.entity.ParrotData;
import org.spongepowered.api.data.manipulator.mutable.entity.RabbitData;
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
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.data.type.Arts;
import org.spongepowered.api.data.type.BigMushroomTypes;
import org.spongepowered.api.data.type.BrickTypes;
import org.spongepowered.api.data.type.Careers;
import org.spongepowered.api.data.type.CoalTypes;
import org.spongepowered.api.data.type.ComparatorTypes;
import org.spongepowered.api.data.type.CookedFishes;
import org.spongepowered.api.data.type.DirtTypes;
import org.spongepowered.api.data.type.DisguisedBlockTypes;
import org.spongepowered.api.data.type.DoublePlantTypes;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.data.type.Fishes;
import org.spongepowered.api.data.type.GoldenApples;
import org.spongepowered.api.data.type.HandPreferences;
import org.spongepowered.api.data.type.Hinges;
import org.spongepowered.api.data.type.LogAxes;
import org.spongepowered.api.data.type.OcelotTypes;
import org.spongepowered.api.data.type.ParrotVariants;
import org.spongepowered.api.data.type.PistonTypes;
import org.spongepowered.api.data.type.PlantTypes;
import org.spongepowered.api.data.type.PortionTypes;
import org.spongepowered.api.data.type.PrismarineTypes;
import org.spongepowered.api.data.type.QuartzTypes;
import org.spongepowered.api.data.type.RabbitTypes;
import org.spongepowered.api.data.type.RailDirections;
import org.spongepowered.api.data.type.SandTypes;
import org.spongepowered.api.data.type.SandstoneTypes;
import org.spongepowered.api.data.type.ShrubTypes;
import org.spongepowered.api.data.type.SkullTypes;
import org.spongepowered.api.data.type.SlabTypes;
import org.spongepowered.api.data.type.StairShapes;
import org.spongepowered.api.data.type.StoneTypes;
import org.spongepowered.api.data.type.TreeTypes;
import org.spongepowered.api.data.type.WallTypes;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Axis;
import org.spongepowered.api.util.Color;
import org.spongepowered.api.util.rotation.Rotations;

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
        /////////////////////
        // General Package //
        /////////////////////

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

        /// variant containers
        registerVariant(DyeableData.class, ImmutableDyeableData.class, Keys.DYE_COLOR, DyeColors.WHITE);
        registerVariant(RotationalData.class, ImmutableRotationalData.class, Keys.ROTATION, Rotations.LEFT);
        registerVariant(SkullData.class, ImmutableSkullData.class, Keys.SKULL_TYPE, SkullTypes.SKELETON);

        /// list containers
        registerList(FireworkEffectData.class, ImmutableFireworkEffectData.class, Keys.FIREWORK_EFFECTS);
        registerList(PotionEffectData.class, ImmutablePotionEffectData.class, Keys.POTION_EFFECTS);

        ///////////////////
        // Block Package //
        ///////////////////

        /// normal containers
        register(AttachedData.class, ImmutableAttachedData.class, LanternAttachedData.class, LanternImmutableAttachedData.class,
                valueContainer -> valueContainer.registerKey(Keys.ATTACHED, false));

        /// variant containers
        registerVariant(AxisData.class, ImmutableAxisData.class, Keys.AXIS, Axis.X);
        registerVariant(BigMushroomData.class, ImmutableBigMushroomData.class, Keys.BIG_MUSHROOM_TYPE, BigMushroomTypes.CENTER);
        registerVariant(BrickData.class, ImmutableBrickData.class, Keys.BRICK_TYPE, BrickTypes.DEFAULT);
        registerVariant(ComparatorData.class, ImmutableComparatorData.class, Keys.COMPARATOR_TYPE, ComparatorTypes.COMPARE);
        registerVariant(DirtData.class, ImmutableDirtData.class, Keys.DIRT_TYPE, DirtTypes.DIRT);
        registerVariant(DisguisedBlockData.class, ImmutableDisguisedBlockData.class, Keys.DISGUISED_BLOCK_TYPE, DisguisedBlockTypes.STONE);
        registerVariant(DoublePlantData.class, ImmutableDoublePlantData.class, Keys.DOUBLE_PLANT_TYPE, DoublePlantTypes.GRASS);
        registerVariant(HingeData.class, ImmutableHingeData.class, Keys.HINGE_POSITION, Hinges.LEFT);
        registerVariant(LogAxisData.class, ImmutableLogAxisData.class, Keys.LOG_AXIS, LogAxes.X);
        registerVariant(PistonData.class, ImmutablePistonData.class, Keys.PISTON_TYPE, PistonTypes.NORMAL);
        registerVariant(PlantData.class, ImmutablePlantData.class, Keys.PLANT_TYPE, PlantTypes.POPPY);
        registerVariant(PortionData.class, ImmutablePortionData.class, Keys.PORTION_TYPE, PortionTypes.BOTTOM);
        registerVariant(PrismarineData.class, ImmutablePrismarineData.class, Keys.PRISMARINE_TYPE, PrismarineTypes.BRICKS);
        registerVariant(QuartzData.class, ImmutableQuartzData.class, Keys.QUARTZ_TYPE, QuartzTypes.DEFAULT);
        registerVariant(RailDirectionData.class, ImmutableRailDirectionData.class, Keys.RAIL_DIRECTION, RailDirections.NORTH_SOUTH);
        registerVariant(SandData.class, ImmutableSandData.class, Keys.SAND_TYPE, SandTypes.NORMAL);
        registerVariant(SandstoneData.class, ImmutableSandstoneData.class, Keys.SANDSTONE_TYPE, SandstoneTypes.DEFAULT);
        registerVariant(ShrubData.class, ImmutableShrubData.class, Keys.SHRUB_TYPE, ShrubTypes.DEAD_BUSH);
        registerVariant(SlabData.class, ImmutableSlabData.class, Keys.SLAB_TYPE, SlabTypes.WOOD);
        registerVariant(StairShapeData.class, ImmutableStairShapeData.class, Keys.STAIR_SHAPE, StairShapes.STRAIGHT);
        registerVariant(StoneData.class, ImmutableStoneData.class, Keys.STONE_TYPE, StoneTypes.STONE);
        registerVariant(TreeData.class, ImmutableTreeData.class, Keys.TREE_TYPE, TreeTypes.OAK);
        registerVariant(WallData.class, ImmutableWallData.class, Keys.WALL_TYPE, WallTypes.NORMAL);

        /// list containers

        ////////////////////
        // Entity Package //
        ////////////////////

        /// normal containers

        /// variant containers
        registerVariant(ArtData.class, ImmutableArtData.class, Keys.ART, Arts.AZTEC);
        registerVariant(CareerData.class, ImmutableCareerData.class, Keys.CAREER, Careers.FARMER);
        registerVariant(DominantHandData.class, ImmutableDominantHandData.class, Keys.DOMINANT_HAND, HandPreferences.RIGHT);
        registerVariant(GameModeData.class, ImmutableGameModeData.class, Keys.GAME_MODE, GameModes.NOT_SET);
        registerVariant(OcelotData.class, ImmutableOcelotData.class, Keys.OCELOT_TYPE, OcelotTypes.WILD_OCELOT);
        registerVariant(ParrotData.class, ImmutableParrotData.class, Keys.PARROT_VARIANT, ParrotVariants.RED);
        registerVariant(RabbitData.class, ImmutableRabbitData.class, Keys.RABBIT_TYPE, RabbitTypes.WHITE);
        registerVariant(OcelotData.class, ImmutableOcelotData.class, Keys.OCELOT_TYPE, OcelotTypes.WILD_OCELOT);

        /// list containers

        //////////////////
        // Item Package //
        //////////////////

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

        /// variant containers
        registerVariant(CoalData.class, ImmutableCoalData.class, Keys.COAL_TYPE, CoalTypes.COAL);
        registerVariant(CookedFishData.class, ImmutableCookedFishData.class, Keys.COOKED_FISH, CookedFishes.COD);
        registerVariant(FishData.class, ImmutableFishData.class, Keys.FISH_TYPE, Fishes.COD);
        registerVariant(GoldenAppleData.class, ImmutableGoldenAppleData.class, Keys.GOLDEN_APPLE_TYPE, GoldenApples.GOLDEN_APPLE);
        registerVariant(SpawnableData.class, ImmutableSpawnableData.class, Keys.SPAWNABLE_ENTITY_TYPE, EntityTypes.CHICKEN);

        /// list containers
        registerList(EnchantmentData.class, ImmutableEnchantmentData.class, Keys.ITEM_ENCHANTMENTS);
        registerList(LoreData.class, ImmutableLoreData.class, Keys.ITEM_LORE);
        registerList(PagedData.class, ImmutablePagedData.class, Keys.BOOK_PAGES);
        registerList(StoredEnchantmentData.class, ImmutableStoredEnchantmentData.class, Keys.STORED_ENCHANTMENTS);

        /// containers with special behavior
        register(InventoryItemData.class, LanternInventoryItemData::new, LanternInventoryItemData::new, LanternInventoryItemData::new,
                ImmutableInventoryItemData.class, LanternImmutableInventoryItemData::new, LanternImmutableInventoryItemData::new);

        /////////////////////////
        // Tile Entity Package //
        /////////////////////////

        /// normal containers

        /// variant containers

        /// list containers
        registerList(SignData.class, ImmutableSignData.class, Keys.SIGN_LINES);
    }

    private static final class RegistrationInfo {

        public static RegistrationInfo build(Class<?> manipulatorType) {
            checkNotNull(manipulatorType, "manipulatorType");
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
        final DataManipulatorRegistration<M, I> registration = new SimpleDataManipulatorRegistration<>(pluginContainer, id, name,
                manipulatorType, manipulatorSupplier, manipulatorCopyFunction, immutableToMutableFunction,
                immutableManipulatorType, immutableManipulatorSupplier, mutableToImmutableFunction, manipulatorSupplier.get().getKeys());
        return register(registration);
    }

    public <M extends VariantData<E, M, I>, I extends ImmutableVariantData<E, I, M>, E> DataManipulatorRegistration<M, I> registerVariant(
            Class<M> manipulatorType, Class<I> immutableManipulatorType, Key<Value<E>> key, E defaultValue) {
        final RegistrationInfo registrationInfo = RegistrationInfo.build(manipulatorType);
        return registerVariant(registrationInfo.pluginContainer, registrationInfo.id, registrationInfo.name,
                manipulatorType, immutableManipulatorType, key, defaultValue);
    }

    public <M extends VariantData<E, M, I>, I extends ImmutableVariantData<E, I, M>, E> DataManipulatorRegistration<M, I> registerVariant(
            PluginContainer pluginContainer, String id, String name, Class<M> manipulatorType, Class<I> immutableManipulatorType,
            Key<Value<E>> key, E defaultValue) {
        final DataManipulatorRegistration<M, I> registration = this.dataManipulatorGenerator.newVariantRegistrationFor(
                pluginContainer, id, name, manipulatorType, immutableManipulatorType, key, defaultValue);
        return register(registration);
    }

    public <M extends ListData<E, M, I>, I extends ImmutableListData<E, I, M>, E> DataManipulatorRegistration<M, I> registerList(
            Class<M> manipulatorType, Class<I> immutableManipulatorType, Key<ListValue<E>> key) {
        return registerList(manipulatorType, immutableManipulatorType, key, ArrayList::new);
    }

    public <M extends ListData<E, M, I>, I extends ImmutableListData<E, I, M>, E> DataManipulatorRegistration<M, I> registerList(
            Class<M> manipulatorType, Class<I> immutableManipulatorType,
            Key<ListValue<E>> key, Supplier<List<E>> listSupplier) {
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

    @SuppressWarnings("unchecked")
    public Optional<DataManipulatorRegistration> getBy(Class manipulatorType) {
        return Optional.ofNullable(this.registrationByClass.get(checkNotNull(manipulatorType, "manipulatorType")));
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
