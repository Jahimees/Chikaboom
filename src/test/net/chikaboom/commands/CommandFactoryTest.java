package net.chikaboom.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommandFactoryTest {

    @Test
    void checkDefineCommand() {

        String testCommandNone = "";
        String testCommandNull = null;
        // + String testCommandEmptyCommand = "Empty_command";

        CommandFactory commandFactory = new CommandFactory();

        Class actual = commandFactory.defineCommand(testCommandNone).getClass(); //переименуй actual1
        Class expected = new EmptyCommand().getClass(); //переименуй expected1

        Class actual1 = commandFactory.defineCommand(testCommandNull).getClass(); //переименуй actual2
        Class expected1 = new EmptyCommand().getClass(); //переименуй expected2

        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expected1, actual1);
// лишняя строка
    }
}