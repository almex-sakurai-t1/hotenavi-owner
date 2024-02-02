<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="jp.happyhotel.common.ReplaceString" %><%@ page import="jp.happyhotel.common.CheckString" %>
<%@ include file="../csrf/refererCheck.jsp" %><%@ include file="../csrf/checkCsrfForPost.jsp" %><%@ include file="../../common/pc/owner_ini.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
%><%@ include file="../../common/pc/magazine_paramget.jsp" %><%
    int    deliver = 0;
    String param_subject = ReplaceString.getParameter(request,"subject");
    String param_body = ReplaceString.getParameter(request,"body");
    String param_unsubscribe_url = ReplaceString.getParameter(request,"unsubscribe_url");
    param_body = param_body.replace("<br>","\r\n");
    String param_deliver = ReplaceString.getParameter(request,"deliver");
    String param_year = ReplaceString.getParameter(request,"year");
    String param_month = ReplaceString.getParameter(request,"month");
    String param_day = ReplaceString.getParameter(request,"day");
    String param_hour = ReplaceString.getParameter(request,"hour");
    String param_min = ReplaceString.getParameter(request,"min");
    String param_historyid = ReplaceString.getParameter(request,"historyid");
    char c;
    int  c_index = 0;
    int  i       = 0;
    boolean ErrorSubject = false;
    boolean ErrorBody    = false;

    if (param_subject.compareTo("") == 0) ErrorSubject = true;

    String[] checkbody; //本文チェック用
    checkbody = param_body.split("\r\n");
    for( i = 0 ; i < checkbody.length; i++ )
    {
        if(checkbody[i].length() > 256) ErrorBody = true;
    }
    if( param_deliver != null )
    {
        deliver = Integer.parseInt(param_deliver);
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>メルマガ作成</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
      </tr>
    </table>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="180" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF">メルマガ作成</font></td>
              <td width="15" height="20"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="../../common/pc/image/spacer.gif" width="200" height="20"></div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- ここから表 -->
      <tr>
        <td align="center" valign="top" bgcolor="#FFF5EE"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><img src="../../common/pc/image/spacer.gif" width="400" height="12"></td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td class="size12"><font color="#CC0000"><strong>4.メールの内容を確認してください</strong></font></td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
          </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td></td>
              </tr>
              <tr>
                <td valign="top">
                <form name=form0 action="magazine_edit_commit.jsp" method="post">
<%
    if( param_historyid != null )
    {
%>
                  <input type="hidden" name="historyid" value="<%= param_historyid %>">
<%
    }
%>
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td align="center" class="size12">
                          件名：<%= param_subject %></td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                      </tr>
                      <tr valign="top">
                        <td width="8" align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="center">
                          <table height="420" border="2" cellpadding="3" cellspacing="0" bordercolor="#666666">
                            <tr>
                              <td width="420" valign="top" bgcolor="#FFFFFF" class="sizepre">
<%= param_body.replace("\r\n","<br>").replace("[unsubscribe]",param_unsubscribe_url)%>
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <tr>
                        <td colspan="2" align="left"><img src="../../common/pc/image/spacer.gif" width="200" height="14"></td>
                        </tr>
                      <tr>
                        <td width="8" align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
<%
    if( deliver == 1 )
    {
%>
                        <td align="center" class="size12">配信予定時間&nbsp;:&nbsp;<%= param_year %>年<%= param_month %>月<%= param_day %>日<%= param_hour %>時<%= param_min %>分 </td>
<%
    }
    else
    {
%>
                        <td align="center" class="size12">配信予定時間&nbsp;:&nbsp;すぐに配信 </td>
<%
    }
%>
                      </tr>
                      <tr>
                        <td align="left">&nbsp;</td>
                        <td align="left" class="size12">&nbsp;</td>
                      </tr>
<%
    if(!ErrorSubject && !ErrorBody)
    {
%>
                      <tr>
                        <td align="left">&nbsp;</td>
                        <td align="center" valign="middle" class="size12" id="confirmMessage">これでよろしいですか？</td>
                      </tr>
                      <tr>
                        <td colspan="2" align="left"><img src="../../common/pc/image/spacer.gif" width="200" height="14"></td>
                        </tr>
                      <tr valign="middle">
                        <td colspan="2" align="center" class="size12" id="Submit3">
                          <input type="hidden" name="subject" id="subject" value="<%=param_subject%>">
                          <input type="hidden" name="body"    id="body"    value="<%=param_body%>">
                          <input type="hidden" name="deliver" id="subject" value="<%=param_deliver%>">
                          <input type="hidden" name="year"    id="year"    value="<%=param_year%>">
                          <input type="hidden" name="month"   id="month"   value="<%=param_month%>">
                          <input type="hidden" name="day"     id="day"     value="<%=param_day%>">
                          <input type="hidden" name="hour"    id="hour"    value="<%=param_hour%>">
                          <input type="hidden" name="min"     id="min"     value="<%=param_min%>">

<%@ include file="../../common/pc/magazine_paramput.jsp" %>
<%
        if( param_historyid != null )
        {
%>
        <input type="checkbox" name="Retry" id="Retry" value="1">メールアドレスを再抽出する<br>
<%
        }
%>
						<input name="Submit3" type="submit" value="はい" onclick="document.getElementById('Submit3').style.display='none';document.getElementById('confirmMessage').innerText='処理中です。しばらくお待ちください';">
                        <img src="../../common/pc/image/spacer.gif" width="12" height="12">
                        <input name="back" type="button" onclick="history.back();" value="前にもどる">
                      </tr>
<%
    }
    else
    {
%>
                      <tr>
                        <td align="left">&nbsp;</td>
                        <td align="center" valign="middle" class="size12">
                          <table border="0" cellpadding="0" cellspacing="0">
                            <tr>
                               <td align="left" valign="middle" class="size12">
                                 <font color=red><strong>
<%
        if(ErrorSubject)
        {
%>                               
								件名を入力してください。<br><br>
<%
        }
        if(ErrorBody)
        {
%>                               
								改行なしで256文字を超えると正常に送信できません。<br>改行を入れてください。<br>
<%
        }
%>
                                 </strong></font>
                               </td>
                             </tr>
                          </table>
                        </td>
                      </tr>
                      <tr>
                        <td colspan="2" align="left"><img src="../../common/pc/image/spacer.gif" width="200" height="14"></td>
                        </tr>
                      <tr valign="middle">
                        <td colspan="2" align="center" class="size12">
                        <input name="back" type="button" onclick="history.back();" value="前にもどる">
                      </tr>
<%
    }
%>
                  </table>
		  </form>
                </td>
              </tr>
           </table>
        </td>
        <td width="3" valign="top" align="left" height="100%">
          <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><img src="../../common/pc/image/tab_kado.gif" width="3" height="3"></td>
            </tr>
            <tr>
              <td bgcolor="#666666" height="100%"><img src="../../common/pc/image/spacer.gif" width="3" height="100"></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td height="3" bgcolor="#999999">
          <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="3"><img src="../../common/pc/image/tab_kado2.gif" width="3" height="3"></td>
              <td bgcolor="#666666"><img src="../../common/pc/image/spacer.gif" width="100" height="3"></td>
            </tr>
          </table>
        </td>
        <td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="3"></td>
      </tr>
      <!-- ここまで -->
    </table></td>
  </tr>
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/Library/footer.lbi" --><img src="../../common/pc/image/imedia.gif" width="63" height="18" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyright&copy; almex
      inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
