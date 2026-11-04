package edu.stark.weapons;

public final class WeaponCatalog {
    private WeaponCatalog() {}
    public static WeaponStrategy select(WeaponType type) {
        return switch (type) {
            case REPULSOR -> new RepulsorStrategy();
            case MISSILE -> new MissileStrategy();
            case NANO_BLADE -> new NanoBladeStrategy();
            case UNIBEAM -> new UnibeamStrategy();
        };
    }
}