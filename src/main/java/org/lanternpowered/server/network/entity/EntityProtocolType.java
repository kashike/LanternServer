package org.lanternpowered.server.network.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

public final class EntityProtocolType {

    private final List<ParameterType<?>> parameterTypes;

    public EntityProtocolType() {
        this(new ArrayList<>());
    }

    private EntityProtocolType(List<ParameterType<?>> parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    /**
     * Copies this {@link EntityProtocolType}.
     */
    public EntityProtocolType copy() {
        return new EntityProtocolType(new ArrayList<>(this.parameterTypes));
    }

    /**
     * Creates a new {@link ParameterType}.
     *
     * @param valueType The parameter value type
     * @param <T> The value type
     * @return The parameter type
     */
    public <T> ParameterType<T> newParameterType(ParameterValueType<T> valueType) {
        final ParameterType<T> parameterType = new ParameterType<>(
                this.parameterTypes.size(), checkNotNull(valueType, "valueType"));
        this.parameterTypes.add(parameterType);
        return parameterType;
    }
}
