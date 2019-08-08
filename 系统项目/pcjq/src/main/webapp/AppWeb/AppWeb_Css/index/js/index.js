window.onload=function(){
	var amBodyBox=document.getElementById("J_amMotion");//滚动部分对象
	var sideNavBox=document.getElementById("J_sideNav");//侧栏点点对象
	var navGroup=sideNavBox.children;
	var activeID=0;										//当前滚动激活的对象
	var activeDot=navGroup[0];							//当前激活点
	var pageHeigh=document.body.offsetHeight;			//满屏页面高度
	var isFinish=true;									//运动停止
	var isDown=true;									//往下滚动
	var aLicai=new Array();								//财产文字对象
	var aBg=new Array();								//背景滚动对象
	var aFtbar=new Array();								//一屏幕底部对象

	//初始化运行，整体流程管理
 	init();

	function init(){
		//数据初始化
		initData();
		//互动管理
		initInteractive();
		//适配管理
		changeCssMedia();
		//ie背景适配管理
		changeIeBg();
	}

	//初始化数据
	function initData(){
		//理财文字对象初始化
		for(var i=0;i<3;i++){
			var licai=document.getElementById("liCai"+i);
			licai.isShow=false;
			aLicai.push(licai);
		}

		//背景对象初始化
		for(var j=0;j<4;j++){
			var tmBg=document.getElementById("amBg"+j);
			var tmFtbar=document.getElementById("ftbar"+j);
			aBg.push(tmBg);
			aFtbar.push(tmFtbar);
		}
	}

	//交互操作
	function initInteractive(){
		// self.resizeTo(1440,900);//IE屏幕调整1920x1080;1440x900;1366x768;1024x768

		//侧栏导航点
		for(var i=0;i<navGroup.length;i++){
			var oNav=navGroup[i];
			
			oNav.onclick=function(){
				gotoPage(this.getAttribute("pageID"));
			}
		}

		//滚动条滚动
		document.body.onmousewheel=msWheel;
		if(document.body.addEventListener)
			document.body.addEventListener("DOMMouseScroll",msWheel,false)
		// document.body.onmousedown=msDown;

		// 快捷键控制
		document.onkeydown=keyDown;

		//屏幕尺寸改变
		window.onresize=resizeView;
	}

	// 滚轮控制事件
	function msWheel(ev){
		ev=ev || window.event;
		if(!isFinish)return;
		isFinish=false;

		
		var scrollID=0;

		// 方向判断
		if(ev.wheelDelta){
			isDown=ev.wheelDelta<0?true:false;
		}else{
			isDown=ev.detail>0?true:false;
		}

		activeID=parseInt(activeID);
		if(isDown){
			//向下滚动
			scrollID=(activeID+1)>=(navGroup.length-1)?navGroup.length-1:activeID+1;
		}else{
			//向上滚动
			scrollID=(activeID-1)<=0?0:activeID-1;
		}

		gotoPage(scrollID);

		// 停止事件流
		if(ev.preventDefault)
			ev.preventDefault();

		return false;
	}

	//跳转到指定页
	function gotoPage(id){
		if(activeID==id){
			isFinish=true;
			return;
		}
		activeID=id;
		//文章内容运动
		// amBodyBox.style.top=(amBodyBox.offsetTop-20)+"px";
		var motionComplete=function(){
			isFinish=true;

			var tmLicai=aLicai[activeID-1];
			if(activeID>0 && activeID<4 && !tmLicai.isShow){
				tmLicai.isShow=true;
				$(tmLicai).show();
			}
		}

		var motionStar=function(){
			if(activeID==4)return;
			if(isDown){
				TweenMax.fromTo(aBg[activeID], 0.8, {margin:0}, {margin:0});
			}else{
				TweenMax.fromTo(aBg[activeID], 0.8, {margin:0}, {margin:0});
			}
			
		}

		// 运动到指定id的page
		TweenMax.to(amBodyBox,.8,{top:-activeID*(pageHeigh-105),onComplete:motionComplete,onStart:motionStar});

		//右侧导航点定位
		activeDot.className="";
		activeDot=navGroup[activeID];
		activeDot.className="active";
		TweenMax.from(activeDot,1,{opacity:0.3});
	}

	// 屏幕尺寸改变
	function resizeView(){
		pageHeigh=document.body.offsetHeight;
		amBodyBox.style.top=(-activeID*(pageHeigh-105))+"px";

		//样式表适配
		changeCssMedia();

		//ie下适配图片
		changeIeBg();
	}

	function changeCssMedia(){
		//底部栏目样式适配
		var setBarHeight=function(h){
			for(var i=0;i<4;i++){
				aBg[i].style.bottom=h+"px";
				aFtbar[i].style.height=h+"px";
			}

			var aFtProduct=getElementsByClassName("ft-product-content");
			for(var j=0;j<aFtProduct.length;j++){
				aFtProduct[j].style.height=h+"px";
				//console.log('hh:'+aFtProduct[j].style.height)
			}
		}
		// 首屏幕底部的图片的适配
		var setMainImg=function(size,bigFontSize,smallFontSize,mainPadding){
			var aIconBox=getElementsByClassName("iconBox");
			var aContentTxt=getElementsByClassName("contentTxt");
			var J_ft_main_content=document.getElementById("J_ft_main_content");

			for(var i=0;i<aIconBox.length;i++){
				aIconBox[i].style.width=size+"px";
				aIconBox[i].style.height=size+"px";
				aIconBox[i].style.lineHeight=size+"px";
				aIconBox[i].style.fontSize=bigFontSize+"px";

				aContentTxt[i].style.fontSize=smallFontSize+"px";
			}

			J_ft_main_content.style.paddingTop=mainPadding+"px";
		}
		// 首屏幕底部的icon集的适配
		/*var setMainIcon=function(size,bigFontSize,smallFontSize,mainPadding){
			var aIconBox=getElementsByClassName("iconBox");
			var aContentTxt=getElementsByClassName("contentTxt");
			var J_ft_main_content=document.getElementById("J_ft_main_content");

			for(var i=0;i<aIconBox.length;i++){
				aIconBox[i].style.width=size+"px";
				aIconBox[i].style.height=size+"px";
				aIconBox[i].style.lineHeight=size+"px";
				aIconBox[i].style.fontSize=bigFontSize+"px";

				aContentTxt[i].style.fontSize=smallFontSize+"px";
			}

			J_ft_main_content.style.paddingTop=mainPadding+"px";
		}*/
		//顶部高度、正文内容字体、标题字体大小、标题与内容间距、li元素间距、icon大小
		var setFootInfo=function(top,allFontSize,titleFontSize,paddingBt,liMarginR,widthHeight){
			var aFtInfo=getElementsByClassName("ft-info");
			var aFtInfoTitle=getElementsByClassName("ft-info-title");
			var aFtInfoContent=getElementsByClassName("ft-info-content");
			var aCanYu=getElementsByClassName("can-yu");
			var aFtInfoLi=getElementsByClassName("ft-info-li");
			var aPdIcon=getElementsByClassName("pd-icon");

			for(var i=0;i<aFtInfo.length;i++){
				aFtInfo[i].style.top=top+"px";
				aFtInfo[i].style.fontSize=allFontSize+"px";
				aFtInfoTitle[i].style.fontSize=titleFontSize+"px";

				aFtInfoTitle[i].style.paddingBottom=aFtInfoContent[i].style.paddingBottom=aCanYu[i].style.paddingBottom=paddingBt+"px";
			}

			for(var j=0;j<aPdIcon.length;j++){
				aFtInfoLi[j].style.marginRight=liMarginR+"px";
				aPdIcon[j].style.width=aPdIcon[j].style.height=widthHeight+"px";
			}
		}

		//设置底部左右边距距离
		// aTuDis 为数组，图片的距离，0为第一张图片（手机）、1为笔记本的位置、2为电视屏幕的位置；
		// infoDis为文字部分离边缘的距离
		/*var setFoot_leftRight_dis=function(aTuDis,infoDis){
			var a_dis_ftInfo=getElementsByClassName("ft-info");
			var a_info_tu=getElementsByClassName("tu-image");

			a_dis_ftInfo[0].style.right=infoDis+"px";
			a_dis_ftInfo[1].style.left=infoDis+"px";
			a_dis_ftInfo[2].style.right=infoDis+"px";

			a_info_tu[0].style.left=aTuDis[0]+"px";
			a_info_tu[1].style.right=aTuDis[1]+"px";
			a_info_tu[2].style.left=aTuDis[2]+"px";
		}*/

		//底部栏目图的位置和宽高调节
		var setTuChuImg=function(aTu1,aTu2,aTu3){
			var jTu1=getElementsByClassName("tu1")[0];
			var jTu2=getElementsByClassName("tu2")[0];
			var jTu3=getElementsByClassName("tu3")[0];

			jTu1.style.left=aTu1[0]+"px";
			jTu1.style.width=aTu1[1]+"px";
			jTu1.style.height=aTu1[2]+"px";

			jTu2.style.right=aTu2[0]+"px";
			jTu2.style.width=aTu2[1]+"px";
			jTu2.style.height=aTu2[2]+"px";

			jTu3.style.left=aTu3[0]+"px";
			jTu3.style.width=aTu3[1]+"px";
			jTu3.style.height=aTu3[2]+"px";
		}

		// 设置理财文字的属性
		// var setLiCaiPosi=function(percent){
		// 	aLicai[0].style.backgroundPosition=aLicai[2].style.backgroundPosition=percent+"% 38%";
		// 	aLicai[1].style.backgroundPosition=(100-percent)+"% 38%";
		// }

		/*// 浏览器高度对内容的高度影像
		if(pageHeigh<729){
			setBarHeight(160);
			setMainIcon(68,16,12,24);
			setFootInfo(14,12,20,2,34,30);
			setTuChuImg([16,208,248],[-10,362,187],[-60,430,189]);
		}else if(pageHeigh>730 && pageHeigh <799){
			setBarHeight(208);
			setMainIcon(84,18,14,32);
			setFootInfo(20,14,28,4,44,42);
			setTuChuImg([16,266,317],[-50,468,242],[-60,558,244]);
		}else{
			setBarHeight(265);
			setMainIcon(110,22,14,50);
			setFootInfo(30,16,26,6,54,52);
			setTuChuImg([16,337,402],[-100,591,306],[-110,708,310]);
		}*/
		var widthWindow=window.innerWidth;
		if(widthWindow>1600&&widthWindow<=1920){
			setBarHeight(265);
			//setMainIcon(110,22,14,50);
			setFootInfo(30,16,26,6,54,52);
			setTuChuImg([16,337,402],[-100,591,306],[-110,708,310]);
		}else if(widthWindow>=1280 && widthWindow <=1600){
			setBarHeight(208);
			//setMainIcon(84,18,14,32);
			setFootInfo(20,14,28,4,44,42);
			setTuChuImg([16,266,317],[-50,468,242],[-60,558,244]);
		}else{
			setBarHeight(160);
			//setMainIcon(68,16,12,24);
			setFootInfo(14,12,20,2,34,30);
			setTuChuImg([16,208,248],[-10,362,187],[-60,430,189]);
		}
		// if(document.body.clientWidth<1015){
		// 	setLiCaiPosi(10);
		// }else if(document.body.clientWidth<1431 && document.body.clientWidth>1016){
		// 	setLiCaiPosi(23);
		// }else {
		// 	setLiCaiPosi(30);
		// }
	}

	// ie浏览器下，对大背景图的适配全屏
	function changeIeBg(){
		if(navigator.userAgent.indexOf("MSIE")==-1) return;

		var setBg=function(name){
			for(var i=0;i<4;i++){
				aBg[i].style.backgroundImage="url(/AppWeb/AppWeb_Css/index/image/bgPart_"+name+"_"+i+".jpg)";
			}
		}


		if(document.body.clientWidth<1347 && document.body.clientHeight<608){
			//1366x768 and 1024x768
			setBg("C");
		}else if(document.body.clientWidth>1347 && document.body.clientWidth<1590 && document.body.clientHeight>608 && document.body.clientHeight<740){
			//1440x900 1420 739
			setBg("B");
			// alert("BB")
		}else if(document.body.clientWidth>1590 && document.body.clientWidth<1800 && document.body.clientHeight>608 && document.body.clientHeight<740){
			//1440x900 1420 739
			setBg("D");
			// alert("BB")
		}else if(document.body.clientWidth>1800 && document.body.clientHeight>740){
			//1920x1080{
			setBg("A");
		}
	}

	// 鼠标点击导航点的互动
	function msDown(e){
		if(!isFinish)return;
		e=e || window.event;
		//mouse middle button
	   if( (e.which == 2) ) {
		   stopDefaultAndBubble(e);
		   //bugfix 搜狗浏览器的ie内核只有在定时器触发这个函数才生效。。
		   setTimeout(function(){
		   		stopDefaultAndBubble(e);
		   },10);
		}
	}
	
	//去掉鼠标中键的点击事件,否则由此产生的向上滚动和向下滚动将不受控制
	function stopDefaultAndBubble(e) {
		e = e || window.event;
		// Stops the Default Browser Action
		if (e.preventDefault) {
			e.preventDefault();
		}
		e.returnValue = false;
		
		//Stops the event bubbling up to the body element
		if (e.stopPropagation) {
			e.stopPropagation();
		}
		e.cancelBubble = true;
	}

	//键盘事件
	function keyDown(ev){
		var ev=ev || window.event;
		var keyID=0;

		if(ev && ev.keyCode == 38){
			//向上按钮
			keyID=(activeID-1)<=0?0:activeID-1;
		}else if(ev && ev.keyCode == 40){
			//向下按钮
			keyID=(activeID+1)>=(navGroup.length-1)?navGroup.length-1:activeID+1;
		}//end if

		/*gotoPage(keyID);*/
	}

	//获取class对象
	function getElementsByClassName(classname,node){ 
		node = node || window.document; 
		if(node.getElementsByClassName){ 
			return node.getElementsByClassName(classname); 
		}else{ 
			var results = new Array(); 
			var elems = node.getElementsByTagName("*"); 
			for (var i = 0; i < elems.length; i++) {
				var child = elems[i];
				var classNames = child.className.split(' ');
				for (var j = 0; j < classNames.length; j++) {
					if (classNames[j] == classname) {
						results.push(child);
						break;
					}
				}
			}
			return results; 
		} 
	}
}
