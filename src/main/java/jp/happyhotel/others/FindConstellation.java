/*
 * @(#)FindConstellation.java 1.00 2009/08/02
 * Copyright (C) ALMEX Inc. 2009
 * 星座割り当てクラス
 */

package jp.happyhotel.others;

import java.io.Serializable;

import jp.happyhotel.common.Logging;

;

/**
 * 星座割り当てクラス
 * 誕生日から該当する星座を割り当てる
 * 
 * @author S.Tashiro
 * @version 1.00 2009/08/12
 */
public class FindConstellation implements Serializable
{

    private static final long serialVersionUID = 7339185125931790275L;
    private static final int  ARIAN            = 1;                   // おひつじ座(3/21〜4/19)
    private static final int  TAURUS           = 2;                   // おうし座(4/20〜5/20)
    private static final int  GEMINI           = 3;                   // ふたご座(5/21〜6/21)
    private static final int  CANCER           = 4;                   // かに座(6/22〜7/22)
    private static final int  LEO              = 5;                   // しし座(7/23〜8/22)
    private static final int  VIRGO            = 6;                   // おとめ座(8/23〜9/22)
    private static final int  LIBRA            = 7;                   // てんびん座(9/23〜10/23)
    private static final int  SCORPIUS         = 8;                   // さそり座(10/24〜11/21)
    private static final int  SAGITTARIUS      = 9;                   // いて座(11/22〜12/21)
    private static final int  CAPRICORN        = 10;                  // やぎ座(12/22〜1/19)
    private static final int  AQUARIUS         = 11;                  // みずがめ座(1/20〜2/18)
    private static final int  PISCES           = 12;                  // うお座(2/19〜3/20)

    /**
     * 星座を取得する
     * 
     * @param birthday 誕生日
     * @return 処理結果(0:失敗,1:おひつじ座,2:おうし座,3:ふたご座,4:かに座,5:しし座,6:おとめ座,
     *         7:てんびん座,8:さそり座,9:いて座,10:やぎ座,11:みずがめ座,12:うお座)
     */
    static public int getConstellation(int birthDay)
    {
        int result;
        result = 0;
        try
        {
            // 桁数チェックして、年を消す
            if ( birthDay / 10000 > 0 )
            {
                birthDay = birthDay % 10000;
            }
            // 1月1日より小さい
            else if ( birthDay < 100 )
            {
                return(0);
            }
            // 12月31日より大きい
            else if ( birthDay > 1231 )
            {
                return(0);
            }

            if ( (birthDay >= 321 && birthDay <= 331) || (birthDay >= 401 && birthDay <= 419) )
            {
                result = ARIAN;
            }
            else if ( (birthDay >= 420 && birthDay <= 430) || (birthDay >= 501 && birthDay <= 520) )
            {
                result = TAURUS;
            }
            else if ( (birthDay >= 521 && birthDay <= 531) || (birthDay >= 601 && birthDay <= 621) )
            {
                result = GEMINI;
            }
            else if ( (birthDay >= 622 && birthDay <= 630) || (birthDay >= 701 && birthDay <= 722) )
            {
                result = CANCER;
            }
            else if ( (birthDay >= 723 && birthDay <= 731) || (birthDay >= 801 && birthDay <= 822) )
            {
                result = LEO;
            }
            else if ( (birthDay >= 823 && birthDay <= 831) || (birthDay >= 901 && birthDay <= 922) )
            {
                result = VIRGO;
            }
            else if ( (birthDay >= 923 && birthDay <= 930) || (birthDay >= 1001 && birthDay <= 1023) )
            {
                result = LIBRA;
            }
            else if ( (birthDay >= 1024 && birthDay <= 1031) || (birthDay >= 1101 && birthDay <= 1121) )
            {
                result = SCORPIUS;
            }
            else if ( (birthDay >= 1122 && birthDay <= 1130) || (birthDay >= 1201 && birthDay <= 1221) )
            {
                result = SAGITTARIUS;
            }
            else if ( (birthDay >= 1222 && birthDay <= 1231) || (birthDay >= 101 && birthDay <= 119) )
            {
                result = CAPRICORN;
            }
            else if ( (birthDay >= 120 && birthDay <= 131) || (birthDay >= 201 && birthDay <= 218) )
            {
                result = AQUARIUS;
            }
            else if ( (birthDay >= 219 && birthDay <= 229) || (birthDay >= 301 && birthDay <= 320) )
            {
                result = PISCES;
            }
            else
            {
                Logging.error( "[FindConstellation.getConstellation()] birthDay:" + birthDay );
                result = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[FindConstellation.getConstellation()] Exceptioon:" + e.toString() );
            result = 0;
        }
        return(result);
    }
}
