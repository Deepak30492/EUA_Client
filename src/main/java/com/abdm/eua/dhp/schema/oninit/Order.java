package com.abdm.eua.dhp.schema.oninit;

import java.util.ArrayList;

public class Order{
    public String id;
    public String state;
    public Provider provider;
    public ArrayList<Item> items;
    public Billing billing;
    public Fulfillment fulfillment;
    public Quote quote;
    public Payment payment;
}
