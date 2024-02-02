/*
 * @(#)FortunatePay 1.00 2009/08/13
 * Copyright (C) ALMEX Inc. 2009
 * �L���肢���擾�N���X
 */

package jp.happyhotel.others;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataFortunatePay;
import jp.happyhotel.data.DataMasterFortune;

/**
 * �L���肢���擾�N���X
 * �L���̐肢���������N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2009/08/13
 */
public class FortunatePay implements Serializable
{
    private static final long serialVersionUID   = -4305973605981647598L;

    private int               masterCount;
    private DataFortunatePay  m_FortunatePay;
    private String            m_Fortune;                                 // ������
    private String            m_FortuneFull;                             // ������(�Y�����t����)
    private String            m_FortuneItem;                             // ���b�L�[
    private String            m_FortuneHotel;                            // ���b�L�[�z�e��
    private String            m_FortuneItemUrl;                          // ���b�L�[�A�C�e��URL
    private String            m_FortuneHotelUrl;                         // ���b�L�[�z�e��URL
    private final int         Fortune            = 0;
    private final int         FortuneItem        = 1;
    private final int         FortuneHotel       = 2;
    /** �[����ʁFDoCoMo **/
    public static final int   USERAGENT_DOCOMO   = 1;
    /** �[����ʁFau **/
    public static final int   USERAGENT_AU       = 2;
    /** �[����ʁFJ-PHONE,Vodafone **/
    public static final int   USERAGENT_JPHONE   = 3;
    /** �[����ʁFJ-PHONE,Vodafone **/
    public static final int   USERAGENT_VODAFONE = 3;
    /** �[����ʁFJ-PHONE,Vodafone,SoftBank **/
    public static final int   USERAGENT_SOFTBANK = 3;
    /** �[����ʁFpc **/
    public static final int   USERAGENT_PC       = 4;

    /**
     * �f�[�^�����������܂��B
     */
    public FortunatePay()
    {
        masterCount = 0;
        m_Fortune = "";
        m_FortuneFull = "";
        m_FortuneItem = "";
        m_FortuneHotel = "";
        m_FortuneItemUrl = "";
        m_FortuneHotelUrl = "";
    }

    /** �肢�����N��񌏐��擾 **/
    public int getCount()
    {
        return(masterCount);
    }

    /** �肢�����N���擾 **/
    public DataFortunatePay getFortunatePayInfo()
    {
        return(m_FortunatePay);
    }

    /** �肢�}�X�^���擾�i�����j **/
    public String getMasterFortune()
    {
        return(m_Fortune);
    }

    /** �肢�}�X�^���擾�i�����@�Y�����t���݁j **/
    public String getMasterFortuneFull()
    {
        return(m_FortuneFull);
    }

    /** �肢�}�X�^���擾�i���b�L�[�A�C�e���j **/
    public String getMasterFortuneItem()
    {
        return(m_FortuneItem);
    }

    /** �肢�}�X�^���擾�i���b�L�[�A�C�e���j **/
    public String getMasterFortuneItemUrl()
    {
        return(m_FortuneItemUrl);
    }

    /** �肢�}�X�^���擾�i���b�L�[�z�e���j **/
    public String getMasterFortuneHotel()
    {
        return(m_FortuneHotel);
    }

    /** �肢�}�X�^���擾�i���b�L�[�z�e���j **/
    public String getMasterFortuneHotelUrl()
    {
        return(m_FortuneHotelUrl);
    }

    /**
     * �肢�f�[�^���擾����
     * 
     * @param today �����̓��t
     * @param constellation ����
     * @param userAgentType ���[�U�[�G�[�W�F���g�^�C�v
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getFortunatePay(int today, int constellation, int userAgentType)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        DataMasterFortune dmf;

        if ( today <= 0 || constellation <= 0 )
        {
            return(false);
        }

        query = "SELECT * FROM hh_fortunate_pay";
        query = query + " WHERE date = ?";
        query = query + " AND constellation = ?";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, today );
            prestate.setInt( 2, constellation );
            ret = geFortuneSub( prestate );
            if ( ret != false )
            {
                dmf = new DataMasterFortune();
                ret = dmf.getData( Fortune, this.m_FortunatePay.getConstellation() );
                if ( ret != false )
                {
                    // �Y�����t���݂̐������ƁA���������擾����
                    this.m_FortuneFull = dmf.getName();
                    this.m_Fortune = m_FortuneFull.substring( 0, m_FortuneFull.indexOf( '(' ) );
                }
                ret = dmf.getData( FortuneItem, this.m_FortunatePay.getLuckyItem() );
                if ( ret != false )
                {
                    this.m_FortuneItem = dmf.getName();
                    if ( userAgentType == USERAGENT_DOCOMO )
                    {
                        this.m_FortuneItemUrl = dmf.getUrlDocomo();
                    }
                    else if ( userAgentType == USERAGENT_AU )
                    {
                        this.m_FortuneItemUrl = dmf.getUrlAu();
                    }
                    else if ( userAgentType == USERAGENT_SOFTBANK )
                    {
                        this.m_FortuneItemUrl = dmf.getUrlSoftBank();
                    }
                    else
                    {
                        this.m_FortuneItemUrl = dmf.getUrl();
                    }
                }
                ret = dmf.getData( FortuneHotel, this.m_FortunatePay.getLuckyHotel() );
                if ( ret != false )
                {
                    this.m_FortuneHotel = dmf.getName();
                    if ( userAgentType == USERAGENT_DOCOMO )
                    {
                        this.m_FortuneHotelUrl = dmf.getUrlDocomo();
                    }
                    else if ( userAgentType == USERAGENT_AU )
                    {
                        this.m_FortuneHotelUrl = dmf.getUrlAu();
                    }
                    else if ( userAgentType == USERAGENT_SOFTBANK )
                    {
                        this.m_FortuneHotelUrl = dmf.getUrlSoftBank();
                    }
                    else
                    {
                        this.m_FortuneHotelUrl = dmf.getUrl();
                    }
                }

            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getFortunatePay] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �v���[���g���̃f�[�^���Z�b�g
     * 
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean geFortuneSub(PreparedStatement prestate)
    {
        ResultSet result = null;

        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    masterCount = result.getRow();
                }
                this.m_FortunatePay = new DataFortunatePay();

                result.beforeFirst();
                if ( result.next() != false )
                {
                    // �肢�����L���O���̐ݒ�
                    this.m_FortunatePay.setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[geFortuneSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( masterCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

}
