package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApHotelCustom;
import jp.happyhotel.others.GenerateXmlDeleteResult;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.user.UserLoginInfo;
import jp.happyhotel.user.UserMyHotel;

public class ActionApiMyHotelDelete extends BaseAction
{
    // TODO エラー時文言定数
    // static String xxxxx = Constants.xxxxx;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int idNum = 0;
        String paramMethod = null; // メソッド
        String paramId = null; // ホテルID
        UserLoginInfo uli;
        UserMyHotel myhotel = null;
        int hotelCount = 0;
        int errorCode = 0;
        String errorMsg = "";
        boolean ret = false;
        DataApHotelCustom dahc = new DataApHotelCustom();

        try
        {
            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramMethod = request.getParameter( "method" );
            paramId = request.getParameter( "id" );
            if ( paramId == null || paramId.equals( "" ) != false || CheckString.numCheck( paramId ) == false )
            {
                paramId = "0";
            }

            if ( uli == null )
            {
                uli = new UserLoginInfo();
            }
            if ( uli.getUserInfo() != null )
            {
                idNum = Integer.parseInt( paramId );

                myhotel = new UserMyHotel();
                ret = myhotel.setDeleteMyHotel( uli.getUserInfo().getUserId(), idNum );
                dahc.getData( idNum, uli.getUserInfo().getUserId() );
                dahc.setDelFlag( 1 );
                dahc.updateData( idNum, uli.getUserInfo().getUserId() );

                hotelCount = 1;
                errorCode = 0;

                if ( ret == false )
                {
                    hotelCount = 0;
                    // TODO エラー毎のerrorCode
                    errorCode = Constants.ERROR_CODE_API15;
                    errorMsg = Constants.ERROR_MSG_API15;
                }

            }
            else
            {
                // ユーザ基本情報取得がnullの場合
                // エラー毎のerrorCode
                hotelCount = 0;
                errorCode = Constants.ERROR_CODE_API16;
                errorMsg = Constants.ERROR_MSG_API16;

            }

            // 削除結果作成
            GenerateXmlDeleteResult deleteResult = new GenerateXmlDeleteResult();
            deleteResult.setErrorCode( errorCode );
            deleteResult.setErrorMessage( errorMsg );

            // 削除結果ヘッダ作成
            GenerateXmlHeader deleteHeader = new GenerateXmlHeader();
            deleteHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            deleteHeader.setMethod( paramMethod );
            deleteHeader.setName( "マイホテル削除" );
            deleteHeader.setCount( hotelCount );
            // 検索結果ノードを検索結果ヘッダーノードに追加
            deleteHeader.setDeleteResult( deleteResult );

            String xmlOut = deleteHeader.createXml();
            ServletOutputStream out = null;

            out = response.getOutputStream();
            response.setContentType( "text/xml; charset=UTF-8" );
            out.write( xmlOut.getBytes( "UTF-8" ) );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionApiMyHotelDelete.execute() ] Exception:", exception );

            // エラーを出力
            GenerateXmlHeader deleteHeader = new GenerateXmlHeader();
            GenerateXmlDeleteResult deleteResult = new GenerateXmlDeleteResult();
            // TODO エラーコード、文言
            errorCode = 3;
            deleteResult.setErrorCode( errorCode );
            deleteResult.setErrorMessage( Constants.ERROR_MSG_API10 );

            // 削除結果ヘッダ作成
            deleteHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            deleteHeader.setMethod( paramMethod );
            deleteHeader.setName( "マイホテル削除" );
            deleteHeader.setCount( 0 );
            // 削除結果を追加
            deleteHeader.setDeleteResult( deleteResult );

            String xmlOut = deleteHeader.createXml();
            ServletOutputStream out = null;

            try
            {
                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionApiMyHotelDelete response]Exception:" + e.toString() );
            }
        }
        finally
        {
        }

    }

}
