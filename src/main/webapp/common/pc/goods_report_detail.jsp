<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%

    NumberFormat nf2      = new DecimalFormat("00");
    StringFormat sf;
    sf = new StringFormat();

    String loginHotelId =  (String)session.getAttribute("LoginHotelId");
    String hotelid = (String)session.getAttribute("SelectHotel");
    if (hotelid.compareTo("all")==0)
    {
       hotelid = loginHotelId;
    }
    String limit_flag = ReplaceString.getParameter(request,"limit_flag");
    if(limit_flag   == null) limit_flag = "true";
    if(limit_flag.compareTo("") == 0) limit_flag ="true";
    String detail_flag = ReplaceString.getParameter(request,"detail_flag");
    if(detail_flag   == null) detail_flag = "false";
    if(detail_flag.compareTo("") == 0) detail_flag ="false";
    String  InputCustomId = ReplaceString.getParameter(request,"custom_id");
    String  InputEntryId  = ReplaceString.getParameter(request,"EntryId");
    if     (InputEntryId == null) InputEntryId = "0";
    int     entry_id          = Integer.parseInt(InputEntryId);
    String  InputEntryBranch  = ReplaceString.getParameter(request,"EntryBranch");
    if     (InputEntryBranch == null) InputEntryBranch = "0";
    int     entry_branch      = Integer.parseInt(InputEntryBranch);
    String  InputStatus       = ReplaceString.getParameter(request,"Status");
    if     (InputStatus == null) InputStatus = "0";
    int     input_status      = Integer.parseInt(InputStatus);

    DateEdit dateedit = new DateEdit();

    int nowdate   =  Integer.parseInt(dateedit.getDate(2));
    int nowtime   =  Integer.parseInt(dateedit.getTime(1));
    String nowdate_s = nf2.format(nowdate / 10000 ) + "/" + nf2.format(nowdate / 100 % 100 ) + "/" + nf2.format(nowdate % 100 );
    String nowtime_s = nf2.format(nowtime / 10000 ) + ":" + nf2.format(nowtime / 100 % 100 ) + ":" + nf2.format(nowtime % 100 );

    int pageno;
    String param_page = ReplaceString.getParameter(request,"page");
    if( param_page == null )
    {
        pageno = 0;
    }
    else
    {
        pageno = Integer.parseInt(param_page);
    }

    String  custom_id        = "";
    String  user_id          = "";
    String  name             = "";
    String  mail_address     = "";
    int     point            = 0;
    int     count            = 0;
    int     use_total        = 0;
    int     method           = 0;
    String  send_name        = "";
    String  zip_code         = "";
    String  pref_name        = "";
    String  address1         = "";
    String  address2         = "";
    String  tel1             = "";
    String  memo             = "";
    String  topic_hotelid    = "";
    String  nick_name        = "";
    int     input_date       = 0;
    int     input_time       = 0;
    int     status           = 0;

    String            query = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection = DBConnection.getConnection();

    query = "SELECT COUNT(*),SUM(goods_entry.use_total),goods_entry.* FROM goods_entry WHERE goods_entry.hotelid=?";
    query = query + " AND goods_entry.entry_id    = ?";
    if (detail_flag.equals("true"))
    {
        query = query + " AND goods_entry.entry_branch  = ?";
    }
    if (hotelid.equals("happyhotel"))
    {
        query = query + " AND goods_entry.status=?";
    }
    query = query + " AND goods_entry.count!=0";
    query = query + " AND goods_entry.status>0";
    query = query + " GROUP BY goods_entry.entry_id";
    prestate = connection.prepareStatement(query);
    int col = 1;
    prestate.setString(col++, hotelid);
    prestate.setInt(col++,entry_id);
    if (detail_flag.equals("true"))
    {
        prestate.setInt(col++,entry_branch);
    }
    if (hotelid.equals("happyhotel"))
    {
        prestate.setInt(col++,input_status);
    }
    result   = prestate.executeQuery();
    try
    {
        if( result != null )
        {
            if( result.next() != false )
            {
                custom_id    = result.getString("goods_entry.custom_id");
                user_id      = result.getString("goods_entry.user_id");
                name         = result.getString("goods_entry.name");
                mail_address = result.getString("goods_entry.mail_address");
                point        = result.getInt("goods_entry.point");
                count        = result.getInt(1);
                use_total    = result.getInt(2);
                method       = result.getInt("goods_entry.method");
                send_name    = result.getString("goods_entry.send_name");
                zip_code     = result.getString("goods_entry.zip_code");
                pref_name    = result.getString("goods_entry.pref_name");
                address1     = result.getString("goods_entry.address1");
                address2     = result.getString("goods_entry.address2");
                tel1         = result.getString("goods_entry.tel1");
                memo         = result.getString("goods_entry.memo");
                nick_name    = result.getString("goods_entry.nick_name");
                topic_hotelid= result.getString("goods_entry.topic_hotelid");
                input_date   = result.getInt("goods_entry.input_date");
                input_time   = result.getInt("goods_entry.input_time");
                status       = result.getInt("goods_entry.status");
            }
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    String  common_title = "景品";
    String  TelFlag         = "必須";
    String  MailaddressFlag = "必須";
    String  memo_title      = "";
    String  NickNameFlag    = "なし";
    String  NameFlag        = "あり";

    query = "SELECT * FROM edit_event_info WHERE hotelid='" + topic_hotelid + "'";
    query = query + " AND disp_flg=1";
    query = query + " AND disp_idx=1";
    query = query + " AND data_type=82";
    query = query + " AND start_date<=" + nowdate;
    query = query + " AND end_date>=" +  nowtime;
    query = query + " ORDER BY id DESC";
    prestate = connection.prepareStatement(query);
    result   = prestate.executeQuery();
    try
    {
        if( result != null )
        {
            if( result.next() != false )
            {
                common_title     =  new String(result.getString("msg1_title").getBytes("Shift_JIS"), "Windows-31J");
                memo_title       =  new String(result.getString("msg2_title").getBytes("Shift_JIS"), "Windows-31J");
                NameFlag         =  result.getString("msg5_title");
                NickNameFlag     =  result.getString("msg6_title");
                MailaddressFlag  =  result.getString("msg7_title");
                TelFlag          =  result.getString("msg8_title");
            }
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    String  hotel_name      = "";

    query = "SELECT name FROM hotel WHERE hotel_id ='" + topic_hotelid + "'";
    prestate = connection.prepareStatement(query);
    result   = prestate.executeQuery();
    try
    {
        if( result != null )
        {
            if( result.next() != false )
            {
                hotel_name       =  result.getString("name");
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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>景品交換レポート</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
      </tr>
    </table>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0" id="print">
            <tr>
              <td width="140" height="20" bgcolor="#22333F" class="tab" align="center"><font color="#FFFFFF">景品交換レポート</font></td>
              <td width="140" height="20" bgcolor="#22333F" class="tab"><font color="#FFFFFF"><% if (name.compareTo("") != 0){%><%=name%>&nbsp;様<%}else if(nick_name.compareTo("") != 0){%><%=nick_name%>&nbsp;様<%}%></font></td>
              <td width="15" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20" valign="bottom"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" >
                 <INPUT name="submit_ret" type=button value=戻る onclick="location.href='goods_report.jsp?limit_flag=<%=limit_flag%>&detail_flag=<%=detail_flag%>'">
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <tr>
        <td bgcolor="#E2D8CF">
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
            </tr>
          </table>
        </td>
      </tr>
      <!-- ここから表 -->
      <tr>
        <td bgcolor="#E2D8CF">
          <form action="goods_report_change.jsp" method="POST">
          <input type="hidden" name="EntryId"     value="<%=entry_id%>">
          <input type="hidden" name="EntryBranch" value="<%=entry_branch%>">
          <input type="hidden" name="InputStatus" value="<%=input_status%>">
          <input type="hidden" name="custom_id"   value="<%=custom_id%>">
          <input type="hidden" name="page"        value="<%=pageno%>">
          <input type="hidden" name="limit_flag"  value="<%=limit_flag%>">
          <input type="hidden" name="detail_flag" value="<%=detail_flag%>">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0" id="print">
            <tr>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
              <td valign="middle" nowrap width="150" class="tableLN" style="padding-left:10px">入力日時</td>
              <td valign="middle" nowrap class="tableRW" style="padding-left:10px">
                <%=nf2.format(input_date / 10000 )%>/<%=nf2.format(input_date / 100 % 100 )%>/<%=nf2.format(input_date % 100 )%>
                <%=nf2.format(input_time / 10000 )%>:<%=nf2.format(input_time / 100 % 100 )%>:<%=nf2.format(input_time % 100 )%>
				(<%=hotel_name%>)
             </td>
            </tr>
            <tr>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
              <td valign="middle" nowrap width="150" class="tableLN" style="padding-left:10px">確認状況</td>
              <td valign="middle" nowrap class="tableRW" style="padding-left:10px">
<%
    if (status == 1)
    {
%>              未確認
<%
    }else if(status == 2)
    {
%>
                確認済
<%
    }else if(status == 3)
          {
              if (method == 1)
              {
%>
                準備完了
<%
               }else if (method == 2)
               {
%>
                発送済
<%
               }
          }
%>
<%
    if (status == 9)
    {
%>
               取消済
<%
    }
%>
                  <img src="../../common/pc/image/spacer.gif" width="10" >
                  <img src="../../common/pc/image/spacer.gif" width="10" >
                  <img src="../../common/pc/image/spacer.gif" width="10" >
                  <img src="../../common/pc/image/spacer.gif" width="10" >
                  <img src="../../common/pc/image/spacer.gif" width="10" >
                  <img src="../../common/pc/image/spacer.gif" width="10" >
                  ⇒
<%
    if (status == 1)
    {
%>
                  <select name="Status">
                    <option value="1">
                    <option value="2">確認済
                    <option value="3"><%if(method == 1){%>準備完了<%}else if (method == 2){%>発送済<%}%>
                    <option value="9">取消
                  </select>
<%
    }else if (status == 2)
    {
%>
                  <select name="Status">
                    <option value="2">
                    <option value="3"><%if(method == 1){%>準備完了<%}else if (method == 2){%>発送済<%}%>
                    <option value="1">未確認
                    <option value="9">取消
                  </select>
<%
    }else if (status == 3)
    {
%>
                  <select name="Status">
                    <option value="3">
                    <option value="2">準備前
                    <option value="1">未確認
                    <option value="9">取消
                  </select>
<%
    } else if (status == 9)
    {
%>
                  <select name="Status">
                    <option value="9">
                    <option value="1">未確認
                    <option value="2">確認済
                    <option value="3"><%if(method == 1){%>準備完了<%}else if (method == 2){%>発送済<%}%>
                  </select>
<%
    }
%>
                  に<input type="submit" value="変更">する
              </td></form>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
            </tr>
            <tr>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
              <td valign="middle" nowrap width="150" class="tableLN" style="padding-left:10px">メンバーID</td>
              <td valign="middle" nowrap class="tableRW" style="padding-left:10px"><%= custom_id %><%= user_id %>&nbsp;</td>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
            </tr>
<%
    if(NameFlag.compareTo("なし") != 0)
    {
%>
            <tr>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
              <td valign="middle" nowrap width="150" class="tableLN" style="padding-left:10px">お名前</td>
              <td valign="middle" nowrap class="tableRW" style="padding-left:10px"><%= name %>&nbsp;</td>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
            </tr>
<%
    }
%>
<%
    if(NickNameFlag.compareTo("なし") != 0)
    {
%>
            <tr>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
              <td valign="middle" nowrap width="150" class="tableLN" style="padding-left:10px">ニックネーム</td>
              <td valign="middle" nowrap class="tableRW" style="padding-left:10px"><%= nick_name %>&nbsp;</td>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
            </tr>
<%
    }
%>
<%
    if(MailaddressFlag.compareTo("なし") != 0)
    {
%>
            <tr>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
              <td valign="middle" nowrap width="150" class="tableLN" style="padding-left:10px">メールアドレス</td>
              <td valign="middle" nowrap class="tableRW" style="padding-left:10px"><%= mail_address %>&nbsp;</td>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
            </tr>
<%
    }
%>
            <tr>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
              <td valign="middle" nowrap width="150" class="tableLN" style="padding-left:10px">申込時ポイント数</td>
              <td valign="middle" nowrap class="tableRW" style="padding-left:10px"><%= point %>ポイント&nbsp;</td>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
            </tr>
            <tr>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
              <td valign="middle" nowrap width="150" class="tableLN" style="padding-left:10px">申込件数</td>
              <td valign="middle" nowrap class="tableRW" style="padding-left:10px"><%=count%>&nbsp;</td>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
            </tr>
            <tr>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
              <td valign="middle" nowrap width="150" class="tableLN" style="padding-left:10px">総使用ポイント</td>
              <td valign="middle" nowrap class="tableRW" style="padding-left:10px"><%=use_total%>ポイント&nbsp;</td>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
            </tr>
            <tr>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
              <td valign="middle" nowrap width="150" class="tableLN" style="padding-left:10px"><%=common_title%>明細</td>
              <td valign="middle" nowrap class="tableRW" style="padding-left:10px">

<jsp:include page="../../common/pc/goods_report_detail_disp.jsp" flush="true" />

              </td>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
            </tr>
            <tr>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
              <td valign="middle" nowrap width="150" class="tableLN" style="padding-left:10px"><%=common_title%>お渡し方法</td>
              <td valign="middle" nowrap class="tableRW" style="padding-left:10px"><% if (method == 1){%>ホテルフロント渡し(<%=hotel_name%>)<%}%><% if (method == 2){%>下記住所に配送<%}%>&nbsp;</td>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
            </tr>
<%
    if (method == 2)
    {
%>
            <tr>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
              <td valign="middle" nowrap width="150" class="tableLN" style="padding-left:10px">送付先氏名</td>
              <td valign="middle" nowrap class="tableRW" style="padding-left:10px"><%= send_name %>&nbsp;</td>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
            </tr>
            <tr>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
              <td valign="middle" nowrap width="150" class="tableLN" style="padding-left:10px">住所</td>
              <td valign="middle" nowrap class="tableRW" style="padding-left:10px">
				<%= zip_code %><br/>
				<%= pref_name %><br/>
				<%= address1 %><br>
				<%= address2 %>&nbsp;
			  </td>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
            </tr>
            <tr>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
              <td valign="middle" nowrap width="150" class="tableLN" style="padding-left:10px">連絡先電話番号</td>
              <td valign="middle" nowrap class="tableRW" style="padding-left:10px"><%= tel1 %>&nbsp;</td>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
            </tr>
<%
    }
%>
            <tr>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
              <td valign="middle" nowrap width="150" class="tableLN" style="padding-left:10px"><%=memo_title%></td>
              <td valign="middle" nowrap class="tableRW" style="padding-left:10px"><%= memo.replace("\r\n","<br/>　")%>&nbsp;</td>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
            </tr>
          </table>
        </td>
        <td width="3" valign="top" align="left" height="100%">
          <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><img src="../../common/pc/image/tab_kado.gif" width="3" height="3"></td>
            </tr>
            <tr>
              <td bgcolor="#666666" height="100%"><img src="../../common/pc/image/spacer.gif" width="3"></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td bgcolor="#E2D8CF">
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
            </tr>
          </table>
        </td>
        <td width="3" valign="top" align="left" height="100%">
          <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><img src="../../common/pc/image/tab_kado.gif" width="3" height="3"></td>
            </tr>
            <tr>
              <td bgcolor="#666666" height="100%"><img src="../../common/pc/image/spacer.gif" width="3" height="6"></td>
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
    <td align="center" valign="middle" class="size10"><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
