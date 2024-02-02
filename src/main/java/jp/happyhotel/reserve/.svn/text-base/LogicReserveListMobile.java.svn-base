package jp.happyhotel.reserve;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 
 * 予約一覧／利用履歴画面 business Logic
 */
public class LogicReserveListMobile implements Serializable
{

    /**
     *
     */
    private static final long  serialVersionUID = 6190403947016211080L;
    /**
     *
     */
    // private static final long serialVersionUID = 5757546715775802830L;
    private static final int   listmax          = 3;                       // 一画面最大明細表示件数
    private int                intMaxCnt        = 0;
    private ArrayList<Integer> hotelIdList      = new ArrayList<Integer>();
    private ArrayList<String>  hotelNmList      = new ArrayList<String>();
    private ArrayList<String>  reserveNoList    = new ArrayList<String>();
    private ArrayList<Integer> reserveDateList  = new ArrayList<Integer>();
    private ArrayList<String>  reserveDtList    = new ArrayList<String>();

    /**
     * 
     * getData
     * 
     * 引数によって対象となるデータを分ける
     * 
     */
    public boolean getData(FormReserveListMobile frm, String mode, String userid, int pageCnt) throws Exception
    {
        boolean isResult;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String convDate = "";
        int stLine = 0;
        int enLine = 0;
        int i = 0;
        int intSt = 0;
        int intEn = 0;

        // 戻り値の初期化
        isResult = false;

        try
        {

            if ( mode.equals( "1" ) )
            {
                // 予約一覧の場合(ステータスが”１”受付のレコードが対象）
                query = "SELECT reserve_no, reserve_date, id, hotel_name " +
                        " FROM hh_rsv_reserve WHERE status = '1' AND user_id = ?" +
                        " UNION SELECT reserve_no, reserve_date, id, hotel_name " +
                        " FROM newRsvDB.hh_rsv_reserve WHERE status = '1' AND user_id = ?" +
                        " ORDER BY reserve_date, id, reserve_no";
            }
            else
            {
                // 利用履歴の場合(ステータスが”２”利用済みのレコードが対象）
                query = "SELECT reserve_no, reserve_date, id, hotel_name " +
                        " FROM hh_rsv_reserve WHERE status = '2' AND user_id = ?" +
                        " UNION SELECT reserve_no, reserve_date, id, hotel_name " +
                        " FROM newRsvDB.hh_rsv_reserve WHERE status = '2' AND user_id = ?" +
                        " ORDER BY reserve_date, id, reserve_no";
            }

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userid );
            prestate.setString( 2, userid );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // 全件抽出
                while( result.next() )
                {
                    this.setHotelIdList( result.getInt( "id" ) );
                    this.setHotelNmList( ConvertCharacterSet.convDb2Form( result.getString( "hotel_name" ) ) );
                    this.setReserveNoList( result.getString( "reserve_no" ) );
                    this.setReserveDateList( result.getInt( "reserve_date" ) );
                    convDate = String.valueOf( result.getInt( "reserve_date" ) );
                    this.setReserveDtList( convDate.substring( 0, 4 ) + "/" + convDate.substring( 4, 6 ) + "/" + convDate.substring( 6, 8 ) );

                    intMaxCnt++;
                }

                if ( intMaxCnt != 0 )
                {
                    if ( pageCnt == 0 )
                    {
                        stLine = 0;
                        if ( listmax > intMaxCnt )
                        {
                            enLine = intMaxCnt - 1;
                        }
                        else
                        {
                            enLine = listmax - 1;
                        }
                        frm.setPageAct( 1 );
                    }
                    else
                    {
                        stLine = (listmax * pageCnt) + 1;
                        enLine = (listmax * (pageCnt + 1));
                        frm.setPageAct( pageCnt - 1 );
                    }

                    if ( enLine > intMaxCnt )
                    {
                        enLine = intMaxCnt;
                    }
                    // 表示対象ページ分のみ
                    if ( stLine == 0 )
                    {
                        intSt = 0;
                        if ( listmax > intMaxCnt )
                        {
                            intEn = intMaxCnt - 1;
                        }
                        else
                        {
                            intEn = listmax - 1;
                        }
                        frm.setPageSt( stLine + 1 );
                        frm.setPageEd( enLine + 1 );
                    }
                    else
                    {
                        intSt = stLine - 1;
                        intEn = enLine - 1;
                        frm.setPageSt( stLine );
                        frm.setPageEd( enLine );
                    }
                    for( i = intSt ; i <= intEn ; i++ )
                    {
                        frm.setHotelIdList( this.hotelIdList.get( i ) );
                        frm.setHotelNmList( this.hotelNmList.get( i ) );
                        frm.setReserveDateList( this.reserveDateList.get( i ) );
                        frm.setReserveDtList( this.reserveDtList.get( i ) );
                        frm.setReserveNoList( this.reserveNoList.get( i ) );
                    }

                    isResult = true;
                }
                else
                {
                    isResult = false;
                }
            }
            else
            {
                // not Found
                isResult = false;
            }
            // 抽出件数
            frm.setPageMax( intMaxCnt );

            // 戻り値
            return isResult;
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveListMobile.getData] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * getter
     */
    public int getMaxCnt()
    {
        return this.intMaxCnt;
    }

    public ArrayList<Integer> getHotelIdList()
    {
        return this.hotelIdList;
    }

    public ArrayList<String> getHotelNmList()
    {
        return this.hotelNmList;
    }

    public ArrayList<String> getReserveNoList()
    {
        return this.reserveNoList;
    }

    public ArrayList<Integer> getReserveDateList()
    {
        return this.reserveDateList;
    }

    public ArrayList<String> getReserveDtList()
    {
        return this.reserveDtList;
    }

    /**
     * setter
     */
    public void setHotelIdList(int hotelid)
    {
        this.hotelIdList.add( hotelid );
    }

    public void setHotelNmList(String hotelnm)
    {
        this.hotelNmList.add( hotelnm );
    }

    public void setReserveNoList(String reserveno)
    {
        this.reserveNoList.add( reserveno );
    }

    public void setReserveDateList(int reservedate)
    {
        this.reserveDateList.add( reservedate );
    }

    public void setReserveDtList(String reservedt)
    {
        this.reserveDtList.add( reservedt );
    }

}
