# Overview

[Equo Framework](https://www.equoplatform.com/framework) is an open source SDK which allows users to write modern GUI applications once, and deploy them across desktop and embedded operating systems without rewriting the source code.

Regarding desktop environments, it provides an SDK to create modern **native** cross-platform desktop applications with Java and web technologies like JavaScript, HTML, and CSS. It brings the web to the desktop.

## License

[Equo Framework](https://www.equoplatform.com/framework) is dual-licensed under commercial and open source licenses (GPLv3). This repository is licensed under GPLv3. To get a commercial license and to not worry about the obligations associated with the GPLv3 license please contact the [Equo Platform support team](https://www.equoplatform.com/request-a-demo).

## Related Projects

| Name | Repo | Version |
| ---- | ---- | ------- |
| **Renderers** | [equo/framework-renderers](https://github.com/equoplatform/framework-renderers) | 1.0.0 |
| **Extras** | [equo/framework-extras](https://github.com/equoplatform/framework-extras) | 1.0.0 |

## Framework Core

The Equo Framework is divided in different projects. The current one is the Framework Core, which is the essential one to run Equo apps.

The core provides:

* App structure and a fluent API to build an app
* Contributions support for multi-module apps to contribute resources into the app (as css or js scripts)
* Equo server and proxy to add custom resources into the app
* Equo Websockets to establish a real-time two-way communication between Java and Javascript

## Getting Started

If you are interested in creating an Equo application, you need to first download the [CLI](https://www.equoplatform.com/cli), and then you can start developing your application. Please visit the documentation website to [start creating an application](https://docs.equoplatform.com/framework/1.0.0/getting-started/creating.html). This README is more useful for those who are interested in contributing to the core SDK.

## Contributing

Thanks you to all the people who are contributing to Equo Framework! Please, read our [Contributors Guide](docs/CONTRIBUTING.md) and then look through our issues.

By contributing you agree to our [Code of Conduct](docs/CODE_OF_CONDUCT.md)'s terms.

## List of Framework Core bundles

You can see the list of bundles in [this document](docs/bundles.md).

## Debugging

Follow [this guide](docs/debugging.md) to know how to debug the framework by doing remote debugging to an Equo app.

## Integration tests

[Guide](docs/integration-tests.md) to create integration tests for the Equo framework.
