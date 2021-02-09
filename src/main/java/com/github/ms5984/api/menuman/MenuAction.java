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

/**
 * Functional interface which defines behavior on use of a Menu's actions.
 */
@FunctionalInterface
public interface MenuAction {
    /**
     * When this action is called, what should happen?
     * @param behavior MenuClick consumer
     */
    void onClick(MenuClick behavior);

    /**
     * Close the inventory of the player.
     * @param menuClick a MenuClick object
     */
    static void CloseInventory(MenuClick menuClick) {
        menuClick.player.closeInventory();
    }
}
