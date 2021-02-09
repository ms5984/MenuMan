/*
 *  Copyright 2021 ms5984 (Matt) <https://github.com/ms5984>
 *
 *  This file is part of MenuMan.
 *
 *  MenuMan is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  MenuMan is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.github.ms5984.api.menuman;

import lombok.val;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Encapsulation to promote lazy-initialization of ItemStacks
 */
public final class MenuElement {
    protected ItemStack baseItem;
    protected String displayName;
    protected List<String> lore;

    protected MenuElement(ItemStack baseItem) {
        this(baseItem, null);
    }
    protected MenuElement(ItemStack baseItem, String text, String... lore) {
        this.baseItem = baseItem;
        this.displayName = text;
        this.lore = new ArrayList<>(Arrays.asList(lore));
    }

    /**
     * Generate the final ItemStack, styled if needed.
     * @return generated ItemStack
     */
    @SuppressWarnings("ConstantConditions")
    public ItemStack generateComplete() {
        if (displayName != null || lore != null) {
            val finalItem = new ItemStack(baseItem);
            val meta = finalItem.getItemMeta();
            if (displayName != null) {
                meta.setDisplayName(displayName);
            }
            if (lore != null && !lore.isEmpty()) {
                meta.setLore(lore);
            }
            finalItem.setItemMeta(meta);
            return finalItem;
        }
        return baseItem;
    }
}
