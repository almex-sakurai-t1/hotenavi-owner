<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td colspan="2" class="tableRN">�����o�[���</td>
  </tr>
  <tr>
<%
    if( ownerinfo.MemberCustomId[0] != null && ownerinfo.MemberCustomId[0].compareTo("") != 0 )
    {
%>
    <td valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td align="left" valign="middle" class="tableLW" nowrap><div class="space2">�����o�[�ԍ�</div></td>
          <td align="right" valign="middle" class="tableRW" nowrap>&nbsp;<%= ownerinfo.MemberCustomId[0] %></td>
        </tr>
        <tr>
          <td align="left" valign="middle" nowrap class="tableLG"><div class="space2">�j�b�N�l�[��</div></td>
          <td align="right" valign="middle" class="tableRG" nowrap>&nbsp;<%= ownerinfo.MemberNickName[0] %></td>
        </tr>
        <tr>
          <td align="left" valign="middle" nowrap class="tableLW"><div class="space2">�a����</div></td>
          <td align="right" valign="middle" class="tableRW" nowrap>&nbsp;
<%
        if( ownerinfo.MemberBirthday1[0] != 0 )
        {
%>
          <%= ownerinfo.MemberBirthday1[0] / 10000 %>/<%= ownerinfo.MemberBirthday1[0] / 100 % 100 %>/<%= ownerinfo.MemberBirthday1[0] % 100 %>
<%
        }
%>
<%
        if( ownerinfo.MemberBirthday2[0] != 0 )
        {
%>
          <br/><%= ownerinfo.MemberBirthday2[0] / 10000 %>/<%= ownerinfo.MemberBirthday2[0] / 100 % 100 %>/<%= ownerinfo.MemberBirthday2[0] % 100 %>
<%
        }
%>
          </td>
        </tr>
        <tr>
          <td align="left" valign="middle" nowrap class="tableLG"><div class="space2">�����N��</div></td>
          <td align="right" valign="middle" class="tableRG" nowrap>&nbsp;<%= ownerinfo.MemberRankName[0] %></td>
        </tr>
        <tr>
          <td align="left" valign="middle" nowrap class="tableLW"><div class="space2">���������L���O</div></td>
          <td align="right" valign="middle" class="tableRW" nowrap>&nbsp;<%= Kanma.get(ownerinfo.MemberNowRanking[0]) %> ��</td>
        </tr>
        <tr>
          <td align="left" valign="middle" nowrap class="tableLG"><div class="space2">�����L���O�����p��</div></td>
          <td align="right" valign="middle" class="tableRG" nowrap>&nbsp;<%= Kanma.get(ownerinfo.MemberRankingCount[0]) %> ��</td>
        </tr>
        <tr>
          <td align="left" valign="middle" nowrap class="tableLW"><div class="space2">�����L���O�����p���z</div></td>
          <td align="right" valign="middle" class="tableRW" nowrap>&nbsp;<%= Kanma.get(ownerinfo.MemberRankingTotal[0]) %> �~</td>
        </tr>
        <tr>
          <td align="left" valign="middle" nowrap class="tableLG"><div class="space2">�J�z���p���z</div></td>
          <td align="right" valign="middle" class="tableRG" nowrap>&nbsp;<%= Kanma.get(ownerinfo.MemberAddupTotal[0]) %> �~</td>
        </tr>
      </table>
    </td>
    <td valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td align="left" valign="middle" nowrap class="tableLW2"><div class="space2">���@��</div></td>
          <td align="right" valign="middle" class="tableRW" nowrap>&nbsp;<%= ownerinfo.MemberName[0] %></td>
        </tr>
        <tr>
          <td align="left" valign="middle" nowrap class="tableLG2"><div class="space2">�o�^��</div></td>
          <td align="right" valign="middle" class="tableRG" nowrap>&nbsp;<%= ownerinfo.MemberEntryDate[0] / 10000 %>/<%= ownerinfo.MemberEntryDate[0] / 100 % 100 %>/<%= ownerinfo.MemberEntryDate[0] % 100 %></td>
        </tr>
        <tr>
          <td align="left" valign="middle" nowrap class="tableLW2"><div class="space2">�L�O��</div></td>
          <td align="right" valign="middle" class="tableRW" nowrap>&nbsp;
<%
        if( ownerinfo.MemberMemorial1[0] != 0 )
        {
%>
            <%= ownerinfo.MemberMemorial1[0] / 10000 %>/<%= ownerinfo.MemberMemorial1[0] / 100 % 100 %>/<%= ownerinfo.MemberMemorial1[0] % 100 %>
<%
        }
%>
<%
        if( ownerinfo.MemberMemorial2[0] != 0 )
        {
%>
            <br/><%= ownerinfo.MemberMemorial2[0] / 10000 %>/<%= ownerinfo.MemberMemorial2[0] / 100 % 100 %>/<%= ownerinfo.MemberMemorial2[0] % 100 %>
<%
        }
%>
          </td>
        </tr>
        <tr>
          <td align="left" valign="middle" nowrap class="tableLG2"><div class="space2">�|�C���g</div></td>
          <td align="right" valign="middle" class="tableRG" nowrap>&nbsp;<%= Kanma.get(ownerinfo.MemberPoint[0]) %> p</td>
        </tr>
        <tr>
          <td align="left" valign="middle" nowrap class="tableLW2"><div class="space2">�O�������L���O</div></td>
          <td align="right" valign="middle" class="tableRW" nowrap>&nbsp;<%= Kanma.get(ownerinfo.MemberOldRanking[0]) %> ��</td>
        </tr>
        <tr>
          <td align="left" valign="middle" nowrap class="tableLG2"><div class="space2">�W�v���ԓ����p��</div></td>
          <td align="right" valign="middle" class="tableRG" nowrap>&nbsp;<%= Kanma.get(ownerinfo.MemberAddupCount[0]) %> ��</td>
        </tr>
        <tr>
          <td align="left" valign="middle" nowrap class="tableLW2"><div class="space2">�W�v���ԓ����p���z</div></td>
          <td align="right" valign="middle" class="tableRW" nowrap>&nbsp;<%= Kanma.get(ownerinfo.MemberAddupTotal[0]) %> �~</td>
        </tr>
        <tr>
          <td align="left" valign="middle" nowrap class="tableLG2"><div class="space2">�@</div></td>
          <td align="right" valign="middle" class="tableRG" nowrap>&nbsp;</td>
        </tr>
      </table>
    </td>
<%
    }
%>
  </tr>
</table>

