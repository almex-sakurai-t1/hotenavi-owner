/*
 * @(#)DistanceDetermination.java 1.00 2009/07/03 Copyright (C) ALMEX Inc. 2009 距離計測クラス 用URL作成クラス
 */
package jp.happyhotel.common;

import java.io.Serializable;

/**
 * 距離計測クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2009/06/17
 * @see "yahooMapAPIについては、地図検索系資料または、APICourseDevelopersGuideを参照してください。"
 */
public class DistanceDetermination implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -4000275997311216660L;

    /**
     * 距離計測メソッド（マップポイントの緯度経度とホテルの緯度経度）
     * 
     * @param startLat スタート地点の緯度(日本測地系:10進表記)
     * @param startLon スタート地点の経度(日本測地系:10進表記)
     * @param goalLat ゴール地点の緯度(日本測地系:10進表記)
     * @param goalLon ゴール地点の経度(日本測地系:10進表記)
     * @return 2点の距離(単位はメートル)
     */
    public int getDistance(int startLat, int startLon, int goalLat, int goalLon)
    {
        double startLatW;
        double startLonW;
        double goalLatW;
        double goalLonW;
        double startX;
        double startY;
        double goalX;
        double goalY;
        double degree;
        double dist;
        long distLong;
        int distNum;

        try
        {
            startLatW = (double)startLat;
            startLonW = (double)startLon;
            goalLatW = (double)goalLat;
            goalLonW = (double)goalLon;
            startX = (goalLonW / 3600000) * Math.PI / 180;
            startY = (goalLatW / 3600000) * Math.PI / 180;
            goalX = (startLonW / 3600000) * Math.PI / 180;
            goalY = (startLatW / 3600000) * Math.PI / 180;
            degree = Math.sin( startY ) * Math.sin( goalY ) + Math.cos( startY ) * Math.cos( goalY ) * Math.cos( goalX - startX );
            dist = 6378140 * (Math.atan( -degree / Math.sqrt( -degree * degree + 1 ) ) + Math.PI / 2);
            distLong = Math.round( dist );
            distNum = (int)distLong;
        }
        catch ( Exception e )
        {
            Logging.error( "[DistanceDetermination.getDistance] Exception:" + e.toString() );
            distNum = 0;
        }
        return(distNum);
    }
}
