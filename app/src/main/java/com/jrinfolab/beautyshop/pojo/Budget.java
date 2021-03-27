package com.jrinfolab.beautyshop.pojo;

public class Budget {

    String category = "";
    String note = "";
    String date = "";
    int amount = 0;
    int type = 0;
    int id;

    public Budget(){}

    public Budget(int id, String category, String note, String date, int amount, int type) {
        this.category = category;
        this.note = note;
        this.date = date;
        this.amount = amount;
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
