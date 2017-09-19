package com.make.equo.application.client.api;

public class DummyEquoAppStarter {

	public static void main(String args[]) {
		try {
			EquoApplication
			.name("Hello Equo!")
			.withSingleView("www.netflix.com")
			.start();
		} catch (Exception e) {
			System.out.println("Unable to run the Equo app");
			e.printStackTrace();
		}
	}
	
}
