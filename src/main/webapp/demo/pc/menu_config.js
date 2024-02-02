function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_openBrWindow(theURL,winName,features,colno) { //v2.0

  switch(colno)
  {
    case 0:
      getcolor = showModalDialog(theURL,document.forms[0].col_title_color.value);
      if( getcolor != null )  document.forms[0].col_title_color.value = "#" + getcolor;
      break;
    case 1:
      getcolor = showModalDialog(theURL,document.forms[0].col_msg1_title_color.value);
      if( getcolor != null )  document.forms[0].col_msg1_title_color.value = "#" + getcolor;
      break;
    case 2:
      getcolor = showModalDialog(theURL,document.forms[0].col_msg2_title_color.value);
      if( getcolor != null )  document.forms[0].col_msg2_title_color.value = "#" + getcolor;
      break;
    case 3:
      getcolor = showModalDialog(theURL,document.forms[0].col_msg3_title_color.value);
      if( getcolor != null )  document.forms[0].col_msg3_title_color.value = "#" + getcolor;
      break;
    case 4:
      getcolor = showModalDialog(theURL,document.forms[0].col_msg4_title_color.value);
      if( getcolor != null )  document.forms[0].col_msg4_title_color.value = "#" + getcolor;
      break;
    case 5:
      getcolor = showModalDialog(theURL,document.forms[0].col_msg5_title_color.value);
      if( getcolor != null )  document.forms[0].col_msg5_title_color.value = "#" + getcolor;
      break;
    case 6:
      getcolor = showModalDialog(theURL,document.forms[0].col_msg6_title_color.value);
      if( getcolor != null )  document.forms[0].col_msg6_title_color.value = "#" + getcolor;
      break;
    case 7:
      getcolor = showModalDialog(theURL,document.forms[0].col_msg7_title_color.value);
      if( getcolor != null )  document.forms[0].col_msg7_title_color.value = "#" + getcolor;
      break;
    case 8:
      getcolor = showModalDialog(theURL,document.forms[0].col_msg8_title_color.value);
      if( getcolor != null )  document.forms[0].col_msg8_title_color.value = "#" + getcolor;
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
    document.form1.action = 'menu_config_edit_preview.jsp?DataType='+ type;
  }
  document.form1.submit();
}

function MM_openInput(input,hotelid,type,id){
  if( input == 'input' )
  {
    document.form1.target = '_self';
    document.form1.action = 'menu_config_edit_input.jsp?HotelId='+hotelid+'&DataType='+type+'&Id='+id;
  }
  document.form1.submit();
}
