package com.make.equo.server.api.impl;

public class StartEquoAppServiceImpl implements StartEquoAppService {

	@Override
	public void start() {
		try {
//			new EquoApplicationClient().start();
		} catch (Exception e) {
			System.out.println("No se pudo iniciar la app");
			e.printStackTrace();
		}
	}
}
