<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String loginHotelId = (String)session.getAttribute("LoginHotelId");

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection        = DBConnection.getConnection();

    Connection        connection_upd  = null;
    PreparedStatement prestate_upd    = null;
    connection_upd    = DBConnection.getConnection();

    String       selecthotel;
    String       hotelid;
    String       hotelname    = "";
    int          hostkind     = 0;
    int          newsales     = 0;
    int          imedia_user  = 0;
    int          level        = 0;
    int          pointtotal   = 0;
    boolean      ret          = false;

     // imedia_user �̃`�F�b�N
    try
    {
    	final String query = "SELECT * FROM owner_user WHERE hotelid=?"
         					+ " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            imedia_user = result.getInt("imedia_user");
            level       = result.getInt("level");
        }
    }
    catch( Exception e )
    {
    	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    // �Z�b�V�����������I�����ꂽ�z�e�����擾
    selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }
    //�Ǘ��X�܈ꗗ����̃����N
    String param_hotelid  = ReplaceString.getParameter(request,"HotelIdfromGroup");
    if (param_hotelid != null)
    {
        if (imedia_user == 1 && level == 3)
        {
            selecthotel = param_hotelid;
        }
        else
        {
        	try
        	{
        		final String query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid = ?"
						            + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid"
						            + " AND hotel.plan <= 2"
						            + " AND hotel.plan >= 1"
						            + " AND owner_user_hotel.userid = ?"
						            + " AND owner_user_hotel.accept_hotelid=?";
	            prestate    = connection.prepareStatement(query);
	            prestate.setString(1, loginHotelId);
	            prestate.setInt(2, ownerinfo.DbUserId);
	            prestate.setString(3, param_hotelid);
	            result      = prestate.executeQuery();
	            if(result.next())//�Ǘ��z�e���̃`�F�b�N
	            {
	                selecthotel = param_hotelid;
	            }
        	}
        	catch( Exception e )
        	{
        	    Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        	}
        	finally
        	{
	            DBConnection.releaseResources(result);
	            DBConnection.releaseResources(prestate);
        	}
        }
    }

    // �p�����^�̓Z�b�g�ς݂Ȃ̂Ńf�[�^�擾�݂̂��s��
    if( selecthotel.compareTo("all") == 0 || selecthotel.compareTo("all_manage") == 0)
    {
        int store_count = 0;

        String param_cnt = ReplaceString.getParameter(request,"cnt");
        if( param_cnt == null )
        {
            param_cnt = "0";
        }
        if(!CheckString.numCheck(param_cnt))
		{
			param_cnt="0";
%>
	        <script type="text/javascript">
	        <!--
	        var dd = new Date();
	        setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
	        //-->
	        </script>
<%
		}
        int cnt = Integer.parseInt(param_cnt);

        try
        {
	        final String query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid ='" + ownerinfo.HotelId + "'"
						         + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid"
						         + " AND hotel.plan <= 2"
						         + " AND hotel.plan >= 1"
						         + " AND owner_user_hotel.userid = " + ownerinfo.DbUserId
						         + " ORDER BY hotel.sort_num,hotel.hotel_id";
	        prestate    = connection.prepareStatement(query);
	        result      = prestate.executeQuery();

	        // �Ǘ��X�ܐ������[�v
	        ret = result.first();
	        ret = result.relative(cnt);
	        // �Ǘ��X�ܐ������[�v
	        while(ret)
	        {
	            hotelid   = result.getString("accept_hotelid");
	            hotelname = result.getString("hotel.name");
	            hostkind  = result.getInt("hotel.host_kind");
	            newsales  = result.getInt("hotel.host_detail");
	            pointtotal= result.getInt("hotel.pointtotal_flag");

	            ownerinfo.sendPacket0102(1, hotelid);

	            if (pointtotal == 0 && ownerinfo.SalesPointTotal!=0)
	            {
	                pointtotal = 1;
	                final String query_upd = "UPDATE hotel SET pointtotal_flag = 1 WHERE hotel_id = ?";
	                prestate_upd = connection_upd.prepareStatement(query_upd);
		            prestate_upd.setString(1, selecthotel);
			        prestate_upd.executeUpdate();
		            DBConnection.releaseResources(prestate_upd);
	            }

	            // �V�V���ł��V�V�����[�p���W���[���������ꍇ��0160�d�����M
	            if(newsales == 1)
	            {
	                ownerinfo.sendPacket0160(1, hotelid);
	            }
	            else
	            {
	                ownerinfo.sendPacket0104(1, hotelid);
	            }
	            ownerinfo.sendPacket0106(1, hotelid);
	            ownerinfo.sendPacket0108(1, hotelid);
	    // �{���̒��P��IN/OUT�g���擾�̂��ߓ��t���O�ɂ���
	            ownerinfo.AddupInOutGetDate = 0;
	            ownerinfo.sendPacket0112(1, hotelid);
%>
<jsp:include page="salesdisp_dispNew.jsp" flush="true" >
  <jsp:param name="NowHotel"     value="<%= hotelid %>" />
  <jsp:param name="NowHotelName" value="<%= hotelname %>" />
  <jsp:param name="PointTotal"   value="<%= pointtotal %>" />
</jsp:include>
<%
	            store_count++;
	            if( store_count >= 3 )
	            {
	                break;
	            }
	            ret = result.next();
	        }
        }
        catch( Exception e )
        {
            Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        }
        finally
        {
	        DBConnection.releaseResources(result);
	        DBConnection.releaseResources(prestate);
        }
    }
    else
    {
    	try
    	{
	        // �z�e�����̂̎擾
	        final String query = "SELECT * FROM hotel WHERE hotel_id=?";
	        prestate    = connection.prepareStatement(query);
	        prestate.setString(1, selecthotel);
	        result      = prestate.executeQuery();
	        if( result.next())
	        {
	            hotelname = result.getString("name");
	            hostkind  = result.getInt("host_kind");
	            newsales  = result.getInt("host_detail");
	            pointtotal= result.getInt("hotel.pointtotal_flag");
	        }
	    	// �w��X�܂̂ݎ擾
	        ownerinfo.sendPacket0102(1, selecthotel);

	        if (pointtotal == 0 && ownerinfo.SalesPointTotal!=0)
	        {
	            pointtotal = 1;
	            final String query_upd = "UPDATE hotel SET pointtotal_flag = 1 WHERE hotel_id = ?";
	            prestate_upd = connection_upd.prepareStatement(query_upd);
	            prestate_upd.setString(1, selecthotel);
		        prestate_upd.executeUpdate();
	            DBConnection.releaseResources(prestate_upd);
	        }

	        // �V�V���ł��V�V�����[�p���W���[���������ꍇ��0160�d�����M
	        if(newsales == 1)
	        {
	            ownerinfo.sendPacket0160(1, selecthotel);
	        }
	        else
	        {
	            ownerinfo.sendPacket0104(1, selecthotel);
	        }
	        ownerinfo.sendPacket0106(1, selecthotel);
	        ownerinfo.sendPacket0108(1, selecthotel);
	    // �{���̒��P��IN/OUT�g���擾�̂��ߓ��t���O�ɂ���
	        ownerinfo.AddupInOutGetDate = 0;
	        ownerinfo.sendPacket0112(1, selecthotel);
%>
<jsp:include page="salesdisp_dispNew.jsp" flush="true" >
  <jsp:param name="NowHotel" value="<%= selecthotel %>" />
  <jsp:param name="NowHotelName" value="<%= hotelname %>" />
  <jsp:param name="PointTotal"   value="<%= pointtotal %>" />
</jsp:include>
<%
    	}
    	catch( Exception e )
    	{
    	    Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    	}
    	finally
    	{
	        DBConnection.releaseResources(result);
	        DBConnection.releaseResources(prestate);
    	}
    }
    DBConnection.releaseResources(connection_upd);
    DBConnection.releaseResources(connection);
%>
