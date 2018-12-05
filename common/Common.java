package common;

import type.Bool;
import type.None;
import type.Type;
import type.Number;

public class Common {
	static final double PI = 3.14159;
	public static final double EPS = 1e-10;
	static final boolean True = true;
	static final boolean False = false;
	static final String[] occipied = {"make","thing","word","erase","number","list","bool","isname","print",
			"read","readlinst","add","sub","mul","div","mod","eq","gt","lt","and","or","not",":","repeat"};
	static final String[] operator = {"make","thing","erase","isname","print",
			"read","readlinst","add","sub","mul","div","mod","eq","gt","lt","and","or","not",":","repeat"};
	static final int[] slotNum = {2, //make
			1, //thing
			1, //erase
			1, //isname
			1, //print
			0, //read
			0, //readlinst
			2, //add
			2, //sub
			2, //mul
			2, //div
			2, //mod
			2, //eq
			2, //gt
			2, //lt
			2, //and
			2, //or
			2, //not
			1, //:
			2, //repeat
	};

	static public int getSlotNum(String str){
		for (int i = 0; i < operator.length; i++){
			if (str.equals(operator[i])) return slotNum[i];
		}
		return -1;
	}

	static public boolean isNumber(Type input) {
		return input.getTypeCode() == 0;
	}

	static public boolean isList(Type input) {
		return input.getTypeCode() == 1;
	}

	static public boolean isWord(Type input) {
		return input.getTypeCode() == 2;
	}

	static public boolean isBool(Type input) {
		return input.getTypeCode() == 3;
	}

	static public boolean isNone(Type input) {
		return input.getTypeCode() == -1;
	}

	static public Type getConstant(String constName) throws Exception {
		if (constName.equals("PI")) {
			return new Number(PI);
		}
		if (constName.equals("True") || constName.equals("False")){
			return new Bool(constName.equals("True"));
		}
		return new None();
	}
}