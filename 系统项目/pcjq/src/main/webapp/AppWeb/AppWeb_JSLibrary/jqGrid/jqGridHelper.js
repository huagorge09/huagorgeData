/**
 * 根据外部容器的大小重置jqGrid控件的宽度
 * 需引入jqGrid和jquery包
 * @author fangdb
 * @param id jqgird控件的ID
 * @return
 */
function doResize(id, obj)
{
    var ss = getPageSize();
	if(obj != null && obj.width){
		ss = {Winw : obj.width}
	}
    $("#" + id).jqGrid("setGridWidth", ss.WinW - 8);
}

/**
 * 获取当前窗口的大小
 * @author fangdb
 * @return
 */
function getPageSize()
{
    var winW, winH;

    if(window.innerHeight)
    {
        winH = window.innerHeight;
        winW = window.innerWidth;
        
    }else if (document.documentElement && document.documentElement.clientHeight)
    {
        winW = document.documentElement.clientWidth;
        winH = document.documentElement.clientHeight;
    }else if (document.body)
    {
        winW = document.body.clientWidth;
        winH = document.body.clientHeight;
    }

    return {WinW: winW, WinH: winH};
} 
