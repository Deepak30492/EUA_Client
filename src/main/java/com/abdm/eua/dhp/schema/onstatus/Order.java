package com.abdm.eua.dhp.schema.onstatus;

import java.util.ArrayList;

public class Order{
    public String id;
    public Provider provider;
    public String state;
    public ArrayList<Item> items;
    public Billing billing;
    public Fulfillment fulfillment;
    public Quote quote;
    public Payment payment;
}
