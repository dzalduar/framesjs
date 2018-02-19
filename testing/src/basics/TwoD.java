package basics;

import frames.core.Frame;
import processing.core.PApplet;
import frames.core.Graph;
import frames.input.Shortcut;
import frames.input.event.KeyEvent;
import frames.input.event.KeyShortcut;
import frames.input.event.MotionEvent;
import frames.primitives.Quaternion;
import frames.primitives.Vector;
import frames.processing.Scene;

public class TwoD extends PApplet {
  Scene scene;
  Frame eye, frame;
  boolean target = true;

  public void info() {
    println(scene.radius());
    scene.center().print();
    scene.eye().position().print();
    println(scene.zNear());
    println(scene.zFar());
    scene.matrixHandler().projection().print();
    scene.matrixHandler().cacheView().print();
    scene.matrixHandler().modelView().print();
  }

  @Override
  public void settings() {
    size(800, 800, P2D);
  }

  @Override
  public void setup() {
    rectMode(CENTER);
    scene = new Scene(this);
    if (scene.is3D())
      scene.setType(Graph.Type.ORTHOGRAPHIC);

    frame = new InteractiveFrame(new Vector(40, -80, 0), new Quaternion(QUARTER_PI));
    eye = new InteractiveFrame();

    scene.setEye(eye);
    scene.setDefaultNode(eye);
    scene.setRadius(200);
    scene.fitBallInterpolation();
  }

  @Override
  public void draw() {
    background(0);
    scene.drawAxes(scene.radius());
    pushMatrix();
    frame.applyTransformation();
    if (frame.grabsInput())
      fill(255, 0, 0);
    else
      fill(0, 255, 0);
    rect(0, 0, 50, 100);
    popMatrix();

    if (target) {
      pushStyle();
      stroke(255);
      strokeWeight(2);
      scene.drawPickingTarget(frame);
      popStyle();
    }

    scene.beginScreenCoordinates();
    fill(0, 0, 255);
    rect(80, 80, 50, 100);
    scene.endScreenCoordinates();
  }

  @Override
  public void keyPressed() {
    if (key == 'f')
      target = !target;
    if (key == ' ')
      scene.flip();
    if (key == 'i')
      info();
  }

  public class InteractiveFrame extends Frame {
    Shortcut left = new Shortcut(PApplet.LEFT);
    Shortcut right = new Shortcut(PApplet.RIGHT);
    Shortcut wheel = new Shortcut(processing.event.MouseEvent.WHEEL);
    KeyShortcut upArrow = new KeyShortcut(PApplet.UP);
    KeyShortcut downArrow = new KeyShortcut(PApplet.DOWN);
    KeyShortcut leftArrow = new KeyShortcut(PApplet.LEFT);
    KeyShortcut rightArrow = new KeyShortcut(PApplet.RIGHT);

    public InteractiveFrame() {
      super(scene);
    }

    public InteractiveFrame(Vector position, Quaternion quaternion) {
      super(scene, null, position, quaternion, 1);
    }

    protected InteractiveFrame(Graph otherGraph, InteractiveFrame otherFrame) {
      super(otherGraph, otherFrame);
    }

    @Override
    public InteractiveFrame get() {
      return new InteractiveFrame(this.graph(), this);
    }

    @Override
    public void interact(MotionEvent event) {
      if (event.shortcut().matches(left))
        translate(event);
      else if (event.shortcut().matches(right))
        rotate(event);
      else if (event.shortcut().matches(wheel))
        if (isEye() && graph().is3D())
          translateZ(event);
        else
          scale(event);
    }

    @Override
    public void interact(KeyEvent event) {
      if (event.shortcut().matches(upArrow))
        translateYPos();
      else if (event.shortcut().matches(downArrow))
        translateYNeg();
      else if (event.shortcut().matches(leftArrow))
        translateXNeg();
      else if (event.shortcut().matches(rightArrow))
        translateXPos();
    }
  }

  public static void main(String args[]) {
    PApplet.main(new String[]{"basics.TwoD"});
  }
}
