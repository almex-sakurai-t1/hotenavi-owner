<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />


<TABLE width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#000000">
  <tr align="left">
    <TD valign="middle" nowrap class="tableRN" colspan="4"><div class="space2">明細</div></TD>
  </tr>

<%
    int    i;

    for( i = 0 ; i < ownerinfo.DetailUseCount ; i++ )
    {
        if( i % 2 != 0)
        {
%>
  <tr>
    <td align="left" valign="middle" class="tableRW" nowrap><div class="space2"><%= ownerinfo.DetailUseGoodsName[i] %></div></td>
<%
            if( ownerinfo.DetailUseGoodsCount[i] != 0 )
            {
%>
    <td align="right" valign="middle" class="tableRW2" nowrap><div class="space2"><%= ownerinfo.DetailUseGoodsCount[i] %></div></td>
<%
            }
            else
            {
%>
    <td align="right" valign="middle" class="tableRW2" nowrap><div class="space2">　</div></td>
<%
            }
%>
    <td align="right" valign="middle" class="tableRW2" nowrap><div class="space2">\ <%= Kanma.get(ownerinfo.DetailUseGoodsPrice[i]) %></div></td>
<%
            if( ownerinfo.DetailUseGoodsDiscount[i] != 0 )
            {
%>
    <td align="right" valign="middle" nowrap class="tableRW2"><div class="space2"><FONT color="#FF0000">　\ <%= Kanma.get(ownerinfo.DetailUseGoodsRegularPrice[i]) %>　<%= Kanma.get(ownerinfo.DetailUseGoodsDiscount[i]) %>%　</FONT></div></td>
<%
            }
            else
            {
%>
    <td align="right" valign="middle" nowrap class="tableRW2"><div class="space2">　</div></td>
<%
            }
%>
  </TR>
<%
        }
        else
        {
%>
  <TR>
    <td align="left" valign="middle" class="tableRG" nowrap><div class="space2"><%= ownerinfo.DetailUseGoodsName[i] %></div></td>
<%
            if( ownerinfo.DetailUseGoodsCount[i] != 0 )
            {
%>
    <td align="right" valign="middle" class="tableRG2" nowrap><div class="space2"><%= ownerinfo.DetailUseGoodsCount[i] %></div></td>
<%
            }
            else
            {
%>
    <td align="right" valign="middle" class="tableRG2" nowrap><div class="space2">　</div></td>
<%
            }
%>
    <td align="right" valign="middle" class="tableRG2" nowrap><div class="space2">\ <%= Kanma.get(ownerinfo.DetailUseGoodsPrice[i]) %></div></td>
<%
            if( ownerinfo.DetailUseGoodsDiscount[i] != 0 )
            {
%>
    <td align="right" valign="middle" nowrap class="tableRG2"><div class="space2"><FONT color="#FF0000">　\ <%= Kanma.get(ownerinfo.DetailUseGoodsRegularPrice[i]) %>　<%= Kanma.get(ownerinfo.DetailUseGoodsDiscount[i]) %>%　</FONT></div></td>
<%
            }
            else
            {
%>
    <td align="right" valign="middle" nowrap class="tableRG2"><div class="space2">　</div></td>
<%
            }
%>

<%
        }
%>
  </tr>
<%
    }
%>
</TABLE>

