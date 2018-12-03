package type;

import java.util.ArrayList;

public class List implements Type{
	ArrayList<Object> content;
	static final int typeCode = 1;

	public List(){
		content = new ArrayList<>();
	}
	
	@SuppressWarnings("unchecked")
	public void set(Object o) throws Exception{
		if (o instanceof List) {
			content = (ArrayList<Object>) ((List) o).content.clone();
		}
		else {
			throw new Exception("Incompatible value assigned to a instance of List."); 
		}
	}

	void add(Object e) {
		content.add(e);
	}
	
	int size() {
		return content.size();
	}
	
	Object get(int index) {
		return content.get(index);
	}
	
	public Object get() {
		return content;
	}
}
