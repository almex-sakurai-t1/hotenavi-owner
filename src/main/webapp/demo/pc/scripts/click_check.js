
function disableRightClick(e)
{
  var message = "�E�N���b�N���Ώۂ̃t�@�C����ۑ���I�����Ă�������";
  
  if(!document.rightClickDisabled) // initialize
  {
    if(document.layers) 
    {
      document.captureEvents(Event.MOUSEDOWN);
      document.onmousedown = disableRightClick;
    }
    return document.rightClickDisabled = true;
  }
  if(document.layers || (document.getElementById && !document.all))
  {
    if (e.which==1)
    {
      alert(message);
      return false;
    }
  }
  else
  {
    alert(message);
    return false;
  }
}
disableRightClick();
