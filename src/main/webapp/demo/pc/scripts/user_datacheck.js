function user_datacheck()
{
    if( document.forms["userform"].loginid.value == "" )
    {
        alert('���[�U������͂�������');
        return false;
    }

    if( document.forms["userform"].passwd_pc.value != document.forms["userform"].passwd_pc_re.value )
    {
        alert('PC�p�X���[�h�Ɗm�F���͂̃p�X���[�h���Ⴂ�܂�');
        return false;
    }

    if( document.forms["userform"].passwd_mobile.value != document.forms["userform"].passwd_mobile_re.value )
    {
        alert('�g�уp�X���[�h�Ɗm�F���͂̃p�X���[�h���Ⴂ�܂�');
        return false;
    }
}

