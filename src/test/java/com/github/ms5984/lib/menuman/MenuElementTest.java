package com.github.ms5984.lib.menuman;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
public class MenuElementTest {
    static final Material MATERIAL = Material.DIRT;
    @Spy
    ItemStack itemStack = new ItemStack(MATERIAL);
    final String displayName = "Test display name";
    final String[] lore = {"This is", "some lore"};
//    final MenuElement menuElementFeatured = new MenuElement(itemStack, displayName, lore);

    @Test
    public void testMenuElementGenerateCompleteWithNullValues() {
        assertSame(itemStack, new MenuElement(itemStack).generateComplete());
    }
/*
    // TODO: revise or remove
    @Disabled
    @Test
    public void testMenuElementGenerateCompleteFeatured() {
        final ItemStack itemStack = menuElementFeatured.generateComplete();
        // verify meta generated
        verify(Bukkit.getItemFactory()).getItemMeta(MATERIAL);
        verify(Bukkit.getItemFactory()).asMetaFor(any(), eq(MATERIAL));
        // attempt to verify meta calls
        final ItemMeta meta = itemStack.getItemMeta();
        verify(meta).setDisplayName(displayName);
    }

    @BeforeAll
    public static void setupFakeServer(@Mock Server server, @Mock(answer = Answers.RETURNS_MOCKS) ItemFactory itemFactory) {
        // fake name, version, bukkitVersion
        doReturn("FakeServerTest").when(server).getName();
        doReturn("1.0").when(server).getVersion();
        doReturn("fakeBukkitVersion").when(server).getBukkitVersion();
        // fake itemFactory
        doReturn(itemFactory).when(server).getItemFactory();
        doReturn(true).when(itemFactory).isApplicable(any(), eq(MATERIAL));
        // add logger
        doReturn(Logger.getLogger("Test")).when(server).getLogger();
        // setup fake server
        Bukkit.setServer(server);
    }*/
}
