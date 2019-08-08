/**
 * 
 */
var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

$(function(){
	$("#isoriginal").val($("input[name=hidIfOriginal]").val());
	$("#isalldoc").val($("input[name=hidIfalldocument]").val());
	$("#isscan").val($("input[name=hidIsscan]").val());
	$("#issaved").val($("input[name=hidIfsaved]").val());
	$("#isupload").val($("input[name=hidIsupload]").val());
	initWidget();
});


function doSubmit(){
	 //资料信息 
	   var docInfo="";
	   $("input[name='custinfoidText']").each(function(data)
	   {
	      if($(this).is(":checked"))
	      {
	    	  docInfo +=$(this).attr("value")+"-"+"1"+",";
	      }
	      else
	      {
	    	  docInfo +=$(this).attr("value")+"-"+"0"+",";
	      }         
	   });
	$("input[name=custinfoid]").val(docInfo);
	if(!docbusinesstp){
		ctools.alert("请选择业务类型","请完善信息","warning");
		return ;
	}
	//提交表单
	SubmitAndPreventSecond("updateCustDataFrom",true);
	
}

function initWidget(){
	var hidAptype = $("input[name=hidAptype]").val();
	var hidAptypeName = $("input[name=hidAptypeName]").val();
	WASP_WIDGET.initializeSelectVal("docbusinesstp",hidAptype,hidAptypeName);
	$("#docbusinesstp").toggle("change");
	WASP_WIDGET.triggerParamListSelectMultName("docbusinesstp",false,"业务类型");
	$("#isoriginal").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#isalldoc").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#isscan").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#issaved").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#isupload").select2({allowClear: false,minimumResultsForSearch:Infinity});
}

function docChange(){
	var docSelt = $("#docbusinesstp").val();
	$.ajax({
    	async:false,
		url:BASE_PATH+'service/widget/queryMatchParamList.do',
		type:"post",
		dataType:'json',
		data:{
			"pmst" : 'DS',
			"pmky" : docSelt
		},
		success:function(data, textStatus){
			var tar = $("#documentinfo");
			if(!!data){
				tar.find("tr[bind_code=append]").remove();
				var selectData = $("input[name=hidCustinfoid]").val();
				var hidAptype = $("input[name=hidAptype]").val();
				for(var i=0 ; i<data.length;i++){
					var text = "<tr bind_code=\"append\">";
					text +=	"<td class=\"white-bg\" colspan=\"3\">";
					text +=	"<div class=\"col-sm-10 form-inner\">";
					text +=	"<label>";
					var value=data[i].PMCO;
					var dispName=data[i].PMNM;
					var checked = selectData.indexOf(value) >=0 && docSelt == hidAptype? "checked":"" ;
					text +=	 "<input class=\"i-checks\" type=\"checkbox\" name=\"custinfoidText\" value=\""+value+"\" "+checked+">"+dispName+"";
					text +="</label>";
					text +="</div>";
					text +="</td>";
					text +="</tr>";
					tar.append(text);
				}
				$("#docListTh").attr("rowspan",tar.find("tr").length);
			}
		}
	});
}

function doUpload(){
	var appserialno = $("input[name=appserialno]").val();
	var fundacct = $("input[name=fundacct]").val();
	var custno = $("input[name=custno]").val();
	if(!fundacct){
		ctools.alert("基金账号为空","","warning");
		return ;
	}
	openDialog(PRIMARY_PATH+'/uploadCustDataDocView.do?appserialno='+appserialno+"&fundacct="+fundacct+"&custno="+custno);
}