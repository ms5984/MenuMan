import com.github.ms5984.lib.menuman.Menu;
import com.github.ms5984.lib.menuman.MenuBuilder;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuBuilderTest {

    final Menu.InventoryRows rows = Menu.InventoryRows.THREE;
    final String title = "Test title";
    MenuBuilder menuBuilder = new MenuBuilder(rows, title);
    @Mock
    Plugin plugin;

    // TODO: complete test
}
