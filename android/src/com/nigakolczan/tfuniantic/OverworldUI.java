package com.nigakolczan.tfuniantic;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class OverworldUI implements ApplicationListener {
    Viewport viewport;
    Stage stage;
    boolean test;
    OrthographicCamera camera;

    public OverworldUI(){
        Overworld overworld = new Overworld();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),camera);
        stage = new Stage(viewport,overworld.overworldBatch);
        Table table = new Table();
        table.left().bottom();
        Image main = new Image(new Texture("pokeball.jpg"));
        main.setSize(180,180);
        main.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("true");
                test = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                test = false;
            }
        });
        table.add();
        table.add();
        table.row().pad(5,5,5,5);
        table.add();
        table.add();
        table.add();
        table.row().padBottom(5);
        table.add();
        table.add();
        table.add();
        table.add(main).size(main.getWidth(),main.getHeight());
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
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
