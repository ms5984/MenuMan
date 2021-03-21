package com.github.ms5984.lib.menuman;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClickActionTest {

    @Mock
    MenuClick menuClick;
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

    @Test
    void testOnClick(@Mock ClickAction clickAction) {
        clickAction.onClick(menuClick);
        verify(clickAction).onClick(menuClick);
    }

    @Test
    void testRunCommandOnClick() {
        doNothing().when(runCommand).onClick(any());
        runCommand.onClick(menuClick);
        verify(runCommand).onClick(menuClick);
    }

    @Test
    void testRunCommandCommandToRun() {
        assertEquals(testCommand, runCommand.commandToRun());
    }

    @Test
    void testRunCommandCloseOnClickDefault() {
        assertTrue(runCommand.closeOnClick());
    }

    @Test
    void testRunCommandCloseOnClickFalse() {
        assertFalse(runCommandFalse.closeOnClick());
    }
}
