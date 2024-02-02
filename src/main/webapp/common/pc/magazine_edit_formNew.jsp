<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    int    i;
    String query;
    String hotelid = (String)session.getAttribute("SelectHotel");
    if( hotelid.compareTo("all") == 0 )
    {
        hotelid = (String)session.getAttribute("HotelId");
    }

    String historyid = ReplaceString.getParameter(request,"history"); 
    if (historyid != null)
    {
     if(!CheckString.numCheck(historyid))
     {
        historyid = "";
%>
        <script type="text/javascript">
        <!--
        var dd = new Date();
        setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
        //-->
        </SCRIPT>
<%
     }
    }
    String subject = "";
    String body = "";
    int    send_date;
    int    send_time;
    int    year;
    int    month;
    int    day;
    int    hour;
    int    minute;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();

    DateEdit dateedit = new DateEdit();
    Calendar cal = Calendar.getInstance();

    year   = cal.get(cal.YEAR);
    month  = cal.get(cal.MONTH) + 1;
    day    = cal.get(cal.DATE);
    hour   = cal.get(cal.HOUR_OF_DAY);
    minute = cal.get(cal.MINUTE);

    send_date = Integer.parseInt(dateedit.getDate(2));
    send_time = Integer.parseInt(dateedit.getTime(3));

    if( historyid != null )
    {
        query = "SELECT * FROM mag_data WHERE hotel_id=?";
        query = query + " AND history_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid );
        prestate.setInt(2,Integer.parseInt(historyid));
        result      = prestate.executeQuery();
        if( result != null )
        {
            if( result.next() != false )
            {
                subject = result.getString("subject");
                body    = result.getString("body");
                send_date = result.getInt("send_date");
                send_time = result.getInt("send_time");
            }
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
%>
        <input type="hidden" name="historyid" value="<%= historyid %>">
<%
    }

    int    data_type      = 62;
    String hotel_name    = "";
    String hotel_address = "";
    String hotel_tel     = "";
    String sign          = "";
    String unsubscribe_url = "";
    query = "SELECT * FROM edit_event_info WHERE hotelid=?";
    query = query + " AND data_type=?";
    prestate    = connection.prepareStatement(query);
    prestate.setString(1,hotelid );
    prestate.setInt(2,data_type);
    result      = prestate.executeQuery();
    if (result != null)
    {
        if( result.next() != false )
        {
            sign    = result.getString("msg1");
        }
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);

    query = "SELECT unsubscribe_url FROM mag_hotel WHERE hotel_id=?";
    prestate    = connection.prepareStatement(query);
    prestate.setString(1,hotelid );
    result      = prestate.executeQuery();
    if( result.next() != false )
    {
        unsubscribe_url = result.getString("unsubscribe_url");
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);

    query = "SELECT * FROM hh_hotel_basic WHERE hotenavi_id=?";
    prestate    = connection.prepareStatement(query);
    prestate.setString(1,hotelid );
    result      = prestate.executeQuery();
    i = 0;
    if( result != null )
    {
        while( result.next() != false )
        {
            i++;
            hotel_name    = result.getString("hh_hotel_basic.name");
            hotel_address = result.getString("hh_hotel_basic.address_all");
            hotel_tel     = result.getString("hh_hotel_basic.tel1");
            if(sign.compareTo("") == 0)
            {
                if ( i == 1)
                {
                    sign = sign + "====================" + "\r\n";
                }
                sign = sign + hotel_name    + "\r\n";
                sign = sign + hotel_address + "\r\n";
                sign = sign + hotel_tel     + "\r\n";
            }
        }
    }
    DBConnection.releaseResources(result,prestate,connection);
    if (body.compareTo("")== 0)
    {
        body = body + "\r\n";
        body = body + sign;
    }
%>



<script Language="JavaScript">
<!--
    //配信予定日時を変更した場合に、強制的に「予定時間に配信」にチェックを変更 20060913S
function setDeliver(obj) {
    obj = obj.form;
    obj.deliver[0].checked = true;
}

function bodyCheck() {
    var bodyArea = document.getElementById("body").value;
    if (bodyArea.indexOf("[unsubscribe]") == -1){
        alert("メール本文にメールマガジン解除URL（[unsubscribe]）は必須です");
        document.getElementById("body").value += "\r\nメールマガジン解除はこちら\r\n[unsubscribe]";
    }
}
function setTime() {
    dt = new Date(new Date().setMinutes(new Date().getMinutes() + 10)); //10分後
    yy = dt.getFullYear();
    mm = dt.getMonth() + 1;
    dd = dt.getDate();
    hour = dt.getHours();
    min = dt.getMinutes();
    document.getElementById("year").value = yy;
    document.getElementById("month").value = mm;
    document.getElementById("day").value  = dd;
    document.getElementById("hour").value  = hour;
    document.getElementById("min").value  = Math.floor(min/5)*5;
}
//配信予定時刻をチェック 20100430
function checkTime(obj) {
    obj = obj.form;
    if (obj.subject.value=='')
    {
        alert("件名が入力されていません");
        return false;
    }

    var nowDt = new Date(new Date().setMinutes(new Date().getMinutes() + 5)); //5分後
    var afterOneMonthDt = new Date(new Date().setDate(new Date().getDate() + 30)); //30日後
    var inputDt = new Date(document.getElementById("year").value,parseInt(document.getElementById("month").value)-1,document.getElementById("day").value,document.getElementById("hour").value,document.getElementById("min").value); 
    console.log(inputDt);
    if (nowDt > inputDt)
    {
       if(!confirm("現在時刻より前の時刻は指定できません。すぐに送信しますか？"))
       {
          return false;
       }
        document.getElementById("year").value = nowDt.getFullYear();
        document.getElementById("month").value = nowDt.getMonth()+1;
        document.getElementById("day").value = nowDt.getDate();
        document.getElementById("hour").value = nowDt.getHours();
        document.getElementById("min").value = Math.floor(nowDt.getMinutes()/5)*5;
    }
    if (inputDt > afterOneMonthDt)
    {
       alert("30日先を超えて指定できません");
       return false;
    } 
    var hour = obj.hour.selectedIndex;
    var min  = obj.min.selectedIndex;
    min = min*5;
    if (hour > 21 || hour < 9)
    {
       if(!confirm("この時刻（"+hour+"時"+min+"分）に送信しても大丈夫ですか？"))
       {
          return false;
       }
       if (hour < 8)
       {
          document.getElementById("hour").value = 8;
          document.getElementById("min").value = 0;
       }
       if (hour >= 23)
       {
          var afterOneDayDt = new Date(new Date().setDate(inputDt.getDate() + 1));
          document.getElementById("year").value = afterOneDayDt.getFullYear();
          document.getElementById("month").value = afterOneDayDt.getMonth()+1;
          document.getElementById("day").value = afterOneDayDt.getDate();
          document.getElementById("hour").value = 8;
          document.getElementById("min").value = 0;
       }
    }
    return true;
}
-->
</script>


		  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td align="left" class="size12">件名：
                        <input name="subject" type="text" id="subject" size="100" maxlength="100" value="<%= subject %>"></td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                      </tr>
                      <tr valign="top">
                        <td width="8" align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left"><div class="size12">
                          <table border="0" cellspacing="0" cellpadding="0">
                            <tr>
                              <td width=40><br>
                                  <div class="size12"><font color="#000000">本文：</font></div>
                              </td>
                              <td align="left">
                                <textarea name="body" cols="70" rows="30" wrap="VIRTUAL" id="body" style="font-family: 'ＭＳ ゴシック', 'Osaka－等幅';" onblur="bodyCheck();" onChange="bodyCheck();"><%= body %></textarea>
                                <img src="../../common/pc/image/spacer.gif" width="6" height="12">
                              </td>
                              <td valign="top" class="size12">
                                <font color="#CC0000">メールマガジン解除用URL（[unsubscribe]）は必須です。<br>
                                [unsubscribe]:<%=unsubscribe_url%></font>
                                <input type="hidden" name="unsubscribe_url" value="<%=unsubscribe_url%>">
                                </td>
                              </tr>
                          </table>
                        </div></td>
                      </tr>
                      <tr>
                        <td colspan="2" align="left"><img src="../../common/pc/image/spacer.gif" width="200" height="14"></td>
                        </tr>
                      <tr>
                        <td width="8" align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">配信予定時間：
                          <select name="year" id="year" onchange="setDeliver(this);">
<%
    for( i = 0 ; i < 2 ; i++ )
    {
        if( year + i == (send_date / 10000) )
        {
%>
                            <option value="<%= year + i %>" selected><%= year + i %></option>
<%
        }
        else
        {
%>
                            <option value="<%= year + i %>"><%= year + i %></option>
<%
        }
    }
%>

                          </select>
                          年
                          <select name="month" id="month" onchange="setDeliver(this);">
<%
    for( i = 0 ; i < 12 ; i++ )
    {
        if( (i + 1) == (send_date / 100 % 100) )
        {
%>
            <OPTION VALUE="<%= i + 1 %>" selected><%= i + 1 %></OPTION>
<%
        }
        else
        {
%>
            <OPTION VALUE="<%= i + 1 %>"><%= i + 1 %></OPTION>
<%
        }
    }
%>
                          </select>
                          月
                          <select name="day" id="day"  onchange="setDeliver(this);">
<%
    for( i = 0 ; i < 31 ; i++ )
    {
        if( (i + 1) == (send_date % 100) )
        {
%>
            <OPTION VALUE="<%= i + 1 %>" selected><%= i + 1 %></OPTION>
<%
        }
        else
        {
%>
            <OPTION VALUE="<%= i + 1 %>"><%= i + 1 %></OPTION>
<%
        }
    }
%>
                          </select>
                        日
                        <select name="hour" id="hour"  onchange="setDeliver(this);">

<%
    for( i = 0 ; i < 24 ; i++ )
    {
        if( i == (send_time / 100) )
        {
%>
            <OPTION VALUE="<%= i %>" selected><%= i %></OPTION>
<%
        }
        else
        {
%>
            <OPTION VALUE="<%= i %>"><%= i %></OPTION>
<%
        }
    }
%>

                        </select>
                        時
                        <select name="min" id="min"  onchange="setDeliver(this);">
<%
    for( i = 0 ; i < 12 ; i++ )
    {
        if( i  == (send_time % 100)/5 )
        {
%>
            <OPTION VALUE="<%= i * 5 %>" selected><%= i * 5 %></OPTION>
<%
 
        }
        else
        {
%>
            <OPTION VALUE="<%= i * 5 %>"><%= i * 5 %></OPTION>
<%
        }
    }
%>
                        </select>
                        分
                        </td>
                      </tr>
                      <tr>
                        <td align="left">&nbsp;</td>
                        <td align="left" class="size12" style="color:red">&nbsp;※注意：予約時間に配信を選択した場合、23:00～7:59まではメルマガ配信ができません。該当時間に設定した際は、朝8時に配信されます。</td>
                      </tr>
                      <tr>
                        <td align="left">&nbsp;</td>
                        <td align="left" class="size12">
                        <p>
<%
    if( historyid != null )
    {
%>
                          <label>
                          <input name="deliver" type="radio" value="1" checked>
                          予約時間に配信　</label><label>
                          <input name="deliver" type="radio" value="2" onclick="setTime();">
                          今すぐ配信</label>
                          <br>
<%
    }
    else
    {
%>
                          <label>
                          <input name="deliver" type="radio" value="1">
                          予約時間に配信　</label><label>
                          <input name="deliver" type="radio" value="2" checked onclick="setTime();">
                          今すぐ配信</label>
                          <br>
<%
    }
%>
                        </p>
                        </td>
                      </tr>
                      <tr>
                        <td colspan="2" align="left"><img src="../../common/pc/image/spacer.gif" width="200" height="14"></td>
                        </tr>
                      <tr valign="middle">
                        <td colspan="2" align="center"><input name="Submit3" type="submit" value="確　定" onclick="return checkTime(this);">
                        <img src="../../common/pc/image/spacer.gif" width="12" height="12">
                        <input type="reset" name="Submit2" value="クリア"></td>
                      </tr>
                  </table>

<script Language="JavaScript">
window.onload=setTime();
</script>