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
package org.lanternpowered.server.data.manipulator.immutable.block;

import org.lanternpowered.server.data.manipulator.IImmutableValueHolder;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableConnectedDirectionData;
import org.spongepowered.api.data.value.immutable.ImmutableSetValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.util.Direction;

public interface LanternImmutableConnectedDirectionData extends ImmutableConnectedDirectionData, IImmutableValueHolder {

    @Override
    default ImmutableSetValue<Direction> connectedDirections() {
        return tryGetImmutableValue(Keys.CONNECTED_DIRECTIONS);
    }

    @Override
    default ImmutableValue<Boolean> connectedNorth() {
        return tryGetImmutableValue(Keys.CONNECTED_NORTH);
    }

    @Override
    default ImmutableValue<Boolean> connectedSouth() {
        return tryGetImmutableValue(Keys.CONNECTED_SOUTH);
    }

    @Override
    default ImmutableValue<Boolean> connectedEast() {
        return tryGetImmutableValue(Keys.CONNECTED_EAST);
    }

    @Override
    default ImmutableValue<Boolean> connectedWest() {
        return tryGetImmutableValue(Keys.CONNECTED_WEST);
    }
}
