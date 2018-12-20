package type;

import common.Common;

import java.util.regex.Pattern;

public class Word implements Type {
    private static final int typeCode = 0;
    private String content;

    public Word() throws Exception {
        this("");
    }

    public Word(Object o) {
        content = o.toString();
    }

    public boolean isBool() {
        return content.equals("true") || content.equals("false");
    }

    @Override
    public Type getFirst() {
        return new Word(content.substring(0,1));
    }

    @Override
    public Type getLast() {
        return new Word(content.substring(content.length()-1));
    }

    @Override
    public Type getButFirst() {
        return new Word(content.substring(1));
    }

    @Override
    public Type getButLast() {
        return new Word(content.substring(0,content.length()-1));
    }

    public boolean getBool() throws Exception {
        if (!isBool()) throw new Exception("Type Error: Incompatible operation for a non-Bool");
        return content.equals("true");
    }

    public boolean isNumber() {
        return Pattern.matches("^-?[0-9]+(.[0-9]+)?$", content);
    }

    public double getNumber() throws Exception {
        if (!isNumber()) throw new Exception("Type Error: Incompatible operation for a non-Number");
        return Double.valueOf(content);
    }

    @Override
    public boolean isWord() {
        return true;
    }

    public void set(Object o) throws Exception {
        if (o instanceof String) {
            content = (String) o;
        } else {
            throw new Exception("Type Error: Incompatible value assigned to a instance of Word.");
        }
    }

    @Override
    public boolean isList() {
        return false;
    }

    public String get() {
        return content.intern();
    }

    public String toString() {
        if (Common.isInteger(this)) return Integer.valueOf(((int)(double)Double.valueOf(content))).toString();
        return content.toString();
    }

    public String getRawString() {
        return "\"" + content.toString();
    }

    @Override
    public int getTypeCode() {
        return typeCode;
    }
}
