package com.developer.nita.hisabKitab.domainobject;

public class PartnerCheckModel {

	private String name;
	private int id;

	private boolean selected;

	public PartnerCheckModel(String name, int id) {
		this.id = id;
		this.name = name;
		selected = false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}