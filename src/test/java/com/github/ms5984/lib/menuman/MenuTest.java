package com.github.ms5984.lib.menuman;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuTest {
    static final Menu.InventoryRows ROWS = Menu.InventoryRows.THREE;
    static final String TITLE = "Test title";
    static final MenuBuilder MENU_BUILDER = new MenuBuilder(ROWS, TITLE);
    @Spy
    static Menu menu;
    @Mock
    static ItemStack ITEM1 = mock(ItemStack.class, RETURNS_DEEP_STUBS);
    @Mock
    static ItemStack ITEM2 = mock(ItemStack.class, RETURNS_DEEP_STUBS);
    static int[] slots1 = {0, 11, 22};
    static int[] slots2 = {3, 10, 19};
    static final ClickAction action1 = click -> {};
    static final ClickAction action2 = MenuClick::allowClick;

    @Test
    public void testGetInventory(@Mock Player player) {
        ArgumentCaptor<Inventory> captor = ArgumentCaptor.forClass(Inventory.class);
        // Open menu for fake player
        menu.open(player);
        // Capture generated inventory
        verify(player).openInventory(captor.capture());
        final Inventory inventory = captor.getValue();
        // Test slots (can't be more specific at this point with item arg matcher)
        for (int s1 : slots1) {
            verify(inventory).setItem(eq(s1), any());
        }
        for (int s2 : slots2) {
            verify(inventory).setItem(eq(s2), any());
        }
    }

    @Test
    public void testInventoryRowsSlotCount() {
        // Assert every InventoryRow slotCount is divisible by 9
        for (Menu.InventoryRows rows : Menu.InventoryRows.values()) {
            assertEquals(0, rows.slotCount % 9);
        }
    }

    @BeforeAll
    public static void setupFakeServerAndMenu(@Mock Server server,
                                              @Mock PluginManager pluginManager,
                                              @Mock JavaPlugin plugin,
                                              @Mock Inventory inventory) {
        // fake name, version, bukkitVersion
        doReturn("FakeServerTest").when(server).getName();
        doReturn("1.0").when(server).getVersion();
        doReturn("fakeBukkitVersion").when(server).getBukkitVersion();
        // fake plugin manager
        doReturn(pluginManager).when(server).getPluginManager();
        // add logger
        doReturn(Logger.getLogger("Test")).when(server).getLogger();
        // setup fake server
        Bukkit.setServer(server);
        // setup Menu
        MENU_BUILDER.addElement(ITEM1).setAction(action1).assignToSlots(slots1);
        MENU_BUILDER.addElement(ITEM2).setAction(action2).assignToSlots(slots2);
        menu = spy(MENU_BUILDER.create(plugin));
        // fake createInventory
        doAnswer(invocationOnMock -> inventory).when(server).createInventory(isNull(), anyInt(), anyString());
    }
}
