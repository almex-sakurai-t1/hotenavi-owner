function datacheck()
{
    if( document.forms[0].LoginId.value == "" )
    {
        alert('ユーザー名を入力してください');
        return false;
    }
    else if( document.forms[0].Password.value == "" )
    {
        alert('パスワードを入力してください');
        return false;
    }
}


