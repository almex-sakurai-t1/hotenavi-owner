/*
 * @(#)UserAgent.java 2.01 2004/04/02
 * Copyright (C) ALMEX Inc. 2004
 * HTTP-USER-AGENT�֘A�N���X
 */

package com.hotenavi2.common;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * USER-AGENT�̎擾�A�g�ѐ����ԍ����̎擾���s���N���X�B
 * 
 * @author S.Shiiya
 * @version 2.01 2004/04/02
 */
public class UserAgent implements Serializable
{
    /** �[����ʁFDoCoMo **/
    public static final int USERAGENT_DOCOMO     = 1;
    /** �[����ʁFJ-PHONE,Vodafone **/
    public static final int USERAGENT_JPHONE     = 2;
    /** �[����ʁFJ-PHONE,Vodafone **/
    public static final int USERAGENT_VODAFONE   = 2;
    /** �[����ʁFJ-PHONE,Vodafone,SoftBank **/
    public static final int USERAGENT_SOFTBANK   = 2;
    /** �[����ʁFau **/
    public static final int USERAGENT_AU         = 3;
    /** �[����ʁFpc **/
    public static final int USERAGENT_PC         = 4;
    /** �[����ʁFSmartPhone **/
    public static final int USERAGENT_SMARTPHONE = 5;

    /**
     * UserAgent�����������܂��B
     */
    public UserAgent()
    {
    }

    /**
     * ���[�U�G�[�W�F���g�擾����
     * 
     * @param requst Http���N�G�X�g
     * @return ���[�U�G�[�W�F���g
     */
    public String getUserAgent(HttpServletRequest request)
    {
        return(request.getHeader( "User-Agent" ));
    }

    /**
     * ���[�U�G�[�W�F���g�^�C�v�擾����
     * 
     * @param requst Http���N�G�X�g
     * @return ���[�U�G�[�W�F���g�^�C�v
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
     * ���[�U�G�[�W�F���g�^�C�v�擾�����i�f�B���N�g���j
     * 
     * @param requst Http���N�G�X�g
     * @return ���[�U�G�[�W�F���g�^�C�v
     *         ("i"�FDoCoMo)
     *         ("j"�FJ-PHONE)
     *         ("ez"�FEzWeb)
     *         ("sp"�F�X�}�[�g�t�H��)
     *         ("pc"�FPC���̑�)
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
     * �����ԍ��擾����
     * 
     * @param requst Http���N�G�X�g
     * @return �����ԍ�
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
            // ser�̕�����������
            adrs = agent.indexOf( "ser" );
            if ( adrs != 0 )
            {
                before = new StringBuffer( agent );

                // �����̍Ō�܂��́A�Z�~�R�����܂��̓X���b�V�����o��܂ŃR�s�[����
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
            // /SN�̎����玟�̃X�y�[�X�܂�
            adrs = agent.indexOf( "/SN" );
            if ( adrs != 0 )
            {
                before = new StringBuffer( agent );

                // �����̍Ō�܂��́A�Z�~�R�������o��܂ŃR�s�[����
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
