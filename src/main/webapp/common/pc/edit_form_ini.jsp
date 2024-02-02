<%@ page contentType="text/html; charset=Windows-31J" %><%
    String[]     TitleDefault      = new String[300];
    String[][]   MsgTitleDefault   = new String[300][8];
    String[][]   MsgDefault        = new String[300][8];
    String[][]   MsgReplace        = new String[300][8];


//クレジットカード（PC）：Default表示
    TitleDefault   [7]    =                   "ご利用可能なクレジットカード";
    MsgTitleDefault[7][0] =                   "ご利用可能なクレジットカード";
    MsgDefault     [7][0] =                   "当ホテルではクレジットカードがご利用になれます。<br><table bgcolor='#ffffff'><tr><td><img src='/common/image/visa.gif'> <img src='/common/image/master.gif'> <img src='/common/image/jcb.gif'> <img src='/common/image/amex.gif'> <img src='/common/image/dc.gif'> <img src='/common/image/nicos.gif'> <img src='/common/image/diners.gif'> <img src='/common/image/uc.gif'> <img src='/common/image/cf.gif'> <img src='/common/image/debit.gif'> <img src='/common/image/mufg.gif'> <img src='/common/image/orico.gif'> <img src='/common/image/saison.gif'> <img src='/common/image/ufj.gif'> <img src='/common/image/discover.gif'></td></tr></table><br/>\r\n";
    MsgDefault     [7][0] = MsgDefault[7][0]+"※ご請求会社名は株式会社○○○となります。<br/>\r\n";

//クレジットカード(PC）：メッセージ
    MsgReplace     [7][0] =                   "[VISA:visa.gif][MASTER:master.gif][JCB:jcb.gif][AMEX:amex.gif][DC:dc.gif][NICOS:nicos.gif][DINERS:diners.gif][UC:uc.gif][CF:cf.gif][DEBIT:debit.gif][MUFG:mufg.gif][ORICO:orico.gif][SAISON:saison.gif][UFJ:ufj.gif][UFJ:discover.gif]";

//クレジットカード（携帯）：Default表示
    TitleDefault   [8]    =                   "ご利用可能なｸﾚｼﾞｯﾄｶｰﾄﾞ";
    MsgTitleDefault[8][0] =                   "ご利用可能なｸﾚｼﾞｯﾄｶｰﾄﾞ";
    MsgDefault     [8][0] =                   "当ﾎﾃﾙではｸﾚｼﾞｯﾄｶｰﾄﾞがご利用になれます。<br><img src='/common/image/visa.gif'> <img src='/common/image/master.gif'> <img src='/common/image/jcb.gif'> <img src='/common/image/amex.gif'> <img src='/common/image/dc.gif'> <img src='/common/image/nicos.gif'> <img src='/common/image/diners.gif'> <img src='/common/image/uc.gif'> <img src='/common/image/cf.gif'> <img src='/common/image/debit.gif'> <img src='/common/image/mufg.gif'> <img src='/common/image/orico.gif'> <img src='/common/image/saison.gif'> <img src='/common/image/ufj.gif'> <img src='/common/image/discover.gif'><br/>\r\n";
    MsgDefault     [8][0] = MsgDefault[8][0]+"※ご請求会社名は株式会社○○○となります。<br/>\r\n";

//クレジットカード(携帯）：メッセージ
    MsgReplace     [8][0] =                   "[VISA:visa.gif][MASTER:master.gif][JCB:jcb.gif][AMEX:amex.gif][DC:dc.gif][NICOS:nicos.gif][DINERS:diners.gif][UC:uc.gif][CF:cf.gif][DEBIT:debit.gif][MUFG:mufg.gif][ORICO:orico.gif][SAISON:saison.gif][UFJ:ufj.gif][UFJ:discover.gif]";

if ((start_date < trialDate || NewFlag) && trialDate != 99999999 )
{
//メンバー情報（PC）：新ホテナビDefault表示
    TitleDefault   [43]    =                   "メンバー情報";
    MsgTitleDefault[43][0] =                   "メンバー情報の確認・変更登録を行います";
    MsgDefault     [43][0] =                   "<div>メンバー情報を変更する場合は、該当する項目を変更し、下の更新ボタンを押してください。</div>";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<table class=\"multi-column\"><thead>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>メンバーID</th>\r\n<td>%CustomId%</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>お名前</th>\r\n<td>%InfoName%</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>ニックネーム</th>\r\n<td>\r\n<input type=\"text\" name=\"nickName\" id=\"nickName\" value=\"%NickName%\" />\r\n</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>住所</th>\r\n<td>\r\n<input type=\"text\" name=\"infoAddress1\" id=\"infoAddress1\" value=\"%InfoAddress1%\" />\r\n<br />\r\n<input type=\"text\" name=\"infoAddress2\" id=\"infoAddress2\" value=\"%InfoAddress2%\"/>\r\n</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>電話番号</th>\r\n<td>\r\n<input type=\"text\" style=\"width:100px\" name=\"infoTel1\" id=\"infoTel1\" value=\"%InfoTel1%\" />\r\n</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>誕生日</th>\r\n<td>\r\n%InfoBirthday1%\r\n</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>誕生日２</th>\r\n<td>\r\n%InfoBirthday2%\r\n</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>記念日</th>\r\n<td>\r\n%InfoMemorial1%\r\n</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>記念日２</th>\r\n<td>\r\n%InfoMemorial2%\r\n</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>メールアドレス</th>\r\n<td>%InfoMailAddress%</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>メンバーランク</th>\r\n<td>%InfoRankName%</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>総利用回数</th>\r\n<td>%InfoUseCountAll%　回</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>ご利用回数</th>\r\n<td>%InfoUseCount%　回</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>ポイント</th>\r\n<td>%InfoPoint%　P</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>本日のディスカウント</th>\r\n<td>%InfoDiscount%</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>ランキング</th>\r\n<td><a href='/%HotelId%/member/ranking'>ランキング表へ</a></td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>メールマガジン</th>\r\n<td>%InfoMag%</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"</thead>\r\n</table>\r\n";
}
else
{
//メンバー情報（PC）：旧ホテナビDefault表示
    TitleDefault   [43]    =                   "メンバー情報";
    MsgTitleDefault[43][0] =                   "メンバー情報の確認・変更登録を行います";
    MsgDefault     [43][0] =                   "メンバー情報を変更する場合は、該当する項目を変更し、下の更新ボタンを押してください。<br/>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<table width='98%' border='0' cellspacing='1' cellpadding='3' class='hyouyou_bordercolor honbun'>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor'>メンバーID</td><td class='hyouyou_bgcolor2' align='left' valign='middle'>　%CustomId%</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>お名前</td><td class='hyouyou_bgcolor2'align='left' valign='middle'>　%InfoName%</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>ニックネーム<br/><td class='hyouyou_bgcolor2'align='left' valign='middle'>　<input type='text' name='NickName' size='20' maxlength='20' value='%NickName%' class='form_text20'></td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>住所<br/><td class='hyouyou_bgcolor2'align='left' valign='middle'>　<input type='text' name='Address1' size='40' maxlength='40' value='%InfoAddress1%' class='form_text20'><br/>　<input type='text' name='Address2' size='40' maxlength='40' value='%InfoAddress2%' class='form_text20'></td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>電話番号<br/><td class='hyouyou_bgcolor2'align='left' valign='middle'>　<input type='text' name='Tel' size='40' maxlength='40' value='%InfoTel1%' class='form_text20'></td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>誕生日</td><td class='hyouyou_bgcolor2'align='left' valign='middle'>　%InfoBirthday1%</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>誕生日2</td><td class='hyouyou_bgcolor2'align='left' valign='middle'>　%InfoBirthday2%</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>記念日</td><td class='hyouyou_bgcolor2'align='left' valign='middle'>　%InfoMemorial1%</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>記念日2</td><td class='hyouyou_bgcolor2'align='left' valign='middle'>　%InfoMemorial2%</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>メールアドレス</td><td class='hyouyou_bgcolor2'align='left' valign='middle'>　%InfoMailAddress%</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>メンバーランク</td><td class='hyouyou_bgcolor2'align='left' valign='middle'>　%InfoRankName%</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>ご利用回数</td><td class='hyouyou_bgcolor2'align='left' valign='middle'>　%InfoUseCount%　回</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>ポイント</td><td class='hyouyou_bgcolor2'align='left' valign='middle'>　%InfoPoint%　P</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>本日のディスカウント</td><td class='hyouyou_bgcolor2'align='left' valign='middle'>　%InfoDiscount%</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>ランキング</td><td class='hyouyou_bgcolor2'align='left' valign='middle'>　<a href='memberranking.jsp?HotelId=＜ホテナビID＞'>ランキング表へ</a></td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>メールマガジン</td><td class='hyouyou_bgcolor2' align='left' valign='middle'>　%InfoMag%</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"</table><br/>\r\n";
}
//メンバー情報(PC）：メッセージ
    MsgReplace     [43][0] =                   "[ﾒﾝﾊﾞｰID:%CustomId%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[お名前:%InfoName%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[ﾆｯｸﾈｰﾑ:%NickName%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[住所:%InfoAddess1%,%InfoAddess2%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[電話番号:%InfoTel1%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[誕生日:%InfoBirthday1%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[誕生日2:%InfoBirthday2%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[記念日:%InfoMemorial1%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[記念日2:%InfoMemorial2%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[ﾒﾝﾊﾞｰﾗﾝｸ:%InfoRankName%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[ご利用回数:%InfoUseCount%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[総ご利用回数:%InfoUseCountAll%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[ﾎﾟｲﾝﾄ:%InfoPoint%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[最終来店日:%InfoLastDay%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[本日のﾃﾞｨｽｶｳﾝﾄ:%InfoDiscount%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[ﾒｰﾙｱﾄﾞﾚｽ:%InfoMailAddress%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[ﾒｰﾙﾏｶﾞｼﾞﾝ配信有無:%InfoMag%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[お車ﾅﾝﾊﾞｰ:%InfoCarNo%]";

//メンバー情報（携帯）：Default表示
    TitleDefault   [44]    =                   "ﾒﾝﾊﾞｰ情報の確認・変更を行います。";
    MsgTitleDefault[44][0] =                   "ﾒﾝﾊﾞｰ情報の確認・変更を行います。";
    MsgDefault     [44][0] =                   "ﾒﾝﾊﾞｰ情報を変更する場合は、該当する項目を変更し、下の更新ﾎﾞﾀﾝを押してください。<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[ﾒﾝﾊﾞｰID]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%CustomId%<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[お名前]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%InfoName%<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[ﾆｯｸﾈｰﾑ]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"<input type='text' name='NickName' size='20' maxlength='20' value='%NickName%'><br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[住所]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"<input type='text' name='Address1' size='40' maxlength='40' value='%InfoAddress1%'><br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"<input type='text' name='Address2' size='40' maxlength='40' value='%InfoAddress2%'><br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[電話番号]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"<input type='text' name='Tel' size='40' maxlength='40' value='%InfoTel1%'><br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[誕生日(ご本人)]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%InfoBirthday1%<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[誕生日(ﾊﾟｰﾄﾅｰ)]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%InfoBirthday2%<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[記念日]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%InfoMemorial1%<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[記念日2]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%InfoMemorial2%<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[ﾒﾝﾊﾞｰﾗﾝｸ]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%InfoRankName%<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[ご利用回数]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%InfoUseCount% 回<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[ﾎﾟｲﾝﾄ]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%InfoPoint% P<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[最終来店日]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%InfoLastDay%<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[本日のﾃﾞｨｽｶｳﾝﾄ]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%InfoDiscount%<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[ﾗﾝｷﾝｸﾞ]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"<a href='memberranking.jsp?HotelId=＜ホテナビID＞'>ﾗﾝｷﾝｸﾞ表へ</a><br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[ﾒｰﾙｱﾄﾞﾚｽ]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%InfoMailAddress%<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[ﾒｰﾙﾏｶﾞｼﾞﾝ]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%InfoMag%<br/>\r\n";

    MsgTitleDefault[44][1] =                   "入力内容を確認してください。";
    MsgDefault     [44][1] =                   "<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[ﾒﾝﾊﾞｰID]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%CustomId%<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[お名前]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoName%<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[ﾆｯｸﾈｰﾑ]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%NickName%<input type='hidden' name='NickName' value='%NickName%'><br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[住所]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoAddress1%<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoAddress2%<input type='hidden' name='Address1'  value='%InfoAddress1%'><input type='hidden' name='Address2'  value='%InfoAddress2%'><br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[電話番号]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoTel1%<input type='hidden' name='Tel'  value='%InfoTel1%'><br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[誕生日(ご本人)]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"<font color=red>%InfoBirthday1%</font><br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[誕生日(ﾊﾟｰﾄﾅｰ)]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoBirthday2%<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[記念日]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"<font color=red>%InfoMemorial1%</font>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[記念日2]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoMemorial2%<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[ﾒﾝﾊﾞｰﾗﾝｸ]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoRankName%<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[ご利用回数]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoUseCount%<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[ﾎﾟｲﾝﾄ]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoPoint%<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[最終来店日]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoLastDay%<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[本日のﾃﾞｨｽｶｳﾝﾄ]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoDiscount%<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[ﾒｰﾙｱﾄﾞﾚｽ]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoMailAddress%<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[ﾒｰﾙﾏｶﾞｼﾞﾝ]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoMag%<br/>\r\n";

//メンバー情報(携帯）：メッセージ
    MsgReplace     [44][0] =                   "[ﾒﾝﾊﾞｰID:%CustomId%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[お名前:%InfoName%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[ﾆｯｸﾈｰﾑ:%NickName%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[住所:%InfoAddess1%,%InfoAddess2%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[電話番号:%InfoTel1%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[誕生日:%InfoBirthday1%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[誕生日2:%InfoBirthday2%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[記念日:%InfoMemorial1%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[記念日2:%InfoMemorial2%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[ﾒﾝﾊﾞｰﾗﾝｸ:%InfoRankName%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[ご利用回数:%InfoUseCount%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[総ご利用回数:%InfoUseCountAll%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[ﾎﾟｲﾝﾄ:%InfoPoint%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[最終来店日:%InfoLastDay%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[本日のﾃﾞｨｽｶｳﾝﾄ:%InfoDiscount%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[ﾒｰﾙｱﾄﾞﾚｽ:%InfoMailAddress%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[ﾒｰﾙﾏｶﾞｼﾞﾝ配信有無:%InfoMag%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[お車ﾅﾝﾊﾞｰ:%InfoCarNo%]";

if ((start_date < trialDate || NewFlag) && trialDate != 99999999)
{
//利用履歴（PC）：新ホテナビDefault表示
    TitleDefault   [45]    =                   "ご利用履歴";
    MsgTitleDefault[45][0] =                   "ご利用履歴";
    MsgDefault     [45][0] =                   "<div class=\"item-table\">\r\n<dl><dt class=\"title\">ご来店回数</dt><dd >　%UseCount%回</dd></dl>\r\n<dl><dt class=\"title\">総ご来店回数</dt><dd >　%UseCountAll%回</dd></dl>\r\n<dl><dt class=\"title\">メンバーランク</dt><dd>　%InfoRankName%</dd></dl>\r\n<dl><dt class=\"title\">ディスカウント率</dt><dd >　%InfoDiscount% %</dd></dl>\r\n</div>\r\n";
    MsgTitleDefault[45][1] =                   "ご利用詳細";
    MsgDefault     [45][1] =                   "<table class=\"multi-column\">\r\n<thead><tr>\r\n<th>ご利用年月日</th>\r\n<th>ご利用部屋</th>\r\n<th>ご利用金額</th>\r\n<th>ご利用店舗</th>\r\n</tr></thead></table>\r\n";
    MsgTitleDefault[45][2] =                   "REPEAT";
    MsgDefault     [45][2] =                   "<tr>\r\n<td data-label=\"ご利用年月日\">%UseDate%</td>\r\n<td data-label=\"ご利用部屋\">%UseRoom%号室</td>\r\n<td data-label=\"ご利用金額\">%UseTotal%円</td><td data-label=\"ご利用店舗\">%UseStoreName%</td>\r\n</tr>\r\n";
}
else
{
//利用履歴（PC）：Default表示
    TitleDefault   [45]    =                   "ご利用履歴";
    MsgTitleDefault[45][0] =                   "ご利用履歴";
    MsgDefault     [45][0] =                   "<table width='98%' border='0' cellspacing='1' cellpadding='3' class='hyouyou_bordercolor honbun'>\r\n";
    MsgDefault     [45][0] = MsgDefault[45][0]+"<tr><td width='120' align='center' valign='middle' class='hyouyou_bgcolor'>ご来店回数</td><td class='hyouyou_bgcolor2' align='left' valign='middle'>　%UseCount%回</td></tr>\r\n";
    MsgDefault     [45][0] = MsgDefault[45][0]+"<tr><td align='center' valign='middle' class='hyouyou_bgcolor honbun'>メンバーランク</td><td class='hyouyou_bgcolor2' align='left' valign='middle'>　%InfoRankName%</td></tr>\r\n";
    MsgDefault     [45][0] = MsgDefault[45][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>ディスカウント率</rd><td class='hyouyou_bgcolor2' align='left' valign='middle'>　%InfoDiscount%</td></tr>\r\n";
    MsgDefault     [45][0] = MsgDefault[45][0]+"</table>\r\n";
    MsgTitleDefault[45][1] =                   "ご利用詳細";
    MsgDefault     [45][1] =                   "<table width='98%' border='0' cellspacing='1' cellpadding='3' class='hyouyou_bordercolor honbun'>\r\n";
    MsgDefault     [45][1] = MsgDefault[45][1]+"<tr><td align='center' valign='middle' class='hyouyou_bgcolor'>ご利用年月日</td><td align='center' valign='middle' class='hyouyou_bgcolor'>ご利用部屋</td><td align='center' valign='middle' class='hyouyou_bgcolor'>ご利用金額</td><td align='center' valign='middle' class='hyouyou_bgcolor'>ご利用店舗</td></tr>\r\n";
    MsgDefault     [45][1] = MsgDefault[45][1]+"</table>\r\n";
    MsgTitleDefault[45][2] =                   "REPEAT";
    MsgDefault     [45][2] =                   "<tr><td align='center' valign='middle' class='hyouyou_bgcolor2'>%UseDate%</td><td align='center' valign='middle' class='hyouyou_bgcolor2'>%UseRoom%号室</td><td align='center' valign='middle' class='hyouyou_bgcolor2'>%UseTotal%円</td><td align='center' valign='middle' class='hyouyou_bgcolor2'>%UseStoreName%</td></tr>\r\n";
}
//利用履歴(PC）：メッセージ
    MsgReplace     [45][0] =                   "[来店回数:%UseCount%]";
    MsgReplace     [45][0] = MsgReplace[45][0]+"[総来店回数:%UseCountAll%]";
    MsgReplace     [45][0] = MsgReplace[45][0]+"[ﾒﾝﾊﾞｰﾗﾝｸ:%InfoRankName%]";
    MsgReplace     [45][0] = MsgReplace[45][0]+"[ﾃﾞｨｽｶｳﾝﾄ率:%InfoDiscount%]";
    MsgReplace     [45][2] =                   "[利用日:%UseDate%]";
    MsgReplace     [45][2] = MsgReplace[45][1]+"[利用部屋:%UseRoom%]";
    MsgReplace     [45][2] = MsgReplace[45][1]+"[利用金額:%UseTotal%]";
    MsgReplace     [45][2] = MsgReplace[45][1]+"[利用店舗:%UseStoreName%]";

//利用履歴（携帯）：Default表示
    TitleDefault   [46]    =                   "ご利用履歴";
    MsgTitleDefault[46][0] =                   "ご利用履歴";
    MsgDefault     [46][0] =                   "[ご来店回数]<br/>\r\n";
    MsgDefault     [46][0] = MsgDefault[46][0]+"%UseCount%回<br/>\r\n";
    MsgDefault     [46][0] = MsgDefault[46][0]+"[ﾒﾝﾊﾞｰﾗﾝｸ]<br/>\r\n";
    MsgDefault     [46][0] = MsgDefault[46][0]+"%InfoRankName%<br/>\r\n";
    MsgDefault     [46][0] = MsgDefault[46][0]+"[ﾃﾞｨｽｶｳﾝﾄ率]<br/>\r\n";
    MsgDefault     [46][0] = MsgDefault[46][0]+"%InfoDiscount%<br/>\r\n";
    MsgTitleDefault[46][1] =                   "ご利用詳細";
    MsgDefault     [46][1] =                   "%UseDate% %UseRoom%号室\r\n";
    MsgDefault     [46][1] = MsgDefault[46][1]+"%UseTotal%円<br/>\r\n";
    MsgDefault     [46][1] = MsgDefault[46][1]+"%UseStoreName%<br/>\r\n";
//利用履歴(携帯）：メッセージ
    MsgReplace     [46][0] =                   "[来店回数:%UseCount%]";
    MsgReplace     [46][0] = MsgReplace[46][0]+"[総来店回数:%UseCountAll%]";
    MsgReplace     [46][0] = MsgReplace[46][0]+"[ﾒﾝﾊﾞｰﾗﾝｸ:%InfoRankName%]";
    MsgReplace     [46][0] = MsgReplace[46][0]+"[ﾃﾞｨｽｶｳﾝﾄ率:%InfoDiscount%]";
    MsgReplace     [46][1] =                   "[利用日:%UseDate%]";
    MsgReplace     [46][1] = MsgReplace[46][1]+"[利用部屋:%UseRoom%]";
    MsgReplace     [46][1] = MsgReplace[46][1]+"[利用金額:%UseTotal%]";
    MsgReplace     [46][1] = MsgReplace[46][1]+"[利用店舗:%UseStoreName%]";

if ((start_date < trialDate || NewFlag) && trialDate != 99999999)
{
//ポイント履歴（PC）：新ホテナビDefault表示
    TitleDefault   [47]    =                   "ポイント履歴";
    MsgTitleDefault[47][0] =                   "現在の有効ポイント";
    MsgDefault     [47][0] =                   "<div class=\"item-table\">\r\n<dl><dt class=\"title\">ポイント</dt><dd >　%Point% ポイント</dd></dl>\r\n</div>\r\n";
    MsgTitleDefault[47][1] =                   "過去のポイント履歴";
    MsgDefault     [47][1] =                   "<table class=\"multi-column\"><thead><tr>\r\n<th>ご利用年月日</th>\r\n<th>有効期限</th>\r\n<th>獲得ポイント</th>\r\n<th>ポイント内容</th>\r\n<th>ご利用店舗</th>\r\n</tr></thead></table>\r\n";
    MsgTitleDefault[47][2] =                   "REPEAT";
    MsgDefault     [47][2] =                   "<tr>\r\n<td data-label=\"ご利用年月日\">%PointDate%</td>\r\n<td data-label=\"有効期限\">%PointLimit%</td>\r\n<td data-label=\"獲得ポイント\">%PointCount%P</td>\r\n<td data-label=\"ポイント内容\">%PointName%</td>\r\n<td data-label=\"ご利用店舗\">%PointStoreName%</td>\r\n</tr>\r\n";
}
else
{
//ポイント履歴（PC）：旧ホテナビDefault表示
    TitleDefault   [47]    =                   "ポイント履歴";
    MsgTitleDefault[47][0] =                   "現在の有効ポイント";
    MsgDefault     [47][0] =                   "<table width='98%' border='0' cellspacing='1' cellpadding='3' class='hyouyou_bordercolor honbun'>\r\n";
    MsgDefault     [47][0] = MsgDefault[47][0]+"<tr><td width='120' align='center' valign='middle' class='hyouyou_bgcolor'>ポイント</td><td align='left' valign='middle' class='hyouyou_bgcolor2'>　%Point% ポイント</td></tr>\r\n";
    MsgDefault     [47][0] = MsgDefault[47][0]+"</table>\r\n";
    MsgTitleDefault[47][1] =                   "過去のポイント履歴";
    MsgDefault     [47][1] =                   "<table width='98%' border='0' cellspacing='1' cellpadding='3' class='hyouyou_bordercolor honbun'>\r\n";
    MsgDefault     [47][1] = MsgDefault[47][1]+"<tr><td nowrap align='center' valign='middle' class='hyouyou_bgcolor'>ご利用年月日</td><td nowrap align='center' valign='middle' class='hyouyou_bgcolor'>有効期限</td><td nowrap align='center' valign='middle' class='hyouyou_bgcolor' rowspan='2'>ご利用店舗</td></tr>\r\n";
    MsgDefault     [47][1] = MsgDefault[47][1]+"<tr><td nowrap align='center' valign='middle' class='hyouyou_bgcolor'>獲得ポイント</td><td nowrap align='center' valign='middle' class='hyouyou_bgcolor'>ポイント内容</td></tr>\r\n";
    MsgDefault     [47][1] = MsgDefault[47][1]+"</table>\r\n";
    MsgTitleDefault[47][2] =                   "REPEAT";
    MsgDefault     [47][2] =                   "<tr><td align='center' valign='middle' class='hyouyou_bgcolor2'>%PointDate%</td><td align='center' valign='middle' class='hyouyou_bgcolor2'>%PointLimit%</td><td align='center' valign='middle' class='hyouyou_bgcolor2' rowspan='2'>%PointStoreName%</td></tr>\r\n";
    MsgDefault     [47][2] = MsgDefault[47][2]+"<tr><td align='center' valign='middle' class='hyouyou_bgcolor2'>%PointCount% P</td><td align='center' valign='middle' class='hyouyou_bgcolor2'>%PointName%</td></tr>\r\n";
}
//ポイント履歴(PC）：メッセージ
    MsgReplace     [47][0] =                   "[有効ポイント:%Point%]";
    MsgReplace     [47][2] =                   "[利用日:%PointDate%]";
    MsgReplace     [47][2] = MsgReplace[47][1]+"[有効期限:%PointLimit%]";
    MsgReplace     [47][2] = MsgReplace[47][1]+"[獲得ポイント:%PointCount%]";
    MsgReplace     [47][2] = MsgReplace[47][1]+"[ポイント内容:%PointName%]";
    MsgReplace     [47][2] = MsgReplace[47][1]+"[利用店舗:%PointStoreName%]";
	
//ポイント履歴（携帯）：Default表示
    TitleDefault   [48]    =                   "ﾎﾟｲﾝﾄ";
    MsgTitleDefault[48][0] =                   "現在の有効ﾎﾟｲﾝﾄ";
    MsgDefault     [48][0] =                   "[有効ﾎﾟｲﾝﾄ数]<br/>\r\n";
    MsgDefault     [48][0] = MsgDefault[48][0]+"%Point% ﾎﾟｲﾝﾄ<br/>\r\n";
    MsgTitleDefault[48][1] =                   "過去のﾎﾟｲﾝﾄ履歴";
    MsgDefault     [48][1] =                   "%PointDate%<br/>\r\n";
    MsgDefault     [48][1] = MsgDefault[48][1]+"%PointLimit%<br/>\r\n";
    MsgDefault     [48][1] = MsgDefault[48][1]+"%PointCount% P<br/>\r\n";
    MsgDefault     [48][1] = MsgDefault[48][1]+"%PointName%<br/>\r\n";
    MsgDefault     [48][1] = MsgDefault[48][1]+"%PointStoreName%<br/>\r\n";
//ポイント履歴（携帯）：メッセージ
    MsgReplace     [48][0] =                   "[有効ﾎﾟｲﾝﾄ数:%Point%]";
    MsgReplace     [48][1] =                   "[獲得日付:%PointDate%]";
    MsgReplace     [48][1] = MsgReplace[48][1]+"[有効期限:%PointLimit%]";
    MsgReplace     [48][1] = MsgReplace[48][1]+"[ﾎﾟｲﾝﾄ:%PointCount%]";
    MsgReplace     [48][1] = MsgReplace[48][1]+"[ﾎﾟｲﾝﾄ名称:%PointName%]";
    MsgReplace     [48][1] = MsgReplace[48][1]+"[獲得店舗:%PointStoreName%]";

if ((start_date < trialDate || NewFlag) && trialDate != 99999999)
{
//ランキング（PC）：新ホテナビDefault表示
    TitleDefault   [53]    =                   "ランキング";
    MsgTitleDefault[53][0] =                   "【開催中】○○○○○杯";
    MsgDefault     [53][0] =                   "<h4 class=\"period\">開催期間：%RankingStart%～%RankingEnd%</h4>\r\n<h4 class=\"period-comment\">開催期間中にご来店いただいた回数によって競う、年に2回のBIGイベントです。</h4>\r\n<br/>\r\n";
    MsgTitleDefault[53][1] =                   "【開催中】○○○○○杯　ランキング明細";
    MsgDefault     [53][1] =                   "<div>\r\n	<table class=\"current container multi-column\">\r\n<thead>\r\n<tr>\r\n<th class=\"rank\">順位</th>\r\n<th class=\"times\">ご利用回数</th>\r\n<th class=\"money\">ご利用金額</th>\r\n<th class=\"point\">獲得ポイント</th>\r\n</tr>\r\n</thead>\r\n</table>\r\n</div>\r\n";
    MsgTitleDefault[53][2] =                   "REPEAT";
    MsgDefault     [53][2] =                   "<div>\r\n	<table class=\"holding multi-column\">\r\n		<tr class=\"first\" data-rank=\"%CssRankingNo%\">\r\n			<td data-label=\"順位\" class=\"first-rank\">%RankingNo%位</td>\r\n			<td data-label=\"ご利用回数\" class=\"first-times\">%RankingCount%回</td>\r\n<td data-label=\"ご利用金額\" class=\"first-money\">%RankingTotal%円</td>\r\n<td data-label=\"獲得ポイント\" class=\"first-point\">%RankingPoint%ポイント</td>\r\n</tr>\r\n</table>\r\n</div>\r\n";
    MsgTitleDefault[53][3] =                   "現在の順位と前後の順位";
    MsgDefault     [53][3] =                   "<div class=\"rank-comment\">\r\n	現在\r\n	<div class=\"color\">第%CurrentRank%位</div>\r\n	で、前の順位の方とは\r\n	<div class=\"color\">%CountDifference1%回差</div>\r\n	、後の順位の方とは\r\n	<div class=\"color\">%CountDifference2%回差</div>\r\n	です。\r\n</div>\r\n<div>\r\n	<table class=\"current multi-column\">\r\n		<tr class=\"first %dispNone%\" data-rank=\"%CssRankingOrderNo1%\">\r\n			<td data-label=\"順位\" class=\"first-rank\">%RankingOrderNo1%位</td>\r\n			<td data-label=\"ご利用回数\" class=\"first-times\">%RankingOrderCount1%回</td>\r\n			<td data-label=\"ご利用金額\" class=\"first-money\">%RankingOrderTotal1%円</td>\r\n			<td data-label=\"獲得ポイント\" class=\"first-point\">%RankingOrderPoint1%ポイント</td>\r\n		</tr>\r\n		<tr class=\"second member-rank\" data-rank=\"%CssRankingOrderNo0%\">\r\n			<td data-label=\"順位\" class=\"second-rank\">%RankingOrderNo0%位</td>\r\n			<td data-label=\"ご利用回数\" class=\"second-times\">%RankingOrderCount0%回</td>\r\n			<td data-label=\"ご利用金額\" class=\"second-money\">%RankingOrderTotal0%円</td>\r\n			<td data-label=\"獲得ポイント\" class=\"second-point\">%RankingOrderPoint0%ポイント</td>\r\n		</tr>\r\n		<tr class=\"third\" data-rank=\"%CssRankingOrderNo2%\">\r\n			<td data-label=\"順位\" class=\"third-rank\">%RankingOrderNo2%位</td>\r\n			<td data-label=\"ご利用回数\" class=\"third-times\">%RankingOrderCount2%回</td>\r\n			<td data-label=\"ご利用金額\" class=\"third-money\">%RankingOrderTotal2%円</td>\r\n			<td data-label=\"獲得ポイント\" class=\"third-point\">%RankingOrderPoint2%ポイント</td>\r\n		</tr>\r\n	</table>\r\n</div>\r\n";
}
else
{
//ランキング（PC）：旧Default表示
    TitleDefault   [53]    =                   "ランキング";
    MsgTitleDefault[53][0] =                   "ランキング";
    MsgDefault     [53][0] =                   "開催期間中ご来店いただいた回数によって競う、年に2回のBIGイベントです。<br/>入賞者には豪華商品をプレゼントいたします。<br/>開催期間：%RankingStart%～%RankingEnd%<br/>\r\n";
    MsgTitleDefault[53][1] =                   "ランキング詳細";
    MsgDefault     [53][1] =                   "<table width='98%' border='0' cellspacing='1' cellpadding='3' class='hyouyou_bordercolor honbun'>\r\n";
    MsgDefault     [53][1] = MsgDefault[53][1]+"<tr><td width='10%' nowrap align='center' valign='middle' class='hyouyou_bgcolor'>順位</td><td width='30%' nowrap align='center' valign='middle' class='hyouyou_bgcolor'>ポイント</td><td width='30%' nowrap align='center' valign='middle' class='hyouyou_bgcolor'>ご利用回数</td><td width='30%' nowrap align='center' valign='middle' class='hyouyou_bgcolor'>ご利用金額</td></tr>\r\n";
    MsgDefault     [53][1] = MsgDefault[53][1]+"</table>\r\n";
    MsgTitleDefault[53][2] =                   "REPEAT";
    MsgDefault     [53][2] =                   "<tr><td align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingNo%位</td><td align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingPoint%P</td><td align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingCount%回</td><td align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingTotal%円</td></tr>\r\n";
    MsgTitleDefault[53][3] =                   "現在の順位と前後の順位";
    MsgDefault     [53][3] =                   "%NickName%の順位\r\n";
    MsgDefault     [53][3] = MsgDefault[53][3]+"<table width='98%' border='0' cellspacing='1' cellpadding='3' class='hyouyou_bordercolor honbun'>\r\n";
    MsgDefault     [53][3] = MsgDefault[53][3]+"<tr><td width='10%' align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingOrderNo0%位</td><td width='30%' align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingOrderPoint0%P</td><td width='30%' align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingOrderCount0%回</td><td width='30%' align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingOrderTotal0%円</td></tr>\r\n";
    MsgDefault     [53][3] = MsgDefault[53][3]+"</table>\r\n";
    MsgDefault     [53][3] = MsgDefault[53][3]+"前後の順位\r\n";
    MsgDefault     [53][3] = MsgDefault[53][3]+"<table width='98%' border='0' cellspacing='1' cellpadding='3' class='hyouyou_bordercolor honbun'>\r\n";
    MsgDefault     [53][3] = MsgDefault[53][3]+"<tr><td width='10%' align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingOrderNo1%位</td><td width='30%' align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingOrderPoint1%P</td><td width='30%' align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingOrderCount1%回</td><td width='30%' align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingOrderTotal1%円</td></tr>\r\n";
    MsgDefault     [53][3] = MsgDefault[53][3]+"<tr><td align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingOrderNo2%位</td><td align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingOrderPoint2%P</td><td align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingOrderCount2%回</td><td align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingOrderTotal2%円</td></tr>\r\n";
    MsgDefault     [53][3] = MsgDefault[53][3]+"</table>\r\n";
}

//ランキング(PC）：メッセージ
    MsgReplace     [53][0] =                   "[開催期間:%RankingStart%～%RankingEnd%]";
    MsgReplace     [53][1] =                   "[順位:%RankingNo%]";
    MsgReplace     [53][1] = MsgReplace[53][1]+"[ポイント:%RankingPoint%]";
    MsgReplace     [53][1] = MsgReplace[53][1]+"[利用回数:%PointCount%]";
    MsgReplace     [53][1] = MsgReplace[53][1]+"[利用金額:%RankingTotal%]";

//ランキング（携帯）：Default表示
    TitleDefault   [54]    =                   "ﾗﾝｷﾝｸﾞ";
    MsgTitleDefault[54][0] =                   "ﾗﾝｷﾝｸﾞ";
    MsgDefault     [54][0] =                   "開催期間中ご来店いただいた回数によって競う、年に2回のBIGｲﾍﾞﾝﾄです。<br>\r\n";
    MsgDefault     [54][0] = MsgDefault[54][0]+"入賞者には豪華商品をﾌﾟﾚｾﾞﾝﾄいたします。<br>\r\n";
    MsgDefault     [54][0] = MsgDefault[54][0]+"開催期間<br>\r\n";
    MsgDefault     [54][0] = MsgDefault[54][0]+"%RankingStart%～%RankingEnd%<br/>\r\n";
    MsgTitleDefault[54][1] =                   "ﾗﾝｷﾝｸﾞ詳細";
    MsgDefault     [54][1] =                   "%RankingNo%位 %RankingPoint%P %RankingCount%回 %RankingTotal%円<br/>\r\n";
    MsgTitleDefault[54][2] =                   "順位と前後の順位";
    MsgDefault     [54][2] =                   "%NickName%の順位<br/>\r\n";
    MsgDefault     [54][2] = MsgDefault[54][2]+"%RankingOrderNo0%位 %RankingOrderPoint0%P %RankingOrderCount0%回 %RankingOrderTotal0%円<br/>\r\n";
    MsgDefault     [54][2] = MsgDefault[54][2]+"<br>前後の順位<br>\r\n";
    MsgDefault     [54][2] = MsgDefault[54][2]+"%RankingOrderNo1%位 %RankingOrderPoint1%P %RankingOrderCount1%回 %RankingOrderTotal1%円<br/>\r\n";
    MsgDefault     [54][2] = MsgDefault[54][2]+"%RankingOrderNo2%位 %RankingOrderPoint2%P %RankingOrderCount2%回 %RankingOrderTotal2%円<br/>\r\n";
//ランキング（携帯）：メッセージ
    MsgReplace     [54][0] =                   "[開催期間:%RankingStart%～%RankingEnd%]";
    MsgReplace     [54][1] =                   "[順位:%RankingNo%][ﾎﾟｲﾝﾄ:%RankingPoint%][回数:%RankingCount%][金額:%RankingTotal%]";
    MsgReplace     [54][2] =                   "[ﾆｯｸﾈｰﾑ:%NickName%]\r\n";
    MsgReplace     [54][2] = MsgReplace[54][2]+"[自分の順位:%RankingOrderNo0%][ﾎﾟｲﾝﾄ:%RankingOrderPoint0%][回数:%RankingOrderCount0%][金額:%RankingOrderTotal0%]";
    MsgReplace     [54][2] = MsgReplace[54][2]+"[前の順位:%RankingOrderNo1%][ﾎﾟｲﾝﾄ:%RankingOrderPoint1%][回数:%RankingOrderCount1%][金額:%RankingOrderTotal1%]";
    MsgReplace     [54][2] = MsgReplace[54][2]+"[後の順位:%RankingOrderNo2%][ﾎﾟｲﾝﾄ:%RankingOrderPoint2%][回数:%RankingOrderCount2%][金額:%RankingOrderTotal2%]";

//メンバー登録(QR)（携帯）：Default表示
//  入力用（1：固定）
    TitleDefault   [56]    =                   "ﾒﾝﾊﾞｰ登録";
    MsgTitleDefault[56][0] =                   "ﾎﾃﾙからお得な情報をお届けします";
    MsgDefault     [56][0] =                   "[ﾆｯｸﾈｰﾑ]<br>\r\n";
    MsgDefault     [56][0] = MsgDefault[56][0]+"<input type='text' name='NickName' size='20' maxlength='20' value='%NickName%' istyle='1'><br/>\r\n";
    MsgDefault     [56][0] = MsgDefault[56][0]+"[誕生日(ご本人)]<br>\r\n";
    MsgDefault     [56][0] = MsgDefault[56][0]+"<input type='text' name='Birthday' size='8' maxlength='8' value='%Birthday%' istyle='4'><font size=1 color='#FF0000'>（*必須）</font><br><font color=orange size=1>入力例:19910101</font><br>\r\n";
    MsgDefault     [56][0] = MsgDefault[56][0]+"[誕生日(ﾊﾟｰﾄﾅｰ)]<br>\r\n";
    MsgDefault     [56][0] = MsgDefault[56][0]+"<input type='text' name='Birthday2' size='8' maxlength='8' value='%Birthday2%' istyle='4'><br><font color=orange size=1>入力例:19910101</font><br>\r\n";
    MsgDefault     [56][0] = MsgDefault[56][0]+"[記念日]<br>\r\n";
    MsgDefault     [56][0] = MsgDefault[56][0]+"<input type='text' name='Memorial' size='8' maxlength='8' value='%Memorial%' istyle='4'><br><font color=orange size=1>入力例:19910101 OR 0101</font><br>\r\n";
    MsgDefault     [56][0] = MsgDefault[56][0]+"[ﾒ-ﾙﾏｶﾞｼﾞﾝ]<br>\r\n";
    MsgDefault     [56][0] = MsgDefault[56][0]+"<input name='status' type='checkbox' value='1' %MailStatus% >希望する<br/>\r\n";
    MsgDefault     [56][0] = MsgDefault[56][0]+"<input type='submit' value='登録する'><br>\r\n";
//  確認用（2:固定）
    MsgTitleDefault[56][1] =                   "入力内容をご確認ください";
    MsgDefault     [56][1] =                   "[ﾆｯｸﾈｰﾑ]<br>\r\n";
    MsgDefault     [56][1] = MsgDefault[56][1]+"%NickName%<br><input type='hidden' name='NickName' value='%NickName%'><br/>\r\n";
    MsgDefault     [56][1] = MsgDefault[56][1]+"[誕生日(ご本人)]<br>\r\n";
    MsgDefault     [56][1] = MsgDefault[56][1]+"%Birthday%<br><input type='hidden' name='Birthday' value='%Birthday%'><br/>\r\n";
    MsgDefault     [56][1] = MsgDefault[56][1]+"[誕生日(ﾊﾟｰﾄﾅｰ)]<br>\r\n";
    MsgDefault     [56][1] = MsgDefault[56][1]+"%Birthday%<br><input type='hidden' name='Birthday2' value='%Birthday2%'><br/>\r\n";
    MsgDefault     [56][1] = MsgDefault[56][1]+"[記念日]<br>\r\n";
    MsgDefault     [56][1] = MsgDefault[56][1]+"%Memorial%<br><input type='hidden' name='Memorial' value='%Memorial%'><br/>\r\n";
    MsgDefault     [56][1] = MsgDefault[56][1]+"[ﾒ-ﾙﾏｶﾞｼﾞﾝ]<br>\r\n";
    MsgDefault     [56][1] = MsgDefault[56][1]+"%MailStatusText%<br>\r\n";
    MsgDefault     [56][1] = MsgDefault[56][1]+"<input type='submit' value='登録する'><br>\r\n";
//メンバー登録(QR)（携帯）：メッセージ
    MsgReplace     [56][0] =                   "<strong>入力用（入力箇所⇒1固定）</strong><br>[ﾆｯｸﾈｰﾑ:%NickName%]<br/>";
    MsgReplace     [56][0] = MsgReplace[56][0]+"[誕生日:%Birthday%][誕生日2:%Birthday2%]<br/>";
    MsgReplace     [56][0] = MsgReplace[56][0]+"[記念日:%Memorial%][記念日2:%Memorial2%]<br/>";
    MsgReplace     [56][0] = MsgReplace[56][0]+"[メルマガ希望:%MailStatus%]<br/>";
    MsgReplace     [56][1] =                   "<strong>確認用（入力箇所⇒2固定）</strong><br>[ﾆｯｸﾈｰﾑ:%NickName%]<br/>";
    MsgReplace     [56][1] = MsgReplace[56][1]+"[誕生日:%Birthday%][誕生日2:%Birthday2%]<br/>";
    MsgReplace     [56][1] = MsgReplace[56][1]+"[記念日:%Memorial%][記念日2:%Memorial2%]<br/>";
    MsgReplace     [56][1] = MsgReplace[56][1]+"[メルマガ希望:%MailStatusText%]<br/>";
    MsgReplace     [56][2] =                   "<strong>3以降は使用しません</strong>";
%>
