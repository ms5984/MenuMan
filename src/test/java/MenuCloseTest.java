import com.github.ms5984.lib.menuman.MenuClose;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class MenuCloseTest {

    @Mock(name = "player")
    Player player;
    @Mock(name = "e")
    private InventoryCloseEvent e;
    @InjectMocks
    private MenuClose menuClose;
    @Mock
    Inventory inventory;
    @Mock
    InventoryView view;

    @Test
    public void testGetPlayer() {
        doReturn(player).when(e).getPlayer();
        assertSame(e.getPlayer(), menuClose.getPlayer());
    }

    @Test
    public void testGetUpperInventory() {
        doReturn(inventory).when(e).getInventory();
        assertSame(e.getInventory(), menuClose.getUpperInventory());
    }

    @Test
    public void testGetInventoryView() {
        //noinspection ResultOfMethodCallIgnored
        doReturn(view).when(e).getView();
        assertSame(e.getView(), menuClose.getInventoryView());
    }
}
