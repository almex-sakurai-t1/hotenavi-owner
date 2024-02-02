<%@ page contentType="text/html; charset=Windows-31J" %>
<%
    if( condition == 0 )
    {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">全員</td>
                      </tr>
<%
    }
    else if( condition == 1 )
    {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">ビジターのみ</td>
                      </tr>
<%
    }
    else if( condition == 2 )
    {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">メンバーのみ</td>
                      </tr>
<%
    }
    else if( condition == 3 )
    {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">絞り込み有りで</td>
                      </tr>
<%
        if( birthday_check == 1 )
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">
                          誕生日：<%= param_birthday_startmonth %>月<%= param_birthday_startday %>日
                          ～<%= param_birthday_endmonth %>月<%= param_birthday_endday %>日
                        </td>
                      </tr>
<%
        }
        else
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">誕生日指定なし</td>
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
                          記念日：<%= param_memorial_startmonth %>月<%= param_memorial_startday %>日
                          ～<%= param_memorial_endmonth %>月<%= param_memorial_endday %>日
                        </td>
                      </tr>
<%
        }
        else
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">記念日指定なし</td>
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
                          最終利用日：<%= param_lastday_startyear %>年<%= param_lastday_startmonth %>月<%= param_lastday_startday %>日
                          ～<%= param_lastday_endyear %>年<%= param_lastday_endmonth %>月<%= param_lastday_endday %>日
                        </td>
                      </tr>
<%
        }
        else
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">最終利用日指定なし</td>
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
                          有効回数：<%= param_count_start %>～<%= param_count_end %>
                        </td>
                      </tr>
<%
        }
        else
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">有効回数指定なし</td>
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
                          利用金額：<%= param_total_start %>～<%= param_total_end %>
                        </td>
                      </tr>
<%
        }
        else
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">利用金額指定なし</td>
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
                          ポイント：<%= param_point_start %>～<%= param_point_end %>
                        </td>
                      </tr>
<%
        }
        else
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">ポイント指定なし</td>
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
                          ポイント2：<%= param_point2_start %>～<%= param_point2_end %>
                        </td>
                      </tr>
<%
        }
        else
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">ポイント2指定なし</td>
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
                          ランク：<%= param_rank_start %>～<%= param_rank_end %>
                        </td>
                      </tr>
<%
        }
        else
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">ランク指定なし</td>
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
                          顧客番号：<%= param_customid_start %>～<%= param_customid_end %>
                        </td>
                      </tr>
<%
        }
        else
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">顧客番号指定なし</td>
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
                          メールアドレス：<%= param_mailaddress %>
                        </td>
                      </tr>
<%
        }
        else
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">メールアドレス指定なし</td>
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
                          最終送信日（除外）：<%= param_lastsend_startyear %>年<%= param_lastsend_startmonth %>月<%= param_lastsend_startday %>日<%= param_lastsend_starthour %>時<%= param_lastsend_startminute %>分
                          ～<%= param_lastsend_endyear %>年<%= param_lastsend_endmonth %>月<%= param_lastsend_endday %>日<%= param_lastsend_endhour %>時<%= param_lastsend_endminute %>分
                        </td>
                      </tr>
<%
        }
        else
        {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12">最終送信日指定なし</td>
                      </tr>
<%
        }
%>
<%
    }
%>
