package com.nigakolczan.tfuniantic;
import java.lang.reflect.Array;
import java.util.List;

public class Pokemon {
    private String name;
    private int lvl;
    List<Integer> stats;
    List<Move> moveSet;
    String passive;

    public Pokemon(){

    }
    public Pokemon(String name,int lvl,List<Integer> stats, List<Move> moveSet,String passive){
        this.name = name;
        this.lvl = lvl;
        this.stats = stats;
        this.moveSet = moveSet;
        this.passive = passive;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setLvl(int lvl){
        this.lvl = lvl;
    }
    public void setStats(List<Integer> stats){
        this.stats = stats;
    }
    public void setStat(List<Integer> stats,int a, int b){
        stats.set(a,b);
        this.stats = stats;
    }
    public void setMoveSet(List<Move> moveSet){
        this.moveSet = moveSet;
    }
    public String getName(){
        return name;
    }
    public int getLvl(){
        return lvl;
    }
    public List<Integer> getStats(){
        return stats;
    }
    public List<Move> getMoveSet(){
        return moveSet;
    }
    public String getPassive(){
        return passive;
    }
}
