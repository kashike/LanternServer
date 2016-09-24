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
package org.lanternpowered.server.inventory.slot;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import org.lanternpowered.server.inventory.FastOfferResult;
import org.lanternpowered.server.inventory.InventoryBase;
import org.lanternpowered.server.inventory.LanternContainer;
import org.lanternpowered.server.inventory.LanternItemStack;
import org.lanternpowered.server.inventory.PeekOfferTransactionsResult;
import org.lanternpowered.server.inventory.PeekPollTransactionsResult;
import org.lanternpowered.server.inventory.PeekSetTransactionsResult;
import org.lanternpowered.server.inventory.equipment.LanternEquipmentType;
import org.lanternpowered.server.util.collect.EmptyIterator;
import org.spongepowered.api.data.property.item.EquipmentProperty;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.equipment.EquipmentType;
import org.spongepowered.api.item.inventory.property.AcceptsItems;
import org.spongepowered.api.item.inventory.property.EquipmentSlotType;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.item.inventory.transaction.SlotTransaction;
import org.spongepowered.api.text.translation.Translation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.annotation.Nullable;

public class LanternSlot extends InventoryBase implements Slot {

    /**
     * The item stack the is stored in this slot.
     */
    @Nullable private ItemStack itemStack;

    /**
     * The maximum stack size that can fit in this slot.
     */
    private int maxStackSize = 64;

    /**
     * All the {@link LanternContainer}s this slot is attached to.
     */
    private final Set<LanternContainer> containers = Collections.newSetFromMap(new WeakHashMap<>());

    public LanternSlot(@Nullable Inventory parent) {
        super(parent, null);
    }

    public LanternSlot(@Nullable Inventory parent, @Nullable Translation name) {
        super(parent, name);
    }

    public void addContainer(LanternContainer container) {
        this.containers.add(checkNotNull(container, "container"));
    }

    private void queueUpdate() {
        for (LanternContainer container : this.containers) {
            container.queueSlotChange(this);
        }
    }

    /**
     * Gets the raw {@link ItemStack} of this slot, without the need
     * to box/unbox the itemstack.
     *
     * @return The item stack
     */
    @Nullable
    public ItemStack getRawItemStack() {
        return this.itemStack;
    }

    /**
     * Check whether the supplied item can be inserted into this slot. Returning
     * false from this method implies that {@link #offer} <b>would always return
     * false</b> for this item.
     *
     * @param stack ItemStack to check
     * @return true if the stack is valid for this slot
     */
    @Override
    public boolean isValidItem(ItemStack stack) {
        return this.doesAllowEquipmentType(stack) &&
                this.doesAcceptItemType(stack);
    }

    @Override
    public boolean isChild(Inventory child) {
        return false;
    }

    @Override
    public int slotCount() {
        return 0;
    }

    @Override
    public int getStackSize() {
        return this.itemStack != null ? this.itemStack.getQuantity() : 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Inventory> Iterable<T> slots() {
        // A slot cannot contain slots
        return Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Inventory> T first() {
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Inventory> T next() {
        // TODO
        return (T) this.emptyInventory;
    }

    @Override
    public Optional<ItemStack> poll(Predicate<ItemStack> matcher) {
        checkNotNull(matcher, "matcher");
        if (this.itemStack == null || !matcher.test(this.itemStack)) {
            return Optional.empty();
        }
        final ItemStack itemStack = this.itemStack;
        // Just remove the item, the complete stack was
        // being polled
        this.itemStack = null;
        this.queueUpdate();
        return Optional.of(itemStack);
    }

    @Override
    public Optional<ItemStack> poll(int limit, Predicate<ItemStack> matcher) {
        checkNotNull(matcher, "matcher");
        checkArgument(limit >= 0, "Limit may not be negative");
        ItemStack itemStack = this.itemStack;
        // There is no item available
        if (itemStack == null || !matcher.test(itemStack)) {
            return Optional.empty();
        }
        // Split the stack if needed
        if (limit < itemStack.getQuantity()) {
            itemStack.setQuantity(itemStack.getQuantity() - limit);
            // Clone the item to be returned
            itemStack = itemStack.copy();
            itemStack.setQuantity(limit);
        } else {
            this.itemStack = null;
        }
        this.queueUpdate();
        return Optional.of(itemStack);
    }

    @Override
    public Optional<ItemStack> peek(Predicate<ItemStack> matcher) {
        checkNotNull(matcher, "matcher");
        return Optional.ofNullable(this.itemStack == null || !matcher.test(this.itemStack) ? null : this.itemStack.copy());
    }

    @Override
    public Optional<PeekPollTransactionsResult> peekPollTransactions(Predicate<ItemStack> matcher) {
        checkNotNull(matcher, "matcher");
        if (this.itemStack == null || !matcher.test(this.itemStack)) {
            return Optional.empty();
        }
        final List<SlotTransaction> transactions = new ArrayList<>();
        transactions.add(new SlotTransaction(this, this.itemStack.createSnapshot(), ItemStackSnapshot.NONE));
        return Optional.of(new PeekPollTransactionsResult(transactions, this.itemStack.copy()));
    }

    @Override
    public Optional<ItemStack> peek(int limit, Predicate<ItemStack> matcher) {
        checkNotNull(matcher, "matcher");
        checkArgument(limit >= 0, "Limit may not be negative");
        ItemStack itemStack = this.itemStack;
        // There is no item available
        if (itemStack == null || !matcher.test(itemStack)) {
            return Optional.empty();
        }
        itemStack = itemStack.copy();
        // Split the stack if needed
        if (limit < itemStack.getQuantity()) {
            itemStack.setQuantity(limit);
        }
        return Optional.of(itemStack);
    }

    @Override
    public Optional<PeekPollTransactionsResult> peekPollTransactions(int limit, Predicate<ItemStack> matcher) {
        checkNotNull(matcher, "matcher");
        checkArgument(limit >= 0, "Limit may not be negative");
        ItemStack itemStack = this.itemStack;
        // There is no item available
        if (limit == 0 || itemStack == null || !matcher.test(itemStack)) {
            return Optional.empty();
        }
        ItemStackSnapshot oldItem = itemStack.createSnapshot();
        itemStack = itemStack.copy();
        int quantity = itemStack.getQuantity();
        ItemStackSnapshot newItem;
        if (limit >= quantity) {
            newItem = ItemStackSnapshot.NONE;
        } else {
            itemStack.setQuantity(quantity - limit);
            newItem = LanternItemStack.toSnapshot(itemStack);
            itemStack.setQuantity(limit);
        }
        final List<SlotTransaction> transactions = new ArrayList<>();
        transactions.add(new SlotTransaction(this, oldItem, newItem));
        return Optional.of(new PeekPollTransactionsResult(transactions, itemStack));
    }

    @Override
    public PeekSetTransactionsResult peekSetTransactions(@Nullable ItemStack stack) {
        stack = LanternItemStack.toNullable(stack);
        boolean fail = false;
        if (stack != null) {
            if (stack.getQuantity() <= 0) {
                stack = null;
            } else {
                fail = !this.isValidItem(stack);
            }
        }
        List<SlotTransaction> transactions = new ArrayList<>();
        if (fail) {
            return new PeekSetTransactionsResult(transactions, InventoryTransactionResult.builder()
                    .type(InventoryTransactionResult.Type.FAILURE)
                    .reject(stack)
                    .build());
        }
        InventoryTransactionResult.Builder resultBuilder = InventoryTransactionResult.builder()
                .type(InventoryTransactionResult.Type.SUCCESS);
        ItemStackSnapshot oldItem = LanternItemStack.toSnapshot(this.itemStack);
        if (this.itemStack != null) {
            resultBuilder.replace(this.itemStack);
        }
        ItemStackSnapshot newItem = ItemStackSnapshot.NONE;
        if (stack != null) {
            int quantity = stack.getQuantity();
            if (quantity > this.maxStackSize) {
                stack = stack.copy();
                stack.setQuantity(this.maxStackSize);
                newItem = LanternItemStack.toSnapshot(stack);
                // Create the rest stack that was rejected,
                // because the inventory doesn't allow so many items
                stack = stack.copy();
                stack.setQuantity(quantity - this.maxStackSize);
                resultBuilder.reject(stack);
            } else {
                newItem = LanternItemStack.toSnapshot(stack);
            }
        }
        transactions.add(new SlotTransaction(this, oldItem, newItem));
        return new PeekSetTransactionsResult(transactions, resultBuilder.build());
    }

    protected boolean doesAllowItem(ItemType type) {
        return this.doesAllowEquipmentType(type) &&
                this.doesAcceptItemType(type);
    }

    protected boolean doesAllowEquipmentType(ItemType type) {
        return this.doesAllowEquipmentTypeWithProperty(() -> type.getDefaultProperty(EquipmentProperty.class));
    }

    protected boolean doesAllowEquipmentType(ItemStack stack) {
        return this.doesAllowEquipmentTypeWithProperty(() -> stack.getProperty(EquipmentProperty.class));
    }

    protected boolean doesAllowEquipmentTypeWithProperty(Supplier<Optional<EquipmentProperty>> equipmentPropertySupplier) {
        return this.doesAllowEquipmentType(() -> equipmentPropertySupplier.get()
                .flatMap(property -> Optional.ofNullable(property.getValue())));
    }

    protected boolean doesAllowEquipmentType(Supplier<Optional<EquipmentType>> equipmentTypeSupplier) {
        Collection<EquipmentSlotType> properties = this.getProperties(this.parent(), EquipmentSlotType.class);
        if (properties.isEmpty()) {
            return true;
        }
        Optional<EquipmentType> optEquipmentProperty = equipmentTypeSupplier.get();
        if (optEquipmentProperty.isPresent()) {
            EquipmentType equipmentType = optEquipmentProperty.get();
            for (EquipmentSlotType slotType : properties) {
                EquipmentType equipmentType1 = slotType.getValue();
                if (equipmentType1 != null && ((LanternEquipmentType) equipmentType1).isChild(equipmentType)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean doesAcceptItemType(ItemStack stack) {
        return this.doesAcceptItemType(stack.getItem());
    }

    protected boolean doesAcceptItemType(ItemType itemType) {
        Collection<AcceptsItems> acceptsItemsProperties = this.getProperties(this.parent(), AcceptsItems.class);
        // All items will be accepted if there are no properties of this type
        if (acceptsItemsProperties.isEmpty()) {
            return true;
        }
        for (AcceptsItems acceptsItems : acceptsItemsProperties) {
            Collection<ItemType> itemTypes = acceptsItems.getValue();
            if (itemTypes != null) {
                for (ItemType expectedType : itemTypes) {
                    if (itemType.equals(expectedType)) {
                        return true;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public FastOfferResult offerFast(ItemStack stack) {
        checkNotNull(stack, "stack");
        if (LanternItemStack.toNullable(stack) == null) {
            return new FastOfferResult(stack, false);
        }
        if (this.itemStack != null && (!((LanternItemStack) this.itemStack).isEqualToOther(stack)
                || this.itemStack.getQuantity() >= this.maxStackSize) || !this.isValidItem(stack)) {
            return new FastOfferResult(stack, false);
        }
        // Get the amount of space we have left
        int availableSpace = this.itemStack == null ? this.maxStackSize :
                this.maxStackSize - this.itemStack.getQuantity();
        int quantity = stack.getQuantity();
        if (quantity > availableSpace) {
            if (this.itemStack == null) {
                this.itemStack = stack.copy();
            }
            this.itemStack.setQuantity(this.maxStackSize);
            stack = stack.copy();
            stack.setQuantity(quantity - availableSpace);
            this.queueUpdate();
            return new FastOfferResult(stack, true);
        } else {
            if (this.itemStack == null) {
                this.itemStack = stack.copy();
            } else {
                this.itemStack.setQuantity(this.itemStack.getQuantity() + quantity);
            }
            this.queueUpdate();
            return FastOfferResult.SUCCESS;
        }
    }

    @Override
    public PeekOfferTransactionsResult peekOfferFastTransactions(ItemStack stack) {
        checkNotNull(stack, "stack");
        List<SlotTransaction> transactions = new ArrayList<>();
        if (LanternItemStack.toNullable(stack) == null) {
            return new PeekOfferTransactionsResult(transactions, new FastOfferResult(stack, false));
        }
        if (this.itemStack != null && (!((LanternItemStack) this.itemStack).isEqualToOther(stack)
                || this.itemStack.getQuantity() >= this.maxStackSize) || !this.isValidItem(stack)) {
            return new PeekOfferTransactionsResult(transactions, new FastOfferResult(stack, false));
        }
        // Get the amount of space we have left
        int availableSpace = this.itemStack == null ? this.maxStackSize :
                this.maxStackSize - this.itemStack.getQuantity();
        int quantity = stack.getQuantity();
        if (quantity > availableSpace) {
            ItemStack newStack;
            if (this.itemStack == null) {
                newStack = stack.copy();
            } else {
                newStack = this.itemStack.copy();
            }
            newStack.setQuantity(this.maxStackSize);
            stack = stack.copy();
            stack.setQuantity(quantity - availableSpace);
            transactions.add(new SlotTransaction(this, LanternItemStack.toSnapshot(this.itemStack),
                    newStack.createSnapshot()));
            return new PeekOfferTransactionsResult(transactions, new FastOfferResult(stack, true));
        } else {
            ItemStack newStack;
            if (this.itemStack == null) {
                newStack = stack.copy();
            } else {
                newStack = this.itemStack.copy();
                newStack.setQuantity(newStack.getQuantity() + quantity);
            }
            transactions.add(new SlotTransaction(this, LanternItemStack.toSnapshot(this.itemStack),
                    newStack.createSnapshot()));
            return new PeekOfferTransactionsResult(transactions, FastOfferResult.SUCCESS);
        }
    }

    @Override
    public InventoryTransactionResult set(@Nullable ItemStack stack) {
        stack = LanternItemStack.toNullable(stack);
        boolean fail = false;
        if (stack != null) {
            if (stack.getQuantity() <= 0) {
                stack = null;
            } else {
                fail = !this.isValidItem(stack);
            }
        }
        if (fail) {
            return InventoryTransactionResult.builder()
                    .type(InventoryTransactionResult.Type.FAILURE)
                    .reject(stack)
                    .build();
        }
        InventoryTransactionResult.Builder resultBuilder = InventoryTransactionResult.builder()
                .type(InventoryTransactionResult.Type.SUCCESS);
        if (this.itemStack != null) {
            resultBuilder.replace(this.itemStack);
        }
        if (stack != null) {
            stack = stack.copy();
            int quantity = stack.getQuantity();
            if (quantity > this.maxStackSize) {
                stack.setQuantity(this.maxStackSize);
                // Create the rest stack that was rejected,
                // because the inventory doesn't allow so many items
                stack = stack.copy();
                stack.setQuantity(quantity - this.maxStackSize);
                resultBuilder.reject(stack);
            }
        }
        this.itemStack = stack;
        this.queueUpdate();
        return resultBuilder.build();
    }

    @Override
    public void clear() {
        this.itemStack = null;
    }

    @Override
    public int size() {
        return this.itemStack == null ? 0 : 1;
    }

    @Override
    public int totalItems() {
        return this.itemStack == null ? 0 : this.itemStack.getQuantity();
    }

    @Override
    public int capacity() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean contains(ItemStack stack) {
        checkNotNull(stack, "stack");
        return this.itemStack != null && ((LanternItemStack) this.itemStack).isEqualToOther(stack);
    }

    @Override
    public boolean contains(ItemType type) {
        checkNotNull(type, "type");
        return this.itemStack != null && this.itemStack.getItem().equals(type);
    }

    @Override
    public int getMaxStackSize() {
        return this.maxStackSize;
    }

    @Override
    public void setMaxStackSize(int size) {
        checkArgument(size > 0, "Size must be greater then 0");
        this.maxStackSize = size;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Inventory> T query(Predicate<Inventory> matcher, boolean nested) {
        return (T) this.emptyInventory;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<Inventory> iterator() {
        return EmptyIterator.get();
    }

    /**
     * Gets whether the content of this slot should be offered
     * in the reverse offer to the main inventory when retrieving
     * the items through shift click.
     *
     * TODO: A cleaner way to implement this?
     *
     * @return Is reverse offer order
     */
    public boolean isReverseShiftClickOfferOrder() {
        return true;
    }

    public boolean doesAllowShiftClickOffer() {
        return true;
    }
}
