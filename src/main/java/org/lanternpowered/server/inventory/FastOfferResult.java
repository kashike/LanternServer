/*
 * This file is part of LanternServer, licensed under the MIT License (MIT).
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
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
package org.lanternpowered.server.inventory;

import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;

import javax.annotation.Nullable;

public final class FastOfferResult {

    public static final FastOfferResult SUCCESS = new FastOfferResult(null, true);

    @Nullable private final ItemStack rest;
    private final boolean success;

    public FastOfferResult(@Nullable ItemStack rest, boolean success) {
        this.success = success;
        this.rest = rest;
    }

    @Nullable
    public ItemStack getRest() {
        return this.rest;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public InventoryTransactionResult asTransactionResult() {
        if (this == FastOfferResult.SUCCESS) {
            return InventoryTransactionResults.SUCCESS;
        } else {
            InventoryTransactionResult.Builder builder = InventoryTransactionResult.builder();
            builder.type(this.success ? InventoryTransactionResult.Type.SUCCESS : InventoryTransactionResult.Type.FAILURE);
            if (this.rest != null) {
                builder.reject(this.rest);
            }
            return builder.build();
        }
    }
}
