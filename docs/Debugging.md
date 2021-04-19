## Remote debug

Remote debug may be useful to debug the framework when running an app in another workspace

1. Open the `bnd.bnd` file of the app you want to debug. Add this line to the file:
`-runvm.debug: "-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=1044"`
2.  Run the app. It will suspend until you connect a remote debugger.
3.  In Eclipse, on the framework workspace, go to _Run -> Debug configurations..._
4.  Create a new Remote Java Application debug configuration, set port to 1044. Start debug

## How to install OSGi Console

The OSGi console is a useful tool that allows to track the state and behavior of your Bundles and Components related to OSGi framework.
To install it, follow the next steps:
  1.  Open equoapp.bnd file contained in `cnf` folder (see it in Package Explorer view)
  2.  In the section `-runproperties.equo` add the following line at the end of the section: 
      ```
          osgi.console= ,\
      ```
  3. In the section `-runbundles.equo` add the following lines at the end of the section:
      ```
      org.eclipse.equinox.console,\
      org.apache.felix.gogo.command,\
      org.apache.felix.gogo.runtime,\
      org.apache.felix.gogo.shell
      ```
  4. Save changes and run TestApp
  5. Use Eclipse Console shell to write some commands described in the following article: https://www.vogella.com/tutorials/OSGi/article.html#using-the-osgi-console

### How to find the unsatisfied requirement using OSGi Console

Using the **list** command you will get the list of all bundles together with their **IDs**.
Using the **bundle ID** or **list ID** command for verify bundle status and requeriments. Additionally use **info NAMEBUNDLE** for obtain more info about it.

You can also use the **help** command to view the available commands if you need it.