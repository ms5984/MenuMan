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

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * A class representing a created Menu.
 */
public class Menu {
    private final JavaPlugin plugin;
    private final Map<Integer, ItemStack> contents;
    private final Map<Integer, MenuAction> actions;
    public final InventoryRows numberOfRows;
    public final String title;
    private Inventory inventory;

    protected Menu(MenuBuilder menuBuilder, JavaPlugin javaPlugin) {
        this.plugin = javaPlugin;
        this.numberOfRows = menuBuilder.numberOfRows;
        this.title = menuBuilder.title;
        this.actions = menuBuilder.actions;
        contents = new HashMap<>(numberOfRows.slotCount);
        menuBuilder.items.forEach((index, element) -> contents.put(index, element.generateComplete()));
        Bukkit.getPluginManager().registerEvents(new ClickListener(), plugin);
    }

    /**
     * Lazy initialization of inventory on first get.
     * <p>Added bonus: listener returns faster before getInventory is called.</p>
     * @return generated Inventory
     */
    private Inventory getInventory() {
        if (inventory == null) {
            inventory = Bukkit.createInventory(null, numberOfRows.slotCount, title);
            for (Map.Entry<Integer, ItemStack> element : contents.entrySet()) {
                inventory.setItem(element.getKey(), element.getValue());
            }
        }
        return inventory;
    }

    /**
     * Open this Menu for the given player.
     * @param player player to open menu for
     */
    public void open(Player player) {
        player.openInventory(getInventory());
    }

    /**
     * Registered listener which passes information to the actions.
     */
    private class ClickListener implements Listener {
        @EventHandler
        public void onMenuClick(InventoryClickEvent e) {
            if (e.getInventory() != inventory) {
                return;
            }
            final HumanEntity whoClicked = e.getWhoClicked();
            if (!(whoClicked instanceof Player)) {
                return;
            }
            final Player player = (Player) whoClicked;
            final int slot = e.getSlot();
            if (contents.keySet().parallelStream().anyMatch(key -> key == slot)) {
                e.setCancelled(true);
            }
            if (actions.containsKey(slot)) {
                actions.get(slot).onClick(new MenuClick(e, player));
            }
        }
    }

    /**
     * Helpful enumeration to enforce slot parameter contract.
     */
    public enum InventoryRows {
        ONE(9),
        TWO(18),
        THREE(27),
        FOUR(36),
        FIVE(45),
        SIX(54);

        public final int slotCount;

        InventoryRows(int slots) {
            this.slotCount = slots;
        }
    }
}
