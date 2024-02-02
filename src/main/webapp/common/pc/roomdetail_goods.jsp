<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr align="left">
    <TD colspan="7" valign="middle" nowrap class="tableRN"><div class="space2">è§ïiñæç◊</div></TD>
  </tr>

<%
    int    i;

    for( i = 0 ; i < ownerinfo.DetailGoodsCount ; i++ )
    {
        if( i % 2 != 0 )
        {
%>
  <tr>
    <td align="left" valign="middle" class="tableRW" nowrap><div class="space2">Å@<%= ownerinfo.DetailGoodsName[i] %></div></td>
    <td align="right" valign="middle" class="tableRW2" nowrap><div class="space2"><%= ownerinfo.DetailGoodsAmount[i] %> </div></td>
    <td align="right" valign="middle" class="tableRW2" nowrap><div class="space2">\ <%= Kanma.get(ownerinfo.DetailGoodsPrice[i]) %> </div></td>
<%
            switch( ownerinfo.DetailGoodsRef[i] )
            {
              case  1:
%>
    <td align="right" valign="middle" nowrap class="tableRW2"><div class="space2">ó‚</div></td>
<%
                break;
              case  2:
%>
    <td align="right" valign="middle" nowrap class="tableRW2"><div class="space2">é©</div></td>
<%
                break;
              case  3:
%>
    <td align="right" valign="middle" nowrap class="tableRW2"><div class="space2">éË</div></td>
<%
                break;
              default:
%>
    <td align="right" valign="middle" nowrap class="tableRW2"><div class="space2"></div></td>
<%
                break;
            }
%>
  </tr>
<%
        }
        else
        {
%>
  <tr>
    <td align="left" valign="middle" class="tableRG" nowrap><div class="space2">Å@<%= ownerinfo.DetailGoodsName[i] %></div></td>
    <td align="right" valign="middle" class="tableRG2" nowrap><div class="space2"><%= ownerinfo.DetailGoodsAmount[i] %> </div></td>
    <td align="right" valign="middle" class="tableRG2" nowrap><div class="space2">\ <%= Kanma.get(ownerinfo.DetailGoodsPrice[i]) %> </div></td>
<%
            switch( ownerinfo.DetailGoodsRef[i] )
            {
              case  1:
%>
    <td align="right" valign="middle" nowrap class="tableRG2"><div class="space2">ó‚</div></td>
<%
                break;
              case  2:
%>
    <td align="right" valign="middle" nowrap class="tableRG2"><div class="space2">é©</div></td>
<%
                break;
              case  3:
%>
    <td align="right" valign="middle" nowrap class="tableRG2"><div class="space2">éË</div></td>
<%
                break;
              default:
%>
    <td align="right" valign="middle" nowrap class="tableRG2"><div class="space2"></div></td>
<%
                break;
            }
%>
  </tr>
<%
        }
    }
%>

</TABLE>


