package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.HotelIp;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApContentsConfig;
import jp.happyhotel.data.DataApHotelCustom;
import jp.happyhotel.data.DataApHotelSetting;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.dto.DtoApCommon;
import jp.happyhotel.dto.DtoApHotelCustomerData;
import jp.happyhotel.dto.DtoApMemberCardReg;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.touch.MemberAcceptInfo;
import jp.happyhotel.touch.MemberRegistExInfo;

/**
 * ハピホテアプリチェックインクラス
 * 
 * @author S.Tashiro
 * @version 1.0 2014/08/26
 * 
 */

public class ActionHtAcceptInfo extends BaseAction
{
    final int                    TIMEOUT             = 5000;
    final int                    HOTENAVI_PORT_NO    = 7023;
    final int                    HAPIHOTE_PORT_NO    = 7046;
    final String                 HTTP                = "http://";
    final String                 CLASS_NAME          = "hapiTouch.act?method=";
    final int                    RESULT_OK           = 1;
    final int                    RESULT_NG           = 2;
    private RequestDispatcher    requestDispatcher;
    private DtoApCommon          apCommon;
    DtoApHotelCustomerData       apHotelCustomerData = null;
    private DtoApMemberCardReg   apMemberCardReg;
    private DataApContentsConfig config              = new DataApContentsConfig();

    /**
     * ハピホテタッチ
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        boolean ret = false;
        boolean boolHotelCustom = false;// 顧客対応状況
        boolean boolMemberAccept = false;// メンバー情報受付結果

        String userId = "";// ユーザID
        String hotenaviIp = "";
        String customId = "";
        String contents = "";

        DataHotelBasic dhb = new DataHotelBasic();
        DataHotelRoomMore dhrm = new DataHotelRoomMore();
        DataApHotelSetting dahs = new DataApHotelSetting();
        DataApHotelCustom dahc = new DataApHotelCustom();
        HotelCi hc = new HotelCi();

        MemberAcceptInfo memberAcceptInfo = new MemberAcceptInfo();// メンバー受付情報クラス

        String paramSeq;
        String paramId;
        String forwardUrl = "";
        try
        {
            // IDとSeqからタッチデータを取得
            paramSeq = request.getParameter( "seq" );
            paramId = request.getParameter( "id" );
            if ( paramSeq == null || paramSeq.equals( "" ) != false || CheckString.numCheck( paramSeq ) == false )
            {
                paramSeq = "0";
            }
            if ( paramId == null || paramId.equals( "" ) != false || CheckString.numCheck( paramId ) == false )
            {
                paramId = "0";
            }
            if ( Integer.parseInt( paramSeq ) > 0 && Integer.parseInt( paramId ) > 0 )
            {
                // 顧客メンバー導入物件チェック
                boolHotelCustom = dahs.getData( Integer.parseInt( paramId ) );

                ret = hc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) );
                // フェリカデータから取得
                if ( hc.getHotelCi().getUserId().equals( "" ) == false )
                {
                    userId = hc.getHotelCi().getUserId();
                    // 既にメンバー登録ずみかどうかをチェック
                    ret = dahc.getData( Integer.parseInt( paramId ), userId );
                }

                // ホテルのフロントIPを取得
                hotenaviIp = HotelIp.getHotenaviIp( Integer.parseInt( paramId ) );

                dhrm.getData( Integer.parseInt( paramId ), hc.getHotelCi().getRoomNo() );

                // 顧客導入物件
                if ( boolHotelCustom != false )
                {
                    // メンバーなし→部屋に対して磁気カード挿入チェック
                    /** （1042）ホテナビ_メンバー受付情報取得電文 **/
                    memberAcceptInfo.setRoomName( dhrm.getRoomNameHost() );
                    memberAcceptInfo.sendToHost( hotenaviIp, TIMEOUT, HOTENAVI_PORT_NO, paramId );

                    // MemberInfo memberInfo = new MemberInfo();
                    MemberRegistExInfo memberRegistExInfo = new MemberRegistExInfo();

                    // カード受付済み
                    if ( memberAcceptInfo.getResult() == RESULT_OK )
                    {
                        boolMemberAccept = true;
                        // メンバーIDを取得
                        // カード受付済み→「新規会員登録受付」と、「このカードを使用する」
                        customId = memberAcceptInfo.getMemberId();

                        // 以前使用していたメンバー情報取得電文（1002）
                        // customId = memberAcceptInfo.getMemberId();
                        // memberInfo.setMemberId( customId );
                        // memberInfo.setBirthMonth( Integer.parseInt( memberAcceptInfo.getBirthMonth() ) );
                        // memberInfo.setBirthDay( Integer.parseInt( memberAcceptInfo.getBirthDay() ) );

                        // ホテナビ電文　メンバー登録前情報取得電文（1050）
                        memberRegistExInfo.setMemberId( customId );
                        memberRegistExInfo.setBirthMonth1( Integer.parseInt( memberAcceptInfo.getBirthMonth() ) );
                        memberRegistExInfo.setBirthDate1( Integer.parseInt( memberAcceptInfo.getBirthDay() ) );

                        // memberInfo.sendToHost( hotenaviIp, TIMEOUT, HOTENAVI_PORT_NO, paramId );
                        // if ( memberInfo.getResult() == RESULT_OK )
                        // {
                        // apHotelCustomerData = memberInfo.getMemberInfo();
                        // }
                        memberRegistExInfo.sendToHost( hotenaviIp, TIMEOUT, HOTENAVI_PORT_NO, paramId );
                        if ( memberRegistExInfo.getResult() == RESULT_OK )
                        {
                            apHotelCustomerData = memberRegistExInfo.getMemberInfo();
                        }

                    }
                }
            }

            if ( customId.equals( "" ) == false )
            {
                // forwardUrl = "MemberCardRegForm.jsp";
                forwardUrl = "MemberCard.jsp";
            }
            else
            {
                forwardUrl = "MemberCardRegIntoApplication.jsp";
            }

            if ( config.getDataCommon( "MemberCard.jsp", 0, Integer.parseInt( paramId ), 0 ) )
            {
                contents = config.getContents();
            }

            apCommon = new DtoApCommon();
            apMemberCardReg = new DtoApMemberCardReg();
            // メンバー情報取得に失敗していたら初期化だけ行う
            if ( apHotelCustomerData == null )
            {
                apHotelCustomerData = new DtoApHotelCustomerData();
            }

            // 共通設定
            apCommon.setHtCheckIn( ret );
            apCommon.setId( Integer.parseInt( paramId ) );
            apCommon.setHotelName( dhb.getName() );
            apCommon.setRoomNo( dhrm.getRoomNameHost() );
            apCommon.setSeq( hc.getHotelCi().getSeq() );

            // ホテル顧客
            apMemberCardReg.setApCommon( apCommon );
            apMemberCardReg.setCustomId( customId );
            apMemberCardReg.setResult( boolMemberAccept );

            apHotelCustomerData.setApCommon( apCommon );
            apHotelCustomerData.setCustomId( customId );
            apHotelCustomerData.setContents( contents );

            request.setAttribute( "DtoApCommon", apCommon );
            request.setAttribute( "DtoApMemberCardReg", apMemberCardReg );
            request.setAttribute( "DtoApHotelCustomerData", apHotelCustomerData );
            request.setAttribute( "id", paramId );
            request.setAttribute( "seq", paramSeq );

            requestDispatcher = request.getRequestDispatcher( forwardUrl );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "HtAcceptInfo:" + exception );
        }
        finally
        {
        }
    }
}
