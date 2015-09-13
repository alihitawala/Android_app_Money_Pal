package com.developer.nita.hisabKitab.db;

public class PartnerDetail {
	int _id;
	int _partnerId;
	String _item;
	double _credit; //i gave
	double _debit; //he/she gave
	String _date;
	
	public String getDate() {
		return _date;
	}

	public void setDate(String _date) {
		this._date = _date;
	}

	private PartnerDetail()
	{
		
	}
	
	public PartnerDetail(int id, int parterId, String item, double credit, double debit, String date)
	{
		this(parterId,item,credit, debit, date);
		this._id = id;
	}
	
	public PartnerDetail(int partnerId, String item, double credit, double debit, String date)
	{
		this();
		this._partnerId = partnerId;
		this._item = item;
		this._date = date;
		this._credit = credit;
		this._debit = debit;
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int _id) {
		this._id = _id;
	}
	public int getPartnerId() {
		return _partnerId;
	}
	public void setPartnerId(int partnerId) {
		this._partnerId = partnerId;
	}
	public String getItem() {
		return _item;
	}
	public void setItem(String _item) {
		this._item = _item;
	}
	public double getCredit() {
		return _credit;
	}
	public void setCredit(double _credit) {
		this._credit = _credit;
	}
	public double getDebit() {
		return _debit;
	}
	public void setDebit(double _debit) {
		this._debit = _debit;
	}
	

}
