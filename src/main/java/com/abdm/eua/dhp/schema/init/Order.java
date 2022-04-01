package com.abdm.eua.dhp.schema.init;

import java.util.ArrayList;
public class Order{
    public Provider provider;
    public ArrayList<Item> items;
    public Billing billing;
    public Fulfillment fulfillment;
}
