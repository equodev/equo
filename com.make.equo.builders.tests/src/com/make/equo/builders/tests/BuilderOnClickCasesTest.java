package com.make.equo.builders.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URISyntaxException;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.make.equo.application.model.EquoApplicationBuilder;
import com.make.equo.builders.tests.util.ModelTestingConfigurator;

public class BuilderOnClickCasesTest extends EquoInjectableTest{

	@Inject
	private EquoApplicationBuilder appBuilder;

	@Rule
	public EquoRule injector = new EquoRule(this);

	@Before
	public void before() {
		modelTestingConfigurator.configure(appBuilder);
		assertThat(appBuilder).isNotNull();
	}

	private ModelTestingConfigurator modelTestingConfigurator = new ModelTestingConfigurator();

	@Test
	public void toolbarSimpleTest() throws URISyntaxException {
		assertThat(appBuilder.plainApp("/").withToolbar().addToolItem("text", "text").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("toolitem event 1");
			}
		})).isNotNull();
	}

	@Test
	public void menuSimpleTest() throws URISyntaxException {
		assertThat(appBuilder.plainApp("/").withMainMenu("Menu1").addMenuItem("item1").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("menuitem event 1");
			}
		})).isNotNull();
	}

	@Test
	public void menuAndToolbarTest() throws URISyntaxException {
		assertThat(appBuilder.plainApp("/").withMainMenu("Menu1").addMenuItem("item1").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("menuitem event 1");
			}
		}).withToolbar().addToolItem("text", "text").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("toolitem event 1");
			}
		})).isNotNull();
	}

	@Test
	public void toolbarAndMenuTest() throws URISyntaxException {
		assertThat(appBuilder.plainApp("/").withToolbar().addToolItem("text", "text").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("toolitem event 1");
			}
		}).withMainMenu("Menu1").addMenuItem("item1").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("menuitem event 1");
			}
		})).isNotNull();
	}


}
