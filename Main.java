import common.InputSource;
import javafx.scene.shape.SVGPath;
import syntax.SyntaxStack;

import java.util.Scanner;

public class Main {

    static SyntaxStack syntax = new SyntaxStack();


    public Main() {
    }

    public static void main(String[] args) {
        Main cli = new Main();
        String command = "";
        while (true) {
            if (syntax.isEmpty()) System.out.print(">>>");
            else System.out.print("...");
            command = InputSource.nextLine();
            try {
                cli.acceptCommand(command);
            } catch (Exception e) {
                syntax.clear();
                System.out.println(e.getMessage());
            }
        }
    }

    public void acceptCommand(String command) throws Exception {
        String newCom = command.trim();
        //System.out.println(newCom);
        if (!newCom.isEmpty())
            syntax.interprete(newCom);
    }
}
