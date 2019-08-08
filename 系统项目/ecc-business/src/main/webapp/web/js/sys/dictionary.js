/**
 * 产品发起公共操作JS类
 * @author ex-huangch 2016.5.5
 */
var PATH_PREFIX ="";

function setPathPrefix(dictPath){
	PATH_PREFIX = dictPath;
}

//注册清空事件
WASP_WIDGET.registerResetClearEvent();

var WASP_DICTIONARY = {
	    //新增系统字典
	    dictionaryAddView: function() {
	    	var actionUrl = PATH_PREFIX+"dictionaryAddView.do";
	    	openDialog(actionUrl);
	    },
	    //修改系统字典
	    dictionaryUpdateView: function(dctId) {
			var actionUrl = PATH_PREFIX+"dictionaryUpdateView.do?dctId="+dctId;
	    	openDialog(actionUrl);
		},
		//查询系统字典
		dictionaryDetailView: function(dctId) {
			var actionUrl = PATH_PREFIX+"dictionaryDetailView.do?dctId="+dctId;
	    	openDialog(actionUrl);
	    },
		//删除系统字典
		deleteDictionary: function(dctId){
			 ctools.confirm("您确定要删除吗？",function(){
				 var actionUrl = PATH_PREFIX+"deleteDictionary.do?dctId="+dctId;
			    	openDialog(actionUrl);
	    	},"");
	    },
	     //刷新缓存
	    dictionaryRefresh: function(dctId){
	    	var actionUrl = PATH_PREFIX+"refresh.do?";
	    	openDialog(actionUrl);
	    },
	    /**
	     * 返回jqGrid中select的候选值，格式 "1:a;2:b"
	     * 构造"<select><option value="1">a</option><option value="2">b</option></select>"
	     * @author ex-lix
	     * getDictionaryAjax.jsp
	     */
		getDictSelectString : function (dctRootType, dctLeftType){
			var result;
			$.ajax({
				type: "POST",
				dataType:"JSON",
				async:false,
				url:PATH_PREFIX + "dictionaryAjaxView.do?dictRootType=" + dctRootType+"&dictLeftType="+dctLeftType, 				
				success: function(msg){
				  result = msg;
				},
			});
			return $.trim(result);
		},
	};
/**
 *数据字典展示列表
 **/
var $dictionaryList = $('#dictionaryList');
$(function() {
    $dictionaryList.jqGrid({
        url: PATH_PREFIX+'dictionaryListPage.do',
        caption: '字典查询列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
        datatype: "json",        
		colNames:['字典ID', "字典根类型", "字典父类型", "字典子类型", "字典名", "字典值", "是否叶子", "是否生效", '操作'],        
		colModel:[
                    {name:'dctId',index:'dctId',align:'center',hidden:true, key:true,sortable:false},              
					{name:'dctRootType',index:'dctRootType', width:40, resizable:true,align:'left',sortable:false},
					{name:'dctFathType',index:'dctFathType', width: 40, resizable:true,align:'left',sortable:false},   
					{name:'dctLeftType',index:'dctLeftType', width:40,resizable:true,align:'left',sortable:false},
					{name:'dctName',index:'dctName', width:50, resizable:true,align:'left', sortable:false},
					{name:'dctValue',index:'dctValue',width:50, resizable:true,align:'left', sortable:false},
					{name:'dctIsLeafNM',index:'dctIsLeafNM', width:20, resizable:true,align:'center', sortable:false},
					{name:'dctStatNM',index:'dctStatNM', width:20, resizable:true,align:'center',sortable:false},
					{name:'option',index:'option', width: 30, resizable:true,align:'left',sortable:false}
				 ],     
        rowNum: 10,
        rowList: [10, 20, 30, 50],
        rownumbers: true,
        rownumWidth: 50,
        prmNames: {
        	        search: "search", 
        	        page: "pageNo",
        	        rows: "limit" 
        	       },
        height: 'auto',
        width: false,
        autowidth:true,
        editurl: '',
        viewrecords: true,
        cellEdit: false,
        shrinkToFit: true,
        grouping: false,
        jsonReader: {
            root: "items", //结果集
            records: "total", //总记录数 
            total: "pageCount", //总页数
            page: "pageNo", //当前页 
            repeatitems: false // (4) 
        },
        multiselect: false,
        pager: "#dictionaryPage",
        viewrecords: true,
        hidegrid: false,
        gridComplete: function() {
            var ids = $dictionaryList.jqGrid('getDataIDs');
				for(var i=0;i < ids.length;i++){
                    var dctId = ids[i];
                    be = '<a date-dctid="' + dctId + '" href="#" class="btn btn-link btn-jqgrid" id="'+ dctId +'Del" name="'+ dctId +'Del" title="删除" onclick="WASP_DICTIONARY.deleteDictionary(\''+dctId+'\');" ><i class="fa fa-trash-o "></i></a>';
                    se = '<a date-dctid="' + dctId + '" href="#" class="btn btn-link btn-jqgrid" id="'+ dctId +'Update" name="'+ dctId +'Update" title="修改" data-toggle="modal" onclick="WASP_DICTIONARY.dictionaryUpdateView(\''+dctId+'\');" data-target="#modal-edit"><i class="fa fa-pencil-square-o"></i></a>';
                    $dictionaryList.jqGrid('setRowData',ids[i],{option:be+se});
				}	
			},
			ondblClickRow:function(dctId)
			{
				WASP_DICTIONARY.dictionaryDetailView(dctId);//双击打开详情页
		    }				
		});
    $dictionaryList.navGrid('#dictionaryPage', { edit: false, add: false, del: false, search: false, refreshstate: 'current' });
    $dictionaryList.jqGrid('setFrozenColumns');
    jqGridResize($dictionaryList);
    
});


/**
 * 通过条件进行搜索
 */
function queryByCondtion(flag) {
	//获取字典根类型值
	var dctRootType = $("#q-txtDictRootType").val();
	//获取字典父类型值
	var dctFathType = $("#q-txtDictFathType").val();
	//获取字典子类型值
	var dctLeftType = $("#q-txtDictLeftType").val();
	var dctName     = $("#q-txtDictName").val();
    var postData = $dictionaryList.jqGrid("getGridParam", "postData");
    $.extend(postData, {
        'sp[dctRootType]':dctRootType,
        'sp[dctFathType]':dctFathType,
        'sp[dctLeftType]':dctLeftType,
        'sp[dctName]':dctName
    });
    if (flag) {
    	$dictionaryList.trigger("reloadGrid", [{ page: 1 }]); //重新载入Grid表格
    } else {
    	$dictionaryList.trigger("reloadGrid"); //重新载入Grid表格
    }
};

//对按钮绑定全屏事件
$(document).on('click', '.btn-fullscreen', function(){
    var $wrapper = $(this).parents('.fullscreen-wrapper')[0];
    toggleFullScreen(document.documentElement);
    //全屏的时候将几个模态框放到下面去
    $('.modal[role="dialog"]').appendTo($wrapper);
});




