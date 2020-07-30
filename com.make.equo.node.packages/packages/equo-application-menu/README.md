# Equo-Application-Menu

Equo-Application-Menu is a menu builder for Equo applications, 
in a chained way.

## Usage

Adding this package in your proyect and read about publishing packages on root folder.

## Usage whit example Methods 

using **EquoMenu** for reference builds methods

### Create example menu

```
var menu = EquoMenu;

    menu.withMainMenu("Menu1") #create main menu 1
            .addMenuItem("SubMenu11").onClick("_test").addShortcut("M1+W") #add equo menu item in main menu
            .addMenu("SubMenu12") # add equo menu in main menu
                .addMenuItem("SubMenu121") #add equo menu item in menu "SubMenu12"

        .withMainMenu("Menu2")
            .addMenuItem("SubMenu21").onClick("_test")
            .addMenuSeparator() # add menu item separator in main menu 2
            .addMenu("SubMenu22")
                .addMenuItem("SubMenu221").onClick("_test").addShortcut("M1+G")
                .addMenu("SubMenu222")
                    .addMenuItem("SubMenu2221")

        .setApplicationMenu(func); # set menu model
```

##### Note:
func is a optional callback for customise message after building the menu.


### appendMenuItem( Path, IndexToAddNewMenu, NameNewMenuItem )

Example method for apped menu item using a path location.

```
    menu.appendMenuItem("Menu1/SubMenu11", 0, "SubMenu10").onClick("_test").addShortcut("M1+L")
        .setApplicationMenu();
```
For append menu at the end use method **appendMenuItemAtTheEnd( Path, NameNewMenuItem )**.

### appendMenu( Path, IndexToAddNewMenu, NameNewMenu )

Example method for apped menu using a path location.

```
    menu.appendMenu("Menu2/SubMenu22", 1, "SubMenu223").addMenuItem("SubMenu2231").onClick("_test").addShortcut("M1+K")
        .setApplicationMenu();
```
For append menu at the end use method **appendMenuAtTheEnd( Path, NameNewMenu )**.

### removeMenuElementByPath( PathToRemoveMenu )

Example method for remove menu using a path location.
```
    menu.removeMenuElementByPath("Menu2/SubMenu22/SubMenu222").setApplicationMenu();
```

### Adding menus on current menu model example
```
EquoMenu.getCurrentModel( # method for use current menu model
    (builder: any) => {
        builder.withMainMenu("Menu3")
            .addMenuItem("subMenu31").onClick("_test").addShortcut("M1+W")
            .addMenu("subMenu32")
                .addMenuItem("subMenu321")
        
        .setApplicationMenu();
})
```
#### Note
If the menu name exists in the current menu model or construction, it will automatically reference the existing menu. 
