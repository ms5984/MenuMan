package com.github.ms5984.lib.menuman;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
class CloseActionTest {

    @Test
    void testOnClose(@Mock MenuClose testClose) {
        CloseAction closeAction = close -> assertSame(testClose, close);
        closeAction.onClose(testClose);
    }
}
