<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int             i;
    int             j;
    int             x;
    int             y;
    int             count;
    String          hotelid;
    String          hotelname;
    String          roomtag[];
    String          now_date;
    String          now_time;
    DateEdit        df;
    NumberFormat    nf;
    String          amount;
    int amount_flag   = 3;
    int total_10000   = 0;
    int total_5000    = 0;
    int total_2000    = 0;
    int total_1000    = 0;
    int total_500     = 0;
    int total_100     = 0;
    int total_50      = 0;
    int total_10      = 0;
    int total_sub1    = 0;
    int total_sub2    = 0;
    int total_sales   = 0;
    int total_safe    = 0;
    int total_supply  = 0;
    int total_surplus = 0;
    boolean TexExist      = false;

    count     = ownerinfo.TexRoomCount;
    roomtag   = new String[ownerinfo.OWNERINFO_ROOMMAX];
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
    // ���z�\�����e�̎擾
    amount    = ReplaceString.getParameter(request,"amount");
    if( amount == null )
    {
        amount = (String)session.getAttribute("Amount");
        if( amount == null )
        {
            amount = "3";
        }
    }
    else
    {
        if(!CheckString.numCheck(amount))
        {
            amount ="3";  
        }
        session.setAttribute("Amount", amount);
    }
    amount_flag = Integer.parseInt(amount);
    // �����g�^�O�̏�����
    for( i = 0 ; i < ownerinfo.OWNERINFO_ROOMMAX ; i++ )
    {
        roomtag[i] = "&nbsp;";
    }

    String tableClass = "";
    for( i = 0 ; i < count ; i++ )
    {
        if (ownerinfo.TexChargeStat[i] !=0 || ownerinfo.TexSupplyDate[i] != 0)
        {
            TexExist = true;
        }
        total_10000   = total_10000   + ownerinfo.TexSafeCount[i][amount_flag][0];
        total_5000    = total_5000    + ownerinfo.TexSafeCount[i][amount_flag][1];
        total_2000    = total_2000    + ownerinfo.TexSafeCount[i][amount_flag][10];
        total_1000    = total_1000    + ownerinfo.TexSafeCount[i][amount_flag][2];
        total_500     = total_500     + ownerinfo.TexSafeCount[i][amount_flag][3];
        total_100     = total_100     + ownerinfo.TexSafeCount[i][amount_flag][4];
        total_50      = total_50      + ownerinfo.TexSafeCount[i][amount_flag][5];
        total_10      = total_10      + ownerinfo.TexSafeCount[i][amount_flag][6];
        total_sub1    = total_sub1    + ownerinfo.TexSafeCount[i][amount_flag][11];
        total_sub2    = total_sub2    + ownerinfo.TexSafeCount[i][amount_flag][12];
        total_supply  = total_supply  + ownerinfo.TexSupplyTotal[i];
        total_sales   = total_sales   + ownerinfo.TexSalesTotal[i][0];
        total_safe    = total_safe    + ownerinfo.TexSafeTotal[i][amount_flag];
        total_surplus = total_surplus + ownerinfo.TexSurplus[i];
        if( i % 2 != 0)
        {
            tableClass = "tableLG";
        }
        else
        {
            tableClass = "tableLW";
        }
        roomtag[i] = "<TD align=center class=" + tableClass + ">";
        // ��������
        roomtag[i] = roomtag[i] + "<A href=texdetail.jsp?HotelId=" + hotelid + "&RoomCode=" + ownerinfo.StatusDetailRoomCode[i] + " target=\"_self\">" + ownerinfo.StatusDetailRoomName[i] + "</A></TD>";
        // �ŏI�W�����i�o�߁j
        roomtag[i] = roomtag[i] + "<TD align=center class=" + tableClass + ">" +
                     ownerinfo.TexSupplyDate[i] / 10000 + "/" +
                     ownerinfo.TexSupplyDate[i] / 100 % 100 + "/" +
                     ownerinfo.TexSupplyDate[i] % 100 +
                     "</TD>";

        // ���ݖ���
        // 10000
        roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                     ownerinfo.TexSafeCount[i][amount_flag][0] + "&nbsp;</TD>";
        // 5000
        roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                     ownerinfo.TexSafeCount[i][amount_flag][1] + "&nbsp;</TD>";
        // 2000
        roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                     ownerinfo.TexSafeCount[i][amount_flag][10] + "&nbsp;</TD>";
        // 1000
        roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                     ownerinfo.TexSafeCount[i][amount_flag][2] + "&nbsp;</TD>";
        // 500
        roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                     ownerinfo.TexSafeCount[i][amount_flag][3] + "&nbsp;</TD>";
        // 100
        roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                     ownerinfo.TexSafeCount[i][amount_flag][4] + "&nbsp;</TD>";
        // 50
        roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                     ownerinfo.TexSafeCount[i][amount_flag][5] + "&nbsp;</TD>";
        // 10
        roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                     ownerinfo.TexSafeCount[i][amount_flag][6] + "&nbsp;</TD>";
        // SUB1
        roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                     ownerinfo.TexSafeCount[i][amount_flag][11] + "&nbsp;</TD>";
        // SUB2
        roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                     ownerinfo.TexSafeCount[i][amount_flag][12] + "&nbsp;</TD>";

        // ���Ȃ���[
        if( ownerinfo.TexSupplyTotal[i] != 0 )
        {
            roomtag[i] = roomtag[i] + "<TD align=center class=" + tableClass + ">" +
                         Kanma.get(ownerinfo.TexSupplyTotal[i]) + "</TD>";
        }
        else
        {
            roomtag[i] = roomtag[i] + "<TD align=center class=" + tableClass + ">" +
                         "�Ȃ�" + "</TD>";
        }

        // ���v���z
        roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                     Kanma.get(ownerinfo.TexSafeTotal[i][amount_flag]) + "&nbsp;</TD>";

        // ������z
        roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                     Kanma.get(ownerinfo.TexSalesTotal[i][0]) + "&nbsp;</TD>";


        // �]���
        roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                     Kanma.get(ownerinfo.TexSurplus[i]) + "&nbsp;</TD>";
    }

    i = count;
    if( i % 2 != 0)
    {
        tableClass = "tableLG";
    }
    else
    {
        tableClass = "tableLW";
    }

    roomtag[i] = "<TD align=center class=" + tableClass + ">";
    // ��������
    roomtag[i] = roomtag[i] + "���v</TD>";
    // �ŏI�W�����i�o�߁j
    roomtag[i] = roomtag[i] + "<TD align=center class=" + tableClass + ">&nbsp;</TD>";
    // ���ݖ���
    // 10000
    roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                 total_10000 + "&nbsp;</TD>";
    // 5000
    roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                 total_5000 + "&nbsp;</TD>";
    // 2000
    roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                 total_2000 + "&nbsp;</TD>";
    // 1000
    roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                 total_1000 + "&nbsp;</TD>";
    // 500
    roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                 total_500  + "&nbsp;</TD>";
    // 100
    roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                 total_100  + "&nbsp;</TD>";
    // 50
    roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                 total_50   + "&nbsp;</TD>";
    // 10
    roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                 total_10   +  "&nbsp;</TD>";
    // SUB1
    roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                 total_sub1 + "&nbsp;</TD>";
    // SUB2
    roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                 total_sub2 + "&nbsp;</TD>";

    // ���Ȃ���[
    if(total_supply != 0 )
    {
        roomtag[i] = roomtag[i] + "<TD align=center class=" + tableClass + ">" +
                     Kanma.get(total_supply) + "</TD>";
    }
    else
    {
        roomtag[i] = roomtag[i] + "<TD align=center class=" + tableClass + ">" +
                     "�Ȃ�" + "</TD>";
    }
    // ���v���z
    roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                 Kanma.get(total_safe) + "&nbsp;</TD>";

    // ������z
    roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                 Kanma.get(total_sales) + "&nbsp;</TD>";

    // �]���
    roomtag[i] = roomtag[i] + "<TD align=right class=" + tableClass + ">" +
                 Kanma.get(total_surplus) + "&nbsp;</TD>";

%>
<%
    if (TexExist != false)
    {
%>
<!-- �X�ܕ\�� --> 
<a name="<%= hotelid %>"></a> 
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<form action="roomdisp_texpay.jsp" id="id<%= hotelid %>" method="post">
  <tr> 
    <td height="20">
      <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
        <tr> 
          <td width="200" height="20" bgcolor="#22333F" class="tab"><font color="#FFFFFF"><%= hotelname %>&nbsp;���p��</font></td>
          <td width="15" height="20" valign="bottom"><IMG src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
          <td valign="bottom">
            <div class="navy10px">
            <img src="../../common/pc/image/spacer.gif" width="12" height="16" align="absmiddle"><a href="#pagetop" class="navy10px">&gt;&gt;���̃y�[�W�̃g�b�v��</a>
			</div>
          </td>
		  <td>
            <div class="navy10px">
         </div>
          </td>
        </tr>
      </table>
    </td>
    <td width="3">&nbsp;</td>
  </tr>

<!-- ��������\ -->
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
                <td class="bar">�����ʓ��o����</td>
				<td class="bar">
            <select name="amount" id="id<%= hotelid %>">

<%
    if( amount_flag == 3 )
    {
%>
              <option value="3" selected>���ݎc���\��</option>
              <option value="2" >�����c���\��</option>
<%
    }
    else
    {
%>
              <option value="3" >���ݎc���\��</option>
              <option value="2" selected>�����c���\��</option>
<%
    }
%>
           </select>
           <input type="submit" id="id<%= hotelid %>" value="�ؑ�">
				</td>
                <td align="right" class="bar2" nowrap>�������ԍ���I�ԂƋq���ʓ��o���󋵂������ɂȂ�܂��B</td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td><img src="../../common/pc/image/spacer.gif" width="160" height="14"></td>
        </tr>
        <tr>
          <td align="center" valign="top">
            <table width="99%" border="0" cellspacing="0" cellpadding="0">
              <tr align="center" valign="middle">
                <td class="tableLN">���@��</td>
                <td class="tableLN" nowrap>�ŏI�W���i�o�߁j</td>
                <td class="tableLN">1��</td>
                <td class="tableLN">5��</td>
                <td class="tableLN">2��</td>
                <td class="tableLN">1��</td>
                <td class="tableLN">500</td>
                <td class="tableLN">100</td>
                <td class="tableLN">50</td>
                <td class="tableLN">10</td>
                <td class="tableLN">sub1</td>
                <td class="tableLN">sub2</td>
                <td class="tableLN" nowrap>���Ȃ���[</td>
                <td class="tableLN" nowrap>���v���z</td>
                <td class="tableLN" nowrap>���@��</td>
                <td class="tableRN" nowrap>�]���</td>
              </tr>
<%
        for( i = 0 ; i < count + 1 ; i++ )
        {
%>
              <TR valign="middle">
                <%= roomtag[i] %>
              </TR>
<%
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
</form>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
  </tr>
</table>

<!-- �����܂� -->
<%
    }
%>
