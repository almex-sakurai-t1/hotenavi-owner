<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%-- ���ݕ������\������ --%>
<%
    String requestUri = request.getRequestURI();
    if( requestUri.indexOf("/mobile/") > 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
    // �z�e��ID�̃Z�b�g
    String hotelid = ReplaceString.getParameter(request,"HotelId");
    if( hotelid == null )
    {
        hotelid = ownerinfo.HotelId;
    }
%>
<%
    int            i;
    int            count;
    int            now_ymd;
    int            now_time;
    DateEdit        df;
    StringFormat    sf;
    NumberFormat    nf;

    df = new DateEdit();
    sf = new StringFormat();
    nf = new DecimalFormat("00");

    // ���ݓ��t�E�����擾(YYYYMMDD / HHMMSS)
    now_ymd = Integer.parseInt(df.getDate(2));
    now_time = Integer.parseInt(df.getTime(1));

    // �z�X�g��ʎ擾
    int type = 0;

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    try
    {
        final String query = "SELECT * FROM hotel WHERE hotel_id = ? ";
        connection  = DBConnection.getConnection();
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);

        result      = prestate.executeQuery();
        if( result != null )
        {
            if( result.next() != false )
            {
                type = result.getInt("host_kind");
            }
        }
    }
    catch( Exception e )
    {
        Logging.error(e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }

%>
<%= now_ymd / 100 % 100 %>/<%= now_ymd % 100 %> <%= now_time / 10000 %>:<%= now_time / 100 % 100 %>����<br>
<hr>
<%-- �����ڍ׏��\�� --%>
<%= ownerinfo.StatusDetailRoomName[0] %>&nbsp;<%= ownerinfo.StatusDetailStatusName[0] %>
<%
            if (ownerinfo.StatusDetailUserChargeMode[0] == 1)
            {
%><img src="/common/mobile/image/rest.gif">
<%
            }
            else if (ownerinfo.StatusDetailUserChargeMode[0] == 2)
            {
%><img src="/common/mobile/image/stay.gif">
<%
            }
%><br>
�o��:&nbsp;<%= ownerinfo.StatusDetailElapseTime[0] / 60 %>:<%= nf.format(ownerinfo.StatusDetailElapseTime[0] % 60) %><br>
����:
<%
    if( ownerinfo.StateInDate[0] != 0 )
    {
%>
        <%= ownerinfo.StateInTime[0] / 100 %>��<%= nf.format(ownerinfo.StateInTime[0] % 100) %>��
<%
    }
%>
<br>

�ގ�:
<%
    if( ownerinfo.StateOutDate[0] == 0 )
    {
        if( ownerinfo.StateInDate[0] != 0 )
        {
%>
            <%= now_time / 10000 %>��<%= nf.format(now_time / 100 % 100) %>��(����)
<%
        }
    }
    else
    {
%>
        <%= ownerinfo.StateOutTime[0] / 100 %>��<%= nf.format(ownerinfo.StateOutTime[0] % 100) %>��
<%
    }
%>
<hr>

<%
    // �z�X�g��AMF�̏ꍇ�̂ݕ\��
    if( type == 1 )
    {
%>
[�x����]<br>
�������z:\<%= Kanma.get(ownerinfo.DetailPayClaim) %><br>
���p���v:\<%= Kanma.get(ownerinfo.DetailPayTotal) %><br>
<br>
<%
        for( i = 0 ; i < ownerinfo.DetailPayCount ; i++ )
        {
%>
<%= ownerinfo.DetailPayName[i] %>:\<%= Kanma.get(ownerinfo.DetailPayMoney[i]) %><br>
<%
        }
%>

<hr>
[���p����]
<%
        for( i = 0 ; i < ownerinfo.DetailUseCount ; i++ )
        {
%>
<br>
<%= ownerinfo.DetailUseGoodsName[i] %><br>
&nbsp;&nbsp;\<%= Kanma.get(ownerinfo.DetailUseGoodsPrice[i]) %>
<%
            if( ownerinfo.DetailUseGoodsDiscount[i] != 0 )
            {
%>
(\<%= Kanma.get(ownerinfo.DetailUseGoodsRegularPrice[i]) %>�@<%= Kanma.get(ownerinfo.DetailUseGoodsDiscount[i]) %>%)
<%
            }
        }
%>

<br>
<hr>
[���i����]

<%
        if( ownerinfo.DetailGoodsCount != 0 )
        {
%>
<%
            for( i = 0 ; i < ownerinfo.DetailGoodsCount ; i++ )
            {
%>
<br>
<%= ownerinfo.DetailGoodsName[i] %><br>
&nbsp;<%= nf.format(ownerinfo.DetailGoodsAmount[i]) %>&nbsp;\<%= Kanma.get(ownerinfo.DetailGoodsPrice[i]) %>
<%
            }
        }
%>

<hr>
<%
    }
    // �z�X�g��AMF�̏ꍇ�̂ݕ\�������܂�
%>


[���ް���]<br>
<%
    if( ownerinfo.MemberCustomId[0].compareTo("") != 0 )
    {
%>
�ԁ@��:<%= ownerinfo.MemberCustomId[0] %><br>
<%
        if(!ownerinfo.MemberName[0].equals(""))
        {
%>
����:<%= ownerinfo.MemberName[0] %><br>
<%
        }
%>
Ư�Ȱ�:<%= ownerinfo.MemberNickName[0] %><br>
�a����:<%= ownerinfo.MemberBirthday1[0] / 100 % 100 %>/<%= ownerinfo.MemberBirthday1[0] % 100 %>
<%if( ownerinfo.MemberBirthday2[0] != 0 ){%> <%= ownerinfo.MemberBirthday2[0] / 100 % 100 %>/<%= ownerinfo.MemberBirthday2[0] % 100 %><%}%><br>
�L�O��:<%= ownerinfo.MemberMemorial1[0] / 100 % 100 %>/<%= ownerinfo.MemberMemorial1[0] % 100 %>
<%if( ownerinfo.MemberMemorial2[0] != 0 ){%> <%= ownerinfo.MemberMemorial2[0] / 100 % 100 %>/<%= ownerinfo.MemberMemorial2[0] % 100 %><%}%><br>
�߲��&nbsp;:<%= Kanma.get(ownerinfo.MemberPoint[0]) %><br>
�ݸ:<%= ownerinfo.MemberRankName[0] %><br>

[��]<br>
�A��:<%= ownerinfo.MemberContact1[0] %><%= ownerinfo.MemberContact2[0] %><br>
�x��:<%= ownerinfo.MemberWarning1[0] %><%= ownerinfo.MemberWarning2[0] %><br>

<%
    }
%>

<%-- �����ڍ׏��\�������܂� --%>
<%
//���[���R�[�h���N���A����
        ownerinfo.RoomCode = 0;

%>
