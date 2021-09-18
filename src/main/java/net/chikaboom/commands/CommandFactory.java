package net.chikaboom.commands;

import net.chikaboom.exception.UnknownCommandException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import static net.chikaboom.constant.AttributeConstant.COMMAND;
import static net.chikaboom.constant.LoggerMessages.GOT;
import static net.chikaboom.constant.LoggerMessages.ILLEGAL_COMMAND;

public class CommandFactory {

    private static final Logger logger = Logger.getLogger(CommandFactory.class);

    public ActionCommand defineCommand(HttpServletRequest request) {
        String currentCommand = request.getParameter(COMMAND);

        logger.info(currentCommand + GOT);

        try {
            if (currentCommand == null || currentCommand.isEmpty()) {
                return new EmptyCommand();
            } else {
                return CommandEnum.valueOf(currentCommand.toUpperCase()).getCommand();
            }
        } catch (UnknownCommandException ex) {
            logger.error(ILLEGAL_COMMAND, ex);
            return new EmptyCommand();
        }
    }
}
