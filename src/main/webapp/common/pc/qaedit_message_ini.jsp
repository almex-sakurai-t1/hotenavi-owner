<%@ page contentType="text/html; charset=Windows-31J" %>
<%
    int data_type = 63;

    // [event_edit_info.disp_idx][event_edit_info.mag���́�����1���������l]
    String[][] Explain    = new String[10][8];
    int[][]    Decoration = new int[10][8];        //0:�����Ȃ�,1:��������
    int[][]    Method     = new int[10][8];

    //Method :1  �u����v�u�Ȃ��v�@�����W�I�{�^���Ń`�F�b�N����

    //event_edit_info.msg1
    Explain   [0][0] = "�A���P�[�g���e�g�b�v��ʂɕ\�����郁�b�Z�[�W�i�r�W�^�[�p�j";
    Decoration[0][0] = 1;

    //event_edit_info.msg2
    Explain   [0][1] = "�A���P�[�g���e�g�b�v��ʂɕ\�����郁�b�Z�[�W�i�����o�[�p�j";
    Decoration[0][1] = 1;

    //event_edit_info.msg3
    Explain   [0][2] = "";
    Decoration[0][2] = 1;

    //event_edit_info.msg4
    Explain   [0][3] = "";
    Method    [0][3] = 1;

    //event_edit_info.msg5
    Explain   [0][4] = "�A���P�[�g���e�������ɓ��e�ώ��ɕ\�����郁�b�Z�[�W�i�r�W�^�[�p�j";
    Decoration[0][4] = 1;

    //event_edit_info.msg6
    Explain   [0][5] = "�A���P�[�g���e�������ɓ��e�ώ��ɕ\�����郁�b�Z�[�W�i�����o�[�p�j";
    Decoration[0][5] = 1;

    //event_edit_info.msg7
    Explain   [0][6] = "�A���P�[�g���e�������ɕ\�����郁�b�Z�[�W�i�r�W�^�[�p�j";
    Decoration[0][6] = 1;

    //event_edit_info.msg8
    Explain   [0][7] = "�A���P�[�g���e�������ɕ\�����郁�b�Z�[�W�i�����o�[�p�j";
    Decoration[0][7] = 1;
%>
