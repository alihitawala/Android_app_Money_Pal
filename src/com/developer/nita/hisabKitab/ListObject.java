package com.developer.nita.hisabKitab;

public class ListObject {

	String name;
	int id;
	
	private ListObject()
	{
	}
	
	public ListObject(String name, int id)
	{
		this();
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return name;
	}
	

}
