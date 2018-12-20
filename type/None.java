package type;

public class None implements Type {

    public None() {
    }

    @Override
    public Object get() {
        return null;
    }

    @Override
    public void set(Object o) throws Exception {
    }

    @Override
    public boolean isList() {
        return false;
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

    @Override
    public Type getFirst() {
        return null;
    }

    @Override
    public Type getLast() {
        return null;
    }

    @Override
    public Type getButFirst() {
        return null;
    }

    @Override
    public Type getButLast() {
        return null;
    }

    @Override
    public int getTypeCode() {
        return -1;
    }

    public String toString() {
        return null;
    }
}
