## Equo Framework Development Environment

1. Download Eclipse Installer and run it (Click advanced mode button):
   https://wiki.eclipse.org/Eclipse_Installer
  
2. Open the installer you just downloaded and switch to advance mode

3. Copy the **content** of the file _EquoFrameworkConfig.setup_ located in _cnf_ folder of this project.

4. Go back to the Eclipse Installer, you will see a button in the toolbar which allows to paste the configuration you just copied to the clipboard. That's the _EquoFrameworkConfig.setup_ configuration. Click that button.

5. Click **Next** and then **Finish** to proceed with the installation.

## Workspace configurations

Once you open a new workspace in Eclipse (the IDE installed before), there are a few settings needed to work well with the Framework:

1. Configure compilance compatibility to generate _.class_ files for Java 1.8 (Window -> Preferences -> Java -> Compiler).
2. Import the _checkstyle_ file to check Equo code styling as you are developing. For this, you need to:
   1. Go to `Window -> Preference -> Checkstyle`.
   2. Select `New...` in the _Global Check Configurations_ section.
   3. Change the type of the new configuration to `External Configuration File`:
      - Write "Equo" on the name field.
      - Import from this project the file located in `config/checkstyle/checkstyle.xml` in the _location_ field.
   4. Press Ok
   5. Select the recently created configuration and press the `Set as Default` button.
   6. Check `Run Checkstyle in backgroun on full builds` option.
3. Import Equo formatter for Eclipse:
   1. Go to `Window -> Preference -> Java -> Code Style -> Formatter`.
   2. Press `Import...` and select the file of this project located in `config/eclipse/Equo formatter.xml`.
   3. Apply changes.