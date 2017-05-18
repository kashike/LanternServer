package org.lanternpowered.server.data.manipulator;

import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

import java.util.Optional;

public interface IImmutableValueHolder {

    <E, R extends ImmutableValue<E>> Optional<R> getImmutableValue(Key<? extends BaseValue<E>> key);
}
