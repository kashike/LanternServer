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

import com.flowpowered.math.vector.Vector3d;
import org.lanternpowered.server.entity.LanternEntity;
import org.lanternpowered.server.entity.living.player.LanternPlayer;
import org.lanternpowered.server.network.message.Message;
import org.spongepowered.api.entity.living.player.Player;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Supplier;

public abstract class AbstractEntityProtocol<E extends LanternEntity> {

    /**
     * All the players tracking this entity.
     */
    private Set<LanternPlayer> trackers = new HashSet<>();

    /**
     * The entity that is being tracked.
     */
    protected final E entity;

    public AbstractEntityProtocol(E entity) {
        this.entity = entity;
    }

    private final class SimpleEntityProtocolContext implements EntityUpdateContext {

        private Set<LanternPlayer> trackers;

        @Override
        public void sendToSelf(Message message) {
            if (entity instanceof Player) {
                ((LanternPlayer) entity).getConnection().send(message);
            }
        }

        @Override
        public void sendToSelf(Supplier<Message> messageSupplier) {
            if (entity instanceof Player) {
                this.sendToSelf(messageSupplier.get());
            }
        }

        @Override
        public void sendToAll(Message message) {
            this.sendToAllExceptSelf(message);
            this.sendToSelf(message);
        }

        @Override
        public void sendToAllExceptSelf(Message message) {
            this.trackers.forEach(tracker -> tracker.getConnection().send(message));
        }

        @Override
        public void sendToAllExceptSelf(Supplier<Message> messageSupplier) {
            if (!this.trackers.isEmpty()) {
                this.sendToAllExceptSelf(messageSupplier.get());
            }
        }
    }

    /**
     * Destroys the entity. This removes all the trackers and sends a destroy
     * message to the client.
     */
    public void destroy() {
        if (!this.trackers.isEmpty()) {
            final SimpleEntityProtocolContext ctx = new SimpleEntityProtocolContext();
            ctx.trackers = this.trackers;
            this.destroy(ctx);
            this.trackers.clear();
        }
    }

    /**
     * Updates the trackers of the entity. The players list contains all the players that
     * are in the same world of the entities.
     *
     * TODO: Or provide players based on the loaded chunks?
     *
     * @param players The players
     */
    public void updateTrackers(Set<LanternPlayer> players) {
        final Set<LanternPlayer> removed = new HashSet<>();
        final Set<LanternPlayer> added = new HashSet<>();

        final Vector3d pos = this.entity.getPosition();

        final Iterator<LanternPlayer> trackerIt = this.trackers.iterator();
        while (trackerIt.hasNext()) {
            final LanternPlayer tracker = trackerIt.next();
            if (!players.remove(tracker) || !this.isVisible(pos, tracker)) {
                trackerIt.remove();
                removed.add(tracker);
            }
        }

        for (LanternPlayer tracker : players) {
            if (this.isVisible(pos, tracker)) {
                added.add(tracker);
            }
        }

        boolean flag0 = !this.trackers.isEmpty();
        boolean flag1 = !added.isEmpty();
        boolean flag2 = !removed.isEmpty();

        if (flag0 || flag1 || flag2) {
            final SimpleEntityProtocolContext ctx = new SimpleEntityProtocolContext();

            // Stream updates to players that are already tracking
            if (flag0) {
                ctx.trackers = this.trackers;
                this.update(ctx);
            }

            // Stream spawn messages to the added trackers
            if (flag1) {
                ctx.trackers = added;
                this.spawn(ctx);
                this.trackers.addAll(added);
            }

            // Stream destroy messages to the removed trackers
            if (flag2) {
                ctx.trackers = removed;
                this.destroy(ctx);
            }
        }
    }

    /**
     * Gets whether the tracked entity is visible for the tracker.
     *
     * @param entityPosition The position of the entity
     * @param tracker The tracker
     * @return Whether the tracker can see the entity
     */
    protected abstract boolean isVisible(Vector3d entityPosition, LanternPlayer tracker);

    /**
     * Spawns the tracked entity.
     *
     * @param context The entity update context
     */
    protected abstract void spawn(EntityUpdateContext context);

    /**
     * Destroys the tracked entity.
     *
     * @param context The entity update context
     */
    protected abstract void destroy(EntityUpdateContext context);

    /**
     * Updates the tracked entity.
     *
     * @param context The entity update context
     */
    protected abstract void update(EntityUpdateContext context);
}
