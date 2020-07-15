$(document).ready(function () {
    
    console.log("Adding test button for Monaco Editor");
    
    const monacoUrl = `http://equomonaco/`;


	let testLink = $("<a/>", {
		role: "link",
		text: "Open Monaco Browser"
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
        equo.openBrowser({
            url: monacoUrl,
            name: 'Monaco Editor',
            position: 'bottom'
        });
    });
	
	const insertTestBeforeAbout = function () {
		testSection.insertBefore($( "#about" ));
	};

	insertTestBeforeAbout();
});