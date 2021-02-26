import com.github.ms5984.lib.menuman.MenuClose;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
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

    @Before
    public void initMocks() {
        doReturn(player).when(e).getPlayer();
        doReturn(inventory).when(e).getInventory();
        //noinspection ResultOfMethodCallIgnored
        doReturn(view).when(e).getView();
    }

    @Test
    public void testGetPlayer() {
        assertSame(e.getPlayer(), menuClose.getPlayer());
    }

    @Test
    public void testGetUpperInventory() {
        assertSame(e.getInventory(), menuClose.getUpperInventory());
    }

    @Test
    public void testGetInventoryView() {
        assertSame(e.getView(), menuClose.getInventoryView());
    }
}
