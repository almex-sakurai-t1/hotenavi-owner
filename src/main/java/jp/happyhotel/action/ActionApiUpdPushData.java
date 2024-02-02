package jp.happyhotel.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApUserPushConfig;
import jp.happyhotel.user.UserMyArea;
import jp.happyhotel.user.UserMyHotel;

public class ActionApiUpdPushData extends BaseAction
{
    private DataApUserPushConfig dataApUserPushConfig;
    private UserMyHotel          userMyHotel;
    private UserMyArea           userMyArea;

    /**
     * ハピホテタッチ
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String userId = "";
        String passwd = "";
        String pushFlag = "";
        String coFlag = "";
        String campaignFlag = "";

        int hotelId = 0;
        int seq = 0;
        int myHotelPushFlag = 0;
        int myAreaPushFlag = 0;
        Map<String, String[]> requestMap = null;
        boolean ret = false;

        try
        {

            userId = request.getParameter( "user_id" );
            passwd = request.getParameter( "password" );
            requestMap = request.getParameterMap();
            userMyHotel = new UserMyHotel();
            userMyArea = new UserMyArea();

            if ( requestMap != null )
            {
                for( Map.Entry<String, String[]> entry : requestMap.entrySet() )
                {
                    String key = entry.getKey();
                    String value = entry.getValue()[0];

                    if ( key.startsWith( "myHotel" ) )
                    {
                        // 『マイホテル』push通知設定
                        hotelId = Integer.parseInt( key.substring( "myHotel".length() ) );
                        myHotelPushFlag = "false".equals( value ) ? 0 : 1;
                        ret = userMyHotel.updataMyHotelData( userId, hotelId, myHotelPushFlag );
                    }

                    if ( key.startsWith( "myArea" ) )
                    {
                        // 『マイエリア』push通知設定
                        seq = Integer.parseInt( key.substring( "myArea".length() ) );
                        myAreaPushFlag = "false".equals( value ) ? 0 : 1;
                        ret = userMyArea.updataMyAreaData( userId, seq, myAreaPushFlag );
                    }

                    if ( key.equals( "pushFlag" ) )
                    {
                        pushFlag = entry.getValue()[0];
                    }
                    if ( key.equals( "coFlag" ) )
                    {
                        coFlag = entry.getValue()[0];
                    }
                    if ( key.equals( "campaignFlag" ) )
                    {
                        campaignFlag = entry.getValue()[0];
                    }
                }
            }

            // 『全体通知』『チェックアウト通知』『お知らせ』push通知設定
            if ( pushFlag != "" || coFlag != "" || campaignFlag != "" )
            {
                dataApUserPushConfig = new DataApUserPushConfig();
                dataApUserPushConfig.getData( userId );

                if ( pushFlag != null )
                {
                    dataApUserPushConfig.setPushFlag( pushFlag.equals( "false" ) ? 0 : 1 );
                }
                if ( coFlag != null )
                {
                    dataApUserPushConfig.setCoFlag( coFlag.equals( "false" ) ? 0 : 1 );
                }
                if ( campaignFlag != null )
                {
                    dataApUserPushConfig.setCampaignFlag( campaignFlag.equals( "false" ) ? 0 : 1 );
                }
                dataApUserPushConfig.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dataApUserPushConfig.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );

                ret = dataApUserPushConfig.updateData( userId );
            }

            if ( ret != false )
            {
                response.sendRedirect( "../../phone/htap/PushInfoControl.jsp?user_id=" + userId + "&password=" + passwd );
            }
            else
            {
                // TODO 更新勝利後 更新件数0件の場合

            }

        }
        catch ( Exception exception )
        {
            Logging.error( "HtPushConfig:" + exception );
        }
        finally
        {
        }
    }

}
