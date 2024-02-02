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

    // �z�e��ID�擾
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

        // ���[���A�h���X�����擾
        query      = "SELECT count(DISTINCT address) FROM mag_address WHERE (hotel_id=?";

        prestate    = connection.prepareStatement(hotelquery);  //�Ǘ��X�܂̓ǂݍ���
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


        // ���[���A�h���X�擾
        query = "SELECT * FROM mag_address WHERE (hotel_id=?";

        prestate    = connection.prepareStatement(hotelquery); //�Ǘ��X�܂̓ǂݍ���
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
        // ���[���A�h���X�����擾
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

        // ���[���A�h���X�擾
        query = "SELECT * FROM mag_address WHERE hotel_id=?";
        query = query + " AND unknown_flag=1";
        query = query + " AND state=1";
    }

    // �z�MOK�ł��z�M�s�\�̂��̂��擾
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
                                <td valign="top" nowrap class="size12"><strong>�z�M�s�\�A�h���X�ꗗ</strong></td>
                                <td>&nbsp;</td>
                                <td class="size12">���݂̌���&nbsp;<%= count %> ��</td>
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
                              <input type="submit" name="Submit4" value="���M��~���m�肷��"><br><br>
                              <input type="submit" name="Submit4" value="���M�Ώۂɖ߂�"><br>
                              <strong><font color=red size=-1>�y���Ӂz���M�Ώۂɖ߂��ꍇ�́A�K�����M�悪��M�ł��邱�Ƃ��m�F���Ă��������B</font></strong>
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
