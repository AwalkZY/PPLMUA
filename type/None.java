package type;

public class None implements Type{

    public None(){}

    @Override
    public Object get() {
        return null;
    }

    @Override
    public void set(Object o) throws Exception {}

    @Override
    public int getTypeCode() {
        return -1;
    }

    public String toString(){
        return null;
    }
}
