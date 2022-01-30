package com.websarva.wings.android.backup;

public class SumPriceinfo {
    int id;
    private String day;
    private String sumPrice;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay(){
        return this.day;
    }

    public void setDay(String day){
        this.day = day;
    }

    public String getSumPrice(){
        return this.sumPrice;
    }

    public void setSumPrice(String sumPrice){
        this.sumPrice = sumPrice;
    }


}