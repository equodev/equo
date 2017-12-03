package com.make.equo.application.client.api;

import com.make.equo.application.api.IEquoFramework;
import com.make.equo.application.model.EquoApplication;
import com.make.equo.application.model.MenuItemBuilder;
import com.make.equo.application.model.OptionalViewBuilder;


public class DummyEquoAppStarter implements IEquoFramework{
	
//	public class NetflixApplication implements IEquoFramework {

	@Override
	public EquoApplication buildApp(EquoApplication application) {
		OptionalViewBuilder mainView = application
				.name("Netflix")
				.withSingleView("https://www.netflix.com");
		
//			File jsScriptFile = new File(getClass().getClassLoader().getResource("script.js").getFile());
		
//			try {
//				try {
//					mainView.addCustomScript("https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js")
//							.addCustomScript("script.js")
				mainView.addShortcut("M1+I", () -> {
							System.out.println("This is a nice global shortcut!");
						})
						.addShortcut("M1+V", () -> {
							System.out.println("Another nice global shortcut!");
						});
//				} catch (IOException | URISyntaxException e) {
//					e.printStackTrace();
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		
		MenuItemBuilder menuItemBuilder = mainView
				.withMainMenu("File")
					.addMenuItem("New Playlist").onClick(() -> {
						System.out.println("New Playlist Window...");
					})
					.addShortcut("M1+N")
					.addMenuSeparator()
					.addMenu("Import Playlist")
						.addMenuItem("iTunes");
		
		mainView.withMainMenu("View")
					.addMenuItem("Right Sidebar")
					.addMenuSeparator()
					.addMenuItem("Actual Size")
					.addMenuItem("Zoom In")
					.addMenuItem("Zoom Out")
					.addMenuSeparator()
					.addMenuItem("Go Back")
					.addMenuItem("Go Forward")
					.addMenuSeparator()
					.addMenuItem("Enter Full Screen")
					.onClick(() -> {
						System.out.println("Hello Netflix world!");
					});
		return menuItemBuilder
				.withMainMenu("Playback")
					.addMenuItem("Play")
					.addMenuSeparator()
					.addMenuItem("Next")
					.addMenuItem("Previous")
					.addMenuItem("Seek Forward")
					.addMenuItem("Seek Backward")
					.addMenuSeparator()
					.addMenuItem("Volume Up")
					.addMenuItem("Volume Down")
					.start();
	}
		
	public static void main(String[] args) {
		Equo.start(DummyEquoAppStarter.class);
	}

//	public static void main(String args[]) {
//		try {
//			EquoApplication
//			.name("Netflix")
//			.withSingleView("netflix.com")
//			.withMainMenu("File").addMenuItem("First item").onClick(new IMenuHandler() {
//				
//				@Override
//				public void execute() {
//					System.out.println("holadasdsa");
//					
//				}
//			})
//			.start();
//		} catch (Exception e) {
//			System.out.println("Unable to run the Equo app");
//			e.printStackTrace();
//		}
//	}
	
}
