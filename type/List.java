package type;

import java.util.ArrayList;

public class List implements Type{
	private ArrayList<Type> content;
	private static final int typeCode = 1;

	public List(){
		content = new ArrayList<>();
	}

	public void set(Object o) throws Exception{
		if (o instanceof List) {
			content = new ArrayList<>(((List) o).content);
		}
		else {
			throw new Exception("Type Error: Incompatible value assigned to a instance of List.");
		}
	}

	public String toString(){
		return content.toString();
	}

	public void set(ArrayList<Type> content) {
		this.content = new ArrayList<>(content);
	}

	public void add(Type e) {
		content.add(e);
	}
	
	int size() {
		return content.size();
	}
	
	public Type get(int index) {
		return (Type) content.get(index);
	}
	
	public Object get() {
		return content;
	}

	@Override
	public int getTypeCode() {
		return typeCode;
	}
}
