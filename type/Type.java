package type;

public interface Type {
    abstract int getTypeCode();

    abstract Object get();

    abstract void set(Object o) throws Exception;

    abstract boolean isList();

    abstract boolean isNumber();

    abstract boolean isWord();

    abstract boolean isBool();
}
