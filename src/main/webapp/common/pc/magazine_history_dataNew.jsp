<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
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
%>

<%
    String query;
    String hotelid = (String)session.getAttribute("SelectHotel");
    if( hotelid.compareTo("all") == 0 )
    {
        hotelid = (String)session.getAttribute("HotelId");
    }

    String historyid = ReplaceString.getParameter(request,"history"); 
     if(!CheckString.numCheck(historyid))
     {
        historyid = "";
%>
        <script type="text/javascript">
        <!--
        var dd = new Date();
        setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
        //-->
        </SCRIPT>
<%
    }
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    Connection        connection_sub  = null;
    PreparedStatement prestate_sub    = null;
    ResultSet         result_sub      = null;
    connection        = DBConnection.getConnection();
    NumberFormat nf = new DecimalFormat("00");
    int  state     = 0;  //配信状態;0:配信準備中,1:配信済,2:削除
    int  send_flag = 0;
    boolean send_success = true;
    String param_subject = "";
    String param_body    = "";
    String param_query   = "";
    char c;
    int  c_index = 0;
    int  i       = 0;
    StringBuffer sb = new StringBuffer();

    query = "SELECT * FROM mag_data WHERE hotel_id=?";
    query = query + " AND history_id=?";
    prestate    = connection.prepareStatement(query);
    prestate.setString(1,hotelid );
    prestate.setInt(2,Integer.parseInt(historyid));
    result      = prestate.executeQuery();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>メルマガ履歴</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
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
              <td width="120" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF">履歴・編集</font></td>
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
              <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td class="size12"><strong><font color="#CC0000">編集・削除しない場合はそのままブラウザの戻るボタンで戻るか上のメニューから戻ってください。</font></strong></td>
            </tr>
            <tr>
              <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><img src="../../common/pc/image/spacer.gif" width="400" height="12"></td>
            </tr>
          </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td></td>
              </tr>
              <tr>
                <td valign="top">
                   <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr valign="top">
                        <td width="8" align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">
<%
    if( result.next() != false )
    {
        state     = result.getInt("state");
        send_flag = result.getInt("send_flag");
        if( state == 0 && ( result.getString("recipient_id").equals("") || result.getString("mail_id").equals("") ))
        {
          send_success = false;
        } 
%>
                        <table width="100%" border="0" cellspacing="0" cellpadding="2">
                          <tr>
                            <td bgcolor="#DDDDDD" class="size12"><strong>発行（予定）日時</strong></td>
                          </tr>
                          <tr>
                            <td><img src="../../common/pc/image/spacer.gif" width="8" height="3"></td>
                          </tr>
                          <tr>
                            <td class="size12">
<%
        if (!send_success)
        {
%>送信失敗 (<%= result.getInt("send_date") / 10000 %>/<%= result.getInt("send_date") / 100 % 100 %>/<%= result.getInt("send_date") % 100 %> <%= result.getInt("send_time") / 100 %>：<%= nf.format(result.getInt("send_time") % 100) %>)
<%
        }
        else
        if( send_flag == 1 )
        {
%>  <%= result.getInt("send_date") / 10000 %>/<%= result.getInt("send_date") / 100 % 100 %>/<%= result.getInt("send_date") % 100 %> <%= result.getInt("send_time") / 100 %>：<%= nf.format(result.getInt("send_time") % 100) %>
<%
        }
        else
        {
%>すぐに配信 (<%= result.getInt("send_date") / 10000 %>/<%= result.getInt("send_date") / 100 % 100 %>/<%= result.getInt("send_date") % 100 %> <%= result.getInt("send_time") / 100 %>：<%= nf.format(result.getInt("send_time") % 100) %>)
<%
        }
%>                          </td>
                          </tr>                
                          <tr>
                            <td><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                          </tr>
<%
        param_query = result.getString("param_query");
        String[] outputdata;
        outputdata = param_query.split("'");

        if (param_query.compareTo("") != 0)
        {
%>
                          <tr>
                            <td bgcolor="#DDDDDD" class="size12"><strong>登録対象店舗</strong></td>
                          </tr>
                          <tr>
                            <td><img src="../../common/pc/image/spacer.gif" width="8" height="3"></td>
                          </tr>
                          <tr valign="top">
                            <td align="left" class="size12">
<%
            connection_sub        = DBConnection.getConnection();
            for( i = 1 ; i < outputdata.length-1 ; i++ )
            {
                 if(outputdata[i].compareTo(",")!= 0 && outputdata[i].compareTo("")!= 0)
                 {
                     query = "SELECT * FROM hotel WHERE hotel_id='" +outputdata[i]+ "'";
                     prestate_sub    = connection_sub.prepareStatement(query);
                     result_sub      = prestate_sub.executeQuery();
                     if (result_sub.next() != false)
                     {
%>
                            <%=result_sub.getString("name")%><br>
<%
                     }
                     DBConnection.releaseResources(result_sub);
                     DBConnection.releaseResources(prestate_sub);
                 }
            }
            DBConnection.releaseResources(connection_sub);
%>
                            </td>
                          </tr>
                          <tr>
                            <td><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                          </tr>
<%
        }
%>
                          <tr>
                            <td bgcolor="#DDDDDD" class="size12"><strong>配信条件</strong></td>
                          </tr>
                          <tr>
                            <td><img src="../../common/pc/image/spacer.gif" width="8" height="3"></td>
                          </tr>

<%
        if( result.getInt("condition_all") == 0 )
        {
%>
                      <tr valign="top">
                        <td align="left" class="size12">全員</td>
                      </tr>
<%
        }
        else if( result.getInt("condition_all") == 1 )
        {
%>
                      <tr valign="top">
                        <td align="left" class="size12">ビジターのみ</td>
                      </tr>
<%
        }
        else if( result.getInt("condition_all") == 2 )
        {
%>
                      <tr valign="top">
                        <td align="left" class="size12">メンバーのみ</td>
                      </tr>
<%
        }
        else if( result.getInt("condition_all") == 3 )
        {
%>
                      <tr valign="top">
                        <td align="left" class="size12">メンバーで</td>
                      </tr>
<%
            if( result.getInt("birthday_flag") == 1 )
            {
%>
                      <tr valign="top">
                        <td align="left" class="size12">
                          誕生日：<%= result.getInt("birthday_start") / 100 %>月<%= result.getInt("birthday_start") % 100 %>日
                          ～<%= result.getInt("birthday_end") / 100 %>月<%= result.getInt("birthday_end") % 100 %>日
                        </td>
                      </tr>
<%
            }
            else
            {
%>
                      <tr valign="top">
                        <td align="left" class="size12">誕生日指定なし</td>
                      </tr>
<%
            }
%>
<%
            if( result.getInt("memorial_flag") == 1 )
            {
%>
                      <tr valign="top">
                        <td align="left" class="size12">
                          記念日：<%= result.getInt("memorial_start") / 100 %>月<%= result.getInt("memorial_start") % 100 %>日
                          ～<%= result.getInt("memorial_end") / 100 %>月<%= result.getInt("memorial_end") % 100 %>日
                        </td>
                      </tr>
<%
            }
            else
            {
%>
                      <tr valign="top">
                        <td align="left" class="size12">記念日指定なし</td>
                      </tr>
<%
            }
%>
<%
            if( result.getInt("lastdate_flag") == 1 )
            {
%>
                      <tr valign="top">
                        <td align="left" class="size12">
                          最終利用日：<%= result.getInt("lastdate_start") / 10000 %>年<%= result.getInt("lastdate_start") / 100 % 100 %>月<%= result.getInt("lastdate_start") % 100 %>日
                          ～<%= result.getInt("lastdate_end") / 10000 %>年<%= result.getInt("lastdate_end") / 100 % 100 %>月<%= result.getInt("lastdate_end") % 100 %>日
                        </td>
                      </tr>
<%
            }
            else
            {
%>
                      <tr valign="top">
                        <td align="left" class="size12">最終利用日指定なし</td>
                      </tr>
<%
            }
%>
<%
            if( result.getInt("use_flag") == 1 )
            {
%>
                      <tr valign="top">
                        <td align="left" class="size12">
                          有効回数：<%= result.getInt("use_start") %>～<%= result.getInt("use_end") %>
                        </td>
                      </tr>
<%
            }
            else
            {
%>
                      <tr valign="top">
                        <td align="left" class="size12">有効回数指定なし</td>
                      </tr>
<%
            }
%>
<%
            if( result.getInt("total_flag") == 1 )
            {
%>
                      <tr valign="top">
                        <td align="left" class="size12">
                          利用金額：<%= result.getInt("total_start") %>～<%= result.getInt("total_end") %>
                        </td>
                      </tr>
<%
            }
            else
            {
%>
                      <tr valign="top">
                        <td align="left" class="size12">利用金額指定なし</td>
                      </tr>
<%
            }
%>
<%
            if( result.getInt("point_flag") == 1 )
            {
%>
                      <tr valign="top">
                        <td align="left" class="size12">
                          ポイント：<%= result.getInt("point_start") %>～<%= result.getInt("point_end") %>
                        </td>
                      </tr>
<%
            }
            else
            {
%>
                      <tr valign="top">
                        <td align="left" class="size12">ポイント指定なし</td>
                      </tr>
<%
            }
%>
<%
            if( result.getInt("rank_flag") == 1 )
            {
%>
                      <tr valign="top">
                        <td align="left" class="size12">
                          ランク：<%= result.getInt("rank_start") %>～<%= result.getInt("rank_end") %>
                        </td>
                      </tr>
<%
            }
            else
            {
%>
                      <tr valign="top">
                        <td align="left" class="size12">ランク指定なし</td>
                      </tr>
<%
            }
%>
<%
            if( result.getInt("lastsend_flag") == 1 )
            {
%>
                      <tr valign="top">
                        <td align="left" class="size12">
                          最終送信日：<%= result.getInt("lastsend_start") / 10000 %>年<%= result.getInt("lastsend_start") / 100 % 100 %>月<%= result.getInt("lastsend_start") % 100 %>日<%= result.getInt("lastsend_starttime") / 100 %>時<%= result.getInt("lastsend_starttime") % 100 %>分
                          ～<%= result.getInt("lastsend_end") / 10000 %>年<%= result.getInt("lastsend_end") / 100 % 100 %>月<%= result.getInt("lastsend_end") % 100 %>日<%= result.getInt("lastsend_endtime") / 100 %>時<%= result.getInt("lastsend_endtime") % 100 %>分
                        </td>
                      </tr>
<%
            }
            else
            {
%>
                      <tr valign="top">
                        <td align="left" class="size12">最終送信日指定なし</td>
                      </tr>
<%
            }
        }
%>
<%
        param_subject = result.getString("subject");
        param_body = result.getString("body");
    }

    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);

    for( i = 0 ; i < param_subject.length() ; i++ )
    {
        c = param_subject.charAt(i);
        switch( c )
        {
            case '<':
                sb.append("&lt;");
                break;
            case '>':
                sb.append("&gt;");
                break;
            case '&':
                sb.append("&amp;");
                break;
            case '"':
                sb.append("&quot;");
                break;
            default :
                sb.append(c);
                break;
        }
    }
    param_subject = sb.toString();


    sb = new StringBuffer();
    for( i = 0 ; i < param_body.length() ; i++ )
    {
        c = param_body.charAt(i);
        switch( c )
        {
            case '<':
                sb.append("&lt;");
                break;
            case '>':
                sb.append("&gt;");
                break;
            case '&':
                sb.append("&amp;");
                break;
            case '"':
                sb.append("&quot;");
                break;
            default :
                sb.append(c);
                break;
        }
    }
    param_body = sb.toString();
%>



                          <tr>
                            <td><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                          </tr>
                          <tr>
                            <td bgcolor="#DDDDDD" class="size12"><strong>件名</strong></td>
                          </tr>
                          <tr>
                            <td><img src="../../common/pc/image/spacer.gif" width="8" height="3"></td>
                          </tr>
                          <tr>
                            <td class="size12"><%= param_subject %></td>
                          </tr>
                          <tr>
                            <td><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                          </tr>
                          <tr>
                            <td bgcolor="#DDDDDD" class="size12"><strong>本文</strong></td>
                          </tr>
                          <tr>
                            <td><img src="../../common/pc/image/spacer.gif" width="8" height="3"></td>
                          </tr>
                          <tr>
                            <td >
                              <table height="425" border="2" cellpadding="3" cellspacing="0" bordercolor="#666666">
                                <tr>
                                  <td width="420" valign="top" bgcolor="#FFFFFF" class="sizepre"><%= param_body.replace("\r\n","<br>")%>
                                  </td>
                                </tr>
                              </table>
                            </td>
                          </tr>
                          <tr>
                            <td class="size12">&nbsp;</td>
                          </tr>
                          <tr>
                            <td bgcolor="#DDDDDD" class="size12"><strong>送信先アドレス一覧</strong></td>
                          </tr>
                          <tr>
                            <td bgcolor="#DDDDDD" class="size12">
<%
    int count = 0;

    query = "SELECT * FROM mag_data_list WHERE hotel_id=?";
    query = query + " AND history_id=?";
    prestate    = connection.prepareStatement(query);
    prestate.setString(1,hotelid );
    prestate.setInt(2,Integer.parseInt(historyid));
    result      = prestate.executeQuery();
    if( result != null )
    {
        while( result.next() != false )
        {
            if( count == 0 )
            {
%>
                              <select size="10">
<%
            }
%>
<option><%= ReplaceString.maskedMailAddress(MailAddressEncrypt.decrypt(result.getString("address")))%></option>
<%
                count++;
        }
    }

    if( count > 0 )
    {
%>
                              </select>
<%
    }
    DBConnection.releaseResources(result,prestate,connection);
%>
                            </td>
                          </tr>
<%@ include file="../csrf/csrf.jsp" %>
                          <tr>
                            <td align="center" valign="middle" class="size12">
<%
    if( state == 0 )
    {
 %>
                            <form action="magazine_edit.jsp?history=<%= historyid %>" method="post">
                              <input type='hidden' name='csrf' value='<%=token%>'>
                              <input type="submit" name="Submit" value="編集する">
                            </form>
                              <img src="../../common/pc/image/spacer.gif" width="14" height="14">
                            <form action="magazine_history_delete.jsp?history=<%= historyid %>" method="post">
                              <input type="submit" name="Submit2" value="削　除">
                            </form>
<%
    }
%>
                            </td>
                          </tr>
                        </table>
                        </td>
                        <td align="left" class="size12"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td align="left">&nbsp;</td>
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
