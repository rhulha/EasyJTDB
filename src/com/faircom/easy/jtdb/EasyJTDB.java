package com.faircom.easy.jtdb;

import java.util.List;

import FairCom.CtreeDb.*;
import FairCom.CtreeDb.Types.*;

public class EasyJTDB {

	private CTSession ctSession;
	private CTDatabase ctDB;

	public EasyJTDB(String database) throws CTException {
		this( "FAIRCOMS", "ADMIN", "ADMIN", database);
	}
	public EasyJTDB(String dbEngine, String username, String password, String database) throws CTException {
		ctSession = new CTSession(SESSION_TYPE.CTDB);
		try {
			ctSession.Logon(dbEngine, username, password);
		} catch (CTException e) {
			System.out.println(e.GetErrorMsg() + " Trying to create a new session");
			ctSession.Create(dbEngine, username, password);
			ctSession.Logon(dbEngine, username, password);
		}
		ctDB = new CTDatabase(ctSession);
		if (!ctSession.FindDatabase(database, new StringBuffer())) {
			ctSession.CreateDatabase(database, "");
		}
		ctDB.Connect(database);
	}
	
	public CTDatabase getDB() {
		return ctDB;
	}

	public CTTable openTable(String name) throws CTException {
		CTTable ctTable = new CTTable(ctDB);
		ctTable.Open(name, OPEN_MODE.NORMAL);
		return ctTable;
	}

	public CTTable openOrCreateTable(String name, List<Field> fields) throws CTException {
		return openOrCreateTable(name, OPEN_MODE.NORMAL, fields);
	}
	
	public CTTable openOrCreateTable(String name, int openMode, List<Field> fields) throws CTException {
		CTTable ctTable = new CTTable(ctDB);
		if (!ctDB.FindTable(name, new StringBuffer())) {
			for (Field field : fields) {
				CTField ctf = ctTable.AddField(field.name, field.type, field.length);
				if(field.index) {
					CTIndex cti = ctTable.AddIndex(name + "_idx", KEY_TYPE.FIXED_INDEX, false, false);
					ctTable.AddSegment(cti, ctf, SEG_MODE.VSCHSEG);
				}
			}
			//ctTable.SetIdentityField("name", 0, 1);
			ctTable.Create(name, CREATE_MODE.NORMAL);
		}
		ctTable.Open(name, openMode);
		return ctTable;
	}

	public void printPath() throws CTException {
		System.out.println(ctSession.GetPath());
	}

	public void deleteTable(String table) throws CTException {
		ctDB.DeleteTable(table, null);
	}

	public void logoff() {
		try {
			ctDB.Disconnect();
			ctSession.Logoff();
		} catch (CTException e) {
			System.err.println(e.getMessage());
		}
	}
	
}
