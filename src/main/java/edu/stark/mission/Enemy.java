package edu.stark.mission;

/** An enemy in the simplified turn-based mission simulation. */
public class Enemy {
    private final String name;
    private final int maxHealth;
    private int health;
    private final int damage;
    private final boolean armored;

    public Enemy(String name, int health, int damage, boolean armored) {
        if (health <= 0 || damage < 0) throw new IllegalArgumentException("Invalid enemy stats");
        this.name = name;
        this.maxHealth = health;
        this.health = health;
        this.damage = damage;
        this.armored = armored;
    }
    public String name() { return name; }
    public int health() { return health; }
    public int maxHealth() { return maxHealth; }
    public int damage() { return damage; }
    public boolean isArmored() { return armored; }
    public boolean isAlive() { return health > 0; }
    public void receiveDamage(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Negative damage");
        health = Math.max(0, health - amount);
    }
}
