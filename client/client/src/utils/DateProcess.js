class DateProcess {
    dateFormat(date, format) {
        if (!(date instanceof Date)) {
            date = new Date(date);
        }
        let year = date.getFullYear();
        let month = date.getMonth() + 1;
        let day = date.getDate();
        let hour = date.getHours();
        let minute = date.getMinutes();
        let second = date.getSeconds();
        let weekday = this.generateWeekday(date.getDay(),"en");

        format = format.replace("YYYY", year.toString());
        format = format.replace("MM", month < 10 ? "0" + month : month.toString());
        format = format.replace("DD", day < 10 ? "0" + day : day.toString());
        format = format.replace("HH", hour < 10 ? "0" + hour : hour.toString());
        format = format.replace("mm", minute == 0 ? "00" : minute.toString());
        format = format.replace("ss", second == 0 ? "00" : second.toString());
        format = format.replace("ww", weekday);

        return format;
    }
    dateDiff(date1, date2, output) {
        if(!(date1 instanceof Date)){
            var dateF = new Date(date1).getTime();
        }else{
            dateF = date1;
        }
        if(!(date2 instanceof Date)){
            var dateN = new Date(date2).getTime();
        }else{
            dateN = date2;
        }
        var result;
        switch (output) {
            case "second": result = Math.abs((dateF - dateN) / (1000)); break;
            case "minute": result = Math.abs((dateF - dateN) / (1000 * 60)); break;
            case "hour": result = Math.abs((dateF - dateN) / (1000 * 60 * 60)); break;
            case "day": result = Math.abs((dateF - dateN) / (1000 * 60 * 60 * 24)); break;
            default: break;
        }

        return result;
    }
    // plus和minus都是以毫秒计
    datePlus(date, plus) {
        var time = new Date(date).getTime();
        var result = new Date(time + plus);
        return result;
    }
    dateMinus(date, minus) {
        var time = new Date(date).getTime();
        var result = new Date(time - minus);
        return result;
    }
    generateWeekday = function (day, lang) {
        let weekday = "";
        if (lang == "cn") {
            switch (day) {
                case 0: weekday = "星期日"; break;
                case 1: weekday = "星期一"; break;
                case 2: weekday = "星期二"; break;
                case 3: weekday = "星期三"; break;
                case 4: weekday = "星期四"; break;
                case 5: weekday = "星期五"; break;
                case 6: weekday = "星期六"; break;
                default: break;
            }
        }else if(lang=="en"){
            switch(day){
                case 0:weekday = "Sunday";break;
                case 1:weekday = "Monday";break;
                case 2:weekday = "Tuesday";break;
                case 3:weekday = "Wednesday";break;
                case 4:weekday = "Thursday";break;
                case 5:weekday = "Friday";break;
                case 6:weekday = "Sarurday";break;
                default:break;
            }
        }

        return weekday;
    }
    translateMonth(month){
        var monthList = ["January","February","March","April","May","June","July","August","September","October","November","December"];
        return monthList[month];
    }
}


export default new DateProcess();