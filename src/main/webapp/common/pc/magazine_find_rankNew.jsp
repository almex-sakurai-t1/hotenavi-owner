<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error/error.html" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int i;
    
    // �Z�b�V�����������I�����ꂽ�z�e�����擾
    String selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }

    // �����o�[�����N�̎擾
    ownerinfo.sendPacket0148(1, selecthotel);
%>

  <input disabled name="RankCheck" type="checkbox" id="RankCheck" value="1" onClick="setCondition(this,RankStart,RankEnd);">
  �����N�F
    <select name="RankStart" id="RankStart" onchange="setCheck(RankCheck,RankStart,RankEnd);">
    <option value="" selected>�w��Ȃ�

<%
    for( i = 0 ; i < ownerinfo.CustomRankCount ; i++ )
    {
%>
    <option value="<%= ownerinfo.CustomRankCode[i] %>"><%= ownerinfo.CustomRankName[i] %>
<%
    }
%>
    </select>
    �`
    <select name="RankEnd" id="RankEnd" onchange="setCheck(RankCheck,RankStart,RankEnd);">
    <option value="" selected>�w��Ȃ�
<%
    for( i = 0 ; i < ownerinfo.CustomRankCount ; i++ )
    {
%>
    <option value="<%= ownerinfo.CustomRankCode[i] %>"><%= ownerinfo.CustomRankName[i] %>
<%
    }
%>
    </select>
