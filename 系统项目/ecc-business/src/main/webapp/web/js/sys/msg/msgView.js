var PATH_PREFIX = "";
function setPathPrefix(path) {
	PATH_PREFIX = path;
}


$(function(){
	var $msgViewList = $('#msgViewList');
	$msgViewList.jqGrid({
		caption: '通知列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
		url:  PATH_PREFIX+'/listPage.do',
		datatype: "json",
		colNames: ['消息ID','stat','是否已读', '标题','content','日期','flag','是否标记'],  
		colModel: [
				  {name:'id',index:'id',hidden:true, align:'left',key:true},
				  {name:'stat',index:'stat',hidden:true, align:'left',sortable:false},
				  {name:'stat-icon',index:'stat-icon',hidden:false, align:'center',sortable:false,width:10,formatter:statFormatter},
				  {name:'title',index:'title',align:'center',sortable:false,title:false,width:60,classes:'msg-view-title-box',formatter:titleFormatter},
				  {name:'content',index:'content',hidden:true, align:'left'},
				  {name:'createDate',index:'createDate',align:'center',sortable:false,title:false,width:20,formatter:dateFormatter},
				  {name:'flag',index:'flag',hidden:true, align:'left',sortable:false},
				  {name:'flag-icon',index:'flag-icon',hidden:false, align:'center',sortable:false,width:10,formatter:flagFormatter}
				  /*{name:'option',index:'option',  resizable:true, align:'left', sortable:false,formatter:optionFormatter}*/
				  ],
		rowNum:10,        
		rowList:[10, 20,30,50],
		rownumbers : true,
		rownumWidth : 50,
		prmNames: {
			search: "search",
			page: "pageNo",     //当前页
			rows: "limit"         //每页行数
		},  
		height: 'auto',
		autowidth : true,
		width: 'auto',
		editurl : '',
		viewrecords : true,
		cellEdit : false,
		shrinkToFit : true,
		grouping : false,
		autowidth : true,
		jsonReader: {  
			root: "items",       //结果集
			records: "total", //总记录数 
			total: "pageCount",	  //总页数
			page: "pageNo",	  //当前页 
			repeatitems : false       // (4)  
		},
		pager: "#msgViewPage", 
		viewrecords: true,
		hidegrid : false,
		multiselect:false,
		multiboxonly:false,
		rownumbers:false,
		ondblClickRow : function(msgConfCode) {
		},
		onCellSelect : function(rowid,iCol,cellcontent,e){
			//console.log(e.target);
		},
		onSelectRow : function(rowid){
			/*var $msgFlag = $(".msg-flag[data-id='"+rowid+"']").show();
			$(".msg-flag.msg-flag-n").not($msgFlag).hide();*/
		},
		gridComplete: function(){
			$(".msg-view-title").popover({
				title:"内容",
				placement:"top",
				container:"body",
				delay: 0,
				animation:false,
				trigger:"focus",
				template:'<div class="popover" style="z-index: 99999" role="tooltip"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>'
			});
			/*$(".msg-flag.msg-flag-n").hide();*/
		}
		
	});

	
	$msgViewList.navGrid('#msgViewPage', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refreshstate : 'current'
	});
	$msgViewList.jqGrid('setFrozenColumns');
	jqGridResize($msgViewList);	
	
	function statFormatter(cellvalue, options, rowObject){
		var stat = rowObject.stat;
		var id = options.rowId;
		if(stat == "INIT"){
			return "<i data-id='" + id + "' class='msg-view-stat fa fa-envelope'></i>";
		}else if(stat == "VIEWED"){
			return "<i data-id='" + id + "' class='msg-view-stat fa fa-envelope-o'></i>";
		}
	}
	
	function flagFormatter(cellvalue, options, rowObject){
		var id = options.rowId;
		var flag = rowObject.flag;
		if(flag == "0"){
			return "<a href='#' data-id='" + id + "' title='点击标记' class='msg-flag msg-flag-n btn btn-link btn-jqgrid'><i class='fa fa-flag-o'></i></a>";
		}else if(flag == "1"){
			return "<a href='#' data-id='" + id + "' title='点击取消标记' class='msg-flag msg-flag-y btn btn-link btn-jqgrid'><i class='fa fa-flag'></i></a>";
		}
	}
	
	function optionFormatter(cellvalue, options, rowObject){
		var id = options.rowId;
		var btns = "";
		btns += "<a href='#' data-id='" + id + "' title='删除' class='btn btn-link btn-jqgrid'><i class='fa fa-trash-o'></i></a>";
		return btns;
	}
	
	function titleFormatter(cellvalue, options, rowObject){
		var id = options.rowId;
		var content = rowObject.content;
		var stat = rowObject.stat;
		if(stat == "INIT"){
			return "<strong style='outline:none;cursor:pointer;' data-id='" + id + "' data-content='" + content + "' tabindex='100' class='msg-view-title'>"+cellvalue+"</strong>";
		}else if(stat == "VIEWED"){
			return "<strong style='outline:none;cursor:pointer;' data-id='" + id + "' data-content='" + content + "' tabindex='100' class='msg-view-title msg-viewed'>"+cellvalue+"</strong>";
		}
	}
	
	function dateFormatter(cellvalue, options, rowObject){
		var id = options.rowId;
		var stat = rowObject.stat;
		if(stat == "INIT"){
			return "<strong data-id='" + id + "' class='msg-view-date'>"+cellvalue+"</strong>";
		}else if(stat == "VIEWED"){
			return "<strong data-id='" + id + "' class='msg-view-date msg-viewed'>"+cellvalue+"</strong>";
		}
	}
	
	$('body').on("click",'.msg-view-title',function(){
		var $this = $(this);
		var msgId = $this.attr("data-id");
		var $msgStat = $(".msg-view-stat[data-id='"+msgId+"']");
		if($msgStat.hasClass("fa-envelope")){
			$.ajax({
				type: "post",
				url: PATH_PREFIX + "view.do",
				async: "false",
				data: {msgId:msgId}
			}).done(function(data){
				if(!data){
					console.log("标记已读失败,msgId:",msgId);
				}else{
					$msgStat.toggleClass("fa-envelope fa-envelope-o");
					$this.addClass("msg-viewed");
					$(".msg-view-date[data-id='"+msgId+"']").addClass("msg-viewed");
				}
			}).fail(function(){
				console.log("标记已读失败,msgId:",msgId);
			});
		};
	});
	
	//点击标记按钮
	$('body').on("click",'.msg-flag',function(){
		var $this = $(this);
		var msgId = $this.attr("data-id");
		var flag = "0";
		if($this.hasClass("msg-flag-n")){
			flag = "1"; //打标记
		}else if($this.hasClass("msg-flag-y")){
			flag = "0"; //取消标记
		}
		$.ajax({
			type: "post",
			url: PATH_PREFIX + "flag.do",
			async: "false",
			data: {msgId:msgId,flag:flag}
		}).done(function(data){
			if(!data){
				ctools.alert_sweet("标记失败！", "error", "");
			}else{
				$this.toggleClass("msg-flag-n msg-flag-y").find("i").toggleClass("fa-flag-o fa-flag");
				var $msgStat = $(".msg-view-stat[data-id='"+msgId+"']");
				if($msgStat.hasClass("fa-envelope")){
					$msgStat.toggleClass("fa-envelope fa-envelope-o");
					$(".msg-view-title[data-id='"+msgId+"']").addClass("msg-viewed");
					$(".msg-view-date[data-id='"+msgId+"']").addClass("msg-viewed");
				};
			}
		}).fail(function(){
			ctools.alert_sweet("标记失败！", "error", "");
		});
	});
	
});

function queryByCondtion (flag)
{
	var title = $("#q-title").val();
	var theDate = $("#q-theDate").val();
	var stat = $("#q-stat").val();
	var flag  = "";
	if(stat == "1"){
		flag = stat;
		stat = "";
	}
    var postData = $("#msgViewList").jqGrid("getGridParam", "postData");
    //将filters参数串加入postData选项  
    $.extend(postData,{
    	'sp[title]':title,
    	'sp[theDate]':theDate,
    	'sp[stat]':stat,
    	'sp[flag]':flag
    });
    if (flag){
    	$("#msgViewList").trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    }
    else{
    	$("#msgViewList").trigger("reloadGrid");//重新载入Grid表格
	}
}

// 对按钮绑定全屏事件
$(document).on('click', '.btn-fullscreen', function() {
	var $wrapper = $(this).parents('.fullscreen-wrapper')[0];
	toggleFullScreen(document.documentElement);
	// 全屏的时候将几个模态框放到下面去
	$('.modal[role="dialog"]').appendTo($wrapper);
})