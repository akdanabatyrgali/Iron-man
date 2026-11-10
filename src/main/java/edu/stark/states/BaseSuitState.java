package edu.stark.states;

import edu.stark.core.ActionResult;
import edu.stark.suits.IronManSuit;

public abstract class BaseSuitState implements SuitState {
    private ActionResult denied(String action) {
        return ActionResult.fail(action + " is not allowed while suit is " + name() + ".");
    }
    @Override public ActionResult activate(IronManSuit suit) { return denied("Activation"); }
    @Override public ActionResult startMission(IronManSuit suit) { return denied("Starting a mission"); }
    @Override public ActionResult endMission(IronManSuit suit) { return denied("Ending a mission"); }
    @Override public ActionResult fly(IronManSuit suit) { return denied("Flying"); }
    @Override public ActionResult recharge(IronManSuit suit) { return denied("Recharging"); }
    @Override public ActionResult repair(IronManSuit suit) { return denied("Repairing"); }
    @Override public boolean canAttack() { return false; }
}
