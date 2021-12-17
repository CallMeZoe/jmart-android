package com.ahmadZufarJsmartMH.model;

public class Product extends Serializable {
    public int accountId;
    public double discount;
    public double price;
    public byte shipmentPlans;
    public String name;
    public int weight;
    public boolean conditionUsed;
    public ProductCategory category;

    public String toString(){
        return "Name: " + this.name +
                "\nWeight: " + this.weight +
                "\nconditionUsed: " + this.conditionUsed +
                "\nprice: " + this.price +
                "\ncategory: " + this.category +
                "\nshipmentPlans: " + this.shipmentPlans +
                "\naccount id: " + this.accountId;
    }
}

