/****************************************************************************************
 * frames
 * Copyright (c) 2018 National University of Colombia, https://visualcomputing.github.io/
 * @author Jean Pierre Charalambos, https://github.com/VisualComputing
 *
 * All rights reserved. A 2D or 3D scene graph library providing eye, input and timing
 * handling to a third party (real or non-real time) renderer. Released under the terms
 * of the GPL v3.0 which is available at http://www.gnu.org/licenses/gpl.html
 ****************************************************************************************/

package frames.input;

/**
 * Every {@link Event} instance has a shortcut which represents a
 * gesture-{@link #id()}. For instance, the button being dragged and the modifier key
 * pressed (see {@link #modifiers()}) at the very moment an user interaction takes place,
 * such as when she drags a giving mouse button while pressing the 'CTRL' modifier key.
 * See {@link Event#shortcut()}.
 * <p>
 * The current implementation supports the following event/shortcut types:
 * <ol>
 * <li>MotionEvent} / Shortcut}. Note that motion-event derived classes:
 * MotionEvent1, MotionEvent2, MotionEvent3 and MotionEvent6, are also
 * related to shortcuts.</li>
 * <li>TapEvent} / TapShortcut
 * </li>
 * </ol>
 */
public class Shortcut {
  protected int _modifiers;
  protected int _id;

  /**
   * Constructs an "empty" shortcut. Same as: {@link #Shortcut(int)} with the integer
   * parameter being NO_NOMODIFIER_MASK.
   */
  public Shortcut() {
    _modifiers = Event.NO_MODIFIER_MASK;
    _id = Event.NO_ID;
  }

  /**
   * Defines a shortcut from the given id.
   *
   * @param id gesture-id
   */
  public Shortcut(int id) {
    _modifiers = Event.NO_MODIFIER_MASK;
    this._id = id;
  }

  /**
   * Defines a shortcut from the given modifier mask and id
   *
   * @param modifiers modifier mask defining the shortcut
   */
  public Shortcut(int modifiers, int id) {
    _modifiers = modifiers;
    _id = id;
  }

  /**
   * Returns the shortcut's modifiers mask.
   */
  public int modifiers() {
    return _modifiers;
  }

  /**
   * Returns the shortcut's id.
   */
  public int id() {
    return _id;
  }

  /**
   * Returns whether or not this shortcut matches the other.
   *
   * @param other shortcut
   */
  public boolean matches(Shortcut other) {
    if (this.getClass() == other.getClass())
      return id() == other.id() && modifiers() == other.modifiers();
    return false;
  }
}
