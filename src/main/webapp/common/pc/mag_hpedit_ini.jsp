<%@ page contentType="text/html; charset=Windows-31J" %>
<%
    // [event_edit_info.disp_idx][event_edit_info.mag���́�����1���������l]
    String[][] Explain       = new String[10][8];
    int[][]    Decoration    = new int[10][8];        //0:�����Ȃ�,1:��������
    String[][] MsgStandard   = new String[10][8];
    int[][]    ImediaOnly    = new int[10][8];
    int[][]    Method        = new int[10][8];  //Method :1  �u����v�u�Ȃ��v�@�����W�I�{�^���Ń`�F�b�N����
    String[][] FooterTitle   = new String[10][8]; //Footer �̃^�C�g��
    String[][] FooterDefault = new String[10][8]; //Footer �̃��b�Z�[�W


    //event_edit_info.msg1
    Explain   [0][0] = "���[���}�K�W�������g�b�v�y�[�W�̃��b�Z�[�W";
    Decoration[0][0] = 1;
    MsgStandard[0][0] = "���z�e���ł͂����ȏ����A���[���ł��q�l�ɂ��͂�����T�[�r�X���s���Ă���܂��B<br>\r\n�z�e���̃C�x���g������������GET!<br>\r\n�����o�^���܂��傤!! <br>";
    ImediaOnly[0][0] = 0;

    //event_edit_info.msg2
    Explain   [0][1] = "QR�����o�[�o�^���g�b�v�y�[�W�̃��b�Z�[�W";
    Decoration[0][1] = 1;
    MsgStandard[0][1]="���z�e���ł͂����ȏ����A���[���ł��q�l�ɂ��͂�����T�[�r�X���s���Ă���܂��B<br>\r\n�z�e���̃C�x���g������������GET!<br>\r\n�����o�^���܂��傤!! <br>";
    ImediaOnly[0][1] = 1;

    //event_edit_info.msg3
    Explain   [0][2] = "�����o�[�ȊO�̏ꍇ�̗U�����b�Z�[�W";
    Decoration[0][2] = 1;
    MsgStandard[0][2]="�������o�[�ȊO�̕��͂����炩��";
    ImediaOnly[0][2] = 0;

    //event_edit_info.msg4
    Explain   [0][3] = "�����o�[�̏ꍇ�̗U�����b�Z�[�W";
    Decoration[0][3] = 1;
    MsgStandard[0][3]="<strong>�������o�[�̕��͂����炩��</strong>";
    ImediaOnly[0][3] = 0;

    //event_edit_info.msg5
    Explain   [0][4] = "�����o�[�o�^�������̃��b�Z�[�W";
    Decoration[0][4] = 1;
    MsgStandard[0][4]="���肪�Ƃ��������܂��B<br>\r\n�z�e������̂����ȏ������͂��������܂��B<br><br><br>";
    ImediaOnly[0][4] = 1;

    //event_edit_info.msg6
    Explain   [0][5] = "�����o�[�o�^���������[�������b�Z�[�W�i���͂��Ȃ��ꍇ�͑����܂���j";
    Decoration[0][5] = 0;
    MsgStandard[0][5]="";
    ImediaOnly[0][5] = 1;

    //event_edit_info.msg7
    Explain   [0][6] = "�����}�K�o�^�������̃��b�Z�[�W";
    Decoration[0][6] = 1;
    MsgStandard[0][6]="���肪�Ƃ��������܂��B<br>\r\n�z�e������̂����ȏ������͂��������܂��B<br><br><br>";
    ImediaOnly[0][6] = 0;

    //event_edit_info.msg8
    Explain   [0][7] = "�����}�K�o�^���������[�������b�Z�[�W�i���͂��Ȃ��ꍇ�͑����܂���j";
    Decoration[0][7] = 0;
    MsgStandard[0][7]="";
    ImediaOnly[0][7] = 0;
    FooterTitle[0][7] = "�u�����Ӂv�\�����e�i�ő�120�����B���b�Z�[�W���Ȃ��ꍇ�͕\�����܂���j";
    FooterDefault[0][7] = "�����̃��[���ɕԐM���Ă����񓚂ł��܂���̂ł����ӂ��������B";
%>
