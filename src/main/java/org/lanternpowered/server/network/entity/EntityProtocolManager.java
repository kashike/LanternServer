package org.lanternpowered.server.network.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import org.lanternpowered.server.entity.LanternEntity;
import org.lanternpowered.server.entity.living.player.LanternPlayer;
import org.lanternpowered.server.network.entity.vanilla.EntityProtocol;
import org.spongepowered.api.entity.Entity;

import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public final class EntityProtocolManager {

    private final Map<Entity, AbstractEntityProtocol<?>> entityProtocols = new ConcurrentHashMap<>();

    /**
     * All the {@link AbstractEntityProtocol}s that will be destroyed.
     */
    private final Queue<AbstractEntityProtocol<?>> queuedForRemoval = new ConcurrentLinkedDeque<>();

    /**
     * Adds the {@link Entity} to be tracked.
     *
     * @param entity The entity
     */
    public void add(LanternEntity entity) {
    }

    /**
     * Adds the {@link Entity} to be tracked with a specific {@link EntityProtocolType}.
     *
     * <p>This method forces the entity protocol to be refreshed, even if the entity
     * already a protocol.<p/>
     *
     * @param entity The entity
     * @param protocolType The protocol type
     */
    public <E extends LanternEntity> void add(E entity,
            EntityProtocolType<E, ? extends EntityProtocol<E>> protocolType) {
        checkNotNull(entity, "entity");
        checkNotNull(protocolType, "protocolType");
        final AbstractEntityProtocol<E> entityProtocol = protocolType.getEntityProtocolSupplier().apply(entity);
        final AbstractEntityProtocol<?> removed = this.entityProtocols.put(entity, entityProtocol);
        if (removed != null) {
            this.queuedForRemoval.add(removed);
        }
    }

    /**
     * Removes the {@link Entity} from being tracked.
     *
     * @param entity The entity
     */
    public void remove(LanternEntity entity) {
        checkNotNull(entity, "entity");
        final AbstractEntityProtocol<?> removed = this.entityProtocols.remove(entity);
        if (removed != null) {
            this.queuedForRemoval.add(removed);
        }
    }

    /**
     * Updates the trackers of the entities. The players list contains all the players that
     * are in the same world of the entities.
     *
     * @param players The players
     */
    public void updateTrackers(Set<LanternPlayer> players) {
        AbstractEntityProtocol<?> removed;
        while ((removed = this.queuedForRemoval.poll()) != null) {
            removed.destroy();
        }

        final Set<AbstractEntityProtocol<?>> protocols = new HashSet<>(this.entityProtocols.values());
        for (AbstractEntityProtocol<?> protocol : protocols) {
            if (protocol.tickCounter % protocol.getTickRate() == 0) {
                protocol.updateTrackers(players);
            }
        }

        for (AbstractEntityProtocol<?> protocol : protocols) {
            if (protocol.tickCounter++ % protocol.getTickRate() == 0) {
                protocol.postUpdateTrackers();
            }
        }
    }
}
