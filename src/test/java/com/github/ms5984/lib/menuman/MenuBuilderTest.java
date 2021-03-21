package com.github.ms5984.lib.menuman;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MenuBuilderTest {

    final Menu.InventoryRows rows = Menu.InventoryRows.THREE;
    final String title = "Test title";
    final String[] lore = {"Some lore"};
    final MenuBuilder menuBuilder = new MenuBuilder(rows, title);
    final ItemStack[] initialStacks = new ItemStack[27];
    final MenuBuilder menuBuilderWithContents = new MenuBuilder(rows, title, initialStacks);

    @Test
    void testSetCloseAction(@Mock CloseAction mock) {
        // test simple set
        menuBuilder.setCloseAction(mock);
        assertSame(mock, menuBuilder.closeAction);
        // test null
        menuBuilder.setCloseAction(null);
        assertNull(menuBuilder.closeAction);
    }

    @Test
    void testSetTitle() {
        // test null
        menuBuilder.setTitle(null);
        assertNull(menuBuilder.title);
        // reset title
        menuBuilder.setTitle(title);
        assertEquals(title, menuBuilder.title);
    }

    @Test
    void testSetInitialContents() {
        // test throw on too big array
        assertThrows(IllegalArgumentException.class, () -> menuBuilder.setInitialContents(new ItemStack[45]));
        // test assignment to null
        menuBuilder.setInitialContents(null);
        assertNull(menuBuilder.initialContents);
        // test reassignment to original array
        menuBuilder.setInitialContents(initialStacks);
        assertSame(initialStacks, menuBuilder.initialContents);
    }

    @Test
    void testDefaultClickBehavior() {
        // test initial state
        assertFalse(menuBuilder.allowItemPickup);
        // set to true
        assertTrue(menuBuilder.defaultClickBehavior(true).allowItemPickup);
        // set back to false
        menuBuilder.defaultClickBehavior(false);
        // test final state
        assertFalse(menuBuilder.allowItemPickup);
    }

    @Test
    void testCancelLowerInventoryClicks() {
        // test initial state
        assertFalse(menuBuilder.cancelLowerInvClick);
        // set to true
        assertTrue(menuBuilder.cancelLowerInventoryClicks(true).cancelLowerInvClick);
        // set back to false
        menuBuilder.cancelLowerInventoryClicks(false);
        // test final state
        assertFalse(menuBuilder.cancelLowerInvClick);
    }

    @Test
    void testAllowLowerShiftClicks() {
        // test initial state
        assertFalse(menuBuilder.allowLowerInvShiftClick);
        // set to true
        assertTrue(menuBuilder.allowLowerShiftClicks(true).allowLowerInvShiftClick);
        // set back to false
        menuBuilder.allowLowerShiftClicks(false);
        // test final state
        assertFalse(menuBuilder.allowLowerInvShiftClick);
    }

    @Test
    void testAddElement(@Mock ItemStack itemStack, @Mock ClickAction clickAction) {
        // create temporary builder
        final MenuBuilder temp = new MenuBuilder(rows, title);
        final ElementBuilder elementBuilder = temp.addElement(itemStack);
        // set test title, lore
        elementBuilder.setText(title).setLore(lore);
        // assign action mock
        elementBuilder.setAction(clickAction);
        // add to temp
        elementBuilder.assignToSlots(0);
        // check element baseItem, title, lore and action on builder
        final MenuElement menuElement = temp.items.get(0);
        assertSame(itemStack, menuElement.baseItem);
        assertEquals(title, menuElement.displayName);
        assertArrayEquals(lore, menuElement.lore.toArray(new String[0]));
        assertSame(clickAction, temp.actions.get(0));
    }

    @Test
    void testAddElementOverload(@Mock ItemStack itemStack, @Mock ClickAction clickAction) {
        // create temporary builder
        final MenuBuilder temp = new MenuBuilder(rows, title);
        // set item, title, lore one-shot
        final ElementBuilder elementBuilder = temp.addElement(itemStack, title, lore);
        // assign action mock
        elementBuilder.setAction(clickAction);
        // add to temp
        elementBuilder.assignToSlots(0);
        // check element baseItem, title, lore and action on builder
        final MenuElement menuElement = temp.items.get(0);
        assertSame(itemStack, menuElement.baseItem);
        assertEquals(title, menuElement.displayName);
        assertArrayEquals(lore, menuElement.lore.toArray(new String[0]));
        assertSame(clickAction, temp.actions.get(0));
    }

    @Test
    void testSetFiller(@Mock ItemStack itemStack, @Mock ClickAction clickAction) {
        // create temporary builder
        final MenuBuilder temp = new MenuBuilder(rows, title);
        final FillerBuilder fillerBuilder = temp.setFiller(itemStack);
        // set test title, lore
        fillerBuilder.setText(title).setLore(lore);
        // assign action mock
        fillerBuilder.setAction(clickAction);
        // add to temp
        fillerBuilder.set();
        // check filler baseItem, title, lore and action on builder
        final MenuElement menuElement = temp.fillerItem;
        assertSame(itemStack, menuElement.baseItem);
        assertEquals(title, menuElement.displayName);
        assertArrayEquals(lore, menuElement.lore.toArray(new String[0]));
        assertSame(clickAction, temp.fillerAction);
    }

    @Test
    void testSetFillerOverload(@Mock ItemStack itemStack, @Mock ClickAction clickAction) {
        // create temporary builder
        final MenuBuilder temp = new MenuBuilder(rows, title);
        // set item, title, lore one-shot
        final FillerBuilder fillerBuilder = temp.setFiller(itemStack, title, lore);
        // assign action mock
        fillerBuilder.setAction(clickAction);
        // add to temp
        fillerBuilder.set();
        // check filler baseItem, title, lore and action on builder
        final MenuElement menuElement = temp.fillerItem;
        assertSame(itemStack, menuElement.baseItem);
        assertEquals(title, menuElement.displayName);
        assertArrayEquals(lore, menuElement.lore.toArray(new String[0]));
        assertSame(clickAction, temp.fillerAction);
    }

    @Test
    void testCreate(@Mock JavaPlugin plugin) {
        // test with array
        menuBuilderWithContents.create(plugin);
        verify(Bukkit.getPluginManager()).registerEvents(any(), eq(plugin));
        // test with null title
        final Menu menu = new MenuBuilder(rows, null).create(plugin);
        assertEquals(menu.hashCode(), Integer.parseInt(menu.title.replaceAll("Menu#", "")));
    }

    @BeforeAll
    public static void setupFakeServer(@Mock Server server, @Mock PluginManager pluginManager) {
        // fake name, version, bukkitVersion
        doReturn("FakeServerTest").when(server).getName();
        doReturn("1.0").when(server).getVersion();
        doReturn("fakeBukkitVersion").when(server).getBukkitVersion();
        // fake plugin manager
        doReturn(pluginManager).when(server).getPluginManager();
        // add logger
        doReturn(Logger.getLogger("Test")).when(server).getLogger();
        // setup fake server
        try {
            final Field serverField = Bukkit.class.getDeclaredField("server");
            serverField.setAccessible(true);
            serverField.set(null, server);
        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
        }
    }
}
