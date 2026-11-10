package edu.stark.commands;

import edu.stark.core.ActionResult;
import edu.stark.suits.IronManSuit;

public class FlyCommand implements Command {
    private final IronManSuit suit;
    public FlyCommand(IronManSuit suit) { this.suit = suit; }
    @Override public ActionResult execute() { return suit.fly(); }
}
