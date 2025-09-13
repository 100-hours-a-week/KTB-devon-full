package org.example.app;

import org.example.view.InputView;
import org.example.view.OutputView;

public class Application {

    private final InputView inputView = new InputView();
    private final OutputView outputView = new OutputView();


    public void run(){

        outputView.printGameStart();



    }
}
