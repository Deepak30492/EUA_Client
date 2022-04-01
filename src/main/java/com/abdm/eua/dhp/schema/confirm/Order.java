package com.abdm.eua.dhp.schema.confirm; 
import java.util.ArrayList;
import java.util.List;
public class Order{
    public Provider provider;
    public State state;
    public ArrayList<Item> items;
    public Billing billing;
    public Fulfillment fulfillment;
}
