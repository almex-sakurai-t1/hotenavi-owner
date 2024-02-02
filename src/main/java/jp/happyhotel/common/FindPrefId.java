/*
 * @(#)FindPrefId.java 1.00 2009/12/08
 * Copyright (C) ALMEX Inc. 2009
 * 都道府県ID取得クラス
 */

package jp.happyhotel.common;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import jp.happyhotel.data.DataMapPoint;
import jp.happyhotel.data.DataMasterArea;
import jp.happyhotel.data.DataMasterCity;

;

/**
 * 都道府県ID取得クラス
 * 都道府県IDをいろいろな条件から取得する
 * 
 * @author N.Ide
 * @version 1.00 2009/12/08
 */
public class FindPrefId implements Serializable
{
    private static final long serialVersionUID = -3685059929468200562L;

    /**
     * 都道府県IDを取得する
     * 
     * @param request リクエスト
     * @return 処理結果(0:該当県なし、それ以外:該当する都道府県ID)
     * @see "以下のパラメータがあれば都道府県IDを取得できる<br>
     *      prefId:都道府県ID<br>
     *      jisCode:市区町村ID<br>
     *      areaId:ホテル街ID<br>
     *      routeId:ルートID"
     */
    static public int getPrefId(HttpServletRequest request)
    {
        int prefId;
        prefId = 0;

        String paramAreaId = "";
        String paramJisCode = "";
        String paramPrefId = "";
        String paramIcId;

        int nAreaId = 0;
        int nJisCode = 0;
        boolean ret = false;

        DataMasterCity dmc;
        DataMasterArea dma;
        DataMapPoint dmap;

        dmc = new DataMasterCity();

        paramAreaId = request.getParameter( "area_id" );
        if ( paramAreaId == null )
        {
            paramAreaId = "";
        }
        else if ( CheckString.numCheck( paramAreaId ) == false )
        {
            paramAreaId = "";
        }

        paramJisCode = request.getParameter( "jis_code" );
        if ( paramJisCode == null )
        {
            paramJisCode = "";
        }
        else if ( CheckString.numCheck( paramJisCode ) == false )
        {
            paramJisCode = "";
        }

        paramPrefId = request.getParameter( "pref_id" );
        if ( paramPrefId == null )
        {
            paramPrefId = "";
        }
        else if ( CheckString.numCheck( paramPrefId ) == false )
        {
            paramPrefId = "";
        }

        paramIcId = request.getParameter( "route_id" );
        if ( paramIcId == null )
        {
            paramIcId = "";
        }

        // パラメータのIDの優先順位にあわせて、都道府県IDを取得する
        //
        // 　1.ホテル外コード（areaId）
        // 　2.市区町村コード（JisCode）
        // 　3.都道府県ID （prefId）
        // 　4.ルートID（routeId）
        try
        {
            if ( paramAreaId.compareTo( "" ) != 0 )
            {
                dma = new DataMasterArea();

                nAreaId = Integer.parseInt( paramAreaId );
                ret = dma.getData( nAreaId );
                if ( ret != false )
                {
                    prefId = dma.getPrefId();
                }
            }
            else if ( paramJisCode.compareTo( "" ) != 0 )
            {
                nJisCode = Integer.parseInt( paramJisCode );
                ret = dmc.getData( nJisCode );
                if ( ret != false )
                {
                    prefId = dmc.getPrefId();
                }
            }
            else if ( paramPrefId.compareTo( "" ) != 0 )
            {
                prefId = Integer.parseInt( paramPrefId );
            }
            else if ( paramIcId.compareTo( "" ) != 0 )
            {
                dmap = new DataMapPoint();
                dmap.getData( paramIcId );

                // JISコードよりpref_idを取得する
                if ( dmap.getJisCode() != 0 )
                {
                    ret = dmc.getData( dmap.getJisCode() );
                    if ( ret != false )
                    {
                        prefId = dmc.getPrefId();
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[ FindPrefId.getPrefId() ] Exception:" + e.toString() );
        }
        if ( prefId < 0 )
        {
            prefId = 0;
        }
        return(prefId);
    }
}
