function cancelMenu(){
    $('input:checkbox[name="addMenuId"]').each(function() {
         this.checked = false;
    });

    $('#sort').val('');
    $('#menuNm').val('');
    $('#menuComment').val('');
    $('#menuGrp').val('');
    $('#menuUrl').val('');
    $('#menuId').val('');
}

function updateMenu(){
    if($('#menuNm').val() != null && $('#menuNm').val() != "" &&
        $('#menuGrp').val() != null && $('#menuGrp').val()  != "" &&
        $('#menuUrl').val() != null && $('#menuUrl').val() != ""){

        var activeYn;
        if($('input:checkbox[name="addMenuId"]').is(":checked") ==  true){
            activeYn = 'Y';
        }else{
            activeYn = 'N';
        }

        var menuId = $('#menuId').val();
        if(typeof menuId == "undefined" ){
            menuId = "0";
        }

        var url = "/settings/menu/updateMenu";
        var param = {
            sort : $('#sort').val(),
            menuNm : $('#menuNm').val(),
            menuComment : $('#menuComment').val(),
            menuGrp : $('#menuGrp').val(),
            menuUrl : $('#menuUrl').val(),
            activeYn : activeYn,
            id : menuId
        };

        $.ajax({
            url : url,
            data : param,
            type : 'POST',
            success : function(data) {
                alert("입력/수정이 완료되었습니다.");
                cancelMenu();

                $('#datatable-menu').empty().html(data);
            },
            error : function(x, e) {
                alert(e);
            }
        });
    }else{
        alert("데이터를 입력해주세요.");
    }
}

function selectMenu(id, sort, menuNm, menuComment, menuGrp, url, activeYn){
	cancelMenu();

    $('#menuId').val(id);
	$('#sort').val(sort);
	$('#menuNm').val(menuNm);
	$('#menuComment').val(menuComment);
	$('#menuGrp').val(menuGrp);
	$('#menuUrl').val(url);

	if(activeYn == 'true'){
		$('input:checkbox[name="addMenuId"]').each(function() {
			 this.checked = true;
		});
	}else{
		$('input:checkbox[name="addMenuId"]').each(function() {
			 this.checked = false;
		});
	}
}

function deleteMenu(id){

    var result = confirm("삭제하시겠습니까?");

    if(result){
        var url = "/settings/menu/deleteMenu";
        var param = {
            id : id
        };

        $.ajax({
            url : url,
            data : param,
            type : 'POST',
            dataType : 'json',
            success : function(data) {
                if(data.code == 'fail'){
                    alert("사용중인 메뉴라 삭제할수 없습니다.");
                }else{
                    alert("삭제가 완료되었습니다.");

                    selectMenuList();
                }
            },
            error : function(x, e) {
                alert(e);
            }
        });
    }else{
        return;
    }
}

function selectMenuList(){
    var url = "/settings/menu/selectMenu";

    $.ajax({
        url : url,
        type : 'POST',
        success : function(data) {
            $('#datatable-menu').empty().html(data);
        },
        error : function(x, e) {
            alert(e);
        }
    });
}