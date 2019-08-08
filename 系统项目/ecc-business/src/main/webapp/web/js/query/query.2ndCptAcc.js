
var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

var secondCptAccInfoList = $("#secondCptAccInfoList");

						function init2ndCptAccInfoListGrid() {
							secondCptAccInfoList.jqGrid({
										url : PRIMARY_PATH + '/queryAckClearAcc.xhtml',
										caption : '二级清算账户确认列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
										datatype : "json",
										mtype:"GET",
										postData : queryPostData(),
										colNames :[ "报表功能", "基金账号", "业务类型", "交易账号",'网点代码',"确认日期", "投资者类型",
												"投资者姓名","处理标志","证件类型","证件号码","清算日期","处理标志",
												"分红方式","返回代码","备注","申请流水号","备注流水号",''],
										colModel : [ {
											name : 'aTag',
											index : 'aTag',
											align : 'left',
											hidden : false,
											key : true,
											sortable : false
										}, {
											name : 'fundacct',
											index : 'fundacct',
											align : 'left',
											hidden : false,
											resizable : true,
											sortable : false
										}, {
											name : 'apkindName',
											index : 'apkindName',
											align : 'left',
											resizable : true,
											sortable : false
										}, {
											name : 'tradeacco',
											index : 'tradeacco',
											align : 'left',
											resizable : true,
											sortable : false
										}, {
											name : 'netPoint',
											index : 'netPoint',
											align : 'left',
											resizable : true,
											sortable : false
										}, {
											name : 'ackdt',
											index : 'ackdt',
											resizable : true,
											align : 'left',
											sortable : false
										}, {
											name : 'invtpName',
											index : 'invtpName',
											resizable : true,
											align : 'left',
											sortable : false
										}, {
											name : 'invnm',
											index : 'invnm',
											resizable : true,
											align : 'left',
											sortable : false
										}, {
											name : 'applyst',
											index : 'applyst',
											resizable : true,
											alisgn : 'left',
											sortable : false
										},{
											name : 'idtpName',
											index : 'idtpName',
											resizable : true,
											alisgn : 'left',
											sortable : false
										},{
											name : 'idno',
											index : 'idno',
											resizable : true,
											alisgn : 'left',
											sortable : false
										},{
											name : 'cleardt',
											index : 'cleardt',
											resizable : true,
											alisgn : 'left',
											sortable : false
										},{
											name : 'applyst',
											index : 'applyst',
											resizable : true,
											alisgn : 'left',
											sortable : false
										},{
											name : 'melonmdName',
											index : 'melonmdName',
											resizable : true,
											alisgn : 'left',
											sortable : false
										},{
											name : 'retcode',
											index : 'retcode',
											resizable : true,
											alisgn : 'left',
											sortable : false
										},{
											name : 'retmsg',
											index : 'retmsg',
											resizable : true,
											alisgn : 'left',
											sortable : false
										},{
											name : 'serialno',
											index : 'serialno',
											resizable : true,
											alisgn : 'left',
											sortable : false
										},{
											name : 'ackno',
											index : 'ackno',
											resizable : true,
											alisgn : 'left',
											sortable : false,
											width : 200
										},{
											name : 'apkind',
											index : 'apkind',
											hidden : true,
											resizable : true,
											alisgn : 'left',
											sortable : false
										}],
										rowNum : 10,
										rowList : [ 10, 30, 50 ],
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
										shrinkToFit : false,
										editurl : '',
										viewrecords : true,
										cellEdit : false,
										grouping : false,
										autoScroll: true,
										jsonReader : {
											root : "items", // 结果集
											records : "total", // 总记录数
											total : "pageCount", // 总页数
											page : "pageNo", // 当前页
											repeatitems : false
										// (4)
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
										pager : "#secondCptAccInfoPage",
										viewrecords : true,
										hidegrid : false,
										gridComplete : function() {
											var ids = secondCptAccInfoList.jqGrid('getDataIDs');
											for (var i = 0; i < ids.length; i++) {
												var id = ids[i];
												var rowData = secondCptAccInfoList.jqGrid('getRowData', id);
												var ackno=rowData.ackno;
												var apkind=rowData.apkind;
												var url = PRIMARY_PATH + "/getDsReportLink.xhtml?";
												var appDate = $("#appDate").val();
												var be = '';
												if(apkind == '101'){
														be = "<a href= ' " + url + "permissionId=8818" 
																+ "&PI_ACKNO=" + ackno
																+ "&PI_ACKSDT=" + appDate
																+ "&PI_ACKEDT=" + appDate
																+ " ' style='color:blue;' target = '_blank' "  
																+ ">账户开户确认回单</a>";
												}else if(apkind == '102' || apkind == '109'){
														be = "<a href= ' " + url + "permissionId=8814" 
																+ "&PI_ACKNO=" + ackno
																+ "&PI_ACKSDT=" + appDate
																+ "&PI_ACKEDT=" + appDate
																+ " ' style='color:blue;' target = '_blank' "  
																+ ">账户销户确认回单</a>";
												}else if(apkind == '103'){
														be = "<a href= ' " + url + "permissionId=8816" 
																+ "&PI_ACKNO=" + ackno
																+ "&PI_ACKSDT=" + appDate
																+ "&PI_ACKEDT=" + appDate
																+ " ' style='color:blue;' target = '_blank' "  
																+ ">账户变更确认回单</a>";
												}else{
														be = "";
												}
												secondCptAccInfoList.jqGrid('setRowData', id, {
													aTag : be
												});
											}
										},
										subGrid : false
									});

							secondCptAccInfoList.navGrid('#secondCptAccInfoPage', {
								edit : false,
								add : false,
								del : false,
								search : false,
								refreshstate : 'current'
							});
							secondCptAccInfoList.jqGrid('setFrozenColumns');
							jqGridResize(secondCptAccInfoList);
						};