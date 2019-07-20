package com.nigakolczan.tfuniantic;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;

public class PokemonUI implements ApplicationListener {
    Viewport viewport;
    OrthographicCamera camera;
    private boolean first,second,third,fourth,fifth,sixth;
    private int size;
    private boolean backClick = false;
    private boolean wasDrawn;
    PlayerData playerData;
    Battle battle;
    Stage stage;
    List<Image> images;

    public PokemonUI(){
        battle = new Battle();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),camera);
        stage = new Stage(viewport,battle.getBatch());
        Gdx.input.setInputProcessor(stage);
    }
    public void draw(){
        if(!wasDrawn){
            battle = new Battle();
            playerData = new PlayerData();
            wasDrawn = true;
            images = new ArrayList<>();
        }
        for(int i = 0; i < playerData.pokemons.size();i++){
            Image image = new Image(new Texture("pkmnSelectBoxTest.png"));
            image.setPosition(0,1400-(i*200));
            image.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                }
            });
            stage.addActor(image);
            images.add(image);
            switch (i){
                case 0:
                    image.addListener(new InputListener(){
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            first = true;
                            return true;
                        }

                        @Override
                        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                            first = false;
                        }
                    });
                    break;
                case 1:
                    image.addListener(new InputListener(){
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            second = true;
                            return true;
                        }

                        @Override
                        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                            second = false;
                        }
                    });
                    break;
                case 2:
                    image.addListener(new InputListener(){
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            third = true;
                            return true;
                        }

                        @Override
                        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                            third = false;
                        }
                    });
                    break;
                case 3:
                    image.addListener(new InputListener(){
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            fourth = true;
                            return true;
                        }

                        @Override
                        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                            fourth = false;
                        }
                    });
                    break;
                case 4:
                    image.addListener(new InputListener(){
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                             fifth= true;
                            return true;
                        }

                        @Override
                        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                            fifth = false;
                        }
                    });
                    break;
                case 5:
                    image.addListener(new InputListener(){
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            sixth = true;
                            return true;
                        }

                        @Override
                        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                            sixth = false;
                        }
                    });
                    break;
            }
        }
        Image back = new Image(new Texture("pokeball.jpg"));
        back.setPosition(600,500);
        back.setSize(200,200);
        back.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                backClick = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                backClick = false;
            }
        });
        stage.addActor(back);
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
    public boolean isFirst() {
        return first;
    }

    public boolean isSecond() {
        return second;
    }

    public boolean isThird() {
        return third;
    }

    public boolean isFourth() {
        return fourth;
    }

    public boolean isSixth() {
        return sixth;
    }

    public boolean isBackClick() {
        return backClick;
    }
}
