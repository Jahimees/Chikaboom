package net.chikaboom.commands;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import static net.chikaboom.constant.CommandConstant.COMMAND;

public class CommandFactory {

    private static final Logger logger = Logger.getLogger(CommandFactory.class);

    public ActionCommand defineCommand(HttpServletRequest request) {

        //TODO UnknownCommandException
            EnumCommands command;
            String currentCommand = request.getParameter(COMMAND);

            logger.info("CommandFactory: '" + currentCommand + "' command got");

            if (currentCommand == null || currentCommand.isEmpty()) {
                command = null;
            } else
                command = EnumCommands.valueOf(currentCommand.toUpperCase());

        return command.getCommand();
    }

}
