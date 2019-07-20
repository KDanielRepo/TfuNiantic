package com.nigakolczan.tfuniantic;

public class Item {
    private String name;
    private String type;
    private int value;
    private Item(){

    }
    public Item(String name, String type,int value){
        this.name = name;
        this.type = type;
        this.value = value;
    }
}
