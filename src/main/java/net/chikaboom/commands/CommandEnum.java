package net.chikaboom.commands;

/**
 * Класс-enum, состоящий из всех возможных команд, поступающих на CommandFactory
 */
public enum CommandEnum {
    EMPTY {
        {
            this.command = new EmptyCommand();
        }
    };

    ActionCommand command;

    /**
     * Метод возращает конкретную команду, в зависимоти от поступившей String на CommandFactory
     * @return конкретную команду, в зависимости от поступившей String на CommandFactory
     */
    public ActionCommand getCommand() {
        return command;
    }
}