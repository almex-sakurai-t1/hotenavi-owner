/*
 * @(#)RandomNumber.java 2.00 2004/12/02 Copyright (C) ALMEX Inc. 2004 ランダム数字取得クラス
 */

package jp.happyhotel.common;

import java.io.Serializable;

/**
 * ランダムの数字を取得するクラス。
 * 
 * @author S.Tashiro
 * @version 1.00 2008/04/02
 */
public class RandomNumber implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 5514127320390762089L;

    /**
     * 
     * @param digit ランダム変数の必要桁数
     * @return 処理結果(-1:失敗)
     */
    static public int getRandomNumber(int digit)
    {
        final double TEN = 10.00;
        int nRan;
        double dblDigit;
        double dblRandom;
        double dblPrefix;

        if ( digit < 0 )
        {
            return(-1);
        }
        else
        {
            dblDigit = digit;
            // ランダム変数を取得（0～1の範囲）
            dblRandom = Math.random();
            dblPrefix = Math.pow( TEN, dblDigit );
            // 必要な桁数にシフトする
            dblRandom = dblRandom * dblPrefix;

            nRan = (int)dblRandom;

            return(nRan);
        }
    }
}
