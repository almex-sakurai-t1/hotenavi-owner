<%@ page contentType="text/html; charset=Windows-31J" %><%
    String[]     TitleDefault      = new String[300];
    String[][]   MsgTitleDefault   = new String[300][8];
    String[][]   MsgDefault        = new String[300][8];
    String[][]   MsgReplace        = new String[300][8];


//�N���W�b�g�J�[�h�iPC�j�FDefault�\��
    TitleDefault   [7]    =                   "�����p�\�ȃN���W�b�g�J�[�h";
    MsgTitleDefault[7][0] =                   "�����p�\�ȃN���W�b�g�J�[�h";
    MsgDefault     [7][0] =                   "���z�e���ł̓N���W�b�g�J�[�h�������p�ɂȂ�܂��B<br><table bgcolor='#ffffff'><tr><td><img src='/common/image/visa.gif'> <img src='/common/image/master.gif'> <img src='/common/image/jcb.gif'> <img src='/common/image/amex.gif'> <img src='/common/image/dc.gif'> <img src='/common/image/nicos.gif'> <img src='/common/image/diners.gif'> <img src='/common/image/uc.gif'> <img src='/common/image/cf.gif'> <img src='/common/image/debit.gif'> <img src='/common/image/mufg.gif'> <img src='/common/image/orico.gif'> <img src='/common/image/saison.gif'> <img src='/common/image/ufj.gif'> <img src='/common/image/discover.gif'></td></tr></table><br/>\r\n";
    MsgDefault     [7][0] = MsgDefault[7][0]+"����������Ж��͊�����Ё������ƂȂ�܂��B<br/>\r\n";

//�N���W�b�g�J�[�h(PC�j�F���b�Z�[�W
    MsgReplace     [7][0] =                   "[VISA:visa.gif][MASTER:master.gif][JCB:jcb.gif][AMEX:amex.gif][DC:dc.gif][NICOS:nicos.gif][DINERS:diners.gif][UC:uc.gif][CF:cf.gif][DEBIT:debit.gif][MUFG:mufg.gif][ORICO:orico.gif][SAISON:saison.gif][UFJ:ufj.gif][UFJ:discover.gif]";

//�N���W�b�g�J�[�h�i�g�сj�FDefault�\��
    TitleDefault   [8]    =                   "�����p�\�ȸڼޯĶ���";
    MsgTitleDefault[8][0] =                   "�����p�\�ȸڼޯĶ���";
    MsgDefault     [8][0] =                   "����قł͸ڼޯĶ��ނ������p�ɂȂ�܂��B<br><img src='/common/image/visa.gif'> <img src='/common/image/master.gif'> <img src='/common/image/jcb.gif'> <img src='/common/image/amex.gif'> <img src='/common/image/dc.gif'> <img src='/common/image/nicos.gif'> <img src='/common/image/diners.gif'> <img src='/common/image/uc.gif'> <img src='/common/image/cf.gif'> <img src='/common/image/debit.gif'> <img src='/common/image/mufg.gif'> <img src='/common/image/orico.gif'> <img src='/common/image/saison.gif'> <img src='/common/image/ufj.gif'> <img src='/common/image/discover.gif'><br/>\r\n";
    MsgDefault     [8][0] = MsgDefault[8][0]+"����������Ж��͊�����Ё������ƂȂ�܂��B<br/>\r\n";

//�N���W�b�g�J�[�h(�g�сj�F���b�Z�[�W
    MsgReplace     [8][0] =                   "[VISA:visa.gif][MASTER:master.gif][JCB:jcb.gif][AMEX:amex.gif][DC:dc.gif][NICOS:nicos.gif][DINERS:diners.gif][UC:uc.gif][CF:cf.gif][DEBIT:debit.gif][MUFG:mufg.gif][ORICO:orico.gif][SAISON:saison.gif][UFJ:ufj.gif][UFJ:discover.gif]";

if ((start_date < trialDate || NewFlag) && trialDate != 99999999 )
{
//�����o�[���iPC�j�F�V�z�e�i�rDefault�\��
    TitleDefault   [43]    =                   "�����o�[���";
    MsgTitleDefault[43][0] =                   "�����o�[���̊m�F�E�ύX�o�^���s���܂�";
    MsgDefault     [43][0] =                   "<div>�����o�[����ύX����ꍇ�́A�Y�����鍀�ڂ�ύX���A���̍X�V�{�^���������Ă��������B</div>";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<table class=\"multi-column\"><thead>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>�����o�[ID</th>\r\n<td>%CustomId%</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>�����O</th>\r\n<td>%InfoName%</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>�j�b�N�l�[��</th>\r\n<td>\r\n<input type=\"text\" name=\"nickName\" id=\"nickName\" value=\"%NickName%\" />\r\n</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>�Z��</th>\r\n<td>\r\n<input type=\"text\" name=\"infoAddress1\" id=\"infoAddress1\" value=\"%InfoAddress1%\" />\r\n<br />\r\n<input type=\"text\" name=\"infoAddress2\" id=\"infoAddress2\" value=\"%InfoAddress2%\"/>\r\n</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>�d�b�ԍ�</th>\r\n<td>\r\n<input type=\"text\" style=\"width:100px\" name=\"infoTel1\" id=\"infoTel1\" value=\"%InfoTel1%\" />\r\n</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>�a����</th>\r\n<td>\r\n%InfoBirthday1%\r\n</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>�a�����Q</th>\r\n<td>\r\n%InfoBirthday2%\r\n</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>�L�O��</th>\r\n<td>\r\n%InfoMemorial1%\r\n</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>�L�O���Q</th>\r\n<td>\r\n%InfoMemorial2%\r\n</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>���[���A�h���X</th>\r\n<td>%InfoMailAddress%</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>�����o�[�����N</th>\r\n<td>%InfoRankName%</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>�����p��</th>\r\n<td>%InfoUseCountAll%�@��</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>�����p��</th>\r\n<td>%InfoUseCount%�@��</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>�|�C���g</th>\r\n<td>%InfoPoint%�@P</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>�{���̃f�B�X�J�E���g</th>\r\n<td>%InfoDiscount%</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>�����L���O</th>\r\n<td><a href='/%HotelId%/member/ranking'>�����L���O�\��</a></td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr>\r\n<th>���[���}�K�W��</th>\r\n<td>%InfoMag%</td>\r\n</tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"</thead>\r\n</table>\r\n";
}
else
{
//�����o�[���iPC�j�F���z�e�i�rDefault�\��
    TitleDefault   [43]    =                   "�����o�[���";
    MsgTitleDefault[43][0] =                   "�����o�[���̊m�F�E�ύX�o�^���s���܂�";
    MsgDefault     [43][0] =                   "�����o�[����ύX����ꍇ�́A�Y�����鍀�ڂ�ύX���A���̍X�V�{�^���������Ă��������B<br/>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<table width='98%' border='0' cellspacing='1' cellpadding='3' class='hyouyou_bordercolor honbun'>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor'>�����o�[ID</td><td class='hyouyou_bgcolor2' align='left' valign='middle'>�@%CustomId%</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>�����O</td><td class='hyouyou_bgcolor2'align='left' valign='middle'>�@%InfoName%</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>�j�b�N�l�[��<br/><td class='hyouyou_bgcolor2'align='left' valign='middle'>�@<input type='text' name='NickName' size='20' maxlength='20' value='%NickName%' class='form_text20'></td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>�Z��<br/><td class='hyouyou_bgcolor2'align='left' valign='middle'>�@<input type='text' name='Address1' size='40' maxlength='40' value='%InfoAddress1%' class='form_text20'><br/>�@<input type='text' name='Address2' size='40' maxlength='40' value='%InfoAddress2%' class='form_text20'></td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>�d�b�ԍ�<br/><td class='hyouyou_bgcolor2'align='left' valign='middle'>�@<input type='text' name='Tel' size='40' maxlength='40' value='%InfoTel1%' class='form_text20'></td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>�a����</td><td class='hyouyou_bgcolor2'align='left' valign='middle'>�@%InfoBirthday1%</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>�a����2</td><td class='hyouyou_bgcolor2'align='left' valign='middle'>�@%InfoBirthday2%</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>�L�O��</td><td class='hyouyou_bgcolor2'align='left' valign='middle'>�@%InfoMemorial1%</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>�L�O��2</td><td class='hyouyou_bgcolor2'align='left' valign='middle'>�@%InfoMemorial2%</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>���[���A�h���X</td><td class='hyouyou_bgcolor2'align='left' valign='middle'>�@%InfoMailAddress%</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>�����o�[�����N</td><td class='hyouyou_bgcolor2'align='left' valign='middle'>�@%InfoRankName%</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>�����p��</td><td class='hyouyou_bgcolor2'align='left' valign='middle'>�@%InfoUseCount%�@��</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>�|�C���g</td><td class='hyouyou_bgcolor2'align='left' valign='middle'>�@%InfoPoint%�@P</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>�{���̃f�B�X�J�E���g</td><td class='hyouyou_bgcolor2'align='left' valign='middle'>�@%InfoDiscount%</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>�����L���O</td><td class='hyouyou_bgcolor2'align='left' valign='middle'>�@<a href='memberranking.jsp?HotelId=���z�e�i�rID��'>�����L���O�\��</a></td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>���[���}�K�W��</td><td class='hyouyou_bgcolor2' align='left' valign='middle'>�@%InfoMag%</td></tr>\r\n";
    MsgDefault     [43][0] = MsgDefault[43][0]+"</table><br/>\r\n";
}
//�����o�[���(PC�j�F���b�Z�[�W
    MsgReplace     [43][0] =                   "[���ްID:%CustomId%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[�����O:%InfoName%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[Ư�Ȱ�:%NickName%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[�Z��:%InfoAddess1%,%InfoAddess2%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[�d�b�ԍ�:%InfoTel1%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[�a����:%InfoBirthday1%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[�a����2:%InfoBirthday2%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[�L�O��:%InfoMemorial1%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[�L�O��2:%InfoMemorial2%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[���ް�ݸ:%InfoRankName%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[�����p��:%InfoUseCount%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[�������p��:%InfoUseCountAll%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[�߲��:%InfoPoint%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[�ŏI���X��:%InfoLastDay%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[�{�����ި�����:%InfoDiscount%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[Ұٱ��ڽ:%InfoMailAddress%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[Ұ�϶޼�ݔz�M�L��:%InfoMag%]";
    MsgReplace     [43][0] = MsgReplace[43][0]+"[�������ް:%InfoCarNo%]";

//�����o�[���i�g�сj�FDefault�\��
    TitleDefault   [44]    =                   "���ް���̊m�F�E�ύX���s���܂��B";
    MsgTitleDefault[44][0] =                   "���ް���̊m�F�E�ύX���s���܂��B";
    MsgDefault     [44][0] =                   "���ް����ύX����ꍇ�́A�Y�����鍀�ڂ�ύX���A���̍X�V���݂������Ă��������B<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[���ްID]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%CustomId%<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[�����O]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%InfoName%<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[Ư�Ȱ�]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"<input type='text' name='NickName' size='20' maxlength='20' value='%NickName%'><br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[�Z��]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"<input type='text' name='Address1' size='40' maxlength='40' value='%InfoAddress1%'><br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"<input type='text' name='Address2' size='40' maxlength='40' value='%InfoAddress2%'><br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[�d�b�ԍ�]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"<input type='text' name='Tel' size='40' maxlength='40' value='%InfoTel1%'><br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[�a����(���{�l)]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%InfoBirthday1%<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[�a����(�߰�Ű)]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%InfoBirthday2%<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[�L�O��]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%InfoMemorial1%<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[�L�O��2]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%InfoMemorial2%<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[���ް�ݸ]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%InfoRankName%<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[�����p��]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%InfoUseCount% ��<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[�߲��]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%InfoPoint% P<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[�ŏI���X��]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%InfoLastDay%<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[�{�����ި�����]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%InfoDiscount%<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[�ݷݸ�]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"<a href='memberranking.jsp?HotelId=���z�e�i�rID��'>�ݷݸޕ\��</a><br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[Ұٱ��ڽ]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%InfoMailAddress%<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"[Ұ�϶޼��]<br/>\r\n";
    MsgDefault     [44][0] = MsgDefault[44][0]+"%InfoMag%<br/>\r\n";

    MsgTitleDefault[44][1] =                   "���͓��e���m�F���Ă��������B";
    MsgDefault     [44][1] =                   "<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[���ްID]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%CustomId%<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[�����O]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoName%<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[Ư�Ȱ�]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%NickName%<input type='hidden' name='NickName' value='%NickName%'><br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[�Z��]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoAddress1%<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoAddress2%<input type='hidden' name='Address1'  value='%InfoAddress1%'><input type='hidden' name='Address2'  value='%InfoAddress2%'><br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[�d�b�ԍ�]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoTel1%<input type='hidden' name='Tel'  value='%InfoTel1%'><br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[�a����(���{�l)]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"<font color=red>%InfoBirthday1%</font><br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[�a����(�߰�Ű)]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoBirthday2%<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[�L�O��]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"<font color=red>%InfoMemorial1%</font>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[�L�O��2]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoMemorial2%<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[���ް�ݸ]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoRankName%<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[�����p��]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoUseCount%<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[�߲��]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoPoint%<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[�ŏI���X��]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoLastDay%<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[�{�����ި�����]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoDiscount%<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[Ұٱ��ڽ]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoMailAddress%<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"[Ұ�϶޼��]<br/>\r\n";
    MsgDefault     [44][1] = MsgDefault[44][1]+"%InfoMag%<br/>\r\n";

//�����o�[���(�g�сj�F���b�Z�[�W
    MsgReplace     [44][0] =                   "[���ްID:%CustomId%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[�����O:%InfoName%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[Ư�Ȱ�:%NickName%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[�Z��:%InfoAddess1%,%InfoAddess2%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[�d�b�ԍ�:%InfoTel1%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[�a����:%InfoBirthday1%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[�a����2:%InfoBirthday2%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[�L�O��:%InfoMemorial1%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[�L�O��2:%InfoMemorial2%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[���ް�ݸ:%InfoRankName%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[�����p��:%InfoUseCount%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[�������p��:%InfoUseCountAll%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[�߲��:%InfoPoint%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[�ŏI���X��:%InfoLastDay%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[�{�����ި�����:%InfoDiscount%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[Ұٱ��ڽ:%InfoMailAddress%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[Ұ�϶޼�ݔz�M�L��:%InfoMag%]";
    MsgReplace     [44][0] = MsgReplace[44][0]+"[�������ް:%InfoCarNo%]";

if ((start_date < trialDate || NewFlag) && trialDate != 99999999)
{
//���p�����iPC�j�F�V�z�e�i�rDefault�\��
    TitleDefault   [45]    =                   "�����p����";
    MsgTitleDefault[45][0] =                   "�����p����";
    MsgDefault     [45][0] =                   "<div class=\"item-table\">\r\n<dl><dt class=\"title\">�����X��</dt><dd >�@%UseCount%��</dd></dl>\r\n<dl><dt class=\"title\">�������X��</dt><dd >�@%UseCountAll%��</dd></dl>\r\n<dl><dt class=\"title\">�����o�[�����N</dt><dd>�@%InfoRankName%</dd></dl>\r\n<dl><dt class=\"title\">�f�B�X�J�E���g��</dt><dd >�@%InfoDiscount% %</dd></dl>\r\n</div>\r\n";
    MsgTitleDefault[45][1] =                   "�����p�ڍ�";
    MsgDefault     [45][1] =                   "<table class=\"multi-column\">\r\n<thead><tr>\r\n<th>�����p�N����</th>\r\n<th>�����p����</th>\r\n<th>�����p���z</th>\r\n<th>�����p�X��</th>\r\n</tr></thead></table>\r\n";
    MsgTitleDefault[45][2] =                   "REPEAT";
    MsgDefault     [45][2] =                   "<tr>\r\n<td data-label=\"�����p�N����\">%UseDate%</td>\r\n<td data-label=\"�����p����\">%UseRoom%����</td>\r\n<td data-label=\"�����p���z\">%UseTotal%�~</td><td data-label=\"�����p�X��\">%UseStoreName%</td>\r\n</tr>\r\n";
}
else
{
//���p�����iPC�j�FDefault�\��
    TitleDefault   [45]    =                   "�����p����";
    MsgTitleDefault[45][0] =                   "�����p����";
    MsgDefault     [45][0] =                   "<table width='98%' border='0' cellspacing='1' cellpadding='3' class='hyouyou_bordercolor honbun'>\r\n";
    MsgDefault     [45][0] = MsgDefault[45][0]+"<tr><td width='120' align='center' valign='middle' class='hyouyou_bgcolor'>�����X��</td><td class='hyouyou_bgcolor2' align='left' valign='middle'>�@%UseCount%��</td></tr>\r\n";
    MsgDefault     [45][0] = MsgDefault[45][0]+"<tr><td align='center' valign='middle' class='hyouyou_bgcolor honbun'>�����o�[�����N</td><td class='hyouyou_bgcolor2' align='left' valign='middle'>�@%InfoRankName%</td></tr>\r\n";
    MsgDefault     [45][0] = MsgDefault[45][0]+"<tr><td width='140' align='center' valign='middle' class='hyouyou_bgcolor honbun'>�f�B�X�J�E���g��</rd><td class='hyouyou_bgcolor2' align='left' valign='middle'>�@%InfoDiscount%</td></tr>\r\n";
    MsgDefault     [45][0] = MsgDefault[45][0]+"</table>\r\n";
    MsgTitleDefault[45][1] =                   "�����p�ڍ�";
    MsgDefault     [45][1] =                   "<table width='98%' border='0' cellspacing='1' cellpadding='3' class='hyouyou_bordercolor honbun'>\r\n";
    MsgDefault     [45][1] = MsgDefault[45][1]+"<tr><td align='center' valign='middle' class='hyouyou_bgcolor'>�����p�N����</td><td align='center' valign='middle' class='hyouyou_bgcolor'>�����p����</td><td align='center' valign='middle' class='hyouyou_bgcolor'>�����p���z</td><td align='center' valign='middle' class='hyouyou_bgcolor'>�����p�X��</td></tr>\r\n";
    MsgDefault     [45][1] = MsgDefault[45][1]+"</table>\r\n";
    MsgTitleDefault[45][2] =                   "REPEAT";
    MsgDefault     [45][2] =                   "<tr><td align='center' valign='middle' class='hyouyou_bgcolor2'>%UseDate%</td><td align='center' valign='middle' class='hyouyou_bgcolor2'>%UseRoom%����</td><td align='center' valign='middle' class='hyouyou_bgcolor2'>%UseTotal%�~</td><td align='center' valign='middle' class='hyouyou_bgcolor2'>%UseStoreName%</td></tr>\r\n";
}
//���p����(PC�j�F���b�Z�[�W
    MsgReplace     [45][0] =                   "[���X��:%UseCount%]";
    MsgReplace     [45][0] = MsgReplace[45][0]+"[�����X��:%UseCountAll%]";
    MsgReplace     [45][0] = MsgReplace[45][0]+"[���ް�ݸ:%InfoRankName%]";
    MsgReplace     [45][0] = MsgReplace[45][0]+"[�ި����ė�:%InfoDiscount%]";
    MsgReplace     [45][2] =                   "[���p��:%UseDate%]";
    MsgReplace     [45][2] = MsgReplace[45][1]+"[���p����:%UseRoom%]";
    MsgReplace     [45][2] = MsgReplace[45][1]+"[���p���z:%UseTotal%]";
    MsgReplace     [45][2] = MsgReplace[45][1]+"[���p�X��:%UseStoreName%]";

//���p�����i�g�сj�FDefault�\��
    TitleDefault   [46]    =                   "�����p����";
    MsgTitleDefault[46][0] =                   "�����p����";
    MsgDefault     [46][0] =                   "[�����X��]<br/>\r\n";
    MsgDefault     [46][0] = MsgDefault[46][0]+"%UseCount%��<br/>\r\n";
    MsgDefault     [46][0] = MsgDefault[46][0]+"[���ް�ݸ]<br/>\r\n";
    MsgDefault     [46][0] = MsgDefault[46][0]+"%InfoRankName%<br/>\r\n";
    MsgDefault     [46][0] = MsgDefault[46][0]+"[�ި����ė�]<br/>\r\n";
    MsgDefault     [46][0] = MsgDefault[46][0]+"%InfoDiscount%<br/>\r\n";
    MsgTitleDefault[46][1] =                   "�����p�ڍ�";
    MsgDefault     [46][1] =                   "%UseDate% %UseRoom%����\r\n";
    MsgDefault     [46][1] = MsgDefault[46][1]+"%UseTotal%�~<br/>\r\n";
    MsgDefault     [46][1] = MsgDefault[46][1]+"%UseStoreName%<br/>\r\n";
//���p����(�g�сj�F���b�Z�[�W
    MsgReplace     [46][0] =                   "[���X��:%UseCount%]";
    MsgReplace     [46][0] = MsgReplace[46][0]+"[�����X��:%UseCountAll%]";
    MsgReplace     [46][0] = MsgReplace[46][0]+"[���ް�ݸ:%InfoRankName%]";
    MsgReplace     [46][0] = MsgReplace[46][0]+"[�ި����ė�:%InfoDiscount%]";
    MsgReplace     [46][1] =                   "[���p��:%UseDate%]";
    MsgReplace     [46][1] = MsgReplace[46][1]+"[���p����:%UseRoom%]";
    MsgReplace     [46][1] = MsgReplace[46][1]+"[���p���z:%UseTotal%]";
    MsgReplace     [46][1] = MsgReplace[46][1]+"[���p�X��:%UseStoreName%]";

if ((start_date < trialDate || NewFlag) && trialDate != 99999999)
{
//�|�C���g�����iPC�j�F�V�z�e�i�rDefault�\��
    TitleDefault   [47]    =                   "�|�C���g����";
    MsgTitleDefault[47][0] =                   "���݂̗L���|�C���g";
    MsgDefault     [47][0] =                   "<div class=\"item-table\">\r\n<dl><dt class=\"title\">�|�C���g</dt><dd >�@%Point% �|�C���g</dd></dl>\r\n</div>\r\n";
    MsgTitleDefault[47][1] =                   "�ߋ��̃|�C���g����";
    MsgDefault     [47][1] =                   "<table class=\"multi-column\"><thead><tr>\r\n<th>�����p�N����</th>\r\n<th>�L������</th>\r\n<th>�l���|�C���g</th>\r\n<th>�|�C���g���e</th>\r\n<th>�����p�X��</th>\r\n</tr></thead></table>\r\n";
    MsgTitleDefault[47][2] =                   "REPEAT";
    MsgDefault     [47][2] =                   "<tr>\r\n<td data-label=\"�����p�N����\">%PointDate%</td>\r\n<td data-label=\"�L������\">%PointLimit%</td>\r\n<td data-label=\"�l���|�C���g\">%PointCount%P</td>\r\n<td data-label=\"�|�C���g���e\">%PointName%</td>\r\n<td data-label=\"�����p�X��\">%PointStoreName%</td>\r\n</tr>\r\n";
}
else
{
//�|�C���g�����iPC�j�F���z�e�i�rDefault�\��
    TitleDefault   [47]    =                   "�|�C���g����";
    MsgTitleDefault[47][0] =                   "���݂̗L���|�C���g";
    MsgDefault     [47][0] =                   "<table width='98%' border='0' cellspacing='1' cellpadding='3' class='hyouyou_bordercolor honbun'>\r\n";
    MsgDefault     [47][0] = MsgDefault[47][0]+"<tr><td width='120' align='center' valign='middle' class='hyouyou_bgcolor'>�|�C���g</td><td align='left' valign='middle' class='hyouyou_bgcolor2'>�@%Point% �|�C���g</td></tr>\r\n";
    MsgDefault     [47][0] = MsgDefault[47][0]+"</table>\r\n";
    MsgTitleDefault[47][1] =                   "�ߋ��̃|�C���g����";
    MsgDefault     [47][1] =                   "<table width='98%' border='0' cellspacing='1' cellpadding='3' class='hyouyou_bordercolor honbun'>\r\n";
    MsgDefault     [47][1] = MsgDefault[47][1]+"<tr><td nowrap align='center' valign='middle' class='hyouyou_bgcolor'>�����p�N����</td><td nowrap align='center' valign='middle' class='hyouyou_bgcolor'>�L������</td><td nowrap align='center' valign='middle' class='hyouyou_bgcolor' rowspan='2'>�����p�X��</td></tr>\r\n";
    MsgDefault     [47][1] = MsgDefault[47][1]+"<tr><td nowrap align='center' valign='middle' class='hyouyou_bgcolor'>�l���|�C���g</td><td nowrap align='center' valign='middle' class='hyouyou_bgcolor'>�|�C���g���e</td></tr>\r\n";
    MsgDefault     [47][1] = MsgDefault[47][1]+"</table>\r\n";
    MsgTitleDefault[47][2] =                   "REPEAT";
    MsgDefault     [47][2] =                   "<tr><td align='center' valign='middle' class='hyouyou_bgcolor2'>%PointDate%</td><td align='center' valign='middle' class='hyouyou_bgcolor2'>%PointLimit%</td><td align='center' valign='middle' class='hyouyou_bgcolor2' rowspan='2'>%PointStoreName%</td></tr>\r\n";
    MsgDefault     [47][2] = MsgDefault[47][2]+"<tr><td align='center' valign='middle' class='hyouyou_bgcolor2'>%PointCount% P</td><td align='center' valign='middle' class='hyouyou_bgcolor2'>%PointName%</td></tr>\r\n";
}
//�|�C���g����(PC�j�F���b�Z�[�W
    MsgReplace     [47][0] =                   "[�L���|�C���g:%Point%]";
    MsgReplace     [47][2] =                   "[���p��:%PointDate%]";
    MsgReplace     [47][2] = MsgReplace[47][1]+"[�L������:%PointLimit%]";
    MsgReplace     [47][2] = MsgReplace[47][1]+"[�l���|�C���g:%PointCount%]";
    MsgReplace     [47][2] = MsgReplace[47][1]+"[�|�C���g���e:%PointName%]";
    MsgReplace     [47][2] = MsgReplace[47][1]+"[���p�X��:%PointStoreName%]";
	
//�|�C���g�����i�g�сj�FDefault�\��
    TitleDefault   [48]    =                   "�߲��";
    MsgTitleDefault[48][0] =                   "���݂̗L���߲��";
    MsgDefault     [48][0] =                   "[�L���߲�Đ�]<br/>\r\n";
    MsgDefault     [48][0] = MsgDefault[48][0]+"%Point% �߲��<br/>\r\n";
    MsgTitleDefault[48][1] =                   "�ߋ����߲�ė���";
    MsgDefault     [48][1] =                   "%PointDate%<br/>\r\n";
    MsgDefault     [48][1] = MsgDefault[48][1]+"%PointLimit%<br/>\r\n";
    MsgDefault     [48][1] = MsgDefault[48][1]+"%PointCount% P<br/>\r\n";
    MsgDefault     [48][1] = MsgDefault[48][1]+"%PointName%<br/>\r\n";
    MsgDefault     [48][1] = MsgDefault[48][1]+"%PointStoreName%<br/>\r\n";
//�|�C���g�����i�g�сj�F���b�Z�[�W
    MsgReplace     [48][0] =                   "[�L���߲�Đ�:%Point%]";
    MsgReplace     [48][1] =                   "[�l�����t:%PointDate%]";
    MsgReplace     [48][1] = MsgReplace[48][1]+"[�L������:%PointLimit%]";
    MsgReplace     [48][1] = MsgReplace[48][1]+"[�߲��:%PointCount%]";
    MsgReplace     [48][1] = MsgReplace[48][1]+"[�߲�Ė���:%PointName%]";
    MsgReplace     [48][1] = MsgReplace[48][1]+"[�l���X��:%PointStoreName%]";

if ((start_date < trialDate || NewFlag) && trialDate != 99999999)
{
//�����L���O�iPC�j�F�V�z�e�i�rDefault�\��
    TitleDefault   [53]    =                   "�����L���O";
    MsgTitleDefault[53][0] =                   "�y�J�Ò��z�����������t";
    MsgDefault     [53][0] =                   "<h4 class=\"period\">�J�Ê��ԁF%RankingStart%�`%RankingEnd%</h4>\r\n<h4 class=\"period-comment\">�J�Ê��Ԓ��ɂ����X�����������񐔂ɂ���ċ����A�N��2���BIG�C�x���g�ł��B</h4>\r\n<br/>\r\n";
    MsgTitleDefault[53][1] =                   "�y�J�Ò��z�����������t�@�����L���O����";
    MsgDefault     [53][1] =                   "<div>\r\n	<table class=\"current container multi-column\">\r\n<thead>\r\n<tr>\r\n<th class=\"rank\">����</th>\r\n<th class=\"times\">�����p��</th>\r\n<th class=\"money\">�����p���z</th>\r\n<th class=\"point\">�l���|�C���g</th>\r\n</tr>\r\n</thead>\r\n</table>\r\n</div>\r\n";
    MsgTitleDefault[53][2] =                   "REPEAT";
    MsgDefault     [53][2] =                   "<div>\r\n	<table class=\"holding multi-column\">\r\n		<tr class=\"first\" data-rank=\"%CssRankingNo%\">\r\n			<td data-label=\"����\" class=\"first-rank\">%RankingNo%��</td>\r\n			<td data-label=\"�����p��\" class=\"first-times\">%RankingCount%��</td>\r\n<td data-label=\"�����p���z\" class=\"first-money\">%RankingTotal%�~</td>\r\n<td data-label=\"�l���|�C���g\" class=\"first-point\">%RankingPoint%�|�C���g</td>\r\n</tr>\r\n</table>\r\n</div>\r\n";
    MsgTitleDefault[53][3] =                   "���݂̏��ʂƑO��̏���";
    MsgDefault     [53][3] =                   "<div class=\"rank-comment\">\r\n	����\r\n	<div class=\"color\">��%CurrentRank%��</div>\r\n	�ŁA�O�̏��ʂ̕��Ƃ�\r\n	<div class=\"color\">%CountDifference1%��</div>\r\n	�A��̏��ʂ̕��Ƃ�\r\n	<div class=\"color\">%CountDifference2%��</div>\r\n	�ł��B\r\n</div>\r\n<div>\r\n	<table class=\"current multi-column\">\r\n		<tr class=\"first %dispNone%\" data-rank=\"%CssRankingOrderNo1%\">\r\n			<td data-label=\"����\" class=\"first-rank\">%RankingOrderNo1%��</td>\r\n			<td data-label=\"�����p��\" class=\"first-times\">%RankingOrderCount1%��</td>\r\n			<td data-label=\"�����p���z\" class=\"first-money\">%RankingOrderTotal1%�~</td>\r\n			<td data-label=\"�l���|�C���g\" class=\"first-point\">%RankingOrderPoint1%�|�C���g</td>\r\n		</tr>\r\n		<tr class=\"second member-rank\" data-rank=\"%CssRankingOrderNo0%\">\r\n			<td data-label=\"����\" class=\"second-rank\">%RankingOrderNo0%��</td>\r\n			<td data-label=\"�����p��\" class=\"second-times\">%RankingOrderCount0%��</td>\r\n			<td data-label=\"�����p���z\" class=\"second-money\">%RankingOrderTotal0%�~</td>\r\n			<td data-label=\"�l���|�C���g\" class=\"second-point\">%RankingOrderPoint0%�|�C���g</td>\r\n		</tr>\r\n		<tr class=\"third\" data-rank=\"%CssRankingOrderNo2%\">\r\n			<td data-label=\"����\" class=\"third-rank\">%RankingOrderNo2%��</td>\r\n			<td data-label=\"�����p��\" class=\"third-times\">%RankingOrderCount2%��</td>\r\n			<td data-label=\"�����p���z\" class=\"third-money\">%RankingOrderTotal2%�~</td>\r\n			<td data-label=\"�l���|�C���g\" class=\"third-point\">%RankingOrderPoint2%�|�C���g</td>\r\n		</tr>\r\n	</table>\r\n</div>\r\n";
}
else
{
//�����L���O�iPC�j�F��Default�\��
    TitleDefault   [53]    =                   "�����L���O";
    MsgTitleDefault[53][0] =                   "�����L���O";
    MsgDefault     [53][0] =                   "�J�Ê��Ԓ������X�����������񐔂ɂ���ċ����A�N��2���BIG�C�x���g�ł��B<br/>���܎҂ɂ͍��؏��i���v���[���g�������܂��B<br/>�J�Ê��ԁF%RankingStart%�`%RankingEnd%<br/>\r\n";
    MsgTitleDefault[53][1] =                   "�����L���O�ڍ�";
    MsgDefault     [53][1] =                   "<table width='98%' border='0' cellspacing='1' cellpadding='3' class='hyouyou_bordercolor honbun'>\r\n";
    MsgDefault     [53][1] = MsgDefault[53][1]+"<tr><td width='10%' nowrap align='center' valign='middle' class='hyouyou_bgcolor'>����</td><td width='30%' nowrap align='center' valign='middle' class='hyouyou_bgcolor'>�|�C���g</td><td width='30%' nowrap align='center' valign='middle' class='hyouyou_bgcolor'>�����p��</td><td width='30%' nowrap align='center' valign='middle' class='hyouyou_bgcolor'>�����p���z</td></tr>\r\n";
    MsgDefault     [53][1] = MsgDefault[53][1]+"</table>\r\n";
    MsgTitleDefault[53][2] =                   "REPEAT";
    MsgDefault     [53][2] =                   "<tr><td align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingNo%��</td><td align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingPoint%P</td><td align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingCount%��</td><td align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingTotal%�~</td></tr>\r\n";
    MsgTitleDefault[53][3] =                   "���݂̏��ʂƑO��̏���";
    MsgDefault     [53][3] =                   "%NickName%�̏���\r\n";
    MsgDefault     [53][3] = MsgDefault[53][3]+"<table width='98%' border='0' cellspacing='1' cellpadding='3' class='hyouyou_bordercolor honbun'>\r\n";
    MsgDefault     [53][3] = MsgDefault[53][3]+"<tr><td width='10%' align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingOrderNo0%��</td><td width='30%' align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingOrderPoint0%P</td><td width='30%' align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingOrderCount0%��</td><td width='30%' align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingOrderTotal0%�~</td></tr>\r\n";
    MsgDefault     [53][3] = MsgDefault[53][3]+"</table>\r\n";
    MsgDefault     [53][3] = MsgDefault[53][3]+"�O��̏���\r\n";
    MsgDefault     [53][3] = MsgDefault[53][3]+"<table width='98%' border='0' cellspacing='1' cellpadding='3' class='hyouyou_bordercolor honbun'>\r\n";
    MsgDefault     [53][3] = MsgDefault[53][3]+"<tr><td width='10%' align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingOrderNo1%��</td><td width='30%' align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingOrderPoint1%P</td><td width='30%' align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingOrderCount1%��</td><td width='30%' align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingOrderTotal1%�~</td></tr>\r\n";
    MsgDefault     [53][3] = MsgDefault[53][3]+"<tr><td align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingOrderNo2%��</td><td align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingOrderPoint2%P</td><td align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingOrderCount2%��</td><td align='center' valign='middle' class='hyouyou_bgcolor2'>%RankingOrderTotal2%�~</td></tr>\r\n";
    MsgDefault     [53][3] = MsgDefault[53][3]+"</table>\r\n";
}

//�����L���O(PC�j�F���b�Z�[�W
    MsgReplace     [53][0] =                   "[�J�Ê���:%RankingStart%�`%RankingEnd%]";
    MsgReplace     [53][1] =                   "[����:%RankingNo%]";
    MsgReplace     [53][1] = MsgReplace[53][1]+"[�|�C���g:%RankingPoint%]";
    MsgReplace     [53][1] = MsgReplace[53][1]+"[���p��:%PointCount%]";
    MsgReplace     [53][1] = MsgReplace[53][1]+"[���p���z:%RankingTotal%]";

//�����L���O�i�g�сj�FDefault�\��
    TitleDefault   [54]    =                   "�ݷݸ�";
    MsgTitleDefault[54][0] =                   "�ݷݸ�";
    MsgDefault     [54][0] =                   "�J�Ê��Ԓ������X�����������񐔂ɂ���ċ����A�N��2���BIG����Ăł��B<br>\r\n";
    MsgDefault     [54][0] = MsgDefault[54][0]+"���܎҂ɂ͍��؏��i����ھ��Ă������܂��B<br>\r\n";
    MsgDefault     [54][0] = MsgDefault[54][0]+"�J�Ê���<br>\r\n";
    MsgDefault     [54][0] = MsgDefault[54][0]+"%RankingStart%�`%RankingEnd%<br/>\r\n";
    MsgTitleDefault[54][1] =                   "�ݷݸޏڍ�";
    MsgDefault     [54][1] =                   "%RankingNo%�� %RankingPoint%P %RankingCount%�� %RankingTotal%�~<br/>\r\n";
    MsgTitleDefault[54][2] =                   "���ʂƑO��̏���";
    MsgDefault     [54][2] =                   "%NickName%�̏���<br/>\r\n";
    MsgDefault     [54][2] = MsgDefault[54][2]+"%RankingOrderNo0%�� %RankingOrderPoint0%P %RankingOrderCount0%�� %RankingOrderTotal0%�~<br/>\r\n";
    MsgDefault     [54][2] = MsgDefault[54][2]+"<br>�O��̏���<br>\r\n";
    MsgDefault     [54][2] = MsgDefault[54][2]+"%RankingOrderNo1%�� %RankingOrderPoint1%P %RankingOrderCount1%�� %RankingOrderTotal1%�~<br/>\r\n";
    MsgDefault     [54][2] = MsgDefault[54][2]+"%RankingOrderNo2%�� %RankingOrderPoint2%P %RankingOrderCount2%�� %RankingOrderTotal2%�~<br/>\r\n";
//�����L���O�i�g�сj�F���b�Z�[�W
    MsgReplace     [54][0] =                   "[�J�Ê���:%RankingStart%�`%RankingEnd%]";
    MsgReplace     [54][1] =                   "[����:%RankingNo%][�߲��:%RankingPoint%][��:%RankingCount%][���z:%RankingTotal%]";
    MsgReplace     [54][2] =                   "[Ư�Ȱ�:%NickName%]\r\n";
    MsgReplace     [54][2] = MsgReplace[54][2]+"[�����̏���:%RankingOrderNo0%][�߲��:%RankingOrderPoint0%][��:%RankingOrderCount0%][���z:%RankingOrderTotal0%]";
    MsgReplace     [54][2] = MsgReplace[54][2]+"[�O�̏���:%RankingOrderNo1%][�߲��:%RankingOrderPoint1%][��:%RankingOrderCount1%][���z:%RankingOrderTotal1%]";
    MsgReplace     [54][2] = MsgReplace[54][2]+"[��̏���:%RankingOrderNo2%][�߲��:%RankingOrderPoint2%][��:%RankingOrderCount2%][���z:%RankingOrderTotal2%]";

//�����o�[�o�^(QR)�i�g�сj�FDefault�\��
//  ���͗p�i1�F�Œ�j
    TitleDefault   [56]    =                   "���ް�o�^";
    MsgTitleDefault[56][0] =                   "��ق��炨���ȏ������͂����܂�";
    MsgDefault     [56][0] =                   "[Ư�Ȱ�]<br>\r\n";
    MsgDefault     [56][0] = MsgDefault[56][0]+"<input type='text' name='NickName' size='20' maxlength='20' value='%NickName%' istyle='1'><br/>\r\n";
    MsgDefault     [56][0] = MsgDefault[56][0]+"[�a����(���{�l)]<br>\r\n";
    MsgDefault     [56][0] = MsgDefault[56][0]+"<input type='text' name='Birthday' size='8' maxlength='8' value='%Birthday%' istyle='4'><font size=1 color='#FF0000'>�i*�K�{�j</font><br><font color=orange size=1>���͗�:19910101</font><br>\r\n";
    MsgDefault     [56][0] = MsgDefault[56][0]+"[�a����(�߰�Ű)]<br>\r\n";
    MsgDefault     [56][0] = MsgDefault[56][0]+"<input type='text' name='Birthday2' size='8' maxlength='8' value='%Birthday2%' istyle='4'><br><font color=orange size=1>���͗�:19910101</font><br>\r\n";
    MsgDefault     [56][0] = MsgDefault[56][0]+"[�L�O��]<br>\r\n";
    MsgDefault     [56][0] = MsgDefault[56][0]+"<input type='text' name='Memorial' size='8' maxlength='8' value='%Memorial%' istyle='4'><br><font color=orange size=1>���͗�:19910101 OR 0101</font><br>\r\n";
    MsgDefault     [56][0] = MsgDefault[56][0]+"[�-�϶޼��]<br>\r\n";
    MsgDefault     [56][0] = MsgDefault[56][0]+"<input name='status' type='checkbox' value='1' %MailStatus% >��]����<br/>\r\n";
    MsgDefault     [56][0] = MsgDefault[56][0]+"<input type='submit' value='�o�^����'><br>\r\n";
//  �m�F�p�i2:�Œ�j
    MsgTitleDefault[56][1] =                   "���͓��e�����m�F��������";
    MsgDefault     [56][1] =                   "[Ư�Ȱ�]<br>\r\n";
    MsgDefault     [56][1] = MsgDefault[56][1]+"%NickName%<br><input type='hidden' name='NickName' value='%NickName%'><br/>\r\n";
    MsgDefault     [56][1] = MsgDefault[56][1]+"[�a����(���{�l)]<br>\r\n";
    MsgDefault     [56][1] = MsgDefault[56][1]+"%Birthday%<br><input type='hidden' name='Birthday' value='%Birthday%'><br/>\r\n";
    MsgDefault     [56][1] = MsgDefault[56][1]+"[�a����(�߰�Ű)]<br>\r\n";
    MsgDefault     [56][1] = MsgDefault[56][1]+"%Birthday%<br><input type='hidden' name='Birthday2' value='%Birthday2%'><br/>\r\n";
    MsgDefault     [56][1] = MsgDefault[56][1]+"[�L�O��]<br>\r\n";
    MsgDefault     [56][1] = MsgDefault[56][1]+"%Memorial%<br><input type='hidden' name='Memorial' value='%Memorial%'><br/>\r\n";
    MsgDefault     [56][1] = MsgDefault[56][1]+"[�-�϶޼��]<br>\r\n";
    MsgDefault     [56][1] = MsgDefault[56][1]+"%MailStatusText%<br>\r\n";
    MsgDefault     [56][1] = MsgDefault[56][1]+"<input type='submit' value='�o�^����'><br>\r\n";
//�����o�[�o�^(QR)�i�g�сj�F���b�Z�[�W
    MsgReplace     [56][0] =                   "<strong>���͗p�i���͉ӏ���1�Œ�j</strong><br>[Ư�Ȱ�:%NickName%]<br/>";
    MsgReplace     [56][0] = MsgReplace[56][0]+"[�a����:%Birthday%][�a����2:%Birthday2%]<br/>";
    MsgReplace     [56][0] = MsgReplace[56][0]+"[�L�O��:%Memorial%][�L�O��2:%Memorial2%]<br/>";
    MsgReplace     [56][0] = MsgReplace[56][0]+"[�����}�K��]:%MailStatus%]<br/>";
    MsgReplace     [56][1] =                   "<strong>�m�F�p�i���͉ӏ���2�Œ�j</strong><br>[Ư�Ȱ�:%NickName%]<br/>";
    MsgReplace     [56][1] = MsgReplace[56][1]+"[�a����:%Birthday%][�a����2:%Birthday2%]<br/>";
    MsgReplace     [56][1] = MsgReplace[56][1]+"[�L�O��:%Memorial%][�L�O��2:%Memorial2%]<br/>";
    MsgReplace     [56][1] = MsgReplace[56][1]+"[�����}�K��]:%MailStatusText%]<br/>";
    MsgReplace     [56][2] =                   "<strong>3�ȍ~�͎g�p���܂���</strong>";
%>
