package org.example.view;

public class OutputView {

    public void printGameStart() {
        System.out.println("====================================");
        System.out.println("        RPG 게임 - 던전 탐색");
        System.out.println("====================================");
        System.out.println("10스테이지의 던전을 탐색해보세요!");
        System.out.println();
    }

    public void printCharacterSelection() {
        System.out.println("캐릭터를 선택하세요:");
    }

    public void printCharacterStatus() {

    }

    public void printEventSelection() {

    }

    public void printInventory() {

    }

    public void printGameClear() {
        System.out.println("\n게임 클리어!");
    }

    public void printGameOver() {
        System.out.println("\n게임 오버");
    }

    public void printGameEnd() {
        System.out.println("\n게임을 종료합니다");
    }

    public void printInvalidInput() {
        System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
    }

    public void printError(String message) {
        System.out.println("오류: " + message);
    }
}
