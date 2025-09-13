package org.example.domain.character;


public abstract class Character {
    protected String name;
    protected String type;
    protected int level;
    protected int health;
    protected int maxHealth;
    protected int attack;
    protected int experience;

    public Character(String name, String type, int health, int attack) {
        this.name = name;
        this.type = type;
        this.level = 1;
        this.health = health;
        this.maxHealth = health;
        this.attack = attack;
        this.experience = 0;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    public void heal(int amount) {
        this.health += amount;
    }

    public void gainExperience(int exp) {
        this.experience += exp;
        if (this.experience >= 100) {
            levelUp();
        }
    }

    private void levelUp() {
        this.level++;
        this.experience -= 100;

        int healthIncrease = 20;
        int attackIncrease = 5;

        this.maxHealth += healthIncrease;
        this.health = healthIncrease;
        this.attack += attackIncrease;
    }

    public boolean isAlive() {
        return this.health > 0;
    }

    public int getDamage() {
        return this.attack;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public int getLevel() {
        return this.level;
    }

    public int getHealth() {
        return this.health;
    }

    public int getAttack() {
        return this.attack;
    }

}