package common;

import java.util.Scanner;

public class InputSource {
    private static Scanner in = new Scanner(System.in);

    private static String hideComment(String command) {
        return command.replaceAll("\\s//.*", "");
    }

    public static String nextLine(){
        return hideComment(in.nextLine());
    }

    public static String next(){
        return in.next();
    }
}
