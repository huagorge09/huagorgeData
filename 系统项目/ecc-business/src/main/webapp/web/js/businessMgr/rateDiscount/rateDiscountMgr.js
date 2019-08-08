/**
 * 
 */
var PRIMARY_PATH = "";
var BASE_PATH = "";
var $rateDiscountList = $('#rateDiscountList');

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

$(function(){
	initGrid();
	initWidget();
});

var rateFunc = {
		addRateDiscount : function(){
			openDialog(PRIMARY_PATH+"/rateDiscountMgrAddView.do");
		},
		copyRateDiscount : function(){
			openDialog(PRIMARY_PATH+"/rateDiscountMgrCopyView.do");
		},
		delRateDiscountByParam : function(bankNo,productId,apKind){
			ctools.confirm("确认删除该数据？",function(isConfirm){
				$.ajax({
			    	async:false,
					url:PRIMARY_PATH+'/delRateDisc.do',
					type:"post",
					dataType:'json',
					data:{
						"bnkNo" : bankNo,
						"productId":productId,
						"apkind" : apKind
					},
					error:function(a,b,c){
						swal("调用失败!", "", "error");
					},
					success:function(data, textStatus){
						if(!!data && "0000" == data.resultCode){
							swal("删除成功!", "", "success");
							queryByCondtion(false);
						}else{
							swal(data.resultMsg, "", "success");
						}
					}
				});
			});
			
		},
		updateRateDiscountByParam : function (bankNo,productId,apKind){
			openDialog(PRIMARY_PATH+"/rateDiscountMgrUpdateView.do?bankNo="+bankNo+"&productId="+productId+"&apKind="+apKind);
		}
};

function initGrid(){
	$rateDiscountList.jqGrid({
		url: PRIMARY_PATH+'/rateDiscListPage.do',
        caption: '费率折扣管理列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
        datatype: "json",
        colNames: ["银行代码id","银行名称","商户产品ID","商户产品ID(基金代码)","业务代码id","业务类型","费率折扣","起始日期","结束日期","状态id","状态","创建人id","创建人","创建时间","操作"],
        colModel: [
            { name: 'bnkNo', index: 'bnkNo',  hidden: true, key: false, sortable: false},
            { name: 'bnkName', index: 'bnkName', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'productId', index: 'productId',  hidden: true, key: false, sortable: false},
            { name: 'productName', index: 'productName', width:200, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'apkind', index: 'apkind',  hidden: true, key: false, sortable: false},
            { name: 'apkindName', index: 'apkindName', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'discount', index: 'discount', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'strDate', index: 'strDate', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'endDate', index: 'endDate', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'status', index: 'status',  hidden: true, key: false, sortable: false},
            { name: 'statusName', index: 'statusName', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'cMan', index: 'cMan',  hidden: true, key: false, sortable: false},
            { name: 'cManName', index: 'cManName', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'cTime', index: 'cTime', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
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
        shrinkToFit:true,
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
        pager: "#rateDiscountPage",
        viewrecords: true,
        hidegrid: false,
		subGrid: false,
		gridComplete: function() {
			var ids = $rateDiscountList.jqGrid('getDataIDs');
			for(var i=0;i < ids.length;i++){
				var id = ids[i];
				var rowData = $rateDiscountList.jqGrid('getRowData', id);	
				var bnkNo = rowData.bnkNo;
				var productId = rowData.productId;
				var apkind = rowData.apkind;
				//按钮置灰
				$(".permissionBtn a").attr("disabled", "disabled");
                $(".permissionBtn a").removeAttr("onclick");
				//初始化 费率管理 修改 删除
                $("#rateDiscUpdateBtn a").removeAttr("disabled");
            	$("#rateDiscUpdateBtn a").attr("onClick", "rateFunc.updateRateDiscountByParam('"+bnkNo+"','"+productId+"','"+apkind+"');");
            	
            	$("#rateDiscDelBtn a").removeAttr("disabled");
            	$("#rateDiscDelBtn a").attr("onClick", "rateFunc.delRateDiscountByParam('"+bnkNo+"','"+productId+"','"+apkind+"');");
            	
            	var cub = $("#rateDiscUpdateBtn").html();
            	var cdb = $("#rateDiscDelBtn").html();
            	
            	$rateDiscountList.jqGrid('setRowData',ids[i],{option: cub+cdb });
			}
		}
    });

	$rateDiscountList.navGrid('#rateDiscountPage', { edit: false, add: false, del: false, search: false, refreshstate: 'current' });
    $rateDiscountList.jqGrid('setFrozenColumns');
    jqGridResize($rateDiscountList);
}

function initWidget(){
	WASP_WIDGET.triggerBnkBaseListSelect("q-bankName",false);
	//注册清空事件
    WASP_WIDGET.registerResetClearEvent();
}

function queryByCondtion(flag){
	var bankName = $("#q-bankName").val();
	
    var postData = $rateDiscountList.jqGrid("getGridParam", "postData");
    $.extend(postData,{
    	'sp[bankName]': bankName
    });

    if (flag) {
    	$rateDiscountList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    } else {
    	$rateDiscountList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
    }
}

//对按钮绑定全屏事件
$(document).on('click', '.btn-fullscreen', function(){
    var $wrapper = $(this).parents('.fullscreen-wrapper')[0];
    toggleFullScreen(document.documentElement);
    //全屏的时候将几个模态框放到下面去
    $('.modal[role="dialog"]').appendTo($wrapper);
});
