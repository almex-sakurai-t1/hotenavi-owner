<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr align="left">
          <td colspan="2" valign="middle" nowrap class="tableRN"><div class="space2">éxï•Ç¢èÛãµ</div></td>
        </tr>
        <tr>
          <td align="left" valign="middle" class="tableLW" nowrap><div class="space2"><font color="#3300FF">êøãÅã‡äz</font></div></td>
          <td align="right" valign="middle" class="tableLW" nowrap><div class="space2"><font color="#3300FF">\<%= Kanma.get(ownerinfo.DetailPayClaim) %></font></div></td>
        </tr>
        <tr>
          <td align="left" valign="middle" class="tableLG"><div class="space2"><font color="#3300FF">Ç≤óòópçáåv</font></div></td>
          <td align="right" valign="middle" class="tableLG"><div class="space2"><font color="#3300FF">\<%= Kanma.get(ownerinfo.DetailPayTotal) %></font></div></td>
        </tr>

<%
    int    i;
    for( i = 0 ; i < ownerinfo.DetailPayCount ; i++ )
    {
%>

        <TR>
          <TD class="space2"><FONT color="#0000FF" size="2"><B class="space2"><%= ownerinfo.DetailPayName[i] %></B></FONT></TD>
          <TD align="right" class="space2"><font color="#3300FF">\</font><FONT color="#0000FF" size="2"><B class="space2"><%= Kanma.get(ownerinfo.DetailPayMoney[i]) %></B></FONT></TD>
        </TR>

<%
    }
%>
      </table>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td><img src="/common/pc/image/spacer.gif" width="80" height="20"></td>
        </tr>
      </table>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr align="left">
          <td valign="middle" nowrap class="tableRN"><div class="space2">òAóçÉÅÉÇ</div></td>
        </tr>
        <tr>
          <td align="left" valign="middle" class="tableRW" nowrap>
            <div class="space2">
              <FONT color="#0000FF" size="1">
                <B class="size10"><%= ownerinfo.MemberContact1[0] %><BR><%= ownerinfo.MemberContact2[0] %></B>
              </FONT>
            </div>
          </td>
        </tr>
      </table>

      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td><img src="/common/pc/image/spacer.gif" width="80" height="20"></td>
        </tr>
      </table>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr align="left">
          <td valign="middle" bgcolor="#ff0000" nowrap class="tableRN"><div class="space2">åxçêÉÅÉÇ</div></td>
        </tr>
        <tr>
          <td align="left" valign="top" class="tableRW" nowrap>
            <div class="space2">
              <FONT color="#FF0000" size="1">
                <B class="size10"><%= ownerinfo.MemberWarning1[0] %><BR><%= ownerinfo.MemberWarning2[0] %></B>
              </FONT>
            </div>
          </td>
        </tr>
      </table>


