//----------------------------------------course.html课程校验区域------------------------------------
$(function(){
    //----------------新建课程验证非空唯一性
    $("#dJFrom").validate({
        rules : {
            courseTypeId : {
                required:true,
            },
            subjectId:{
                required:true
            },
            courseName:{
                required:true
            },
            errorPlacement: function(error, element) {
                if ( element.is(":radio") ){
                    error.appendTo( element.next() );
                }else if ( element.is(":checkbox") ){
                    error.appendTo ( element.next() );
                } else{
                    error.appendTo( element.next() );
                }

            },
            success : function( lable ){
                lable.ligerHideTip();
                lable.remove();
            }

        }
    });
    //验证课程类别序号唯一性
    jQuery.validator.addMethod('checkName', function (value, element) {
        var layOrder_bak = $("#layOrder_bak").val();
        if(layOrder_bak==value){
            return true;
        }
        var msgcode=null;
        $.ajax({
            url: basePath + "/jwcentercourse/checkLayOrder",
            type: "get",
            dataType: 'json',
            async:false,
            contentType:"application/json;charset=UTF-8",
            data: {"layorder":value},
            success: function(result){
                msgcode= result.code;
            }
        })
        if(msgcode==1001){
            $(element).data('error-msg','该序号不能重复！');
            return false;
        }
        return true;
    }, function(params, element) {
        return $(element).data('error-msg');
    });




    //----------------新建课程类别验证非空唯一性
    $("#courstypeform").validate({
        rules : {
            layOrder : {
                required:true,
                digits:true,
                checkName:true
            },
            courseTypeName:{
                required:true
            },
            messages: {
                layOrder: {
                    required: "排序不能为空",
                    digits: "必须输入数字"
                },
                courseTypeName:"类别名不能为空"
            },
            errorPlacement: function(error, element) {
                if ( element.is(":radio") ){
                    error.appendTo( element.parent().next().next() );
                }else if ( element.is(":checkbox") ){
                    error.appendTo ( element.next() );
                } else{
                    error.appendTo( element.parent().next() );
                }

            },
            success : function( lable ){
                lable.ligerHideTip();
                lable.remove();
            }

        }
    });
    //验证课程类别序号唯一性
    jQuery.validator.addMethod('checkName', function (value, element) {
        var layOrder_bak = $("#layOrder_bak").val();
        if(layOrder_bak==value){
            return true;
        }
        var msgcode=null;
        $.ajax({
            url: basePath + "/jwcentercourse/checkLayOrder",
            type: "get",
            dataType: 'json',
            async:false,
            contentType:"application/json;charset=UTF-8",
            data: {"layorder":value},
            success: function(result){
                msgcode= result.code;
            }
        })
        if(msgcode==1001){
            $(element).data('error-msg','该序号不能重复！');
            return false;
        }
        return true;
    }, function(params, element) {
        return $(element).data('error-msg');
    });
    //课程类别添加框取消时间
    $('#modal-table1').on('hide.bs.modal', function () {
        $('#modal-table1').find("input").each(function(){
                $(this).val("");
        });
        $("#courtype_new").text("新建课程类别");
    });



})




































































































