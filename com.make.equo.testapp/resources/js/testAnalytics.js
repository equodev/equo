$(document).ready(function () {
    
	let imdbImage = $("<span/>");
	imdbImage.css({
		"max-width": "80px",
		"height": "auto"
	});
	imdbImage.append("<img id=\"imdb_img\" src=\"equo/images/IMDB_logo_2016.png\" style=\"max-width:60px;height:auto;\" alt=\"IMDB\" />")

	
	let testLink = $("<a/>", {
		role: "link",
		class: "nf-icon-button"
	});
	testLink.append(imdbImage);

    
	let divWrapper = $("<div/>").css({
		"margin-top": "10px"
	});

	let testDiv = $("<div/>", {
		class: "testLink"
	});
	testDiv.append(divWrapper).append(testLink);
	
	testDiv.click(function () {
		equo.registerEvent({
		      key: 'test_analytics',
		      segmentation: {
		        'test': 'test1'
		      }
		 });
	});
});