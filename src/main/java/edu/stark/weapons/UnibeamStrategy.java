package edu.stark.weapons;

import edu.stark.missions.Enemy;

public class UnibeamStrategy implements WeaponStrategy {
    @Override public WeaponType type() { return WeaponType.UNIBEAM; }
    @Override public int energyCost() { return 34; }
    @Override public int calculateDamage(Enemy enemy) { return 42; }
}
