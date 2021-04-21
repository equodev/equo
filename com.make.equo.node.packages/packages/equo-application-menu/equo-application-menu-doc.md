## Classes

<dl>
<dt><a href="#MenuItemSeparatorBuilder">MenuItemSeparatorBuilder</a></dt>
<dd></dd>
<dt><a href="#MenuBuilder">MenuBuilder</a></dt>
<dd></dd>
<dt><a href="#MenuItemBuilder">MenuItemBuilder</a></dt>
<dd></dd>
<dt><a href="#EquoMenu">EquoMenu</a></dt>
<dd></dd>
</dl>

## Objects

<dl>
<dt><a href="#Menu">Menu</a> : <code>object</code></dt>
<dd><p>Equo-Application-Menu is a node package with the name &#39;@equo/equo-application-menu&#39; that allows through an api a native constructor of menus for Equo applications, in a chained way.</p>
<p>This document specifies the full usage methods for Equo-Application-Menu and full examples for each of them.
see <a href="how-to-include-equo-components.md">more</a> about how to include Equo component in your projects.</p>
</dd>
</dl>

## Functions

<dl>
<dt><a href="#create">create()</a> ⇒ <code><a href="#MenuBuilder">MenuBuilder</a></code></dt>
<dd><p>Create a menu instance.</p>
</dd>
<dt><a href="#getCurrentModel">getCurrentModel(callback)</a> ⇒ <code>void</code></dt>
<dd><p>Get current app menu model in callback&#39;s parameter. You can build a new menu model from the one established in your application.</p>
</dd>
</dl>

<a name="MenuItemSeparatorBuilder"></a>

## MenuItemSeparatorBuilder
**Kind**: global class  
**Summary**: Create a MenuItemSeparatorBuilder.  

* [MenuItemSeparatorBuilder](#MenuItemSeparatorBuilder)
    * [new MenuItemSeparatorBuilder(linker)](#new_MenuItemSeparatorBuilder_new)
    * [.addMenu(label)](#MenuItemSeparatorBuilder+addMenu) ⇒ [<code>MenuBuilder</code>](#MenuBuilder)
    * [.addMenuItem(label)](#MenuItemSeparatorBuilder+addMenuItem) ⇒ [<code>MenuItemBuilder</code>](#MenuItemBuilder) \| <code>null</code>

<a name="new_MenuItemSeparatorBuilder_new"></a>

### new MenuItemSeparatorBuilder(linker)

| Param | Type |
| --- | --- |
| linker | <code>Linker</code> | 

<a name="MenuItemSeparatorBuilder+addMenu"></a>

### menuItemSeparatorBuilder.addMenu(label) ⇒ [<code>MenuBuilder</code>](#MenuBuilder)
Add a new menu that will contain other sub menus.

**Kind**: instance method of [<code>MenuItemSeparatorBuilder</code>](#MenuItemSeparatorBuilder)  

| Param | Type | Description |
| --- | --- | --- |
| label | <code>string</code> | Menu title. |

<a name="MenuItemSeparatorBuilder+addMenuItem"></a>

### menuItemSeparatorBuilder.addMenuItem(label) ⇒ [<code>MenuItemBuilder</code>](#MenuItemBuilder) \| <code>null</code>
Add a new menu that will contain other menus.

**Kind**: instance method of [<code>MenuItemSeparatorBuilder</code>](#MenuItemSeparatorBuilder)  
**Returns**: [<code>MenuItemBuilder</code>](#MenuItemBuilder) \| <code>null</code> - If name exists and the menu type is 'EquoMenuItem' will return MenuItemBuilder. If exists and type is 'EquoMenu' will return null.  

| Param | Type | Description |
| --- | --- | --- |
| label | <code>string</code> | Menu title. |

<a name="MenuBuilder"></a>

## MenuBuilder
**Kind**: global class  
**Summary**: Create a MenuBuilder.  

* [MenuBuilder](#MenuBuilder)
    * [new MenuBuilder(linker)](#new_MenuBuilder_new)
    * [.withMainMenu(label)](#MenuBuilder+withMainMenu) ⇒ [<code>MenuBuilder</code>](#MenuBuilder)
    * [.addMenu(label)](#MenuBuilder+addMenu) ⇒ [<code>MenuBuilder</code>](#MenuBuilder)
    * [.addMenuItem(label)](#MenuBuilder+addMenuItem) ⇒ [<code>MenuItemBuilder</code>](#MenuItemBuilder) \| <code>null</code>
    * [.setApplicationMenu([callback])](#MenuBuilder+setApplicationMenu) ⇒ <code>void</code>
    * [.setApplicationMenuWithJson(json)](#MenuBuilder+setApplicationMenuWithJson) ⇒ <code>void</code>
    * [.getMenus()](#MenuBuilder+getMenus) ⇒ [<code>Array.&lt;EquoMenu&gt;</code>](#EquoMenu)
    * [.appendMenuItem(menuPath, indexToAddItem, menuItemName)](#MenuBuilder+appendMenuItem) ⇒ [<code>MenuItemBuilder</code>](#MenuItemBuilder) \| <code>null</code>
    * [.appendMenu(menuPath, indexToAddItem, menuName)](#MenuBuilder+appendMenu) ⇒ [<code>MenuItemBuilder</code>](#MenuItemBuilder) \| <code>null</code>
    * [.appendMenuItemAtTheEnd(menuElementPath, menuItemName)](#MenuBuilder+appendMenuItemAtTheEnd) ⇒ [<code>MenuItemBuilder</code>](#MenuItemBuilder) \| <code>null</code>
    * [.appendMenuAtTheEnd(menuElementPath, menuName)](#MenuBuilder+appendMenuAtTheEnd) ⇒ [<code>MenuBuilder</code>](#MenuBuilder) \| <code>null</code>
    * [.addMenuItemToModel(parentMenuId, index, menuItem)](#MenuBuilder+addMenuItemToModel) ⇒ <code>void</code>
    * [.removeMenuElementById(menuToRemoveId)](#MenuBuilder+removeMenuElementById) ⇒ <code>boolean</code>
    * [.removeMenuElementByPath(menuNamePathToRemove)](#MenuBuilder+removeMenuElementByPath) ⇒ [<code>MenuBuilder</code>](#MenuBuilder)

<a name="new_MenuBuilder_new"></a>

### new MenuBuilder(linker)

| Param | Type |
| --- | --- |
| linker | <code>Linker</code> | 

<a name="MenuBuilder+withMainMenu"></a>

### menuBuilder.withMainMenu(label) ⇒ [<code>MenuBuilder</code>](#MenuBuilder)
Create a root menu, visible in the bar.

**Kind**: instance method of [<code>MenuBuilder</code>](#MenuBuilder)  

| Param | Type | Description |
| --- | --- | --- |
| label | <code>string</code> | Menu title. |

<a name="MenuBuilder+addMenu"></a>

### menuBuilder.addMenu(label) ⇒ [<code>MenuBuilder</code>](#MenuBuilder)
Add a new menu that will contain other sub menus.

**Kind**: instance method of [<code>MenuBuilder</code>](#MenuBuilder)  

| Param | Type | Description |
| --- | --- | --- |
| label | <code>string</code> | Menu title. |

<a name="MenuBuilder+addMenuItem"></a>

### menuBuilder.addMenuItem(label) ⇒ [<code>MenuItemBuilder</code>](#MenuItemBuilder) \| <code>null</code>
Add a new menu that will contain other menus.

**Kind**: instance method of [<code>MenuBuilder</code>](#MenuBuilder)  
**Returns**: [<code>MenuItemBuilder</code>](#MenuItemBuilder) \| <code>null</code> - If name exists and the menu type is 'EquoMenuItem' will return MenuItemBuilder. If exists and type is 'EquoMenu' will return null.  

| Param | Type | Description |
| --- | --- | --- |
| label | <code>string</code> | Menu title. |

<a name="MenuBuilder+setApplicationMenu"></a>

### menuBuilder.setApplicationMenu([callback]) ⇒ <code>void</code>
Set the application menu to the current app menu. Optionally, pass a function parameter for custom actions called once the operation has been done.

**Kind**: instance method of [<code>MenuBuilder</code>](#MenuBuilder)  

| Param | Type |
| --- | --- |
| [callback] | <code>function</code> | 

<a name="MenuBuilder+setApplicationMenuWithJson"></a>

### menuBuilder.setApplicationMenuWithJson(json) ⇒ <code>void</code>
Set the application menu from a json format to the current model.

**Kind**: instance method of [<code>MenuBuilder</code>](#MenuBuilder)  

| Param | Type | Description |
| --- | --- | --- |
| json | <code>JSON</code> | Json menu |

<a name="MenuBuilder+getMenus"></a>

### menuBuilder.getMenus() ⇒ [<code>Array.&lt;EquoMenu&gt;</code>](#EquoMenu)
Get all Equo menus from the under construction Menu.

**Kind**: instance method of [<code>MenuBuilder</code>](#MenuBuilder)  
<a name="MenuBuilder+appendMenuItem"></a>

### menuBuilder.appendMenuItem(menuPath, indexToAddItem, menuItemName) ⇒ [<code>MenuItemBuilder</code>](#MenuItemBuilder) \| <code>null</code>
Add a new menu that will contain other menus.

**Kind**: instance method of [<code>MenuBuilder</code>](#MenuBuilder)  
**Returns**: [<code>MenuItemBuilder</code>](#MenuItemBuilder) \| <code>null</code> - If name exists and the menu type is 'EquoMenuItem' will return MenuItemBuilder. If exists and type is 'EquoMenu' will return null.  

| Param | Type | Description |
| --- | --- | --- |
| menuPath | <code>string</code> | Path to add menu. |
| indexToAddItem | <code>number</code> | Index to add item. |
| menuItemName | <code>string</code> | Menu title. |

<a name="MenuBuilder+appendMenu"></a>

### menuBuilder.appendMenu(menuPath, indexToAddItem, menuName) ⇒ [<code>MenuItemBuilder</code>](#MenuItemBuilder) \| <code>null</code>
Append menu using path location.

**Kind**: instance method of [<code>MenuBuilder</code>](#MenuBuilder)  
**Returns**: [<code>MenuItemBuilder</code>](#MenuItemBuilder) \| <code>null</code> - Returns null if menuPath does not exists  

| Param | Type | Description |
| --- | --- | --- |
| menuPath | <code>string</code> | Path to add menu. |
| indexToAddItem | <code>number</code> | Index to add item. |
| menuName | <code>string</code> | Menu title. |

<a name="MenuBuilder+appendMenuItemAtTheEnd"></a>

### menuBuilder.appendMenuItemAtTheEnd(menuElementPath, menuItemName) ⇒ [<code>MenuItemBuilder</code>](#MenuItemBuilder) \| <code>null</code>
Append menu item at the end.

**Kind**: instance method of [<code>MenuBuilder</code>](#MenuBuilder)  
**Returns**: [<code>MenuItemBuilder</code>](#MenuItemBuilder) \| <code>null</code> - If name exists and the menu type is 'EquoMenuItem' will return MenuItemBuilder. If exists and type is 'EquoMenu' or menuPath does not exists will return null.  

| Param | Type | Description |
| --- | --- | --- |
| menuElementPath | <code>string</code> | Path to menu element to add. |
| menuItemName | <code>string</code> | Menu title. |

<a name="MenuBuilder+appendMenuAtTheEnd"></a>

### menuBuilder.appendMenuAtTheEnd(menuElementPath, menuName) ⇒ [<code>MenuBuilder</code>](#MenuBuilder) \| <code>null</code>
Append menu at the end.

**Kind**: instance method of [<code>MenuBuilder</code>](#MenuBuilder)  
**Returns**: [<code>MenuBuilder</code>](#MenuBuilder) \| <code>null</code> - Returns null if menuElementPath does not exists  

| Param | Type | Description |
| --- | --- | --- |
| menuElementPath | <code>string</code> | Path to menu element to add. |
| menuName | <code>string</code> | Menu title. |

<a name="MenuBuilder+addMenuItemToModel"></a>

### menuBuilder.addMenuItemToModel(parentMenuId, index, menuItem) ⇒ <code>void</code>
Add a new EquoMenu to the model using the parent menu id. If the parent menu id exists in the constructor, it will be added at the specified parent index.

**Kind**: instance method of [<code>MenuBuilder</code>](#MenuBuilder)  

| Param | Type | Description |
| --- | --- | --- |
| parentMenuId | <code>string</code> | Parent ID to be added to. |
| index | <code>number</code> | Index to add item. |
| menuItem | [<code>EquoMenu</code>](#EquoMenu) | Menu element to add. |

<a name="MenuBuilder+removeMenuElementById"></a>

### menuBuilder.removeMenuElementById(menuToRemoveId) ⇒ <code>boolean</code>
Remove menu element by id.

**Kind**: instance method of [<code>MenuBuilder</code>](#MenuBuilder)  
**Returns**: <code>boolean</code> - Returns true if the menu was found and removed, false otherwise.  

| Param | Type | Description |
| --- | --- | --- |
| menuToRemoveId | <code>string</code> | ID from menu to remove. |

<a name="MenuBuilder+removeMenuElementByPath"></a>

### menuBuilder.removeMenuElementByPath(menuNamePathToRemove) ⇒ [<code>MenuBuilder</code>](#MenuBuilder)
Remove menu element by path. If the element exists, it will be removed.

**Kind**: instance method of [<code>MenuBuilder</code>](#MenuBuilder)  

| Param | Type | Description |
| --- | --- | --- |
| menuNamePathToRemove | <code>string</code> | Path|name from menu to remove. |

<a name="MenuItemBuilder"></a>

## MenuItemBuilder
**Kind**: global class  
**Summary**: Create a MenuItemBuilder.  

* [MenuItemBuilder](#MenuItemBuilder)
    * [new MenuItemBuilder(linker)](#new_MenuItemBuilder_new)
    * [.withMainMenu(label)](#MenuItemBuilder+withMainMenu) ⇒ [<code>MenuBuilder</code>](#MenuBuilder)
    * [.addMenuItem(label)](#MenuItemBuilder+addMenuItem) ⇒ [<code>MenuItemBuilder</code>](#MenuItemBuilder) \| <code>null</code>
    * [.onClick(action)](#MenuItemBuilder+onClick) ⇒ [<code>MenuItemBuilder</code>](#MenuItemBuilder)
    * [.addShortcut(shortcut)](#MenuItemBuilder+addShortcut) ⇒ [<code>MenuItemBuilder</code>](#MenuItemBuilder)
    * [.addMenu(label)](#MenuItemBuilder+addMenu) ⇒ [<code>MenuBuilder</code>](#MenuBuilder)
    * [.addMenuSeparator()](#MenuItemBuilder+addMenuSeparator) ⇒ [<code>MenuItemSeparatorBuilder</code>](#MenuItemSeparatorBuilder)
    * [.setApplicationMenu([callback])](#MenuItemBuilder+setApplicationMenu) ⇒ <code>void</code>

<a name="new_MenuItemBuilder_new"></a>

### new MenuItemBuilder(linker)

| Param | Type |
| --- | --- |
| linker | <code>Linker</code> | 

<a name="MenuItemBuilder+withMainMenu"></a>

### menuItemBuilder.withMainMenu(label) ⇒ [<code>MenuBuilder</code>](#MenuBuilder)
Create a root menu, visible in the bar.

**Kind**: instance method of [<code>MenuItemBuilder</code>](#MenuItemBuilder)  

| Param | Type | Description |
| --- | --- | --- |
| label | <code>string</code> | Menu title. |

<a name="MenuItemBuilder+addMenuItem"></a>

### menuItemBuilder.addMenuItem(label) ⇒ [<code>MenuItemBuilder</code>](#MenuItemBuilder) \| <code>null</code>
Add a new menu that will contain other menus.

**Kind**: instance method of [<code>MenuItemBuilder</code>](#MenuItemBuilder)  
**Returns**: [<code>MenuItemBuilder</code>](#MenuItemBuilder) \| <code>null</code> - If name exists and the menu type is 'EquoMenuItem' will return MenuItemBuilder. If exists and type is 'EquoMenu' will return null.  

| Param | Type | Description |
| --- | --- | --- |
| label | <code>string</code> | Menu title. |

<a name="MenuItemBuilder+onClick"></a>

### menuItemBuilder.onClick(action) ⇒ [<code>MenuItemBuilder</code>](#MenuItemBuilder)
Add action in menu element.

**Kind**: instance method of [<code>MenuItemBuilder</code>](#MenuItemBuilder)  

| Param | Type | Description |
| --- | --- | --- |
| action | <code>string</code> \| <code>function</code> | Define onclick action. |

<a name="MenuItemBuilder+addShortcut"></a>

### menuItemBuilder.addShortcut(shortcut) ⇒ [<code>MenuItemBuilder</code>](#MenuItemBuilder)
Add shortcut in menu element.

**Kind**: instance method of [<code>MenuItemBuilder</code>](#MenuItemBuilder)  

| Param | Type | Description |
| --- | --- | --- |
| shortcut | <code>string</code> | Define shortcut. |

<a name="MenuItemBuilder+addMenu"></a>

### menuItemBuilder.addMenu(label) ⇒ [<code>MenuBuilder</code>](#MenuBuilder)
Add a new menu that will contain other sub menus.

**Kind**: instance method of [<code>MenuItemBuilder</code>](#MenuItemBuilder)  

| Param | Type | Description |
| --- | --- | --- |
| label | <code>string</code> | Menu title. |

<a name="MenuItemBuilder+addMenuSeparator"></a>

### menuItemBuilder.addMenuSeparator() ⇒ [<code>MenuItemSeparatorBuilder</code>](#MenuItemSeparatorBuilder)
Add separator between menus.

**Kind**: instance method of [<code>MenuItemBuilder</code>](#MenuItemBuilder)  
<a name="MenuItemBuilder+setApplicationMenu"></a>

### menuItemBuilder.setApplicationMenu([callback]) ⇒ <code>void</code>
* Set the application menu to the current app menu. Optionally, pass a function parameter for custom actions called once the operation has been done.

**Kind**: instance method of [<code>MenuItemBuilder</code>](#MenuItemBuilder)  

| Param | Type |
| --- | --- |
| [callback] | <code>function</code> | 

<a name="EquoMenu"></a>

## EquoMenu
**Kind**: global class  
**Summary**: Create EquoMenu element.  

* [EquoMenu](#EquoMenu)
    * [.fillFromJSON(json)](#EquoMenu+fillFromJSON) ⇒ <code>void</code>
    * [.setRunnable(runnable)](#EquoMenu+setRunnable) ⇒ <code>void</code>
    * [.setType(type)](#EquoMenu+setType) ⇒ <code>void</code>
    * [.setTitle(title)](#EquoMenu+setTitle) ⇒ <code>void</code>
    * [.setAction(action)](#EquoMenu+setAction) ⇒ <code>void</code>
    * [.setShortcut(shortcut)](#EquoMenu+setShortcut) ⇒ <code>void</code>
    * [.addChildren(children)](#EquoMenu+addChildren) ⇒ <code>void</code>
    * [.setChildren(childrens)](#EquoMenu+setChildren) ⇒ <code>void</code>
    * [.getType()](#EquoMenu+getType) ⇒ <code>string</code>
    * [.getTitle()](#EquoMenu+getTitle) ⇒ <code>string</code>
    * [.getAction()](#EquoMenu+getAction) ⇒ <code>string</code>
    * [.getShortcut()](#EquoMenu+getShortcut) ⇒ <code>string</code>
    * [.getChildren()](#EquoMenu+getChildren) ⇒ [<code>Array.&lt;EquoMenu&gt;</code>](#EquoMenu)
    * [.getId()](#EquoMenu+getId) ⇒ <code>string</code>
    * [.addChildrenAtIndex(index, children)](#EquoMenu+addChildrenAtIndex) ⇒ <code>boolean</code>
    * [.removeMenuItemOfIndex(index)](#EquoMenu+removeMenuItemOfIndex) ⇒ <code>void</code>
    * [.removeMenuItemById(menuItemId)](#EquoMenu+removeMenuItemById) ⇒ <code>void</code>
    * [.getTitleMenus()](#EquoMenu+getTitleMenus) ⇒ <code>Array.&lt;string&gt;</code>

<a name="EquoMenu+fillFromJSON"></a>

### equoMenu.fillFromJSON(json) ⇒ <code>void</code>
Initialize EquoMenu from json.

**Kind**: instance method of [<code>EquoMenu</code>](#EquoMenu)  

| Param | Type | Description |
| --- | --- | --- |
| json | <code>JSON</code> | Json with menu. |

<a name="EquoMenu+setRunnable"></a>

### equoMenu.setRunnable(runnable) ⇒ <code>void</code>
Set specific runnable for menu action.

**Kind**: instance method of [<code>EquoMenu</code>](#EquoMenu)  

| Param | Type |
| --- | --- |
| runnable | <code>function</code> | 

<a name="EquoMenu+setType"></a>

### equoMenu.setType(type) ⇒ <code>void</code>
Set the menu type. Valid types are EquoMenu or EquoMenuItem.

**Kind**: instance method of [<code>EquoMenu</code>](#EquoMenu)  

| Param | Type | Description |
| --- | --- | --- |
| type | <code>string</code> | Valid types: EquoMenu | EquoMenuItem |

<a name="EquoMenu+setTitle"></a>

### equoMenu.setTitle(title) ⇒ <code>void</code>
Set title for menu.

**Kind**: instance method of [<code>EquoMenu</code>](#EquoMenu)  

| Param | Type | Description |
| --- | --- | --- |
| title | <code>string</code> | Menu title |

<a name="EquoMenu+setAction"></a>

### equoMenu.setAction(action) ⇒ <code>void</code>
Set action id for menu.

**Kind**: instance method of [<code>EquoMenu</code>](#EquoMenu)  

| Param | Type | Description |
| --- | --- | --- |
| action | <code>string</code> | Action ID. |

<a name="EquoMenu+setShortcut"></a>

### equoMenu.setShortcut(shortcut) ⇒ <code>void</code>
Set shortcut for menu.

**Kind**: instance method of [<code>EquoMenu</code>](#EquoMenu)  

| Param | Type |
| --- | --- |
| shortcut | <code>string</code> | 

<a name="EquoMenu+addChildren"></a>

### equoMenu.addChildren(children) ⇒ <code>void</code>
Add children in EquoMenu at the end. It will not be added if there is another child with the same title. Returns -1 if it is not added or the index to which it was added.

**Kind**: instance method of [<code>EquoMenu</code>](#EquoMenu)  

| Param | Type |
| --- | --- |
| children | [<code>EquoMenu</code>](#EquoMenu) | 

<a name="EquoMenu+setChildren"></a>

### equoMenu.setChildren(childrens) ⇒ <code>void</code>
Set childrens for menu.

**Kind**: instance method of [<code>EquoMenu</code>](#EquoMenu)  

| Param | Type |
| --- | --- |
| childrens | [<code>Array.&lt;EquoMenu&gt;</code>](#EquoMenu) | 

<a name="EquoMenu+getType"></a>

### equoMenu.getType() ⇒ <code>string</code>
Get menu type. Returns EquoMenu or EquoMenuItem.

**Kind**: instance method of [<code>EquoMenu</code>](#EquoMenu)  
<a name="EquoMenu+getTitle"></a>

### equoMenu.getTitle() ⇒ <code>string</code>
Get menu title.

**Kind**: instance method of [<code>EquoMenu</code>](#EquoMenu)  
<a name="EquoMenu+getAction"></a>

### equoMenu.getAction() ⇒ <code>string</code>
Get action id from menu.

**Kind**: instance method of [<code>EquoMenu</code>](#EquoMenu)  
<a name="EquoMenu+getShortcut"></a>

### equoMenu.getShortcut() ⇒ <code>string</code>
Get shortcut from menu.

**Kind**: instance method of [<code>EquoMenu</code>](#EquoMenu)  
<a name="EquoMenu+getChildren"></a>

### equoMenu.getChildren() ⇒ [<code>Array.&lt;EquoMenu&gt;</code>](#EquoMenu)
Get childrens from menu.

**Kind**: instance method of [<code>EquoMenu</code>](#EquoMenu)  
<a name="EquoMenu+getId"></a>

### equoMenu.getId() ⇒ <code>string</code>
Get menu id.

**Kind**: instance method of [<code>EquoMenu</code>](#EquoMenu)  
<a name="EquoMenu+addChildrenAtIndex"></a>

### equoMenu.addChildrenAtIndex(index, children) ⇒ <code>boolean</code>
Add children in EquoMenu at the index. It will not be added if there is another child with the same title.

**Kind**: instance method of [<code>EquoMenu</code>](#EquoMenu)  

| Param | Type | Description |
| --- | --- | --- |
| index | <code>number</code> | Index to add item. |
| children | [<code>EquoMenu</code>](#EquoMenu) | Menu element to add. |

<a name="EquoMenu+removeMenuItemOfIndex"></a>

### equoMenu.removeMenuItemOfIndex(index) ⇒ <code>void</code>
Remove children at the index.

**Kind**: instance method of [<code>EquoMenu</code>](#EquoMenu)  

| Param | Type | Description |
| --- | --- | --- |
| index | <code>number</code> | Index to remove item. |

<a name="EquoMenu+removeMenuItemById"></a>

### equoMenu.removeMenuItemById(menuItemId) ⇒ <code>void</code>
Remove children by id.

**Kind**: instance method of [<code>EquoMenu</code>](#EquoMenu)  

| Param | Type | Description |
| --- | --- | --- |
| menuItemId | <code>string</code> | ID from menu to remove. |

<a name="EquoMenu+getTitleMenus"></a>

### equoMenu.getTitleMenus() ⇒ <code>Array.&lt;string&gt;</code>
Get all menu titles in the EquoMenu from all childrends.

**Kind**: instance method of [<code>EquoMenu</code>](#EquoMenu)  
<a name="Menu"></a>

## Menu : <code>object</code>
Equo-Application-Menu is a node package with the name '@equo/equo-application-menu' that allows through an api a native constructor of menus for Equo applications, in a chained way.

This document specifies the full usage methods for Equo-Application-Menu and full examples for each of them.
see [more](how-to-include-equo-components.md) about how to include Equo component in your projects.

**Kind**: global namespace  
<a name="create"></a>

## create() ⇒ [<code>MenuBuilder</code>](#MenuBuilder)
Create a menu instance.

**Kind**: global function  
<a name="getCurrentModel"></a>

## getCurrentModel(callback) ⇒ <code>void</code>
Get current app menu model in callback's parameter. You can build a new menu model from the one established in your application.

**Kind**: global function  

| Param | Type |
| --- | --- |
| callback | <code>function</code> | 

