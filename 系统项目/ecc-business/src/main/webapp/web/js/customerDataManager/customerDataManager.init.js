var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

var customerDataInfoList = $('#customerDataInfoList');
$(function() {
	$('.use-select2').select2({allowClear: false,minimumResultsForSearch:Infinity}); 
	initGrid();
})

/**
 * 弹出新窗口
 * 
 * @author fangdb
 * @param actionUrl
 * @return
 */
var openDialog = function(actionUrl) {
	window.open(actionUrl, "", "height=725, width=1050, top=50, left=250, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
};


function exportDetail(){
	//客户名称
	var idnm = $("#idnm").val();
	//基金账号
	var fundacct = $("#fundacct").val();
	//评估过期日期开始
	var startDate = $("#startDate").val();
	//评估过期日期结束
	var endDate = $("#endDate").val();
	//客户类型
	var invtp = $("#invtp").val();
	//投资者类型
	var invprtp = $("#invprtp").val();
	//导出按钮
	var exportForm = $("#exportForm");
	//投资者类型
	var invprtp = $("#invprtp").val();
	//风险承受能力
	var risklevel = $("#risklevel").val();
	
	var specRiskLevel = $("#specRiskLevel");
	
	var specRiskLevelVal = $("#specRiskLevel").val();
	
	var clickFlag = specRiskLevel.is(":checked");
	if(clickFlag){
		console.log(clickFlag);
		specRiskLevelVal = $("#specRiskLevel").val();
	}else{
		specRiskLevelVal = "";
	}
	
	var url = PRIMARY_PATH + "/exportCustData.xhtml?";
	var params = 
			'sp[invnm]=' + idnm +
			'&sp[fundacct]=' + fundacct + 
			'&sp[startDate]=' + startDate +
			'&sp[endDate]=' + endDate + 
			'&sp[invtp]=' + invtp + 
			'&sp[invprtp]=' + invprtp +
			'&sp[risklevel]=' + risklevel + 
			'&sp[specRiskLevel]=' + specRiskLevelVal;
	var actionUrl = url + params;
	exportForm.prop("action",actionUrl);
	exportForm.submit();
}


function resetSearchVal(){
	//客户名称
	var idnm = $("#idnm").val(null);
	//基金账号
	var fundacct = $("#fundacct").val(null);
	//评估过期日期开始
	var startDate = $("#startDate").val(null);
	//评估过期日期结束
	var endDate = $("#endDate").val(null);
	//客户类型
	var invtp = $("#invtp").val(null);
	//投资者类型
	var invprtp = $("#invprtp").val(null);
	//投资者类型
	var invprtp = $("#invprtp").val(null);
	//风险承受能力
	var risklevelVal = $("#risklevel").val(null);
	var specRiskLevel = $("#specRiskLevel");
	var risklevel = $("#risklevel");
	specRiskLevel.attr("checked",false);
	risklevel.attr("disabled",false);
	risklevel.css("background-color","");
}

$(function(){
	riskAffordAbilityAppend();
});

/*日期控件 单个日期选择  */
WASP_WIDGET.triggerDateStyleWithYMD("startDate");
WASP_WIDGET.triggerDateStyleWithYMD("endDate");

//查询
function queryByCondtion(flag){
	//客户名称
	var idnm = $("#idnm").val();
	//基金账号
	var fundacct = $("#fundacct").val();
	//评估过期日期开始
	var startDate = $("#startDate").val();
	//评估过期日期结束
	var endDate = $("#endDate").val();
	//客户类型
	var invtp = $("#invtp").val();
	//投资者类型
	var invprtp = $("#invprtp").val();
	//最低
	//投资者类型
	var invprtp = $("#invprtp").val();
	//风险承受能力
	var risklevel = $("#risklevel").val();
	
	var specRiskLevel = $("#specRiskLevel");
	
	var specRiskLevelVal = $("#specRiskLevel").val();
	
	var clickFlag = specRiskLevel.is(":checked");
	if(clickFlag){
		specRiskLevelVal = $("#specRiskLevel").val();
	}else{
		specRiskLevelVal = "";
	}
    var postData = customerDataInfoList.jqGrid("getGridParam", "postData");
    	$.extend(postData,{
        	'sp[invnm]': idnm,
        	'sp[fundacct]': fundacct,
        	'sp[startDate]': startDate,
        	'sp[endDate]': endDate,
        	'sp[invtp]': invtp,
        	'sp[invprtp]': invprtp,
        	'sp[risklevel]': risklevel,
        	'sp[specRiskLevel]': specRiskLevelVal
        });
    if (flag) {
    	customerDataInfoList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    } else {
    	customerDataInfoList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
    }
}


function changeSpecRiskLevel(ele, targetId) {
	var checked = ele.checked;
	if (checked) {
		document.getElementById(targetId).value = "";
		document.getElementById(targetId).disabled = true;
		document.getElementById(targetId).style.backgroundColor = "#dddddd";
	} else {
		document.getElementById(targetId).disabled = false;
		document.getElementById(targetId).style.backgroundColor = "";
	}
}

/*风险承受能力下拉框数据*/
function riskAffordAbilityAppend(){
	var risklevel = $("#risklevel");
	$.ajax({
		url: PRIMARY_PATH + "/queryRisklevlArrayInfo.xhtml",
		type:"POST",
		data: 
		{
			'sp[pmst]' : 'SYSTEM',
			'sp[pmky]' : 'CUSTRISKLEVEL'
		},
		success: function(data){
			for(var i = 0; i < data.length; i++){
				var html = "<option value =" + data[i].pmco + ">" + data[i].pmco +"  " + data[i].pmnm  + "</option>";
				risklevel.append(html);
			}
		},
		dataType: 'json'
		}
	)
}


function initGrid(){
			 	customerDataInfoList.jqGrid({
				url : PRIMARY_PATH + '/queryRisklevlInfo.xhtml',
				caption : '客户评估数据管理<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
				datatype : "json",
				mtype:"GET",
				colNames :[ "客户名称", "客户类型", "基金账号", "风险等级", "风险等级(旧)", "评估日期",
						"过期时间","投资者类型", "交易方式","录音文件编号",'网上客户转换申请','转换申请日期',
						'操作用户','操作','','','','','','','',''],
				colModel : [ {
					name : 'invnm',
					index : 'invnm',
					align : 'left',
					hidden : false,
					sortable : false
				}, {
					name : 'invtpName',
					index : 'invtpName',
					align : 'left',
					hidden : false,
					resizable : true,
					sortable : false,
					formatter:function(colValue){
						if(colValue=='1')
							return '个人客户';
						if(colValue=='0')
							return '机构客户';
						return '';
					}
				}, {
					name : 'fundacc',
					index : 'fundacc',
					align : 'left',
					key : true,
					resizable : true,
					sortable : false
				}, {
					name : 'risklevelName',
					index : 'risklevelName',
					align : 'left',
					resizable : true,
					sortable : false
				}, {
					name : 'oldRiskLevelName',
					index : 'oldRiskLevelName',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'custriskdate',
					index : 'custriskdate',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'outdate',
					index : 'outdate',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'invprtpName',
					index : 'invprtpName',
					resizable : true,
					alisgn : 'left',
					sortable : false,
					formatter:function(colValue){
						if(colValue=='0')
							return '专业投资者';
						if(colValue=='1')
							return '普通投资者';
						return '';
					}
				},{
					name : 'regioncodeName',
					index : 'regioncodeName',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'voicerecord',
					index : 'voicerecord',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'appstName',
					index : 'appstName',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'apdt',
					index : 'apdt',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'opnm',
					index : 'opnm',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'option',
					index : 'option',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'custno',
					index : 'custno',
					resizable : true,
					key:true,
					hidden : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'answer',
					index : 'answer',
					hidden : true,
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'apptp',
					index : 'apptp',
					hidden : true,
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'risklevel',
					index : 'risklevel',
					hidden : true,
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'appst',
					index : 'appst',
					hidden : true,
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'invtp',
					index : 'invtp',
					hidden : true,
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'invprtp',
					index : 'invprtp',
					hidden : true,
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'regioncode',
					index : 'regioncode',
					hidden : true,
					resizable : true,
					alisgn : 'left',
					sortable : false
				}],
				rowNum : 20,
				rowList : [ 20, 30, 50 ],
				rownumbers : true,
				rownumWidth : 50,
				prmNames : {
					search : "search",
					page : "pageNo",
					rows : "limit"
				},
				height : 'auto',
				width : false,
				//autowidth : true,
				shrinkToFit : true,
				editurl : '',
				viewrecords : true,
				cellEdit : false,
				autoScroll: true,
				grouping : false,
				jsonReader : {
					root : "items", // 结果集
					records : "total", // 总记录数
					total : "pageCount", // 总页数
					page : "pageNo", // 当前页
					repeatitems : false
				},
				loadError : function(xhr, status, error) {
					switch (status) {
					case 403:
						sweetAlert("对不起，您无此权限！", "", "error");
						break;
					case 404:
						sweetAlert("对不起，无此页面！", "", "error");
						break;
					case 500:
						sweetAlert("内部错误，请联系管理员！", "", "error");
						break;
					case 504:
						sweetAlert("超时，请联系管理员！", "", "error");
						break;
					}
				},
				loadComplete : function(data) {
					if (data.ResultCode == '8000') {
						swal({
							title:'请重新登录',
							type : "warning",
							showCancelButton : true,
							confirmButtonColor : '#DD6B55',
							confirmButtonText : "确定",
							cancelButtonText : "取消",
							closeOnConfirm : false,
							closeOnCancel : true
						},function(isConfirm){
							if(isConfirm){
								window.location.href = '/service/login.jsp';
							}
						})
						return;
					}
				},
				pager : "#customerDataInfoPage",
				viewrecords : true,
				hidegrid : false,
				gridComplete : function() {
					var ids = customerDataInfoList.jqGrid('getDataIDs');
					for (var i = 0; i < ids.length; i++) {
						var id = ids[i];
						var rowData = customerDataInfoList.jqGrid('getRowData', id);
						var custno=rowData.custno;
						var invnm=rowData.invnm;
						var fundacc=rowData.fundacc;
						var invtp=rowData.invtp;
						var invtpName=rowData.invtpName;
						var risklevelName=rowData.risklevelName;
						var risklevel=rowData.risklevel;
						var invprtp=rowData.invprtp;
						var invprtpName=rowData.invprtpName;
						var regioncodeName=rowData.regioncodeName;
						var regioncode=rowData.regioncode;
						var appstName=rowData.appstName;
						var appst=rowData.appst;
						var apptp=rowData.apptp;
						var answer=rowData.answer;
						var voicerecord = rowData.voicerecord;	
						var params  = "&custNo=" +custno; 
									  /*"&invnm=" +invnm + 
									  "&fundacc=" +fundacc + 
									  "&voicerecord=" +voicerecord + 
									  "&invtp=" +invtp + 
									  "&invtpName=" +invtpName + 
									  "&risklevelName=" +risklevelName + 
									  "&risklevel=" +risklevel + 
									  "&regioncodeName=" +regioncodeName + 
									  "&regioncode=" +regioncode + 
									  "&invprtp=" +invprtp + 
									  "&invprtpName=" +invprtpName + 
									  "&appst=" +appst + 
									  "&appstName=" +appstName + 
									  "&apptp=" +apptp + 
								 	  "&answer=" +answer ;*/
						var se = '';
						if(invprtp == '0'){
							risklevelName = '';
						}
						var appstString = '';
						if(regioncode !=null && regioncode.length>0 && regioncode != ''){
							if(invprtp !=null && invprtp == '1'){
								if(apptp !=null && apptp == '0'){
									if(appst == 'N'){
										appstString = "已申请";
									}else if(appst == 'I'){
										appstString = "处理中";
									}else if(appst == 'C'){
										appstString = "申请失败";
									}else{
										appstString = "";
									}
								}else{
									appstString = "未申请";
								}
							}else{
								if(invprtp !=null && invprtp == '0'){
									if(appst == 'N'){
										appstString = "已申请";
									}else if(appst == 'I'){
										appstString = "处理中";
									}else if(appst == 'C'){
										appstString = "申请失败";
									}else{
										appstString = "";
									}
								}else{
									appstString = "";
								}	
							}
						}else{
							appstString = "";
						}	
						se = "<a href='javascript:void(0)' class='btn btn-link btn-jqgrid' onclick='" +
								"openEditDialog(\""
								+ params 
								+ "\")' title='修改'><i class='fa fa-pencil-square-o'></i></a>";
						de = "<a href='javascript:void(0)' class='btn btn-link btn-jqgrid' onclick='" +
								"openDetailDialog(\""
								+ params 
								+ "\")' title='详情'><i class='fa fa-file-text-o'></i></a>";
						customerDataInfoList.jqGrid('setRowData', id, {
							option : se + de, risklevelName : risklevelName,appstName : appstString
						});
					}
				},
				subGrid : false
			});
		
		 	customerDataInfoList.navGrid('#customerDataInfoPage', {
			edit : false,
			add : false,
			del : false,
			search : false,
			refreshstate : 'current'
			});
		 	customerDataInfoList.jqGrid('setFrozenColumns');
			jqGridResize(customerDataInfoList);
};
function openEditDialog(params){
	var action= PRIMARY_PATH + "/openDialog.xhtml?method=update"+params;
	openDialog(action);
}	
function openDetailDialog(params){
	var action= PRIMARY_PATH + "/openDialog.xhtml?method=detail"+params;
	openDialog(action);
}	