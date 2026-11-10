package edu.stark.commands;

import edu.stark.core.ActionResult;
import edu.stark.missions.MissionSession;
import edu.stark.weapons.WeaponStrategy;

public class AttackCommand implements Command {
    private final MissionSession mission;
    private final WeaponStrategy weapon;
    public AttackCommand(MissionSession mission, WeaponStrategy weapon) {
        this.mission = mission;
        this.weapon = weapon;
    }
    @Override public ActionResult execute() { return mission.attack(weapon); }
}
