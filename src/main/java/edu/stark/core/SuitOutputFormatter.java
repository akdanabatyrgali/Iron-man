package edu.stark.core;

import edu.stark.missions.Enemy;
import edu.stark.missions.MissionResult;
import edu.stark.suits.IronManSuit;
import edu.stark.weapons.WeaponStrategy;
import edu.stark.weapons.WeaponType;
import java.util.List;
import java.util.stream.Collectors;

public final class SuitOutputFormatter {
    public String status(IronManSuit suit, WeaponStrategy weapon, Enemy enemy) {
        String result = "Suit: " + suit.name() + "\nAssistant: " + suit.assistant()
                + "\nState: " + suit.stateName()
                + "\nEnergy: " + suit.energy() + "/" + suit.maxEnergy()
                + "\nArmor: " + suit.armor() + "/" + suit.maxArmor()
                + "\nWeapon: " + weapon.type().displayName()
                + "\nAvailable weapons: " + suit.supportedWeapons().stream()
                .map(WeaponType::displayName)
                .collect(Collectors.joining(", "));
        if (enemy != null) {
            result += "\nEnemy: " + enemy.name() + " (HP "
                    + enemy.health() + "/" + enemy.maxHealth() + ")";
        }
        return result;
    }

    public String history(List<MissionResult> results) {
        if (results.isEmpty()) {
            return "No completed missions yet.";
        }
        StringBuilder text = new StringBuilder("MISSION HISTORY\n");
        for (int i = 0; i < results.size(); i++) {
            text.append(i + 1).append(". ").append(results.get(i)).append('\n');
        }
        return text.toString().stripTrailing();
    }
}
