<%@ page import="java.io.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
%>

<%
    int       readsize;
    byte      outbuff[] = new byte[4096];
    String    filepath;

    String hotelid = ReplaceString.getParameter(request,"HotelId");
    if( hotelid != null )
    {
		if(!CheckString.hotenaviIdCheck(hotelid))
		{
%>
		<script type="text/javascript">
		<!--
		var dd = new Date();
		setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
		//-->
		</script>
<%
		}
		else
		{
			// ファイル名を取得
			String filename = ReplaceString.getParameter(request,"fname");
			if( filename != null )
			{
				if( filename.compareTo("") != 0 )
				{
					if( filename.indexOf("/") == -1 && filename.indexOf("\\") == -1 && filename.indexOf("..") == -1 )
					{
						try
						{
							filepath = "/hotenavi/" + hotelid.toLowerCase() + "/pc/access/";

							FileInputStream fistream = new FileInputStream(filepath + filename);
							ServletOutputStream outstream = response.getOutputStream();

							if( filename.indexOf(".xls") >= 0 )
							{
								response.setContentType("application/vnd.ms-excel");
								response.setHeader("Content-disposition", "attachment; filename=\"" + filename + "\"");
							}
							else
							{
								response.setContentType("application/pdf");
								response.setHeader("Content-disposition", "inline; filename=\"" + filename + "\"");
							}

							while( (readsize = fistream.read(outbuff)) != -1 )
							{
								outstream.write(outbuff, 0, readsize);
							}

							fistream.close();
							outstream.close();
							return;
						}
						catch( Exception e )
						{
						}
					}
				}
			}
		}
    }

%>
<jsp:forward page="error.jsp" />

