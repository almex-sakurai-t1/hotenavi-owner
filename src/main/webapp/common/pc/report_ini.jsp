<%@ page contentType="text/html; charset=Windows-31J" %>
<%
    // �Z�b�V�����̊m�F
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<script type="text/javascript">
<!--
    var dd = new Date();
    setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
//-->
</SCRIPT>
<%
    }
    ReplaceString rs = new ReplaceString();
    DateEdit  de   = new DateEdit();
    int  nowdate   = Integer.parseInt(de.getDate(2));
    int  now_year  = nowdate / 10000;
    int  now_month = nowdate / 100 % 100;
    int  now_day   = nowdate % 100;
    int  nowtime   = Integer.parseInt(de.getTime(1));

    String hotelid     = (String)session.getAttribute("SelectHotel");
    String param_year  = ReplaceString.getParameter(request,"Year");
    String param_month = ReplaceString.getParameter(request,"Month");
    String param_day   = ReplaceString.getParameter(request,"Day");
    if  (param_day == null)
    {
         param_day     = "1";
    }
    if(!CheckString.numCheck(param_year) || !CheckString.numCheck(param_month) || !CheckString.numCheck(param_day) )
    {
        param_year = "0";
        param_month = "0";
        param_day = "0";
%>
        <script type="text/javascript">

        <!--
        var dd = new Date();
        setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
        //-->
        </SCRIPT>
<%
    }
    int  target_year  = Integer.parseInt(param_year);
    int  target_month = Integer.parseInt(param_month);
    int  target_day   = Integer.parseInt(param_day);
    int  target_date  = target_year * 10000 + target_month * 100 + target_day;

    NumberFormat nf4 = new DecimalFormat("0000");
    NumberFormat nf2 = new DecimalFormat("00");
    String year  = nf4.format(Integer.valueOf(param_year).intValue());
    String month = nf2.format(Integer.valueOf(param_month).intValue());
    String day   = nf2.format(Integer.valueOf(param_day).intValue());

    String loginhotel = (String)session.getAttribute("LoginHotelId");
    DbAccess db_sec =  new DbAccess();
    // �Z�L�����e�B���̎擾
    ResultSet DbUserSecurity = ownerinfo.getUserSecurity(db_sec, loginhotel, ownerinfo.DbUserId);
    // �Z�L�����e�B�`�F�b�N
    if( DbUserSecurity == null )
    {
         db_sec.close();
%>
        <jsp:forward page="../../common/pc/error/notlogin.html" />
<%
    }
    if( DbUserSecurity.first() == false )
    {
         db_sec.close();
%>
        <jsp:forward page="../../common/pc/error/notlogin.html" />
<%
    }

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    String            query       = "";
    connection  = DBConnection.getConnection();

    //�z�e�����E�z�X�g��ʂ̎擾
    int     type  = 0;
    String  hname = "";
    try
    {
        query = "SELECT * FROM hotel WHERE hotel_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            type        = result.getInt("host_kind");
            hname       = result.getString("name");
            hname       = rs.replaceKanaFull(hname);
            if  (type  == 0 || type > 3)
            {
                type = 0;
            }
            else
            {
                type = type -1;
            }
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception e=" + e.toString() );
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    String[][] ReportList = new String[3][3];
    //����@�V���E�X�W��
    ReportList[0][0] =                   "1.������v�W�v\r\n";
    ReportList[0][0] = ReportList[0][0] +"2.�t�����g���o������\r\n";
    ReportList[0][0] = ReportList[0][0] +"3.���㕪��\r\n";
    ReportList[0][0] = ReportList[0][0] +"4.�^�C���`���[�g\r\n";
    ReportList[0][0] = ReportList[0][0] +"5.������v����\r\n";
    ReportList[0][0] = ReportList[0][0] +"6.�ԗ�����\r\n";
    ReportList[0][0] = ReportList[0][0] +"7.�����ʉғ�����\r\n";
    ReportList[0][0] = ReportList[0][0] +"8.���i���v����\r\n";
    ReportList[0][0] = ReportList[0][0] +"9.�N���W�b�g���ד��v\r\n";
    ReportList[0][0] = ReportList[0][0] +"10.���Z�@������v\r\n";
    ReportList[0][0] = ReportList[0][0] +"11.���Z�@���o��\r\n";
    ReportList[0][0] = ReportList[0][0] +"12.��ƔǓ��v\r\n";
    ReportList[0][0] = ReportList[0][0] +"13.�ڋq�������\r\n";
    //����@�V�V���W��
    ReportList[0][1] =                   "1.������v�W�v\r\n";
    ReportList[0][1] = ReportList[0][1] +"2.���㕪��\r\n";
    ReportList[0][1] = ReportList[0][1] +"3.�^�C���`���[�g\r\n";
    ReportList[0][1] = ReportList[0][1] +"4.������v����\r\n";
    ReportList[0][1] = ReportList[0][1] +"5.�ԗ�����\r\n";
    ReportList[0][1] = ReportList[0][1] +"6.�����ʉғ�����\r\n";
    ReportList[0][1] = ReportList[0][1] +"7.���i���v����\r\n";
    ReportList[0][1] = ReportList[0][1] +"8.�N���W�b�g���ד��v\r\n";
    ReportList[0][1] = ReportList[0][1] +"9.��ƔǓ��v\r\n";
    ReportList[0][1] = ReportList[0][1] +"10.�ڋq�������\r\n";
    //����@���[���T�[�o�W��
    ReportList[0][2] =                   "1.������v�W�v\r\n";
    ReportList[0][2] = ReportList[0][2] +"2.���㕪��\r\n";
    ReportList[0][2] = ReportList[0][2] +"3.�^�C���`���[�g\r\n";
    ReportList[0][2] = ReportList[0][2] +"4.������v����\r\n";
    ReportList[0][2] = ReportList[0][2] +"5.�ԗ�����\r\n";
    ReportList[0][2] = ReportList[0][2] +"6.�����ʉғ�����\r\n";
    ReportList[0][2] = ReportList[0][2] +"7.���i���v����\r\n";
    ReportList[0][2] = ReportList[0][2] +"8.�N���W�b�g���ד��v\r\n";
    ReportList[0][2] = ReportList[0][2] +"9.���Z�@������v\r\n";
    ReportList[0][2] = ReportList[0][2] +"10.���Z�@���o��\r\n";
    ReportList[0][2] = ReportList[0][2] +"11.��ƔǓ��v\r\n";
    ReportList[0][2] = ReportList[0][2] +"12.�ڋq�������\r\n";
    //����@�V���E�X�W��
    ReportList[1][0] =                   "1.��������W�v\r\n";
    ReportList[1][0] = ReportList[1][0] +"2.�������㖾��\r\n";
    ReportList[1][0] = ReportList[1][0] +"3.���������ʉғ�����\r\n";
    ReportList[1][0] = ReportList[1][0] +"4.�������������N�ʉғ�����\r\n";
    ReportList[1][0] = ReportList[1][0] +"5.�����������[�h�ʉғ�����\r\n";
    ReportList[1][0] = ReportList[1][0] +"6.�������ʉғ�����\r\n";
    ReportList[1][0] = ReportList[1][0] +"7.�����i�i��������\r\n";
    ReportList[1][0] = ReportList[1][0] +"8.�������㕪��\r\n";
    ReportList[1][0] = ReportList[1][0] +"9.�����������[�h�ʕ���\r\n";
    ReportList[1][0] = ReportList[1][0] +"10.�������i���㖾��\r\n";
    ReportList[1][0] = ReportList[1][0] +"11.�������[�h���ڕ\\r\n";
    ReportList[1][0] = ReportList[1][0] +"12.�O�N������r�\\r\n";
    ReportList[1][0] = ReportList[1][0] +"13.�����ڋq���p����\r\n";
    ReportList[1][0] = ReportList[1][0] +"14.�����v��������\r\n";
    ReportList[1][0] = ReportList[1][0] +"15.������\r\n";
    ReportList[1][0] = ReportList[1][0] +"16.�����o�[�W�v\r\n";
    //����@�V�V���W��
    ReportList[1][1] =                   "1.��������W�v\r\n";
    ReportList[1][1] = ReportList[1][1] +"2.�������㖾��\r\n";
    ReportList[1][1] = ReportList[1][1] +"3.���������ʉғ�����\r\n";
    ReportList[1][1] = ReportList[1][1] +"4.�������������N�ʉғ�����\r\n";
    ReportList[1][1] = ReportList[1][1] +"5.�����������[�h�ʉғ�����\r\n";
    ReportList[1][1] = ReportList[1][1] +"6.�������ʉғ�����\r\n";
    ReportList[1][1] = ReportList[1][1] +"7.�����i�i��������\r\n";
    ReportList[1][1] = ReportList[1][1] +"8.�������㕪��\r\n";
    ReportList[1][1] = ReportList[1][1] +"9.�����������[�h�ʕ���\r\n";
    ReportList[1][1] = ReportList[1][1] +"10.�������i���㖾��\r\n";
    ReportList[1][1] = ReportList[1][1] +"11.�������[�h���ڕ\\r\n";
    ReportList[1][1] = ReportList[1][1] +"12.�O�N������r�\\r\n";
    ReportList[1][1] = ReportList[1][1] +"13.�����ڋq���p����\r\n";
    //����@���[���T�[�o�W��
    ReportList[1][2] =                   "1.��������W�v\r\n";
    ReportList[1][2] = ReportList[1][2] +"2.�������㖾��\r\n";
    ReportList[1][2] = ReportList[1][2] +"3.���������ʉғ�����\r\n";
    ReportList[1][2] = ReportList[1][2] +"4.�������������N�ʉғ�����\r\n";
    ReportList[1][2] = ReportList[1][2] +"5.�����������[�h�ʉғ�����\r\n";
    ReportList[1][2] = ReportList[1][2] +"6.�������ʉғ�����\r\n";
    ReportList[1][2] = ReportList[1][2] +"7.�����i�i��������\r\n";
    ReportList[1][2] = ReportList[1][2] +"8.�������㕪��\r\n";
    ReportList[1][2] = ReportList[1][2] +"9.�����������[�h�ʕ���\r\n";
    ReportList[1][2] = ReportList[1][2] +"10.�������i���㖾��\r\n";
    ReportList[1][2] = ReportList[1][2] +"11.�������[�h���ڕ\\r\n";
    ReportList[1][2] = ReportList[1][2] +"12.�O�N������r�\\r\n";
    ReportList[1][2] = ReportList[1][2] +"13.�����ڋq���p����\r\n";
    ReportList[1][2] = ReportList[1][2] +"14.�����o�[�W�v\r\n";

    //���X�ܒ��[
    ReportList[2][0] =                   "1.���X�܏W�v��\r\n";

    int    data_type = 900 + report_flag;
%>
