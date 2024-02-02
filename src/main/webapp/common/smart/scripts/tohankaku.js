//全角カナを半角カナに変換
function toHankaku(motoText){
    
	han = "ｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜｦﾝｧｨｩｪｫｬｭｮｯｰ｢｣!?()";
	han+= "ｳｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾊﾋﾌﾍﾎ";
	han+= "ﾊﾋﾌﾍﾎ";
//  0～61
	txt = "アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲンァィゥェォャュョッー「」！？（）"
//  62～82
	txt+= "ヴガギグゲゴザジズゼゾダヂヅデドバビブベボ";
//　83～87
	txt+= "パピプペポ";
	str = "";
	for (i=0; i<motoText.length; i++)
	{
	c = motoText.charAt(i);
	n = txt.indexOf(c,0);
	if (n >= 0) 
		{
		c = han.charAt(n);
		}
	str += c;
	if (n >= 83) 
		{
		str += "ﾟ";
		}
	else if (n  >= 62)
		{
		str += "ﾞ";
		}
	}
	return str;
}
