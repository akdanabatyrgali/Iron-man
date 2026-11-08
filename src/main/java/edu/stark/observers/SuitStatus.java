package edu.stark.observers;

import edu.stark.suits.SuitType;

public record SuitStatus(SuitType type, int energy, int armor, String state) {}
