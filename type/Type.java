package type;

import java.io.Serializable;

public interface Type extends Serializable {
    abstract int getTypeCode();

    abstract Object get();

    abstract void set(Object o) throws Exception;

    abstract boolean isList();

    abstract boolean isNumber();

    abstract boolean isWord();

    abstract boolean isBool();

    abstract Type getFirst();

    abstract Type getLast();

    abstract Type getButFirst();

    abstract Type getButLast();
}
