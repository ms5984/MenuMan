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

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class ElementBuilder {
    protected final MenuBuilder menuBuilder;
    protected MenuAction menuAction;
    protected MenuElement menuElement;

    protected ElementBuilder(MenuBuilder menuBuilder, MenuElement menuElement) {
        this.menuBuilder = menuBuilder;
        this.menuElement = menuElement;
    }

    /**
     * Set a new action for this element.
     * @param menuAction which accepts MenuClick
     */
    public ElementBuilder setAction(MenuAction menuAction) {
        this.menuAction = menuAction;
        return this;
    }

    /**
     * Set a new base item for this element.
     * @param itemStack a valid ItemStack
     */
    public ElementBuilder setItem(@NotNull ItemStack itemStack) {
        this.menuElement.baseItem = itemStack;
        return this;
    }

    /**
     * Set a new display text for this element.
     * @param text display name of item
     */
    public ElementBuilder setText(String text) {
        this.menuElement.displayName = text;
        return this;
    }

    /**
     * Set new lore text for this element.
     * @param lore String varargs new lore
     */
    public ElementBuilder setLore(String... lore) {
        this.menuElement.lore = new ArrayList<>(Arrays.asList(lore));
        return this;
    }

    /**
     * Add a line to lore for this element.
     * @param line line of lore to be added
     */
    public ElementBuilder addLore(@NotNull String line) {
        this.menuElement.lore.add(line);
        return this;
    }

    /**
     * Assign this element to slots of the menu.
     * <p>Slots are zero-indexed: the first row is 0-8, not 1-9.</p>
     * @param slots varargs of desired slots
     * @throws IllegalArgumentException if any int outside of inventory range
     */
    public MenuBuilder assignToSlots(int... slots) throws IllegalArgumentException {
        for (int slot : slots) {
            if (slot >= menuBuilder.numberOfRows.slotCount) throw new IllegalArgumentException("That is not a valid slot!");
            if (menuBuilder.numberOfRows.slotCount < 0) throw new IllegalArgumentException("That is not a valid slot!");
            menuBuilder.items.put(slot, menuElement);
            menuBuilder.actions.put(slot, menuAction);
        }
        return menuBuilder;
    }
}
