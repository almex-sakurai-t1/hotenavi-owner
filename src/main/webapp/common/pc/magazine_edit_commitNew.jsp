<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="jp.happyhotel.common.*" %>
<%@ page import="com.hotenavi2.mailmagazine.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
%>

<%@ include file="../../common/pc/magazine_paramget.jsp" %>

<%

    int history_id = 0;
    int ret = 0;
    boolean duplicate_mag = false;
    String query;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection        = DBConnection.getConnection();

    Connection        connection_upd  = null;
    PreparedStatement prestate_upd    = null;
    ResultSet         result_upd      = null;


    // セッション属性にセットされているデータを取得
    String hotelid = (String)session.getAttribute("SelectHotel");
    if( hotelid.compareTo("all") == 0 )
    {
        hotelid = (String)session.getAttribute("HotelId");
    }

    // メール内容
    String param_subject   = ReplaceString.getParameter(request,"subject");
    param_subject = param_subject.replace("&nbsp;"," ").replace("&#160;"," ");
    String param_body      = ReplaceString.getParameter(request,"body");
    param_body = param_body.replace("<br>","\r\n").replace("&nbsp;"," ").replace("&#160;"," ");
    String param_deliver   = ReplaceString.getParameter(request,"deliver");
    String param_year      = ReplaceString.getParameter(request,"year");
    String param_month     = ReplaceString.getParameter(request,"month");
    String param_day       = ReplaceString.getParameter(request,"day");
    String param_hour      = ReplaceString.getParameter(request,"hour");
    String param_min       = ReplaceString.getParameter(request,"min");
    String param_historyid = ReplaceString.getParameter(request,"historyid");
    String param_retry     = ReplaceString.getParameter(request,"Retry");
    int    deliver         = Integer.parseInt(param_deliver);
    int    year            = Integer.parseInt(param_year);
    int    month           = Integer.parseInt(param_month);
    int    day             = Integer.parseInt(param_day);
    int    hour            = Integer.parseInt(param_hour);
    int    min             = Integer.parseInt(param_min);

    if( param_historyid != null )                              //変更の場合
    {
        // 現在登録済内容の読み込み
        query = "SELECT * FROM mag_data ";
        query = query + " WHERE hotel_id=?";
        query = query + " AND history_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid);
        prestate.setInt(2,Integer.parseInt(param_historyid));
        result      = prestate.executeQuery();
        if( result != null )
        {
            if( result.next() != false )
            {
                rec_count          = result.getInt("count");
                condition          = result.getInt("condition_all");
                birthday_check     = result.getInt("birthday_flag");
                birthday_start     = result.getInt("birthday_start");
                birthday_end       = result.getInt("birthday_end");
                memorial_check     = result.getInt("memorial_flag");
                memorial_start     = result.getInt("memorial_start");
                memorial_end       = result.getInt("memorial_end");
                lastday_check      = result.getInt("lastdate_flag");
                lastday_start      = result.getInt("lastdate_start");
                lastday_end        = result.getInt("lastdate_end");
                count_check        = result.getInt("use_flag");
                count_start        = result.getInt("use_start");
                count_end          = result.getInt("use_end");
                total_check        = result.getInt("total_flag");
                total_start        = result.getInt("total_start");
                total_end          = result.getInt("total_end");
                point_check        = result.getInt("point_flag");
                point_start        = result.getInt("point_start");
                point_end          = result.getInt("point_end");
                rank_check         = result.getInt("rank_flag");
                rank_start         = result.getInt("rank_start");
                rank_end           = result.getInt("rank_end");
                lastsend_check     = result.getInt("lastsend_flag");
                lastsend_start     = result.getInt("lastsend_start");
                lastsend_end       = result.getInt("lastsend_end");
                lastsend_start_hhmm= result.getInt("lastsend_starttime");
                lastsend_end_hhmm  = result.getInt("lastsend_endtime");
                param_query        = result.getString("param_query");
                if (param_query.compareTo("") == 0)
                {
                    param_query = "SELECT address,custom_id FROM mag_address WHERE hotel_id IN ('" + hotelid + "') AND state=1 AND unknown_flag=0";
                }
                else
                {
                    if (param_query.indexOf("GROUP BY address") != -1)
                    {
                        param_query = "SELECT address,custom_id FROM mag_address WHERE hotel_id IN (" + param_query.replace("GROUP BY address","") + " AND state=1 AND unknown_flag=0 GROUP BY address";
                    }
                    else
                    {
                        param_query = "SELECT address,custom_id FROM mag_address WHERE hotel_id IN (" + param_query + " AND state=1 AND unknown_flag=0";
                    }
                }
            }
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);

        if (param_retry != null)  //変更で再抽出
        {
            query = "DELETE FROM mag_data_list ";
            query = query + " WHERE hotel_id=?";
            query = query + " AND history_id=?";
            // 再抽出するので削除
            prestate    = connection.prepareStatement(query);
            prestate.setString(1,hotelid);
            prestate.setInt(2,Integer.parseInt(param_historyid));
            ret         = prestate.executeUpdate();
            DBConnection.releaseResources(prestate);
        }
        query = "UPDATE mag_data SET ";
        query = query + "subject=?,";
        query = query + "body=?,";
        query = query + "send_flag=" + deliver + ",";
        query = query + "send_date=" + (year * 10000 + month * 100 + day) + ",";
        query = query + "send_time=" + (hour * 100   + min)   + " ";
        query = query + " WHERE hotel_id=?";
        query = query + " AND history_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,ReplaceString.SQLEscape(ReplaceString.unEscape(param_subject)).replace("&nbsp;"," ").replace("&#160;"," ").replace("''","'"));    
        prestate.setString(2,ReplaceString.SQLEscape(ReplaceString.unEscape(param_body)).replace("&nbsp;"," ").replace("&#160;"," ").replace("''","'"));
        prestate.setString(3,hotelid);
        prestate.setString(4,param_historyid);
        ret     = prestate.executeUpdate();
        DBConnection.releaseResources(prestate);
    }
    else  //新規登録の場合
    {
        // 2重登録確認
        query = "SELECT * FROM mag_data ";
        query = query + " WHERE hotel_id=?";
        query = query + " AND subject= ?";
        query = query + " AND body= ? ";
        query = query + " AND send_date=" + (year * 10000 + month * 100 + day);
        query = query + " AND send_time=" + (hour * 100 + min);
        query = query + " AND state <= 1";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid);
        prestate.setString(2,ReplaceString.SQLEscape(ReplaceString.unEscape(param_subject)).replace("&nbsp;"," ").replace("&#160;"," ").replace("''","'"));
        prestate.setString(3,ReplaceString.SQLEscape(ReplaceString.unEscape(param_body)).replace("&nbsp;"," ").replace("&#160;"," ").replace("''","'"));
        result      = prestate.executeQuery();
        if( result != null )
        {
            if( result.next() != false )
            {
                // すでに同じ内容が登録済み
                duplicate_mag = true;
            }
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);

        if(!duplicate_mag)  //新規登録
        {
            // mag_dataデータベースへ登録
            query = "INSERT INTO mag_data";
            query = query + " ( hotel_id, history_id, subject, body, count, state, send_flag, send_date, send_time,";
            query = query + " condition_all, birthday_flag, birthday_start, birthday_end, memorial_flag, memorial_start, memorial_end,";
            query = query + " lastdate_flag, lastdate_start, lastdate_end, use_flag, use_start, use_end, total_flag, total_start, total_end,";
            query = query + " point_flag, point_start, point_end, rank_flag, rank_start, rank_end,";
            query = query + " lastsend_flag, lastsend_start, lastsend_end, lastsend_starttime, lastsend_endtime, param_query";
            query = query + " ) VALUES ( ";
            query = query + "?,";
            query = query + "0,";
            query = query + "?,";
            query = query + "?,";
            query = query + rec_count + ",";
            query = query + "2,";
            query = query + deliver + ",";
            query = query + (year * 10000 + month * 100 + day) + ",";
            query = query + (hour * 100 + min) + ",";
            query = query + condition + ",";
            query = query + birthday_check + ",";
            query = query + birthday_start + ",";
            query = query + birthday_end + ",";
            query = query + memorial_check + ",";
            query = query + memorial_start + ",";
            query = query + memorial_end + ",";
            query = query + lastday_check + ",";
            query = query + lastday_start + ",";
            query = query + lastday_end + ",";
            query = query + count_check + ",";
            query = query + count_start + ",";
            query = query + count_end + ",";
            query = query + total_check + ",";
            query = query + total_start + ",";
            query = query + total_end + ",";
            query = query + point_check + ",";
            query = query + point_start + ",";
            query = query + point_end + ",";
            query = query + rank_check + ",";
            query = query + rank_start + ",";
            query = query + rank_end + ",";
            query = query + lastsend_check + ",";
            query = query + lastsend_start + ",";
            query = query + lastsend_end + ",";
            query = query + lastsend_start_hhmm + ",";
            query = query + lastsend_end_hhmm + ",";
            query = query + "?)";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1,hotelid);
            prestate.setString(2,ReplaceString.SQLEscape(ReplaceString.unEscape(param_subject)).replace("&nbsp;"," ").replace("&#160;"," ").replace("''","'"));
            prestate.setString(3,ReplaceString.SQLEscape(ReplaceString.unEscape(param_body)).replace("&nbsp;"," ").replace("&#160;"," ").replace("''","'"));
            prestate.setString(4,ReplaceString.unEscape(param_query).replace("%AL%","address LIKE"));
            ret     = prestate.executeUpdate();
            DBConnection.releaseResources(prestate);
        }
        else
        {
            query = "";
        }
    }

    if( param_historyid == null )//新規登録なので、ヒストリーIDを発行
    {
        query = "SELECT history_id FROM mag_data WHERE hotel_id=?";
        query = query + " ORDER BY history_id DESC LIMIT 1";
        prestate    = connection.prepareStatement(query);
		    prestate.setString(1,hotelid );
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            history_id = result.getInt(1);
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
    else
    {
      history_id = Integer.parseInt(param_historyid);
    }

    if (!duplicate_mag && (param_retry != null || param_historyid == null)) // 再抽出した場合と、新規の場合
    {
        rec_count   = 0;
        String execQuery =  param_query.indexOf("SELECT")==-1 ? "SELECT address FROM mag_address WHERE hotel_id IN (" + ReplaceString.DBEscape(param_query).replace("&#160;"," ").replace("%AL%","address LIKE").replace("GROUP BY address"," ") + " AND state=1 AND unknown_flag=0 GROUP BY address" : ReplaceString.DBEscape(param_query).replace("&#160;"," ").replace("%AL%","address LIKE").replace("GROUP BY address"," ");
        prestate    = connection.prepareStatement( ReplaceString.DBEscape(execQuery));
        if (param_historyid==null)
        {
          param_query = ReplaceString.DBEscape(execQuery);
        }

%>
<!--<%=param_query%><br>
<%= ReplaceString.DBEscape(param_query)%><br>
<%=prestate.toString()%>-->
<%
        result      = prestate.executeQuery();
        connection_upd  = DBConnection.getConnection();
        while (result.next() != false )
        {
            rec_count++;
            query = "INSERT INTO mag_data_list";
            query = query + " ( hotel_id, history_id, address, send_flag ) VALUES ( ";
            query = query + "'" + hotelid + "',";
            query = query + history_id + ",";
            query = query + "'" + ReplaceString.SQLEscape(result.getString("address")) + "',";
            query = query + " 1 ) ";
            prestate_upd    = connection_upd.prepareStatement(query);
            ret     = prestate_upd.executeUpdate();
            DBConnection.releaseResources(prestate_upd);
        }
        DBConnection.releaseResources(connection_upd);
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);

        query = "UPDATE mag_data SET ";
        query = query + "count=" + rec_count + ", ";
        query = query + "state=0";
        query = query + " WHERE hotel_id=?";
        query = query + " AND history_id=?";
        // レコード件数の書き込み
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid);
        prestate.setInt(2,history_id);
        ret     = prestate.executeUpdate();
        DBConnection.releaseResources(prestate);

        String update_param = "last_update=" + DateEdit.getDate(2) + ",last_uptime=" + DateEdit.getTime(1) + ",last_senddate=" + DateEdit.getDate(2) + ",last_sendtime=" + DateEdit.getTime(1); 
        String update_query = param_query.replace("address,custom_id","address").replace("SELECT address FROM mag_address","UPDATE mag_address SET " + update_param).replace("GROUP BY address","").replace("&#160;"," ").replace("%AL%","address LIKE");
        prestate    = connection.prepareStatement(update_query);
        ret     = prestate.executeUpdate();
        DBConnection.releaseResources(prestate);
    }
    DBConnection.releaseResources(connection);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>メルマガ作成</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
      </tr>
    </table>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="180" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF">メルマガ作成</font></td>
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
        <td align="center" valign="top" bgcolor="#FFF5EE"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><img src="../../common/pc/image/spacer.gif" width="400" height="12"></td>
            </tr>
            <tr>
              <td>&nbsp;</td>
<%
if (rec_count == 0)
{
%>
              <td class="size12"><font color="#CC0000"><strong>内容、送信時刻が前回登録済み内容と同じなので配信確定できませんでした。<br>配信するにはブラウザの戻るボタンで内容を修正してください。</strong></font></td>
<%
}
else
{
              LogicMagazineSend logicMagazineSend = new LogicMagazineSend();
              if (logicMagazineSend.execute(hotelid, history_id, param_retry != null))      
              {
%>
              <td class="size12"><font color="#CC0000"><strong>配信を確定しました</strong></font></td>
<%
              }
              else
              {
%>
                <td class="size12"><font color="#CC0000"><strong>配信が失敗しました</strong></font><br>
                  【<%=logicMagazineSend.getResponseCode()%>】(<%=logicMagazineSend.getErrorMessage()%>)
                </td>
<%
              } 
}
%>

            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
          </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td></td>
              </tr>
              <tr>
                <td valign="top">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">

<%@ include file="../../common/pc/magazine_paramdisp.jsp" %>

                      <tr valign="top">
                        <td><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td align="left" class="size12"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                      </tr>
                      <tr valign="top">
                        <td align="left">&nbsp;</td>
                        <td align="left" class="size12">件数：<%= rec_count %>件</td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td align="left" class="size12"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td align="left" class="size12">
                          件名：<%= param_subject.replace("&nbsp;"," ").replace("&#160;"," ") %></td>
                      </tr>
                      <tr>
                        <td width="8" align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
<%
    if( deliver == 1 )
    {
%>
                        <td align="left" class="size12">配信予定時間&nbsp;:&nbsp;<%= year %>年<%= month %>月<%= day %>日<%= hour %>時<%= min %>分 
<%
    }
    else
    {
%>
                        <td align="left" class="size12">配信予定時間&nbsp;:&nbsp;すぐに配信 
<%
    }
%>
						<br><br><font color="#CC0000">
						※配信状況は「履歴・編集」でご確認ください。<br>
						※混雑状況によっては、配信までに1時間以上かかる場合があります。<br>
						※再度入力すると二重に送信されますので、ご注意ください。<br>
						※1時間以上経過しても配信されない場合は、お手数ですがアルメックスまで連絡ください。<br></font>
                      </tr>
                      <tr>
                        <td colspan="2" align="left"><img src="../../common/pc/image/spacer.gif" width="200" height="14"></td>
                      </tr>
                  </table>
				</td>
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
      <!-- ここまで -->
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
