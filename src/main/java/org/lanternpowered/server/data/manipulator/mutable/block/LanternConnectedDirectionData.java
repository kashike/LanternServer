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
package org.lanternpowered.server.data.manipulator.mutable.block;

import org.lanternpowered.server.data.manipulator.IValueHolder;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.block.ConnectedDirectionData;
import org.spongepowered.api.data.value.mutable.SetValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.util.Direction;

public interface LanternConnectedDirectionData extends ConnectedDirectionData, IValueHolder {

    @Override
    default SetValue<Direction> connectedDirections() {
        return tryGetValue(Keys.CONNECTED_DIRECTIONS);
    }

    @Override
    default Value<Boolean> connectedNorth() {
        return tryGetValue(Keys.CONNECTED_NORTH);
    }

    @Override
    default Value<Boolean> connectedSouth() {
        return tryGetValue(Keys.CONNECTED_SOUTH);
    }

    @Override
    default Value<Boolean> connectedEast() {
        return tryGetValue(Keys.CONNECTED_EAST);
    }

    @Override
    default Value<Boolean> connectedWest() {
        return tryGetValue(Keys.CONNECTED_WEST);
    }
}
