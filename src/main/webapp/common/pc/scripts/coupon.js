function dialog()
{
    flag = confirm("���̃N�[�|�����폜���܂��B��낵���ł����H");
    if (flag==true) alert("�폜���܂����B");
    else alert("�f�[�^�͎c�����܂܂ł�");
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
function setCouponShow(o) {
	var form = document.createElement("form");
	form.setAttribute("action", "coupon_edit.jsp");
    form.setAttribute("method", "GET");
    
    // �G�������g���쐬
    var ele = document.createElement('input');
    // �f�[�^��ݒ�
    ele.setAttribute('type', 'hidden');
    ele.setAttribute('name', 'showExpiredCoupon');
    
    var value = "0";
	if (o.checked) {
		value = "1";
	}
    ele.setAttribute('value', value);
    
    form.appendChild(ele);
    document.body.appendChild(form);
    form.submit();
}