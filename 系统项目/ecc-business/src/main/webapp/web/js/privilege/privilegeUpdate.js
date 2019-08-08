/**
 * 
 */
var allSet=[];
var selectedSet=new AUISet();
var flag = false;
var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(basePath,primaryPath){
	BASE_PATH = basePath;
	PRIMARY_PATH = primaryPath;
}

$(document).ready(function(){
	selectBox(flag);
	
	//查询输入框绑定事件进行搜索（本地搜索）
	//查询输入框绑定事件进行搜索（本地搜索）
	$("#queryBtn").on("click",function(){
		search4Input();
	});
});

function selectBox(f){  
	  var resName = $("#resName").val();
	  var roleId = $("input[name=roleId]:eq(0)").val();
	  param = {"resName":resName,"roleId":roleId};
	  var url = PRIMARY_PATH +"/queryNotAuthResListByRoleEmps.do";
	  
	  $("#options-body").find('.multiselect-item').remove(); //追加成功则清空
	  var $list=$(".selected-body").find(".multiselect-item");//获取右边的值
	  var arr=[];
	  selectedSet.clear();
	  if($list.length>0){
	  for(var i=0;i<$list.length;i++){
			var item=$($list[i]);
			arr[i]=item.attr("id");
			selectedSet.add(arr[i]);
		}
	  }
		
	  $.ajax({
			type: "POST",
			dataType: "json",
			url:url,
			data:param,
			async : true,
			success: function(data){
				$(".options-body").find('.multiselect-item').remove();//追加成功则清空
				//将整个资源信息获取出来，存在于ids[]（已选列表）中的追加于selected-body，否之追加于options-body
				if(data && data.length>0){
					var $h=[];
					for(var i=0;i<data.length;i++){
						var _item={
								resId:data[i].resId,
								resName:data[i].resName
						};
							
						allSet[i]=_item;//将所有的资源保存到一个集合，用于本地搜索
						if(i<=100){//只拼接100个，性能问题考虑
							if(!selectedSet.contains(data[i].resId)){
								var $html='<div class="multiselect-item" id="'+data[i].resId +'">\
				 				'+data[i].resName+'\
				 				<button type="button" class="mulitselect-remove">×</button>\
				 				</div>';
								$h.push($html);
							}
						}
					}
					$(".options-body").append($h);
				}
				//如果已选框有值，则左移按钮显示可用
				initMultiselect();//处理已选不选按钮
			},
			error: function(jqXHR, textStatus, errorThrown){
				ctools.alert_sweet("初始化列表错误 " + textStatus,"error","");
			}
	});
}


function search4Input(){
	var $list=$(".selected-body").find(".multiselect-item");//获取右边的值
	var arr=[];
	selectedSet.clear();
	if($list.length>0){
		for(var i=0;i<$list.length;i++){
			var item=$($list[i]);
			arr[i]=item.attr("id");
			selectedSet.add(arr[i]);
		}
	}
	var sNames=$("#resName").val();//用户的查询关键字
	var searchedSet=[];//本次查询出来的数据集合
	$.each(allSet,function(index,item){//从所有的集合中查找出用户输入的关键字的资源放入查找集合
		console.info(item);
		if(!selectedSet.contains(item.resId) && item.resName.indexOf(sNames)!=-1){//右边没有选中并且符合用户查询数据
			searchedSet.push(item);
		}
	});
	$(".options-body").find('.multiselect-item').remove(); //追加成功则清空
	var $h=[];
	$.each(searchedSet,function(index,item){
		if(index>100){//只拼接100个，性能问题考虑
			return;
		}
		var $html='<div class="multiselect-item" sNames="'+item.resName+'" id="'+item.resId
		+'">\
		'+item.resName+'\
		<button type="button" class="mulitselect-remove">×</button>\
		</div>';
		$h.push($html);
	});
	$(".options-body").append($h);
}

function save(){
	SubmitAndPreventSecond("privilegeUpdateForm",function(){
		var roleId = $("#roleId").val();
		var selValue = "";
		var data=[];
		var $list=$(".selected-body").find(".multiselect-item");//获取已选项div
		if($list.length>0){
				for(var i=0;i<$list.length;i++){
					var item=$($list[i]);
					data[i]=item.attr("id");
					if( i == 0){
						selValue = selValue + data[i];
					}else{
						selValue = selValue + "," + data[i] ;
					}
				}
				$("#resId").val(selValue);
				$("#privilegeUpdateForm").attr("action",PRIMARY_PATH + "/updatePrivilege.do");
		}else{
			$("#resId").val(selValue);
			$("#privilegeUpdateForm").attr("action",PRIMARY_PATH + "/updatePrivilege.do");
		}
	});
		
}
