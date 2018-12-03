import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import type.*;
import common.*;

public class Main {

	private ArrayList<Type> variable;
	
	public Main() {
		variable = new ArrayList<>();
		
	}
		
	void interprete(String command) {
		String[] comList = command.split("[ |\t|\n]+");
		String operator = comList[0];
		String[] paraList = Arrays.copyOfRange(comList,1,comList.length);
		System.out.println(operator);
		for (String aParaList : paraList)
			System.out.println(aParaList);
	}
	

	public static void main(String[] args) {
		Main cli = new Main();
		String command;
		
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in); 
		while (true) {
			command = in.nextLine();
			cli.interprete(command);
		}
	}
}
