package jp.happyhotel.action;

import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.owner.LogicOwnerRsvPlanCharge;

/**
 *
 * プラン別料金、モード別料金設定画面で使用する最低予約金額チェック用Action
 */
public class ActionOwnerRsvCheckCharge extends BaseAction
{

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int paramHotelId = 0;
        int chashDeposit = 0;
        int charge = 0;
        int ret = 0;
        LogicOwnerRsvPlanCharge logic = new LogicOwnerRsvPlanCharge();
        ArrayList<Integer> modeList = new ArrayList<Integer>();
        PrintWriter writer;

        // 画面でチェックされている内容を取得
        String[] selModes = request.getParameterValues( "sel" );
        if ( selModes != null )
        {
            for( int i = 0 ; i < selModes.length ; i++ )
            {
                modeList.add( Integer.parseInt( selModes[i] ) );
            }
        }
        paramHotelId = Integer.parseInt( request.getParameter( "selHotelId" ) );

        try
        {
            //最低予約金額取得
            chashDeposit = logic.getChashDeposit( paramHotelId );

            //チェックされている料金モードの金額と、最低予約金額を比較
            for (int i = 0; i < modeList.size(); i ++) {
                //大人2人料金
                charge = Integer.parseInt( request.getParameter( "adultTwo" + modeList.get( i ) ) );
                if ( chashDeposit > charge ) {
                    ret = 1;
                    break;
                }
                //大人一人
                charge = Integer.parseInt( request.getParameter( "adultOne" + modeList.get( i ) ) );
                if ( chashDeposit > charge ) {
                    ret = 1;
                    break;
                }
                //大人追加
                charge = Integer.parseInt( request.getParameter( "adultAdd" + modeList.get( i ) ) );
                if ( chashDeposit > charge ) {
                    ret = 1;
                    break;
                }
                //子供追加
                charge = Integer.parseInt( request.getParameter( "childAdd" + modeList.get( i ) ) );
                if ( chashDeposit > charge ) {
                    ret = 1;
                    break;
                }
            }

            writer = response.getWriter();
            writer.print( ret );
            writer.close();
        }
        catch ( Exception e )
        {
            Logging.error( "Error ActionOwnerRsvCheckCharge.execute = " + e.toString() );
        }
    }
}
