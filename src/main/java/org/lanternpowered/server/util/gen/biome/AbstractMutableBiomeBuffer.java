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
package org.lanternpowered.server.util.gen.biome;

import com.flowpowered.math.vector.Vector3i;
import org.lanternpowered.server.world.extent.MutableBiomeViewDownsize;
import org.lanternpowered.server.world.extent.MutableBiomeViewTransform;
import org.lanternpowered.server.world.extent.UnmodifiableBiomeVolumeWrapper;
import org.lanternpowered.server.world.extent.worker.LanternMutableBiomeVolumeWorker;
import org.spongepowered.api.util.DiscreteTransform3;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.extent.MutableBiomeVolume;
import org.spongepowered.api.world.extent.UnmodifiableBiomeVolume;
import org.spongepowered.api.world.extent.worker.MutableBiomeVolumeWorker;

public abstract class AbstractMutableBiomeBuffer extends AbstractBiomeBuffer implements MutableBiomeVolume {

    protected AbstractMutableBiomeBuffer(Vector3i start, Vector3i size) {
        super(start, size);
    }

    @Override
    public UnmodifiableBiomeVolume getUnmodifiableBiomeView() {
        return new UnmodifiableBiomeVolumeWrapper(this);
    }

    @Override
    public void setBiome(Vector3i position, BiomeType biome) {
        setBiome(position.getX(), position.getY(), position.getZ(), biome);
    }

    @Override
    public MutableBiomeVolume getBiomeView(Vector3i newMin, Vector3i newMax) {
        checkRange(newMin);
        checkRange(newMax);
        return new MutableBiomeViewDownsize(this, newMin, newMax);
    }

    @Override
    public MutableBiomeVolume getBiomeView(DiscreteTransform3 transform) {
        return new MutableBiomeViewTransform(this, transform);
    }

    @Override
    public MutableBiomeVolumeWorker<? extends MutableBiomeVolume> getBiomeWorker() {
        return new LanternMutableBiomeVolumeWorker<>(this);
    }
}
