
function disableRightClick(e)
{
  var message = "右クリックより対象のファイルを保存を選択してください";
  
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
