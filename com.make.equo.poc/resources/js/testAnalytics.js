$(document).ready(function () {
    
	console.log("Adding Test Button For Analytics");
	
		
	let testLink = $("<a/>", {
		role: "link",
		text: "Analytics Send Event Test"
	});
	
    
	let divWrapper = $("<div/>").css({
		"margin-top": "10px"
	});

	let testDiv = $("<div/>", {
		class: "testLink"
	});
	testDiv.append(divWrapper).append(testLink);
	
	
	let testSection = $("<section/>", {
		
	});
	
	testSection.append("<h2 class=\"section-heading\">Equo Test Javascript</h2>").append(testDiv);
	
	testDiv.click(function () {
		equo.registerEvent({
		      key: 'test_analytics',
		      segmentation: {
		        'test': 'test1'
		      }
		 });
	});
	
	const insertTestBeforeAbout = function () {
		testSection.insertAfter($( "#title" ));
	};

	insertTestBeforeAbout();
});