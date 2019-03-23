

$(function(){
    comboboxSysorg("#sysorgId_combobox","#courseId_combobox","#classId_combobox");
    comboboxSysorg("#sysorgId_combobox_new","#courseId_combobox_new","#classId_combobox_new");
});

function comboboxSysorg(a,b,c){
    var sysorgId_combobox = $(a);
    sysorgId_combobox.combobox({
        url:basePath + "/jw_center_arranging_course/findSysOragList",
        valueField:'id',
        addable:false,
        editable:false,
        required:true,
        textField:'organName',
        onLoadSuccess:function(){
            var orgid = $(a).combobox('getValue');
            $(c).combobox({
                url:basePath + "/jw_center_arranging_course/findClassListByorgid?orgid="+orgid,
                valueField:'id',
                addable:false,
                editable:false,
                required:true,
                textField:'className'

            });
            $(b).combobox({
                url:basePath + "/jw_center_arranging_course/findCourseByorgid?orgid="+orgid,
                valueField:'id',
                addable:false,
                editable:false,
                required:true,
                textField:'employeeName',
                onLoadSuccess:function(){
                    var teacherid = $(b).combobox('getValue');
                    $(c).combobox({
                        url:basePath + "/jw_center_arranging_course/findClassListByorgid?teacherid="+teacherid,
                        valueField:'id',
                        addable:false,
                        editable:false,
                        required:true,
                        textField:'className'

                    });
                    if(teacherid=="0"){
                        $("input[name=c]").val("0");
                    }
                },
                onChange:function(){
                    var teacherid = $(b).combobox('getValue');
                    $(c).combobox({
                        url:basePath + "/jw_center_arranging_course/findClassListByorgid?teacherid="+teacherid,
                        valueField:'id',
                        addable:false,
                        editable:false,
                        required:true,
                        textField:'className'

                    });
                    $("input[name=c]").val("0");
                }

            });
        },
        onChange:function(){
            var orgid = $(a).combobox('getValue');
            $(c).combobox({
                url:basePath + "/jw_center_arranging_course/findClassListByorgid?orgid="+orgid,
                valueField:'id',
                addable:false,
                editable:false,
                required:true,
                textField:'className'

            });
            $(b).combobox({
                url:basePath + "/jw_center_arranging_course/findCourseByorgid?orgid="+orgid,
                valueField:'id',
                addable:false,
                editable:false,
                required:true,
                textField:'employeeName',
                onChange:function(){
                    var teacherid = $(b).combobox('getValue');
                    $(c).combobox({
                        url:basePath + "/jw_center_arranging_course/findClassListByorgid?teacherid="+teacherid,
                        valueField:'id',
                        addable:false,
                        editable:false,
                        required:true,
                        textField:'className'

                    });
                    $("input[name=c]").val("0");
                }
            });
            $("input[name=b]").val("0");
            $("input[name=c]").val("0");
        },
    });
}
