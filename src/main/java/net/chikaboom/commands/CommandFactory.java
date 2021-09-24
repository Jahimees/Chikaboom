package net.chikaboom.commands;

import net.chikaboom.exception.UnknownCommandException;
import org.apache.log4j.Logger;

import static net.chikaboom.constant.LoggerMessageConstant.COMMAND_GOT;
import static net.chikaboom.constant.LoggerMessageConstant.COMMAND_IS_NOT_EXISTS;
/**
 * Класс, отвечающий за определение команд, поступивших на сервлет
 * Возвращает конкретную команду в соответствии с параметром, принятым из сессии "COMMAND"
 *
 * КОММЕНТАРИЙ Тоже в доку класса, а не метода ↓
 * Реализован с помощью паттерна "Фабричный метод" (Method Factory)
 */
public class CommandFactory {

    private static final Logger logger = Logger.getLogger(CommandFactory.class);

    /**
     * Метод предназначен для определения команды в зависимости от полученной строки
     * @param currentCommand строка, содержащая название команды
     * @return экземпляр команды
     */

    public ActionCommand defineCommand(String currentCommand) throws UnknownCommandException{

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
            //return new EmptyCommand(); Пишет unreachable... тут я малек запутался с тем что throws, а что отлавливаем
            //и почему именно так, а не иначе...
        }

//        У тебя нигде не генерируется ошибка UnknownCommandException, поэтому она никогда не поймается.
//        При этом, у тебя будет постоянно падать ошибка IllegalArgumentException в строке
//        return CommandEnum.valueOf(currentCommand.toUpperCase()).getCommand();

//        Что делать?
//        1*) В блоке catch ловить не UnknownCommandException, а IllegalArgumentException.
//        2*) Внутри блока писать throw new UnknownCommandException(...);
//        3*) В сигнатуре метода дописать throws UnknownCommandException
//        4*) Отловить ошибку в месте, где вызывается метод
//        5*) И в месте, где отлавливаешь ошибку (в блоке catch) уже и попробуй создать EmptyCommand,
//        а из метода defineCommand в блоке catch - убрать.
    }
}
