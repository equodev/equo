package com.make.equo.application.api;

import com.make.equo.application.model.EquoApplicationBuilder;
import com.make.equo.application.model.IApplicationBuilder;

public interface IEquoApplication {

	public IApplicationBuilder buildApp(EquoApplicationBuilder appBuilder);

}
