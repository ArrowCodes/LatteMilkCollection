package com.enlace.lattemilkcollection;

public class SalesList {
    public String id;
    public String farmer_id;
    public String name;
    public String litres;
    public String amount_to_pay;

    public SalesList(String id, String farmer_id, String name, String litres, String amount_to_pay) {
        this.id = id;
        this.farmer_id = farmer_id;
        this.name = name;
        this.litres = litres;
        this.amount_to_pay = amount_to_pay;
    }

    public String getId() {
        return id;
    }

    public String getFarmer_id() {
        return farmer_id;
    }

    public String getName() {
        return name;
    }

    public String getLitres() {
        return litres;
    }

    public String getAmount_to_pay() {
        return amount_to_pay;
    }
}
