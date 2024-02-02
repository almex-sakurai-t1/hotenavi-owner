package jp.happyhotel.action;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApHotelCustom;
import jp.happyhotel.others.GenerateXmlHapiTouchHtMemberChange;

/**
 * ハピホテアプリチェックインクラス
 * 
 * @author S.Tashiro
 * @version 1.0 2014/08/26
 * 
 */

public class ActionHtMemberChange extends BaseAction
{
    private static final String CONTENT_TYPE = "text/xml; charset=UTF-8";
    private static final String ENCODE       = "UTF-8";

    /**
     * ハピホテタッチ
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        Logging.info( "HtMemberChange Called" );

        // XML出力
        boolean ret = false;
        boolean retMemberOperation = false;
        String paramId = "";
        int id = 0;
        int errorCode = 0;
        int result = 0;
        String customId = "";
        String newCustomId = "";
        DataApHotelCustom dahc = new DataApHotelCustom();
        GenerateXmlHapiTouchHtMemberChange gxTouch = new GenerateXmlHapiTouchHtMemberChange();
        ServletOutputStream stream = null;

        // ホテルIDはライセンスキー認証で通ったホテルIDをセット
        paramId = (String)request.getAttribute( "HOTEL_ID" );
        customId = request.getParameter( "memberId" );
        newCustomId = request.getParameter( "newMemberId" );
        if ( paramId == null )
        {
            paramId = "0";
        }
        if ( customId == null || customId.equals( "" ) != false || CheckString.numCheck( customId ) == false )
        {
            customId = "0";
        }
        if ( newCustomId == null || newCustomId.equals( "" ) != false || CheckString.numCheck( newCustomId ) == false )
        {
            newCustomId = "0";
        }

        id = Integer.parseInt( paramId );
        Logging.info( "[ActionHtMemberChange]customId:" + customId );
        Logging.info( "[ActionHtMemberChange]newCustomId:" + newCustomId );

        try
        {
            stream = response.getOutputStream();
            if ( dahc.customerCount( id, customId ) > 0 )
            {
                // ホテルIDとメンバーIDからデータを取得
                ret = dahc.changeNewCustomId( id, customId, newCustomId );
                if ( ret != false )
                {
                    gxTouch.setResult( "OK" );
                }
                else
                {
                    gxTouch.setResult( "NG" );
                }
            }
            else
            {
                gxTouch.setResult( "OK" );
            }

            gxTouch.setErrorCode( errorCode );

            // XMLの出力
            String xmlOut = gxTouch.createXml();
            Logging.info( xmlOut );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHtMemberChange execute]Exception:" + e.toString() );

            // エラーが発生した場合も返す。
            gxTouch.setResult( "NG" );
            gxTouch.setErrorCode( 99999 );

            try
            {
                // エラーが発生した場合も返す。
                gxTouch.setResult( "NG" );
                gxTouch.setErrorCode( 99999 );

                stream = response.getOutputStream();
                // XMLの出力
                String xmlOut = gxTouch.createXml();
                Logging.info( xmlOut );
                ServletOutputStream out = null;
                out = response.getOutputStream();
                response.setContentType( CONTENT_TYPE );
                out.write( xmlOut.getBytes( ENCODE ) );
            }
            catch ( Exception exception )
            {
                Logging.error( "[ActionHtMemberReuse execute]Exception:" + exception.toString() );
            }

        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHtMemberChange execute]Exception:" + e.toString() );
                }
            }
        }
    }
}
