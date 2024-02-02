<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int             i;
    int             j;
    int             k;
    int             x;
    int             y;
    int             z;
    int             z_max = 0;
    int             count;
    String          hotelid;
    String          hotelname;
    String          roomtag[][][];
    String          now_date;
    String          now_time;
    DateEdit        df;
    NumberFormat    nf;
    boolean         coordinate;
    String          TexExist = ReplaceString.getParameter(request,"TexExist");

    count     = ownerinfo.StatusDetailCount;
    roomtag   = new String[12][12][12];
    df        = new DateEdit();
    now_date  = df.getDate(1);
    now_time  = df.getTime(2);
    nf        = new DecimalFormat("00");

    hotelid = ReplaceString.getParameter(request,"NowHotel");
    if( hotelid == null )
    {
        hotelid = "";
    }
    hotelname = ReplaceString.getParameter(request,"NowHotelName");
    if( hotelname == null )
    {
        hotelname = "";
    }

   // ホスト種別取得
    int type = 0;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();
    try
    {
        String query = "SELECT * FROM hotel WHERE hotel_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
               type = result.getInt("host_kind");
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
	
    // 部屋枠タグの初期化
    for( i = 0 ; i < 12 ; i++ )
    {
        for( j = 0 ; j < 12 ; j++ )
        {
            for( k = 0 ; k < 12 ; k++ )
            {
                roomtag[i][j][k] = "&nbsp;";
            }
        }
    }

    // 座標チェック
    coordinate = false;
    for( i = 0 ; i < count ; i++ )
    {
        if( ownerinfo.StatusDetailX[i] != 0 ||
            ownerinfo.StatusDetailY[i] != 0 ||
            ownerinfo.StatusDetailZ[i] != 0 )
        {
            coordinate = true;
        }
    }

    x = 0;
    y = 0;
    z = 0;

    for( i = 0 ; i < count ; i++ )
    {
        if( coordinate != false )
        {
            x = ownerinfo.StatusDetailX[i];
            y = ownerinfo.StatusDetailY[i];
            z = ownerinfo.StatusDetailZ[i];
            if( z > z_max )
            {
                z_max = z;
            }
        }
        else
        {
            if( i != 0 )
            {
                x = x + 1;
                if( x >= 5 )
                {
                    x = 0;
                    y = y + 1;
                }
            }
        }

        // 部屋枠の色指定
        roomtag[x][y][z] = "<TD nowrap bgcolor=\"#" + ownerinfo.StatusDetailColor[i] + "\"";
        // 精算機エラーのとき
        if (type != 2 && TexExist.compareTo("true") == 0)
        {
            if (ownerinfo.TexErrorStat[i] == 2 || ownerinfo.TexLineStat[i] == 2)
            {
                roomtag[x][y][z] = roomtag[x][y][z] + " style=\"background-image:url(../../../common/pc/image/texerror.gif);background-repeat: no-repeat;background-position: right bottom;\"";
            }
        }
        // リンク先の設定
        roomtag[x][y][z] = roomtag[x][y][z] + "  valign=top height=100 onClick=\"MM_goToURL('self', 'roomdetail.jsp?HotelId=" + hotelid + "&RoomCode=" + ownerinfo.StatusDetailRoomCode[i] + "');return document.MM_returnValue\" >";
        // 文字色設定
        roomtag[x][y][z] = roomtag[x][y][z] + "<FONT size=4 class=size14px color=\"#" + ownerinfo.StatusDetailForeColor[i] + "\">";
        // 部屋名称
        roomtag[x][y][z] = roomtag[x][y][z] + "<B>" + ownerinfo.StatusDetailRoomName[i] + "&nbsp;&nbsp;&nbsp;";
        // 経過時間
        roomtag[x][y][z] = roomtag[x][y][z] + ownerinfo.StatusDetailElapseTime[i] / 60 + ":" + nf.format(ownerinfo.StatusDetailElapseTime[i] % 60) + "<BR>";
        // 部屋ステータス名
        roomtag[x][y][z] = roomtag[x][y][z] + ownerinfo.StatusDetailStatusName[i] + "</B><BR><BR>";

        // 照明１
        if( ownerinfo.EquipActMode[i][1] == 1 && type != 2  )
        {
            roomtag[x][y][z] = roomtag[x][y][z] + "<IMG SRC=../../common/pc/image/scene.gif width=14 height=14> ";
        }
        else
        {
            roomtag[x][y][z] = roomtag[x][y][z] + "<IMG SRC=../../common/pc/image/blank.gif width=14 height=14> ";
        }

        // 空調
        if( ownerinfo.EquipActMode[i][9] == 1 && type != 2  )
        {
            roomtag[x][y][z] = roomtag[x][y][z] + "<IMG SRC=../../common/pc/image/air.gif width=14 height=14> ";
        }
        else
        {
            roomtag[x][y][z] = roomtag[x][y][z] + "<IMG SRC=../../common/pc/image/blank.gif width=14 height=14> ";
        }

        // 部屋換気扇
        if( ownerinfo.EquipActMode[i][5] == 0 && type != 2  )
        {
            roomtag[x][y][z] = roomtag[x][y][z] + "<IMG SRC=../../common/pc/image/roomfan.gif width=14 height=14> ";
        }
        else
        {
            roomtag[x][y][z] = roomtag[x][y][z] + "<IMG SRC=../../common/pc/image/blank.gif width=14 height=14> ";
        }

        // 風呂換気扇
        if( ownerinfo.EquipActMode[i][6] == 0 && type != 2 )
        {
            roomtag[x][y][z] = roomtag[x][y][z] + "<IMG SRC=../../common/pc/image/bathfan.gif width=14 height=14> ";
        }
        else
        {
            roomtag[x][y][z] = roomtag[x][y][z] + "<IMG SRC=../../common/pc/image/blank.gif width=14 height=14> ";
        }

        // 部屋窓
        if( ownerinfo.EquipActMode[i][4] == 0 && type != 2  )
        {
            roomtag[x][y][z] = roomtag[x][y][z] + "<IMG SRC=../../common/pc/image/roomwindow.gif width=14 height=14> ";
        }
        else
        {
            roomtag[x][y][z] = roomtag[x][y][z] + "<IMG SRC=../../common/pc/image/blank.gif width=14 height=14> ";
        }

        roomtag[x][y][z] = roomtag[x][y][z] + "<BR>";

        // アラーム
        if( ownerinfo.EquipActMode[i][15] == 0 && type != 2 )
        {
            roomtag[x][y][z] = roomtag[x][y][z] + "<IMG SRC=../../common/pc/image/alarm.gif width=14 height=14> " + ownerinfo.EquipStatusData[i][15];
        }
        else
        {
            roomtag[x][y][z] = roomtag[x][y][z] + "<IMG SRC=../../common/pc/image/blank.gif width=14 height=14> " + "&nbsp;&nbsp;&nbsp;&nbsp;";
        }

        // 室温
        if( ownerinfo.EquipActMode[i][16] == 0  && type != 2 )
        {
            roomtag[x][y][z] = roomtag[x][y][z] + "<IMG SRC=../../common/pc/image/roomtemp.gif width=14 height=14> " + ownerinfo.EquipStatusData[i][16];
        }
        else
        {
            roomtag[x][y][z] = roomtag[x][y][z] + "<IMG SRC=../../common/pc/image/blank.gif width=14 height=14> " + "&nbsp;&nbsp;&nbsp;&nbsp;";
        }

        roomtag[x][y][z] = roomtag[x][y][z] + "<BR>";

        // ドア
        if( ownerinfo.EquipActMode[i][0] == 1 && type != 2 )
        {
            roomtag[x][y][z] = roomtag[x][y][z] + "<IMG SRC=../../common/pc/image/door.gif width=14 height=14> ";
        }
        else
        {
            roomtag[x][y][z] = roomtag[x][y][z] + "<IMG SRC=../../common/pc/image/blank.gif width=14 height=14> ";
        }
        // 予定室料適用区分
        if( ownerinfo.StatusDetailUserChargeMode[i] != 0 )
        {
            roomtag[x][y][z] = roomtag[x][y][z] + "<DIV style='float:right;clear:both'>";
            if( ownerinfo.StatusDetailUserChargeMode[i] == 1 )
            {
                roomtag[x][y][z] = roomtag[x][y][z] + "<IMG SRC=../../common/pc/image/rest.gif width=40 height=16> ";
            }
            else
            {
                roomtag[x][y][z] = roomtag[x][y][z] + "<IMG SRC=../../common/pc/image/stay.gif width=40 height=16> ";
            }
            roomtag[x][y][z] = roomtag[x][y][z] + "</DIV>";
        }

//        roomtag[x][y][z] = roomtag[x][y][z] + "<BR>";
//        if (ownerinfo.TexErrorStat[i] == 2)
//        {
//            roomtag[x][y][z] = roomtag[x][y][z] + "</FONT><FONT size=4 class=size14px color=\"#" + ownerinfo.StatusDetailForeColor[i] + "\"><strong>×</strong></FONT><FONT size=4 class=size12ex color=\"#" + ownerinfo.StatusDetailForeColor[i] + "\">" + "(" + ownerinfo.TexErrorCode[i]  + ")" + "<BR>";
//        }

        roomtag[x][y][z] = roomtag[x][y][z] + "</FONT></TD>";
    }
%>

<!-- 店舗表示 --> 
<a name="<%= hotelid %>"></a> 
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td height="20">
      <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
        <tr> 
          <td width="200" height="20" bgcolor="#22333F" class="tab"><font color="#FFFFFF"><%= hotelname %>&nbsp;利用状況</font></td>
          <td width="15" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
          <td valign="bottom">
            <div class="navy10px"><img src="../../common/pc/image/spacer.gif" width="12" height="16" align="absmiddle"><a href="#pagetop" class="navy10px">&gt;&gt;このページのトップへ</a></div>
          </td>
        </tr>
      </table>
    </td>
    <td width="3">&nbsp;</td>
  </tr>

<!-- ここから表 -->
  <tr>
    <td align="center" valign="top" bgcolor="#D0CED5">
      <table width="98%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="6" align="center"><img src="../../common/pc/image/spacer.gif" width="300" height="6"></td>
        </tr>
        <tr>
          <td align="center">
            <table width="99%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="100" class="bar">モード：<%= ownerinfo.ModeName %></td>
                <td class="bar">従業員：<%= ownerinfo.EmployeeName %>　</td>
                <td width="170" align="right" class="bar" nowrap><div class="size12">最終更新：<%= now_date %>&nbsp;<%= now_time %></div></td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td><img src="../../common/pc/image/spacer.gif" width="160" height="14"></td>
        </tr>
        <tr>
          <td><div class="size12" style="float:left">※お部屋を選ぶと客室詳細がご覧になれます。</div>
            <jsp:include page="roomdisp_summary.jsp" flush="true" >
              <jsp:param name="NowHotel"     value="<%= hotelid %>" />
            </jsp:include>
          </td>
        </tr>
        <tr>
          <td align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
        </tr>
        <tr>
          <td align="left">
            <table width="99%" border="0" cellspacing="3" cellpadding="0">
<%
    for( k = 0 ; k < (z_max+1) ; k++ )
    {
        for( i = 0 ; i < 12 ; i++ )
        {
%>
              <tr>
<%
            for( j = 0 ; j < 12 ; j++ )
            {
                if( roomtag[j][i][k].compareTo("&nbsp;") == 0 )
                {
%>
                <td>
                  <%= roomtag[j][i][k] %>
                </td>
<%
                }
                else
                {
%>
                <td align="left" valign="top" class="roomWaku">
                  <table width="100%" height="112" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                      <%= roomtag[j][i][k] %>
                    </tr>
                  </table>
                </td>
<%
                }
            }
%>
              </tr>
<%
        }
    }
%>

            </table>
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
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
  </tr>
</table>
<!-- ここまで -->

