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
public class MenuClickTest {

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
    public void testGetPlayer() {
        assertSame(player, menuClick.getPlayer());
    }

    @Test
    public void testGetInventoryView() {
        when(e.getView()).thenReturn(view);
        assertSame(e.getView(), menuClick.getInventoryView());
    }

    @Test
    public void testGetClickType() {
        when(e.getClick()).thenReturn(testClick);
        assertSame(e.getClick(), menuClick.getClickType());
    }

    @Test
    public void testGetSlotClicked() {
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
    public void testGetItemOnMouseCursor() {
        // init stubbing
        doReturn(cursor, cursor, null).when(e).getCursor();
        // test present
        assertTrue(menuClick.getItemOnMouseCursor().isPresent());
        assertSame(cursor, menuClick.getItemOnMouseCursor().get());
        // test empty
        assertFalse(menuClick.getItemOnMouseCursor().isPresent());
    }

    @Test
    public void testAllowClick() {
        menuClick.allowClick();
        verify(e).setCancelled(false);
    }

    @Test
    public void testDisallowClick() {
        menuClick.disallowClick();
        verify(e).setCancelled(true);
    }

    @Test
    public void testInventorySlotGetItem() {
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
    public void testInventorySlotSetItem() {
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
