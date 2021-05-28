package com.equo.application.impl;

import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MParameter;

/**
 * Builder to create a new command parameter.
 */
public interface MParameterBuilder {

  /**
   * Creates a new parameter.
   * @param  id    parameter id
   * @param  value parameter value
   * @return       new created parameter
   */
  default MParameter createMParameter(String id, String value) {
    MParameter parameter = MCommandsFactory.INSTANCE.createParameter();
    // set the identifier for the corresponding command parameter
    parameter.setName(id);
    parameter.setValue(value);
    return parameter;
  }

}
