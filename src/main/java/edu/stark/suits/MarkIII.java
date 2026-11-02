package edu.stark.suits;

import edu.stark.weapons.WeaponType;
import java.util.EnumSet;

public class MarkIII extends IronManSuit {
    public MarkIII() {
        super(SuitType.MARK_III, 100, 120, "J.A.R.V.I.S.",
                EnumSet.of(WeaponType.REPULSOR, WeaponType.MISSILE, WeaponType.UNIBEAM));
    }
}
