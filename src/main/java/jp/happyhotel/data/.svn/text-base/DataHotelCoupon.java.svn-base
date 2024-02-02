/*
 * @(#)DataHotelCoupon.java 1.00 2007/07/17 Copyright (C) ALMEX Inc. 2007 ホテルクーポン情報データ取得クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;
import jp.happyhotel.common.*;

/**
 * ホテルクーポン情報データ取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/17
 * @version 1.1 2007/11/16
 * @version 1.2 2008/03/25
 */
public class DataHotelCoupon implements Serializable
{
    private static final long serialVersionUID = -4585645439526206154L;

    private int               id;
    private int               seq;
    private int               startDate;
    private int               endDate;
    private String            benefitText1;
    private String            benefitCondition1;
    private String            benefitText2;
    private String            benefitCondition2;
    private String            benefitText3;
    private String            benefitCondition3;
    private String            commonCondition;
    private int               period;
    private int               dispMobile;
    private int               delFlag;
    private int               ownerMail;
    private String            dispMobileMessage;
    private int               allSeq;
    private final int         COUPON_KIND      = 1;

    /**
     * データを初期化します。
     */
    public DataHotelCoupon()
    {
        id = 0;
        seq = 0;
        startDate = 0;
        endDate = 0;
        benefitText1 = "";
        benefitCondition1 = "";
        benefitText2 = "";
        benefitCondition2 = "";
        benefitText3 = "";
        benefitCondition3 = "";
        commonCondition = "";
        period = 0;
        dispMobile = 0;
        delFlag = 0;
        ownerMail = 0;
        dispMobileMessage = "";
        allSeq = 0;
    }

    public int getAllSeq()
    {
        return allSeq;
    }

    public String getBenefitCondition1()
    {
        return benefitCondition1;
    }

    public String getBenefitCondition2()
    {
        return benefitCondition2;
    }

    public String getBenefitCondition3()
    {
        return benefitCondition3;
    }

    public String getBenefitText1()
    {
        return benefitText1;
    }

    public String getBenefitText2()
    {
        return benefitText2;
    }

    public String getBenefitText3()
    {
        return benefitText3;
    }

    public String getCommonCondition()
    {
        return commonCondition;
    }

    public int getDelFlag()
    {
        return delFlag;
    }

    public int getDispMobile()
    {
        return dispMobile;
    }

    public String getDispMobileMessage()
    {
        return dispMobileMessage;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public int getId()
    {
        return id;
    }

    public int getOwnerMail()
    {
        return ownerMail;
    }

    public int getPeriod()
    {
        return period;
    }

    public int getSeq()
    {
        return seq;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public void setAllSeq(int allSeq)
    {
        this.allSeq = allSeq;
    }

    public void setBenefitCondition1(String benefitCondition1)
    {
        this.benefitCondition1 = benefitCondition1;
    }

    public void setBenefitCondition2(String benefitCondition2)
    {
        this.benefitCondition2 = benefitCondition2;
    }

    public void setBenefitCondition3(String benefitCondition3)
    {
        this.benefitCondition3 = benefitCondition3;
    }

    public void setBenefitText1(String benefitText1)
    {
        this.benefitText1 = benefitText1;
    }

    public void setBenefitText2(String benefitText2)
    {
        this.benefitText2 = benefitText2;
    }

    public void setBenefitText3(String benefitText3)
    {
        this.benefitText3 = benefitText3;
    }

    public void setCommonCondition(String commonCondition)
    {
        this.commonCondition = commonCondition;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    public void setDispMobile(int dispMobile)
    {
        this.dispMobile = dispMobile;
    }

    public void setDispMobileMessage(String dispMobileMessage)
    {
        this.dispMobileMessage = dispMobileMessage;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setOwnerMail(int ownerMail)
    {
        this.ownerMail = ownerMail;
    }

    public void setPeriod(int period)
    {
        this.period = period;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    /**
     * ホテルクーポン情報データ取得
     * 
     * @param hotelId ホテルコード
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int hotelId, int seq)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_coupon WHERE id = ? AND seq = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.seq = result.getInt( "seq" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    this.benefitText1 = result.getString( "benefit_text1" );
                    this.benefitCondition1 = result.getString( "benefit_condition1" );
                    this.benefitText2 = result.getString( "benefit_text2" );
                    this.benefitCondition2 = result.getString( "benefit_condition2" );
                    this.benefitText3 = result.getString( "benefit_text3" );
                    this.benefitCondition3 = result.getString( "benefit_condition3" );
                    this.commonCondition = result.getString( "common_condition" );
                    this.period = result.getInt( "period" );
                    this.dispMobile = result.getInt( "disp_mobile" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.ownerMail = result.getInt( "owner_mail" );
                    this.dispMobileMessage = result.getString( "disp_mobile_message" );
                    this.allSeq = result.getInt( "all_seq" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelCoupon.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ホテルクーポン情報データ設定
     * 
     * @param result ホテルクーポン情報データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.id = result.getInt( "id" );
                this.seq = result.getInt( "seq" );
                this.startDate = result.getInt( "start_date" );
                this.endDate = result.getInt( "end_date" );
                this.benefitText1 = result.getString( "benefit_text1" );
                this.benefitCondition1 = result.getString( "benefit_condition1" );
                this.benefitText2 = result.getString( "benefit_text2" );
                this.benefitCondition2 = result.getString( "benefit_condition2" );
                this.benefitText3 = result.getString( "benefit_text3" );
                this.benefitCondition3 = result.getString( "benefit_condition3" );
                this.commonCondition = result.getString( "common_condition" );
                this.period = result.getInt( "period" );
                this.dispMobile = result.getInt( "disp_mobile" );
                this.delFlag = result.getInt( "del_flag" );
                this.ownerMail = result.getInt( "owner_mail" );
                this.dispMobileMessage = result.getString( "disp_mobile_message" );
                this.allSeq = result.getInt( "all_seq" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelCoupon.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * ホテルクーポン情報データ追加
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

        ret = false;

        query = "INSERT hh_hotel_coupon SET ";
        query = query + " id = ?,";
        query = query + " seq = 0,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " benefit_text1 = ?,";
        query = query + " benefit_condition1 = ?,";
        query = query + " benefit_text2 = ?,";
        query = query + " benefit_condition2 = ?,";
        query = query + " benefit_text3 = ?,";
        query = query + " benefit_condition3 = ?,";
        query = query + " common_condition = ?,";
        query = query + " period = ?,";
        query = query + " disp_mobile = ?,";
        query = query + " del_flag = ?,";
        query = query + " owner_mail = ?,";
        query = query + " disp_mobile_message = ?,";
        query = query + " all_seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.startDate );
            prestate.setInt( 3, this.endDate );
            prestate.setString( 4, this.benefitText1 );
            prestate.setString( 5, this.benefitCondition1 );
            prestate.setString( 6, this.benefitText2 );
            prestate.setString( 7, this.benefitCondition2 );
            prestate.setString( 8, this.benefitText3 );
            prestate.setString( 9, this.benefitCondition3 );
            prestate.setString( 10, this.commonCondition );
            prestate.setInt( 11, this.period );
            prestate.setInt( 12, this.dispMobile );
            prestate.setInt( 13, this.delFlag );
            prestate.setInt( 14, this.ownerMail );
            prestate.setString( 15, this.dispMobileMessage );
            prestate.setInt( 16, this.allSeq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelCoupon.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * ホテルクーポン情報データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ホテルID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int id, int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_hotel_coupon SET ";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " benefit_text1 = ?,";
        query = query + " benefit_condition1 = ?,";
        query = query + " benefit_text2 = ?,";
        query = query + " benefit_condition2 = ?,";
        query = query + " benefit_text3 = ?,";
        query = query + " benefit_condition3 = ?,";
        query = query + " common_condition = ?,";
        query = query + " period = ?,";
        query = query + " disp_mobile = ?,";
        query = query + " del_flag = ?,";
        query = query + " owner_mail = ?,";
        query = query + " disp_mobile_message = ?,";
        query = query + " all_seq = ?";
        query = query + " WHERE id = ? AND seq = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.startDate );
            prestate.setInt( 2, this.endDate );
            prestate.setString( 3, this.benefitText1 );
            prestate.setString( 4, this.benefitCondition1 );
            prestate.setString( 5, this.benefitText2 );
            prestate.setString( 6, this.benefitCondition2 );
            prestate.setString( 7, this.benefitText3 );
            prestate.setString( 8, this.benefitCondition3 );
            prestate.setString( 9, this.commonCondition );
            prestate.setInt( 10, this.period );
            prestate.setInt( 11, this.dispMobile );
            prestate.setInt( 12, this.delFlag );
            prestate.setInt( 13, this.ownerMail );
            prestate.setString( 14, this.dispMobileMessage );
            prestate.setInt( 15, this.allSeq );
            prestate.setInt( 16, id );
            prestate.setInt( 17, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelCoupon.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * クーポン連番データ登録設定
     * 
     * @param id ホテルID
     * @see hh_hotel_system_seqにホテルIDでデータを登録し、最新のseqを取得する
     * @return 処理結果(0以上：正常,-1:異常)
     */
    public int getCouponSeqData(int id)
    {
        boolean ret;
        DataHotelSystemSeq dhss;
        dhss = new DataHotelSystemSeq();

        ret = false;

        try
        {
            if ( id < 0 )
                return(-1);
            dhss.setKind( COUPON_KIND );
            dhss.setId( id );
            dhss.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dhss.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            ret = dhss.insertData();

            if ( ret != false )
            {
                ret = dhss.getLatestData( COUPON_KIND );
                if ( ret != false )
                {
                    return(dhss.getSeq());
                }
                else
                    return(-1);
            }
            else
            {
                return(-1);
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelCoupon.getCouponSeqData] Exception=" + e.toString() );
            return(-1);
        }
    }
}
