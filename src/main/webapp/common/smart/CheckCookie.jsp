<%@ page contentType="text/html; charset=Windows-31J" %>
<%

   // cookie‚ÌŠm”F
    int              loop;
    Cookie[]         cookies;
    String           loginHid = "";
    String           loginLid = "";
    String           loginNid = "";
    String           loginLidSave = "";
    String           loginNidSave = "";

    cookies = request.getCookies();
    if( cookies != null )
    {
        for( loop = 0 ; loop < cookies.length ; loop++ )
        {
            if( cookies[loop].getName().compareTo("ownhid") == 0 )
            {
                loginHid = cookies[loop].getValue();
                break;
            }
        }
        for( loop = 0 ; loop < cookies.length ; loop++ )
        {
            if( cookies[loop].getName().compareTo("ownnid") == 0 )
            {
                loginNid = cookies[loop].getValue();
                break;
            }
        }
        for( loop = 0 ; loop < cookies.length ; loop++ )
        {
            if( cookies[loop].getName().compareTo("ownlid") == 0 )
            {
                loginLid = cookies[loop].getValue();
                break;
            }
        }
        for( loop = 0 ; loop < cookies.length ; loop++ )
        {
            if( cookies[loop].getName().compareTo("ownnidsave") == 0 )
            {
                loginNidSave = cookies[loop].getValue();
                break;
            }
        }
        for( loop = 0 ; loop < cookies.length ; loop++ )
        {
            if( cookies[loop].getName().compareTo("ownlidsave") == 0 )
            {
                loginLidSave = cookies[loop].getValue();
                break;
            }
        }
    }
%>
