package edu.stark.mission;

import edu.stark.suits.SuitType;

public record MissionResult(String mission, SuitType suit, boolean success) {
    @Override public String toString() {
        return mission + " | " + suit.displayName() + " | " + (success ? "SUCCESS" : "FAILED");
    }
}
