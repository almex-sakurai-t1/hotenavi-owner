<%@ page contentType="text/html;charset=Windows-31J"%>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %><%@ page import="org.apache.commons.net.ftp.FTPClient" %><%@ page import="org.apache.commons.net.ftp.FTPReply" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%!
public static String fileCheck(String host,String user,String password,String filename)
throws IOException {
    FTPClient fp = new FTPClient();
    try
    {
        fp.connect(host);
        if (!FTPReply.isPositiveCompletion(fp.getReplyCode())) { // コネクトできたか？
            return "つながらないっす";
        }
        else
            if (fp.login(user, password) == false) { // ログインできたか？
                return "ログインできません";
            }
            else
            {
                String getModificationTime = fp.getModificationTime(filename);
                if (getModificationTime == null)
                {
                    return "ファイルがない";
                }
                else
                {
                    return getModificationTime;
                }
            }
    }
    finally
    {
        fp.disconnect();
    }
}
%>
<%!
public static int sendTrans(String host,String user,String password,String path,String filename)
throws IOException {
    FTPClient fp = new FTPClient();
    FileInputStream is = null;
    try
    {
        fp.connect(host);
        if (!FTPReply.isPositiveCompletion(fp.getReplyCode())) { // コネクトできたか？
            return 1;
        }
        if (fp.login(user, password) == false) { // ログインできたか？
            return 2;
        }
// ファイル送信
        is = new FileInputStream(path+filename);// クライアント側
        if(fp.storeFile(filename, is))// サーバー側
        {
           return 0;
        }
        else
        {
           return 3;
        }
    }
    finally
    {
        fp.disconnect();
    }
}
%>
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String jupiter_server   = "jupiter.hotenavi.com";
    String client_path      = "/hotenavi/_debug_/open/new/";
    boolean existRedirectFlag = false;
    String ftp_user         = "";
    String ftp_password     = "";
    int nowdate = Integer.parseInt(new DateEdit().getDate(2));
    String uploadMessage    = "";

    String header_msg = "";
    String query;

//ホテル別設定ファイル
    int trial_date			= 99999999;   		//リニューアル開始日付
    int start_date			= 99999999;   		//稼動開始日付
    int chk_age_flg			= 0;          		//年齢確認フラグ （1:クッションページあり）
    String html_head		= "";
    String html_login_form	= "";
    String offlineflg		= "0";          	//ログイン有無フラグ（0:メンバーページなし 1:メンバーパージあり）
    String mailmagazineflg	= "1";         		//メルマガ有無フラグ（0：無し 1:有）
    String mailtoflg		= "1";          	//ホテルへ一言有無フラグ（0：無し 1:有）
    String mailnameflg		= "0";         		//ホテルへ一言ホテル件名有無（0：件名にホテル名無し(default) 1:件名にホテル名有）
    String mailname			= "";         		//ホテルへ一言ホテル名
    String viewflg			= "0";         		//0:通常　1:参照バージョン 2:参照バージョン（メンバーも） 9:休止中
    String bbsgroupflg		= "0";         		//多店舗掲示板の使用(0:通常　1:多店舗バージョン)
    String prize_hotelid	= "";         		//商品交換用ホテルID（未入力の場合はhotel_idを使用）
    int coupon_map_flg		= 0;      	 		//0:Yahoo!MAPを使用,1:画像ファイルを使用
    String coupon_map_img1	= "";     			//クーポン画像1
    String coupon_map_img2	= "";     			//クーポン画像2
    int bbs_temp_flg		= 0;           		//0:通常掲示板,1:仮投稿掲示板（掲示板追加時にdel_flagに1をセット）
    int ranking_hidden_flg	= 0;     			//0:通常,1:ランキング情報を出力しない
    int ownercount	= 0;     			//オーナーズルーム達成回数
    int convertRoomCount    = 0;
    int count_whatsnew_for_hoteltop = 0;

    String mode               = "NEW";           //NEW:新規 UPD:更新

    // ホテルID取得
    String hotelid = (String)session.getAttribute("SelectHotel");
    if  (hotelid.compareTo("all") == 0)
    {
        hotelid    = request.getParameter("HotelId");
    }
    
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();
 
    query = "SELECT * FROM hotel_element WHERE hotel_id= ?";
    prestate    = connection.prepareStatement(query);
    prestate.setString( 1, hotelid);
    result      = prestate.executeQuery();
    if (result != null)
    {
        if( result.next() != false )
        {
            header_msg = "更新";
            mode = "UPD";
            trial_date              	= result.getInt("trial_date");
            start_date              	= result.getInt("start_date");
            chk_age_flg              	= result.getInt("chk_age_flg");
            html_head  		      		= result.getString("html_head");
            html_login_form             = result.getString("html_login_form");
            offlineflg           		= result.getString("offlineflg");
            mailmagazineflg           	= result.getString("mailmagazineflg");
            mailtoflg      				= result.getString("mailtoflg");
            mailnameflg             	= result.getString("mailnameflg");
            mailname           			= result.getString("mailname");
            viewflg      				= result.getString("viewflg");
            bbsgroupflg      			= result.getString("bbsgroupflg");
		      	prize_hotelid      			= result.getString("prize_hotelid");
		      	coupon_map_flg      		= result.getInt("coupon_map_flg");
			      coupon_map_img1      		= result.getString("coupon_map_img1");
			      coupon_map_img2      		= result.getString("coupon_map_img2");
			      bbs_temp_flg      			= result.getInt("bbs_temp_flg");
			      ranking_hidden_flg      	= result.getInt("ranking_hidden_flg");
			      ownercount		      		= result.getInt("ownercount");
			      count_whatsnew_for_hoteltop	= result.getInt("count_whatsnew_for_hoteltop");
        }
        else
        {
            header_msg = "新規作成";
            mode = "NEW";
        }
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);

    query = "SELECT * FROM hotel WHERE hotel_id = ?";
    prestate    = connection.prepareStatement(query);
    prestate.setString( 1, hotelid);
    result      = prestate.executeQuery();
    if (result != null)
    {
        if( result.next() != false )
        {
            ftp_user     = result.getString("hotel_id");
            ftp_password = result.getString("ftp_passwd");
        }
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);

    String  fileTime = fileCheck(jupiter_server,ftp_user,ftp_password,"index.jsp");
    if (fileTime.equals("ファイルがない") && start_date <= nowdate)
    {
        existRedirectFlag = true;
        if (request.getParameter("UPLOAD") != null)
        {
            int ret = sendTrans(jupiter_server,ftp_user,ftp_password,client_path,"index.jsp");
            if (ret == 0)
            {
                uploadMessage = "<font color=red>リダイレクト用index.jsp を http://www.hotenavi.com/"+hotelid+ "/ にアップロードしました。</font><br>";
            }
            ret = sendTrans(jupiter_server,ftp_user,ftp_password,client_path,"index.html");
            if (ret == 0)
            {
                uploadMessage += "<font color=red>リダイレクト用index.html を http://www.hotenavi.com/"+hotelid+ "/ にアップロードしました。</font><br>";
            }
        }
    }
    else
    {
        existRedirectFlag = false;
    }

    if (trial_date != 99999999)
    {
        query = " SELECT count(*) FROM room r_main ";
        query += " WHERE";
        query += " r_main.hotelid = ?";
        query += " AND";
        query += " NOT EXISTS";
        query += " (SELECT 1 FROM room r_sub ";
        query += " WHERE r_main.hotelid=r_sub.hotelid ";
        query += " AND r_main.room_no = r_sub.room_no";
        query += " AND r_main.id < r_sub.id";
        query += " )";
        query += " AND r_main.start_date < ? ";

        prestate    = connection.prepareStatement(query);
        prestate.setString( 1, hotelid);
        prestate.setInt( 2, trial_date);
        result      = prestate.executeQuery();
        if (result != null)
        {
            if( result.next() != false )
            {
                convertRoomCount = result.getInt(1);
            }
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    String localName = "";
    String prefName = "";
    String hotelName = "";
    String address1 = "";
    query  = "SELECT local.name,basic.name,basic.pref_name,basic.address1 FROM hh_master_local local";
    query += " INNER JOIN hh_master_pref pref ON pref.local_id=local.local_id";
    query += " INNER JOIN hh_hotel_basic basic ON pref.pref_id = basic.pref_id";
    query += " WHERE basic.hotenavi_id = ?";
    prestate    = connection.prepareStatement(query);
    prestate.setString( 1, hotelid);
    result      = prestate.executeQuery();

    if (result != null)
    {
        if( result.next() != false )
        {
            localName = result.getString("local.name");
            prefName = result.getString("basic.pref_name");
            hotelName = result.getString("basic.name");
            address1 = result.getString("basic.address1");
        }
    }
    if (html_head.indexOf("<script") == 0) 
    {
        html_head = "";
    } 
    if (html_head.equals(""))
    {
        html_head = "<meta name=\"keywords\" content=\" レジャーホテル,ラブホテル,ラブホ,カップルズホテル, "+localName+", %hotel_name%, "+prefName+", "+address1+"\" />\r\n";
        html_head +="<meta name=\"description\" content=\""+prefName+" "+address1+ "のラブホテル %hotel_name% （%hotel_name_kana%）\"/>\r\n";
        html_head +="<title>%hotel_name% - %page_title% "+prefName+ " " +address1+ " (" +localName+ ") のラブホテル・ラブホ</title>\r\n";
        html_head +="<link rel=\"stylesheet\" href=\"/contents/"+hotelid+"/css/"+hotelid+"-%design_type%.css\" type=\"text/css\" id=\"cssStyle\" />\r\n";
        html_head +="<link rel=\"stylesheet\" media=\"screen and (max-width: 480px) and (min-width: 0px)\" href=\"/contents/"+hotelid+"/css/"+hotelid+"-%design_type%-sp.css\" id=\"cssStyleSp\" />\r\n";
        html_head +="<link rel=\"stylesheet\" href=\"/contents/common/css/jquery.bxslider.css\" type=\"text/css\" /><link rel=\"stylesheet\" href=\"/contents/common/css/nodisp18.css\" type=\"text/css\" />\r\n";
        html_head +="<script type=\"text/javascript\" src=\"/contents/common/js/jquery.bxslider.min.js\"></script>\r\n";
        html_head +="<script type=\"text/javascript\" src=\"/contents/common/js/style-%design_type%.js\"></script>\r\n";
        html_head +="<script type=\"text/javascript\" src=\"/contents/"+hotelid+"/js/"+hotelid+"-%design_type%.js\"></script>\r\n";//HTMLヘッダ情報
    }
    DBConnection.releaseResources(result,prestate,connection);

    String pwInput = "";


%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>初期設定</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">

<script type="text/javascript">
function MM_openPreview(input,hotelid,rec_flag){
  if( input == 'preview' )
  {
    document.form1.target = '_blank';
    document.form1.action = 'etc_edit_preview.jsp?HotelId='+hotelid;
  }
  document.form1.submit();
}

function MM_openInput(input,hotelid,mode){ 
  document.form1.target = '_self';
  document.form1.action = 'etc_edit_input.jsp?HotelId='+hotelid+'&Mode='+mode;
  document.form1.submit();
}
function dateCheck(obj){
  if (isNaN(obj.value))
  {
     obj.value=99999999;
  }
}
function designTypeChange(obj){
 var htmlHead = document.getElementById("html_head").value;
 htmlHead = htmlHead.replace(/%design_type%/g , obj.value);
 htmlHead = htmlHead.replace(/\-a\.js/g , "-"+obj.value+".js");
 htmlHead = htmlHead.replace(/\-b\.js/g , "-"+obj.value+".js");
 htmlHead = htmlHead.replace(/\-c\.js/g , "-"+obj.value+".js");
 htmlHead = htmlHead.replace(/\-d\.js/g , "-"+obj.value+".js");
 htmlHead = htmlHead.replace(/\-e\.js/g , "-"+obj.value+".js");
 htmlHead = htmlHead.replace(/\-a\.css/g , "-"+obj.value+".css");
 htmlHead = htmlHead.replace(/\-b\.css/g , "-"+obj.value+".css");
 htmlHead = htmlHead.replace(/\-c\.css/g , "-"+obj.value+".css");
 htmlHead = htmlHead.replace(/\-d\.css/g , "-"+obj.value+".css");
 htmlHead = htmlHead.replace(/\-e\.css/g , "-"+obj.value+".css");
 htmlHead = htmlHead.replace(/\-a\-sp\.css/g , "-"+obj.value+"-sp.css");
 htmlHead = htmlHead.replace(/\-b\-sp\.css/g , "-"+obj.value+"-sp.css");
 htmlHead = htmlHead.replace(/\-c\-sp\.css/g , "-"+obj.value+"-sp.css");
 htmlHead = htmlHead.replace(/\-d\-sp\.css/g , "-"+obj.value+"-sp.css");
 htmlHead = htmlHead.replace(/\-e\-sp\.css/g , "-"+obj.value+"-sp.css");
 document.getElementById("html_head").value = htmlHead;
}

function pwInputs(obj){
 var htmlHead = document.getElementById("html_head").value;
 if (obj.value==1)
 {
    htmlHead +="<script type=\"text/javascript\">\r\nif(getCookie(\"pw\")==null){\r\nvar pw=\"\";\r\npw=prompt(\" パスワードを入力してください \",\"\");\r\nif(pw != \"<%=hotelid%>\"){\r\nwindow.location.href = \"http://happyhotel.jp/\";\r\n}else{document.cookie = \"pw=<%=hotelid%>\";\r\n}}\r\nfunction getCookie( name ){\r\n var result = null;\r\n var cookieName = name + \"=\";\r\n var cookies = document.cookie;\r\n var position = cookies.indexOf( cookieName );\r\n if( position != -1 ) {\r\n var startIndex = position + cookieName.length;\r\n var endIndex = cookies.indexOf( \";\", startIndex );\r\n if( endIndex == -1 ) {\r\n endIndex = cookies.length;\r\n }\r\n result = decodeURIComponent(\r\n cookies.substring( startIndex, endIndex ) );\r\n }\r\n return result;\r\n}\r\n<"+"/"+"script"+">";
 }
 else
 {
    var htmlHead_split = htmlHead.split("<script type=\"text/javascript\">");
    htmlHead = htmlHead_split[0];
 }
 document.getElementById("html_head").value = htmlHead;
}

function stop18Disp(obj){
 var htmlHead = document.getElementById("html_head").value;
 if (obj.value==1) //18禁あり
 {
    htmlHead = htmlHead.replace("<link rel=\"stylesheet\" href=\"/contents/common/css/jquery.bxslider.css\" type=\"text/css\" /><link rel=\"stylesheet\" href=\"/contents/common/css/nodisp18.css\" type=\"text/css\" />","<link rel=\"stylesheet\" href=\"/contents/common/css/jquery.bxslider.css\" type=\"text/css\" />");
 }
 else
 {
    htmlHead = htmlHead.replace("<link rel=\"stylesheet\" href=\"/contents/common/css/jquery.bxslider.css\" type=\"text/css\" />","<link rel=\"stylesheet\" href=\"/contents/common/css/jquery.bxslider.css\" type=\"text/css\" /><link rel=\"stylesheet\" href=\"/contents/common/css/nodisp18.css\" type=\"text/css\" />");
 }
 document.getElementById("html_head").value = htmlHead;
}

function loginFormChange(obj){

 var loginForm = "";
 if (document.getElementById("loginForm1").checked)
 {
     loginForm += "<div class=\"container\">\r\n	<form action=\"%action%\" method=\"post\" class=\"loginForm\" id=\"loginForm1\">\r\n		<div class=\"loginTitle\">メンバーズカードをお持ちの方</div>\r\n		<p class=\"comment\">メンバーの方は、メンバーズＮｏを入力して誕生日を選択してログインボタンを押してください｡</p>\r\n		<div class=\"item-table\">\r\n			<dl>\r\n				<dt>メンバーＮｏ</dt>\r\n				<dd>\r\n					<input type=\"text\" id=\"customId\" name=\"customId\" maxlength=\"9\" />\r\n				</dd>\r\n			</dl>\r\n			<dl>\r\n				<dt>誕生日</dt>\r\n				<dd>\r\n					<div class=\"select-box1\">\r\n						<label>\r\n							<select id=\"birthday1\" name=\"birthday1\" class=\"form_value\">\r\n								<option value=0 selected></option>\r\n								<option value=1>1</option>\r\n								<option value=2>2</option>\r\n								<option value=3>3</option>\r\n								<option value=4>4</option>\r\n								<option value=5>5</option>\r\n								<option value=6>6</option>\r\n								<option value=7>7</option>\r\n								<option value=8>8</option>\r\n								<option value=9>9</option>\r\n								<option value=10>10</option>\r\n								<option value=11>11</option>\r\n								<option value=12>12</option>\r\n							</select>\r\n						</label>\r\n					</div>\r\n					月					<div class=\"select-box1\">\r\n						<label>\r\n							<select id=\"birthday2\" name=\"birthday2\" class=\"form_value\">\r\n								<option value=0 selected></option>\r\n								<option value=1>1</option>\r\n								<option value=2>2</option>\r\n								<option value=3>3</option>\r\n								<option value=4>4</option>\r\n								<option value=5>5</option>\r\n								<option value=6>6</option>\r\n								<option value=7>7</option>\r\n								<option value=8>8</option>\r\n								<option value=9>9</option>\r\n								<option value=10>10</option>\r\n								<option value=11>11</option>\r\n								<option value=12>12</option>\r\n								<option value=13>13</option>\r\n								<option value=14>14</option>\r\n								<option value=15>15</option>\r\n								<option value=16>16</option>\r\n								<option value=17>17</option>\r\n								<option value=18>18</option>\r\n								<option value=19>19</option>\r\n								<option value=20>20</option>\r\n								<option value=21>21</option>\r\n								<option value=22>22</option>\r\n								<option value=23>23</option>\r\n								<option value=24>24</option>\r\n								<option value=25>25</option>\r\n								<option value=26>26</option>\r\n								<option value=27>27</option>\r\n								<option value=28>28</option>\r\n								<option value=29>29</option>\r\n								<option value=30>30</option>\r\n								<option value=31>31</option>\r\n							</select>\r\n						</label>\r\n					</div>\r\n					日\r\n				</dd>\r\n			</dl>\r\n		</div>\r\n		<div class=\"loginBtn\">\r\n			</br>\r\n			<input type=\"submit\" value=\"ログイン\" onclick=\"return datacheck(this.form)\" class=\"btn-positive\">\r\n			<input type=\"hidden\" name=\"requestUri\" value=\"\">\r\n			<input type=\"hidden\" name=\"formId\" value=\"loginForm1\">\r\n		</div>\r\n	</form>\r\n</div>\r\n";
 }
 if (document.getElementById("loginForm2").checked)
 {
     loginForm += "<div class=\"container\">\r\n	<form action=\"%action%\" method=\"post\" class=\"loginForm\" id=\"loginForm2\">\r\n		<div class=\"loginTitle\">メンバーズカードをお持ちの方</div>\r\n		<p class=\"comment\">メンバーの方は、メンバーズＮｏを入力して誕生日を選択してログインボタンを押してください｡</p>\r\n		<div class=\"item-table\">\r\n			<dl>\r\n				<dt>メンバーＮｏ</dt>\r\n				<dd>\r\n					<input type=\"text\" id=\"customId\" name=\"customId\" maxlength=\"6\" />\r\n				</dd>\r\n			</dl>\r\n			<dl>\r\n				<dt>誕生日</dt>\r\n	  			<dd>\r\n	  				<div class=\"select-box1\">\r\n						<label>\r\n							<select id=\"birthday1\" name=\"birthday1\" class=\"form_value\">\r\n								<option value=0 selected></option>\r\n								<option value=1>1</option>\r\n								<option value=2>2</option>\r\n								<option value=3>3</option>\r\n								<option value=4>4</option>\r\n								<option value=5>5</option>\r\n								<option value=6>6</option>\r\n								<option value=7>7</option>\r\n								<option value=8>8</option>\r\n								<option value=9>9</option>\r\n								<option value=10>10</option>\r\n								<option value=11>11</option>\r\n								<option value=12>12</option>\r\n							</select>\r\n						</label>\r\n					</div>\r\n					月\r\n					<div class=\"select-box1\">\r\n						<label>\r\n							<select id=\"birthday2\" name=\"birthday2\" class=\"form_value\">\r\n								<option value=0 selected></option>\r\n								<option value=1>1</option>\r\n								<option value=2>2</option>\r\n								<option value=3>3</option>\r\n								<option value=4>4</option>\r\n								<option value=5>5</option>\r\n								<option value=6>6</option>\r\n								<option value=7>7</option>\r\n								<option value=8>8</option>\r\n								<option value=9>9</option>\r\n								<option value=10>10</option>\r\n								<option value=11>11</option>\r\n								<option value=12>12</option>\r\n								<option value=13>13</option>\r\n								<option value=14>14</option>\r\n								<option value=15>15</option>\r\n								<option value=16>16</option>\r\n								<option value=17>17</option>\r\n								<option value=18>18</option>\r\n								<option value=19>19</option>\r\n								<option value=20>20</option>\r\n								<option value=21>21</option>\r\n								<option value=22>22</option>\r\n								<option value=23>23</option>\r\n								<option value=24>24</option>\r\n								<option value=25>25</option>\r\n								<option value=26>26</option>\r\n								<option value=27>27</option>\r\n								<option value=28>28</option>\r\n								<option value=29>29</option>\r\n								<option value=30>30</option>\r\n								<option value=31>31</option>\r\n							</select>\r\n						</label>\r\n					</div>\r\n					日\r\n				</dd>\r\n			</dl>\r\n		</div>\r\n		<div class=\"loginBtn\">\r\n			</br>\r\n			<input type=\"submit\" value=\"ログイン\" onclick=\"return datacheck(this.form)\" class=\"btn-positive\">\r\n			<input type=\"hidden\" name=\"requestUri\" value=\"\">\r\n			<input type=\"hidden\" name=\"formId\" value=\"loginForm2\">\r\n			<input type=\"hidden\" name=\"tenpoId\" value=\"   \">\r\n	</div>\r\n	</form>\r\n</div>\r\n";
 }
 if (document.getElementById("loginForm3").checked)
 {
     loginForm += "<div class=\"container\">\r\n		<form action=\"%action%\" method=\"post\" class=\"loginForm\" id=\"loginForm3\">\r\n			<div class=\"loginTitle\">メンバーズカードをお持ちの方</div>\r\n			<p class=\"comment\">メンバーの方は、メンバーズＮｏを入力して誕生日を選択してログインボタンを押してください｡</p>\r\n			<div class=\"item-table\">\r\n			<dl>\r\n				<dt>店舗ＩＤ</dt>\r\n		    	<dd class=\"tenpoName\">\r\n		    		<div class=\"select-box1\">\r\n						<label>\r\n							<select id=\"tenpoId\" name=\"tenpoId\" class=\"form_value\">\r\n								<option value=\"000\" selected>店舗１</option>\r\n								<option value=\"001\">店舗２</option>\r\n								<option value=\"002\">店舗３</option>\r\n							</select>\r\n						</label>\r\n					</div>\r\n				</dd>\r\n			</dl>\r\n			<dl>\r\n				<dt>メンバーＮｏ</dt>\r\n				<dd>\r\n					<input type=\"text\" id=\"customId\" name=\"customId\" maxlength=\"6\" />\r\n				</dd>\r\n			</dl>\r\n			<dl>\r\n				<dt>誕生日</dt>\r\n			    <dd>\r\n			    <div class=\"select-box1\">\r\n						<label>\r\n							<select id=\"birthday1\" name=\"birthday1\" class=\"form_value\">\r\n								<option value=0 selected></option>\r\n								<option value=1>1</option>\r\n								<option value=2>2</option>\r\n								<option value=3>3</option>\r\n								<option value=4>4</option>\r\n								<option value=5>5</option>\r\n								<option value=6>6</option>\r\n								<option value=7>7</option>\r\n								<option value=8>8</option>\r\n								<option value=9>9</option>\r\n								<option value=10>10</option>\r\n								<option value=11>11</option>\r\n								<option value=12>12</option>\r\n							</select>\r\n						</label>\r\n					</div>\r\n					月\r\n					<div class=\"select-box1\">\r\n						<label>\r\n							<select id=\"birthday2\" name=\"birthday2\" class=\"form_value\">\r\n								<option value=0 selected></option>\r\n								<option value=1>1</option>\r\n								<option value=2>2</option>\r\n								<option value=3>3</option>\r\n								<option value=4>4</option>\r\n								<option value=5>5</option>\r\n								<option value=6>6</option>\r\n								<option value=7>7</option>\r\n								<option value=8>8</option>\r\n								<option value=9>9</option>\r\n								<option value=10>10</option>\r\n								<option value=11>11</option>\r\n								<option value=12>12</option>\r\n								<option value=13>13</option>\r\n								<option value=14>14</option>\r\n								<option value=15>15</option>\r\n								<option value=16>16</option>\r\n								<option value=17>17</option>\r\n								<option value=18>18</option>\r\n								<option value=19>19</option>\r\n								<option value=20>20</option>\r\n								<option value=21>21</option>\r\n								<option value=22>22</option>\r\n								<option value=23>23</option>\r\n								<option value=24>24</option>\r\n								<option value=25>25</option>\r\n								<option value=26>26</option>\r\n								<option value=27>27</option>\r\n								<option value=28>28</option>\r\n								<option value=29>29</option>\r\n								<option value=30>30</option>\r\n								<option value=31>31</option>\r\n							</select>\r\n						</label>\r\n					</div>\r\n					日\r\n				</dd>\r\n			</dl>\r\n		</div>\r\n		<div class=\"loginBtn\">\r\n			</br>\r\n			<input type=\"submit\" value=\"ログイン\" onclick=\"return datacheck(this.form)\" class=\"btn-positive\">\r\n			<input type=\"hidden\" name=\"requestUri\" value=\"\">\r\n			<input type=\"hidden\" name=\"formId\" value=\"loginForm3\">\r\n		</div>\r\n	</form>\r\n</div>\r\n";
 }
 if (document.getElementById("loginForm4").checked)
 {
     loginForm += "<div class=\"container\">\r\n	<form action=\"%action%\" method=\"post\" class=\"loginForm\" id=\"loginForm4\">\r\n		<p class=\"comment\">ユーザＩＤとパスワードを入力してログインボタンを押してください｡</p>\r\n		<div class=\"item-table\">\r\n			<dl>\r\n				<dt>ユーザＩＤ</dt>\r\n				<dd>\r\n					<input type=\"text\" id=\"userId\" name=\"userId\" maxlength=\"11\" />\r\n				</dd>\r\n			</dl>\r\n			<dl>\r\n				<dt>パスワード</dt>\r\n				<dd>\r\n					<input type=\"password\" id=\"password\" name=\"password\" maxlength=\"10\" />\r\n				</dd>\r\n			</dl>\r\n		</div>\r\n		<div class=\"loginBtn\">\r\n			</br>\r\n			<input type=\"submit\" value=\"ログイン\" onclick=\"return datacheck(this.form)\" class=\"btn-positive\">\r\n			<input type=\"hidden\" name=\"requestUri\" value=\"\">\r\n			<input type=\"hidden\" name=\"formId\" value=\"loginForm4\">\r\n		</div>\r\n	</form>\r\n</div>";
 }

 if (document.getElementById("html_login_form").value != ""){
  if (confirm("フォーム内容を変更します。すでに編集していた内容は破棄されます。\r\n本当に変更してよろしいですか？"))
  {
      document.getElementById("html_login_form").value = loginForm;
  }
  else
  {
     if (obj.checked)
     {
         obj.checked = false;
     }
     else
     {
         obj.checked = true;
     }
  }
 }
 else
 {
      document.getElementById("html_login_form").value = loginForm;
 }
}
</script>
<style>
td .size12 {
 padding:2px;
}
</style>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="180" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF"><%= header_msg %></font></td>
              <td width="15" height="20"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="../../common/pc/image/spacer.gif" width="200" height="20"></div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- ここから表 -->
      <tr>
        <td align="center" valign="top" bgcolor="#FFFFFF"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><img src="../../common/pc/image/spacer.gif" width="400" height="12">
              </td>
            </tr>
            <tr>
              <td width="8"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td colspan="2" class="size12"><font color="#CC0000"><strong>※このページを編集し終えたら、「<%= header_msg %>」ボタンを必ず押してください</strong></font></td>
                  </tr>
                  <form name=form1 method=post>
 
				  <tr align="left">
                      <td align="left" colspan="2" valign="middle" bgcolor="#969EAD"><input name="regsubmit" type=button value="<%= header_msg %>" onClick="MM_openInput('input', '<%= hotelid %>', '<%= mode %>')"></td>
                   </tr>
                  <tr align="left">
                    <td width="180"><img src="../../common/pc/image/spacer.gif" width="120" height="14"></td>
                    <td ></td>
                  </tr>
				  <tr align="left">
                    <td nowrap class="size12">リニューアル開始日付 </td>
					<td nowrap class="size12">
						<input name="trial_date" type="text" size="5" maxlength="8" value="<%= trial_date %>" style="text-align:right" onChange="return dateCheck(this);">
						※旧サイトにアクセスした場合にこの日付以降のデータは参照しません (room,roomrank,equipment,edit_event_infoで最新1件のみ読み込むコンテンツ）
						<%if (convertRoomCount > 0){%><br><input type="button" value="部屋画像一括変換" onclick="location.href='room_convert.jsp';"><%}%>
					</td>
				  </tr>
				  <tr align="left">
                    <td nowrap class="size12">新ホテナビ稼動開始日付 </td>
					<td nowrap class="size12">
						<input name="start_date" type="text" size="5" maxlength="8" value="<%= start_date %>" style="text-align:right" onChange="return dateCheck(this);">
						※この日付以降に旧サイトにアクセスした場合に、新サイトの対象ページにリダイレクトします<br>
						<%
                            if (existRedirectFlag){ 
                                if (uploadMessage.equals("")){
                                 %><input type="button" value="リダイレクト用index.jspアップロード" onclick="location.href='etc_edit.jsp?UPLOAD';">
								 <%}else{%><%=uploadMessage%>
						<%      }
                            } %></td>
				  </tr>
				  <tr align="left">
                    <td nowrap class="size12">年齢確認フラグ </td>
					<td nowrap class="size12">
						<input type="radio" name="chk_age_flg" id="chk_age_flg_radio1" value="0"  <%if (chk_age_flg == 0)  {%>checked<%}%> >クッションページなし
						<input type="radio" name="chk_age_flg" id="chk_age_flg_radio2" value="1"  <%if (chk_age_flg == 1)  {%>checked<%}%> >クッションページあり
					</td>
                  </tr>
				  <tr align="left">
                    <td nowrap class="size12">HTMLヘッダ情報</td>
					<td nowrap class="size12">
						<% if(hotelName.equals("")){%><span style="color:red"><strong>ホテル基本情報が取得できません！ホテナビIDをホテル基本情報に入力してから設定してください。</strong></span><br><%}%>
						<textarea name="html_head" id="html_head" rows="10" cols="80" style="ime-mode:active"><%=html_head%></textarea>
						<div style="float:left;padding:10px 10px 0 0">
						デザインタイプ<br>
						<input type=radio name="designType" value="a" onclick="designTypeChange(this);" <%if (html_head.indexOf("style-a.js")!=-1){%>checked<%}%>>A
						<input type=radio name="designType" value="b" onclick="designTypeChange(this);" <%if (html_head.indexOf("style-b.js")!=-1){%>checked<%}%>>B
						<input type=radio name="designType" value="c" onclick="designTypeChange(this);" <%if (html_head.indexOf("style-c.js")!=-1){%>checked<%}%>>C
						<input type=radio name="designType" value="d" onclick="designTypeChange(this);" <%if (html_head.indexOf("style-d.js")!=-1){%>checked<%}%>>D
						<input type=radio name="designType" value="e" onclick="designTypeChange(this);" <%if (html_head.indexOf("style-e.js")!=-1){%>checked<%}%>>E
						<br>
						パスワード<br>
						<input type=radio name="pwInput" value="1" onclick="pwInputs(this);" <%if (html_head.indexOf("パスワードを入力してください")!=-1){%>checked<%}%>>あり
						<input type=radio name="pwInput" value="0" onclick="pwInputs(this);" <%if (html_head.indexOf("パスワードを入力してください")==-1){%>checked<%}%>>なし
						<br>
						18禁表示<br>
						<input type=radio name="stop18" value="1" onclick="stop18Disp(this);" <%if (html_head.indexOf("nodisp18.css")==-1){%>checked<%}%>>あり
						<input type=radio name="stop18" value="0" onclick="stop18Disp(this);" <%if (html_head.indexOf("nodisp18.css")!=-1){%>checked<%}%>>なし
						</div>
					</td>
                  </tr>
				  <tr align="left">
                    <td nowrap class="size12">ログイン有無フラグ </td>
					<td nowrap class="size12">
						<input type="radio" name="offlineflg" id="offlineflg_radio1" value="0"  <%if (offlineflg.equals("0"))  {%>checked<%}%> >メンバーページなし 
						<input type="radio" name="offlineflg" id="offlineflg_radio2" value="1"  <%if (offlineflg.equals("1"))  {%>checked<%}%> >メンバーページあり
					</td>
                  </tr>
				  <tr align="left">
                    <td nowrap class="size12">ログインフォーム</td>
					<td nowrap class="size12">
						<textarea name="html_login_form" id="html_login_form" rows="7" cols="80" style="ime-mode:active"><%=html_login_form%></textarea>
						<div style="float:left;padding:10px 10px 0 0">
						<input type=checkbox id="loginForm1" onclick="loginFormChange(this);" <%if (html_login_form.indexOf("loginForm1")!=-1){%>checked<%}%>>通常（9桁）<br>
						<input type=checkbox id="loginForm2" onclick="loginFormChange(this);" <%if (html_login_form.indexOf("loginForm2")!=-1){%>checked<%}%>>通常（6桁）<br>
						<input type=checkbox id="loginForm3" onclick="loginFormChange(this);" <%if (html_login_form.indexOf("loginForm3")!=-1){%>checked<%}%>>店舗選択<br>
						<input type=checkbox id="loginForm4" onclick="loginFormChange(this);" <%if (html_login_form.indexOf("loginForm4")!=-1){%>checked<%}%>>カードレス<br>
						</div>
					</td>
                  </tr>
				  <tr align="left">
                    <td nowrap class="size12">メルマガ有無フラグ </td>
					<td nowrap class="size12">
						<input type="radio" name="mailmagazineflg" id="mailmagazineflg_radio1" value="0"  <%if (mailmagazineflg.equals("0"))  {%>checked<%}%> >メルマガなし 
						<input type="radio" name="mailmagazineflg" id="mailmagazineflg_radio2" value="1"  <%if (mailmagazineflg.equals("1"))  {%>checked<%}%> >メルマガあり
					</td>
                  </tr>
				  <tr align="left">
                    <td nowrap class="size12">ホテルへ一言有無フラグ </td>
					<td nowrap class="size12">
						<input type="radio" name="mailtoflg" id="mailtoflg_radio1" value="0"  <%if (mailtoflg.equals("0"))  {%>checked<%}%> >ホテルへ一言なし 
						<input type="radio" name="mailtoflg" id="mailtoflg_radio2" value="1"  <%if (mailtoflg.equals("1"))  {%>checked<%}%> >ホテルへ一言あり
					</td>
                  </tr>
				  <tr align="left">
                    <td nowrap class="size12">ホテルへ一言ホテル件名有無 </td>
					<td nowrap class="size12">
						<input type="radio" name="mailnameflg" id="mailtoflg_radio1" value="0"  <%if (mailnameflg.equals("0"))  {%>checked<%}%> >件名にホテル名無し 
						<input type="radio" name="mailnameflg" id="mailtoflg_radio2" value="1"  <%if (mailnameflg.equals("1"))  {%>checked<%}%> >件名にホテル名有
					</td>
                  </tr>
				  <tr align="left">
                    <td nowrap class="size12"> ホテルへ一言ホテル名 </td>
					<td nowrap class="size12">
						<input name="mailname" type="text" size="64" maxlength="64" value="<%= mailname %>" >
					</td>
                  </tr>
				  <tr align="left">
                    <td nowrap class="size12">掲示板参照フラグ </td>
					<td nowrap class="size12">
						<input type="radio" name="viewflg" id="viewflg_radio1" value="0"  <%if (viewflg.equals("0"))  {%>checked<%}%> >通常
						<input type="radio" name="viewflg" id="viewflg_radio2" value="1"  <%if (viewflg.equals("1"))  {%>checked<%}%> >参照バージョン
						<input type="radio" name="viewflg" id="viewflg_radio3" value="2"  <%if (viewflg.equals("2"))  {%>checked<%}%> >参照バージョン（メンバーも） 
						<input type="radio" name="viewflg" id="viewflg_radio4" value="9"  <%if (viewflg.equals("9"))  {%>checked<%}%> >休止中
					</td>
                  </tr>
				  <tr align="left">
                    <td nowrap class="size12">多店舗掲示板の使用 </td>
					<td nowrap class="size12">
						<input type="radio" name="bbsgroupflg" id="bbsgroupflg_radio1" value="0"  <%if (bbsgroupflg.equals("0"))  {%>checked<%}%>>通常
						<input type="radio" name="bbsgroupflg" id="bbsgroupflg_radio2" value="1"  <%if (bbsgroupflg.equals("1"))  {%>checked<%}%>>:多店舗バージョン
					</td>
                  </tr>
				  <tr align="left">
                    <td nowrap class="size12"> 商品交換用ホテルID </td>
					<td nowrap class="size12">
						<input name="prize_hotelid" type="text" size="10" maxlength="10" value="<%= prize_hotelid %>" style="ime-mode:disabled">（未入力の場合はhotel_idを使用）
					</td>
                  </tr>
				  <tr align="left">
                    <td nowrap class="size12">地図 </td>
					<td nowrap class="size12">
						<input type="radio" name="coupon_map_flg" id="coupon_map_flg_radio1" value=0  <%if (coupon_map_flg == 0)  {%>checked<%}%>>Yahoo!MAPを使用
						<input type="radio" name="coupon_map_flg" id="coupon_map_flg_radio2" value=1  <%if (coupon_map_flg == 1)  {%>checked<%}%>>画像ファイルを使用
					</td>
                  </tr>
				  <tr align="left">
                    <td nowrap class="size12"> クーポン地図画像1 </td>
					<td nowrap class="size12">
						<input name="coupon_map_img1" type="text" size="50" maxlength="50" value="<%= coupon_map_img1 %>" style="ime-mode:disabled">
					</td>
                  </tr>
				  <tr align="left">
                    <td nowrap class="size12"> クーポン地図画像2 </td>
					<td nowrap class="size12">
						<input name="coupon_map_img2" type="text" size="50" maxlength="50" value="<%= coupon_map_img2 %>" style="ime-mode:disabled">
					</td>
                  </tr>
				  <tr align="left">
                    <td nowrap class="size12">掲示板 </td>
					<td nowrap class="size12">
						<input type="radio" name="bbs_temp_flg" id="bbs_temp_flg_radio1" value=0  <%if (bbs_temp_flg == 0)  {%>checked<%}%>>通常掲示板
						<input type="radio" name="bbs_temp_flg" id="bbs_temp_flg_radio2" value=1  <%if (bbs_temp_flg == 1)  {%>checked<%}%>>仮投稿掲示板（掲示板追加時にdel_flagに1をセット）
					</td>
                  </tr>
				  <tr align="left">
                    <td nowrap class="size12">ランキング </td>
					<td nowrap class="size12">
						<input type="radio" name="ranking_hidden_flg" id="ranking_hidden_flg_radio1" value=0  <%if (ranking_hidden_flg == 0)  {%>checked<%}%> >通常
						<input type="radio" name="ranking_hidden_flg" id="ranking_hidden_flg_radio2" value=1  <%if (ranking_hidden_flg == 1)  {%>checked<%}%> >ランキング情報を出力しない
					</td>
                  </tr>
				  <tr align="left">
                    <td nowrap class="size12">オーナーズルーム達成回数 </td>
					<td nowrap class="size12">
						<input name="ownercount" type="text" size="3" maxlength="2" value="<%= ownercount %>" style="text-align:right">
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">TOPページのWhatsNew表示件数</td>
					<td nowrap class="size12">
						<input name="count_whatsnew_for_hoteltop" type="text" size="3" maxlength="3" value="<%= count_whatsnew_for_hoteltop %>" style="text-align:right"> （0～999)  0の場合はWhat’sNew非表示
					</td>
                  </tr>

                  <tr align="left">
                    <td width="180"><img src="../../common/pc/image/spacer.gif" width="120" height="14"></td>
                    <td ></td>
                  </tr>
                </form>  
                </table>
              </td>
              </tr>
              <tr>
                <td valign="top">&nbsp;</td>
                <td valign="top"></td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
            </table>
        </td>
        <td width="3" valign="top" align="left" height="100%">
          <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><img src="image/tab_kado.gif" width="3" height="3"></td>
            </tr>
            <tr>
              <td bgcolor="#666666" height="100%"><img src="../../common/pc/image/spacer.gif" width="3" height="100"></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td height="3" bgcolor="#999999">
          <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="3"><img src="../../common/pc/image/tab_kado2.gif" width="3" height="3"></td>
              <td bgcolor="#666666"><img src="../../common/pc/image/spacer.gif" width="100" height="3"></td>
            </tr>
          </table>
        </td>
        <td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="3"></td>
      </tr>
      <!-- ここまで -->
    </table></td>
  </tr>
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/owner/Library/footer.lbi" --><img src="../../common/pc/image/imedia.gif" width="63" height="18"><img src="../../common/pc/image/spacer.gif" width="12" height="18" align="absmiddle">Copyrigtht&copy; imedia
    inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>