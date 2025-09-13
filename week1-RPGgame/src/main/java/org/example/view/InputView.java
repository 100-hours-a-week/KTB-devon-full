package org.example.view;

import java.util.Scanner;

public class InputView {
    private Scanner scanner;

    public InputView() {
        this.scanner = new Scanner(System.in);
    }

    public int getInputInt() {
        return scanner.nextInt();
    }

    public String getInputString() {
        scanner.nextLine();
        return scanner.nextLine();
    }
}
