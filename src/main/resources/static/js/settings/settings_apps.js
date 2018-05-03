function updateApps(){
    if($("#appName").val() != null && $("#appName").val() != "" &&
       $("#appDivision").val() != null && $("#appDivision").val() != null){
       $('#imageUploadForm').attr("action", "/settings/apps/updateApps").submit();
    }else{
        alert("데이터를 입력해주세요.");
    }

    cancelApps();
}

function cancelApps(){
    $("#appDivision").val('');
    $("#appName").val('');
    $("#imageFileName").val('');
    $("#appId").val('');
    $('#imageFile').val('');
}

function editAppsData(thumbnailName, appName, appDivision, appId){
    $("#appDivision").val(appDivision);
    $("#appName").val(appName);
    $("#imageFileName").val(thumbnailName);
    $("#appId").val(appId);
}

function deleteApps(id){
    var result = confirm("삭제하시겠습니까?");

    if(result){
        var url = "/settings/apps/deleteApps";
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
                    alert("삭제할수 없습니다.");
                }else{
                    alert("삭제가 완료되었습니다.");

                    selectAppsList();
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

function selectAppsList(){
    var url = "/settings/apps/selectApps";

    $.ajax({
        url : url,
        type : 'POST',
        success : function(data) {
            $('#datatable-apps').empty().html(data);
        },
        error : function(x, e) {
            alert(e);
        }
    });
}