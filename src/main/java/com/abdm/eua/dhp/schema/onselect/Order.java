package com.abdm.eua.dhp.schema.onselect;

import java.util.ArrayList;

public class Order{
    public Provider provider;
    public ArrayList<Item> items;
    public Fulfillment fulfillment;
    public Quote quote;
}
