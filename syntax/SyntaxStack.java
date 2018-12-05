package syntax;

import common.Common;
import type.*;
import type.Number;

import java.util.ArrayList;
import java.util.HashMap;
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
                int order = Common.getSlotNum(comStr);
                if (order == -1){
                    if (instStack.empty()) throw new Exception("Name Error: Unexpected parameter found.");
                    slotStack.peek().add(fetch(comStr));
                }
                else {
                    instStack.push(comStr);
                    slotStack.push(new ArrayList<Type>());
                }
                com = new StringBuilder();
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
            else if (ch == '"') {
                word = new StringBuilder();
                while (ch != ' ' && ch != '\t') {
                    index++;
                    if (index >= command.length()) break;
                    ch = command.charAt(index);
                    word.append(ch);
                }
                Word newWord = new Word(word.toString());
                slotStack.peek().add(newWord);
            }  //end of word
            else com.append(command.charAt(index));
            sortStack();
            index++;
        }  //end of while-loop
    }

    private List genList(String listStr) {
        List newList = new List();
        ArrayList<Type> element = new ArrayList<>();
        Stack<ArrayList<Type>> eleStack = new Stack<>();
        for (int i = 0; i < listStr.length(); i++){
            char ch = listStr.charAt(i);
            if (ch == '[') {
                eleStack.push(new ArrayList<>());
                continue;
            }
            if (ch == ']') {
                element = eleStack.pop();

                continue;
            }
            if (ch == ' ' || ch == '\t') {
                continue;
            }

        }
        return newList;
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
        if (variable.containsKey(operand)) {
            return variable.get(operand);
        }
        if (!Common.isNone(Common.getConstant(operand))) {
            return Common.getConstant(operand);
        }
        if (Pattern.matches("^-?[0-9]+(.[0-9]+)?$",operand)){  //number
            return new Number(Double.valueOf(operand));
        }
        throw new Exception("Name Error: Unknown parameter or operation detected.");
    }

    public Type issueFun(String operator, ArrayList<Type> slot) throws Exception {
        switch (operator) {
            case "make": return make(slot);
            case "thing": return thing(slot);
            case ":": return thing(slot);
            case "erase": return erase(slot);
            case "print": return print(slot);
            case "read": return read(slot);
            case "readlinst": return readlinst(slot);
            case "add": case "sub": case "mul": case "div": case "mod": return calculate(slot,operator);
            case "isname": return isname(slot);
        }
        return new None();
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

    private Type read(ArrayList<Type> slot) {
        assert slot.size() == 0;
        return new None();
    }

    private Type print(ArrayList<Type> slot){
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
        String varStr = ((Word)(para1)).get();
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
        String varStr = ((Word)(para)).get();
        if (variable.containsKey(varStr)) return variable.get(varStr);
        throw new Exception("Name Error: Unknown variable name detected!");
    }

    private Type calculate(ArrayList<Type> slot, String operation) throws Exception {
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
        if (operation.equals("mod")) {
            if (!((Number) para1).isInt() || !((Number) para2).isInt())
                throw new Exception("Modulo Error: Divisor should be an integer.");
            int num = ((Double) para1.get()).intValue();
            int den = ((Double) para2.get()).intValue();
            return new Number(num % den);
        }
        return new None();
    }
}
