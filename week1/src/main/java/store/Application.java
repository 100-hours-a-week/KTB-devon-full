package store;

import store.controller.Mart;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Application {
    public static void main(String[] args) {
        // 한국어가 안먹혀서 강제로...
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
            System.setErr(new PrintStream(System.err, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.err.println("UTF-8 encoding not supported");
        }

        Mart app = new Mart();
        app.start();
    }
}
