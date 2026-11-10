package edu.stark.states;

import edu.stark.core.ActionResult;
import edu.stark.suits.IronManSuit;

public class DamagedState extends BaseSuitState {
    @Override public String name() { return "DAMAGED"; }
    @Override public ActionResult recharge(IronManSuit suit) {
        suit.restoreEnergy();
        return ActionResult.ok("Reactor recharged, but armor still requires repair.");
    }
    @Override public ActionResult repair(IronManSuit suit) {
        suit.restoreArmor();
        suit.setState(new StandbyState());
        return ActionResult.ok("Armor restored. Suit returned to STANDBY: activate it again.");
    }
}
