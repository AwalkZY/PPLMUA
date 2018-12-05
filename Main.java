import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;
import common.*;
import syntax.SyntaxStack;

public class Main {

    SyntaxStack syntax = new SyntaxStack();

    public Main() {}

    private String hideComment(String command) {
        return command.replaceAll("//.*","");
    }

    public void acceptCommand(String command) throws Exception {
        String newCom = hideComment(command).trim();
        System.out.println(newCom);
        if (!newCom.isEmpty())
            syntax.interprete(hideComment(command));
    }

    public static void main(String[] args) {
		Main cli = new Main();
		String command;
		
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in); 
		while (true) {
            System.out.print(">>>");
			command = in.nextLine();
            try {
                cli.acceptCommand(command);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
	}
}
