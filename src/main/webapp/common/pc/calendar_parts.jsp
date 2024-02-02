<%@ page contentType="text/html; charset=Windows-31J" %>
    <td bgcolor="#BBBBBB" width=50% style="vertical-align:top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="3"></td>
          <td height="3" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="3"></td>
          <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="3"></td>
          <td width="3"><img src="../../common/pc/image/grey.gif" width="3" height="3"></td>
        </tr>
        <tr>
          <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="20"></td>
          <td valign="top" colspan=2>
            <table border="0" cellspacing="0" cellpadding="0" width="100%" >
              <tr> 
                <td height="20">
                  <div class="size14">
<table border="0" cellspacing="0" cellpadding="2" width="100%">
    <tr>
      <td height="30" colspan="7"><table width="100%" height="30" border="0" cellpadding="0" cellspacing="0">
          <tr valign="middle">
            <td align="center" bgcolor="#21323F"><div class="size14"><font color="#FFFFFF"><%= year %>îN<%= month %>åé</font></div>
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td colspan="7"><img src="../../common/pc/image/spacer.gif" width="100" height="4"></td>
    </tr>
    <tr>
      <td>
        <table width="99%" height="16" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td align="center" valign="middle" bgcolor="#AF0011"><div class="size10"><font color="#FFFFFF">ì˙</font></div>
            </td>
          </tr>
        </table>
      </td>
      <td>
        <table width="99%" height="16" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td align="center" valign="middle" bgcolor="#000066"><div class="size10"><font color="#FFFFFF">åé</font></div>
            </td>
          </tr>
        </table>
      </td>
      <td>
        <table width="99%" height="16" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td align="center" valign="middle" bgcolor="#000066"><div class="size10"><font color="#FFFFFF">âŒ</font></div>
            </td>
          </tr>
        </table>
      </td>
      <td>
        <table width="99%" height="16" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td align="center" valign="middle" bgcolor="#000066"><div class="size10"><font color="#FFFFFF">êÖ</font></div>
            </td>
          </tr>
        </table>
      </td>
      <td>
        <table width="99%" height="16" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td align="center" valign="middle" bgcolor="#000066"><div class="size10"><font color="#FFFFFF">ñÿ</font></div>
            </td>
          </tr>
        </table>
      </td>
      <td>
        <table width="99%" height="16" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td align="center" valign="middle" bgcolor="#000066"><div class="size10"><font color="#FFFFFF">ã‡</font></div>
            </td>
          </tr>
        </table>
      </td>
      <td>
        <table width="99%" height="16" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td align="center" valign="middle" bgcolor="#000066"><div class="size10"><font color="#FFFFFF">ìy</font></div>
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>

<!-- ëÊÇPèTñ⁄ÇÃï\é¶ -->
<%
    day = 1;

    // ãÛîíÇÃì˙ït
    for( i = 1 ; i < start ; i++ )
    {
%>
      <td style="height:99%">
        <table style="visibility: hidden;" border="0" cellpadding="0" cellspacing="0" class="access" style="">
          <tr>
            <td align="center" valign="middle">
              <a href="#"><div class="size12"> </div></a>
            </td>
          </tr>
        </table>
      </td>
<%
    }

    for( i = start ; i < 8 ; i++ )
    {
        if( i == 1 )
        {
%>

      <td style="height:99%">
        <table style="width:99%; height:99%" border="0" cellpadding="0" cellspacing="0" class="access">
          <tr>
            <td align="center" valign="middle" bgcolor="#FF0000">
              <div class="size18"> <%= day %></div><div class="size12"><%=ownerinfo.CalDayModeName[day-1].equals("")?"ñ¢ê›íË":ownerinfo.CalDayModeName[day-1]%></div>
            </td>
          </tr>
        </table>
      </td>

<%
        }
        else if( i == 7 )
        {
%>
      <td style="height:99%">
        <table style="width:99%; height:99%" border="0" cellpadding="0" cellspacing="0" class="access">
          <tr>
            <td align="center" valign="middle" bgcolor="#00FFFF">
              <div class="size18"> <%= day %></div><div class="size12"><%=ownerinfo.CalDayModeName[day-1].equals("")?"ñ¢ê›íË":ownerinfo.CalDayModeName[day-1]%></div>
            </td>
          </tr>
        </table>
      </td>
<%
        }
        else
        {
%>
      <td style="height:99%">
        <table style="width:99%; height:99%" border="0" cellpadding="0" cellspacing="0" class="access">
          <tr>
            <td align="center" valign="middle">
              <div class="size18"> <%= day %></div><div class="size12"><%=ownerinfo.CalDayModeName[day-1].equals("")?"ñ¢ê›íË":ownerinfo.CalDayModeName[day-1]%></div>
            </td>
          </tr>
        </table>
      </td>
<%
        }

        day++;
    }
%>
<!-- ëÊÇPèTñ⁄ÇÃï\é¶Ç±Ç±Ç‹Ç≈ -->

    </tr>

<!-- ëÊÇQèTñ⁄à»ç~ -->
<%
    while( day <= max_month )
    {
%>
    <tr>
<%
        for( i = 1 ; i <= 7 && day <= max_month ; i++ )
        {
            if( i == 1 )
            {
%>
      <td style="height:99%">
        <table style="width:99%; height:99%" border="0" cellpadding="0" cellspacing="0" class="access">
          <tr>
            <td align="center" valign="middle" bgcolor="#FF0000">
              <div class="size18"> <%= day %></div><div class="size12"><%=ownerinfo.CalDayModeName[day-1].equals("")?"ñ¢ê›íË":ownerinfo.CalDayModeName[day-1]%></div>
            </td>
          </tr>
        </table>
      </td>
<%
            }
            else if( i == 7 )
            {
%>
      <td style="height:99%">
        <table style="width:99%; height:99%" border="0" cellpadding="0" cellspacing="0" class="access">
          <tr>
            <td align="center" valign="middle" bgcolor="#00FFFF">
              <div class="size18"> <%= day %></div><div class="size12"><%=ownerinfo.CalDayModeName[day-1].equals("")?"ñ¢ê›íË":ownerinfo.CalDayModeName[day-1]%></div>
            </td>
          </tr>
        </table>
      </td>
<%
            }
            else
            {
%>
      <td style="height:99%">
        <table style="width:99%; height:99%" border="0" cellpadding="0" cellspacing="0" class="access">
          <tr>
            <td align="center" valign="middle">
              <div class="size18"> <%= day %></div><div class="size12"><%=ownerinfo.CalDayModeName[day-1].equals("")?"ñ¢ê›íË":ownerinfo.CalDayModeName[day-1]%></div>
            </td>
          </tr>
        </table>
      </td>
<%
            }

            day++;
        }
%>
    </tr>

<%
    }
%>
<!-- ëÊÇQèTñ⁄à»ç~Ç±Ç±Ç‹Ç≈ -->

    <tr>
      <td colspan="7"><img src="../../common/pc/image/spacer.gif" width="200" height="4"></td>
    </tr>
                  </div>
                </td>
              </tr>
            </table>
          </td>
          <td bgcolor="#666666"><img src="../../common/pc/image/grey.gif" width="3"></td>
        </tr>
      </table>
    </td>
