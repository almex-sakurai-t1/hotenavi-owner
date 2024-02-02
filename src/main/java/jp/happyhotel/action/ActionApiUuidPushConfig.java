package jp.happyhotel.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApUuidPushConfig;

public class ActionApiUuidPushConfig extends BaseAction
{
    private DataApUuidPushConfig dataApUuidPushConfig;

    /**
     * �n�s�z�ePUSH�ʒm�ݒ�Action�N���X
     *
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String uuid = "";
        String pushFlag = "";
        String coFlag = "";
        String campaignFlag = "";

        Map<String, String[]> requestMap = null;
        boolean ret = false;

        try
        {

            uuid = request.getParameter( "uuid" );
            requestMap = request.getParameterMap();

            if ( requestMap != null )
            {
                for( Map.Entry<String, String[]> entry : requestMap.entrySet() )
                {
                    String key = entry.getKey();

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

            // �w�S�̒ʒm�x�w�`�F�b�N�A�E�g�ʒm�x�w���m�点�xpush�ʒm�ݒ�
            if ( pushFlag != "" || coFlag != "" || campaignFlag != "" )
            {
                dataApUuidPushConfig = new DataApUuidPushConfig();
                dataApUuidPushConfig.getData( uuid );

                if ( pushFlag != null )
                {
                    dataApUuidPushConfig.setPushFlag( pushFlag.equals( "false" ) ? 0 : 1 );
                }
                if ( coFlag != null )
                {
                    dataApUuidPushConfig.setCoFlag( coFlag.equals( "false" ) ? 0 : 1 );
                }
                if ( campaignFlag != null )
                {
                    dataApUuidPushConfig.setCampaignFlag( campaignFlag.equals( "false" ) ? 0 : 1 );
                }
                dataApUuidPushConfig.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dataApUuidPushConfig.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );

                ret = dataApUuidPushConfig.updateData( uuid );
            }

            if ( ret != false )
            {
                response.sendRedirect( "../../phone/htap/PushInfoControlTotal.jsp?uuid=" + uuid );
            }
            else
            {
                // TODO �X�V������ �X�V����0���̏ꍇ

            }

        }
        catch ( Exception exception )
        {
            Logging.error( "ActionApiUuidPushConfig : " + exception );
        }
        finally
        {
        }
    }

}
