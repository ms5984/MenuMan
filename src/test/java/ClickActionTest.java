import com.github.ms5984.lib.menuman.ClickAction;
import com.github.ms5984.lib.menuman.MenuClick;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class ClickActionTest {

    @Mock
    MenuClick menuClick;
    ClickAction clickAction = click -> assertSame(menuClick, click);
    final String testCommand = "gamemode creative";
    @Spy
    ClickAction.RunCommand runCommand = new ClickAction.RunCommand() {
        @Override
        public @NotNull String commandToRun() {
            return testCommand;
        }
    };
    ClickAction.RunCommand runCommandFalse = new ClickAction.RunCommand() {
        @Override
        public @NotNull String commandToRun() {
            return testCommand;
        }

        @Override
        public boolean closeOnClick() {
            return false;
        }
    };

    @Before
    public void initSpy() {
        doNothing().when(runCommand).onClick(any());
    }

    @Test
    public void testOnClick() {
        clickAction.onClick(menuClick);
    }

    @Test
    public void testRunCommandOnClick() {
        runCommand.onClick(menuClick);
        verify(runCommand).onClick(menuClick);
    }

    @Test
    public void testRunCommandCommandToRun() {
        assertEquals(testCommand, runCommand.commandToRun());
    }

    @Test
    public void testRunCommandCloseOnClickDefault() {
        assertTrue(runCommand.closeOnClick());
    }

    @Test
    public void testRunCommandCloseOnClickFalse() {
        assertFalse(runCommandFalse.closeOnClick());
    }
}
