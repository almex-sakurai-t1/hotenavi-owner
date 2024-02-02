function user_datacheck()
{
    if( document.forms["userform"].loginid.value == "" )
    {
        alert('ユーザ名を入力ください');
        return false;
    }

    if( document.forms["userform"].passwd_pc.value != document.forms["userform"].passwd_pc_re.value )
    {
        alert('PCパスワードと確認入力のパスワードが違います');
        return false;
    }

    if( document.forms["userform"].passwd_mobile.value != document.forms["userform"].passwd_mobile_re.value )
    {
        alert('携帯パスワードと確認入力のパスワードが違います');
        return false;
    }
}

