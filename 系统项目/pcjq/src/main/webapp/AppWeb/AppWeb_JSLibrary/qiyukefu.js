	$.ajax({
		url : "/AppService/kefu/queryQiyuURL.xhtml",
		type : "get",
		async : true,
		cache : false,
		success : function(qiyuUrl) {
			
			console.log(qiyuUrl);
			(function (w, d, n, a, j) {
				w[n] = w[n] || function () {
					(w[n].a = w[n].a || []).push(arguments);
				};
				j = d.createElement('script');
				j.async = true;
				j.src = qiyuUrl;
				d.body.appendChild(j);
			})(window, document, 'ysf');
		},
		error : function(data) {
			toastr.error("加载网易七鱼系统异常");
		}
	});
	