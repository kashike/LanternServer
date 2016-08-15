package org.lanternpowered.server.network.message;

import io.netty.util.ReferenceCounted;

/**
 * A simple implementation of a {@link Message} that is reference counted.
 *
 * @param <C> The type of the content
 */
public interface ReferenceCountedMessage<C extends ReferenceCounted> extends Message, ReferenceCounted {

    C getContent();

    @Override
    default int refCnt() {
        return getContent().refCnt();
    }

    @Override
    default ReferenceCounted retain() {
        return getContent().retain();
    }

    @Override
    default ReferenceCounted retain(int increment) {
        return getContent().retain(increment);
    }

    @Override
    default ReferenceCounted touch() {
        return getContent().touch();
    }

    @Override
    default ReferenceCounted touch(Object hint) {
        return getContent().touch(hint);
    }

    @Override
    default boolean release() {
        return getContent().release();
    }

    @Override
    default boolean release(int decrement) {
        return getContent().release(decrement);
    }
}
