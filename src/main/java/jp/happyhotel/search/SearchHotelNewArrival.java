package jp.happyhotel.search;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelAdjustmentHistory;
import jp.happyhotel.data.DataHotelBasic;

/**
 * �V���z�e���擾�N���X
 * 
 * @author koshiba-y1
 */
public class SearchHotelNewArrival
{
    /**
     * �z�e����{�����i�[�����z��
     */
    private DataHotelBasic[]             hotelInfoArray;

    /**
     * �z�e���ύX���������i�[�����z��
     */
    private DataHotelAdjustmentHistory[] hotelAdjustmentHistoryArray;

    /**
     * �S�V���z�e���̃z�e��ID���i�[�����z��
     */
    private int[]                        hotelIdArray;

    /**
     * �R���X�g���N�^
     */
    public SearchHotelNewArrival()
    {
        initInstanceVariables();
    }

    /**
     * �z�e����{���̌����̎擾
     * 
     * @return �z�e����{�����i�[�����z��̗v�f��
     */
    public int getCount()
    {
        return this.hotelInfoArray.length;
    }

    /**
     * �V���z�e���̑S�����̎擾
     * 
     * @return �V���z�e���̑S����
     */
    public int getAllCount()
    {
        return this.hotelIdArray.length;
    }

    /**
     * �z�e����{���̎擾
     * 
     * @return �z�e����{�����i�[�����z��
     */
    public DataHotelBasic[] getHotelInfo()
    {
        return hotelInfoArray;
    }

    /**
     * �z�e���ύX�������̎擾
     * 
     * @return �z�e���ύX���������i�[�����z��
     */
    public DataHotelAdjustmentHistory[] getHotelAdjustment()
    {
        return hotelAdjustmentHistoryArray;
    }

    /**
     * �S�V���z�e���̃z�e��ID���i�[�����z��̎擾
     * 
     * @return �S�V���z�e���̃z�e��ID���i�[�����z��
     */
    public int[] getHotelIdList()
    {
        return hotelIdArray;
    }

    /**
     * �z�e���ꗗ���擾<br>
     * <br>
     * ���L���\�b�h�œ�����l���X�V���܂��B<br>
     * {@link #getCount()}<br>
     * {@link #getAllCount()}<br>
     * {@link #getHotelInfo()}<br>
     * {@link #getHotelAdjustment()}<br>
     * {@link #getHotelIdList()}<br>
     * <br>
     * {@code countNum}��{@code pageNum}�͎��̂悤�Ȋ֌W�ɂ���܂��B<br>
     * �E{@code countNum}: {@code 3}, {@code pageNum}: {@code 1} �̏ꍇ<br>
     * &emsp;�E�S���R�[�h: {@code [1, 2, 3, 4, 5, 6, 7, 8, 9]} �̏ꍇ<br>
     * &emsp;&emsp;�擾�Ώ�: {@code [4, 5, 6]}<br>
     * &emsp;�E�S���R�[�h: {@code [1, 2, 3, 4, 5]} �̏ꍇ<br>
     * &emsp;&emsp;�擾�Ώ�: {@code [4, 5]}<br>
     * &emsp;�E�S���R�[�h: {@code [1, 2, 3]} �̏ꍇ<br>
     * &emsp;&emsp;�擾�Ώ�: {@code []}<br>
     * 
     * @param countNum �擾�����i{@code 0}�F�S���i{@code pageNum}�͖����j�j
     * @param pageNum �y�[�W�ԍ��i{@code 0}�`�j
     * @return �������ʁi{@code true}�F����A{@code false}�F�ُ�j
     */
    public boolean getHotelList(int countNum, int pageNum)
    {
        if ( countNum <= -1 || pageNum <= -1 )
        {
            initInstanceVariables();
            return false;
        }

        return getHotelList( null, null, countNum, pageNum );
    }

    /**
     * �z�e���ꗗ���擾<br>
     * <br>
     * ���L���\�b�h�œ�����l���X�V���܂��B<br>
     * {@link #getCount()}<br>
     * {@link #getAllCount()}<br>
     * {@link #getHotelInfo()}<br>
     * {@link #getHotelAdjustment()}<br>
     * {@link #getHotelIdList()}<br>
     * <br>
     * {@code countNum}��{@code pageNum}�͎��̂悤�Ȋ֌W�ɂ���܂��B<br>
     * �E{@code countNum}: {@code 3}, {@code pageNum}: {@code 1} �̏ꍇ<br>
     * &emsp;�E�S���R�[�h: {@code [1, 2, 3, 4, 5, 6, 7, 8, 9]} �̏ꍇ<br>
     * &emsp;&emsp;�擾�Ώ�: {@code [4, 5, 6]}<br>
     * &emsp;�E�S���R�[�h: {@code [1, 2, 3, 4, 5]} �̏ꍇ<br>
     * &emsp;&emsp;�擾�Ώ�: {@code [4, 5]}<br>
     * &emsp;�E�S���R�[�h: {@code [1, 2, 3]} �̏ꍇ<br>
     * &emsp;&emsp;�擾�Ώ�: {@code []}<br>
     * 
     * @param prefId �s���{��ID�i{@code 0}�F�i�荞�݂Ȃ��j
     * @param countNum �擾�����i{@code 0}�F�S���i{@code pageNum}�͖����j�j
     * @param pageNum �y�[�W�ԍ��i{@code 0}�`�j
     * @return �������ʁi{@code true}�F����A{@code false}�F�ُ�j
     */
    public boolean getHotelListByPref(int prefId, int countNum, int pageNum)
    {
        if ( countNum <= -1 || pageNum <= -1 )
        {
            initInstanceVariables();
            return false;
        }

        return getHotelList( prefId, null, countNum, pageNum );
    }

    /**
     * �z�e���ꗗ���擾<br>
     * <br>
     * ���L���\�b�h�œ�����l���X�V���܂��B<br>
     * {@link #getCount()}<br>
     * {@link #getAllCount()}<br>
     * {@link #getHotelInfo()}<br>
     * {@link #getHotelAdjustment()}<br>
     * {@link #getHotelIdList()}<br>
     * <br>
     * {@code countNum}��{@code pageNum}�͎��̂悤�Ȋ֌W�ɂ���܂��B<br>
     * �E{@code countNum}: {@code 3}, {@code pageNum}: {@code 1} �̏ꍇ<br>
     * &emsp;�E�S���R�[�h: {@code [1, 2, 3, 4, 5, 6, 7, 8, 9]} �̏ꍇ<br>
     * &emsp;&emsp;�擾�Ώ�: {@code [4, 5, 6]}<br>
     * &emsp;�E�S���R�[�h: {@code [1, 2, 3, 4, 5]} �̏ꍇ<br>
     * &emsp;&emsp;�擾�Ώ�: {@code [4, 5]}<br>
     * &emsp;�E�S���R�[�h: {@code [1, 2, 3]} �̏ꍇ<br>
     * &emsp;&emsp;�擾�Ώ�: {@code []}<br>
     * 
     * @param hotelIdList �z�e��ID���i�[�����z��i{@code null}�F�i�荞�݂Ȃ��A{@code []}�F�i�荞�݂���j
     * @param countNum �擾�����i{@code 0}�F�S���i{@code pageNum}�͖����j�j
     * @param pageNum �y�[�W�ԍ��i{@code 0}�`�j
     * @return �������ʁi{@code true}�F����A{@code false}�F�ُ�j
     */
    public boolean getHotelListByHotelIdList(int[] hotelIdList, int countNum, int pageNum)
    {
        if ( countNum <= -1 || pageNum <= -1 )
        {
            initInstanceVariables();
            return false;
        }

        if ( hotelIdList != null && hotelIdList.length == 0 )
        {
            initInstanceVariables();
            return true;
        }

        return getHotelList( null, hotelIdList, countNum, pageNum );
    }

    /**
     * �C���X�^���X�ϐ��̏�����
     */
    private void initInstanceVariables()
    {
        this.hotelInfoArray = new DataHotelBasic[0];
        this.hotelAdjustmentHistoryArray = new DataHotelAdjustmentHistory[0];
        this.hotelIdArray = new int[0];
    }

    /**
     * �z�e���ꗗ���擾�i�{�́j<br>
     * <br>
     * ���L���\�b�h�œ�����l���X�V���܂��B<br>
     * {@link #getCount()}<br>
     * {@link #getAllCount()}<br>
     * {@link #getHotelInfo()}<br>
     * {@link #getHotelAdjustment()}<br>
     * {@link #getHotelIdList()}<br>
     * <br>
     * {@code countNum}��{@code pageNum}�͎��̂悤�Ȋ֌W�ɂ���܂��B<br>
     * �E{@code countNum}: {@code 3}, {@code pageNum}: {@code 1} �̏ꍇ<br>
     * &emsp;�E�S���R�[�h: {@code [1, 2, 3, 4, 5, 6, 7, 8, 9]} �̏ꍇ<br>
     * &emsp;&emsp;�擾�Ώ�: {@code [4, 5, 6]}<br>
     * &emsp;�E�S���R�[�h: {@code [1, 2, 3, 4, 5]} �̏ꍇ<br>
     * &emsp;&emsp;�擾�Ώ�: {@code [4, 5]}<br>
     * &emsp;�E�S���R�[�h: {@code [1, 2, 3]} �̏ꍇ<br>
     * &emsp;&emsp;�擾�Ώ�: {@code []}<br>
     * 
     * @param prefId �s���{��ID�i{@code null} or {@code 0}�F�i�荞�݂Ȃ��j
     * @param hotelIdList �z�e��ID���i�[�����z��i{@code null}�F�i�荞�݂Ȃ��A{@code []}�F�i�荞�݂���j
     * @param countNum �擾�����i{@code 0}�F�S���i{@code pageNum}�͖����j�j
     * @param pageNum �y�[�W�ԍ��i{@code 0}�`�j
     * @return �������ʁi{@code true}�F����A{@code false}�F�ُ�j
     */
    private boolean getHotelList(final Integer prefId, final int[] hotelIdList, final int countNum, final int pageNum)
    {
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        try
        {
            connection = DBConnection.getConnection();
            String query = makeQuery( prefId, hotelIdList );
            prestate = connection.prepareStatement( query );
            prestate = insertQueryParam( prestate, prefId, hotelIdList );
            result = prestate.executeQuery();

            if ( result == null )
            {
                initInstanceVariables();
                return false;
            }

            // �S�V���z�e���̃z�e��ID���i�[�����z��̍쐬
            this.hotelIdArray = makeHotelIdArray( result );

            // �z�e����{�����i�[�����z��̍쐬
            this.hotelInfoArray = makeHotelInfoArray( result, countNum, pageNum );

            // �z�e���ύX���������i�[�����z��
            this.hotelAdjustmentHistoryArray = makeHotelAdjustmentHistoryArray( result, countNum, pageNum );
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelList] Exception=" + e.toString() );

            initInstanceVariables();
            return false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return true;
    }

    /**
     * �N�G���쐬����
     * 
     * @param prefId �s���{��ID�i{@code null} or {@code 0}�F�i�荞�݂Ȃ��j
     * @param hotelIds �z�e��ID���i�[�����z��i{@code null}�F�i�荞�݂Ȃ��A{@code []}�F�i�荞�݂���j
     * @return �N�G��
     */
    private static String makeQuery(final Integer prefId, final int[] hotelIds)
    {
        String query = "";

        query += "SELECT ";
        query += "    tmp.* ";
        query += "FROM ( ";
        query += "    SELECT ";
        query += "        hh_hotel_adjustment_history.* ";

        // �J�������J���}�ŋ�؂��āA�J�����Ɂuhh_hotel_basic_�v�Ŏn�܂�ʖ���t����
        for( String column : DataHotelBasic.COLUMNS.split( "," ) )
        {
        	if ("empty_kind".equals(column.toLowerCase())) {
                query += "        ,IFNULL(hh_hotel_status.`mode`, 0)";
        	} else if ("empty_status".equals(column.toLowerCase())) {
                query += "        ,IFNULL(hh_hotel_status." + column + ", 0)";
        	} else {
                query += "        ,hh_hotel_basic." + column;
        	}
            query += " hh_hotel_basic_" + column + "";
        }

        query += "    FROM ";
        query += "        hh_hotel_adjustment_history ";
        query += "    INNER JOIN ";
        query += "        hh_hotel_basic ";
        query += "        ON hh_hotel_adjustment_history.id = hh_hotel_basic.id ";
        query += "        AND hh_hotel_basic.kind <= 7 ";
        query += "        AND hh_hotel_basic.rank != 0 ";

        // �s���{���ɂ��i�荞�݂���̏ꍇ
        if ( prefId != null && prefId > 0 )
        {
            query += "        AND hh_hotel_basic.pref_id = ? ";
        }

        // �z�e��ID�ɂ��i�荞�݂���̏ꍇ
        if ( hotelIds != null && hotelIds.length > 0 )
        {
            query += "        AND hh_hotel_basic.id IN(";
            for( int i = 0 ; i < hotelIds.length ; i++ )
            {
                query += "?";
                if ( i < hotelIds.length - 1 )
                {
                    query += ", ";
                }
            }
            query += ") ";
        }
        
        query += "    LEFT JOIN hh_hotel_status";
        query += "        ON hh_hotel_basic.id = hh_hotel_status.id";

        query += "    WHERE ";
        query += "        (edit_id = 101 AND edit_sub = 0) OR (edit_id = 102 AND edit_sub = 0) ";
        query += "    ORDER BY ";
        query += "        input_date DESC, ";
        query += "        input_time DESC ";
        query += ") AS tmp ";
        query += "GROUP BY ";
        query += "    tmp.id ";
        query += "ORDER BY ";
        query += "    tmp.input_date DESC, ";
        query += "    tmp.input_time DESC"; // this.hotelIdArray�ɑS�����̃f�[�^������K�v�����邽�߁ALIMIT��͎g�p���Ȃ�

        return query;
    }

    /**
     * �N�G���p�����[�^�ݒ菈��
     * 
     * @param prestate �N�G�����ݒ肳�ꂽ{@code PreparedStatement}�I�u�W�F�N�g
     * @param prefId �s���{��ID�i{@code null} or {@code 0}�F�i�荞�݂Ȃ��j
     * @param hotelIds �z�e��ID���i�[�����z��i{@code null}�F�i�荞�݂Ȃ��A{@code []}�F�i�荞�݂���j
     * @return �p�����[�^���}�����ꂽ{@code PreparedStatement}�I�u�W�F�N�g
     * @throws SQLException DB�֘A�̃G���[�����������ꍇ
     */
    private static PreparedStatement insertQueryParam(
            final PreparedStatement prestate, final Integer prefId, final int[] hotelIds)
            throws SQLException
    {
        int i = 1;

        // �s���{���ɂ��i�荞�݂���̏ꍇ
        if ( prefId != null && prefId > 0 )
        {
            prestate.setInt( i++, prefId );
        }

        // �z�e��ID�ɂ��i�荞�݂���̏ꍇ
        if ( hotelIds != null && hotelIds.length > 0 )
        {
            for( int hotelId : hotelIds )
            {
                prestate.setInt( i++, hotelId );
            }
        }

        return prestate;
    }

    /**
     * �S�V���z�e���̃z�e��ID���i�[�����z��̍쐬
     * 
     * @param result �������ʂ��i�[����{@code ResultSet}�I�u�W�F�N�g
     * @return �S�V���z�e���̃z�e��ID���i�[�����z��
     * @throws SQLException DB�֘A�̃G���[�����������ꍇ
     */
    private static int[] makeHotelIdArray(final ResultSet result) throws SQLException
    {
        final int numberOfRecords;
        if ( result.last() )
        {
            numberOfRecords = result.getRow();
        }
        else
        {
            numberOfRecords = 0;
        }

        result.beforeFirst();

        int[] hotelIdArray = new int[numberOfRecords];

        int rowCount = 0;
        while( result.next() )
        {
            hotelIdArray[rowCount] = result.getInt( "id" );
            rowCount++;
        }

        return hotelIdArray;
    }

    /**
     * �z�e����{�����i�[�����z��̍쐬
     * 
     * @param result �������ʂ��i�[����{@code ResultSet}�I�u�W�F�N�g
     * @param countNum �擾�����i{@code 0}�F�S���i{@code pageNum}�͖����j�j
     * @param pageNum �y�[�W�ԍ��i{@code 0}�`�j
     * @return �z�e����{�����i�[�����z��
     * @throws SQLException DB�֘A�̃G���[�����������ꍇ
     */
    private static DataHotelBasic[] makeHotelInfoArray(
            final ResultSet result, final int countNum, final int pageNum)
            throws SQLException
    {
        final int rowJump = countNum * pageNum;
        if ( rowJump == 0 )
        {
            result.beforeFirst();
        }
        else
        {
            result.absolute( rowJump );
        }

        List<DataHotelBasic> hotelInfoList = new ArrayList<DataHotelBasic>();

        int rowCount = 1;
        while( result.next() )
        {
            DataHotelBasic hotelInfo = new DataHotelBasic();
            hotelInfo.setData( result, "hh_hotel_basic_" );
            hotelInfoList.add( hotelInfo );

            // countNum��0�̏ꍇ�͍i�荞�݂Ȃ�
            if ( countNum != 0 && rowCount >= countNum )
            {
                break;
            }

            rowCount++;
        }

        return hotelInfoList.toArray( new DataHotelBasic[hotelInfoList.size()] );
    }

    /**
     * �z�e���ύX���������i�[�����z��̍쐬
     * 
     * @param result �������ʂ��i�[����{@code ResultSet}�I�u�W�F�N�g
     * @param countNum �擾�����i{@code 0}�F�S���i{@code pageNum}�͖����j�j
     * @param pageNum �y�[�W�ԍ��i{@code 0}�`�j
     * @return �z�e���ύX���������i�[�����z��̍쐬
     * @throws SQLException DB�֘A�̃G���[�����������ꍇ
     */
    private static DataHotelAdjustmentHistory[] makeHotelAdjustmentHistoryArray(
            final ResultSet result, final int countNum, final int pageNum)
            throws SQLException
    {
        final int rowJump = countNum * pageNum;
        if ( rowJump == 0 )
        {
            result.beforeFirst();
        }
        else
        {
            result.absolute( rowJump );
        }

        List<DataHotelAdjustmentHistory> hotelAdjustmentHistoryList = new ArrayList<DataHotelAdjustmentHistory>();

        int rowCount = 1;
        while( result.next() )
        {
            DataHotelAdjustmentHistory hotelAdjustmentHistory = new DataHotelAdjustmentHistory();
            hotelAdjustmentHistory.setData( result );
            hotelAdjustmentHistoryList.add( hotelAdjustmentHistory );

            // countNum��0�̏ꍇ�͍i�荞�݂Ȃ�
            if ( countNum != 0 && rowCount >= countNum )
            {
                break;
            }

            rowCount++;
        }

        return hotelAdjustmentHistoryList.toArray( new DataHotelAdjustmentHistory[hotelAdjustmentHistoryList.size()] );
    }
}
