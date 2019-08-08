;(function(){
	var imgCompose=function(id,obj,callback){
	    this.container=document.getElementById(id)  //元素对象
	    this.imgUrl=obj.imgUrl||"xxxxx";  //背景图片
	    this.name=obj.name||"demo名字";
	    this.Position=obj.Position||"demo职位"; 
	    this.phone=obj.phone||"demo电话号码";
	    this.logoUrl=obj.logoUrl||"demoUrl";
	    this.lifeMotto=obj.lifeMotto||"demo人生格言";
 	    this.QCcanves=document.createElement("canvas"); //创建二维码的一个canves对像
 	    this.QCcanvesWidth=obj.QCcanvesWidth||"100";
 	    this.QCcanvesHeight=obj.QCcanvesHeight||"100";
	    this.QCcanvasCtx=this.QCcanves.getContext("2d");//获取二维码2d绘画环境
	    this.QCcanvesInfo=obj.QCcanvesInfo||"balabala"
	    this.imgCanvas=document.createElement('canvas');//创建图片的一个canves对像
	    this.imgCanvasCtx=this.imgCanvas.getContext("2d");//获取图片2d绘画环境    
	    this.backGroundInit();
	    console.log(new Date())
	}
	imgCompose.prototype={ 
		/*
		 * 添加图片
	     */
		backGroundInit:function(){    
			var _self=this;
			var img=new Image();
			img.src=this.imgUrl;
			img.onload=function(){
				_self.imgCanvas.width=img.width;
		        _self.imgCanvas.height=img.height;
				_self.imgCanvasCtx.drawImage(img,0,0); 
				 var imgSrc=_self.imgCanvas.toDataURL("image/jpg");
				//_self.container.src=imgSrc;
				_self.QcodeInit()
			}
		},
		/**
		 * 二维码合成
		 */
		QcodeInit:function(){  
			var _self=this;
			var qrcode=document.createElement("div");
			qrcode.id="qrcode";
			$("body").append(qrcode);
			$("#qrcode").hide();
			console.log(_self.logoUrl)	
			var qrcode = new QRCode('qrcode', {
				  text: _self.QCcanvesInfo,
				  width: _self.QCcanvesWidth,
				  height: _self.QCcanvesHeight
			});
		    var base64 = $('#qrcode').find("canvas")[0].toDataURL('image/jpeg');
		    var img=new Image();
			img.src=base64;
			img.onload=function(){
			    _self.QCcanves.width=img.width;
		        _self.QCcanves.height=img.height;
				_self.QCcanvasCtx.drawImage(img,0,0); 
			    var imgSrc=_self.QCcanves.toDataURL("image/jpg");
			  
			}
			var img2=new Image();
			img2.src=_self.logoUrl;
		    //var codeSrc="";
			img2.onload=function(){
				_self.QCcanvasCtx.drawImage(img2,35,35,30,30); 
			    var imgSrc=_self.QCcanves.toDataURL("image/jpg");
			    var  codeSrc=imgSrc;
			    var img3=new Image();
			    img3.src=codeSrc;
			    img3.onload=function(){
			    	_self.imgCanvasCtx.drawImage(img3,50,1700,350,350); 
			    	_self.imgCanvasCtx.fillStyle='#f70606';
			    	_self.imgCanvasCtx.font = '40px Arial';
			    	_self.imgCanvasCtx.fillText(_self.name,420,1738);
			    	_self.imgCanvasCtx.fillText(_self.Position,420,1830);
			    	_self.imgCanvasCtx.fillText(_self.phone,420,1900); 
			    	_self.imgCanvasCtx.fillText(_self.lifeMotto,420,1990); 
			         var imgSrc=_self.imgCanvas.toDataURL("image/jpg");
				    _self.container.src=imgSrc;
				      console.log(new Date())
			    }
			}
			
		   
		}
	}
    window.imgCompose = imgCompose;
})();





