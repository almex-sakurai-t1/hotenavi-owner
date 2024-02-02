<%@ page contentType="text/html; charset=Windows-31J" %>
<%
    // [event_edit_info.disp_idx][event_edit_info.mag□の□から1引いた数値]
    String[][] Explain       = new String[10][8];
    int[][]    Decoration    = new int[10][8];        //0:装飾なし,1:装飾あり
    String[][] MsgStandard   = new String[10][8];
    int[][]    ImediaOnly    = new int[10][8];
    int[][]    Method        = new int[10][8];  //Method :1  「あり」「なし」　をラジオボタンでチェックする
    String[][] FooterTitle   = new String[10][8]; //Footer のタイトル
    String[][] FooterDefault = new String[10][8]; //Footer のメッセージ


    //event_edit_info.msg1
    Explain   [0][0] = "メールマガジン初期トップページのメッセージ";
    Decoration[0][0] = 1;
    MsgStandard[0][0] = "当ホテルではお得な情報を、メールでお客様にお届けするサービスを行っております。<br>\r\nホテルのイベント情報をいち早くGET!<br>\r\nさぁ登録しましょう!! <br>";
    ImediaOnly[0][0] = 0;

    //event_edit_info.msg2
    Explain   [0][1] = "QRメンバー登録時トップページのメッセージ";
    Decoration[0][1] = 1;
    MsgStandard[0][1]="当ホテルではお得な情報を、メールでお客様にお届けするサービスを行っております。<br>\r\nホテルのイベント情報をいち早くGET!<br>\r\nさぁ登録しましょう!! <br>";
    ImediaOnly[0][1] = 1;

    //event_edit_info.msg3
    Explain   [0][2] = "メンバー以外の場合の誘導メッセージ";
    Decoration[0][2] = 1;
    MsgStandard[0][2]="→メンバー以外の方はこちらから";
    ImediaOnly[0][2] = 0;

    //event_edit_info.msg4
    Explain   [0][3] = "メンバーの場合の誘導メッセージ";
    Decoration[0][3] = 1;
    MsgStandard[0][3]="<strong>→メンバーの方はこちらから</strong>";
    ImediaOnly[0][3] = 0;

    //event_edit_info.msg5
    Explain   [0][4] = "メンバー登録完了時のメッセージ";
    Decoration[0][4] = 1;
    MsgStandard[0][4]="ありがとうございます。<br>\r\nホテルからのお得な情報をお届けいたします。<br><br><br>";
    ImediaOnly[0][4] = 1;

    //event_edit_info.msg6
    Explain   [0][5] = "メンバー登録完了時メール内メッセージ（入力がない場合は送られません）";
    Decoration[0][5] = 0;
    MsgStandard[0][5]="";
    ImediaOnly[0][5] = 1;

    //event_edit_info.msg7
    Explain   [0][6] = "メルマガ登録完了時のメッセージ";
    Decoration[0][6] = 1;
    MsgStandard[0][6]="ありがとうございます。<br>\r\nホテルからのお得な情報をお届けいたします。<br><br><br>";
    ImediaOnly[0][6] = 0;

    //event_edit_info.msg8
    Explain   [0][7] = "メルマガ登録完了時メール内メッセージ（入力がない場合は送られません）";
    Decoration[0][7] = 0;
    MsgStandard[0][7]="";
    ImediaOnly[0][7] = 0;
    FooterTitle[0][7] = "「ご注意」表示内容（最大120文字。メッセージがない場合は表示しません）";
    FooterDefault[0][7] = "★このメールに返信してもご回答できませんのでご注意ください。";
%>
