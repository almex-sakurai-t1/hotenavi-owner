<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    NumberFormat    nf;
    nf = new DecimalFormat("00");

    // ホスト種別取得
    String hotelid = ReplaceString.getParameter(request,"HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
    if( hotelid == null )
    {
        hotelid = "";
    }
    String TexExist = ReplaceString.getParameter(request,"TexExist");

    int        host_kind               = 0;
    String     query              = "";

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();
    try
    {
        query = "SELECT * FROM hotel WHERE hotel_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
               host_kind = result.getInt("host_kind");
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
    if (ownerinfo.sendPacket0100(1,hotelid))
    {
        if (ownerinfo.SystemKind.equals(ownerinfo.SYSTEM_KIND_NEO))
        {
            if (ownerinfo.SystemVer1 >= ownerinfo.SYSTEM_VER1_MIN  && ownerinfo.SystemVer2 >= ownerinfo.SYSTEM_VER2_NEO_TO_SIRIUS)
            {
                host_kind = 1; 
            }
        } 
    }
%>

      <table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#<%= ownerinfo.StatusDetailColor[0] %>">
        <tr>
          <td valign="top" class="roomWaku">
            <TABLE width="100%" height="96" border="0" cellpadding="2" cellspacing="0">
              <tr>
                <TD align="left" valign="top" nowrap width="150"><font color="#<%= ownerinfo.StatusDetailForeColor[0] %>"><div class="size14px"><%= ownerinfo.StatusDetailRoomName[0] %>　<%= ownerinfo.StatusDetailElapseTime[0] / 60 %>：<%= nf.format(ownerinfo.StatusDetailElapseTime[0] % 60) %></div>
                <div class="size14px"><%= ownerinfo.StatusDetailStatusName[0] %></div></font></TD>
              </tr>
              <tr>
                <TD height="26" align="right" valign="top"
<%
        // 精算機エラーのとき
        if (host_kind != 2 && TexExist.compareTo("true") == 0)
        {
          if (ownerinfo.TexErrorStat[0] == 2 || ownerinfo.TexLineStat[0] == 2)
          {
%>
             style="background-image:url(../../../common/pc/image/texerror.gif);background-repeat: no-repeat;background-position: right bottom;"
<%
          }
        }
%>
				>
<%
    // メンバーID
    if( ownerinfo.StateCustomId[0] != null && ownerinfo.StateCustomId[0].compareTo("") != 0 )
    {
        // メンバーイベントあり？
        if( ownerinfo.StateCustomEvent[0] != 0 )
        {
%>
                  <IMG SRC=../../common/pc/image/customevent.gif width=25 height=25>
<%
        }
        else
        {
%>
                  <IMG SRC=../../common/pc/image/card.gif width=25 height=25>
<%
        }
    }
    else
    {
%>
                  <IMG SRC=../../common/pc/image/blank.gif width=25 height=25>
<%
    }

    // 警告情報
    if( ownerinfo.StateCustomWarning[0] != 0 )
    {
%>
                  <IMG SRC=../../common/pc/image/keikoku.gif width=25 height=25>
<%
    }
    else
    {
%>
                  <IMG SRC=../../common/pc/image/blank.gif width=25 height=25>
<%
    }

    // 連絡情報
    if( ownerinfo.StateCustomContact[0] != 0 )
    {
%>
                  <IMG SRC=../../common/pc/image/contact.gif width=25 height=25>
<%
    }
    else
    {
%>
                  <IMG SRC=../../common/pc/image/blank.gif width=25 height=25>
<%
    }

    // 景品情報
    if( ownerinfo.StateCustomPresent[0] != 0 )
    {
%>
                  <IMG SRC=../../common/pc/image/present.gif width=25 height=25>
<%
    }
    else
    {
%>
                  <IMG SRC=../../common/pc/image/blank.gif width=25 height=25>
<%
    }

    // 冷蔵庫状態
    if( ownerinfo.StateRefUse[0] != 0 )
    {
%>
                  <IMG SRC=../../common/pc/image/ref.gif width=25 height=25>
<%
    }
    else
    {
%>
                  <IMG SRC=../../common/pc/image/blank.gif width=25 height=25>
<%
    }

    // コンビニ状態
    if( ownerinfo.StateConveniUse[0] != 0 )
    {
%>
                  <IMG SRC=../../common/pc/image/conveni.gif width=25 height=25>
<%
    }
    else
    {
%>
                  <IMG SRC=../../common/pc/image/blank.gif width=25 height=25>
<%
    }

    // ドア状態
    if( ownerinfo.StateDoor[0] != 0 )
    {
%>
                  <IMG SRC=../../common/pc/image/door.gif width=25 height=25>
<%
    }
    else
    {
%>
                  <IMG SRC=../../common/pc/image/blank.gif width=25 height=25>
<%
    }
        // 予定室料適用区分
    if( ownerinfo.StatusDetailUserChargeMode[0] != 0 )
    {
%>
                  <BR><DIV style='float:right'>
<%
        if( ownerinfo.StatusDetailUserChargeMode[0] == 1 )
        {
%>
                  <IMG SRC=../../common/pc/image/rest.gif width=50 height=20>
<%
        }
        else
        {
%>
                  <IMG SRC=../../common/pc/image/stay.gif width=50 height=20>
<%
        }
%>
                  </DIV>
<%
    }
%>
                </TD>
              </tr>


<%
        if (host_kind != 2 && TexExist.compareTo("true") == 0)
        {
          if (ownerinfo.TexLineStat[0] == 2)
          {
%>
              <tr>
                <TD height="26" align="right" valign="top">
                   <FONT size=4 class=size14px color="#<%= ownerinfo.StatusDetailForeColor[0] %>">精算機回線切断</FONT><BR>
                </TD>
              </tr>
<%
          }
          else
          if (ownerinfo.TexErrorStat[0] == 2)
          {
%>
              <tr>
                <TD height="26" align="right" valign="top">
                   <FONT size=4 class=size14px color="#<%= ownerinfo.StatusDetailForeColor[0] %>">精算機<strong>×</strong></FONT><FONT size=4 class=size12ex color="#<%= ownerinfo.StatusDetailForeColor[0] %>">(<%=ownerinfo.TexErrorCode[0]%>)</font><BR>
                </TD>
              </tr>
<%
          }
          else
          if (ownerinfo.TexErrorStat[0] == 3)
          {
%>
<!--
              <tr>
                <TD height="26" align="right" valign="top">
                     <FONT size=4 class=size12ex color="#<%= ownerinfo.StatusDetailForeColor[0] %>">精算機△(<%= ownerinfo.TexErrorCode[0]  %>)</font><BR>
                </TD>
              </tr>
-->
<%
         }
      }
%>
            </TABLE>
          </td>
        </tr>
      </table>

