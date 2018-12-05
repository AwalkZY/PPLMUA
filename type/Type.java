package type;

public interface Type {
    abstract int getTypeCode();
	abstract Object get();
	abstract void set(Object o) throws Exception;
}
