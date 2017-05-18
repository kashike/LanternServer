package org.lanternpowered.server.data.manipulator.gen.dummy;

import org.lanternpowered.server.data.manipulator.AbstractDataManipulatorRegistration;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableAttachedData;
import org.spongepowered.api.data.manipulator.mutable.block.AttachedData;
import org.spongepowered.api.plugin.PluginContainer;

public class AttachedDataManipulatorRegistration extends AbstractDataManipulatorRegistration<AttachedData, ImmutableAttachedData> {

    public AttachedDataManipulatorRegistration(PluginContainer plugin, String id, String name) {
        super(plugin, id, name, AttachedData.class, ImmutableAttachedData.class);
    }

    @Override
    public AttachedData createMutable() {
        return new LanternAttachedDataImpl();
    }

    @Override
    public ImmutableAttachedData createImmutable() {
        return new LanternImmutableAttachedDataImpl();
    }

    @Override
    public AttachedData copyMutable(AttachedData manipulator) {
        return new LanternAttachedDataImpl(manipulator);
    }

    @Override
    public AttachedData toMutable(ImmutableAttachedData manipulator) {
        return new LanternAttachedDataImpl(manipulator);
    }

    @Override
    public ImmutableAttachedData toImmutable(AttachedData manipulator) {
        return new LanternImmutableAttachedDataImpl(manipulator);
    }
}
