## Table of Contents
  - [Table of Contents](#table-of-contents)
  - [Introduction and Environment Setup](#introduction-and-environment-setup)
  - [Equo Framework Development Environment](#equo-framework-development-environment)
    - [Developing the Equo Framework](#developing-the-equo-framework)
    - [Developing the Netflix Application and the Equo Framework in parallel](#developing-the-netflix-application-and-the-equo-framework-in-parallel)

The Equo Framework allows to build cross platform desktop apps with Java, JavaScript, HTML, and CSS.

This is a guide that describes how to install the development environment of the Equo Framework. Once you have this development environment installed, you should be able to start developing for the Equo Framework seamlessly, without any further configuration.

## Introduction and Environment Setup

To setup the development environment of Equo Framework, Eclipse Installer is suggested. Eclipse Installer is an installer and updater for development environments. It allows to get reproducible development environment with all required bundles and configuration.

## Equo Framework Development Environment

1. Download Eclipse Installer and run it (Click advanced mode button):
  - https://wiki.eclipse.org/Eclipse_Installer
  
2. Open the installer you just downloaded and switch to advance mode:
  ![Advanced mode](https://raw.githubusercontent.com/maketechnology/setups/master/guide/image_01.png)

3. Copy the **content** of the file _EquoIDEConfig.setup_ located in the Make Setups repository (https://github.com/maketechnology/setups).

4. Go back to the Eclipse Installer, you will see a button in the toolbar which allows to paste the configuration you just copied to the clipboard. That's the _EquoIDEConfig.setup_ configuration. Click that button.

5. After you click the toolbar button, the following screen appears:
  ![Variables Screen](https://raw.githubusercontent.com/maketechnology/setups/master/guide/image_02.png)

6. Click **Next** and then **Finish** to proceed with the installation. You will be promted for your Gitlab credentials. Eclipse installer will import all the Equo Framework bundles for you.

7. Now, you should have an Eclipse environment up and running with all the Equo Framework bundles. To use and test the features of the Equo Framework a sample Equo application is provided. This application lives in the `com.make.equo.testapp` bundle. You can develop new features, enhancements and fix bugs of the Equo Framework and use this sample application to try and test those features easily. To run the sample application go to the `com.make.equo.testapp` bundle, open the _bnd.bnd_ file, then go to the _Run_ tab, and then click on the _Run OSGI_ button.

### Developing the Equo Framework

1. After you finished the previous section you will have all the Equo Framework bundles ready in your workspace and you are ready to start developing the Equo Framework.

2. Once you make changes on your local Equo Framework code, and you want to test or use it in an Equo app, you have to perform some actions in order to use that local Equo Framework code and not the one located in the external repository. To achieve that, first go to a terminal and run `./gradlew release` in the _Equo Framework root directory_. It might take a while. This command generates the index.xml.gz file which is needed in the following step. **Note that you must run this step every time you want your Equo Framework code changes to be reflected in your Equo apps**.

3. Then, go to the **cnf** bundle and open the `equobuild.bnd` file and change it like this:
    - **Comment** the code:

      ```
      -plugin.1.Equo: \
      aQute.bnd.repository.osgi.OSGiRepository; \
          name = Equo; \
          locations = "http://www.equo.maketechnology.io/framework/rls/repo/index.xml.gz"; \
          poll.time = -1; \
          cache = ~/.bnd/cache/equo
      ```
    - **Uncomment** the following code, and update the _locations_ field to your index.xml.gz file. The index.xml.gz file is located inside the cnf bundle of the Equo Framework. Then, go to the cnf bundle, open it, then go to the _release_ folder, and there you will find the index.xml.gz file. Get the absolute path of this file and put it into the _locations_ field.

      ```
      #-plugin.1.Equo: \
      #    aQute.bnd.repository.osgi.OSGiRepository; \
      #        name = Equo; \
      #        locations = "file:///home/guille/ws/equo/framework/cnf/release/index.xml.gz"; \
      #        poll.time = -1; \
      #        cache = ~/.bnd/cache/equo
      ```

4. Save the `equobuild.bnd` file and perform a Project -> clean -> clean all.

### Developing the Netflix Application and the Equo Framework in parallel

**Note**: You must have completed the previous sections [Equo Framework Development Environment](#Equo-Framework-Development-Environment) and [Developing the Equo Framework](#Developing-the-Equo-Framework) to continue with this one.

As mentioned before, it's useful to have a sample application to develop the Equo Framework. For that purpose we are developing a Netflix application. The Netflix application is an Equo application which allows us to develop against the Equo Framework, to use its features, to test it, to show how the Equo framework works, and to innovate in ways we don't think before. The Netflix application is a separate project, which lives in: https://gitlab.com/maketechnology/equo/netflix-app.

To develop the Netflix application and continue developing the Equo Framework in parallel, follow the next steps:

1. Clone the Netflix Gitlab repo: https://gitlab.com/maketechnology/equo/netflix-app.

2. Open another Eclipse instance *from the same Eclipse you have generated in the previous section*. Then, you will have two instances of the same Eclipse for both the Equo Framework and the Netflix application, but they will run in different workspaces.

3. After you opened a new Eclipse instance, create a new workspace for the Netflix app (i.e. netflix-ws).

4. Import the Netflix application projects from the location you cloned it. After the import, it should look something like this:
  ![Netflix Projects](https://raw.githubusercontent.com/maketechnology/setups/master/guide/image_03.png)

5. If you see errors on your projects, go to Projects -> clean, and clean all.

6. Perform the following actions to use your local Equo Framework code:
    - Open the `cnf/build.bnd` file and modify the first `-include` sentence so that it points to the `equobuild.bnd` file of your local Equo Framework. After your change, it should look like this:

      ```
      -include: file:///home/lonelion/equo/equo-framework/cnf/equobuild.bnd
      -runvm: -XstartOnFirstThread
      ```
    - Open the `netflix/bnd.bnd` file and modify the first `-include` sentence so that it points to the `equoapp.bnd` file of your local Equo Framework. After your change, it should look like this:

      ```
      -include: file:///home/lonelion/equo/equo-framework/cnf/equoapp.bnd
      Bundle-Version: 0.1.0
      Private-Package: netflix
      bgcolor=#FF0000
      ```

7. Rebuild the entire workspace: Project -> clean -> clean all.

8. To run the Netflix application go to the **netflix** project, open the _bnd.bnd_ file, then go to the _Run_ tab, and then click on the _Run OSGI_ button.

Now you can start coding both the Equo Framework and the Netflix application in parallel. When you perform code changes in the Equo Framework and want it to be reflected on your Netflix application you have to repeat the step 2 of the [Developing the Equo Framework](#Developing-the-Equo-Framework) section (`./gradlew release`). Then, clean both the Equo Framework workspace and the Netflix one.

Happy coding!
