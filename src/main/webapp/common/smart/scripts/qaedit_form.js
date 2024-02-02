var sel_length, end_length=0, start_length=0;
var bl=0;
var el=null;
function get_pos(d){
if( d ) el=d;
 var ret=0;
 if( bl==1 ) {
   var sel=document.selection.createRange();
   sel_length = sel.text.length;
   var r=d.createTextRange();
   var all=r.text.length;
   r.moveToPoint(sel.offsetLeft,sel.offsetTop);
   r.moveEnd("textedit");

   end_length=r.text.length;
   start_length=all-end_length;
 } else if( bl==2 ) {
     start_length=d.selectionStart;
     end_length=d.value.length - d.selectionEnd;
     sel_length=d.selectionEnd-start_length;
 }else if( bl==3 ){
     var ln=new String(d.value);
     start_length=ln.length;
     end_length=start_length;
     sel_length=0;
 }
}

function atach_focus(ln){
  if( bl == 1 ){
    var e=el.createTextRange();
    var tx=el.value.substr(0, ln);
    var pl=tx.split(/\n/);
    e.collapse(true);
    e.moveStart("character",ln-pl.length+1);
    e.text=e.text+"";
    e.collapse(false);
    e.select();
  } else if( bl == 2 ){
    el.setSelectionRange(ln, ln);
  } else if( bl == 3 ){
//
  }
  el.focus();
}

function enclose(s, e){
  if( !el) return;
  var itext=el.value;

  if( bl == 4 ){
    el.value = itext + s + e;
  } else if( bl ){
    var click_s=itext.substr(0, start_length);
    var click_m=itext.substr(start_length, sel_length);
    var click_e=itext.substr(start_length+sel_length, end_length);
    el.value=click_s + s + click_m + e + click_e;
  }

  atach_focus(s.length+e.length+start_length+sel_length);
}

function convert(s, e){
  if( !el ) return;
  if( (bl!=1) && (bl!=2) ){
     el.focus();
     return;
  }
  var itext=el.value;

  var click_s=itext.substr(0, start_length);
  var click_m=itext.substr(start_length, sel_length);
  var click_e=itext.substr(start_length+sel_length, end_length);
  var cnv = click_m.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;");
  el.value=click_s + cnv + click_e;

  atach_focus(start_length+cnv.length);
}

function fstins(af){
  if( !el ) return;
  if( (bl!=1) && (bl!=2) ){
     el.focus();
     return;
  }
  var itext=el.value;
  var flag=false;

  if( start_length ){
    if( itext.substr(start_length-1,1) == "\n" ){
      start_length--;
    }
  }
  var click_s=itext.substr(0, start_length);
  var click_m=itext.substr(start_length, sel_length);
  var click_e=itext.substr(start_length+sel_length, end_length);
  var cnv = click_m.replace(/\n/g, "\n"+af);
  if( start_length==0 ){
     flag=true;
  }
  if( flag ){
    cnv=af + cnv;
  }

  el.value=click_s + cnv + click_e;

  atach_focus(start_length+cnv.length);
}
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}


function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

function MM_openPreview(input,type){
  if( input == 'preview' )
  {
    document.form1.target = '_blank';
    document.form1.action = 'event_edit_preview.jsp?DataType='+ type;
  }
  document.form1.submit();
}

function MM_openInput(id,disp_flg){
  document.form1.target = '_self';
  document.form1.action = 'qaedit_input.jsp?Id='+id+'&col_disp_flg='+disp_flg;
  document.form1.submit();
}


function ColFunc(dispArea,col_msg,col_id,qid) {

  var source = col_msg.value;
  var id     = col_id.value % 10;
  var disp_data = "";
  switch(id)
  {
    case 1:
//      disp_data = disp_data + "<input type=button value=閉じる onclick='dispArea" + qid + ".innerHTML=\"\";'><br>";
      for (i = 0;i<50;i++)
      {
          if (source.split("\n")[i] != "" && source.split("\n")[i] != null && source.split("\n")[i] != "\r")
          disp_data = disp_data + "<input disabled type='checkbox'>" + source.split("\n")[i] +"<br/>";
      }
      break;
    case 2:
//      disp_data = disp_data + "<input type=button value=閉じる onclick='dispArea" + qid + ".innerHTML=\"\";'><br>";
      for (i = 0;i<50;i++)
      {
          if (source.split("\n")[i] != "" && source.split("\n")[i] != null && source.split("\n")[i] != "\r")
          disp_data = disp_data + "<input disabled type='radio'>" + source.split("\n")[i] +"<br/>";
      }
      break;
    case 3:
//      disp_data = disp_data + "<input type=button value=閉じる onclick='dispArea" + qid + ".innerHTML=\"<br><br><br><br><br>\";'><br>";
      if (source.split("\n")[0] != "" && source.split("\n")[0] != null)
      {
         disp_data = disp_data + source.split("\r\n")[0] +"<br><br><br><br><br>";
      }
      else
      {
         disp_data = disp_data + "<br><br><br><br><br>";
      }
      break;
    case 4:
//      disp_data = disp_data + "<input type=button value=閉じる onclick='dispArea" + qid + ".innerHTML=\"\";'><br>";
      disp_data = "<select>";
      for (i = 0;i<50;i++)
      {
          if (source.split("\n")[i] != "" && source.split("\n")[i] != null && source.split("\n")[i] != "\r")
          disp_data = disp_data + "<option>" + source.split("\n")[i];
      }
      disp_data = disp_data + "</select>";
      break;
    default:
      break;
  }
  dispArea.innerHTML = disp_data;
}

function IdFunc(inputArea,explainArea,col_msg,col_id) {

  var id     = col_id.value % 10;
  var disp_data = "";
  switch(id)
  {
    case 1:
      inputArea.style.display = 'block';
      col_msg.rows = 10;
      disp_data = "チェックボックスは選択肢について複数回答を求めるものです。質問の最後に（複数回答）と入力することをおすすめします。<br><br>";
      disp_data = disp_data + "改行すると選択肢を追加できます。<br>空白行は無視されます。<br>";
      break;
    case 2:
      col_msg.rows = 10;
      inputArea.style.display = 'block';
      disp_data = "ラジオボタンは、選択肢のうち1つだけ回答を求めるものです。<br><br>";
      disp_data = disp_data + "改行すると選択肢を追加できます。<br>空白行は無視されます。<br>";
      break;
    case 3:
      col_msg.rows = 1;
      inputArea.style.display = 'block';
      disp_data = "テキスト入力は、自由に回答を入力してもらうものです。<br><br>";
      disp_data = disp_data + "前問のチェックボックスやラジオボタンで最後に「その他」の選択肢を用意した場合は、質問を入力せずに、この左側に「その他を選択した方」等入力するとわかりやすくレイアウトされます。<br>";
      break;
    case 4:
      col_msg.rows = 10;
      inputArea.style.display = 'block';
      disp_data = "コンボボックスは、プルダウン式で1つだけ回答を求めるものです。何も入力しない場合は先頭に表示されているものが選択されます。<br><br>";
      disp_data = disp_data + "改行すると選択肢を追加できます。<br>空白行は無視されます。<br>";
      break;
    default:
      inputArea.style.display = 'none';
      break;
  }
  explainArea.innerHTML = disp_data;
}

function QidFunc(hotelid,id,Mode,QidMax,Qid) {
  max   =  QidMax.value;
  document.form1.target = '_self';
  switch(Mode)
  {
    case "ADD":
        if (max != 0)
        {
            max++;
        }
        else
        {
            max = 1;
        }
        target = max;
        document.form1.action = 'qaedit_form.jsp?HotelId='+hotelid+'&Id='+id+'&EDIT=Y&col_qid_max='+ max + '&ins_qid='+ max + '#qid_name' + target;
        break;
    case "DEL":
        target = Qid;
        if (Qid != 0)
        {
            target = Qid -1;
        }
        document.form1.action = 'qaedit_form.jsp?HotelId='+hotelid+'&Id='+id+'&EDIT=Y&col_qid_max='+ max + '&del_qid='+ Qid + '#qid_name' + target;
        break;
    case "INS":
        if (max != 0)
        {
            max++;
        }
        else
        {
            max = 1;
        }
        target = Qid;
        document.form1.action = 'qaedit_form.jsp?HotelId='+hotelid+'&Id='+id+'&EDIT=Y&col_qid_max='+ max + '&ins_qid='+ Qid + '#qid_name' + target;
        break;
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
		else {
		     return true;
			}
		}
	  else
	 	{
		     return true;
		}
		
}

