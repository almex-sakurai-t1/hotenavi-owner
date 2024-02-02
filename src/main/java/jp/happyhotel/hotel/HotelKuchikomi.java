/*
 * @(#)HotelKuchikomi.java 1.00
 * 2011/05/01 Copyright (C) ALMEX Inc. 2011
 * ホテルクチコミクラス
 */
package jp.happyhotel.hotel;

import java.io.Serializable;

import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReplaceString;

/**
 * ホテルクチコミクラス
 * 
 * @author S.Tashiro
 * @version 1.00 2011/01/24
 */
public class HotelKuchikomi implements Serializable
{
    public final int          BBS_MAX_DISP     = 5;
    public final int          GET_BBS_FLAG     = 1;
    public final String       DOCOMO           = "DoCoMo";
    public final String       AU               = "KDDI-";
    public final String       AU_2             = "UP.Browser";
    public final String       SOFTBANK         = "SoftBank";
    public final String       VODAFONE         = "Vodafone";
    public final String       J_PHONE          = "J-PHONE";
    public final String       SEMULATOR        = "Semulator";

    /**
     *
     */
    private static final long serialVersionUID = 130043483767139695L;
    private int               count;
    private float             average;
    private String[]          postDate;
    private String[]          browser;
    private String[]          name;
    private int[]             sex;
    private int[]             cleanness;
    private int[]             width;
    private int[]             equip;
    private int[]             service;
    private int[]             cost;
    private int[]             point;
    private String[]          message;
    private int[]             voteCount;
    private int[]             voteYes;
    private String[]          replyName;
    private String[]          replyPostDate;
    private String[]          replyMessage;
    private int               allCount;

    /**
     * データを初期化します。
     */
    public HotelKuchikomi()
    {
        count = 0;
        average = 0;
        allCount = 0;
    }

    public int getCount()
    {
        return count;
    }

    public float getAverage()
    {
        return average;
    }

    public String[] getPostDate()
    {
        return postDate;
    }

    public String[] getBrowser()
    {
        return browser;
    }

    public String[] getName()
    {
        return name;
    }

    public int[] getSex()
    {
        return sex;
    }

    public int[] getCleanness()
    {
        return cleanness;
    }

    public int[] getWidth()
    {
        return width;
    }

    public int[] getEquip()
    {
        return equip;
    }

    public int[] getService()
    {
        return service;
    }

    public int[] getCost()
    {
        return cost;
    }

    public int[] getPoint()
    {
        return point;
    }

    public String[] getMessage()
    {
        return message;
    }

    public int[] getVoteCount()
    {
        return voteCount;
    }

    public int[] getVoteYes()
    {
        return voteYes;
    }

    public String[] getReplyName()
    {
        return replyName;
    }

    public String[] getReplyPostDate()
    {
        return replyPostDate;
    }

    public String[] getReplyMessage()
    {
        return replyMessage;
    }

    public int getAllCount()
    {
        return allCount;
    }

    /**
     * ホテル詳細データ取得
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int hotelId, int page)
    {
        boolean ret = false;
        HotelBasicInfo hotelBasic;
        HotelBbs bbs;

        hotelBasic = new HotelBasicInfo();
        bbs = new HotelBbs();

        ret = hotelBasic.getHotelBasicInfo( hotelId );
        hotelBasic.getHotelMaster( hotelId );

        if ( hotelBasic.getHotelInfo().getRank() >= 2 && hotelBasic.getHotelMaster().getBbsConfig() == 1 )
        {
            bbs.getBbsList( hotelId, BBS_MAX_DISP, page, GET_BBS_FLAG );
            this.count = bbs.getBbsCount();
            this.allCount = bbs.getBbsAllCount();
            if ( this.allCount > 0 )
            {
                this.average = (float)bbs.getPointAverage( hotelId );
                this.average = this.average / 100;
                this.setBbsMessage( bbs );
            }
        }
        else
        {
            ret = false;
        }
        return ret;
    }

    /**
     * ホテルクチコミメッセージ
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public void setBbsMessage(HotelBbs bbs)
    {
        int i;
        int contributeDate;
        int returnDate;

        try
        {
            if ( bbs != null )
            {
                if ( bbs.getBbsCount() > 0 )
                {
                    this.postDate = new String[bbs.getBbsCount()];
                    this.browser = new String[bbs.getBbsCount()];
                    this.name = new String[bbs.getBbsCount()];
                    this.sex = new int[bbs.getBbsCount()];
                    this.cleanness = new int[bbs.getBbsCount()];
                    this.width = new int[bbs.getBbsCount()];
                    this.equip = new int[bbs.getBbsCount()];
                    this.service = new int[bbs.getBbsCount()];
                    this.cost = new int[bbs.getBbsCount()];
                    this.point = new int[bbs.getBbsCount()];
                    this.message = new String[bbs.getBbsCount()];
                    this.voteCount = new int[bbs.getBbsCount()];
                    this.voteYes = new int[bbs.getBbsCount()];
                    this.replyName = new String[bbs.getBbsCount()];
                    this.replyPostDate = new String[bbs.getBbsCount()];
                    this.replyMessage = new String[bbs.getBbsCount()];

                    for( i = 0 ; i < bbs.getBbsCount() ; i++ )
                    {
                        // 初期化
                        this.postDate[i] = new String();
                        this.browser[i] = new String();
                        this.name[i] = new String();
                        this.message[i] = new String();
                        this.replyName[i] = new String();
                        this.replyPostDate[i] = new String();
                        this.replyMessage[i] = new String();

                        contributeDate = bbs.getHotelBbs()[i].getContributeDate();
                        this.postDate[i] = Integer.toString( contributeDate / 10000 ) + "/" + String.format( "%1$02d", contributeDate / 100 % 100 ) + "/" + String.format( "%1$02d", contributeDate % 100 % 100 );
                        this.browser[i] = this.setBrowserMessage( bbs.getHotelBbs()[i].getContributeUserAgent() );
                        this.sex[i] = this.setSexMessage( bbs, bbs.getHotelBbs()[i].getUserId() );
                        this.name[i] = bbs.getHotelBbs()[i].getUserName();
                        this.cleanness[i] = bbs.getHotelBbs()[i].getCleannessPoint();
                        this.width[i] = bbs.getHotelBbs()[i].getWidthPoint();
                        this.equip[i] = bbs.getHotelBbs()[i].getEquipPoint();
                        this.service[i] = bbs.getHotelBbs()[i].getServicePoint();
                        this.cost[i] = bbs.getHotelBbs()[i].getCostPoint();
                        this.point[i] = bbs.getHotelBbs()[i].getPoint();
                        this.message[i] = ReplaceString.HTMLEscape( bbs.getHotelBbs()[i].getMessage() );
                        this.voteCount[i] = bbs.getHotelBbs()[i].getVoteCount();
                        this.voteYes[i] = bbs.getHotelBbs()[i].getVoteYes();

                        // 返信
                        returnDate = bbs.getHotelBbs()[i].getReturnDate();
                        this.replyPostDate[i] = Integer.toString( returnDate / 10000 ) + "/" + String.format( "%1$02d", returnDate / 100 % 100 ) + "/" + String.format( "%1$02d", returnDate % 100 % 100 );
                        this.replyName[i] = bbs.getHotelBbs()[i].getReturnOwnerName();
                        this.replyMessage[i] = ReplaceString.HTMLEscape( bbs.getHotelBbs()[i].getReturnMessage() );
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelKuchikomi.setBbsMessage]Exception" + e.toString() );
        }
    }

    /**
     * ユーザエージェント判断
     * 
     * @param userAgent ユーザエージェント
     * @return 整数値（PC:1、携帯:2）
     */
    private String setBrowserMessage(String userAgent)
    {
        String carrier = "";
        if ( userAgent.startsWith( DOCOMO ) != false )
        {
            carrier = "2";
        }
        else if ( userAgent.startsWith( AU ) || userAgent.startsWith( AU_2 ) )
        {
            carrier = "2";
        }
        else if ( userAgent.startsWith( "J-PHONE" ) || userAgent.startsWith( "Vodafone" ) || userAgent.startsWith( "SoftBank" ) || userAgent.startsWith( "Semulator" ) )
        {
            carrier = "2";
        }
        // else if ( userAgent.indexOf( "iPhone" ) != -1 || userAgent.indexOf( "Android" ) != -1 )
        // {
        // carrier = "3";
        // }
        else
        {
            carrier = "1";
        }

        return carrier;
    }

    /***
     * 性別判断
     * 
     * @param bbs
     * @param userId
     * @return 0:男、1:女、2:未登録
     */
    private int setSexMessage(HotelBbs bbs, String userId)
    {
        int nSex = 0;
        boolean ret;

        ret = bbs.getUserData( userId );
        if ( ret != false )
        {
            if ( bbs.getUserBasic().getSex() == 0 )
            {
                nSex = 0;
            }
            else if ( bbs.getUserBasic().getSex() == 1 )
            {
                nSex = 1;
            }
        }
        else
        {
            nSex = 2;
        }
        return nSex;
    }
}
