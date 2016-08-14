/*
 * This file is part of LanternServer, licensed under the MIT License (MIT).
 *
 * Copyright (c) LanternPowered <https://github.com/LanternPowered>
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) Contributors
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
package org.lanternpowered.server.network.entity;

import com.flowpowered.math.vector.Vector3f;
import com.flowpowered.math.vector.Vector3i;
import org.lanternpowered.server.game.registry.type.block.BlockRegistryModule;
import org.lanternpowered.server.network.buffer.ByteBuffer;
import org.lanternpowered.server.network.buffer.objects.Types;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Direction;

import java.util.Optional;
import java.util.UUID;

public final class ParameterValueTypes {

    public static final ParameterValueType<Byte> BYTE = new ParameterValueType<Byte>(0) {
        @Override
        void serialize(ByteBuffer buf, Byte value) {
            buf.writeByte(value);
        }
    };
    public static final ParameterValueType<Integer> INTEGER = new ParameterValueType<Integer>(1) {
        @Override
        void serialize(ByteBuffer buf, Integer value) {
            buf.writeVarInt(value);
        }
    };
    public static final ParameterValueType<Float> FLOAT = new ParameterValueType<Float>(2) {
        @Override
        void serialize(ByteBuffer buf, Float value) {
            buf.writeFloat(value);
        }
    };
    public static final ParameterValueType<String> STRING = new ParameterValueType<String>(3) {
        @Override
        void serialize(ByteBuffer buf, String value) {
            buf.writeString(value);
        }
    };
    public static final ParameterValueType<Text> TEXT = new ParameterValueType<Text>(4) {
        @Override
        void serialize(ByteBuffer buf, Text value) {
            buf.write(Types.TEXT, value);
        }
    };
    public static final ParameterValueType<ItemStack> ITEM_STACK = new ParameterValueType<ItemStack>(5) {
        @Override
        void serialize(ByteBuffer buf, ItemStack value) {
            buf.write(Types.ITEM_STACK, value);
        }
    };
    public static final ParameterValueType<Boolean> BOOLEAN = new ParameterValueType<Boolean>(6) {
        @Override
        void serialize(ByteBuffer buf, Boolean value) {
            buf.writeBoolean(value);
        }
    };
    public static final ParameterValueType<Vector3f> VECTOR_F = new ParameterValueType<Vector3f>(7) {
        @Override
        void serialize(ByteBuffer buf, Vector3f value) {
            buf.write(Types.VECTOR_3_F, value);
        }
    };
    public static final ParameterValueType<Vector3i> VECTOR_I = new ParameterValueType<Vector3i>(8) {
        @Override
        void serialize(ByteBuffer buf, Vector3i value) {
            buf.write(Types.VECTOR_3_I, value);
        }
    };
    public static final ParameterValueType<Optional<Vector3i>> OPTIONAL_VECTOR_I = new ParameterValueType<Optional<Vector3i>>(9) {
        @Override
        void serialize(ByteBuffer buf, Optional<Vector3i> value) {
            final Vector3i position = value.orElse(null);
            buf.writeBoolean(value.isPresent());
            if (position != null) {
                buf.write(Types.VECTOR_3_I, position);
            }
        }
    };
    public static final ParameterValueType<Direction> DIRECTION = new ParameterValueType<Direction>(10) {
        @Override
        void serialize(ByteBuffer buf, Direction value) {
            buf.writeVarInt(0); // TODO
        }
    };
    public static final ParameterValueType<Optional<UUID>> OPTIONAL_UUID = new ParameterValueType<Optional<UUID>>(11) {
        @Override
        void serialize(ByteBuffer buf, Optional<UUID> value) {
            final UUID uuid = value.orElse(null);
            buf.writeBoolean(uuid != null);
            if (uuid != null) {
                buf.writeUniqueId(uuid);
            }
        }
    };
    public static final ParameterValueType<Optional<BlockState>> OPTIONAL_BLOCK_STATE = new ParameterValueType<Optional<BlockState>>(12) {
        @Override
        void serialize(ByteBuffer buf, Optional<BlockState> value) {
            buf.writeVarInt(value.map(v -> BlockRegistryModule.get().getStateInternalId(v)).orElse((short) 0));
        }
    };

}
