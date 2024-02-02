//�ǂݍ��񂾂Ƃ��ɂ̓��͕s�ӏ��̃`�F�b�N
function disabledCheck() {
    setCheck(document.getElementById("CustomidCheck"),document.getElementById("CustomidStart"),document.getElementById("CustomidEnd"));
    setCheck(document.getElementById("MailaddressCheck"),document.getElementById("Mailaddress"),document.getElementById("Mailaddress"));
    setCheck(document.getElementById("CountCheck"),document.getElementById("CountStart"),document.getElementById("CountEnd"));
    setCheck(document.getElementById("TotalCheck"),document.getElementById("TotalStart"),document.getElementById("TotalEnd"));
    setCheck(document.getElementById("PointCheck"),document.getElementById("PointStart"),document.getElementById("PointEnd"));
    setCheck(document.getElementById("Point2Check"),document.getElementById("Point2Start"),document.getElementById("Point2End"));
    setCheck(document.getElementById("RankCheck"),document.getElementById("RankStart"),document.getElementById("RankEnd"));
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
              !document.getElementById("CustomidCheck").checked &&
              !document.getElementById("MailaddressCheck").checked &&
              !document.getElementById("BirthdayCheck").checked &&
              !document.getElementById("MemorialCheck").checked &&
              !document.getElementById("LastDayCheck").checked &&
              !document.getElementById("CountCheck").checked &&
              !document.getElementById("TotalCheck").checked &&
              !document.getElementById("PointCheck").checked &&
              !document.getElementById("Point2Check").checked &&
              !document.getElementById("RankCheck").checked &&
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
              !document.getElementById("CustomidCheck").checked &&
              !document.getElementById("MailaddressCheck").checked &&
              !document.getElementById("BirthdayCheck").checked &&
              !document.getElementById("MemorialCheck").checked &&
              !document.getElementById("LastDayCheck").checked &&
              !document.getElementById("CountCheck").checked &&
              !document.getElementById("TotalCheck").checked &&
              !document.getElementById("PointCheck").checked &&
              !document.getElementById("Point2Check").checked &&
              !document.getElementById("RankCheck").checked &&
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
