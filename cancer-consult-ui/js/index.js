$(function(){
    $("#imgCancer").change(function(){
        console.log();
    });

    $("#btnEnviar").click(function(){
        var data = new FormData();
        data.append("image", $("#imgCancer").prop("files")[0]);
        $("#resposta").html("");
        jQuery.ajax({
            url: 'http://localhost:8080/api/consult/',
            data: data,
            dataType: 'multpart/form-data',
            cache: false,
            contentType: false,
            processData: false,
            method: 'POST',
            type: 'POST', // For jQuery < 1.9
            success: function(resp){
                alert(resp);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                $("#resposta").html(XMLHttpRequest.responseText);
            }
        });
    });
});