var dialog = {
	alert: function(a) {
		this.type = a.type;
		this.title = a.title || "��ず淇℃�";
		this.content = a.content || "��ず���";
		this.confirmText = a.confirmText || "纭��";
		this.cancelText = a.cancelText || "���";
		this.confirm = a.confirm;
		this.cancel = a.cancel;
		this.init()
	},
	init: function() {
		var d = this;
		var b = this.type;
		var a = "";
		switch(b) {
			case "1":
				a = '<div class="dialog">' + '<div class="mask"></div>' + '<div class="alertPopup">' + '<h2 class="title">' + this.title + "</h2>" + '<div class="contain">' + this.content + "</div>" + '<div class="btn" id="confirm">' + this.confirmText + "</div>" + "</div>" + "</div>";
			case "2":
				a = '<div class="dialog">' + '<div class="mask"></div>' + '<div class="alertPopup single">' + '<h2 class="title">' + this.title + "</h2>" + '<div class="contain">' + this.content + "</div>" + '<div class="btn clearFloat">' + '<div id="cancel">' + this.cancelText + "</div>" + '<div id="confirm">' + this.confirmText + "</div>" + " </div>" + "</div>" + "</div>";
			case "3":
				var c = new Date().getTime();
				a = '<div class="dialog tipBox autoBox" id="dialogAutoBox">' + this.content + "</div>"
		}
		if($(".dialog").length > 0) {
			$(".dialog").remove()
		}
		$("body").append(a);
		if(b == "3") {
			setTimeout(function() {
				$("#dialogAutoBox").css("opacity", 0)
			}, 1000);
			setTimeout(function() {
				$("#dialogAutoBox").remove();
				typeof d.confirm == "function" && confirm()
			}, 1500)
		}
		$(document).on("click", "#confirm", function() {
			$(".dialog").remove();
			typeof d.confirm == "function" && d.confirm()
		});
		$(document).on("click", "#cancel", function() {
			$(".dialog").remove();
			typeof d.cancel == "function" && d.cancel()
		})
	}
};