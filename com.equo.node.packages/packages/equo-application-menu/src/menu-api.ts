/****************************************************************************
**
** Copyright (C) 2021 Equo
**
** This file is part of Equo Framework.
**
** Commercial License Usage
** Licensees holding valid commercial Equo licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Equo. For licensing terms
** and conditions see https://www.equoplatform.com/terms.
**
** GNU General Public License Usage
** Alternatively, this file may be used under the terms of the GNU
** General Public License version 3 as published by the Free Software
** Foundation. Please review the following
** information to ensure the GNU General Public License requirements will
** be met: https://www.gnu.org/licenses/gpl-3.0.html.
**
****************************************************************************/


import { EquoCommService, EquoComm } from "@equo/comm"

export class Linker{

  private menuBuilder: MenuBuilder;
  private menuItemBuilder: MenuItemBuilder;
  private menuItemSeparatorBuilder: MenuItemSeparatorBuilder;
  private menuAct!: EquoMenu;
  public buildWithCurrentModel!: Function;
  public buildMenuItemPosition!: number;

  constructor(){
    this.menuItemBuilder = new MenuItemBuilder(this);
    this.menuBuilder = new MenuBuilder(this);
    this.menuItemSeparatorBuilder = new MenuItemSeparatorBuilder(this);
  }

  public setBuildWithCurrentModel(func: Function): void {
    this.buildWithCurrentModel = func;
    this.menuBuilder = new MenuBuilder(this);
  }

  public execBuildWithCurrentModel():void {
    this.buildWithCurrentModel(this.menuBuilder);
  }

  public getMenuBuilder(): MenuBuilder {
    return this.menuBuilder;
  }

  public getMenuItemBuilder(): MenuItemBuilder {
    return this.menuItemBuilder;
  }

  public getMenuItemSeparatorBuilder(): MenuItemSeparatorBuilder {
    return this.menuItemSeparatorBuilder;
  }

  public setMenuAct(menuAct: EquoMenu): void {
    this.menuAct = menuAct;
  }

  public getMenuAct(): EquoMenu {
    return this.menuAct;
  }
}

export class MenuItemSeparatorBuilder{
  private linker: Linker;
  /**
   * @name MenuItemSeparatorBuilder
   * @class
   */
  constructor(linker: Linker) {
    this.linker = linker;
  }
  /**
   * Adds a new menu that will contain other sub menus.
   * @param {string} label - Menu title.
   * @return {MenuBuilder}
   */
  public addMenu(label: string): MenuBuilder {
    return this.linker.getMenuItemBuilder().addMenu(label);
  }
  /**
   * Adds a new menu item that will not contain other menus.
   * @param {string} label - Menu title.
   * @return {MenuItemBuilder|null} If name exists and the menu type is 'EquoMenuItem' will return MenuItemBuilder. If exists and type is 'EquoMenu' will return null.
   */
  public addMenuItem(label: string): MenuItemBuilder | null {
    return this.linker.getMenuItemBuilder().addMenuItem(label);
  }
}

export class MenuBuilder{
  private comm = EquoCommService.get();
  protected menus = new Array<EquoMenu>();
  private linker: Linker;
  private indexToAddItem!: number;
  /**
   * @name MenuBuilder
   * @class
   */
  constructor(linker: Linker) {
    this.linker = linker;

    if (this.linker.buildWithCurrentModel)
      this.bindEquoFunctions();
  }
  /**
   * Creates a root menu, visible in the bar.
   * @param {string} label - Menu title.
   * @return {MenuBuilder}
   */
  public withMainMenu(label: string): MenuBuilder {
    let menu = new EquoMenu();
    menu.setType("EquoMenu");
    menu.setTitle(label);

    let exist = false;
    this.menus.forEach(element => {
      if (element.getTitle() === menu.getTitle()) {
        exist = true;
        this.linker.setMenuAct(element);
        return;
      }
    });

    if (!exist) {
      this.linker.setMenuAct(menu);
      this.menus.push(menu);
    }
    return this;
  }
  /**
   * Adds a new menu that will contain other sub menus.
   * @param {string} label - Menu title.
   * @return {MenuBuilder}
   */
  public addMenu(label: string): MenuBuilder {
    let equoMenu = new EquoMenu();
    equoMenu.setType("EquoMenu");
    equoMenu.setTitle(label);

    let index = this.linker.getMenuAct().addChildren(equoMenu);
    if (!(index !== -1 && this.linker.getMenuAct().getChildren()[index].getType() === "EquoMenuItem")) {
      this.linker.setMenuAct(this.linker.getMenuAct().getChildren()[index]);
    }

    return this;
  }
  /**
   * Adds a new menu item that will not contain other menus.
   * @param {string} label - Menu title.
   * @return {MenuItemBuilder|null} If name exists and the menu type is 'EquoMenuItem' will return MenuItemBuilder. If exists and type is 'EquoMenu' will return null.
   */
  public addMenuItem(label: string): MenuItemBuilder | null{
    let equoMenu = new EquoMenu();
    equoMenu.setType("EquoMenuItem");
    equoMenu.setTitle(label);

    this.linker.buildMenuItemPosition = this.linker.getMenuAct().addChildren(equoMenu);
    if (this.linker.buildMenuItemPosition !== -1 && this.linker.getMenuAct().getChildren()[this.linker.buildMenuItemPosition].getType() === "EquoMenu") {
      return null;
    }
    return this.linker.getMenuItemBuilder();
  }

  private bindEquoFunctions(): void {
    this.comm.on("_doGetMenu", (values: JSON) => {
      let params = JSON.parse(JSON.stringify(values));

      let equoMenuModel = new EquoMenuModel(new Array<EquoMenu>());
      equoMenuModel.fillFromJSON(JSON.stringify(params["menus"]));
      this.menus = equoMenuModel.getMenus();

      this.linker.execBuildWithCurrentModel();

    })
    this.comm.send("_getMenu", {});
  }
  /**
   * @callback setApplicationMenuCallback
   * @param {EquoComm} comm - EquoComm instance.
   * @param {JSON} json - Json menu.
   */
  /**
   * Sets the application menu to the current app menu. Optionally, pass a function parameter for custom actions called once the operation has been done.
   * @param {setApplicationMenuCallback} [callback] - Optional
   * @returns {void}
   */
  public setApplicationMenu(funct?: (comm: EquoComm, json :JSON) => void): void {
    let equoMenuModel = new EquoMenuModel(this.menus);
    this.setApplicationMenuWithJson(JSON.parse(JSON.stringify(equoMenuModel)));

    if(funct){
      funct(this.comm, JSON.parse(JSON.stringify(equoMenuModel)));
    }
  }
  /**
   * Sets the application menu from a json format to the current model.
   * @param {JSON} json - Json menu
   * @returns {void}
   */
  public setApplicationMenuWithJson(json: JSON): void{
    this.comm.send("_setMenu", json);
  }
  /**
   * Gets the all Equo menus from the under construction Menu.
   * @returns {EquoMenu[]}
   */
  public getMenus(): Array<EquoMenu>{
      return this.menus;
  }
  /**
   * Appends a new menu item that will not contain other menus.
   * @param {string} menuPath - Path to add menu.
   * @param {number} indexToAddItem - Index to add item.
   * @param {string} menuItemName - Menu title.
   * @returns {MenuItemBuilder|null} If name exists and the menu type is 'EquoMenuItem' will return MenuItemBuilder. If exists and type is 'EquoMenu' will return null.
   */
  public appendMenuItem(menuPath: string, indexToAddItem: number, menuItemName:string): MenuItemBuilder | null{
    this.indexToAddItem = indexToAddItem;
    if (this.createEquoMenu(menuItemName, "EquoMenuItem", menuPath))
      return this.linker.getMenuItemBuilder();
    return null;
  }
  /**
   * Appends the menu using path location.
   * @param {string} menuPath - Path to add menu.
   * @param {number} indexToAddItem - Index to add item.
   * @param {string} menuName - Menu title.
   * @returns {MenuItemBuilder|null} Returns null if menuPath does not exists
   */
  public appendMenu(menuPath: string, indexToAddItem: number, menuName:string): MenuBuilder | null {
    this.indexToAddItem = indexToAddItem;
    if (this.createEquoMenu(menuName, "EquoMenu", menuPath))
      return this;
    return null;
  }

  private createEquoMenu(title: string, type:string, path:string): boolean {
    let equoMenu = new EquoMenu();
    equoMenu.setType(type);
    equoMenu.setTitle(title);
    return this.searchByPathMenuRecursively(this.menus, path, equoMenu);
  }
  /**
   * Appends the menu item at the end.
   * @param {string} menuElementPath - Path to menu element to add.
   * @param {string} menuItemName - Menu title.
   * @returns {MenuItemBuilder|null} If name exists and the menu type is 'EquoMenuItem' will return MenuItemBuilder. If exists and type is 'EquoMenu' or menuPath does not exists will return null.
   */
  public appendMenuItemAtTheEnd(menuElementPath: string, menuItemName:string): MenuItemBuilder | null{
    return this.appendMenuItem(menuElementPath,-1, menuItemName);
  }
  /**
   * Appends the menu at the end.
   * @param {string} menuElementPath - Path to menu element to add.
   * @param {string} menuName - Menu title.
   * @returns {MenuBuilder|null} Returns null if menuElementPath does not exists
   */
  public appendMenuAtTheEnd(menuElementPath: string, menuName:string): MenuBuilder | null{
    return this.appendMenu(menuElementPath,-1, menuName);
  }

  private searchByPathMenuRecursively(menuItems: Array<EquoMenu>, path: string, equoMenu:EquoMenu): boolean{
    let arr = path.split("/");
    let newPath = path;
    let inserted = false;
    if (path !== "") {
      for (var i=0; i<menuItems.length; i++){
        if (menuItems[i].getTitle() === arr[0]) {
          arr.splice(0, 1);
          newPath = arr.join("/");

          if (newPath === "") {
            if (menuItems[i].addChildrenAtIndex(this.indexToAddItem, equoMenu)) {
              if (menuItems[i].getType() === "EquoMenu") {
                if (equoMenu.getType() === "EquoMenu")
                  this.linker.setMenuAct(equoMenu);
                else
                  this.linker.setMenuAct(menuItems[i]);
              }
              inserted = true;
              break;
            }
          }
        }
        if (menuItems[i].getType() === "EquoMenu"){
          inserted = this.searchByPathMenuRecursively(menuItems[i].getChildren(), newPath, equoMenu);
          if (inserted)
              break;
        }
      };
    }
    return inserted;
  }
  /**
   * Adds a new EquoMenu to the model using the parent menu id. If the parent menu id exists in the constructor, it will be added at the specified parent index.
   * @param {string} parentMenuId - Parent ID to be added to.
   * @param {number} index - Index to add item.
   * @param {EquoMenu} menuItem - Menu element to add.
   * @returns {void}
   */
  public addMenuItemToModel(parentMenuId: string, index: number, menuItem: EquoMenu):void {
    //if not found, then insert in position on main menu
    if (!this.addMenuElementRecursively(this.menus, parentMenuId, index, menuItem)) {
      this.menus.splice(index, 0, menuItem);
    }
  }

  private addMenuElementRecursively(menus: Array<EquoMenu>, parentMenuId: string, index: number, menuItem:EquoMenu): boolean {
    let added = false;
    for (var i = 0; i < menus.length; i++){
      if (menus[i].getId() === parentMenuId) {
        menus[i].addChildrenAtIndex(index, menuItem);
        added = true;
        break;
      }
      if (menus[i].getType() === "EquoMenu") {
        added = this.addMenuElementRecursively(menus[i].getChildren(), parentMenuId, index, menuItem);
        if (added)
          break;
      }
    };
    return added;
  }
  /**
   * Removes the menu element by id.
   * @param {string} menuToRemoveId - ID from menu to remove.
   * @returns {boolean} Returns true if the menu was found and removed, false otherwise.
   */
  public removeMenuElementById(menuToRemoveId: string): boolean {
    return this.removeMenuElementByIdRecursively(this.menus, menuToRemoveId);
  }

  private removeMenuElementByIdRecursively(menuItems: Array<EquoMenu>, menuToRemoveId: string): boolean {
    let removed = false;
    for (var i = 0; i < menuItems.length; i++){
      if (menuItems[i].getId() === menuToRemoveId) {
        menuItems.splice(i, 1);
        removed = true;
        this.setApplicationMenu();
        break;
      }
      if (menuItems[i].getType() === "EquoMenu") {
        removed = this.removeMenuElementByIdRecursively(menuItems[i].getChildren(), menuToRemoveId);
        if (removed)
          break;
      }
    };
    return removed;
  }
  /**
   * Removes the menu element by path. If the element exists, it will be removed.
   * @param {string} menuNamePathToRemove - Path|name from menu to remove.
   * @returns {MenuBuilder}
   */
  public removeMenuElementByPath(menuNamePathToRemove: string): MenuBuilder {
    this.removeMenuElementByPathRecursively(this.menus, menuNamePathToRemove);
    return this;
  }

  private removeMenuElementByPathRecursively(menuItems: Array<EquoMenu>, menuNamePathToRemove: string): boolean {
    let removed = false;
    let arr = menuNamePathToRemove.split("/");
    let newPath = menuNamePathToRemove;
    if (menuNamePathToRemove !== "") {
      let index = 0;
      for (var i = 0; i < menuItems.length; i++ ){
        if (menuItems[i].getTitle() === arr[0]) {
          arr.splice(0, 1);
          newPath = arr.join("/");
          if (newPath === "") {
            menuItems.splice(index, 1);
            removed = true;
            break;
          }
        }
        index++;
        if (menuItems[i].getType() === "EquoMenu") {
          removed = this.removeMenuElementByPathRecursively(menuItems[i].getChildren(), newPath);
          if (removed)
            break;
        }
      };
    }
    return removed;
  }
}

export class MenuItemBuilder {
  private linker: Linker;
  /**
   * @name MenuItemBuilder
   * @class
   */
  constructor(linker: Linker) {
    this.linker = linker;
  }
  /**
   * Creates a root menu, visible in the bar.
   * @param {string} label - Menu title.
   * @return {MenuBuilder}
   */
  public withMainMenu(label: string): MenuBuilder{
    return this.linker.getMenuBuilder().withMainMenu(label);
  }
  /**
   * Adds a new menu item that will not contain other menus.
   * @param {string} label - Menu title.
   * @return {MenuItemBuilder|null} If name exists and the menu type is 'EquoMenuItem' will return MenuItemBuilder. If exists and type is 'EquoMenu' will return null.
   */
  public addMenuItem(label: string): MenuItemBuilder | null{
    return this.linker.getMenuBuilder().addMenuItem(label);
  }
  /**
   * Adds the action in menu element.
   * @param {string|function} action - Define onclick action.
   * @returns {MenuItemBuilder}
   */
  public onClick(action: string| (() => void)): MenuItemBuilder{
    if (action instanceof Function){
      this.linker.getMenuAct().getChildren()[this.linker.buildMenuItemPosition].setRunnable(action);
    }else{
      this.linker.getMenuAct().getChildren()[this.linker.buildMenuItemPosition].setAction(action);
    }
    return this;
  }
  /**
   * Adds the shortcut in menu element.
   * @param {string} shortcut - Define shortcut.
   * @returns {MenuItemBuilder}
   */
  public addShortcut(shortcut: string): MenuItemBuilder{
    this.linker.getMenuAct().getChildren()[this.linker.buildMenuItemPosition].setShortcut(shortcut);
    return this;
  }
  /**
   * Adds a new menu that will contain other sub menus.
   * @param {string} label - Menu title.
   * @return {MenuBuilder}
   */
  public addMenu(label: string): MenuBuilder {
    return this.linker.getMenuBuilder().addMenu(label);
  }
  /**
   * Adds a separator between menus.
   * @returns {MenuItemSeparatorBuilder}
   */
  public addMenuSeparator(): MenuItemSeparatorBuilder {
    let equoMenu = new EquoMenu();
    equoMenu.setType("EquoMenuItemSeparator");
    this.linker.getMenuAct().addChildren(equoMenu);
    return this.linker.getMenuItemSeparatorBuilder();
  }
  /**
   * Sets the application menu to the current app menu. Optionally, pass a function parameter for custom actions called once the operation has been done.
   * @param {setApplicationMenuCallback} [callback] - Optional
   * @returns {void}
   */
  public setApplicationMenu(funct?: (comm: EquoComm, json :JSON) => void):void {
    this.linker.getMenuBuilder().setApplicationMenu(funct);
  }
}

export class EquoMenuModel{
  private menus:Array<EquoMenu>;

  constructor(arrayEquoMenu: Array<EquoMenu>) {
    this.menus = arrayEquoMenu;
  }

  public getMenus(): Array<EquoMenu> {
    return this.menus;
  }

  fillFromJSON(json: string): void {
    var jsonObj = JSON.parse(json);

    for (var jsonMenu in jsonObj){
      let menu = new EquoMenu();
      menu.fillFromJSON(JSON.stringify(jsonObj[jsonMenu]));
      this.menus.push(menu);
    }
  }
}
export class EquoMenu{
  private type!:string;
  private title!: string;
  private children!: Array<EquoMenu>;
  private shortcut!: string;
  private action!: string;
  private id: string;
  /**
   * @name EquoMenu
   * @class
   */
  constructor() {
    this.id = Math.random().toString(36).substr(2, 9).trim();
  }
  /**
   * Initializes the EquoMenu from json.
   * @param {JSON} json - Json with menu.
   * @returns {void}
   */
  public fillFromJSON(json: string): void {
    var jsonObj = JSON.parse(json);
    this.title = jsonObj["title"];
    this.type = jsonObj["type"];
    if (jsonObj["shortcut"])
      this.shortcut = jsonObj["shortcut"];
    if (jsonObj["action"])
      this.action = jsonObj["action"];

    if (this.type === "EquoMenu") {
      this.children = new Array<EquoMenu>();

      let childs = jsonObj["children"];
      for (var elementChild in childs) {
        let child = new EquoMenu();
        child.fillFromJSON(JSON.stringify(childs[elementChild]));
        this.children.push(child);
      }
    }
  }
  /**
   * Sets the specific runnable for menu action.
   * @param {Function} runnable 
   * @returns {void}
   */
  public setRunnable(runnable: () => void): void{
    EquoCommService.get().on(this.id, runnable);
    this.setAction(this.id);
  }
  /**
   * Sets the menu type. Valid types are EquoMenu or EquoMenuItem.
   * @param {string} type - Valid types: EquoMenu | EquoMenuItem
   * @returns {void}
   */
  public setType(type: string): void{
    if (type === "EquoMenu")
      this.children = new Array<EquoMenu>();
    this.type = type;
  }
  /**
   * Sets the title for menu.
   * @param {string} title - Menu title
   * @returns {void} 
   */
  public setTitle(title: string): void{
    this.title = title;
  }
  /**
   * Sets the action id for menu.
   * @param {string} action - Action ID.
   * @returns {void}
   */
  public setAction(action: string): void{
    this.action = action;
  }
  /**
   * Sets the shortcut for menu.
   * @param {string} shortcut
   * @returns {void}
   */
  public setShortcut(shortcut: string): void{
    this.shortcut = shortcut;
  }
  /**
   * Adds the children in EquoMenu at the end. It will not be added if there is another child with the same title. Returns -1 if it is not added or the index to which it was added.
   * @param {EquoMenu} children
   * @returns {void}
   */
  public addChildren(children: EquoMenu): number{
    let position= -1;
    for (var i = 0; i < this.children.length; i++ ){
      if (this.children[i].getTitle() === children.getTitle()) {
        position = i;
        break;
      }
    };
    if (position === -1) {
      this.children.push(children);
      position = this.children.length-1;
    }
    return position;
  }
  /**
   * Sets the childrens for menu.
   * @param {EquoMenu[]} childrens
   * @returns {void}
   */
  public setChildren(childrens: Array<EquoMenu>): void{
    this.children = childrens;
  }
  /**
   * Gets the menu type. Returns EquoMenu or EquoMenuItem.
   * @returns {string} 
   */
  public getType():string {
    return this.type;
  }
  /**
   * Gets the menu title.
   * @returns {string}
   */
  public getTitle():string {
    return this.title;
  }
  /**
   * Gets the action id from menu.
   * @returns {string}
   */
  public getAction():string {
    return this.action;
  }
  /**
   * Gets the shortcut from menu.
   * @returns {string}
   */
  public getShortcut():string {
    return this.shortcut;
  }
  /**
   * Gets the childrens from menu.
   * @returns {EquoMenu[]}
   */
  public getChildren(): Array<EquoMenu> {
    return this.children;
  }
  /**
   * Gets the menu id.
   * @returns {string}
   */
  public getId(): string {
    return this.id;
  }
  /**
   * Adds the children in EquoMenu at the index. It will not be added if there is another child with the same title.
   * @param {number} index - Index to add item.
   * @param {EquoMenu} children - Menu element to add.
   * @returns {boolean}
   */
  public addChildrenAtIndex(index: number, children: EquoMenu): boolean {
    let exist = false;
    this.children.forEach(element => {
      if (element.getTitle() === children.getTitle()) {
        exist = true;
        return;
      }
    });
    if (!exist) {
      if (index >= 0)
        this.children.splice(index, 0, children);
      else
        this.children.push(children);
    }
    return !exist;
  }
  /**
   * Removes the children at the index.
   * @param {number} index - Index to remove item.
   * @returns {void}
   */
  public removeMenuItemOfIndex(index: number): void {
    this.children.splice(index,1);
  }
  /**
   * Removes the children by id.
   * @param {string} menuItemId - ID from menu to remove.
   * @returns {void}
   */
  public removeMenuItemById(menuItemId: string): void {
    let index = 0;
    this.children.forEach(element => {
      if (element.getId() === menuItemId) {
        this.removeMenuItemOfIndex(index);
        return;
      }
      index++;
    });
  }
  /**
   * Gets the all menu titles in the EquoMenu from all childrends.
   * @returns {string[]}
   */
  public getTitleMenus(): Array<string>{
    let titleMenus = new Array<string>();
    titleMenus.push(this.title);

    if (this.type === "EquoMenu") {
      this.children.forEach(child => {
        titleMenus.push(...child.getTitleMenus());
      });
    }
    return titleMenus;
  }
}
/**
 * @namespace
 * @description Equo-Application-Menu is a node package with the name '@equo/equo-application-menu' that allows through an api a native constructor of menus for Equo applications, in a chained way.
 * 
 * This document specifies the usage methods for Equo-Application-Menu.
 */
export namespace Menu{
  /**
   * Creates a menu instance.
   * @function
   * @name create
   * @returns {MenuBuilder}
   */
  export function create(): MenuBuilder {
    return new Linker().getMenuBuilder();
  }
  /**
   * Gets the current app menu model in callback's parameter. You can build a new menu model from the one established in your application.
   * @function
   * @name getCurrentModel
   * @param  {Function} callback
   * @returns {void}
   */
  export function getCurrentModel(funct: (mb : MenuBuilder) => void): void{
    new Linker().setBuildWithCurrentModel(funct);
  }
}
