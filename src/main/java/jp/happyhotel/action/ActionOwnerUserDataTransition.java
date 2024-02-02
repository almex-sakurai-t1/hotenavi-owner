package jp.happyhotel.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataTransition;

public class ActionOwnerUserDataTransition
{

    /**
     * データ移行処理
     * 
     * @param request
     * @return
     * @throws IOException
     */
    public String doUserDataTransition(HttpServletRequest request) throws IOException
    {

        StringBuilder resultScript = new StringBuilder();

        String sourceUserID = "";
        String destinationUserID = "";

        DataTransition dt = null;

        try
        {

            sourceUserID = request.getParameter( "sourceUserID" );
            destinationUserID = request.getParameter( "destinationUserID" );

            dt = new DataTransition( sourceUserID, destinationUserID );

            // データ移行開始
            dt.transitionData();

            resultScript.append( "alert('データ移行が完了しました。');" );
            resultScript.append( "$('#uuidHistoryResultBody, #uuidHistoryResultPagingDiv, #userMileBalanceBody, #userMileBalancePagingDiv').empty();" );
            resultScript.append( "showTransitionDiv(0);" );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionOwnerUserDataTransition.doUserDataTransition] Exception=" + e.toString() );
            resultScript = new StringBuilder();
            resultScript.append( "alert('エラーが発生しました。');" );
        }
        finally
        {

            sourceUserID = null;
            destinationUserID = null;

            dt = null;
        }

        return resultScript.toString();

    }

}
