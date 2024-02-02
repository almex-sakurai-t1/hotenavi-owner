package jp.happyhotel.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReserveCommon;


public class ActionReserveSearchAddress extends BaseAction
{

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String paramZip3 = "";
        String paramZip4 = "";
        int paramPrefId = 0;
        int paramJisCd = 0;
        JSONArray jsonAllArray = new JSONArray();
        PrintWriter writer;
        ReserveCommon rsvCmm = new ReserveCommon();

        try
        {
            paramZip3 = request.getParameter( "zip3" );
            paramZip4 = request.getParameter( "zip4" );
            if ( (request.getParameter( "prefId" ) != null) && (request.getParameter( "prefId" ).length() != 0) ) {
                paramPrefId = Integer.parseInt( request.getParameter( "prefId" ) );
            }
            if ( (request.getParameter( "jisCd" ) != null) && (request.getParameter( "jisCd" ).length() != 0) ) {
                paramJisCd = Integer.parseInt( request.getParameter( "jisCd" ) );
            }

            if ((paramZip3.trim().length() != 0) || (paramZip4.trim().length() != 0) ){
                //óXï÷î‘çÜÇ≈åüçı
                jsonAllArray = rsvCmm.getZipAddress(paramZip3, paramZip4);

            } else {
                //ëIëÇ≥ÇÍÇΩìsìπï{åßÇ≈ëIë
                jsonAllArray = rsvCmm.getPrefAddress(paramPrefId, paramJisCd);
            }

            response.setContentType( "application/json; charset=UTF-8" );
            writer = response.getWriter();
            writer.print( jsonAllArray.toString() );
            writer.close();

            return;
        }
        catch ( Exception e )
        {
            Logging.error("Error ActionReserveAddressSearch.execute = " + e.toString());
        }
    }
}
