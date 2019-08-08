var common = {
        addCookie: function(name,value,path, expiresHours){
            var cookieString= name+"="+escape(value);
            //判断是否设置过期时间
            if(expiresHours>0){
                var date=new Date();
                date.setTime(date.getTime()+expiresHours*3600*1000);
                cookieString=cookieString+"; expires="+date.toGMTString();
            }

            cookieString +=  "; path=" +( path ? path : "/");
            document.cookie=cookieString;
        },
        getCookie: function(name){
            var strCookie=document.cookie;
            var arrCookie=strCookie.split("; ");
            for(var i=0;i<arrCookie.length;i++){
                var arr=arrCookie[i].split("=");
                if(arr[0]==name)return arr[1];
            }
            return "";
        },
        deleteCookie: function(name){
            var date=new Date();
            date.setTime(date.getTime()-10000);
            document.cookie=name+"=v; expires="+date.toGMTString();
        },

        /**
         * 解析url
         */
        parseURL : function(url) {
            var a =  document.createElement('a');
            a.href = url;
            return {
                source: url,
                protocol: a.protocol.replace(':',''),
                host: a.hostname,
                port: a.port,
                query: a.search,
                params: function(){
                    var ret = {},
                        seg = a.search.replace(/^\?/,'').split('&'),
                        len = seg.length, i = 0, s;
                    for (;i<len;i++) {
                        if (!seg[i]) { continue; }
                        s = seg[i].split('=');
                        ret[s[0]] = s[1];
                    }
                    return ret;
                }
            }
        }
 }       