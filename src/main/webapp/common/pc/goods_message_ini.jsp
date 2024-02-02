<%@ page contentType="text/html; charset=Windows-31J" %>
<%
    int DispIdxMax   = 1;
    int DataTypeFrom = 81;
    int DataTypeTo   = 82;

    // disp_idx は、data_typeから81を引いた値と一致する。
    // [event_edit_info.disp_idx][event_edit_info.mag□の□から1引いた数値]

    String[][] Explain    = new String[10][8];
    int[][]    MsgInput   = new int[10][8];        //0:入力なし
    int[][]    Decoration = new int[10][8];        //0:装飾なし,1:装飾あり
    int[][]    Method     = new int[10][8];

    //Method :1  「あり」「なし」　をラジオボタンでチェックする
    //Method :2  「必須」「あり」「なし」をラジオボタンでチェックする

    //event_edit_info.msg1
    Explain   [0][0] = "ポイント交換説明文";
    Decoration[0][0] = 1;
    MsgInput  [0][0] = 1;

    //event_edit_info.msg2
    Explain   [0][1] = "共通タイトル（メールタイトル「○○○受付完了」などに使用されます）";
    Decoration[0][1] = 0;
    MsgInput  [0][1] = 0;

    //event_edit_info.msg3
    Explain   [0][2] = "ポイント交換注意書き";
    Decoration[0][2] = 1;
    MsgInput  [0][2] = 1;

    //event_edit_info.msg4
    Explain   [0][3] = "ポイント残高とのチェック";
    Method    [0][3] = 1;
    MsgInput  [0][3] = 0;

    //event_edit_info.msg5
    Explain   [0][4] = "入力完了時、「ホテル引渡し」の場合のメール内メッセージ";
    Decoration[0][4] = 0;
    MsgInput  [0][4] = 1;

    //event_edit_info.msg6
    Explain   [0][5] = "入力完了時、「配送」の場合のメール内メッセージ";
    Decoration[0][5] = 0;
    MsgInput  [0][5] = 1;

    //event_edit_info.msg7
    Explain   [0][6] = "入力完了時、「ホテル引渡し」の場合の表示メッセージ";
    Decoration[0][6] = 1;
    MsgInput  [0][6] = 1;

    //event_edit_info.msg8
    Explain   [0][7] = "入力完了時、「配送」の場合の表示メッセージ";
    Decoration[0][7] = 1;
    MsgInput  [0][7] = 1;

    //event_edit_info.msg1
    Explain   [1][0] = "呼称：景品or商品 etc";
    Decoration[1][0] = 0;
    MsgInput  [1][0] = 0;

    //event_edit_info.msg2
    Explain   [1][1] = "見出し：個人情報入力時コメント欄呼称、本文：個人情報入力時コメント欄説明";
    Decoration[1][1] = 1;
    MsgInput  [1][1] = 1;

    //event_edit_info.msg3
    Explain   [1][2] = "個人情報入力時注意書き";
    Decoration[1][2] = 1;
    MsgInput  [1][2] = 1;

    Explain   [1][3] = "";

    //event_edit_info.msg5
    Explain   [1][4] = "個人情報：お名前";
    Method    [1][4] = 2;
    MsgInput  [1][4] = 0;

    //event_edit_info.msg6
    Explain   [1][5] = "個人情報：ニックネーム";
    Method    [1][5] = 2;
    MsgInput  [1][5] = 0;

    //event_edit_info.msg7
    Explain   [1][6] = "個人情報：メールアドレス入力";
    Method    [1][6] = 2;
    MsgInput  [1][6] = 0;

    //event_edit_info.msg8
    Explain   [1][7] = "個人情報：電話番号入力";
    Method    [1][7] = 2;
    MsgInput  [1][7] = 0;
%>
