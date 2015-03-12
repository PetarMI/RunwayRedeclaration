package View;

import Model.Runway;
import Model.Values;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class ThreeDVisuals extends JFXPanel {

    public static final int SCENE_WIDTH = 400;
    public static final int SCENE_HEIGHT = 300;
    public static final int TORA_TRANS = 250;
    public static final int LDA_TRANS = 150;
    public static final int ASDA_TRANS = 350;
    public static final int TODA_TRANS = 450;
    public static final int LABEL_MARGIN = - 20;
    public static final int TEXT_ADJUSTMENT = 5;
    public static final double TRUE_ANGLE = 1.1475;
    public static final int SIZE_STRETCH = 9;
    public static final int FONT_SIZE = 35;
    public static final int TOP_DOWN_ANGLE = -90;
    public static final int SIDE_VIEW_ANGLE = 0;

    public static final int TEXT_ABOVE = -2;
    private Box floor, obstacle, slope, clearway1, clearway2, stopway1, stopway2, dt1, dt2;
    private Box toraBox, todaBox, asdaBox, ldaBox;
    private Text toraTxt, todaTxt, asdaTxt, ldaTxt;
    private Box tora1Box, toda1Box, asda1Box, lda1Box;
    private Text tora1Txt, toda1Txt, asda1Txt, lda1Txt;
    private Box tora2Box, toda2Box, asda2Box, lda2Box;
    private Text tora2Txt, toda2Txt, asda2Txt, lda2Txt;
    private Box tora3Box, toda3Box, asda3Box, lda3Box;
    private Text tora3Txt, toda3Txt, asda3Txt, lda3Txt;
    private Translate zoom;
    private Scene scene;
    private Group root;
    private PerspectiveCamera camera;
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    private final Rotate rotatePlain = new Rotate(0, Rotate.Z_AXIS);
    public static final int ARROW_SMALL = 1;
    public static final int ARROW_BIG = 30;
    private int obstHeight = 15;
    private int obstWidth = 12;
    private int obstDepth = 12;
    private int runwayDepth = 200;
    private int runwayHeight = 50;
    private int obstPos;

    private int runwayLength = 1500;
    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private int stopway1Width = 30;
    private int stopway2Width = 30;
    private int clearway1Width = 100;
    private int clearway2Width = 100;
    private int dt1Pos = -600;
    private int dt2Pos = 670;
    private Values vals1, vals2;
    private Values origVals1, origVals2;


    public void init (Runway runway){
        this.origVals1 = runway.getStrip1().getOrigVal();
        this.origVals2 = runway.getStrip2().getOrigVal();
        this.vals1 = runway.getStrip1().getRecVal();
        this.vals2 = runway.getStrip2().getRecVal();
        this.obstHeight = runway.getObstacle().getHeight();
        this.obstWidth = runway.getObstacle().getLength();
        this.obstDepth = runway.getObstacle().getWidth();
        this.runwayLength = origVals1.getTora();
        this.dt1Pos = - runwayLength/2 + runway.getStrip1().getDisplacedThreshold();
        this.dt2Pos = + runwayLength/2 - runway.getStrip2().getDisplacedThreshold();
        this.obstPos = runway.getPositionFromLeftDT() + dt1Pos;

        this.calculateClearWays();
        this.calculateStopWays();
        this.oldMethod();
    }



    private void calculateStopWays() {
        this.stopway2Width = origVals1.getAsda() - origVals1.getTora();
        this.stopway1Width = origVals2.getAsda() - origVals2.getTora();
    }

    private void calculateClearWays() {
        this.clearway2Width = origVals1.getToda() - origVals1.getTora();
        this.clearway1Width = origVals2.getToda() - origVals2.getTora();
    }


    private void oldMethod() {

        root = new Group();
        scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT, true, SceneAntialiasing.BALANCED);
        camera = new PerspectiveCamera(true);
        floor = new Box(runwayLength, runwayHeight, runwayDepth);
        obstacle = new Box(obstWidth, obstHeight, obstDepth);
        slope = new Box(1500, 5, obstDepth);
        zoom = new Translate(0,0,-3000);

        clearway1 = new Box(clearway1Width, runwayHeight, runwayDepth);
        clearway2 = new Box(clearway2Width, runwayHeight, runwayDepth);
        stopway1 = new Box(stopway1Width, runwayHeight, runwayDepth);
        stopway2 = new Box(stopway2Width, runwayHeight, runwayDepth);
        dt1 = new Box(3, runwayHeight, runwayDepth);
        dt2 = new Box(3, runwayHeight, runwayDepth);

        todaBox = new Box(vals1.getToda(), ARROW_SMALL, ARROW_BIG);
        toraBox = new Box(vals1.getTora(), ARROW_SMALL, ARROW_BIG);
        asdaBox = new Box(vals1.getAsda(), ARROW_SMALL, ARROW_BIG);
        ldaBox = new Box(vals1.getLda(), ARROW_SMALL, ARROW_BIG);

        toda1Box = new Box(vals2.getToda(), ARROW_SMALL, ARROW_BIG);
        tora1Box = new Box(vals2.getTora(), ARROW_SMALL, ARROW_BIG);
        asda1Box = new Box(vals2.getAsda(), ARROW_SMALL, ARROW_BIG);
        lda1Box = new Box(vals2.getLda(), ARROW_SMALL, ARROW_BIG);

        toda2Box = new Box(vals1.getToda(), ARROW_BIG, ARROW_SMALL);
        tora2Box = new Box(vals1.getTora(), ARROW_BIG, ARROW_SMALL);
        asda2Box = new Box(vals1.getAsda(), ARROW_BIG, ARROW_SMALL);
        lda2Box = new Box(vals1.getLda(), ARROW_BIG, ARROW_SMALL);

        toda3Box = new Box(vals2.getToda(), ARROW_BIG, ARROW_SMALL);
        tora3Box = new Box(vals2.getTora(), ARROW_BIG, ARROW_SMALL);
        asda3Box = new Box(vals2.getAsda(), ARROW_BIG, ARROW_SMALL);
        lda3Box = new Box(vals2.getLda(), ARROW_BIG, ARROW_SMALL);


        todaTxt = new Text("TODA: " + vals1.getToda());
        toraTxt = new Text("TORA: " + vals1.getTora());
        asdaTxt = new Text("ASDA: " + vals1.getAsda());
        ldaTxt = new Text("LDA: " + vals1.getLda());

        toda1Txt = new Text("TODA: " + vals2.getToda());
        tora1Txt = new Text("TORA: " + vals2.getTora());
        asda1Txt = new Text("ASDA: " + vals2.getAsda());
        lda1Txt = new Text("LDA: " + vals2.getLda());

        toda2Txt = new Text("TODA: " + vals1.getToda());
        tora2Txt = new Text("TORA: " + vals1.getTora());
        asda2Txt = new Text("ASDA: " + vals1.getAsda());
        lda2Txt = new Text("LDA: " + vals1.getLda());

        toda3Txt = new Text("TODA: " + vals2.getToda());
        tora3Txt = new Text("TORA: " + vals2.getTora());
        asda3Txt = new Text("ASDA: " + vals2.getAsda());
        lda3Txt = new Text("LDA: " + vals2.getLda());

        Font myFont = new Font(FONT_SIZE);

        //Shape props
        floor.setMaterial(new PhongMaterial(Color.GRAY));
        obstacle.setMaterial(new PhongMaterial(Color.DARKCYAN));
        slope.setMaterial(new PhongMaterial(Color.BISQUE));
        clearway1.setMaterial(new PhongMaterial(Color.BLACK));
        clearway2.setMaterial(new PhongMaterial(Color.BLACK));
        stopway1.setMaterial(new PhongMaterial(Color.ANTIQUEWHITE));
        stopway2.setMaterial(new PhongMaterial(Color.ANTIQUEWHITE));
        dt1.setMaterial(new PhongMaterial(Color.BLACK));
        dt2.setMaterial(new PhongMaterial(Color.BLACK));


        obstacle.setTranslateY(- (obstHeight/2 + runwayHeight/2));
        obstacle.setTranslateX(obstPos);

        slope.getTransforms().add(rotatePlain);
        slope.setTranslateY(-20);

        clearway1.setTranslateX(-(runwayLength/2 + clearway1Width/2));
        clearway2.setTranslateX(runwayLength / 2 + clearway2Width / 2);

        stopway1.setTranslateX(-(runwayLength/2 + stopway1Width/2));
        stopway2.setTranslateX(runwayLength / 2 + stopway2Width/2);

        dt1.setTranslateX(dt1Pos);
        dt2.setTranslateX(dt2Pos);


        //TOPDOWN
        toraBox.setMaterial(new PhongMaterial(Color.LIGHTCORAL));
        toraTxt.setFont(myFont);
        toraTxt.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
        toraBox.setTranslateX(-runwayLength/2 + vals1.getTora()/2);
        toraTxt.setTranslateX(-runwayLength/2 + vals1.getTora()/2 );

        todaBox.setMaterial(new PhongMaterial(Color.LIGHTCORAL));
        todaTxt.setFont(myFont);
        todaTxt.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
        todaBox.setTranslateX(-runwayLength/2 + vals1.getToda()/2);
        todaTxt.setTranslateX(-runwayLength/2 + vals1.getToda()/2 );

        asdaBox.setMaterial(new PhongMaterial(Color.LIGHTCORAL));
        asdaTxt.setFont(myFont);
        asdaTxt.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
        asdaBox.setTranslateX(-runwayLength/2 + vals1.getAsda()/2);
        asdaTxt.setTranslateX(-runwayLength/2 + vals1.getAsda()/2 );

        ldaBox.setMaterial(new PhongMaterial(Color.LIGHTCORAL));
        ldaTxt.setFont(myFont);
        ldaTxt.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
        ldaBox.setTranslateX(vals1.getLda()/2 + dt1Pos);
        ldaTxt.setTranslateX(vals1.getLda()/2 + dt1Pos);

        //TOPDOWN (1)
        tora1Box.setMaterial(new PhongMaterial(Color.LIGHTSALMON));
        tora1Txt.setFont(myFont);
        tora1Txt.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
        tora1Box.setTranslateX(runwayLength/2 - vals2.getTora()/2);
        tora1Txt.setTranslateX(runwayLength/2 - vals2.getTora()/2 );

        toda1Box.setMaterial(new PhongMaterial(Color.LIGHTSALMON));
        toda1Txt.setFont(myFont);
        toda1Txt.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
        toda1Box.setTranslateX(runwayLength/2 - vals2.getToda()/2);
        toda1Txt.setTranslateX(runwayLength/2 - vals2.getToda()/2 );

        asda1Box.setMaterial(new PhongMaterial(Color.LIGHTSALMON));
        asda1Txt.setFont(myFont);
        asda1Txt.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
        asda1Box.setTranslateX(runwayLength/2 - vals2.getAsda()/2);
        asda1Txt.setTranslateX(runwayLength/2 - vals2.getAsda()/2 );

        lda1Box.setMaterial(new PhongMaterial(Color.LIGHTSALMON));
        lda1Txt.setFont(myFont);
        lda1Txt.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
        lda1Box.setTranslateX( dt2Pos - vals2.getLda()/2);
        lda1Txt.setTranslateX(dt2Pos - vals2.getLda()/2 );


        //SIDE (2)
        tora2Box.setMaterial(new PhongMaterial(Color.LIGHTCORAL));
        tora2Txt.setFont(myFont);
        tora2Box.setTranslateX(-runwayLength/2 + vals1.getTora()/2);
        tora2Txt.setTranslateX(-runwayLength/2 + vals1.getTora()/2);


        toda2Box.setMaterial(new PhongMaterial(Color.LIGHTCORAL));
        toda2Txt.setFont(myFont);
        toda2Box.setTranslateX(-runwayLength/2 + vals1.getToda()/2);
        toda2Txt.setTranslateX(-runwayLength/2 + vals1.getToda()/2);

        asda2Box.setMaterial(new PhongMaterial(Color.LIGHTCORAL));
        asda2Txt.setFont(myFont);
        asda2Box.setTranslateX(-runwayLength/2 + vals1.getAsda()/2);
        asda2Txt.setTranslateX(-runwayLength/2 + vals1.getAsda()/2);

        lda2Box.setMaterial(new PhongMaterial(Color.LIGHTCORAL));
        lda2Txt.setFont(myFont);
        lda2Box.setTranslateX(vals1.getLda()/2 + dt1Pos);
        lda2Txt.setTranslateX(vals1.getLda()/2 + dt1Pos);

        //SIDE (3)
        tora3Box.setMaterial(new PhongMaterial(Color.LIGHTSALMON));
        tora3Txt.setFont(myFont);
        tora3Txt.getTransforms().add(new Rotate(180, Rotate.Y_AXIS));
        tora3Box.setTranslateX(runwayLength/2 - vals2.getTora()/2);
        tora3Txt.setTranslateX(runwayLength/2 - vals2.getTora()/2 );

        toda3Box.setMaterial(new PhongMaterial(Color.LIGHTSALMON));
        toda3Txt.setFont(myFont);
        toda3Txt.getTransforms().add(new Rotate(180, Rotate.Y_AXIS));
        toda3Box.setTranslateX(runwayLength/2 - vals2.getToda()/2);
        toda3Txt.setTranslateX(runwayLength/2 - vals2.getToda()/2 );

        asda3Box.setMaterial(new PhongMaterial(Color.LIGHTSALMON));
        asda3Txt.setFont(myFont);
        asda3Txt.getTransforms().add(new Rotate(180, Rotate.Y_AXIS));
        asda3Box.setTranslateX(runwayLength/2 - vals2.getAsda()/2);
        asda3Txt.setTranslateX(runwayLength/2 - vals2.getAsda()/2 );

        lda3Box.setMaterial(new PhongMaterial(Color.LIGHTSALMON));
        lda3Txt.setFont(myFont);
        lda3Txt.getTransforms().add(new Rotate(180, Rotate.Y_AXIS));
        lda3Txt.setTranslateX(-600 + lda3Box.getWidth()/2);
        lda3Box.setTranslateX(runwayLength/2 - vals2.getLda()/2);
        lda3Txt.setTranslateX(runwayLength/2 - vals2.getLda()/2 );


        //TOPDOWN
        toraBox.setTranslateZ(TORA_TRANS);
        toraTxt.setTranslateZ(TORA_TRANS - TEXT_ADJUSTMENT);
        toraTxt.setTranslateY(TEXT_ABOVE);

        todaBox.setTranslateZ(TODA_TRANS);
        todaTxt.setTranslateZ(TODA_TRANS - TEXT_ADJUSTMENT);
        todaTxt.setTranslateY(TEXT_ABOVE);

        asdaBox.setTranslateZ(ASDA_TRANS);
        asdaTxt.setTranslateZ(ASDA_TRANS - TEXT_ADJUSTMENT);
        asdaTxt.setTranslateY(TEXT_ABOVE);

        ldaBox.setTranslateZ(LDA_TRANS);
        ldaTxt.setTranslateZ(LDA_TRANS - TEXT_ADJUSTMENT);
        ldaTxt.setTranslateY(TEXT_ABOVE);

        //TOPDOWN (1)
        tora1Box.setTranslateZ(-TORA_TRANS);
        tora1Txt.setTranslateZ(-TORA_TRANS - TEXT_ADJUSTMENT);
        tora1Txt.setTranslateY(TEXT_ABOVE);

        toda1Box.setTranslateZ(-TODA_TRANS);
        toda1Txt.setTranslateZ(-TODA_TRANS - TEXT_ADJUSTMENT);
        todaTxt.setTranslateY(TEXT_ABOVE);

        asda1Box.setTranslateZ(-ASDA_TRANS);
        asda1Txt.setTranslateZ(-ASDA_TRANS - TEXT_ADJUSTMENT);
        asda1Txt.setTranslateY(TEXT_ABOVE);

        lda1Box.setTranslateZ(-LDA_TRANS);
        lda1Txt.setTranslateZ(-LDA_TRANS - TEXT_ADJUSTMENT);
        lda1Txt.setTranslateY(TEXT_ABOVE);

        //SIDE (2)
        tora2Box.setTranslateY(TORA_TRANS);
        tora2Txt.setTranslateY(TORA_TRANS + TEXT_ADJUSTMENT);
        tora2Txt.setTranslateZ(TEXT_ABOVE + LABEL_MARGIN);
        tora2Box.setTranslateZ(LABEL_MARGIN);

        toda2Box.setTranslateY(TODA_TRANS);
        toda2Txt.setTranslateY(TODA_TRANS + TEXT_ADJUSTMENT);
        toda2Txt.setTranslateZ(TEXT_ABOVE + LABEL_MARGIN);
        toda2Box.setTranslateZ(LABEL_MARGIN);

        asda2Box.setTranslateY(ASDA_TRANS);
        asda2Txt.setTranslateY(ASDA_TRANS + TEXT_ADJUSTMENT);
        asda2Txt.setTranslateZ(TEXT_ABOVE + LABEL_MARGIN);
        asda2Box.setTranslateZ(LABEL_MARGIN);

        lda2Box.setTranslateY(LDA_TRANS);
        lda2Txt.setTranslateY(LDA_TRANS + TEXT_ADJUSTMENT);
        lda2Txt.setTranslateZ(TEXT_ABOVE + LABEL_MARGIN);
        lda2Box.setTranslateZ(LABEL_MARGIN);


        //SIDE (3)
        tora3Box.setTranslateY(TORA_TRANS);
        tora3Txt.setTranslateY(TORA_TRANS + TEXT_ADJUSTMENT);
        tora3Txt.setTranslateZ(-(TEXT_ABOVE + LABEL_MARGIN));
        tora3Box.setTranslateZ(-LABEL_MARGIN);

        toda3Box.setTranslateY(TODA_TRANS);
        toda3Txt.setTranslateY(TODA_TRANS + TEXT_ADJUSTMENT);
        toda3Txt.setTranslateZ(-(TEXT_ABOVE + LABEL_MARGIN));
        toda3Box.setTranslateZ(-LABEL_MARGIN);

        asda3Box.setTranslateY(ASDA_TRANS);
        asda3Txt.setTranslateY(ASDA_TRANS + TEXT_ADJUSTMENT);
        asda3Txt.setTranslateZ(-(TEXT_ABOVE + LABEL_MARGIN));
        asda3Box.setTranslateZ(-LABEL_MARGIN);

        lda3Box.setTranslateY(LDA_TRANS);
        lda3Txt.setTranslateY(LDA_TRANS + TEXT_ADJUSTMENT);
        lda3Txt.setTranslateZ(-(TEXT_ABOVE + LABEL_MARGIN));
        lda3Box.setTranslateZ(-LABEL_MARGIN);


        rotatePlain.setAngle(TRUE_ANGLE * SIZE_STRETCH);


        //Camera properties
        camera.setNearClip(0.1);
        camera.setFarClip(100000.0);
        camera.setVerticalFieldOfView(false);
        this.setSelectedView(TOP_DOWN_ANGLE);
        camera.getTransforms().addAll(rotateX, rotateY, zoom);

        PointLight light = new PointLight(Color.LIGHTGRAY);
        light.setTranslateY(-300);
        //Scene props
        scene.setFill(Color.SKYBLUE);
        scene.setCamera(camera);

        //add elems
        root.getChildren().add(floor);
        root.getChildren().add(obstacle);
        root.getChildren().add(slope);
        root.getChildren().add(light);
        root.getChildren().addAll(clearway1, clearway2, stopway1, stopway2, dt1, dt2);
        root.getChildren().addAll(toraBox, toraTxt,todaBox, todaTxt, asdaBox, asdaTxt, ldaBox, ldaTxt);
        root.getChildren().addAll(tora1Box, tora1Txt,toda1Box, toda1Txt, asda1Box, asda1Txt, lda1Box, lda1Txt);
        root.getChildren().addAll(tora2Box, tora2Txt,toda2Box, toda2Txt, asda2Box, asda2Txt, lda2Box, lda2Txt);
        root.getChildren().addAll(tora3Box, tora3Txt,toda3Box, toda3Txt, asda3Box, asda3Txt, lda3Box, lda3Txt);
        root.getChildren().add(new AmbientLight(Color.WHITE));

        this.setListeners();

        //Stage props
//        primaryStage.setTitle("Visualization");
        this.setScene(scene);
//        primaryStage.setWidth(SCENE_WIDTH);
//        primaryStage.setHeight(SCENE_HEIGHT);
//        primaryStage.show();
    }

    private void setListeners(){
        scene.setOnScroll(
                new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent event) {
                        double zoomFactor = 1.1;
                        double deltaY = event.getDeltaY();
                        if (deltaY < 0){
                            zoomFactor = 2.0 - zoomFactor;
                        }
                        zoom.setZ(zoom.getZ() * zoomFactor);

                        event.consume();
                    }
                });

        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mousePosX = event.getSceneX();
                mousePosY = event.getSceneY();
                rotateX.setAngle(rotateX.getAngle()-(mousePosY - mouseOldY));
                rotateY.setAngle(rotateY.getAngle()+(mousePosX - mouseOldX));
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
            }
        });

    }

    public void setObstSize(int width,int height, int depth){
        obstWidth = width;
        obstHeight = height;
        obstDepth = depth;
        updateObstPos();
    }

    private void updateObstPos(){
        obstacle.setWidth(obstWidth);
        obstacle.setHeight(obstHeight);
        obstacle.setDepth(obstDepth);
    }

    public void setObstPos(int runwayLength, int xPos){
        obstacle.setTranslateX(-(runwayLength / 2 - xPos));
    }

    public void setRunwayLength(int length){
        runwayLength = length;
        floor.setWidth(runwayLength);
    }

    public void setSelectedView(int angle){
        rotateX.setAngle(angle);
    }

//    public static void main(String[] args) {
//        launch(args);
//    }
}
