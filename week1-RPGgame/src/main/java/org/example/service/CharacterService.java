package org.example.service;

import org.example.domain.character.Character;
import org.example.domain.character.Warrior;
import org.example.domain.character.Mage;
import org.example.domain.character.Rogue;

public class CharacterService {

    public Character createCharacter(int choice, String name) {
        switch (choice) {
            case 1:
                return new Warrior(name);
            case 2:
                return new Mage(name);
            case 3:
                return new Rogue(name);
            default:
                throw new IllegalArgumentException("잘못된 캐릭터 선택입니다.");
        }
    }

    public void processExperience(Character character, int exp) {
        int currentLevel = character.getLevel();
        character.gainExperience(exp);
        int newLevel = character.getLevel();
    }

    public void processBattle(Character character, int damage) {
        character.takeDamage(damage);
    }

    public boolean isCharacterAlive(Character character) {
        return character.isAlive();
    }
}