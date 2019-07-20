package com.nigakolczan.tfuniantic;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BattleUI implements ApplicationListener {
    Viewport viewport;
    Stage stage;
    boolean onePress,twoPress,threePress,fourPress;
    OrthographicCamera camera;
    Image bar;
    Image enemyBar;
    boolean at1Click,at2Click,at3Click,at4Click;


    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    Table table;

    public boolean isOnePress() {
        return onePress;
    }

    public boolean isTwoPress() {
        return twoPress;
    }

    public boolean isThreePress() {
        return threePress;
    }

    public boolean isFourPress() {
        return fourPress;
    }

    public BattleUI(){
        Battle battle = new Battle();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),camera);
        stage = new Stage(viewport,battle.getBatch());
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.left().bottom();

        Image fight = new Image(new Texture("attack.png"));
        fight.setSize(270,200);
        fight.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onePress = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                onePress = false;
            }
        });
        Image bag = new Image(new Texture("backpack.png"));
        bag.setSize(270,200);
        bag.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                twoPress = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                twoPress = false;
            }
        });
        Image pokemon = new Image(new Texture("pkmn.png"));
        pokemon.setSize(270,200);
        pokemon.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                threePress = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                threePress = false;
            }
        });
        Image run = new Image(new Texture("run.png"));
        run.setSize(270,200);
        run.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                fourPress = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                fourPress = false;
            }
        });
        bar = new Image(new Texture("testBarNew.png"));
        bar.setSize(700,200);
        enemyBar = new Image(new Texture("testBarNew.png"));
        enemyBar.setSize(700,200);

        battle.batch.begin();
        bar.draw(battle.batch,100f);
        bar.setPosition(380,220);
        enemyBar.draw(battle.batch,1f);
        enemyBar.setPosition(20,1600);
        battle.batch.end();

        table.add();
        table.add();
        table.add();
        table.add();
        table.row().pad(5,0,0,5);
        table.add(fight).size(fight.getWidth(),fight.getHeight());
        table.add(bag).size(bag.getWidth(),bag.getHeight());
        table.add(pokemon).size(pokemon.getWidth(),pokemon.getHeight());
        table.add(run).size(run.getWidth(),run.getHeight());
        table.row().padBottom(0);
        table.add();
        table.add();
        table.add();
        table.add();
        stage.addActor(table);
        stage.addActor(bar);
        stage.addActor(enemyBar);

    }
    public void draw(){
        stage.draw();
    }
    public void attackDraw(){
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
        //Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        //battleUI.getTable().setSkin(skin);
        table.clearChildren();
        table.add();
        table.add();
        table.add();
        table.add();
        table.pad(5,5,5,5);
        table.add();
        table.add();
        table.add();
        table.add();
        table.padBottom(5);
        table.add(at1).size(270,200);
        table.add(at2).size(270,200);
        table.add(at3).size(270,200);
        table.add(at4).size(270,200);
    }
    public void pkmnDraw(){

    }
    public void bagDraw(){

    }
    @Override
    public void create() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
