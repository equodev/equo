## Bundles involved

### Core bundles

The bundles that provide the core functionality of the SDK:

| Bundle | Description |
| ----------- | ----------- |
| com.equo.aer.api | API for Advanced Error Reporting. This API defines how an application developer will report errors to App Manager. It does nothing without the SDK running |
| com.equo.aer.internal.api | Internal API for AER. This API is intended to be used only by Equo SDK and not the user. It does nothing without the SDK running | 
| com.equo.analytics.client.api | API for Analytics. Defines how the developer will register events to App Manager. It does nothing without the SDK running | 
| com.equo.analytics.internal.api | Internal Analytics API. Used by the SDK to register internal events. It does nothing without the SDK running | 
| com.equo.application.client | The main API entry point of the Equo SDK. Here we have the app structure, with Equo builders to create it, and its model (which includes the main window, toolbars, shortcuts, menus, etc). We also have the app lifecycle and Eclipse handlers to manage the E4 app | 
| com.equo.contribution.api | API defined to allow other bundles to contribute resources to the app such as css, js scripts, startup scripts, and more |
| com.equo.contribution.provider | Contributions API implementation. It has the manager and resolver to process contributions in runtime | 
| com.equo.logging.client.api | API for Equo logging service. It contains the API, configurations, a logger factory and a basic logger implementation that logs to stdout using [Logback](https://logback.qos.ch) | 
| com.equo.logging.client.core.provider | Provides the Javascript logging API | 
| com.equo.node.packages | Bundle with all the Node packages variants for the core Equo APIs. Includes comm, menu builders, logging, and a main SDK API | 
| com.equo.server.api | API for the server running in Equo apps | 
| com.equo.server.offline.api | API of the offline server support |
| com.equo.server.provider | Provides a modified Littleproxy server. This server runs at start and it's responsable to inject custom resources on the Equo app. Here the browser is configured to use the server as proxy for its requests | 
### Test bundles

This bundles contain integration tests or provide the necessary elements for them:

| Bundle | Description |
| ----------- | ----------- |
| com.equo.builders.tests | Integration tests for the creation of Menus and Toolbars with the builder fluent API |
| com.equo.node.packages.tests | Tests for the Node packages APIs present in com.equo.node.packages bundle | 
| com.equo.testing.common.internal | Provides common elements for all integration tests. Currently, it provides the initial IEquoApplication component to run an Equo app in the tests | 

### Release bundle

Finally, there's the bundle `com.equo.p2repo`, which is responsible of the p2 repository generation of the SDK for its release.

This special bundle only has two files:
* `bnd.bnd`: Which has a _runrequires_ list with the needed bundles to run an app with the SDK and a _runbundles_ variable with that requirements resolved by Bndtools
* `p2.bndrun`: A bndrun file configured to generate a p2 repository with all the bundles listed in _bnd.bnd_ file. It executes on `./gradlew run.p2`
