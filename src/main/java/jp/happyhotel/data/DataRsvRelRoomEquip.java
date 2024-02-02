/*
 * 部屋・設備設定データ
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/*
 * ここにimportするクラスを追加
 */

public class DataRsvRelRoomEquip implements Serializable
{

    private static final long  serialVersionUID = 8920093660427422113L;

    private int                iD;
    private int                seq;
    private int                equipId;
    private ArrayList<Integer> idList;
    private ArrayList<Integer> seqList;
    private ArrayList<Integer> equipIdList;

    /**
     * データの初期化
     */
    public DataRsvRelRoomEquip()
    {
        iD = 0;
        seq = 0;
        equipId = 0;
        setIdList( new ArrayList<Integer>() );
        setSeqList( new ArrayList<Integer>() );
        setEquipIdList( new ArrayList<Integer>() );
    }

    // getter
    public int getID()
    {
        return this.iD;
    }

    public int getSeq()
    {
        return this.seq;
    }

    public int getEquipId()
    {
        return this.equipId;
    }

    public void setEquipIdList(ArrayList<Integer> equipIdList)
    {
        this.equipIdList = equipIdList;
    }
    public ArrayList<Integer> getIdList()
    {
        return idList;
    }

    public ArrayList<Integer> getSeqList()
    {
        return seqList;
    }

    /**
     *
     * setter
     *
     */
    public void setId(int iD)
    {
        this.iD = iD;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setEquipId(int equipId)
    {
        this.equipId = equipId;
    }

    public ArrayList<Integer> getEquipIdList()
    {
        return equipIdList;
    }

    public void setIdList(ArrayList<Integer> idList)
    {
        this.idList = idList;
    }

    public void setSeqList(ArrayList<Integer> seqList)
    {
        this.seqList = seqList;
    }

    /**
     * 部屋・設備設定データ情報取得
     *
     * @param iD ホテルID
     * @param seq 管理番号
     * @param equipId 設備ID
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getData(int Id, int seq, int equipId)
    {
        // 変数定義
        boolean ret; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT id, seq, equip_id " +
                " FROM hh_rsv_rel_room_equip WHERE id = ? AND seq = ? AND equip_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Id );
            prestate.setInt( 2, seq );
            prestate.setInt( 3, equipId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.iD = result.getInt( "id" );
                    this.seq = result.getInt( "seq" );
                    this.equipId = result.getInt( "equip_id" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvRelRoomEquip.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 設備IDをリストで取得する。
     *
     * @param iD ホテルID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getData(int Id, int seq)
    {
        // 変数定義
        boolean ret = false;
        ArrayList<Integer> idList = new ArrayList<Integer>();
        ArrayList<Integer> seqList = new ArrayList<Integer>();
        ArrayList<Integer> eqIdList = new ArrayList<Integer>();
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        query = "SELECT id, seq, equip_id " +
                " FROM hh_rsv_rel_room_equip WHERE id = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Id );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                while( result.next() )
                {
                    idList.add(  result.getInt( "id" ) );
                    seqList.add(  result.getInt( "seq" ) );
                    eqIdList.add( result.getInt( "equip_id" ) );
                }

                this.idList = idList;
                this.seqList = seqList;
                this.equipIdList = eqIdList;
            }
            ret = true;
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvRelRoomEquip.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

}
