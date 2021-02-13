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
public final class MenuBuilder {
    /**
     * Describes the number of rows and slots in the final Menu.
     */
    public final Menu.InventoryRows numberOfRows;
    /**
     * ItemStack to slot mappings.
     */
    protected final Map<Integer, MenuElement> items = new HashMap<>();
    /**
     * MenuAction to slot mappings.
     */
    protected final Map<Integer, ClickAction> actions = new HashMap<>();
    /**
     * Callback to run on menu close. Defaults to null.
     */
    protected CloseAction closeAction;
    /**
     * Title of the final Menu inventory.
     */
    protected String title;
    /**
     * Initial contents of the menu inventory before elements are added.
     */
    protected ItemStack[] initialContents;
    /**
     * Determine whether clicks on the lower inventory should be cancelled.
     */
    protected boolean cancelLowerInvClick;
    /**
     * Allow items to be removed from the menu inventory.
     */
    protected boolean allowItemPickup;

    /**
     * Create a new MenuBuilder with a number of rows and a title.
     * <p>Menu will start out blank and elements will added.</p>
     * @param rows number of rows in final Inventory
     * @param title Title of generated inventory
     */
    public MenuBuilder(Menu.InventoryRows rows, String title) {
        this.numberOfRows = rows;
        this.title = title;
    }

    /**
     * Create a new MenuBuilder with rows, title and initial contents.
     * @param rows number of rows in final Inventory
     * @param title Title of generated inventory
     * @param initialContents an array of items to prefill the menu with
     * @throws IllegalArgumentException if initialContents.length > slots
     */
    public MenuBuilder(Menu.InventoryRows rows, String title, ItemStack[] initialContents) {
        this.numberOfRows = rows;
        this.title = title;
        if (initialContents.length > rows.slotCount)
            throw new IllegalArgumentException("Initial contents larger than inventory slots!");
        this.initialContents = initialContents;
    }

    /**
     * Define a callback to run on inventory close.
     * <p>Null by default; set to null to disable.</p>
     * @param closeAction a CloseAction or null for none
     * @return this MenuBuilder
     */
    public MenuBuilder setCloseAction(CloseAction closeAction) {
        this.closeAction = closeAction;
        return this;
    }

    /**
     * Set a new title for the menu.
     * @param title a new title for the generate menu
     * @return this MenuBuilder
     */
    public MenuBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Set the initial contents of the inventory.
     * <p>This happens after creation of the inventory but before
     * menu elements are processed.</p>
     * @param contents an array of items
     * @return this MenuBuilder
     */
    public MenuBuilder setInitialContents(ItemStack[] contents) {
        this.initialContents = contents;
        return this;
    }

    /**
     * Determine default pickup behavior when clicking ItemStacks.
     * <p>Defaults to false. You can set this true and set different
     * pickup logic for specific elements (for instance, an item
     * vault system with pagination, possibly a sub-menu).</p>
     * @param allowPickup whether to allow item pickup
     * @return this MenuBuilder
     */
    public MenuBuilder defaultClickBehavior(boolean allowPickup) {
        this.allowItemPickup = allowPickup;
        return this;
    }

    /**
     * Should ALL clicks on the lower inventory be cancelled as well?
     * <p>False by default.</p>
     * @param toCancel true to cancel clicks on lower
     * @return this MenuBuilder
     */
    public MenuBuilder cancelLowerInventoryClicks(boolean toCancel) {
        this.cancelLowerInvClick = toCancel;
        return this;
    }

    /**
     * Add a previously styled ItemStack directly to the menu.
     * <p>This is useful if you're used to managing your own
     * custom items. Your item will not be altered.</p>
     * @param item item to add
     * @return a new ElementBuilder to customize the element
     */
    public ElementBuilder addElement(ItemStack item) {
        return new ElementBuilder(this, new MenuElement(item));
    }

    /**
     * Add an ItemStack to the menu of specified display name and lore.
     * <p>Recommended if you are not used to altering ItemMetas.
     * Handles styling for you.</p>
     * @param item item to add
     * @param text display name of item
     * @param lore optional lore to add as varargs
     * @return a new ElementBuilder to customize the element
     */
    public ElementBuilder addElement(ItemStack item, String text, String... lore) {
        return new ElementBuilder(this, new MenuElement(item, text, lore));
    }

    /**
     * Create the Menu specified by the contents of this builder.
     * <p>Requires plugin reference for event syncing.</p>
     * @param yourPlugin an instance of your plugin
     * @return new Menu initialized with this object's contents
     */
    public Menu create(JavaPlugin yourPlugin) {
        return new Menu(this, yourPlugin);
    }
}
