package net.chikaboom.commands;

public enum CommandEnum {
    EMPTY{
        {
            this.command = new EmptyCommand();
        }
    };

    ActionCommand command;

    public ActionCommand getCommand() {
        return command;
    }
}