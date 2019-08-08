var $menuList = $('#menuList');
var $organizationList = $('#organizationList');
var $buttonList = $("#buttonList");
$(function() {
	// 初始化组织树
	$organizationList
			.jstree(
					{
						'core' : {
							'animation' : 0,
							"check_callback" : true,
							'data' : {
								'url' : MENU_AUTH_PATH_PREFIX+'ajaxOrganizationList.do',
								'data' : function(node) {
									var nodeId = '';
									if (node.id) {
										nodeId = node.id.replace('#', '');
									}
									return {
										'orgId' : nodeId
									};
								}
							},
							"themes" : {
								"name" : "default",
								"dots" : false,
								"icons" : false
							}
						},
						'contextmenu' : {
							'items' : function(node) {
								var id = $(node).attr('id') || '';
								if ($organizationList.jstree('is_leaf', node)
										&& id.indexOf('EMP-') != -1) {
									var tmp = {
										'copy' : {},
										'mcopy' : {},
										'clear' : {}
									};
									tmp.copy.label = "复制岗位菜单并覆盖";
									tmp.copy.action = function(data) {
										var inst = $.jstree.reference(data.reference);
										var obj = inst.get_node(data.reference);
										var id = $(obj).attr('id'), parent = $(obj).attr('parent');
										copyOrmemberMenuForEmp(id, parent,false);
									};
									tmp.mcopy.label = "复制岗位菜单后合并";
									tmp.mcopy.action = function(data) {
										var inst = $.jstree.reference(data.reference);
										var obj = inst.get_node(data.reference);
										var id = $(obj).attr('id'), parent = $(obj).attr('parent');
										copyOrmemberMenuForEmp(id, parent,true);
									};
									tmp.clear.label = "清空权限菜单";
									tmp.clear.action = function(data) {
										var inst = $.jstree.reference(data.reference);
										var obj = inst.get_node(data.reference);
										var id = $(obj).attr('id');
										clearEmpOrOrmemberMenu(id);
									};
									return tmp;
								} else if (id.indexOf('ST-') != -1) {
									var tmp = {
										'copy' : {},
										'paste' : {},
										'mpaste' : {},
										'clear' : {}
									};
									tmp.copy.label = "复制当前菜单";
									tmp.copy.action = function(data) {
										var inst = $.jstree
												.reference(data.reference);
										var obj = inst.get_node(data.reference);
										var id = $(obj).attr('id');
										$("#srcOrmemberId").val(id);
									};
									tmp.paste.label = "粘贴覆盖权限菜单";
									tmp.paste.action = function(data) {
										var inst = $.jstree.reference(data.reference);
										var obj = inst.get_node(data.reference);
										var destId = $(obj).attr('id');
										var srcId = $("#srcOrmemberId").val();
										if (srcId != '0'
												&& srcId.indexOf('ST-') != -1) {
											copyOrmemberMenuToNext(srcId,destId,false);
										} else {
											toastr.warning('', '请先选择要复制的源岗位');
										}

									};
									tmp.mpaste.label = "粘贴合并权限菜单";
									tmp.mpaste.action = function(data) {
										var inst = $.jstree.reference(data.reference);
										var obj = inst.get_node(data.reference);
										var destId = $(obj).attr('id');
										var srcId = $("#srcOrmemberId").val();
										if (srcId != '0'&& srcId.indexOf('ST-') != -1) {
											copyOrmemberMenuToNext(srcId,destId,true);
										} else {
											toastr.warning('', '请先选择要复制的源岗位');
										}

									};
									tmp.clear.label = "清空权限菜单";
									tmp.clear.action = function(data) {
										var inst = $.jstree
												.reference(data.reference);
										var obj = inst.get_node(data.reference);
										var id = $(obj).attr('id');
										clearEmpOrOrmemberMenu(id);
									};
									return tmp;
								}
								return {};
							}
						},
						'plugins' : [ 'wholerow', "contextmenu" ]

					}).on("changed.jstree", function(e, data) {
				var doms = $organizationList.jstree("get_selected", true);
				if (doms.length > 1) {
					toastr.warning('', '只能选择一个节点');
				}
				$.each(doms, function(i, dom) {
					$('#authId').val($(dom).attr('id'));
				});
				ajaxRefreshMenuList();
			});
});

var flag = true;
// 更新权限菜单
function ajaxRefreshMenuList() {
	var authId = $('#authId').val();
	$.ajax({
		type : 'POST',
		dataType : "json",
		async : false,
		url : MENU_AUTH_PATH_PREFIX+'ajaxMenuList.do',
		data : {
			'authId' : authId
		},
		success : function(data) {
			// 按钮菜单清空
			$("#buttonList").html('');
			// 清空菜单
			$menuList.remove();
			$('<div id="menuList"></div>').appendTo($("#menuList-ibox"));
			$menuList = $("#menuList");
			$menuList.jstree({
				'core' : {
					data : data,
					"themes" : {
						"name" : "default",
						"dots" : false,
						"icons" : false
					}
				},
				'plugins' : [ 'checkbox', 'wholerow' ]

			}).on("changed.jstree", function(e, data) {
				console.info(data.selected.length)
				var doms = $menuList.jstree("get_selected", true);
				var menuIds = '';
				$.each(doms, function(i, dom) {
					if ($menuList.jstree("is_leaf", dom)) {
						menuIds += $(dom).attr('id') + ',';
					}
				});
				refreshButtonMenu(authId, menuIds);
			});
		},
		error : function(e) {
			console.log(e);
		}
	});
}

function refreshButtonMenu(authId, menuIds) {
	$.ajax({
		type : 'POST',
		dataType : "json",
		async : false,
		url : MENU_AUTH_PATH_PREFIX+'ajaxDisplayButtonMenu.do',
		data : {
			'menuIds' : menuIds,
			'authId' : authId
		},
		success : function(data) {
			$buttonList.remove();
			$('<div id="buttonList"></div>').appendTo($("#buttonList-ibox"));
			$buttonList = $("#buttonList");
			if (data && data.length > 0) {
				// 清空按钮菜单
				console.info("data.result:" + data);
				$buttonList.jstree({
					'core' : {
						data : data,
						"themes" : {
							"name" : "default",
							"dots" : false,
							"icons" : false
						}
					},
					'plugins' : [ 'checkbox', 'wholerow' ]

				});
			}
		},
		error : function(e) {
			toastr.warning('', '保存出错，请检查重新操作');
		}
	});
}

// 复制岗位权限给用户
function copyOrmemberMenuForEmp(empId, stId,merge) {
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : MENU_AUTH_PATH_PREFIX+'/copyOrmemberMenuForEmp.do',
		data : {
			'empId' : empId,
			'stId' : stId,
			'merge' : merge
		},
		success : function(data) {
			if ('success' == data.result) {
				ajaxRefreshMenuList();
				toastr.success('', '操作成功');
			}
		},
		error : function(e) {
			toastr.warning('', '保存出错，请检查重新操作');
		}
	});
}
// 清空权限菜单
function clearEmpOrOrmemberMenu(authId) {
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : MENU_AUTH_PATH_PREFIX+'clearMenu.do',
		data : {
			'authId' : authId
		},
		success : function(data) {
			console.log(data);
			if ('success' == data.result) {
				ajaxRefreshMenuList();
				toastr.success('', '操作成功');
			}
		},
		error : function(e) {
			toastr.warning('', '保存出错，请检查重新操作');
		}
	});
}

// 复制一个岗位权限菜单给另一个岗位
function copyOrmemberMenuToNext(srcId, destId,merge) {
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : MENU_AUTH_PATH_PREFIX+'copyOrmemberMenuToNext.do',
		data : {
			'srcId' : srcId,
			'destId' : destId,
			'merge'  : merge
		},
		success : function(data) {
			console.log(data);
			if ('success' == data.result) {
				ajaxRefreshMenuList();
				toastr.success('', '操作成功');
			}
		},
		error : function(e) {
			toastr.warning('', '保存出错，请检查重新操作');
		}
	});
}

// 保存按钮进行保存选中的菜单
function ajaxSaveMenuAuth() {
	var event = event || window.event;
	var target = event.target || event.srcElement;
	var targetx = $(target);
	targetx.button('loading');

	var authId = $("#authId").val();
	var selected = $menuList.jstree("get_selected", true);
	var menuIds = '';
	// 保存左边菜单ID
	$.each(selected, function(i, dom) {
		if ($menuList.jstree('is_leaf', dom)) {
			menuIds += $(dom).attr('id') + ',';
		}
	});
	// 保存右边按钮菜单ID
	var btnSelected = $buttonList.jstree("get_selected", true);
	$.each(btnSelected, function(i, dom) {
		if ($buttonList.jstree('is_leaf', dom)) {
			menuIds += $(dom).attr('id') + ',';
		}
	});

	console.info(menuIds)
	if (authId.length == 0) {
		toastr.warning('', '请选择岗位或人员');
		$(targetx).button('reset');
		return;
	}
	if (menuIds.length == 0) {
		toastr.warning('', '请选择菜单');
		$(targetx).button('reset');
		return;
	}

	$.ajax({
		type : 'POST',
		dataType : "json",
		url : MENU_AUTH_PATH_PREFIX+'ajaxSaveMenuAuth.do',
		data : {
			'authId' : authId,
			'menuIds' : menuIds
		},
		success : function(data) {
			if ('success' == data.result) {
				ajaxRefreshMenuList();
				toastr.success('', '操作成功');
			}
			$(targetx).button('reset');
		},
		error : function(e) {
			toastr.warning('', '保存出错，请检查重新操作');
			$(targetx).button('reset');
		}
	});

}