<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="jp.happyhotel.common.DateEdit" %>
<%@ page import="jp.happyhotel.common.DBConnection" %>
<%@ page import="jp.happyhotel.common.Logging" %>
<%
    NumberFormat nf2      = new DecimalFormat("00");
    DateEdit dateedit = new DateEdit();
    int nowdate   =  Integer.parseInt(dateedit.getDate(2));
    int nowtime   =  Integer.parseInt(dateedit.getTime(1));

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    String            query = "";
    String            title = "";
    int               id         = 0;
    int               start_date = 0;
    try
    {
    	connection    = DBConnection.getConnection();
        query = "SELECT * FROM hh_system_info WHERE data_type=31";
        query = query + " AND start_date <=" + nowdate;
        query = query + " AND end_date   >=" + nowdate;
        query = query + " AND (disp_flag =1 OR disp_flag =2)";
        query = query + " ORDER BY disp_idx DESC";
        prestate       = connection.prepareStatement(query);
        result         = prestate.executeQuery();
        if( result != null)
        {
            while( result.next() )
            {
                id         = result.getInt("id");
                title      = result.getString("title");
                start_date = result.getInt("start_date");
                if (id != 0)
                {
%>
					<tr>
                      <td bgcolor="#FF0000" class="size14">
					  <a href="javascript:;" onClick="MM_openBrWindow('/common/pc/information_detail.jsp?id=<%=id%>','_blank','menubar=no,location=no,scrollbars=yes,width=640,height=400')" >
					  <strong style="color:#FFFFFF; margin:'2' '2' '2' '2'">&nbsp;<%=title%>(<%=start_date/10000%>/<%=nf2.format(start_date/100%100)%>/<%=nf2.format(start_date%100)%>)<br></strong>
					  </a></td>
                    </tr>
<%
                }
            }
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }
%>
