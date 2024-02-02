<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.ReplaceString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/qaedit_ini.jsp" %>
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String hotelid = (String)session.getAttribute("SelectHotel");
    if( hotelid == null )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String     loginHotelId  = (String)session.getAttribute("LoginHotelId");

    boolean member_flag  = false;
    boolean running_flag = false;

    ReplaceString rs = new ReplaceString();
    String paramId   = ReplaceString.getParameter(request,"Id");

    int qid         = 0;
    int i           = 0;
    int id[]        = new int[30];
    int required[]  = new int[30];
    String msg_subtitle = "";
    String msg[] = new String[30];
    String query   = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection    = DBConnection.getConnection();

    //メッセージ表示用データ読み込み
    int    data_type = 63;
    String[]  msg_title  = new String[8];
    for(i = 0 ; i < 8 ; i++ )
    {
        msg_title[i]       = "";
    }

    if (paramId != null)
    {
        query = "SELECT * FROM question_master WHERE hotel_id=?";
        query = query + " AND (start <= ?";
        query = query + " AND end >= ?)";
        query = query + " AND id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid );
        prestate.setInt(2,nowdate);
        prestate.setInt(3,nowdate);
        prestate.setInt(4,Integer.parseInt(paramId));
        result          = prestate.executeQuery();
        if( result.next() != false )
        {
            if (result.getInt("member_flag") % 2 == 1)
            {
                member_flag = true;
            }
            if (result.getInt("member_flag") <= 1)
            {
                running_flag = true;
            }
            qid = result.getInt("id");
            msg_subtitle = result.getString("msg");
            for(i = 0 ; i < 30 ; i++ )
            {
                id[i]  = result.getInt("q" + (i+1));
                msg[i] = result.getString("q" + (i+1) + "_msg");
                required[i]  = id[i]/10;
            }
        }
        else
        {
                member_flag = false;
                id[0] = 0;
                qid = 0;
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
    query = "SELECT * FROM edit_event_info WHERE hotelid=?";
    query = query + " AND data_type=?";
    query = query + " AND (start_date <= ?";
    query = query + " AND end_date >= ?)";
    prestate    = connection.prepareStatement(query);
    prestate.setString(1,hotelid );
    prestate.setInt(2,data_type);
    prestate.setInt(3,nowdate);
    prestate.setInt(4,nowdate);
    result = prestate.executeQuery();
    if (result != null)
    {
        if( result.next() != false )
        {
            for( i = 0 ; i < 8 ; i++ )
            {
                if( result.getString("msg" + (i + 1) + "_title").length() != 0 || result.getString("msg" + (i + 1)).length() > 1)
                {
                    msg_title[i]             = result.getString("msg" + (i + 1));
                }
            }
        }
    }
    DBConnection.releaseResources(result,prestate,connection);

    String parameter = "";  //修正受け渡し用
    boolean parameter_flag = false;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Content-Style-Type" content="text/css">
<title>アンケート</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../<%= hotelid %>/pc/contents.css" rel="stylesheet" type="text/css">
<% if (loginHotelId.compareTo("demo") == 0){%>
<link href="http://www.hotenavi.com/<%= hotelid %>/contents.css" rel="stylesheet" type="text/css">
<%}%>
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">

	<tr valign="top">
		<td>


			<table width="100%" border="0" cellspacing="0" cellpadding="0">

				<tr>
					<td align="left" valign="middle" class="titlebar"><img src="../../common/pc/image/title_question.gif" width="140" height="14" alt="アンケート"></td>
				</tr>

				<tr>
					<td><img src="../../common/pc/image/spacer.gif" width="500" height="20"></td>
				</tr>
<%
        if( id[0] != 0 )
        {
            if (member_flag)
            {
                if (msg_title[1].length() > 1)
                {
%>
				<tr>
					<td class="honbun">
						<%= msg_title[1] %>
					</td>
				</tr>
				<tr>
					<td><img src="../../common/pc/image/spacer.gif" width="500" height="20"></td>
				</tr>
<%
                }
            }
            else
            {
                if (msg_title[0].length() > 1)
                {
%>
				<tr>
					<td class="honbun">
						<%= msg_title[0] %>
					</td>
				</tr>
				<tr>
					<td><img src="../../common/pc/image/spacer.gif" width="500" height="20"></td>
				</tr>
<%
                }
            }
        }
%>
				<tr>
					<td>

						<!-- ############### サブタイトルバー1 ############### -->
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="100%">
							<tr valign="middle">
								<td width="7" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="7" height="18"></td>
								<td width="3"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td width="3" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td>
									<div class="subtitle_text">
<%
            if (msg_subtitle.length() > 1)
            {
%>
									<%= msg_subtitle %>
<%
            }
            else
            {
%>
									お客様について教えてください。
<%
            }
%>
									</div>
								</td>
							</tr>
						</table>
						<!-- ############### サブタイトルバー1 ここまで ############### -->

					</td>
				</tr>

				<tr>
					<td><img src="../../common/pc/image/spacer.gif" width="500" height="8"></td>
				</tr>

				<tr>
					<td align="left" valign="top" class="honbun">
						<div class="honbun_margin">

						<!-- アンケートのテーブル -->
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
<%
        if( id[0] == 0 )
        {
%>
							<tr>
								<td class="honbun">
<%
            if (member_flag)
            {
                if (msg_title[1].length() > 1)
                {
%>
									<%= msg_title[1] %>
<%
                }
                else
                {
%>
									現在アンケートは行っておりません。
<%
                }
            }
            else
            {
                if (msg_title[0].length() > 1)
                {
%>
									<%= msg_title[0] %>
<%
                }
                else
                {
%>
									現在アンケートは行っておりません。
<%
                }
            }
%>
								</td>
							</tr>
<%
        }
        else
        {
            Connection        connection_sub  = null;
            PreparedStatement prestate_sub    = null;
            ResultSet         result_sub      = null;
            connection_sub    = DBConnection.getConnection();
            boolean data_exist= false;
%>
							<tr>
								<td class="hyouyou_bordercolor"><img src="../../common/pc/image/spacer.gif" width="500" height="1"></td>
							</tr>
							<form action="questionanswer.jsp" method="POST">
							<input TYPE="hidden" NAME="HotelId" VALUE="<%= hotelid %>">
							<input TYPE="hidden" NAME="id" VALUE="<%= qid %>">
<%
            for( i = 0; i < 30; i++ )
            {
                if( id[i] == 0 )
                {
                    break;
                }
                if(msg[i].length() > 1)
                {
%>
							<tr>
								<td class="hyouyou_bgcolor"><img src="../../common/pc/image/spacer.gif" width="500" height="4"></td>
							</tr>
							<tr>
								<td class="hyouyou_bgcolor honbun">　<%= msg[i] %>
<%
                    if(required[i] == 1)
                    {
%>
								　<strong><font size="1">(*必須)</font></strong>
<%
                    }
%>

								</td>
							</tr>
							<tr>
								<td><img src="../../common/pc/image/spacer.gif" width="500" height="4"></td>
							</tr>
<%
                }
                data_exist = false;
                switch( id[i]%10 )
                {
                    // チェックボックス
                    case 1:
                            parameter = ReplaceString.getParameter(request,"q" + i);
                            if (parameter == null)
                            {
                                parameter = "";
                            }
                            else
                            {
                                parameter_flag = true;
                            }
                            parameter = "," + parameter;
                            query = "SELECT * FROM question_data WHERE hotel_id=?";
                            query = query + " AND id=?";
                            query = query + " AND q_id=?";
                            prestate_sub    = connection_sub.prepareStatement(query);
                            prestate_sub.setString(1, hotelid);
                            prestate_sub.setInt(2, qid);
                            prestate_sub.setString(3,"q" + (i + 1));
                            result_sub      = prestate_sub.executeQuery();
                            if( result_sub != null )
                            {
                                while( result_sub.next() != false )
                                {
                                    data_exist = true;
%>

							<tr>
								<td><img src="../../common/pc/image/spacer.gif" width="500" height="4"></td>
							</tr>
							<tr>
								<td class="honbun">
									<input disabled type="checkbox" name="<%= result_sub.getString("q_id") %>" value="<%= result_sub.getInt("value") %>" <%if(parameter.indexOf("," +result_sub.getInt("value")+ ",") != -1){%>checked<%}%>><%= result_sub.getString("msg") %>
								</td>
							</tr>
<%
                                }
                            }
                            DBConnection.releaseResources(result_sub);
                            DBConnection.releaseResources(prestate_sub);
                            break;

                    // ラジオボタン
                    case 2:
                            parameter = ReplaceString.getParameter(request,"q" + i);
                            if (parameter == null)
                            {
                                parameter = "";
                            }
                            else
                            {
                                parameter_flag = true;
                            }
                            parameter = "," + parameter;
                            query = "SELECT * FROM question_data WHERE hotel_id=?";
                            query = query + " AND id=?";
                            query = query + " AND q_id=?";
                            prestate_sub    = connection_sub.prepareStatement(query);
                            prestate_sub.setString(1, hotelid);
                            prestate_sub.setInt(2, qid);
                            prestate_sub.setString(3,"q" + (i + 1));
                            result_sub      = prestate_sub.executeQuery();
                            if( result_sub != null )
                            {
                                while( result_sub.next() != false )
                                {
                                    data_exist = true;
%>

							<tr>
								<td><img src="../../common/pc/image/spacer.gif" width="500" height="4"></td>
							</tr>
							<tr>
								<td class="honbun">
									<input disabled type="radio" name="<%= result_sub.getString("q_id") %>" value="<%= result_sub.getInt("value") %>" <%if(parameter.indexOf("," +result_sub.getInt("value")+ ",") != -1){%>checked<%}%>><%= result_sub.getString("msg") %>
								</td>
							</tr>
<%
                                }
                            }
                            DBConnection.releaseResources(result_sub);
                            DBConnection.releaseResources(prestate_sub);
                            break;

                    // テキストボックス
                    case 3:
                            parameter = ReplaceString.getParameter(request,"q" + i);
                            if (parameter == null)
                            {
                                parameter = ",";
                            }
                            else
                            {
                                parameter_flag = true;
                            }
                            parameter =  parameter + ",,,,";
                            parameter =  parameter.replace(",,,,,","");
                            query = "SELECT * FROM question_data WHERE hotel_id=?";
                            query = query + " AND id=?";
                            query = query + " AND q_id=?";
                            prestate_sub    = connection_sub.prepareStatement(query);
                            prestate_sub.setString(1, hotelid);
                            prestate_sub.setInt(2, qid);
                            prestate_sub.setString(3,"q" + (i + 1));
                            result_sub      = prestate_sub.executeQuery();
                            if( result_sub != null )
                            {
                                if( result_sub.next() != false )
                                {
                                    data_exist = true;
%>
							<tr>
								<td><img src="../../common/pc/image/spacer.gif" width="500" height="4"></td>
							</tr>
							<tr>
								<td class="honbun">
<%
                                    if (result_sub.getString("msg")!= null)
                                    {
                                        if (result_sub.getString("msg").length() > 1)
                                        {
%>
									　<%= result_sub.getString("msg") %><br>
<%
                                        }
                                     }
%>
									<textarea name="<%= result_sub.getString("q_id") %>" cols="50" rows="5" disabled><%=parameter%></textarea>
								</td>
							</tr>
<%
                                }
                            }
                            DBConnection.releaseResources(result_sub);
                            DBConnection.releaseResources(prestate_sub);
                            break;

                    // コンボボックス
                    case 4:
                            parameter = ReplaceString.getParameter(request,"q" + i);
                            if (parameter == null)
                            {
                                parameter = "";
                            }
                            else
                            {
                                parameter_flag = true;
                            }
                            parameter = "," + parameter;
                            query = "SELECT * FROM question_data WHERE hotel_id=?";
                            query = query + " AND id=?";
                            query = query + " AND q_id=?";
                            prestate_sub    = connection_sub.prepareStatement(query);
                            prestate_sub.setString(1, hotelid);
                            prestate_sub.setInt(2, qid);
                            prestate_sub.setString(3,"q" + (i + 1));
                            result_sub      = prestate_sub.executeQuery();
                            if( result_sub != null )
                            {
%>

							<tr>
								<td><img src="../../common/pc/image/spacer.gif" width="500" height="4"></td>
							</tr>
							<tr>
								<td class="honbun">
									<select name="q<%= ( i + 1 ) %>">

<%
                                 while( result_sub.next() != false )
                                 {
                                     data_exist = true;
%>
										<option value="<%= result_sub.getInt("value") %>" <%if(parameter.indexOf(","+result_sub.getInt("value")+",") != -1){%>selected<%}%>><%= result_sub.getString("msg") %></option>

<%
                                 }
%>

									</select>
								</td>
							</tr>
<%
                            }
                            DBConnection.releaseResources(result_sub);
                            DBConnection.releaseResources(prestate_sub);
                            break;
                }
                if (data_exist)
                {
%>
							<tr>
								<td><img src="../../common/pc/image/spacer.gif" width="500" height="4"></td>
							</tr>
							<tr>
								<td height="1" background="image/dot_yoko.gif"><img src="../../common/pc/image/spacer.gif" width="500" height="1"></td>
							</tr>
<%
                }
            }  // End of for( i = 0; i < 30; i++ )
            DBConnection.releaseResources(connection_sub);
%>
							<tr>
								<td><img src="../../common/pc/image/spacer.gif" width="500" height="16"></td>
							</tr>
							<tr>
								<td align="right">
								</td>
							</tr>
							<tr>
								<td><img src="../../common/pc/image/spacer.gif" width="500" height="16"></td>
							</tr>

							<tr valign="middle">
								<td align="center" valign="middle" bgcolor="#cccccc">
<%
            if (running_flag)
            {
%>
								<input  type="button" value="掲載を停止する">
<%
            }
            else
            {
%>
								<input  type="button" value="掲載を開始する">
<%
            }
%>
								<img src="../../common/pc/image/spacer.gif" width="30" height="24">
								</td>
							</tr>

							</form>

<%
        }  // if( id[0] == 0 )
%>

						</table>
						<!-- アンケートのテーブル ここまで -->


						</div>
					</td>
				</tr>

				<tr>
					<td><img src="../../common/pc/image/spacer.gif" width="500" height="50"></td>
				</tr>
			</table>
		</td>
	</tr>

	<tr valign="bottom">
		<td>
			<!-- copyright テーブル -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="center" valign="middle">
						<div class="copyright">
							Copy right &copy; almex inc, All Rights Reserved
						</div>
					</td>
				</tr>
				<tr>
					<td><img src="../../common/pc/image/spacer.gif" width="500" height="20"></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</body>
</html>
