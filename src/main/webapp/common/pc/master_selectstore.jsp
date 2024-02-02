<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int          count = 0;
    boolean      ret;
    String       query;
    String       hotelname = "";
    String       storecount;
    String       selecthotel;
    String       param_store;
    ResultSet    result;

    selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }

    param_store = request.getParameter("Store");
    if( param_store != null )
    {
        selecthotel = param_store;
    }

    // �Z�b�V���������ɑI���z�e�����Z�b�g����
    session.setAttribute("SelectHotel", selecthotel);

    // �Ǘ��X�ܐ��̎擾
//    storecount = (String)session.getAttribute("StoreCount");
//    if( storecount != null )
//    {
//        count = Integer.valueOf(storecount).intValue();
//    }
//    else
//    {
//        count = 0;
//    }
    DbAccess  dbcount;
    ResultSet retcount;
    query = "SELECT count(*) FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid ='" + ownerinfo.HotelId + "'";
    query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
    query = query + " AND hotel.plan <= 2";
    query = query + " AND hotel.plan >= 1";
    query = query + " AND owner_user_hotel.userid = " + ownerinfo.DbUserId;
    dbcount =  new DbAccess();
    retcount = dbcount.execQuery(query);
    if( retcount.next() != false )
    {
       count = retcount.getInt(1);
    }
    dbcount.close();

if  (count == 1){
    DbAccess  dbhotelone;
    ResultSet rethotelone;
    query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid ='" + ownerinfo.HotelId + "'";
    query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
    query = query + " AND hotel.plan <= 2";
    query = query + " AND hotel.plan >= 1";
    query = query + " AND owner_user_hotel.userid = " + ownerinfo.DbUserId;
    dbhotelone =  new DbAccess();
    rethotelone = dbhotelone.execQuery(query);
    if( rethotelone.next() != false )
    {
       selecthotel = rethotelone.getString("hotel.hotel_id");
       session.setAttribute("SelectHotel", selecthotel);
    }
    dbhotelone.close();
}

    // �Z�b�V�����ς݃z�e����������{���\���ǂ������`�F�b�N���ANG�̏ꍇ�̓Z�b�V�������e��ύX����B
    DbAccess  dbhotel;
    ResultSet rethotel;
    query = "SELECT * FROM hotel WHERE hotel_id = ?";
    query = query + " AND plan <= 2";
    query = query + " AND plan >= 1";
    dbhotel =  new DbAccess();
    rethotel = dbhotel.execQuery(query, selecthotel);
    if( rethotel.next() == false )
    {
       if (count >= 2)
       {
       session.setAttribute("SelectHotel", "all");
       }
    }
    dbhotel.close();

    // 2�X�܈ȏ�̏ꍇ�\������
    if( count > 1 )
    {
%>

<td width="70" align="center" nowrap bgcolor="#000000">
  <div class="white12" align="center">&nbsp;�X�ܑI��&nbsp;</div>
</td>
<form action="master_select.jsp" method="post" name="selectstore">
  <td width="25%" align="left" valign="middle" nowrap bgcolor="#FFFFFF">
    <select name="Store" onChange="document.selectstore.elements['submitstore'].click()">

<%
        if( selecthotel.compareTo("") == 0 || selecthotel.compareTo("all") == 0)
        {
%>
      <option value="" selected>�I�����Ă�������</option>
<%
        }
%>
<%
        DbAccess db_manage =  new DbAccess();
        query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid ='" + ownerinfo.HotelId + "'";
        query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
        query = query + " AND hotel.plan <= 2";
        query = query + " AND hotel.plan >= 1";
        query = query + " AND owner_user_hotel.userid = " + ownerinfo.DbUserId;
        query = query + " ORDER BY hotel.sort_num,hotel.hotel_id";
        ResultSet DbManageHotel = db_manage.execQuery(query);

        // �Ǘ��X�ܐ������[�v
//        ResultSet DbManageHotel = ownerinfo.getManageHotel(db_manage, ownerinfo.HotelId, ownerinfo.DbUserId);
        ret = DbManageHotel.first();
        while( ret )
        {
            DbAccess db =  new DbAccess();

            // �z�e�����̂̎擾
             query = "SELECT name FROM hotel WHERE hotel_id=?";
            result = db.execQuery(query,DbManageHotel.getString("accept_hotelid"));
            if( result != null )
            {
                result.next();
                hotelname = result.getString("name");
            }

            if( selecthotel.compareTo(DbManageHotel.getString("accept_hotelid")) == 0 )
            {
%>
      <option value="<%= DbManageHotel.getString("accept_hotelid") %>" selected><%= hotelname %></option>
<%
            }
            else
            {
%>
      <option value="<%= DbManageHotel.getString("accept_hotelid") %>"><%= hotelname %></option>
<%
            }

            db.close();

            ret = DbManageHotel.next();
        }

        db_manage.close();
%>

    </select>
    <input type="submit" value="�X�ܐؑ�" name="submitstore" onClick="return datacheck()">
  </td>
</form>

<%
    }
    else
    {
%>
<form action="master_select.jsp" method="post" name="selectstore">
  <input type="hidden" name="Store" value="<%= ownerinfo.HotelId %>">
</form>
<%
    }
%>

