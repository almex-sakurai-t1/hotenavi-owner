function MM_openPreview(input,hotelid,type,id){
  if( input == 'preview' )
  {
    document.form1.target = '_self';
    document.form1.action = 'coupon_edit_preview.jsp?HotelId='+hotelid+'&CouponType='+type+'&Id='+id;
  }
  document.form1.submit();
}

function MM_openInput(input,hotelid,type,id){
  if( input == 'input' )
  {
    document.form1.target = '_self';
    document.form1.action = 'coupon_edit_input.jsp?HotelId='+hotelid+'&CouponType='+type+'&Id='+id;
  }
  document.form1.submit();
}

function setDayRange(obj,start_input,end_input){
	var start_year  = Math.floor(start_input / 10000);
	var start_month = Math.floor(start_input / 100) % 100;
	var start_day   = start_input % 100;
	var end_year    = Math.floor(end_input / 10000);
	var end_month   = Math.floor(end_input / 100) % 100;
	var end_day     = end_input % 100;

	obj = obj.form;
	var years  = parseInt(obj.col_start_yy.value,10);
	var months = parseInt(obj.col_start_mm.value,10);
	var days   = parseInt(obj.col_start_dd.value,10);
	var yeare  = parseInt(obj.col_end_yy.value,10);
	var monthe = parseInt(obj.col_end_mm.value,10);
	var daye   = parseInt(obj.col_end_dd.value,10);

    if (isNaN(years))
	{
		obj.col_start_yy.value = start_year;
		years =  start_year;
	}
	else
	{
		obj.col_start_yy.value = years;
	}
    if (years < 2000)
	{
	    obj.col_start_yy.value = 2000;
	}
    if (years > 2999)
	{
	    obj.col_start_yy.value = 2999;
	}

    if (isNaN(months))
	{
		obj.col_start_mm.value = start_month;
		months = start_month;
	}
	else
	{
		obj.col_start_mm.value = months;
	}
    if (months < 1)
	{
	    obj.col_start_mm.value = 1;
	}
    if (months > 12)
	{
	    obj.col_start_mm.value = 12;
	}

//入力されている年月より、その月の最終日付を算出
	var lastday_s = monthday(years,months);

    if (isNaN(days))
	{
		obj.col_start_dd.value = start_day;
		days = start_day;
	}
	else
	{
		obj.col_start_dd.value = days;
	}
    if (days < 1)
	{
	    obj.col_start_dd.value = 1;
	}
		
//入力されている月によって、最終日付を変更。ありえない日付の場合には、最終日付に強制的に変換。
	if (lastday_s < days) {
		obj.col_start_dd.value = lastday_s;
	}



    if (isNaN(yeare))
	{
		obj.col_end_yy.value = end_year;
		yeare = end_year;
	}
	else
	{
		obj.col_end_yy.value = yeare;
	}
    if (yeare < 2000)
	{
	    obj.col_end_yy.value = 2000;
	}
    if (yeare > 2999)
	{
	    obj.col_end_yy.value = 2999;
	}

    if (isNaN(monthe))
	{
		obj.col_end_mm.value = end_month;
		monthe = end_month;
	}
	else
	{
		obj.col_end_mm.value = monthe;
	}
    if (monthe < 1)
	{
	    obj.col_end_mm.value = 1;
	}
    if (monthe > 12)
	{
	    obj.col_end_mm.value = 12;
	}

//入力されている年月より、その月の最終日付を算出
	var lastday_e = monthday(yeare,monthe);

    if (isNaN(daye))
	{
		obj.col_end_dd.value = end_day;
		daye = end_day;
	}
	else
	{
		obj.col_end_dd.value = daye;
	}
    if (daye < 1)
	{
	    obj.col_end_dd.value = 1;
	}
		
//入力されている月によって、最終日付を変更。ありえない日付の場合には、最終日付に強制的に変換。
	if (lastday_e < daye) {
		obj.col_end_dd.value = lastday_e;
	}

}


function monthday(yearx,monthx){
	var lastday = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
	if (((yearx % 4 == 0) && (yearx % 100 != 0)) || (yearx % 400 == 0)){
		lastday[1] = 29;
	}
	return lastday[monthx - 1];
}
function validation_range(){
	var input_years = parseInt(form1.col_start_yy.value,10);
	var input_months = parseInt(form1.col_start_mm.value,10);
	var input_days = parseInt(form1.col_start_dd.value,10);
		
	var input_yeare = parseInt(form1.col_end_yy.value,10);
	var input_monthe = parseInt(form1.col_end_mm.value,10);
	var input_daye = parseInt(form1.col_end_dd.value,10);


	if  (input_yeare < input_years)
		     {
			 alert("期間指定（開始日付≦終了日付）を正しく入力してください");
			 document.form1.col_start_yy.focus();
			 return false;
			 }
	else
	   if  (input_yeare == input_years)
	   { 
		if (input_monthe < input_months)
		     {
			 alert("期間指定（開始日付≦終了日付）を正しく入力してください");
			 document.form1.col_start_yy.focus();
			 return false;
			 }
		else if  (input_monthe == input_months && input_daye < input_days)
		     {
			 alert("期間指定（開始日付≦終了日付）を正しく入力してください");
			 document.form1.col_start_yy.focus();
			 return false;
			 }
       }
    return true;
}
