<%@ page contentType="text/html; charset=Windows-31J" %>
<%
    int DispIdxMax   = 1;
    int DataTypeFrom = 81;
    int DataTypeTo   = 82;

    // disp_idx �́Adata_type����81���������l�ƈ�v����B
    // [event_edit_info.disp_idx][event_edit_info.mag���́�����1���������l]

    String[][] Explain    = new String[10][8];
    int[][]    MsgInput   = new int[10][8];        //0:���͂Ȃ�
    int[][]    Decoration = new int[10][8];        //0:�����Ȃ�,1:��������
    int[][]    Method     = new int[10][8];

    //Method :1  �u����v�u�Ȃ��v�@�����W�I�{�^���Ń`�F�b�N����
    //Method :2  �u�K�{�v�u����v�u�Ȃ��v�����W�I�{�^���Ń`�F�b�N����

    //event_edit_info.msg1
    Explain   [0][0] = "�|�C���g����������";
    Decoration[0][0] = 1;
    MsgInput  [0][0] = 1;

    //event_edit_info.msg2
    Explain   [0][1] = "���ʃ^�C�g���i���[���^�C�g���u��������t�����v�ȂǂɎg�p����܂��j";
    Decoration[0][1] = 0;
    MsgInput  [0][1] = 0;

    //event_edit_info.msg3
    Explain   [0][2] = "�|�C���g�������ӏ���";
    Decoration[0][2] = 1;
    MsgInput  [0][2] = 1;

    //event_edit_info.msg4
    Explain   [0][3] = "�|�C���g�c���Ƃ̃`�F�b�N";
    Method    [0][3] = 1;
    MsgInput  [0][3] = 0;

    //event_edit_info.msg5
    Explain   [0][4] = "���͊������A�u�z�e�����n���v�̏ꍇ�̃��[�������b�Z�[�W";
    Decoration[0][4] = 0;
    MsgInput  [0][4] = 1;

    //event_edit_info.msg6
    Explain   [0][5] = "���͊������A�u�z���v�̏ꍇ�̃��[�������b�Z�[�W";
    Decoration[0][5] = 0;
    MsgInput  [0][5] = 1;

    //event_edit_info.msg7
    Explain   [0][6] = "���͊������A�u�z�e�����n���v�̏ꍇ�̕\�����b�Z�[�W";
    Decoration[0][6] = 1;
    MsgInput  [0][6] = 1;

    //event_edit_info.msg8
    Explain   [0][7] = "���͊������A�u�z���v�̏ꍇ�̕\�����b�Z�[�W";
    Decoration[0][7] = 1;
    MsgInput  [0][7] = 1;

    //event_edit_info.msg1
    Explain   [1][0] = "�ď́F�i�ior���i etc";
    Decoration[1][0] = 0;
    MsgInput  [1][0] = 0;

    //event_edit_info.msg2
    Explain   [1][1] = "���o���F�l�����͎��R�����g���ď́A�{���F�l�����͎��R�����g������";
    Decoration[1][1] = 1;
    MsgInput  [1][1] = 1;

    //event_edit_info.msg3
    Explain   [1][2] = "�l�����͎����ӏ���";
    Decoration[1][2] = 1;
    MsgInput  [1][2] = 1;

    Explain   [1][3] = "";

    //event_edit_info.msg5
    Explain   [1][4] = "�l���F�����O";
    Method    [1][4] = 2;
    MsgInput  [1][4] = 0;

    //event_edit_info.msg6
    Explain   [1][5] = "�l���F�j�b�N�l�[��";
    Method    [1][5] = 2;
    MsgInput  [1][5] = 0;

    //event_edit_info.msg7
    Explain   [1][6] = "�l���F���[���A�h���X����";
    Method    [1][6] = 2;
    MsgInput  [1][6] = 0;

    //event_edit_info.msg8
    Explain   [1][7] = "�l���F�d�b�ԍ�����";
    Method    [1][7] = 2;
    MsgInput  [1][7] = 0;
%>
