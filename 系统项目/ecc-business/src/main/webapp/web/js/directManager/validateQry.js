/**
 * 
 */
var PRIMARY_PATH = "";
var BASE_PATH = "";
var custInfoList = $('#custInfoList');

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

$(function(){
	initGrid();
	initWidget();
	$("#operatorType").change();
});

var validFunc = {
		validateUpdateView : function(idtp,idno){
			var operatorType = $("#operatorType").val();
			var begindate = $("#begindate").val().replace(/-/ig,"");
			var enddate = $("#enddate").val().replace(/-/ig,"");
			var param = "?idtp="+idtp+"&idno="+idno+"&operatorType="+operatorType+"&begindate="+begindate+"&enddate="+enddate;
			openDialog(PRIMARY_PATH+"/validateUpdateView.do"+param);
		}
};

function initGrid(){
	custInfoList.jqGrid({
		url: PRIMARY_PATH+'/queryValidateList.do',
        caption: '证件有效期查询列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
        datatype: "json",
        postData :queryPostData(),
        colNames: ["客户号","证件持有人姓名","客户类别id","客户类别","证件类型id","证件类型","证件号码","有效期","电话号码","手机号码","传真","邮件","详细地址","操作"],
        colModel: [
            { name: 'custno', index: 'custno',  hidden: true, key: false, sortable: false},
            { name: 'idnm', index: 'idnm', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'invtp', index: 'invtp',  hidden: true, key: false, sortable: false},
            { name: 'invtpName', index: 'invtpName', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'idtp', index: 'idtp',  hidden: true, key: false, sortable: false},
            { name: 'idtpName', index: 'idtpName', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'idno', index: 'idno', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'idvalidate', index: 'idvalidate', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'tel', index: 'tel', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'mobile', index: 'mobile', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'fax', index: 'fax', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'email', index: 'email', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'addr', index: 'addr', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'option', index:'option',width:100, align:'left', resizable:true, resizable: true, sortable: false }
        ],
        rowNum: 20,
        rowList: [10, 20, 30, 50],
        rownumbers: true,
        rownumWidth: 50,
        prmNames: {search:"search",page:"pageNo",rows:"limit"},
        height: 'auto',
        width: false,
        autowidth:true,
        shrinkToFit: true,
        editurl: '',
        viewrecords: true,
        cellEdit: false,
        grouping: false,
        jsonReader: {
            root: "items", //结果集
            records: "total", //总记录数 
            total: "pageCount", //总页数
            page: "pageNo", //当前页 
            repeatitems: false // (4) 
        },
        multiselect: false,
        pager: "#custInfoPage",
        viewrecords: true,
        hidegrid: false,
		subGrid: false,
		gridComplete: function() {
			var ids = custInfoList.jqGrid('getDataIDs');
			for(var i=0;i < ids.length;i++){
				var id = ids[i];
				var rowData = custInfoList.jqGrid('getRowData', id);
				var custno = rowData.custno;
				var idtp = rowData.idtp;
				var idno = rowData.idno;
				//按钮置灰
				$(".permissionBtn a").attr("disabled", "disabled");
                $(".permissionBtn a").removeAttr("onclick");
                //初始化修改按钮
                $("#validateUpdateBtn a").removeAttr("disabled");
            	$("#validateUpdateBtn a").attr("onClick", "validFunc.validateUpdateView('"+idtp+"','"+idno+"');");
            	
            	var vub = $("#validateUpdateBtn").html();
            	
            	custInfoList.jqGrid('setRowData',ids[i],{option:vub});
			}
		}
    });

	custInfoList.navGrid('#custInfoPage', { edit: false, add: false, del: false, search: false, refreshstate: 'current' });
	custInfoList.jqGrid('setFrozenColumns');
    jqGridResize(custInfoList);
}

function operatorTypeChange(evenEle){
	var value =evenEle.value;
	$("#invtp").attr("disabled",false);
	if("2" == value){
		$("#invtp").val("");
		$("#invtp").attr("disabled",true);
	}
	var invtp = $("#invtp").val();
	//加载证件类型
	if("1" == value || "2" == value|| "5" == value){
		//按照个人证件类型 加载
		loadIdtp("1");
	}
	//查询 客户类型  按选择客户类别 加载 
	if("0" == value){
		if("0" == invtp){
			//加载机构
			loadIdtp("0");
		}else{
			//加载个人
			loadIdtp("1");
		}
	}
}

function loadIdtp(invtp){
	var tar = $("#idtp");
	var param = "{\"pmst\":\"SYSTEM\",\"pmky\":\"IDTP\",\"pmv1\":\"1\"}";
	if("0" == invtp){
		param = "{\"pmst\":\"SYSTEM\",\"pmky\":\"IDTP\",\"pmv1\":\"0\"}";
		tar.attr("param",param);
	}else{
		tar.attr("param",param);
	}
	WASP_WIDGET.triggerParamListSelect("idtp",false,"证件类型");
}

function initWidget(){
	$("#operatorType").select2({placeholder: "查询类别"});
	WASP_WIDGET.triggerParamListSelect("invtp",false,"证件类别");
	WASP_WIDGET.triggerParamListSelect("idtp",false,"证件类别");
	WASP_WIDGET.triggerDateStyleWithYMD("begindate");
	WASP_WIDGET.triggerDateStyleWithYMD("enddate");
	WASP_WIDGET.triggerICheck("#defaultEndDat",defaultEndDatChecked,defaultEndDatUnChecked,true);
	//注册清空事件
//    WASP_WIDGET.registerResetClearEvent();
    $("#resetBtn").bind("click",function(){
    	resetBtnClick();
    });
}

function queryByCondtion(flag){
    var postData = custInfoList.jqGrid("getGridParam", "postData");
    var data = queryPostData();
    if(!data.sp.begindate){
    	ctools.alert("请输入开始日期","","warning");
    	return ;
    }
    if(!data.sp.enddate){
    	ctools.alert("请输入结束日期","","warning");
    	return ;
    }
    
    $.extend(postData,data);
    if (flag) {
    	custInfoList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    } else {
    	custInfoList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
    }
}

function queryPostData(){
	var postData={};
	postData.sp={};
	postData.sp.operatorType = $("#operatorType").val();
	postData.sp.tradeacco = $("input[name=tradeacco]").val();
	postData.sp.fundacco = $("input[name=fundacco]").val();
	postData.sp.invtp = $("#invtp").val();
	postData.sp.idno = $("input[name=idno]").val();
	postData.sp.idnm = $("input[name=idnm]").val();
	postData.sp.idtp = $("#idtp").val();
	postData.sp.begindate = $("#begindate").val().replace(/-/ig,"");
	postData.sp.enddate = $("#enddate").val().replace(/-/ig,"");
	return postData;
}

//对按钮绑定全屏事件
$(document).on('click', '.btn-fullscreen', function(){
    var $wrapper = $(this).parents('.fullscreen-wrapper')[0];
    toggleFullScreen(document.documentElement);
    //全屏的时候将几个模态框放到下面去
    $('.modal[role="dialog"]').appendTo($wrapper);
});

function defaultEndDatChecked(){
	$("input[name=enddate]").val("2099-12-31");
	$("input[name=enddate]").attr("disabled",true);
}

function defaultEndDatUnChecked(){
	$("input[name=enddate]").val($("input[name=hidEnddate]").val());
	$("input[name=enddate]").attr("disabled",false);
}

function resetBtnClick(){
	$("input[name=tradeacco]").val(null);
	$("input[name=fundacco]").val(null);
	var invtp = $("#invtp");invtp.val(null);invtp.change();
	$("input[name=idno]").val(null);
	$("input[name=idnm]").val(null);
	$("#idtp").val(null);
	$("#idtp").select2();
	$("#operatorType").val("0");
	$("#operatorType").select2({placeholder: "查询类别"});
	$("#operatorType").change();
	$('#defaultEndDat').iCheck('uncheck');
	$("input[name=begindate]").val($("input[name=hidBegindate]").val());
	$("input[name=enddate]").val($("input[name=hidEnddate]").val());
}