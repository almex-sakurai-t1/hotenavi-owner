<%@ page contentType="text/html; charset=Windows-31J" %><%@ page import="java.io.File" %><%
    String cssFile = "https://hotenavi.com/" + hotelid + "/contents.css";
    String imageTitleBbs = "https://hotenavi.com/" + hotelid + "/image/title_bbs.gif";
    String imageSpacer = "https://hotenavi.com/" + hotelid + "/image/spacer.gif";
    String imageDot1 = "https://hotenavi.com/" + hotelid + "/image/dot1.gif";
    String imageBbsImage = "https://hotenavi.com/" + hotelid + "/image/bbs_image.gif";
	String imageBtnToukou = "https://hotenavi.com/" + hotelid + "/image/btn_toukou.gif";
	String imageResyajirushi = "https://hotenavi.com/" + hotelid + "/image/resyajirushi.gif";
	String imageYajirushi = "https://hotenavi.com/" + hotelid + "/image/yajirushi.gif";
	String imageYajirushiGrey = "https://hotenavi.com/" + hotelid + "/image/yajirushiGrey.gif";
    boolean isCssExist = false; 
    File checkFile = null;
    checkFile = new File("/hotenavi/"+ hotelid +"/css/contents.css");
    if(checkFile.exists()) 
    {
		isCssExist = true;
    }
    if (isCssExist){
        cssFile = "/" + hotelid +"/css/contents.css";
        imageTitleBbs = "/" + hotelid +"/image/title_bbs.gif";
        imageSpacer = "/" + hotelid +"/image/spacer.gif";
        imageDot1 = "/" + hotelid +"/image/dot1.gif";
        imageBbsImage = "/" + hotelid +"/image/bbs_image.gif";
        imageResyajirushi = "/" + hotelid +"/image/resyajirushi.gif";
        imageYajirushi = "/" + hotelid +"/image/yajirushi.gif";
        imageYajirushiGrey = "/" + hotelid +"/image/yajirushiGrey.gif";
	  }
%>