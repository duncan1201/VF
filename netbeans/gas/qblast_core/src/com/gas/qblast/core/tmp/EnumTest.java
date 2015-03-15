package com.gas.qblast.core.tmp;

import java.util.ArrayList;
import java.util.List;

import com.gas.qblast.core.parameter.BlastParams;
import com.gas.qblast.core.parameter.Database;

public class EnumTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Database> list = new ArrayList<Database>();
		
		list.add(Database.ALU);
		list.add(Database.DBSTS);
		list.add(Database.ENV_NT);
		
		System.out.println(list.contains(Database.ENV_NT));
		System.out.println(list.contains(Database.DBSTS));
		System.out.println(list.contains(Database.ENV_NT));


	}

}
