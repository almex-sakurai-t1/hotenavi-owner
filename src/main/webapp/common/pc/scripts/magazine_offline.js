//�ǂݍ��񂾂Ƃ��ɂ̓��͕s�ӏ��̃`�F�b�N
function disabledCheck() {
    setCheck(document.getElementById("MailaddressCheck"),document.getElementById("Mailaddress"),document.getElementById("Mailaddress"));
}


//�`�F�b�N�������ꍇ�ɁA�u���L�̏����Ɉ�v�����ꍇ�v�ɕύX����
function setCondition(obj,inputobj1,inputobj2) {
    var objc = document.getElementById("Condition");
    if(obj.checked)
    {
        objc.selectedIndex = objc.options.length - 1;
        objc.value = "3";
        objc.text  = "���L�̏����Ɉ�v����ꍇ";
    }
    else
    {
        inputobj1.value = "";
        inputobj2.value = "";
        obj.disabled = true;
        if (objc.value == 3)
        {
            if(
              !document.getElementById("MailaddressCheck").checked &&
              !document.getElementById("LastSendCheck").checked 
            )
            {
                objc.selectedIndex = 0;
                objc.value = "0";
                objc.text  = "�S����";
            }
        }
    }
}

//�`�F�b�N�������ꍇ�ɁA�u���L�̏����Ɉ�v�����ꍇ�v�ɕύX����
function setConditionCheckOnly(obj) {
    var objc = document.getElementById("Condition");
    if(obj.checked)
    {
        objc.selectedIndex = objc.options.length - 1;
        objc.value = "3";
        objc.text  = "���L�̏����Ɉ�v����ꍇ";
    }
    else
    {
        if (objc.value == 3)
        {
            if(
              !document.getElementById("MailaddressCheck").checked &&
              !document.getElementById("LastSendCheck").checked 
            )
            {
                objc.selectedIndex = 0;
                objc.value = "0";
                objc.text  = "�S����";
            }
        }
    }
}
//
function setCheck(obj,inputobj1,inputobj2) {
    if (inputobj1.value == "" && inputobj2.value == "")
    {
        obj.checked  = false;
        obj.disabled = true;
    }
    else
    {
        obj.checked  = true;
        obj.disabled = false;
    }
    setCondition(obj,inputobj1,inputobj2);
}
