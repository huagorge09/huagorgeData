/**
 * 
 */
var PRIMARY_PATH = "";
var BASE_PATH = "";
var BACKLOG_PATH = "";
var CONTACTS_PATH ="";
function setPath(path,basePath,backlogPath,conPath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
	BACKLOG_PATH = backlogPath;
	CONTACTS_PATH = conPath;
}

var visAddFuns = {
		saveVisitHistoryInfo : function(){
			//提交信息
			//校验表单
			if($("#visAddForm").valid()){
				//提交表单
				//合成日期
				var start = $("input[name=visDateStart]").val();
				var end = $("input[name=visDateEnd]").val();
				$("input[name=visDate]").val(start + "-" + end);
				
				SubmitAndPreventSecond("visAddForm",true);
			}else{
				$("label[class=error]:eq(0)").focus();
				ctools.alert("请完善信息","","warning");
			}
		},
		backlogAddView :function(method){
			var visId = $("input[name=visId]:eq(0)").val();
			var actionUrl =  BACKLOG_PATH + "/todoInfoAddView.do?method="+method+"&visId="+visId;
			openDialog(actionUrl);
		},
		todoListDelFunc: function(todoId){
			var actionUrl = backlogPath+"/delTodoListByTodoId.do";
			ctools.confirm("您确认要删除该数据吗？",function(){
				$.ajax({
					url : actionUrl,
					type :	"post",
					data : {
						todoId : todoId
					},
					dataType : "json",
					success : function(data) {
						if("1" == data.returnCode ){
							ctools.alert_sweet('删除成功！', "success", "");
						}else if("-1" == data.returnCode){
							ctools.alert_sweet('程序异常，请联系管理员！', "success", "");
						}
						queryByCondtion(true);
					}
				});
			});
		},
		todoListUpdateFunc : function(method,todoId){
			var visId = $("input[name=visId]:eq(0)").val();
			var actionUrl =  backlogPath + "/todoInfoAddView.do?method="+method+"&todoId="+todoId+"&visId="+visId;
			openDialog(actionUrl);
		},
		instChangeResetContactsFun : function(ele){
			var visContacts = document.getElementById("visContacts");
			visContacts.options.length = 0;
			$(visContacts).trigger('change.select2');
			WASP_WIDGET.triggerInstContactsByAuthSelect("visContacts","instId",true,"联系人");
			/*var actionUrl = PRIMARY_PATH+"/instChangeResetContacts.do";
			$.ajax({
				url : actionUrl,
				type :	"post",
				data : {
					instId : ele.value
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if("1" == data.returnCode ){
						visContacts.options.length = 0;
						$(visContacts).trigger('change.select2');
						WASP_WIDGET.initializeSelectVal("visContacts",data.returnConIds,data.returnConNames);
						WASP_WIDGET.triggerContactsByAuthSelect("visContacts",true,"联系人");
						
					}else if("-1" == data.returnCode){
						ctools.alert('程序异常，请联系管理员！',"","error");
					}
				}
			});*/
		},
		openContactsAddPageFunc : function(){
			var instId = $("#instId").val();
			instId = !instId ?"":instId;
			var actionUrl =  CONTACTS_PATH + "/contactsInfoAddView.do?method=add"+"&instId=" + instId;
			openDialog(actionUrl);
		},
		contactAddCallback : function(conId,conName,backInstId){
			var instId = $("#instId").val();
			var instIds = backInstId.split(",");
			if($.inArray(instId,instIds) >= 0){
				var visConIds = new Array;
				var visConNames = new Array;
				
				var visContacts = $("#visContacts");
				var visContactChildren = visContacts.children();
				
				visConIds.push(conId);
				visConNames.push(conName);
				for (var i = 0; i < visContactChildren.length; i++) {
					visConIds.push(visContactChildren[i].value);
					visConNames.push(visContactChildren[i].innerText);
				}
				WASP_WIDGET.initializeSelectVal("visContacts",visConIds.join(","),visConNames.join(","));
			}
		}
};

var $backlogList = $("#backlogList");

$(document).ready(function(){
	var method = $("input[name=method]:eq(0)").val();
	
	WASP_WIDGET.initializeSelectVal("instId");
	WASP_WIDGET.triggerInstitutionSelect("instId",false,"机构简称");
	
	var visEmpId = $("input[name=visEmpId]:eq(0)").val();
	var visEmpName = $("input[name=visEmpName]:eq(0)").val();
	var hiVisContacts = $("input[name=hiVisContacts]:eq(0)").val();
	var hiVisContactsName = $("input[name=hiVisContactsName]:eq(0)").val();
	
	var hiVisMeetId = $("input[name=hiVisMeetId]:eq(0)").val();
	var hiVisMeetName = $("input[name=hiVisMeetName]:eq(0)").val();
	
	//lil
	var hiVisCopyToPeople = $("input[name=hiVisCopyToPeople]:eq(0)").val();
	var hiVisCopyToPeopleName = $("input[name=hiVisCopyToPeopleName]:eq(0)").val();

	$('#visMeetId').select2();
    WASP_WIDGET.triggerEmployeeSelect("visMeetId",true);
	
    //leo
    $('#visCopyToPeople').select2();
    WASP_WIDGET.triggerEmployeeSelect("visCopyToPeople",true);
    
//	WASP_WIDGET.initializeSelectVal("visContacts",hiVisContacts,hiVisContactsName);
//	WASP_WIDGET.triggerContactsSelect("visContacts","instId",true,"联系人");
	
	WASP_WIDGET.initializeSelectVal("visContacts",hiVisContacts,hiVisContactsName);
	WASP_WIDGET.initializeSelectVal("visMeetId",hiVisMeetId,hiVisMeetName);
	
	//leo
	WASP_WIDGET.initializeSelectVal("visCopyToPeople",hiVisCopyToPeople,hiVisCopyToPeopleName);
	/*WASP_WIDGET.triggerDateStyleWithYMD("visDateStart");
	WASP_WIDGET.triggerDateStyleWithYMD("visDateEnd");*/
	
	WASP_WIDGET.triggerDateStyleWithYMDAndSlash("visDateStart");
	WASP_WIDGET.triggerDateStyleWithYMDAndSlash("visDateEnd");
	
	var method = $("input[name=method]:eq(0)").val();
	if(method == "add"){
		WASP_WIDGET.initializeSelectVal("visMeetId",visEmpId,visEmpName);
	}else{
		getVisConTel();
	}
	
	$backlogList.jqGrid({
        url: BACKLOG_PATH+'/todoListPage.do',
        caption: '待办事项列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
        datatype: "json",
        postData : {
        	'sp[visId]' : $("input[name=visId]:eq(0)").val() 
        },
        colNames: ["id","接收人员id","接收人员","待办内容","截止日期","是否完成","操作"],
        colModel: [
            { name: 'todoId', index: 'todoId', width: 50, align:'left', resizable:true, hidden: true, key: true, sortable: false },
            { name: 'todoReceiveId', index: 'todoReceiveId', width: 100, align:'left', resizable:true, hidden: true, key: false, sortable: false },
            { name: 'todoReceiveName', index: 'todoReceiveName', width: 150, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'todoContent', index: 'todoContent', width: 300, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'todoEndDate', index: 'todoEndDate', width: 150, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'isComplete', index: 'todoEndDate', width: 100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'option', index: 'option', width: 100, align:'left', resizable:true, resizable: true, sortable: false }
        ],
        rowNum: 20,
        rowList: [20, 30, 50],
        rownumbers: true,
        rownumWidth: 70,
        prmNames: {
        	        search: "search", 
        	        page: "pageNo",
        	        rows: "limit" 
        	       },
        height: '200',
        width: 'auto',
        autowidth:true,
//        shrinkToFit:false,
        editurl: '',
        viewrecords: true,
        autoScroll: true,
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
        pager: "#backlogListPage",
        hidegrid: false,
		subGrid: false,
		gridComplete: function() {
			var ids = $backlogList.jqGrid('getDataIDs');
			for(var i=0;i < ids.length;i++){
				var id = ids[i];					 						    
				var rowData = $backlogList.jqGrid('getRowData', id);	
				var todoId = rowData.todoId;
				var completeName = "否";
				//按钮置灰
				$(".permissionBtn a").attr("disabled", "disabled");
                $(".permissionBtn a").removeAttr("onclick");
                
				//初始化 待办 修改 删除 
                
                $("#todoListUpdateBtn a").removeAttr("disabled");
            	$("#todoListUpdateBtn a").attr("onClick", "visAddFuns.todoListUpdateFunc('update','"+todoId+"');");
            	
            	$("#todoListDelBtn a").removeAttr("disabled");
            	$("#todoListDelBtn a").attr("onClick", "visAddFuns.todoListDelFunc('"+todoId+"');");
            	
            	var tlu = "";
            	var tld = "";
            	
            	if("Y" == rowData.isComplete){
            		completeName = "是";
            	}
            	if( method!="detail" ){
            		tlu = $("#todoListUpdateBtn").html();
                	tld = $("#todoListDelBtn").html();
            	}
            	$backlogList.jqGrid('setRowData',ids[i],{option: tlu+tld,isComplete:completeName });
			}
		}
    });

    $backlogList.navGrid('#backlogListPage', { edit: false, add: false, del: false, search: false, refreshstate: 'current' });
    $backlogList.jqGrid('setFrozenColumns');
    jqGridResize($backlogList);
    
    if("detail"==method){
    	WASP_WIDGET.triggerContactsSelect("visContacts",true,"联系人");
    	disableFormInputs();
    	//显示修改时间信息
    	$("#timeInfo").show();
    }else{
    	WASP_WIDGET.triggerInstContactsByAuthSelect("visContacts","instId",true,"联系人");
    }
    
    triggerValidOnSelectChange("instId","visContacts","visDate","visMeetId");
    triggerValidOnSelectChange("instId","visContacts","visDate","visCopyToPeople");
    validateAddFormInput();
});

function queryByCondtion(flag){
	var visId = $("input[name=visId]:eq(0)").val();
	
    var postData = $backlogList.jqGrid("getGridParam", "postData");
    $.extend(postData,{
    	'sp[visId]': visId,
    });

    if (flag) {
    	$backlogList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    } else {
    	$backlogList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
    }
}


function validateAddFormInput(){
	$("#visAddForm").validate({
		focusInvalid:true,
		focusCleanup: false,
		rules : {
			instId : {
				required : true
			},
			visContacts : {
				required : true
			},
			visDateStart : {
				required : true
			},
			visDateEnd : {
				required : true
			},
			visAddress : {
				required : true,
				maxlength4Byte : 100
			},
			visMeetId : {
				required : true
			}
		}
	});
}

//将表单元素禁用
function disableFormInputs(){
	/*
	var $parent = $("#visAddForm").find(".form-inner");
	var $select = $parent.find('.use-select2').val(null);disabled="disabled"
	$parent.find('textarea').attr("readonly","readonly");
	$parent.find('select').attr("disabled","disabled").attr("readonly","readonly");*/
	$('#instId').select2("destroy");
	$('#instId').removeClass('select2');
	$("#instId").attr("readonly","readonly").attr("disabled","disabled");
	
	$('#visContacts').select2("destroy");
//	$("#visContacts").removeAttr("multiple");
	$('#visContacts').removeClass('select2');
	$("#visContacts").attr("readonly","readonly").attr("disabled","disabled");
	WASP_WIDGET.triggerContactsSelect("visContacts",true,"");
	
	$('#visMeetId').select2("destroy");
	$('#visMeetId').removeClass('select2');
	$("#visMeetId").attr("readonly","readonly").attr("disabled","disabled");
	WASP_WIDGET.triggerContactsSelect("visMeetId",true,"");
	
	//leo
	$('#visCopyToPeople').select2("destroy");
	$('#visCopyToPeople').removeClass('select2');
	$("#visCopyToPeople").attr("disabled","disabled").attr("readonly","readonly");
	WASP_WIDGET.triggerContactsSelect("visCopyToPeople",true,"");
	
//	$('#visContacts').select2("destroy");
//	$("#visContacts").attr("disabled","disabled").attr("readonly","readonly");
	$("#visDateStart").attr("disabled","disabled").attr("readonly","readonly");
	$("#visDateEnd").attr("disabled","disabled").attr("readonly","readonly");
	
	$("input[name=visAddress]:eq(0)").attr("disabled","disabled").attr("readonly","readonly");
	$("textarea[name=visMeetExplain]:eq(0)").attr("disabled","disabled").attr("readonly","readonly");
	$("textarea[name=visMeetingFeedback]:eq(0)").attr("disabled","disabled").attr("readonly","readonly");
	
	$("#visConAddBtn").remove();
	$("#addBtn").remove();
	$("#saveBtn").remove();
	$("#cancelBtn").remove();
//	$("#cancelBtn").attr("class","btn btn-primary btn-save btn-loading btn-w-xs").val("关闭") ;
}

//对按钮绑定全屏事件
$(document).on('click', '.btn-fullscreen', function(){
    var $wrapper = $(this).parents('.fullscreen-wrapper')[0];
    toggleFullScreen(document.documentElement);
    //全屏的时候将几个模态框放到下面去
    $('.modal[role="dialog"]').appendTo($wrapper);
});

//同步拜访开始和结束时间
$(function(){  
    $("#visDateStart").change(function() {  
        var value = $(this).val(); 
        if($("#visDateEnd").val ==null || $("#visDateEnd").val()==""){
        	$("#visDateEnd").datepicker('setDate',value);
        }
        if($("#visDateEnd").val() < $("#visDateStart").val() && $("#visDateEnd").val() != ""){
    		$(".errDate").show();
    	}else{
    		$(".errDate").hide();
    	}
    });  
    
}); 
$(function(){  
	$("#visDateEnd").change (function(){
		if($("#visDateStart").val() ==null || $("#visDateStart").val()==""){
			$("#visDateStart").datepicker('setDate', $("#visDateEnd").val());
		}
    	if($("#visDateEnd").val() < $("#visDateStart").val() && $("#visDateEnd").val() != ""){
    		$(".errDate").show();
    	}else{
    		$(".errDate").hide();
    	}
    })
}); 
//定义变更动作的功能
$(function(){
	$("#visContacts").change(function(){
		console.info($(this).val());
		if($(this).val() != null && $(this).val() != ""){
			getVisConTel();
		}else{
			$("#visConTel").val("");
		}
	})
});
//通过AJAX得到数据
function getVisConTel(){
	$("#visConTel").val("");
	var visContacts = $("#visContacts").val();
	var conUrl =CONTACTS_PATH + "/queryConTel.do"+"?conId="+visContacts;
	$.ajax({
		url : conUrl,
		type :"get",
		dataType :"json",
		success :function(data){
			$("#visConTel").val(data.phone);
		}
	});
}
