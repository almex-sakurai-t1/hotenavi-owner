<%@ page contentType="text/html; charset=Windows-31J" %><%@ page import="java.sql.*" %><%@ page import="java.util.*" %><%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
%><jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/owner_ini.jsp" %>
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    boolean ErrorFlag    = false;
    boolean ErrorStore   = false;
%>
<%@ include file="../../common/pc/magazine_paramget.jsp" %>
<%
    if (ErrorNumeric) ErrorFlag  = true;

    int i = 0;
    String store_list[];
    store_list = request.getParameterValues("Store");
    List<Object> storeList = new ArrayList();
    if( store_list != null )
    {
        if (store_list.length > 0)
        {
            for( i = 0 ; i < store_list.length ; i++ )
            {
                storeList.add(i, store_list[i]);
            }
        }
    }
    else
    {
        ErrorStore = true;
        ErrorFlag  = true;
    }

    String query;
    String count_query;
    String rec_query;
    String group_query     = "";
    String condition_query = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection        = DBConnection.getConnection();

    String selecthotel = (String)session.getAttribute("SelectHotel");

    StringBuilder builder = new StringBuilder();
    for( int j = 0 ; j < storeList.size(); j++ ) {
        builder.append("?,");
    }

    String hotelId_in_list = builder.deleteCharAt( builder.length() -1 ).toString();

    count_query = "SELECT count(DISTINCT address) FROM mag_address WHERE hotel_id IN (" +  hotelId_in_list + ")";
    rec_query   = "SELECT address,custom_id FROM mag_address WHERE hotel_id IN (" + hotelId_in_list + ")";

    if (i > 1)
    {
       group_query = " GROUP BY address";
    }
    query = " AND state=1 AND unknown_flag=0";

    if( condition == 1 )
    {
        // ビジターのみ
        query = query + " AND (custom_id='' AND user_id='')";
    }
    else if( condition == 2 )
    {
        // メンバーのみ
        query = query + " AND (custom_id<>'' OR user_id<>'')";
    }
    else if( condition == 3 )
    {
        // 条件に一致した場合

        condition_query = "";

        // メンバー条件検索
        if( birthday_check == 1 )
        {
            condition_query = condition_query + " ((birthday1>= ? AND birthday1<= ? )"
                                                   + " OR  (birthday2>= ? AND birthday2<= ? ))";
            condition_count++;
        }
        if( memorial_check == 1 )
        {
            if( condition_count > 0 )
            {
                condition_query = condition_query + " AND ";
            }
            condition_query = condition_query + " ((memorial1>= ? AND memorial1<= ? )"
                                                    + " OR (memorial2 >= ? AND memorial2 <= ? ))";
            condition_count++;
        }
        if( lastday_check == 1 )
        {
            if( condition_count > 0 )
            {
                condition_query = condition_query + " AND ";
            }
            condition_query = condition_query + " (last_date >= ? AND last_date <= ? )";
            condition_count++;
        }
        if( count_check == 1 )
        {
            if( condition_count > 0 )
            {
                condition_query = condition_query + " AND ";
            }
            condition_query = condition_query + " (use_count >= ? AND use_count <= ? )";
            condition_count++;
        }
        if( total_check == 1 )
        {
            if( condition_count > 0 )
            {
                condition_query = condition_query + " AND ";
            }
            condition_query = condition_query + " (use_total >= ? AND use_total <= ? )";
            condition_count++;
        }
        if( point_check == 1 )
        {
            if( condition_count > 0 )
            {
                condition_query = condition_query + " AND ";
            }
            condition_query = condition_query + " (point >= ? AND point <= ? )";
            condition_count++;
        }
        if( point2_check == 1 )
        {
            if( condition_count > 0 )
            {
                condition_query = condition_query + " AND ";
            }
            condition_query = condition_query + " (point2 >= ? AND point2 <= ? )";
            condition_count++;
        }
        if( customid_check == 1 )
        {
            if( condition_count > 0 )
            {
                condition_query = condition_query + " AND ";
            }
            condition_query = condition_query + " (custom_id >= ? AND custom_id <= ? )";
            condition_count++;
        }
        if( mailaddress_check == 1 )
        {
            if( condition_count > 0 )
            {
                condition_query = condition_query + " AND ";
            }
            condition_query = condition_query +  " (address = ?)";
            condition_count++;
        }
        if( rank_check == 1 )
        {
            if( condition_count > 0 )
            {
                condition_query = condition_query + " AND ";
            }
            condition_query = condition_query + " (rank >= ? AND rank <= ? )";
            condition_count++;
        }
        if( lastsend_check == 1 )
        {
            if( condition_count > 0 )
            {
                condition_query = condition_query + " AND ";
            }
            condition_query = condition_query + " ((last_senddate < ? OR last_senddate <= ? AND last_sendtime < ? )"
                                                    + " OR (last_senddate > ? OR last_senddate >= ? AND last_sendtime > ? ))";
            condition_count++;
        }

        // 条件指定あり
        if( condition_count > 0 )
        {
            query = query + " AND ( " + condition_query + " ) ";
        }

    }
    if( !ErrorFlag )
    {
        // レコード件数
        prestate    = connection.prepareStatement(count_query + query);

        int index = 1;
        for( Object o : storeList ) {
            prestate.setObject(  index++, o );
        }

        if( condition == 3 )
        {
            // メンバー条件検索
            if( birthday_check == 1 )
            {
                prestate.setInt(index++, birthday_start);
                prestate.setInt(index++, birthday_end);
                prestate.setInt(index++, birthday_start);
                prestate.setInt(index++, birthday_end);
            }
            if( memorial_check == 1 )
            {
                prestate.setInt(index++, memorial_start);
                prestate.setInt(index++, memorial_end);
                prestate.setInt(index++, memorial_start);
                prestate.setInt(index++, memorial_end);
            }
            if( lastday_check == 1 )
            {
                prestate.setInt(index++, lastday_start);
                prestate.setInt(index++, lastday_end);
            }
            if( count_check == 1 )
            {
                prestate.setInt(index++, count_start);
                prestate.setInt(index++, count_end);
            }
            if( total_check == 1 )
            {
                prestate.setInt(index++, total_start);
                prestate.setInt(index++, total_end);
            }
            if( point_check == 1 )
            {
                prestate.setInt(index++, point_start);
                prestate.setInt(index++, point_end);
            }
            if( point2_check == 1 )
            {
                prestate.setInt(index++, point2_start);
                prestate.setInt(index++, point2_end);
            }
            if( customid_check == 1 )
            {
                prestate.setInt(index++, customid_start);
                prestate.setInt(index++, customid_end);
            }
            if( mailaddress_check == 1 )
            {
                prestate.setString(index++, MailAddressEncrypt.encrypt(mailaddress));
            }
            if( rank_check == 1 )
            {
                prestate.setInt(index++, rank_start);
                prestate.setInt(index++, rank_end);
            }
            if( lastsend_check == 1 )
            {
                prestate.setInt(index++, lastsend_start);
                prestate.setInt(index++, lastsend_start);
                prestate.setInt(index++, lastsend_start_hhmm);
                prestate.setInt(index++, lastsend_end);
                prestate.setInt(index++, lastsend_end);
                prestate.setInt(index++, lastsend_end_hhmm);
            }
        }

        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            // 取得件数
            rec_count = result.getInt(1);
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
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
<%
    if( !ErrorFlag )
    {
%>

            <tr>
              <td>&nbsp;</td>
              <td class="size12"><font color="#CC0000"><strong>2.配信者を確認してください</strong></font></td>
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
                      <tr valign="top">
                        <td width="8" align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">【登録対象店舗】</td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
                        <td align="left" class="size12"><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">
<%
        if( store_list != null )
        {
            if (store_list.length > 0)
            {
                for( i = 0 ; i < store_list.length ; i++ )
                {
                    prestate = connection.prepareStatement("SELECT name FROM hotel WHERE hotel_id = ? ");
                    prestate.setString(1, store_list[i] );

                    result      = prestate.executeQuery();
                    if( result != null )
                    {
                        if( result.next() != false )
                        {
%>
                            <%=result.getString(1)%><br>
<%
                        }
                    }
                }
            }
        }
%>
						</td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td align="left" class="size12"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                      </tr>
                      <tr valign="top">
                        <td width="8" align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">【配信条件】</td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
                        <td align="left" class="size12"><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
                      </tr>
<%@ include file="../../common/pc/magazine_paramdisp.jsp" %>

                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td align="left" class="size12"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                      </tr>
                      <tr valign="top">
                        <td><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td class="size12"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                      </tr>

                      <tr>
                        <td><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td class="tbl"><strong>送信先アドレス一覧</strong></td>
                      </tr>
                      <tr>
                        <td><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td class="tbl">
<%
// 配信先アドレス一覧
        int count = 0;
        query = rec_query + query + group_query;
        prestate    = connection.prepareStatement(query);
        int index = 1;
        for( Object o : storeList ) {
            prestate.setObject(  index++, o );
        }

        if( condition == 3 )
        {
            // メンバー条件検索
            if( birthday_check == 1 )
            {
                prestate.setInt(index++, birthday_start);
                prestate.setInt(index++, birthday_end);
                prestate.setInt(index++, birthday_start);
                prestate.setInt(index++, birthday_end);
            }
            if( memorial_check == 1 )
            {
                prestate.setInt(index++, memorial_start);
                prestate.setInt(index++, memorial_end);
                prestate.setInt(index++, memorial_start);
                prestate.setInt(index++, memorial_end);
            }
            if( lastday_check == 1 )
            {
                prestate.setInt(index++, lastday_start);
                prestate.setInt(index++, lastday_end);
            }
            if( count_check == 1 )
            {
                prestate.setInt(index++, count_start);
                prestate.setInt(index++, count_end);
            }
            if( total_check == 1 )
            {
                prestate.setInt(index++, total_start);
                prestate.setInt(index++, total_end);
            }
            if( point_check == 1 )
            {
                prestate.setInt(index++, point_start);
                prestate.setInt(index++, point_end);
            }
            if( point2_check == 1 )
            {
                prestate.setInt(index++, point2_start);
                prestate.setInt(index++, point2_end);
            }
            if( customid_check == 1 )
            {
                prestate.setInt(index++, customid_start);
                prestate.setInt(index++, customid_end);
            }
            if( mailaddress_check == 1 )
            {
              prestate.setString(index++, MailAddressEncrypt.encrypt(mailaddress));
            }
            if( rank_check == 1 )
            {
                prestate.setInt(index++, rank_start);
                prestate.setInt(index++, rank_end);
            }
            if( lastsend_check == 1 )
            {
                prestate.setInt(index++, lastsend_start);
                prestate.setInt(index++, lastsend_start);
                prestate.setInt(index++, lastsend_start_hhmm);
                prestate.setInt(index++, lastsend_end);
                prestate.setInt(index++, lastsend_end);
                prestate.setInt(index++, lastsend_end_hhmm);
            }
        }

        result      = prestate.executeQuery();
        param_query = prestate.toString().split(":")[1];
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
<option><%= ReplaceString.maskedMailAddress(MailAddressEncrypt.decrypt(result.getString("address")))%><%if(result.getString("custom_id").compareTo("")!=0){%>&nbsp;&nbsp;(<%= result.getString("custom_id")%>)<%}%></option>
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
    }
    else
    {
%>

                          <tr>
                              <td>&nbsp;</td>
                              <td class="size12">
<%
       if(ErrorStore)
       {
%>
                                  <font color="#CC0000"><strong>店舗が選択されていません。</strong></font>

<%
       }
       if(ErrorNumeric)
       {
%>
                                  <font color="#CC0000"><strong>数字入力箇所に数字以外の文字が入力されています。</strong></font>

<%
       }
    }
        DBConnection.releaseResources(result,prestate,connection);
%>
                            </td>
                          </tr>
<%@ include file="../csrf/csrf.jsp" %> 
<%
    if(!ErrorFlag)
    {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="center" class="size12"><font color="#CC0000" class="size14px"><strong><%= rec_count %>件</strong></font>　いらっしゃいました</td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td align="left" class="size12"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                      </tr>
<%
    }
    if( rec_count != 0 && !ErrorFlag)
    {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="center" class="size12"><strong>この条件でよろしいですか</strong></td>
                      </tr>
                      <tr>
                        <td colspan="2" align="left"><img src="../../common/pc/image/spacer.gif" width="200" height="14"></td>
                        </tr>
                      <tr valign="middle">
                        <td align="center">
                        </td>
                        <td align="center">
                        <form action="magazine_edit.jsp" method="post">

<%@ include file="../../common/pc/magazine_paramput.jsp" %>

                          <input name="Submit3" type="submit" value="はい">
                        </form>
                        <form action="magazine_find.jsp" method="post">
                          <input type='hidden' name='csrf' value='<%=token%>'>
                          <input name="Submit3" type="submit" value="戻る">
                        </form>
                        </td>
                      </tr>
<%
    }
    else
    {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="center" class="size12">&nbsp;</td>
                      </tr>
                      <tr>
                        <td colspan="2" align="left"><img src="../../common/pc/image/spacer.gif" width="200" height="14"></td>
                        </tr>
                      <tr valign="middle">
                        <td align="center">
                        </td>
                        <td align="center">
                        <form action="magazine_find.jsp" method="post">
                          <input type='hidden' name='csrf' value='<%=token%>'>
                          <input name="Submit3" type="submit" value="戻る">
                        </form>
                        </td>
                      </tr>
<%
    }
%>
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
