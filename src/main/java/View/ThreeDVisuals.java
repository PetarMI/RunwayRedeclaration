package View;

import Model.MathHandler;
import Model.Runway;
import Model.Values;
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

public class ThreeDVisuals extends SubScene {

    public static final int SCENE_WIDTH = 2000;
    public static final int SCENE_HEIGHT = 3000;
    public static final int LABEL_TRANS_MARGIN = 100;
    public static final int LDA_TRANS = 150;
    public static final int STRIPEND = 60;
    public static final int TORA_TRANS = LDA_TRANS +LABEL_TRANS_MARGIN;
    public static final int ASDA_TRANS = TORA_TRANS + LABEL_TRANS_MARGIN;
    public static final int TODA_TRANS = ASDA_TRANS + LABEL_TRANS_MARGIN;
    public static final int LABEL_MARGIN = - 20;
    public static final int TEXT_ADJUSTMENT = 15;
    public static final double TRUE_ANGLE = 1.1475;
    public static final int SIZE_STRETCH = 9;
    public static final int FONT_SIZE = 55;
    public static final int TOP_DOWN_ANGLE = -90;
    public static final int SIDE_VIEW_ANGLE = 0;
    public static final int SIDE_SECOND_VIEW_ANGLE = 180;
    public static final int PLAIN_ANGLE = 0;
    public static final int TEXT_ABOVE = -5;
    public static final int ARROW_SMALL = 1;
    public static final int ARROW_BIG = 50;
    public static final double SLOPE_ANGLE = TRUE_ANGLE * SIZE_STRETCH;
    public static final String MODEL_FILE_NAME = "/models/palm/palm_tree.obj";
    private static final double MODEL_SCALE_FACTOR = 400;

    private Box floor, obstacle, slope, clearway1, clearway2, stopway1, stopway2, dt1, dt2;
    private Box toraBox, todaBox, asdaBox, ldaBox;
    private Text toraTxt, todaTxt, asdaTxt, ldaTxt;
    private Box tora1Box, toda1Box, asda1Box, lda1Box;
    private Text tora1Txt, toda1Txt, asda1Txt, lda1Txt;
    private Box tora2Box, toda2Box, asda2Box, lda2Box;
    private Text tora2Txt, toda2Txt, asda2Txt, lda2Txt;
    private Box tora3Box, toda3Box, asda3Box, lda3Box;
    private Text tora3Txt, toda3Txt, asda3Txt, lda3Txt;
    private Box takeOffBox1, landBox1, takeOffBox2, landBox2;
    private Text runwayId1Txt, runwayId2Txt;
    private Translate zoom;
    private Scene scene;
    private static Group root ;
    private PerspectiveCamera camera;
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    private final Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);

    private int obstHeight = 15;
    private int obstWidth = 12;
    private int obstDepth = 12;
    private int runwayDepth = 150;
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
    private Runway runway;
    private int orientationAngle;
    private int obstacleDistFromCentralLine;
    private Group obstModel;
    private int colorType;


    public static final int NORMAL_COLORS = 0;
    public static final int PROTANOPIA_COLORS = 1;
    public static final int DEUTERANOPIA_COLORS = 2;
    public static final int TRITANOPIA_COLORS = 3;

    public static final Color[] FLOOR_COLOR = {Color.GRAY, Color.rgb(150, 150, 150, 1), Color.rgb(150, 150, 150, 1), Color.rgb(150, 150, 150, 1)};
    public static final Color[] OBSTACLE_COLOR = {Color.DARKCYAN, Color.rgb(204, 204, 0, 1), Color.rgb(204, 204, 0, 1), Color.rgb(165, 15, 21, 1)};
    public static final Color[] SLOPE1_COLOR = {Color.DARKGOLDENROD, Color.rgb(37, 37, 37, 1), Color.rgb(37, 37, 37, 1), Color.rgb(37, 37, 37, 1)};
    public static final Color[] SLOPE2_COLOR = {Color.DARKORANGE, Color.rgb(255, 255, 255, 1), Color.rgb(255, 255, 255, 1), Color.rgb(255, 255, 255, 1)};
    public static final Color[] SLOPE3_COLOR = {Color.CADETBLUE, Color.rgb(37, 37, 37, 1), Color.rgb(37, 37, 37, 1), Color.rgb(37, 37, 37, 1)};
    public static final Color[] SLOPE4_COLOR = {Color.BLUEVIOLET, Color.rgb(255, 255, 255, 1), Color.rgb(255, 255, 255, 1), Color.rgb(255, 255, 255, 1)};

    public static final Color[] THRESHOLD_COLOR = {Color.BLACK, Color.rgb(37, 37, 37, 1),Color.rgb(37, 37, 37, 1), Color.rgb(37, 37, 37, 1)};
    public static final Color[] CLEARWAY_COLOR = {Color.BLACK, Color.rgb(37, 37, 37, 1), Color.rgb(37, 37, 37, 1), Color.rgb(37, 37, 37, 1)};
    public static final Color[] STOPWAY_COLOR = {Color.ANTIQUEWHITE, Color.rgb(0, 0, 0, 1),  Color.rgb( 0, 0, 0, 1),  Color.rgb( 0, 0, 0, 1)};
    public static final Color[] BACKGROUND_COLOR = {Color.SKYBLUE, Color.rgb(106, 126, 189, 1), Color.rgb(160, 95, 190, 1), Color.rgb(107, 174, 214, 1)};
    public static final String[] TEXT_COLOR = {"rgb(0,0,0)", "rgb(37, 37, 37)", "rgb(37, 37, 37)", "rgb(37, 37, 37)"};
    public static final Color[] STRIP1_COLOR = {Color.LIGHTCORAL, Color.rgb(252, 174, 145, 1), Color.rgb(130, 215, 140, 1), Color.rgb(252, 174, 145, 1)};
    public static final Color[] STRIP2_COLOR = {Color.LIGHTSALMON, Color.rgb(251, 106, 74, 1), Color.rgb(100, 170, 45, 1), Color.rgb(225, 0, 125, 1)};

    public ThreeDVisuals(double width, double height) {
        super(root = new Group(), width, height, true, SceneAntialiasing.BALANCED);
    }

    public void init(Runway runway){
        this.init(runway, NORMAL_COLORS);

    }

    public void init (Runway runway, int colorType){
        this.colorType = colorType;
        this.runway = runway;
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
        this.orientationAngle = runway.getCompassHeading();
        this.obstacleDistFromCentralLine = runway.getDistanceFromCentrelineFor3D();

    //Code for importing a 3d model
//        ObjModelImporter objImporter = new ObjModelImporter();
//        try {
//            URL modelUrl = this.getClass().getResource(MODEL_FILE_NAME);
//            objImporter.read(modelUrl);
//        }
//        catch (ImportException e) {
//            e.printStackTrace();
//        }
//        MeshView[] meshViews = objImporter.getImport();
//        for (int i = 0; i < meshViews.length; i++) {
//            meshViews[i].setScaleX(obstHeight);
//            meshViews[i].setScaleY(obstHeight);
//            meshViews[i].setScaleZ(obstHeight);
//        }
//        obstModel = new Group(meshViews);
//        obstModel.setTranslateY(obstHeight/2);
        this.calculateClearWays();
        this.calculateStopWays();
        this.oldMethod();
    }


    public void setCompassOrientation(boolean isSet){
        if(isSet){
            rotateZ.setAngle(orientationAngle);
        }
        else {
            rotateZ.setAngle(PLAIN_ANGLE);
        }
        rotateX.setAngle(TOP_DOWN_ANGLE);
        rotateY.setAngle(PLAIN_ANGLE);
    }

    private void calculateStopWays() {
        this.stopway1Width = origVals1.getAsda() - origVals1.getTora();
        this.stopway2Width = origVals2.getAsda() - origVals2.getTora();
    }

    private void calculateClearWays() {
        this.clearway1Width = origVals1.getToda() - origVals1.getTora();
        this.clearway2Width = origVals2.getToda() - origVals2.getTora();
    }


    private void oldMethod() {

        camera = new PerspectiveCamera(true);
        floor = new Box(runwayLength, runwayHeight, runwayDepth);
        obstacle = new Box(obstWidth, obstHeight, obstDepth);
        slope = new Box(1500, 5, obstDepth);
        zoom = new Translate(0,0, -runwayLength*2.2);

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

        takeOffBox1 = new Box(runwayLength/3, 10, ARROW_SMALL);
        landBox1 = new Box(runwayLength/3, 10, ARROW_SMALL);
        takeOffBox2 = new Box(runwayLength/3, 10, ARROW_SMALL);
        landBox2 = new Box(runwayLength/3, 10, ARROW_SMALL);

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

        runwayId1Txt = new Text(runway.getStrip1().getStripId());
        runwayId2Txt = new Text(runway.getStrip2().getStripId());

        runwayId1Txt.setFont(new Font(80));
        runwayId1Txt.getTransforms().addAll( rotateX, new Translate(-runwayLength/2 - clearway2Width - 200, 0 ,0));

        runwayId2Txt.setFont(new Font(80));
        runwayId2Txt.getTransforms().addAll( rotateX, new Translate(runwayLength/2  +clearway1Width + 100, 0 ,0));

        Font myFont = new Font(FONT_SIZE);
        String textStyle = "-fx-fill: " + TEXT_COLOR[colorType] + ";";
        obstacle.setTranslateZ(obstacleDistFromCentralLine);



        //Shape props
        floor.setMaterial(new PhongMaterial(FLOOR_COLOR[colorType]));
        obstacle.setMaterial(new PhongMaterial(OBSTACLE_COLOR[colorType]));
        slope.setMaterial(new PhongMaterial(SLOPE1_COLOR[colorType]));
        clearway1.setMaterial(new PhongMaterial(CLEARWAY_COLOR[colorType]));
        clearway2.setMaterial(new PhongMaterial(CLEARWAY_COLOR[colorType]));
        stopway1.setMaterial(new PhongMaterial(STOPWAY_COLOR[colorType]));
        stopway2.setMaterial(new PhongMaterial(STOPWAY_COLOR[colorType]));
        dt1.setMaterial(new PhongMaterial(THRESHOLD_COLOR[colorType]));
        dt2.setMaterial(new PhongMaterial(THRESHOLD_COLOR[colorType]));
        takeOffBox1.setMaterial(new PhongMaterial(SLOPE1_COLOR[colorType]));
        landBox1.setMaterial(new PhongMaterial(SLOPE2_COLOR[colorType]));
        takeOffBox2.setMaterial(new PhongMaterial(SLOPE3_COLOR[colorType]));
        landBox2.setMaterial(new PhongMaterial(SLOPE4_COLOR[colorType]));



        obstacle.setTranslateY(- (obstHeight/2 + runwayHeight/2));
        obstacle.setTranslateX(obstPos);
//
        //so that the slopes do not overlap
        takeOffBox1.setTranslateZ( 2 * TEXT_ADJUSTMENT);
        landBox1.setTranslateZ(TEXT_ADJUSTMENT);
        takeOffBox2.setTranslateZ( - 2 * TEXT_ADJUSTMENT);
        landBox2.setTranslateZ( - TEXT_ADJUSTMENT);

        //so that the slopes are o the right place minding the main plane
        takeOffBox1.setTranslateY(-Math.sin(Math.toRadians(SLOPE_ANGLE)) * takeOffBox1.getWidth() / 2 - runwayHeight / 2);
        landBox1.setTranslateY(- Math.sin(Math.toRadians(SLOPE_ANGLE)) * landBox1.getWidth() / 2 - runwayHeight / 2);
        takeOffBox2.setTranslateY(-Math.sin(Math.toRadians(SLOPE_ANGLE)) * takeOffBox2.getWidth() / 2 - runwayHeight / 2);
        landBox2.setTranslateY(- Math.sin(Math.toRadians(SLOPE_ANGLE)) * landBox2.getWidth() / 2 - runwayHeight / 2);

        //distance from the slope to the obstacle
        double slopeFromObst = obstHeight / (Math.tan(Math.toRadians(SLOPE_ANGLE)) * 2);
        slopeFromObst = Math.max(slopeFromObst, 240) + STRIPEND;
        if(runway.getStrip1().getRecVal().getTakeoff().equals(MathHandler.TAKEOFF_TOWARDS)){
            takeOffBox1.setTranslateX(obstPos - slopeFromObst);
            takeOffBox1.getTransforms().add(new Rotate(180 - SLOPE_ANGLE, Rotate.Z_AXIS));
            root.getChildren().add(takeOffBox1);
        }

        if(runway.getStrip2().getRecVal().getTakeoff().equals(MathHandler.TAKEOFF_TOWARDS)){
            takeOffBox2.setTranslateX( obstPos + slopeFromObst );
            takeOffBox2.getTransforms().add(new Rotate(SLOPE_ANGLE, Rotate.Z_AXIS));
            root.getChildren().add(takeOffBox2);
        }

        if(runway.getStrip1().getRecVal().getLanding().equals(MathHandler.LAND_OVER)){
            landBox1.setTranslateX( obstPos + slopeFromObst);
            landBox1.getTransforms().add(new Rotate(SLOPE_ANGLE, Rotate.Z_AXIS));
            root.getChildren().add(landBox1);
        }

        if(runway.getStrip2().getRecVal().getLanding().equals(MathHandler.LAND_OVER)){
            landBox2.setTranslateX( obstPos - slopeFromObst);
            landBox2.getTransforms().add(new Rotate(180 - SLOPE_ANGLE, Rotate.Z_AXIS));
            root.getChildren().add(landBox2);
        }


        clearway1.setTranslateX(runwayLength / 2 + clearway1Width / 2);
        clearway2.setTranslateX(-(runwayLength / 2 + clearway2Width / 2));

        stopway1.setTranslateX(-(runwayLength/2 + stopway1Width/2));
        stopway2.setTranslateX(runwayLength / 2 + stopway2Width/2);

        dt1.setTranslateX(dt1Pos);
        dt2.setTranslateX(dt2Pos);


        //TOPDOWN
        toraBox.setMaterial(new PhongMaterial(STRIP1_COLOR[colorType]));
        toraTxt.setFont(myFont);
        toraTxt.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
        toraTxt.setStyle(textStyle);

        todaBox.setMaterial(new PhongMaterial(STRIP1_COLOR[colorType]));
        todaTxt.setFont(myFont);
        todaTxt.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
        todaTxt.setStyle(textStyle);

        asdaBox.setMaterial(new PhongMaterial(STRIP1_COLOR[colorType]));
        asdaTxt.setFont(myFont);
        asdaTxt.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
        asdaTxt.setStyle(textStyle);

        ldaBox.setMaterial(new PhongMaterial(STRIP1_COLOR[colorType]));
        ldaTxt.setFont(myFont);
        ldaTxt.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
        ldaTxt.setStyle(textStyle);


        //TOPDOWN (1)
        tora1Box.setMaterial(new PhongMaterial(STRIP2_COLOR[colorType]));
        tora1Txt.setFont(myFont);
        tora1Txt.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
        tora1Txt.setStyle(textStyle);

        toda1Box.setMaterial(new PhongMaterial(STRIP2_COLOR[colorType]));
        toda1Txt.setFont(myFont);
        toda1Txt.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
        toda1Txt.setStyle(textStyle);

        asda1Box.setMaterial(new PhongMaterial(STRIP2_COLOR[colorType]));
        asda1Txt.setFont(myFont);
        asda1Txt.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
        asda1Txt.setStyle(textStyle);

        lda1Box.setMaterial(new PhongMaterial(STRIP2_COLOR[colorType]));
        lda1Txt.setFont(myFont);
        lda1Txt.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
        lda1Txt.setStyle(textStyle);




        //SIDE (2)
        tora2Box.setMaterial(new PhongMaterial(STRIP1_COLOR[colorType]));
        tora2Txt.setFont(myFont);
        tora2Txt.setStyle(textStyle);

        toda2Box.setMaterial(new PhongMaterial(STRIP1_COLOR[colorType]));
        toda2Txt.setFont(myFont);
        toda2Txt.setStyle(textStyle);

        asda2Box.setMaterial(new PhongMaterial(STRIP1_COLOR[colorType]));
        asda2Txt.setFont(myFont);
        asda2Txt.setStyle(textStyle);

        lda2Box.setMaterial(new PhongMaterial(STRIP1_COLOR[colorType]));
        lda2Txt.setFont(myFont);
        lda2Txt.setStyle(textStyle);



        //SIDE (3)
        tora3Box.setMaterial(new PhongMaterial(STRIP2_COLOR[colorType]));
        tora3Txt.setFont(myFont);
        tora3Txt.getTransforms().add(new Rotate(180, Rotate.Y_AXIS));
        tora3Txt.setStyle(textStyle);

        toda3Box.setMaterial(new PhongMaterial(STRIP2_COLOR[colorType]));
        toda3Txt.setFont(myFont);
        toda3Txt.getTransforms().add(new Rotate(180, Rotate.Y_AXIS));
        toda3Txt.setStyle(textStyle);

        asda3Box.setMaterial(new PhongMaterial(STRIP2_COLOR[colorType]));
        asda3Txt.setFont(myFont);
        asda3Txt.getTransforms().add(new Rotate(180, Rotate.Y_AXIS));
        asda3Txt.setStyle(textStyle);

        lda3Box.setMaterial(new PhongMaterial(STRIP2_COLOR[colorType]));
        lda3Txt.setFont(myFont);
        lda3Txt.getTransforms().add(new Rotate(180, Rotate.Y_AXIS));
        lda3Txt.setStyle(textStyle);

        this.setTransXforStrip1();
        this.setTransXforStrip2();


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



        //Camera properties
        camera.setNearClip(0.1);
        camera.setFarClip(100000.0);
        camera.setVerticalFieldOfView(false);
        this.setSelectedView(TOP_DOWN_ANGLE);
        camera.getTransforms().addAll(rotateX, rotateY, rotateZ, zoom);

        PointLight light = new PointLight(Color.LIGHTGRAY);
        light.setTranslateY(-300);
        //Scene props
        this.setFill(BACKGROUND_COLOR[colorType]);
        this.setCamera(camera);

        //add elems
        root.getChildren().add(floor);
        root.getChildren().add(obstacle);
        root.getChildren().add(light);
        root.getChildren().addAll(clearway1, clearway2, stopway1, stopway2, dt1, dt2);
        root.getChildren().addAll(toraBox, toraTxt,todaBox, todaTxt, asdaBox, asdaTxt, ldaBox, ldaTxt);
        root.getChildren().addAll(tora1Box, tora1Txt,toda1Box, toda1Txt, asda1Box, asda1Txt, lda1Box, lda1Txt);
        root.getChildren().addAll(tora2Box, tora2Txt,toda2Box, toda2Txt, asda2Box, asda2Txt, lda2Box, lda2Txt);
        root.getChildren().addAll(tora3Box, tora3Txt,toda3Box, toda3Txt, asda3Box, asda3Txt, lda3Box, lda3Txt);
        root.getChildren().addAll(runwayId1Txt, runwayId2Txt);
        root.getChildren().add(new AmbientLight(Color.WHITE));

        this.setListeners();

        //Stage props
//        primaryStage.setTitle("Visualization");
//        this.setScene(scene);
//        primaryStage.setWidth(SCENE_WIDTH);
//        primaryStage.setHeight(SCENE_HEIGHT);
//        primaryStage.show();

    }

    private void setTransXforStrip1() {

        if(!runway.getStrip1().getRecVal().getTakeoff().equals(MathHandler.TAKEOFF_AWAY)) {
            toraBox.setTranslateX(-runwayLength / 2 + vals1.getTora() / 2);
            toraTxt.setTranslateX(-runwayLength / 2 + vals1.getTora() / 2);

            todaBox.setTranslateX(-runwayLength / 2 + vals1.getToda() / 2);
            todaTxt.setTranslateX(-runwayLength / 2 + vals1.getToda() / 2);

            asdaBox.setTranslateX(-runwayLength / 2 + vals1.getAsda() / 2);
            asdaTxt.setTranslateX(-runwayLength / 2 + vals1.getAsda() / 2);

            tora2Box.setTranslateX(-runwayLength / 2 + vals1.getTora() / 2);
            tora2Txt.setTranslateX(-runwayLength / 2 + vals1.getTora() / 2);

            toda2Box.setTranslateX(-runwayLength / 2 + vals1.getToda() / 2);
            toda2Txt.setTranslateX(-runwayLength / 2 + vals1.getToda() / 2);

            asda2Box.setTranslateX(-runwayLength / 2 + vals1.getAsda() / 2);
            asda2Txt.setTranslateX(-runwayLength / 2 + vals1.getAsda() / 2);


        }
        else{
            toraBox.setTranslateX( runwayLength / 2 - vals1.getTora() / 2 );
            toraTxt.setTranslateX(runwayLength / 2 - vals1.getTora() / 2 );

            todaBox.setTranslateX( runwayLength/ 2 - vals1.getToda() / 2 + clearway1Width);
            todaTxt.setTranslateX( runwayLength/ 2 - vals1.getToda() / 2 + clearway1Width);

            asdaBox.setTranslateX( runwayLength/ 2 - vals1.getAsda() / 2 + stopway1Width);
            asdaTxt.setTranslateX( runwayLength/ 2 - vals1.getAsda() / 2 + stopway1Width);

            tora2Box.setTranslateX( runwayLength / 2 - vals1.getTora() / 2 );
            tora2Txt.setTranslateX( runwayLength / 2 - vals1.getTora() / 2);

            toda2Box.setTranslateX(runwayLength/ 2 - vals1.getToda() / 2 + clearway1Width);
            toda2Txt.setTranslateX(runwayLength/ 2 - vals1.getToda() / 2 + clearway1Width);

            asda2Box.setTranslateX(runwayLength/ 2 - vals1.getAsda() / 2 + stopway1Width);
            asda2Txt.setTranslateX(runwayLength/ 2 - vals1.getAsda() / 2 + stopway1Width);


        }
        if(runway.getStrip1().getRecVal().getLanding().equals(MathHandler.LAND_TOWARDS)){

            ldaBox.setTranslateX(vals1.getLda() / 2 + dt1Pos);
            ldaTxt.setTranslateX(vals1.getLda() / 2 + dt1Pos);

            lda2Box.setTranslateX(vals1.getLda() / 2 + dt1Pos);
            lda2Txt.setTranslateX(vals1.getLda() / 2 + dt1Pos);
        }
        else{
            ldaBox.setTranslateX(runwayLength/2 - vals1.getLda() / 2);
            ldaTxt.setTranslateX(runwayLength/2 - vals1.getLda() / 2);

            lda2Box.setTranslateX(runwayLength/2 - vals1.getLda() / 2);
            lda2Txt.setTranslateX(runwayLength/2 - vals1.getLda() / 2);
        }
    }

    private void setTransXforStrip2() {


        if(!runway.getStrip2().getRecVal().getTakeoff().equals(MathHandler.TAKEOFF_AWAY)){

            tora1Box.setTranslateX(runwayLength / 2 - vals2.getTora() / 2);
            tora1Txt.setTranslateX(runwayLength / 2 - vals2.getTora() / 2);

            toda1Box.setTranslateX(runwayLength / 2 - vals2.getToda() / 2);
            toda1Txt.setTranslateX(runwayLength / 2 - vals2.getToda() / 2);

            asda1Box.setTranslateX(runwayLength / 2 - vals2.getAsda() / 2);
            asda1Txt.setTranslateX(runwayLength / 2 - vals2.getAsda() / 2);

            tora3Box.setTranslateX(runwayLength / 2 - vals2.getTora() / 2);
            tora3Txt.setTranslateX(runwayLength / 2 - vals2.getTora() / 2);

            toda3Box.setTranslateX(runwayLength / 2 - vals2.getToda() / 2);
            toda3Txt.setTranslateX(runwayLength / 2 - vals2.getToda() / 2);

            asda3Box.setTranslateX(runwayLength / 2 - vals2.getAsda() / 2);
            asda3Txt.setTranslateX(runwayLength / 2 - vals2.getAsda() / 2);


        }
        else{
            tora1Box.setTranslateX( -(runwayLength / 2 - vals2.getTora() / 2 ));
            tora1Txt.setTranslateX(-(runwayLength / 2 - vals2.getTora() / 2 ));

            toda1Box.setTranslateX( -(runwayLength/ 2 - vals2.getToda() / 2 + clearway2Width));
            toda1Txt.setTranslateX( -(runwayLength/ 2 - vals2.getToda() / 2 + clearway2Width));

            asda1Box.setTranslateX( -(runwayLength/ 2 - vals2.getAsda() / 2 + stopway2Width));
            asda1Txt.setTranslateX( -(runwayLength/ 2 - vals2.getAsda() / 2 + stopway2Width));

            tora3Box.setTranslateX( -(runwayLength / 2 - vals2.getTora() / 2 ));
            tora3Txt.setTranslateX( -(runwayLength / 2 - vals2.getTora() / 2));

            toda3Box.setTranslateX( -(runwayLength/ 2 - vals2.getToda() / 2 + clearway2Width));
            toda3Txt.setTranslateX( -(runwayLength/ 2 - vals2.getToda() / 2 + clearway2Width));

            asda3Box.setTranslateX( -(runwayLength/ 2 - vals2.getAsda() / 2 + stopway2Width));
            asda3Txt.setTranslateX( -(runwayLength/ 2 - vals2.getAsda() / 2 + stopway2Width));
        }
        if(!runway.getStrip2().getRecVal().getLanding().equals(MathHandler.LAND_OVER)){
            lda1Box.setTranslateX(dt2Pos - vals2.getLda() / 2);
            lda1Txt.setTranslateX(dt2Pos - vals2.getLda() / 2);

            lda3Box.setTranslateX(dt2Pos - vals2.getLda() / 2);
            lda3Txt.setTranslateX(dt2Pos - vals2.getLda() / 2);
        }
        else{
            lda1Box.setTranslateX(-runwayLength / 2 + vals2.getLda() / 2);
            lda1Txt.setTranslateX(-runwayLength / 2 + vals2.getLda() / 2);

            lda3Box.setTranslateX(-runwayLength / 2 + vals2.getLda() / 2);
            lda3Txt.setTranslateX(-runwayLength / 2 + vals2.getLda() / 2);
        }

    }

    private void setListeners(){
        this.setOnScroll(
                new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent event) {
                        double zoomFactor = 1.1;
                        double deltaY = event.getDeltaY();
                        if (deltaY > 0) {
                            zoomFactor = 2.0 - zoomFactor;
                        }
                        zoom.setZ(zoom.getZ() * zoomFactor);

                        event.consume();
                    }
                });


        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseOldX = event.getX();
                mouseOldY = event.getY();
            }
        });
        this.setOnMouseDragged(new EventHandler<MouseEvent>() {
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


    public void setSelectedView(int angle){
        rotateX.setAngle(angle);
        rotateY.setAngle(PLAIN_ANGLE);
        rotateZ.setAngle(PLAIN_ANGLE);
    }

    public void setHorizontalRotation(int angle){
        rotateX.setAngle(angle);
    }

    public void setVerticalRotation(int angle){
        rotateY.setAngle(angle);
    }

    public void setZRotation(int angle){
        rotateZ.setAngle(angle);
    }


//    public static void main(String[] args) {
//        launch(args);
//    }
}
