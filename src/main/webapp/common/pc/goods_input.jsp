<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // ÉZÉbÉVÉáÉìÇÃämîF
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<%
    }
%>
<%
    String loginhotel = (String)session.getAttribute("LoginHotelId");
    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);

    int result_new = 0;
    DateEdit  de = new DateEdit();

    // ì˙ït√ﬁ∞¿ÇÃï“èW
    int start_ymd = 0;
    int end_ymd = 0;
    int old_ymd = 0;

    int start_yy = Integer.parseInt(ReplaceString.getParameter(request,"col_start_yy"));
    int start_mm = Integer.parseInt(ReplaceString.getParameter(request,"col_start_mm"));
    int start_dd = Integer.parseInt(ReplaceString.getParameter(request,"col_start_dd"));
    start_ymd = start_yy*10000 + start_mm*100 + start_dd;

    int end_yy = Integer.parseInt(ReplaceString.getParameter(request,"col_end_yy"));
    int end_mm = Integer.parseInt(ReplaceString.getParameter(request,"col_end_mm"));
    int end_dd = Integer.parseInt(ReplaceString.getParameter(request,"col_end_dd"));
    end_ymd = end_yy*10000 + end_mm*100 + end_dd;
    old_ymd = de.addDay(start_ymd , -1);

    String BackUp        = ReplaceString.getParameter(request,"BackUp");
    if    (BackUp == null) BackUp="0";


    // ÉzÉeÉãIDéÊìæ
    String hotelid = (String)session.getAttribute("SelectHotel");

    String Seq              = ReplaceString.getParameter(request,"col_seq");
    int    seq              = Integer.parseInt(Seq);
    int    new_seq          = 0;
    String query ="";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();

    if( seq == 0 || BackUp.compareTo("1") == 0)
    {
        new_seq     = 1;
        try
        {
            query = "SELECT * FROM goods WHERE hotelid= ? ";
            query = query + " ORDER BY seq DESC";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            result      = prestate.executeQuery();
            if( result.next() != false )
            {
                new_seq  = result.getInt("seq") + 1;
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
    String CategoryId       = ReplaceString.getParameter(request,"col_category_id");
    int    category_id      = Integer.parseInt(CategoryId);
    String DispIdx          = ReplaceString.getParameter(request,"col_disp_idx");
    if (!CheckString.numCheck(DispIdx)) DispIdx = "999";
    int    disp_idx         = Integer.parseInt(DispIdx);
    String title            = ReplaceString.getParameter(request,"col_title");
    String title_color      = ReplaceString.getParameter(request,"col_title_color");
    String title_sub1       = ReplaceString.getParameter(request,"col_title_sub1");
    String title_sub1_color = ReplaceString.getParameter(request,"col_msg1_title_color");
    String msg              = ReplaceString.getParameter(request,"col_msg");
    String Point            = ReplaceString.getParameter(request,"col_point");
    if (!CheckString.numCheck(Point)) Point = "99999";
    int    point            = Integer.parseInt(Point);
    String CountFlag        = ReplaceString.getParameter(request,"col_count_flag");
    if (CountFlag == null)  CountFlag = "0";
    int    count_flag       = Integer.parseInt(CountFlag);
    String CountMax         = ReplaceString.getParameter(request,"col_count_max");
    if (!CheckString.numCheck(CountMax)) CountMax = "10";
    int    count_max        = Integer.parseInt(CountMax);
    String CountUnit        = ReplaceString.getParameter(request,"col_count_unit");
    if (!CheckString.numCheck(CountUnit)) CountUnit = "1";
    int    count_unit       = Integer.parseInt(CountUnit);
    String picture_pc       = ReplaceString.getParameter(request,"col_picture_pc");
    String picture_gif      = ReplaceString.getParameter(request,"col_picture_gif");
    String SuggestFlag      = ReplaceString.getParameter(request,"col_suggest_flag");
    if (SuggestFlag == null)  SuggestFlag = "0";
    int    suggest_flag     = Integer.parseInt(SuggestFlag);
    String SuggestIdx       = ReplaceString.getParameter(request,"col_suggest_idx");
    if (SuggestIdx == null)                SuggestIdx = "999";
    if (!CheckString.numCheck(SuggestIdx)) SuggestIdx = "999";
    int    suggest_idx      = Integer.parseInt(SuggestIdx);

    try
    {

    if( seq == 0 || BackUp.compareTo("1") == 0)
    {
        query = "INSERT INTO goods SET ";
        query = query + "hotelid=?, ";
        query = query + "seq=?, ";
    }
    else
    {
        query = "UPDATE goods SET ";
    }
        query = query + "category_id=?, ";
        query = query + "disp_idx=?, ";
        query = query + "title=?, ";
        query = query + "title_color=?, ";
        query = query + "title_sub1=?, ";
        query = query + "title_sub1_color=?, ";
        query = query + "msg=?, ";
        query = query + "point=?, ";
        query = query + "count_flag=?, ";
        query = query + "count_max=?, ";
        query = query + "count_unit=?, ";
        query = query + "picture_pc=?, ";
        query = query + "picture_gif=?, ";
        query = query + "disp_from=?, ";
        query = query + "disp_to=?, ";
        query = query + "suggest_flag=?, ";
        query = query + "suggest_idx=?, ";
        query = query + "upd_hotelid=?, ";
        query = query + "upd_userid=?, ";
        query = query + "last_update=?, ";
        query = query + "last_uptime=? ";
    if( seq == 0 || BackUp.compareTo("1") == 0)
    {
        query = query + ",add_hotelid=?, ";
        query = query + "memo='', ";
        query = query + "msg_user='', ";
        query = query + "msg_mail='', ";
        query = query + "add_userid=?, ";
        query = query + "add_date=?, ";
        query = query + "add_time=? ";
    }
    else
    {
        query = query + " WHERE hotelid=? AND seq=?";
    }
    prestate    = connection.prepareStatement(query);

    int i = 1;

    if( seq == 0 || BackUp.compareTo("1") == 0)
    {
        prestate.setString(i++, hotelid);
        prestate.setInt(i++, new_seq);
    }
    prestate.setInt(i++, category_id);
    prestate.setInt(i++, disp_idx);
    prestate.setString(i++, ReplaceString.SQLEscape(title));
    prestate.setString(i++, ReplaceString.SQLEscape(title_color));
    prestate.setString(i++, ReplaceString.SQLEscape(title_sub1));
    prestate.setString(i++, ReplaceString.SQLEscape(title_sub1_color));
    prestate.setString(i++, ReplaceString.SQLEscape(msg));
    prestate.setInt(i++, point);
    prestate.setInt(i++, count_flag);
    prestate.setInt(i++, count_max);
    prestate.setInt(i++, count_unit);
    prestate.setString(i++, ReplaceString.SQLEscape(picture_pc));
    prestate.setString(i++, ReplaceString.SQLEscape(picture_gif));
    prestate.setInt(i++, start_ymd);
    prestate.setInt(i++, end_ymd);
    prestate.setInt(i++, suggest_flag);
    prestate.setInt(i++, suggest_idx);
    prestate.setString(i++, loginhotel);
    prestate.setInt(i++, ownerinfo.DbUserId);
    prestate.setInt(i++, nowdate);
    prestate.setInt(i++, nowtime);

    if( seq == 0 || BackUp.compareTo("1") == 0)
    {
	    prestate.setString(i++, loginhotel);
	    prestate.setInt(i++, ownerinfo.DbUserId);
	    prestate.setInt(i++, nowdate);
	    prestate.setInt(i++, nowtime);
    }
    else
    {
	    prestate.setString(i++, hotelid);
	    prestate.setInt(i++, seq);
    }
    result_new  = prestate.executeUpdate();
    }
    catch( Exception e )
    {
       Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
       DBConnection.releaseResources(prestate);
    }

    if (BackUp.compareTo("1") == 0)
    {
        try
        {
        query = "UPDATE goods SET ";
        query = query + "disp_to=?, ";
        query = query + "upd_hotelid=?, ";
        query = query + "upd_userid=?, ";
        query = query + "last_update=?, ";
        query = query + "last_uptime=? ";
        query = query + "WHERE hotelid=? AND seq=? ";
        prestate    = connection.prepareStatement(query);
        prestate.setInt(1, old_ymd);
        prestate.setString(2, loginhotel);
        prestate.setInt(3, ownerinfo.DbUserId);
        prestate.setInt(4, nowdate);
        prestate.setInt(5, nowtime);
        prestate.setString(6, hotelid);
        prestate.setInt(7, seq);
        result_new  = prestate.executeUpdate();
        }
        catch( Exception e )
        {
           Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        }
        finally
        {
           DBConnection.releaseResources(prestate);
        }
    }




    DBConnection.releaseResources(connection);
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>åiïiìoò^</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/access.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/coupon.js"></script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="100" height="20" nowrap bgcolor="#22333F" class="tab">
                <font color="#FFFFFF">ìoò^ämîF</font></td>
              <td width="15" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="../../common/pc/image/spacer.gif" width="200" height="20"></div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- Ç±Ç±Ç©ÇÁï\ -->
      <tr>
        <td align="center" valign="top" bgcolor="#FFFFFF"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><img src="../../common/pc/image/spacer.gif" width="400" height="12"></td>
          </tr>
          <tr>
            <td width="8"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="size12">
<%
    if( result_new != 0 )
    {
%>
ìoò^ÇµÇ‹ÇµÇΩÅB<br>
<%
    }
    else
    {
%>
ìoò^Ç…é∏îsÇµÇ‹ÇµÇΩÅB
<%
    }

%>
                    </td>
                  </tr>
                  <tr>
                    <td class="size12">&nbsp;</td>
                  </tr>
                  <tr>
                    <td class="size12">
                    <form action="goods_list_detail.jsp?HotelId=<%= hotelid %>&CategoryId=<%= category_id %>" method="POST">
                      <INPUT name="submit_ret" type=submit value=ñﬂÇÈ >
                    </form></td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
        </table>
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td></td>
              </tr>
            </table>
        </td>
        <td width="3" valign="top" align="left" height="100%">
          <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><img src="../../common/pc/image/tab_kado.gif" width="3" height="3"></td>
            </tr>
            <tr>
              <td bgcolor="#666666" height="100%"><img src="../../common/pc/image/spacer.gif" width="3" height="100"></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
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
      <!-- Ç±Ç±Ç‹Ç≈ -->
    </table></td>
  </tr>
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/Library/footer.lbi" --><img src="../../common/pc/image/imedia.gif" width="63" height="18" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyright&copy; almex
      inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
