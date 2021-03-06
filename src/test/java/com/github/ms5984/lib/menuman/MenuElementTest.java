package com.github.ms5984.lib.menuman;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
class MenuElementTest {
    final ItemStack itemStack = new ItemStack(Material.DIRT);

    @Test
    void testMenuElementGenerateCompleteWithNullValues() {
        assertSame(itemStack, new MenuElement(itemStack).generateComplete());
    }
}
