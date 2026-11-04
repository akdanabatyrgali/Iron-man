package edu.stark.weapons;

import edu.stark.missions.Enemy;

public class RepulsorStrategy implements WeaponStrategy {
    @Override public WeaponType type() { return WeaponType.REPULSOR; }
    @Override public int energyCost() { return 12; }
    @Override public int calculateDamage(Enemy enemy) { return 20; }
}