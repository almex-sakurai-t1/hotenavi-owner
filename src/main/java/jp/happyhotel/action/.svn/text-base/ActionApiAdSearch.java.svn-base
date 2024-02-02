package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.others.GenerateXmlAd;
import jp.happyhotel.others.GenerateXmlContents;
import jp.happyhotel.sponsor.SponsorData_M2;

/**
 * 
 * 住所検索クラス（携帯）
 * 
 * @author S.Tashiro
 * @version 1.0 2011/04/06
 */

public class ActionApiAdSearch
{

    static int              pageRecords     = Constants.pageLimitRecords;
    static int              maxRecords      = Constants.maxRecordsMobile;
    static String           recordNotFound2 = Constants.errorRecordsNotFound2;
    public static final int dispFormat      = 1;

    /**
     * 住所検索
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        final int DISP_COUNT = 1;
        final int DISP_FLAG = 2;
        boolean ret = false;
        int kind = 0;
        int chainId = 0;
        int prefId;
        String url = "";
        String dispText = "";
        String paramPrefId = null;
        String errorMsg = "";
        String paramMethod = null;
        SponsorData_M2 sd;
        GenerateXmlAd ad = new GenerateXmlAd();
        sd = new SponsorData_M2();

        try
        {
            paramPrefId = request.getParameter( "pref_id" );

            if ( paramPrefId == null || paramPrefId.equals( "" ) != false || CheckString.numCheck( paramPrefId ) == false )
            {
                paramPrefId = "0";
            }
            ret = sd.getAdData( Integer.parseInt( paramPrefId ), DISP_COUNT, DISP_FLAG );

            if ( ret != false )
            {
                // スマートフォンの表示数を追加
                sd.setImpressionCountForSmart( sd.getSponsor()[0].getSponsorCode() );

                // 広告用のXMLを追加
                ad.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                ad.setAdInfo( sd.getSponsor()[0] ,request);

                String xmlOut = ad.createXml();
                ServletOutputStream out = null;
                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );

            }
            else
            {
                GenerateXmlContents contents = new GenerateXmlContents();
                contents.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                contents.setError( Constants.ERROR_MSG_API6 );
                contents.setErrorCode( Constants.ERROR_CODE_API6 );

                String xmlOut = contents.createXml();
                ServletOutputStream out = null;
                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionApiAreaSearch.execute() ][paramPrefId = " + paramPrefId + "] Exception", exception );

            GenerateXmlContents contents = new GenerateXmlContents();
            contents.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            contents.setError( Constants.ERROR_MSG_API6 );
            contents.setErrorCode( Constants.ERROR_CODE_API6 );

            String xmlOut = contents.createXml();
            ServletOutputStream out = null;
            try
            {
                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionApiAreaSearch.execute() ][paramPrefId = " + paramPrefId + "] Exception:" + e.toString() );
            }

        }
    }
}
