function datacheck()
{
    if( document.forms["selectstore"].Store.value == "all" || document.forms["selectstore"].Store.value == "" )
    {
        alert('�X�܂�I�����Ă�������');
        return false;
    }
}