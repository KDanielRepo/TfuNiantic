package com.nigakolczan.tfuniantic;

import java.util.List;

public class Move {
    public Move(){

    }
    public Move(String name,String type, int damage, int accuracy){
        this.name = name;
        this.type = type;
        this.damage = damage;
        this.accuracy = accuracy;
    }
    private String name;
    private String type; //attack,status,buff,
    private int damage;
    private int accuracy;

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setDamage(int damage){
        this.damage = damage;
    }
    public void setAccuracy(int accuracy){
        this.accuracy = accuracy;
    }
    public String getName(){
        return name;
    }
    public int getDamage(){
        return damage;
    }
    public int getAccuracy(){
        return accuracy;
    }
}
