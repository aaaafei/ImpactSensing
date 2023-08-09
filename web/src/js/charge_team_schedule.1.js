/* eslint-disable */
  var user_dept_id = null;

  function initChargeTeamSchedule() {
      //  $("#main_content_title").html("收费人员排班")

      var user = $.zui.store.get("user"); // 缓存用户信息，取出dept_id

      // 用户的dept_id
      user_dept_id = user.organid;

      // 收费小组type
      var team_type = 1;

      // 日期游标
      var date_cursor = 0;

      $('#calendar').calendar();
      var calendar = $('#calendar').data('zui.calendar');

      calendarInit();

      // 请求收费人员排班数据
      function calendarInit(data_param) {
          var datap = {
              "dept_id": user_dept_id,
              "start_time": getMonthByCondition(date_cursor, 'first').Format("yyyy-MM-dd HH:mm:ss"), //getCurrentMonthFirst(),
              "end_time": getMonthByCondition(date_cursor, 'last').Format("yyyy-MM-dd HH:mm:ss"), //getCurrentMonthLast(),
              'type': team_type
          }
          if (data_param != undefined) {
              datap = data_param;
          }
          getTeamClass(datap, function(data) {
              var newEvents = [];



              for (var i = 0; i < data.length; i++) {
                  var obj = '';
                  if (data[i].class_type == 1) {
                      obj = { id: data[i].id, desc: data[i].team_name, title: data[i].team_name + "-" + getTeamName(data[i].class_type), start: data[i].end_time + ":02", end: data[i].end_time + ":03" };
                  } else if (data[i].class_type == 2) {
                      obj = { id: data[i].id, desc: data[i].team_name, title: data[i].team_name + "-" + getTeamName(data[i].class_type), start: data[i].end_time + ":01", end: data[i].end_time + ":02" };
                  } else {
                      obj = { id: data[i].id, desc: data[i].team_name, title: data[i].team_name + "-" + getTeamName(data[i].class_type), start: data[i].end_time + ":03", end: data[i].end_time + ":04" };
                  }
                  newEvents.push(obj);
              }

              // 这里轮训给每天都增加一个事件
              var date = getMonthByCondition(date_cursor, 'first'); //new Date();
              var end_date = getMonthByCondition(date_cursor, 'last'); //new Date(getCurrentMonthLast2()) ;
              if (data_param != undefined) {
                  date = new Date(data_param.start_time);
                  end_date = new Date(data_param.end_time);
              }

              for (var i = 0; i < 31; i++) {
                  if (date.setDate(i + 1) < end_date) {
                      var obj = { id: -1, desc: '详细排班', title: '+详细排班', start: date.Format('yyyy-MM-dd 00:00:05'), end: date.Format('yyyy-MM-dd 00:00:06'), color: '#bd7b46' };
                      newEvents.push(obj);
                  }
              }
              calendar.addEvents(newEvents);
          })
      }

      // 获取排班信息
      function getTeamClass(data, _function) {
          post_common_service("teamClass/getClassList", data, function(data) {
              _function(data);
          }, function(error) {
              return "";
          })
      }

      getStation();


      var lans = 0;
      var entrance_number = 0;
      var exit_number = 0;

      // 获取收费站信息
      function getStation() {
          get_common_service("dept/getFsByDept", "", function(data) {
              if (data != null) {
                  $(".thread_charge").append("<th>" + data.name + "</th>");
                  $(".thread_charge").append("<th>值机班长</th>");

                  //灌南东
                  if (user_dept_id == 35) {
                      $(".thread_charge").append("<th>收费班长(老)</th>");
                      $(".thread_charge").append("<th>收费班长(新）</th>");

                  } else {
                      $(".thread_charge").append("<th>收费班长</th>");
                  }

                  $(".thread_charge").append("<th>值班室</th>");


                  // console.log(data);
                  lans = data.lanes_number;
                  entrance_number = data.entrance_numer;
                  exit_number = data.exit_numer;
                  // for(var i=0;i<data.lanes_number;i++){
                  //     $(".thread_charge").append("<th>进口车道"+(i+1)+"</th>");
                  // }
                  for (var i = 0; i < entrance_number; i++) {
                      if (i == 1 && user_dept_id == 34) {
                          $(".thread_charge").append("<th>入口" + 9 + "</th>");
                      } else {
                          $(".thread_charge").append("<th>入口" + (i + 1) + "</th>");
                      }
                  }
                  for (var i = 0; i < exit_number; i++) {
                      $(".thread_charge").append("<th>出口" + (i + 1) + "</th>");
                  }
              }
          }, function(error) {
              return "";
          })
      }

      getClassCode();

      var classCode = {};


      /**
       * 获取班组信息
       */
      function getClassCode() {
          var data = {};
          post_common_service("teamClass/getClassCode", data, function(data) {
              for (var i = 0; i < data.length; i++) {
                  classCode[data[i].code] = data[i];
              }
              // console.log(classCode);
              $(".class_type_1").html(classCode[1].class_name);
              $(".class_type_2").html(classCode[2].class_name);
              $(".class_type_3").html(classCode[3].class_name);
          }, function(error) {
              return "";
          })
      }

      // 更新日历数据
      function updateCalendar(data) {
          calendar.events = [];
          calendar.display();
          calendarInit(data);
      }

      // 当月第一天
      function getCurrentMonthFirst() {
          var date = new Date();
          date.setDate(1);
          date.setHours(0);
          date.setMinutes(0);
          date.setSeconds(0);
          return date.Format("yyyy-MM-dd HH:mm:ss");
      }

      // 当月最后一天
      function getCurrentMonthLast() {
          var date = new Date();
          var currentMonth = date.getMonth();
          var nextMonth = ++currentMonth;
          var nextMonthFirstDay = new Date(date.getFullYear(), nextMonth, 1);
          var oneDay = 1000 * 60 * 60 * 24;
          var date1 = new Date(nextMonthFirstDay - oneDay);
          date1.setHours(23);
          date1.setMinutes(59);
          date1.setSeconds(59);
          return date1.Format("yyyy-MM-dd HH:mm:ss");
      }


      // 当月最后一天
      function getCurrentMonthLast2() {
          var date = new Date();
          var currentMonth = date.getMonth();
          var nextMonth = ++currentMonth;
          var nextMonthFirstDay = new Date(date.getFullYear(), nextMonth, 1);
          var oneDay = 0; //1000*60*60*24;

          var date1 = new Date(nextMonthFirstDay - oneDay);
          date1.setHours(0);
          date1.setMinutes(0);
          date1.setSeconds(0);
          return date1.Format("yyyy-MM-dd HH:mm:ss");
      }

      // 上一个月或者下个月的第一天或者最后一天
      function getMonthByCondition(pre_or_after, first_or_last) {
          if (first_or_last == 'first') {
              var date = new Date();
              date.setDate(1);
              date.setHours(0);
              date.setMinutes(0);
              date.setSeconds(0);
              date.setMonth(date.getMonth() + pre_or_after);
              return date;
          } else if (first_or_last == 'last') {
              var date = new Date();
              date.setMonth(date.getMonth() + pre_or_after); //此处月份加减，不能放到最后加减，会引起月份天数不对
              var currentMonth = date.getMonth();
              var nextMonth = ++currentMonth;
              var nextMonthFirstDay = new Date(date.getFullYear(), nextMonth, 1);

              var date1 = nextMonthFirstDay;
              date1.setDate(date1.getDate() - 1);
              date1.setHours(23);
              date1.setMinutes(59);
              date1.setSeconds(59);
              return date1;
          }
      }
      /**
       * 顶班日期
       */
      function date_time() {
          $.jeDate("#start_time", {
              format: "hh:mm:00",
              isinitVal: true,
              zIndex: 99999999
          });
          $.jeDate("#end_time", {
              format: "hh:mm:00",
              isinitVal: true,
              zIndex: 99999999
          });
      }
      date_time();
      $("#save_user_class").click(function() {
          $("#save_user_class").addClass("disabled");


          getCodeByFlagAndValue("record_class_days", 1, function(data) {
              $("#save_user_class").removeClass("disabled");

              var days = 1;
              if (data && data.code_name) {
                  days = data.code_name;
              }
              var now_date = moment(nowStr).toDate(); //new Date();
              now_date.setDate(now_date.getDate() - days);
              if (now_date.Format('yyyy-MM-dd') >= $("#save_user_class").data("work_date").trim()) {
                  tip("不能生成过期的排班");
                  return;
              }

              var dutyList = [];
              var work_date = $("#save_user_class").data("work_date") + "00:00:00";


              for (var j = 1; j < 4; j++) {
                  var cache = $(".tr-" + j).data("cache");

                  var zhiji_m_id = $(".c-" + j + "_0 select").val();
                  var station_m_id = $(".c-" + j + "_1 select").val();
                  var station_m_id1 = null;
                  if (user_dept_id == 35) {
                      station_m_id1 = $(".c-" + j + "_2 select").val();
                      if (station_m_id1 == "" || station_m_id1 == null || typeof(station_m_id1) == "undefined") {
                          station_m_id1 = 0;
                      }
                  }

                  if (zhiji_m_id == "" || zhiji_m_id == null || typeof(zhiji_m_id) == "undefined") {
                      zhiji_m_id = 0;
                  }
                  if (station_m_id == "" || station_m_id == null || typeof(station_m_id) == "undefined") {
                      station_m_id = 0;
                  }

                  if (user_dept_id == 35) {
                      station_m_id1 = parseInt(station_m_id1)
                  }

                  zhiji_m_id = parseInt(zhiji_m_id)
                  station_m_id = parseInt(station_m_id)

                  if (cache) {
                      var n_c = 2;
                      if (user_dept_id == 35) {
                          n_c = 3;
                      }
                      for (var i = 2; i < lans + 1 + n_c; i++) {
                          if ($(".tr-" + j + " td select").eq(i).val() != '' && $(".tr-" + j + " td select").eq(i).val() != null) {
                              var start_time = $("#save_user_class").data("work_date") + classCode[j].start_time + ":00";
                              if (j == 3) {
                                  var date = new Date($("#save_user_class").data("work_date").replace(/-/, "/"));
                                  date.setDate(date.getDate() - 1);
                                  start_time = date.Format("yyyy-MM-dd") + " " + classCode[j].start_time + ":00";
                              }
                              var end_time = $("#save_user_class").data("work_date") + classCode[j].end_time + ":00";

                              var lane_id = $(".tr-" + j + " td select").eq(i).parent().attr('tag');
                              if (lane_id == null || typeof(lane_id) == "undefined") {
                                  continue;
                              }
                              var m_id = station_m_id
                              if (lane_id == 0) {
                                  m_id = zhiji_m_id;
                              } else {
                                  if (user_dept_id == 35) {
                                      if (Math.abs(lane_id) <= 2) {
                                          m_id = station_m_id;
                                      } else {
                                          m_id = station_m_id1;
                                      }
                                  }
                              }



                              var list = $(".tr-" + j + " td select").eq(i).data("data");
                              var obj = { "list": list, "work_date": work_date, "dept_id": cache.dept_id, "user_id": $(".tr-" + j + " td select").eq(i).val(), "class_type": j, "team_id": cache.team_id, "start_time": start_time, "end_time": end_time, "lane": lane_id, "monitor_user_id": m_id };
                              console.log(obj)

                              dutyList.push(obj);

                          }
                      }
                  }
              }

              $("#save_user_class").attr("disabled", "disabled");

              if ($("#save_user_class").data("modify_flag")) {
                  $("#save_user_class").data("modify_flag", false);
                  post_common_service("userClass/updateList", dutyList, function(data) {
                      tip("修改成功");
                      $('#add_fee_station').modal('hide');
                      $("#save_user_class").attr("disabled", false);
                  }, function(error) {
                      return "";
                  })
                  return;
              }

              post_common_service("userClass/addList", dutyList, function(data) {
                  tip("添加成功");
                  $('#add_fee_station').modal('hide');
                  $("#save_user_class").attr("disabled", false);
              }, function(error) {
                  return "";
              })
          })
      })

      var cols = [{ width: 60, text: '序号', type: 'number', flex: false, colClass: 'text-center' },
          { width: 130, text: '开始时间', type: 'string', flex: false, colClass: 'text-center' },
          { width: 130, text: '结束时间', type: 'string', flex: false, sort: 'down', colClass: 'text-center' },
          { width: 130, text: '人员姓名', type: 'string', flex: false, colClass: 'text-center' },
          { width: 230, text: '操作', type: 'string', flex: false, colClass: 'text-center' }
      ];

      $('.datatable').datatable({
          checkable: false,
          sortable: false,
          data: {
              cols: cols,
              rows: []
          }
      });

      var temp_data;

      // 更新表格数据
      function updateTableData(obj) {
          var rowdata = [];
          if (obj != null && obj.length > 0) {
              for (var i = 0; i < obj.length; i++) {
                  var userdata = {};
                  userdata["checked"] = false;
                  userdata["data"] = [i + 1, obj[i].start_time, obj[i].end_time, obj[i].user_name, ' <button class="btn btn-primary btn_remove"  value=' + i + ' ><i class="icon icon-remove-circle"></i>删除</button>'];
                  rowdata.push(userdata);
              }
          }
          $('.datatable').datatable('load', {
              cols: cols,
              rows: rowdata
          });
      }

      $(".datatable").delegate(".btn_remove", "click", function() {
          var bind_data = temp_data;
          // alert($(this).val())
          if ($(this).val() != null && $(this).val() != '') {
              bind_data.splice($(this).val(), 1)
                  // $(".add-btn").data("data",bind_data);
              temp_data = bind_data;
              updateTableData(temp_data);
          }

      })

      var time_interval_obj = "";

      $(".table").delegate(".i-self", "click", function() {
              $("#team_info_ti").html($(this).prev().html());
              $(".save_class_info").data("data", $(this).prev());
              // 将时间区间值的span 对象缓存起来
              time_interval_obj = $(this).parent().parent().children().eq(0).children();
              //console.log(time_interval_obj);
              temp_data = $(this).prev().data("data");
              $("#lists").show();
              $("#add_update").hide();
              updateTableData(temp_data);
              showPopup4CommonBtns("添加顶班详情", "720px", "#add_detail_info", "", function() {

              }, function() {})
          })
          // 增加按钮
      $(".add-btn").click(function() {
          $("#team_info_ti").val("");
          $("#lists").hide();
          $("#add_update").show();

          $("#start_time").val(time_interval_obj.eq(1).html() + ':00');
          $("#end_time").val(time_interval_obj.eq(2).html() + ':00');
      })

      // 保存班组信息
      $(".save_class_info").click(function() {
          $(".save_class_info").data("data").data("data", temp_data);
          layer.closeAll();
      })

      $(".return-btn").click(function() {
          $("#lists").show();
          $("#add_update").hide();
      })

      // 保存
      $("#save_btn").click(function() {
          var bind_data = temp_data;
          if (bind_data == null) {
              bind_data = [];
          }
          // 判断时间区间
          // $(".modal-title span").html()
          var start_time_interval = $(".modal-title span").html() + ' ' + time_interval_obj.eq(1).html() + ":00";
          var end_time_interval = $(".modal-title span").html() + ' ' + time_interval_obj.eq(2).html() + ":00";

          var id = $("#team_info_ti").val();
          var start_time = $(".modal-title span").html() + ' ' + $("#start_time").val();
          var end_time = $(".modal-title span").html() + ' ' + $("#end_time").val();
          var name = $("#team_info_ti").find("option:selected").text();

          if (start_time_interval > end_time_interval) {
              var prev_date = new Date((start_time_interval).replace(/-/, "/"));
              prev_date.setDate(prev_date.getDate() - 1);
              start_time_interval = prev_date.Format("yyyy-MM-dd HH:mm:ss");

              if ($("#start_time").val() >= '23:00:00') {
                  prev_date = new Date((start_time).replace(/-/, "/"));
                  prev_date.setDate(prev_date.getDate() - 1);
                  start_time = prev_date.Format("yyyy-MM-dd HH:mm:00");
              }

              if ($("#end_time").val() > '23:00:00') {
                  prev_date = new Date((end_time).replace(/-/, "/"));
                  prev_date.setDate(prev_date.getDate() - 1);
                  end_time = prev_date.Format("yyyy-MM-dd HH:mm:00");
              }
          }

          //            alert( 'start_time:'+start_time + 'end_time:'+end_time  + 'start_time_i:'+start_time_interval +'end_time_i:'+end_time_interval   );

          if (start_time >= end_time) {
              tip("时间设置不合理，开始时间不能大于结束时间");
              return;
          }

          if (!(start_time >= start_time_interval && end_time <= end_time_interval)) {
              tip("不是当前值班时间段");
              return;
          }

          // 这边需要判断一下，时间不能重叠
          for (var i = 0; i < bind_data.length; i++) {
              if (!(start_time >= bind_data[i].end_time || end_time <= bind_data[i].start_time)) {
                  tip("设置的时间不能重叠");
                  return;
              }
          }

          if (id != "" && start_time != '' && end_time != '') {
              bind_data.push({ "user_name": name, "start_time": start_time, "end_time": end_time, "id": id });

              temp_data = bind_data;
              $("#lists").show();
              $("#add_update").hide();
              updateTableData(temp_data);
          } else {
              tip("信息需填写完整");
          }
      })

      $('#calendar').calendar().on("clickEvent.zui.calendar", function(event) {
          // console.log(event。event.id);
          if (event.event.id == -1) {

              // 这边负责添加整个页面的数据 按车道排班
              $('#add_fee_station').modal({ 'backdrop': 'static' });
              $(".modal-title").html("收费站排班表-<span>" + event.event.start.Format("yyyy-MM-dd") + "</span>");

              $("#save_user_class").data("work_date", event.event.start.Format("yyyy-MM-dd "));

              var data = {
                  "dept_id": user_dept_id,
                  "start_time": event.event.start.Format("yyyy-MM-dd 00:00:00"),
                  "end_time": event.event.start.Format("yyyy-MM-dd 00:00:00"),
                  'type': team_type
              }
              post_common_service("teamClass/getClassListAndUsers", data, function(data) {
                  $("#save_user_class").html("生成今日值班");
                  $(".tr-1,.tr-2,.tr-3").empty();

                  console.error(data)

                  for (var i = 0; i < data.length; i++) {
                      var ct = data[i].class_type;
                      $(".tr-" + ct).data("cache", data[i]);
                      $(".tr-" + ct).append("<td vertical-align='middle' align='center' style='width:140px;background:#F8F8F8;'><span style='font-weight:bold;'>" + classCode[ct].class_name + "<br></span><span>" + classCode[ct].start_time + "</span> - <span>" + classCode[ct].end_time + "</span></td>");



                      var selectv = "<select class='form-control select-self' style='width:100px;'>"
                      selectv += ("<option value=''>请选择</option>");
                      selectv += ("<option value='0'>临时开道</option>");
                      for (var j = 0; j < data[i].users.length; j++) {
                          var obj = data[i].users[j];
                          selectv += ("<option value='" + data[i].users[j].id + "'>" + data[i].users[j].truename + "</option>");
                      }
                      selectv += "</select>";

                      var selectv1 = selectv;
                      selectv1 = selectv1.replace("<option value='0'>临时开道</option>", "");
                      selectv1 = selectv1.replace("<option value=''>请选择</option>", "<option value='0'>请选择</option>");

                      $(".tr-" + ct).append("<td style='vertical-align:top;width:137px;padding:2px;' class='c-" + ct + "_0' >" + selectv1 + "</td>")

                      if (user_dept_id == 35) {
                          $(".tr-" + ct).append("<td style='vertical-align:top;width:137px;padding:2px;' class='c-" + ct + "_1' >" + selectv1 + "</td>");
                          $(".tr-" + ct).append("<td style='vertical-align:top;width:137px;padding:2px;' class='c-" + ct + "_2' >" + selectv1 + "</td>");
                      } else {
                          $(".tr-" + ct).append("<td style='vertical-align:top;width:137px;padding:2px;' class='c-" + ct + "_1' >" + selectv1 + "</td>");
                      }



                      selectv += "<i title='顶班详情' alt='顶班详情' class='icon icon-tags i-self' style='float:right;margin-top:7px;'></i>";
                      selectv += "<ul style='padding:0px;'></ul>"

                      var selectv2 = selectv;
                      selectv2 = selectv2.replace("<option value='0'>临时开道</option>", "");

                      $(".tr-" + ct).append("<td style='vertical-align:top;width:137px;padding:2px;' class='c-" + ct + "-0' tag='0'>" + selectv2 + "</td>")

                      for (var z = 0; z < entrance_number; z++) {
                          $(".tr-" + ct).append("<td style='vertical-align:top;width:137px;padding:2px;' class='c-" + ct + "-" + (z + 1) + "' tag='" + (z + 1) + "'>" + selectv + "</td>")
                      }
                      for (var z = 0; z < exit_number; z++) {
                          $(".tr-" + ct).append("<td style='vertical-align:top;width:137px;padding:2px;' class='c-" + ct + "--" + (z + 1) + "' tag='-" + (z + 1) + "'>" + selectv + "</td>")
                      }
                  }
                  if (data.length == 0) {
                      $(".tr-1").append("<td>" + classCode["1"].class_name + " " + classCode["1"].start_time + " - " + classCode["1"].end_time + "</td>");
                      $(".tr-2").append("<td>" + classCode[2].class_name + " " + classCode[2].start_time + " - " + classCode[2].end_time + "</td>");
                      $(".tr-3").append("<td>" + classCode[3].class_name + " " + classCode[3].start_time + " - " + classCode[3].end_time + "</td>");
                      var n_c = 2;
                      if (user_dept_id == 35) {
                          n_c = 3;
                      }
                      for (var i = 0; i < lans + 1 + n_c; i++) {
                          $(".tr-1,.tr-2,.tr-3").append("<td></td>");
                      }
                  } else {
                      var cache = $(".tr-1").data("cache");
                      var work_date = $("#save_user_class").data("work_date") + "00:00:00";
                      var param = {
                          'dept_id': user_dept_id,
                          'work_date': work_date
                      }

                      post_common_service("userClass/getList", param, function(data) {
                          // 这边将值班数据检索出来，初始化收费站车道排班中
                          // console.log(data);
                          if (data.length > 0) {
                              $("#save_user_class").data("modify_flag", true);
                              $("#save_user_class").html("修改今日值班");
                          } else {
                              $("#save_user_class").html("生成今日值班");
                              $("#save_user_class").data("modify_flag", false);
                          }
                          for (var i = 0; i < data.length; i++) {
                              // console.log(data[i].list)
                              // 这边需要把id转换成user_id
                              var m_id = data[i].monitor_user_id;
                              m_id = m_id <= 0 ? 0 : m_id;

                              if (data[i].list != null)
                                  $(".c-" + data[i].class_type + "-" + data[i].lane + " select").data("data", data[i].list);
                              var liStr = "";
                              for (var mm = 0; mm < data[i].list.length; mm++) {
                                  var obj = data[i].list[mm];
                                  liStr += "<li style='list-style-type:none;font-size:12px;color:#777777'>" + obj.start_time.substr(11, 5) + "-" + obj.end_time.substr(11, 5) + " " + obj.user_name + "</li>";
                              }
                              $(".c-" + data[i].class_type + "-" + data[i].lane + " ul").append(liStr);
                              for (var j = 0; j < data[i].list.length; j++) {
                                  data[i].list[j].id = data[i].list[j].user_id;
                              }

                              var aim_selector = $(".c-" + data[i].class_type + "-" + data[i].lane + " select");
                              var zhiji_m_id = $(".c-" + data[i].class_type + "_0 select");
                              var station_m_id = $(".c-" + data[i].class_type + "_1 select");
                              var station_m_id1 = null;
                              if (user_dept_id == 35) {
                                  station_m_id1 = $(".c-" + data[i].class_type + "_2 select");
                              }

                              if (data[i].lane == 0) {
                                  zhiji_m_id.val(m_id);
                              } else {
                                  if (user_dept_id == 35) {
                                      if (Math.abs(data[i].lane) <= 2) {
                                          station_m_id.val(m_id)
                                      } else {
                                          station_m_id1.val(m_id)
                                      }

                                  } else {
                                      station_m_id.val(m_id);
                                  }
                              }

                              aim_selector.val(data[i].user_id);

                              if (aim_selector.find("option:selected").text() == '') {
                                  aim_selector.append("<option value='" + data[i].user_id + "' selected='selected'>" + data[i].user_name + "</option>")
                              }
                              // $(".table .tr-"+data[i].class_type+" select").eq(i%(lans+1)).val(data[i].user_id);
                          }
                      }, function(error) {
                          return "";
                      })
                  }
              }, function(error) {
                  return "";
              })
          } else {
              click_function(event.event.start);
          }
      });

      /**
       * 点击日历触发函数
       */
      function click_function(date) {
          setAddTermTitle(date.Format("yyyy-MM-dd"));
          // 保存当前日期
          $("#save_term_class").data("date", date.Format("yyyy-MM-dd"));

          var data = {
              "dept_id": user_dept_id,
              "start_time": date.Format("yyyy-MM-dd 00:00:00"),
              "end_time": date.Format("yyyy-MM-dd 00:00:00"),
              'type': team_type
          }

          getTeamClass(data, function(data) {
              for (var i = 0; i < $("select[id*='_term']").length; i++) {
                  $("select[id*='_term']").eq(i).val("");
              }
              if (data.length > 0) {
                  $("#save_term_class").data("modify_flag", 1);
                  for (var i = 0; i < data.length; i++) {
                      $(".class_team_" + data[i].class_type).val(data[i].team_id);
                  }
              } else {
                  $("#save_term_class").data("modify_flag", 0);
              }
              $('#myModal').modal();
          })
      }

      $('#calendar').calendar().on("clickCell.zui.calendar", function(event) {
          // console.log(event);
          click_function(event.date);
      });

      // 下一个日期
      $('#calendar').calendar().on("clickNextBtn.zui.calendar", function(event) {
          // console.log(event);
          date_cursor++;

          var data = {
              "dept_id": user_dept_id,
              "start_time": getMonthByCondition(date_cursor, 'first').Format("yyyy-MM-dd HH:mm:ss"),
              "end_time": getMonthByCondition(date_cursor, 'last').Format("yyyy-MM-dd HH:mm:ss"),
              'type': team_type
          }
          updateCalendar(data);
      });

      // 上一个日期
      $('#calendar').calendar().on("clickPrevBtn.zui.calendar", function(event) {
          date_cursor--;
          var data = {
              "dept_id": user_dept_id,
              "start_time": getMonthByCondition(date_cursor, 'first').Format("yyyy-MM-dd HH:mm:ss"),
              "end_time": getMonthByCondition(date_cursor, 'last').Format("yyyy-MM-dd HH:mm:ss"),
              'type': team_type
          }
          updateCalendar(data);
      });

      // 今天
      $('#calendar').calendar().on("clickTodayBtn.zui.calendar", function(event) {
          // console.log(event);
          date_cursor = 0;
          calendarInit();
      });


      $("#save_term_class").click(function() {

          $("#save_term_class").addClass("disabled");

          getCodeByFlagAndValue("record_class_days", 1, function(data) {
              $("#save_term_class").removeClass("disabled");
              var days = 1;
              if (data && data.code_name) {
                  days = data.code_name;
              }
              var now_date = moment(nowStr).toDate(); //new Date();
              now_date.setDate(now_date.getDate() - days);
              // 判断当前时间和待添加班组时间判断
              //   now_date.setDate(now_date.getDate() - 1);
              if (now_date.Format('yyyy-MM-dd') >= $("#save_term_class").data("date").trim()) {
                  tip("不能生成过期的排班");
                  return;
              }

              var data = [];
              for (var i = 0; i < $("select[id*='_term']").length; i++) {
                  if ($("select[id*='_term']").eq(i).val() != "") {

                      // 这边需要转换一下，关于值
                      var team_type_value = 1;
                      if (i == 0) {
                          team_type_value = 3;
                      } else if (i == 1) {
                          team_type_value = 1;
                      } else {
                          team_type_value = 2;
                      }

                      var start_time = $("#save_term_class").data("date") + " " + classCode[team_type_value].start_time + ":00";
                      if (i == 0) {
                          var date = new Date($("#save_term_class").data("date").replace(/-/, "/"));
                          date.setDate(date.getDate() - 1);
                          start_time = date.Format("yyyy-MM-dd") + " " + classCode[team_type_value].start_time + ":00";
                      }

                      var obj = {
                          "class_type": team_type_value,
                          "team_id": $("select[id*='_term']").eq(i).val(),
                          "work_date": $("#save_term_class").data("date") + " 00:00:00",
                          "start_time": start_time,
                          "end_time": $("#save_term_class").data("date") + " " + classCode[team_type_value].end_time + ":00",
                          "dept_id": user_dept_id,
                          "user_type": 1 //表示收费站
                      };

                      data.push(obj);
                  }
              }
              if (data.length == 0) {
                  tip("请增加排班!");
                  return;
              }
              if ($("#save_term_class").data("modify_flag") != 1) {
                  post_common_service("teamClass/add/list", data, function(data) {
                      updateCalendar();
                      $('#myModal').modal('hide');
                  }, function(error) {
                      $('#myModal').modal('hide');
                  })
              } else {
                  post_common_service("teamClass/update/list", data, function(data) {
                      updateCalendar();
                      $('#myModal').modal('hide');
                  }, function(error) {
                      $('#myModal').modal('hide');
                  })
              }
          })
      })

      $(".close_btn").click(function() {
          $('#myModal,#add_fee_station').modal('hide');
      })

      // 获取部门为1的班组信息
      getTermClass(user_dept_id);

      // 获取班组信息
      function getTermClass(dept_id) {
          var data = {
              'dept_id': dept_id,
              'type': team_type
          }
          post_common_service("team/getTeamByDeptId/1/50", data, function(data) {
              for (var i = 0; i < data.list.length; i++) {
                  $("#mooring_term,#middle_term,#night_term,#all_term").append("<option value='" + data.list[i].id + "'>" + data.list[i].name + "</option>")
              }
          }, function(error) {})
      }
  }
  // 根据class_type 获取班次名称
  function getTeamName(class_type) {
      if (class_type == 1) {
          return "早班";
      } else if (class_type == 2) {
          return "中班";
      } else if (class_type == 3) {
          return "夜班";
      } else if (class_type == 4) {
          return "全班";
      }
  }

  function setAddTermTitle(title) {
      $(".modal-title").html("添加班组-" + title);
  }