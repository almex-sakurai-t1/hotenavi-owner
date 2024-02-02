<%@ page contentType="text/html; charset=Windows-31J" %><%@ page import="java.sql.*" %><%@ page import="com.hotenavi2.common.*" %><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>テーブル一覧</title>
</head>
<body>
<%
	Connection        connection  = null;
	PreparedStatement prestate    = null;
	ResultSet         result      = null;
	connection  = DBConnection.getConnection();
	String query;
  String alter_table_code = "";
  try
	{
    if (request.getParameter("table") == null)
	  {  
  	  query = "SELECT TABLE_NAME,ENGINE,TABLE_ROWS,TABLE_COMMENT from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='hotenavi' AND TABLE_COMMENT LIKE '[ホテナビ]%'";
      prestate    = connection.prepareStatement(query);
%><div style="width:100%;text-align: right;"><input type="button" value="MarkDown出力" id="md_list"></div>
<table width="100%" border="1" cellpadding="1" cellspacing="0" style="font-size:12px;line-height:2em"  id="table_list">
  <tr>
   <th>NAME</th>	
   <th>ENGINE</th>	
   <th>ROWS</th>	
   <th>TABLE_COMMENT</th>	
 </tr>
<%	  
      result      = prestate.executeQuery();
	    while( result.next() )
      {
%><tr class="table_list" style="cursor: pointer;">
<td><%=result.getString("TABLE_NAME")%></td>	
<td><%=result.getString("ENGINE")%></td>	
<td style="text-align:right"><%=result.getString("TABLE_ROWS")%></td>	
<td><%=result.getString("TABLE_COMMENT").replace("[ホテナビ]","")%></td>	
</tr>
<%    }
	  }
	  else
    {
		  query = "SELECT TABLE_NAME,ENGINE,TABLE_ROWS,TABLE_COMMENT from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='hotenavi' AND TABLE_NAME = ? ";
      prestate    = connection.prepareStatement(query);
      prestate.setString(1,request.getParameter("table"));
%><table width="100%" border="1" cellpadding="1" cellspacing="0" style="font-size:12px;line-height:2em"  id="table_title">
<tr>
   <th>NAME</th>	
   <th>ENGINE</th>	
   <th>TABLE_COMMENT</th>	
 </tr>
<%	  
      result      = prestate.executeQuery();
	    if( result.next() )
      {
         alter_table_code += "ALTER TABLE `" + result.getString("TABLE_NAME") + "`<br>";
%><tr class="table_list">
<td><%=result.getString("TABLE_NAME")%></td>	
<td><%=result.getString("ENGINE")%></td>	
<td><%=result.getString("TABLE_COMMENT").replace("[ホテナビ]","")%></td>	
</tr>
</table>
<div style="width:100%"><a href=".">←</a></div>
<div style="width:100%;text-align: right;margin-top: -20px;"><input type="button" value="MarkDown出力" id="md_detail"></div>
<table width="100%" border="1" cellpadding="1" cellspacing="0" style="font-size:12px;line-height:2em" id="table_detail">
<%    }
      query = "SELECT ORDINAL_POSITION,SUBSTRING_INDEX(COLUMN_COMMENT,';',1) LOGICAL_NAME,COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,CASE WHEN EXTRA = '' THEN COLUMN_DEFAULT ELSE EXTRA END COLUMN_DEFAULT,COLUMN_KEY,CASE WHEN LOCATE(';',COLUMN_COMMENT) = 0 THEN '' ELSE SUBSTRING_INDEX(COLUMN_COMMENT,';',-1) END COLUMN_COMMENT";
		  query += " FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA ='hotenavi' AND TABLE_NAME = ?";
      prestate    = connection.prepareStatement(query);
      prestate.setString(1,request.getParameter("table"));
%><tr>
	<th>No.</th>	
	<th>論理名</th>	
	<th>物理名</th>	
	<th>データ型</th>	
	<th>NULL</th>	
	<th>default</th>	
	<th>KEY</th>	
	<th>備考</th>	
  </tr>
<%
      String pre_column_name = "FIRST";
      result = prestate.executeQuery();
	    while( result.next() )
	    {
        alter_table_code += " MODIFY COLUMN `" + result.getString("COLUMN_NAME") + "`";
        alter_table_code += " " + result.getString("COLUMN_TYPE") + " ";
        alter_table_code += " " + (result.getString("IS_NULLABLE").equals("YES") ? "NULL" : "NOT NULL") +  " ";
        alter_table_code += " " + (result.getString("COLUMN_TYPE").equals("text") ? "" : (result.getString("COLUMN_DEFAULT") == null ? "DEFAULT NULL" : (result.getString("COLUMN_DEFAULT").equals("auto_increment") ? "AUTO_INCREMENT" : (result.getString("COLUMN_DEFAULT").equals("CURRENT_TIMESTAMP") ? "DEFAULT CURRENT_TIMESTAMP" : "DEFAULT '" + result.getString("COLUMN_DEFAULT") + "'")))) + " ";          alter_table_code += " COMMENT '" + result.getString("LOGICAL_NAME") + (result.getString("COLUMN_COMMENT").equals("") ? "" : ";"+result.getString("COLUMN_COMMENT")) + "' ";
        alter_table_code += " " + pre_column_name + ",<br>";
        pre_column_name = "AFTER `" + result.getString("COLUMN_NAME") + "`";
%><tr class="table_detail">
<th><%=result.getString("ORDINAL_POSITION")%></th>	
<td><%=result.getString("LOGICAL_NAME")%></td>
<td><%=result.getString("COLUMN_NAME")%></td>
<td><%=result.getString("COLUMN_TYPE")%></td>
<td><%=result.getString("IS_NULLABLE")%></td>	
<td><%=result.getString("COLUMN_DEFAULT")%></td>
<td><%=result.getString("COLUMN_KEY")%></td>
<td><%=result.getString("COLUMN_COMMENT")%></td>
</tr>
<%	  }
      alter_table_code += "ALGORITHM=INPLACE, LOCK=NONE<br>";
%><div id="alter_table_code_button"><input type=button value="mysql change comment code"></div>
<%    }
	}
	catch ( Exception e )
	{
%><tr class="error_message">
	<th>ERROR:<%=e%></th>	
	</tr>
<%	}
	finally
	{
		DBConnection.releaseResources( result, prestate, connection );
	}
%>
</table>
<div><a id="downloader"></a></div>
<div id="alter_table_code" style="font-size:12px;display:none"><%=alter_table_code%></div>
<script>
var target_table="<%= request.getParameter("table") %>";
let targets = document.getElementsByClassName("table_list");
for(let i = 0; i < targets.length; i++){
  targets[i].addEventListener("click",() => {
        location.href=".?table="+targets[i].cells[0].innerText;
  }, false);
}
<%
  if (request.getParameter("table") == null)
  {  
%>let md_list = document.getElementById("md_list");
  md_list.addEventListener("click",() => {
	downloadMD("hotenavi_table_list","table_list");
  }, false);
<%}
  else
{
%>let md_detail = document.getElementById("md_detail");
  md_detail.addEventListener("click",() => {
	downloadMD(target_table,"table_detail");
  }, false);
<%
  }
%>
document.getElementById("alter_table_code_button").addEventListener("click",() => {
  document.getElementById("alter_table_code_button").style.display = "none";
  document.getElementById("alter_table_code").style.display = "";
}, false);

function downloadMD(data,targetId) {
  var data_md = "";
  if (target_id = "table_detail"){
    data_md += makeMD("table_title");
    data_md += "\n";
  }

  data_md += makeMD(targetId);
  // BOM の用意（文字化け対策）
  var bom = new Uint8Array([0xEF, 0xBB, 0xBF]);
 
  // CSV データの用意
  var blob = new Blob([bom, data_md], { type: 'text/csv' });
  var url = (window.URL || window.webkitURL).createObjectURL(blob);
  var a = document.getElementById('downloader');
  a.download = data+ '.md';
  a.href = url;
  // ダウンロードリンクをクリックする
  a.click();
}


function makeMD(targetId){
  var data_md = "";
	var table = document.getElementById(targetId);
//表タイトル箇所
  for(var j = 0; j < table.rows[0].cells.length; j++){
    data_md += "|"+table.rows[0].cells[j].innerText;//HTML中の表のセル値をdata_mdに格納
    if(j == table.rows[0].cells.length-1) data_md += "|\n";//行終わりに改行コードを追加
  }
  for(var j = 0; j < table.rows[1].cells.length; j++){
    if(table.rows[1].cells[j].tagName == "TH"){
      data_md += "|:---:";
    }
    else{
      data_md += "|:---";
    }
    if(j == table.rows[1].cells.length-1) data_md += "|\n";//行終わりに改行コードを追加
  }
  for(var i = 1;  i < table.rows.length; i++){
    for(var j = 0; j < table.rows[i].cells.length; j++){
      data_md += "|"+table.rows[i].cells[j].innerText;//HTML中の表のセル値をdata_mdに格納
      if(j == table.rows[i].cells.length-1) data_md += "|\n";//行終わりに改行コードを追加
    }
  }
  return data_md;
}
</script>
</body>
</html>


