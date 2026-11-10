package edu.stark.missions;

import edu.stark.core.ActionResult;
import edu.stark.suits.IronManSuit;
import edu.stark.weapons.WeaponStrategy;
import java.util.List;

/** Turns attacks, defense, victory and defeat into a cohesive mission. */
public class MissionManager {
    private final IronManSuit suit;
    private final List<MissionResult> history;
    private MissionScenario scenario;
    private Enemy enemy;

    public MissionManager(IronManSuit suit, List<MissionResult> history) {
        this.suit = suit;
        this.history = history;
    }
    public boolean inMission() { return enemy != null; }
    public Enemy enemy() { return enemy; }

    public ActionResult start(MissionScenario selected) {
        if (selected == null) return ActionResult.fail("Unknown mission.");
        if (inMission()) return ActionResult.fail("Finish or retreat from the current mission first.");
        ActionResult start = suit.startMission();
        if (!start.success()) return start;
        scenario = selected;
        enemy = selected.createEnemy();
        return start.add("Mission: " + selected.missionName() + " | Enemy: " + enemy.name()
                + " | HP: " + enemy.health() + ".");
    }

    public ActionResult attack(WeaponStrategy strategy) {
        if (!inMission() || !suit.canAttack()) return ActionResult.fail("Start a mission before attacking.");
        if (strategy == null) return ActionResult.fail("Select a weapon first.");
        if (!suit.supports(strategy.type())) return ActionResult.fail("This suit cannot use " + strategy.type().displayName() + ".");
        if (!suit.useEnergy(strategy.energyCost())) {
            return ActionResult.fail("Not enough energy for " + strategy.type().displayName()
                    + ". Required: " + strategy.energyCost() + ".");
        }
        int damage = strategy.calculateDamage(enemy);
        enemy.receiveDamage(damage);
        String log = strategy.type().displayName() + " hit " + enemy.name() + " for " + damage
                + " damage (-" + strategy.energyCost() + " energy). Enemy HP: " + enemy.health() + ".";
        if (!enemy.isAlive()) return conclude(true, log);
        suit.takeDamage(enemy.damage());
        log += "\n" + enemy.name() + " counterattacked for " + enemy.damage()
                + " armor damage. Remaining armor: " + suit.armor() + ".";
        if (suit.armor() == 0) return conclude(false, log);
        return ActionResult.ok(log);
    }

    public ActionResult defend() {
        if (!inMission() || !suit.canAttack()) return ActionResult.fail("Defense is only available during combat.");
        if (!suit.useEnergy(5)) return ActionResult.fail("Defense requires 5 energy.");
        int blockedDamage = (int) Math.round(enemy.damage() * (1.0 - suit.blockingEfficiency()));
        suit.takeDamage(blockedDamage);
        String log = suit.defenseDescription() + " reduced the incoming attack to "
                + blockedDamage + " damage (-5 energy). Remaining armor: " + suit.armor() + ".";
        if (suit.armor() == 0) return conclude(false, log);
        return ActionResult.ok(log);
    }

    public ActionResult retreat() {
        if (!inMission()) return ActionResult.fail("No ongoing mission to retreat from.");
        return conclude(false, "Tactical retreat initiated.");
    }

    private ActionResult conclude(boolean won, String log) {
        history.add(new MissionResult(scenario.missionName(), suit.type(), won));
        if (suit.armor() > 0) suit.endMission(); // otherwise State is DAMAGED
        enemy = null;
        scenario = null;
        return ActionResult.ok(log + "\nMission " + (won ? "SUCCESS!" : "FAILED.")
                + " Current suit state: " + suit.stateName() + ".");
    }
}
