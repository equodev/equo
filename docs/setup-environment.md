# Equo Framework

## Development Environment

1. Download [<u>_Eclipse Installer_</u>][Eclipse] and run it (Click _advanced mode_ button):

   ![img](img/equo-framework-development-environment-1.png)

2. Open the installer you just downloaded and switch to advance mode

   ![img](img/equo-framework-development-environment-2.png)

3. Copy the **content** of the file _EquoFrameworkConfig.setup_ located in _cnf_ folder of this project.

   ```bash
      # Linux & Mac
      cat framework/cnf/EquoFrameworkConfig.setup
   ```

   ```powershell
      # Windows
      type framework\cnf\EquoFrameworkConfig.setup
   ```

4. Go back to the Eclipse Installer, you will see a button in the toolbar which allows to paste the configuration you just copied to the clipboard. That's the _EquoFrameworkConfig.setup_ configuration. Click that button.

   ![img](img/equo-framework-development-environment-3.png)

5. Click **Next** and then **Finish** to proceed with the installation.

   ![img](img/equo-framework-development-environment-4.png)
   ![img](img/equo-framework-development-environment-5.png)
   ![img](img/equo-framework-development-environment-6.png)
   ![img](img/equo-framework-development-environment-7.png)
   ![img](img/equo-framework-development-environment-8.png)
   ![img](img/equo-framework-development-environment-9.png)

## Workspace configurations

Once you open a new workspace in Eclipse (the IDE installed before), there are a few settings needed to work well with the Framework:

1. Configure compilance compatibility to generate _.class_ files for Java 1.8 (`Window ⟶ Preferences ⟶ Java ⟶ Compiler`).

   ![img](img/Workspace%20configurations/1.png)
   ![img](img/Workspace%20configurations/2.png)
   ![img](img/Workspace%20configurations/3.png)
   ![img](img/Workspace%20configurations/4.png)

   But if you want set `Compiler compilance level` in 11, for example, so disable the checkboxes `Use '--release' option` and `Use default compilance settings`.

   Then, you should set to 1.8 the fields `Generated .class files compatibility` and `Source compatibility`.
   <br/>

   ![img](img/Workspace%20configurations/4.5.png)

   ![img](img/Workspace%20configurations/5.png)

2. Import the _checkstyle_ file to check Equo code styling as you are developing. For this, you need to:
   1. Go to `Window ⟶ Preferences ⟶ Checkstyle`.

      ![img](img/Workspace%20configurations/1.png)
      ![img](img/Workspace%20configurations/2.png)
      ![img](img/Workspace%20configurations/6.png)

   2. Select `New...` in the _Global Check Configurations_ section.

   3. Change the type of the new configuration to `External Configuration File`:

      ![img](img/Workspace%20configurations/7.png)

      - Write "Equo" on the name field.
      - Import from this project the file located in `config/checkstyle/checkstyle.xml` in the _location_ field.
   4. Press `Ok`
   5. Select the recently created configuration and press the `Set as Default` button.

      ![img](img/Workspace%20configurations/8.png)

   6. Check `Run Checkstyle in background on full builds` option.

      ![img](img/Workspace%20configurations/9.png)

   7. Press `Apply and Close`
   8. If a dialog box appears, saying _rebuild suggested_, press `Yes`

      ![img](img/Workspace%20configurations/10.png)

3. Import Equo formatter for Eclipse:
   1. Go to `Window ⟶ Preferences ⟶ Java ⟶ Code Style ⟶ Formatter`.

      ![img](img/Workspace%20configurations/1.png)
      ![img](img/Workspace%20configurations/2.png)
      ![img](img/Workspace%20configurations/3.png)
      ![img](img/Workspace%20configurations/11.png)
      ![img](img/Workspace%20configurations/12.png)

   2. Press `Import...` and select the file of this project located in `config/eclipse/Equo formatter.xml`.
   3. Press `Apply and close`

      ![img](img/Workspace%20configurations/13.png)

<!-- links -->
   [Eclipse]: https://wiki.eclipse.org/Eclipse_Installer
