package org.lanternpowered.server.data;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import org.spongepowered.api.data.DataAlreadyRegisteredException;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.plugin.PluginContainer;

import javax.annotation.Nullable;

public final class LanternDataRegistrationBuilder<M extends DataManipulator<M, I>, I extends ImmutableDataManipulator<I, M>>
        implements DataRegistration.Builder<M, I> {

    @Nullable Class<M> manipulatorClass;
    @Nullable Class<I> immutableClass;
    @Nullable DataManipulatorBuilder<M, I> manipulatorBuilder;
    @Nullable PluginContainer plugin;
    @Nullable String id;
    @Nullable String name;

    @SuppressWarnings("unchecked")
    @Override
    public  <D extends DataManipulator<D, C>, C extends ImmutableDataManipulator<C, D>> LanternDataRegistrationBuilder<D, C> dataClass(Class<D> manipulatorClass) {
        this.manipulatorClass = (Class<M>) checkNotNull(manipulatorClass, "DataManipulator class cannot be null!");
        return (LanternDataRegistrationBuilder<D, C>) this;
    }

    @Override
    public LanternDataRegistrationBuilder<M, I> immutableClass(Class<I> immutableDataClass) {
        checkState(this.manipulatorClass != null, "DataManipulator class must be set prior to setting the immutable variant!");
        this.immutableClass = checkNotNull(immutableDataClass, "ImmutableDataManipulator class cannot be null!");
        return this;
    }

    @Override
    public LanternDataRegistrationBuilder<M, I> manipulatorId(String id) {
        this.id = checkNotNull(id);
        checkArgument(!this.id.contains(":"), "Data ID must be formatted correctly!");
        checkArgument(!this.id.isEmpty(), "Data ID cannot be empty!");
        checkArgument(!this.id.contains(" "), "Data ID cannot contain spaces!");

        return this;
    }

    @Override
    public LanternDataRegistrationBuilder<M, I> dataName(String name) {
        this.name = checkNotNull(name);
        checkArgument(!this.name.isEmpty(), "Name cannot be empty!");
        return this;
    }

    @Override
    public LanternDataRegistrationBuilder<M, I> builder(DataManipulatorBuilder<M, I> builder) {
        this.manipulatorBuilder = checkNotNull(builder, "ManipulatorBuilder cannot be null!");
        return this;
    }

    @Override
    @SuppressWarnings("deprecation")
    public LanternDataRegistrationBuilder<M, I> from(DataRegistration<M, I> value) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Cannot set a builder with already created DataRegistrations!");
    }

    @Override
    public LanternDataRegistrationBuilder<M, I> reset() {
        this.manipulatorClass = null;
        this.immutableClass = null;
        this.manipulatorBuilder = null;
        this.plugin = null;
        this.id = null;
        this.name = null;
        return this;
    }

    @Override
    public DataRegistration<M, I> buildAndRegister(PluginContainer container)
            throws IllegalStateException, IllegalArgumentException, DataAlreadyRegisteredException {
        //checkState(!SpongeDataManager.areRegistrationsComplete(), "Registrations cannot take place at this time!");
        checkState(this.manipulatorBuilder != null, "ManipulatorBuilder cannot be null!");
        checkState(this.manipulatorClass != null, "DataManipulator class cannot be null!");
        checkState(this.immutableClass != null, "ImmutableDataManipulator class cannot be null!");
        checkState(this.id != null, "Data ID cannot be null!");
        this.plugin = container;
        //SpongeManipulatorRegistry.getInstance().validateRegistration(this);
        //SpongeDataManager.getInstance().validateRegistration(this);
        final LanternDataRegistration<M, I> registration = new LanternDataRegistration<>(this);
        //SpongeDataManager.getInstance().registerInternally(registration);
        //SpongeManipulatorRegistry.getInstance().register(registration);
        return registration;
    }
}
