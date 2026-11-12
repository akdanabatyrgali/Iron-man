package edu.stark.core;

import java.util.List;

/** Immutable view of the current in-memory simulation, safe to serialize to JSON. */
public record GameSnapshot(
        boolean selected,
        String suitType,
        String suitName,
        String assistant,
        String state,
        int energy,
        int maxEnergy,
        int armor,
        int maxArmor,
        String weaponType,
        String weaponName,
        List<String> supportedWeapons,
        boolean inMission,
        String missionType,
        String missionName,
        String enemyName,
        int enemyHealth,
        int enemyMaxHealth,
        boolean enemyArmored) {

    public static GameSnapshot empty() {
        return new GameSnapshot(false, null, null, null, "NONE",
                0, 0, 0, 0, null, null, List.of(), false,
                null, null, null, 0, 0, false);
    }
}
