package org.lanternpowered.server.network.entity;

import org.lanternpowered.server.entity.LanternEntity;

import java.util.function.Function;

public final class EntityProtocolType<E extends LanternEntity, P extends AbstractEntityProtocol<E>> {

    private final Class<E> entityType;
    private final Class<P> entityProtocolType;
    private final Function<E, P> entityProtocolSupplier;

    public EntityProtocolType(Class<E> entityType, Class<P> entityProtocolType, Function<E, P> entityProtocolSupplier) {
        this.entityProtocolSupplier = entityProtocolSupplier;
        this.entityProtocolType = entityProtocolType;
        this.entityType = entityType;
    }

    public Class<E> getEntityType() {
        return this.entityType;
    }

    public Class<P> getEntityProtocolType() {
        return this.entityProtocolType;
    }

    public Function<E, P> getEntityProtocolSupplier() {
        return this.entityProtocolSupplier;
    }
}
