<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%

        DateEdit dateedit    = new DateEdit();

        String hotelid       = (String)session.getAttribute("SelectHotel");
        String InputEntryId  =  ReplaceString.getParameter(request,"EntryId");
        if    (InputEntryId == null) InputEntryId = "0";
        int    entry_id      = Integer.parseInt(InputEntryId);

        Connection        connection  = null;
        PreparedStatement prestate    = null;
        ResultSet         result      = null;
        connection       = DBConnection.getConnection();

        int i            = 0;
        int entry_branch = 0;
        int count        = 0;
        int count_max    = 0;
        String query     = "";

        query = "SELECT * FROM goods_entry,goods WHERE goods_entry.hotelid=?";
        query = query + " AND goods_entry.hotelid     = goods.hotelid";
        query = query + " AND goods_entry.category_id = goods.category_id";
        query = query + " AND goods_entry.seq         = goods.seq";
        query = query + " AND goods_entry.input_date  >= goods.disp_from";
        query = query + " AND goods_entry.input_date  <= goods.disp_to";
        query = query + " AND goods_entry.count!=0";
        query = query + " AND goods_entry.entry_id= ?";
        prestate = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        prestate.setInt(2,entry_id);
        result   = prestate.executeQuery();
%>
						<table>
<%
        while( result.next() != false )
        {
            entry_branch = result.getInt("goods_entry.entry_branch");
            count++;
            if (count % 4 == 1)
            {
%>
							<tr>
<%
            }
%>
								<td align="left" valign="top">
									<table border="0" cellspacing="1" cellpadding="3">
										<tr>
											<td class="tableLN" width="125" colspan="2" nowrap>&nbsp;
												<%= new String(result.getString("goods.title").getBytes("Shift_JIS"), "Windows-31J") %>
											</td>
										</tr>
										<tr>
											<td  class="tableLW tableRW" width="125" colspan="2" nowrap>&nbsp;
												<%= new String(result.getString("goods.title_sub1").getBytes("Shift_JIS"), "Windows-31J") %>
											</td>
										</tr>
										<tr>
											<td class="tableLW tableRW" width="125" height="150" colspan="2" align=center>
<%
            if(result.getString("goods.picture_pc").compareTo("") != 0)
            {
%>
												<img src="http://www.hotenavi.com/<%=hotelid%>/<%=result.getString("goods.picture_pc")%>" style="clear:both">
<%
            }
%>
												<div align=left><%= new String(result.getString("goods.msg").getBytes("Shift_JIS"), "Windows-31J") %></font></div>
											</td>
										</tr>
										<tr>
<%
            if(result.getInt("goods.count_flag") == 1)
            {
               count_max = result.getInt("goods.count_max");
            }
            else
            {
               count_max = result.getInt("goods.count_unit");
            }
%>
											<td class="tableLW" nowrap>
												å¬êî:<%=result.getInt("goods_entry.count")%>
											</td>
											<td  class="tableLW  tableRW" align="right">
												<%=result.getInt("goods_entry.use_total")%>É|ÉCÉìÉg
											</td>
										</tr>
									</table>
								</td>
<%
            if (count % 4 == 0)
            {
%>
							</tr>
<%
            }
        }
%>
<%
       if (count % 4 != 0)
       {
%>
							</tr>
<%
        }
%>
						</table>
<%
        DBConnection.releaseResources(result,prestate,connection);
%>
