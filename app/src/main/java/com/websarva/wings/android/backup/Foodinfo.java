package com.websarva.wings.android.backup;

public class Foodinfo {
    int id;
    private String foodName;
    private String buyDate;
    private String dueDate;
    private String price;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodName(){
        return this.foodName;
    }

    public void setFoodName(String foodName){
        this.foodName = foodName;
    }

    public String getBuyDate(){
        return this.buyDate;
    }

    public void setBuyDate(String buyDate){
        this.buyDate = buyDate;
    }

    public String getDueDate(){
        return this.dueDate;
    }

    public void setDueDate(String dueDate){
        this.dueDate = dueDate;
    }

    public String getPrice(){
        return this.price;
    }

    public void setPrice(String price){
        this.price = price;
    }

}
