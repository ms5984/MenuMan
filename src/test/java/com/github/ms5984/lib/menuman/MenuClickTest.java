package com.github.ms5984.lib.menuman;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuClickTest {

    @Mock(name = "player")
    Player player;
    @Mock(name = "e")
    private InventoryClickEvent e;
    @InjectMocks
    private MenuClick menuClick;
    @Mock
    InventoryView view;
    final ClickType testClick = ClickType.LEFT;
    @Mock
    Inventory inventory;
    int testSlot = 11;
    @Mock
    ItemStack cursor;

    @Test
    void testGetPlayer() {
        assertSame(player, menuClick.getPlayer());
    }

    @Test
    void testGetInventoryView() {
        when(e.getView()).thenReturn(view);
        assertSame(e.getView(), menuClick.getInventoryView());
    }

    @Test
    void testGetClickType() {
        when(e.getClick()).thenReturn(testClick);
        assertSame(e.getClick(), menuClick.getClickType());
    }

    @Test
    void testGetSlotClicked() {
        // init stubs
        when(e.getSlot()).thenReturn(testSlot);
        doReturn(inventory).when(e).getClickedInventory();
        // get slot clicked
        final Optional<MenuClick.InventorySlot> slotClicked = menuClick.getSlotClicked();
        assertTrue(slotClicked.isPresent());
        final MenuClick.InventorySlot inventorySlot = slotClicked.get();
        assertSame(inventory, inventorySlot.getClickedInventory());
        assertEquals(testSlot, inventorySlot.getIndex());
    }

    @Test
    void testGetItemOnMouseCursor() {
        // init stubbing
        doReturn(cursor, cursor, null).when(e).getCursor();
        // test present
        assertTrue(menuClick.getItemOnMouseCursor().isPresent());
        assertSame(cursor, menuClick.getItemOnMouseCursor().get());
        // test empty
        assertFalse(menuClick.getItemOnMouseCursor().isPresent());
    }

    @Test
    void testAllowClick() {
        menuClick.allowClick();
        verify(e).setCancelled(false);
    }

    @Test
    void testDisallowClick() {
        menuClick.disallowClick();
        verify(e).setCancelled(true);
    }

    @Test
    void testInventorySlotGetItem() {
        // init stubs
        when(e.getSlot()).thenReturn(testSlot);
        doReturn(inventory).when(e).getClickedInventory();
        // get slot clicked
        final Optional<MenuClick.InventorySlot> slotClicked = menuClick.getSlotClicked();
        assertTrue(slotClicked.isPresent());
        assertNull(slotClicked.get().getItem());
        verify(inventory).getItem(testSlot);
    }

    @Test
    void testInventorySlotSetItem() {
        // init stubs
        when(e.getSlot()).thenReturn(testSlot);
        doReturn(inventory).when(e).getClickedInventory();
        // get slotClicked
        final Optional<MenuClick.InventorySlot> slotClicked = menuClick.getSlotClicked();
        assertTrue(slotClicked.isPresent());
        slotClicked.get().setItem(null);
        verify(inventory).setItem(testSlot, null);
    }
}
