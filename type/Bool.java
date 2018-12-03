package type;

public class Bool implements Type{
	Boolean content;
	static final int typeCode = 3;
	
	Bool() throws Exception {
		this(false);
	}
	
	Bool(boolean content) throws Exception {
		set(content);
	}
	
	public void set(Object o) throws Exception {
		if (!(o instanceof Boolean)) {
			throw new Exception("Incompatible value assigned to a instance of Bool."); 
		}
	}
	
	public Boolean get() {
		return content;
	}
}
