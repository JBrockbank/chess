import chess.*;
import client.repl;



public class Main {

    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        new repl(serverUrl).run();
    }

}