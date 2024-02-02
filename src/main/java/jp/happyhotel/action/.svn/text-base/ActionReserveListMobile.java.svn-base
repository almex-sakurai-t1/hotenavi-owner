package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.PagingDetails;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.reserve.FormReserveListMobile;
import jp.happyhotel.reserve.LogicReserveListMobile;
import jp.happyhotel.user.UserBasicInfo;

/**
 *
 * 予約一覧画面 Action Class
 */

public class ActionReserveListMobile extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        boolean blnRet;
        boolean memberFlag;
        boolean ret;
        int type = 1;
        int pageNum = 0;
        String userid = "";
        String mode = "";
        String page = "";
        String pageLinks = "";
        String queryString = "";

        String strErr = "";
        String carrierUrl = "";
        String paramUidLink = "";
        String uidParam = "";
        String uidLink = "";
        String url = "";

        FormReserveListMobile dsp1;
        LogicReserveListMobile lgLM = null; // logic
        UserBasicInfo ubi;

        dsp1 = new FormReserveListMobile();

        ubi = new UserBasicInfo();

        try
        {
            userid = request.getParameter( "userid" );
            mode = request.getParameter( "mode" );
            page = request.getParameter( "page" );
            if ( !CheckString.numCheck( page ) )
            {
                page = "0";
            }

            pageNum = Integer.parseInt( page );

            // 画面内容を基に入力項目のチェック
            lgLM = new LogicReserveListMobile();
            blnRet = lgLM.getData( dsp1, mode, userid, pageNum );

            if ( blnRet == true )
            {
                request.setAttribute( "err", "" );

                request.setAttribute( "dsp", dsp1 );
            }
            else
            {
                request.setAttribute( "err", "お客様の御予約情報はございません。" );
                request.setAttribute( "dsp", dsp1 );
            }

            // ここから携帯情報取得
            paramUidLink = (String)request.getAttribute( "UID-LINK" );
            type = UserAgent.getUserAgentType( request );

            if ( type == UserAgent.USERAGENT_AU )
            {
                uidParam = request.getHeader( "x-up-subno" );
                uidLink = "";
                if ( (strErr == null) || (strErr.compareTo( "" ) == 0) )
                {
                    /**
                     * 本番用
                     */
                    if ( mode.equals( "1" ) )
                    {
                        // 予約一覧へ
                        carrierUrl = "../../au/reserve/reserve_list.jsp";
                    }
                    else
                    {
                        // 利用履歴一覧へ
                        carrierUrl = "../../au/reserve/reserve_list_history.jsp";
                    }
                }
            }
            else if ( type == UserAgent.USERAGENT_VODAFONE )
            {
                uidParam = request.getHeader( "x-jphone-uid" );
                // UID通知していない場合、uidParamがnullになる
                if ( uidParam != null )
                {
                    uidParam = uidParam.substring( 1 );
                }
                uidLink = "uid=1&sid=BN14&pid=P423";
                if ( (strErr == null) || (strErr.compareTo( "" ) == 0) )
                {
                    /**
                     * 本番用
                     */
                    if ( mode.equals( "1" ) )
                    {
                        // 予約一覧へ
                        carrierUrl = "../../y/reserve/reserve_list.jsp";
                    }
                    else
                    {
                        // 利用履歴一覧へ
                        carrierUrl = "../../y/reserve/reserve_list_history.jsp";
                    }
                }
            }
            else if ( type == UserAgent.USERAGENT_DOCOMO )
            {
                uidParam = request.getParameter( "uid" );
                uidLink = "uid=NULLGWDOCOMO";
                if ( (strErr == null) || (strErr.compareTo( "" ) == 0) )
                {
                    /**
                     * 本番用
                     */
                    if ( mode.equals( "1" ) )
                    {
                        // 予約一覧へ
                        carrierUrl = "../../i/reserve/reserve_list.jsp";
                    }
                    else
                    {
                        // 利用履歴一覧へ
                        carrierUrl = "../../i/reserve/reserve_list_history.jsp";
                    }
                }
            }
            else
            {
                uidLink = "";
                uidParam = "";
                if ( (strErr == null) || (strErr.compareTo( "" ) == 0) )
                {
                    /**
                     * 本番用
                     */
                    if ( mode.equals( "1" ) )
                    {
                        // 予約一覧へ
                        carrierUrl = "reserve_list.jsp";
                    }
                    else
                    {
                        // 利用履歴一覧へ
                        carrierUrl = "reserve_list_history.jsp";
                    }
                }
            }

            if ( uidParam != null )
            {
                if ( request.getServerPort() != 80 && type == UserAgent.USERAGENT_DOCOMO )
                {
                    ret = ubi.getUserBasicByMd5( uidParam );
                }
                else
                {
                    ret = ubi.getUserBasicByTermno( uidParam );
                }
                if ( ret != false )
                {
                    memberFlag = true;
                }
                else
                {
                    memberFlag = false;
                }

                if ( request.getServerPort() != 80 && type == UserAgent.USERAGENT_DOCOMO )
                {
                    uidLink = "uid=" + "uid=" + ubi.getUserInfo().getMailAddrMobileMd5();
                }
            }
            // ここまで

            //
            if ( lgLM.getMaxCnt() > 3 )
            {
                if ( mode.equals( "1" ) )
                {
                    // 予約一覧
                    queryString = "actionReserveListMobile.act?userid=" + userid + "&mode=" + mode;
                }
                else
                {
                    // 利用履歴一覧
                    queryString = "actionReserveListMobile.act?userid=" + userid + "&mode=" + mode;
                }
                pageLinks = PagingDetails.getPagenationLinkMobile( pageNum, 3, dsp1.getPageMax(), queryString, paramUidLink );
                dsp1.setPageLink( pageLinks );
            }

            // デバッグ環境かどうか
            if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
            {
                carrierUrl = "/_debug_" + carrierUrl;
            }
            /**
             * 本番用
             */
            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + carrierUrl + "?" + paramUidLink );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionReserveListMobile.execute() ][userlId = "
                    + userid + ",mode = " + mode + "] Exception", exception );
            try
            {
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "err", Message.getMessage( "erro.30005" ) );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionReserveListMobile.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
    }

}
