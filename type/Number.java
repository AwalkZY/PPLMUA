package type;

public class Number implements Type{
	private Double content = 0.0;
	private static final int typeCode = 0;
	
	public Number() throws Exception {
		this(0);
	}
	
	public Number(Integer number) throws Exception {
		set(number);
	}

    public Number(Double number) throws Exception {
		set(number);
	}
	
	public Double get() {
		return content;
	}
	
	Integer getInt() {
		return (int) (double) content;
	}

	public void set(Object o) throws Exception {
        if (o instanceof Integer || o instanceof Double) this.content = (Double) o;
		else {
			throw new Exception("Type Error: Incompatible value assigned to a instance of Number.");
		}
	}

    public String toString(){
        return content.toString();
    }

	@Override
	public int getTypeCode() {
		return typeCode;
	}

	public boolean isInt() {
	    double con = content;
	    return (int) con == con;
    }
}
