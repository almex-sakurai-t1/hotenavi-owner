package jp.happyhotel.hotel;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelPresentOffer;
import jp.happyhotel.data.DataHotelPresentOfferSub;
import jp.happyhotel.data.DataMasterLocal;
import jp.happyhotel.data.DataMasterPref;
import jp.happyhotel.data.DataUserPresentEntry;
import jp.happyhotel.search.SearchEngineBasic;

public class HotelPresentOffer implements Serializable
{
    /**
     *
     */
    private static final long          serialVersionUID     = -3957984258161172180L;
    private int                        hotelCount;
    private int                        hotelAllCount;
    private int                        localCount;
    private int[]                      prefCount;
    private DataMasterLocal            dmLocal;
    private DataMasterPref[]           dmPref;
    private DataHotelBasic[]           m_hotelBasic;
    private DataHotelPresentOffer[]    hotelPresentOffer;
    private DataHotelPresentOfferSub[] formSub;
    private int[]                      entryCount;

    private int                        userCount;
    private DataUserPresentEntry[]     userPresentEntry;
    private String[]                   handleNameList;

    private int                        lotteryUserCount;
    private String[]                   lotteryUserList;

    final int                          FORM_ENTRY           = 0;
    final int                          FORM_DROW            = 1;
    final int                          FORM_DROW_FIX        = 2;
    final int                          FORM_MAKE_ELECT_MAIL = 3;
    final int                          FORM_SEND_ELECT_MAIL = 4;
    final int                          FORM_DRAW_LOSE_FIX   = 5;
    final int                          FORM_MAKE_LOSE_MAIL  = 6;
    final int                          FORM_SEND_LOSE_MAIL  = 7;
    final int                          FORM_STATUS_FIX      = 9;

    public HotelPresentOffer()
    {
        hotelCount = 0;
        hotelAllCount = 0;
        localCount = 0;
        lotteryUserCount = 0;
        userCount = 0;
    }

    /** �z�e�������擾�i1�y�[�W�̌����j **/
    public int getHotelCount()
    {
        return hotelCount;
    }

    /** �z�e�������擾 **/
    public int getHotelAllCount()
    {
        return hotelAllCount;
    }

    /** �z�e�����擾 **/
    public DataHotelBasic[] getHotelInfo()
    {
        return(m_hotelBasic);
    }

    /** �z�e�����擾 **/
    public DataHotelPresentOffer[] getHotel()
    {
        return(hotelPresentOffer);
    }

    /** �n�������擾 **/
    public int getLocalCount()
    {
        return localCount;
    }

    /** �s���{�������擾 **/
    public int[] getPrefCount()
    {
        return prefCount;
    }

    /** �n���}�X�^�擾 **/
    public DataMasterLocal getMasterLocal()
    {
        return dmLocal;
    }

    /** �s���{���}�X�^�擾 **/
    public DataMasterPref[] getMasterPref()
    {
        return dmPref;
    }

    /** ���匏�� **/
    public int[] getEntryCount()
    {
        return entryCount;
    }

    /** �t�H�[���X�e�[�^�X **/
    public DataHotelPresentOfferSub[] getFormSub()
    {
        return formSub;
    }

    public int getUserCount()
    {
        return userCount;
    }

    public DataUserPresentEntry[] getUser()
    {
        return userPresentEntry;
    }

    public String[] getHandleNameList()
    {
        return handleNameList;
    }

    /**
     * �v���[���g�z�e�������擾�i�s���{��ID����j
     * 
     * @param prefId �s���{��ID
     * @return �z�e������
     */
    public int getHotelCountByPref(int prefId)
    {
        int i = 0;
        int count;
        int today = 0;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        today = Integer.parseInt( DateEdit.getDate( 2 ) );
        if ( prefId <= 0 )
        {
            return(-1);
        }
        query = "SELECT COUNT(A.id) FROM hh_hotel_basic A";
        query += " INNER JOIN hh_master_pref B ON A.pref_id = B.pref_id";
        query += " INNER JOIN hh_hotel_present_offer C ON A.id = C.id AND C.del_flag=0";
        query += "   AND C.start_date <= ? AND C.end_date >= ?";
        query += " WHERE A.rank > 1 ";
        query += " AND A.kind <= 7";
        query += " AND B.pref_id = ?";
        query += " GROUP BY A.id";

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( ++i, today );
            prestate.setInt( ++i, today );
            if ( prefId > 0 )
            {
                prestate.setInt( ++i, prefId );
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
            Logging.info( "[getHotelCountByPref] Exception=" + e.toString() );
            return(0);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(count);
    }

    /**
     * �v���[���g�z�e�������擾�i�n��ID����j
     * 
     * @param localId �n��ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getHotelCountByLocal(int localId)
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
                                this.localCount = this.localCount + getHotelCountByPref( seb.getMasterPref()[j].getPrefId() );
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
                                this.localCount = localCount + getHotelCountByPref( seb.getMasterPref()[j].getPrefId() );

                                this.dmPref[j] = new DataMasterPref();
                                this.dmPref[j] = seb.getMasterPref()[j];
                                this.prefCount[j] = getHotelCountByPref( seb.getMasterPref()[j].getPrefId() );
                            }
                        }
                    }
                    else
                    {
                        localCount = 0;
                        Logging.error( "[getHotelCountByLocal] pref=0" );
                    }
                }
            }
            else
            {
                localCount = 0;
                Logging.error( "[getHotelCountByLocal] local=0" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelCountByLocal] Exception=" + e.toString() );
            return false;
        }
        return true;
    }

    /**
     * �v���[���g�z�e�����擾�i�s���{�����ƂɁj
     * 
     * @param prefId �s���{��ID
     * @param order �\�[�g�t���O(0:disp_pos���A1:�n�揇�A2:�L�������̏����A3:�z�e�����̏���)
     * @param countNum �擾�����i0�F�S�� ��pageNum����
     * @param pageNum �y�[�W�ԍ��i0�`)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getHotel(int prefId, int order, int countNum, int pageNum)
    {
        int i = 0;
        int today = 0;
        int count;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = "";
        today = Integer.parseInt( DateEdit.getDate( 2 ) );
        if ( order < 0 || prefId < 0 )
        {
            return(false);
        }
        else if ( order >= 0 && order <= 3 )
        {
            query = "SELECT A.* FROM hh_hotel_present_offer A";
            query += " INNER JOIN hh_hotel_basic B ON A.id = B.id ";
            query += "  AND B.rank > 1";
            query += "  AND B.kind <= 7";
            query += " WHERE A.del_flag=0";
            query += "  AND A.start_date <= ?";
            query += "  AND A.end_date >= ?";

            if ( prefId > 0 )
            {
                query += " AND B.pref_id = ?";
            }
            query += " GROUP BY A.id";

            // disp_pos��
            if ( order == 0 )
            {
                query += " ORDER BY A.start_date DESC";
            }
            // �n�揇
            if ( order == 1 )
            {
                query += " ORDER BY B.pref_id, A.start_date DESC";
            }
            // �L�������̏I�����t�̏���
            else if ( order == 2 )
            {
                query += " ORDER BY A.end_date, B.pref_id, A.start_date DESC";
            }
            else if ( order == 3 )
            {
                query += " ORDER BY B.name, B.rank DESC, A.start_date DESC";

            }
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
            prestate.setInt( ++i, today );
            prestate.setInt( ++i, today );
            if ( prefId > 0 )
            {
                prestate.setInt( ++i, prefId );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    this.hotelCount = result.getRow();
                    Logging.error( "[getHotel] count=" + result.getRow() );

                }
                m_hotelBasic = new DataHotelBasic[hotelCount];
                hotelPresentOffer = new DataHotelPresentOffer[hotelCount];

                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    this.m_hotelBasic[count] = new DataHotelBasic();
                    this.hotelPresentOffer[count] = new DataHotelPresentOffer();

                    this.m_hotelBasic[count].getData( result.getInt( "A.id" ) );
                    this.hotelPresentOffer[count].setData( result );

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
            Logging.info( "[getHotel] Exception=" + e.toString() );
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
        else if ( order >= 0 && order <= 3 )
        {
            query = "SELECT COUNT(A.id) FROM hh_hotel_present_offer A";
            query += " INNER JOIN hh_hotel_basic B ON A.id = B.id ";
            query += "  AND B.rank > 1";
            query += "  AND B.kind <= 7";
            query += " WHERE A.del_flag=0";
            query += "  AND A.start_date <= ?";
            query += "  AND A.end_date >= ?";

            if ( prefId > 0 )
            {
                query += " AND B.pref_id = ?";
            }
            query += " GROUP BY A.id";

            // disp_pos��
            if ( order == 0 )
            {
                query += " ORDER BY A.start_date DESC";
            }
            // �n�揇
            if ( order == 1 )
            {
                query += " ORDER BY B.pref_id, A.start_date DESC";
            }
            // �L�������̏I�����t�̏���
            else if ( order == 2 )
            {
                query += " ORDER BY A.end_date, B.pref_id, A.start_date DESC";
            }
            else if ( order == 3 )
            {
                query += " ORDER BY B.name, B.rank DESC, A.start_date DESC";

            }
        }

        try
        {
            i = 0;
            prestate = connection.prepareStatement( query );
            prestate.setInt( ++i, today );
            prestate.setInt( ++i, today );
            if ( prefId > 0 )
            {
                prestate.setInt( ++i, prefId );
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
            Logging.info( "[getHotel] Exception=" + e.toString() );
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
     * �v���[���g�z�e�����擾�i�n�����ƂɁj
     * 
     * @param localId �n��ID
     * @param order �\�[�g�t���O(0:disp_pos���A1:�n�揇�A2:�L�������̏����A3:�z�e�����̏���)
     * @param countNum �擾�����i0�F�S�� ��pageNum����
     * @param pageNum �y�[�W�ԍ��i0�`)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getHotelByLocal(int localId, int order, int countNum, int pageNum)
    {
        int i = 0;
        int today;
        int count;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = "";
        today = Integer.parseInt( DateEdit.getDate( 2 ) );
        if ( order < 0 || localId < 0 )
        {
            return(false);
        }
        else if ( order >= 0 && order <= 3 )
        {
            query = "SELECT A.* FROM hh_hotel_present_offer A";
            query += " INNER JOIN hh_hotel_basic B ON A.id = B.id ";
            query += "  AND B.rank > 1";
            query += "  AND B.kind <= 7";
            query += " INNER JOIN hh_master_pref C ON B.pref_id = C.pref_id ";

            query += " WHERE A.del_flag=0";
            query += "  AND A.start_date <= ?";
            query += "  AND A.end_date >= ?";

            if ( localId > 0 )
            {
                query = query + " AND C.local_id = ?";
            }
            query = query + " GROUP BY A.id";
            // disp_pos��
            if ( order == 0 )
            {
                query = query + " ORDER BY A.start_date DESC";
            }
            // �n�揇
            if ( order == 1 )
            {
                query = query + " ORDER BY B.pref_id, A.start_date DESC";
            }
            // �L�������̏I�����t�̏���
            else if ( order == 2 )
            {
                query = query + " ORDER BY A.end_date, B.pref_id, A.start_date DESC";
            }
            else if ( order == 3 )
            {
                query += " ORDER BY B.name_kana, B.rank DESC,";
                query += " A.start_date DESC";
            }
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
            prestate.setInt( ++i, today );
            prestate.setInt( ++i, today );

            if ( localId > 0 )
            {
                prestate.setInt( ++i, localId );

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
                hotelPresentOffer = new DataHotelPresentOffer[hotelCount];

                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    this.m_hotelBasic[count] = new DataHotelBasic();
                    this.hotelPresentOffer[count] = new DataHotelPresentOffer();

                    this.m_hotelBasic[count].getData( result.getInt( "A.id" ) );
                    this.hotelPresentOffer[count].setData( result );
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
            Logging.info( "[getHotelByLocal] Exception=" + e.toString() );
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
        else if ( order >= 0 && order <= 3 )
        {
            query = "SELECT COUNT(A.id) FROM hh_hotel_present_offer A";
            query += " INNER JOIN hh_hotel_basic B ON A.id = B.id ";
            query += "  AND B.rank > 1";
            query += "  AND B.kind <= 7";
            query += " INNER JOIN hh_master_pref C ON B.pref_id = C.pref_id ";

            query += " WHERE A.del_flag=0";
            query += "  AND A.start_date <= ?";
            query += "  AND A.end_date >= ?";

            if ( localId > 0 )
            {
                query = query + " AND C.local_id = ?";
            }
            query = query + " GROUP BY A.id";
            // disp_pos��
            if ( order == 0 )
            {
                query = query + " ORDER BY A.start_date DESC";
            }
            // �n�揇
            if ( order == 1 )
            {
                query = query + " ORDER BY B.pref_id, A.start_date DESC";
            }
            // �L�������̏I�����t�̏���
            else if ( order == 2 )
            {
                query = query + " ORDER BY A.end_date, B.pref_id, A.start_date DESC";
            }
            else if ( order == 3 )
            {
                query += " ORDER BY B.name_kana, B.rank DESC,";
                query += " A.start_date DESC";
            }
        }
        try
        {
            i = 0;
            prestate = connection.prepareStatement( query );
            prestate.setInt( ++i, today );
            prestate.setInt( ++i, today );
            if ( localId > 0 )
            {
                prestate.setInt( ++i, localId );
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
            Logging.info( "[getHotelByLocal] Exception=" + e.toString() );
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
     * �v���[���g�z�e�����擾�i�ŐV�̂��́j
     * 
     * @param newSpan �V���\��������ԁi�����O����j
     * @param countNum �擾�����i0�F�S�� ��pageNum����
     * @param pageNum �y�[�W�ԍ��i0�`)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getHotelLatest(int newSpan, int countNum, int pageNum)
    {
        int i = 0;
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

            query = "SELECT A.* FROM hh_hotel_present_offer A";
            query += " INNER JOIN hh_hotel_basic B ON A.id = B.id ";
            query += "  AND B.rank > 1";
            query += "  AND B.kind <= 7";
            query += " WHERE A.del_flag=0";
            query += "  AND A.start_date >= ?";
            query += "  AND A.start_date <= ?";
            query += "  AND A.end_date >= ?";

            query = query + " GROUP BY A.id";
            query = query + " ORDER BY A.start_date DESC";
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
            prestate.setInt( ++i, startDate );
            prestate.setInt( ++i, endDate );
            prestate.setInt( ++i, endDate );

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
                hotelPresentOffer = new DataHotelPresentOffer[hotelCount];

                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    this.m_hotelBasic[count] = new DataHotelBasic();
                    this.hotelPresentOffer[count] = new DataHotelPresentOffer();

                    this.m_hotelBasic[count].getData( result.getInt( "hotel_id" ) );
                    this.hotelPresentOffer[count].setData( result );

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
            Logging.info( "[getHotelLatest] Exception=" + e.toString() );
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
            query = "SELECT A.form_id, A.id FROM hh_hotel_present_offer A";
            query += " INNER JOIN hh_hotel_basic B ON A.id = B.id ";
            query += "  AND B.rank > 1";
            query += "  AND B.kind <= 7";
            query += " WHERE A.del_flag=0";
            query += "  AND A.start_date >= ?";
            query += "  AND A.start_date <= ?";
            query += "  AND A.end_date >= ?";

            query = query + " GROUP BY A.id";
            query = query + " ORDER BY A.start_date DESC";
        }
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( ++i, startDate );
            prestate.setInt( ++i, endDate );
            prestate.setInt( ++i, endDate );
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
            Logging.info( "[getHotelLatest] Exception=" + e.toString() );
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
     * �z�e���̉���ꗗ���擾
     * 
     * @param id
     * @param countNum
     * @param pageNum
     * @return
     */
    public boolean getHotel(int id, int countNum, int pageNum)
    {
        boolean ret = false;

        int i = 0;
        int count;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = "SELECT * FROM hh_hotel_present_offer ";
        query += " WHERE del_flag=0";
        query += "  AND id = ?";

        query = query + " ORDER BY start_date DESC";

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
            prestate.setInt( ++i, id );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    this.hotelCount = result.getRow();
                    Logging.error( "[getHotel] count=" + result.getRow() );

                }
                hotelPresentOffer = new DataHotelPresentOffer[this.hotelCount];

                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    this.hotelPresentOffer[count] = new DataHotelPresentOffer();
                    this.hotelPresentOffer[count].setData( result );
                    count++;
                }

                this.entryCount = new int[this.hotelCount];
                this.formSub = new DataHotelPresentOfferSub[this.hotelCount];

                for( i = 0 ; i < this.hotelCount ; i++ )
                {
                    this.entryCount[i] = this.getEntryCount( connection, this.hotelPresentOffer[i].getFormId(), this.hotelPresentOffer[i].getId() );
                    this.formSub[i] = new DataHotelPresentOfferSub();
                    this.getFormStatus( this.hotelPresentOffer[i], this.formSub[i] );
                }
            }
            else
            {
                this.hotelCount = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getHotel] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        query = "SELECT * FROM hh_hotel_present_offer ";
        query += " WHERE del_flag=0";
        query += "  AND id = ?";

        query = query + " ORDER BY start_date DESC";

        try
        {
            i = 0;
            prestate = connection.prepareStatement( query );
            prestate.setInt( ++i, id );
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
            Logging.info( "[getHotel] Exception=" + e.toString() );
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
     * �z�e���̉���ꗗ���擾
     * 
     * @param id
     * @param countNum
     * @param pageNum
     * @return
     */
    public int getEntryCount(Connection con, int formId, int id)
    {

        int i = 0;
        int count;
        String query;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = "SELECT COUNT(user_id) FROM hh_user_present_entry ";
        query += " WHERE form_id = ?";
        query += "  AND id = ?";
        query += "  AND status_flag < 9";

        count = 0;
        try
        {

            prestate = con.prepareStatement( query );
            prestate.setInt( ++i, formId );
            prestate.setInt( ++i, id );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.next() != false )
                {
                    count = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getHotel] Exception=" + e.toString() );
            return(0);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        return(count);
    }

    /**
     * �z�e���̉���t�H�[���̃X�e�[�^�X�𔻒f
     * 
     * @param dhpo DataHotelPresentOffer
     * @return
     */
    public void getFormStatus(DataHotelPresentOffer dhpo, DataHotelPresentOfferSub dhpoSub)
    {

        String status = "";
        String value = "";
        String url = "";

        // ������ԓ�
        if ( dhpo.getStartDate() <= Integer.parseInt( DateEdit.getDate( 2 ) ) &&
                dhpo.getEndDate() >= Integer.parseInt( DateEdit.getDate( 2 ) ) )
        {
            if ( dhpo.getStatusFlag() == FORM_ENTRY )
            {
                status = "<font color='blue'>��t��</font>";
            }
            else
            {
                status = "�X�e�[�^�X�ُ�";
            }
            value = "������Ԓ�";
        }
        else
        {
            switch( dhpo.getStatusFlag() )
            {
            // ���咆
                case FORM_ENTRY:
                    if ( dhpo.getStartDate() >= Integer.parseInt( DateEdit.getDate( 2 ) ) )
                    {
                        status = "�����";
                        value = "";

                    }
                    if ( dhpo.getEndDate() <= Integer.parseInt( DateEdit.getDate( 2 ) ) )
                    {
                        status = "<font color='red'>������</font>";
                        value = "���I";
                        url = "owner_stay_draw_list.jsp";
                    }
                    break;
                case FORM_DROW:
                    status = "���I�����ρi���m��j";
                    value = "���I���[���쐬";
                    url = "owner_stay_draw_confirm.jsp";
                    break;

                case FORM_DROW_FIX:
                    status = "���I�����ρi�m��j";
                    value = "���I���[���쐬";
                    url = "owner_stay_make_mail.jsp";
                    break;

                case FORM_MAKE_ELECT_MAIL:
                    status = "���I���[���쐬��";
                    value = "���[�����M";
                    url = "owner_stay_send_mail.jsp";
                    break;

                case FORM_SEND_ELECT_MAIL:
                    status = "���I���[�����M��";
                    value = "�V���A���ԍ�";
                    url = "owner_stay_serial.jsp";
                    break;

                case FORM_DRAW_LOSE_FIX:
                    status = "�͂��ꏈ����";
                    url = "owner_stay_serial.jsp";
                    break;

                case FORM_MAKE_LOSE_MAIL:
                    status = "�͂��ꃁ�[���쐬��";
                    url = "owner_stay_serial.jsp";
                    break;

                case FORM_SEND_LOSE_MAIL:
                    status = "�͂��ꃁ�[�����M��";
                    url = "owner_stay_serial.jsp";
                    break;

                case FORM_STATUS_FIX:
                    status = "�V���A���ԍ�";
                    url = "owner_stay_serial.jsp";
                    break;
                default:
                    status = "�X�e�[�^�X�ُ�";
                    break;
            }
        }
        dhpoSub.setFormId( dhpo.getFormId() );
        dhpoSub.setId( dhpo.getId() );
        dhpoSub.setFormUrl( url );
        dhpoSub.setFormValue( value );
        dhpoSub.setFormStatus( status );
    }

    /**
     * ���僆�[�U�ꗗ�f�[�^
     * 
     * @param formId �t�H�[��ID
     * @param id �z�e��ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getEntryUser(int formId, int id)
    {
        int i = 0;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = " SELECT A.form_id, A.id, B.status_flag, A.title, A.elect_no, C.user_id, C.handle_name, B.address1, B.address2, B.address3 ";
        query += " FROM hh_hotel_present_offer A";
        query += " INNER JOIN hh_user_present_entry B ON A.form_id = B.form_id AND A.id = B.id AND B.status_flag < 9";
        query += " INNER JOIN hh_user_basic C ON B.user_id = C.user_id AND C.regist_status = 9 AND C.del_flag = 0";
        query += "  WHERE A.form_id = ? AND A.id = ? AND A.status_flag < 9";
        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( ++i, formId );
            prestate.setInt( ++i, id );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    this.userCount = result.getRow();
                }

                // �N���X�̔z���p�ӂ��A����������B
                this.userPresentEntry = new DataUserPresentEntry[this.userCount];
                this.hotelPresentOffer = new DataHotelPresentOffer[this.userCount];
                this.handleNameList = new String[this.userCount];

                // ���R�[�h�Z�b�g��擪�ɖ߂��B
                result.beforeFirst();

                while( result.next() != false )
                {
                    this.userPresentEntry[count] = new DataUserPresentEntry();
                    this.hotelPresentOffer[count] = new DataHotelPresentOffer();
                    this.handleNameList[count] = new String();

                    // ���[�U�����Z�b�g
                    this.userPresentEntry[count].setFormId( result.getInt( "form_id" ) );
                    this.userPresentEntry[count].setId( result.getInt( "id" ) );
                    this.userPresentEntry[count].setStatusFlag( result.getInt( "status_flag" ) );
                    this.userPresentEntry[count].setUserId( result.getString( "user_id" ) );
                    this.userPresentEntry[count].setAddress1( result.getString( "address1" ) );
                    this.userPresentEntry[count].setAddress2( result.getString( "address2" ) );
                    this.userPresentEntry[count].setAddress3( result.getString( "address3" ) );

                    // this.userPresentEntry[count].setData( result );
                    // �z�e�������Z�b�g
                    this.hotelPresentOffer[count].setFormId( result.getInt( "form_id" ) );
                    this.hotelPresentOffer[count].setId( result.getInt( "id" ) );
                    this.hotelPresentOffer[count].setTitle( result.getString( "title" ) );
                    this.hotelPresentOffer[count].setElectNo( result.getInt( "elect_no" ) );
                    // this.hotelPresentOffer[count].setData( result );
                    // �n���h���l�[�����Z�b�g
                    this.handleNameList[count] = result.getString( "handle_name" );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelPresentOffer.getEntryUser()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(true);
    }

    /**
     * �����_�����I�f�[�^�擾
     * 
     * @param collectDate �Ώۓ��t(0:�ŐVPV)
     * @param getCount �擾����(0:1000��)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean lotteryPresent(int formId, int id, int electNo, String[] userIdList)
    {
        int i = 0;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String electUser = "";
        int electCount = 0;

        // ��ɓ��I�҂𒊑I����
        query = " SELECT A.form_id, A.id, B.status_flag, A.title, A.elect_no, C.user_id, C.handle_name, B.address1, B.address2, B.address3, B.freeword ";
        query += " FROM hh_hotel_present_offer A";
        query += " INNER JOIN hh_user_present_entry B ON A.form_id = B.form_id AND A.id = B.id AND B.status_flag BETWEEN 1 AND 2";
        query += " INNER JOIN hh_user_basic C ON B.user_id = C.user_id AND C.regist_status = 9 AND C.del_flag = 0";
        query += " WHERE A.form_id = ? AND A.id = ? AND A.status_flag < 9";
        // �c��̓��I�Ґ�������҂���擾(�����_����L���ɂ��邽��)
        query += " UNION ( SELECT A.form_id, A.id, B.status_flag, A.title, A.elect_no, C.user_id, C.handle_name, B.address1, B.address2, B.address3, B.freeword ";
        query += " FROM hh_hotel_present_offer A";
        query += " INNER JOIN hh_user_present_entry B ON A.form_id = B.form_id AND A.id = B.id AND B.status_flag = 0";
        query += " INNER JOIN hh_user_basic C ON B.user_id = C.user_id AND C.regist_status = 9 AND C.del_flag = 0";
        query += "  WHERE A.form_id = ? AND A.id = ? AND A.status_flag < 9";
        if ( userIdList != null )
        {
            query += " AND C.user_id IN (";
            for( i = 0 ; i < userIdList.length ; i++ )
            {
                if ( i > 0 )
                {
                    query += ",";
                }
                query += "'" + userIdList[i] + "'";
            }
            query += ") ";
        }
        query += " ORDER BY RAND()";
        if ( electNo > 0 )
        {
            // ���I���Ă��Ȃ����[�U�������_���擾���邽�߂�limit��
            query += " LIMIT 0, ? ";
        }
        query += " )";
        if ( electNo > 0 )
        {
            // �S�̂̓��I�Ґ����i�荞�ނ��߂�limit��
            query += " LIMIT 0, ? ";
        }

        count = 0;

        i = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( ++i, formId );
            prestate.setInt( ++i, id );
            prestate.setInt( ++i, formId );
            prestate.setInt( ++i, id );
            if ( electNo > 0 )
            {
                prestate.setInt( ++i, electNo );
                prestate.setInt( ++i, electNo );
            }

            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    this.userCount = result.getRow();
                }

                // �N���X�̔z���p�ӂ��A����������B
                this.userPresentEntry = new DataUserPresentEntry[this.userCount];
                this.hotelPresentOffer = new DataHotelPresentOffer[this.userCount];
                this.handleNameList = new String[this.userCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    this.userPresentEntry[count] = new DataUserPresentEntry();
                    this.hotelPresentOffer[count] = new DataHotelPresentOffer();
                    this.handleNameList[count] = new String();

                    // ���[�U�����Z�b�g
                    this.userPresentEntry[count].setFormId( result.getInt( "form_id" ) );
                    this.userPresentEntry[count].setId( result.getInt( "id" ) );
                    this.userPresentEntry[count].setStatusFlag( result.getInt( "status_flag" ) );
                    this.userPresentEntry[count].setUserId( result.getString( "user_id" ) );
                    this.userPresentEntry[count].setAddress1( result.getString( "address1" ) );
                    this.userPresentEntry[count].setAddress2( result.getString( "address2" ) );
                    this.userPresentEntry[count].setAddress3( result.getString( "address3" ) );
                    this.userPresentEntry[count].setFreeword( result.getString( "freeword" ) );

                    // this.userPresentEntry[count].setData( result );
                    // �z�e�������Z�b�g
                    this.hotelPresentOffer[count].setFormId( result.getInt( "form_id" ) );
                    this.hotelPresentOffer[count].setId( result.getInt( "id" ) );
                    this.hotelPresentOffer[count].setTitle( result.getString( "title" ) );
                    this.hotelPresentOffer[count].setElectNo( result.getInt( "elect_no" ) );
                    // this.hotelPresentOffer[count].setData( result );
                    // �n���h���l�[�����Z�b�g
                    this.handleNameList[count] = result.getString( "handle_name" );

                    // �X�V�������s�����[�UID���܂Ƃ߂Ă���
                    if ( result.getInt( "status_flag" ) == 0 )
                    {
                        if ( electCount > 0 )
                        {
                            electUser += ",";
                        }
                        electUser += "'" + result.getString( "user_id" ) + "'";
                        electCount++;
                    }

                    count++;
                }

                // ���I���Ă��Ȃ��l�������I�����Ă����B
                query = " UPDATE hh_user_present_entry SET status_flag = 1";
                query += " WHERE form_id = ? AND id = ?";
                query += " AND user_id IN (" + electUser + ")";

                i = 0;
                prestate = connection.prepareStatement( query );
                prestate.setInt( ++i, formId );
                prestate.setInt( ++i, id );
                if ( prestate.executeUpdate() <= 0 )
                {
                    Logging.info( query );
                    Logging.info( "[HotelPresentOffer.lotteryPresent()] electUser Update Error" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelPresentOffer.lotteryPresent()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(true);
    }
}
