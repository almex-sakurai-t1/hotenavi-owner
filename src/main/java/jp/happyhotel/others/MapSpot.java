/*
 * @(#)MasterQuestionData.java 1.00 2008/05/14 Copyright (C) ALMEX Inc. 2007 �A���P�[�g����擾�N���X
 */

package jp.happyhotel.others;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMapSpot;
import jp.happyhotel.data.DataMasterSpot;
import jp.happyhotel.search.SearchEngineBasic;

/**
 * �n�}�X�|�b�g�擾�N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2010/04/28
 */
public class MapSpot implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -3308342896691422306L;
    int                       spotCount;
    int                       spotAllCount;
    int[]                     localIdList;
    int[]                     prefIdList;
    int[]                     spotIdList;
    DataMasterSpot            masterSpot;
    DataMapSpot[]             mapSpot;

    /**
     * �f�[�^�����������܂��B
     */
    public MapSpot()
    {
        spotCount = 0;
        spotAllCount = 0;
        localIdList = null;
        prefIdList = null;
        spotIdList = null;
    }

    /** �X�|�b�g�����擾 **/
    public int getCount()
    {
        return(spotCount);
    }

    /** �X�|�b�g�����擾 **/
    public int getAllCount()
    {
        return(spotAllCount);
    }

    /** �X�|�b�g�}�X�^�擾 **/
    public DataMasterSpot getMasterSpot()
    {
        return(masterSpot);
    }

    /** �n�}�X�|�b�g�擾 **/
    public DataMapSpot[] getMapSpotInfo()
    {
        return(mapSpot);
    }

    public int[] getLocalIdList()
    {
        return(localIdList);
    }

    public int[] getPrefIdList()
    {
        return(prefIdList);
    }

    public int[] getSpotIdList()
    {
        return(spotIdList);
    }

    /**
     * �n�}�X�|�b�g���擾����
     * 
     * @param spotId �X�|�b�gID
     * @param prefId �s���{��ID(0:�S�s���{���擾)
     * @param recommendFlag �������߃t���O(1:�������߂̂ݎ擾)
     * @param countNum �擾����
     * @param pageNum �y�[�W�ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getMapSpot(int spotId, int prefId, int recommendFlag, int countNum, int pageNum)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( spotId < 0 )
        {
            return(false);
        }

        /* �C�ӂ̃y�[�W�A�C�ӂ̌������擾����SQL */
        query = "SELECT * FROM hh_map_spot, hh_master_spot";
        query = query + " WHERE hh_map_spot.spot_id = hh_master_spot.spot_id";
        query = query + " AND hh_map_spot.spot_id = ?";
        // �s���{���̎w�肪����ꍇ
        if ( prefId > 0 )
        {
            query = query + " AND hh_map_spot.pref_id = ?";
        }
        // �������ߕ\��
        if ( recommendFlag > 0 )
        {
            query = query + " AND hh_map_spot.recommend_flag >= 1";
        }
        query = query + " AND hh_map_spot.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_map_spot.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_spot.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_spot.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        // �������ߕ\���̏ꍇ��recommend_flag�Ń\�[�g
        if ( recommendFlag > 0 )
        {
            query = query + " ORDER BY hh_map_spot.recommend_flag";
        }
        else
        {
            query = query + " ORDER BY hh_map_spot.disp_index";
        }
        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, spotId );
            if ( prefId > 0 )
            {
                prestate.setInt( 2, prefId );
            }
            ret = getMapSpotSub( prestate );
            if ( ret != false )
            {
                this.masterSpot = new DataMasterSpot();
                this.masterSpot.getDataByLimit( spotId );
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.info( "[MapSpot.getMapSpot()] Exception=" + e.toString() );
        }
        finally
        {
            prestate = null;
        }

        /* �S�������擾����SQL */
        query = "SELECT * FROM hh_map_spot, hh_master_spot";
        query = query + " WHERE hh_map_spot.spot_id = hh_master_spot.spot_id";
        query = query + " AND hh_map_spot.spot_id = ?";
        // �s���{���̎w�肪����ꍇ
        if ( prefId > 0 )
        {
            query = query + " AND hh_map_spot.pref_id = ?";
        }
        // �������߂̂ݕ\������
        if ( recommendFlag > 0 )
        {
            query = query + " AND hh_map_spot.recommend_flag >= 1";
        }
        query = query + " AND hh_map_spot.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_map_spot.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_spot.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_spot.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, spotId );
            if ( prefId > 0 )
            {
                prestate.setInt( 2, prefId );
            }
            // �S�������擾
            ret = getMapSpotCountSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "Exception in getHotelList =" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �n�}�X�|�b�g�̃f�[�^���Z�b�g
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getMapSpotSub(PreparedStatement prestate)
    {
        int count;
        ResultSet result = null;
        try
        {
            result = prestate.executeQuery();
            count = 0;

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    this.spotCount = result.getRow();
                }
                this.mapSpot = new DataMapSpot[this.spotCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    this.mapSpot[count] = new DataMapSpot();
                    this.mapSpot[count].setData( result );
                    count++;
                }
            }
            else
            {
                this.spotCount = 0;
            }
            Logging.info( "count:" + this.spotCount );
        }
        catch ( Exception e )
        {
            Logging.info( "[MapSpot.getMapSpotSub()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result );
        }
        return(true);
    }

    /**
     * �n�}�X�|�b�g�̑S�������Z�b�g
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getMapSpotCountSub(PreparedStatement prestate)
    {
        ResultSet result = null;
        try
        {
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    this.spotAllCount = result.getRow();
                }
            }
            else
            {
                this.spotAllCount = 0;
            }
            Logging.info( "count:" + this.spotCount );
        }
        catch ( Exception e )
        {
            Logging.info( "[MapSpot.getMapSpotCountSub()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result );
        }
        return(true);
    }

    /**
     * �n�}�X�|�b�g���擾����
     * 
     * @param spotId �X�|�b�gID
     * @param seq �Ǘ��ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getMapSpotBySeq(int spotId, int seq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( spotId < 0 )
        {
            return(false);
        }

        query = "SELECT * FROM hh_map_spot, hh_master_spot";
        query = query + " WHERE hh_map_spot.spot_id = hh_master_spot.spot_id";
        query = query + " AND hh_map_spot.spot_id = ?";
        query = query + " AND hh_map_spot.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_map_spot.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_spot.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_spot.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_map_spot.seq = ?";
        query = query + " ORDER BY hh_map_spot.disp_index";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, spotId );
            prestate.setInt( 2, seq );
            ret = getMapSpotSub( prestate );
            if ( ret != false )
            {
                this.masterSpot = new DataMasterSpot();
                if ( this.mapSpot.length > 0 )
                {
                    this.masterSpot.getData( this.mapSpot[0].getSpotId() );
                }
            }
            // �S�����Ɍ�������
            this.spotAllCount = this.spotCount;
            if ( this.spotAllCount == 0 )
            {
                ret = false;
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.info( "[MapSpot.getMapSpotBySeq()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �n��ID���Ƃɒn�}�X�|�b�g�̌������擾
     * 
     * @param spotId �X�|�b�gID
     * @param localId �n��ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getSpotIdListLocal(int spotId, int localId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        SearchEngineBasic seb;

        //
        seb = new SearchEngineBasic();
        seb.getLocalList( localId, 0 );
        if ( seb.getMasterLocalCount() > 0 )
        {
            this.localIdList = new int[seb.getMasterLocalCount()];
            this.spotIdList = new int[seb.getMasterLocalCount()];
            for( int i = 0 ; i < seb.getMasterLocalCount() ; i++ )
            {
                this.localIdList[i] = seb.getMasterLocal()[i].getLocalId();
                // ����������0����
                this.spotIdList[i] = 0;
            }
        }
        seb = null;

        if ( spotId < 0 )
        {
            return(false);
        }

        query = "SELECT hh_master_pref.local_id, COUNT(*) AS count FROM hh_master_pref, hh_map_spot, hh_master_spot";
        query = query + " WHERE hh_map_spot.spot_id = hh_master_spot.spot_id";
        query = query + " AND hh_map_spot.pref_id = hh_master_pref.pref_id";
        query = query + " AND hh_map_spot.spot_id = ?";
        if ( localId > 0 )
        {
            query = query + " AND hh_master_pref.local_id = ?";
        }
        query = query + " AND hh_map_spot.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_map_spot.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_spot.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_spot.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " GROUP BY hh_master_pref.local_id";
        query = query + " ORDER BY hh_master_pref.local_id";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, spotId );
            if ( localId > 0 )
            {
                prestate.setInt( 2, localId );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    for( int i = 0 ; i < this.localIdList.length ; i++ )
                    {
                        // ��v�����n���̃f�[�^�������Z�b�g
                        if ( this.localIdList[i] == result.getInt( "local_id" ) )
                        {
                            this.spotIdList[i] = result.getInt( "count" );
                            break;
                        }
                    }
                }
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.info( "[MapSpot.getMapSpotBySeq()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        if ( this.localIdList.length > 0 )
        {
            ret = true;
        }

        return(ret);
    }

    /**
     * �s���{��ID���Ƃɒn�}�X�|�b�g�̌������擾
     * 
     * @param spotId �X�|�b�gID
     * @param localId �n��ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getSpotIdListPref(int spotId, int localId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        SearchEngineBasic seb;

        if ( (spotId < 0) || (localId <= 0) )
        {
            return(false);
        }

        //
        seb = new SearchEngineBasic();
        seb.getPrefListByLocal( localId, 0 );
        if ( seb.getMasterPrefCount() > 0 )
        {
            this.prefIdList = new int[seb.getMasterPrefCount()];
            this.spotIdList = new int[seb.getMasterPrefCount()];
            for( int i = 0 ; i < seb.getMasterPrefCount() ; i++ )
            {
                this.prefIdList[i] = seb.getMasterPref()[i].getPrefId();
                // ����������0����
                this.spotIdList[i] = 0;
            }
        }
        seb = null;

        query = "SELECT hh_master_pref.pref_id, COUNT(*) AS count FROM hh_master_pref, hh_map_spot, hh_master_spot";
        query = query + " WHERE hh_map_spot.spot_id = hh_master_spot.spot_id";
        query = query + " AND hh_map_spot.pref_id = hh_master_pref.pref_id";
        query = query + " AND hh_map_spot.spot_id = ?";
        if ( localId > 0 )
        {
            query = query + " AND hh_master_pref.local_id = ?";
        }
        query = query + " AND hh_map_spot.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_map_spot.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_spot.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_spot.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " GROUP BY hh_master_pref.pref_id";
        query = query + " ORDER BY hh_master_pref.local_id, hh_master_pref.pref_id";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, spotId );
            if ( localId > 0 )
            {
                prestate.setInt( 2, localId );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    for( int i = 0 ; i < this.prefIdList.length ; i++ )
                    {
                        // ��v�����s���{���̃f�[�^�������Z�b�g
                        if ( this.prefIdList[i] == result.getInt( "pref_id" ) )
                        {
                            this.spotIdList[i] = result.getInt( "count" );
                            break;
                        }
                    }
                }
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.info( "[MapSpot.getMapSpotBySeq()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        if ( this.prefIdList != null && this.prefIdList.length > 0 )
        {
            ret = true;
        }
        return(ret);
    }

    /**
     * TOP�y�[�W�ɕ\������n�}�X�|�b�g���擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getSpotDataByTopDisp()
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        // hh_map_spot.top_disp_flagl=1�̃f�[�^���擾
        query = "SELECT * FROM hh_map_spot, hh_master_spot";
        query = query + " WHERE hh_map_spot.spot_id = hh_master_spot.spot_id";
        query = query + " AND hh_map_spot.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_map_spot.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_spot.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_spot.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_map_spot.top_disp_flag >= 1";
        query = query + " ORDER BY hh_map_spot.top_disp_flag ";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            ret = getMapSpotSub( prestate );
            if ( ret != false )
            {
                this.masterSpot = new DataMasterSpot();
                if ( this.mapSpot.length > 0 )
                {
                    this.masterSpot.getData( this.mapSpot[0].getSpotId() );
                }
            }
            // �S�����Ɍ�������
            this.spotAllCount = this.spotCount;
            if ( this.spotAllCount == 0 )
            {
                ret = false;
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.info( "[MapSpot.getMapSpotBySeq()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }
}
