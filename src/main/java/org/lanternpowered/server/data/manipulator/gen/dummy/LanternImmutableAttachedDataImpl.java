package org.lanternpowered.server.data.manipulator.gen.dummy;

import org.lanternpowered.server.data.manipulator.immutable.AbstractImmutableData;
import org.lanternpowered.server.data.manipulator.immutable.block.LanternImmutableAttachedData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableAttachedData;
import org.spongepowered.api.data.manipulator.mutable.block.AttachedData;

public class LanternImmutableAttachedDataImpl extends AbstractImmutableData<ImmutableAttachedData, AttachedData> implements ImmutableAttachedData,
        LanternImmutableAttachedData {

    public LanternImmutableAttachedDataImpl() {
        super(ImmutableAttachedData.class, AttachedData.class);
    }

    public LanternImmutableAttachedDataImpl(AttachedData manipulator) {
        super(manipulator);
    }

    @Override
    public void registerKeys() {
        // Cannot be null
        LanternAttachedDataImpl.registrationConsumer.accept(this);
    }
}
