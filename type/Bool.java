package type;

public class Bool implements Type{
	private Boolean content;
	private static final int typeCode = 3;
	
	public Bool() throws Exception {
		this(false);
	}
	
	public Bool(boolean content) throws Exception {
		set(content);
	}
	
	public void set(Object o) throws Exception {
		if (!(o instanceof Boolean)) {
			throw new Exception("Type Error: Incompatible value assigned to a instance of Bool.");
		}
		this.content = (Boolean) o;
	}

	public String toString(){
		return content.toString();
	}

	@Override
	public int getTypeCode() {
		return typeCode;
	}

	public Boolean get() {
		return content;
	}
}
