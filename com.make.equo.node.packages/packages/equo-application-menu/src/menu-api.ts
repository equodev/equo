
import { EquoWebSocketService, EquoWebSocket } from '@equo/websocket'

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

  public setBuildWithCurrentModel(func: Function) {
    this.buildWithCurrentModel = func;
    this.menuBuilder = new MenuBuilder(this);
  }

  public execBuildWithCurrentModel():void {
    this.buildWithCurrentModel(this.menuBuilder);
  }

  public getMenuBuilder() {
    return this.menuBuilder;
  }

  public getMenuItemBuilder() {
    return this.menuItemBuilder;
  }

  public getMenuItemSeparatorBuilder() {
    return this.menuItemSeparatorBuilder;
  }

  public setMenuAct(menuAct: EquoMenu) {
    this.menuAct = menuAct;
  }

  public getMenuAct() {
    return this.menuAct;
  }
}

export class MenuItemSeparatorBuilder{
  private linker: Linker;

  constructor(linker: Linker) {
    this.linker = linker;
  }

  public addMenu(label: string): MenuBuilder {
    return this.linker.getMenuItemBuilder().addMenu(label);
  }

  public addMenuItem(label: string): MenuItemBuilder | null {
    return this.linker.getMenuItemBuilder().addMenuItem(label);
  }
}

export class MenuBuilder{
  private webSocket= EquoWebSocketService.get();
  protected menus = new Array<EquoMenu>();
  private linker: Linker;
  private indexToAddItem!: number;

  constructor(linker: Linker) {
    this.linker = linker;

    if (this.linker.buildWithCurrentModel)
      this.bindEquoFunctions();
  }

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
    this.webSocket.on("_doGetMenu", (values: JSON) => {
      let params = JSON.parse(JSON.stringify(values));

      let equoMenuModel = new EquoMenuModel(new Array<EquoMenu>());
      equoMenuModel.fillFromJSON(JSON.stringify(params["menus"]));
      this.menus = equoMenuModel.getMenus();

      this.linker.execBuildWithCurrentModel();

    })
    this.webSocket.send("_getMenu", {});
  }

  public setApplicationMenu(funct?: (ws: EquoWebSocket, json :JSON) => void): void {
    let equoMenuModel = new EquoMenuModel(this.menus);
    this.setApplicationMenuWithJson(JSON.parse(JSON.stringify(equoMenuModel)));

    if(funct){
      funct(this.webSocket, JSON.parse(JSON.stringify(equoMenuModel)));
    }
  }

  public setApplicationMenuWithJson(json: JSON): void{
    this.webSocket.send("_setMenu", json);
  }

  public getMenus(): Array<EquoMenu>{
      return this.menus;
  }

  public appendMenuItem(menuPath: string, indexToAddItem: number, menuItemName:string): MenuItemBuilder | null{
    this.indexToAddItem = indexToAddItem;
    if (this.createEquoMenu(menuItemName, "EquoMenuItem", menuPath))
      return this.linker.getMenuItemBuilder();
    return null;
  }

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

  public appendMenuItemAtTheEnd(itemMenuPath: string, menuItemName:string): MenuItemBuilder | null{
    return this.appendMenuItem(itemMenuPath,-1, menuItemName);
  }

  public appendMenuAtTheEnd(itemMenuPath: string, menuName:string): MenuBuilder | null{
    return this.appendMenu(itemMenuPath,-1, menuName);
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

  public addMenuItemToModel(parentMenuId: string, index: number, menuItem: EquoMenu):void {
    //if not found, then insert in position on main menu
    if (!this.addMenuElementRecursively(this.menus, parentMenuId, index, menuItem)) {
      this.menus.splice(index, 0, menuItem);
    }
  }

  private addMenuElementRecursively(menus: Array<EquoMenu>, parentMenuId: string, index: number, menuItem:EquoMenu): boolean {
    let removed = false;
    for (var i = 0; i < menus.length; i++){
      if (menus[i].getId() === parentMenuId) {
        menus[i].addChildrenAtIndex(index, menuItem);
        removed = true;
        break;
      }
      if (menus[i].getType() === "EquoMenu") {
        removed = this.addMenuElementRecursively(menus[i].getChildren(), parentMenuId, index, menuItem);
        if (removed)
          break;
      }
    };
    return removed;
  }

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

  constructor(linker: Linker) {
    this.linker = linker;
  }

  public withMainMenu(label: string): MenuBuilder{
    return this.linker.getMenuBuilder().withMainMenu(label);
  }

  public addMenuItem(label: string): MenuItemBuilder | null{
    return this.linker.getMenuBuilder().addMenuItem(label);
  }

  public onClick(action: string| (() => void)): MenuItemBuilder{
    if (action instanceof Function){
      this.linker.getMenuAct().getChildren()[this.linker.buildMenuItemPosition].setRunnable(action);
    }else{
      this.linker.getMenuAct().getChildren()[this.linker.buildMenuItemPosition].setAction(action);
    }
    return this;
  }

  public addShortcut(shortcut: string): MenuItemBuilder{
    this.linker.getMenuAct().getChildren()[this.linker.buildMenuItemPosition].setShortcut(shortcut);
    return this;
  }

  public addMenu(label: string): MenuBuilder {
    return this.linker.getMenuBuilder().addMenu(label);
  }

  public addMenuSeparator(): MenuItemSeparatorBuilder {
    let equoMenu = new EquoMenu();
    equoMenu.setType("EquoMenuItemSeparator");
    this.linker.getMenuAct().addChildren(equoMenu);
    return this.linker.getMenuItemSeparatorBuilder();
  }

  public setApplicationMenu(funct?: (ws: EquoWebSocket, json :JSON) => void):void {
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

  fillFromJSON(json: string) {
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
  
  constructor() {
    this.id = Math.random().toString(36).substr(2, 9).trim();
  }

  public fillFromJSON(json: string) {
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

  public setRunnable(runnable: () => void){
    EquoWebSocketService.get().on(this.id, runnable);
    this.setAction(this.id);
  }

  public setType(type: string): void{
    if (type === "EquoMenu")
      this.children = new Array<EquoMenu>();
    this.type = type;
  }

  public setTitle(title: string): void{
    this.title = title;
  }

  public setAction(action: string): void{
    this.action = action;
  }

  public setShortcut(shortcut: string): void{
    this.shortcut = shortcut;
  }

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

  public setChildren(childrens: Array<EquoMenu>): void{
    this.children = childrens;
  }

  public getType():string {
    return this.type;
  }

  public getTitle():string {
    return this.title;
  }

  public getAction():string {
    return this.action;
  }

  public getShortcut():string {
    return this.shortcut;
  }

  public getChildren(): Array<EquoMenu> {
    return this.children;
  }

  public getId() {
    return this.id;
  }

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

  public removeMenuItemOfIndex(index: number) {
    this.children.splice(index,1);
  }

  public removeMenuItemById(menuItemId: string) {
    let index = 0;
    this.children.forEach(element => {
      if (element.getId() === menuItemId) {
        this.removeMenuItemOfIndex(index);
        return;
      }
      index++;
    });
  }

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

export namespace EquoMenu{
  export function create(): MenuBuilder {
    return new Linker().getMenuBuilder();
  }
  export function getCurrentModel(funct: (mb : MenuBuilder) => void): void{
    new Linker().setBuildWithCurrentModel(funct);
  }
}