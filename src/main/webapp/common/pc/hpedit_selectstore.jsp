<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %><%@ page import="java.util.*" %>
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

    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }

    param_store = ReplaceString.getParameter(request,"Store");
    if( param_store != null )
    {
        if( !CheckString.hotenaviIdCheck(param_store) )
        {
            param_store = "";
%>
            <script type="text/javascript">
            <!--
            var dd = new Date();
            setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
            //-->
            </SCRIPT>
<%
        }
        selecthotel = param_store;
    }

    // �Z�b�V���������ɑI���z�e�����Z�b�g����
    session.setAttribute("SelectHotel", selecthotel);

    // �Ǘ��X�ܐ��̎擾
    List<Object> list = new ArrayList<Object>();
    list.add(ownerinfo.HotelId);
    list.add(ownerinfo.DbUserId);

    DbAccess  dbcount;
    ResultSet retcount;
    query = "SELECT count(*) FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid =?";
    query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
    query = query + " AND hotel.plan <= 4";
    query = query + " AND hotel.plan >= 1";
    query = query + " AND hotel.plan != 2";
    query = query + " AND owner_user_hotel.userid = ?";
    dbcount =  new DbAccess();
    retcount = dbcount.execQuery(query,list);
    if( retcount != null )
    {
        if( retcount.next() != false )
        {
           count = retcount.getInt(1);
        }
    }
    dbcount.close();

if  (count == 1){
    DbAccess  dbhotelone;
    ResultSet rethotelone;
    query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid =?";
    query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
    query = query + " AND hotel.plan <= 4";
    query = query + " AND hotel.plan >= 1";
    query = query + " AND hotel.plan != 2";
    query = query + " AND owner_user_hotel.userid = ?";
    dbhotelone =  new DbAccess();
    rethotelone = dbhotelone.execQuery(query,list);
    if( rethotelone != null )
    {
        if( rethotelone.next() != false )
        {
           selecthotel = rethotelone.getString("hotel.hotel_id");
           session.setAttribute("SelectHotel", selecthotel);
        }
    }
    dbhotelone.close();
}

    // �Z�b�V�����ς݃z�e�����{���\���ǂ������`�F�b�N���ANG�̏ꍇ�̓Z�b�V�������e��ύX����B
    DbAccess  dbhotel;
    ResultSet rethotel;
    query = "SELECT * FROM hotel WHERE hotel_id = ?";
    query = query + " AND hotel.plan <= 4";
    query = query + " AND hotel.plan >= 1";
    query = query + " AND hotel.plan != 2";
    dbhotel =  new DbAccess();
    rethotel = dbhotel.execQuery(query, selecthotel);
    if( rethotel != null )
    {
        if( rethotel.next() == false )
        {
           if (count >= 2)
           {
           session.setAttribute("SelectHotel", "all");
           }
        }
    }
    dbhotel.close();

    // 2�X�܈ȏ�̏ꍇ�\������
    if( count > 1 )
    {
%>

<td width="80" rowspan="2" align="left" valign="middle" nowrap bgcolor="#000000">
  <div class="white12" align="center">�X�ܑI��</div>
</td>
<form action="hpedit_select.jsp" method="post" name="selectstore" target="topFrame">
  <td width="100" rowspan="2" align="left" valign="middle" nowrap bgcolor="#FFFFFF">
    <select name="Store" onChange="document.selectstore.elements['submitstore'].click()">



<%
        if( selecthotel.compareTo("") == 0)
        {
%>
      <option value="" selected>�I�����Ă�������</option>
<%
        }
%>

<% if (loginHotelId.compareTo("tsukasa") == 0) {%>
<%
        if( selecthotel.compareTo("all") == 0 )
        {
%>
      <option value="all" selected>�S�X����</option>
<%
        }
        else
        {
%>
      <option value="all">�S�X����</option>
<%
        }
}
%>

<%
        DbAccess db_manage =  new DbAccess();
        query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid ='" + ownerinfo.HotelId + "'";
        query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
        query = query + " AND hotel.plan <= 4";
        query = query + " AND hotel.plan >= 1";
        query = query + " AND hotel.plan != 2";
        query = query + " AND owner_user_hotel.userid = " + ownerinfo.DbUserId;
        query = query + " ORDER BY hotel.sort_num,hotel.hotel_id";
        ResultSet DbManageHotel = db_manage.execQuery(query);

        // �Ǘ��X�ܐ������[�v
//        ResultSet DbManageHotel = ownerinfo.getManageHotel(db_manage, ownerinfo.HotelId, ownerinfo.DbUserId);
        ret = DbManageHotel.first();
        while( ret != false )
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
    <input type="submit" value="�X�ܑI��" name="submitstore">
  </td>
</form>

<%
    }
%>

