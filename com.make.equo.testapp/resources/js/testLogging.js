$(document).ready(function () {
    
	console.log("Adding Test Button For Loggings");
	
		
	let testLink = $("<a/>", {
		role: "link",
		text: "Logging Send Event Test"
	});
	
    
	let divWrapper = $("<div/>").css({
		"margin-top": "10px",
		"color" : "blue"
	});

	let testDiv = $("<div/>", {
		class: "testLink"
	});
	testDiv.append(divWrapper).append(testLink);
	
	
	let testSection = $("<section/>", {
		
	});
	
	testSection.append(testDiv);
	

	testDiv.click(function () {

		equo.send("ActionHandlerExample",{"name": "John","address": "FakeStreet 123" });


		equo.send("ActionHandlerExampleTwo");

		equo.logInfo("this is a info log.");

		equo.logWarn("this is a warning log.");

		equo.logError("this is a error log.");

		equo.logInfo({
			message: "this is a info log.",
			segmentation : {}
		});

		equo.logWarn({
			message: "this is a warning log.",
			segmentation : {}
		});

		equo.logError({
			message: "this is a error log.",
			segmentation : {}
		});
	});
	
	const insertTestBeforeAbout = function () {
		testSection.insertBefore($( "#about" ));
	};

	insertTestBeforeAbout();
});