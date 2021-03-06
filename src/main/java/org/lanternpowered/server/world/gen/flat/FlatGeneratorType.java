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
package org.lanternpowered.server.world.gen.flat;

import com.google.common.collect.Lists;
import org.lanternpowered.server.world.gen.LanternGeneratorType;
import org.lanternpowered.server.world.gen.LanternWorldGenerator;
import org.lanternpowered.server.world.gen.SingleBiomeGenerator;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.biome.BiomeTypes;
import org.spongepowered.api.world.gen.WorldGenerator;

import java.util.List;

public final class FlatGeneratorType extends LanternGeneratorType {

    public final static DataQuery SETTINGS = DataQuery.of("customSettings");

    /**
     * Creates the default settings of the flat generator.
     * 
     * @return the default settings
     */
    public static FlatGeneratorSettings getDefaultSettings() {
        final List<FlatLayer> layers = Lists.newArrayListWithCapacity(3);
        layers.add(new FlatLayer(BlockTypes.BEDROCK, 1));
        layers.add(new FlatLayer(BlockTypes.DIRT, 2));
        layers.add(new FlatLayer(BlockTypes.GRASS, 1));
        return new FlatGeneratorSettings(BiomeTypes.PLAINS, layers);
    }

    public FlatGeneratorType(String pluginId, String name) {
        super(pluginId, name);
    }

    @Override
    public DataContainer getGeneratorSettings() {
        return super.getGeneratorSettings().set(SETTINGS, FlatGeneratorSettingsParser.toString(
                getDefaultSettings()));
    }

    @Override
    public WorldGenerator createGenerator(World world) {
        final DataContainer generatorSettings = world.getProperties().getGeneratorSettings();
        FlatGeneratorSettings settings = null;
        if (generatorSettings.contains(SETTINGS)) {
            settings = FlatGeneratorSettingsParser.fromString(generatorSettings.getString(SETTINGS).get());
        }
        if (settings == null) {
            settings = getDefaultSettings();
        }
        final SingleBiomeGenerator biomeGenerator = new SingleBiomeGenerator(settings.getBiomeType());
        final FlatGenerationPopulator populatorGenerator = new FlatGenerationPopulator(settings,
                (LanternGeneratorType) world.getProperties().getGeneratorType());
        return new LanternWorldGenerator(world, biomeGenerator, populatorGenerator);
    }

}
