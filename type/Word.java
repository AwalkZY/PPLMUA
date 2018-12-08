package type;

public class Word implements Type{
	private String content;
	private static final int typeCode = 2;
	
	public Word() throws Exception {
		this("");
	}
	
	public Word(String str) {
		content = str.intern().toString();
	}
	
	public void set(Object o) throws Exception {
		if (o instanceof String) {
			content = (String) o; 
		}
		else {
			throw new Exception("Type Error: Incompatible value assigned to a instance of Word.");
		}
	}
	
	public String get() {
		return content;
	}

	public String toString(){
		return content.toString();
	}

	@Override
	public int getTypeCode() {
		return typeCode;
	}
}
