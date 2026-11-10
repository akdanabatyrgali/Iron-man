package edu.stark.suits;

import edu.stark.weapons.WeaponType;
import java.util.EnumSet;

public class MarkL extends IronManSuit {
    public MarkL() {
        super(SuitType.MARK_L, 160, 150, "F.R.I.D.A.Y.",
                EnumSet.of(WeaponType.REPULSOR, WeaponType.NANO_BLADE, WeaponType.UNIBEAM));
    }
    @Override public double blockingEfficiency() { return 0.75; }
    @Override public String defenseDescription() { return "Nanotech shield"; }
}