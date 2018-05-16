package com.make.equo.testapp;

import org.osgi.service.component.annotations.Component;

import com.make.equo.application.api.IEquoFramework;
import com.make.equo.application.model.EquoApplication;

@Component
public class TestApp implements IEquoFramework {

    @Override
    public EquoApplication buildApp(EquoApplication application) {
        return application
                .name("TestApp")
                .withSingleView("http://equo.maketechnology.io")
                .start();
    }

}
