package edu.stark.commands;

import edu.stark.core.ActionResult;

/** Invoker is independent of the concrete operation and its receiver. */
public class CommandInvoker {
    public ActionResult executeCommand(Command command) { return command.execute(); }
}
