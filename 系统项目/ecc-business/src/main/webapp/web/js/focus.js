function autoFocus(firstIndex,divId) {
	var $focusDiv;
	if(divId!=undefined && divId!=null){
		$focusDiv = $("#"+divId);
	}
	
	if($focusDiv!=undefined){
		var $inp = $(':visible:input',$focusDiv);
		/*$(':visible:input:button',$focusDiv).bind('focus', function (e) {
			 $(this).click();
		 });*/
		 if(firstIndex!=undefined && firstIndex!=null && $.isNumeric(firstIndex) && firstIndex<$inp.length){
			 $(":visible:input:eq(" + firstIndex + ")",$focusDiv).focus();
		 }
		 $inp.bind('keydown', function (e) {
			 var key = e.which;
			 if (key == 13) {
				 e.preventDefault();
				 var nxtIdx = $inp.index(this) + 1;
				 if($(":visible:input:eq(" + nxtIdx + ")",$focusDiv).attr("type")=="button"){
					 $(":visible:input:eq(" + nxtIdx + ")",$focusDiv).click();
				 }else{
					 $(":visible:input:eq(" + nxtIdx + ")",$focusDiv).focus();
				 }
				 
				  
		 	}
		 });
	}else{
		var $inp = $(':visible:input');
		/*$(':visible:input:button').bind('focus', function (e) {
			 $(this).click();
		 });*/
		 if(firstIndex!=undefined && firstIndex!=null && $.isNumeric(firstIndex) && firstIndex<$inp.length){
			 $(":visible:input:eq(" + firstIndex + ")").focus();
		 }
		 $inp.bind('keydown', function (e) {
			 var key = e.which;
			 if (key == 13) {
				 e.preventDefault();
				 var nxtIdx = $inp.index(this) + 1;
				 if($(":visible:input:eq(" + nxtIdx + ")").attr("type")=="button"){
					 $(":visible:input:eq(" + nxtIdx + ")").click();
				 }else{
					 $(":visible:input:eq(" + nxtIdx + ")").focus();
				 }
				 
				  
		 	}
		 });
	}
	
}
