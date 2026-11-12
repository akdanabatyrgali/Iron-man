package edu.stark.web;
import java.util.List;

public record CatalogResponse (
        List<SuitOption> suits,
        List<WeaponOption> weapons,
        List<MissionOption> missions
) {

    public record SuitOption(
            String id,
            String name,
            int energy,
            int armor,
            String assistant
    ) {}

    public record WeaponOption(
            String id,
            String name,
            int energyCost
    ) {}

    public record MissionOption(
            String id,
            String name,
            String enemy,
            int enemyHealth,
            boolean armored
    ) {}
}
