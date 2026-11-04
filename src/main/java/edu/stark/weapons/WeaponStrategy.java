package edu.stark.weapons;

import edu.stark.missions.Enemy;

public interface WeaponStrategy {
    WeaponType type();
    int energyCost();
    int calculateDamage(Enemy enemy);
}
