package edu.stark.commands;

import edu.stark.core.ActionResult;
import edu.stark.suits.IronManSuit;

public class RechargeCommand implements Command {
    private final IronManSuit suit;
    public RechargeCommand(IronManSuit suit) { this.suit = suit; }
    @Override public ActionResult execute() { return suit.recharge(); }
}
