/*
 * @(#)RandomString.java 1.00
 * 2009/07/15 Copyright (C) ALMEX Inc. 2009
 * ランダム文字列取得クラス
 */

package jp.happyhotel.common;

import java.io.Serializable;

import jp.happyhotel.data.DataUserSp;
import jp.happyhotel.user.UserCreditInfo;
import jp.happyhotel.user.UserSpInfo;
import jp.happyhotel.user.UserUuidInfo;

/**
 * ユーザ支払区分クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2014/12/14
 */
public class UserPayKind implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 8781679419778871829L;

    static public String getPayKind(String userId)
    {
        boolean cardmemberFlag = false;
        boolean cardmemberNgFlag = false;
        UserSpInfo loginUserSpInfo = new UserSpInfo();
        DataUserSp loginUserSp = new DataUserSp();
        UserCreditInfo userCredit = new UserCreditInfo();

        String payKind = "";

        // キャリア課金情報取得
        loginUserSp = loginUserSpInfo.getUserSp( userId );
        if ( loginUserSp != null )
        {
            if ( loginUserSp.getCarrierKind() == DataUserSp.DOCOMO )
            {
                payKind = "プレミアム会員(SP)";
            }
            else if ( loginUserSp.getCarrierKind() == DataUserSp.AU )
            {
                payKind = "プレミアム会員(au)";
            }
            else if ( loginUserSp.getCarrierKind() == DataUserSp.SOFTBANK )
            {
                payKind = "プレミアム会員(SoftBank)";
            }
            else if ( loginUserSp.getCarrierKind() == DataUserSp.YAHOO_WALLET )
            {
                payKind = "プレミアム会員(Yahoo)";
            }
        }

        // クレジット有料メンバーフラグ取得
        cardmemberFlag = userCredit.getPayMemberFlag( userId );
        // クレジット会員NG情報
        cardmemberNgFlag = userCredit.getNgMemberFlag( userId );

        if ( cardmemberFlag != false || cardmemberNgFlag != false )
        {
            payKind = "プレミアム会員(クレジット)";
        }

        if ( payKind.equals( "" ) != false )
        {

            if ( UserUuidInfo.isPayMember( userId ) )
            {
                payKind = "プレミアム会員(アプリ)";
            }
            else
            {
                payKind = "プレミアム会員(携帯)";
            }
        }
        return payKind;
    }

}
