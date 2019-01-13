

$(function(){
    comboboxSysorg();
});

function comboboxSysorg(){
    var sysorgId_combobox = $("#sysorgId_combobox");
    sysorgId_combobox.combobox({
        url:basePath + "/jw_center_arranging_course/findSysOragList",
        valueField:'id',
        addable:false,
        editable:false,
        required:true,
        textField:'organName',
        onLoadSuccess:function(){
            var orgid = $('#sysorgId_combobox').combobox('getValue');
            $('#classId_combobox').combobox({
                url:basePath + "/jw_center_arranging_course/findClassListByorgid?orgid="+orgid,
                valueField:'id',
                addable:false,
                editable:false,
                required:true,
                textField:'className'

            });
            $('#courseId_combobox').combobox({
                url:basePath + "/jw_center_arranging_course/findCourseByorgid?orgid="+orgid,
                valueField:'id',
                addable:false,
                editable:false,
                required:true,
                textField:'courseName'

            });
        },
        onChange:function(){
            var orgid = $('#sysorgId_combobox').combobox('getValue');
            $('#classId_combobox').combobox({
                url:basePath + "/jw_center_arranging_course/findClassListByorgid?orgid="+orgid,
                valueField:'id',
                addable:false,
                editable:false,
                required:true,
                textField:'className'

            });
            $('#courseId_combobox').combobox({
                url:basePath + "/jw_center_arranging_course/findCourseByorgid?orgid="+orgid,
                valueField:'id',
                addable:false,
                editable:false,
                required:true,
                textField:'courseName'

            });
        },
    });
}