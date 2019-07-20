package com.nigakolczan.tfuniantic;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.UBJsonReader;

import java.util.ArrayList;
import java.util.List;

public class Battle implements Screen, InputProcessor {
    public SpriteBatch batch;
    PerspectiveCamera perspectiveCamera;
    ModelBatch modelBatch;
    Environment environment;
    Model model;
    Array<ModelInstance> instanceArray;
    ModelInstance player;
    ModelInstance enemy;
    ModelInstance ground;
    ModelInstance skybox;
    btCollisionShape playerShape;
    btCollisionShape enemyShape;
    btCollisionShape groundShape;
    btCollisionObject groundObject;
    btCollisionObject playerObject;
    btCollisionObject enemyObject;
    btCollisionConfiguration collisionConfiguration;
    btDispatcher dispatcher;
    BattleUI battleUI;
    public BitmapFont font;
    boolean collision;
    boolean turn;
    boolean alive,enemyAlive;
    boolean wasClicked;
    boolean loading;
    int enemyHp;
    int playerHp;
    int selectedPokemon;
    final Start game;
    Pokemon pokemon1;
    Pokemon pokemon2;
    PlayerData playerData;
    PokemonUI pokemonUI;
    AssetManager assetManager;
    InputMultiplexer multiplexer;
    Image playerSprite;
    BitmapFont pkmnFont;
    AnimationController controller;

    public Battle(Pokemon pokemon, Pokemon pokemon2){
        this.game = new Start();

    }
    public Battle(){
        batch = new SpriteBatch();
        this.game = new Start();
    }
    public Battle(final Start game){
        selectedPokemon = 0;
        playerData = new PlayerData();
        pokemonUI = new PokemonUI();
        batch = new SpriteBatch();
        font = new BitmapFont();
        enemyAlive = true;
        assetManager = new AssetManager();
        assetManager.load("data/skydome.g3db",Model.class);
        assetManager.load("data/ampharos.g3dj",Model.class);
        assetManager.load("data/garchompF.g3dj",Model.class);
        assetManager.load("data/zangoose.g3dj",Model.class);
        pkmnFont = new BitmapFont();
        List<Integer> stats2 = new ArrayList<>();
        stats2.add(60);
        stats2.add(50);
        stats2.add(10);
        stats2.add(5);
        stats2.add(12);
        stats2.add(8);
        stats2.add(20);
        Move move1 = new Move("nie","attack",10,50);
        Move move2 = new Move("wiem","attack",5,50);
        Move move3 = new Move("co","attack",3,50);
        Move move4 = new Move("robie","attack",15,50);
        List<Move> moves = new ArrayList<>();
        moves.add(move1);
        moves.add(move2);
        moves.add(move3);
        moves.add(move4);
        pokemon1 = playerData.pokemons.get(selectedPokemon);
        pokemon2 = new Pokemon("Garchomp",5,stats2,moves,"testingDwa");
        this.game = game;
        Bullet.init();
        battleUI = new BattleUI();
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(pokemonUI.stage);
        multiplexer.addProcessor(battleUI.stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight,0.4f,0.4f,0.4f,1f));
        environment.add(new DirectionalLight().set(1f,1f,1f,6f,-21f,-10f));

        perspectiveCamera = new PerspectiveCamera(67,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        perspectiveCamera.near = 0.1f;
        perspectiveCamera.far = 600f;
        perspectiveCamera.position.set(-30,27f,10f);
        perspectiveCamera.rotate(-80,0,1,0);
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.node().id="ground";
        modelBuilder.part("box",GL20.GL_TRIANGLES, VertexAttributes.Usage.Position| VertexAttributes.Usage.TextureCoordinates| VertexAttributes.Usage.Normal,new Material(TextureAttribute.createDiffuse(new Texture("grassTest.jpg")))).box(110f,1f,110f);
        model = modelBuilder.end();
        ground = new ModelInstance(model,"ground");
        ground.transform.setToTranslation(0,20,0);
        instanceArray = new Array<ModelInstance>();
        instanceArray.add(ground);
        groundShape = new btBoxShape(new Vector3(5f,0.5f,5f));
        groundObject = new btCollisionObject();
        playerShape = new btBoxShape(new Vector3(5f,1f,5f));
        playerObject = new btCollisionObject();
        enemyShape = new btBoxShape(new Vector3(5f,1f,5f));
        enemyObject = new btCollisionObject();
        collisionConfiguration = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfiguration);
        batch.begin();
        /*playerSprite = new Image(new Texture(pokemon1.getName()+".png"));
        playerSprite.setSize(600,800);
        playerSprite.setPosition(-60,280);
        Image enemySprite = new Image(new Texture(pokemon2.getName()+".png"));
        enemySprite.setSize(300,300);
        enemySprite.setPosition(750,900);*/
        //battleUI.stage.addActor(playerSprite);
        //battleUI.stage.addActor(enemySprite);
        batch.end();
        perspectiveCamera.update();
        write("Atakuje cie dziki "+pokemon2.getName(),20,500);
        enemyHp = pokemon2.getStats().get(1);
        playerHp = pokemon1.getStats().get(1);
        loading = true;
    }
    private void doneLoading(){
        skybox = new ModelInstance(assetManager.get("data/skydome.g3db",Model.class));
        player = new ModelInstance(assetManager.get("data/"+pokemon1.getName()+".g3dj",Model.class));
        player.transform.setToTranslation(-20,21,6);
        player.transform.scale(0.03f,0.03f,0.03f);
        player.transform.rotate(0,1,0,-100);
        player.transform.rotate(1,0,0,90);
        player.transform.rotate(0,0,1,180);
        enemy = new ModelInstance(assetManager.get("data/garchompF.g3dj",Model.class));
        enemy.transform.setToTranslation(-6,21,10);
        enemy.transform.scale(0.03f,0.03f,0.03f);
        enemy.transform.rotate(0,0,1,90);
        enemy.transform.rotate(1,0,0,-15);
        enemy.transform.rotate(0,1,0,-90);
        //player.materials.add(new DirectionalLight().set(1f,1f,1f,-19,24,4));
        controller = new AnimationController(player);
        System.out.println(player.model.animations.size);
        System.out.println(enemy.animations.first());
            //Gdx.app.log("Animation", anim.id);
        for(Animation animation:player.model.animations){

        }
        if(controller!= null)
            //controller.setAnimation("test");
        /*if(player!=null)
            controller.setAnimation("test",-1);*/
        loading = false;
    }
    public void moveCameraToEnemy(){
        final Vector3 test = new Vector3(perspectiveCamera.position);
        Vector3 enmpos = new Vector3();
        enmpos = enemy.transform.getTranslation(new Vector3());
        perspectiveCamera.position.set(enmpos.x-13,enmpos.y+6,enmpos.z+2);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                perspectiveCamera.position.set(test);
            }
        },2);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(loading && assetManager.update())
            doneLoading();
        Gdx.gl.glClearColor(0.3f,0.3f,0.3f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT| GL20.GL_DEPTH_BUFFER_BIT);
        perspectiveCamera.update();
        modelBatch.begin(perspectiveCamera);
        for(int i = 0;i<instanceArray.size;i++){
            modelBatch.render(instanceArray.get(i),environment);
        }
        if(skybox!=null)
        modelBatch.render(skybox);
        if(player!=null)
            modelBatch.render(player,environment);
        if(enemy!=null) {
            modelBatch.render(enemy,environment);
        }
        if(controller!=null){
           controller.update(Gdx.graphics.getDeltaTime());
        }
        modelBatch.end();
        battleUI.draw();
        drawHp();
        update();
        checkAlive();
        writePlayerBarData(pokemon1.getName(),Integer.toString(pokemon1.getStats().get(0)),Integer.toString(pokemon1.getStats().get(1)),Integer.toString(pokemon1.getLvl()));
        writeEnemyBarData(pokemon2.getName(),Integer.toString(pokemon2.getStats().get(0)),Integer.toString(pokemon2.getStats().get(1)),Integer.toString(pokemon2.getLvl()));
    }

    @Override
    public void resize(int width, int height) {

    }
    public void write(String message,float x, float y){
        batch.begin();
        font.draw(batch,message,x,y);
        font.getData().setScale(5,5);
        batch.end();
    }
    public void drawHp(){
        batch.begin();
        if(playerHp > pokemon1.getStats().get(0)/2){
            batch.setColor(new Color(0,180,0,0.7f));
        }else if(playerHp > pokemon1.getStats().get(0)/4){
            batch.setColor(new Color(256,256,0,0.7f));
        }else{
            batch.setColor(new Color(180,0,0,0.7f));
        }
        batch.draw(new Texture("barColor.png"),battleUI.bar.getX()+308,battleUI.bar.getY()+150,((292f/pokemon1.getStats().get(0))*playerHp),35);
        if(enemyHp > pokemon2.getStats().get(0)/2){
            batch.setColor(new Color(0,180,0,0.7f));
        }else if(enemyHp > pokemon2.getStats().get(0)/4){
            batch.setColor(new Color(256,256,0,0.7f));
        }else{
            batch.setColor(new Color(180,0,0,0.7f));
        }
        batch.draw(new Texture("barColor.png"),battleUI.enemyBar.getX()+308,battleUI.enemyBar.getY()+150,((292f/pokemon2.getStats().get(0))*enemyHp),35);
        batch.end();
    }
    public void writePlayerBarData(String name,String maxHp,String currentHp,String lvl){
        batch.begin(); //380,220
        font.draw(batch,name,battleUI.bar.getX()+60,battleUI.bar.getY()+255);
        font.draw(batch,maxHp,battleUI.bar.getX()+500,battleUI.bar.getY()+135);
        font.draw(batch,currentHp,battleUI.bar.getX()+350,battleUI.bar.getY()+135);
        font.draw(batch,"Lvl: "+lvl,battleUI.bar.getX()+420,battleUI.bar.getY()+255);
        batch.end();
    }
    public void writeEnemyBarData(String name,String maxHp,String currentHp,String lvl){
        batch.begin();
        font.draw(batch,name,battleUI.enemyBar.getX()+60,battleUI.enemyBar.getY()+255);
        font.draw(batch,maxHp,battleUI.enemyBar.getX()+500,battleUI.enemyBar.getY()+135);
        font.draw(batch,currentHp,battleUI.enemyBar.getX()+350,battleUI.enemyBar.getY()+135);
        font.draw(batch,"Lvl: "+lvl,battleUI.enemyBar.getX()+420,battleUI.enemyBar.getY()+255);
        batch.end();
    }
    public void writePlayerPokemons(){
        for(int i = 0; i < playerData.pokemons.size();i++) {
            batch.begin();
            pkmnFont.getData().setScale(3,3);
            pkmnFont.draw(batch, playerData.pokemons.get(i).getName(), pokemonUI.images.get(i).getX() + 50, pokemonUI.images.get(i).getY() + 150);
            pkmnFont.draw(batch, Integer.toString(playerData.pokemons.get(i).getLvl()), pokemonUI.images.get(i).getX() + 80, pokemonUI.images.get(i).getY() + 65);
            pkmnFont.draw(batch, Integer.toString(playerData.pokemons.get(i).getStats().get(1)), pokemonUI.images.get(i).getX() + 250, pokemonUI.images.get(i).getY() + 65);
            pkmnFont.draw(batch, Integer.toString(playerData.pokemons.get(i).getStats().get(0)), pokemonUI.images.get(i).getX() + 370, pokemonUI.images.get(i).getY() + 65);
            batch.end();
        }
    }
    public void changeSprite(){
        pokemon1 = playerData.pokemons.get(selectedPokemon);
        playerHp = pokemon1.getStats().get(1);
        player = new ModelInstance(assetManager.get("data/"+pokemon1.getName()+".g3dj",Model.class));
        //playerSprite.remove();
        //System.out.println(pokemon1.getName());
        //playerSprite = new Image(new Texture(pokemon1.getName()+".png"));
        //playerSprite.setSize(600,800);
        //playerSprite.setPosition(-60,280);
        //battleUI.stage.addActor(playerSprite);
        player.transform.setToTranslation(-20,21,6);
        player.transform.scale(0.03f,0.03f,0.03f);
        player.transform.rotate(0,1,0,-100);
        player.transform.rotate(1,0,0,90);
        player.transform.rotate(0,0,1,180);
        drawHp();
    }


    public void update(){
        if(battleUI.onePress){
            battleUI.attackDraw();
            //""+pokemon1.getMoveSet().get(0)).size
            //battleUI.getTable().add((pokemon1.getMoveSet().get(1).getName()));
            //battleUI.getTable().add("nie");
            //battleUI.getTable().add((pokemon1.getMoveSet().get(2).getName())).size(270,200);
            //battleUI.getTable().add((pokemon1.getMoveSet().get(3).getName())).size(270,200);
            //battleUI.getTable().add((pokemon1.getMoveSet().get(4).getName())).size(270,200);
            //battleUI.getTable().add(""+pokemon1.getMoveSet().get(1)).size(270,200);
            //battleUI.getTable().add(""+pokemon1.getMoveSet().get(2)).size(270,200);
            //battleUI.getTable().add(""+pokemon1.getMoveSet().get(3)).size(270,200);
            //game.setScreen(new Overworld(game));
        }
        if(battleUI.twoPress){

        }
        if(battleUI.threePress){
            wasClicked = true;
        }
        if(wasClicked) {
            pokemonUI.draw();
            writePlayerPokemons();
            if (pokemonUI.isFirst()) {
                selectedPokemon = 0;
                changeSprite();
                pokemonUI.stage.clear();
                wasClicked = false;
            }
            if(pokemonUI.isSecond()){
                selectedPokemon = 1;
                System.out.println("nowy pok");
                changeSprite();
                pokemonUI.stage.clear();
                wasClicked = false;
            }
            if (pokemonUI.isBackClick()) {
                pokemonUI.stage.clear();
                wasClicked = false;
            }
        }
        if(battleUI.fourPress){
            write("udalo sie uciec!",20,500);
            if(Gdx.input.isTouched()){
                game.setScreen(new Overworld(game));
                dispose();
            }
        }
        if(battleUI.at1Click){
            battleUI.at1Click = false;
            int a = 0;
            String type = pokemon1.getMoveSet().get(a).getType();
            System.out.println(type);
            if(type.equals("attack")) {
                attack(a);
            }else if(type.equals("status")){
                status();
            }else{
                buff();
            }
            moveCameraToEnemy();
        }
        if(battleUI.at2Click){
            battleUI.at2Click = false;
            int a = 1;
            String type = pokemon1.getMoveSet().get(a).getType();
            if(type.equals("attack")) {
                attack(a);
            }else if(type.equals("status")){
                status();
            }else{
                buff();
            }
        }
        if(battleUI.at3Click){
            battleUI.at3Click = false;
            int a = 2;
            String type = pokemon1.getMoveSet().get(a).getType();
            if(type.equals("attack")) {
                attack(a);
            }else if(type.equals("status")){
                status();
            }else{
                buff();
            }
        }
        if(battleUI.at4Click){
            battleUI.at4Click = false;
            int a = 3;
            String type = pokemon1.getMoveSet().get(a).getType();
            if(type.equals("attack")) {
                attack(a);
            }else if(type.equals("status")){
                status();
            }else{
                buff();
            }
        }
    }
    public void attack(int a){
        int dmg = pokemon1.getMoveSet().get(a).getDamage();
        enemyHp -= dmg;
        System.out.println("pkmn 1: "+pokemon1.getStats().get(0));
        System.out.println(pokemon2.getStats().get(0));
        if(enemyHp<=0){
            System.out.println("zeroooo");
        }
        pokemon2.setStat(pokemon2.getStats(),1,enemyHp);
    }
    public void status(){

    }
    public void buff(){

    }
    public void checkAlive(){
        if(enemyHp<=0){
            enemyAlive = false;
        }
        if(!enemyAlive){
            write("brawo, koniec",20,500);
            if(Gdx.input.isTouched()){
                game.setScreen(new Overworld(game));
                dispose();
            }
        }
    }
    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        batch.dispose();
        model.dispose();
        battleUI.dispose();
        groundObject.dispose();
        groundShape.dispose();
        dispatcher.dispose();
        collisionConfiguration.dispose();
        font.dispose();
        //playerObject.dispose();
        //playerShape.dispose();
        //enemyObject.dispose();
        //enemyShape.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
