function setDay(obj){
	obj = obj.form;
	var years = parseInt(obj.StartYear.options[obj.StartYear.selectedIndex].value,10);
	var months = parseInt(obj.StartMonth.options[obj.StartMonth.selectedIndex].value,10);
	var days = parseInt(obj.StartDay.options[obj.StartDay.selectedIndex].value,10);

//	if (years == now_year)
//		{ 
//		var lastmonth =  now_month;
//		}
//		else 
//		{
//		var lastmonth = 12;
//		}

//	var itemnum = obj.StartMonth.length;
//	if (lastmonth - 1 < obj.StartMonth.selectedIndex) {
//		obj.StartMonth.selectedIndex = lastmonth - 1;
//		obj.StartMonth.options[obj.StartMonth.selectedIndex].value = lastmonth;
//		months = lastmonth; 
//	}
//	obj.StartMonth.length = lastmonth;
//	for (cnt = itemnum + 1;cnt <= lastmonth;cnt++) {
//		obj.StartMonth.options[cnt - 1].text = cnt;
//		obj.StartMonth.options[cnt - 1].value = cnt;
//	}

//	if (years ==  now_year && months == now_month)
//		{ 
//		var lastday = now_day;
//		}
//		else 
//		{
//入力されている年月より、その月の最終日付を算出
	var lastday = monthday(years,months);
//		}

//入力されている月によって、最終日付を変更。ありえない日付の場合には、最終日付に強制的に変換。
	var itemnum = obj.StartDay.length;
	if (lastday - 1 < obj.StartDay.selectedIndex) {
		obj.StartDay.selectedIndex = lastday - 1;
		obj.StartDay.options[obj.StartDay.selectedIndex].value = lastday;
	}
	obj.StartDay.length = lastday;
	for (cnt = itemnum + 1;cnt <= lastday;cnt++) {
		obj.StartDay.options[cnt - 1].text = cnt;
		obj.StartDay.options[cnt - 1].value = cnt;
	}
}

function monthday(years,months){
	var lastday = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
	if (((years % 4 == 0) && (years % 100 != 0)) || (years % 400 == 0)){
		lastday[1] = 29;
	}
	return lastday[months - 1];
}

function validation(now_date){
	var now_year    = parseInt(now_date / 10000);
	var now_month   = parseInt(now_date / 100 % 100);
	var now_day     = parseInt(now_date % 100);
	var input_year = parseInt(selectday.StartYear.options[selectday.StartYear.selectedIndex].value,10);
	var input_month = parseInt(selectday.StartMonth.options[selectday.StartMonth.selectedIndex].value,10);
	var input_day = parseInt(selectday.StartDay.options[selectday.StartDay.selectedIndex].value,10);
	if  (input_year == now_year)
		{ 
		if (input_month > now_month)
		     {
			 alert("先の日付は入力できません");
			 return false;
			 }
		else if  (input_month == now_month && input_day > now_day)
		     {
			 alert("先の日付は入力できません");
			 return false;
			 }
		}
}
