;(function(){
	var imgCompose=function(id,obj,callback){
	    this.container=document.getElementById(id)  //元素对象
	    this.imgUrlBg=obj.imgUrlBg||"xxxxx";  //背景图片
	    this.logoUrl=obj.logoUrl||"demoUrl";
        this.headUrl=obj.headUrl,  //用户头像
        this.nickName=obj.nickName,  //昵称
 	    this.QCcanves=document.createElement("canvas"); //创建二维码的一个canves对像
 	    this.QCcanvesWidth=obj.QCcanvesWidth||"100";
 	    this.QCcanvesHeight=obj.QCcanvesHeight||"100";
	    this.QCcanvasCtx=this.QCcanves.getContext("2d");//获取二维码2d绘画环境
	    this.QCcanvesInfo=obj.text||""
	    this.imgCanvas=document.createElement('canvas');//创建图片的一个canves对像
	    this.imgCanvasCtx=this.imgCanvas.getContext("2d");//获取图片2d绘画环境    
	    this.headerCanves=document.createElement("canvas"); //创建头像的一个canves对像
	    this.headerImgCtx=this.headerCanves.getContext("2d");
	    this.headerCanvesImg="";
	    this.load=obj.load;
	    this.headUrlCanves()
	    this.backGroundInit();
	    if(!this.load){
			msgTip.loadingAdd("生成中...");
		}
	    
        setTimeout(function(){
        	 msgTip.loadingRemove();
        },5000)
	}
	imgCompose.prototype={ 
		/*
		 * 添加图片
	     */
		backGroundInit:function(){    
			var _self=this;
			var img=new Image();
			img.src=this.imgUrlBg;
			img.onload=function(){
				_self.imgCanvas.width=img.width;
		        _self.imgCanvas.height=img.height;
				_self.imgCanvasCtx.drawImage(img,0,0); 
				 var imgSrc=_self.imgCanvas.toDataURL("image/jpg");
				//_self.container.src=imgSrc;
				_self.QcodeInit();
				//_self.circle_image_v2(_self.headUrl)
			}
		},
		headUrlCanves:function(){   //合成圆形头像
			var _self=this;
			var img5=new Image();
			try{
				img5.crossOrigin='';
				img5.src=this.headUrl;
				img5.onload=function(){
					_self.headerCanves.width=img5.width;
			        _self.headerCanves.height=img5.height;
			        
			        circle = {
				        x: img5.width / 2,
				        y: img5.width / 2,
				        r: img5.width / 2
				    };
				    _self.headerImgCtx.clearRect(0, 0, img5.width, img5.width);
				    _self.headerImgCtx.save();
				    _self.headerImgCtx.beginPath();
				    _self.headerImgCtx.arc(circle.x, circle.y, circle.r, 0, Math.PI * 2, false);
				    _self.headerImgCtx.clip();
					_self.headerImgCtx.drawImage(img5,0,0,132,132);
					var imgSrc=_self.headerCanves.toDataURL("image/jpg");
					_self.headerCanvesImg=imgSrc;
				}
			}
			catch(err) {
			    msgTip.loadingRemove();
			    msgTip.autoBox({
					  contain: "生成失败，请刷新再试"
				});

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
			var qrcode = new QRCode('qrcode', {
				  text: _self.QCcanvesInfo,
				  width: _self.QCcanvesWidth,
				  height: _self.QCcanvesHeight
			});
		    var base64 = $('#qrcode').find("canvas")[0].toDataURL('image/jpeg');
		    $('#qrcode').remove()
		    var img=new Image();
			img.src=base64;  
			img.onload=function(){  //二维码
			    _self.QCcanves.width=img.width;
		        _self.QCcanves.height=img.height;
				_self.QCcanvasCtx.drawImage(img,0,0);  //背景图片添加二维码
			    var imgSrc=_self.QCcanves.toDataURL("image/jpg");
			}
			var img2=new Image();
			img2.src=_self.logoUrl;  //logo
			
			img2.onload=function(){
				_self.QCcanvasCtx.drawImage(img2,40,40,18,18);  //二维码添加logo
			    var imgSrc=_self.QCcanves.toDataURL("image/jpg");
			    var codeSrc=imgSrc;
			    var img3=new Image();
			    img3.src=codeSrc;
			    img3.onload=function(){
			        _self.imgCanvasCtx.drawImage(img3,220,510,306,306); 
	                _self.imgCanvasCtx.fillStyle='#000';
			    	_self.imgCanvasCtx.font = '32px Arial';
			    	_self.imgCanvasCtx.fillText(_self.nickName,140,60);
			    	
			        var imgSrc=_self.imgCanvas.toDataURL("image/jpg");
                    var img4=new Image();
				    img4.src=_self.headerCanvesImg;
		         	img4.onload=function(){
		         		_self.imgCanvasCtx.drawImage(img4,22,22,100,100); 
		         		 var imgSrc=_self.imgCanvas.toDataURL("image/jpg");
		         		 msgTip.loadingRemove();
				         _self.container.src=imgSrc;
				         $("#hideHeadUrl img").attr("src",imgSrc)
				         if(!_self.load){
				         	$(".subscribe").show();
				         }
				          
		         	}
			    }
			}	
		}
	}
    window.imgCompose = imgCompose;
})();





