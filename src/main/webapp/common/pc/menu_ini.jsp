<%@ page contentType="text/html; charset=Windows-31J" %>
<%
    int[][]    MenuNo   = new int[3][100];
    String[][] Title    = new String[3][100];
    String[][] Contents = new String[3][100];
    int[]      MenuDataType = new int[200]; //コピーのときに使用する
    //ここの値は、menu.hpedit_id の値に関連する。[n] の n = 携帯のdatatype/2
    MenuDataType[ 1] = 2; //What'sNew     1- 2
    MenuDataType[ 2] = 3; //イベント情報  3- 4
    MenuDataType[ 3] = 0; //TOP           5- 6
    MenuDataType[ 4] =20; //クレジット    7- 8
    MenuDataType[ 5] = 7; //掲示板        9-10
    MenuDataType[ 6] = 6; //アクセス情報 11-12
    MenuDataType[ 7] = 8; //ホテルへ一言 13-14
    MenuDataType[ 8] = 4; //サービス情報 15-16
    MenuDataType[ 9] =12; //クーポン  　 17-18
    MenuDataType[10] = 5; //飲食メニュー 19-20
    MenuDataType[11] = 9; //FAQ　　　　　21-22
    MenuDataType[12] =10; //求人情報　　 23-24
    MenuDataType[13] =11; //メール予約　 25-26
    MenuDataType[14] = 1; //メンバー特典 27-28
    MenuDataType[15] =13; //料金情報     29-30
    MenuDataType[16] =14; //設備情報     31-32
    MenuDataType[17] =15; //料金ｼﾐｭﾚｰｼｮﾝ 33-34
    MenuDataType[18] =16; //特典など　　 35-36
    MenuDataType[19] =17; //無料レンタル 37-38
    MenuDataType[20] =18; //VOD，DVDなど 39-40
    MenuDataType[21] =19; //部屋情報     41-42
    MenuDataType[22] =21; //メンバー情報 43-44
    MenuDataType[23] =22; //利用履歴     45-46
    MenuDataType[24] =23; //ポイント履歴 47-48
    MenuDataType[25] =24; //全室達成     49-50
    MenuDataType[26] =25; //ｵｰﾅｰｽﾞﾙｰﾑ    51-52
    MenuDataType[27] =26; //ランキング   53-54
    MenuDataType[28] =27; //QRメンバー   55-56
    MenuDataType[29] =28; //ｵﾘｼﾞﾅﾙｸｰﾎﾟﾝ  57-58
    MenuDataType[30] =29; //予備2        59-60
    MenuDataType[31] =35; //全店全室達成 65-66
    MenuDataType[35] =34; //ﾒﾝﾊﾞｰﾛｸﾞｲﾝ   69-70
    MenuDataType[36] =30; //予備3        71-72
    MenuDataType[37] =31; //予備4        73-74
    MenuDataType[38] =32; //予備5        75-76
    MenuDataType[39] =33; //予備6        77-78
    MenuDataType[40] =36; //メールマガジン利用規約      79-80

    for (int r = 42; r < 200; r++)
    {
        MenuDataType[r] = r-7; //予備7～
    }

    MenuNo  [0][ 0] = 5;
    Title   [0][ 0] = "TOPページ";
    Contents[0][ 0] = "searchnew.jsp";

    MenuNo  [0][ 1] = 27;
    Title   [0][ 1] = "Member's Only";
    Contents[0][ 1] = "memberonly.jsp";

    MenuNo  [0][ 2] = 1;
    Title   [0][ 2] = "What's New";
    Contents[0][ 2] = "new.jsp";

    MenuNo  [0][ 3] = 3;
    Title   [0][ 3] = "イベント情報";
    Contents[0][ 3] = "event.jsp";

    MenuNo  [0][ 4] = 15;
    Title   [0][ 4] = "サービス情報";
    Contents[0][ 4] = "service.jsp";

    MenuNo  [0][ 5] = 19;
    Title   [0][ 5] = "レストランメニュー";
    Contents[0][ 5] = "service.jsp";

    MenuNo  [0][ 6] = 11;
    Title   [0][ 6] = "アクセス情報";
    Contents[0][ 6] = "access.jsp";

    MenuNo  [0][ 7] = 9;
    Title   [0][ 7] = "掲示板";
    Contents[0][ 7] = "bbs.jsp";

    MenuNo  [0][ 8] = 13;
    Title   [0][ 8] = "ホテルへ一言";
    Contents[0][ 8] = "mailto.jsp";

    MenuNo  [0][ 9] = 21;
    Title   [0][ 9] = "FAQ";
    Contents[0][ 9] = "faq.jsp";

    MenuNo  [0][10] = 23;
    Title   [0][10] = "求人情報";
    Contents[0][10] = "recruit.jsp";

    MenuNo  [0][11] = 25;
    Title   [0][11] = "メール予約";
    Contents[0][11] = "mailreserve.jsp";

    MenuNo  [0][12] = 17;
    Title   [0][12] = "クーポン";
    Contents[0][12] = "coupon.jsp";

    MenuNo  [0][13] = 29;
    Title   [0][13] = "料金情報";
    Contents[0][13] = "priceinfo.jsp";

    MenuNo  [0][14] = 31;
    Title   [0][14] = "設備情報";
    Contents[0][14] = "service.jsp";

    MenuNo  [0][15] = 33;
    Title   [0][15] = "料金シミュレーション";
    Contents[0][15] = "simulate.jsp";

    MenuNo  [0][16] = 35;
    Title   [0][16] = "メンバーズ特典";
    Contents[0][16] = "premium.jsp";

    MenuNo  [0][17] = 37;
    Title   [0][17] = "無料レンタル";
    Contents[0][17] = "rental.jsp";

    MenuNo  [0][18] = 39;
    Title   [0][18] = "コンテンツ";
    Contents[0][18] = "contents.jsp";

    MenuNo  [0][19] = 41;
    Title   [0][19] = "部屋情報";
    Contents[0][19] = "roominfo.jsp";

    MenuNo  [0][20] = 7;
    Title   [0][20] = "クレジットカード";
    Contents[0][20] = "credit.jsp";

    MenuNo  [0][21] = 43;
    Title   [0][21] = "メンバー情報";
    Contents[0][21] = "memberinfo.jsp";

    MenuNo  [0][22] = 45;
    Title   [0][22] = "利用履歴";
    Contents[0][22] = "memberuse.jsp";

    MenuNo  [0][23] = 47;
    Title   [0][23] = "ポイント履歴";
    Contents[0][23] = "memberpoint.jsp";

    MenuNo  [0][24] = 49;
    Title   [0][24] = "全室達成";
    Contents[0][24] = "memberallroom.jsp";

    MenuNo  [0][25] = 51;
    Title   [0][25] = "オーナーズルーム";
    Contents[0][25] = "memberowner.jsp";

    MenuNo  [0][26] = 53;
    Title   [0][26] = "ランキング";
    Contents[0][26] = "memberranking.jsp";

    MenuNo  [0][27] = 55;
    Title   [0][27] = "メンバー登録(QR)";
    Contents[0][27] = "memberregist.jsp";

    MenuNo  [0][28] = 57;
    Title   [0][28] = "オリジナルクーポン";
    Contents[0][28] = "originalcoupon.jsp";

    MenuNo  [0][29] = 59;
    Title   [0][29] = "予備2";
    Contents[0][29] = "general2.jsp";

    MenuNo  [0][30] = 71;
    Title   [0][30] = "予備3";
    Contents[0][30] = "general3.jsp";

    MenuNo  [0][31] = 73;
    Title   [0][31] = "予備4";
    Contents[0][31] = "general4.jsp";

    MenuNo  [0][32] = 75;
    Title   [0][32] = "予備5";
    Contents[0][32] = "general5.jsp";

    MenuNo  [0][33] = 77;
    Title   [0][33] = "予備6";
    Contents[0][33] = "general6.jsp";

    MenuNo  [0][34] = 69;
    Title   [0][34] = "ﾒﾝﾊﾞｰﾛｸﾞｲﾝ";
    Contents[0][34] = "memberlogin.jsp";

    MenuNo  [0][35] = 65;
    Title   [0][35] = "全店全室達成";
    Contents[0][35] = "memberallallroom.jsp";

    MenuNo  [0][36] = 79;
    Title   [0][36] = "メールマガジン利用規約";
    Contents[0][36] = "mailmag_term.jsp";

    for (int r = 37; r < 100; r++)
    {
        MenuNo  [0][r] = (r*2)+13;
        Title   [0][r] = "予備" + (r-28);
        Contents[0][r] = "general"+ (r-28) + ".jsp";
    }
    MenuNo  [1][ 0] = 6;
    Title   [1][ 0] = "TOPﾍﾟｰｼﾞ";
    Contents[1][ 0] = "searchnew.jsp";

    MenuNo  [1][ 1] = 28;
    Title   [1][ 1] = "Member's Only";
    Contents[1][ 1] = "search.jsp";

    MenuNo  [1][ 2] = 2;
    Title   [1][ 2] = "What's New";
    Contents[1][ 2] = "new.jsp";

    MenuNo  [1][ 3] = 4;
    Title   [1][ 3] = "ｲﾍﾞﾝﾄ情報";
    Contents[1][ 3] = "event.jsp";

    MenuNo  [1][ 4] = 16;
    Title   [1][ 4] = "ｻｰﾋﾞｽ情報";
    Contents[1][ 4] = "service.jsp";

    MenuNo  [1][ 5] = 20;
    Title   [1][ 5] = "ﾚｽﾄﾗﾝﾒﾆｭｰ";
    Contents[1][ 5] = "service.jsp";

    MenuNo  [1][ 6] = 12;
    Title   [1][ 6] = "ｱｸｾｽ情報";
    Contents[1][ 6] = "access.jsp";

    MenuNo  [1][ 7] = 10;
    Title   [1][ 7] = "掲示板";
    Contents[1][ 7] = "bbs.jsp";

    MenuNo  [1][ 8] = 14;
    Title   [1][ 8] = "ﾎﾃﾙへ一言";
    Contents[1][ 8] = "mailto.jsp";

    MenuNo  [1][ 9] = 22;
    Title   [1][ 9] = "FAQ";
    Contents[1][ 9] = "faq.jsp";

    MenuNo  [1][10] = 24;
    Title   [1][10] = "求人情報";
    Contents[1][10] = "recruit.jsp";

    MenuNo  [1][11] = 26;
    Title   [1][11] = "ﾒｰﾙ予約";
    Contents[1][11] = "mailreserve.jsp";

    MenuNo  [1][12] = 18;
    Title   [1][12] = "ｸｰﾎﾟﾝ";
    Contents[1][12] = "coupon.jsp";

    MenuNo  [1][13] = 30;
    Title   [1][13] = "料金情報";
    Contents[1][13] = "priceinfo.jsp";

    MenuNo  [1][14] = 32;
    Title   [1][14] = "設備情報";
    Contents[1][14] = "service.jsp";

    MenuNo  [1][15] = 34;
    Title   [1][15] = "料金ｼﾐｭﾚｰｼｮﾝ";
    Contents[1][15] = "simulate.jsp";

    MenuNo  [1][16] = 36;
    Title   [1][16] = "ﾒﾝﾊﾞｰｽﾞ特典";
    Contents[1][16] = "premium.jsp";

    MenuNo  [1][17] = 38;
    Title   [1][17] = "無料ﾚﾝﾀﾙ";
    Contents[1][17] = "rental.jsp";

    MenuNo  [1][18] = 40;
    Title   [1][18] = "ｺﾝﾃﾝﾂ";
    Contents[1][18] = "contents.jsp";

    MenuNo  [1][19] = 42;
    Title   [1][19] = "部屋情報";
    Contents[1][19] = "roominfo.jsp";

    MenuNo  [1][20] = 8;
    Title   [1][20] = "ｸﾚｼﾞｯﾄｶｰﾄﾞ";
    Contents[1][20] = "credit.jsp";

    MenuNo  [1][21] = 44;
    Title   [1][21] = "ﾒﾝﾊﾞｰ情報";
    Contents[1][21] = "memberinfo.jsp";

    MenuNo  [1][22] = 46;
    Title   [1][22] = "利用履歴";
    Contents[1][22] = "memberuse.jsp";

    MenuNo  [1][23] = 48;
    Title   [1][23] = "ﾎﾟｲﾝﾄ履歴";
    Contents[1][23] = "memberpoint.jsp";

    MenuNo  [1][24] = 50;
    Title   [1][24] = "全室達成";
    Contents[1][24] = "memberallroom.jsp";

    MenuNo  [1][25] = 52;
    Title   [1][25] = "ｵｰﾅｰｽﾞﾙｰﾑ";
    Contents[1][25] = "memberowner.jsp";

    MenuNo  [1][26] = 54;
    Title   [1][26] = "ﾗﾝｷﾝｸﾞ";
    Contents[1][26] = "memberranking.jsp";

    MenuNo  [1][27] = 56;
    Title   [1][27] = "ﾒﾝﾊﾞｰ登録(QR)";
    Contents[1][27] = "memberregist.jsp";

    MenuNo  [1][28] = 58;
    Title   [1][28] = "ｵﾘｼﾞﾅﾙｸｰﾎﾟﾝ";
    Contents[1][28] = "originalcoupon.jsp";

    MenuNo  [1][29] = 60;
    Title   [1][29] = "予備2";
    Contents[1][29] = "general2.jsp";

    MenuNo  [1][30] = 72;
    Title   [1][30] = "予備3";
    Contents[1][30] = "general3.jsp";

    MenuNo  [1][31] = 74;
    Title   [1][31] = "予備4";
    Contents[1][31] = "general4.jsp";

    MenuNo  [1][32] = 76;
    Title   [1][32] = "予備5";
    Contents[1][32] = "general5.jsp";

    MenuNo  [1][33] = 78;
    Title   [1][33] = "予備6";
    Contents[1][33] = "general6.jsp";

    MenuNo  [1][34] = 70;
    Title   [1][34] = "ﾒﾝﾊﾞｰﾛｸﾞｲﾝ";
    Contents[1][34] = "memberlogin.jsp";

    MenuNo  [1][35] = 66;
    Title   [1][35] = "全店全室達成";
    Contents[1][35] = "memberallallroom.jsp";

    MenuNo  [1][36] = 80;
    Title   [1][36] = "メールマガジン利用規約";
    Contents[1][36] = "mailmag_term.jsp";

    for (int r = 37; r < 100; r++)
    {
        MenuNo  [1][r] = (r*2)+14;
        Title   [1][r] = "予備" + (r-28);
        Contents[1][r] = "general"+ (r-28) + ".jsp";
    }

    MenuNo  [2][ 0] = 81;
    Title   [2][ 0] = "賞品交換メッセージ";
    Contents[2][ 0] = "goods_message.jsp";

    MenuNo  [2][ 1] = 82;
    Title   [2][ 1] = "賞品登録";
    Contents[2][ 1] = "goods_list.jsp";
%>
