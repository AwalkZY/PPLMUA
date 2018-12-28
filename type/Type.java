package type;

import java.io.Serializable;

public interface Type extends Serializable {
    int getTypeCode();

    Object get();

    void set(Object o) throws Exception;

    boolean isList();

    boolean isNumber();

    boolean isWord();

    boolean isBool();

    Type getFirst();

    Type getLast();

    Type getButFirst();

    Type getButLast();
}
