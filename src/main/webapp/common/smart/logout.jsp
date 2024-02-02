<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    Cookie    owncookie;

    owncookie = new Cookie( "ownhid", "" );
    owncookie.setPath( "/" );
    owncookie.setDomain( ".hotenavi.com" );
    owncookie.setMaxAge( 1 );
    response.addCookie( owncookie );

    owncookie = new Cookie( "ownnid", "" );
    owncookie.setPath( "/" );
    owncookie.setDomain( ".hotenavi.com" );
    owncookie.setMaxAge( 1 );
    response.addCookie( owncookie );

    owncookie = new Cookie( "ownlid", "" );
    owncookie.setPath( "/" );
    owncookie.setDomain( ".hotenavi.com" );
    owncookie.setMaxAge( 1 );
    response.addCookie( owncookie );

    owncookie = new Cookie( "ownnidsave", "" );
    owncookie.setPath( "/" );
    owncookie.setDomain( ".hotenavi.com" );
    owncookie.setMaxAge( 1 );
    response.addCookie( owncookie );

    owncookie = new Cookie( "ownlidsave", "" );
    owncookie.setPath( "/" );
    owncookie.setDomain( ".hotenavi.com" );
    owncookie.setMaxAge( 1 );
    response.addCookie( owncookie );

    // session”jŠü
    request.getSession().invalidate();

    response.sendRedirect(response.encodeURL("index.jsp"));
%>
