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
 * Grabbers are means to attach a user-space object to all the
 * {@link Agent}s (see
 * {@link Agent#addGrabber(Grabber)}) through which it's going to be
 * handled. For details, refer to the {@link Agent} documentation.
 */
public interface Grabber {
  /**
   * Defines the rules to set the grabber as an agent inputGrabber-grabber.
   *
   * @see Agent#poll(Event)
   * @see Agent#inputGrabber()
   */
  boolean track(Event event);

  /**
   * Defines how the grabber should react according to the given event.
   *
   * @see Agent#handle(Event)
   */
  void interact(Event event);
}
