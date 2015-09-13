package com.developer.nita.hisabKitab.db;

public class PartnerPhoneNumber {
	int _partnerId;
	String _phone;
	private PartnerPhoneNumber() {
		// TODO Auto-generated constructor stub
	}
	
	public PartnerPhoneNumber(int partnerId, String phone)
	{
		this();
		this._partnerId = partnerId;
		this._phone = phone;
	}
	
	public int getPartnerId() {
		return _partnerId;
	}
	
	public void setPartnerId(int partnerId) {
		this._partnerId = partnerId;
	}

	public String getPhone() {
		return _phone;
	}

	public void setPhone(String _phone) {
		this._phone = _phone;
	}

}