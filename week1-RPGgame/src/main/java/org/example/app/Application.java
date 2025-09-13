package org.example.app;

import org.example.view.InputView;
import org.example.view.OutputView;
import org.example.service.CharacterService;
import org.example.domain.character.Character;

public class Application {

    private final InputView inputView = new InputView();
    private final OutputView outputView = new OutputView();
    private final CharacterService characterService = new CharacterService();
    private Character player;

    public void run(){
        try {
            outputView.printGameStart();

            selectCharacter();

            if (player != null) {
                startGame();
            }

        } catch (Exception e) {
            outputView.printError(e.getMessage());
            outputView.printGameEnd();
        }
    }

    public void selectCharacter(){

        outputView.printNameInput();
        String characterName = inputView.getInputString();

        while (true) {
            try{
                outputView.printCharacterSelection();
                int select = inputView.getInputInt();
                player = characterService.createCharacter(select, characterName);
                break;
            } catch (IllegalArgumentException e){
                outputView.printInvalidInput();
            }
        }
        outputView.printCharacterStatus();
    }

    private void startGame() {
        // todo : 게임 로직
    }
}
