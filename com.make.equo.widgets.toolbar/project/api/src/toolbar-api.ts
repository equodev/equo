export class Linker {

  private toolbarBuilder: ToolbarBuilder;
  private toolItemBuilder!: ToolItemBuilder;

  constructor() {
    this.toolbarBuilder = new ToolbarBuilder(this);
  }

  public getToolbarBuilder() : ToolbarBuilder {
    return this.toolbarBuilder;
  }

  public getToolItemBuilder() : ToolItemBuilder {
    return this.toolItemBuilder;
  }

  public createNewToolItemBuilder(toolbar: EquoToolbar) : ToolItemBuilder {
    this.toolItemBuilder = new ToolItemBuilder(this, toolbar);
    return this.toolItemBuilder;
  }

  public createNewToolbarBuilder() : ToolbarBuilder {
    this.toolbarBuilder = new ToolbarBuilder(this);
    return this.toolbarBuilder;
  }

}

//----------------------------BUILDERS----------------------------
export class ToolbarBuilder {
  private linker: Linker;
  private equoToolbar: EquoToolbar;

  constructor(linker: Linker) {
    this.linker = linker;
    this.equoToolbar = new EquoToolbar();
  }

  public withCustomColor(color: string) {
    return this;
  }

  public addToolItem(): ToolItemBuilder {
    let newToolItem = new EquoToolItem();
    this.equoToolbar.addToolItem(newToolItem);
    return this.linker.createNewToolItemBuilder(this.equoToolbar);
  }

  public build(): void {
    let toolbar = document.createElement('equotoolbarwc-toolbar');

    let toolbarColor = this.equoToolbar.getColour();
    if (typeof toolbarColor !== undefined) {
      toolbar.setAttribute('color', toolbarColor);
    }

    var toolitems: Array<EquoToolItem> = this.equoToolbar.getToolItems();

    for (let i = 0; i < toolitems.length; i++) {
      let toolitem = document.createElement('equotoolbarwc-toolitem');

      toolitem.setAttribute("icon", toolitems[i].getIcon());
      toolitem.setAttribute("eventhandler", toolitems[i].getEventHandler().name);
      toolitem.setAttribute("tooltip", toolitems[i].getTooltip());

      toolbar.appendChild(toolitem);
    }

    document.body.appendChild(toolbar);
  }

}

//-------------------------------------------
export class ToolItemBuilder {
  private linker: Linker;
  private toolItem!: EquoToolItem
  private toolbar: EquoToolbar

  constructor(linker: Linker, toolbar: EquoToolbar) {
    this.linker = linker;
    this.toolItem = new EquoToolItem();
    this.toolbar = toolbar;
    this.toolbar.addToolItem(this.toolItem);
  }

  public addToolItem(): ToolItemBuilder {
    return this.linker.createNewToolItemBuilder(this.toolbar);
  }

  public onClick(eventHandler: Function): ToolItemBuilder {
    this.toolItem.setEventHandler(eventHandler);
    return this;
  }

  public addIcon(icon: string): ToolItemBuilder {
    this.toolItem.setIcon(icon);
    return this;
  }

  public addTooltip(tooltip: string): ToolItemBuilder {
    this.toolItem.setTooltip(tooltip);
    return this;
  }

  public build(): void {
    this.linker.getToolbarBuilder().build();
  }

}
//--------------------------POJOS-----------------------
export class EquoToolbar {
  private colour: string;
  private childrens!: Array<EquoToolItem>;

  constructor() {
    this.colour = "";
    this.childrens = [];
  }

  public setColour(colour: string): void {
    this.colour = colour;
  }

  public getColour(): string {
    return this.colour;
  }

  public getToolItems(): Array<EquoToolItem> {
    return this.childrens;
  }

  public addToolItem(toolItem: EquoToolItem): void {
    this.childrens.push(toolItem);
  }

  public clearToolItems(): void {
    this.childrens = [];
  }
}
//----------------------------------------------------------
export class EquoToolItem {
  private tooltip: string;
  private icon: string;
  private eventHandler!: Function;

  constructor() {
    this.icon = "";
    this.tooltip = "";
  }

  public setTooltip(tooltip: string): void {
    this.tooltip = tooltip;
  }

  public getTooltip(): string {
    return this.tooltip;
  }

  public setIcon(icon: string): void {
    this.icon = icon;
  }

  public getIcon(): string {
    return this.icon;
  }

  public setEventHandler(eventHandler: Function): void {
    this.eventHandler = eventHandler;
  }

  public getEventHandler(): Function {
    return this.eventHandler;
  }

}

export namespace EquoToolbar {
  export function create(): ToolbarBuilder {
    return new Linker().getToolbarBuilder();
  }
}
