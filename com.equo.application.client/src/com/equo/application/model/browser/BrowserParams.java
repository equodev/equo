package com.equo.application.model.browser;

/**
 * Encapsulation of parameters required to open a new browser.
 */
public class BrowserParams {

  private String url;
  private String name;
  private String position;

  public BrowserParams() {
  }

  public BrowserParams(String url) {
    this();
    this.url = url;
  }

  public BrowserParams(String url, String name) {
    this(url);
    this.name = name;
  }

  public BrowserParams(String url, String name, String position) {
    this(url, name);
    this.position = position;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getUrl() {
    return url;
  }

  public String getName() {
    return name;
  }

  public String getPosition() {
    return position;
  }

}
