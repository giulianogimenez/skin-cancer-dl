$(function(){
    $("#imgCancer").change(function(){
        console.log();
    });

    $("#btnEnviar").click(function(){
        $("#btnEnviar").prop("disabled", true);
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
                $.each(JSON.parse(XMLHttpRequest.responseText), function(i, item){
                    console.log(item);
                    $("#resposta").html($("#resposta").html() + item.label + ": <strong>" + item.probability + "%</strong><br />");
                    $("#imagePreview").prop("hidden", true);
                });
                $("#btnEnviar").prop("disabled", false);
            }
        });
    });

    $("#btnSelecionarArquivo").click(function() {
        $("#imgCancer").click();
    });

    $("#imgCancer").change(function() {
        readURL(this);
    });

    $("#btnCancelar").click(function() {
        $("#btnSelecionarArquivo").prop("hidden", false);
        $("#imgCancer").files = null;
        $('#imgToUpload').removeAttr('src');
        $("#spanName").html('');
        $(this).prop("hidden", true);
        $("#btnEnviar").prop("hidden", true);
    });


});

function readURL(input) {
  if (input.files && input.files[0]) {
    var reader = new FileReader();
    $("#spanName").html(input.files[0].name);
    $("#btnSelecionarArquivo").prop("hidden", true);
    reader.onload = function(e) {
      $('#imgToUpload').attr('src', e.target.result);
    }
    $("#btnCancelar").prop("hidden", false);
    $("#btnEnviar").prop("hidden", false);

    reader.readAsDataURL(input.files[0]);
  }
}


