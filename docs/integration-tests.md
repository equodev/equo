## Integration Testing in Equo Framework (OSGi plugin Tests)

The way to develop tests that involve multiple parts inside of the Equo Framework is allowed through OSGi plugin test. 
Unlike Unit test, OSGi plugin test must be a separate bundle inside of the framework, therefore, each integration test developed has to be a new part of the framework.

The following steps describe how to create and run a OSGi test:

1- Create a new bundle on the Equo framework project

2- Add the dependencies related to Equo testing in `bnd.bnd` of the new bundle created:

`-include: https://testing-core.ams3.digitaloceanspaces.com/0.2.0/testbundle.bnd`

3- In the `bnd.bnd` file, add the required bundles for your tests in both _buildpath_ and _runrequires_ instructions. Resolve run requirements to get the _runbundles_ list.

4- Add bundle `com.make.equo.testing.common` to the buildpath and runbundles.

5- It is important to add that if you need (and it is too much possible that you need it) create an instance of a specific class or component in the framework, you use a Rule created to the automatic injection of this instances, called EquoRule.

To inject a class inside of your Test Class you have to add the next rule:
   ```
 @Rule
 public EquoRule injector = new EquoRule(this);
```

Moreover, you have to add the @Inject Anotation before of the attribute definition  that you decide inject:
 ```
   @Inject
    private EquoApplicationBuilder appBuilder;
```
6- When you are ready to run your test class, you have to run as `Bnd OSGi Test Launcher (JUnit)`: 
Right-click on the class on Package Explorer -> Run As -> Bnd OSGi Test Launcher (JUnit)

**If you need a functional example of an Integration Test, take a look at com.make.equo.builders.tests bundle on Equo framework project.**