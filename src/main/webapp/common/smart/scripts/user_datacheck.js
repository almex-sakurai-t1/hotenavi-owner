function user_datacheck()
{
    if( document.forms["userform"].loginid.value == "" )
    {
        alert('ユーザ名を入力ください');
        return false;
    }
}

