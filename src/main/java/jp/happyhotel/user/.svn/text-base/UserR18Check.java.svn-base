/*
 * @(#)UserTermInfo.java 1.00
 * 2010/12/27 Copyright (C) ALMEX Inc. 2010
 * ユーザ18禁情報取得・更新クラス
 */

package jp.happyhotel.user;

import java.io.Serializable;

import jp.happyhotel.common.DateEdit;
import jp.happyhotel.data.DataUserR18Check;

/**
 * ユーザの18歳以上(18禁)の確認を行うクラス
 * 
 * @author S.Tashiro
 * @version 1.00 2010/12/27
 */
public class UserR18Check implements Serializable
{

    private static final long serialVersionUID = 7180630700601158446L;
    private final int         KIND             = 3;                   // DateEdit.isValidDateで使用する種別(0：年、1：月、2：日、3：時間、4：分、5：秒)
    private final int         ELAPSED_TIME     = 24;                  // DateEdit.isValidDateで使用する経過時間。有効時間は24時間。

    private DataUserR18Check  userR18Check;
    private Boolean           validFlag;                              // true:有効期限内、false:有効期限外
    private Boolean           getFlag;                                // true:データ取得、false：データなし

    /**
     * データを初期化します。
     */
    public UserR18Check()
    {
        getFlag = false;
        validFlag = false;
    }

    /** ユーザ18禁情報取得 **/
    public DataUserR18Check getR18()
    {
        return(this.userR18Check);
    }

    /**
     * ユーザ18禁情報取得
     * 
     * @return 処理結果 (true:データ取得、false：データなし)
     */
    public boolean isGetFlag()
    {
        return(this.getFlag);
    }

    /**
     * ユーザ18禁情報有効期限チェック
     * 
     * @return 処理結果（true:有効、false:無効）
     **/
    public boolean isValidDate()
    {
        return(this.validFlag);
    }

    /**
     * 18禁情報を取得する
     * 
     * @param termNo 端末番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getUserR18(String termNo)
    {
        boolean ret = false;

        this.userR18Check = new DataUserR18Check();
        ret = this.userR18Check.getData( termNo );

        // データ取得の結果を更新フラグにセット
        this.getFlag = ret;

        // データがあった場合は有効期限もチェックする
        if ( this.getFlag != false )
        {
            this.validFlag = DateEdit.isValidDate( this.userR18Check.getRegistDate(), this.userR18Check.getRegistTime(),
                    KIND, ELAPSED_TIME );
        }
        return(ret);
    }

    /**
     * 18禁情報を更新する
     * 
     * @param termNo 端末番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateUserR18(String termNo)
    {
        boolean ret = false;

        this.userR18Check.setMobileTermno( termNo );
        this.userR18Check.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        this.userR18Check.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
        if ( this.getFlag != false )
        {
            ret = this.userR18Check.updateData( termNo );
        }
        else
        {
            ret = this.userR18Check.insertData();
        }
        if ( ret != false )
        {
            this.validFlag = true;
            this.getFlag = true;
        }

        return(ret);
    }
}
