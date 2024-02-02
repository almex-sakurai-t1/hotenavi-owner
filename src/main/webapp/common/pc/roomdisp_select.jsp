<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %><%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<script type="text/javascript">
<!--
    var dd = new Date();
    setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
//-->
</SCRIPT>
<%
    }
%>

<%
    String    param_href;
    String    storeselect;
    String    forward_page;

    // パラメタを取得し、Beanにセットしておく
    param_href = ReplaceString.getParameter(request,"href");

    // セッション属性から選択した店舗を取得
    storeselect = (String)session.getAttribute("SelectHotel");

    // 不明
    if( storeselect == null || param_href == null )
    {
        forward_page = "page.html";
    }
    else if( param_href.compareTo("use") == 0 )
    {
    // 利用状況
        if( storeselect.compareTo("all") == 0 )
        {
            forward_page = "roomdisp_use_f.html";
        }
        else
        {
            forward_page = "roomdisp_use.jsp";
        }
    }
    else if( param_href.compareTo("control") == 0 )
    {
    // 管理機状況
        if( storeselect.compareTo("all") == 0 )
        {
            forward_page = "roomdisp_control_f.html";
        }
        else
        {
            forward_page = "roomdisp_control.jsp";
        }
    }
    else if( param_href.compareTo("linen") == 0 )
    {
    // リネン状況
        if( storeselect.compareTo("all") == 0 )
        {
            forward_page = "roomdisp_linen_f.html";
        }
        else
        {
            forward_page = "roomdisp_linen.jsp";
        }
    }
    else if( param_href.compareTo("member") == 0 )
    {
    // メンバー状況
        if( storeselect.compareTo("all") == 0 )
        {
            forward_page = "roomdisp_member_f.html";
        }
        else
        {
            forward_page = "roomdisp_member.jsp";
        }
    }
    else if( param_href.compareTo("multi") == 0 )
    {
    // マルチメディア状況
        if( storeselect.compareTo("all") == 0 )
        {
            forward_page = "roomdisp_multi_f.html";
        }
        else
        {
            forward_page = "roomdisp_multi.jsp";
        }
    }
    else if( param_href.compareTo("car") == 0 )
    {
    // 車番状況
        if( storeselect.compareTo("all") == 0 )
        {
            forward_page = "../../common/pc/roomdisp_car_f.html";
        }
        else
        {
            forward_page = "roomdisp_car.jsp";
        }
    }
    else if( param_href.compareTo("tex") == 0 )
    {
    // 精算機状況
        if( storeselect.compareTo("all") == 0 )
        {
            forward_page = "roomdisp_tex_f.html";
        }
        else
        {
            forward_page = "roomdisp_tex.jsp";
        }
    }
    else if( param_href.compareTo("texpay") == 0 )
    {
    // 精算機入出金状況
        if( storeselect.compareTo("all") == 0 )
        {
            forward_page = "roomdisp_texpay_f.html";
        }
        else
        {
            forward_page = "roomdisp_texpay.jsp";
        }
    }
    else if( param_href.compareTo("fronttexpay") == 0 )
    {
    // フロント精算機入出金状況
        if( storeselect.compareTo("all") == 0 )
        {
            forward_page = "roomdisp_fronttexpay_f.html";
        }
        else
        {
            forward_page = "roomdisp_fronttexpay.jsp?amount=6";
        }
    }
    else if( param_href.compareTo("status") == 0 )
    {
    // ステータス遷移
        if( storeselect.compareTo("all") == 0 )
        {
            forward_page = "roomhistory_f.html";
        }
        else
        {
            forward_page = "roomhistory_f.html";
        }
    }
    else if( param_href.compareTo("kitchen") == 0 )
    {
    // キッチン端末
        if( storeselect.compareTo("all") == 0 )
        {
            forward_page = "kitchen_info_f.html";
        }
        else
        {
            forward_page = "kitchen_info.jsp";
        }
    }
    else if( param_href.compareTo("timechart") == 0 )
    {
    // タイムチャート
        if( storeselect.compareTo("all") == 0 )
        {
            forward_page = "roomdisp_timechart_f.html";
        }
        else
        {
            forward_page = "roomdisp_timechart.jsp";
        }
    }
    else
    {
        forward_page = "page.html";
    }

    // jsp:forwardではなくsendRedirectで処理
    response.sendRedirect(forward_page);
%>


