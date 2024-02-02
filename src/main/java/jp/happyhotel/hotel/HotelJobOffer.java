/*
 * @(#)HotelJobOffer.java 1.00 2009/03/16 Copyright (C) ALMEX Inc. 2009 �z�e�����l�f�[�^�擾�N���X
 */
package jp.happyhotel.hotel;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;
import jp.happyhotel.data.DataMasterSponsor;
import jp.happyhotel.data.DataHotelJobOffer;

/**
 * �z�e�����l�L���f�[�^�擾�N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2009/03/16
 */
public class HotelJobOffer implements Serializable
{
    /**
     *
     */
    private static final long   serialVersionUID   = 8748004432656209320L;

    private int                 sponsorCount;
    private DataMasterSponsor   sponsor;
    private int                 hotelJobOfferCount;
    private DataHotelJobOffer[] hotelJobOffer;

    /** �[����ʁFDoCoMo **/
    public static final int     USERAGENT_DOCOMO   = 1;
    /** �[����ʁFau **/
    public static final int     USERAGENT_AU       = 2;
    /** �[����ʁFJ-PHONE,Vodafone **/
    public static final int     USERAGENT_JPHONE   = 3;
    /** �[����ʁFJ-PHONE,Vodafone **/
    public static final int     USERAGENT_VODAFONE = 3;
    /** �[����ʁFJ-PHONE,Vodafone,SoftBank **/
    public static final int     USERAGENT_SOFTBANK = 3;
    /** �[����ʁFpc **/
    public static final int     USERAGENT_PC       = 4;

    public HotelJobOffer()
    {
        sponsorCount = 0;
        hotelJobOfferCount = 0;
    }

    public DataMasterSponsor getSponsor()
    {
        return sponsor;
    }

    public int getSponsorCount()
    {
        return sponsorCount;
    }

    public DataHotelJobOffer[] getHotelJobOffer()
    {
        return hotelJobOffer;
    }

    public int getHotelJobOfferCount()
    {
        return hotelJobOfferCount;
    }

    /**
     * �z�e�����l���擾
     * 
     * @param sponsorCode �X�|���T�[�R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     * @throws Exception
     */
    public boolean getHotelJobOffer(int sponsorCode) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        this.sponsor = new DataMasterSponsor();
        ret = this.sponsor.getData( sponsorCode );
        if ( ret != false )
        {
            this.sponsorCount = 1;
        }
        else
        {
            this.sponsorCount = 0;
        }

        // �\���Ώۂ̂���
        query = "SELECT * from hh_hotel_joboffer WHERE sponsor_code = ?"
                + " AND start_date <= ? AND end_date >= ? "
                + " AND del_flag = 0"
                + " ORDER BY seq";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, sponsorCode );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            // Gets Sponsor Data
            ret = getHotekJobOfferSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelJobOffer.getHotekJobOffer( int sponsorCode = " + sponsorCode + " )] Exception=" + e.toString() );
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
     * �z�e�����l���擾
     * 
     * @param sponsorCode �X�|���T�[�R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     * @throws Exception
     */
    public boolean getHotelJobOffer(int sponsorCode, int date) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        this.sponsor = new DataMasterSponsor();
        ret = this.sponsor.getData( sponsorCode );
        if ( ret != false )
        {
            this.sponsorCount = 1;
        }
        else
        {
            this.sponsorCount = 0;
        }

        // �\���Ώۂ̂���
        query = "SELECT * from hh_hotel_joboffer WHERE sponsor_code = ?"
                + " AND start_date <= ? AND end_date >= ? "
                + " AND del_flag = 0"
                + " ORDER BY seq";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, sponsorCode );
            prestate.setInt( 2, date );
            prestate.setInt( 3, date );
            // Gets Sponsor Data
            ret = getHotekJobOfferSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelJobOffer.getHotekJobOffer( int sponsorCode = " + sponsorCode + " )] Exception=" + e.toString() );
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
    private boolean getHotekJobOfferSub(PreparedStatement prestate) throws Exception
    {
        int i;
        int count = 0;
        boolean ret = false;
        ResultSet result = null;

        this.hotelJobOfferCount = 0;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    this.hotelJobOfferCount = result.getRow();
                }

                // �N���X�̔z���p�ӂ��A����������B
                this.hotelJobOffer = new DataHotelJobOffer[this.hotelJobOfferCount];
                for( i = 0 ; i < hotelJobOfferCount ; i++ )
                {
                    hotelJobOffer[i] = new DataHotelJobOffer();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // �X�|���T�[���̐ݒ�
                    this.hotelJobOffer[count].setData( result );

                    count++;
                }
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[HotelJobOffer.getHotelJobOfferSub(prestate)] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result );
        }
        if ( hotelJobOfferCount > 0 )
            ret = true;
        else
            ret = false;

        return(ret);
    }
}
