package net.chikaboom.commands;

import net.chikaboom.exception.UnknownCommandException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest; //Убрать. не используется ctrl + alt + o

import static net.chikaboom.constant.AttributeConstant.COMMAND; //Убрать. не используется   ctrl + alt + o
import static net.chikaboom.constant.LoggerMessages.GOT;
import static net.chikaboom.constant.LoggerMessages.ILLEGAL_COMMAND;

public class CommandFactory {

    //Та же ошибка. Ты пишешь, что "Класс", а описываешь доку непонятно где
    /**
     * Класс, отвечающий за определение команд, поступивших на сервлет
     * Возвращает конкретную команду в соответствии с параметром, принятым из сессии "COMMAND"
     *
     * КОММЕНТАРИЙ Тоже в доку класса, а не метода ↓
     * Реализован с помощью паттерна "Фабричный метод" (Method Factory)
     */

    private static final Logger logger = Logger.getLogger(CommandFactory.class);

    //Тут соответственно доку для метода типа "Определяет команду по входной строке и возвращает её экземпляр"
    //Для параметра String currentCommand напишешь типа "строка, которая содержит название команды"
    public ActionCommand defineCommand(String currentCommand) { //3*) Добавить throws UnknownCommandException, чтобы обрабатывать ошибку "выше" по коду

        logger.info(currentCommand + GOT);

        try {
            if (currentCommand == null || currentCommand.isEmpty()) {
                return new EmptyCommand();
            } else {
                return CommandEnum.valueOf(currentCommand.toUpperCase()).getCommand();
            }
        } catch (UnknownCommandException ex) { //1*) Отлавливать IllegalArgumentException
            logger.error(ILLEGAL_COMMAND, ex);
            //2*) Дописать throw new UnknownCommandException(...)
            return new EmptyCommand();
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
