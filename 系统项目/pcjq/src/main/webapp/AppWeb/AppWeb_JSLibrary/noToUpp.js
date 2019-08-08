function toUpperCase(_val) {
	_val = Transverter.toStr(_val);
	var low	;
	var i, k, j, l_xx1;
	var cap = "", xx1 = "", unit;
	var digits = "零壹贰叁肆伍陆柒捌玖";
	var units = "分角元拾佰仟万拾佰仟亿拾佰仟";
	if (!_val.match(/^(\d{1,}(|(\.{1}\d{0,})))$|^(\.{1}\d{1,})$/)) {
		return "金额无效";
	}
	low = parseFloat(_val);
	if (low == 0 || low >= 1) {
		xx1 = Transverter.toNumber(Formater.decimal(low, 2, true));
		xx1 = Math.round(xx1 * 100.0) + "";
	} else if (low > 0 && low < 1) {
		xx1 = Formater.decimal(low, 2, true);
	}
	l_xx1 = xx1.length;
	if (Transverter.toNumber(_val) >= 2100000000) {
		return "金额过大";
	}
	for (i = 0; i < l_xx1; i++) {
		j = l_xx1 -1 - i;
		unit = units.substr(j, 1);
		k = parseInt(xx1.substr(i, 1));
		var digit = digits.substr(k, 1);
		cap = cap + digit + unit;
	}
	cap = cap.replace( /零分|零角|零拾|零佰|零仟/g, "零");
	cap = cap.replace( /零+/g, "零");
	cap = cap.replace( /零亿/g, "亿");
	cap = cap.replace( /零万/g, "万");
	cap = cap.replace( /零元/g, "元");
	cap = cap.replace( /亿万/g, "亿");
	cap = cap.replace( /^壹拾/, "拾");
	cap = cap.replace( /零$/, "整");
	if (cap == "整") cap = "零";
// 	var fontSize = (cap.length >= 15 ? 12 : 14);
// 	cap = ("<font color=\"#FF6900\"><b style=\"font-size:" + fontSize + "px;\">" + cap + "</b></font>");
	return cap;
}
function capMoneyNoCheck(xxje,amtNM) {

	var low	;				
	var i,k,j, l_xx1; 			
	var cap = "", dxnr, xx1, unit, lastDigit = "", endUnit ="", lastUnit = "" ; 	
	
	var digits = "零壹贰叁肆伍陆柒捌玖"; 
	var units = "分角元拾佰仟万拾佰仟亿拾佰仟"; 
		
	if(xxje == ""){
		return "";
	}
	if ( !xxje.match(/^(\d{1,}(|(\.{1}\d{0,2})))$|^(\.{1}\d{1,2})$/) )
	{
		return "金额无效"; 
	} else if(xxje.length>0 && xxje.substr(0,1) == 0){		//第一位为0
		return "金额无效"; 
	}
	low = parseFloat(xxje);	
	//if (isNaN(low)) return "输入无效"; 

	xx1 = Math.round(low * 100.0) + "" ;
	l_xx1 = xx1.length; 
	
	for (i=0; i<l_xx1; i++) { 
		j = l_xx1 -1 - i; 
		unit = units.substr(j, 1); 			
		k = parseInt(xx1.substr(i, 1)); 
		digit = digits.substr(k, 1);			
		cap = cap + digit + unit;
	}	
	
	cap = cap.replace( /零分|零角|零拾|零佰|零仟/g, "零");
	cap = cap.replace( /零+/g, "零");
	cap = cap.replace( /零亿/g, "亿");
	cap = cap.replace( /零万/g, "万");
	cap = cap.replace( /零元/g, "元");
	cap = cap.replace( /亿万/g, "亿");
	cap = cap.replace( /^壹拾/, "拾");
	cap = cap.replace( /零$/, "整");
	
	if (cap == "整") cap = "零";
	
// 	cap="<font color=red>"+ cap +"</font>";
	return cap; 
}
function capMoneyNegative(xxje,amtNM) {

	var low	;				
	var i,k,j, l_xx1; 			
	var cap = "", dxnr, xx1, unit, lastDigit = "", endUnit ="", lastUnit = "" ; 	
	
	var digits = "零壹贰叁肆伍陆柒捌玖"; 
	var units = "分角元拾佰仟万拾佰仟亿拾佰仟"; 
		
	if(xxje == ""){
		return "";
	}
	if ( !xxje.match(/^(-)?(\d{1,}(|(\.{1}\d{0,2})))$|^(\.{1}\d{1,2})$/) )
	{
		return "金额无效"; 
	} else if(xxje.length>0 && xxje.substr(0,1) == 0){		//第一位为0
		return "金额无效"; 
	}
	low = parseFloat(xxje);	

	var head = low<0 ? "负" : "";

	xx1 = Math.round(low * 100.0) + "" ;
	l_xx1 = xx1.length; 
	
	for (i=0; i<l_xx1; i++) { 
		j = l_xx1 -1 - i; 
		unit = units.substr(j, 1); 			
		k = parseInt(xx1.substr(i, 1)); 
		digit = digits.substr(k, 1);			
		cap = cap + digit + unit;
	}	
	
	cap = cap.replace( /零分|零角|零拾|零佰|零仟/g, "零");
	cap = cap.replace( /零+/g, "零");
	cap = cap.replace( /零亿/g, "亿");
	cap = cap.replace( /零万/g, "万");
	cap = cap.replace( /零元/g, "元");
	cap = cap.replace( /亿万/g, "亿");
	cap = cap.replace( /^壹拾/, "拾");
	cap = cap.replace( /零$/, "整");
	
	if (cap == "整") cap = "零";

	cap = head + cap;
	
// 	cap="<font color=red>"+ cap +"</font>";
	return cap; 
}