import syntax.SyntaxStack;

import java.util.Scanner;

public class Main {

    SyntaxStack syntax = new SyntaxStack();

    public Main() {
    }

    public static void main(String[] args) {
        Main cli = new Main();
        String command;

        @SuppressWarnings("resource")
        Scanner in = new Scanner(System.in);
        while (true) {
            if (cli.syntax.isEmpty()) System.out.print(">>>");
            else System.out.print("...");
            command = in.nextLine();
            try {
                cli.acceptCommand(command);
            } catch (Exception e) {
                cli.syntax.clear();
                System.out.println(e.getMessage());
            }
        }
    }

    private String hideComment(String command) {
        return command.replaceAll("\\s//.*", "");
    }

    public void acceptCommand(String command) throws Exception {
        String newCom = hideComment(command).trim();
        //System.out.println(newCom);
        if (!newCom.isEmpty())
            syntax.interprete(newCom);
    }
}
