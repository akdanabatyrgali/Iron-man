package edu.stark.commands;

import edu.stark.core.ActionResult;

public class CommandInvoker {
    public ActionResult executeCommand(Command command) { return command.execute(); }
}
