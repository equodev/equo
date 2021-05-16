# Publishing packages

<b>These steps are useful for local and remote package publishing, a shortcut for local publishing is shown below.</b>

## 1 - Versioning:

```bash
lerna version
```

If a package exists in the registry with the same version the publish will fail so this step is mandatory.


## 2 - Committing the changes

The versioning command will generate changes in the working tree that need to be committed before going to the next step.


## 3 - Publishing

If these changes are to be published to our registry, the CI will handle the publishing so pushing the branch is enough.


To publish locally to any registry instead, the `--registry` flag can be passed to the `lerna publish` command as follows:

```bash
lerna publish from-package --registry your-registry-url
```


## A shortcut to publishing packages locally

By default, `lerna publish` runs `lerna version` first in its lifecycle, when working on scripts that need to be tested frequently locally the above steps can be avoided and executing `lerna publish` with the `--registry` flag is enough.

```bash
lerna publish --registry your-registry-url
```


# The structure and default command settings of this bundle

In this bundle there are a couple of settings in place that directly affect the commands mentioned above.

### NPM lifecycle scripts

There's more information about them [here](https://docs.npmjs.com/misc/scripts#description)

In the `./package.json` file, the `prepare` lifecycle is being used to hook the `lerna run prepare` script. This lifecycle script is run when executing `yarn(npm) install` and `yarn(npm) publish` and it's set up in this bundle with a lerna command that handles the build and runs our `prepare` script in each of our packages individually. This allows `yarn` to be the complete command needed to build all packages.


### Lerna command line arguments

Lerna uses [yargs](https://github.com/yargs/yargs) to parse the default arguments for its commands and they can be configured in the `./lerna.json` file. Passing the arguments as command flags from the command line has precedence over the settings in the file.

Documentation about the commands and flags that can be used is written in their github repository [here](https://github.com/lerna/lerna). Each command has its own repository that has explanations specific to that command.