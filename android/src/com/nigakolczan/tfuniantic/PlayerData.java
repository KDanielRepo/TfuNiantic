package com.nigakolczan.tfuniantic;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {
    List<Item> bag;
    List<Pokemon> pokemons;
    private int id;
    private String name;
    private int sex;
    private int lvl;
    private int money;

    public PlayerData(){
        testGetPokemons();

    }
    public PlayerData(List<Item> bag, List<Pokemon> pokemons, int id, String name, int lvl, int money){
        this.bag = bag;
        this.pokemons = pokemons;
        this.id = id;
        this.name = name;
        this.lvl = lvl;
        this.money = money;
    }
    public void testGetPokemons(){
        pokemons = new ArrayList<>();
        List<Integer> stats1 = new ArrayList<>();
        stats1.add(80); //max hp
        stats1.add(67); //current hp
        stats1.add(70); //ph at
        stats1.add(50); //deff
        stats1.add(60); //sp at
        stats1.add(40); //sp deff
        stats1.add(75); //speed
        List<Move> moveset1 = new ArrayList<>();
        Move move1 = new Move("slash","attack",10,100);
        Move move2 = new Move("ghost claw","attack",60,100);
        Move move3 = new Move("sword dance","buff",0,100);
        Move move4 = new Move("false swipe","attack",70,100);
        moveset1.add(move1);
        moveset1.add(move2);
        moveset1.add(move3);
        moveset1.add(move4);
        Pokemon pkmn1 = new Pokemon("zangoose",10,stats1,moveset1,"Immune");
        List<Integer> stats2 = new ArrayList<>();
        stats2.add(60);
        stats2.add(44);
        stats2.add(10);
        stats2.add(5);
        stats2.add(12);
        stats2.add(8);
        stats2.add(20);
        Pokemon pkmn2 = new Pokemon("ampharos",5,stats2,moveset1,"testing");
        pokemons.add(pkmn1);
        pokemons.add(pkmn2);
    }

}
