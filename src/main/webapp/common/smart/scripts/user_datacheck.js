function user_datacheck()
{
    if( document.forms["userform"].loginid.value == "" )
    {
        alert('���[�U������͂�������');
        return false;
    }
}

