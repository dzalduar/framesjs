framesjs
[![All Contributors](https://img.shields.io/badge/all_contributors-4-orange.svg?style=flat-square)](#contributors)
===========================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================

**Table of Contents**

- [Description](#user-content-description)
- [Usage](#user-content-usage)
- [Interpolators](#user-content-interpolators)
- [Interactivity](#user-content-interactivity)
- [Drawing](#user-content-drawing)
- [Installation](#user-content-installation)
- [Contributors](#user-content-contributors)

## Description

Simple, expressive, language-agnostic, and extensible [(2D/3D) scene graph](https://en.wikipedia.org/wiki/Scene_graph) of [frames](https://en.wikipedia.org/wiki/Frame_of_reference) featuring interaction, inverse kinematics, visualization and animation frameworks and supporting advanced (onscreen/offscreen) rendering techniques, such as [view frustum culling](http://cgvr.informatik.uni-bremen.de/teaching/cg_literatur/lighthouse3d_view_frustum_culling/index.html).

*Framesjs* is meant to be coupled with third party raster and ray-tracing renderers. The _master git branch_ (the one you're looking at) is coupled with all [p5.js](https://p5js.org/) renderers.

## Usage

Typical usage comprises three steps: scene instantiation, setting an eye and setting some shapes.

### Scene instantiation

Instantiate your on-screen scene at the [setup()](https://p5js.org/reference/#/p5/setup):

```js
var scene;
function setup() {
  scene = new Scene(this);
}
```

The `scene` [frontBuffer()](https://visualcomputing.github.io/frames-javadocs/frames/processing/Scene.html#frontBuffer--) corresponds to the *p5.js* main canvas.
 
Off-screen scenes should be instantiated upon a [PGraphics](https://p5js.org/reference/#/p5.Graphics) object:

```js
var scene;
var canvas;
function setup() {
  canvas = createGraphics(500, 500, P3D);
  scene = new Scene(this, canvas);
}
```

In this case, the `scene` [frontBuffer()](https://visualcomputing.github.io/frames-javadocs/frames/processing/Scene.html#frontBuffer--) corresponds to the `canvas`.

### The eye

The scene eye can be an instance of [Frame](https://visualcomputing.github.io/frames-javadocs/frames/primitives/Frame.html) or [Node](https://visualcomputing.github.io/frames-javadocs/frames/core/Node.html). To set the eye from a `frame` instance use code such as the following:

```js
...
var eye;
function setup() {
  ...
  eye = new Frame();
  scene.setEye(eye);
}
```

The eye can be controlled programmatically using the powerful [Frame](https://visualcomputing.github.io/frames-javadocs/frames/primitives/Frame.html) API.

To set the eye from a node instance use code such as the following:

```js
...
var eye;
function setup() {
  ...
  eye = new Node(scene) {
    @Override
    function interact(event) {
      // translate the node from a LEFT mouse button drag
      if (event.shortcut().matches(new Shortcut(LEFT)))
        translate(event);
    }
  };
  scene.setEye(eye);
  scene.setDefaultGrabber(eye);
}
```

The eye can be controlled both programmatically (since a [Node](https://visualcomputing.github.io/frames-javadocs/frames/core/Node.html) is a [Frame](https://visualcomputing.github.io/frames-javadocs/frames/primitives/Frame.html) specialization) and interactively (using the mouse, see [mouse()](https://visualcomputing.github.io/frames-javadocs/frames/processing/Scene.html#mouse--) and [Mouse](https://visualcomputing.github.io/frames-javadocs/frames/processing/Mouse.html)). Observe the anonymous inner [Node](https://visualcomputing.github.io/frames-javadocs/frames/core/Node.html) class intance which is used to define how the node will behave, refer to the [Node](https://visualcomputing.github.io/frames-javadocs/frames/core/Node.html) API for details. Note also the [setDefaultGrabber(Grabber)](https://visualcomputing.github.io/frames-javadocs/frames/core/Graph.html#setDefaultGrabber-frames.input.Grabber-) call which will direct user input (e.g., mouse) to the eye when no other node is being picked.

### Shapes

A [Shape](https://visualcomputing.github.io/frames-javadocs/frames/processing/Shape.html) is a [Node](https://visualcomputing.github.io/frames-javadocs/frames/core/Node.html) specialization that can be set from a retained-mode rendering *p5.js* [PShape](https://p5js.org/reference/#/p5.Geometry) or from an immediate-mode rendering *p5.js* procedure. Shapes can be picked precisely using their projection onto the screen, see [setPrecision(Node.Precision)](https://visualcomputing.github.io/frames-javadocs/frames/processing/Shape.html#setPrecision-frames.core.Node.Precision-). Use [traverse()](https://visualcomputing.github.io/frames-javadocs/frames/processing/Scene.html#traverse--) to render all scene-graph shapes or [draw()](https://visualcomputing.github.io/frames-javadocs/frames/processing/Shape.html#draw--) to render a specific one instead.

#### Retained-mode shapes

To set a retained-mode shape use `Shape shape = new Shape(Scene scene, PShape shape)` or `Shape shape = new Shape(Scene scene)` and then call [Shape.set(PShape)](https://visualcomputing.github.io/frames-javadocs/frames/processing/Shape.html#set-processing.core.PShape-).

#### Immediate-mode shapes

Immediate-mode shapes should override `Shape.set(PGraphics)`, e.g., using an anonymous inner [Shape](https://visualcomputing.github.io/frames-javadocs/frames/processing/Shape.html#set-processing.core.PShape-) class intance, such as with the following:
 
```js
...
var shape;
function setup() {
  ...
  shape = new Shape(scene) {
    @Override
    function set(canvas) {
      //immediate-mode rendering procedure
    }
  };
}
```

Note tha shapes like nodes can be controlled interactively by overriding [interact(Event)](https://visualcomputing.github.io/frames-javadocs/frames/core/Node.html#interact-frames.input.Event-), like it has been done above.

## Interpolators

A frame (and hence a node or a shape) can be animated through a key-frame [Catmull-Rom](https://en.wikipedia.org/wiki/Cubic_Hermite_spline#Catmull%E2%80%93Rom_spline) interpolator path. Use code such as the following:

```js
var scene;
var pshape;
var shape;
var interpolator;
function setup() {
  ...
  shape = new Shape(scene, pshape);
  interpolator = new Interpolator(shape);
  for (var i = 0; i < random(4, 10); i++)
    interpolator.addKeyFrame(Node.random(scene));
  interpolator.start();
}
```

which will create a random interpolator path containing [4..10] key-frames. The interpolation is also started. The interpolator path may be drawn with code like this:

```js
...
function draw() {
  scene.traverse();
  scene.drawPath(interpolator, 5);
}
```

while `traverse()` will draw the animated shape(s) `drawPath(Interpolator, int)` will draw the interpolated path too.
 
## Interactivity
 
To control your scene nodes by means different than the [mouse()](https://visualcomputing.github.io/frames-javadocs/frames/processing/Scene.html#mouse--) (see [Mouse](https://visualcomputing.github.io/frames-javadocs/frames/processing/Mouse.html)), implement an [Agent](https://visualcomputing.github.io/frames-javadocs/frames/input/Agent.html) and call [registerAgent(Agent)](https://visualcomputing.github.io/frames-javadocs/frames/core/Graph.html#registerAgent-frames.input.Agent-).

## Drawing

The [Scene](https://visualcomputing.github.io/frames-javadocs/frames/processing/Scene.html) implements several static drawing functions that complements those already provided by *p5,js*, such as: `drawCylinder(PGraphics, int, float, float)}`, `drawHollowCylinder(PGraphics, int, float, float, Vector, Vector)`, `drawCone(PGraphics, int, float, float, float, float)`, `drawCone(PGraphics, int, float, float, float, float, float)` and `drawTorusSolenoid(PGraphics, int, int, float, float)`.

Drawing functions that take a `PGraphics` parameter (including the above static ones), such as `beginScreenCoordinates(PGraphics)`,
`endScreenCoordinates(PGraphics)`, `drawAxes(PGraphics, float)`, `drawCross(PGraphics, float, float, float)` and `drawGrid(PGraphics)` among others, can be used to set a `Shape` (see [set(PGraphics)](https://visualcomputing.github.io/frames-javadocs/frames/processing/Shape.html#set-processing.core.PShape-)).

Another scene's eye (different than this one) can be drawn with `drawEye(Graph)`. Typical usage include interactive [minimaps](https://en.wikipedia.org/wiki/Mini-map) and _visibility culling_ visualization and debugging.

## Installation

Pending

## Contributors

Thanks goes to these wonderful people ([emoji key](https://github.com/kentcdodds/all-contributors#emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore -->
| [<img src="https://avatars2.githubusercontent.com/u/645599?v=4" width="100px;"/><br /><sub><b>Jean Pierre Charalambos</b></sub>](https://github.com/nakednous)<br />[📝](#blog-nakednous "Blogposts") [🐛](https://github.com/VisualComputing/proscene.js/issues?q=author%3Anakednous "Bug reports") [💻](https://github.com/VisualComputing/proscene.js/commits?author=nakednous "Code") [🎨](#design-nakednous "Design") [📖](https://github.com/VisualComputing/proscene.js/commits?author=nakednous "Documentation") [📋](#eventOrganizing-nakednous "Event Organizing") [💡](#example-nakednous "Examples") [💵](#financial-nakednous "Financial") [🔍](#fundingFinding-nakednous "Funding Finding") [🤔](#ideas-nakednous "Ideas, Planning, & Feedback") [📦](#platform-nakednous "Packaging/porting to new platform") [🔌](#plugin-nakednous "Plugin/utility libraries") [💬](#question-nakednous "Answering Questions") [👀](#review-nakednous "Reviewed Pull Requests") [📢](#talk-nakednous "Talks") [⚠️](https://github.com/VisualComputing/proscene.js/commits?author=nakednous "Tests") [✅](#tutorial-nakednous "Tutorials") [📹](#video-nakednous "Videos") | [<img src="https://avatars2.githubusercontent.com/u/9769647?v=4" width="100px;"/><br /><sub><b>sechaparroc</b></sub>](https://github.com/sechaparroc)<br />[📝](#blog-sechaparroc "Blogposts") [🐛](https://github.com/VisualComputing/proscene.js/issues?q=author%3Asechaparroc "Bug reports") [💻](https://github.com/VisualComputing/proscene.js/commits?author=sechaparroc "Code") [🎨](#design-sechaparroc "Design") [📖](https://github.com/VisualComputing/proscene.js/commits?author=sechaparroc "Documentation") [💡](#example-sechaparroc "Examples") [🔍](#fundingFinding-sechaparroc "Funding Finding") [🤔](#ideas-sechaparroc "Ideas, Planning, & Feedback") [💬](#question-sechaparroc "Answering Questions") [📢](#talk-sechaparroc "Talks") [✅](#tutorial-sechaparroc "Tutorials") [📹](#video-sechaparroc "Videos") | [<img src="https://avatars2.githubusercontent.com/u/13878096?v=4" width="100px;"/><br /><sub><b>Jimmynex</b></sub>](https://github.com/jiapulidoar)<br />[📝](#blog-jiapulidoar "Blogposts") [🐛](https://github.com/VisualComputing/proscene.js/issues?q=author%3Ajiapulidoar "Bug reports") [💻](https://github.com/VisualComputing/proscene.js/commits?author=jiapulidoar "Code") [🎨](#design-jiapulidoar "Design") [📖](https://github.com/VisualComputing/proscene.js/commits?author=jiapulidoar "Documentation") [💡](#example-jiapulidoar "Examples") [🔍](#fundingFinding-jiapulidoar "Funding Finding") [🤔](#ideas-jiapulidoar "Ideas, Planning, & Feedback") [💬](#question-jiapulidoar "Answering Questions") [📢](#talk-jiapulidoar "Talks") [✅](#tutorial-jiapulidoar "Tutorials") [📹](#video-jiapulidoar "Videos") | [<img src="https://avatars2.githubusercontent.com/u/96923?v=4" width="100px;"/><br /><sub><b>xyos</b></sub>](https://github.com/xyos)<br />[💻](https://github.com/VisualComputing/proscene.js/commits?author=xyos "Code") [📢](#talk-xyos "Talks") [💡](#example-xyos "Examples") |
| :---: | :---: | :---: | :---: |
<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/kentcdodds/all-contributors) specification. Contributions of any kind welcome!
