$(document).ready(function(e) {
	var bodyHeight = $(window).height();
	$(".success").height(bodyHeight);
	$("#ticket1").html(getUrlParameter("ticket1"));
	$("#ticket2").html(getUrlParameter("ticket2"));
});
$(window).resize(function() {
	var bodyHeight = $(window).height();
	$(".success").height(bodyHeight);
})