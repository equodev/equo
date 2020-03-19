package com.make.equo.builders.tests;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;

import com.make.equo.application.model.EquoApplicationBuilder;
import com.make.equo.builders.tests.util.ModelTestingConfigurator;

public abstract class EquoInjectableTest {

	@Inject
	protected EquoApplicationBuilder appBuilder;
	
	protected ModelTestingConfigurator modelTestingConfigurator = new ModelTestingConfigurator();
	
	@Rule
	public EquoRule injector = new EquoRule(this);

	public EquoInjectableTest() {
		super();
	}

	@Before
	public void before() {
		modelTestingConfigurator.configure(appBuilder);
		assertThat(appBuilder).isNotNull();
	}

}