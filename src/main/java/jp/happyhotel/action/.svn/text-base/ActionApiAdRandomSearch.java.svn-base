package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.others.GenerateXmlAd;
import jp.happyhotel.others.GenerateXmlContents;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.others.GenerateXmlSearchResult;
import jp.happyhotel.sponsor.SponsorData_M2;

/**
 * 
 * 住所検索クラス（携帯）
 * 
 * @author S.Tashiro
 * @version 1.0 2011/04/06
 */

public class ActionApiAdRandomSearch
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
        final int DISP_COUNT = 10;
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
        sd = new SponsorData_M2();
        
        GenerateXmlHeader header = new GenerateXmlHeader();
        GenerateXmlSearchResult result = new GenerateXmlSearchResult();
        try
        {
            paramPrefId = request.getParameter( "pref_id" );

            if ( paramPrefId == null || paramPrefId.equals( "" ) != false || CheckString.numCheck( paramPrefId ) == false )
            {
                paramPrefId = "0";
            }
            // ローテーションバナー取得
            ret = sd.getAdRandomData( Integer.parseInt( paramPrefId ), DISP_COUNT, DISP_FLAG );

            if ( ret != false )
            {

                for( int i = 0 ; i < sd.getSponsor().length ; i++ )
                {
                    GenerateXmlAd ad = new GenerateXmlAd();
                    // スマートフォンの表示数を追加
                    sd.setImpressionCountForSmart( sd.getSponsor()[i].getSponsorCode() );

                    // 広告用のXMLを追加
                    ad.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                    if ( Integer.parseInt( paramPrefId ) > 0 )
                    {
                        ad.setAdInfo2( sd.getSponsor()[i],request );
                    }
                    else
                    {
                        ad.setAdInfo( sd.getSponsor()[i],request );
                    }
                    result.addAd( ad );

                }
                result.setResultCount( sd.getSponsor().length );

                result.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                String xmlOut = result.createXml();
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
                
                Logging.debug( "[ActionApiAdRandomSearch.execute() ][paramPrefId = " + paramPrefId + "] xmlOut:" + xmlOut );
            }
            
            
            
            
            
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionApiAdRandomSearch.execute() ][paramPrefId = " + paramPrefId + "] Exception", exception );

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
                Logging.error( "[ActionApiAdRandomSearch.execute() ][paramPrefId = " + paramPrefId + "] Exception:" + e.toString() );
            }

        }
    }
}
