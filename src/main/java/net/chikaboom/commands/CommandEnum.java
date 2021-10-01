package net.chikaboom.commands;

/**
 * Класс-enum, состоящий из всех возможных команд, поступающих на CommandFactory
 *
 * В зависимости от принятой String из HttpServletRequest в поле command
 * осуществляется поиск команды из нижеприведенного enum и возврат соответствующей комманды,
 * "понятной" для ControllerServlet
 */
//Убрать последний абзац
public enum CommandEnum {
    EMPTY{ //пробел после EMPTY
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