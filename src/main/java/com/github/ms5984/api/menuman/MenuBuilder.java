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
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Fluid interface menu builder
 */
public class MenuBuilder {

    public final Menu.InventoryRows numberOfRows;
    protected final Map<Integer, MenuElement> items = new HashMap<>();
    protected final Map<Integer, MenuAction> actions = new HashMap<>();
    protected String title;

    public MenuBuilder(Menu.InventoryRows rows, String title) {
        this.numberOfRows = rows;
        this.title = title;
    }

    /**
     * Set a new title for the menu
     * @param title a new title for the generate menu
     */
    public MenuBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Add a previously styled ItemStack directly to the menu.
     * <p>This is useful if you're used to managing your own
     * custom items. Your item will not be altered.</p>
     * @param item item to add
     */
    public ElementBuilder addElement(ItemStack item) {
        return new ElementBuilder(this, new MenuElement(item));
    }

    /**
     * Add an ItemStack to the menu specified display name and lore.
     * <p>Recommended if you are not used to altering ItemMetas.
     * Handles styling for you.</p>
     * @param item item to add
     * @param text display name of item
     * @param lore optional lore to add as varargs
     */
    public ElementBuilder addElement(ItemStack item, String text, String... lore) {
        return new ElementBuilder(this, new MenuElement(item, text, lore));
    }

    /**
     * Create the Menu specified by the contents of this builder.
     * <p>Requires plugin reference for event syncing.</p>
     * @return new Menu initialized with this object's contents
     */
    public Menu create(JavaPlugin yourPlugin) {
        return new Menu(this, yourPlugin);
    }
}
