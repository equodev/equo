## Table of Contents
  - [Introduction and Environment Setup](#introduction-and-environment-setup)
  - [Equo Framework Development Environment](#equo-framework-development-environment)
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

3. Copy the content of the file _EquoIDEConfig.setup_ located in the Make Setups repository (https://github.com/maketechnology/setups) .

4. Go back to the Eclipse Installer, you you see a button in the toolbar which allows to paste the configuration you just copied to the clipboard. That's the _EquoIDEConfig.setup_ configuration. Click that button.

5. After you click the toolbar button, the following screen appears:
  ![Variables Screen](https://raw.githubusercontent.com/maketechnology/setups/master/guide/image_02.png)

6. Click *Next* and then *Finish* to proceed with the installation. You will be promted for your Gitlab credentials. Eclipse installer will import all the Equo Framework bundles for you.

7. Now, you should have an Eclipse environment up and running with all the Equo Framework bundles. However, you will see that your projects have some errors. To fix them, go to Project -> Clean... There, clean all projects.

8. Once the project is clean, without errors, you can start developing for the Equo Framework. To use and test the features you developed in the Equo Framework, you may want to develop an Equo application (i.e. Netflix). To start the development of the Netflix application, and in parallel continue with the development of the Equo Framework follow the next section.

### Developing the Netflix Application and the Equo Framework in parallel

*Note*: You must have completed the previous section [Equo Framework Development Environment](#Equo-Framework-Development-Environment) to continue with this one.

As mentioned before, it's useful to have a sample application to develop the Equo Framework. For that purpose we are developing a Netflix application. The Netflix application is an Equo app which allows us to develop against the Equo Framework, to use its features, to test it, and to innovate in ways we don't think before. The Netflix application is a separate project, which lives in: https://gitlab.com/maketechnology/equo/netflix-app.

To develop the Netflix application and continue developing the Equo Framework in parallel, follow the next steps:

1. Clone the Netflix Gitlab repo: https://gitlab.com/maketechnology/equo/netflix-app.

2. Open another Eclipse instance *from the same Eclipse you have generated in the previous section*. Then, you will have two instances of the same Eclipse for both the Equo Framework and the Netflix application, but they will run in different workspaces.

3. After open a new Eclipse instance, create a new workspace for the Netflix app (i.e. netflix-ws).

4. Import the Netflix application projects from the location you cloned it. After the import, it should look something like this:
  ![Netflix Projects](https://raw.githubusercontent.com/maketechnology/setups/master/guide/image_03.png)

5. If you see errors on your projects, go to Projects -> clean, and clean all.

6. Go to a terminal and run `./gradlew release` in the _Equo Framework root directory_. It might take a while. This step generates the index.xml.gz file which is needed in the following steps.

7. Go back to the Eclipse instance where you have the Equo Framework workspace. Open the CNF bundle, then open the `equobuild.bnd` file and perform the following actions:
    - _Comment_ the code:

      ```
      -plugin.1.Equo: \
      aQute.bnd.repository.osgi.OSGiRepository; \
          name = Equo; \
          locations = "http://www.equo.maketechnology.io/framework/rls/repo/index.xml.gz"; \
          poll.time = -1; \
          cache = ~/.bnd/cache/equo
      ```
    - *Uncomment* the following code, and update the _locations_ field to your index.xml.gz file. The index.xml.gz file is located inside the cnf bundle of the Equo Framework. Then, go to the cnf bundle, open it, then go to the _release_ folder, and there you will find the index.xml.gz file. Get the absolute path of this file and put it into the _locations_ field.

      ```
      #-plugin.1.Equo: \
      #    aQute.bnd.repository.osgi.OSGiRepository; \
      #        name = Equo; \
      #        locations = "file:///home/guille/ws/equo/framework/cnf/release/index.xml.gz"; \
      #        poll.time = -1; \
      #        cache = ~/.bnd/cache/equo
      ```

8. Save the `equobuild.bnd` file and perform a Project -> clean -> clean all.

9. Now, go back to the Eclipse instance where you have the Netflix application workspace and perform the following actions:
    - Open the `cnf/build.bnd` file and modify the first `-include` sentence so that it points to the recenlty modified `equobuild.bnd` file of your local Equo Framework. After your change, it should look like this:

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

10. Rebuild the entire workspace: Project -> clean -> clean all.

Now you can start coding both the Equo Framework and the Netflix application in parallel. When you perform a code change in the Equo Framework and want it to be reflected on your Netflix application you have to repeat the step 6 of this section (`./gradlew release`). Then, clean both workspaces, the Equo Framework workspace and the Netflix one.

Happy coding!
