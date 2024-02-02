<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %><%@ page import="jp.happyhotel.common.HotelMailmagazine" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/SyncGcp_ini.jsp" %>
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String loginhotel = (String)session.getAttribute("LoginHotelId");
    HotelMailmagazine hotelMailmagazine = new HotelMailmagazine();
    boolean isMailmagazine = false;


    DbAccess db =  new DbAccess();

    // セキュリティ情報の取得
    ResultSet DbUserSecurity = ownerinfo.getUserSecurity(db, loginhotel, ownerinfo.DbUserId);

    // セキュリティチェック
    if( DbUserSecurity == null )
    {
%>
        <jsp:forward page="../../common/pc/error/notlogin.html" />
<%
    }
    if( DbUserSecurity.first() == false )
    {
%>
        <jsp:forward page="../../common/pc/error/notlogin.html" />
<%
    }

    String  query;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();


    // 最終ログイン日時取得
    String lastlogin = "";
    try
    {
        query = "SELECT * FROM owner_user_login WHERE hotelid=?";
        query = query + " AND userid=?";
        query = query + " AND kind = 1";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,loginhotel);
        prestate.setInt(2,ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            lastlogin = result.getDate("last_login_date") +
                        "&nbsp;" +
                        result.getTime("last_login_time");
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
    if (lastlogin.equals(""))
    {
        try
        {
            query = "SELECT * FROM owner_user_log WHERE hotelid=?";
            query = query + " AND userid=?";
            query = query + " ORDER BY logid DESC";
            query = query + " LIMIT 1,1";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1,loginhotel);
            prestate.setInt(2,ownerinfo.DbUserId);
            result      = prestate.executeQuery();
            if( result.next() != false )
            {
            lastlogin = result.getDate("login_date") +
                        "&nbsp;" +
                        result.getTime("login_time");
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
    }
    if (lastlogin.equals(""))
    {
        lastlogin = "なし";
    }

    int               imedia_user = 0;
    int               level       = 0;
    int               passwd_pc_update= 0;

    // imedia_user のチェック
    try
    {
        query = "SELECT * FROM owner_user WHERE hotelid=?";
        query = query + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,loginhotel);
        prestate.setInt(2,ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            imedia_user      = result.getInt("imedia_user");
            level            = result.getInt("level");
            passwd_pc_update = result.getInt("passwd_pc_update");
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


//  カスタム・オーナープランの閲覧可能件数を算出
    int       count  = 0;
    try
    {
        query = "SELECT count(*) FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid =?";
        query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
        query = query + " AND hotel.plan <= 2";
        query = query + " AND hotel.plan >= 1";
        query = query + " AND owner_user_hotel.userid = ?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,loginhotel);
        prestate.setInt(2,ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
           count = result.getInt(1);
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

//  カスタム・エクストラ・オフラインプランの閲覧可能件数を算出
    int       count_e  = 0;
    try
    {
        query = "SELECT count(*) FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid =?";
        query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
        query = query + " AND hotel.plan <= 4";
        query = query + " AND hotel.plan >= 1";
        query = query + " AND hotel.plan != 2";
        query = query + " AND owner_user_hotel.userid = ?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,loginhotel);
        prestate.setInt(2,ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
           count_e = result.getInt(1);
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
//  ハッピー・ホテル　スタンダードコース件数を算出
    int count_h = 0;
    try
    {
        query = "SELECT count(*) FROM hh_hotel_basic,owner_user_hotel WHERE owner_user_hotel.hotelid =?";
        query = query + " AND hh_hotel_basic.hotenavi_id = owner_user_hotel.accept_hotelid";
        query = query + " AND hh_hotel_basic.rank >= 2";
        query = query + " AND owner_user_hotel.userid = ?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,loginhotel);
        prestate.setInt(2,ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
           count_h = result.getInt(1);
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }

    if(hotelMailmagazine.getManageHotel(loginhotel,ownerinfo.DbUserId))
    {
      isMailmagazine = hotelMailmagazine.getHotelCount() == 0 ? false : true;
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<META http-equiv="Content-Style-Type" content="text/css">
<title>オーナーサイトメニュー</title>
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
<link rel="stylesheet" href="../../common/pc/style/head.css" type="text/css">
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="parent.Main.location.href='page.html'" >
<table width="100%" height="69" border="0" cellpadding="0" cellspacing="0" background="../../common/pc/image/grade_h1.gif">
  <tr>
    <td>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="49" align="left"><img src="../../common/pc/image/hotel_owner.jpg" alt="ホテルオーナーサイト" width="365" height="49"></td>
          <td height="49" align="left">&nbsp;</td>
          <td height="49" align="right" nowrap>
            <div class="sama">
              <%= ownerinfo.DbUserName %> 様 &nbsp;&nbsp;<a href="loginhistory.jsp?UserId=<%= ownerinfo.DbUserId %>" target="Main" class="logout">(最終ログイン：<%= lastlogin %>)</a>
            </div>
            <div class="logout">
<%
              if (count_h != 0)
              {
%>
              <a href="../../common/pc/owner_happyhotel.jsp" target=_blank class="sama">&gt;&gt;&nbsp;ハピホテへ<img src="../../common/pc/image/hapihote_logo.gif" width="66" height="19" border="0" alt="ハピホテ"></a>&nbsp;&nbsp;
<%
              }
%>
              <a href="ownerlogout.jsp" target="_parent" class="sama">&gt;&gt;&nbsp;ログアウト</a>
            </div>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td height="20" align="left" valign="top" bgcolor="#000000">
      <TABLE height="20" border="0" cellpadding="0" cellspacing="0">
        <tr align="left" valign="top">

<!-- ユーザレベルによってメニューを切り替える -->
<%
    if( DbUserSecurity != null && passwd_pc_update != 0 )
//    if( DbUserSecurity != null)
    {
        if(( (DbUserSecurity.getInt("sec_level01") == 1 || DbUserSecurity.getInt("admin_flag") == 1) && count != 0 ) || (DbUserSecurity.getInt("sec_level01") == 1 && level == 3)  )
        {
%>
          <td width="72" height="22" nowrap><img src="../../common/pc/image/spacer.gif" width="5" height="20">
			<a href="sales_f.html" target="Main" onMouseOver="MM_swapImage('uriagekanri','','../../common/pc/image/m_kanri_o.gif',1)" onMouseOut="MM_swapImgRestore()">
			<img alt="売上管理" src="../../common/pc/image/m_kanri.gif" name="uriagekanri" width="57" height="20" border="0" id="uriagekanri">
			</a>
			<img src="../../common/pc/image/spacer.gif" width="5" height="20">
		  </td>
          <td width="1" bgcolor="#FFFFFF"><img src="../../common/pc/image/spacer.gif" width="1" height="20"></td>
<%
        }
        if(( (DbUserSecurity.getInt("sec_level02") == 1 || DbUserSecurity.getInt("admin_flag") == 1) && count != 0) ||  (DbUserSecurity.getInt("sec_level02") == 1 && level == 3) )
        {
%>
          <td width="72" height="22" nowrap><img src="../../common/pc/image/spacer.gif" width="5" height="20">
			<a href="room_f.html" target="Main" onMouseOver="MM_swapImage('heya','','../../common/pc/image/m_heya_o.gif',1)" onMouseOut="MM_swapImgRestore()">
			<img alt="部屋情報" src="../../common/pc/image/m_heya.gif" name="heya" width="56" height="20" border="0" id="heya">
			</a>
			<img src="../../common/pc/image/spacer.gif" width="5" height="20">
		  </td>
          <td width="1" bgcolor="#FFFFFF"><img src="../../common/pc/image/spacer.gif" width="1" height="20"></td>
<%
        }
        if(( (DbUserSecurity.getInt("sec_level03") == 1 || DbUserSecurity.getInt("admin_flag") == 1)  && count != 0) ||  (DbUserSecurity.getInt("sec_level03") == 1 && level == 3) )
        {
%>
          <td width="72" height="22" nowrap><img src="../../common/pc/image/spacer.gif" width="5" height="20">
			<a href="report_f.html" target="Main"  onMouseOver="MM_swapImage('choyo','','../../common/pc/image/chohyo_f2.gif',1)" onMouseOut="MM_swapImgRestore()">
			<img alt="帳票管理" src="../../common/pc/image/chohyo.gif" name="choyo" width="59" height="20" border="0" id="choyo">
			</a>
			<img src="../../common/pc/image/spacer.gif" width="5" height="20">
		  </td>
          <td width="1" bgcolor="#FFFFFF"><img src="../../common/pc/image/spacer.gif" width="1" height="20"></td>
<%
        }
        if(( (DbUserSecurity.getInt("sec_level04") == 1  || DbUserSecurity.getInt("admin_flag") == 1)  && count_e != 0) ||  (DbUserSecurity.getInt("sec_level04") == 1 && level == 3))
        {
%>
          <td width="86" height="22" nowrap><img src="../../common/pc/image/spacer.gif" width="5" height="20">
			<a href="hpreport_f.html" target="Main" onMouseOver="MM_swapImage('access','','../../common/pc/image/m_access_o.gif',1)" onMouseOut="MM_swapImgRestore()">
			<img  alt="HPレポート" src="../../common/pc/image/m_access.gif" name="access" width="74" height="20" border="0" id="access">
			</a>
			<img src="../../common/pc/image/spacer.gif" width="5" height="20">
		  </td>
          <td width="1"bgcolor="#FFFFFF"><img src="../../common/pc/image/spacer.gif" width="1" height="20"></td>
<%
        }
        if(((DbUserSecurity.getInt("sec_level05") == 1  || DbUserSecurity.getInt("admin_flag") == 1) && isMailmagazine) ||  (DbUserSecurity.getInt("sec_level05") == 1 && level == 3))
        {
%>
          <td width="100" height="22" nowrap><img src="../../common/pc/image/spacer.gif" width="5" height="20">
			<a href="magazine_f.html" target="Main" onMouseOver="MM_swapImage('zine','','../../common/pc/image/m_zine_o.gif',1)" onMouseOut="MM_swapImgRestore()">
			<img  alt="メールマガジン" src="../../common/pc/image/m_zine.gif" name="zine" width="86" height="20" border="0" id="zine">
			</a>
			<img src="../../common/pc/image/spacer.gif" width="5" height="20">
		  </td>
          <td width="1" bgcolor="#FFFFFF"><img src="../../common/pc/image/spacer.gif" width="1" height="20"></td>
<%
        }
        if(( (DbUserSecurity.getInt("sec_level06") == 1  || DbUserSecurity.getInt("admin_flag") == 1)  && count_e != 0) ||  (DbUserSecurity.getInt("sec_level06") == 1 && level == 3))
        {
%>
          <td width="64" height="22" nowrap><img src="../../common/pc/image/spacer.gif" width="5" height="20">
			<a href="hpedit_f.html" target="Main"  onMouseOver="MM_swapImage('hnsyu','','../../common/pc/image/m_m_hnsyu_o.gif',1)" onMouseOut="MM_swapImgRestore()">
			<img alt="HP編集" src="../../common/pc/image/m_m_hnsyu.gif" name="hnsyu" width="52" height="20" border="0" id="hnsyu">
			</a>
			<img src="../../common/pc/image/spacer.gif" width="5" height="20">
		  </td>
          <td width="1" bgcolor="#FFFFFF"><img src="../../common/pc/image/spacer.gif" width="1" height="20"></td>
<%
        }
		if( DbUserSecurity.getInt("sec_level11") == 1 )
        {
%>
          <td width="64" height="22" nowrap><img src="../../common/pc/image/spacer.gif" width="1" height="20">
			<a href="bbs_f.html" target="Main"  onMouseOver="MM_swapImage('bbs','','../../common/pc/image/m_bbs_o.gif',1)" onMouseOut="MM_swapImgRestore()">
			<img alt="掲示板" src="../../common/pc/image/m_bbs.gif" name="bbs" width="52" height="20" border="0" id="bbs">
			</a>
			<img src="../../common/pc/image/spacer.gif" width="5" height="20">
		  </td>
          <td width="1" bgcolor="#FFFFFF"><img src="../../common/pc/image/spacer.gif" width="1" height="20"></td>
<%
        }
        if(( (DbUserSecurity.getInt("sec_level07") == 1 || DbUserSecurity.getInt("admin_flag") == 1) && count != 0)||  (DbUserSecurity.getInt("sec_level07") == 1 && level == 3))
        {
%>
          <td width="100" height="22" nowrap><img src="../../common/pc/image/spacer.gif" width="5" height="20">
			<a href="master_f.html" target="Main" onMouseOver="MM_swapImage('settei','','../../common/pc/image/m_settei_o.gif',1);" onMouseOut="MM_swapImgRestore()">
			<img alt="設定メニュー" src="../../common/pc/image/m_settei.gif" name="settei" width="82" height="20" border="0" id="settei">
			</a>
			<img src="../../common/pc/image/spacer.gif" width="5" height="20">
		  </td>
          <td width="1" bgcolor="#FFFFFF"><img src="../../common/pc/image/spacer.gif" width="1" height="20"></td>
<%
        }
        if( DbUserSecurity.getInt("sec_level08") == 1)
        {
%>
          <td width="110" height="22" nowrap><img src="../../common/pc/image/spacer.gif" width="5" height="20">
			<a href="manageeye_f.html" target="Main" onMouseOver="MM_swapImage('Image1','','../../common/pc/image/manage_f2.gif',1)" onMouseOut="MM_swapImgRestore()">
			<img alt="マネージアイ" src="../../common/pc/image/manage.gif" name="Image1" width="94" height="20" border="0" id="Image1">
			</a>
			<img src="../../common/pc/image/spacer.gif" width="5" height="20">
		  </td>
          <td width="1" bgcolor="#FFFFFF"><img src="../../common/pc/image/spacer.gif" width="1" height="20"></td>
<%
        }
        if( DbUserSecurity.getInt("admin_flag") == 1 )
        {
%>
		  <td width="100" height="22" nowrap><img src="../../common/pc/image/spacer.gif" width="5" height="20">
			<a href="user_f.html" target="Main"  onMouseOver="MM_swapImage('settei1','','../../common/pc/image/m_user_o.gif',1)" onMouseOut="MM_swapImgRestore()">
			<img alt="ユーザー管理" src="../../common/pc/image/m_user.gif" name="settei1" width="82" height="20" border="0" id="settei1">
			</a>
			<img src="../../common/pc/image/spacer.gif" width="5" height="20"></td>
		  <td width="1" bgcolor="#FFFFFF"><img src="../../common/pc/image/spacer.gif" width="1" height="20"></td>
<%
        }
        else
        {
%>
		  <td width="100" height="22" nowrap><img src="../../common/pc/image/spacer.gif" width="5" height="20">
			<a href="user_f.html" target="Main"  onMouseOver="MM_swapImage('settei1','','../../common/pc/image/m_user_o.gif',1)" onMouseOut="MM_swapImgRestore()">
			<img alt="ユーザー管理" src="../../common/pc/image/m_user.gif" name="settei1" width="82" height="20" border="0" id="settei1">
			</a>
			<img src="../../common/pc/image/spacer.gif" width="5" height="20"></td>
          <td width="1" bgcolor="#FFFFFF"><img src="../../common/pc/image/spacer.gif" width="1" height="20"></td>
<%
        }
%>
          <td width="58" height="22" nowrap><img src="../../common/pc/image/spacer.gif" width="5" height="20">
			<img alt="ヘルプ" src="../../common/pc/image/m_help.gif" name="help" width="44" height="20" id="help" onClick="MM_openBrWindow('../../common/pc/help.html','よくある質問','scrollbars=yes,resizable=yes,width=640,height=335,height=335')" onMouseOver="MM_swapImage('help','','../../common/pc/image/m_help_o.gif',1)" onMouseOut="MM_swapImgRestore()">
			<img src="../../common/pc/image/spacer.gif" width="5" height="20">
		  </td>
          <td width="1" bgcolor="#FFFFFF"><img src="../../common/pc/image/spacer.gif" width="1" height="20"></td>
<%
    }
%>
<!-- ユーザレベルによってメニューを切り替える -->

        </tr>
      </TABLE>
    </td>
  </tr>
</table>

<%
    db.close();
%>

</body>
</html>
