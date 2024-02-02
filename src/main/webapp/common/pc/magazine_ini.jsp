<%@ page contentType="text/html; charset=Windows-31J" %>
<%
    // [event_edit_info.disp_idx][event_edit_info.mag□の□から1引いた数値]
    String[][] Explain    = new String[10][8];
    int[][]    Decoration = new int[10][8];        //0:装飾なし,1:装飾あり
    int[][]    Method     = new int[10][8];

    //Method :1  「あり」「なし」　をラジオボタンでチェックする

    //event_edit_info.msg1
    Explain   [0][0] = "メールマガジン初期トップページのメッセージ（入力がない場合は標準の文言を表示）";
    Decoration[0][0] = 1;

    //event_edit_info.msg2
    Explain   [0][1] = "";
    Decoration[0][1] = 1;

    //event_edit_info.msg3
    Explain   [0][2] = "";
    Decoration[0][2] = 1;

    //event_edit_info.msg4
    Explain   [0][3] = "";
    Method    [0][3] = 1;

    //event_edit_info.msg5
    Explain   [0][4] = "";
    Decoration[0][4] = 1;

    //event_edit_info.msg6
    Explain   [0][5] = "";
    Decoration[0][5] = 1;

    //event_edit_info.msg7
    Explain   [0][6] = "メルマガ登録完了時のメッセージ（入力がない場合は標準の文言を表示）";
    Decoration[0][6] = 1;

    //event_edit_info.msg8
    Explain   [0][7] = "メルマガ登録完了時メール内メッセージ（入力がない場合は送られません）";
    Decoration[0][7] = 0;
%>
