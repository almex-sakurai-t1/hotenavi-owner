//読み込んだときにの入力不可箇所のチェック
function disabledCheck() {
    setCheck(document.getElementById("MailaddressCheck"),document.getElementById("Mailaddress"),document.getElementById("Mailaddress"));
}


//チェックをした場合に、「下記の条件に一致した場合」に変更する
function setCondition(obj,inputobj1,inputobj2) {
    var objc = document.getElementById("Condition");
    if(obj.checked)
    {
        objc.selectedIndex = objc.options.length - 1;
        objc.value = "3";
        objc.text  = "下記の条件に一致する場合";
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
                objc.text  = "全員に";
            }
        }
    }
}

//チェックをした場合に、「下記の条件に一致した場合」に変更する
function setConditionCheckOnly(obj) {
    var objc = document.getElementById("Condition");
    if(obj.checked)
    {
        objc.selectedIndex = objc.options.length - 1;
        objc.value = "3";
        objc.text  = "下記の条件に一致する場合";
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
                objc.text  = "全員に";
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
