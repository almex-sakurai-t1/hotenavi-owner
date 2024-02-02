<%@ page contentType="text/html; charset=Windows-31J" %>
<%
    int data_type = 63;

    // [event_edit_info.disp_idx][event_edit_info.mag□の□から1引いた数値]
    String[][] Explain    = new String[10][8];
    int[][]    Decoration = new int[10][8];        //0:装飾なし,1:装飾あり
    int[][]    Method     = new int[10][8];

    //Method :1  「あり」「なし」　をラジオボタンでチェックする

    //event_edit_info.msg1
    Explain   [0][0] = "アンケート投稿トップ画面に表示するメッセージ（ビジター用）";
    Decoration[0][0] = 1;

    //event_edit_info.msg2
    Explain   [0][1] = "アンケート投稿トップ画面に表示するメッセージ（メンバー用）";
    Decoration[0][1] = 1;

    //event_edit_info.msg3
    Explain   [0][2] = "";
    Decoration[0][2] = 1;

    //event_edit_info.msg4
    Explain   [0][3] = "";
    Method    [0][3] = 1;

    //event_edit_info.msg5
    Explain   [0][4] = "アンケート投稿完了時に投稿済時に表示するメッセージ（ビジター用）";
    Decoration[0][4] = 1;

    //event_edit_info.msg6
    Explain   [0][5] = "アンケート投稿完了時に投稿済時に表示するメッセージ（メンバー用）";
    Decoration[0][5] = 1;

    //event_edit_info.msg7
    Explain   [0][6] = "アンケート投稿完了時に表示するメッセージ（ビジター用）";
    Decoration[0][6] = 1;

    //event_edit_info.msg8
    Explain   [0][7] = "アンケート投稿完了時に表示するメッセージ（メンバー用）";
    Decoration[0][7] = 1;
%>
