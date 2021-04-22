## Overview

The Equo Framework allows to build cross platform desktop apps with Java, JavaScript, HTML, and CSS.

> It's a modern Java/HTML Application framework based on latest Chromium 
embedded browser and powerful Eclipse platform. 

> From the creators of SWT Chromium widget.

> Bringing the web to eclipse.

The current project is the Framework Core project, which is the main one. There are also the following ones:
* [Sdk](https://gitlab.com/maketechnology/equo/framework-sdk)
* [Renderers](https://gitlab.com/maketechnology/equo/framework-renderers)
* [Extras](https://gitlab.com/maketechnology/equo/framework-extras)

## Framework Core

Equo Framework is divided in different projects. The current one is the Framework Core, which is the essential one to run Equo apps.
The core provides:
* App structure and fluent API to build an app
* Contributions support for each bundle to contribute resources into the app (as css or js scripts)
* Equo server and proxy to add custom resources into the app
* Equo websockets to stablish a two-way communication between Java and Javascript

## How to develop an Equo app

Refer to [website documentation](https://docs.equoplatform.com) to know how to develop Equo apps

## Developing the Equo Framework

To develop the framework you must:
* [Setup the development environment](docs/Setup environment.md).
* After that, clone this project. Open a terminal in the framework folder and build it with Gradle for the first time with `./gradlew assemble`
* Import the project in Eclipse (the one configured before) into a new workspace.
* Then, you can start to make modifications. Eclipse will automatically be building the framework on saved changes and you can [develop an Equo app in parallel consuming local framework](docs/develop-app-in-parallel.md) to test the changes

## List of Framework Core bundles

You can see the list of bundles in [this document](docs/bundles.md)

## How to debug

Follow [this guide](docs/debugging.md) to know how to debug the framework by doing remote debugging to an Equo app

## Integration tests

[Guide](docs/Integration-tests.md) to create integration tests for the Equo framework
