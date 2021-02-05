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

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * Class which encapsulates relevant data of InventoryClickEvent
 */
public class MenuClick {
    protected final Player player;
    private final InventoryClickEvent inventoryClickEvent;

    protected MenuClick(InventoryClickEvent e, Player player) {
        this.player = player;
        inventoryClickEvent = e;
    }

    /**
     * Get the player that clicked the Menu.
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the full InventoryView of the transaction.
     */
    public InventoryView getInventoryView() {
        return inventoryClickEvent.getView();
    }

    /**
     * Get the index of the slot that was clicked.
     * @return {@link InventoryClickEvent#getSlot()}
     */
    public int getSlotIndex() {
        return inventoryClickEvent.getSlot();
    }

    /**
     * Get the {@link ClickType} of this event.
     * <p>Feel free to explore the enum, it looks powerful.</p>
     * @return {@link InventoryClickEvent#getClick()}
     */
    public ClickType getClickType() {
        return inventoryClickEvent.getClick();
    }

    // TODO: decide if actions on empty slots should be allowed
    /**
     * Try to get the item clicked.
     * <p>As the API exists right now, this method is useless.
     * If there is interest in adding actions on empty slots/etc.,
     * I will look into at that point in time.</p>
     * @return an Optional of the clicked ItemStack
     */
    public Optional<ItemStack> getItemClicked() {
        return Optional.ofNullable(inventoryClickEvent.getCurrentItem());
    }

    /**
     * Check for the item currently on the cursor.
     * <p>This is usually null, so instead of using the Nullable
     * annotation here I chose to use {@link Optional#ofNullable}.</p>
     * @return an Optional describing an ItemStack or an empty Optional
     */
    public Optional<ItemStack> getItemOnMouseCursor() {
        return Optional.ofNullable(inventoryClickEvent.getCursor());
    }

    /**
     * Permanently uncancel the event and allow the click to follow through.
     */
    public void allowClick() {
        inventoryClickEvent.setCancelled(false);
    }
}
