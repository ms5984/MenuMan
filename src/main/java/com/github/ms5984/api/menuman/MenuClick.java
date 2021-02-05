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
