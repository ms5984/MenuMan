package com.github.ms5984.lib.menuman;

import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ElementBuilderTest {
    final Menu.InventoryRows rows = Menu.InventoryRows.THREE;
    @Mock
    ItemStack fakeItem;

    @Test
    void testSimpleConstructor() {
        final MenuBuilder menuBuilder = getSimpleBuilder().assignToSlots(0);
        assertSame(fakeItem, menuBuilder.items.get(0).baseItem);
    }

    @Test
    void testSetAction() {
        final ClickAction clickAction = click -> {};
        final MenuBuilder menuBuilder = getSimpleBuilder().setAction(clickAction).assignToSlots(0);
        assertSame(clickAction, menuBuilder.actions.get(0));
    }

    @Test
    void testSetItem(@Mock ItemStack newItem) {
        final MenuBuilder menuBuilder = getSimpleBuilder().setItem(newItem).assignToSlots(0);
        assertSame(newItem, menuBuilder.items.get(0).baseItem);
    }

    @Test
    void testSetLore() {
        // test default
        final MenuBuilder plain = getSimpleBuilder().assignToSlots(0);
        assertNull(plain.items.get(0).lore);
        // test empty
        final MenuBuilder menuBuilder = getSimpleBuilder().setLore().assignToSlots(0);
        assertTrue(menuBuilder.items.get(0).lore.isEmpty());
        // test one line
        final MenuBuilder menuBuilder1 = getSimpleBuilder().setLore("line1").assignToSlots(0);
        assertTrue(menuBuilder1.items.get(0).lore.contains("line1"));
        // test two lines
        final MenuBuilder menuBuilder2 = getSimpleBuilder().setLore("line1", "line2").assignToSlots(0);
        assertEquals("line1", menuBuilder2.items.get(0).lore.get(0));
        assertEquals("line2", menuBuilder2.items.get(0).lore.get(1));
        // test array
        final String[] lore = {"arr1"};
        final MenuBuilder menuBuilder3 = getSimpleBuilder().setLore(lore).assignToSlots(0);
        assertTrue(menuBuilder3.items.get(0).lore.contains("arr1"));
        // test array with elements
        final String[] lore1 = {"arr1", "arr2"};
        final MenuBuilder menuBuilder4 = getSimpleBuilder().setLore(lore1).assignToSlots(0);
        assertEquals("arr1", menuBuilder4.items.get(0).lore.get(0));
        assertEquals("arr2", menuBuilder4.items.get(0).lore.get(1));
    }

    @Test
    void testAddLore() {
        // test simple add
        assertDoesNotThrow(() -> getSimpleBuilder().addLore("test"));
        // test set then add
        final MenuBuilder menuBuilder = getSimpleBuilder().setLore("Test").addLore("test2").assignToSlots(0);
        assertEquals("Test", menuBuilder.items.get(0).lore.get(0));
        assertEquals("test2", menuBuilder.items.get(0).lore.get(1));
    }

    @SuppressWarnings("ConfusingArgumentToVarargsMethod")
    @Test
    void testAssignToSlots() {
        // test empty
        assertTrue(getSimpleBuilder().assignToSlots().items.isEmpty());
        // test null throws
        final ElementBuilder simpleBuilder = getSimpleBuilder();
        assertThrows(NullPointerException.class, () -> simpleBuilder.assignToSlots(null));
        // test one slot
        assertEquals(fakeItem, getSimpleBuilder().assignToSlots(0).items.get(0).baseItem);
        // test a few slots
        final MenuBuilder menuBuilder = getSimpleBuilder().assignToSlots(3, 7);
        assertEquals(fakeItem, menuBuilder.items.get(3).baseItem);
        assertEquals(fakeItem, menuBuilder.items.get(7).baseItem);
        // test out of range
        final ElementBuilder simpleBuilder1 = getSimpleBuilder();
        assertThrows(IllegalArgumentException.class, () -> simpleBuilder1.assignToSlots(rows.slotCount));
    }

    ElementBuilder getSimpleBuilder() {
        return new MenuBuilder(rows, "Test title").addElement(fakeItem);
    }
}
