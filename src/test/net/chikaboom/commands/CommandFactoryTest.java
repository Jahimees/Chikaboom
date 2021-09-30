package net.chikaboom.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommandFactoryTest {

    @Test
    void checkDefineCommand() {

        String testCommandNone = "";
        String testCommandNull = null;

        CommandFactory commandFactory = new CommandFactory();

        Class actual = commandFactory.defineCommand(testCommandNone).getClass();
        Class expected = new EmptyCommand().getClass();

        Class actual1 = commandFactory.defineCommand(testCommandNull).getClass();
        Class expected1 = new EmptyCommand().getClass();

        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expected1, actual1);

    }
}