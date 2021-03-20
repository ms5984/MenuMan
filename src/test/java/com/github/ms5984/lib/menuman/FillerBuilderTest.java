package com.github.ms5984.lib.menuman;

import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FillerBuilderTest {
    static final Menu.InventoryRows ROWS = Menu.InventoryRows.THREE;
    static final String TITLE = "Test title";
    @Mock
    static ItemStack FAKE_ITEM;
    @Mock
    static ItemStack DISPLACEMENT_ITEM;
    static final int DISPLACEMENT_ITEM_SLOT = 13;

    @Test
    public void simpleConstructor() {
        getSimpleFillerBuilder().set().items.forEach((index, element) -> {
            if (index == DISPLACEMENT_ITEM_SLOT) {
                assertNotSame(FAKE_ITEM, element.baseItem);
                return;
            }
            assertSame(FAKE_ITEM, element.baseItem);
        });
    }

    @Test
    public void setAction() {
        final ClickAction clickAction = click -> {};
        getSimpleFillerBuilder().setAction(clickAction).set().actions.forEach((index, action) -> {
            if (index == DISPLACEMENT_ITEM_SLOT) {
                assertNotSame(clickAction, action);
                return;
            }
            assertSame(clickAction, action);
        });
    }

    @Test
    public void setItem(@Mock ItemStack newItem) {
        getSimpleFillerBuilder().setItem(newItem).set().items.forEach((index, menuElement) -> {
            if (index == DISPLACEMENT_ITEM_SLOT) {
                assertNotSame(newItem, menuElement.baseItem);
                return;
            }
            assertSame(newItem, menuElement.baseItem);
        });
    }

    // TODO: test setLore

    static FillerBuilder getSimpleFillerBuilder() {
        return new MenuBuilder(ROWS, TITLE)
                .addElement(DISPLACEMENT_ITEM).assignToSlots(DISPLACEMENT_ITEM_SLOT)
                .setFiller(FAKE_ITEM);
    }
}
