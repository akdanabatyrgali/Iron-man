package edu.stark.commands;

import edu.stark.core.ActionResult;
import edu.stark.suits.IronManSuit;

public class ActivateCommand implements Command {
    private final IronManSuit suit;
    public ActivateCommand(IronManSuit suit) { this.suit = suit; }
    @Override public ActionResult execute() { return suit.activate(); }
}
