<%@ page contentType="text/html; charset=Windows-31J" %>
<%
    int[][]    MenuNo   = new int[3][100];
    String[][] Title    = new String[3][100];
    String[][] Contents = new String[3][100];
    int[]      MenuDataType = new int[200]; //�R�s�[�̂Ƃ��Ɏg�p����
    //�����̒l�́Amenu.hpedit_id �̒l�Ɋ֘A����B[n] �� n = �g�т�datatype/2
    MenuDataType[ 1] = 2; //What'sNew     1- 2
    MenuDataType[ 2] = 3; //�C�x���g���  3- 4
    MenuDataType[ 3] = 0; //TOP           5- 6
    MenuDataType[ 4] =20; //�N���W�b�g    7- 8
    MenuDataType[ 5] = 7; //�f����        9-10
    MenuDataType[ 6] = 6; //�A�N�Z�X��� 11-12
    MenuDataType[ 7] = 8; //�z�e���ֈꌾ 13-14
    MenuDataType[ 8] = 4; //�T�[�r�X��� 15-16
    MenuDataType[ 9] =12; //�N�[�|��  �@ 17-18
    MenuDataType[10] = 5; //���H���j���[ 19-20
    MenuDataType[11] = 9; //FAQ�@�@�@�@�@21-22
    MenuDataType[12] =10; //���l���@�@ 23-24
    MenuDataType[13] =11; //���[���\��@ 25-26
    MenuDataType[14] = 1; //�����o�[���T 27-28
    MenuDataType[15] =13; //�������     29-30
    MenuDataType[16] =14; //�ݔ����     31-32
    MenuDataType[17] =15; //����Эڰ��� 33-34
    MenuDataType[18] =16; //���T�Ȃǁ@�@ 35-36
    MenuDataType[19] =17; //���������^�� 37-38
    MenuDataType[20] =18; //VOD�CDVD�Ȃ� 39-40
    MenuDataType[21] =19; //�������     41-42
    MenuDataType[22] =21; //�����o�[��� 43-44
    MenuDataType[23] =22; //���p����     45-46
    MenuDataType[24] =23; //�|�C���g���� 47-48
    MenuDataType[25] =24; //�S���B��     49-50
    MenuDataType[26] =25; //��Ű��ٰ�    51-52
    MenuDataType[27] =26; //�����L���O   53-54
    MenuDataType[28] =27; //QR�����o�[   55-56
    MenuDataType[29] =28; //�ؼ��ٸ����  57-58
    MenuDataType[30] =29; //�\��2        59-60
    MenuDataType[31] =35; //�S�X�S���B�� 65-66
    MenuDataType[35] =34; //���ް۸޲�   69-70
    MenuDataType[36] =30; //�\��3        71-72
    MenuDataType[37] =31; //�\��4        73-74
    MenuDataType[38] =32; //�\��5        75-76
    MenuDataType[39] =33; //�\��6        77-78
    MenuDataType[40] =36; //���[���}�K�W�����p�K��      79-80

    for (int r = 42; r < 200; r++)
    {
        MenuDataType[r] = r-7; //�\��7�`
    }

    MenuNo  [0][ 0] = 5;
    Title   [0][ 0] = "TOP�y�[�W";
    Contents[0][ 0] = "searchnew.jsp";

    MenuNo  [0][ 1] = 27;
    Title   [0][ 1] = "Member's Only";
    Contents[0][ 1] = "memberonly.jsp";

    MenuNo  [0][ 2] = 1;
    Title   [0][ 2] = "What's New";
    Contents[0][ 2] = "new.jsp";

    MenuNo  [0][ 3] = 3;
    Title   [0][ 3] = "�C�x���g���";
    Contents[0][ 3] = "event.jsp";

    MenuNo  [0][ 4] = 15;
    Title   [0][ 4] = "�T�[�r�X���";
    Contents[0][ 4] = "service.jsp";

    MenuNo  [0][ 5] = 19;
    Title   [0][ 5] = "���X�g�������j���[";
    Contents[0][ 5] = "service.jsp";

    MenuNo  [0][ 6] = 11;
    Title   [0][ 6] = "�A�N�Z�X���";
    Contents[0][ 6] = "access.jsp";

    MenuNo  [0][ 7] = 9;
    Title   [0][ 7] = "�f����";
    Contents[0][ 7] = "bbs.jsp";

    MenuNo  [0][ 8] = 13;
    Title   [0][ 8] = "�z�e���ֈꌾ";
    Contents[0][ 8] = "mailto.jsp";

    MenuNo  [0][ 9] = 21;
    Title   [0][ 9] = "FAQ";
    Contents[0][ 9] = "faq.jsp";

    MenuNo  [0][10] = 23;
    Title   [0][10] = "���l���";
    Contents[0][10] = "recruit.jsp";

    MenuNo  [0][11] = 25;
    Title   [0][11] = "���[���\��";
    Contents[0][11] = "mailreserve.jsp";

    MenuNo  [0][12] = 17;
    Title   [0][12] = "�N�[�|��";
    Contents[0][12] = "coupon.jsp";

    MenuNo  [0][13] = 29;
    Title   [0][13] = "�������";
    Contents[0][13] = "priceinfo.jsp";

    MenuNo  [0][14] = 31;
    Title   [0][14] = "�ݔ����";
    Contents[0][14] = "service.jsp";

    MenuNo  [0][15] = 33;
    Title   [0][15] = "�����V�~�����[�V����";
    Contents[0][15] = "simulate.jsp";

    MenuNo  [0][16] = 35;
    Title   [0][16] = "�����o�[�Y���T";
    Contents[0][16] = "premium.jsp";

    MenuNo  [0][17] = 37;
    Title   [0][17] = "���������^��";
    Contents[0][17] = "rental.jsp";

    MenuNo  [0][18] = 39;
    Title   [0][18] = "�R���e���c";
    Contents[0][18] = "contents.jsp";

    MenuNo  [0][19] = 41;
    Title   [0][19] = "�������";
    Contents[0][19] = "roominfo.jsp";

    MenuNo  [0][20] = 7;
    Title   [0][20] = "�N���W�b�g�J�[�h";
    Contents[0][20] = "credit.jsp";

    MenuNo  [0][21] = 43;
    Title   [0][21] = "�����o�[���";
    Contents[0][21] = "memberinfo.jsp";

    MenuNo  [0][22] = 45;
    Title   [0][22] = "���p����";
    Contents[0][22] = "memberuse.jsp";

    MenuNo  [0][23] = 47;
    Title   [0][23] = "�|�C���g����";
    Contents[0][23] = "memberpoint.jsp";

    MenuNo  [0][24] = 49;
    Title   [0][24] = "�S���B��";
    Contents[0][24] = "memberallroom.jsp";

    MenuNo  [0][25] = 51;
    Title   [0][25] = "�I�[�i�[�Y���[��";
    Contents[0][25] = "memberowner.jsp";

    MenuNo  [0][26] = 53;
    Title   [0][26] = "�����L���O";
    Contents[0][26] = "memberranking.jsp";

    MenuNo  [0][27] = 55;
    Title   [0][27] = "�����o�[�o�^(QR)";
    Contents[0][27] = "memberregist.jsp";

    MenuNo  [0][28] = 57;
    Title   [0][28] = "�I���W�i���N�[�|��";
    Contents[0][28] = "originalcoupon.jsp";

    MenuNo  [0][29] = 59;
    Title   [0][29] = "�\��2";
    Contents[0][29] = "general2.jsp";

    MenuNo  [0][30] = 71;
    Title   [0][30] = "�\��3";
    Contents[0][30] = "general3.jsp";

    MenuNo  [0][31] = 73;
    Title   [0][31] = "�\��4";
    Contents[0][31] = "general4.jsp";

    MenuNo  [0][32] = 75;
    Title   [0][32] = "�\��5";
    Contents[0][32] = "general5.jsp";

    MenuNo  [0][33] = 77;
    Title   [0][33] = "�\��6";
    Contents[0][33] = "general6.jsp";

    MenuNo  [0][34] = 69;
    Title   [0][34] = "���ް۸޲�";
    Contents[0][34] = "memberlogin.jsp";

    MenuNo  [0][35] = 65;
    Title   [0][35] = "�S�X�S���B��";
    Contents[0][35] = "memberallallroom.jsp";

    MenuNo  [0][36] = 79;
    Title   [0][36] = "���[���}�K�W�����p�K��";
    Contents[0][36] = "mailmag_term.jsp";

    for (int r = 37; r < 100; r++)
    {
        MenuNo  [0][r] = (r*2)+13;
        Title   [0][r] = "�\��" + (r-28);
        Contents[0][r] = "general"+ (r-28) + ".jsp";
    }
    MenuNo  [1][ 0] = 6;
    Title   [1][ 0] = "TOP�߰��";
    Contents[1][ 0] = "searchnew.jsp";

    MenuNo  [1][ 1] = 28;
    Title   [1][ 1] = "Member's Only";
    Contents[1][ 1] = "search.jsp";

    MenuNo  [1][ 2] = 2;
    Title   [1][ 2] = "What's New";
    Contents[1][ 2] = "new.jsp";

    MenuNo  [1][ 3] = 4;
    Title   [1][ 3] = "����ď��";
    Contents[1][ 3] = "event.jsp";

    MenuNo  [1][ 4] = 16;
    Title   [1][ 4] = "���޽���";
    Contents[1][ 4] = "service.jsp";

    MenuNo  [1][ 5] = 20;
    Title   [1][ 5] = "ڽ����ƭ�";
    Contents[1][ 5] = "service.jsp";

    MenuNo  [1][ 6] = 12;
    Title   [1][ 6] = "�������";
    Contents[1][ 6] = "access.jsp";

    MenuNo  [1][ 7] = 10;
    Title   [1][ 7] = "�f����";
    Contents[1][ 7] = "bbs.jsp";

    MenuNo  [1][ 8] = 14;
    Title   [1][ 8] = "��قֈꌾ";
    Contents[1][ 8] = "mailto.jsp";

    MenuNo  [1][ 9] = 22;
    Title   [1][ 9] = "FAQ";
    Contents[1][ 9] = "faq.jsp";

    MenuNo  [1][10] = 24;
    Title   [1][10] = "���l���";
    Contents[1][10] = "recruit.jsp";

    MenuNo  [1][11] = 26;
    Title   [1][11] = "Ұٗ\��";
    Contents[1][11] = "mailreserve.jsp";

    MenuNo  [1][12] = 18;
    Title   [1][12] = "�����";
    Contents[1][12] = "coupon.jsp";

    MenuNo  [1][13] = 30;
    Title   [1][13] = "�������";
    Contents[1][13] = "priceinfo.jsp";

    MenuNo  [1][14] = 32;
    Title   [1][14] = "�ݔ����";
    Contents[1][14] = "service.jsp";

    MenuNo  [1][15] = 34;
    Title   [1][15] = "����Эڰ���";
    Contents[1][15] = "simulate.jsp";

    MenuNo  [1][16] = 36;
    Title   [1][16] = "���ް�ޓ��T";
    Contents[1][16] = "premium.jsp";

    MenuNo  [1][17] = 38;
    Title   [1][17] = "��������";
    Contents[1][17] = "rental.jsp";

    MenuNo  [1][18] = 40;
    Title   [1][18] = "�����";
    Contents[1][18] = "contents.jsp";

    MenuNo  [1][19] = 42;
    Title   [1][19] = "�������";
    Contents[1][19] = "roominfo.jsp";

    MenuNo  [1][20] = 8;
    Title   [1][20] = "�ڼޯĶ���";
    Contents[1][20] = "credit.jsp";

    MenuNo  [1][21] = 44;
    Title   [1][21] = "���ް���";
    Contents[1][21] = "memberinfo.jsp";

    MenuNo  [1][22] = 46;
    Title   [1][22] = "���p����";
    Contents[1][22] = "memberuse.jsp";

    MenuNo  [1][23] = 48;
    Title   [1][23] = "�߲�ė���";
    Contents[1][23] = "memberpoint.jsp";

    MenuNo  [1][24] = 50;
    Title   [1][24] = "�S���B��";
    Contents[1][24] = "memberallroom.jsp";

    MenuNo  [1][25] = 52;
    Title   [1][25] = "��Ű��ٰ�";
    Contents[1][25] = "memberowner.jsp";

    MenuNo  [1][26] = 54;
    Title   [1][26] = "�ݷݸ�";
    Contents[1][26] = "memberranking.jsp";

    MenuNo  [1][27] = 56;
    Title   [1][27] = "���ް�o�^(QR)";
    Contents[1][27] = "memberregist.jsp";

    MenuNo  [1][28] = 58;
    Title   [1][28] = "�ؼ��ٸ����";
    Contents[1][28] = "originalcoupon.jsp";

    MenuNo  [1][29] = 60;
    Title   [1][29] = "�\��2";
    Contents[1][29] = "general2.jsp";

    MenuNo  [1][30] = 72;
    Title   [1][30] = "�\��3";
    Contents[1][30] = "general3.jsp";

    MenuNo  [1][31] = 74;
    Title   [1][31] = "�\��4";
    Contents[1][31] = "general4.jsp";

    MenuNo  [1][32] = 76;
    Title   [1][32] = "�\��5";
    Contents[1][32] = "general5.jsp";

    MenuNo  [1][33] = 78;
    Title   [1][33] = "�\��6";
    Contents[1][33] = "general6.jsp";

    MenuNo  [1][34] = 70;
    Title   [1][34] = "���ް۸޲�";
    Contents[1][34] = "memberlogin.jsp";

    MenuNo  [1][35] = 66;
    Title   [1][35] = "�S�X�S���B��";
    Contents[1][35] = "memberallallroom.jsp";

    MenuNo  [1][36] = 80;
    Title   [1][36] = "���[���}�K�W�����p�K��";
    Contents[1][36] = "mailmag_term.jsp";

    for (int r = 37; r < 100; r++)
    {
        MenuNo  [1][r] = (r*2)+14;
        Title   [1][r] = "�\��" + (r-28);
        Contents[1][r] = "general"+ (r-28) + ".jsp";
    }

    MenuNo  [2][ 0] = 81;
    Title   [2][ 0] = "�ܕi�������b�Z�[�W";
    Contents[2][ 0] = "goods_message.jsp";

    MenuNo  [2][ 1] = 82;
    Title   [2][ 1] = "�ܕi�o�^";
    Contents[2][ 1] = "goods_list.jsp";
%>
