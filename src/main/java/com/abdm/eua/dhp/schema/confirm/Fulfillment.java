package com.abdm.eua.dhp.schema.confirm; 
public class Fulfillment{
    public String id;
    public String type;
    public String provider_id;
    public State state;
    public boolean tracking;
    public Customer customer;
    public Agent agent;
    public Person person;
    public Contact contact;
    public Start start;
    public End end;
    public Tags tags;
}
