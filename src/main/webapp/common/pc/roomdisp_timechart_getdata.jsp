<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String       selecthotel;
    String       hotelid;
    String       query;
    String       hotelname = "";
    int          timechart_flag = 0;
    boolean      ret;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    Connection        connection_sub  = null;
    PreparedStatement prestate_sub    = null;
    ResultSet         result_sub      = null;

    // セッション属性より選択されたホテルを取得
    selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }

    connection  = DBConnection.getConnection();
    if (ReplaceString.getParameter(request,"timechart_submit") != null && ReplaceString.getParameter(request,"target_hotelid") != null)
    {
        String target_hotelid = ReplaceString.getParameter(request,"target_hotelid");

        String ftp_password = "";
        String ftp_server = "172.25.2.81";
        query = "SELECT * FROM hotel WHERE hotel_id =?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,target_hotelid);        result      = prestate.executeQuery();
        if( result.next())
        {
            ftp_password = result.getString("ftp_passwd");
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);

        copyTrans(ftp_server,target_hotelid,ftp_password);

        if (checkFile(target_hotelid))
        {
            query = "UPDATE hotel SET timechart_flag = 1";
            query = query + " WHERE hotel_id='" + selecthotel + "'";
            try
            {
                prestate    = connection.prepareStatement(query);
                prestate.executeUpdate();
                DBConnection.releaseResources(prestate);
            }
            catch( Exception e )
            {
%>
	<%= e.toString() %>
<%
            }
        }
        else
        {
%>
<script>alert("タイムチャートコンテンツのアップロードに失敗しました");</script>
<%
         }
    }
    // ルームコードの初期化（全部屋取得）
    ownerinfo.RoomCode = 0;

    // パラメタはセット済みなのでデータ取得のみを行う
    if( selecthotel.compareTo("all") == 0 )
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

        query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid =?";
        query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
        query = query + " AND hotel.plan <= 2";
        query = query + " AND hotel.plan >= 1";
        query = query + " AND owner_user_hotel.userid = ?";
        query = query + " ORDER BY hotel.sort_num,hotel.hotel_id";
        query = query + " LIMIT " + cnt + ",3";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,ownerinfo.HotelId);
        prestate.setInt(2,ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        connection_sub  = DBConnection.getConnection();

        while( result.next())
        {
            hotelid = result.getString("accept_hotelid");
             // ホテル名称の取得
            query = "SELECT name,timechart_flag FROM hotel WHERE hotel_id=?";
            prestate_sub = connection_sub.prepareStatement(query);
            prestate_sub.setString(1,hotelid);
            result_sub   = prestate_sub.executeQuery();
            if( result_sub != null )
            {
                result_sub.next();
                hotelname      = result_sub.getString("name");
                timechart_flag = result_sub.getInt("timechart_flag");
            }
            DBConnection.releaseResources(result_sub);
            DBConnection.releaseResources(prestate_sub);

            ownerinfo.sendPacket0158(1, hotelid);
%>
<jsp:include page="roomdisp_timechart_disp.jsp" flush="true" >
  <jsp:param name="NowHotel"      value="<%= hotelid %>" />
  <jsp:param name="NowHotelName"  value="<%= hotelname %>" />
  <jsp:param name="TimeChartFlag" value="<%= timechart_flag %>" />
</jsp:include>
<%
            store_count++;
            if( store_count >= 3 )
            {
                break;
            }
        }
        DBConnection.releaseResources(connection_sub);
    }
    else
    {
        // ホテル名称の取得
        query = "SELECT name,timechart_flag FROM hotel WHERE hotel_id='" + selecthotel + "'";
        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery();
        if( result != null )
        {
            result.next();
            hotelname      = result.getString("name");
            timechart_flag = result.getInt("timechart_flag");
        }
    // 指定店舗のみ取得
        ownerinfo.sendPacket0158(1, selecthotel);
%>
<jsp:include page="roomdisp_timechart_disp.jsp" flush="true" >
  <jsp:param name="NowHotel" value="<%= selecthotel %>" />
  <jsp:param name="NowHotelName" value="<%= hotelname %>" />
  <jsp:param name="TimeChartFlag" value="<%= timechart_flag %>" />
</jsp:include>
<%
    }
    DBConnection.releaseResources(result,prestate,connection);
%>
<%@ page import="java.io.File" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.FileOutputStream" %>
<%@ page import="org.apache.commons.net.ftp.FTPClient" %>
<%@ page import="org.apache.commons.net.ftp.FTPReply" %>
<%!
public static void copyTrans(String host,String user,String password)
throws IOException {
    FTPClient fp = new FTPClient();
    FileInputStream is = null;
    try
    {
        fp.connect(host);
        if (!FTPReply.isPositiveCompletion(fp.getReplyCode())) { // コネクトできたか？
            System.out.println("connection failed");
            System.exit(1); // 異常終了
        }

        if (fp.login(user, password) == false) { // ログインできたか？
             System.out.println("login failed");
             System.exit(1); // 異常終了
        }
// ファイル送信
        is = new FileInputStream("/hotenavi/demo/upd_timechart/pc/roomdisp_timechart.jsp");
        fp.storeFile("/pc/roomdisp_timechart.jsp", is);
        is.close();

        is = new FileInputStream("/hotenavi/demo/upd_timechart/pc/roomdisp_timechart.jsp");// クライ
        fp.storeFile("/smartpc/roomdisp_timechart.jsp", is);
        is.close();

        is = new FileInputStream("/hotenavi/demo/upd_timechart/pc/roomdisp_timechart_f.html");
        fp.storeFile("/pc/roomdisp_timechart_f.html", is);
        is.close();

        is = new FileInputStream("/hotenavi/demo/upd_timechart/pc/roomdisp_timechart_f.html");
        fp.storeFile("/smartpc/roomdisp_timechart_f.html", is);
        is.close();

        is = new FileInputStream("/hotenavi/demo/upd_timechart/pc/roomdisp_timechart_disp.jsp");
        fp.storeFile("/pc/roomdisp_timechart_disp.jsp", is);
        is.close();

        is = new FileInputStream("/hotenavi/demo/upd_timechart/pc/roomdisp_timechart_disp.jsp");
        fp.storeFile("/smartpc/roomdisp_timechart_disp.jsp", is);
        is.close();

        is = new FileInputStream("/hotenavi/demo/upd_timechart/pc/roomdisp_timechart_getdata.jsp");
        fp.storeFile("/pc/roomdisp_timechart_getdata.jsp", is);
        is.close();

        is = new FileInputStream("/hotenavi/demo/upd_timechart/pc/roomdisp_timechart_getdata.jsp");
        fp.storeFile("/smartpc/roomdisp_timechart_getdata.jsp", is);
        is.close();

        is = new FileInputStream("/hotenavi/demo/upd_timechart/pc/roomdisp_timechart_jump.jsp");
        fp.storeFile("/pc/roomdisp_timechart_jump.jsp", is);
        is.close();

        is = new FileInputStream("/hotenavi/demo/upd_timechart/pc/roomdisp_timechart_jump.jsp");
        fp.storeFile("/smartpc/roomdisp_timechart_jump.jsp", is);
        is.close();

        is = new FileInputStream("/hotenavi/demo/upd_timechart/i/timechart.jsp");
        fp.storeFile("/i/timechart.jsp", is);
        is.close();

        is = new FileInputStream("/hotenavi/demo/upd_timechart/i/timechart.jsp");
        fp.storeFile("/ez/timechart.jsp", is);
        is.close();

        is = new FileInputStream("/hotenavi/demo/upd_timechart/i/timechart.jsp");
        fp.storeFile("/j/timechart.jsp", is);
        is.close();

        is = new FileInputStream("/hotenavi/demo/upd_timechart/smart/timechart.jsp");
        fp.storeFile("/smart/timechart.jsp", is);
        is.close();

        System.out.println("FTP PUT COMPLETED");
    }
    finally
    {
        fp.disconnect();
    }
}

public static boolean checkFile(String hotelId) throws IOException {
    try
    {
    File checkFile = null;
    checkFile = new File("/hotenavi/" + hotelId + "/pc/roomdisp_timechart.jsp");
    if (!checkFile(checkFile)) return false;
    checkFile = new File("/hotenavi/" + hotelId + "/smartpc/roomdisp_timechart.jsp");
    if (!checkFile(checkFile)) return false;
    checkFile = new File("/hotenavi/" + hotelId + "/pc/roomdisp_timechart_f.html");
    if (!checkFile(checkFile)) return false;
    checkFile = new File("/hotenavi/" + hotelId + "/smartpc/roomdisp_timechart_f.html");
    if (!checkFile(checkFile)) return false;
    checkFile = new File("/hotenavi/" + hotelId + "/pc/roomdisp_timechart_disp.jsp");
    if (!checkFile(checkFile)) return false;
    checkFile = new File("/hotenavi/" + hotelId + "/smartpc/roomdisp_timechart_disp.jsp");
    if (!checkFile(checkFile)) return false;
    checkFile = new File("/hotenavi/" + hotelId + "/pc/roomdisp_timechart_getdata.jsp");
    if (!checkFile(checkFile)) return false;
    checkFile = new File("/hotenavi/" + hotelId + "/smartpc/roomdisp_timechart_getdata.jsp");
    if (!checkFile(checkFile)) return false;
    checkFile = new File("/hotenavi/" + hotelId + "/pc/roomdisp_timechart_jump.jsp");
    if (!checkFile(checkFile)) return false;
    checkFile = new File("/hotenavi/" + hotelId + "/smartpc/roomdisp_timechart_jump.jsp");
    if (!checkFile(checkFile)) return false;
    checkFile = new File("/hotenavi/" + hotelId + "/i/timechart.jsp");
    if (!checkFile(checkFile)) return false;
    checkFile = new File("/hotenavi/" + hotelId + "/ez/timechart.jsp");
    if (!checkFile(checkFile)) return false;
    checkFile = new File("/hotenavi/" + hotelId + "/j/timechart.jsp");
    if (!checkFile(checkFile)) return false;
    checkFile = new File("/hotenavi/" + hotelId + "/smart/timechart.jsp");
    if (!checkFile(checkFile)) return false;
    }
    finally
    {
        return true;
    }
}

public static boolean checkFile(File checkFile){
    if(checkFile.exists()){
       if (checkFile.length() == 0){
           return false;
       }
    }
    else
    {
        return false;
    }
    return true;
}
%>
