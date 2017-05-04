package com.faircom.easy.jtdb;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import FairCom.CtreeDb.*;
import FairCom.CtreeDb.Types.FIELD_TYPE;

public class TestEasyJTDB {
	
	public static void main(String[] args) {
		try {
			System.out.println(System.getProperty("sun.arch.data.model"));
			main1();
		} catch (CTException e) {
			System.out.println(e.GetErrorMsg());
		}
	}
	
	public static void main1() throws CTException {
		EasyJTDB e = new EasyJTDB("Wikipedia");
		//CTDatabase db = e.getDB();
		
		CTTable tab = e.openTable("Wikipedia");
		for (int i = 0; i < 10000; i++) {
			search(tab);
			
		}
		System.out.println("Done");

		e.logoff();
	}

	private static void search(CTTable tab) throws CTException {
		CTRecord rec = new CTRecord(tab);
		rec.Clear();
		rec.searchOn("'apple' -'computer'");

		boolean found = rec.First();
		while (found) {
			String title = rec.GetFieldAsString("title");
			System.out.println(title);
			found = rec.Next();
		}
	}

	public static void main2() throws CTException {
		EasyJTDB e = new EasyJTDB("FTS");
		//CTDatabase db = e.getDB();
		List<Field> fields = new ArrayList<>();
		
		fields.add(new Field("name", FIELD_TYPE.V4STRING, 1, false));
		
		CTTable tab = e.openOrCreateTable("testt82", fields);
		
		CTRecord rec = new CTRecord(tab);
		rec.Clear();
		rec.SetFieldAsBytes("name", new byte[]{0,21,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3});
		rec.Write();

		boolean found = rec.First();
		while (found) {
			byte[] bytes = rec.GetFieldAsBytes("name");
			String result = new BigInteger(1, bytes).toString(16);  
			System.out.println("x " + result);
			found = rec.Next();
		}
		System.out.println("Done");

		e.logoff();
	}

}
