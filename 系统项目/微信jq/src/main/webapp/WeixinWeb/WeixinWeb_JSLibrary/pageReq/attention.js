$(document).ready(function(e) {
	var bodyHeight = $(window).height();
	$(".success").height(bodyHeight);
});
$(window).resize(function() {
	var bodyHeight = $(window).height();
	$(".success").height(bodyHeight);
})