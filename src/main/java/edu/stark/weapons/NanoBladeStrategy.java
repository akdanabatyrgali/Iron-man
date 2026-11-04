package edu.stark.weapons;

import edu.stark.missions.Enemy;

public class NanoBladeStrategy implements WeaponStrategy {
    @Override public WeaponType type() { return WeaponType.NANO_BLADE; }
    @Override public int energyCost() { return 18; }
    @Override public int calculateDamage(Enemy enemy) { return enemy.isArmored() ? 33 : 27; }
}
