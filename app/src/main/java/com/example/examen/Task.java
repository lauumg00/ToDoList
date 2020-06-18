package com.example.examen;

import java.util.Date;

public class Task {
    private int _id;
    private byte[] _img;
    private String _name,_date,_complete;

    public Task(){
    }

    public Task(int id,byte[] image, String name, String date, String complete){
        this._id=id;
        this._img = image;
        this._name= name;
        this._date=date;
        this._complete=complete;
    }
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public byte[] get_img() {
        return _img;
    }
    public void set_img(byte[] _img) {
        this._img = _img;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getDate(){
        return _date;
    }

    public void setDate(String date){
        this._date=date;
    }

    public String getComplete(){
        return _complete;
    }

    public void setComplete(String complete){
        this._complete=complete;
    }

}
