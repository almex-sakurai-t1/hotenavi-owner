<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.happyhotel.common.ConvertHtml" %>
<%@ page import="jp.happyhotel.common.DBConnection" %>
<%@ page import="jp.happyhotel.common.Logging" %>
<%@ page import="jp.happyhotel.common.DateEdit" %>
<%@ page import="jp.happyhotel.common.HotelElement" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String hotelid = (String)session.getAttribute("SelectHotel");
    if( hotelid == null )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }

    boolean    TargetAll  = false;
    if  (hotelid.compareTo("all") == 0)
    {
         TargetAll = true;
         hotelid   = "demo";
    }


    String     loginHotelId  = (String)session.getAttribute("LoginHotelId");

    String Type      = request.getParameter("Type");
    if    (Type     == null)
    {
           Type      = "pc";
    }
    int        type               = 0; //0:PC 1:携帯
    if (Type.compareTo("pc") != 0)
    {
               type               = 1; //携帯
    }

    int        imedia_user        = 0;
    String     query              = "";
    boolean    ExistFlag          = false;

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();

    // imedia_user のチェック
    try
    {
        query = "SELECT * FROM owner_user WHERE hotelid='" + loginHotelId + "'";
        query = query + " AND userid=" + ownerinfo.DbUserId;
        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            imedia_user = result.getInt("imedia_user");
        }
    }
    catch( Exception e )
    {
        Logging.info("foward Exception e=" + e.toString() );
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
%>
<%@ include file="../../common/pc/menu_ini.jsp" %>
<%
    boolean updateMode = false;
    if (request.getParameter("submit") != null)
    {
        if (request.getParameter("submit").equals("変換"))
        {
            updateMode = true;
        }
    }
    String hotelName = "";
    query = "SELECT name FROM hotel";
    query += " WHERE hotel_id = ?";
    prestate = connection.prepareStatement(query);
    prestate.setString( 1, hotelid);
    result   = prestate.executeQuery();
    if( result != null )
    {
        if( result.next() != false )
        {
            hotelName = result.getString("name");
        }
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);

    DateEdit  de          = new DateEdit();
    int nowdate   = Integer.parseInt(de.getDate(2));
    int trialDate = HotelElement.getTrialDate(hotelid);
    // HP編集メニュー名称の書き換え
    try
    {
        query = "SELECT * FROM menu WHERE hotelid='" + hotelid + "'";
        query = query + " AND data_type=1";
        query = query + " AND start_date <=" + nowdate;
        query = query + " AND end_date >=" + nowdate;
        query = query + " AND disp_flg =1";
        query = query + " AND hpedit_id>0";
        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery();
        while( result.next() != false )
        {
            Title[type][result.getInt("hpedit_id")-1]=result.getString("title");
        }
    }
    catch( Exception e )
    {
        Logging.info("foward Exception e=" + e.toString() );
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    int count= 0;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>html変換</title>
<link href="contents.css" rel="stylesheet" type="text/css">
</head>
<body>
<div>
	<form name="form0" id="form0" action="" method="post">
	<table cellspacing="0" bgcolor="#ffffff"  border="1" cellpadding="0" bordercolor="#666666" width="100%">
	<tr>
		<td colspan=5>
	ホテルID:<input type="hidden" name="hotelid" id="hotelid" value="<%=hotelid%>" style="text-align:left" onChange="if (this.value!=''){readHotel(this.value);}" size=8>
			<span id="hotelName"><%=hotelName%></span>
			<input type="submit" name="submit" id="submit"  value="変換" <%if(updateMode){%>style="display:none;"<%}%>>
		</td>
	</tr>
<%
    query  = "SELECT * FROM edit_event_info";
    query  +=" WHERE hotelid = ?";
    query  +=" AND   data_type%2 = 1";
    prestate = connection.prepareStatement(query);
    prestate.setString( 1, hotelid );
    result   = prestate.executeQuery();
    while( result.next() )
    {
        java.sql.Date start = result.getDate("start_date");
        java.sql.Date end   = result.getDate("end_date");
        int start_date = (start.getYear()+1900)*10000 + (start.getMonth()+1)*100 + start.getDate();
        int end_date = (end.getYear()+1900)*10000 + (end.getMonth()+1)*100 + end.getDate();

        String convertStr = "";
        String temp = result.getString("title");
        if (!temp.equals(ConvertHtml.convertFontToCss(temp)))
        {
            if (!convertStr.equals("")) convertStr +="<hr>";
            convertStr += temp + "<br>";
            convertStr += "↓<br>";
            convertStr += ConvertHtml.convertFontToCss(temp) +"<br>";
        }
        for (int i = 1; i <=8 ; i++)
        {
            temp = result.getString("msg"+ i + "_title");
            if (!temp.equals(ConvertHtml.convertFontToCss(temp)))
            {
                if (!convertStr.equals("")) convertStr +="<hr>";
                convertStr += temp + "<br>";
                convertStr += "↓<br>";
                convertStr += ConvertHtml.convertFontToCss(temp) +"<br>";
            }
            temp = result.getString("msg"+ i);
            if (!temp.equals(ConvertHtml.convertFontToCss(temp)))
            {
                if (!convertStr.equals("")) convertStr +="<hr>";
                convertStr += temp + "<br>";
                convertStr += "↓<br>";
                convertStr += ConvertHtml.convertFontToCss(temp) +"<br>";
            }
        }
        if (result.getInt("data_type") == 5 && start_date < trialDate && result.getInt("disp_flg") != 9 && end_date != DateEdit.addDay(trialDate,-1) && end_date >= nowdate && result.getInt("disp_idx") != -9999 )
        {
               convertStr +=  "data_type=1（What's New）になければコピーをする<br>";
        }

        if (!convertStr.equals(""))
        {
            count++;
            if (count == 1)
            {
%>			<tr>
				<td class="hoteldetail_cel" width="50">type</td>
				<td class="hoteldetail_cel" width="50">index</td>
				<td class="hoteldetail_cel" width="50">disp</td>
				<td class="hoteldetail_cel">日付</td>
				<td>変換</td>
			</tr>
<%          }
        }
        if (!convertStr.equals(""))
        {
%>			<tr>
				<td class="hoteldetail_cel" width="50"><%=result.getInt("data_type")%>:<%=getTitle(result.getInt("data_type"),Title,MenuNo)%></td>
				<td class="hoteldetail_cel" width="50"><%=result.getInt("disp_idx")%></td>
				<td class="hoteldetail_cel" width="50"><%=result.getInt("disp_flg")%></td>
				<td class="hoteldetail_cel"><%= result.getDate("start_date")%>～<%= result.getDate("end_date")%></td>
				<td class="hoteldetail_cel">
<%
             if (updateMode)
             {
                 if (ConvertHtml.convertFontToCss(connection,hotelid,result.getInt("data_type"),result.getInt("id"),loginHotelId,ownerinfo.DbUserId))
                 {
%>					<strong>変換完了</strong>
<%
                 }
             }
%>                <%=convertStr%>
				</td>
			</tr>
<%
        }
    }
    if (count == 0)
    {
%>	<tr>
				<td class="hoteldetail_cel">変換対象コンテンツはありません</td>
	</tr>
<%
    }
    DBConnection.releaseResources(result,prestate,connection);
%>
    </table>
	</form>
</div>
</body>
</html>
<%!
    private static String getTitle(int dataType, String[][] title,int[][] menuNo) {
        String retStr = "";
        for (int i=0; i < title[0].length; i++)
        {
            if (menuNo[0][i]== dataType)
            {
                retStr = title[0][i];
                break;
            }
        }
        return retStr;
    }
%>
