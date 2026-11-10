package edu.stark.missions;

public enum MissionScenario {
    MONACO(1, "Monaco Emergency", "Whiplash", 80, 18, false),
    EXPO(2, "Stark Expo Defense", "Hammer Drone", 110, 21, true),
    TITAN(3, "Titan Rescue", "Alien Vanguard", 150, 26, true);

    private final int number;
    private final String name;
    private final String enemyName;
    private final int enemyHealth;
    private final int enemyDamage;
    private final boolean armored;

    MissionScenario(int number, String name, String enemyName, int enemyHealth,
                    int enemyDamage, boolean armored) {
        this.number = number;
        this.name = name;
        this.enemyName = enemyName;
        this.enemyHealth = enemyHealth;
        this.enemyDamage = enemyDamage;
        this.armored = armored;
    }
    public int number() { return number; }
    public String missionName() { return name; }
    public Enemy createEnemy() { return new Enemy(enemyName, enemyHealth, enemyDamage, armored); }
    public static MissionScenario fromNumber(int number) {
        for (MissionScenario scenario : values()) {
            if (scenario.number == number) return scenario;
        }
        return null;
    }
}
