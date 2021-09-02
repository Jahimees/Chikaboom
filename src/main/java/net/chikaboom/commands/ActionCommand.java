package net.chikaboom.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ActionCommand {

    abstract String execute(HttpServletRequest request, HttpServletResponse response);
}
