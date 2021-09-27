package net.chikaboom.commands;

import net.chikaboom.exception.UnknownCommandException;
import org.apache.log4j.Logger;

import static net.chikaboom.constant.LoggerMessageConstant.COMMAND_GOT;
import static net.chikaboom.constant.LoggerMessageConstant.COMMAND_IS_NOT_EXISTS;

/**
 * Класс, отвечающий за определение команд, поступивших на сервлет
 * Возвращает конкретную команду в соответствии с параметром, принятым из сессии "COMMAND"
 *
 * Реализован с помощью паттерна "Фабричный метод" (Method Factory)
 */
public class CommandFactory {
    private static final Logger logger = Logger.getLogger(CommandFactory.class);

    /**
     * Метод предназначен для определения команды в зависимости от полученной строки
     * @param currentCommand строка, содержащая название команды
     * @return экземпляр команды
     */
    public ActionCommand defineCommand(String currentCommand) throws UnknownCommandException {

        logger.info(currentCommand + COMMAND_GOT);

        try {
            if (currentCommand == null || currentCommand.isEmpty()) {
                return new EmptyCommand();
            } else {
                return CommandEnum.valueOf(currentCommand.toUpperCase()).getCommand();
            }
        } catch (IllegalArgumentException ex) {
            logger.error(COMMAND_IS_NOT_EXISTS, ex);
            throw new UnknownCommandException(COMMAND_IS_NOT_EXISTS);
        }
    }
}
