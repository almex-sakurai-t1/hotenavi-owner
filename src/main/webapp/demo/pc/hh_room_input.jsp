<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.DBSync" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String hotelid  = request.getParameter("HotelId");
    String param_id = request.getParameter("Id");
    if( hotelid == null )
    {
        hotelid = (String)session.getAttribute("SelectHotel");
    }
    if( hotelid == null || !CheckString.hotenaviIdCheck(hotelid))
    {
%>
        <jsp:forward page="error/error.html" />
<%
    }
    if( param_id == null || !CheckString.numCheck(param_id))
    {
%>
        <jsp:forward page="error/error.html" />
<%
    }
    NumberFormat  nf3;
    nf3    = new DecimalFormat("000");

    String  query;
    Calendar now = Calendar.getInstance();
    int seq    = 0;
    int seq_sv = 999;
    int now_date = now.get(now.YEAR)*10000 + (now.get(now.MONTH)+1)*100 + now.get(now.DATE);
    String  room_name   = "";
    String  room_text   = "";
    String  refer_name  = "";
    int     room_no     = 0;
    int     room_rank   = 0;
    int     hapihote_id = Integer.parseInt(param_id);
    int     result_new  = 0;

    int rank    = 0;
    int rank_sv = 999;
    String  room_no3 = "";

    String  rank_name   = "";
    String  memo        = "";
    int     disp_index  = 0;

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection      = DBConnection.getConnection();
    Connection        connection_upd  = null;
    PreparedStatement prestate_upd    = null;
    ResultSet         result_upd      = null;
    connection_upd  = DBConnection.getConnection();

    int  hapihote_rank = 2;

    query = "SELECT rank FROM hh_hotel_basic ";
    query = query + " WHERE id = ?";
    prestate = connection.prepareStatement(query);
    prestate.setInt(1,hapihote_id);
    result   = prestate.executeQuery();
    if( result.next() != false )
    {
        hapihote_rank =  result.getInt("rank");
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);

    int   rank_count = 0;
    query = "SELECT count(DISTINCT roomrank.rank) FROM roomrank WHERE roomrank.hotelid=?";
    query = query + " AND roomrank.start_date<=" + now_date;
    query = query + " AND roomrank.end_date>=" + now_date;
    query = query + " AND roomrank.disp_flg=1";
    prestate = connection.prepareStatement(query);
    prestate.setString(1,hotelid);
    result   = prestate.executeQuery();
    if( result.next() != false )
    {
        rank_count = result.getInt(1);
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);
    
    query = "SELECT * FROM hh_hotel_room_more ";
    query = query + " WHERE id = ?";
    query = query + " AND disp_flag<=1";
    prestate     = connection.prepareStatement(query);
    prestate.setInt(1,hapihote_id);
    result       = prestate.executeQuery();
    while( result.next() != false )
    {
        seq =  result.getInt("hh_hotel_room_more.seq");
        query = "SELECT * FROM room WHERE room.hotelid=?";
        query = query + " AND room.room_no=?";
        query = query + " AND room.start_date<=" + now_date;
        query = query + " AND room.end_date>=" + now_date;
        query = query + " AND room.disp_flg=1";
        prestate_upd     = connection_upd.prepareStatement(query);
        prestate_upd.setString(1,hotelid);
        prestate_upd.setInt(2,seq);
        result_upd   = prestate_upd.executeQuery();
        boolean ret = result_upd.next();
        DBConnection.releaseResources(result_upd);
        DBConnection.releaseResources(prestate_upd);
        if (!ret)
        {
            query = "DELETE FROM hotenavi.hh_hotel_room_more ";
            query = query + " WHERE id = ?";
            query = query + " AND seq = ?";
            query = query + " AND disp_flag<=1";
            prestate_upd     = connection_upd.prepareStatement(query);
            prestate_upd.setInt(1,hapihote_id);
            prestate_upd.setInt(2,seq);
            prestate_upd.executeUpdate();
            DBSync.publish(prestate_upd.toString().split(":",2)[1]);
            DBSync.publish(prestate_upd.toString().split(":",2)[1],true);
            DBConnection.releaseResources(prestate_upd);
        }        
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);

   
    query = "SELECT * FROM room WHERE room.hotelid=?";
    query = query + " AND room.start_date<=" + now_date;
    query = query + " AND room.end_date>=" + now_date;
    query = query + " AND room.disp_flg=1";
    query = query + " ORDER BY room.room_no,room.id DESC";
    prestate = connection.prepareStatement(query);
    prestate.setString(1,hotelid);
    result   = prestate.executeQuery();
    while( result.next() != false )
    {
        seq =  result.getInt("room.room_no");
        if(seq_sv != seq)
        {
            room_no    = result.getInt("room.room_no");
            if (room_no < 100)
            {
                room_no3    = nf3.format(room_no);
            }
            else
            {
                room_no3    = Integer.toString(room_no);
            }

            room_rank  = result.getInt("room.rank");
            room_name   = result.getString("room.room_name");
            room_name   = room_name.replace("号室","");
            //PC版のリンク用デキストの文字列をみる
            if (result.getString("image_thumb").indexOf("%ROOMNO%</a>") > 0)
            {
                    room_name = room_no3;
            }

            refer_name = "";
            if (!result.getString("room.image_thumb").equals("") && result.getString("image_thumb").indexOf("nowprinting") == -1 )
            {
                if (result.getString("room.refer_name").compareTo("") != 0)
                {
                    refer_name = result.getString("room.refer_name");
                }
                else
                {
                    refer_name = nf3.format(result.getInt("room.room_no"));
                }
            }
            seq_sv     = seq;
            query = "SELECT * FROM hh_hotel_room_more ";
            query = query + " WHERE id = ?";
            query = query + " AND seq=?";
            prestate_upd     = connection_upd.prepareStatement(query);
            prestate_upd.setInt(1,hapihote_id);
            prestate_upd.setInt(2,seq);
            result_upd   = prestate_upd.executeQuery();
            boolean ret = result_upd.next();
            DBConnection.releaseResources(result_upd);
            DBConnection.releaseResources(prestate_upd);
            if (ret)
            {
                query = "UPDATE hotenavi.hh_hotel_room_more SET ";

            }
            else
            {
                query = "INSERT INTO hotenavi.hh_hotel_room_more SET ";
                query = query + " id=?";
                query = query + ",seq=? ";
                query = query + ",room_name_host=? ,";
            }
            query = query + " room_name=? ";
            query = query + ",disp_flag=1 ";
            query = query + ",refer_name=? ";
            query = query + ",room_no=? ";
            query = query + ",room_rank=?";
            if (ret)
            {
                query = query + " WHERE id = ?";
                query = query + " AND seq = ?";
            }
            prestate_upd = connection_upd.prepareStatement(query);
            if (ret)
            {
              prestate_upd.setString(1,room_name);
              prestate_upd.setString(2,refer_name);
              prestate_upd.setInt(3,room_no);
              prestate_upd.setInt(4,room_rank);
              prestate_upd.setInt(5,hapihote_id);
              prestate_upd.setInt(6,seq);
            }
            else
            {
              prestate_upd.setInt(1,hapihote_id);
              prestate_upd.setInt(2,seq);
              prestate_upd.setString(3,room_name);
              prestate_upd.setString(4,room_name);
              prestate_upd.setString(5,refer_name);
              prestate_upd.setInt(6,room_no);
              prestate_upd.setInt(7,room_rank);
            }
            prestate_upd.executeUpdate();
            DBSync.publish(prestate_upd.toString().split(":",2)[1]);
            DBSync.publish(prestate_upd.toString().split(":",2)[1],true);
            DBConnection.releaseResources(prestate_upd);
        }
    }
    query = "UPDATE hotenavi.room SET ";
    query = query + "sync_flag=0";
    query = query + " WHERE room.hotelid = ?";
    prestate_upd = connection_upd.prepareStatement(query);
    prestate_upd.setString(1,hotelid);
    result_new   = prestate_upd.executeUpdate();
    DBSync.publish(prestate_upd.toString().split(":",2)[1]);
    DBSync.publish(prestate_upd.toString().split(":",2)[1],true);

    DBConnection.releaseResources(prestate_upd);
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);

    query = "SELECT * FROM roomrank WHERE roomrank.hotelid=?";
    query = query + " AND roomrank.start_date<=" + now_date;
    query = query + " AND roomrank.end_date>=" + now_date;
    query = query + " AND roomrank.disp_flg=1";
    query = query + " ORDER BY roomrank.rank,roomrank.id DESC";
    prestate = connection.prepareStatement(query);
    prestate.setString(1,hotelid);
    result   = prestate.executeQuery();
    while( result.next() != false )
    {
        rank =  result.getInt("roomrank.rank");
        if(rank_sv != rank)
        {
            room_rank   = rank;
            rank_name   = result.getString("roomrank.rank_name");
            room_text   = result.getString("roomrank.system");
            if (room_text.length() > 255)
            {
                room_text = room_text.substring(0,255);
            }
            memo        = result.getString("roomrank.memo_for_happyhotel");
            if (rank_count == 1)
            {
                disp_index =  99;
            }
            else
            {
                disp_index =  rank;
            }
            if (rank_sv == 999)
            {
                query = "DELETE FROM hotenavi.hh_hotel_roomrank ";
                query = query + " WHERE id = ?";
                prestate_upd = connection_upd.prepareStatement(query);
                prestate_upd.setInt(1,hapihote_id);
                result_new   = prestate_upd.executeUpdate();
                DBSync.publish(prestate_upd.toString().split(":",2)[1]);
                DBSync.publish(prestate_upd.toString().split(":",2)[1],true);
                DBConnection.releaseResources(result_upd);
                DBConnection.releaseResources(prestate_upd);
            }
            rank_sv     = rank;
            query = "INSERT INTO hotenavi.hh_hotel_roomrank SET ";
            query = query + " id= ?";
            query = query + ",room_rank=?";
            query = query + ",rank_name=?";
            query = query + ",room_text=?";
            query = query + ",memo=?";
            query = query + ",disp_index=?";
            prestate_upd = connection_upd.prepareStatement(query);
            prestate_upd.setInt(1,hapihote_id);
            prestate_upd.setInt(2,room_rank);
            prestate_upd.setString(3,rank_name);
            prestate_upd.setString(4,room_text);
            prestate_upd.setString(5,memo);
            prestate_upd.setInt(6,disp_index);
            result_new   = prestate_upd.executeUpdate();
            DBSync.publish(prestate_upd.toString().split(":",2)[1]);
            DBSync.publish(prestate_upd.toString().split(":",2)[1],true);
            DBConnection.releaseResources(result_upd);
            DBConnection.releaseResources(prestate_upd);
        }
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);

    query = "UPDATE hotenavi.roomrank SET ";
    query = query + "sync_flag=0";
    query = query + " WHERE roomrank.hotelid = ?";
    prestate_upd = connection_upd.prepareStatement(query);
    prestate_upd.setString(1,hotelid);
    result_new   = prestate_upd.executeUpdate();
    DBSync.publish(prestate_upd.toString().split(":",2)[1]);
    DBSync.publish(prestate_upd.toString().split(":",2)[1],true);
    DBConnection.releaseResources(result_upd);
    DBConnection.releaseResources(prestate_upd);

    DBConnection.releaseResources(result_upd,prestate_upd,connection_upd);
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);

    query = "SELECT hrp.plan_id,hrp.plan_name,hrrpr.seq,hhrm.room_rank,hrpr.room_rank";
    query +=" FROM newRsvDB.hh_rsv_plan hrp";
    query +=" INNER JOIN newRsvDB.hh_rsv_rel_plan_room hrrpr ON hrp.id = hrrpr.id AND hrp.plan_id = hrrpr.plan_id AND hrp.plan_sub_id = hrrpr.plan_sub_id";
    query +=" INNER JOIN hotenavi.hh_hotel_room_more hhrm ON hrrpr.id = hhrm.id AND hrrpr.seq = hhrm.seq";
    query +=" LEFT JOIN newRsvDB.hh_rsv_plan_roomrank hrpr ON hrp.id=hrpr.id  AND hrp.plan_id = hrpr.plan_id AND hrp.plan_sub_id = hrpr.plan_sub_id AND hrpr.room_rank=hhrm.room_rank";
    query +=" WHERE hrp.id=?";
    query +=" AND hrp.latest_flag=1";
    query +=" AND hrp.plan_sales_status=1";
    query +=" AND hrp.room_select_kind IN (1,2)";
    query +=" AND hrp.sales_end_date >= " + now_date;
    query +=" having hrpr.room_rank IS NULL";
    prestate = connection.prepareStatement(query);
    prestate.setInt(1,hapihote_id);
    result   = prestate.executeQuery();
    while( result.next() != false )
    {
%>
    <script>
		alert("ハピホテ予約でランク別料金が未設定のものがあります\r\nプランID=<%=result.getInt("plan_id")%>\r\nプラン名=<%=result.getString("plan_name")%>");
    </script>
<%
    }
    DBConnection.releaseResources(result,prestate,connection);

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>部屋情報作成</title>
<link href="/common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="/common/pc/style/access.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="scripts/main.js"></script>
<script type="text/javascript" src="/common/pc/scripts/coupon.js"></script>
</head>

<body bgcolor="#666666" background="/common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="100" height="20" nowrap bgcolor="#22333F" class="tab">
                <font color="#FFFFFF">登録確認</font></td>
              <td width="15" height="20" valign="bottom"><img src="/common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="/common/pc/image/spacer.gif" width="200" height="20"></div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- ここから表 -->
      <tr>
        <td align="center" valign="top" bgcolor="#FFFFFF"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><img src="/common/pc/image/spacer.gif" width="400" height="12"></td>
          </tr>
          <tr>
            <td width="8"><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="size12">
<%
    if( result_new != 0 )
    {

%>
登録しました。<br><%=rank_count%>
<%
    }
    else
    {
%>
登録に失敗しました。<br><%=query%>
<%
    }
%>
                    </td>
                  </tr>
                  <tr>
                    <td class="size12">&nbsp;</td>
                  </tr>
                  <tr>
                    <td class="size12"><form action="room_list2.jsp?HotelId=<%= hotelid %>" method="POST">
                      <INPUT name="submit_ret" type=submit value=戻る >
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
              <td><img src="/common/pc/image/tab_kado.gif" width="3" height="3"></td>
            </tr>
            <tr>
              <td bgcolor="#666666" height="100%"><img src="/common/pc/image/spacer.gif" width="3" height="100"></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td height="3" bgcolor="#999999">
          <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="3"><img src="/common/pc/image/tab_kado2.gif" width="3" height="3"></td>
              <td bgcolor="#666666"><img src="/common/pc/image/spacer.gif" width="100" height="3"></td>
            </tr>
          </table>
        </td>
        <td height="3" width="3"><img src="/common/pc/image/grey.gif" width="3" height="3"></td>
      </tr>
      <!-- ここまで -->
    </table></td>
  </tr>
  <tr>
    <td><img src="/common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/Library/footer.lbi" --><img src="/common/pc/image/imedia.gif" width="63" height="18" align="absmiddle"><img src="/common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyrigtht&copy; imedia
      inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="/common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
