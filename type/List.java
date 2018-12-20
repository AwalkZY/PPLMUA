package type;

import java.util.regex.Pattern;

public class List implements Type {
    private static final int typeCode = 1;
    private String content;
    private String first = "";
    private String last = "";
    private String butFirst = "";
    private String butLast = "";
    private int paraLen = 0;

    public List() {
        content = "";
    }

    public List(String content) {
        this.content = content;
    }

    public int getParaLen() {
        Type firstEle = getFirst();
        if (firstEle.isList()) {
            return ((List) firstEle).getLength();
        }
        return -1;
    }

    public Type getButFirst() {
        getLength();
        return new List(butFirst);
    }

    public Type getButLast() {
        getLength();
        return new List(butLast);
    }

    public Type getFirst() {
        getLength();
        if (first.length() > 0 && first.charAt(0) == '[') return new List(first.substring(1, first.length() - 1));
        return new Word(first);
    }

    public Type getLast() {
        getLength();
        if (last.length() > 0 && last.charAt(0) == '[') return new List(last.substring(1, last.length() - 1));
        return new Word(last);
    }

    public void set(Object o) throws Exception {
        if (o instanceof String) {
            content = (String) o;
        } else {
            throw new Exception("Type Error: Incompatible value assigned to a instance of List.");
        }
    }

    @Override
    public boolean isList() {
        return true;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isWord() {
        return false;
    }

    @Override
    public boolean isBool() {
        return false;
    }

    public String toString() {
        return "[ " + content + " ]";
    }

    public Object get() {
        return content;
    }

    public boolean isEmpty() {
        return content.trim().length() == 0;
    }

    @Override
    public int getTypeCode() {
        return typeCode;
    }

    public int getLength() {
        int bracePair = 0, ans = 0, lastBlank = 0;
        StringBuilder element = new StringBuilder();
        for (int i = 0; i < content.length(); i++) {
            char ch = content.charAt(i);
            boolean isBlank = Pattern.matches("\\s*", content.substring(i, i + 1));
            if (bracePair != 0) {
                if (ch == '[') bracePair++;
                if (ch == ']') bracePair--;
                if (bracePair != 0) element.append(ch);
                else {
                    ans++;
                    if (ans == 1) {
                        first = "[" + element.toString().trim() + "]";
                        butFirst = content.substring(i + 1);
                    }
                    last = "[" + element.toString().trim() + "]";
                    butLast = content.substring(0, lastBlank);
                    element = new StringBuilder();
                }
                continue;
            }
            if (isBlank) {
                lastBlank = i;
                String item = element.toString();
                if (!item.isEmpty()) {
                    ans++;
                    if (ans == 1) {
                        first = element.toString().trim();
                        butFirst = content.substring(i + 1);
                    }
                    last = element.toString().trim();
                    butLast = content.substring(0, lastBlank);
                    element = new StringBuilder();
                }
                continue;
            }
            if (ch == '[') {
                bracePair++;
                continue;
            }
            element.append(ch);
        }
        if (element.toString().isEmpty()) return ans;
        if (ans == 0) {
            first = element.toString().trim();
            butFirst = "";
        }
        last = element.toString().trim();
        butLast = content.substring(0, lastBlank);
        return ans + 1;
    }

    public void add(Type element) {
        content = content+" "+element.toString();
    }
}
