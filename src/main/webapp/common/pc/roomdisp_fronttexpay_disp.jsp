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
    String          termtag[];
    String          now_date;
    String          now_time;
    DateEdit        df;
    NumberFormat    nf;
    String          amount;
    int amount_flag   = 6;
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
    int sub_total     = 0;

    count     = ownerinfo.FrontTexTermCount;
    termtag   = new String[ownerinfo.OWNERINFO_ROOMMAX];
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
    amount = ReplaceString.getParameter(request,"amount");
    if( amount == null )
    {
        amount_flag = 6;
    }
    else
    {
        amount_flag = Integer.parseInt(amount);
    }
    // �����g�^�O�̏�����
    for( i = 0 ; i < ownerinfo.OWNERINFO_ROOMMAX ; i++ )
    {
        termtag[i] = "&nbsp;";
    }

    for( i = 0 ; i < count ; i++ )
    {
        total_10000   = total_10000   + ownerinfo.FrontTexSafeCount[i][amount_flag][0];
        total_5000    = total_5000    + ownerinfo.FrontTexSafeCount[i][amount_flag][1];
        total_2000    = total_2000    + ownerinfo.FrontTexSafeCount[i][amount_flag][10];
        total_1000    = total_1000    + ownerinfo.FrontTexSafeCount[i][amount_flag][2];
        total_500     = total_500     + ownerinfo.FrontTexSafeCount[i][amount_flag][3];
        total_100     = total_100     + ownerinfo.FrontTexSafeCount[i][amount_flag][4];
        total_50      = total_50      + ownerinfo.FrontTexSafeCount[i][amount_flag][5];
        total_10      = total_10      + ownerinfo.FrontTexSafeCount[i][amount_flag][6];
        total_sub1    = total_sub1    + ownerinfo.FrontTexSafeCount[i][amount_flag][11];
        total_sub2    = total_sub2    + ownerinfo.FrontTexSafeCount[i][amount_flag][12];
        sub_total     = ownerinfo.FrontTexSalesTotal[i][0]
                        + ownerinfo.FrontTexSalesTotal[i][1]
                        + ownerinfo.FrontTexSalesTotal[i][2]
                        + ownerinfo.FrontTexSalesTotal[i][3]
                        + ownerinfo.FrontTexSalesTotal[i][4]
                        + ownerinfo.FrontTexSalesTotal[i][5]
                        + ownerinfo.FrontTexSalesTotal[i][6]
                        + ownerinfo.FrontTexSalesTotal[i][7];

        total_sales   = total_sales   + ownerinfo.FrontTexSalesTotal[i][0]
                                      + ownerinfo.FrontTexSalesTotal[i][1]
                                      + ownerinfo.FrontTexSalesTotal[i][2]
                                      + ownerinfo.FrontTexSalesTotal[i][3]
                                      + ownerinfo.FrontTexSalesTotal[i][4]
                                      + ownerinfo.FrontTexSalesTotal[i][5]
                                      + ownerinfo.FrontTexSalesTotal[i][6]
                                      + ownerinfo.FrontTexSalesTotal[i][7];
        total_safe    = total_safe    + ownerinfo.FrontTexSafeTotal[i][amount_flag];

        if( i % 2 != 0)
        {
            termtag[i] = "<TD align=center class=tableLG>";
            // �[������
            termtag[i] = termtag[i] + "<A href=fronttexdetail.jsp?HotelId=" + hotelid + "&TermCode=" + ownerinfo.FrontTexTermCode[i] + " target=\"_self\">" + ownerinfo.FrontTexTermName[i] +amount_flag+ "</A></TD>";

            // �戵���
            if( ownerinfo.FrontTexServiceStat[i] == 1 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLG>�戵��</TD>";
            }
            else if( ownerinfo.FrontTexServiceStat[i] == 2 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLG>�戵���~</TD>";
            }
            else if( ownerinfo.FrontTexServiceStat[i] == 3 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLG>�Ɩ��I��</TD>";
            }
            else if( ownerinfo.FrontTexServiceStat[i] == 4 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLG>���Ȃ���</TD>";
            }
            else if( ownerinfo.FrontTexServiceStat[i] == 5 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLG>�����</TD>";
            }
            else
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLG>�s��</TD>";
            }

            // ���[�h�i�L�[SW��ԁj
            if( ownerinfo.FrontTexKeySwStat[i] == 1 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLG>���Ȑf�f</TD>";
            }
            else if( ownerinfo.FrontTexKeySwStat[i] == 2 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLG>�ێ�</TD>";
            }
            else if( ownerinfo.FrontTexKeySwStat[i] == 3 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLG>�ʏ�</TD>";
            }
            else if( ownerinfo.FrontTexKeySwStat[i] == 4 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLG>�戵���~</TD>";
            }
            else if( ownerinfo.FrontTexKeySwStat[i] == 5 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLG>��[</TD>";
            }
            else if( ownerinfo.FrontTexKeySwStat[i] == 6 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLG>�Ɩ�</TD>";
            }
            else
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLG>�s��</TD>";
            }

            // �Z�L�����e�B���
            if( ownerinfo.FrontTexSecurityStat[i] == 1 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLG>����</TD>";
            }
            else if( ownerinfo.FrontTexSecurityStat[i] == 2 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLG>��~</TD>";
            }
            else
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLG>�s��</TD>";
            }

            // ���ݖ���
            // 10000
            termtag[i] = termtag[i] + "<TD align=right class=tableLG>" +
                         ownerinfo.FrontTexSafeCount[i][amount_flag][0] + "&nbsp;</TD>";
            // 5000
            termtag[i] = termtag[i] + "<TD align=right class=tableLG>" +
                         ownerinfo.FrontTexSafeCount[i][amount_flag][1] + "&nbsp;</TD>";
            // 2000
            termtag[i] = termtag[i] + "<TD align=right class=tableLG>" +
                         ownerinfo.FrontTexSafeCount[i][amount_flag][10] + "&nbsp;</TD>";
            // 1000
            termtag[i] = termtag[i] + "<TD align=right class=tableLG>" +
                         ownerinfo.FrontTexSafeCount[i][amount_flag][2] + "&nbsp;</TD>";
            // 500
            termtag[i] = termtag[i] + "<TD align=right class=tableLG>" +
                         ownerinfo.FrontTexSafeCount[i][amount_flag][3] + "&nbsp;</TD>";
            // 100
            termtag[i] = termtag[i] + "<TD align=right class=tableLG>" +
                         ownerinfo.FrontTexSafeCount[i][amount_flag][4] + "&nbsp;</TD>";
            // 50
            termtag[i] = termtag[i] + "<TD align=right class=tableLG>" +
                         ownerinfo.FrontTexSafeCount[i][amount_flag][5] + "&nbsp;</TD>";
            // 10
            termtag[i] = termtag[i] + "<TD align=right class=tableLG>" +
                         ownerinfo.FrontTexSafeCount[i][amount_flag][6] + "&nbsp;</TD>";
            // sub1
            termtag[i] = termtag[i] + "<TD align=right class=tableLG>" +
                         ownerinfo.FrontTexSafeCount[i][amount_flag][11] + "&nbsp;</TD>";
            // sub2
            termtag[i] = termtag[i] + "<TD align=right class=tableLG>" +
                         ownerinfo.FrontTexSafeCount[i][amount_flag][12] + "&nbsp;</TD>";
            // ���v���z
            termtag[i] = termtag[i] + "<TD align=right class=tableLG>" +
                         Kanma.get(ownerinfo.FrontTexSafeTotal[i][amount_flag]) + "&nbsp;</TD>";

            // ������z
            termtag[i] = termtag[i] + "<TD align=right class=tableRG>" +
                         Kanma.get(sub_total) + "&nbsp;</TD>";

        }
        else
        {
            termtag[i] = "<TD align=center class=tableLW>";
            // ��������
            termtag[i] = termtag[i] + "<A href=fronttexdetail.jsp?HotelId=" + hotelid + "&TermCode=" + ownerinfo.FrontTexTermCode[i] + " target=\"_self\">" + ownerinfo.FrontTexTermName[i] +amount_flag+ "</A></TD>";

            // �戵���
            if( ownerinfo.FrontTexServiceStat[i] == 1 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLW>�戵��</TD>";
            }
            else if( ownerinfo.FrontTexServiceStat[i] == 2 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLW>�戵���~</TD>";
            }
            else if( ownerinfo.FrontTexServiceStat[i] == 3 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLW>�Ɩ��I��</TD>";
            }
            else if( ownerinfo.FrontTexServiceStat[i] == 4 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLW>���Ȃ���</TD>";
            }
            else if( ownerinfo.FrontTexServiceStat[i] == 5 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLW>�����</TD>";
            }
            else
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLW>�s��</TD>";
            }

            // ���[�h�i�L�[SW��ԁj
            if( ownerinfo.FrontTexKeySwStat[i] == 1 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLW>���Ȑf�f</TD>";
            }
            else if( ownerinfo.FrontTexKeySwStat[i] == 2 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLW>�ێ�</TD>";
            }
            else if( ownerinfo.FrontTexKeySwStat[i] == 3 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLW>�ʏ�</TD>";
            }
            else if( ownerinfo.FrontTexKeySwStat[i] == 4 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLW>�戵���~</TD>";
            }
            else if( ownerinfo.FrontTexKeySwStat[i] == 5 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLW>��[</TD>";
            }
            else if( ownerinfo.FrontTexKeySwStat[i] == 6 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLW>�Ɩ�</TD>";
            }
            else
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLW>�s��</TD>";
            }

            // �Z�L�����e�B���
            if( ownerinfo.FrontTexSecurityStat[i] == 1 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLW>����</TD>";
            }
            else if( ownerinfo.FrontTexSecurityStat[i] == 2 )
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLW>��~</TD>";
            }
            else
            {
                termtag[i] = termtag[i] + "<TD align=center class=tableLW>�s��</TD>";
            }

            // ���ݖ���
            // 10000
            termtag[i] = termtag[i] + "<TD align=right class=tableLW>" +
                         ownerinfo.FrontTexSafeCount[i][amount_flag][0] + "&nbsp;</TD>";
            // 5000
            termtag[i] = termtag[i] + "<TD align=right class=tableLW>" +
                         ownerinfo.FrontTexSafeCount[i][amount_flag][1] + "&nbsp;</TD>";
            // 2000
            termtag[i] = termtag[i] + "<TD align=right class=tableLW>" +
                         ownerinfo.FrontTexSafeCount[i][amount_flag][10] + "&nbsp;</TD>";
            // 1000
            termtag[i] = termtag[i] + "<TD align=right class=tableLW>" +
                         ownerinfo.FrontTexSafeCount[i][amount_flag][2] + "&nbsp;</TD>";
            // 500
            termtag[i] = termtag[i] + "<TD align=right class=tableLW>" +
                         ownerinfo.FrontTexSafeCount[i][amount_flag][3] + "&nbsp;</TD>";
            // 100
            termtag[i] = termtag[i] + "<TD align=right class=tableLW>" +
                         ownerinfo.FrontTexSafeCount[i][amount_flag][4] + "&nbsp;</TD>";
            // 50
            termtag[i] = termtag[i] + "<TD align=right class=tableLW>" +
                         ownerinfo.FrontTexSafeCount[i][amount_flag][5] + "&nbsp;</TD>";
            // 10
            termtag[i] = termtag[i] + "<TD align=right class=tableLW>" +
                         ownerinfo.FrontTexSafeCount[i][amount_flag][6] + "&nbsp;</TD>";
            // sub1
            termtag[i] = termtag[i] + "<TD align=right class=tableLW>" +
                         ownerinfo.FrontTexSafeCount[i][amount_flag][11] + "&nbsp;</TD>";
            // sub2
            termtag[i] = termtag[i] + "<TD align=right class=tableLW>" +
                         ownerinfo.FrontTexSafeCount[i][amount_flag][12] + "&nbsp;</TD>";
            // ���v���z
            termtag[i] = termtag[i] + "<TD align=right class=tableLW>" +
                         Kanma.get(ownerinfo.FrontTexSafeTotal[i][amount_flag]) + "&nbsp;</TD>";
            // ������z
            termtag[i] = termtag[i] + "<TD align=right class=tableRW>" +
                         Kanma.get(sub_total) + "&nbsp;</TD>";
        }
    }

    i = count;
    if( i % 2 != 0)
    {
        termtag[i] = "<TD align=center class=tableLG>";
        // ��������
        termtag[i] = termtag[i] + "���v</TD>";

        termtag[i] = termtag[i] + "<td align=right class=tableLG>&nbsp;</TD>";
        termtag[i] = termtag[i] + "<td align=right class=tableLG>&nbsp;</TD>";
        termtag[i] = termtag[i] + "<td align=right class=tableLG>&nbsp;</TD>";

        // ���ݖ���
        // 10000
        termtag[i] = termtag[i] + "<TD align=right class=tableLG>" +
                     total_10000 + "&nbsp;</TD>";
        // 5000
        termtag[i] = termtag[i] + "<TD align=right class=tableLG>" +
                     total_5000 + "&nbsp;</TD>";
        // 2000
        termtag[i] = termtag[i] + "<TD align=right class=tableLG>" +
                     total_2000 + "&nbsp;</TD>";
        // 1000
        termtag[i] = termtag[i] + "<TD align=right class=tableLG>" +
                     total_1000 + "&nbsp;</TD>";
        // 500
        termtag[i] = termtag[i] + "<TD align=right class=tableLG>" +
                     total_500  + "&nbsp;</TD>";
        // 100
        termtag[i] = termtag[i] + "<TD align=right class=tableLG>" +
                     total_100  + "&nbsp;</TD>";
        // 50
        termtag[i] = termtag[i] + "<TD align=right class=tableLG>" +
                     total_50   + "&nbsp;</TD>";
        // 10
        termtag[i] = termtag[i] + "<TD align=right class=tableLG>" +
                     total_10   +  "&nbsp;</TD>";
        // sub1
        termtag[i] = termtag[i] + "<TD align=right class=tableLG>" +
                     total_sub1 + "&nbsp;</TD>";
        // sub2
        termtag[i] = termtag[i] + "<TD align=right class=tableLG>" +
                     total_sub2 +  "&nbsp;</TD>";
        // ���v���z
        termtag[i] = termtag[i] + "<TD align=right class=tableLG>" +
                     Kanma.get(total_safe) + "&nbsp;</TD>";

        // ������z
        termtag[i] = termtag[i] + "<TD align=right class=tableRG>" +
                     Kanma.get(total_sales) + "&nbsp;</TD>";
    }
    else
    {
        termtag[i] = "<TD align=center class=tableLW>";
        // ��������
        termtag[i] = termtag[i] + "���v</TD>";

        termtag[i] = termtag[i] + "<td align=right class=tableLW>&nbsp;</TD>";
        termtag[i] = termtag[i] + "<td align=right class=tableLW>&nbsp;</TD>";
        termtag[i] = termtag[i] + "<td align=right class=tableLW>&nbsp;</TD>";

        // ���ݖ���
        // 10000
        termtag[i] = termtag[i] + "<TD align=right class=tableLW>" +
                     total_10000 + "&nbsp;</TD>";
        // 5000
        termtag[i] = termtag[i] + "<TD align=right class=tableLW>" +
                     total_5000 + "&nbsp;</TD>";
        // 2000
        termtag[i] = termtag[i] + "<TD align=right class=tableLW>" +
                     total_2000 + "&nbsp;</TD>";
        // 1000
        termtag[i] = termtag[i] + "<TD align=right class=tableLW>" +
                     total_1000 + "&nbsp;</TD>";
        // 500
        termtag[i] = termtag[i] + "<TD align=right class=tableLW>" +
                     total_500  + "&nbsp;</TD>";
        // 100
        termtag[i] = termtag[i] + "<TD align=right class=tableLW>" +
                     total_100  + "&nbsp;</TD>";
        // 50
        termtag[i] = termtag[i] + "<TD align=right class=tableLW>" +
                     total_50   + "&nbsp;</TD>";
        // 10
        termtag[i] = termtag[i] + "<TD align=right class=tableLW>" +
                     total_10   +  "&nbsp;</TD>";
        // sub1
        termtag[i] = termtag[i] + "<TD align=right class=tableLW>" +
                     total_sub1 + "&nbsp;</TD>";
        // sub2
        termtag[i] = termtag[i] + "<TD align=right class=tableLW>" +
                     total_sub2 +  "&nbsp;</TD>";
        // ���v���z
        termtag[i] = termtag[i] + "<TD align=right class=tableLW>" +
                     Kanma.get(total_safe) + "&nbsp;</TD>";

        // ������z
        termtag[i] = termtag[i] + "<TD align=right class=tableRW>" +
                     Kanma.get(total_sales) + "&nbsp;</TD>";

    }
    if (count != 0)
    {
%>
<!-- �X�ܕ\�� --> 
<a name="<%= hotelid %>"></a> 
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td height="20">
      <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
        <tr> 
          <td width="200" height="20" bgcolor="#22333F" class="tab"><font color="#FFFFFF"><%= hotelname %>&nbsp;���p��</font></td>
          <td width="15" height="20" valign="bottom"><IMG src="/common/pc/image/tab1.gif" width="15" height="20"></td>
          <td valign="bottom">
            <div class="navy10px">
            <img src="/common/pc/image/spacer.gif" width="12" height="16" align="absmiddle"><a href="#pagetop" class="navy10px">&gt;&gt;���̃y�[�W�̃g�b�v��</a>
            </div>
          </td>
          <td>
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
          <td height="6" align="center"><img src="/common/pc/image/spacer.gif" width="300" height="6"></td>
        </tr>
        <tr>
          <td align="center">
            <table width="99%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td class="bar">���@�ʓ��o����</td>
                <td align="right" class="bar2" nowrap>�����@�ԍ���I�Ԃƍ��@�ʓ��o���󋵂������ɂȂ�܂��B</td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td><img src="/common/pc/image/spacer.gif" width="160" height="14"></td>
        </tr>
        <tr>
          <td align="center" valign="top">
            <table width="99%" border="0" cellspacing="0" cellpadding="0">
              <tr align="center" valign="middle">
                <td class="tableLN">�[��</td>
                <td class="tableLN">���</td>
                <td class="tableLN">���[�h</td>
                <td class="tableLN">�Z�L�����e�B</td>
                <td class="tableLN">1��</td>
                <td class="tableLN">5��</td>
                <td class="tableLN">2��</td>
                <td class="tableLN">1��</td>
                <td class="tableLN">500</td>
                <td class="tableLN">100</td>
                <td class="tableLN">50</td>
                <td class="tableLN">10</td>
                <td class="tableLN">5</td>
                <td class="tableLN">1</td>
                <td class="tableLN" nowrap>���v���z</td>
                <td class="tableRN" nowrap>���@��</td>
              </tr>
<%
        for( i = 0 ; i < count + 1 ; i++ )
        {
%>
              <TR valign="middle">
                <%= termtag[i] %>
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
          <td><img src="/common/pc/image/tab_kado.gif" width="3" height="3"></td>
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
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="/common/pc/image/spacer.gif" width="100" height="6"></td>
  </tr>
</table>

<!-- �����܂� -->
<%
    }
%>
