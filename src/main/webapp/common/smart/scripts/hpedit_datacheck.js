function datacheck()
{
    if( document.forms["selectstore"].Store.value == "all" || document.forms["selectstore"].Store.value == "" )
    {
        alert('店舗を選択してください');
        return false;
    }
}
