package common;

import type.List;
import type.None;
import type.Type;
import type.Word;

import java.util.regex.Pattern;

public class Common {
    public static final double EPS = 1e-8;
    static final boolean True = true;
    static final boolean False = false;
    static final String[] occipied = {"make", "thing", "word", "erase", "number", "list", "bool", "isname", "print",
            "read", "readlist", "add", "sub", "mul", "div", "mod", "eq", "gt", "lt", "and", "or", "not", "repeat","random",
            "sqrt","run","output","stop","export","if"};
    static final String[] operator = {"make", "thing", "erase", "isname", "print",
            "read", "readlist", "add", "sub", "mul", "div", "mod", "eq", "gt", "lt", "and", "or", "not", "repeat",
            "int", "isnumber", "isword", "islist", "isbool", "isempty","random","sqrt","run","output", "stop","export","if"};
    static final int[] slotNum = {2, //make
            1, //thing
            1, //erase
            1, //isname
            1, //print
            0, //read
            0, //readlist
            2, //add
            2, //sub
            2, //mul
            2, //div
            2, //mod
            2, //eq
            2, //gt
            2, //lt
            2, //and
            2, //or
            1, //not
            2, //repeat
            1, //int,
            1, //isnumber
            1, //isword
            1, //islist
            1, //isbool
            1, //isempty
            1, //random
            1, //sqrt
            1, //run
            1, //output
            0, //stop
            0, //export
            3, //if
    };
    private static final double PI = 3.14159;

    static public int getSlotNum(String str) {
        for (int i = 0; i < operator.length; i++) {
            if (str.equals(operator[i])) return slotNum[i];
        }
        return -1;
    }

    static public boolean isZero(double x) {
        return Math.abs(x) <= EPS;
    }

    static public boolean isNumber(Type input) {
        return input.isNumber();
    }

    public static boolean isInteger(Type input) throws Exception {
        if (!input.isNumber()) return false;
        return isZero((int)((Word) input).getNumber()-((Word) input).getNumber());
    }

    static public boolean isList(Type input) {
        return input.isList();
    }

    static public boolean isWord(Type input) {
        return input.isWord();
    }

    static public boolean isBool(Type input) {
        return input.isBool();
    }

    static public boolean isNone(Type input) {
        return input.getTypeCode() == -1;
    }

    static public Type getConstant(String constName) throws Exception {
        if (constName.equals("PI")) {
            return new Word(PI);
        }
        if (constName.equals("true") || constName.equals("false")) {
            return new Word(constName.equals("true"));
        }
        return new None();
    }

    static public boolean matchNumber(String str) {
        return Pattern.matches("^-?[0-9]+(.[0-9]+)?$", str);
    }

    public static boolean isOccupied(String varStr) {
        for (String str : occipied) {
            if (str.equals(varStr)) return true;
        }
        return false;
    }

    public static boolean isRunnable(Type input){
        if (!input.isList()) return false;
        List curList = (List) input;
        if ((curList.getLength() != 2)) return false;
        if (!curList.getFirst().isList()) return false;
        return curList.getLast().isList();
    }
}