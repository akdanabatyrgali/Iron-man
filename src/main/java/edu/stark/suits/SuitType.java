package edu.stark.suits;

public enum SuitType {
    MARK_III("Mark III"), MARK_V("Mark V"), MARK_L("Mark L");

    private final String displayName;
    SuitType(String displayName) { this.displayName = displayName; }
    public String displayName() { return displayName; }
}
