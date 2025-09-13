package org.example.app;

import org.example.view.InputView;
import org.example.view.OutputView;

public class Application {

    private final InputView inputView = new InputView();
    private final OutputView outputView = new OutputView();


    public void run(){

        outputView.printGameStart();

        // 캐릭터 선택 예외

        // 게임 시작

        // 게임 진행



    }

    public void selectCharacter(){
        try{
            outputView.printCharacterSelection();
            int select = inputView.getUserInput();
            // 올바른 캐릭터 선택 검증

        } catch (Exception e){

        }
    }

}
