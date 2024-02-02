<%@ page contentType="text/html; charset=Windows-31J" %>
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<script type="text/javascript">
<!--
    var dd = new Date();
    setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
//-->
</SCRIPT>
<%
    }
    ReplaceString rs = new ReplaceString();
    DateEdit  de   = new DateEdit();
    int  nowdate   = Integer.parseInt(de.getDate(2));
    int  now_year  = nowdate / 10000;
    int  now_month = nowdate / 100 % 100;
    int  now_day   = nowdate % 100;
    int  nowtime   = Integer.parseInt(de.getTime(1));

    String hotelid     = (String)session.getAttribute("SelectHotel");
    String param_year  = ReplaceString.getParameter(request,"Year");
    String param_month = ReplaceString.getParameter(request,"Month");
    String param_day   = ReplaceString.getParameter(request,"Day");
    if  (param_day == null)
    {
         param_day     = "1";
    }
    if(!CheckString.numCheck(param_year) || !CheckString.numCheck(param_month) || !CheckString.numCheck(param_day) )
    {
        param_year = "0";
        param_month = "0";
        param_day = "0";
%>
        <script type="text/javascript">

        <!--
        var dd = new Date();
        setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
        //-->
        </SCRIPT>
<%
    }
    int  target_year  = Integer.parseInt(param_year);
    int  target_month = Integer.parseInt(param_month);
    int  target_day   = Integer.parseInt(param_day);
    int  target_date  = target_year * 10000 + target_month * 100 + target_day;

    NumberFormat nf4 = new DecimalFormat("0000");
    NumberFormat nf2 = new DecimalFormat("00");
    String year  = nf4.format(Integer.valueOf(param_year).intValue());
    String month = nf2.format(Integer.valueOf(param_month).intValue());
    String day   = nf2.format(Integer.valueOf(param_day).intValue());

    String loginhotel = (String)session.getAttribute("LoginHotelId");
    DbAccess db_sec =  new DbAccess();
    // セキュリティ情報の取得
    ResultSet DbUserSecurity = ownerinfo.getUserSecurity(db_sec, loginhotel, ownerinfo.DbUserId);
    // セキュリティチェック
    if( DbUserSecurity == null )
    {
         db_sec.close();
%>
        <jsp:forward page="../../common/pc/error/notlogin.html" />
<%
    }
    if( DbUserSecurity.first() == false )
    {
         db_sec.close();
%>
        <jsp:forward page="../../common/pc/error/notlogin.html" />
<%
    }

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    String            query       = "";
    connection  = DBConnection.getConnection();

    //ホテル名・ホスト種別の取得
    int     type  = 0;
    String  hname = "";
    try
    {
        query = "SELECT * FROM hotel WHERE hotel_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            type        = result.getInt("host_kind");
            hname       = result.getString("name");
            hname       = rs.replaceKanaFull(hname);
            if  (type  == 0 || type > 3)
            {
                type = 0;
            }
            else
            {
                type = type -1;
            }
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception e=" + e.toString() );
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    String[][] ReportList = new String[3][3];
    //日報　シリウス標準
    ReportList[0][0] =                   "1.売上日計集計\r\n";
    ReportList[0][0] = ReportList[0][0] +"2.フロント入出金明細\r\n";
    ReportList[0][0] = ReportList[0][0] +"3.売上分析\r\n";
    ReportList[0][0] = ReportList[0][0] +"4.タイムチャート\r\n";
    ReportList[0][0] = ReportList[0][0] +"5.売上日計明細\r\n";
    ReportList[0][0] = ReportList[0][0] +"6.車両日報\r\n";
    ReportList[0][0] = ReportList[0][0] +"7.部屋別稼動明細\r\n";
    ReportList[0][0] = ReportList[0][0] +"8.商品日計明細\r\n";
    ReportList[0][0] = ReportList[0][0] +"9.クレジット明細日計\r\n";
    ReportList[0][0] = ReportList[0][0] +"10.精算機売上日計\r\n";
    ReportList[0][0] = ReportList[0][0] +"11.精算機入出金\r\n";
    ReportList[0][0] = ReportList[0][0] +"12.作業班日計\r\n";
    ReportList[0][0] = ReportList[0][0] +"13.顧客操作日報\r\n";
    //日報　新シリ標準
    ReportList[0][1] =                   "1.売上日計集計\r\n";
    ReportList[0][1] = ReportList[0][1] +"2.売上分析\r\n";
    ReportList[0][1] = ReportList[0][1] +"3.タイムチャート\r\n";
    ReportList[0][1] = ReportList[0][1] +"4.売上日計明細\r\n";
    ReportList[0][1] = ReportList[0][1] +"5.車両日報\r\n";
    ReportList[0][1] = ReportList[0][1] +"6.部屋別稼動明細\r\n";
    ReportList[0][1] = ReportList[0][1] +"7.商品日計明細\r\n";
    ReportList[0][1] = ReportList[0][1] +"8.クレジット明細日計\r\n";
    ReportList[0][1] = ReportList[0][1] +"9.作業班日計\r\n";
    ReportList[0][1] = ReportList[0][1] +"10.顧客操作日報\r\n";
    //日報　ルームサーバ標準
    ReportList[0][2] =                   "1.売上日計集計\r\n";
    ReportList[0][2] = ReportList[0][2] +"2.売上分析\r\n";
    ReportList[0][2] = ReportList[0][2] +"3.タイムチャート\r\n";
    ReportList[0][2] = ReportList[0][2] +"4.売上日計明細\r\n";
    ReportList[0][2] = ReportList[0][2] +"5.車両日報\r\n";
    ReportList[0][2] = ReportList[0][2] +"6.部屋別稼動明細\r\n";
    ReportList[0][2] = ReportList[0][2] +"7.商品日計明細\r\n";
    ReportList[0][2] = ReportList[0][2] +"8.クレジット明細日計\r\n";
    ReportList[0][2] = ReportList[0][2] +"9.精算機売上日計\r\n";
    ReportList[0][2] = ReportList[0][2] +"10.精算機入出金\r\n";
    ReportList[0][2] = ReportList[0][2] +"11.作業班日計\r\n";
    ReportList[0][2] = ReportList[0][2] +"12.顧客操作日報\r\n";
    //月報　シリウス標準
    ReportList[1][0] =                   "1.月次売上集計\r\n";
    ReportList[1][0] = ReportList[1][0] +"2.月次売上明細\r\n";
    ReportList[1][0] = ReportList[1][0] +"3.月次部屋別稼働明細\r\n";
    ReportList[1][0] = ReportList[1][0] +"4.月次部屋ランク別稼働明細\r\n";
    ReportList[1][0] = ReportList[1][0] +"5.月次料金モード別稼働明細\r\n";
    ReportList[1][0] = ReportList[1][0] +"6.月次日別稼働明細\r\n";
    ReportList[1][0] = ReportList[1][0] +"7.月次景品交換履歴\r\n";
    ReportList[1][0] = ReportList[1][0] +"8.月次売上分析\r\n";
    ReportList[1][0] = ReportList[1][0] +"9.月次料金モード別分析\r\n";
    ReportList[1][0] = ReportList[1][0] +"10.月次商品売上明細\r\n";
    ReportList[1][0] = ReportList[1][0] +"11.料金モード推移表\r\n";
    ReportList[1][0] = ReportList[1][0] +"12.前年同月比較表\r\n";
    ReportList[1][0] = ReportList[1][0] +"13.月次顧客利用分析\r\n";
    ReportList[1][0] = ReportList[1][0] +"14.料金プラン分析\r\n";
    ReportList[1][0] = ReportList[1][0] +"15.視聴状況\r\n";
    ReportList[1][0] = ReportList[1][0] +"16.現金出納集計\r\n";
    //月報　新シリ標準
    ReportList[1][1] =                   "1.月次売上集計\r\n";
    ReportList[1][1] = ReportList[1][1] +"2.月次売上明細\r\n";
    ReportList[1][1] = ReportList[1][1] +"3.月次部屋別稼働明細\r\n";
    ReportList[1][1] = ReportList[1][1] +"4.月次部屋ランク別稼働明細\r\n";
    ReportList[1][1] = ReportList[1][1] +"5.月次料金モード別稼働明細\r\n";
    ReportList[1][1] = ReportList[1][1] +"6.月次日別稼働明細\r\n";
    ReportList[1][1] = ReportList[1][1] +"7.月次景品交換履歴\r\n";
    ReportList[1][1] = ReportList[1][1] +"8.月次売上分析\r\n";
    ReportList[1][1] = ReportList[1][1] +"9.月次料金モード別分析\r\n";
    ReportList[1][1] = ReportList[1][1] +"10.月次商品売上明細\r\n";
    ReportList[1][1] = ReportList[1][1] +"11.料金モード推移表\r\n";
    ReportList[1][1] = ReportList[1][1] +"12.前年同月比較表\r\n";
    ReportList[1][1] = ReportList[1][1] +"13.月次顧客利用分析\r\n";
    //月報　ルームサーバ標準
    ReportList[1][2] =                   "1.月次売上集計\r\n";
    ReportList[1][2] = ReportList[1][2] +"2.月次売上明細\r\n";
    ReportList[1][2] = ReportList[1][2] +"3.月次部屋別稼働明細\r\n";
    ReportList[1][2] = ReportList[1][2] +"4.月次部屋ランク別稼働明細\r\n";
    ReportList[1][2] = ReportList[1][2] +"5.月次料金モード別稼働明細\r\n";
    ReportList[1][2] = ReportList[1][2] +"6.月次日別稼働明細\r\n";
    ReportList[1][2] = ReportList[1][2] +"7.月次景品交換履歴\r\n";
    ReportList[1][2] = ReportList[1][2] +"8.月次売上分析\r\n";
    ReportList[1][2] = ReportList[1][2] +"9.月次料金モード別分析\r\n";
    ReportList[1][2] = ReportList[1][2] +"10.月次商品売上明細\r\n";
    ReportList[1][2] = ReportList[1][2] +"11.料金モード推移表\r\n";
    ReportList[1][2] = ReportList[1][2] +"12.前年同月比較表\r\n";
    ReportList[1][2] = ReportList[1][2] +"13.月次顧客利用分析\r\n";
    ReportList[1][2] = ReportList[1][2] +"14.現金出納集計\r\n";

    //多店舗帳票
    ReportList[2][0] =                   "1.多店舗集計書\r\n";

    int    data_type = 900 + report_flag;
%>
