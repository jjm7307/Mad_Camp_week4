package com.madcamp.week4.adapters;

public class ModelCoins {


    private String coin, number;
    private String id;

    public ModelCoins(String id, String coin, String number) {
        this.id = id;
        this.coin = coin;
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
