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

function MM_openBrWindow0(theURL,winName,features,colno) { //v2.0

  switch(colno)
  {
    case 0:
      getcolor = showModalDialog(theURL,document.forms[0].col0_title_color.value);
      if( getcolor == ""   ) { document.forms[0].col0_title_color.value = "";title_big.style.color=document.forms[0].col0_title_color.value }
      else if( getcolor != null ) { document.forms[0].col0_title_color.value = "#" + getcolor;title_big.style.color=document.forms[0].col0_title_color.value }
      break;
    case 1:
      getcolor = showModalDialog(theURL,document.forms[0].col0_msg1_title_color.value);
      if( getcolor == ""   ) { document.forms[0].col0_msg1_title_color.value = "";msg_title_1.style.color=document.forms[0].col0_msg1_title_color.value }
      else if( getcolor != null ) { document.forms[0].col0_msg1_title_color.value = "#" + getcolor;msg_title_1.style.color=document.forms[0].col0_msg1_title_color.value }
      break;
    case 2:
      getcolor = showModalDialog(theURL,document.forms[0].col0_msg2_title_color.value);
      if( getcolor == ""   ) { document.forms[0].col0_msg2_title_color.value = "";msg_title_2.style.color=document.forms[0].col0_msg2_title_color.value }
      else if( getcolor != null ) { document.forms[0].col0_msg2_title_color.value = "#" + getcolor;msg_title_2.style.color=document.forms[0].col0_msg2_title_color.value }
      break;
    case 3:
      getcolor = showModalDialog(theURL,document.forms[0].col0_msg3_title_color.value);
      if( getcolor == ""   ) { document.forms[0].col0_msg3_title_color.value = "";msg_title_3.style.color=document.forms[0].col0_msg3_title_color.value }
      else if( getcolor != null ) { document.forms[0].col0_msg3_title_color.value = "#" + getcolor;msg_title_3.style.color=document.forms[0].col0_msg3_title_color.value }
      break;
    case 4:
      getcolor = showModalDialog(theURL,document.forms[0].col0_msg4_title_color.value);
      if( getcolor == ""   ) { document.forms[0].col0_msg4_title_color.value = "";msg_title_4.style.color=document.forms[0].col0_msg4_title_color.value }
      else if( getcolor != null ) { document.forms[0].col0_msg4_title_color.value = "#" + getcolor;msg_title_4.style.color=document.forms[0].col0_msg4_title_color.value }
      break;
    case 5:
      getcolor = showModalDialog(theURL,document.forms[0].col0_msg5_title_color.value);
      if( getcolor == ""   ) { document.forms[0].col0_msg5_title_color.value = "";msg_title_5.style.color=document.forms[0].col0_msg5_title_color.value }
      else if( getcolor != null ) { document.forms[0].col0_msg5_title_color.value = "#" + getcolor;msg_title_5.style.color=document.forms[0].col0_msg5_title_color.value }
      break;
    case 6:
      getcolor = showModalDialog(theURL,document.forms[0].col0_msg6_title_color.value);
      if( getcolor == ""   ) { document.forms[0].col0_msg6_title_color.value = "";msg_title_6.style.color=document.forms[0].col0_msg6_title_color.value }
      else if( getcolor != null ) { document.forms[0].col0_msg6_title_color.value = "#" + getcolor;msg_title_6.style.color=document.forms[0].col0_msg6_title_color.value }
      break;
    case 7:
      getcolor = showModalDialog(theURL,document.forms[0].col0_msg7_title_color.value);
      if( getcolor == ""   ) { document.forms[0].col0_msg7_title_color.value = "";msg_title_7.style.color=document.forms[0].col0_msg7_title_color.value }
      else if( getcolor != null ) { document.forms[0].col0_msg7_title_color.value = "#" + getcolor;msg_title_7.style.color=document.forms[0].col0_msg7_title_color.value }
      break;
    case 8:
      getcolor = showModalDialog(theURL,document.forms[0].col0_msg8_title_color.value);
      if( getcolor == ""   ) { document.forms[0].col0_msg8_title_color.value = "";msg_title_8.style.color=document.forms[0].col0_msg8_title_color.value }
      else if( getcolor != null ) { document.forms[0].col0_msg8_title_color.value = "#" + getcolor;msg_title_8.style.color=document.forms[0].col0_msg8_title_color.value }
      break;
    default:
      break;

  }
  //window.open(theURL,winName,features);
}

function MM_openBrWindow1(theURL,winName,features,colno) { //v2.0

  switch(colno)
  {
    case 0:
      getcolor = showModalDialog(theURL,document.forms[0].col1_title_color.value);
      if( getcolor == ""   ) { document.forms[0].col1_title_color.value = "";title_big.style.color=document.forms[0].col1_title_color.value }
      else if( getcolor != null ) { document.forms[0].col1_title_color.value = "#" + getcolor;title_big.style.color=document.forms[0].col1_title_color.value }
      break;
    case 1:
      getcolor = showModalDialog(theURL,document.forms[0].col1_msg1_title_color.value);
      if( getcolor == ""   ) { document.forms[0].col1_msg1_title_color.value = "";msg_title_1.style.color=document.forms[0].col1_msg1_title_color.value }
      else if( getcolor != null ) { document.forms[0].col1_msg1_title_color.value = "#" + getcolor;msg_title_1.style.color=document.forms[0].col1_msg1_title_color.value }
      break;
    case 2:
      getcolor = showModalDialog(theURL,document.forms[0].col1_msg2_title_color.value);
      if( getcolor == ""   ) { document.forms[0].col1_msg2_title_color.value = "";msg_title_2.style.color=document.forms[0].col1_msg2_title_color.value }
      else if( getcolor != null ) { document.forms[0].col1_msg2_title_color.value = "#" + getcolor;msg_title_2.style.color=document.forms[0].col1_msg2_title_color.value }
      break;
    case 3:
      getcolor = showModalDialog(theURL,document.forms[0].col1_msg3_title_color.value);
      if( getcolor == ""   ) { document.forms[0].col1_msg3_title_color.value = "";msg_title_3.style.color=document.forms[0].col1_msg3_title_color.value }
      else if( getcolor != null ) { document.forms[0].col1_msg3_title_color.value = "#" + getcolor;msg_title_3.style.color=document.forms[0].col1_msg3_title_color.value }
      break;
    case 4:
      getcolor = showModalDialog(theURL,document.forms[0].col1_msg4_title_color.value);
      if( getcolor == ""   ) { document.forms[0].col1_msg4_title_color.value = "";msg_title_4.style.color=document.forms[0].col1_msg4_title_color.value }
      else if( getcolor != null ) { document.forms[0].col1_msg4_title_color.value = "#" + getcolor;msg_title_4.style.color=document.forms[0].col1_msg4_title_color.value }
      break;
    case 5:
      getcolor = showModalDialog(theURL,document.forms[0].col1_msg5_title_color.value);
      if( getcolor == ""   ) { document.forms[0].col1_msg5_title_color.value = "";msg_title_5.style.color=document.forms[0].col1_msg5_title_color.value }
      else if( getcolor != null ) { document.forms[0].col1_msg5_title_color.value = "#" + getcolor;msg_title_5.style.color=document.forms[0].col1_msg5_title_color.value }
      break;
    case 6:
      getcolor = showModalDialog(theURL,document.forms[0].col1_msg6_title_color.value);
      if( getcolor == ""   ) { document.forms[0].col1_msg6_title_color.value = "";msg_title_6.style.color=document.forms[0].col1_msg6_title_color.value }
      else if( getcolor != null ) { document.forms[0].col1_msg6_title_color.value = "#" + getcolor;msg_title_6.style.color=document.forms[0].col1_msg6_title_color.value }
      break;
    case 7:
      getcolor = showModalDialog(theURL,document.forms[0].col1_msg7_title_color.value);
      if( getcolor == ""   ) { document.forms[0].col1_msg7_title_color.value = "";msg_title_7.style.color=document.forms[0].col1_msg7_title_color.value }
      else if( getcolor != null ) { document.forms[0].col1_msg7_title_color.value = "#" + getcolor;msg_title_7.style.color=document.forms[0].col1_msg7_title_color.value }
      break;
    case 8:
      getcolor = showModalDialog(theURL,document.forms[0].col1_msg8_title_color.value);
      if( getcolor == ""   ) { document.forms[0].col1_msg8_title_color.value = "";msg_title_8.style.color=document.forms[0].col1_msg8_title_color.value }
      else if( getcolor != null ) { document.forms[0].col1_msg8_title_color.value = "#" + getcolor;msg_title_8.style.color=document.forms[0].col1_msg8_title_color.value }
      break;
    default:
      break;

  }

  //window.open(theURL,winName,features);
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

function MM_openInput(input,hotelid,id){
  document.form1.target = '_self';
  document.form1.action = input+'_input.jsp?HotelId='+hotelid+'&Id='+id;
  document.form1.submit();
}

function MM_openInput2(input,hotelid,type,id,disp_flg){
  document.form1.target = '_self';
  document.form1.action = input+'_input.jsp?HotelId='+hotelid+'&DataType='+type+'&Id='+id+'&col_disp_flg='+disp_flg;
  document.form1.submit();
}


function ColFunc0(colno,hotelid) {
    switch(colno)
    {
      case 1:
        source = document.form1.col0_msg1.value;
        break;
      case 2:
        source = document.form1.col0_msg2.value;
        break;
      case 3:
        source = document.form1.col0_msg3.value;
        break;
      case 4:
        source = document.form1.col0_msg4.value;
        break;
      case 5:
        source = document.form1.col0_msg5.value;
        break;
      case 6:
        source = document.form1.col0_msg6.value;
        break;
      case 7:
        source = document.form1.col0_msg7.value;
        break;
      case 8:
        source = document.form1.col0_msg8.value;
        break;
      default:
        break;
    }
    source     = source.replace(/src="image/g, "src=\"http://www.hotenavi.com/" + hotelid + "/image");
    source     = source.replace(/strong>\r\n/g, "strong><BR>");
    source     = source.replace(/a>\r\n/g, "a><BR>");
    source     = source.replace(/font>\r\n/g, "font><BR>");
    source     = source.replace(/em>\r\n/g, "em><BR>");
    source     = source.replace(/u>\r\n/g, "u><BR>");
    source     = source.replace(/>/g, "%>%");
    source     = source.replace(/\r\n|\r|\n/g, "<BR>");
    source     = source.replace(/%>%<BR>/g, ">");
    source     = source.replace(/%>%/g, ">");
    switch(colno)
    {
      case 1:
        disp0Area1.innerHTML = source;
        break;
      case 2:
        disp0Area2.innerHTML = source;
        break;
      case 3:
        disp0Area3.innerHTML = source;
        break;
      case 4:
        disp0Area4.innerHTML = source;
        break;
      case 5:
        disp0Area5.innerHTML = source;
        break;
      case 6:
        disp0Area6.innerHTML = source;
        break;
      case 7:
        disp0Area7.innerHTML = source;
        break;
      case 8:
        disp0Area8.innerHTML = source;
        break;
      default:
        break;
    }
}


function ColFunc1(colno,hotelid) {
    switch(colno)
    {
      case 1:
        source = document.form1.col1_msg1.value;
        break;
      case 2:
        source = document.form1.col1_msg2.value;
        break;
      case 3:
        source = document.form1.col1_msg3.value;
        break;
      case 4:
        source = document.form1.col1_msg4.value;
        break;
      case 5:
        source = document.form1.col1_msg5.value;
        break;
      case 6:
        source = document.form1.col1_msg6.value;
        break;
      case 7:
        source = document.form1.col1_msg7.value;
        break;
      case 8:
        source = document.form1.col1_msg8.value;
        break;
      default:
        break;
    }
    source     = source.replace(/src="image/g, "src=\"http://www.hotenavi.com/" + hotelid + "/image");
    source     = source.replace(/strong>\r\n/g, "strong><BR>");
    source     = source.replace(/a>\r\n/g, "a><BR>");
    source     = source.replace(/font>\r\n/g, "font><BR>");
    source     = source.replace(/em>\r\n/g, "em><BR>");
    source     = source.replace(/u>\r\n/g, "u><BR>");
    source     = source.replace(/>/g, "%>%");
    source     = source.replace(/\r\n|\r|\n/g, "<BR>");
    source     = source.replace(/%>%<BR>/g, ">");
    source     = source.replace(/%>%/g, ">");
    switch(colno)
    {
      case 1:
        disp1Area1.innerHTML = source;
        break;
      case 2:
        disp1Area2.innerHTML = source;
        break;
      case 3:
        disp1Area3.innerHTML = source;
        break;
      case 4:
        disp1Area4.innerHTML = source;
        break;
      case 5:
        disp1Area5.innerHTML = source;
        break;
      case 6:
        disp1Area6.innerHTML = source;
        break;
      case 7:
        disp1Area7.innerHTML = source;
        break;
      case 8:
        disp1Area8.innerHTML = source;
        break;
      default:
        break;
    }
}

function MM_openBrWindowDetail0(theURL,winName,features,colno) { //v2.0
  switch(colno)
  {
    case 1:
      getcolor = showModalDialog(theURL,document.forms[0].span0col1.value);
      if( getcolor != null && getcolor != "") { document.forms[0].span0col1.value = "#" + getcolor;enclose('<font color="' + document.getElementById('span0col1').value + '">', '</font>') }
      break;
    case 2:
      getcolor = showModalDialog(theURL,document.forms[0].span0col2.value);
      if( getcolor != null && getcolor != "") { document.forms[0].span0col2.value = "#" + getcolor;enclose('<font color="' + document.getElementById('span0col2').value + '">', '</font>') }
      break;
    case 3:
      getcolor = showModalDialog(theURL,document.forms[0].span0col3.value);
      if( getcolor != null && getcolor != "") { document.forms[0].span0col3.value = "#" + getcolor;enclose('<font color="' + document.getElementById('span0col3').value + '">', '</font>') }
      break;
    case 4:
      getcolor = showModalDialog(theURL,document.forms[0].span0col4.value);
      if( getcolor != null && getcolor != "") { document.forms[0].span0col4.value = "#" + getcolor;enclose('<font color="' + document.getElementById('span0col4').value + '">', '</font>') }
      break;
    case 5:
      getcolor = showModalDialog(theURL,document.forms[0].span0col5.value);
      if( getcolor != null && getcolor != "") { document.forms[0].span0col5.value = "#" + getcolor;enclose('<font color="' + document.getElementById('span0col5').value + '">', '</font>') }
      break;
    case 6:
      getcolor = showModalDialog(theURL,document.forms[0].span0col6.value);
      if( getcolor != null && getcolor != "") { document.forms[0].span0col6.value = "#" + getcolor;enclose('<font color="' + document.getElementById('span0col6').value + '">', '</font>') }
      break;
    case 7:
      getcolor = showModalDialog(theURL,document.forms[0].span0col7.value);
      if( getcolor != null && getcolor != "") { document.forms[0].span0col7.value = "#" + getcolor;enclose('<font color="' + document.getElementById('span0col7').value + '">', '</font>') }
      break;
    case 8:
      getcolor = showModalDialog(theURL,document.forms[0].span0col8.value);
      if( getcolor != null && getcolor != "") { document.forms[0].span0col8.value = "#" + getcolor;enclose('<font color="' + document.getElementById('span0col8').value + '">', '</font>') }
      break;
    default:
      break;
  }
}
function MM_openBrWindowDetail1(theURL,winName,features,colno) { //v2.0
  switch(colno)
  {
    case 1:
      getcolor = showModalDialog(theURL,document.forms[0].span1col1.value);
      if( getcolor != null && getcolor != "") { document.forms[0].span1col1.value = "#" + getcolor;enclose('<font color="' + document.getElementById('span1col1').value + '">', '</font>') }
      break;
    case 2:
      getcolor = showModalDialog(theURL,document.forms[0].span1col2.value);
      if( getcolor != null && getcolor != "") { document.forms[0].span1col2.value = "#" + getcolor;enclose('<font color="' + document.getElementById('span1col2').value + '">', '</font>') }
      break;
    case 3:
      getcolor = showModalDialog(theURL,document.forms[0].span1col3.value);
      if( getcolor != null && getcolor != "") { document.forms[0].span1col3.value = "#" + getcolor;enclose('<font color="' + document.getElementById('span1col3').value + '">', '</font>') }
      break;
    case 4:
      getcolor = showModalDialog(theURL,document.forms[0].span1col4.value);
      if( getcolor != null && getcolor != "") { document.forms[0].span1col4.value = "#" + getcolor;enclose('<font color="' + document.getElementById('span1col4').value + '">', '</font>') }
      break;
    case 5:
      getcolor = showModalDialog(theURL,document.forms[0].span1col5.value);
      if( getcolor != null && getcolor != "") { document.forms[0].span1col5.value = "#" + getcolor;enclose('<font color="' + document.getElementById('span1col5').value + '">', '</font>') }
      break;
    case 6:
      getcolor = showModalDialog(theURL,document.forms[0].span1col6.value);
      if( getcolor != null && getcolor != "") { document.forms[0].span1col6.value = "#" + getcolor;enclose('<font color="' + document.getElementById('span1col6').value + '">', '</font>') }
      break;
    case 7:
      getcolor = showModalDialog(theURL,document.forms[0].span1col7.value);
      if( getcolor != null && getcolor != "") { document.forms[0].span1col7.value = "#" + getcolor;enclose('<font color="' + document.getElementById('span1col7').value + '">', '</font>') }
      break;
    case 8:
      getcolor = showModalDialog(theURL,document.forms[0].span1col8.value);
      if( getcolor != null && getcolor != "") { document.forms[0].span1col8.value = "#" + getcolor;enclose('<font color="' + document.getElementById('span1col8').value + '">', '</font>') }
      break;
    default:
      break;
  }
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

//���͂���Ă���N�����A���̌��̍ŏI���t���Z�o
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
		
//���͂���Ă��錎�ɂ���āA�ŏI���t��ύX�B���肦�Ȃ����t�̏ꍇ�ɂ́A�ŏI���t�ɋ����I�ɕϊ��B
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

//���͂���Ă���N�����A���̌��̍ŏI���t���Z�o
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
		
//���͂���Ă��錎�ɂ���āA�ŏI���t��ύX�B���肦�Ȃ����t�̏ꍇ�ɂ́A�ŏI���t�ɋ����I�ɕϊ��B
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

function validation_range(mobile,data_type){
	var input_years = parseInt(form1.col_start_yy.value,10);
	var input_months = parseInt(form1.col_start_mm.value,10);
	var input_days = parseInt(form1.col_start_dd.value,10);
		
	var input_yeare = parseInt(form1.col_end_yy.value,10);
	var input_monthe = parseInt(form1.col_end_mm.value,10);
	var input_daye = parseInt(form1.col_end_dd.value,10);


	if  (input_yeare < input_years)
		     {
			 alert("���Ԏw��i�J�n���t���I�����t�j�𐳂������͂��Ă�������");
			 document.form1.col_start_yy.focus();
			 return false;
			 }
	else
	   if  (input_yeare == input_years)
	   { 
		if (input_monthe < input_months)
		     {
			 alert("���Ԏw��i�J�n���t���I�����t�j�𐳂������͂��Ă�������");
			 document.form1.col_start_yy.focus();
			 return false;
			 }
		else if  (input_monthe == input_months && input_daye < input_days)
		     {
			 alert("���Ԏw��i�J�n���t���I�����t�j�𐳂������͂��Ă�������");
			 document.form1.col_start_yy.focus();
			 return false;
			 }
       }
		
    return true;
}

function CheckEdit(){
	if (
		(document.form1.old_title.value != document.form1.col_title.value)           ||
		(document.form1.old_msg1_title.value != document.form1.col_msg1_title.value) ||
		(document.form1.old_msg2_title.value != document.form1.col_msg2_title.value) ||
		(document.form1.old_msg3_title.value != document.form1.col_msg3_title.value) ||
		(document.form1.old_msg4_title.value != document.form1.col_msg4_title.value) ||
		(document.form1.old_msg5_title.value != document.form1.col_msg5_title.value) ||
		(document.form1.old_msg6_title.value != document.form1.col_msg6_title.value) ||
		(document.form1.old_msg7_title.value != document.form1.col_msg7_title.value) ||
		(document.form1.old_msg8_title.value != document.form1.col_msg8_title.value) ||
		(document.form1.old_msg1.value != document.form1.col_msg1.value)             ||
		(document.form1.old_msg2.value != document.form1.col_msg2.value)             ||
		(document.form1.old_msg3.value != document.form1.col_msg3.value)             ||
		(document.form1.old_msg4.value != document.form1.col_msg4.value)             ||
		(document.form1.old_msg5.value != document.form1.col_msg5.value)             ||
		(document.form1.old_msg6.value != document.form1.col_msg6.value)             ||
		(document.form1.old_msg7.value != document.form1.col_msg7.value)             ||
		(document.form1.old_msg8.value != document.form1.col_msg8.value)
	   )
	{
		document.form1.BackUp.checked=true;
	}
	else
	{
		document.form1.BackUp.checked=false;
	}
}

