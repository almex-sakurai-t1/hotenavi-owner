<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int         now_date;
    int         now_time;
    DateEdit    df;

    df        = new DateEdit();
    now_date  = Integer.valueOf(df.getDate(2)).intValue();
    now_time  = Integer.valueOf(df.getTime(1)).intValue();
%>

<TABLE width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="left" valign="middle" nowrap class="tableRN">利用者状況</td>
  </tr>
  <tr>
    <td align="left" valign="top" class="tableRW" nowrap>
      <table width="100%" border="0" cellpadding="2" cellspacing="2">
        <tr>
          <td valign="top" nowrap class="size12">入室時刻：

<%
    if( ownerinfo.StateInDate[0] != 0 )
    {
%>
            <%= ownerinfo.StateInDate[0] / 10000%>年<%= ownerinfo.StateInDate[0] / 100 % 100 %>月<%= ownerinfo.StateInDate[0] % 100 %>日 <%= ownerinfo.StateInTime[0] / 100 %>時<%= ownerinfo.StateInTime[0] % 100 %>分
<%
    }
%>
            <br>
            退室時刻：
<%
    if( ownerinfo.StateOutDate[0] == 0 )
    {
        if( ownerinfo.StateInDate[0] != 0 )
        {
%>
            <%= now_date / 10000%>年<%= now_date / 100 % 100 %>月<%= now_date % 100 %>日 <%= now_time / 10000 %>時<%= now_time / 100 % 100 %>分(現在)
<%
        }
    }
    else
    {
%>
        <%= ownerinfo.StateOutDate[0] / 10000%>年<%= ownerinfo.StateOutDate[0] / 100 % 100 %>月<%= ownerinfo.StateOutDate[0] % 100 %>日 <%= ownerinfo.StateOutTime[0] / 100 %>時<%= ownerinfo.StateOutTime[0] % 100 %>分
<%
    }
%>
            <br>
            利用人数：<%= ownerinfo.StatePerson[0] %>人
          </td>
        </tr>
        <tr>
          <td height="97" valign="top" nowrap class="size12">

<%
    if( ownerinfo.StateCustomId[0].compareTo("") != 0 )
    {
%>
            <IMG src="/common/pc/image/card.gif"><%= ownerinfo.StateCustomRankName[0] %>
<%
    }
%>
          </td>
        </tr>
        <tr>
          <td nowrap class="size12">&nbsp;</td>
        </tr>
      </table>
    </td>
  </tr>
</TABLE>

