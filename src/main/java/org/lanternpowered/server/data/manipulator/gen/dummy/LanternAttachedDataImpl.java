package org.lanternpowered.server.data.manipulator.gen.dummy;

import org.lanternpowered.server.data.manipulator.IDataManipulatorBase;
import org.lanternpowered.server.data.manipulator.mutable.AbstractData;
import org.lanternpowered.server.data.manipulator.mutable.block.LanternAttachedData;
import org.lanternpowered.server.data.value.IValueContainer;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableAttachedData;
import org.spongepowered.api.data.manipulator.mutable.block.AttachedData;

import java.util.function.Consumer;

public class LanternAttachedDataImpl extends AbstractData<AttachedData, ImmutableAttachedData> implements AttachedData, LanternAttachedData {

    public static Consumer<IValueContainer<?>> registrationConsumer;

    protected LanternAttachedDataImpl() {
        super(AttachedData.class, ImmutableAttachedData.class);
    }

    protected LanternAttachedDataImpl(ImmutableAttachedData manipulator) {
        super(manipulator);
    }

    protected LanternAttachedDataImpl(AttachedData manipulator) {
        super(manipulator);
    }

    protected LanternAttachedDataImpl(
            IDataManipulatorBase<AttachedData, ImmutableAttachedData> manipulator) {
        super(manipulator);
    }

    @Override
    public void registerKeys() {
        // Cannot be null
        registrationConsumer.accept(this);
    }
}
