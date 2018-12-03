package type;

public class Number implements Type{
	Double content = 0.0;
	boolean isInt = true;
	static final int typeCode = 0;
	
	Number() throws Exception {
		this(0);
	}
	
	Number(Integer number) throws Exception {
		set(number);
	}
	
	Number(Double number) throws Exception {
		set(number);
	}
	
	public Double get() {
		return content;
	}
	
	Integer getInt() {
		return Integer.valueOf((int)(double) content);
	}

	public void set(Object o) throws Exception {
		if (o instanceof Integer) {
			this.isInt = true;
			this.content = (Double) o;
		}
		else if (o instanceof Double) {
			this.isInt = false;
			this.content = (Double) o;
		}
		else {
			throw new Exception("Incompatible value assigned to a instance of Number."); 
		}
	}
	
}
