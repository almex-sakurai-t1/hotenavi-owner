function dialog()
{
    flag = confirm("このクーポンを削除します。よろしいですか？");
    if (flag==true) alert("削除しました。");
    else alert("データは残ったままです");
}

function MM_openPreview(input,hotelid,type,id){
  if( input == 'preview' )
  {
    document.form1.target = '_blank';
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
