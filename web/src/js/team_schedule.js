/* eslint-disable */
  function initChargeTeamSchedule () {
      //  $("#main_content_title").html("收费人员排班")

    // var user = $.zui.store.get('user'); // 缓存用户信息，取出dept_id

    //   // 用户的dept_id
    // user_dept_id = user.organid;

    //   // 收费小组type
    // var team_type = 1;

    //   // 日期游标
    // var date_cursor = 0;

    // $('#calendar').calendar();
    // var calendar = $('#calendar').data('zui.calendar');

    calendarInit();
    getTeamClass(datap, function (data) {
      var newEvents = [];
      for (var i = 0; i < data.length; i++) {
        var obj = '';
        if (data[i].class_type == 1) {
          obj = { id: data[i].id, desc: data[i].team_name, title: data[i].team_name + '-' + getTeamName(data[i].class_type), start: data[i].end_time + ':02', end: data[i].end_time + ':03' };
        } else if (data[i].class_type == 2) {
          obj = { id: data[i].id, desc: data[i].team_name, title: data[i].team_name + '-' + getTeamName(data[i].class_type), start: data[i].end_time + ':01', end: data[i].end_time + ':02' };
        } else {
          obj = { id: data[i].id, desc: data[i].team_name, title: data[i].team_name + '-' + getTeamName(data[i].class_type), start: data[i].end_time + ':03', end: data[i].end_time + ':04' };
        }
        newEvents.push(obj);
      }
        // 这里轮训给每天都增加一个事件
      var date = getMonthByCondition(date_cursor, 'first'); // new Date();
      var end_date = getMonthByCondition(date_cursor, 'last'); // new Date(getCurrentMonthLast2()) ;
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
    });
  }
// 请求收费人员排班数据
  function calendarInit (data_param) {
    var datap = {
      'dept_id': user_dept_id,
      'start_time': getMonthByCondition(date_cursor, 'first').Format('yyyy-MM-dd HH:mm:ss'), // getCurrentMonthFirst(),
      'end_time': getMonthByCondition(date_cursor, 'last').Format('yyyy-MM-dd HH:mm:ss'), // getCurrentMonthLast(),
      'type': team_type
    };
    if (data_param != undefined) {
      datap = data_param;
    }
  }

// 获取排班信息
  function getTeamClass (data, _function) {
    post_common_service('teamClass/getClassList', data, function (data) {
      _function(data);
    }, function (error) {
      return '';
    });
  }
  // 根据class_type 获取班次名称
  function getTeamName (class_type) {
    if (class_type == 1) {
      return '早班';
    } else if (class_type == 2) {
      return '中班';
    } else if (class_type == 3) {
      return '夜班';
    } else if (class_type == 4) {
      return '全班';
    }
  }

//   function setAddTermTitle (title) {
//     $('.modal-title').html('添加班组-' + title);
//   }

  export default {
    init: initChargeTeamSchedule
  };
