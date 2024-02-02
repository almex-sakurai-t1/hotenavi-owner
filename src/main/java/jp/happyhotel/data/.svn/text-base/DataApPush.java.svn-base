package jp.happyhotel.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * プッシュ配信データリスト拡張クラス
 *
 * @author Kaori.Mitsuhashi
 * @version 1.00 2016/6/8
 */
public class DataApPush implements Serializable
{
    /**
     *
     */
    private String               tokens;                              //複数トークン（カンマ区切り）
    ArrayList<DataApPushDataListExtend> listPl = new ArrayList<DataApPushDataListExtend>();


    /**
     * データを初期化します。
     */
    public DataApPush()
    {
        this.tokens = "";
        this.listPl = null;
    }

    public String getTokens()
    {
        return tokens;
    }

    public void setTokens(String tokens)
    {
        this.tokens = tokens;
    }

    public  ArrayList<DataApPushDataListExtend> getListPl()
    {
        return listPl;
    }

    public void setListPl(ArrayList<DataApPushDataListExtend> listPl)
    {
        this.listPl = listPl;
    }

}
