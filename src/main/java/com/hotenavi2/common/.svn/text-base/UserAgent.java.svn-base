/*
 * @(#)UserAgent.java 2.01 2004/04/02
 * Copyright (C) ALMEX Inc. 2004
 * HTTP-USER-AGENT関連クラス
 */

package com.hotenavi2.common;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * USER-AGENTの取得、携帯製造番号等の取得を行うクラス。
 * 
 * @author S.Shiiya
 * @version 2.01 2004/04/02
 */
public class UserAgent implements Serializable
{
    /** 端末種別：DoCoMo **/
    public static final int USERAGENT_DOCOMO     = 1;
    /** 端末種別：J-PHONE,Vodafone **/
    public static final int USERAGENT_JPHONE     = 2;
    /** 端末種別：J-PHONE,Vodafone **/
    public static final int USERAGENT_VODAFONE   = 2;
    /** 端末種別：J-PHONE,Vodafone,SoftBank **/
    public static final int USERAGENT_SOFTBANK   = 2;
    /** 端末種別：au **/
    public static final int USERAGENT_AU         = 3;
    /** 端末種別：pc **/
    public static final int USERAGENT_PC         = 4;
    /** 端末種別：SmartPhone **/
    public static final int USERAGENT_SMARTPHONE = 5;

    /**
     * UserAgentを初期化します。
     */
    public UserAgent()
    {
    }

    /**
     * ユーザエージェント取得処理
     * 
     * @param requst Httpリクエスト
     * @return ユーザエージェント
     */
    public String getUserAgent(HttpServletRequest request)
    {
        return(request.getHeader( "User-Agent" ));
    }

    /**
     * ユーザエージェントタイプ取得処理
     * 
     * @param requst Httpリクエスト
     * @return ユーザエージェントタイプ
     */
    public int getUserAgentType(HttpServletRequest request)
    {
        String agent;

        agent = request.getHeader( "User-Agent" );
        if ( agent.startsWith( "DoCoMo" ) != false )
        {
            return(USERAGENT_DOCOMO);
        }
        if ( agent.startsWith( "J-PHONE" ) != false || agent.startsWith( "Vodafone" ) != false || agent.startsWith( "SoftBank" ) != false )
        {
            return(USERAGENT_JPHONE);
        }
        if ( agent.startsWith( "KDDI-" ) != false || agent.startsWith( "UP.Browser" ) != false )
        {
            return(USERAGENT_AU);
        }
        if ( agent.indexOf( "iPhone" ) != -1 || agent.indexOf( "Android" ) != -1 )
        {
            return(USERAGENT_SMARTPHONE);
        }

        return(USERAGENT_PC);
    }

    /**
     * ユーザエージェントタイプ取得処理（ディレクトリ）
     * 
     * @param requst Httpリクエスト
     * @return ユーザエージェントタイプ
     *         ("i"：DoCoMo)
     *         ("j"：J-PHONE)
     *         ("ez"：EzWeb)
     *         ("sp"：スマートフォン)
     *         ("pc"：PCその他)
     */
    public String getUserAgentTypeString(HttpServletRequest request)
    {
        String agent;

        agent = request.getHeader( "User-Agent" );
        if ( agent.startsWith( "DoCoMo" ) != false )
        {
            return("i");
        }
        if ( agent.startsWith( "J-PHONE" ) != false || agent.startsWith( "Vodafone" ) != false || agent.startsWith( "SoftBank" ) != false )
        {
            return("j");
        }
        if ( agent.startsWith( "KDDI-" ) != false || agent.startsWith( "UP.Browser" ) != false )
        {
            return("ez");
        }
        if ( agent.indexOf( "iPhone" ) != -1 || agent.indexOf( "Android" ) != -1 )
        {
            return("sp");
        }

        return("pc");
    }

    /**
     * 製造番号取得処理
     * 
     * @param requst Httpリクエスト
     * @return 製造番号
     */
    public String getSerialNo(HttpServletRequest request)
    {
        int i;
        int adrs;
        String agent;
        String productno;
        StringBuffer before;
        StringBuffer after;

        productno = "";
        after = new StringBuffer();

        agent = request.getHeader( "User-Agent" );
        if ( agent.startsWith( "DoCoMo" ) != false )
        {
            // serの文字を見つける
            adrs = agent.indexOf( "ser" );
            if ( adrs != 0 )
            {
                before = new StringBuffer( agent );

                // 文字の最後または、セミコロンまたはスラッシュが出るまでコピーする
                for( i = adrs + 3 ; i < agent.length() ; i++ )
                {
                    if ( before.charAt( i ) == ';' || before.charAt( i ) == '/' )
                    {
                        break;
                    }
                    after.append( before.charAt( i ) );
                }
            }

            productno = after.toString();
        }
        if ( agent.startsWith( "J-PHONE" ) != false || agent.startsWith( "Vodafone" ) != false || agent.startsWith( "SoftBank" ) != false )
        {
            // /SNの次から次のスペースまで
            adrs = agent.indexOf( "/SN" );
            if ( adrs != 0 )
            {
                before = new StringBuffer( agent );

                // 文字の最後または、セミコロンが出るまでコピーする
                for( i = adrs + 3 ; i < agent.length() ; i++ )
                {
                    if ( before.charAt( i ) == ' ' )
                    {
                        break;
                    }
                    after.append( before.charAt( i ) );
                }
            }

            productno = after.toString();
        }
        if ( agent.startsWith( "KDDI-" ) != false || agent.startsWith( "UP.Browser" ) != false )
        {
            productno = request.getHeader( "x-up-subno" );
        }

        return(productno);
    }
}
