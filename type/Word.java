package type;

public class Word implements Type{
	String content;
	static final int typeCode = 2;
	
	Word() throws Exception {
		this(new String(""));
	}
	
	Word(String str) {
		content = str.intern().toString();
	}
	
	public void set(Object o) throws Exception {
		if (o instanceof String) {
			content = (String) o; 
		}
		else {
			throw new Exception("Incompatible value assigned to a instance of Word."); 
		}
	}
	
	public String get() {
		return content;
	}
}
