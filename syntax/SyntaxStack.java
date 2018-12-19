package syntax;

import common.Common;
import type.List;
import type.None;
import type.Type;
import type.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

public class SyntaxStack {
    private Stack<String> instStack;
    private Stack<ArrayList<Type>> slotStack;
    private HashMap<String, Type> function;
    private HashMap<String, Type> variable;

    public SyntaxStack() {
        variable = new HashMap<>();
        function = new HashMap<>();
        instStack = new Stack<>();
        slotStack = new Stack<>();
    }

    public boolean isEmpty() {
        return instStack.isEmpty();
    }

    public void interprete(String command) throws Exception {
        StringBuilder com = new StringBuilder();
        StringBuilder list;
        StringBuilder word;
        int bracePair = 0;
        int index = 0;
        char ch;
        command = command.concat(" ");
        while (index < command.length()) {
            ch = command.charAt(index);
            if ((Pattern.matches("\\s*",command.substring(index,index+1)))) {
                String comStr = com.toString();
                if (!comStr.isEmpty()) {
                    int order = Common.getSlotNum(comStr);
                    if (order == -1) {
                        if (instStack.empty())
                            throw new Exception("Name Error: Unexpected parameter or operator found.");
                        slotStack.peek().add(fetch(comStr));
                    } else {
                        instStack.push(comStr);
                        slotStack.push(new ArrayList<Type>());
                    }
                    com = new StringBuilder();
                }
            } //end of issuing
            else if (ch == '[') {  //here we generate a list
                list = new StringBuilder();
                list.append(ch);
                bracePair++;
                while (bracePair != 0) {
                    index++;
                    if (index >= command.length()) break;
                    ch = command.charAt(index);
                    if (ch == '[') bracePair++;
                    if (ch == ']') bracePair--;
                    list.append(ch);
                }
                if (bracePair != 0) throw new Exception("Invalid Syntax: Unmatched braces.");
                String ans = list.toString();
                ans = ans.substring(1,ans.length()-1);
                List newList = new List(ans);
                System.out.println("Len"+newList.length());
                System.out.println("Get"+newList.get());
                System.out.println("1st"+newList.getFirst());
                System.out.println("Last"+newList.getLast());
                System.out.println(Common.isRunnable(newList));
                slotStack.peek().add(newList);
            }  //end of list
            else if (ch == '"' || ch == ':') {
                boolean flag = ch == ':';
                word = new StringBuilder();
                while (true) {
                    index++;
                    if (index >= command.length()) break;
                    ch = command.charAt(index);
                    if (ch == ' ' || ch == '\t') break;
                    word.append(ch);
                }
                if (flag)
                    if (variable.containsKey(word.toString()))
                        slotStack.peek().add(variable.get(word.toString()));
                    else throw new Exception("Name Error : Unknown parameter detected!");
                else {
                    Word newWord = new Word(word.toString());
                    slotStack.peek().add(newWord);
                }
            }  //end of word
            else com.append(command.charAt(index));
            sortStack();
            index++;
        }  //end of while-loop
    }

    private void sortStack() throws Exception {
        if (slotStack.empty()) return;
        ArrayList<Type> slot = slotStack.peek();
        while (slot.size() == Common.getSlotNum(instStack.peek())) {
            String curInst = instStack.pop();
            slot = slotStack.pop();
            Type ans = issueFun(curInst, slot);
            if (slotStack.isEmpty()) break;
            slot = slotStack.peek();
            slot.add(ans);
        }
    }

    private Type fetch(String operand) throws Exception {
        /*if (variable.containsKey(operand)) {
            return variable.get(operand);
        }*/
        if (!Common.isNone(Common.getConstant(operand))) {
            return Common.getConstant(operand);
        }
        if (Common.matchNumber(operand)) {  //number
            return new Word(Double.valueOf(operand));
        }
        throw new Exception("Name Error: Unknown parameter or operation detected!");
    }

    private Type issueFun(String operator, ArrayList<Type> slot) throws Exception {
        for (Type type : slot) {
            if (Common.isNone(type))
                throw new Exception("Type Error: Invalid data type for " + operator + " operation!");
        }
        switch (operator) {
            case "make":
                return make(slot);
            case "thing":
                return thing(slot);
            case "repeat":
                return repeat(slot);
            case "erase":
                return erase(slot);
            case "print":
                return print(slot);
            case "read":
                return read(slot);
            case "readlist":
                return readlist(slot);
            case "add":
            case "sub":
            case "mul":
            case "div":
            case "mod":
                return calNum(slot, operator);
            case "eq":
            case "gt":
            case "lt":
                return compare(slot, operator);
            case "and":
            case "or":
            case "not":
                return calBool(slot, operator);
            case "isname":
                return isname(slot);
            case "int":
                return floor(slot);
            case "isword":
                return isword(slot);
            case "isnumber":
                return isnumber(slot);
            case "islist":
                return islist(slot);
            case "isempty":
                return isempty(slot);
            case "isbool":
                return isbool(slot);
            case "random":
                return random(slot);
            case "sqrt":
                return sqrt(slot);
        }
        return new None();
    }

    private Type repeat(ArrayList<Type> slot) throws Exception {
        assert slot.size() == 2;
        Type para1 = slot.get(0);
        Type para2 = slot.get(1);
        if (!Common.isInteger(para1) || !Common.isList(para2)) throw new Exception("Type Error: Improper type for repeat operation!");
        int times = (int)((Word)(para1)).getNumber();
        for (int i = 0; i < times; i++) interprete((String)para2.get());
        return new None();
    }

    private Type random(ArrayList<Type> slot) throws Exception {
        assert slot.size() == 1;
        Type para = slot.get(0);
        if (!Common.isNumber(para)) throw new Exception("Type Error: Improper data type for random operation");
        double paraNum = ((Word) para).getNumber();
        if (paraNum < 0) throw new Exception("Math Error: Negative parameter detected in sqrt operation!");
        return new Word(Math.floor(Math.random()*paraNum));
    }

    private Type sqrt(ArrayList<Type> slot) throws Exception {
        assert slot.size() == 1;
        Type para = slot.get(0);
        if (!Common.isNumber(para)) throw new Exception("Type Error: Improper data type for sqrt operation");
        double paraNum = ((Word) para).getNumber();
        if (paraNum < 0) throw new Exception("Math Error: Negative parameter detected in sqrt operation!");
        return new Word(Math.sqrt(paraNum));
    }

    private Type isbool(ArrayList<Type> slot) throws Exception {
        assert slot.size() == 1;
        return new Word(Common.isBool(slot.get(0)));
    }

    private Type isempty(ArrayList<Type> slot) throws Exception {
        assert slot.size() == 1;
        Type para = slot.get(0);
        if (Common.isWord(para)) {
            return new Word(((Word) (para)).get().isEmpty());
        }
        if (Common.isList(para)) {
            return new Word(((List) (para)).isEmpty());
        }
        throw new Exception("Type Error: Improper data type for isempty operation");
    }

    private Type islist(ArrayList<Type> slot) throws Exception {
        assert slot.size() == 1;
        return new Word(Common.isList(slot.get(0)));
    }

    private Type isnumber(ArrayList<Type> slot) throws Exception {
        assert slot.size() == 1;
        return new Word(Common.isNumber(slot.get(0)));
    }

    private Type isword(ArrayList<Type> slot) throws Exception {
        assert slot.size() == 1;
        return new Word(Common.isWord(slot.get(0)));
    }

    private Type floor(ArrayList<Type> slot) throws Exception {
        assert slot.size() == 1;
        Type para = slot.get(0);
        if (!Common.isNumber(para)) throw new Exception("Type Error: Improper data type for floor operation");
        double ans = ((Word) para).getNumber();
        return new Word(ans);
    }

    private Type calBool(ArrayList<Type> slot, String operator) throws Exception {
        if (operator.equals("not")) {
            assert slot.size() == 1;
            Type para = slot.get(0);
            if (!Common.isBool(para)) throw new Exception("Type Error: Improper data type for bool operation");
            return new Word(!((Word) (para)).getBool());
        }
        assert slot.size() == 2;
        Type para1 = slot.get(0);
        Type para2 = slot.get(1);
        if (!Common.isBool(para1) || !Common.isBool(para2))
            throw new Exception("Type Error: Improper data type for bool operation");
        if (operator.equals("and")) return new Word(((Word) (para1)).getBool() && ((Word) (para2)).getBool());
        if (operator.equals("or")) return new Word(((Word) (para1)).getBool() || ((Word) (para2)).getBool());
        return new None();
    }

    private Type compare(ArrayList<Type> slot, String operator) throws Exception {
        assert slot.size() == 2;
        Type para1 = slot.get(0);
        Type para2 = slot.get(1);
        if (para1.getTypeCode() != para2.getTypeCode())
            throw new Exception("Type Error: Improper data type for comparison.");
        if (Common.isNumber(para1) && Common.isNumber(para2)) {
            double num1 = ((Word) (para1)).getNumber();
            double num2 = ((Word) (para2)).getNumber();
            if (operator.equals("eq") && num1 == num2) return new Word(true);
            if (operator.equals("lt") && num1 < num2) return new Word(true);
            if (operator.equals("gt") && num1 > num2) return new Word(true);
            return new Word(false);
        }
        String str1 = ((Word) (para1)).get();
        String str2 = ((Word) (para2)).get();
        if (operator.equals("eq") && str1.compareTo(str2) == 0) return new Word(true);
        if (operator.equals("lt") && str1.compareTo(str2) < 0) return new Word(true);
        if (operator.equals("gt") && str1.compareTo(str2) > 0) return new Word(true);
        return new Word(false);
    }


    private Type isname(ArrayList<Type> slot) throws Exception {
        assert slot.size() == 1;
        Type para = slot.get(0);
        if (!Common.isWord(para)) return new Word(false);
        if (variable.containsKey(para.toString())) return new Word(true);
        if (Common.isOccupied(para.toString())) return new Word(true);
        return new Word(false);
    }

    private Type readlist(ArrayList<Type> slot) {
        assert slot.size() == 0;
        Scanner in = new Scanner(System.in);
        System.out.print("<<<");
        String str = in.nextLine();
        return new List(str);
    }

    private Type read(ArrayList<Type> slot) throws Exception {
        assert slot.size() == 0;
        Scanner in = new Scanner(System.in);
        System.out.print("<<<");
        String str = in.next();
        if (Common.matchNumber(str)) return new Word(Double.valueOf(str));
        if (str.equals("True") || str.equals("False")) return new Word(str.equals("True"));
        return new Word(str);
    }

    private Type print(ArrayList<Type> slot) {
        assert slot.size() == 1;
        Type outPara = slot.get(0);
        if (!Common.isNone(outPara)) System.out.println(outPara.toString());
        return new None();
    }

    private Type make(ArrayList<Type> slot) throws Exception {
        assert slot.size() == 2;
        Type para1 = slot.get(0);
        Type para2 = slot.get(1);
        if (!Common.isWord(para1)) throw new Exception("Syntax Error: Improper parameter for make operation!");
        String varStr = ((Word) (para1)).get();
        if (Common.isOccupied(varStr)) throw new Exception("Syntax Error: Improper parameter for make operation!");
        if ((varStr.charAt(0) >= 'a' && varStr.charAt(0) <= 'z') || (varStr.charAt(0) >= 'A' && varStr.charAt(0) <= 'Z'))
            variable.put(varStr, para2);
        else throw new Exception("Type Error: Improper data type for make operation!");
        return new None();
    }

    private Type erase(ArrayList<Type> slot) throws Exception {
        assert slot.size() == 1;
        Type para = slot.get(0);
        if (!Common.isWord(para)) throw new Exception("Type Error: Improper parameter for erase operation");
        if (Common.isOccupied(para.toString())) throw new Exception("Name Error: keyword can't be erased.");
        if (!variable.containsKey(para.toString())) throw new Exception("Name Error: Unknown parameter detected.");
        variable.remove(para.toString());
        return new None();
    }

    private Type thing(ArrayList<Type> slot) throws Exception {
        assert slot.size() == 1;
        Type para = slot.get(0);
        if (!Common.isWord(para)) throw new Exception("Type Error: Improper parameter for thing operation!");
        String varStr = ((Word) (para)).get();
        if (variable.containsKey(varStr)) return variable.get(varStr);
        throw new Exception("Name Error: Unknown variable name detected!");
    }

    private Type calNum(ArrayList<Type> slot, String operation) throws Exception {
        assert slot.size() == 2;
        Type para1 = slot.get(0);
        Type para2 = slot.get(1);
        if (!Common.isNumber(para1) || !Common.isNumber(para2))
            throw new Exception("Type Error: Improper data type for add operation");
        if (operation.equals("add")) return new Word(((Word) para1).getNumber() + ((Word) para2).getNumber());
        if (operation.equals("sub")) return new Word(((Word) para1).getNumber() - ((Word) para2).getNumber());
        if (operation.equals("mul")) return new Word(((Word) para1).getNumber() * ((Word) para2).getNumber());
        double divisor = ((Word) para2).getNumber();
        if (Math.abs(divisor) < Common.EPS) throw new Exception("Zero Division Error : Divided by zero.");
        if (operation.equals("div")) return new Word(((Word) para1).getNumber() / ((Word) para2).getNumber());
        if (operation.equals("mod")) return new Word(((Word) para1).getNumber() % ((Word) para2).getNumber());
        return new None();
    }

    public void clear() {
        instStack = new Stack<>();
        slotStack = new Stack<>();
    }
}
