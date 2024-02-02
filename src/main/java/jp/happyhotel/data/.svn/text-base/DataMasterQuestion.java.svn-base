/*
 * @(#)DataMasterQuestion.java 1.00 2008/05/12 Copyright (C) ALMEX Inc. 2007 アンケートマスタ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * アンケートマスタ取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2008/05/12
 */
public class DataMasterQuestion implements Serializable
{

    private static final long serialVersionUID = 2672477239579866864L;

    private int               questionId;
    private String            title;
    private String            titleDetail;
    private int               dispFlag;
    private int               memberFlag;
    private int               ownerFlag;
    private int               id;
    private int               startDate;
    private int               endDate;
    private int               addDate;
    private int               addTime;
    private String            userId;
    private String            ownerHotelId;
    private int               ownerUserId;
    private int               realFlag;
    private int               backnumStartDate;
    private int               backnumEndDate;

    /**
     * データを初期化します。
     */
    public DataMasterQuestion()
    {
        questionId = 0;
        title = "";
        titleDetail = "";
        dispFlag = 0;
        memberFlag = 0;
        ownerFlag = 0;
        id = 0;
        startDate = 0;
        endDate = 0;
        addDate = 0;
        addTime = 0;
        userId = "";
        ownerHotelId = "";
        ownerUserId = 0;
        realFlag = 0;
        backnumStartDate = 0;
        backnumEndDate = 0;

    }

    // ゲッター
    public int getAddDate()
    {
        return addDate;
    }

    public int getAddTime()
    {
        return addTime;
    }

    public int getBackNumEndDate()
    {
        return backnumEndDate;
    }

    public int getBackNumStartDate()
    {
        return backnumStartDate;
    }

    public int getDispFlag()
    {
        return dispFlag;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public int getId()
    {
        return id;
    }

    public int getMemberFlag()
    {
        return memberFlag;
    }

    public int getOwnerFlag()
    {
        return ownerFlag;
    }

    public String getOwnerHotelId()
    {
        return ownerHotelId;
    }

    public int getOwnerUserId()
    {
        return ownerUserId;
    }

    public int getRealFlag()
    {
        return realFlag;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public String getTitle()
    {
        return title;
    }

    public String getTitleDetail()
    {
        return titleDetail;
    }

    public int getQuestionId()
    {
        return questionId;
    }

    public String getUserId()
    {
        return userId;
    }

    // セッター
    public void setAddDate(int addDate)
    {
        this.addDate = addDate;
    }

    public void setAddTime(int addTime)
    {
        this.addTime = addTime;
    }

    public void setBackNumEndDate(int backnumEndDate)
    {
        this.backnumEndDate = backnumEndDate;
    }

    public void setBackNumStartDate(int backnumStartDate)
    {
        this.backnumStartDate = backnumStartDate;
    }

    public void setDispFlag(int dispFlag)
    {
        this.dispFlag = dispFlag;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setMemberFlag(int memberFlag)
    {
        this.memberFlag = memberFlag;
    }

    public void setOwnerFlag(int ownerFlag)
    {
        this.ownerFlag = ownerFlag;
    }

    public void setOwnerHotelId(String ownerHotelId)
    {
        this.ownerHotelId = ownerHotelId;
    }

    public void setOwnerUserId(int ownerUserId)
    {
        this.ownerUserId = ownerUserId;
    }

    public void setRealFlag(int realFlag)
    {
        this.realFlag = realFlag;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setTilteDetail(String titleDetail)
    {
        this.titleDetail = titleDetail;
    }

    public void setQuestionId(int questionId)
    {
        this.questionId = questionId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * アンケートマスタデータ取得
     * 
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int seq)
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_question WHERE question_id= ?";

        count = 0;
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, questionId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.questionId = result.getInt( "question_id" );
                    this.title = result.getString( "title" );
                    this.titleDetail = result.getString( "title_detail" );
                    this.dispFlag = result.getInt( "disp_flag" );
                    this.memberFlag = result.getInt( "member_flag" );
                    this.ownerFlag = result.getInt( "owner_flag" );
                    this.id = result.getInt( "id" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    this.addDate = result.getInt( "add_date" );
                    this.addTime = result.getInt( "add_time" );
                    this.userId = result.getString( "user_id" );
                    this.ownerHotelId = result.getString( "owner_hotelid" );
                    this.ownerUserId = result.getInt( "owner_userid" );
                    this.realFlag = result.getInt( "real_flag" );
                    this.backnumStartDate = result.getInt( "backnum_start_date" );
                    this.backnumEndDate = result.getInt( "backnum_end_date" );
                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
            Logging.error( "[DataMasterPresent.getData] count=" + count );
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
     * アンケートマスタデータ設定
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
                this.questionId = result.getInt( "question_id" );
                this.title = result.getString( "title" );
                this.titleDetail = result.getString( "title_detail" );
                this.dispFlag = result.getInt( "disp_flag" );
                this.memberFlag = result.getInt( "member_flag" );
                this.ownerFlag = result.getInt( "owner_flag" );
                this.id = result.getInt( "id" );
                this.startDate = result.getInt( "start_date" );
                this.endDate = result.getInt( "end_date" );
                this.addDate = result.getInt( "add_date" );
                this.addTime = result.getInt( "add_time" );
                this.userId = result.getString( "user_id" );
                this.ownerHotelId = result.getString( "owner_hotelid" );
                this.ownerUserId = result.getInt( "owner_userid" );
                this.realFlag = result.getInt( "real_flag" );
                this.backnumStartDate = result.getInt( "backnum_start_date" );
                this.backnumEndDate = result.getInt( "backnum_end_date" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterPresent.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * アンケートマスタデータ設定
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

        query = "INSERT hh_master_question SET";
        query = query + " question_id = ?,";
        query = query + " title = ?,";
        query = query + " title_detail = ?,";
        query = query + " disp_flag = ?,";
        query = query + " member_flag = ?,";
        query = query + " owner_flag = ?,";
        query = query + " id = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " add_date = ?,";
        query = query + " add_time = ?, ";
        query = query + " user_id = ?, ";
        query = query + " owner_hotelid = ?,";
        query = query + " owner_userid = ?,";
        query = query + " real_flag = ?,";
        query = query + " backnum_start_date = ?,";
        query = query + " backnum_end_date = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.questionId );
            prestate.setString( 2, this.title );
            prestate.setString( 3, this.titleDetail );
            prestate.setInt( 4, this.dispFlag );
            prestate.setInt( 5, this.memberFlag );
            prestate.setInt( 6, this.ownerFlag );
            prestate.setInt( 7, this.id );
            prestate.setInt( 8, this.startDate );
            prestate.setInt( 9, this.endDate );
            prestate.setInt( 10, this.addDate );
            prestate.setInt( 11, this.addTime );
            prestate.setString( 12, this.userId );
            prestate.setString( 13, this.ownerHotelId );
            prestate.setInt( 14, this.ownerUserId );
            prestate.setInt( 15, this.realFlag );
            prestate.setInt( 16, this.backnumStartDate );
            prestate.setInt( 17, this.backnumEndDate );

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
     * アンケートマスタデータ設定
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int quetionId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "UPDATE hh_master_question SET";
        query = query + " title = ?,";
        query = query + " title_detail = ?,";
        query = query + " disp_flag = ?,";
        query = query + " member_flag = ?,";
        query = query + " owner_flag = ?,";
        query = query + " id = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " add_date = ?,";
        query = query + " add_time = ?, ";
        query = query + " user_id = ?, ";
        query = query + " owner_hotelid = ?,";
        query = query + " owner_userid = ?,";
        query = query + " real_flag = ?,";
        query = query + " backnum_start_date = ?,";
        query = query + " backnum_end_date = ?";
        query = query + " WHERE quetion_id = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, this.title );
            prestate.setString( 2, this.titleDetail );
            prestate.setInt( 3, this.dispFlag );
            prestate.setInt( 4, this.memberFlag );
            prestate.setInt( 5, this.ownerFlag );
            prestate.setInt( 6, this.id );
            prestate.setInt( 7, this.startDate );
            prestate.setInt( 8, this.endDate );
            prestate.setInt( 9, this.addDate );
            prestate.setInt( 10, addTime );
            prestate.setString( 11, this.userId );
            prestate.setString( 12, this.ownerHotelId );
            prestate.setInt( 13, this.ownerUserId );
            prestate.setInt( 14, this.realFlag );
            prestate.setInt( 15, this.backnumStartDate );
            prestate.setInt( 16, this.backnumEndDate );
            prestate.setInt( 17, quetionId );
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
}
