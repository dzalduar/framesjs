package demos;

import common.InteractiveShape;
import frames.core.constraint.AxisPlaneConstraint;
import frames.core.constraint.LocalConstraint;
import frames.core.constraint.WorldConstraint;
import frames.primitives.Quaternion;
import frames.primitives.Vector;
import frames.processing.Scene;
import processing.core.PConstants;
import processing.core.PGraphics;

/**
 * Lamp by Jean Pierre Charalambos.
 * <p>
 * This class is part of the Luxo example.
 * <p>
 * Add a graphics handler to an InteractiveFrame to automatically pick
 * an object. The object is described in the graphics handler procedure.
 * <p>
 * Press 'h' to toggle the mouse and keyboard navigation help.
 */

public class Lamp implements PConstants {
  Scene scene;
  LampShape[] frameArray;

  Lamp(Scene s) {
    scene = s;
    frameArray = new LampShape[4];

    for (int i = 0; i < 4; ++i) {
      frameArray[i] = new LampShape(scene);
      frame(i).setReference(i > 0 ? frameArray[i - 1] : null);
      frame(i).setHighlighting(InteractiveShape.Highlighting.FRONT);
    }

    // Initialize frames
    frame(1).setTranslation(0f, 0f, 8f); // Base height
    frame(2).setTranslation(0, 0, 50);  // Arm length
    frame(3).setTranslation(0, 0, 50);  // Arm length

    frame(1).setRotation(new Quaternion(new Vector(1.0f, 0.0f, 0.0f), 0.6f));
    frame(2).setRotation(new Quaternion(new Vector(1.0f, 0.0f, 0.0f), -2.0f));
    frame(3).setRotation(new Quaternion(new Vector(1.0f, -0.3f, 0.0f), -1.7f));

    //graphics handlers
    frame(0).mode = 1;
    frame(1).mode = 2;
    frame(2).mode = 2;
    frame(3).mode = 3;

    // Set frame constraints
    WorldConstraint baseConstraint = new WorldConstraint();
    baseConstraint.setTranslationConstraint(AxisPlaneConstraint.Type.PLANE, new Vector(0.0f, 0.0f, 1.0f));
    baseConstraint.setRotationConstraint(AxisPlaneConstraint.Type.AXIS, new Vector(0.0f, 0.0f, 1.0f));
    frame(0).setConstraint(baseConstraint);

    LocalConstraint XAxis = new LocalConstraint();
    XAxis.setTranslationConstraint(AxisPlaneConstraint.Type.FORBIDDEN, new Vector(0.0f, 0.0f, 0.0f));
    XAxis.setRotationConstraint(AxisPlaneConstraint.Type.AXIS, new Vector(1.0f, 0.0f, 0.0f));
    frame(1).setConstraint(XAxis);
    frame(2).setConstraint(XAxis);

    LocalConstraint headConstraint = new LocalConstraint();
    headConstraint.setTranslationConstraint(AxisPlaneConstraint.Type.FORBIDDEN, new Vector(0.0f, 0.0f, 0.0f));
    frame(3).setConstraint(headConstraint);
  }

  public LampShape frame(int i) {
    return frameArray[i];
  }

  public class LampShape extends InteractiveShape {
    int mode;

    public LampShape(Scene scene) {
      super(scene);
    }

    public void drawCone(PGraphics pg, float zMin, float zMax, float r1, float r2, int nbSub) {
      pg.translate(0.0f, 0.0f, zMin);
      Scene.drawCone(pg, nbSub, 0, 0, r1, r2, zMax - zMin);
      pg.translate(0.0f, 0.0f, -zMin);
    }

    @Override
    public void set(PGraphics pGraphics) {
      switch (mode) {
        case 1:
          pGraphics.fill(grabsInput() ? 255 : 0, 0, 255);
          drawCone(pGraphics, 0, 3, 15, 15, 30);
          drawCone(pGraphics, 3, 5, 15, 13, 30);
          drawCone(pGraphics, 5, 7, 13, 1, 30);
          drawCone(pGraphics, 7, 9, 1, 1, 10);
          break;
        case 2:
          pGraphics.pushMatrix();
          pGraphics.rotate(HALF_PI, 0, 1, 0);
          drawCone(pGraphics, -5, 5, 2, 2, 20);
          pGraphics.popMatrix();

          pGraphics.translate(2, 0, 0);
          drawCone(pGraphics, 0, 50, 1, 1, 10);
          pGraphics.translate(-4, 0, 0);
          drawCone(pGraphics, 0, 50, 1, 1, 10);
          pGraphics.translate(2, 0, 0);
          break;
        case 3:
          pGraphics.fill(0, 255, grabsInput() ? 0 : 255);
          drawCone(pGraphics, -2, 6, 4, 4, 30);
          drawCone(pGraphics, 6, 15, 4, 17, 30);
          drawCone(pGraphics, 15, 17, 17, 17, 30);
          pGraphics.spotLight(155, 255, 255, 0, 0, 0, 0, 0, 1, THIRD_PI, 1);
          break;
      }
    }
  }
}