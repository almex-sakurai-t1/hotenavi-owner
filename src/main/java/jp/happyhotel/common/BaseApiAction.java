package jp.happyhotel.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.data.DataApiError;
import jp.happyhotel.data.DataApiResult;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

/**
 * APIのActionクラスのabstractクラス
 * 
 */
public class BaseApiAction extends BaseAction
{
    protected boolean isDebug = false;

    public BaseApiAction()
    {
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
    }

    public void initialize(HttpServletRequest request)
    {
        isDebug = StringUtils.equals( request.getParameter( "debug" ), "true" );
    }

    /**
     * jsonをレスポンスに出力
     * 
     * @param response
     * @param result
     * @throws Exception
     */
    public void outputJson(HttpServletResponse response, DataApiResult result) throws Exception
    {
        outputJson( response, result, null );
    }

    /**
     * jsonをレスポンスに出力
     * 
     * @param response
     * @param result
     * @param excludes
     * @throws Exception
     */
    public void outputJson(HttpServletResponse response, DataApiResult result, String[] excludes) throws Exception
    {
        try
        {

            Map<String, DataApiResult> map = new HashMap<String, DataApiResult>();
            map.put( "results", result );
            JSONObject jsonObject = JSONObject.fromObject( map, excludes );
            String jsonOut;
            if ( this.isDebug )
            {
                jsonOut = jsonObject.toString( 4 );
            }
            else
            {
                jsonOut = jsonObject.toString();
            }

            jsonOut = ReplaceString.replaceApiBr2Space( jsonOut );
            Logging.info( "[BaseApiAction.outputJson() ] jsonOut:" + jsonOut );

            // ObjectMapper mapper = new ObjectMapper();
            // mapper.enable( SerializationFeature.INDENT_OUTPUT );
            // String jsonOut = mapper.writeValueAsString( result );

            response.setContentType( "application/json; charset=utf-8" );
            response.addHeader( "X-Content-Type-Options", "nosniff" );
            ServletOutputStream out = response.getOutputStream();
            out.write( jsonOut.getBytes( "UTF-8" ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[BaseApiAction.outputErrorJon() ] Exception", e );

        }
    }

    /**
     * エラーjsonをレスポンスに出力
     * 
     * @param response
     * @param code
     * @param message
     */
    public void outputErrorJon(HttpServletResponse response, int code, String message)
    {
        outputErrorJon( response, String.valueOf( code ), message );
    }

    /**
     * エラーjsonをレスポンスに出力
     * 
     * @param response
     * @param code
     * @param message
     */
    public void outputErrorJon(HttpServletResponse response, String code, String message)
    {
        DataApiError result = new DataApiError( code, convertEOL( message ) );
        try
        {
            outputJson( response, result );
        }
        catch ( Exception e )
        {
            Logging.error( "[BaseApiAction.outputErrorJon() ] Exception", e );

        }
    }

    /**
     * 改行コードを統一する
     * 
     * @param str
     * @return
     */
    public static String convertEOL(String str)
    {
        return str.replaceAll( "(\r\n|\r|\n)", "\n" );
    }

}
