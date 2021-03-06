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
package com.github.ms5984.lib.menuman;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A class representing a created Menu.
 */
public final class Menu {
    private final JavaPlugin plugin;
    private final ItemStack[] initialContents;
    private final Map<Integer, ItemStack> contents;
    private final Map<Integer, ClickAction> actions;
    private final CloseAction closeAction;
    /**
     * Number of rows in the generated Inventory.
     */
    public final InventoryRows numberOfRows;
    /**
     * Title of generated Inventory.
     */
    public final String title;
    /**
     * Defines whether clicks on lower inventory are cancelled.
     */
    public final boolean cancelClickLower;
    /**
     * Defines default item pickup behavior for top inventory.
     */
    public final boolean allowPickupFromMenu;
    /**
     * Defines shift-click behavior on the lower inventory.
     */
    public final boolean allowShiftClickLower;
    private Inventory inventory;

    /**
     * Create a new Menu using the data from a builder and a plugin reference.
     *
     * @param menuBuilder a MenuBuilder
     * @param javaPlugin your plugin
     */
    protected Menu(MenuBuilder menuBuilder, JavaPlugin javaPlugin) {
        this.plugin = javaPlugin;
        this.numberOfRows = menuBuilder.numberOfRows;
        this.title = (menuBuilder.title != null) ? menuBuilder.title : "Menu#"+hashCode();
        this.initialContents = (menuBuilder.initialContents == null) ? null : menuBuilder.initialContents;
        this.cancelClickLower = menuBuilder.cancelLowerInvClick;
        this.allowPickupFromMenu = menuBuilder.allowItemPickup;
        this.allowShiftClickLower = menuBuilder.allowLowerInvShiftClick;
        this.actions = new HashMap<>(menuBuilder.actions);
        this.closeAction = menuBuilder.closeAction;
        this.contents = new HashMap<>(numberOfRows.slotCount);
        menuBuilder.items.forEach((index, element) -> contents.put(index, element.generateComplete()));
        final ItemStack fillerItem = Optional
                .ofNullable(menuBuilder.fillerItem)
                .map(MenuElement::generateComplete)
                .orElse(null);
        if (fillerItem != null) {
            for (int i = 0; i < numberOfRows.slotCount; ++i) {
                contents.putIfAbsent(i, fillerItem);
            }
        }
        if (menuBuilder.fillerAction != null) {
            for (int i = 0; i < numberOfRows.slotCount; ++i) {
                actions.putIfAbsent(i, menuBuilder.fillerAction);
            }
        }
        Bukkit.getPluginManager().registerEvents(new ClickListener(), plugin);
    }

    /**
     * Lazy initialization of inventory on first get.
     * <p>
     * Added bonus: listener returns faster before getInventory is called.
     *
     * @return generated Inventory
     */
    private Inventory getInventory() {
        if (inventory == null) {
            inventory = Bukkit.createInventory(null, numberOfRows.slotCount, title);
            if (initialContents != null) inventory.setContents(initialContents);
            for (Map.Entry<Integer, ItemStack> element : contents.entrySet()) {
                inventory.setItem(element.getKey(), element.getValue());
            }
        }
        return inventory;
    }

    /**
     * Open this Menu for the given player.
     *
     * @param player player to open menu for
     */
    public void open(Player player) {
        player.openInventory(getInventory());
    }

    /**
     * Gets the Inventory at this exact moment.
     * <p>
     * Expressed as an Optional (no viewers = no Inventory).
     * <p><b>Not</b> async safe.
     *
     * @return an Optional describing the currently generated Inventory
     */
    public Optional<Inventory> getCurrentInventory() {
        return Optional.ofNullable(inventory);
    }

    /**
     * Get all Players currently viewing this menu.
     * <p>
     * Returns an empty set if inventory == null
     *
     * @return a set of Players viewing this menu
     */
    public Set<Player> getViewers() {
        if (inventory == null) return Collections.emptySet();
        return inventory.getViewers().parallelStream()
                .filter(Player.class::isInstance)
                .map(Player.class::cast)
                .collect(Collectors.toSet());
    }

    /**
     * Registered listener which passes Inventory event
     * information to the menu's actions.
     */
    protected class ClickListener implements Listener {
        private BukkitRunnable pendingDelete;

        private ClickListener() {}

        /**
         * Process InventoryClickEvent and encapsulate to send
         * to MenuAction if so defined.
         *
         * @param e original click event
         */
        @EventHandler
        public void onMenuClick(InventoryClickEvent e) {
            // If the top inventory isn't ours, ignore it
            if (e.getInventory() != inventory) {
                return;
            }
            // If the bottom inventory was clicked...
            if (e.getClickedInventory() == e.getView().getBottomInventory()) {
                // and we want to cancel clicks for the bottom, cancel the event
                if (cancelClickLower) e.setCancelled(true);
                // if we are not allowing shift clicks
                if (e.isShiftClick() && !allowShiftClickLower) {
                    e.setCancelled(true);
                }
                return;
            }
            // If the player used hotkeys, cancel the event
            if (e.getHotbarButton() != -1) {
                e.setCancelled(true);
            }
            final HumanEntity whoClicked = e.getWhoClicked();
            // if for some reason the click isn't a player, ignore it
            if (!(whoClicked instanceof Player)) {
                return;
            }
            final Player player = (Player) whoClicked;
            final int slot = e.getSlot();
            // if this is a menu click (top inventory)
            if (e.getClickedInventory() == e.getInventory()) {
                // search the menu elements map for the slot
                if (contents.keySet().parallelStream().anyMatch(key -> key == slot)) {
                    // cancel the click
                    e.setCancelled(true);
                }
                // if we are not allowing ANY pickup from the top menu, cancel the event
                if (!allowPickupFromMenu) {
                    e.setCancelled(true);
                }
            }
            // if this slot has an associated action
            if (actions.containsKey(slot)) {
                // run action function
                actions.get(slot).onClick(new MenuClick(e, player));
            }
        }

        /**
         * Process {@link InventoryDragEvent}.
         * <p>
         * Cancel item drag events which include the top inventory.
         *
         * @param e original InventoryDragEvent
         */
        @EventHandler
        public void onMenuDrag(InventoryDragEvent e) {
            // If the top inventory isn't ours, ignore it
            if (e.getInventory() != inventory) {
                return;
            }
            // If the slots include the top inventory, cancel the event
            if (e.getRawSlots().parallelStream().anyMatch(slot -> slot < numberOfRows.slotCount)) {
                e.setCancelled(true);
            }
        }

        /**
         * Process {@link InventoryOpenEvent}.
         * <p>
         * If the currently made {@link Inventory} is opened again
         * by another player before the task timer has elapsed,
         * cancel the current destruction task.
         *
         * @param e original InventoryOpenEvent.
         */
        @EventHandler
        public void onMenuOpen(InventoryOpenEvent e) {
            if (e.getInventory() != inventory) {
                return;
            }
            if (pendingDelete != null) {
                pendingDelete.cancel();
                pendingDelete = null;
            }
        }

        /**
         * Perform close logic and schedule cleanup.
         * <p>These steps include running the CloseAction callback
         * (if present) and then the following:
         * <ul>
         *     <li>Setup a task to null the inventory after all
         *     viewers have closed it.
         *     <li>Sets this task to run in ten ticks, trying again every
         *     50 ticks until all viewers have left.
         *
         * @param e original InventoryCloseEvent
         */
        @EventHandler
        public void onMenuClose(InventoryCloseEvent e) {
            if (e.getInventory() != inventory) {
                return;
            }
            final HumanEntity closer = e.getPlayer();
            if (closeAction != null && closer instanceof Player) {
                closeAction.onClose(new MenuClose(e, (Player) closer));
            }
            pendingDelete = new BukkitRunnable() {
                @Override
                public void run() {
                    if (inventory == null) cancel();
                    if (inventory.getViewers().isEmpty()) {
                        inventory = null;
                        this.cancel();
                    }
                }
            };
            pendingDelete.runTaskTimer(plugin, 10L, 50L);
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

        /**
         * Number of slots in an Inventory of these rows.
         */
        public final int slotCount;

        InventoryRows(int slots) {
            this.slotCount = slots;
        }
    }
}
