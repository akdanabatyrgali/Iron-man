package edu.stark.states;

import edu.stark.core.ActionResult;
import edu.stark.suits.IronManSuit;

public class CombatState extends BaseSuitState {
    @Override public String name() { return "COMBAT"; }
    @Override public boolean canAttack() { return true; }
    @Override public ActionResult endMission(IronManSuit suit) {
        suit.setState(new ActiveState());
        return ActionResult.ok("Combat ended. Suit is ACTIVE.");
    }
    @Override public ActionResult fly(IronManSuit suit) {
        if (!suit.useEnergy(10)) return ActionResult.fail("Flight needs 10 energy points.");
        return ActionResult.ok("Combat flight maneuver completed (-10 energy).");
    }
}
