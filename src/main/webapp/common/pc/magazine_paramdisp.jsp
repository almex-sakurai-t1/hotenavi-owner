<%@ page contentType="text/html; charset=Windows-31J" %>
<%
    if( condition == 0 )
    {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">�S��</td>
                      </tr>
<%
    }
    else if( condition == 1 )
    {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">�r�W�^�[�̂�</td>
                      </tr>
<%
    }
    else if( condition == 2 )
    {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">�����o�[�̂�</td>
                      </tr>
<%
    }
    else if( condition == 3 )
    {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">�i�荞�ݗL���</td>
                      </tr>
<%
        if( birthday_check == 1 )
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">
                          �a�����F<%= param_birthday_startmonth %>��<%= param_birthday_startday %>��
                          �`<%= param_birthday_endmonth %>��<%= param_birthday_endday %>��
                        </td>
                      </tr>
<%
        }
        else
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">�a�����w��Ȃ�</td>
                      </tr>
<%
        }
%>
<%
        if( memorial_check == 1 )
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">
                          �L�O���F<%= param_memorial_startmonth %>��<%= param_memorial_startday %>��
                          �`<%= param_memorial_endmonth %>��<%= param_memorial_endday %>��
                        </td>
                      </tr>
<%
        }
        else
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">�L�O���w��Ȃ�</td>
                      </tr>
<%
        }
%>
<%
        if( lastday_check == 1 )
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">
                          �ŏI���p���F<%= param_lastday_startyear %>�N<%= param_lastday_startmonth %>��<%= param_lastday_startday %>��
                          �`<%= param_lastday_endyear %>�N<%= param_lastday_endmonth %>��<%= param_lastday_endday %>��
                        </td>
                      </tr>
<%
        }
        else
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">�ŏI���p���w��Ȃ�</td>
                      </tr>
<%
        }
%>
<%
        if( count_check == 1 )
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">
                          �L���񐔁F<%= param_count_start %>�`<%= param_count_end %>
                        </td>
                      </tr>
<%
        }
        else
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">�L���񐔎w��Ȃ�</td>
                      </tr>
<%
        }
%>
<%
        if( total_check == 1 )
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">
                          ���p���z�F<%= param_total_start %>�`<%= param_total_end %>
                        </td>
                      </tr>
<%
        }
        else
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">���p���z�w��Ȃ�</td>
                      </tr>
<%
        }
%>
<%
        if( point_check == 1 )
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">
                          �|�C���g�F<%= param_point_start %>�`<%= param_point_end %>
                        </td>
                      </tr>
<%
        }
        else
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">�|�C���g�w��Ȃ�</td>
                      </tr>
<%
        }
%>
<%
        if( point2_check == 1 )
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">
                          �|�C���g2�F<%= param_point2_start %>�`<%= param_point2_end %>
                        </td>
                      </tr>
<%
        }
        else
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">�|�C���g2�w��Ȃ�</td>
                      </tr>
<%
        }
%>
<%
        if( rank_check == 1 )
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">
                          �����N�F<%= param_rank_start %>�`<%= param_rank_end %>
                        </td>
                      </tr>
<%
        }
        else
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">�����N�w��Ȃ�</td>
                      </tr>
<%
        }
%>
<%
        if( customid_check == 1 )
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">
                          �ڋq�ԍ��F<%= param_customid_start %>�`<%= param_customid_end %>
                        </td>
                      </tr>
<%
        }
        else
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">�ڋq�ԍ��w��Ȃ�</td>
                      </tr>
<%
        }
%>
<%
        if( mailaddress_check == 1 )
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">
                          ���[���A�h���X�F<%= param_mailaddress %>
                        </td>
                      </tr>
<%
        }
        else
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">���[���A�h���X�w��Ȃ�</td>
                      </tr>
<%
        }
%>
<%
        if( lastsend_check == 1 )
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">
                          �ŏI���M���i���O�j�F<%= param_lastsend_startyear %>�N<%= param_lastsend_startmonth %>��<%= param_lastsend_startday %>��<%= param_lastsend_starthour %>��<%= param_lastsend_startminute %>��
                          �`<%= param_lastsend_endyear %>�N<%= param_lastsend_endmonth %>��<%= param_lastsend_endday %>��<%= param_lastsend_endhour %>��<%= param_lastsend_endminute %>��
                        </td>
                      </tr>
<%
        }
        else
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">�ŏI���M���w��Ȃ�</td>
                      </tr>
<%
        }
%>
<%
    }
%>
