
$(function(){
    var type = $('input[name="openingType"]:checked').val();
    var courseId = $("#courseId").val();
    //schoolType(type,courseId)
});

function schoolType(type,courseId){
    $("#dingjiaiform tr:not(:first)").remove();
    //收费模式 按课时
    var chargingMode = $('input[name="chargingMode"]:checked').val();
    var charging ="按课时";
    if(chargingMode=='0'){charging="按课时";}else
    if(chargingMode=='2'){ charging="按期";}else
    if(chargingMode=='1'){charging="按时间";}
    //全部校区
    if(type=='0'){
        //选中所有校区
        $('#zhidingSchool input:checkbox').each(function() {
            $(this).prop('checked', true);
        });
        //不显示
        $("#zhidingSchool span").hide();
        dingJiaBiaoZunAll(courseId,charging)

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
        var charging ="按课时";
        if(chargingMode=='0'){
            charging="按课时";
        }else if(chargingMode=='2'){
            charging="按期";
        }else if(chargingMode=='1'){
            charging="按时间";
        }
        dingJiaBiaoZunZD(i,schoolName,courseId,organIds,charging);
    }else{
        $("#delete_"+i).remove();
    }
}
//定价标准 i(list坐标) schoolName（校区名称）courseId（课程id） organIds(校区id)
//chargingMode （收费模式）
function dingJiaBiaoZunZD(i,schoolName,courseId,organIds,chargingMode){
    var html = [];
    html.push('<tr id="delete_'+i+'">                                                                          ');
    html.push('	<td>'+schoolName+'</td>                                                            ');
    html.push('	<input type="hidden" id="XbCoursePresetcourseId" name="XbCoursePreset['+i+'].courseId" value="'+courseId+'"/>                              ');
    html.push('	<input type="hidden" id="XbCoursePresetorganIds" name="XbCoursePreset['+i+'].organIds" value="'+organIds+'"/>                              ');
    html.push('	<td>                                                                         ');
    html.push('		<div class="extend-list">                                                ');
    html.push('			<div class="form-group">                                             ');
    html.push('				<label>'+chargingMode+'</label>                                            ');
    html.push('				<div class="numCon">                                             ');
    html.push('					<input id="XbCoursePresetperiodNum"  name="XbCoursePreset['+i+'].periodNum" type="text" value="0"/>');
    html.push('				</div>                                                           ');
    html.push('				<span class="txt">课时</span>                                    ');
    html.push('				<span class="txt">=</span>                                       ');
    html.push('				<div class="numCon">                                             ');
    html.push('					<input id="XbCoursePresetmoney" name="XbCoursePreset['+i+'].money" type="text" value="0"/>   ');
    html.push('				</div>                                                           ');
    html.push('				<span class="txt">元</span>                                      ');
    html.push('				<span class="glyphicon glyphicon-remove-sign"></span>            ');
    html.push('			</div>                                                               ');
    html.push('		</div>                                                                   ');
    html.push('	</td>                                                                        ');
    html.push('</tr>                                                                         ');
    $("#dingjiaiform").append(html.join(""))
}
//定价标准 i(list坐标) schoolName（校区名称）courseId（课程id） organIds(校区id)
//chargingMode （收费模式）
function dingJiaBiaoZunZD_BAK(i,schoolName,courseId,organIds,chargingMode){
    var html = [];
    html.push('<tr id="delete_'+i+'">                                                                          ');
    html.push('	<td>'+schoolName+'</td>                                                            ');
    html.push('	<input type="hidden" name="XbCoursePreset['+i+'].courseId" value="'+courseId+'"/>                              ');
    html.push('	<input type="hidden" name="XbCoursePreset['+i+'].organIds" value="'+organIds+'"/>                              ');
    html.push('	<td>                                                                         ');
    html.push('		<div class="extend-list">                                                ');
    html.push('			<div class="form-group">                                             ');
    html.push('				<label>'+chargingMode+'</label>                                            ');
    html.push('				<div class="numCon">                                             ');
    html.push('					<input id="text_box" name="XbCoursePreset['+i+'].periodNum" type="text" value="0"/>');
    html.push('				</div>                                                           ');
    html.push('				<span class="txt">课时</span>                                    ');
    html.push('				<span class="txt">=</span>                                       ');
    html.push('				<div class="numCon">                                             ');
    html.push('					<input id="text_box1" name="XbCoursePreset['+i+'].money" type="text" value="0"/>   ');
    html.push('				</div>                                                           ');
    html.push('				<span class="txt">元</span>                                      ');
    html.push('				<span class="glyphicon glyphicon-remove-sign"></span>            ');
    html.push('			</div>                                                               ');
    html.push('		</div>                                                                   ');
    html.push('	</td>                                                                        ');
    html.push('</tr>                                                                         ');
    $("#dingjiaiform").append(html.join(""))
}
//定价标准 i(list坐标) schoolName（校区名称）courseId（课程id） organIds(校区id)
//chargingMode （收费模式）
function dingJiaBiaoZunAll(courseId,chargingMode){
    var html = [];
    html.push('<tr id="delete_0">                                                                          ');
    html.push('	<td>全校</td>                                                            ');
    html.push('	<input type="hidden" id="XbCoursePresetcourseId" name="XbCoursePresetcourseId" value="'+courseId+'"/>                              ');
    html.push('	<input type="hidden" id="XbCoursePresetorganIds" name="XbCoursePresetorganIds" value=""/>                              ');
    html.push('	<td>                                                                         ');
    html.push('		<div class="extend-list">                                                ');
    html.push('			<div class="form-group">                                             ');
    html.push('				<label>'+chargingMode+'</label>                                            ');
    html.push('				<div class="numCon">                                             ');
    html.push('					<input id="XbCoursePresetperiodNum" name="XbCoursePresetperiodNum" type="text" value="0"/>');
    html.push('				</div>                                                           ');
    html.push('				<span class="txt">课时</span>                                    ');
    html.push('				<span class="txt">=</span>                                       ');
    html.push('				<div class="numCon">                                             ');
    html.push('					<input id="XbCoursePresetmoney" name="money" type="text" value="0"/>   ');
    html.push('				</div>                                                           ');
    html.push('				<span class="txt">元</span>                                      ');
    html.push('				<span class="glyphicon glyphicon-remove-sign"></span>            ');
    html.push('			</div>                                                               ');
    html.push('		</div>                                                                   ');
    html.push('	</td>                                                                        ');
    html.push('</tr>                                                                         ');
    $("#dingjiaiform").append(html.join(""))
}