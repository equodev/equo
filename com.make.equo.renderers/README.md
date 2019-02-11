# How to create Equo Renderers on the Framework

The Equo framework now supports the creation of renderers at a framework level. To create Eclipse renderers based on Equo you have to follow a couple of simple steps.

1. Add the type of element you want to add a renderer for in the `EclipseWebRendererFactory` class.

2. Create a renderer class which extends the correct `*PartRenderer` (i.e. SWTPartRenderer) of the Eclipse framework, and also which implements the `IEquoRenderer` interface. This interface contains default methods that are in charged of the configuration and the initiation of the render process. It provides some hooks that implementators renders have to implement. All the heavy work is performed by this interface. Specifically, this class will create the Chromium browser widget, handle the configuration of the Equo Proxy and the renderer Javascript files, and also the WebSocket communication between the Java and Javascript sides.

3. In the renderer class you just created, an in the method that creates the widget (it can be `createWidget` for example), call the method `configureAndStartRenderProcess`. Something like this:

  ```java
    @Override
    public Object createWidget(MUIElement toolBar, Object parent) {
      this.namespace = "ToolBar" + Integer.toHexString(toolBar.hashCode());
      this.toolBar = toolBar;

      Composite toolBarComposite = (Composite) parent;

      configureAndStartRenderProcess(toolBarComposite);

      return toolBarComposite;
    }
  ```

4. Implement the required methods of the `IEquoRenderer` interface.

   - `getJsFileNamesForRendering`: This method returns a list of custom Javascript file names which are used to render an element. These scripts will be processed by the Equo Proxy and added automatically to the base Equo Renderer file (baseRenderer.html).

   - `getNamespace()`: A unique namespace for the element mdoel which is used for the websocket communication. A namespace must be created before the initiation of the render process.

   - `createBrowserComponent()`: Create the browser widget where the elements are rendered.

   - `getEclipse4Model`: Get the Eclipse 4 model of the element to be rendered. This model may involve the element children too.

   - `onActionPerformedOnElement()`: It's called when an action is performed on an element at the Javascript side, for example, a click on a button. The renderer itself knows what to do when an action is performed on a button. It can trigger the execution of a command, for example.

Once you finish with the Java side, you can continue with the Javascript part. You'll likely add only one file per renderer, and the task of this file is to receive the e4Model and render it in HTML (tip: use jquery to render the model in html.). Notice how all the development of renderers has been simplified in Javascript too. There are some Javascript files at the framework level which are in charge of the Java/Javascript communication and greatly simplify the creation of renderers. The developer of a renderer may define a renderer file like this (this is only an example):

```Javascript
  $(document).ready(function () {

      // Add toolbar elements to the html body
      const createToolbar = function (e4Model) {
          // invoke jquery to create html elements
      }

      equo.getE4Model(createToolbar);

      const sendOnclick = function (accion) {
          equo.send(namespace + '_itemClicked', {
              command: accion
          });
      }
  });
```

Calling the method `equo.getE4Model(callbackFunction);` with a callback function is a must. That's because calling that method will gather the E4 model and call the callback function you pass as parameter to finally render the model in HTML.
