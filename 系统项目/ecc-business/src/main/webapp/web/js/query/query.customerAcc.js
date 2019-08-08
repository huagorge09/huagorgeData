
var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

var customerAccInfoList = $("#customerAccInfoList");

function initCustomerAccInfoListGrid() {
	customerAccInfoList.jqGrid({
					url : PRIMARY_PATH + '/queryAppModifyAcc.xhtml',
					caption : '客户账户资料修改列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
					datatype : "json",
					mtype:"GET",
					postData : queryPostData(),
					colNames :[ "申请日期", "基金账号", "投资者名称",'网点代码', "注册登记证件号码", "注册登记证件类型", "注册登记证件有效期",
							"开户银行","预留银行全称","预留银行开户地","预留银行账号","预留银行户名","办公电话",
							"移动电话","住宅电话","传真号码","传真委托","电子邮件","邮政编码",
							"账单送达方式","通讯地址"],
					colModel : [ {
						name : 'apdt',
						index : 'apdt',
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
						name : 'idno',
						index : 'idno',
						align : 'left',
						resizable : true,
						sortable : false
					}, {
						name : 'idtpName',
						index : 'idtpName',
						resizable : true,
						align : 'left',
						sortable : false
					}, {
						name : 'instreprdate',
						index : 'instreprdate',
						resizable : true,
						align : 'left',
						sortable : false
					},{
						name : 'bankno',
						index : 'bankno',
						resizable : true,
						align : 'left',
						sortable : false
					}, {
						name : 'banklongname',
						index : 'banklongname',
						resizable : true,
						align : 'left',
						sortable : false
					}, {
						name : 'option',
						index : 'option',
						resizable : true,
						align : 'left',
						sortable : false
					},{
						name : 'bankacco',
						index : 'bankacco',
						resizable : true,
						alisgn : 'left',
						sortable : false
					},{
						name : 'bankacname',
						index : 'bankacname',
						resizable : true,
						alisgn : 'left',
						sortable : false
					},{
						name : 'officetel',
						index : 'officetel',
						resizable : true,
						alisgn : 'left',
						sortable : false
					},{
						name : 'mobileno',
						index : 'mobileno',
						resizable : true,
						alisgn : 'left',
						sortable : false
					},{
						name : 'hometel',
						index : 'hometel',
						resizable : true,
						alisgn : 'left',
						sortable : false
					},{
						name : 'faxno',
						index : 'faxno',
						resizable : true,
						alisgn : 'left',
						sortable : false
					},{
						name : 'option',
						index : 'option',
						resizable : true,
						align : 'left',
						sortable : false
					},{
						name : 'email',
						index : 'email',
						resizable : true,
						alisgn : 'left',
						sortable : false
					},{
						name : 'postcode',
						index : 'postcode',
						resizable : true,
						alisgn : 'left',
						sortable : false
					},{
						name : 'delivertype',
						index : 'delivertype',
						resizable : true,
						alisgn : 'left',
						sortable : false
					},{
						name : 'addr',
						index : 'addr',
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
					pager : "#customerAccInfoPage",
					viewrecords : true,
					hidegrid : false,
					subGrid : false
				});

					customerAccInfoList.navGrid('#customerAccInfoPage', {
					edit : false,
					add : false,
					del : false,
					search : false,
					refreshstate : 'current'
				});
				customerAccInfoList.jqGrid('setFrozenColumns');
				jqGridResize(customerAccInfoList);
				customerAccInfoList.jqGrid('destroyGroupHeader');//最关键的一步、销毁合并表头分组、防止出现表头重叠
				customerAccInfoList.jqGrid("setGroupHeaders", {  
				    useColSpanStyle : true ,//没有表头的列是否与表头所在行的空单元格合并  
				    groupHeaders : [//{},{}...  
				        {  
				            startColumnName : "idno",//合并列的起始位置 colModel中的name  
				            numberOfColumns : 3, //合并列数 包含起始列  
				            titleText :'<div  style="text-align:center;border-right:1px solid #e2e2e2;border-left:1px solid #e2e2e2;"><span>证件信息</span></div>'//表头  
				        },{  
				            startColumnName : "bankno",  
				            numberOfColumns : 5,   
				            titleText : '<div  style="text-align:center;border-right:1px solid #e2e2e2;"><span>银行信息</span></div>'
				        },{  
				            startColumnName : "officetel",  
				            numberOfColumns : 9,   
				            titleText : '<div  style="text-align:center;border-right:1px solid #e2e2e2;"><span>其他信息</span></div>'
				        }   
				    ]  
				});  
		
		};