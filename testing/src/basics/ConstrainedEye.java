package basics;

import common.InteractiveNode;
import frames.core.Node;
import frames.primitives.Vector;
import frames.primitives.constraint.AxisPlaneConstraint;
import frames.primitives.constraint.LocalConstraint;
import frames.primitives.constraint.WorldConstraint;
import frames.processing.Scene;
import processing.core.PApplet;
import processing.core.PFont;

/**
 * Created by pierre on 11/15/16.
 */
public class ConstrainedEye extends PApplet {
  Scene scene;
  PFont myFont;
  Node eye;

  private int transDir;
  private int rotDir;
  AxisPlaneConstraint constraints[] = new AxisPlaneConstraint[2];
  int activeConstraint;

  public void settings() {
    size(800, 800, P3D);
  }

  public void setup() {
    //rectMode(CENTER);
    myFont = loadFont("FreeSans-13.vlw");
    textFont(myFont);
    scene = new Scene(this);
    scene.setRadius(400);
    scene.fitBallInterpolation();

    eye = new InteractiveNode(scene);
    eye.setDamping(0f);
    //eye.setRotationSensitivity(0.1f);
    //eye.setSpinningSensitivity(1);
    scene.setEye(eye);
    scene.setFieldOfView(PI / 3);
    scene.setDefaultGrabber(eye);
    scene.fitBallInterpolation();

    constraints[0] = new WorldConstraint();
    // Note that an EyeConstraint(eye) would produce the same results:
    // An EyeConstraint is a LocalConstraint when applied to the camera frame !
    constraints[1] = new LocalConstraint();
    transDir = 0;
    rotDir = 0;
    activeConstraint = 0;
    eye.setConstraint(constraints[activeConstraint]);
  }

  public void draw() {
    background(0);
    scene.drawAxes();
    fill(204, 102, 0, 150);
    scene.drawTorusSolenoid();
    fill(0, 0, 255);
    scene.beginScreenCoordinates();
    displayText();
    scene.endScreenCoordinates();
  }

  public void keyPressed() {
    if (key == 'b' || key == 'B') {
      rotDir = (rotDir + 1) % 3;
    }
    if (key == 'd' || key == 'D') {
      transDir = (transDir + 1) % 3;
    }
    if (key == 'u' || key == 'U') {
      changeConstraint();
    }
    if (key == 't' || key == 'T') {
      constraints[activeConstraint].setTranslationConstraintType(nextTranslationConstraintType(constraints[activeConstraint].translationConstraintType()));
    }
    if (key == 'r' || key == 'R') {
      constraints[activeConstraint].setRotationConstraintType(nextRotationConstraintType(constraints[activeConstraint].rotationConstraintType()));
    }

    Vector dir = new Vector(0.0f, 0.0f, 0.0f);
    switch (transDir) {
      case 0:
        dir.setX(1.0f);
        break;
      case 1:
        dir.setY(1.0f);
        break;
      case 2:
        dir.setZ(1.0f);
        break;
    }

    constraints[activeConstraint].setTranslationConstraintDirection(dir);

    dir.set(0.0f, 0.0f, 0.0f);
    switch (rotDir) {
      case 0:
        dir.setX(1.0f);
        break;
      case 1:
        dir.setY(1.0f);
        break;
      case 2:
        dir.setZ(1.0f);
        break;
    }
    constraints[activeConstraint].setRotationConstraintDirection(dir);
  }

  public static AxisPlaneConstraint.Type nextTranslationConstraintType(AxisPlaneConstraint.Type type) {
    AxisPlaneConstraint.Type rType;
    switch (type) {
      case FREE:
        rType = AxisPlaneConstraint.Type.PLANE;
        break;
      case PLANE:
        rType = AxisPlaneConstraint.Type.AXIS;
        break;
      case AXIS:
        rType = AxisPlaneConstraint.Type.FORBIDDEN;
        break;
      case FORBIDDEN:
        rType = AxisPlaneConstraint.Type.FREE;
        break;
      default:
        rType = AxisPlaneConstraint.Type.FREE;
    }
    return rType;
  }

  public static AxisPlaneConstraint.Type nextRotationConstraintType(AxisPlaneConstraint.Type type) {
    AxisPlaneConstraint.Type rType;
    switch (type) {
      case FREE:
        rType = AxisPlaneConstraint.Type.AXIS;
        break;
      case PLANE:
        rType = AxisPlaneConstraint.Type.FREE;
        break;
      case AXIS:
        rType = AxisPlaneConstraint.Type.FORBIDDEN;
        break;
      case FORBIDDEN:
        rType = AxisPlaneConstraint.Type.FREE;
        break;
      default:
        rType = AxisPlaneConstraint.Type.FREE;
    }
    return rType;
  }

  private void changeConstraint() {
    int previous = activeConstraint;
    activeConstraint = (activeConstraint + 1) % 2;

    constraints[activeConstraint].setTranslationConstraintType(constraints[previous].translationConstraintType());
    constraints[activeConstraint].setTranslationConstraintDirection(constraints[previous].translationConstraintDirection());
    constraints[activeConstraint].setRotationConstraintType(constraints[previous].rotationConstraintType());
    constraints[activeConstraint].setRotationConstraintDirection(constraints[previous].rotationConstraintDirection());

    eye.setConstraint(constraints[activeConstraint]);
  }

  protected void displayType(AxisPlaneConstraint.Type type, int x, int y, char c) {
    String textToDisplay = new String();
    switch (type) {
      case FREE:
        textToDisplay = "FREE (";
        textToDisplay += c;
        textToDisplay += ")";
        break;
      case PLANE:
        textToDisplay = "PLANE (";
        textToDisplay += c;
        textToDisplay += ")";
        break;
      case AXIS:
        textToDisplay = "AXIS (";
        textToDisplay += c;
        textToDisplay += ")";
        break;
      case FORBIDDEN:
        textToDisplay = "FORBIDDEN (";
        textToDisplay += c;
        textToDisplay += ")";
        break;
    }
    text(textToDisplay, x, y);
  }

  protected void displayDir(int dir, int x, int y, char c) {
    String textToDisplay = new String();
    switch (dir) {
      case 0:
        textToDisplay = "X (";
        textToDisplay += c;
        textToDisplay += ")";
        break;
      case 1:
        textToDisplay = "Y (";
        textToDisplay += c;
        textToDisplay += ")";
        break;
      case 2:
        textToDisplay = "Z (";
        textToDisplay += c;
        textToDisplay += ")";
        break;
    }
    text(textToDisplay, x, y);
  }

  public void displayText() {
    text("TRANSLATION :", 350, height - 30);
    displayDir(transDir, (350 + 105), height - 30, 'D');
    displayType(constraints[activeConstraint].translationConstraintType(), 350, height - 60, 'T');

    text("ROTATION :", width - 120, height - 30);
    displayDir(rotDir, width - 40, height - 30, 'B');
    displayType(constraints[activeConstraint].rotationConstraintType(), width - 120, height - 60, 'R');

    switch (activeConstraint) {
      case 0:
        text("Constraint direction defined w/r to WORLD (U)", 350, 20);
        break;
      case 1:
        text("Constraint direction defined w/r to EYE (U)", 370, 20);
        break;
    }
  }

  public static void main(String args[]) {
    PApplet.main(new String[]{"basics.ConstrainedEye"});
  }
}
