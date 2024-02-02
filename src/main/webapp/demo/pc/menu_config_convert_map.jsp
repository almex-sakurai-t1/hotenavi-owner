<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.util.*" %>
<%

	HashMap<String,Integer> map = new HashMap<String,Integer>();
	map.put("searchnew",5);
	map.put("memberonly",27);
	map.put("new",1);
	map.put("event",3);
	map.put("food",19);
	map.put("access",11);
	map.put("bbs",9);
	map.put("mailto",13);
	map.put("faq",21);
	map.put("recruit",23);
	map.put("mailreserve",25);
	map.put("coupon",17);
	map.put("priceinfo",29);
	map.put("simulate",33);
	map.put("premium",35);
	map.put("rental",37);
	map.put("contents",39);
	map.put("roominfo",41);
	map.put("credit",7);
	map.put("memberinfo",43);
	map.put("memberuse",45);
	map.put("memberpoint",47);
	map.put("memberallroom",49);
	map.put("memberowner",51);
	map.put("memberranking",53);
	map.put("memberregist",55);
	map.put("originalcoupon",57);
	map.put("general2",59);
	map.put("general3",71);
	map.put("general4",73);
	map.put("general5",75);
	map.put("general6",77);
	map.put("memberlogin",69);
	map.put("member",5);
	map.put("hotelTop",5);
	map.put("search",27);
	map.put("room",41);
	map.put("price",29);
	map.put("question",63);
	map.put("mailmagazine",61);
    for (int i = 35; i < 100; i++)
    {
		map.put("general"+(i-28),(i*2)+13);
    }

%>