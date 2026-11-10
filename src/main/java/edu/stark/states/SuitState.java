package edu.stark.states;

import edu.stark.core.ActionResult;
import edu.stark.suits.IronManSuit;

public interface SuitState {
    String name();
    ActionResult activate(IronManSuit suit);
    ActionResult startMission(IronManSuit suit);
    ActionResult endMission(IronManSuit suit);
    ActionResult fly(IronManSuit suit);
    ActionResult recharge(IronManSuit suit);
    ActionResult repair(IronManSuit suit);
    boolean canAttack();
}
