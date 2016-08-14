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

import org.lanternpowered.server.network.buffer.ByteBuffer;

/**
 * A {@link ParameterList} which writes the content directly to
 * a {@link ByteBuffer}. The value of a specific {@link ParameterType}
 * cannot be overwritten by calling the method again, this will
 * result in an {@link IllegalStateException}.
 * The {@link #finish()} method should be called once all the entries
 * are written.
 */
public class ByteBufParameterList implements ParameterList {

    private final ByteBuffer buf;

    public ByteBufParameterList(ByteBuffer buf) {
        this.buf = buf;
    }

    private <T> void writeValueHeader(ParameterType<T> type) {
        this.buf.writeByte(type.getValueType().getInternalId());
        this.buf.writeByte(type.index);
    }

    @Override
    public <T> void add(ParameterType<T> type, T value) {
        this.writeValueHeader(type);
        type.getValueType().serialize(this.buf, value);
    }

    @Override
    public void add(ParameterType<Byte> type, byte value) {
        this.writeValueHeader(type);
        this.buf.writeByte(value);
    }

    @Override
    public void add(ParameterType<Integer> type, int value) {
        this.writeValueHeader(type);
        this.buf.writeVarInt(value);
    }

    @Override
    public void add(ParameterType<Float> type, float value) {
        this.writeValueHeader(type);
        this.buf.writeFloat(value);
    }

    @Override
    public void add(ParameterType<Boolean> type, boolean value) {
        this.writeValueHeader(type);
        this.buf.writeBoolean(value);
    }

    /**
     * Finishes this {@link ParameterList}, this writes the end
     * tag of the list.
     */
    public void finish() {
        this.buf.writeByte((byte) 0xff);
    }
}
