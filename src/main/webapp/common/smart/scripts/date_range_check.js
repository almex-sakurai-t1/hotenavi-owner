function setDayRange(obj){
	obj = obj.form;
	var years = parseInt(obj.StartYear.options[obj.StartYear.selectedIndex].value,10);
	var months = parseInt(obj.StartMonth.options[obj.StartMonth.selectedIndex].value,10);
	var days = parseInt(obj.StartDay.options[obj.StartDay.selectedIndex].value,10);
	var yeare = parseInt(obj.EndYear.options[obj.EndYear.selectedIndex].value,10);
	var monthe = parseInt(obj.EndMonth.options[obj.EndMonth.selectedIndex].value,10);
	var daye = parseInt(obj.EndDay.options[obj.EndDay.selectedIndex].value,10);
//入力されている年月より、その月の最終日付を算出
	var lastday_s = monthday_s(years,months);
		
//入力されている月によって、最終日付を変更。ありえない日付の場合には、最終日付に強制的に変換。
	var itemnum = obj.StartDay.length;
	if (lastday_s - 1 < obj.StartDay.selectedIndex) {
		obj.StartDay.selectedIndex = lastday_s - 1;
		obj.StartDay.options[obj.StartDay.selectedIndex].value = lastday_s;
	}
	obj.StartDay.length = lastday_s;
	for (cnt = itemnum + 1;cnt <= lastday_s;cnt++) {
		obj.StartDay.options[cnt - 1].text = cnt;
		obj.StartDay.options[cnt - 1].value = cnt;
	}

//入力されている年月より、その月の最終日付を算出
	var lastday_e = monthday_e(yeare,monthe);
		
//入力されている月によって、最終日付を変更。ありえない日付の場合には、最終日付に強制的に変換。
	var itemnum_e = obj.EndDay.length;
	if (lastday_e - 1 < obj.EndDay.selectedIndex) {
		obj.EndDay.selectedIndex = lastday_e - 1;
		obj.EndDay.options[obj.EndDay.selectedIndex].value = lastday_e;
	}
	obj.EndDay.length = lastday_e;
	for (cnt = itemnum_e + 1;cnt <= lastday_e;cnt++) {
		obj.EndDay.options[cnt - 1].text = cnt;
		obj.EndDay.options[cnt - 1].value = cnt;
	}
}
function monthday_s(yearx,monthx){
	var lastday_s = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
	if (((yearx % 4 == 0) && (yearx % 100 != 0)) || (yearx % 400 == 0)){
		lastday_s[1] = 29;
	}
	return lastday_s[monthx - 1];
}

function monthday_e(yearx,monthx){
	var lastday_e = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
	if (((yearx % 4 == 0) && (yearx % 100 != 0)) || (yearx % 400 == 0)){
		lastday_e[1] = 29;
	}
	return lastday_e[monthx - 1];
}
function validation_range(now_date){
	var now_year    = parseInt(now_date / 10000);
	var now_month   = parseInt(now_date / 100 % 100);
	var now_day     = parseInt(now_date % 100);
	var input_years = parseInt(selectrange.StartYear.options[selectrange.StartYear.selectedIndex].value,10);
	var input_months = parseInt(selectrange.StartMonth.options[selectrange.StartMonth.selectedIndex].value,10);
	var input_days = parseInt(selectrange.StartDay.options[selectrange.StartDay.selectedIndex].value,10);

	if  (input_years == now_year)
		{ 
		if (input_months > now_month)
		     {
			 alert("先の日付は入力できません");
			 return false;
			 }
		else if  (input_months == now_month && input_days > now_day)
		     {
			 alert("先の日付は入力できません");
			 return false;
			 }
		}
		
	var input_yeare = parseInt(selectrange.EndYear.options[selectrange.EndYear.selectedIndex].value,10);
	var input_monthe = parseInt(selectrange.EndMonth.options[selectrange.EndMonth.selectedIndex].value,10);
	var input_daye = parseInt(selectrange.EndDay.options[selectrange.EndDay.selectedIndex].value,10);

	if  (input_yeare == now_year)
		{ 
		if (input_monthe > now_month)
		     {
			 alert("先の日付は入力できません");
			 return false;
			 }
		else if  (input_monthe == now_month && input_daye > now_day)
		     {
			 alert("先の日付は入力できません");
			 return false;
			 }
		}

	if  (input_yeare < input_years)
		     {
			 alert("期間指定（開始日付≦終了日付）を正しく入力してください");
			 return false;
			 }
	else if  (input_yeare == input_years)
		{ 
		if (input_monthe < input_months)
		     {
			 alert("期間指定（開始日付≦終了日付）を正しく入力してください");
			 return false;
			 }
		else if  (input_monthe == input_months && input_daye < input_days)
		     {
			 alert("期間指定（開始日付≦終了日付）を正しく入力してください");
			 return false;
			 }
		}
}
