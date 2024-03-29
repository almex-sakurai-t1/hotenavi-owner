package jp.happyhotel.common;

public class ConvertHexToInt
{

    /**
     * 16進数1桁からintに変換
     * 
     * @param hex 16進数文字列
     * @return int 10進数
     */
    public static int HexToInt(String hex)
    {
        int value = 0;
        char hexDigit[] = hex.toCharArray();

        value = (Character.digit( hexDigit[0], 16 ));
        return(value);
    }
}
