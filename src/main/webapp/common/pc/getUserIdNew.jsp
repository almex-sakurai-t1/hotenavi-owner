<%@ page contentType="text/html; charset=Windows-31J" %>
<%
        Connection        connection  = null;
        PreparedStatement prestate    = null;
        ResultSet         resultNew      = null;
        connection  = DBConnection.getConnection();

        query ="SELECT max(userid) FROM owner_user WHERE hotelid='" + loginHotelId + "'";
        prestate    = connection.prepareStatement(query);
        resultNew      = prestate.executeQuery();
        if( resultNew.next())
        {
            if (useridNew < resultNew.getInt(1))
                useridNew = resultNew.getInt(1);
        }
        DBConnection.releaseResources(resultNew);
        DBConnection.releaseResources(prestate);
        query ="SELECT max(userid) FROM owner_user_log WHERE hotelid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        resultNew      = prestate.executeQuery();
        if( resultNew.next())
        {
            if (useridNew < resultNew.getInt(1))
                useridNew = resultNew.getInt(1);
        }
        DBConnection.releaseResources(resultNew);
        DBConnection.releaseResources(prestate);
        query ="SELECT max(userid) FROM owner_user_login WHERE hotelid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        resultNew      = prestate.executeQuery();
        if( resultNew.next())
        {
            if (useridNew < resultNew.getInt(1))
                useridNew = resultNew.getInt(1);
        }
        DBConnection.releaseResources(resultNew);
        DBConnection.releaseResources(prestate);
        query ="SELECT max(userid) FROM owner_user_security WHERE hotelid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        resultNew      = prestate.executeQuery();
        if( resultNew.next())
        {
            if (useridNew < resultNew.getInt(1))
                useridNew = resultNew.getInt(1);
        }
        DBConnection.releaseResources(resultNew);
        DBConnection.releaseResources(prestate);
        query ="SELECT max(userid) FROM owner_user_hotel WHERE hotelid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        resultNew      = prestate.executeQuery();
        if( resultNew.next())
        {
            if (useridNew < resultNew.getInt(1))
                useridNew = resultNew.getInt(1);
        }
        DBConnection.releaseResources(resultNew);
        DBConnection.releaseResources(prestate);
        query ="SELECT max(user_id) FROM hh_owner_user_log WHERE hotel_id=?";
        query = query + " AND user_id < 10000";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        resultNew      = prestate.executeQuery();
        if( resultNew.next())
        {
            if (useridNew < resultNew.getInt(1))
                useridNew = resultNew.getInt(1);
        }
        DBConnection.releaseResources(resultNew,prestate,connection);
%>
