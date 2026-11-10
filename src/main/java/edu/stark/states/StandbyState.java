package edu.stark.states;

import edu.stark.core.ActionResult;
import edu.stark.suits.IronManSuit;

public class StandbyState extends BaseSuitState {
    @Override public String name() { return "STANDBY"; }
    @Override public ActionResult activate(IronManSuit suit) {
        if (suit.energy() == 0) return ActionResult.fail("Charge the reactor before activation.");
        suit.setState(new ActiveState());
        return ActionResult.ok(suit.name() + " activated. All systems online.");
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
