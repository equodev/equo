package com.make.equo.builders.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;

import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.model.EquoApplicationBuilder;
import com.make.equo.testing.common.util.EquoRule;
import com.make.equo.builders.tests.util.ModelTestingConfigurator;

public abstract class AbstractBuilderTest {

	@Inject
	protected EquoApplicationBuilder appBuilder;
	
	@Inject
	protected IEquoApplication equoApp; 
	
	protected ModelTestingConfigurator modelTestingConfigurator = new ModelTestingConfigurator();
	
	@Rule
	public EquoRule injector = new EquoRule(this);

	@Before
	public void before() {
		modelTestingConfigurator.configure(appBuilder,equoApp);
		assertThat(appBuilder).isNotNull();
	}
	
	protected <T> Object getValueFromField(Class<T> clazz, Object O, String fieldName)
			throws NoSuchFieldException, IllegalAccessException {
		Field field = clazz.getDeclaredField(fieldName);    
		  field.setAccessible(true);
		  Object value = field.get(O);
		return value;
	}

}