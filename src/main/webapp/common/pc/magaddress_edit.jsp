<%@ page contentType="text/html; charset=Windows-31J"%><%@ page import="java.sql.*" %><%@ page import="java.text.*" %><%@ page import="java.util.*" %><%@ page import="jp.happyhotel.common.CheckString" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="com.hotenavi2.custom.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<jsp:useBean id="custominfo" scope="session" class="com.hotenavi2.custom.CustomInfo" />
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
%><%
    DateEdit  de          = new DateEdit();
    NumberFormat nf2      = new DecimalFormat("00");
    DateFormat cal1 = DateFormat.getDateInstance();
    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    String header_msg = "";
    String error_msg = "";
    String query = "";
    // ﾊﾟﾗﾒｰﾀ
    int update_flag       = 0;
    int update_week       = 0;
    int update_force      = 0;
    String message        = "";
    String key_word       = "";
    int last_update       = 0;
    int last_uptime       = 0;
    String address        = "";
    String address_mailto    = "";
    String mag_address    = "";
    int member_only       = 0;
    int group_cancel_flag = 0;
    int group_add_flag    = 0;
    int change_flag       = 0;
    int delete_flag       = 0;
    int add_flag          = 0;
    int report_mail_flag  = 0;
    String target_hotelid = "";
    String customId       = "";

    String hotelid = "";
    // ホテルID取得
    String selecthotel = (String)session.getAttribute("SelectHotel");
    if    (selecthotel.compareTo("all") == 0)
    {
           hotelid    = loginHotelId;
    }
    else
    {
           hotelid    = selecthotel;
    }
    custominfo.HotelId = hotelid;


    String param_mode     = "";
    param_mode = ReplaceString.getParameter(request,"mode");
    if (param_mode == null)
    {
        param_mode = "inquiry";
    }
    if (param_mode.equals("inquiry") || param_mode.equals("upload"))
    {
        header_msg = "照会";
    }
    else if (param_mode.equals("update"))
    {
        header_msg = "更新";
    }
    else if (param_mode.equals("new"))
    {
        header_msg = "登録";
    }

    String InfoMailAddress = "";
    String OldMailAddress = "";
    String Birthday = "";
    String Birthday1 = "00";
    String Birthday2 = "00";
    boolean MemberCheck = false;

    customId = ReplaceString.getParameter(request,"CustomId");
    if (customId == null)
    {
        customId = "";
    }
    if (CheckString.numCheck(customId))
    {
        custominfo.CustomId = customId;
        custominfo.Birthday1 = "00";
        custominfo.Birthday2 = "00";
        custominfo.UserId = "";
        custominfo.Password = "";
        if (custominfo.sendPacket1038())
        {
           if (custominfo.Result == 1)
           {
                MemberCheck = true;
                if (custominfo.sendPacket1002())
                {
                    if (custominfo.Result != 1)
                    {
                        MemberCheck = false;
                        custominfo.InfoMailAddress = "";
                    }
                }
                else
                {
                    custominfo.InfoMailAddress = "";
                }
           }
           else
           {
               custominfo.InfoMailAddress = "";
           }
        }
        else
        {
            custominfo.InfoMailAddress = "";
        }
    }
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();

    if (param_mode.equals("upload") && ReplaceString.getParameter(request,"target_hotelid") != null)
    {
        target_hotelid = ReplaceString.getParameter(request,"target_hotelid");

        String ftp_password = "";
        String ftp_server = "172.25.2.81";
        query = "SELECT * FROM hotel WHERE hotel_id =?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,target_hotelid);
        result      = prestate.executeQuery();
        if( result.next())
        {
            ftp_password = result.getString("ftp_passwd");
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);

        copyTrans(ftp_server,target_hotelid,ftp_password);

        File checkFile = new File("/hotenavi/" + target_hotelid + "/pc/magaddress_edit.jsp");
        if(!checkFile.exists())
        {
            error_msg += "コンテンツのアップロードに失敗しました<br>";
        }
%><%=error_msg%><br><%
    }
    if (param_mode.equals("upload"))
    {
        param_mode = "inquiry";
    }
    boolean magUpdate = false;
    int state = 0;
    int last_senddate = 0;
    int last_sendtime = 0;
    int unknown_flag = 0;

    if (param_mode.equals("update"))
    {
        InfoMailAddress = ReplaceString.getParameter(request,"InfoMailAddress");
        if (InfoMailAddress == null)
        {
            InfoMailAddress = "";
        }
        OldMailAddress = ReplaceString.getParameter(request,"OldMailAddress");
        if (OldMailAddress == null)
        {
            OldMailAddress = "";
        }
        boolean isMailAddress = false;
        query = "SELECT * FROM mag_address WHERE hotel_id=?";
        query = query + " AND address=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, selecthotel);
        prestate.setString(2, MailAddressEncrypt.encrypt(InfoMailAddress));
        result      = prestate.executeQuery();
        try
        {
            if( result.next() != false )
            {
                isMailAddress = true;
            }
        }
        catch( Exception e )
        {
            Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }
        if (!InfoMailAddress.matches("^[a-zA-Z0-9!#$%&'_`/=~\\*\\+\\-\\?\\^\\{\\|\\}]+(\\.[a-zA-Z0-9!#$%&'_`/=~\\*\\+\\-\\?\\^\\{\\|\\}]+)*+(.*)@[a-zA-Z0-9][a-zA-Z0-9\\-]*(\\.[a-zA-Z0-9\\-]+)+$"))
        {
            error_msg += "メールアドレスが正しくありません<br>";
        }
        else if (isMailAddress && !InfoMailAddress.equals(OldMailAddress) && !customId.equals(custominfo.CustomId))
        {
            error_msg += "メールアドレス登録済みです"+custominfo.CustomId+"<br>";
        }
        else
        {
            if (custominfo.HotelId != null && !custominfo.HotelId.equals(""))
            {
                if (!InfoMailAddress.equals(OldMailAddress))
                {
                    custominfo.MailReplaceAddress = InfoMailAddress;
                    if (custominfo.sendPacket1018())
                    {
                        if (custominfo.Result == 1)
                        {
                            custominfo.InfoMailAddress = InfoMailAddress;
                        }
                    }
                }
            }
             query = "custom_id = ? ";
             query = query + ",user_id = ? ";
             query = query + ",password = ? ";
             query = query + ",birthday1 = ? ";
             query = query + ",birthday2 = ? ";
             query = query + ",memorial1 = ? ";
             query = query + ",memorial2 = ? ";
             query = query + ",last_date = ? ";
             query = query + ",use_count = ? ";
             query = query + ",use_total = ? ";
             query = query + ",point = ? ";
             query = query + ",rank = ? ";
             query = query + ",last_update="   + de.getDate(2);
             query = query + ",last_uptime="   + de.getTime(1);
             query = query + ",state = 1";
             query = query + ",unknown_flag = 0";

             if (isMailAddress)
             {
                 prestate = connection.prepareStatement("UPDATE mag_address SET " + query + " WHERE hotel_id = ? AND address= ?");
                 prestate.setString(1, custominfo.CustomId);
                 prestate.setString(2, custominfo.UserId);
                 prestate.setString(3, custominfo.Password);
                 prestate.setInt(4, custominfo.InfoBirthday1);
                 prestate.setInt(5, custominfo.InfoBirthday2);
                 prestate.setInt(6, custominfo.InfoMemorial1);
                 prestate.setInt(7, custominfo.InfoMemorial2);                 
                 prestate.setInt(8, custominfo.InfoLastDay);
                 prestate.setInt(9, custominfo.InfoUseCount);
                 prestate.setInt(10, custominfo.InfoUseTotal);
                 prestate.setInt(11, custominfo.InfoPoint);
                 prestate.setInt(12, custominfo.InfoRankCode);
                 prestate.setString(13, selecthotel);
                 prestate.setString(14, MailAddressEncrypt.encrypt(InfoMailAddress));
             }
             else
             {
                 prestate = connection.prepareStatement("INSERT INTO mag_address SET hotel_id = ? ,address = ? ,"  + query);
                 prestate.setString(1, selecthotel);
                 prestate.setString(2, MailAddressEncrypt.encrypt(InfoMailAddress));
                 prestate.setString(3, custominfo.CustomId);
                 prestate.setString(4, custominfo.UserId);
                 prestate.setString(5, custominfo.Password);
                 prestate.setInt(6, custominfo.InfoBirthday1);
                 prestate.setInt(7, custominfo.InfoBirthday2);
                 prestate.setInt(8, custominfo.InfoMemorial1);
                 prestate.setInt(9, custominfo.InfoMemorial2);                 
                 prestate.setInt(10, custominfo.InfoLastDay);
                 prestate.setInt(11, custominfo.InfoUseCount);
                 prestate.setInt(12, custominfo.InfoUseTotal);
                 prestate.setInt(13, custominfo.InfoPoint);
                 prestate.setInt(14, custominfo.InfoRankCode);
              }
             if (prestate.executeUpdate() == 1)
             {
                  error_msg += "メールアドレスを更新しました<br>";
             }
             DBConnection.releaseResources(prestate);


             query = "INSERT INTO mag_address_history SET hotel_id = ? ,address = ? ";
             query = query + ",custom_id = ?";
             query = query + ",user_id = ?";
             query = query + ",password = ?";
             query = query + ",birthday1= ? ";
             query = query + ",birthday2= ? ";
             query = query + ",regist_date="   + de.getDate(2);
             query = query + ",regist_time="   + de.getTime(1);
             query = query + ",state = 1 ";
             query = query + ",unknown_flag = 0 ";
             query = query + ",user_agent = ? ";
             query = query + ",remote_ip = ? ";
             query = query + ",url = ? ";
             prestate = connection.prepareStatement(query);
             prestate.setString(1, selecthotel);
             prestate.setString(2, MailAddressEncrypt.encrypt(InfoMailAddress));
             prestate.setString(3, custominfo.CustomId);
             prestate.setString(4, custominfo.UserId);
             prestate.setString(5, custominfo.Password);
             prestate.setInt(6, custominfo.InfoBirthday1);
             prestate.setInt(7, custominfo.InfoBirthday2);
             prestate.setString(8, request.getHeader("user-agent"));
             prestate.setString(9, ( request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR").split( "," )[0] : request.getRemoteAddr() ));                 
             prestate.setString(10, request.getRequestURL().toString() );
             prestate.executeUpdate();
             DBConnection.releaseResources(prestate);
        }
    }
    if (param_mode.equals("new"))
    {
        InfoMailAddress = ReplaceString.getParameter(request,"InfoMailAddress");
        if (InfoMailAddress == null)
        {
            InfoMailAddress = "";
        }
        OldMailAddress = ReplaceString.getParameter(request,"OldMailAddress");
        if (OldMailAddress == null)
        {
            OldMailAddress = "";
        }
        Birthday  = ReplaceString.getParameter(request,"Birthday");
        if (Birthday == null)
        {
            Birthday = "";
        }
        if (Birthday.equals(""))
        {
            error_msg +=   "誕生日が入力されていません<br>";
        }
        else if (!CheckString.numCheck(Birthday))
        {
             error_msg +=  "誕生日を正しく入力してください<font size=1>（入力例:19910101 もしくは0101）</font><br>";
        }
        else if (Birthday.length()!=8 && Birthday.length()!=4)
        {
             error_msg += "誕生日を正しく入力してください<font size=1>（入力例:19910101 もしくは 0101）</font><br>";
        }
        else if (Birthday.length()==8)
        {
            if (Integer.parseInt(de.getDate(2))/10000 - Integer.parseInt(Birthday)/10000 > 100 || Integer.parseInt(de.getDate(2))/10000 - Integer.parseInt(Birthday)/10000 < 10)
            {
                 error_msg += "誕生日を正しく入力してください<font size=1>（入力例:19910101 もしくは 0101）</font><br>";
            }
            else
            {
                cal1.setLenient(false);
                String strBirthday = Integer.parseInt(Birthday)/10000 + "/" + Integer.parseInt(Birthday)/100%100 + "/" + Integer.parseInt(Birthday)%100;
                try {
                    cal1.parse(strBirthday);
                } catch (Exception e) {
                 error_msg += "誕生日を正しく入力してください<font size=1>（入力例:19910101 もしくは 0101）</font><br>";
                }
            }
        }
        else if (Birthday.length()==4)
        {
            if (Integer.parseInt(Birthday)/100 == 0 || Integer.parseInt(Birthday)/100 > 12)
            {
                error_msg += "誕生日を正しく入力してください<font size=1>（入力例:19910101 もしくは 0101）</font><br>";
            }
            else if (Integer.parseInt(Birthday)%100 == 0 || Integer.parseInt(Birthday)%100 > 31)
            {
                error_msg += "誕生日を正しく入力してください<font size=1>（入力例:19910101 もしくは 0101）</font><br>";
            }
        }
        if (error_msg.equals(""))
        {
            if (Birthday.length()==4)
            {
                custominfo.BirthdayYear = "0000";
                custominfo.Birthday1    = Birthday.substring(0,2);
                custominfo.Birthday2    = Birthday.substring(2,4);
            }
            else
            {
                custominfo.BirthdayYear = Birthday.substring(0,4);
                custominfo.Birthday1    = Birthday.substring(4,6);
                custominfo.Birthday2    = Birthday.substring(6,8);
            }
        }

        boolean isMailAddress = false;
        query = "SELECT * FROM mag_address WHERE hotel_id=?";
        query = query + " AND address=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, selecthotel);
        prestate.setString(2, InfoMailAddress);
        result      = prestate.executeQuery();
        try
        {
            if( result.next() != false )
            {
                isMailAddress = true;
            }
        }
        catch( Exception e )
        {
            Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }

        if (!InfoMailAddress.matches("^[a-zA-Z0-9!#$%&'_`/=~\\*\\+\\-\\?\\^\\{\\|\\}]+(\\.[a-zA-Z0-9!#$%&'_`/=~\\*\\+\\-\\?\\^\\{\\|\\}]+)*+(.*)@[a-zA-Z0-9][a-zA-Z0-9\\-]*(\\.[a-zA-Z0-9\\-]+)+$"))
        {
            error_msg += "メールアドレスが正しくありません<br>";
        }
        else if (isMailAddress)
        {
            error_msg += "メールアドレス登録済みです<br>";
        }
        else
        {
            if (custominfo.HotelId != null && !custominfo.HotelId.equals("") && error_msg.equals(""))
            {
                custominfo.InfoMailAddress = InfoMailAddress;
                custominfo.InfoMailMag = 1;
                boolean result_custom;
                custominfo.sendPacket1040();
                result_custom = custominfo.sendPacket1000();
                %><%=result_custom%><br><%
                if(custominfo.Result==1)
                {
                    result_custom = custominfo.sendPacket1002();
                    %><%=result_custom%><br><%
                    query = "INSERT INTO mag_address SET ";
                    query = query + " hotel_id = ? ";
                    query = query + ",address = ? ";
                    query = query + ",custom_id = ? ";
                    query = query + ",user_id = ? ";
                    query = query + ",password = ? ";
                    query = query + ",birthday1 = ? ";
                    query = query + ",birthday2 = ? ";
                    query = query + ",memorial1 = ? ";
                    query = query + ",memorial2 = ? ";
                    query = query + ",last_date = ? ";
                    query = query + ",use_count = ? ";
                    query = query + ",use_total = ? ";
                    query = query + ",point = ? ";
                    query = query + ",rank = ? ";
                    query = query + ",last_update="   + de.getDate(2);
                    query = query + ",last_uptime="   + de.getTime(1);
                    query = query + ",state = 1";
                    query = query + ",unknown_flag = 0";
                    prestate = connection.prepareStatement(query);
                    prestate.setString(1, selecthotel);
                    prestate.setString(2, MailAddressEncrypt.encrypt(InfoMailAddress));
                    prestate.setString(3, custominfo.CustomId);
                    prestate.setString(4, custominfo.UserId);
                    prestate.setString(5, custominfo.Password);
                    prestate.setInt(6, custominfo.InfoBirthday1);
                    prestate.setInt(7, custominfo.InfoBirthday2);
                    prestate.setInt(8, custominfo.InfoMemorial1);
                    prestate.setInt(9, custominfo.InfoMemorial2);                 
                    prestate.setInt(10, custominfo.InfoLastDay);
                    prestate.setInt(11, custominfo.InfoUseCount);
                    prestate.setInt(12, custominfo.InfoUseTotal);
                    prestate.setInt(13, custominfo.InfoPoint);
                    prestate.setInt(14, custominfo.InfoRankCode);
                      if (prestate.executeUpdate() == 1)
                    {
                        error_msg += "メールアドレスを更新しました<br>";
                    }
                    DBConnection.releaseResources(prestate);
                    query = "INSERT INTO mag_address_history SET hotel_id = ? ,address = ? ";
                    query = query + ",custom_id = ?";
                    query = query + ",user_id = ?";
                    query = query + ",password = ?";
                    query = query + ",birthday1= ? ";
                    query = query + ",birthday2= ? ";
                    query = query + ",regist_date="   + de.getDate(2);
                    query = query + ",regist_time="   + de.getTime(1);
                    query = query + ",state = 1 ";
                    query = query + ",unknown_flag = 0 ";
                    query = query + ",user_agent = ? ";
                    query = query + ",remote_ip = ? ";
                    query = query + ",url = ? ";
                    prestate = connection.prepareStatement(query);
                    prestate.setString(1, selecthotel);
                    prestate.setString(2, MailAddressEncrypt.encrypt(InfoMailAddress));
                    prestate.setString(3, custominfo.CustomId);
                    prestate.setString(4, custominfo.UserId);
                    prestate.setString(5, custominfo.Password);
                    prestate.setInt(6, custominfo.InfoBirthday1);
                    prestate.setInt(7, custominfo.InfoBirthday2);
                    prestate.setString(8, request.getHeader("user-agent"));
                    prestate.setString(9, ( request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR").split( "," )[0] : request.getRemoteAddr() ));                 
                    prestate.setString(10, request.getRequestURL().toString() );
                    prestate.executeUpdate();
                    DBConnection.releaseResources(prestate);
                }
            }
        }
    }

    if (MemberCheck && !custominfo.InfoMailAddress.equals(""))
    {
        query = "SELECT * FROM mag_address WHERE hotel_id=?";
        query = query + " AND address=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, selecthotel);
        prestate.setString(2, MailAddressEncrypt.encrypt(custominfo.InfoMailAddress));
        result      = prestate.executeQuery();
        try
        {
            if( result.next() != false )
            {
                state = result.getInt("state");
                last_senddate = result.getInt("last_senddate");
                last_sendtime = result.getInt("last_sendtime");
                unknown_flag = result.getInt("unknown_flag");
                if (last_senddate != 0)
                error_msg += "送信済みメールアドレスです<br>";
            }
        }
        catch( Exception e )
        {
            Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }
    }

    int               imedia_user = 0;
    int               level       = 0;
    // imedia_user のチェック
    try
    {
        query = "SELECT * FROM owner_user WHERE hotelid=?";
        query = query + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            imedia_user = result.getInt("imedia_user");
            level       = result.getInt("level");
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    // メンバーページ有無のチェック
    boolean member_flag = false;
    try
    {
        query = "SELECT * FROM menu WHERE hotelid=?";
        query = query + " AND contents='search.jsp'";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, selecthotel);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            member_flag = true;
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
    try
    {
        query = "SELECT * FROM menu_config WHERE hotelid=?";
        query = query + " AND data_type=2";
        query = query + " AND contents='mailmagazine'";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, selecthotel);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            member_flag = true;
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
    try
    {
        query = "SELECT * FROM mag_hotel WHERE hotel_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid);
        result      = prestate.executeQuery();
        if (result != null)
        {
            if( result.next() != false )
            {
                update_flag       = result.getInt("update_flag");
                update_week       = result.getInt("update_week");
                update_force      = result.getInt("update_force");
                message           = result.getString("message");
                key_word          = result.getString("key_word");
                last_update       = result.getInt("last_update");
                last_uptime       = result.getInt("last_uptime");
                address           = result.getString("address");
                mag_address       = result.getString("mag_address");
                address_mailto       = result.getString("address_mailto");
                member_only       = result.getInt("member_only");
                change_flag       = result.getInt("change_flag");
                group_cancel_flag = result.getInt("group_cancel_flag");
                delete_flag       = result.getInt("delete_flag");
                group_add_flag    = result.getInt("group_add_flag");
                add_flag          = result.getInt("add_flag");
                report_mail_flag  = result.getInt("report_mail_flag");
            }
            else
            {
                header_msg = "メールマガジン設定がされていません。";
            }
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }
%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>メールアドレス登録</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../<%= hotelid %>/pc/contents.css" rel="stylesheet" type="text/css">
<% if (loginHotelId.compareTo("demo") == 0){%>
<link href="http://www.hotenavi.com/<%= hotelid %>/contents.css" rel="stylesheet" type="text/css">
<%}%>
<script type="text/javascript" src="../../common/pc/scripts/mailmagazine_form.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/tohankaku.js"></script>

<script type="text/javascript">

function MM_Input(input){
  document.getElementById("mode").value= input;
  document.form1.target = '_self';
  document.form1.action = 'magaddress_edit.jsp';
  document.form1.submit();
}
</script>

</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >
<form name=form1 method=post>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="180" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF">メールアドレス登録</font></td>
              <td width="15" height="20"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="../../common/pc/image/spacer.gif" width="200" height="20"></div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- ここから表 -->
      <tr>
        <td align="center" valign="top" >
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr bgcolor="#FFFFFF">
              <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><img src="../../common/pc/image/spacer.gif" width="400" height="12"></td>
            </tr>
            <tr bgcolor="#FFFFFF">
              <td width="8"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
<%
if  (!header_msg.equals("更新") && !header_msg.equals("照会") && !header_msg.equals("登録"))
{
%>
				  <tr align="left">
                      <td align="left" valign="middle" bgcolor="#969EAD" colspan="2"><strong><%= header_msg %></strong></td>
                  </tr>

<%
}
else
{
%>
					<input type="hidden" id="mode" name="mode" value="inquiry">
				  <tr align="left">
                      <th align="left" valign="middle">
						メンバーNo.
					  </th>
                      <td align="left" valign="middle">
						<input type="text" name=CustomId value="<%=customId%>" maxlength=9 onchange="document.getElementById('submitInquiry').click();">
					  </td>
                  </tr>
				  <tr align="left">
                      <td align="left" valign="middle" bgcolor="#969EAD" colspan="2">
					  	<input id="submitInquiry"  type=button value="照会" onClick="MM_Input('inquiry');return false;">
					  </td>
                   </tr>
					<tr bgcolor="#FFFFFF">
						<td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
						<td><img src="../../common/pc/image/spacer.gif" width="400" height="12"></td>
					</tr>
<%
    if (MemberCheck){
        if (request.getRequestURL().toString().indexOf("_debug_") != -1)
        {
%>					  <tr align="left">
                      <th align="left" valign="middle">
						ニックネーム
					  </th>
                      <td align="left" valign="middle">
							<%=custominfo.NickName%>
					  </td>
					</tr>				  <tr align="left">
                      <th align="left" valign="middle">
						誕生日
					  </th>
                      <td align="left" valign="middle">
							<%if (!custominfo.BirthdayYear.equals("")){%><%=custominfo.BirthdayYear%>/<%}%><%=custominfo.Birthday1%>/<%=custominfo.Birthday2%>
					  </td>
					</tr>
<%
        }
%>	 				<tr align="left">
                      <th align="left" valign="middle">
						メールアドレス
					  </th>
                      <td align="left" valign="middle">
						<input type="hidden" name=OldMailAddress value="<%=custominfo.InfoMailAddress%>">					<input type="text" name=InfoMailAddress value="<%=custominfo.InfoMailAddress%>"><br>
						<%if (!error_msg.equals("")){%><font color=red><strong><%=error_msg%></strong></font><%}%>
						<%if (state == 0){magUpdate = true;%>メルマガ未登録<br>
						<%}else{%>
							<%if (state == 2){magUpdate = true;%>配信停止<br><%}
							  else if (unknown_flag == 1){magUpdate = true;%>Unkwnonメールアドレス<br><%}
							  else if (last_senddate!=0){%>最終送信日時：<%=last_senddate/10000%>/<%=nf2.format(last_senddate/100%100)%>/<%=nf2.format(last_senddate%100)%>　<%=nf2.format(last_sendtime/10000)%>:<%=nf2.format(last_sendtime/100%100)%>:<%=nf2.format(last_sendtime%100)%><br><%}else{magUpdate = true;}%>
						<%}%>
					  </td>
					</tr>
					<% if (magUpdate){%><tr align="left">
						<td align="left" valign="middle" bgcolor="#969EAD" colspan="2">
					  	<input name="regsubmit" type=button value="更新" onClick="MM_Input('update');return false;">
					</td>
                   </tr>
<%
                    }
    }
    else if (CheckString.numCheck(customId))
    {
%>	 				<tr align="left">
                      <th align="left" valign="middle" colspan=2>
						未登録です。ホテルコンピュータに登録してください。
					  </th>
                   </tr>
<%
    }
}
%>
					<tr bgcolor="#FFFFFF">
						<td valign="top">&nbsp;</td>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/owner/Library/footer.lbi" --><img src="../../common/pc/image/imedia.gif" width="63" height="18" border="0"><img src="../../common/pc/image/spacer.gif" width="12" height="18" align="absmiddle">Copyright&copy; almex
    inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
					</tr>
					<tr bgcolor="#FFFFFF">
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					</tr>
<%
   if (request.getRequestURL().toString().indexOf("_debug_") != -1)
   {
%>					<tr align="center" valign="middle">
						<td colspan="146" align="left" colspan="2">
<%@ page import="java.io.File" %>
<%
        boolean isMagaddressEdit = true;
        File checkFile = null;
        query = "SELECT DISTINCT(hotel.hotel_id),hotel.name FROM hotel,owner_user_hotel WHERE owner_user_hotel.accept_hotelid =?";
        query = query + " AND hotel.hotel_id = owner_user_hotel.hotelid";
        connection  = DBConnection.getConnection();
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery();
        while( result.next())
        {
             checkFile = new File("/hotenavi/" + result.getString("hotel.hotel_id") + "/pc/magaddress_edit.jsp");
%>
						<input type="radio" name="target_hotelid" value="<%=result.getString("hotel.hotel_id")%>" <%if(checkFile.exists()){%>disabled<%}%>><%=result.getString("hotel.name")%>(<%=result.getString("hotel.hotel_id")%>)<%if(checkFile.exists()){%><font size=-1 color=red>*アップロード済み</font><%}else{isMagaddressEdit= false;}%><br>
<%
        }
        DBConnection.releaseResources(result,prestate,connection);
        String ipAddr = (request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR").split( "," )[0] : request.getRemoteAddr());
        if (!isMagaddressEdit && !ipAddr.equals("172.25.35.11"))
        {
%>
		<input name="magaddress_edit_submit" type="button" value="メルマガ登録機能追加" onClick="MM_Input('upload');return false;"><br>
<%
        }
%>
						</td>
					</tr>
<%
   }
%>                </table>
              </td>
              </tr>
            </table>
        </td>
        <td width="3" valign="top" align="left" height="100%">
          <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><img src="new/image/tab_kado.gif" width="3" height="3"></td>
            </tr>
            <tr>
              <td bgcolor="#666666" height="100%"><img src="../../common/pc/image/spacer.gif" width="3" height="100"></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr bgcolor="#FFFFFF">
        <td height="3" bgcolor="#999999">
          <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="3"><img src="../../common/pc/image/tab_kado2.gif" width="3" height="3"></td>
              <td bgcolor="#666666"><img src="../../common/pc/image/spacer.gif" width="100" height="3"></td>
            </tr>
          </table>
        </td>
        <td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="3"></td>
      </tr>
      <!-- ここまで -->
    </table></td>
  </tr>
</table>
</form>
</body>
</html>
<%@ page import="java.io.File" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.FileOutputStream" %>
<%@ page import="org.apache.commons.net.ftp.FTPClient" %>
<%@ page import="org.apache.commons.net.ftp.FTPReply" %>
<%!
public static void copyTrans(String host,String user,String password) throws IOException {
    FTPClient fp = new FTPClient();
    FileInputStream is = null;
    try
    {
        fp.connect(host);
        if (!FTPReply.isPositiveCompletion(fp.getReplyCode())) { // コネクトできたか？
            System.out.println("connection failed");
            System.exit(1); // 異常終了
        }

        if (fp.login(user, password) == false) { // ログインできたか？
             System.out.println("login failed");
             System.exit(1); // 異常終了
        }
// ファイル送信
        is = new FileInputStream("/hotenavi/_debug_/demo/pc/magaddress_edit.jsp");
        fp.storeFile("/pc/magaddress_edit.jsp", is);
        is.close();
        is = new FileInputStream("/hotenavi/_debug_/demo/pc/magaddress_edit.jsp");
        fp.storeFile("/smartpc/magaddress_edit.jsp", is);
        is.close();

        System.out.println("FTP PUT COMPLETED");
    }
   finally
   {
       fp.disconnect();
   }
 }
%>