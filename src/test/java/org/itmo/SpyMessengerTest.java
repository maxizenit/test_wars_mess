package org.itmo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SpyMessengerTest {

    private SpyMessenger messenger;

    @BeforeEach
    void initMessenger() {
        messenger = new SpyMessenger();
    }

    @Test
    void testMessageSelfDestructionAfterReading() {
        messenger.sendMessage("Alice", "Bob", "Top Secret", "1234");

        assertEquals("Top Secret", messenger.readMessage("Bob", "1234"));
        assertNull(messenger.readMessage("Bob", "1234"));

        messenger.sendMessage("Alice", "Bob", "1", "1234");
        messenger.sendMessage("Alice", "Bob", "2", "1234");

        assertEquals("1", messenger.readMessage("Bob", "1234"));
        assertEquals("2", messenger.readMessage("Bob", "1234"));
    }

    @Test
    void testMessageSelfDestructionAfterDelay() throws InterruptedException {
        messenger.sendMessage("Alice", "Bob", "Top Secret", "1234");
        Thread.sleep(2000);

        assertNull(messenger.readMessage("Bob", "1234"));

        messenger.sendMessage("Alice", "Bob", "1", "1234");
        Thread.sleep(800);
        messenger.sendMessage("Alice", "Bob", "2", "1234");
        Thread.sleep(800);

        assertEquals("2", messenger.readMessage("Bob", "1234"));
    }

    @Test
    void testWrongPasscode() {
        messenger.sendMessage("Alice", "Bob", "Top Secret", "1234");

        assertNull(messenger.readMessage("Bob", "2345"));
    }

    @Test
    void testClearRedundantMessages() {
        messenger.sendMessage("Alice", "Bob", "1", "1234");
        messenger.sendMessage("Alice", "Bob", "2", "1234");
        messenger.sendMessage("Alice", "Bob", "3", "1234");
        messenger.sendMessage("Alice", "Bob", "4", "1234");
        messenger.sendMessage("Alice", "Bob", "5", "1234");
        messenger.sendMessage("Alice", "Bob", "6", "1234");
        messenger.sendMessage("Alice", "Bob", "7", "1234");

        assertEquals("3", messenger.readMessage("Bob", "1234"));
    }


}