function datacheck()
{
    if( document.forms[0].LoginId.value == "" )
    {
        alert('���[�U�[������͂��Ă�������');
        return false;
    }
    else if( document.forms[0].Password.value == "" )
    {
        alert('�p�X���[�h����͂��Ă�������');
        return false;
    }
}


