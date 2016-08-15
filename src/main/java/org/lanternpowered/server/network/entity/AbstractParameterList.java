package org.lanternpowered.server.network.entity;

import org.lanternpowered.server.network.buffer.ByteBuffer;

public abstract class AbstractParameterList implements ParameterList {

    /**
     * Writes the {@link ParameterList} to the {@link ByteBuffer}.
     *
     * @param byteBuffer The byte buffer
     */
    public abstract void write(ByteBuffer byteBuffer);
}
