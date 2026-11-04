package edu.stark.weapons;

public enum WeaponType {
    REPULSOR("Repulsor"), MISSILE("Missile"), NANO_BLADE("Nanotech Blade"), UNIBEAM("Unibeam");

    private final String displayName;
    WeaponType(String displayName) { this.displayName = displayName; }
    public String displayName() { return displayName; }
}
