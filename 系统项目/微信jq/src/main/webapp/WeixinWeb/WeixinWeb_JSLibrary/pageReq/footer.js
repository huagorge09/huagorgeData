function footerLink(url, i) {
	$("#menu_f1,#menu_f2,#menu_f3").removeClass("act");
	$("#menu_f" + i).addClass("act");
	redirectUrl(url);
}