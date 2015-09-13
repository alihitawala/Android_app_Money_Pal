package com.developer.nita.hisabKitab.db;

public class Partner {
	 
    //private variables
    int _id;
    String _name;
    String _sex;
    double totalAmount = 0;
 
    // Empty constructor
    public Partner(){
 
    }
    // constructor
    public Partner(int id, String name, String _sex){
        this._id = id;
        this._name = name;
        this._sex = _sex;
    }
 
    // constructor
    public Partner(String name, String _sex){
        this._name = name;
        this._sex = _sex;
    }
    // getting ID
    public int getID(){
        return this._id;
    }
 
    // setting id
    public void setID(int id){
        this._id = id;
    }
 
    // getting name
    public String getName(){
        return this._name;
    }
 
    // setting name
    public void setName(String name){
        this._name = name;
    }
 
    // getting phone number
    public String getSex(){
        return this._sex;
    }
 
    // setting phone number
    public void setSex(String sex){
        this._sex = sex;
    }

    public double getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount)
    {
        this.totalAmount = totalAmount;
    }
}
