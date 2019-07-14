package com.nigakolczan.tfuniantic;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
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

import java.util.ArrayList;
import java.util.List;

public class Battle implements Screen, InputProcessor {
    public SpriteBatch batch;
    PerspectiveCamera perspectiveCamera;
    ModelBatch modelBatch;
    Array<ModelInstance> instanceArray;
    Environment environment;
    Model model;
    ModelInstance player;
    ModelInstance enemy;
    ModelInstance ground;
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
    int enemyHp;
    final Start game;
    Pokemon pokemon1;
    Pokemon pokemon2;
    boolean at1Click,at2Click,at3Click,at4Click;
    public Battle(Pokemon pokemon, Pokemon pokemon2){
        this.game = new Start();

    }
    public Battle(){
        batch = new SpriteBatch();
        this.game = new Start();
    }
    public Battle(final Start game){
        enemyAlive = true;
        enemyHp = 1;
        List<Integer> stats = new ArrayList<>();
        stats.add(60);
        stats.add(10);
        stats.add(5);
        stats.add(12);
        stats.add(8);
        stats.add(20);
        Move move1 = new Move("nie","attack",10,50);
        Move move2 = new Move("wiem","attack",5,50);
        Move move3 = new Move("co","attack",3,50);
        Move move4 = new Move("robie","attack",15,50);
        List<Move> moves = new ArrayList<>();
        moves.add(move1);
        moves.add(move2);
        moves.add(move3);
        moves.add(move4);
        pokemon1 = new Pokemon("testName",5,stats,moves,"testing");
        pokemon2 = new Pokemon("testNameDwa",5,stats,moves,"testingDwa");
        this.game = game;
        Bullet.init();
        Gdx.input.setInputProcessor(this);
        batch = new SpriteBatch();
        font = new BitmapFont();
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight,0.4f,0.4f,0.4f,1f));
        environment.add(new DirectionalLight().set(0.8f,0.8f,0.8f,-1f,-0.8f,-0.2f));

        perspectiveCamera = new PerspectiveCamera(67,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.node().id="ground";
        modelBuilder.part("box",GL20.GL_TRIANGLES, VertexAttributes.Usage.Position| VertexAttributes.Usage.TextureCoordinates| VertexAttributes.Usage.Normal,new Material(TextureAttribute.createDiffuse(new Texture("grass.jpg")))).box(70f,1f,70f);
        modelBuilder.node().id="player";
        modelBuilder.part("box",GL20.GL_TRIANGLES,VertexAttributes.Usage.Position| VertexAttributes.Usage.TextureCoordinates| VertexAttributes.Usage.Normal,new Material(TextureAttribute.createDiffuse(new Texture("Amph.png")))).box(20f,10f,10f);
        modelBuilder.node().id="enemy";
        modelBuilder.part("box",GL20.GL_TRIANGLES,VertexAttributes.Usage.Position| VertexAttributes.Usage.TextureCoordinates| VertexAttributes.Usage.Normal,new Material(TextureAttribute.createDiffuse(new Texture("Bidoof.png")))).box(20f,30f,10f);
        model = modelBuilder.end();
        ground = new ModelInstance(model,"ground");
        player = new ModelInstance(model,"player");
        enemy = new ModelInstance(model,"enemy");
        instanceArray = new Array<ModelInstance>();
        instanceArray.add(ground);
        instanceArray.add(player);
        instanceArray.add(enemy);
        groundShape = new btBoxShape(new Vector3(5f,0.5f,5f));
        groundObject = new btCollisionObject();
        playerShape = new btBoxShape(new Vector3(5f,1f,5f));
        playerObject = new btCollisionObject();
        enemyShape = new btBoxShape(new Vector3(5f,1f,5f));
        enemyObject = new btCollisionObject();
        Vector3 enemyPos = new Vector3(20f,2f,10f);
        enemy.transform.setTranslation(enemyPos);
        collisionConfiguration = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfiguration);
        battleUI = new BattleUI();
        perspectiveCamera.position.set(-30,10f,10f);
        perspectiveCamera.rotate(-80,0,1,0);
        perspectiveCamera.update();
        write("Atakuje cie dziki "+pokemon2.getName());
    }
    public void moveCameraToEnemy(){
        Vector3 test = new Vector3();
        test = perspectiveCamera.position;
        perspectiveCamera.translate(enemy.transform.getTranslation(new Vector3()));
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                perspectiveCamera.position.sub(enemy.transform.getTranslation(new Vector3()));
            }
        },2);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.3f,0.3f,0.3f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT| GL20.GL_DEPTH_BUFFER_BIT);
        perspectiveCamera.update();
        modelBatch.begin(perspectiveCamera);
        for(int i = 0;i<instanceArray.size;i++){
            modelBatch.render(instanceArray.get(i),environment);

        }
        modelBatch.end();
        battleUI.draw();
        update();
        checkAlive();
    }

    @Override
    public void resize(int width, int height) {

    }
    public void write(String message){
        batch.begin();
        font.draw(batch,message,200,600);
        batch.end();
    }


    public void update(){
        if(battleUI.onePress){
            Table temp = new Table();
            Image at1 = new Image(new Texture("at1.png"));
            at1.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    at1Click = true;
                }
            });
            Image at2 = new Image(new Texture("at2.png"));
            at2.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    at2Click = true;
                }
            });
            Image at3 = new Image(new Texture("at3.png"));
            at3.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    at3Click = true;
                }
            });
            Image at4 = new Image(new Texture("at4.png"));
            at4.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                    return true;
                }
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    at4Click = true;
                }
            });
            temp = battleUI.getTable();
            //Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
           //battleUI.getTable().setSkin(skin);
            battleUI.getTable().clearChildren();
            battleUI.getTable().add();
            battleUI.getTable().add();
            battleUI.getTable().add();
            battleUI.getTable().add();
            battleUI.getTable().pad(5,5,5,5);
            battleUI.getTable().add();
            battleUI.getTable().add();
            battleUI.getTable().add();
            battleUI.getTable().add();
            battleUI.getTable().padBottom(5);
            battleUI.getTable().add(at1).size(270,200);
            battleUI.getTable().add(at2).size(270,200);
            battleUI.getTable().add(at3).size(270,200);
            battleUI.getTable().add(at4).size(270,200);
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

        }
        if(battleUI.fourPress){
            write("udalo sie uciec!");
            if(Gdx.input.isTouched()){
                game.setScreen(new Overworld(game));
                dispose();
            }
        }
        if(at1Click){
            at1Click = false;
            int a = 0;
            String type = pokemon1.getMoveSet().get(a).getType();
            if(type.equals("attack")) {
                attack(a);
            }else if(type.equals("status")){
                status();
            }else{
                buff();
            }
            moveCameraToEnemy();
        }
        if(at2Click){
            at2Click = false;
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
        if(at3Click){
            at3Click = false;
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
        if(at4Click){
            at4Click = false;
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
        enemyHp = pokemon2.getStats().get(0)-dmg;
        System.out.println(pokemon2.getStats().get(0));
        if(enemyHp<=0){
            System.out.println("zeroooo");
        }
        pokemon2.setStat(pokemon2.getStats(),0,enemyHp);
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
            write("brawo, koniec");
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
        playerObject.dispose();
        playerShape.dispose();
        enemyObject.dispose();
        enemyShape.dispose();
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
