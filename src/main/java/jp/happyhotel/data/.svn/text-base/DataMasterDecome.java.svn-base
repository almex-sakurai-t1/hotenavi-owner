/*
 * @(#)DataMasterPoint.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 ポイント管理マスタ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * デコメ管理マスタ取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2007/01/29
 */
public class DataMasterDecome implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 2315573336543031033L;

    private String            title;
    private String            fileName;
    private int               seq;
    private int               category;
    private int               pointCode;
    private byte[]            decomePicture;
    private byte[]            decomeSamplePicture;
    private int               classCode;
    private int               payFlag;
    private int               startDate;
    private int               endDate;
    private int               dispPos;
    private int               pictureKind;
    private byte[]            pictureDocomo;
    private byte[]            pictureAu;
    private byte[]            pictureSoftbank;
    private String            objectDocomo;
    private String            objectAu;
    private String            objectSoftbank;

    /**
     * データを初期化します。
     */
    public DataMasterDecome()
    {
        title = "";
        fileName = "";
        seq = 0;
        category = 0;
        pointCode = 0;
        decomePicture = new byte[0];
        decomeSamplePicture = new byte[0];
        classCode = 0;
        payFlag = 0;
        startDate = 0;
        endDate = 0;
        dispPos = 0;
        pictureKind = 0;
        pictureDocomo = new byte[0];
        pictureAu = new byte[0];
        pictureSoftbank = new byte[0];
        objectDocomo = "";
        objectAu = "";
        objectSoftbank = "";
    }

    /* getter */
    public int getCategory()
    {
        return category;
    }

    public int getClassCode()
    {
        return classCode;
    }

    public byte[] getDecomePicture()
    {
        return decomePicture;
    }

    public byte[] getDecomeSamplePicture()
    {
        return decomeSamplePicture;
    }

    public int getDispPos()
    {
        return dispPos;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public String getFileName()
    {
        return fileName;
    }

    public String getObjectAu()
    {
        return objectAu;
    }

    public String getObjectDocomo()
    {
        return objectDocomo;
    }

    public String getObjectSoftbank()
    {
        return objectSoftbank;
    }

    public int getPayFlag()
    {
        return payFlag;
    }

    public byte[] getPictureAu()
    {
        return pictureAu;
    }

    public byte[] getPictureDocomo()
    {
        return pictureDocomo;
    }

    public int getPictureKind()
    {
        return pictureKind;
    }

    public byte[] getPictureSoftbank()
    {
        return pictureSoftbank;
    }

    public int getPointCode()
    {
        return pointCode;
    }

    public int getSeq()
    {
        return seq;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public String getTitle()
    {
        return title;
    }

    /* setter */
    public void setCategory(int category)
    {
        this.category = category;
    }

    public void setClassCode(int classCode)
    {
        this.classCode = classCode;
    }

    public void setDecomePicture(byte[] decomePicture)
    {
        this.decomePicture = decomePicture;
    }

    public void setDecomeSamplePicture(byte[] decomeSamplePicture)
    {
        this.decomeSamplePicture = decomeSamplePicture;
    }

    public void setDispPos(int dispPos)
    {
        this.dispPos = dispPos;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public void setObjectDocomo(String objectDocomo)
    {
        this.objectDocomo = objectDocomo;
    }

    public void setObjectAu(String objectAu)
    {
        this.objectAu = objectAu;
    }

    public void setObjectSoftbank(String objectSoftbank)
    {
        this.objectSoftbank = objectSoftbank;
    }

    public void setPayFlag(int payFlag)
    {
        this.payFlag = payFlag;
    }

    public void setPictureAu(byte[] pictureAu)
    {
        this.pictureAu = pictureAu;
    }

    public void setPictureDocomo(byte[] pictureDocomo)
    {
        this.pictureDocomo = pictureDocomo;
    }

    public void setPictureKind(int pictureKind)
    {
        this.pictureKind = pictureKind;
    }

    public void setPictureSoftbank(byte[] pictureSoftbank)
    {
        this.pictureSoftbank = pictureSoftbank;
    }

    public void setPointCode(int pointCode)
    {
        this.pointCode = pointCode;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * デコメ管理マスタデータ取得
     * 
     * @param code ポイントコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int seq)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_decome WHERE seq = ?";

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
                    this.decomePicture = result.getBytes( "decome_picture" );
                    this.decomeSamplePicture = result.getBytes( "decome_sample_picture" );
                    this.fileName = result.getString( "file_name" );
                    this.classCode = result.getInt( "class" );
                    this.payFlag = result.getInt( "pay_flag" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    this.dispPos = result.getInt( "disp_pos" );
                    this.pictureKind = result.getInt( "picture_kind" );
                    this.pictureDocomo = result.getBytes( "picture_docomo" );
                    this.pictureAu = result.getBytes( "picture_au" );
                    this.pictureSoftbank = result.getBytes( "picture_softbank" );
                    this.objectDocomo = result.getString( "object_docomo" );
                    this.objectAu = result.getString( "object_au" );
                    this.objectSoftbank = result.getString( "object_softbank" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterDecome.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * デコメ管理マスタデータ設定
     * 
     * @param result デコメ管理マスタデータレコード
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
                this.decomePicture = result.getBytes( "decome_picture" );
                this.decomeSamplePicture = result.getBytes( "decome_sample_picture" );
                this.fileName = result.getString( "file_name" );
                this.classCode = result.getInt( "class" );
                this.payFlag = result.getInt( "pay_flag" );
                this.startDate = result.getInt( "start_date" );
                this.endDate = result.getInt( "end_date" );
                this.dispPos = result.getInt( "disp_pos" );
                this.pictureKind = result.getInt( "picture_kind" );
                this.pictureDocomo = result.getBytes( "picture_docomo" );
                this.pictureAu = result.getBytes( "picture_au" );
                this.pictureSoftbank = result.getBytes( "picture_softbank" );
                this.objectDocomo = result.getString( "object_docomo" );
                this.objectAu = result.getString( "object_au" );
                this.objectSoftbank = result.getString( "object_softbank" );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterDecome.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * ポイント管理マスタデータ設定
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

        query = "INSERT hh_master_decome SET";
        query = query + " seq = ?,";
        query = query + " title = ?,";
        query = query + " category = ?,";
        query = query + " point_code = ?,";
        query = query + " decome_picture = ?,";
        query = query + " decome_sample_picture = ?,";
        query = query + " file_name = ?, ";
        query = query + " class = ?, ";
        query = query + " pay_flag = ?, ";
        query = query + " start_date = ?, ";
        query = query + " end_date = ?, ";
        query = query + " disp_pos = ?, ";
        query = query + " picture_kind = ?, ";
        query = query + " picture_docomo = ?, ";
        query = query + " picture_au = ?, ";
        query = query + " picture_softbank = ?, ";
        query = query + " object_docomo = ?, ";
        query = query + " object_au = ?, ";
        query = query + " object_softbank = ? ";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.seq );
            prestate.setString( 2, this.title );
            prestate.setInt( 3, this.category );
            prestate.setInt( 4, this.pointCode );
            prestate.setBytes( 5, this.decomePicture );
            prestate.setBytes( 6, this.decomeSamplePicture );
            prestate.setString( 7, this.fileName );
            prestate.setInt( 8, this.classCode );
            prestate.setInt( 9, this.payFlag );
            prestate.setInt( 10, this.startDate );
            prestate.setInt( 11, this.endDate );
            prestate.setInt( 12, this.dispPos );
            prestate.setInt( 13, this.pictureKind );
            prestate.setBytes( 14, this.pictureDocomo );
            prestate.setBytes( 15, this.pictureAu );
            prestate.setBytes( 16, this.pictureSoftbank );
            prestate.setString( 17, this.objectDocomo );
            prestate.setString( 18, this.objectAu );
            prestate.setString( 19, this.objectSoftbank );
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
     * ポイント管理マスタデータ設定
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

        query = "UPDATE hh_master_decome SET";
        query = query + " title = ?,";
        query = query + " category = ?,";
        query = query + " point_code = ?,";
        query = query + " decome_picture = ?,";
        query = query + " decome_sample_picture = ?,";
        query = query + " file_name = ?,";
        query = query + " class = ?,";
        query = query + " pay_flag = ?, ";
        query = query + " start_date = ?, ";
        query = query + " end_date = ?, ";
        query = query + " disp_pos = ?, ";
        query = query + " picture_kind = ?, ";
        query = query + " picture_docomo = ?, ";
        query = query + " picture_au = ?, ";
        query = query + " picture_softbank = ?, ";
        query = query + " object_docomo = ?, ";
        query = query + " object_au = ?, ";
        query = query + " object_softbank = ? ";
        query = query + " WHERE seq = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, this.title );
            prestate.setInt( 2, this.category );
            prestate.setInt( 3, this.pointCode );
            prestate.setBytes( 4, this.decomePicture );
            prestate.setBytes( 5, this.decomeSamplePicture );
            prestate.setString( 6, this.fileName );
            prestate.setInt( 7, this.classCode );
            prestate.setInt( 8, this.payFlag );
            prestate.setInt( 9, this.startDate );
            prestate.setInt( 10, this.endDate );
            prestate.setInt( 11, this.dispPos );
            prestate.setInt( 12, this.pictureKind );
            prestate.setBytes( 13, this.pictureDocomo );
            prestate.setBytes( 14, this.pictureAu );
            prestate.setBytes( 15, this.pictureSoftbank );
            prestate.setString( 16, this.objectDocomo );
            prestate.setString( 17, this.objectAu );
            prestate.setString( 18, this.objectSoftbank );
            prestate.setInt( 19, seq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterDecome.updateData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * オブジェクトタブ取得
     * 
     * @param carrier キャリア（0:DoCoMo,1:au,2:Softbank）
     * @see getDataの後に使用すること
     * @return 処理結果(空白以外:オブジェクトタブ取得、空白:取得失敗)
     */
    public String getObjectData(int carrier)
    {
        String objectTag;

        objectTag = "";
        if ( carrier == DataMasterUseragent.CARRIER_DOCOMO )
        {
            objectTag = this.objectDocomo;
        }
        else if ( carrier == DataMasterUseragent.CARRIER_AU )
        {
            objectTag = this.objectAu;
        }
        else if ( carrier == DataMasterUseragent.CARRIER_SOFTBANK )
        {
            objectTag = this.objectSoftbank;
        }
        return(objectTag);

    }
}
