package com.nigakolczan.tfuniantic;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
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
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.CollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithm;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithmConstructionInfo;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDispatcherInfo;
import com.badlogic.gdx.physics.bullet.collision.btManifoldResult;
import com.badlogic.gdx.physics.bullet.collision.btSphereBoxCollisionAlgorithm;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;


public class Overworld implements Screen, InputProcessor {
    PerspectiveCamera perspectiveCamera;

    public SpriteBatch getOverworldBatch() {
        return overworldBatch;
    }
    public static class PokemonObject extends ModelInstance {
        public final Vector3 center = new Vector3();
        public final Vector3 dimensions = new Vector3();
        public final float radius;
        private final static BoundingBox bounds = new BoundingBox();
        public PokemonObject(Model model, String rootNode, boolean mergeTransform) {
            super(model,rootNode,mergeTransform);
            calculateBoundingBox(bounds);
            bounds.getCenter(center);
            bounds.getDimensions(dimensions);
            radius = dimensions.len()/200f;
        }
    }
    public SpriteBatch overworldBatch;
    CameraInputController cic;
    ModelBatch modelBatch;
    Array<ModelInstance> instanceArray;
    Environment environment;
    Model model;
    ModelInstance ground;
    ModelInstance ball;
    ModelInstance skyBox;
    btCollisionShape groundShape;
    btCollisionShape ballShape;
    btCollisionObject groundObject;
    btCollisionObject ballObject;
    btCollisionConfiguration collisionConfiguration;
    btDispatcher dispatcher;
    Texture test;
    Vector3 positionBall;
    Vector3 positionGround;
    OverworldUI overworldUI;
    public BitmapFont font;
    private Vector3 position = new Vector3();
    boolean clicked = false;
    boolean collision;
    private float x1,y1;
    float distance = 30f;
    float angleAroundPlayer = 0;
    float pitch = 20;
    float yaw  = 0;
    float roll;
    float dX;
    float dY;
    boolean loading;
    final Start game;
    private int select = -1;
    private int selecting = -1;
    PokemonObject poks;
    public AssetManager assetManager;
    public Overworld(){
        overworldBatch = new SpriteBatch();
        this.game = new Start();
    }
    public Overworld(final Start game) {
        this.game = game;
        Bullet.init();
        overworldUI = new OverworldUI();
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(overworldUI.stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
        font = new BitmapFont();
        test = new Texture("texture.png");
        modelBatch = new ModelBatch();
        environment = new Environment();
        overworldBatch = new SpriteBatch();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight,0.4f,0.4f,0.4f,1f));
        environment.add(new DirectionalLight().set(0.8f,0.8f,0.8f,-1f,-0.8f,-0.2f));

        perspectiveCamera = new PerspectiveCamera(67,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        perspectiveCamera.position.set(10f,30f,15f);
        perspectiveCamera.near = 0.1f;
        perspectiveCamera.far = 500f;

        perspectiveCamera.update();
        assetManager = new AssetManager();
        assetManager.load("data/skydome.g3db",Model.class);
        assetManager.load("data/garchompF.g3dj",Model.class);

        //cic = new CameraInputController(perspectiveCamera);
        //Gdx.input.setInputProcessor(cic);

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.node().id = "ground";
        modelBuilder.part("box",GL20.GL_TRIANGLES, VertexAttributes.Usage.Position| VertexAttributes.Usage.TextureCoordinates| VertexAttributes.Usage.Normal,new Material(TextureAttribute.createDiffuse(test))).box(110f,1f,110f);
        modelBuilder.node().id = "ball";
        modelBuilder.part("sphere",GL20.GL_TRIANGLES,VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal| VertexAttributes.Usage.TextureCoordinates,new Material(ColorAttribute.createDiffuse(com.badlogic.gdx.graphics.Color.BLUE))).sphere(1f,1f,1f,10,10);
        model = modelBuilder.end();

        ground = new ModelInstance(model,"ground");
        ball = new ModelInstance(model,"ball");
        ground.transform.setToTranslation(0f,20f,0f);
        ball.transform.setToTranslation(0,21f,0);

        instanceArray = new Array<ModelInstance>();
        instanceArray.add(ground);
        instanceArray.add(ball);
        ballShape = new btSphereShape(0.5f);
        groundShape = new btBoxShape(new Vector3(2.5f,0.5f,2.5f));
        groundObject = new btCollisionObject();
        groundObject.setCollisionShape(groundShape);
        groundObject.setWorldTransform(ground.transform);

        ballObject = new btCollisionObject();
        ballObject.setCollisionShape(ballShape);
        ballObject.setWorldTransform(ball.transform);

        collisionConfiguration = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfiguration);
        positionBall = ball.transform.getTranslation(new Vector3());
        perspectiveCamera.lookAt(positionBall);
        loading = true;
    }
    private void doneLoading(){
        skyBox = new ModelInstance(assetManager.get("data/skydome.g3db",Model.class));
        Model model1 = assetManager.get("data/garchompF.g3dj",Model.class);
        String id = model1.nodes.get(0).id;
        poks = new PokemonObject(model1,id,true);
        //poks = new ModelInstance(assetManager.get("data/garchomp.g3dj",Model.class));
        poks.transform.setToTranslation(15,21,-10);
        poks.transform.scale(0.01f,0.01f,0.01f);
        poks.dimensions.scl(0.01f,0.01f,0.01f);
        poks.center.scl(0.01f,0.01f,0.01f);
        //poks.transform.rotate(0,0,1,90);
        //poks.transform.rotate(1,0,0,-90);
        poks.transform.rotate(0,1,0,-90);
        loading = false;
    }
    public int getObject(int screenX,int screenY){
        com.badlogic.gdx.math.collision.Ray ray = perspectiveCamera.getPickRay(screenX,screenY);
        int result = -1;
        float distance = -1;
        if(poks!=null) {
            poks.transform.getTranslation(position);
            position.add(poks.center);
            float dist2 = ray.origin.dst2(position);
            /*if(distance >= 0f && dist2 > distance) continue;*/
            if (Intersector.intersectRaySphere(ray, position, poks.radius, null)) {
                result = 0;
                distance = dist2;
            }
        }
        return result;

    }
    public void setSelected(int value){
        if(selecting >= 0){
            modelBatch.dispose();
            game.setScreen(new Battle(game));
            dispose();
        }
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(loading && assetManager.update())
            doneLoading();
        final float deltaa = Math.min(1f/30f,Gdx.graphics.getDeltaTime());
        if(!collision){
            ball.transform.translate(0f,-deltaa,0f);
            ballObject.setWorldTransform(ball.transform);
            collision = checkCollision();
        }
        perspectiveCamera.update();


        //cic.update();
        update();
        if(!overworldUI.test){
            Gdx.gl.glClearColor(0.3f,0.3f,0.3f,1f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT| GL20.GL_DEPTH_BUFFER_BIT);
            modelBatch.begin(perspectiveCamera);
            modelBatch.render(instanceArray,environment);
            if(skyBox!=null)
            modelBatch.render(skyBox);
            if(poks!=null)
                modelBatch.render(poks);
            //modelBatch.render(skyBox);
            modelBatch.end();
        }
        overworldUI.draw();

    }

    @Override
    public void resize(int width, int height) {

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
        model.dispose();
        groundObject.dispose();
        groundShape.dispose();
        ballObject.dispose();
        ballShape.dispose();
        dispatcher.dispose();
        collisionConfiguration.dispose();
        overworldBatch.dispose();
        overworldUI.dispose();
        font.dispose();
        assetManager.dispose();
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
        x1 = screenX;
        y1 = screenY;
        selecting = getObject(screenX,screenY);
        return selecting>=0;
    }
    /*public int getObject(int screenX, int screenY){
        com.badlogic.gdx.math.collision.Ray ray = perspectiveCamera.getPickRay(screenX,screenY);
        int result = -1;
        float distance = -1;
        //position = new Vector3();
        float dist2 = ((com.badlogic.gdx.math.collision.Ray) ray).origin.dst2(instanceArray.get(1).transform.getTranslation(position));
        if(Intersector.intersectRaySphere(ray,instanceArray.get(1).transform.getTranslation(position),instanceArray.get(1).transform.getTranslation(position).radius,null)){
            result = 1;
            distance = dist2;
        }
        return result;
    }*/
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(selecting>=0){
            setSelected(selecting);
            selecting = -1;
            return true;
        }
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
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        dX = (float)(screenX-x1)/(float)Gdx.graphics.getWidth();
        dY = (float)(screenY-y1)/(float)Gdx.graphics.getHeight();
        x1 = screenX;
        y1 = screenY;
        positionBall = ball.transform.getTranslation(new Vector3());
        positionGround = ground.transform.getTranslation(new Vector3());
        calculateAngle();
        //calculatePitch();
        //calculateZoom();
        float hD = calculateHorizontalDistance();
        float vD = calculateVerticalDistance();
        calculateCameraPosition(hD,vD);
        perspectiveCamera.update();
        return true;
    }

    private void update() {
        if(overworldUI.test){
            modelBatch.dispose();
            game.setScreen(new Battle(game));
            dispose();
        }
        if(overworldUI.menuTouch){
            if (clicked){
                overworldUI.menuOverlayDispose();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        clicked = false;
                    }
                },0.5f);
            }else{
                overworldUI.menuOverlay();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        clicked = true;
                    }
                },0.5f);
            }

        }

    }

    private boolean checkCollision() {
        CollisionObjectWrapper co0 = new CollisionObjectWrapper(ballObject);
        CollisionObjectWrapper co1 = new CollisionObjectWrapper(groundObject);
        btCollisionAlgorithmConstructionInfo ci = new btCollisionAlgorithmConstructionInfo();
        ci.setDispatcher1(dispatcher);
        btCollisionAlgorithm  algorithm = new btSphereBoxCollisionAlgorithm(null,ci,co0.wrapper,co1.wrapper,false);

        btDispatcherInfo info = new btDispatcherInfo();
        btManifoldResult result = new btManifoldResult(co0.wrapper,co1.wrapper);

        algorithm.processCollision(co0.wrapper,co1.wrapper,info,result);

        boolean r = result.getPersistentManifold().getNumContacts() > 0;

        result.dispose();
        info.dispose();
        algorithm.dispose();
        ci.dispose();
        co0.dispose();
        co1.dispose();
        return r;
    }

    private void calculateZoom(){
        float zoomLvl = dY * 3f;
        distance -=zoomLvl;
    }
    private void calculateAngle(){
        float angle = dX * 3f;
        angleAroundPlayer -=angle;

    }
    private void calculatePitch(){
        float pitchChange = dY * 3f;
        pitch -=pitchChange;
        perspectiveCamera.rotate(pitch,1,0,0);
    }
    float calculateHorizontalDistance(){
        return (float) (distance * Math.cos(Math.toRadians(pitch)));
    }
    float calculateVerticalDistance(){
        return (float) (distance * Math.sin(Math.toRadians(pitch)));
    }
    private void calculateCameraPosition(float a, float b){
        float offX = (float) (a * Math.sin(Math.toRadians(angleAroundPlayer*10)));
        float offZ = (float) (a * Math.cos(Math.toRadians(angleAroundPlayer*10)));
        float x = positionBall.x - offX;
        float y = positionBall.y + b;
        float z = positionBall.z - offZ;
        perspectiveCamera.position.set(-x,y,-z);
        perspectiveCamera.rotate(-dX*30f,0,1,0);
        perspectiveCamera.position.rotate(33,0,1,0);
        perspectiveCamera.update();
    }
}
