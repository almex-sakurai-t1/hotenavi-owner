package jp.happyhotel.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.LoginCheckAndroid;
import jp.happyhotel.common.LoginCheckIos;
import jp.happyhotel.common.UserAgent;

public class ActionApiLoginCheck extends BaseAction
{

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        Logging.info( "ActionApiLoginCheck.execute start" );

        String userAgent = UserAgent.getUserAgentTypeString( request );

        Logging.info( "User-Agent=" + UserAgent.getUserAgent( request ) );

        if ( userAgent.equals( "ipa" ) != false )
        {
            new LoginCheckIos().execute( request, response );
        }

        else
        {
            new LoginCheckAndroid().execute( request, response );
        }

        Logging.info( "ActionApiLoginCheck.execute end" );
    }
}
