package jp.happyhotel.common;

import java.io.Serializable;

/**
 * 時間変換クラス
 *
 * @author
 * @version 1.00 2010/12/27
 */
public class ConvertTime implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1014150637847161186L;

    /**
     * 時間編集１
     * 引数の値を基に文字型で返す。
     *
     * @param convTime 変換前時間
     * @param mode 編集タイプ(0:HHMMSS, 1:HH:MM:SS, 2:HHMM, 3:HH:MM)
     * @return HHMMSS形式
     */
    public static String convTimeStr(int convTime, int mode )
    {
        String retTime = "";

        //
        if ( convTime == 0 )
        {
            retTime = "000000";
        }
        else if ( convTime < 100 )
        {
            // 秒のみでも０時０分で返す
            retTime = "000000";
        }
        else if ( convTime < 1000 )
        {
            retTime = "000" + String.valueOf( convTime );
        }
        else if ( convTime < 10000 )
        {
            retTime = "00" + String.valueOf( convTime );
        }
        else if ( convTime < 100000 )
        {
            retTime = "0" + String.valueOf( convTime );
        }
        else
        {
            retTime = String.valueOf( convTime );
        }
        switch (mode)
        {
            case 0:
                break;
            case 1:
                retTime = retTime.substring( 0, 2 ) + ":" + retTime.substring( 2, 4 ) + ":" + retTime.substring( 4, 6 );
                break;
            case 2:
                retTime = retTime.substring( 0, 4 );
                break;
            case 3:
                retTime = retTime.substring( 0, 2 ) + ":" + retTime.substring( 2, 4 );
               break;
            default:
                break;
        }

        return retTime;
    }

    /**
     * 時間編集２
     * 引数の値を基に文字型で返す。
     *
     * @param convTime 変換前時間
     * @param mode 編集基タイプ(0:HHMMSS, 1:HH:MM:SS, 2:HHMM, 3:HH:MM)
     * @return
     */
    public static int convTimeInt(String convTime, int mode)
    {
        int retTime = 0;
        String strVal = "";

        // 省略時は値０で返す
        if ((convTime.equals( null )) || (convTime.equals( "" )))
        {
            retTime = 0;
        }
        else
        {
            if ( (mode == 1) || (mode == 3) )
            {
                // ":"付きで引き渡された場合":"をはずす
                strVal = convTime.replace( ":", "" );
            }
            else
            {
                strVal = convTime;
            }
            if ( (mode == 2) || (mode == 3) )
            {
                // 時分の場合は秒を付加する
                strVal = strVal + "00";
            }
            retTime = Integer.parseInt( strVal );
        }
        return retTime;
    }

    /**
     * 時間編集３
     * 引数の値を100倍(秒数)して、int型で返す。
     *
     * @param convTime 変換前時間
     * @param mode 編集基タイプ(0:HHMMSS, 1:HH:MM:SS, 2:HHMM, 3:HH:MM)
     * @return HHMM00形式(int型)
     */
    public static int convTimeSS(String convTime, int mode){
        int retTime = 0;
        int convTimeHHMM = 0;

        if ((convTime.equals( null )) || (convTime.equals( "" )))
        {
            retTime = 0;
            return (retTime);
        }

        convTimeHHMM = convTimeInt(convTime, mode);

        retTime = convTimeHHMM * 100;

        return (retTime);
    }

    /**
     * 時間編集４
     * 引数で指定された値をHHMMSSに変換して返す
     *
     * @param convTimeHH 変換前時間 時
     * @param convTimeMM 変換前時間 分
     * @param mode 編集基タイプ(0:HHMMSS, 1:HH:MM:SS, 2:HHMM, 3:HH:MM)
     * @return HH:MM形式
     */
    public static int convTimeSS(int timeHH, int timeMM, int mode){
        int retTime = 0;
        int convTime = 0;
        String strTime = "";

        if (timeMM < 10) {
            strTime = Integer.toString( timeHH ) +  "0" + Integer.toString( timeMM );
        } else {
            strTime = Integer.toString( timeHH ) +  Integer.toString( timeMM );
        }
        convTime = convTimeInt(strTime, mode);

        retTime = convTime;

        return (retTime);
    }

    /**
     * 時間編集５
     * 引数で指定された値をHH:MMに変換して返す
     *
     * @param convTime 変換前時間
     * @param mode 編集基タイプ(0:HHMMSS, 1:HH:MM:SS, 2:HHMM, 3:HH:MM)
     * @return HH:MM形式
     */
    public static String convTimeHHMM(int convTime, int mode){
        String retTime = "";
        String convTimeHHMM = "";

        convTimeHHMM = convTimeStr(convTime, mode);

        retTime = convTimeHHMM.substring( 0, 2 ) + ":" + convTimeHHMM.substring( 2, 4 );

        return (retTime);
    }

    /**
     * 時間編集６
     * 引数で指定された値の時間(HH)を返す
     *
     * @param convTime 変換前時間(HHMMSSのint型の値)
     * @return HH
     */
    public static String convTimeHH(int convTime){
        String retTime = "";
        String convTimeHH = "";

        convTimeHH = convTimeStr(convTime, 0);

        retTime = convTimeHH.substring( 0, 2 );

        return (retTime);
    }

    /**
     * 時間編集７
     * 引数で指定された値の分(MM)を返す
     *
     * @param convTime 変換前時間(HHMMSSのint型の値)
     * @return MM
     */
    public static String convTimeMM(int convTime){
        String retTime = "";
        String convTimeMM = "";

        convTimeMM = convTimeStr(convTime, 0);

        retTime = convTimeMM.substring( 2, 4 );

        return (retTime);
    }
}
