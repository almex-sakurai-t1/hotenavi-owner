/*
 * @(#)DataMasterPoint.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 ポイント管理マスタ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 賞品管理マスタ取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2007/01/29
 */
public class DataMasterPresent implements Serializable
{
    private static final long serialVersionUID = 6598782440011616906L;

    private String            title;
    private String            fileName;
    private int               seq;
    private int               category;
    private int               pointCode;
    private byte[]            presentPicturePc;
    private byte[]            presentPictureGif;
    private byte[]            presentPicturePng;
    private int               limitFrom;
    private int               limitTo;
    private String            memo;
    private int               electNumber;
    private int               dispFrom;
    private int               dispTo;
    private int               offerHotel;
    private int               localId;
    private int               prefId;
    private int               remainsNumber;
    private int               kindFlag;
    private byte[]            presentPicture;
    private byte[]            presentSamplePicture;
    private String            dispPos;

    /**
     * データを初期化します。
     */
    public DataMasterPresent()
    {
        title = "";
        fileName = "";
        seq = 0;
        category = 0;
        pointCode = 0;
        presentPicturePc = new byte[0];
        presentPictureGif = new byte[0];
        presentPicturePng = new byte[0];
        limitFrom = 0;
        limitTo = 0;
        memo = "";
        electNumber = 0;
        dispFrom = 0;
        dispTo = 0;
        offerHotel = 0;
        localId = 0;
        prefId = 0;
        remainsNumber = 0;
        kindFlag = 0;
        presentPicture = new byte[0];
        presentSamplePicture = new byte[0];
        dispPos = "";

    }

    public int getCategory()
    {
        return category;
    }

    public int getDispFrom()
    {
        return dispFrom;
    }

    public String getDispPos()
    {
        return dispPos;
    }

    public int getDispTo()
    {
        return dispTo;
    }

    public int getElectNumber()
    {
        return electNumber;
    }

    public String getFileName()
    {
        return fileName;
    }

    public int getKindFlag()
    {
        return kindFlag;
    }

    public int getLimitFrom()
    {
        return limitFrom;
    }

    public int getLimitTo()
    {
        return limitTo;
    }

    public int getLocalId()
    {
        return localId;
    }

    public String getMemo()
    {
        return memo;
    }

    public int getOfferHotel()
    {
        return offerHotel;
    }

    public int getPointCode()
    {
        return pointCode;
    }

    public int getPrefId()
    {
        return prefId;
    }

    public byte[] getPresentPicture()
    {
        return presentPicture;
    }

    public byte[] getPresentPicturePc()
    {
        return presentPicturePc;
    }

    public byte[] getPresentPictureGif()
    {
        return presentPictureGif;
    }

    public byte[] getPresentPicturePng()
    {
        return presentPicturePng;
    }

    public byte[] getPresentSamplePicture()
    {
        return presentSamplePicture;
    }

    public int getRemainsNumber()
    {
        return remainsNumber;
    }

    public int getSeq()
    {
        return seq;
    }

    public String getTitle()
    {
        return title;
    }

    public void setCategory(int category)
    {
        this.category = category;
    }

    public void setDispFrom(int dispFrom)
    {
        this.dispFrom = dispFrom;
    }

    public void setDispPos(String dispPos)
    {
        this.dispPos = dispPos;
    }

    public void setDispTo(int dispTo)
    {
        this.dispTo = dispTo;
    }

    public void setElectNumber(int electNumber)
    {
        this.electNumber = electNumber;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public void setKindFlag(int kindFlag)
    {
        this.kindFlag = kindFlag;
    }

    public void setLimitFrom(int limitFrom)
    {
        this.limitFrom = limitFrom;
    }

    public void setLimitTo(int limitTo)
    {
        this.limitTo = limitTo;
    }

    public void setLocalId(int localId)
    {
        this.localId = localId;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public void setOfferHotel(int offerHotel)
    {
        this.offerHotel = offerHotel;
    }

    public void setPrefId(int prefId)
    {
        this.prefId = prefId;
    }

    public void setPresentPicture(byte[] presentPicture)
    {
        this.presentPicture = presentPicture;
    }

    public void setPresentPicturePc(byte[] presentPicturePc)
    {
        this.presentPicturePc = presentPicturePc;
    }

    public void setPresentPictureGif(byte[] presentPictureGif)
    {
        this.presentPictureGif = presentPictureGif;
    }

    public void setPresentPicturePng(byte[] presentPicturePng)
    {
        this.presentPicturePng = presentPicturePng;
    }

    public void setPresentSamplePicture(byte[] presentSamplePicture)
    {
        this.presentSamplePicture = presentSamplePicture;
    }

    public void setPointCode(int pointCode)
    {
        this.pointCode = pointCode;
    }

    public void setRemainsNumber(int remainsNumber)
    {
        this.remainsNumber = remainsNumber;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * 賞品管理マスタデータ取得
     * 
     * @param code ポイントコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int seq)
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT SQL_NO_CACHE * FROM hh_master_present WHERE seq = ?";

        count = 0;
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.title = result.getString( "title" );
                    this.seq = result.getInt( "seq" );
                    this.category = result.getInt( "category" );
                    this.pointCode = result.getInt( "point_code" );
                    this.presentPicturePc = result.getBytes( "present_picture_pc" );
                    this.presentPictureGif = result.getBytes( "present_picture_gif" );
                    this.presentPicturePng = result.getBytes( "present_picture_png" );
                    this.fileName = result.getString( "file_name" );
                    this.limitFrom = result.getInt( "limit_from" );
                    this.limitTo = result.getInt( "limit_to" );
                    this.memo = result.getString( "memo" );
                    this.electNumber = result.getInt( "elect_number" );
                    this.dispFrom = result.getInt( "disp_from" );
                    this.dispTo = result.getInt( "disp_to" );
                    this.offerHotel = result.getInt( "offer_hotel" );
                    this.localId = result.getInt( "local_id" );
                    this.prefId = result.getInt( "pref_id" );
                    this.remainsNumber = result.getInt( "remains_number" );
                    this.kindFlag = result.getInt( "kind_flag" );
                    this.presentPicture = result.getBytes( "present_picture" );
                    this.presentSamplePicture = result.getBytes( "present_sample_picture" );
                    this.dispPos = result.getString( "disp_pos" );
                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterPresent.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( count > 0 )
            return(true);
        else
            return(false);
    }

    /**
     * 賞品管理マスタデータ設定
     * 
     * @param result 賞品管理マスタデータレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.title = result.getString( "title" );
                this.seq = result.getInt( "seq" );
                this.category = result.getInt( "category" );
                this.pointCode = result.getInt( "point_code" );
                this.presentPicturePc = result.getBytes( "present_picture_pc" );
                this.presentPictureGif = result.getBytes( "present_picture_gif" );
                this.presentPicturePng = result.getBytes( "present_picture_png" );
                this.fileName = result.getString( "file_name" );
                this.limitFrom = result.getInt( "limit_from" );
                this.limitTo = result.getInt( "limit_to" );
                this.memo = result.getString( "memo" );
                this.electNumber = result.getInt( "elect_number" );
                this.dispFrom = result.getInt( "disp_from" );
                this.dispTo = result.getInt( "disp_to" );
                this.offerHotel = result.getInt( "offer_hotel" );
                this.localId = result.getInt( "local_id" );
                this.prefId = result.getInt( "pref_id" );
                this.remainsNumber = result.getInt( "remains_number" );
                this.kindFlag = result.getInt( "kind_flag" );
                this.presentPicture = result.getBytes( "present_picture" );
                this.presentSamplePicture = result.getBytes( "present_sample_picture" );
                this.dispPos = result.getString( "disp_pos" );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterPresent.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * 賞品管理マスタデータ設定
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData()
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "INSERT hh_master_present SET";
        query = query + " seq = ?,";
        query = query + " title = ?,";
        query = query + " category = ?,";
        query = query + " point_code = ?,";
        query = query + " present_picture_pc = ?,";
        query = query + " present_picture_gif = ?,";
        query = query + " present_picture_png = ?,";
        query = query + " file_name = ?,";
        query = query + " limit_from = ?,";
        query = query + " limit_to = ?,";
        query = query + " memo = ?, ";
        query = query + " elect_number = ?, ";
        query = query + " disp_from = ?, ";
        query = query + " disp_to = ?, ";
        query = query + " offer_hotel = ?, ";
        query = query + " local_id = ?, ";
        query = query + " pref_id = ?, ";
        query = query + " remains_number = ?, ";
        query = query + " kind_flag = ?,";
        query = query + " present_picture = ?,";
        query = query + " present_sample_picture = ?,";
        query = query + " disp_pos = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.seq );
            prestate.setString( 2, this.title );
            prestate.setInt( 3, this.category );
            prestate.setInt( 4, this.pointCode );
            prestate.setBytes( 5, this.presentPicturePc );
            prestate.setBytes( 6, this.presentPictureGif );
            prestate.setBytes( 7, this.presentPicturePng );
            prestate.setString( 8, this.fileName );
            prestate.setInt( 9, this.limitFrom );
            prestate.setInt( 10, this.limitTo );
            prestate.setString( 11, this.memo );
            prestate.setInt( 12, this.electNumber );
            prestate.setInt( 13, this.dispFrom );
            prestate.setInt( 14, this.dispTo );
            prestate.setInt( 15, this.offerHotel );
            prestate.setInt( 16, this.localId );
            prestate.setInt( 17, this.prefId );
            prestate.setInt( 18, this.remainsNumber );
            prestate.setInt( 19, this.kindFlag );
            prestate.setBytes( 20, this.presentPicture );
            prestate.setBytes( 21, this.presentSamplePicture );
            prestate.setString( 22, this.dispPos );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterDecome.insertData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 賞品管理マスタデータ設定
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "UPDATE hh_master_present SET";
        query = query + " title = ?,";
        query = query + " category = ?,";
        query = query + " point_code = ?,";
        query = query + " present_picture_pc = ?,";
        query = query + " present_picture_gif = ?,";
        query = query + " present_picture_png = ?,";
        query = query + " file_name = ?,";
        query = query + " limit_from = ?,";
        query = query + " limit_to = ?,";
        query = query + " memo = ?,";
        query = query + " elect_number = ?,";
        query = query + " disp_from = ?, ";
        query = query + " disp_to = ?, ";
        query = query + " offer_hotel = ?, ";
        query = query + " local_id = ?, ";
        query = query + " pref_id = ?, ";
        query = query + " remains_number = ?, ";
        query = query + " kind_flag = ?,";
        query = query + " present_picture = ?,";
        query = query + " present_sample_picture = ?,";
        query = query + " disp_pos = ?";
        query = query + " WHERE seq = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, this.title );
            prestate.setInt( 2, this.category );
            prestate.setInt( 3, this.pointCode );
            prestate.setBytes( 4, this.presentPicturePc );
            prestate.setBytes( 5, this.presentPictureGif );
            prestate.setBytes( 6, this.presentPicturePng );
            prestate.setString( 7, this.fileName );
            prestate.setInt( 8, this.limitFrom );
            prestate.setInt( 9, this.limitTo );
            prestate.setString( 10, memo );
            prestate.setInt( 11, this.electNumber );
            prestate.setInt( 12, this.dispFrom );
            prestate.setInt( 13, this.dispTo );
            prestate.setInt( 14, this.offerHotel );
            prestate.setInt( 15, this.localId );
            prestate.setInt( 16, this.prefId );
            prestate.setInt( 17, this.remainsNumber );
            prestate.setInt( 18, this.kindFlag );
            prestate.setBytes( 19, this.presentPicture );
            prestate.setBytes( 20, this.presentSamplePicture );
            prestate.setString( 21, this.dispPos );
            prestate.setInt( 22, seq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterPresent.updateData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * キャンペーン応募用データ取得
     * 
     * @param カテゴリー
     * @param 当日
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public ArrayList<DataMasterPresent> getDataByCategory(int category, int today)
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int index = 1;
        ArrayList<DataMasterPresent> dmpList = new ArrayList<DataMasterPresent>();

        query = "SELECT SQL_NO_CACHE * FROM hh_master_present ";
        query += " WHERE category = ? ";
        query += " AND disp_from <= ? ";
        query += " AND disp_to >= ? ";

        count = 0;
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( index++, category );
            prestate.setInt( index++, today );
            prestate.setInt( index++, today );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() )
                {
                    DataMasterPresent dmp = new DataMasterPresent();
                    dmp.setData( result );
                    dmpList.add( dmp );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterPresent.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return dmpList;
    }

}
