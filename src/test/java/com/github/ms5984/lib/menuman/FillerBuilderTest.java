package com.github.ms5984.lib.menuman;

import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FillerBuilderTest {
    @Mock
    ItemStack fakeItem;
    @Mock
    ItemStack displacementItem;
    final int displacementItemSlot = 13;

    @Test
    void simpleConstructor() {
        getSimpleFillerBuilder().set().items.forEach((index, element) -> {
            if (index == displacementItemSlot) {
                assertNotSame(fakeItem, element.baseItem);
                return;
            }
            assertSame(fakeItem, element.baseItem);
        });
    }

    @Test
    void setAction() {
        final ClickAction clickAction = click -> {};
        getSimpleFillerBuilder().setAction(clickAction).set().actions.forEach((index, action) -> {
            if (index == displacementItemSlot) {
                assertNotSame(clickAction, action);
                return;
            }
            assertSame(clickAction, action);
        });
    }

    @Test
    void setItem(@Mock ItemStack newItem) {
        getSimpleFillerBuilder().setItem(newItem).set().items.forEach((index, menuElement) -> {
            if (index == displacementItemSlot) {
                assertNotSame(newItem, menuElement.baseItem);
                return;
            }
            assertSame(newItem, menuElement.baseItem);
        });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void testSetLore() {
        // test default
        final MenuBuilder plain = getSimpleFillerBuilder().set();
        plain.items.forEach((index, element) -> assertNull(element.lore));
        // test empty
        final MenuBuilder menuBuilder = getSimpleFillerBuilder().setLore().set();
        menuBuilder.items.forEach((index, element) -> {
            if (index == displacementItemSlot) {
                assertThrows(NullPointerException.class, () -> element.lore.isEmpty());
                return;
            }
            assertTrue(element.lore.isEmpty());
        });
        // test one line
        final MenuBuilder menuBuilder1 = getSimpleFillerBuilder().setLore("line1").set();
        menuBuilder1.items.forEach((index, element) -> {
            if (index == displacementItemSlot) {
                assertThrows(NullPointerException.class, () -> element.lore.contains("line1"));
                return;
            }
            assertTrue(element.lore.contains("line1"));
        });
        // test two lines
        final MenuBuilder menuBuilder2 = getSimpleFillerBuilder().setLore("line1", "line2").set();
        menuBuilder2.items.forEach((index, element) -> {
            if (index == displacementItemSlot) {
                assertThrows(NullPointerException.class, () -> element.lore.contains("line1"));
                assertThrows(NullPointerException.class, () -> element.lore.contains("line2"));
                return;
            }
            assertTrue(element.lore.contains("line1"));
            assertTrue(element.lore.contains("line2"));
        });
        // test array
        final String[] lore = {"arr1"};
        final MenuBuilder menuBuilder3 = getSimpleFillerBuilder().setLore(lore).set();
        menuBuilder3.items.forEach((index, element) -> {
            if (index == displacementItemSlot) {
                assertThrows(NullPointerException.class, () -> element.lore.contains("arr1"));
                return;
            }
            assertTrue(element.lore.contains("arr1"));
        });
        // test array with elements
        final String[] lore1 = {"arr1", "arr2"};
        final MenuBuilder menuBuilder4 = getSimpleFillerBuilder().setLore(lore1).set();
        menuBuilder4.items.forEach((index, element) -> {
            if (index == displacementItemSlot) {
                assertThrows(NullPointerException.class, () -> element.lore.contains("arr1"));
                assertThrows(NullPointerException.class, () -> element.lore.contains("arr2"));
                return;
            }
            assertTrue(element.lore.contains("arr1"));
            assertTrue(element.lore.contains("arr2"));
        });
    }

    @Test
    void testAddLore() {
        // test simple add
        assertDoesNotThrow(() -> getSimpleFillerBuilder().addLore("test"));
        // test set then add
        getSimpleFillerBuilder().setLore("Test").addLore("test2").set().items.forEach((index, element) -> {
            if (index == displacementItemSlot) {
                assertNull(element.lore);
                return;
            }
            assertEquals("Test", element.lore.get(0));
            assertEquals("test2", element.lore.get(1));
        });
    }

    FillerBuilder getSimpleFillerBuilder() {
        return new MenuBuilder(Menu.InventoryRows.THREE, "Test title")
                .addElement(displacementItem).assignToSlots(displacementItemSlot)
                .setFiller(fakeItem);
    }
}
