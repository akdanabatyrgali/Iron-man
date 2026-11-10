package edu.stark.states;

import edu.stark.core.ActionResult;
import edu.stark.suits.IronManSuit;

public class ActiveState extends BaseSuitState {
    @Override public String name() { return "ACTIVE"; }
    @Override public ActionResult startMission(IronManSuit suit) {
        if (suit.energy() == 0) return ActionResult.fail("Not enough energy to begin a mission.");
        suit.transitionFrom(this, new CombatState());
        return ActionResult.ok("Mission started. Combat mode enabled.");
    }
    @Override public ActionResult fly(IronManSuit suit) {
        if (!suit.useEnergy(10)) return ActionResult.fail("Flight needs 10 energy points.");
        return ActionResult.ok("Flight thrusters engaged (-10 energy).");
    }
    @Override public ActionResult recharge(IronManSuit suit) {
        suit.restoreEnergy();
        return ActionResult.ok("Arc reactor recharged to " + suit.energy() + ".");
    }
    @Override public ActionResult repair(IronManSuit suit) {
        suit.restoreArmor();
        return ActionResult.ok("Armor repaired to " + suit.armor() + ".");
    }
}
