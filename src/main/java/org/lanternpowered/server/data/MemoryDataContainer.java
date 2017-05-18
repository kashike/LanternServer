package org.lanternpowered.server.data;

import static com.google.common.base.Preconditions.checkNotNull;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.BaseValue;

import java.util.Optional;

/**
 * The default implementation of {@link DataContainer} that can be instantiated
 * for any use. This is the primary implementation of any {@link DataView} that
 * is used throughout both SpongeAPI and Sponge implementation.
 */
@SuppressWarnings("deprecation")
public class MemoryDataContainer extends MemoryDataView implements DataContainer {

    /**
     * Creates a new {@link MemoryDataContainer} with a default
     * {@link org.spongepowered.api.data.DataView.SafetyMode} of
     * {@link org.spongepowered.api.data.DataView.SafetyMode#ALL_DATA_CLONED}.
     *
     */
    public MemoryDataContainer() {
        this(DataView.SafetyMode.ALL_DATA_CLONED);
    }

    /**
     * Creates a new {@link MemoryDataContainer} with the provided
     * {@link org.spongepowered.api.data.DataView.SafetyMode}.
     *
     * @param safety The safety mode to use
     * @see org.spongepowered.api.data.DataView.SafetyMode
     */
    public MemoryDataContainer(DataView.SafetyMode safety) {
        super(safety);
    }

    @Override
    public Optional<DataView> getParent() {
        return Optional.empty();
    }

    @Override
    public final DataContainer getContainer() {
        return this;
    }

    @Override
    public DataContainer set(DataQuery path, Object value) {
        return (DataContainer) super.set(path, value);
    }

    @Override
    public <E> DataContainer set(Key<? extends BaseValue<E>> key, E value) {
        return set(checkNotNull(key).getQuery(), value);
    }

    @Override
    public DataContainer remove(DataQuery path) {
        return (DataContainer) super.remove(path);
    }
}
