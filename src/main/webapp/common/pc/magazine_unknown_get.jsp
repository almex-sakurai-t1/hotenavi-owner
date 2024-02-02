<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();
    int    count = 0;
    String query;
    String hotelquery;

    // ホテルID取得
    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    String hotelid = (String)session.getAttribute("SelectHotel");

    if( hotelid.compareTo("all") == 0 )
    {
        hotelquery = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid =?";
        hotelquery = hotelquery + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
        hotelquery = hotelquery + " AND owner_user_hotel.userid = ?";
        hotelquery = hotelquery + " AND hotel.plan <= 4";
        hotelquery = hotelquery + " AND hotel.plan >= 1";
        hotelquery = hotelquery + " AND hotel.plan != 2";

        // メールアドレス件数取得
        query      = "SELECT count(DISTINCT address) FROM mag_address WHERE (hotel_id=?";

        prestate    = connection.prepareStatement(hotelquery);  //管理店舗の読み込み
        prestate.setString(1,loginHotelId );
        prestate.setInt(2,ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result != null )
        {
            while( result.next() != false )
            {
                query = query + " OR hotel_id='" + result.getString("owner_user_hotel.accept_hotelid") + "'"; 
            }
        }

        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);

        query = query + ") ";
        query = query + " AND unknown_flag=1";
        query = query + " AND state=1";

        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid );
        result      = prestate.executeQuery();
        if( result != null )
        {
            if (result.next() != false)
            {
                count = result.getInt(1);
            }
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);


        // メールアドレス取得
        query = "SELECT * FROM mag_address WHERE (hotel_id=?";

        prestate    = connection.prepareStatement(hotelquery); //管理店舗の読み込み
        prestate.setString(1,loginHotelId );
        prestate.setInt(2,ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result != null )
        {
            while( result.next() != false )
            {
                query = query + " OR hotel_id='" + result.getString("owner_user_hotel.accept_hotelid") + "'"; 
            }
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);

        query = query + ") ";
        query = query + " AND unknown_flag=1";
        query = query + " AND state=1";
        query = query + " GROUP BY address";
    }
    else
    {
        // メールアドレス件数取得
        query = "SELECT count(*) FROM mag_address WHERE hotel_id=?";
        query = query + " AND unknown_flag=1";
        query = query + " AND state=1";

        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid );
        result      = prestate.executeQuery();
        if( result != null )
        {
            if (result.next() != false)
            {
                count = result.getInt(1);
            }
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);

        // メールアドレス取得
        query = "SELECT * FROM mag_address WHERE hotel_id=?";
        query = query + " AND unknown_flag=1";
        query = query + " AND state=1";
    }

    // 配信OKでかつ配信不能のものを取得
    prestate    = connection.prepareStatement(query);
    prestate.setString(1,hotelid );
    result      = prestate.executeQuery();
%>


                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr valign="top">
                        <td width="8" align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12"><table border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td width="300" nowrap><table width="300" border="0" cellspacing="0" cellpadding="0">
                              <tr>
                                <td valign="top" nowrap class="size12"><strong>配信不能アドレス一覧</strong></td>
                                <td>&nbsp;</td>
                                <td class="size12">現在の件数&nbsp;<%= count %> 件</td>
                              </tr>
                            </table></td>
                            </tr>
                          <tr>
                            <td width="300">

<%
    if( result != null )
    {
        if( count != 0 )
        {

%>
                              <select name="UnknownAddress" size="<%if (count>50){%>50<%}else{%><%=count%><%}%>" multiple>
<%
            while( result.next() != false )
            {
%>
<%
                           if(result.getString("custom_id").compareTo("") !=0) 
                            {
%>
                            <option value='<%= result.getString("address") %>'><%= ReplaceString.maskedMailAddress(MailAddressEncrypt.decrypt(result.getString("address"))) %>&nbsp;(<%= result.getString("custom_id") %>)</option>
<%
                            }
                            else
                            {
%>
                            <option value='<%= result.getString("address") %>'><%= ReplaceString.maskedMailAddress(MailAddressEncrypt.decrypt(result.getString("address"))) %></option>
<%
                            }
%>
<%
            }
%>
                              </select>
<%
        }
    }
%>


                            </td>
                            <td align="left" valign="bottom"><img src="../../common/pc/image/spacer.gif" width="30" height="12"></td>
                          </tr>
                          <tr valign="top">
                            <td colspan="2"><img src="../../common/pc/image/spacer.gif" width="400" height="8"></td>
                          </tr>
                          <tr>
                            <td colspan="2" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="400" height="8"></td>
                            </tr>
                          <tr>
                            <td>
<%
    if( count != 0 )
    {
%>
                              <input type="submit" name="Submit4" value="送信停止を確定する"><br><br>
                              <input type="submit" name="Submit4" value="送信対象に戻す"><br>
                              <strong><font color=red size=-1>【注意】送信対象に戻す場合は、必ず送信先が受信できることを確認してください。</font></strong>
<%
    }
%>
                            </td>
                            <td align="left" valign="bottom"><img src="../../common/pc/image/spacer.gif" width="30" height="12"></td>
                            </tr>
                        </table></td>
                        <td width="8" align="left" class="size12"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td width="8" align="left">&nbsp;</td>
                      </tr>
                  </table>

<%
    DBConnection.releaseResources(result,prestate,connection);
%>
