<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="com.hotenavi2.kitchen.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    boolean FrontTexExist = true;
    boolean TexExist      = true;
    boolean MultiMediaExist = true;
    boolean KitchenExist  = false;
    KitchenInfo kitcheninfo = new KitchenInfo();
 
    int storecount = Integer.parseInt((String)session.getAttribute("StoreCount"));

    String     loginHotelId  = (String)session.getAttribute("LoginHotelId");
    String     selecthotel   = "";
    selecthotel = request.getParameter("Store");
    if  (selecthotel == null)
    {
        selecthotel = (String)session.getAttribute("SelectHotel");
    }
    if  (selecthotel == null || selecthotel.equals(""))
    {
        selecthotel = "all";
    }
    if(!CheckString.numAlphaCheck(selecthotel))
    {
        response.sendError(400);
        return;
    }

    int        imedia_user        = 0;
    int        level              = 0;
    int        count              = 0;
    int        sec_level01        = 0;

    String     query              = "";

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();

    //  店舗数のチェック
    try
    {
        query = "SELECT count(*),hotel.hotel_id FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid =?";
        query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
        query = query + " AND owner_user_hotel.userid = ?";
        query = query + " AND hotel.plan <= 2";
        query = query + " AND hotel.plan >= 1";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if  (result != null)
        {
            if( result.next() != false )
            {
                storecount   = result.getInt(1);
                if (storecount == 1)
                {
                    selecthotel = result.getString("hotel.hotel_id");
                }
            }
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

    // imedia_user のチェック
    try
    {
        query = "SELECT * FROM owner_user,owner_user_security WHERE owner_user.hotelid='" + loginHotelId + "'";
        query = query + " AND owner_user.userid=" + ownerinfo.DbUserId;
        query = query + " AND owner_user.hotelid=owner_user_security.hotelid";
        query = query + " AND owner_user.userid=owner_user_security.userid";
        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            imedia_user      = result.getInt("owner_user.imedia_user");
            sec_level01      = result.getInt("owner_user_security.sec_level01");
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

    // ホスト種別取得
    int host_kind = 0;
    boolean TimeChartFlag = false;
    int neoVer = 0;

    if( selecthotel.compareTo("all") != 0 )
    {
        query = "SELECT * FROM hotel WHERE hotel_id=?";

        prestate    = connection.prepareStatement(query);
        prestate.setString(1, selecthotel);
        result      = prestate.executeQuery();

        if( result != null )
        {
            if( result.next() != false )
            {
                host_kind = result.getInt("host_kind");
                if (result.getInt("timechart_flag") == 1)
                {
                    TimeChartFlag = true;
                }
            }
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);

         //キッチン端末有無判断
        KitchenExist = kitcheninfo.sendPacket0000(1,selecthotel);

        if (ownerinfo.sendPacket0100(1,selecthotel))
        {
            if (ownerinfo.SystemKind.equals(ownerinfo.SYSTEM_KIND_NEO))
            {
                if (ownerinfo.SystemVer1 >= ownerinfo.SYSTEM_VER1_MIN  && ownerinfo.SystemVer2 >= ownerinfo.SYSTEM_VER2_NEO_TO_SIRIUS)
                {
                    host_kind = 1; 
                }
            } 
        }
        if( host_kind != 2 )
        {
            // 室内精算機有無判断
            ownerinfo.RoomCode = 0;
            ownerinfo.sendPacket0124(1,selecthotel);
            TexExist      = false;
            for (int i = 0; i < ownerinfo.TexRoomCount; i++)
            {
                if (ownerinfo.TexChargeStat[i] !=0 || ownerinfo.TexSupplyDate[i] != 0)
                {
                    TexExist = true;
                    break;
                }
            }
            // マルチメディア有無判断
            ownerinfo.RoomCode = 0;
            ownerinfo.sendPacket0126(1,selecthotel);
            MultiMediaExist      = false;
            for (int i = 0; i < ownerinfo.MultiRoomCount; i++)
            {
                if (ownerinfo.MultiLineStat[i] !=0 || ownerinfo.MultiPowerStat[i] != 0)
                {
                    MultiMediaExist = true;
                    break;
                }
            }
            // フロント精算機有無判断
            ownerinfo.FrontTexTermCodeIn = 999;
            ownerinfo.sendPacket0156(1, selecthotel);
            if (ownerinfo.FrontTexTermCount== 0)        FrontTexExist = false;
        }
        else
        {
            TexExist      = false;
            FrontTexExist = false;
        }
    }
    else
    {
        boolean amf = false;
        boolean asc = false;

        // 全店舗ホスト種別を調べる
        String hotelid = (String)session.getAttribute("HotelId");
        if( hotelid != null )
        {
            query = "SELECT host_kind,timechart_flag FROM hotel WHERE group_id=?";

            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            result      = prestate.executeQuery();

            if( result != null )
            {
                while( result.next() != false )
                {
                    host_kind = result.getInt("host_kind");
                    if( host_kind == 1 )
                    {
                        amf = true;
                    }
                    if( host_kind == 2 )
                    {
                        asc = true;
                    }
                    if (result.getInt("timechart_flag") == 1)
                    {
                        TimeChartFlag = true;
                    }
                }
            }

           DBConnection.releaseResources(result);
           DBConnection.releaseResources(prestate);
        }

        
        if( asc != false && amf != false )
        {
            // AMF/ASC両方
            host_kind = 3;
        }
        else if( asc == false && amf != false )
        {
            // AMFのみ
            host_kind = 1;
        }
        else if( asc != false && amf == false )
        {
            // ASCのみ
            host_kind = 2;
        }
    }
    DBConnection.releaseResources(connection);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<title>部屋状況選択</title>
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/room_datacheck.js"></script>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF"  leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" <% if(storecount > 1){%>onload="if(document.selectstore.Store.value !='<%=selecthotel%>'){document.selectstore.elements['submitstore'].click();top.Main.mainFrame.location.href='page.html';}"<%}%>>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="middle">
      <table width="100%" height="40" border="0" cellpadding="0" cellspacing="0">
        <tr valign="middle">

          <!-- 店舗選択表示 -->
    <jsp:include page="../../common/pc/selectstoreRoom.jsp" flush="true">
       <jsp:param name="StoreCount" value="<%=storecount%>" />
    </jsp:include>
          <!-- 店舗選択表示ここまで -->
          <td rowspan="2" align="center" bgcolor="#000000" nowrap>
            <div class="white12" align="center">部屋情報</div>
          </td>
<%
   if( selecthotel.compareTo("all_manage") == 0)
   {
%>
<script>
top.Main.mainFrame.location.href="sales_wait.html";
setTimeout("roomDisp()",1000);

function roomDisp(){
    top.Main.mainFrame.location.href="roomdisp_group.jsp?<%=new DateEdit().getDate(2)%><%=new DateEdit().getTime(1)%>";
}
</script>
		<td align="left" valign="middle" class="size12" nowrap colspan="5" rowspan="2" height="40" width="500">
		  <a href="sales_wait.html" target="mainFrame" onclick="setTimeout('roomDisp()',1000);">&gt;&gt;管理店舗部屋状況一覧</a>
        </td>
<%
   }
   else
   {
        // ASCの場合表示する
        if( host_kind == 2 )
        {
%>
		<td align="left" valign="middle" class="size12" nowrap colspan="5" rowspan="2" height="40">
		  <a href="roomdisp_select.jsp?href=use&<%=new DateEdit().getDate(2)%><%=new DateEdit().getTime(1)%>"    <% if ( storecount > 1 ){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;利用状況</a>&nbsp;&nbsp;&nbsp;
          <a href="roomdisp_select.jsp?href=linen"  <% if ( storecount > 1 ){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;リネン</a>&nbsp;&nbsp;&nbsp;
          <a href="roomdisp_select.jsp?href=member" <% if ( storecount > 1 ){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;メンバー</a>&nbsp;&nbsp;&nbsp;
		  <a href="roomdisp_select.jsp?href=status" <% if ( storecount > 1 ){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;ステータス遷移</a>&nbsp;&nbsp;&nbsp;
<%
          if(imedia_user == 1 || TimeChartFlag)
          {
%>
          <a href="roomdisp_select.jsp?href=timechart" <% if ( storecount > 1 ){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;タイムチャート</a>&nbsp;&nbsp;&nbsp;
<%
          }
%>
          &nbsp;</td>
<%
        }
		//ルームサーバー
        else if( host_kind == 3 )
        {
%>
          <td align="left" valign="bottom" class="size12" nowrap colspan="5" height="20">
		  <a href="roomdisp_select.jsp?href=use&<%=new DateEdit().getDate(2)%><%=new DateEdit().getTime(1)%>"    <% if ( storecount > 1 ){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;利用状況</a>&nbsp;&nbsp;&nbsp;
          <a href="roomdisp_select.jsp?href=linen"  <% if ( storecount > 1 ){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;リネン</a>&nbsp;&nbsp;&nbsp;
          <a href="roomdisp_select.jsp?href=member" <% if ( storecount > 1 ){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;メンバー</a>&nbsp;&nbsp;&nbsp;
		  </td>
<%
        }
        else
        {
%>
          <td align="left" valign="bottom" class="size12" nowrap colspan="5" height="20">
		  <a href="roomdisp_select.jsp?href=use&<%=new DateEdit().getDate(2)%><%=new DateEdit().getTime(1)%>"     <% if ( storecount > 1 ){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;利用状況</a>&nbsp;&nbsp;&nbsp;
          <a href="roomdisp_select.jsp?href=control&<%=new DateEdit().getDate(2)%><%=new DateEdit().getTime(1)%>" <% if ( storecount > 1 ){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;管理機</a>&nbsp;&nbsp;&nbsp;
          <a href="roomdisp_select.jsp?href=linen&<%=new DateEdit().getDate(2)%><%=new DateEdit().getTime(1)%>"   <% if ( storecount > 1 ){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;リネン</a>&nbsp;&nbsp;&nbsp;
          <a href="roomdisp_select.jsp?href=member&<%=new DateEdit().getDate(2)%><%=new DateEdit().getTime(1)%>"  <% if ( storecount > 1 ){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;メンバー</a>&nbsp;&nbsp;&nbsp;
<%
            if(MultiMediaExist)
            {
%>
          <a href="roomdisp_select.jsp?href=multi&<%=new DateEdit().getDate(2)%><%=new DateEdit().getTime(1)%>"   <% if ( storecount > 1 ){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;マルチメディア</a>
<%
            }
%>
          &nbsp;</td>
<%
        }
%>
          <td rowspan="2" align="left" valign="middle" >
            <a href="javascript:;" onClick="MM_openBrWindow('../../common/pc/icon.html','アイコン説明','scrollbars=yes,resizable=yes,width=440,height=500')"><img src="../../common/pc/image/icon_b.gif" width="88" height="22" border="0"></a>
          </td>
        </tr>

<%
        // ASC以外の場合表示する
        if( host_kind != 2 )
        {
%>
        <tr align="left" valign="middle">
          <td align="left" valign="middle" class="size12" nowrap colspan="5" height="20">
<%
          if( host_kind == 1 )
          {
%>
		  <a href="roomdisp_select.jsp?href=car" <% if ( storecount > 1 ){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;車番</a>&nbsp;&nbsp;&nbsp;
<%
		  }
%>
<%
          if(TexExist)
          {
%>
          <a href="roomdisp_select.jsp?href=tex" <% if ( storecount > 1 ){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;精算機</a>&nbsp;&nbsp;&nbsp;
<%
              if (sec_level01 == 1 || imedia_user == 1)
              {
%>
          <a href="roomdisp_select.jsp?href=texpay" <% if ( storecount > 1 ){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;精算機入出金</a>&nbsp;&nbsp;&nbsp;
<%
              }
          }
%>
<%
          if(FrontTexExist)
          {
              if (sec_level01 == 1 || imedia_user == 1)
              {
%>
          <a href="roomdisp_select.jsp?href=fronttexpay" <% if ( storecount > 1 ){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;フロント精算機入出金</a>&nbsp;&nbsp;&nbsp;
<%
              }
          }
%>
          <a href="roomdisp_select.jsp?href=status" <% if ( storecount > 1 ){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;ステータス遷移</a>&nbsp;&nbsp;&nbsp;
<%
          if(imedia_user == 1 || TimeChartFlag)
          {
%>
          <a href="roomdisp_select.jsp?href=timechart" <% if ( storecount > 1 ){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;タイムチャート</a>&nbsp;&nbsp;&nbsp;
<%
          }
%>
<%
//          if(KitchenExist)
          if(KitchenExist && imedia_user == 1 || selecthotel.equals("tropicana"))
          {
%>
          <a href="roomdisp_select.jsp?href=kitchen" <% if ( storecount > 1 ){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;キッチン端末</a>
<%
          }
%>
          </td>
<%
        }
    }
%>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>
</html>
