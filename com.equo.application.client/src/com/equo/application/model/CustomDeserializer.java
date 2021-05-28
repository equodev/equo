package com.equo.application.model;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * Custom deserializer for equo menu objects.
 */
public class CustomDeserializer implements JsonDeserializer<Menu> {

  private Map<String, Class<? extends AbstractEquoMenu>> typeRegistry;

  /**
   * Initializes custom deserializer for equo menu objects.
   */
  public CustomDeserializer() {
    this.typeRegistry = new HashMap<>();
  }

  /**
   * Registers a valid menu class for custom deserialization of menu objects equo.
   * @param menuTypeName the menu type name.
   * @param menuType     the menu class.
   */
  public void registerMenuType(String menuTypeName, Class<? extends AbstractEquoMenu> menuType) {
    this.typeRegistry.put(menuTypeName, menuType);
  }

  /**
   * Deserializes the json menu to equo menu model.
   */
  @Override
  public Menu deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject menu = json.getAsJsonObject();

    Menu equoMenuModel = new Menu();

    JsonArray menus = menu.get("menus").getAsJsonArray();

    for (JsonElement je : menus) {
      equoMenuModel.addMenu((EquoMenu) getJsonMenu(equoMenuModel, (JsonObject) je, context));
    }

    return equoMenuModel;
  }

  private AbstractEquoMenu getJsonMenu(IEquoMenu parent, JsonObject menu,
      JsonDeserializationContext context) {
    String type = menu.get("type").getAsString();
    switch (type) {
      case EquoMenu.CLASSNAME:
        EquoMenu equoMenu = new EquoMenu(parent, menu.get("title").getAsString());
        JsonArray subMenus = menu.get("children").getAsJsonArray();
        for (JsonElement je : subMenus) {
          equoMenu.addItem(getJsonMenu(equoMenu, (JsonObject) je, context));
        }
        return equoMenu;
      default:
        if (typeRegistry.containsKey(type)) {
          return context.deserialize(menu, typeRegistry.get(type));
        }
        return new EquoMenu(parent, "");
    }
  }
}