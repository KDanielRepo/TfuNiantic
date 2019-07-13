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
        table.add();
        table.add();
        table.add();
        table.add();
        table.row().pad(5,5,5,5);
        table.add(fight).size(fight.getWidth(),fight.getHeight());
        table.add(bag).size(bag.getWidth(),bag.getHeight());
        table.add(pokemon).size(pokemon.getWidth(),pokemon.getHeight());
        table.add(run).size(run.getWidth(),run.getHeight());
        table.row().padBottom(5);
        table.add();
        table.add();
        table.add();
        table.add();
        stage.addActor(table);

    }
    public void draw(){
        stage.draw();

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
