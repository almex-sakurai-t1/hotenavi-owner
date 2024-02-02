/*
 * @(#)SponsorData.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 �X�|���T�[�f�[�^�擾�N���X
 */
package jp.happyhotel.sponsor;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataMasterLocal;
import jp.happyhotel.data.DataMasterPref;
import jp.happyhotel.data.DataMasterSponsor;
import jp.happyhotel.data.DataSponsorData;
import jp.happyhotel.search.SearchEngineBasic;

/**
 * �X�|���T�[�f�[�^�擾�N���X
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.00 2007/12/03
 */
public class SponsorData_M2 implements Serializable
{
    /**
     *
     */
    private static final long   serialVersionUID     = 49553862735802305L;

    private int                 sponsorCount;
    private boolean             sponsorDataStatus;
    private DataMasterSponsor[] sponsor;
    /* ���T�p�ϐ� */
    private int                 hotelCount;
    private int                 hotelAllCount;
    private int                 localCount;
    private int[]               prefCount;
    private DataMasterLocal     dmLocal;
    private DataMasterPref[]    dmPref;
    private DataHotelBasic[]    m_hotelBasic;

    /** �[����ʁFDoCoMo **/
    public static final int     USERAGENT_DOCOMO     = 1;
    /** �[����ʁFau **/
    public static final int     USERAGENT_AU         = 2;
    /** �[����ʁFJ-PHONE,Vodafone **/
    public static final int     USERAGENT_JPHONE     = 3;
    /** �[����ʁFJ-PHONE,Vodafone **/
    public static final int     USERAGENT_VODAFONE   = 3;
    /** �[����ʁFJ-PHONE,Vodafone,SoftBank **/
    public static final int     USERAGENT_SOFTBANK   = 3;
    /** �[����ʁFpc **/
    public static final int     USERAGENT_PC         = 4;
    /** �[����ʁFpc **/
    public static final int     USERAGENT_SMARTPHONE = 5;

    public SponsorData_M2()
    {
        sponsorCount = 0;
        hotelCount = 0;
        hotelAllCount = 0;
        localCount = 0;
    }

    public DataMasterSponsor[] getSponsor()
    {
        return sponsor;
    }

    public int getSponsorCount()
    {
        return sponsorCount;
    }

    public boolean isSponsorDataStatus()
    {
        return sponsorDataStatus;
    }

    public void setSponsorDataStatus(boolean sponsorDataStatus)
    {
        this.sponsorDataStatus = sponsorDataStatus;
    }

    /* �������牺�͓��T�p�̃��\�b�h */
    /** �z�e�������擾�i1�y�[�W�̌����j **/
    public int getPrivilegeHotelCount()
    {
        return hotelCount;
    }

    /** �z�e�������擾 **/
    public int getPrivilegeHotelAllCount()
    {
        return hotelAllCount;
    }

    /** �z�e�����擾 **/
    public DataHotelBasic[] getPrivilegeHotelInfo()
    {
        return(m_hotelBasic);
    }

    /** �n�������擾 **/
    public int getPrivilegeLocalCount()
    {
        return localCount;
    }

    /** �s���{�������擾 **/
    public int[] getPrivilegePrefCount()
    {
        return prefCount;
    }

    /** �n���}�X�^�擾 **/
    public DataMasterLocal getPrivilegeLocal()
    {
        return dmLocal;
    }

    /** �s���{���}�X�^�擾 **/
    public DataMasterPref[] getPrivilegePref()
    {
        return dmPref;
    }

    /**
     * �X�|���T�[���擾�i�s���{��ID�j
     * 
     * @param prefId �s���{��ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     * @throws Exception
     */
    public boolean getSponsorByPref(int prefId) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // �\���Ώۂ̂���
        query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor,hh_master_pref"
                + " WHERE hh_master_sponsor.sponsor_code <> 0"
                + " AND hh_master_sponsor.area_code <> 0"
                + " AND hh_master_pref.pref_id = ?"
                + " AND hh_master_pref.sponsor_area_code = hh_master_sponsor.area_code"
                + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)"
                + " AND hh_master_sponsor.random_disp_flag = 0"
                + " ORDER BY hh_master_sponsor.disp_pos";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, prefId );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            // Gets Sponsor Data
            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData.getSponsorByPref( int prefId = " + prefId + " )] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ���[�e�[�V�����o�i�[�p�X�|���T�[���擾�i�s���{��ID�j
     * 
     * @param prefId �s���{��ID
     * @param dispCount �擾����
     * @param mobileFlag �g�уt���O(true:�g��,false�FPC)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     * @throws Exception
     */
    public boolean getRandomSponsorByPref(int prefId, int dispCount, boolean mobileFlag) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // �\���Ώۂ̂���
        query = "SELECT sponsor.* FROM hh_master_sponsor sponsor"
                + " INNER JOIN hh_master_pref pref ON pref.sponsor_area_code = sponsor.area_code"
                + " AND pref.pref_id = ?"
                + " WHERE sponsor.sponsor_code <> 0"
                + " AND sponsor.area_code <> 0"
                + " AND sponsor.start_date <= ? AND sponsor.end_date >= ?"
                + " AND sponsor.random_disp_flag = 2"
                + " ORDER BY RAND() LIMIT 0,?";

        /*
         * // �\���Ώۂ̂���
         * query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor,hh_master_pref, hh_sponsor_data"
         * + " WHERE hh_master_sponsor.sponsor_code <> 0"
         * + " AND hh_master_sponsor.area_code <> 0"
         * + " AND hh_master_sponsor.sponsor_code = hh_sponsor_data.sponsor_code"
         * + " AND hh_master_pref.pref_id = ?"
         * + " AND hh_master_pref.sponsor_area_code = hh_master_sponsor.area_code"
         * + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)"
         * + " AND hh_sponsor_data.addup_date = ?"
         * + " AND hh_master_sponsor.random_disp_flag = 2";
         * // �\���敪�ɂ��
         * if ( mobileFlag != false )
         * {
         * query = query + " ORDER BY hh_sponsor_data.impression_mobile LIMIT 0,?";
         * }
         * else
         * {
         * query = query + " ORDER BY hh_sponsor_data.impression LIMIT 0,?";
         * }
         */
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, prefId );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 4, dispCount );

            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData.getSponsorByPref( int prefId = " + prefId + " )] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ���[�e�[�V�����o�i�[�p�X�|���T�[���擾�i�s���{��ID�j
     * 
     * @param prefId �s���{��ID
     * @param dispCount �擾����
     * @param dispFlag �g�уt���O(0:PC,1:�g��,2�F�X�}�[�g�t�H��)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     * @throws Exception
     */
    public boolean getRandomSponsorByPref(int prefId, int dispCount, int dispFlag) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // �\���Ώۂ̂���
        query = "SELECT sponsor.* FROM hh_master_sponsor sponsor"
                + " INNER JOIN hh_master_pref pref ON pref.sponsor_area_code = sponsor.area_code"
                + " AND pref.pref_id = ?"
                + " WHERE sponsor.sponsor_code <> 0"
                + " AND sponsor.area_code <> 0"
                + " AND sponsor.start_date <= ? AND sponsor.end_date >= ?"
                + " AND sponsor.random_disp_flag = 2"
                + " ORDER BY RAND() LIMIT 0,?";
        /*
         * query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor,hh_master_pref, hh_sponsor_data"
         * + " WHERE hh_master_sponsor.sponsor_code <> 0"
         * + " AND hh_master_sponsor.area_code <> 0"
         * + " AND hh_master_sponsor.sponsor_code = hh_sponsor_data.sponsor_code"
         * + " AND hh_master_pref.pref_id = ?"
         * + " AND hh_master_pref.sponsor_area_code = hh_master_sponsor.area_code"
         * + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)"
         * + " AND hh_sponsor_data.addup_date = ?"
         * + " AND hh_master_sponsor.random_disp_flag = 2";
         * // �\���敪�ɂ��
         * if ( dispFlag == 0 )
         * {
         * query = query + " ORDER BY hh_sponsor_data.impression_mobile LIMIT 0,?";
         * }
         * else if ( dispFlag == 1 )
         * {
         * query = query + " ORDER BY hh_sponsor_data.impression LIMIT 0,?";
         * }
         * else if ( dispFlag == 2 )
         * {
         * query = query + " ORDER BY hh_sponsor_data.impression_smart LIMIT 0,?";
         * }
         */
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, prefId );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 4, dispCount );

            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData_M2.getSponsorByPref( int prefId = " + prefId + " )] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �X�|���T�[���擾�i�s�撬���R�[�h�j
     * 
     * @param jisCode �s�撬���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     * @throws Exception
     */
    public boolean getSponsorByCity(int jisCode) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // �\���Ώۂ̂���
        query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor,hh_master_city"
                + " WHERE hh_master_sponsor.sponsor_code <> 0"
                + " AND hh_master_sponsor.area_code <> 0"
                + " AND hh_master_city.jis_code = ?"
                + " AND hh_master_city.sponsor_area_code = hh_master_sponsor.area_code"
                + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)"
                + " AND hh_master_sponsor.random_disp_flag = 0"
                + " ORDER BY hh_master_sponsor.disp_pos";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, jisCode );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            // Fetches Sponsor Data
            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData_M2.getSponsorByCity( int jisCode = " + jisCode + " )] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �X�|���T�[���擾�i�z�e���X�R�[�h�j
     * 
     * @param areaId �G���AID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     * @throws Exception
     */
    public boolean getSponsorByArea(int areaId) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // �\���Ώۂ̂���
        query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor,hh_master_area"
                + " WHERE hh_master_sponsor.sponsor_code <> 0"
                + " AND hh_master_sponsor.area_code <> 0"
                + " AND hh_master_area.area_id = ?"
                + " AND hh_master_area.sponsor_area_code = hh_master_sponsor.area_code"
                + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)"
                + " AND hh_master_sponsor.random_disp_flag = 0"
                + " ORDER BY hh_master_sponsor.disp_pos";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, areaId );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData.getSponsorByArea] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �X�|���T�[���擾�i�\���ʒu�w��j
     * 
     * @param dispPos �\���ʒu�敪
     * @return ��������(TRUE:����,FALSE:�ُ�)
     * @throws Exception
     */
    public boolean getSponsorByDispPos(String dispPos) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // �\���Ώۂ̂���
        query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor"
                + " WHERE hh_master_sponsor.sponsor_code <> 0"
                + " AND hh_master_sponsor.area_code <> 0"
                + " AND hh_master_sponsor.disp_pos = ?"
                + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)"
                + " AND hh_master_sponsor.random_disp_flag = 0"
                + " ORDER BY hh_master_sponsor.disp_pos";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, dispPos );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData.getSponsorByDispPos] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �X�|���T�[�����Z�b�g
     * 
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @return ��������(TRUE:����,FALSE:�ُ�)
     * @throws Exception
     */
    private boolean getSponsorDataSub(PreparedStatement prestate) throws Exception
    {
        int i;
        int count = 0;
        boolean ret = false;
        ResultSet result = null;

        this.sponsorCount = 0;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    this.sponsorCount = result.getRow();
                }

                // �N���X�̔z���p�ӂ��A����������B
                this.sponsor = new DataMasterSponsor[this.sponsorCount];
                for( i = 0 ; i < sponsorCount ; i++ )
                {
                    sponsor[i] = new DataMasterSponsor();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // �X�|���T�[���̐ݒ�
                    this.sponsor[count].setData( result );

                    count++;
                }
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[SponsorData_M2.getSponsorDataSub(prestate)] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result );
        }
        if ( sponsorCount > 0 )
            ret = true;
        else
            ret = false;

        return(ret);
    }

    /**
     * �C���v���b�V�����J�E���g
     * 
     * @param sponsorCode �X�|���T�[�R�[�h
     * @param mobileFlag �g�уt���O�itrue:�g�сj
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setImpressionCount(int sponsorCode, boolean mobileFlag)
    {
        boolean ret;
        DataSponsorData dsd;

        ret = false;

        // �ΏۍL���̃f�[�^���擾����
        dsd = new DataSponsorData();
        ret = dsd.getData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        if ( ret != false )
        {
            if ( mobileFlag != false )
            {
                dsd.setImpressionMobile( dsd.getImpressionMobile() + 1 );
            }
            else
            {
                dsd.setImpression( dsd.getImpression() + 1 );
            }

            ret = dsd.updateData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            // ���s�����烍�O�Ɏc��
            if ( ret == false )
            {
                Logging.error( "[SponsorData_M2.setImpressionCount updateData] Exception sponsorCode:" + sponsorCode
                        + ", Impression:" + Integer.toString( dsd.getImpression() + 1 ) );
            }
        }
        else
        {
            dsd.setSponsorCode( sponsorCode );
            dsd.setAddupDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dsd.setSponsorCode( sponsorCode );
            if ( mobileFlag != false )
            {
                dsd.setImpressionMobile( 1 );
            }
            else
            {
                dsd.setImpression( 1 );
            }

            ret = dsd.insertData();
            // ���s�����烍�O�Ɏc��
            if ( ret == false )
            {
                Logging.error( "[SponsorData_M2.setImpressionCount insertData] Exception sponsorCode:" + sponsorCode );

            }
        }

        return(ret);
    }

    /**
     * �N���b�N�J�E���g
     * 
     * @param sponsorCode �X�|���T�[�R�[�h
     * @param mobileFlag �g�уt���O�itrue:�g�сj
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setClickCount(int sponsorCode, boolean mobileFlag)
    {
        boolean ret;
        DataSponsorData dsd;

        ret = false;

        // �ΏۍL���̃f�[�^���擾����
        dsd = new DataSponsorData();
        ret = dsd.getData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        if ( ret != false )
        {
            if ( mobileFlag != false )
            {
                dsd.setClickCountMobile( dsd.getClickCountMobile() + 1 );
            }
            else
            {
                dsd.setClickCount( dsd.getClickCount() + 1 );
            }

            ret = dsd.updateData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        }
        else
        {
            dsd.setSponsorCode( sponsorCode );
            dsd.setAddupDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dsd.setSponsorCode( sponsorCode );
            if ( mobileFlag != false )
            {
                dsd.setClickCountMobile( 1 );
            }
            else
            {
                dsd.setClickCount( 1 );
            }

            ret = dsd.insertData();
        }

        return(ret);
    }

    /**
     * �g���N���b�N�J�E���g
     * 
     * @param sponsorCode �X�|���T�[�R�[�h
     * @param mobileFlag �g�уt���O�itrue:�g�сj
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setExClickCount(int sponsorCode, boolean mobileFlag)
    {
        boolean ret;
        DataSponsorData dsd;

        ret = false;

        // �ΏۍL���̃f�[�^���擾����
        dsd = new DataSponsorData();
        ret = dsd.getData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        if ( ret != false )
        {
            if ( mobileFlag != false )
            {
                dsd.setExClickCountMobile( dsd.getExClickCountMobile() + 1 );
            }
            else
            {
                dsd.setExClickCount( dsd.getExClickCount() + 1 );
            }

            ret = dsd.updateData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        }
        else
        {
            dsd.setSponsorCode( sponsorCode );
            dsd.setAddupDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dsd.setSponsorCode( sponsorCode );
            if ( mobileFlag != false )
            {
                dsd.setExClickCountMobile( 1 );
            }
            else
            {
                dsd.setExClickCount( 1 );
            }

            ret = dsd.insertData();
        }

        return(ret);
    }

    /**
     * �g���N���b�N�J�E���g�Q
     * 
     * @param sponsorCode �X�|���T�[�R�[�h
     * @param mobileFlag �g�уt���O�itrue:�g�сj
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setExClickCount2(int sponsorCode, boolean mobileFlag)
    {
        boolean ret;
        DataSponsorData dsd;

        ret = false;

        // �ΏۍL���̃f�[�^���擾����
        dsd = new DataSponsorData();
        ret = dsd.getData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        if ( ret != false )
        {
            if ( mobileFlag != false )
            {
                dsd.setExClickCountMobile2( dsd.getExClickCountMobile() + 1 );
            }
            else
            {
                dsd.setExClickCount2( dsd.getExClickCount() + 1 );
            }

            ret = dsd.updateData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        }
        else
        {
            dsd.setSponsorCode( sponsorCode );
            dsd.setAddupDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dsd.setSponsorCode( sponsorCode );
            if ( mobileFlag != false )
            {
                dsd.setExClickCountMobile2( 1 );
            }
            else
            {
                dsd.setExClickCount2( 1 );
            }

            ret = dsd.insertData();
        }

        return(ret);
    }

    /**
     * �g���C���v���b�V�����J�E���g
     * 
     * @param sponsorCode �X�|���T�[�R�[�h
     * @param mobileFlag �g�уt���O�itrue:�g�сj
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setExImpressionCount(int sponsorCode, boolean mobileFlag)
    {
        boolean ret;
        DataSponsorData dsd;

        ret = false;

        // �ΏۍL���̃f�[�^���擾����
        dsd = new DataSponsorData();
        ret = dsd.getData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        if ( ret != false )
        {
            if ( mobileFlag != false )
            {
                dsd.setExImpressionMobile( dsd.getExImpressionMobile() + 1 );
            }
            else
            {
                dsd.setExImpression( dsd.getExImpression() + 1 );
            }

            ret = dsd.updateData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        }
        else
        {
            dsd.setSponsorCode( sponsorCode );
            dsd.setAddupDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dsd.setSponsorCode( sponsorCode );
            if ( mobileFlag != false )
            {
                dsd.setExImpressionMobile( 1 );
            }
            else
            {
                dsd.setExImpression( 1 );
            }

            ret = dsd.insertData();
        }

        return(ret);
    }

    /**
     * �A�t�B���G�C�g���擾�i�����_���łP���j
     * 
     * @param userAgentType ���[�U�[�G�[�W�F���g�^�C�v
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getAffiliateRandom(int userAgentType)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int i;
        int datacount;
        int count;

        // �\�����Ă���L�����A�ɊY������url���o�^����Ă��郌�R�[�h�̒����烉���_���Ŏ擾����B
        query = "SELECT hhms.* FROM hh_master_sponsor AS hhms";
        query = query + " INNER JOIN (";
        query = query + " SELECT sponsor_code FROM hh_master_sponsor";
        query = query + " WHERE area_code = '0'";
        query = query + " AND disp_pos = '1'";
        query = query + " AND start_date <= ?";
        query = query + " AND end_date >= ?";

        if ( userAgentType == USERAGENT_DOCOMO )
        {
            query = query + " AND url_docomo != ''";
        }
        else if ( userAgentType == USERAGENT_AU )
        {
            query = query + " AND url_au != ''";
        }
        else if ( userAgentType == USERAGENT_SOFTBANK )
        {
            query = query + " AND url_softbank != ''";
        }
        query = query + " ORDER BY RAND()";
        query = query + " ) AS random";
        query = query + " ON hhms.sponsor_code = random.sponsor_code";
        query = query + " LIMIT 0,1";

        count = 0;
        datacount = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    datacount = result.getRow();
                }

                // �N���X�̔z���p�ӂ��A����������B
                this.sponsor = new DataMasterSponsor[datacount];
                for( i = 0 ; i < datacount ; i++ )
                {
                    sponsor[i] = new DataMasterSponsor();
                }
                result.beforeFirst();
                if ( result.next() != false )
                {
                    // �A�t�B���G�C�g���̎擾
                    this.sponsor[count].setData( result );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData.getAffiliaterandom] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( datacount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * �A�t�B���G�C�g���擾�i�����_���j
     * 
     * @param userAgentType ���[�U�[�G�[�W�F���g�^�C�v
     * @param dispCount �\����
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getAffiliateRandom(int userAgentType, int dispCount) throws Exception
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int i;
        int count;

        // �\�����Ă���L�����A�ɊY������url���o�^����Ă��郌�R�[�h�̒����烉���_���Ŏ擾����B
        query = "SELECT hhms.* FROM hh_master_sponsor AS hhms";
        query = query + " INNER JOIN (";
        query = query + " SELECT sponsor_code FROM hh_master_sponsor";
        query = query + " WHERE area_code = '0'";
        query = query + " AND disp_pos = '1'";
        query = query + " AND start_date <= ?";
        query = query + " AND end_date >= ?";

        if ( userAgentType == USERAGENT_DOCOMO )
        {
            query = query + " AND url_docomo != ''";
        }
        else if ( userAgentType == USERAGENT_AU )
        {
            query = query + " AND url_au != ''";
        }
        else if ( userAgentType == USERAGENT_SOFTBANK )
        {
            query = query + " AND url_softbank != ''";
        }
        else
        {
            query = query + " AND url != '' ";
        }
        query = query + " ORDER BY RAND()";
        query = query + " ) AS random";
        query = query + " ON hhms.sponsor_code = random.sponsor_code";
        query = query + " LIMIT 0,?";

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, dispCount );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    sponsorCount = result.getRow();
                }

                // �N���X�̔z���p�ӂ��A����������B
                this.sponsor = new DataMasterSponsor[sponsorCount];
                for( i = 0 ; i < sponsorCount ; i++ )
                {
                    sponsor[i] = new DataMasterSponsor();
                }
                result.beforeFirst();
                while( result.next() != false )
                {
                    // �A�t�B���G�C�g���̎擾
                    this.sponsor[count].setData( result );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            sponsorCount = 0;
            Logging.error( "[SponsorData.getAffiliaterandom] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( sponsorCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ���L���z�e�����擾
     * 
     * @param areaCode �G���A�R�[�h
     * @param randomFlag �����_���t���O(0:disp_pos���A1:�����_��)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getAdHotelList(int areaCode, int randomFlag) throws Exception
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int i;
        int count;

        if ( randomFlag == 0 )
        {
            query = "SELECT * FROM hh_master_sponsor";
            query = query + " WHERE area_code = ?";
            query = query + " AND start_date <= ?";
            query = query + " AND end_date >= ?";
            query = query + " ORDER BY disp_pos";
        }
        else if ( randomFlag == 1 )
        {
            query = "SELECT hhms.* FROM hh_master_sponsor AS hhms";
            query = query + " INNER JOIN (";
            query = query + " SELECT sponsor_code FROM hh_master_sponsor";
            query = query + " WHERE area_code = ?";
            query = query + " AND start_date <= ?";
            query = query + " AND end_date >= ?";
            query = query + " ORDER BY RAND()";
            query = query + " ) AS random";
            query = query + " ON hhms.sponsor_code = random.sponsor_code";
        }
        // �s���ȃp�����[�^���󂯎�����ꍇ�̏���
        else
        {
            query = "SELECT * FROM hh_master_sponsor";
            query = query + " WHERE area_code = ?";
            query = query + " AND start_date <= ?";
            query = query + " AND end_date >= ?";
        }

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, areaCode );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    this.sponsorCount = result.getRow();
                }

                // �N���X�̔z���p�ӂ��A����������B
                this.sponsor = new DataMasterSponsor[this.sponsorCount];
                for( i = 0 ; i < sponsorCount ; i++ )
                {
                    sponsor[i] = new DataMasterSponsor();
                }
                result.beforeFirst();
                while( result.next() != false )
                {
                    // ���L���z�e�����̎擾
                    this.sponsor[count].setData( result );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            sponsorCount = 0;
            Logging.error( "[SponsorData.getAdHotelList] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( sponsorCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ���L���z�e�����擾�i�X�|���T�[�R�[�h�w��j
     * 
     * @param sponsorCode �\���ʒu�敪
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getAdHotelBySponsorCode(int sponsorCode) throws Exception
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int i;
        int count;

        query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor"
                + " WHERE hh_master_sponsor.area_code <> 0"
                + " AND hh_master_sponsor.sponsor_code = ?"
                + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)"
                + " AND hh_master_sponsor.random_disp_flag = 0"
                + " ORDER BY hh_master_sponsor.disp_pos";
        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, sponsorCode );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    this.sponsorCount = result.getRow();
                }

                // �N���X�̔z���p�ӂ��A����������B
                this.sponsor = new DataMasterSponsor[this.sponsorCount];
                for( i = 0 ; i < sponsorCount ; i++ )
                {
                    sponsor[i] = new DataMasterSponsor();
                }
                result.beforeFirst();
                while( result.next() != false )
                {
                    // ���L���z�e�����̎擾
                    this.sponsor[count].setData( result );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            sponsorCount = 0;
            Logging.error( "[SponsorData.getAdHotelList] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( sponsorCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * TOP�o�i�[���擾�i�����_���j
     * 
     * @param userAgentType ���[�U�[�G�[�W�F���g�^�C�v
     * @param dispCount �\����
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getBannerRandom(int userAgentType, int dispCount) throws Exception
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int i;
        int count;

        // �\�����Ă���L�����A�ɊY������url���o�^����Ă��郌�R�[�h�̒����烉���_���Ŏ擾����B
        query = "SELECT hhms.* FROM hh_master_sponsor AS hhms";
        query = query + " INNER JOIN (";
        query = query + " SELECT sponsor_code FROM hh_master_sponsor";
        query = query + " WHERE sponsor_code > 2000000";
        query = query + " AND sponsor_code < 3000000";
        query = query + " AND area_code = 0";
        query = query + " AND random_disp_flag = 1";
        query = query + " AND start_date <= ?";
        query = query + " AND end_date >= ?";

        if ( userAgentType == USERAGENT_DOCOMO )
        {
            query = query + " AND url_docomo != ''";
        }
        else if ( userAgentType == USERAGENT_AU )
        {
            query = query + " AND url_au != ''";
        }
        else if ( userAgentType == USERAGENT_SOFTBANK )
        {
            query = query + " AND url_softbank != ''";
        }
        else
        {
            query = query + " AND url != '' ";
        }
        query = query + " ORDER BY RAND()";
        query = query + " ) AS random";
        query = query + " ON hhms.sponsor_code = random.sponsor_code";
        query = query + " LIMIT 0,?";

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, dispCount );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    sponsorCount = result.getRow();
                }

                // �N���X�̔z���p�ӂ��A����������B
                this.sponsor = new DataMasterSponsor[sponsorCount];
                for( i = 0 ; i < sponsorCount ; i++ )
                {
                    sponsor[i] = new DataMasterSponsor();
                }
                result.beforeFirst();
                while( result.next() != false )
                {
                    // �A�t�B���G�C�g���̎擾
                    this.sponsor[count].setData( result );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            sponsorCount = 0;
            Logging.error( "[SponsorData.getBannerRandom] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( sponsorCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * �g�їp�X�|���T�[��񃉃��_���擾�i�s���{��ID�j
     * 
     * @param prefId �s���{��ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getSponsorRandomByPref(int prefId) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // �\���Ώۂ̂���
        query = "SELECT hhms.* FROM hh_master_sponsor AS hhms";
        query = query + " INNER JOIN (";
        query = query + " SELECT hh_master_sponsor.sponsor_code FROM hh_master_sponsor,hh_master_pref";
        query = query + " WHERE hh_master_sponsor.sponsor_code <> 0";
        query = query + " AND hh_master_sponsor.area_code <> 0";
        query = query + " AND hh_master_pref.pref_id = ?";
        query = query + " AND hh_master_pref.sponsor_area_code = hh_master_sponsor.area_code";
        query = query + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)";
        query = query + " AND hh_master_sponsor.random_disp_flag = 0";
        query = query + " ORDER BY RAND()";
        query = query + " ) AS random";
        query = query + " ON hhms.sponsor_code = random.sponsor_code";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, prefId );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData_M2.getSponsorByPref] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �g�їp�X�|���T�[���i��W���������j�����_���擾�i�s���{��ID�j
     * 
     * @param prefId �s���{��ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getSponsorRandomByPrefAdOnly(int prefId) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // �\���Ώۂ̂���
        query = "SELECT hhms.* FROM hh_master_sponsor AS hhms";
        query = query + " INNER JOIN (";
        query = query + " SELECT hh_master_sponsor.sponsor_code FROM hh_master_sponsor,hh_master_pref";
        query = query + " WHERE hh_master_sponsor.sponsor_code <> 0";
        query = query + " AND hh_master_sponsor.area_code <> 0";
        query = query + " AND hh_master_pref.pref_id = ?";
        query = query + " AND hh_master_pref.sponsor_area_code = hh_master_sponsor.area_code";
        query = query + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)";
        query = query + " AND hh_master_sponsor.random_disp_flag = 0";
        query = query + " AND hh_master_sponsor.hotel_id <> 99999999";
        query = query + " ORDER BY RAND()";
        query = query + " ) AS random";
        query = query + " ON hhms.sponsor_code = random.sponsor_code";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, prefId );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData_M2.getSponsorByPref] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �g�їp�X�|���T�[��񃉃��_���擾�i�s�撬���R�[�h�j
     * 
     * @param jisCode �s�撬���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getSponsorRandomByCity(int jisCode) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // �\���Ώۂ̂���
        query = "SELECT hhms.* FROM hh_master_sponsor AS hhms";
        query = query + " INNER JOIN (";
        query = query + " SELECT hh_master_sponsor.sponsor_code FROM hh_master_sponsor,hh_master_city";
        query = query + " WHERE hh_master_sponsor.sponsor_code <> 0";
        query = query + " AND hh_master_sponsor.area_code <> 0";
        query = query + " AND hh_master_city.jis_code = ?";
        query = query + " AND hh_master_city.sponsor_area_code = hh_master_sponsor.area_code";
        query = query + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)";
        query = query + " AND hh_master_sponsor.random_disp_flag = 0";
        query = query + " ORDER BY RAND()";
        query = query + " ) AS random";
        query = query + " ON hhms.sponsor_code = random.sponsor_code";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, jisCode );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData_M2.getSponsorByCity] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �g�їp�X�|���T�[��񃉃��_���擾�i�z�e���X�R�[�h�j
     * 
     * @param areaId �G���AID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getSponsorRandomByArea(int areaId) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // �\���Ώۂ̂���
        query = "SELECT hhms.* FROM hh_master_sponsor AS hhms";
        query = query + " INNER JOIN (";
        query = query + " SELECT hh_master_sponsor.sponsor_code FROM hh_master_sponsor,hh_master_area";
        query = query + " WHERE hh_master_sponsor.sponsor_code <> 0";
        query = query + " AND hh_master_sponsor.area_code <> 0";
        query = query + " AND hh_master_area.area_id = ?";
        query = query + " AND hh_master_area.sponsor_area_code = hh_master_sponsor.area_code";
        query = query + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)";
        query = query + " AND hh_master_sponsor.random_disp_flag = 0";
        query = query + " ORDER BY RAND()";
        query = query + " ) AS random";
        query = query + " ON hhms.sponsor_code = random.sponsor_code";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, areaId );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData_M2.getSponsorByArea] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �X�|���T�[���擾�i�\���ʒu�w��j
     * 
     * @param dispPos �\���ʒu�敪
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getSponsorRandomByDispPos(String dispPos) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // �\���Ώۂ̂���
        query = "SELECT hhms.* FROM hh_master_sponsor AS hhms";
        query = query + " INNER JOIN (";
        query = query + " SELECT sponsor_code FROM hh_master_sponsor";
        query = query + " WHERE hh_master_sponsor.sponsor_code <> 0";
        query = query + " AND hh_master_sponsor.area_code <> 0";
        query = query + " AND hh_master_sponsor.disp_pos = ?";
        query = query + " AND hh_master_sponsor.title_mobile <> ''";
        query = query + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)";
        query = query + " AND hh_master_sponsor.random_disp_flag = 0";
        query = query + " ORDER BY RAND()";
        query = query + " ) AS random";
        query = query + " ON hhms.sponsor_code = random.sponsor_code";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, dispPos );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData_M2.getSponsorByDispPos] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ���T�����擾�i�s���{��ID����j
     * 
     * @param prefId �s���{��ID
     * @param memberFlag ����t���O(0:����,1:���)
     * @return ���T����
     */
    public int getPrivilegeCountByPref(int prefId, int memberFlag)
    {
        int count;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        if ( prefId <= 0 )
        {
            return(-1);
        }
        query = "SELECT COUNT( * ) FROM hh_master_sponsor";
        query = query + " WHERE start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND area_code = 100000";
        query = query + " AND pref_id = ?";
        if ( memberFlag == 0 )
        {
            query = query + " AND member_flag = 0 ";
        }
        else if ( memberFlag == 1 )
        {
            query = query + " AND( member_flag = 0 OR member_flag = 1 ) ";
        }
        query = query + " GROUP BY hotel_id";

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
            else
            {
                count = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getsponsorCountByPref] Exception=" + e.toString() );
            return(0);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(count);
    }

    /**
     * ���T�����擾�i�n��ID����j
     * 
     * @param localId �n��ID
     * @param memberFlag ����t���O(0:����,1:���)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getPrivilegeCountByLocal(int localId, int memberFlag)
    {
        int i;
        int j;
        SearchEngineBasic seb;
        seb = new SearchEngineBasic();

        if ( localId < 0 )
            return(false);

        this.localCount = 0;
        seb.getLocalList( localId, 0 );
        try
        {
            if ( seb.getMasterLocalCount() > 0 )
            {
                // �n��ID�̐��������[�v������
                for( i = 0 ; i < seb.getMasterLocalCount() ; i++ )
                {
                    // �n��ID�Ɋ܂܂��s���{�����擾����
                    seb.getPrefListByLocal( seb.getMasterLocal()[i].getLocalId(), 0 );
                    if ( seb.getMasterPrefCount() > 0 )
                    {
                        // �S���̌����擾�̏ꍇ�̓J�E���g�����擾����
                        if ( localId == 0 )
                        {
                            for( j = 0 ; j < seb.getMasterPrefCount() ; j++ )
                            {
                                this.localCount = this.localCount + getPrivilegeCountByPref( seb.getMasterPref()[j].getPrefId(), memberFlag );
                            }
                        }
                        // �n�����ƂɌ����擾����ꍇ�́A�n���A�s���{���̌����ƃf�[�^���擾
                        else
                        {
                            // �n���̃f�[�^���Z�b�g
                            this.dmLocal = new DataMasterLocal();
                            this.dmLocal = seb.getMasterLocal()[i];
                            // �s���{���̔z���p��
                            this.dmPref = new DataMasterPref[seb.getMasterPrefCount()];
                            this.prefCount = new int[seb.getMasterPrefCount()];
                            for( j = 0 ; j < seb.getMasterPrefCount() ; j++ )
                            {
                                this.localCount = localCount + getPrivilegeCountByPref( seb.getMasterPref()[j].getPrefId(), memberFlag );

                                this.dmPref[j] = new DataMasterPref();
                                this.dmPref[j] = seb.getMasterPref()[j];
                                this.prefCount[j] = getPrivilegeCountByPref( seb.getMasterPref()[j].getPrefId(), memberFlag );
                            }
                        }
                    }
                    else
                    {
                        localCount = 0;
                        Logging.error( "[getsponsorCountByLocal] pref=0" );
                    }
                }
            }
            else
            {
                localCount = 0;
                Logging.error( "[getsponsorCountByLocal] local=0" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getsponsorCountByLocal] Exception=" + e.toString() );
            return false;
        }
        return true;
    }

    /**
     * ���T�z�e�����擾�i�s���{�����ƂɁj
     * 
     * @param prefId �s���{��ID
     * @param memberFlag ����t���O(0:�����A1:�����)
     * @param order �\�[�g�t���O(0:disp_pos���A1:�n�揇�A2:�L�������̏����A3:�z�e�����̏���)
     * @param countNum �擾�����i0�F�S�� ��pageNum����
     * @param pageNum �y�[�W�ԍ��i0�`)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getPrivilegeHotel(int prefId, int memberFlag, int order, int countNum, int pageNum)
    {
        int count;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = "";
        if ( order < 0 || prefId < 0 )
        {
            return(false);
        }
        else if ( order >= 0 && order <= 2 )
        {
            query = "SELECT * FROM hh_master_sponsor";
            query = query + " WHERE start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND area_code = 100000";
            if ( memberFlag == 0 )
            {
                query = query + " AND member_flag = 0 ";
            }
            else if ( memberFlag == 1 )
            {
                query = query + " AND( member_flag = 0 OR member_flag = 1 ) ";
            }

            if ( prefId > 0 )
            {
                query = query + " AND pref_id = ?";
            }
            query = query + " AND hotel_id > 0";
            query = query + " AND pref_id > 0";
            query = query + " GROUP BY hotel_id";
            // disp_pos��
            if ( order == 0 )
            {
                query = query + " ORDER BY start_date DESC";
            }
            // �n�揇
            if ( order == 1 )
            {
                query = query + " ORDER BY pref_id, start_date DESC";
            }
            // �L�������̏I�����t�̏���
            else if ( order == 2 )
            {
                query = query + " ORDER BY end_date, pref_id, start_date DESC";
            }
        }
        // �z�e�����̏���
        else if ( order == 3 )
        {
            query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor, hh_hotel_basic, hh_hotel_pv";
            query = query + " WHERE hh_master_sponsor.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND hh_master_sponsor.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND hh_master_sponsor.hotel_id = hh_hotel_basic.id ";
            query = query + " AND hh_hotel_pv.id = hh_hotel_basic.id ";
            query = query + " AND hh_master_sponsor.hotel_id = hh_hotel_pv.id ";
            query = query + " AND hh_master_sponsor.area_code = 100000";
            if ( memberFlag == 0 )
            {
                query = query + " AND hh_master_sponsor.member_flag = 0 ";
            }
            else if ( memberFlag == 1 )
            {
                query = query + " AND( hh_master_sponsor.member_flag = 0 OR hh_master_sponsor.member_flag = 1 ) ";
            }

            if ( prefId > 0 )
            {
                query = query + " AND hh_master_sponsor.pref_id = ?";
            }
            query = query + " AND hh_master_sponsor.hotel_id > 0";
            query = query + " GROUP BY hh_master_sponsor.hotel_id";
            query = query + " ORDER BY hh_hotel_basic.name_kana, hh_hotel_basic.rank DESC,";
            query = query + " hh_master_sponsor.start_date DESC";
        }

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        ret = false;
        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    this.hotelCount = result.getRow();
                    Logging.error( "[getSortOfferHotel] count=" + result.getRow() );

                }
                m_hotelBasic = new DataHotelBasic[hotelCount];
                sponsor = new DataMasterSponsor[hotelCount];
                sponsorCount = hotelCount;

                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    this.m_hotelBasic[count] = new DataHotelBasic();
                    this.sponsor[count] = new DataMasterSponsor();

                    this.sponsor[count].setData( result );
                    this.m_hotelBasic[count].getData( result.getInt( "hotel_id" ) );

                    count++;
                }
            }
            else
            {
                this.hotelCount = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getPrivilegeHotel] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // �z�e���������̎擾
        if ( order < 0 || prefId < 0 )
        {
            return(false);
        }
        else if ( order >= 0 && order <= 2 )
        {
            query = "SELECT * FROM hh_master_sponsor";
            query = query + " WHERE start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND area_code = 100000";
            if ( memberFlag == 0 )
            {
                query = query + " AND member_flag = 0 ";
            }
            else if ( memberFlag == 1 )
            {
                query = query + " AND( member_flag = 0 OR member_flag = 1 ) ";
            }

            if ( prefId > 0 )
            {
                query = query + " AND pref_id = ?";
            }
            query = query + " AND hotel_id > 0";
            query = query + " GROUP BY hotel_id";
        }
        // �z�e�����̏���
        else if ( order == 3 )
        {
            query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor, hh_hotel_basic, hh_hotel_pv";
            query = query + " WHERE hh_master_sponsor.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND hh_master_sponsor.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND hh_master_sponsor.hotel_id = hh_hotel_basic.id ";
            query = query + " AND hh_hotel_pv.id = hh_hotel_basic.id ";
            query = query + " AND hh_master_sponsor.hotel_id = hh_hotel_pv.id ";
            query = query + " AND hh_master_sponsor.area_code = 100000";
            if ( memberFlag == 0 )
            {
                query = query + " AND hh_master_sponsor.member_flag = 0 ";
            }
            else if ( memberFlag == 1 )
            {
                query = query + " AND( hh_master_sponsor.member_flag = 0 OR hh_master_sponsor.member_flag = 1 ) ";
            }

            if ( prefId > 0 )
            {
                query = query + " AND hh_master_sponsor.pref_id = ?";
            }
            query = query + " AND hh_master_sponsor.hotel_id > 0";
            query = query + " GROUP BY hh_master_sponsor.hotel_id";
        }
        try
        {
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }

            result = prestate.executeQuery();

            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    // �������̎擾
                    this.hotelAllCount = result.getRow();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getPrivilegeHotel] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        if ( (hotelCount > 0) && (hotelAllCount > 0) )
            ret = true;
        else
            ret = false;

        return(ret);
    }

    /**
     * ���T�z�e�����擾�i�n�����ƂɁj
     * 
     * @param localId �n��ID
     * @param memberFlag ����t���O(0:�����A1:�����)
     * @param order �\�[�g�t���O(0:disp_pos���A1:�n�揇�A2:�L�������̏����A3:�z�e�����̏���)
     * @param countNum �擾�����i0�F�S�� ��pageNum����
     * @param pageNum �y�[�W�ԍ��i0�`)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getPrivilegeHotelByLocal(int localId, int memberFlag, int order, int countNum, int pageNum)
    {
        int count;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = "";
        if ( order < 0 || localId < 0 )
        {
            return(false);
        }
        else if ( order >= 0 && order <= 2 )
        {
            query = "SELECT * FROM hh_master_sponsor";
            query = query + " WHERE start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND area_code = 100000";
            if ( memberFlag == 0 )
            {
                query = query + " AND member_flag = 0 ";
            }
            else if ( memberFlag == 1 )
            {
                query = query + " AND( member_flag = 0 OR member_flag = 1 ) ";
            }

            if ( localId > 0 )
            {
                query = query + " AND local_id = ?";
            }
            query = query + " AND hotel_id > 0";
            query = query + " GROUP BY hotel_id";
            // disp_pos��
            if ( order == 0 )
            {
                query = query + " ORDER BY start_date DESC";
            }
            // �n�揇
            if ( order == 1 )
            {
                query = query + " ORDER BY pref_id, start_date DESC";
            }
            // �L�������̏I�����t�̏���
            else if ( order == 2 )
            {
                query = query + " ORDER BY end_date, pref_id, start_date DESC";
            }
        }
        // �z�e�����̏���
        else if ( order == 3 )
        {
            query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor, hh_hotel_basic, hh_hotel_pv";
            query = query + " WHERE hh_master_sponsor.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND hh_master_sponsor.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND hh_master_sponsor.hotel_id = hh_hotel_basic.id ";
            query = query + " AND hh_hotel_pv.id = hh_hotel_basic.id ";
            query = query + " AND hh_master_sponsor.hotel_id = hh_hotel_pv.id ";
            query = query + " AND hh_master_sponsor.area_code = 100000";
            if ( memberFlag == 0 )
            {
                query = query + " AND hh_master_sponsor.member_flag = 0 ";
            }
            else if ( memberFlag == 1 )
            {
                query = query + " AND( hh_master_sponsor.member_flag = 0 OR hh_master_sponsor.member_flag = 1 ) ";
            }

            if ( localId > 0 )
            {
                query = query + " AND hh_master_sponsor.local_id = ?";
            }
            query = query + " AND hh_master_sponsor.hotel_id > 0";
            query = query + " GROUP BY hh_master_sponsor.hotel_id";
            query = query + " ORDER BY hh_hotel_basic.name_kana, hh_hotel_basic.rank DESC,";
            query = query + " hh_master_sponsor.start_date DESC";
        }

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        ret = false;
        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( localId > 0 )
            {
                prestate.setInt( 1, localId );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    this.hotelCount = result.getRow();
                    Logging.error( "[getSortOfferHotel] count=" + result.getRow() );

                }
                m_hotelBasic = new DataHotelBasic[hotelCount];
                sponsor = new DataMasterSponsor[hotelCount];
                sponsorCount = hotelCount;

                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    this.m_hotelBasic[count] = new DataHotelBasic();
                    this.sponsor[count] = new DataMasterSponsor();

                    this.sponsor[count].setData( result );
                    this.m_hotelBasic[count].getData( result.getInt( "hotel_id" ) );

                    count++;
                }
            }
            else
            {
                this.hotelCount = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getPrivilegeHotel] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // �z�e���������̎擾
        if ( order < 0 || localId < 0 )
        {
            return(false);
        }
        else if ( order >= 0 && order <= 2 )
        {
            query = "SELECT * FROM hh_master_sponsor";
            query = query + " WHERE start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND area_code = 100000";
            if ( memberFlag == 0 )
            {
                query = query + " AND member_flag = 0 ";
            }
            else if ( memberFlag == 1 )
            {
                query = query + " AND( member_flag = 0 OR member_flag = 1 ) ";
            }

            if ( localId > 0 )
            {
                query = query + " AND local_id = ?";
            }
            query = query + " AND hotel_id > 0";
            query = query + " GROUP BY hotel_id";
        }
        // �z�e�����̏���
        else if ( order == 3 )
        {
            query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor, hh_hotel_basic, hh_hotel_pv";
            query = query + " WHERE hh_master_sponsor.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND hh_master_sponsor.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND hh_master_sponsor.hotel_id = hh_hotel_basic.id ";
            query = query + " AND hh_hotel_pv.id = hh_hotel_basic.id ";
            query = query + " AND hh_master_sponsor.hotel_id = hh_hotel_pv.id ";
            query = query + " AND hh_master_sponsor.area_code = 100000";
            if ( memberFlag == 0 )
            {
                query = query + " AND hh_master_sponsor.member_flag = 0 ";
            }
            else if ( memberFlag == 1 )
            {
                query = query + " AND( hh_master_sponsor.member_flag = 0 OR hh_master_sponsor.member_flag = 1 ) ";
            }

            if ( localId > 0 )
            {
                query = query + " AND hh_master_sponsor.local_id = ?";
            }
            query = query + " AND hh_master_sponsor.hotel_id > 0";
            query = query + " GROUP BY hh_master_sponsor.hotel_id";
        }
        try
        {
            prestate = connection.prepareStatement( query );
            if ( localId > 0 )
            {
                prestate.setInt( 1, localId );
            }

            result = prestate.executeQuery();

            if ( result != null )
            {
                // ���R�[�h�����擾to
                if ( result.last() != false )
                {
                    // �������̎擾
                    this.hotelAllCount = result.getRow();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getPrivilegeHotel] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        if ( (hotelCount > 0) && (hotelAllCount > 0) )
            ret = true;
        else
            ret = false;

        return(ret);
    }

    /**
     * ���T�z�e�����擾�i�ŐV�̂��́j
     * 
     * @param prefId �s���{��ID
     * @param memberFlag ����t���O(0:�����A1:�����)
     * @param order �\�[�g�t���O(0:disp_pos���A1:�n�揇�A2:�L�������̏����A3:�z�e�����̏���)
     * @param countNum �擾�����i0�F�S�� ��pageNum����
     * @param pageNum �y�[�W�ԍ��i0�`)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getPrivilegeHotelLatest(int memberFlag, int newSpan, int countNum, int pageNum)
    {
        int count;
        int startDate;
        int endDate;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        startDate = 0;
        endDate = 0;
        query = "";
        if ( newSpan < 0 )
        {
            return(false);
        }
        else
        {
            startDate = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -newSpan );
            endDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            query = "SELECT * FROM hh_master_sponsor";
            query = query + " WHERE start_date >= " + startDate;
            query = query + " AND start_date <= " + endDate;
            query = query + " AND area_code = 100000";
            if ( memberFlag == 0 )
            {
                query = query + " AND member_flag = 0 ";
            }
            else if ( memberFlag == 1 )
            {
                query = query + " AND( member_flag = 0 OR member_flag = 1 ) ";
            }

            query = query + " AND hotel_id > 0";
            query = query + " AND pref_id > 0";
            query = query + " GROUP BY hotel_id";
            query = query + " ORDER BY start_date DESC";
        }

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        ret = false;
        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    this.hotelCount = result.getRow();
                    Logging.error( "[getSortOfferHotel] count=" + result.getRow() );

                }
                m_hotelBasic = new DataHotelBasic[hotelCount];
                sponsor = new DataMasterSponsor[hotelCount];
                sponsorCount = hotelCount;

                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    this.m_hotelBasic[count] = new DataHotelBasic();
                    this.sponsor[count] = new DataMasterSponsor();

                    this.sponsor[count].setData( result );
                    this.m_hotelBasic[count].getData( result.getInt( "hotel_id" ) );

                    count++;
                }
            }
            else
            {
                this.hotelCount = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getPrivilegeHotel] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // �z�e���������̎擾
        if ( newSpan < 0 )
        {
            return(false);
        }
        else
        {
            startDate = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -newSpan );
            endDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            query = "SELECT * FROM hh_master_sponsor";
            query = query + " WHERE start_date >= " + startDate;
            query = query + " AND start_date <= " + endDate;
            query = query + " AND area_code = 100000";
            if ( memberFlag == 0 )
            {
                query = query + " AND member_flag = 0 ";
            }
            else if ( memberFlag == 1 )
            {
                query = query + " AND( member_flag = 0 OR member_flag = 1 ) ";
            }

            query = query + " AND hotel_id > 0";
            query = query + " AND pref_id > 0";
            query = query + " GROUP BY hotel_id";
            query = query + " ORDER BY start_date DESC";
        }
        try
        {
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    // �������̎擾
                    this.hotelAllCount = result.getRow();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getPrivilegeHotel] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        if ( (hotelCount > 0) && (hotelAllCount > 0) )
            ret = true;
        else
            ret = false;

        return(ret);
    }

    /**
     * �z�e�����l�L���擾
     * 
     * @see "�S������getPrivilegeHotelAllCount���g�p����B1�y�[�W������̌����́AgetSponsorCount�܂��́AgetPrivilegeHotelCount���g�p����B"
     * @param prefId �s���{��ID(0:�S������)
     * @param kind�@�敪(0:PC, 1:�g��)
     * @param countNum �擾�����i0�F�S�� ��pageNum�����j
     * @param pageNum �y�[�W�ԍ��i0�`�j
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getHotelJobOffer(int prefId, int kind, int countNum, int pageNum)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_master_sponsor.* from hh_master_sponsor, hh_hotel_basic";
        query = query + " WHERE hh_master_sponsor.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_sponsor.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_sponsor.sponsor_code >= 5000000";
        query = query + " AND hh_master_sponsor.sponsor_code < 6000000";
        query = query + " AND hh_master_sponsor.hotel_id = hh_hotel_basic.id";
        if ( prefId > 0 )
        {
            query = query + " AND hh_master_sponsor.pref_id = ? ";
        }
        query = query + " ORDER BY hh_master_sponsor.start_date DESC, hh_master_sponsor.append_date DESC, hh_master_sponsor.append_time DESC, hh_hotel_basic.name_kana";
        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }
            ret = getSponsorDataSub( prestate );
            if ( ret != false )
            {
                // �z�e���������Z�b�g����
                this.hotelCount = this.sponsorCount;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[HotelJobOffer.getHotelJobOffer1] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            result = null;
            prestate = null;
        }

        try
        {
            query = "SELECT hh_master_sponsor.* from hh_master_sponsor, hh_hotel_basic";
            query = query + " WHERE hh_master_sponsor.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND hh_master_sponsor.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND hh_master_sponsor.sponsor_code >= 5000000";
            query = query + " AND hh_master_sponsor.sponsor_code < 6000000";
            query = query + " AND hh_master_sponsor.hotel_id = hh_hotel_basic.id";
            if ( prefId > 0 )
            {
                query = query + " AND hh_master_sponsor.pref_id = ? ";
            }

            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    // �������̎擾
                    this.hotelAllCount = result.getRow();
                }
            }

        }
        catch ( Exception e )
        {
            Logging.info( "[HotelJobOffer.getHotelJobOffer2] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * �z�e�����l�L���擾
     * 
     * @see�@"�����擾��getPrivilegeHotelAllCount�AgetSponsorCount�܂��́AgetPrivilegeHotelCount���g�p����B"
     * @param hotelIdList �z�e��ID���X�g(0:�S������)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getHotelJobOfferLatest(int hotelId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        if ( hotelId <= 0 )
        {
            return(false);
        }
        // �N�G���[��o�^
        query = "SELECT hh_master_sponsor.* from hh_master_sponsor, hh_hotel_basic";
        query = query + " WHERE hh_master_sponsor.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_sponsor.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_sponsor.sponsor_code >= 5000000";
        query = query + " AND hh_master_sponsor.sponsor_code < 6000000";
        query = query + " AND hh_master_sponsor.hotel_id = hh_hotel_basic.id";
        if ( hotelId > 0 )
        {
            query = query + " AND hh_master_sponsor.hotel_id = ? ";
        }
        query = query + " ORDER BY hh_master_sponsor.start_date DESC, hh_master_sponsor.append_date DESC," +
                " hh_master_sponsor.append_time DESC, hh_hotel_basic.name_kana";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( hotelId > 0 )
            {
                prestate.setInt( 1, hotelId );
            }
            result = prestate.executeQuery();

            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    // �������̎擾
                    this.hotelAllCount = result.getRow();
                }
                result.beforeFirst();
                if ( result.next() != false )
                {
                    // �z��̏�����
                    this.sponsor = new DataMasterSponsor[1];
                    // �v�f�̏�����
                    this.sponsor[0] = new DataMasterSponsor();
                    // �v�f�Ƀf�[�^���Z�b�g
                    this.sponsor[0].setData( result );
                    this.hotelCount = this.hotelAllCount;
                    this.sponsorCount = this.hotelAllCount;
                    Logging.info( "[HotelJobOffer.getHotelJobOffer] test" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[HotelJobOffer.getHotelJobOfferLatest2] Exception=" + e.toString() );
            this.hotelCount = 0;
            this.sponsorCount = 0;
            this.hotelAllCount = 0;
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        Logging.info( "[HotelJobOffer.getHotelJobOfferLatest2] hotelAllCount=" + hotelAllCount );

        if ( hotelAllCount > 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ���[�e�[�V�����o�i�[�p�f�[�^�쐬����
     * 
     * @param prefId �s���{��ID
     * @param dispCount �擾����
     * @return ��������(TRUE:����,FALSE:�ُ�)
     * @throws Exception
     */
    public boolean insertRandomSponsor() throws Exception
    {
        boolean ret;
        int nextDate;
        int i;
        int count;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        DataSponsorData dsd;

        ret = false;
        count = 0;
        i = 0;

        nextDate = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), 1 );
        // �\���Ώۂ̂���
        query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor,hh_master_pref"
                + " WHERE hh_master_sponsor.sponsor_code <> 0"
                + " AND hh_master_sponsor.area_code <> 0"
                + " AND hh_master_pref.sponsor_area_code = hh_master_sponsor.area_code"
                + " AND ( hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ? )"
                + " AND hh_master_sponsor.random_disp_flag = 2";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, nextDate );
            prestate.setInt( 2, nextDate );

            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[SponsorData_M2.insertRandomSponsor( )] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        try
        {
            if ( ret != false )
            {
                // �擾�����f�[�^���C���T�[�g����
                if ( sponsorCount > 0 )
                {
                    dsd = new DataSponsorData();
                    // �擾�����f�[�^�������J��Ԃ�
                    for( i = 0 ; i < sponsorCount ; i++ )
                    {
                        Logging.error( "[SponsorData_M2.insertRandomSponsor( )] sponsor=" + sponsor[i].getSponsorCode() );
                        dsd.setSponsorCode( sponsor[i].getSponsorCode() );
                        dsd.setAddupDate( nextDate );
                        ret = dsd.insertData();
                        if ( ret == false )
                        {
                            count++;
                        }
                    }
                    // 1�ł�false����������G���[�Ƃ��ĕԂ�
                    if ( count == 0 )
                    {
                        ret = true;
                    }
                    else
                    {
                        ret = false;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[SponsorData_M2.insertRandomSponsor( )] Exception=" + e.toString() );
            throw e;
        }

        return(ret);
    }

    /**
     * �X�[�p�[�o�i�[���擾�i�����_���j
     * 
     * @param userAgentType ���[�U�[�G�[�W�F���g�^�C�v
     * @param kind �敪(0:TOP�y�[�W�A1:����ȊO)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getSuperBanner(int userAgentType, int kind) throws Exception
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int i;
        int count;

        // �\�����Ă���L�����A�ɊY������url���o�^����Ă��郌�R�[�h�̒����烉���_���Ŏ擾����B
        query = "SELECT hhms.* FROM hh_master_sponsor AS hhms";
        query = query + " INNER JOIN (";
        query = query + " SELECT sponsor_code FROM hh_master_sponsor";
        query = query + " Where area_code = 0";
        query = query + " AND random_disp_flag = 1";

        if ( kind == 1 )
        {
            query = query + " AND sponsor_code > 7000000";
            query = query + " AND sponsor_code < 8000000";
            query = query + " AND disp_pos = '7'";
        }
        else
        {
            query = query + " AND sponsor_code > 6000000";
            query = query + " AND sponsor_code < 7000000";
            query = query + " AND disp_pos = '6'";

        }
        query = query + " AND start_date <= ?";
        query = query + " AND end_date >= ?";

        if ( userAgentType == USERAGENT_DOCOMO )
        {
            query = query + " AND url_docomo != ''";
        }
        else if ( userAgentType == USERAGENT_AU )
        {
            query = query + " AND url_au != ''";
        }
        else if ( userAgentType == USERAGENT_SOFTBANK )
        {
            query = query + " AND url_softbank != ''";
        }
        else
        {
            query = query + " AND url != '' ";
        }
        query = query + " ORDER BY RAND()";
        query = query + " ) AS random";
        query = query + " ON hhms.sponsor_code = random.sponsor_code";
        query = query + " LIMIT 0,1";

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    sponsorCount = result.getRow();
                }

                // �N���X�̔z���p�ӂ��A����������B
                this.sponsor = new DataMasterSponsor[sponsorCount];
                for( i = 0 ; i < sponsorCount ; i++ )
                {
                    sponsor[i] = new DataMasterSponsor();
                }
                result.beforeFirst();
                while( result.next() != false )
                {
                    // �X�|���T�[���̎擾
                    this.sponsor[count].setData( result );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            sponsorCount = 0;
            Logging.error( "[SponsorData_M2.get] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( sponsorCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * �X�}�[�g�t�H�������C���v���b�V�����J�E���g
     * 
     * @param sponsorCode �X�|���T�[�R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setImpressionCountForSmart(int sponsorCode)
    {
        boolean ret;
        DataSponsorData dsd;

        ret = false;

        // �ΏۍL���̃f�[�^���擾����
        dsd = new DataSponsorData();
        ret = dsd.getData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        if ( ret != false )
        {
            dsd.setImpressionSmart( dsd.getImpressionSmart() + 1 );

            ret = dsd.updateData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            // ���s�����烍�O�Ɏc��
            if ( ret == false )
            {
                Logging.error( "[SponsorData_M2.setImpressionCountForSmart updateData] Exception sponsorCode:" + sponsorCode
                        + ", Impression:" + Integer.toString( dsd.getImpression() + 1 ) );
            }
        }
        else
        {
            dsd.setSponsorCode( sponsorCode );
            dsd.setAddupDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dsd.setImpressionSmart( 1 );

            ret = dsd.insertData();
            // ���s�����烍�O�Ɏc��
            if ( ret == false )
            {
                Logging.error( "[SponsorData_M2.setImpressionCountForSmart insertData] Exception sponsorCode:" + sponsorCode );

            }
        }

        return(ret);
    }

    /**
     * �X�}�[�g�t�H�������N���b�N�J�E���g
     * 
     * @param sponsorCode �X�|���T�[�R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setClickCountForSmart(int sponsorCode)
    {
        boolean ret;
        DataSponsorData dsd;

        ret = false;

        // �ΏۍL���̃f�[�^���擾����
        dsd = new DataSponsorData();
        ret = dsd.getData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        if ( ret != false )
        {
            dsd.setClickCountSmart( dsd.getClickCountSmart() + 1 );

            ret = dsd.updateData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        }
        else
        {
            dsd.setSponsorCode( sponsorCode );
            dsd.setAddupDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dsd.setClickCountSmart( 1 );

            ret = dsd.insertData();
        }

        return(ret);
    }

    /**
     * �X�}�[�g�t�H�������g���N���b�N�J�E���g
     * 
     * @param sponsorCode �X�|���T�[�R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setExClickCountForSmart(int sponsorCode)
    {
        boolean ret;
        DataSponsorData dsd;

        ret = false;

        // �ΏۍL���̃f�[�^���擾����
        dsd = new DataSponsorData();
        ret = dsd.getData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        if ( ret != false )
        {
            dsd.setExClickCountSmart( dsd.getExClickCountSmart() + 1 );

            ret = dsd.updateData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        }
        else
        {
            dsd.setSponsorCode( sponsorCode );
            dsd.setAddupDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dsd.setExClickCount( 1 );

            ret = dsd.insertData();
        }

        return(ret);
    }

    /**
     * �X�}�[�g�t�H�������g���N���b�N�J�E���g�Q
     * 
     * @param sponsorCode �X�|���T�[�R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setExClickCount2ForSmart(int sponsorCode)
    {
        boolean ret;
        DataSponsorData dsd;

        ret = false;

        // �ΏۍL���̃f�[�^���擾����
        dsd = new DataSponsorData();
        ret = dsd.getData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        if ( ret != false )
        {
            dsd.setExClickCountSmart2( dsd.getExClickCountSmart2() + 1 );

            ret = dsd.updateData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        }
        else
        {
            dsd.setSponsorCode( sponsorCode );
            dsd.setAddupDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dsd.setSponsorCode( sponsorCode );
            dsd.setExClickCountSmart2( 1 );

            ret = dsd.insertData();
        }

        return(ret);
    }

    /**
     * �X�}�[�g�t�H�������g���C���v���b�V�����J�E���g
     * 
     * @param sponsorCode �X�|���T�[�R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setExImpressionCountForSmart(int sponsorCode)
    {
        boolean ret;
        DataSponsorData dsd;

        ret = false;

        // �ΏۍL���̃f�[�^���擾����
        dsd = new DataSponsorData();
        ret = dsd.getData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        if ( ret != false )
        {
            dsd.setExImpressionSmart( dsd.getExImpressionSmart() + 1 );

            ret = dsd.updateData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        }
        else
        {
            dsd.setSponsorCode( sponsorCode );
            dsd.setAddupDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dsd.setExImpressionSmart( 1 );

            ret = dsd.insertData();
        }

        return(ret);
    }

    /**
     * �X�|���T�[�L���擾
     * 
     * @param prefId �s���{���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     * @throws Exception
     */
    public boolean getAdData(int prefId, int dispCount, int dispFlag) throws Exception
    {
        final int DISP_SMART_PHONE = 2;
        boolean ret;
        ret = false;

        if ( prefId == 0 )
        {
            try
            {
                ret = this.getBannerRandom( 1, dispCount );
            }
            catch ( Exception e )
            {
                Logging.error( "[SponsorData_M2.getAdData( int prefId = " + prefId + " )] Exception=" + e.toString() );
                throw e;
            }
        }
        else
        {
            try
            {
                ret = this.getRandomAdByPref( prefId, dispCount, DISP_SMART_PHONE );
            }
            catch ( Exception e )
            {
                Logging.error( "[SponsorData_M2.getAdData( int prefId = " + prefId + " )] Exception=" + e.toString() );
                throw e;
            }
        }

        return(ret);
    }

    /**
     * �X�|���T�[�L���擾
     * 
     * @param prefId �s���{���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     * @throws Exception
     */
    public boolean getAdRandomData(int prefId, int dispCount, int dispFlag) throws Exception
    {
        ArrayList<Integer> spCodeList;
        boolean ret;
        ret = false;

        spCodeList = new ArrayList<Integer>();
        if ( prefId == 0 )
        {
            try
            {
                ret = this.getBannerRandom( dispFlag, dispCount );
            }
            catch ( Exception e )
            {
                Logging.error( "[SponsorData_M2.getAdData( int prefId = " + prefId + " )] Exception=" + e.toString() );
                throw e;
            }
        }
        else
        {
            try
            {
                // ���ʂ̃X�|���T�[�R�[�h���擾
                ret = this.getRandomAdByPref( prefId, dispCount, dispFlag );
                if ( ret != false )
                {
                    for( int i = 0 ; i < this.sponsorCount ; i++ )
                    {
                        spCodeList.add( sponsor[i].getSponsorCode() );
                    }
                }
                // ���[�e�[�V�����̃X�|���T�[�R�[�h���擾
                ret = this.getRandomSponsorByPref( prefId, 1, dispFlag );
                if ( ret != false )
                {
                    for( int i = 0 ; i < this.sponsorCount ; i++ )
                    {
                        spCodeList.add( sponsor[i].getSponsorCode() );
                    }
                }

                if ( spCodeList.size() > 0 )
                {
                    ret = true;
                    this.getAdData( spCodeList );
                }

            }
            catch ( Exception e )
            {
                Logging.error( "[SponsorData_M2.getAdData( int prefId = " + prefId + " )] Exception=" + e.toString() );
                throw e;
            }
        }

        return(ret);
    }

    /**
     * �X�|���T�[�L���擾
     * 
     * @param prefId �s���{���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     * @throws Exception
     */
    public boolean getAdData(ArrayList<Integer> spCodeList) throws Exception
    {
        boolean ret;
        String query = "";
        String subQuery = "";
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( spCodeList == null )
        {
            return false;
        }
        else
        {
            for( int i = 0 ; i < spCodeList.size() ; i++ )
            {
                if ( spCodeList.get( i ) >= 0 )
                {
                    if ( i > 0 )
                    {
                        subQuery += ",";
                    }
                    subQuery += spCodeList.get( i );
                }
            }
        }

        // �\���Ώۂ̂���
        query = "SELECT hhms.* FROM hh_master_sponsor AS hhms";
        query = query + " INNER JOIN (";
        query = query + " SELECT sponsor_code FROM hh_master_sponsor";
        query = query + " WHERE sponsor_code <> 0";
        query = query + " AND sponsor_code IN ( " + subQuery + " )";
        query = query + " ORDER BY RAND()";
        query = query + " ) AS random";
        query = query + " ON hhms.sponsor_code = random.sponsor_code";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData_M2.get=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return true;

    }

    /**
     * �G���A�L���p�X�|���T�[���擾�i�s���{��ID�j
     * 
     * @param prefId �s���{��ID
     * @param dispCount �擾����
     * @param dispFlag �g�уt���O(0:PC,1:�g��,2�F�X�}�[�g�t�H��)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     * @throws Exception
     */
    public boolean getRandomAdByPref(int prefId, int dispCount, int dispFlag) throws Exception
    {
        boolean ret;
        int[] sponsorCode = null;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;
        sponsorCode = this.getAreaAdByPref( prefId );
        if ( sponsorCode != null )
        {
            insertSponsorData( sponsorCode );
        }

        // �\���Ώۂ̂���
        query = "SELECT sponsor.* FROM hh_master_sponsor sponsor"
                + " INNER JOIN hh_master_pref pref ON pref.sponsor_area_code = sponsor.area_code"
                + " AND pref.pref_id = ?"
                + " WHERE sponsor.sponsor_code <> 0"
                + " AND sponsor.area_code <> 0"
                + " AND sponsor.start_date <= ? AND sponsor.end_date >= ?"
                + " AND sponsor.random_disp_flag = 0"
                + " AND sponsor.hotel_id != 99999999"
                + " ORDER BY RAND() LIMIT 0,?";
        /*
         * query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor,hh_master_pref, hh_sponsor_data"
         * + " WHERE hh_master_sponsor.sponsor_code <> 0"
         * + " AND hh_master_sponsor.area_code <> 0"
         * + " AND hh_master_sponsor.sponsor_code = hh_sponsor_data.sponsor_code"
         * + " AND hh_master_pref.pref_id = ?"
         * + " AND hh_master_pref.sponsor_area_code = hh_master_sponsor.area_code"
         * + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)"
         * + " AND hh_sponsor_data.addup_date = ?"
         * + " AND hh_master_sponsor.random_disp_flag = 0"
         * + " AND hh_master_sponsor.hotel_id != 99999999";
         * // �\���敪�ɂ��
         * if ( dispFlag == 0 )
         * {
         * query = query + " ORDER BY hh_sponsor_data.impression_mobile LIMIT 0,?";
         * }
         * else if ( dispFlag == 1 )
         * {
         * query = query + " ORDER BY hh_sponsor_data.impression LIMIT 0,?";
         * }
         * else if ( dispFlag == 2 )
         * {
         * query = query + " ORDER BY hh_sponsor_data.impression_smart LIMIT 0,?";
         * }
         */
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, prefId );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 4, dispCount );

            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData_M2.getRandomAdByPref( int prefId = " + prefId + " )] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �G���A�L���p�X�|���T�[���擾�i�s���{��ID�j
     * 
     * @param prefId �s���{��ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     * @throws Exception
     */
    public int[] getAreaAdByPref(int prefId) throws Exception
    {
        boolean ret;
        int dataCount = 0;
        int count = 0;
        int[] sponsorCode = null;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        ret = false;
        query = "SELECT sponsor_code FROM hh_master_sponsor"
                + " WHERE sponsor_code <> 0"
                + " AND area_code = ?"
                + " AND (start_date <= ? AND end_date >= ?)"
                + " AND random_disp_flag = 0";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, prefId );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // ���R�[�h�����擾
                    if ( result.last() != false )
                    {
                        dataCount = result.getRow();
                        sponsorCode = new int[dataCount];
                    }

                    result.beforeFirst();
                    while( result.next() != false )
                    {
                        sponsorCode[count] = result.getInt( "sponsor_code" );
                        count++;
                    }
                }
            }
        }
        catch ( Exception e )
        {
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(sponsorCode);
    }

    /**
     * �G���A�L���p�X�|���T�[���}������
     * 
     * @param prefId �s���{��ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     * @throws Exception
     */
    public void insertSponsorData(int[] sponsorCode) throws Exception
    {
        boolean ret;
        DataSponsorData dsd;

        dsd = new DataSponsorData();
        try
        {
            for( int i = 0 ; i < sponsorCode.length ; i++ )
            {
                ret = dsd.getData( sponsorCode[i], Integer.parseInt( DateEdit.getDate( 2 ) ) );
                if ( ret == false )
                {
                    dsd.setSponsorCode( sponsorCode[i] );
                    dsd.setAddupDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dsd.insertData();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData_M2.insertSponsorData()] Exception=" + e.toString() );
        }
    }
}
