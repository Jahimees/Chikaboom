package net.chikaboom.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommandFactoryTest {

    @Test
    void checkDefineCommand() {

        String testCommandNone = "";
        String testCommandNull = null;
        String testCommandEmptyCommand = "empty_command";

        CommandFactory commandFactory = new CommandFactory();

        Class actual1 = commandFactory.defineCommand(testCommandNone).getClass();
        Class actual2 = commandFactory.defineCommand(testCommandNull).getClass();
        Class actual3 = commandFactory.defineCommand(testCommandEmptyCommand).getClass();
        Class expected = EmptyCommand.class;

        Assertions.assertEquals(expected, actual1);
        Assertions.assertEquals(expected, actual2);
        Assertions.assertEquals(expected, actual3);
    }
}