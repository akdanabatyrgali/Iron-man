package edu.stark.commands;

import edu.stark.core.ActionResult;
import edu.stark.suits.IronManSuit;

public class RepairCommand implements Command {
    private final IronManSuit suit;
    public RepairCommand(IronManSuit suit) { this.suit = suit; }
    @Override public ActionResult execute() { return suit.repair(); }
}
