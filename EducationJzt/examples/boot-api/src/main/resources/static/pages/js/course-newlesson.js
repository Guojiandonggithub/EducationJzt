
$(function(){

});
//收费模式选择 按期 按课时
function chargingModeClick(type){
    var preid = $("#preid").val();
    $("#dingjiaiform tr:not(:first)").remove();
    //获取校区类型 0全部 1指定
    var openingType = $('input:radio[name="openingType"]:checked').val();
    if(openingType=='0'){
        dingJiaBiaoZunAll('',type);
    }else{
        $("input[name='openingTypes']:checked").each(function (j) {
            var organIds = $(this).val();
            var  organName = $(this).parent().find("span").text();
            dingJiaBiaoZunZD('',organName,'',organIds,type,preid);
        });
    }
}
//开课校区
function schoolType(type,courseId){
    $("#dingjiaiform tr:not(:first)").remove();
    //收费模式 按课时
    var chargingMode = $('input[name="chargingMode"]:checked').val();
    //全部校区
    if(type=='0'){
        //选中所有校区
        $('#zhidingSchool input:checkbox').each(function() {
            $(this).prop('checked', true);
        });
        //不显示
        $("#zhidingSchool span").hide();
        dingJiaBiaoZunAll(courseId,chargingMode)

    //指定校区
    }else{
        $('#zhidingSchool input:checkbox').each(function() {
            $(this).prop('checked',false);
        });
        $("#zhidingSchool span").show();
    }
}

//校区复选框点击
function appointSchool(i,schoolName,courseId,organIds,shi){
    if ( shi.checked == true){
        var chargingMode = $('input[name="chargingMode"]:checked').val();
        dingJiaBiaoZunZD(i,schoolName,courseId,organIds,chargingMode,'');
    }else{
        $("#delete_"+i).remove();
    }
}
//校区复选框 i(list坐标) schoolName（校区名称）courseId（课程id） organIds(校区id)
//chargingMode （收费模式）
function dingJiaBiaoZunZD(i,schoolName,courseId,organIds,chargingMode,id){
    var html = [];
    html.push('<tr id="delete_'+i+'">                                                                          ');
    html.push('	<td>'+schoolName+'</td>                                                            ');
    html.push('	<input type="hidden" id="XbCoursePresetcourseId" name="XbCoursePresetcourseId" value=""/>                              ');
    html.push('	<input type="hidden" id="XbCoursePresetorganIds" name="XbCoursePresetorganIds" value="'+organIds+'"/>                              ');
    html.push('	<td>                                                                         ');
    html.push('		<div class="extend-list">                                                ');
    html.push('			<div class="form-group">                                             ');
    if(chargingMode=='0'){
        html.push('				<label>按课时</label>                                  ');
        html.push('				<div class="numCon">                                             ');
        html.push('					<input id="XbCoursePresetperiodNum"  name="XbCoursePresetperiodNum" type="text" value="0"/>');
        html.push('				</div>                                                           ');
        html.push('				<span class="txt">课时</span>                                    ');
        html.push('				<span class="txt">=</span>                                       ');
        html.push('				<div class="numCon">                                             ');
        html.push('					<input id="XbCoursePresetmoney" name="XbCoursePresetmoney" type="text" value="0"/>   ');
        html.push('				</div>                                                           ');
        html.push('				<input type="hidden" id="preid" name="id" value="'+id+'" />    ');
        html.push('				<span class="txt">元</span>                                      ');
        html.push('				<span class="glyphicon glyphicon-plus-sign" onclick="appointSchooladd(\''+schoolName+'\',\'\',\''+organIds+'\',this)"></span>                                  ');
       // html.push('				<span class="glyphicon glyphicon-remove-sign" onclick="appointSchoolremove(this)"></span>            ');
    }else if(chargingMode=='2'){
        html.push('				<label>按期数</label>                                  ');
        html.push('				<div class="numCon">                                             ');
        html.push('					<input id="XbCoursePresetmoney" name="XbCoursePresetmoney" type="text" value="0"/>   ');
        html.push('				</div>                                                           ');
        html.push('				<span class="txt">元/期</span>                                    ');
        html.push('				<span class="txt">包含</span>                                       ');
        html.push('				<div class="numCon">                                             ');
        html.push('					<input id="XbCoursePresetperiodNum"  name="XbCoursePresetperiodNum" type="text" value="0"/>');
        html.push('				</div>                                                           ');
        html.push('				<input type="hidden" id="preid" name="id" value="'+id+'" />    ');
        html.push('				<span class="txt">课时</span>                                      ');
        html.push('				<span class="glyphicon glyphicon-plus-sign" onclick="appointSchooladd(\''+schoolName+'\',\'\',\''+organIds+'\',this)"></span>  ');
       // html.push('				<span class="glyphicon glyphicon-remove-sign" onclick="appointSchoolremove(this)"></span>            ');
    }
    html.push('			</div>                                                               ');
    html.push('		</div>                                                                   ');
    html.push('	</td>                                                                        ');
    html.push('</tr>                                                                         ');
    $("#dingjiaiform").append(html.join(""));
}
//开课校区 i(list坐标) schoolName（校区名称）courseId（课程id） organIds(校区id)
//chargingMode （收费模式）
function dingJiaBiaoZunAll(courseId,chargingMode){
    var html = [];
    html.push('<tr id="delete_0">                                                                          ');
    html.push('	<td>全校</td>                                                            ');
    html.push('	<input type="hidden" id="XbCoursePresetcourseId" name="XbCoursePresetcourseId" value=""/>                              ');
    html.push('	<input type="hidden" id="XbCoursePresetorganIds" name="XbCoursePresetorganIds" value=""/>                              ');
    html.push('	<td>                                                                         ');
    html.push('		<div class="extend-list">                                                ');
    html.push('			<div class="form-group">                                             ');
    if(chargingMode=='0'){
        html.push('				<label>按课时</label>                                  ');
        html.push('				<div class="numCon">                                             ');
        html.push('					<input id="XbCoursePresetperiodNum"  name="XbCoursePresetperiodNum" type="text" value="0"/>');
        html.push('				</div>                                                           ');
        html.push('				<span class="txt">课时</span>                                    ');
        html.push('				<span class="txt">=</span>                                       ');
        html.push('				<div class="numCon">                                             ');
        html.push('					<input id="XbCoursePresetmoney" name="XbCoursePresetmoney" type="text" value="0"/>   ');
        html.push('				</div>                                                           ');
        html.push('				<span class="txt">元</span>                                      ');
    }else if(chargingMode=='2'){
        html.push('				<label>按期数</label>                                  ');
        html.push('				<div class="numCon">                                             ');
        html.push('					<input id="XbCoursePresetmoney" name="XbCoursePresetmoney" type="text" value="0"/>   ');
        html.push('				</div>                                                           ');
        html.push('				<span class="txt">元/期</span>                                    ');
        html.push('				<span class="txt">包含</span>                                       ');
        html.push('				<div class="numCon">                                             ');
        html.push('					<input id="XbCoursePresetperiodNum"  name="XbCoursePresetperiodNum" type="text" value="0"/>');
        html.push('				</div>                                                           ');
        html.push('				<span class="txt">课时</span>                                      ');
    }
    html.push('				<span class="glyphicon glyphicon-plus-sign" onclick="dingJiaBiaoZunAllAdd(\''+courseId+'\',\''+chargingMode+'\',this)"></span>  ');
    /*html.push('				<span class="glyphicon glyphicon-remove-sign" ></span>            ');*/
    html.push('			</div>                                                               ');
    html.push('		</div>                                                                   ');
    html.push('	</td>                                                                        ');
    html.push('</tr>                                                                         ');
    $("#dingjiaiform").append(html.join(""))
}

//开课校区 i(list坐标) schoolName（校区名称）courseId（课程id） organIds(校区id)
//chargingMode （收费模式）
function dingJiaBiaoZunAllAdd(courseId,chargingMode,shi){
    var html = [];
    html.push('<tr id="delete_0">                                                                          ');
    html.push('	<td>全校</td>                                                            ');
    html.push('	<input type="hidden" id="XbCoursePresetcourseId" name="XbCoursePresetcourseId" value=""/>                              ');
    html.push('	<input type="hidden" id="XbCoursePresetorganIds" name="XbCoursePresetorganIds" value=""/>                              ');
    html.push('	<td>                                                                         ');
    html.push('		<div class="extend-list">                                                ');
    html.push('			<div class="form-group">                                             ');
    if(chargingMode=='0'){
        html.push('				<label>按课时</label>                                  ');
        html.push('				<div class="numCon">                                             ');
        html.push('					<input id="XbCoursePresetperiodNum"  name="XbCoursePresetperiodNum" type="text" value="0"/>');
        html.push('				</div>                                                           ');
        html.push('				<span class="txt">课时</span>                                    ');
        html.push('				<span class="txt">=</span>                                       ');
        html.push('				<div class="numCon">                                             ');
        html.push('					<input id="XbCoursePresetmoney" name="XbCoursePresetmoney" type="text" value="0"/>   ');
        html.push('				</div>                                                           ');
        html.push('				<span class="txt">元</span>                                      ');
        html.push('				<span class="glyphicon glyphicon-remove-sign" onclick="appointSchoolremove(this)"></span>            ');
    }else if(chargingMode=='2'){
        html.push('				<label>按期数</label>                                  ');
        html.push('				<div class="numCon">                                             ');
        html.push('					<input id="XbCoursePresetmoney" name="XbCoursePresetmoney" type="text" value="0"/>   ');
        html.push('				</div>                                                           ');
        html.push('				<span class="txt">元/期</span>                                    ');
        html.push('				<span class="txt">包含</span>                                       ');
        html.push('				<div class="numCon">                                             ');
        html.push('					<input id="XbCoursePresetperiodNum"  name="XbCoursePresetperiodNum" type="text" value="0"/>');
        html.push('				</div>                                                           ');
        html.push('				<span class="txt">课时</span>                                      ');
        html.push('				<span class="glyphicon glyphicon-remove-sign" onclick="appointSchoolremove(this)"></span>            ');
    }
    html.push('			</div>                                                               ');
    html.push('		</div>                                                                   ');
    html.push('	</td>                                                                        ');
    html.push('</tr>                                                                         ');
    $(shi).parent().parent().parent().parent().after(html.join(""));
}
function appointSchoolremove(shi){
    $(shi).parent().parent().parent().parent().remove();
}
//校区增加
function appointSchooladd(schoolName,courseId,organIds,shi){
    var chargingMode = $('input[name="chargingMode"]:checked').val();
    dingJiaBiaoZunZDadd(schoolName,courseId,organIds,chargingMode,shi,'');

}
//校区复选框 i(list坐标) schoolName（校区名称）courseId（课程id） organIds(校区id)
//chargingMode （收费模式）
function dingJiaBiaoZunZDadd(schoolName,courseId,organIds,chargingMode,shi,id){
    var html = [];
    html.push('<tr id="delete_">                                                                          ');
    html.push('	<td>'+schoolName+'</td>                                                            ');
    html.push('	<input type="hidden" id="XbCoursePresetcourseId" name="XbCoursePresetcourseId" value=""/>                              ');
    html.push('	<input type="hidden" id="XbCoursePresetorganIds" name="XbCoursePresetorganIds" value="'+organIds+'"/>                              ');
    html.push('	<td>                                                                         ');
    html.push('		<div class="extend-list">                                                ');
    html.push('			<div class="form-group">                                             ');
    if(chargingMode=='0'){
        html.push('				<label>按课时</label>                                  ');
        html.push('				<div class="numCon">                                             ');
        html.push('					<input id="XbCoursePresetperiodNum"  name="XbCoursePresetperiodNum" type="text" value="0"/>');
        html.push('				</div>                                                           ');
        html.push('				<span class="txt">课时</span>                                    ');
        html.push('				<span class="txt">=</span>                                       ');
        html.push('				<div class="numCon">                                             ');
        html.push('					<input id="XbCoursePresetmoney" name="XbCoursePresetmoney" type="text" value="0"/>   ');
        html.push('				</div>                                                           ');
        html.push('				<input type="hidden" id="preid" name="id" value="'+id+'" />    ');
        html.push('				<span class="txt">元</span>                                      ');
        html.push('				<span class="glyphicon glyphicon-remove-sign" onclick="appointSchoolremove(this)"></span>            ');
    }else if(chargingMode=='2'){
        html.push('				<label>按期数</label>                                  ');
        html.push('				<div class="numCon">                                             ');
        html.push('					<input id="XbCoursePresetmoney" name="XbCoursePresetmoney" type="text" value="0"/>   ');
        html.push('				</div>                                                           ');
        html.push('				<span class="txt">元/期</span>                                    ');
        html.push('				<span class="txt">包含</span>                                       ');
        html.push('				<div class="numCon">                                             ');
        html.push('					<input id="XbCoursePresetperiodNum"  name="XbCoursePresetperiodNum" type="text" value="0"/>');
        html.push('				</div>                                                           ');
        html.push('				<input type="hidden" id="preid" name="id" value="'+id+'" />    ');
        html.push('				<span class="txt">课时</span>                                      ');
        html.push('				<span class="glyphicon glyphicon-remove-sign" onclick="appointSchoolremove(this)"></span>            ');
    }
    html.push('			</div>                                                               ');
    html.push('		</div>                                                                   ');
    html.push('	</td>                                                                        ');
    html.push('</tr>                                                                         ');
    //$("#dingjiaiform").append(html.join(""));
    $(shi).parent().parent().parent().parent().after(html.join(""));
}