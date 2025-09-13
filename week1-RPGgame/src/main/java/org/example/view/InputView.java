package org.example.view;

import java.util.Scanner;

public class InputView {
    private Scanner scanner;

    public InputView() {
        this.scanner = new Scanner(System.in);
    }

    public int getUserInput() {
        return scanner.nextInt();
    }
}
