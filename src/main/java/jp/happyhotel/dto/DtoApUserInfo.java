package jp.happyhotel.dto;

import java.io.Serializable;

/**
 * タッチユーザデータ
 *
 * @author Shingo Tashiro
 * @version 1.00 2014/8/17
 */
public class DtoApUserInfo implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -5559515117210828916L;
//    private DataUserBasic     userBasic;                               // ユーザ基本情報
    private DtoApCommon       apCommon;                                // タッチ共通データ

    /**
     * データを初期化します。
     */
    public DtoApUserInfo()
    {
//        this.userBasic = null;
        this.apCommon = null;
    }

//    public DataUserBasic getUserBasic()
//    {
//        return userBasic;
//    }
//
//    public void setUserBasic(DataUserBasic userBasic)
//    {
//        this.userBasic = userBasic;
//    }

    public DtoApCommon getApCommon()
    {
        return apCommon;
    }

    public void setApCommon(DtoApCommon apCommon)
    {
        this.apCommon = apCommon;
    }

}
