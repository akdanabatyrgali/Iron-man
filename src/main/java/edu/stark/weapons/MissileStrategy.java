package edu.stark.weapons;

import edu.stark.missions.Enemy;

public class MissileStrategy implements WeaponStrategy {
    @Override public WeaponType type() { return WeaponType.MISSILE; }
    @Override public int energyCost() { return 24; }
    @Override public int calculateDamage(Enemy enemy) { return enemy.isArmored() ? 38 : 30; }
}
