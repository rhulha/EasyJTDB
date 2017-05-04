package com.faircom.easy.jtdb;

public class Field {

	public final String name;
	public final int type;
	public final int length;
	public final boolean index;

	public Field(String name, int type, int length, boolean index) {
		this.name = name;
		this.type = type;
		this.length = length;
		this.index = index;
	}

}
