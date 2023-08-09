/* eslint-disable */
function dateUtil(d){ 
    if(d == undefined || d == null){
        this.jsDateUtilDateParamLikeStaticParamUse = new Date(); 
    }
    else{
        this.jsDateUtilDateParamLikeStaticParamUse = new Date(d); 
    } 
    this.OneDayMillis = 24*60*60*1000;
}

dateUtil.prototype.getOneDayMillis = function(){
    return this.OneDayMillis;
}

 dateUtil.prototype.getCurYear =  function (){
        return this.jsDateUtilDateParamLikeStaticParamUse.getYear();//结果为当前年份和1900的差
    }

  dateUtil.prototype.getFullYear = function (){
        return this.jsDateUtilDateParamLikeStaticParamUse.getFullYear();
    }
  dateUtil.prototype.getMonth =  function (){
        return (this.jsDateUtilDateParamLikeStaticParamUse.getMonth()+1);//月份从0~11
    }
  dateUtil.prototype.getDate =  function (){
        return this.jsDateUtilDateParamLikeStaticParamUse.getDate()<10?('0'+this.jsDateUtilDateParamLikeStaticParamUse.getDate()):this.jsDateUtilDateParamLikeStaticParamUse.getDate();
    }
    dateUtil.prototype.getDay= function (){
        return this.jsDateUtilDateParamLikeStaticParamUse.getDay();//星期几
        /*
            0="星期日"
            1="星期一"
            2="星期二"
            3="星期三"
            4="星期四"
            5="星期五"
            6="星期六"
        */
    }
  dateUtil.prototype.getTime =  function (){
        return this.jsDateUtilDateParamLikeStaticParamUse.getTime();//时间戳，从1970.1.1开始的毫秒数。例：1601173441300
    }
  dateUtil.prototype.getHours=  function (){
        return this.jsDateUtilDateParamLikeStaticParamUse.getHours();
    }
 dateUtil.prototype.getMinutes =  function (){

        return this.jsDateUtilDateParamLikeStaticParamUse.getMinutes()<10?('0'+this.jsDateUtilDateParamLikeStaticParamUse.getMinutes()):this.jsDateUtilDateParamLikeStaticParamUse.getMinutes();
    }
   dateUtil.prototype.getSeconds = function (){
        return this.jsDateUtilDateParamLikeStaticParamUse.getSeconds();
    }
  dateUtil.prototype.getMilliseconds = function (){
        return this.jsDateUtilDateParamLikeStaticParamUse.getMilliseconds();
    }
   dateUtil.prototype.localeString = function (){
        return this.jsDateUtilDateParamLikeStaticParamUse.toLocaleString();//"2020/9/27 上午10:24:01"
    }
   dateUtil.prototype.localeDateString = function (){
        return this.jsDateUtilDateParamLikeStaticParamUse.toLocaleDateString();//"2020/9/27"
    }
    dateUtil.prototype.localeTimeString = function (){
        return this.jsDateUtilDateParamLikeStaticParamUse.toLocaleTimeString();//"上午10:24:01"
    } 

    dateUtil.prototype.getYearMonthDay = function(){
        return this.getFullYear() + '-' + 
               this.getMonth() + '-' +
               this.getDate() + ' 00:00:00';
    }

    
export { dateUtil }