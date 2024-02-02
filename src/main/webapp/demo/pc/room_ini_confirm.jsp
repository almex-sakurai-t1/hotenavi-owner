<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.ReplaceString" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />


<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<%
    }
%>

<%
    // ホテルID取得
    String hotelid = request.getParameter("HotelId");

    if  (hotelid.compareTo("all") == 0)
    {
        hotelid = request.getParameter("HotelId");
    }
    NumberFormat  nf3;
    nf3    = new DecimalFormat("000");
    ReplaceString rs = new ReplaceString();
    CheckString   cs = new CheckString();

    java.util.Date start = new java.util.Date();
    java.util.Date end = new java.util.Date();

    String room_text      = rs.SQLEscape(request.getParameter("col_room_text"));
    String image_pc       = rs.SQLEscape(request.getParameter("col_image_pc"));
    String image_thumb    = rs.SQLEscape(request.getParameter("col_image_thumb"));
    String image_mobile   = rs.SQLEscape(request.getParameter("col_image_mobile"));

    // 日付ﾃﾞｰﾀの編集
    int start_ymd = 0;
    int end_ymd = 0;

    int start_yy = Integer.parseInt(request.getParameter("col_start_yy"));
    int start_mm = Integer.parseInt(request.getParameter("col_start_mm"));
    int start_dd = Integer.parseInt(request.getParameter("col_start_dd"));
    start_ymd = start_yy*10000 + start_mm*100 + start_dd;

    int end_yy = Integer.parseInt(request.getParameter("col_end_yy"));
    int end_mm = Integer.parseInt(request.getParameter("col_end_mm"));
    int end_dd = Integer.parseInt(request.getParameter("col_end_dd"));
    end_ymd = end_yy*10000 + end_mm*100 + end_dd;

    int      i        =  0;
    boolean  rankMode =  false;
    int      rank_i   =  0;
    String[] rankName = new String[100];
    boolean  roomMode =  false;
    int      room_i   =  0;
    String[] roomNo   = new String[100];
    int[]    roomRank = new int[100];
    String[] roomName = new String[100];
    for( i = 0 ; i < 100; i++ )
    {
        rankName[i]     = "";
        roomNo[i]       = "";
        roomRank[i]     = 0;
        roomName[i]     = "";
    }

    room_text       =  room_text + "\r\n";
    room_text       =  room_text.replace("\r\n\r\n","\r\n");
    room_text       =  room_text.replace("\r\n\r\n","\r\n");
    image_pc        =  image_pc.replace("\"","&quot;");
    image_thumb      =  image_thumb.replace("\"","&quot;");


    String   temp = "";
    StringBuffer buf     = new StringBuffer();
    StringBuffer buf_num = new StringBuffer();
    for( i = 0 ; i < room_text.length() ; i++ )
    {
        char c = room_text.charAt(i);
        switch( c )
        {
               case '◇':
                   buf     = new StringBuffer();
                   rankMode = true;
                   rank_i++;
                   break;
               case '\r':
                   if (rankMode)
                   {
                       temp = buf.toString();
                       temp = temp.replace("\r\n","");
                       temp = rs.replaceKanaFull(temp);
                       rankName[rank_i] = temp;
                       buf     = new StringBuffer();
                   }
                   rankMode = false;
                   if (roomMode)
                   {
                       temp = buf.toString();
                       temp = temp.replace("\r\n","");
                       temp = temp.replace(" ","");
                       temp = rs.replaceKanaFull(temp);
                       roomName[room_i] = temp;
                       if(cs.numCheck(temp))roomName[room_i] = roomName[room_i]+ "号室";
                       roomNo  [room_i] = buf_num.toString();
                       roomRank[room_i] = rank_i;
                       buf     = new StringBuffer();
                       buf_num = new StringBuffer();
                   }
                   roomMode = false;
                   buf.append(c);
                   break;
               case '\n':
                   buf.append(c);
                   break;
               case ' ':
                   if (roomMode)
                   {
                       temp = buf.toString();
                       temp = temp.replace("\r\n","");
                       temp = temp.replace(" ","");
                       temp = rs.replaceKanaFull(temp);
                       roomName[room_i] = temp;
                       if(cs.numCheck(temp))roomName[room_i] = roomName[room_i]+ "号室";
                       roomNo  [room_i] = buf_num.toString();
                       roomRank[room_i] = rank_i;
                       buf     = new StringBuffer();
                       buf_num = new StringBuffer();
                   }
                   roomMode = false;
                   temp = buf.toString();
                   temp = temp.replace("\r\n","");
                   buf.append(c);
                   break;
               default :
                   if(!roomMode)
                   {
                       if(!rankMode)
                       {
                           roomMode = true;
                           room_i++;
                       }
                   }
                   if(roomMode)
                   {
                       if (Character.toString(c).matches("[0-9]+"))
                       buf_num.append(c);
                   }
                   buf.append(c);
                   break;
            }
        }



%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>PC版イベント情報編集</title>
<link href="/common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="/common/pc/style/access.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="room2.js"></script>
<script type="text/javascript" src="tohankaku.js"></script>

</head>

<body bgcolor="#666666" background="/common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="180" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF"> 編　集</font></td>
              <td width="15" height="20"><img src="/common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="/common/pc/image/spacer.gif" width="200" height="20"></div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- ここから表 -->
      <tr>
        <td align="center" valign="top" bgcolor="#FFFFFF"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><img src="/common/pc/image/spacer.gif" width="400" height="12">
              </td>
            </tr>
            <tr>
              <td width="8"><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td><strong></strong></td>
                    <td align="right">&nbsp;</td>
                    <td width="20" align="right"><img src="/common/pc/image/spacer.gif" width="20" height="12"></td>
                  </tr>
                </table>
              </div>
              </td>
            </tr>
          </table>

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
                <td>
                  <table width="100%" border="0" cellspacing="0" cellpadding="2">

                  <form name=form1 action=room_ini_input.jsp method=post>
                  <tr align="left">
                    <td height="24" bgcolor="#666666"><strong><font color="#FFFFFF">&nbsp;表示期間</font></strong>
                      <input name="HotelId" type=hidden value="<%=hotelid%>">
                      <input name="col_start_yy" type="hidden"  value="<%= start_yy %>"><%= start_yy %>
                      年
                      <input name="col_start_mm" type="hidden"  value="<%= start_mm %>"><%= start_mm %>
                      月
                      <input name="col_start_dd" type="hidden"  value="<%= start_dd %>"><%= start_dd %>
                      日～
                      <input name="col_end_yy" type="hidden"  value="<%= end_yy %>"><%= end_yy %>
                      年
                      <input name="col_end_mm" type="hidden"  value="<%= end_mm %>"><%= end_mm %>
                      月
                      <input name="col_end_dd" type="hidden"  value="<%= end_dd %>"><%= end_dd %>
                      日
                    </td>
                  </tr>
                  <tr align="left">
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>
                  <tr align="left">
                    <td>
                  <table width="100%" border="0" cellspacing="0" cellpadding="2">
					<tr>
					<td valign="top">
						<!-- ############### サブタイトルバー ############### -->				
						<table cellspacing="0" bgcolor="#ffffff"  border="1" cellpadding="0" bordercolor="#666666" width="100%" >
							<tr valign="middle" >
								<td width="20"  style="text-align:center;padding:5px" class="size12">
									<div>
										No
									</div>
								</td>
								<td width="200" class="size12" style="padding:5px">
									<div>ランク名</div>
								</td>
							</tr>
<%
        for( i = 1 ; i <= rank_i; i++ )
        {
%>
							<tr valign="middle" >
								<td width="20"  style="text-align:center;padding:5px" class="size12">
									<div>
										<input type="hidden" name="rank_id_<%= i %>" value="<%= i %>"><%=i%>
									</div>
								</td>
								<td width="200"  class="size12" style="padding:5px">
									<div><input type=hidden name="rank_name_<%= i %>" value="<%=rankName[i] %>"><%=rankName[i]%></div>
								</td>
							</tr>
<%
        }
%>
						</table>

					</td>
					<td>
						<!-- ############### サブタイトルバー ############### -->				
						<table cellspacing="0" bgcolor="#ffffff"  border="1" cellpadding="0" bordercolor="#666666" width="100%">
							<tr valign="middle">
								<td width="20"  style="text-align:center" class="size12">
									<div>
										No
									</div>
								</td>
								<td width="80"  style="text-align:center"  class="size12">
									<div>部屋No</div>
								</td>
								<td width="80"  style="text-align:center" class="size12">
									<div>部屋名称</div>
								</td>
								<td width="50"  style="text-align:center" class="size12">
									<div>ランク</div>
								</td>
								<td width="50"  style="text-align:center" class="size12">
									<div>画像</div>
								</td>
							</tr>
<%
        for( i = 1 ; i <= room_i; i++ )
        {
%>
							<tr valign="middle">
								<td style="text-align:right" class="size12">
									<div>
										<input type="hidden" name="room_id_<%= i %>" value="<%= i %>"><%=i%>
									</div>
								</td>
								<td style="padding-left:5px" class="size12">
									<div><input type=text name="room_no_<%= i %>" value="<%=roomNo[i] %>" size="10" style="text-align:right"></div>
								</td>
								<td style="padding-left:5px" class="size12">
									<div><input type=text name="room_name_<%= i %>" value="<%=roomName[i] %>" size="10" style="text-align:right"></div>
								</td>
								<td style="text-align:center" class="size12">
									<div><input type=hidden name="room_rank_<%= i %>" value="<%=roomRank[i] %>"><%=roomRank[i]%></div>
								</td>
								<td  style="text-align:center" class="size12">
									<div><input type=checkbox name="room_image_<%= i %>" value="1" <%if (image_mobile.compareTo("") != 0){%>checked<%}%>></div>
								</td>
							</tr>
<%
        }
%>
								<input type="hidden" name=col_image_pc     value="<%=image_pc%>">
								<input type="hidden" name=col_image_thumb  value="<%=image_thumb%>">
								<input type="hidden" name=col_image_mobile value="<%=image_mobile%>">
								<input type="hidden" name=room_i value="<%=room_i%>">
								<input type="hidden" name=rank_i value="<%=rank_i%>">

						</table>
						</td>
						</tr>
						</table>
					</td>
				</tr>
                  <tr align="left">
                    <td>
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td width="50%" align="center" valign="middle" bgcolor="#969EAD"><input name="regsubmit" type=submit value="登録"></td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table>
              </form>
              </td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td align="center">
                  <input name="back" type=button value="戻る" onclick="history.back();">
                </td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
            </table>
        </td>
        <td width="3" valign="top" align="left" height="100%">
          <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><img src="new/image/tab_kado.gif" width="3" height="3"></td>
            </tr>
            <tr>
              <td bgcolor="#666666" height="100%"><img src="/common/pc/image/spacer.gif" width="3" height="100"></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td height="3" bgcolor="#999999">
          <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="3"><img src="/common/pc/image/tab_kado2.gif" width="3" height="3"></td>
              <td bgcolor="#666666"><img src="/common/pc/image/spacer.gif" width="100" height="3"></td>
            </tr>
          </table>
        </td>
        <td height="3" width="3"><img src="/common/pc/image/grey.gif" width="3" height="3"></td>
      </tr>
      <!-- ここまで -->
    </table></td>
  </tr>
  <tr>
    <td><img src="/common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/owner/Library/footer.lbi" --><img src="/common/pc/image/imedia.gif" width="63" height="18"><img src="/common/pc/image/spacer.gif" width="12" height="18" align="absmiddle">Copyrigtht&copy; imedia
    inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="/common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
