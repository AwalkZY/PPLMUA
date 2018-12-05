package syntax;

import common.Common;
import type.*;
import type.Number;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

public class SyntaxStack {
    private Stack<String> instStack;
    private Stack<ArrayList<Type>> slotStack;

    private HashMap<String, Type> variable;

    public SyntaxStack() {
        variable = new HashMap<>();
        instStack = new Stack<>();
        slotStack = new Stack<>();
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
            if (ch == ' ' || ch == '\t') {
                String comStr = com.toString();
                if (!comStr.isEmpty()) {
                    int order = Common.getSlotNum(comStr);
                    if (order == -1) {
                        if (instStack.empty()) throw new Exception("Name Error: Unexpected parameter found.");
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
                if (bracePair != 0) throw new Exception("Invalid Syntax: Improper braces.");
                List newList = genList(list.toString());
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
                    else throw new Exception("Name Error : Unknown parameter detected.");
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

    private List genList(String listStr) throws Exception {
        ArrayList<Type> eleList = new ArrayList<>();
        Stack<ArrayList<Type>> eleStack = new Stack<>();
        StringBuilder eleBuilder = new StringBuilder();
        String eleStr;
        for (int i = 0; i < listStr.length(); i++) {
            char ch = listStr.charAt(i);
            if (ch == '[') {
                eleStack.push(new ArrayList<>());
                continue;
            }
            if ((ch == ' ' || ch == '\t' || ch == ']') && !eleBuilder.toString().isEmpty()) {
                eleStr = eleBuilder.toString();
                if (eleStr.charAt(0) == '"') { //word
                    eleStack.peek().add(new Word(eleStr.substring(1)));
                } else if (!Common.isNone(Common.getConstant(eleStr))) { //constant
                    eleStack.peek().add(Common.getConstant(eleStr));
                }
                if (Pattern.matches("^-?[0-9]+(.[0-9]+)?$", eleStr)) {  //number
                    eleStack.peek().add(new Number(Double.valueOf(eleStr)));
                }
                eleBuilder = new StringBuilder();
                if (ch == ']') {
                    eleList = eleStack.pop();
                    eleStack.peek().add(new List(eleList));
                }
                continue;
            }
            eleBuilder.append(ch);
        }
        return new List(eleStack.pop());
    }

    private void sortStack() throws Exception {  //整理栈内容
        if (slotStack.empty()) return;
        ArrayList<Type> slot = slotStack.peek();  //取出最顶端的slot
        while (slot.size() == Common.getSlotNum(instStack.peek())) { //如果栈顶的ArrayList已经达到要求的值
            String curInst = instStack.pop();
            slot = slotStack.pop();
            /*System.out.println(curInst);
            System.out.println(slot);*/
            Type ans = issueFun(curInst, slot);
            if (slotStack.isEmpty()) break;
            slot = slotStack.peek();
            slot.add(ans);
        }
    }

    public Type fetch(String operand) throws Exception {
        //优先判断是否是标识符，如果是，查找变量表，如果变量表内无该标识符，则查找常量表，如常量表内无该操作符，抛出异常
        /*if (variable.containsKey(operand)) {
            return variable.get(operand);
        }*/
        if (!Common.isNone(Common.getConstant(operand))) {
            return Common.getConstant(operand);
        }
        if (Common.matchNumber(operand)) {  //number
            return new Number(Double.valueOf(operand));
        }
        throw new Exception("Name Error: Unknown parameter or operation detected.");
    }

    public Type issueFun(String operator, ArrayList<Type> slot) throws Exception {
        switch (operator) {
            case "make":
                return make(slot);
            case "thing":
                return thing(slot);
            case "erase":
                return erase(slot);
            case "print":
                return print(slot);
            case "read":
                return read(slot);
            case "readlinst":
                return readlinst(slot);
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
        }
        return new None();
    }

    private Type isbool(ArrayList<Type> slot) throws Exception {
        assert slot.size() == 1;
        return new Bool(Common.isBool(slot.get(0)));
    }

    private Type isempty(ArrayList<Type> slot) throws Exception {
        assert slot.size() == 1;
        Type para = slot.get(0);
        if (Common.isWord(para)) {
            return new Bool(((Word)(para)).get().isEmpty());
        }
        if (Common.isList(para)) {
            return new Bool(((List)(para)).isEmpty());
        }
        throw new Exception("Type Error: this data type is improper for isempty operation");
    }

    private Type islist(ArrayList<Type> slot) throws Exception {
        assert slot.size() == 1;
        return new Bool(Common.isList(slot.get(0)));
    }

    private Type isnumber(ArrayList<Type> slot) throws Exception {
        assert slot.size() == 1;
        return new Bool(Common.isNumber(slot.get(0)));
    }

    private Type isword(ArrayList<Type> slot) throws Exception {
        assert slot.size() == 1;
        return new Bool(Common.isWord(slot.get(0)));
    }

    private Type floor(ArrayList<Type> slot) throws Exception {
        assert slot.size() == 1;
        Type para = slot.get(0);
        if (!Common.isNumber(para)) throw new Exception("Type Error: this data type is improper for floor operation");
        double ans = ((Number) para).get().intValue();
        return new Number(ans);
    }

    private Type calBool(ArrayList<Type> slot, String operator) throws Exception {
        if (operator.equals("not")) {
            assert slot.size() == 1;
            Type para = slot.get(0);
            if (!Common.isBool(para)) throw new Exception("Type Error: this data type is improper for bool operation");
            return new Bool(!((Bool) (para)).get());
        }
        assert slot.size() == 2;
        Type para1 = slot.get(0);
        Type para2 = slot.get(1);
        if (!Common.isBool(para1) || !Common.isBool(para2))
            throw new Exception("Type Error: this data type is improper for bool operation");
        if (operator.equals("and")) return new Bool(((Bool) (para1)).get() && ((Bool) (para2)).get());
        if (operator.equals("or")) return new Bool(((Bool) (para1)).get() || ((Bool) (para2)).get());
        return new None();
    }

    private Type compare(ArrayList<Type> slot, String operator) throws Exception {
        assert slot.size() == 2;
        Type para1 = slot.get(0);
        Type para2 = slot.get(1);
        if (para1.getTypeCode() != para2.getTypeCode() || (!Common.isWord(para1) && !Common.isNumber(para1)) || (!Common.isWord(para2) && !Common.isNumber(para2)))
            throw new Exception("Type Error: This kind of data can't be compared.");
        if (Common.isNumber(para1)) {
            double num1 = ((Number) (para1)).get();
            double num2 = ((Number) (para2)).get();
            if (operator.equals("eq") && num1 == num2) return new Bool(true);
            if (operator.equals("lt") && num1 < num2) return new Bool(true);
            if (operator.equals("gt") && num1 > num2) return new Bool(true);
            return new Bool(false);
        }
        if (Common.isWord(para1)) {
            String str1 = ((Word) (para1)).get();
            String str2 = ((Word) (para2)).get();
            if (operator.equals("eq") && str1.compareTo(str2) == 0) return new Bool(true);
            if (operator.equals("lt") && str1.compareTo(str2) < 0) return new Bool(true);
            if (operator.equals("gt") && str1.compareTo(str2) > 0) return new Bool(true);
            return new Bool(false);
        }
        return new Bool(false);
    }


    private Type isname(ArrayList<Type> slot) throws Exception {
        assert slot.size() == 1;
        Type para = slot.get(0);
        if (!Common.isWord(para)) return new Bool(false);
        if (variable.containsKey(para.toString())) return new Bool(true);
        return new Bool(false);
    }

    private Type readlinst(ArrayList<Type> slot) {
        return new None();
    }

    private Type read(ArrayList<Type> slot) throws Exception {
        assert slot.size() == 0;
        Scanner in = new Scanner(System.in);
        System.out.println("<<<");
        String str = in.next();
        if (Common.matchNumber(str)) return new Number(Double.valueOf(str));
        if (str.equals("True") || str.equals("False")) return new Bool(str.equals("True"));
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
        if (!Common.isWord(para1)) throw new Exception("Syntax Error: improper parameter for make operation!");
        String varStr = ((Word) (para1)).get();
        variable.put(varStr, para2);
        return new None();
    }

    private Type erase(ArrayList<Type> slot) throws Exception {
        assert slot.size() == 1;
        Type para = slot.get(0);
        if (!Common.isWord(para)) throw new Exception("Type Error: improper parameter for erase operation");
        variable.remove(para.toString());
        return new None();
    }

    private Type thing(ArrayList<Type> slot) throws Exception {
        assert slot.size() == 1;
        Type para = slot.get(0);
        if (!Common.isWord(para)) throw new Exception("Type Error: improper parameter for thing operation!");
        String varStr = ((Word) (para)).get();
        if (variable.containsKey(varStr)) return variable.get(varStr);
        throw new Exception("Name Error: Unknown variable name detected!");
    }

    private Type calNum(ArrayList<Type> slot, String operation) throws Exception {
        assert slot.size() == 2;
        Type para1 = slot.get(0);
        Type para2 = slot.get(1);
        if (!Common.isNumber(para1) || !Common.isNumber(para2))
            throw new Exception("Type Error: this data type is improper for add operation");
        if (operation.equals("add")) return new Number((Double) para1.get() + (Double) para2.get());
        if (operation.equals("sub")) return new Number((Double) para1.get() - (Double) para2.get());
        if (operation.equals("mul")) return new Number((Double) para1.get() * (Double) para2.get());
        double divisor = (Double) para2.get();
        if (Math.abs(divisor) < Common.EPS) throw new Exception("Zero Division Error : Divided by zero.");
        if (operation.equals("div")) return new Number((Double) para1.get() / (Double) para2.get());
        if (operation.equals("mod")) return new Number((Double) para1.get() % (Double) para2.get());
        return new None();
    }
}
