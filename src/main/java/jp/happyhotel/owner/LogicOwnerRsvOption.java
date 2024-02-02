package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.ConvertTime;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReplaceString;

/**
 * �I�v�V�����ݒ�r�W�l�X���W�b�N
 */
public class LogicOwnerRsvOption implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = 5308398067444805918L;

    private FormOwnerRsvOption frm;

    /* �t�H�[���I�u�W�F�N�g */
    public FormOwnerRsvOption getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerRsvOption frm)
    {
        this.frm = frm;
    }

    /**
     * ���̃I�v�V����ID�擾
     * 
     * @param �Ȃ�
     * @return ���̃I�v�V����ID
     */
    public int getNewOptId() throws Exception
    {
        int newOptId = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = query + "SELECT";
        query = query + "  CASE MAX(option_id) IS NULL ";
        query = query + "       WHEN 1 THEN 1 ";
        query = query + "       ELSE MAX(option_id) + 1 ";
        query = query + "  END optID ";
        query = query + "FROM hh_rsv_option ";
        query = query + "WHERE id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                newOptId = result.getInt( "optID" );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvOption.getNewOptId] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(newOptId);
    }

    /**
     * ���̃T�u�I�v�V����ID�擾
     * 
     * @param �Ȃ�
     * @return ���̃T�u�I�v�V����ID
     */
    public int getNewOptSubId() throws Exception
    {
        int newSubOptId = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = query + "SELECT";
        query = query + "  CASE MAX(option_sub_id) IS NULL ";
        query = query + "       WHEN 1 THEN 1 ";
        query = query + "       ELSE MAX(option_sub_id) + 1 ";
        query = query + "  END subID ";
        query = query + "FROM hh_rsv_option ";
        query = query + "WHERE id = ? ";
        query = query + "  AND option_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            prestate.setInt( 2, frm.getSelOptId() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                newSubOptId = result.getInt( "subID" );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvOption.getNewOptSubId] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(newSubOptId);
    }

    /**
     * �K�{�I�v�V�������o�^
     * 
     * @param
     * @return �Ȃ�
     */
    public void registOption() throws Exception
    {
        int optId = 0;
        int subOptId = 0;
        String optNm = "";
        String optNmSearch = "";
        String subOptNm = "";
        int dispIdx = 0;
        int delSubOptId = 0;
        boolean delFlg = false;

        optId = frm.getSelOptId();
        optNm = frm.getOptionNm_Must();
        optNmSearch = frm.getOptionNm_MustSearch();
        dispIdx = Integer.parseInt( frm.getDispIdx() );

        // �폜����
        for( int i = 0 ; i < frm.getDelOptSubIdList().size() ; i++ )
        {
            delSubOptId = frm.getDelOptSubIdList().get( i );
            deleteOption( delSubOptId );
        }

        // �ǉ���X�V����
        for( int i = 0 ; i < frm.getOptSubIdMustList().size() ; i++ )
        {
            subOptId = frm.getOptSubIdMustList().get( i );
            subOptNm = frm.getOptSubNmMustList().get( i );
            delFlg = false;

            for( int j = 0 ; j < frm.getDelOptSubIdList().size() ; j++ )
            {
                if ( subOptId == frm.getDelOptSubIdList().get( j ) )
                {
                    // �폜�Ώ�
                    delFlg = true;
                    break;
                }
            }

            if ( delFlg == false )
            {
                // �I�v�V������񂪑��݂��邩
                if ( existsOption( optId, subOptId ) == true )
                {
                    // ���݂���ꍇ�͍X�V
                    updOption( subOptId, optNm, optNmSearch, subOptNm, dispIdx );
                }
                else
                {
                    // ���݂��Ȃ��ꍇ�͐V�K�ǉ�
                    insertOption( optId, subOptId, optNm, optNmSearch, subOptNm, dispIdx );
                }
            }
        }
    }

    /**
     * �I�v�V������񑶍݃`�F�b�N
     * 
     * @param optId �I�v�V����ID
     * @param subOptId �T�u�I�v�V����ID
     * @return true:���݂���AFalse�F���݂��Ȃ�
     */
    private boolean existsOption(int optId, int subOptId) throws Exception
    {
        boolean isExists = false;
        int cnt = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = query + "SELECT COUNT(*) AS CNT FROM hh_rsv_option ";
        query = query + "WHERE id = ? ";
        query = query + "  AND option_id = ? ";
        query = query + "  AND option_sub_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            prestate.setInt( 2, optId );
            prestate.setInt( 3, subOptId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                cnt = result.getInt( "CNT" );
            }
            if ( cnt != 0 )
            {
                isExists = true;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvOption.existsOption] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(isExists);
    }

    /**
     * �K�{�I�v�V�������V�K�ǉ�
     * 
     * @param optId �I�v�V����ID
     * @param subOptId �T�u�I�v�V����ID
     * @param optNm �I�v�V������
     * @param optNmSearch �����\���I�v�V������
     * @param subOptNm �T�u�I�v�V������
     * @param dispIdx �\����
     */
    private void insertOption(int optId, int subOptId, String optNm, String optNmSearch, String subOptNm, int dispIdx) throws Exception
    {
        String query = "";
        Connection conn = null;
        PreparedStatement prestate = null;

        query = query + "INSERT hh_rsv_option SET ";
        query = query + "id = ?, ";
        query = query + "option_id = ?, ";
        query = query + "option_sub_id = ?, ";
        query = query + "option_name = ?, ";
        query = query + "search_option_name = ?, ";
        query = query + "option_sub_name = ?, ";
        query = query + "option_charge = ?, ";
        query = query + "max_quantity = ?, ";
        query = query + "input_max_quantity = ?, ";
        query = query + "cancel_limit_date = ?, ";
        query = query + "cancel_limit_time = ?, ";
        query = query + "option_flag = ?, ";
        query = query + "image_pc = ?, ";
        query = query + "image_gif = ?, ";
        query = query + "image_png = ?, ";
        query = query + "disp_index = ?, ";
        query = query + "hotel_id = ?,";
        query = query + "user_id = ?, ";
        query = query + "last_update = ?, ";
        query = query + "last_uptime = ?  ";

        try
        {
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            prestate.setInt( 2, frm.getSelOptId() );
            prestate.setInt( 3, subOptId );
            prestate.setString( 4, ConvertCharacterSet.convForm2Db( optNm ) );
            prestate.setString( 5, ConvertCharacterSet.convForm2Db( optNmSearch ) );
            prestate.setString( 6, ConvertCharacterSet.convForm2Db( subOptNm ) );
            prestate.setInt( 7, 0 );
            prestate.setInt( 8, 0 );
            prestate.setInt( 9, 0 );
            prestate.setInt( 10, 0 );
            prestate.setInt( 11, 0 );
            prestate.setInt( 12, 1 );
            prestate.setString( 13, "" );
            prestate.setString( 14, "" );
            prestate.setString( 15, "" );
            prestate.setInt( 16, dispIdx );
            prestate.setString( 17, frm.getOwnerHotelID() );
            prestate.setInt( 18, frm.getUserId() );
            prestate.setInt( 19, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 20, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.executeUpdate();

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvOption.insertOption] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( conn );
        }
    }

    /**
     * �K�{�I�v�V�������V�K�ǉ�
     * 
     * @param subOptId �T�u�I�v�V����ID
     */
    private void deleteOption(int subOptId) throws Exception
    {
        String query = "";
        Connection conn = null;
        PreparedStatement prestate = null;
        query = query + "DELETE FROM hh_rsv_option ";
        query = query + "WHERE id = ? ";
        query = query + "  AND option_id = ? ";
        query = query + "  AND option_sub_id = ? ";

        try
        {
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            prestate.setInt( 2, frm.getSelOptId() );
            prestate.setInt( 3, subOptId );
            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvOption.deleteOption] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( conn );
        }
    }

    /**
     * �K�{�I�v�V�������X�V
     * 
     * @param optId �I�v�V����ID
     * @param subOptId �T�u�I�v�V����ID
     * @param optNm �I�v�V������
     * @param searchOptNm �����\���I�v�V������
     * @param subOptNm �T�u�I�v�V������
     * @param dispIdx �\����
     */
    private void updOption(int subOptId, String optNm, String optNmSearch, String subOptNm, int dispIdx) throws Exception
    {
        String query = "";
        Connection conn = null;
        PreparedStatement prestate = null;

        query = query + "UPDATE hh_rsv_option SET ";
        query = query + "option_name = ?, ";
        query = query + "search_option_name = ?, ";
        query = query + "option_sub_name = ?, ";
        query = query + "disp_index = ?, ";
        query = query + "hotel_id = ?,";
        query = query + "user_id = ?, ";
        query = query + "last_update = ?, ";
        query = query + "last_uptime = ?  ";
        query = query + "WHERE id = ? ";
        query = query + "  AND option_id = ? ";
        query = query + "  AND option_sub_id = ? ";

        try
        {
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );
            prestate.setString( 1, ConvertCharacterSet.convForm2Db( optNm ) );
            prestate.setString( 2, ConvertCharacterSet.convForm2Db( optNmSearch ) );
            prestate.setString( 3, ConvertCharacterSet.convForm2Db( subOptNm ) );
            prestate.setInt( 4, dispIdx );
            prestate.setString( 5, frm.getOwnerHotelID() );
            prestate.setInt( 6, frm.getUserId() );
            prestate.setInt( 7, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 8, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 9, frm.getSelHotelId() );
            prestate.setInt( 10, frm.getSelOptId() );
            prestate.setInt( 11, subOptId );
            prestate.executeUpdate();

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvOption.updOption] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( conn );
        }
    }

    /**
     * �I�v�V�������擾
     * 
     * @param selKbn �����敪(0:�ʏ�I�v�V�����A1:�K�{�I�v�V����)
     * @return �Ȃ�
     */
    public void getOpt(int selKbn) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int rowCnt = 0;
        ArrayList<Integer> optSubIdMustList = new ArrayList<Integer>();
        ArrayList<String> optSubNmMustList = new ArrayList<String>();
        String cancelTimeHH = "";
        String cancelTimeMM = "";
        int maxSubOptID = 1;

        query = query + "SELECT * FROM hh_rsv_option ";
        query = query + "WHERE id = ? ";
        query = query + "  AND option_id = ? ";
        query = query + "  AND option_flag = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            prestate.setInt( 2, frm.getSelOptId() );
            prestate.setInt( 3, selKbn );
            result = prestate.executeQuery();

            while( result.next() )
            {
                if ( rowCnt == 0 )
                {
                    // �K�{�I�v�V����
                    frm.setOptionNm_Must( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "option_name" ) ) ) );
                    frm.setOptionNmMustView( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "option_name" ) ) ) );
                    frm.setOptionNm_MustSearch( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "search_option_name" ) ) ) );
                    frm.setOptionNm_MustSearchView( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "search_option_name" ) ) ) );
                    frm.setDispIdx( result.getString( "disp_index" ) );

                    // �ʏ�I�v�V����
                    frm.setOptionNm_Comm( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "option_name" ) ) ) );
                    frm.setOptionNmCommView( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "option_name" ) ) ) );
                    frm.setOptionNm_CommSearch( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "search_option_name" ) ) ) );
                    frm.setOptionNm_CommSearchView( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "search_option_name" ) ) ) );
                    frm.setOptCharge( result.getString( "option_charge" ) );
                    frm.setOptChargeView( String.format( "%1$,3d", result.getInt( "option_charge" ) ) );
                    frm.setMaxQuantity( result.getString( "max_quantity" ) );
                    frm.setMaxQuantityView( result.getString( "max_quantity" ) );
                    frm.setInpMaxQuantity( result.getString( "input_max_quantity" ) );
                    frm.setInpMaxQuantityView( result.getString( "input_max_quantity" ) );
                    frm.setCancelLimitDate( result.getString( "cancel_limit_date" ) );
                    frm.setCancelLimitDateView( result.getString( "cancel_limit_date" ) );
                    cancelTimeHH = ConvertTime.convTimeHH( result.getInt( "cancel_limit_time" ) );
                    cancelTimeMM = ConvertTime.convTimeMM( result.getInt( "cancel_limit_time" ) );
                    frm.setCancelLimitTimeHH( cancelTimeHH );
                    frm.setCancelLimitTimeHHView( cancelTimeHH );
                    frm.setCancelLimitTimeMM( cancelTimeMM );
                    frm.setCancelLimitTimeMMView( cancelTimeMM );
                    frm.setDispIdx( result.getString( "disp_index" ) );
                    frm.setDispIndexComm( result.getString( "disp_index" ) );

                }
                optSubIdMustList.add( result.getInt( "option_sub_id" ) );
                optSubNmMustList.add( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "option_sub_name" ) ) ) );

                rowCnt++;
            }

            frm.setOptSubIdMustList( optSubIdMustList );
            frm.setOptSubNmMustList( optSubNmMustList );
            frm.setMaxRow( rowCnt );

            // �ő�̃T�u�I�v�V����ID�擾
            maxSubOptID = getNewOptSubId();
            frm.setMaxSubOptId( maxSubOptID - 1 );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvOption.getOpt] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * �ʏ�I�v�V�������o�^
     * 
     * @param
     * @return �Ȃ�
     */
    public void registCommOption() throws Exception
    {
        int optId = 0;

        optId = frm.getSelOptId();

        // �I�v�V������񂪑��݂��邩
        if ( existsOption( optId, 1 ) == true )
        {
            // ���݂���ꍇ�͍X�V
            updCommOption();
        }
        else
        {
            // ���݂��Ȃ��ꍇ�͐V�K�ǉ�
            insertCommOption();
        }
    }

    /**
     * �ʏ�I�v�V�������V�K�ǉ�
     * 
     * @param �Ȃ�
     */
    private void insertCommOption() throws Exception
    {
        String query = "";
        Connection conn = null;
        PreparedStatement prestate = null;
        int cancelLimitTime = 0;

        query = query + "INSERT hh_rsv_option SET ";
        query = query + "id = ?, ";
        query = query + "option_id = ?, ";
        query = query + "option_sub_id = ?, ";
        query = query + "option_name = ?, ";
        query = query + "search_option_name = ?, ";
        query = query + "option_sub_name = ?, ";
        query = query + "option_charge = ?, ";
        query = query + "max_quantity = ?, ";
        query = query + "input_max_quantity = ?, ";
        query = query + "cancel_limit_date = ?, ";
        query = query + "cancel_limit_time = ?, ";
        query = query + "option_flag = ?, ";
        query = query + "image_pc = ?, ";
        query = query + "image_gif = ?, ";
        query = query + "image_png = ?, ";
        query = query + "disp_index = ?, ";
        query = query + "hotel_id = ?,";
        query = query + "user_id = ?, ";
        query = query + "last_update = ?, ";
        query = query + "last_uptime = ?  ";

        try
        {
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            prestate.setInt( 2, frm.getSelOptId() );
            prestate.setInt( 3, 1 );
            prestate.setString( 4, ConvertCharacterSet.convForm2Db( frm.getOptionNm_Comm() ) );
            prestate.setString( 5, ConvertCharacterSet.convForm2Db( frm.getOptionNm_CommSearch() ) );
            prestate.setString( 6, "" );
            prestate.setInt( 7, Integer.parseInt( frm.getOptCharge() ) );
            prestate.setInt( 8, Integer.parseInt( frm.getMaxQuantity() ) );
            prestate.setInt( 9, Integer.parseInt( frm.getInpMaxQuantity() ) );
            prestate.setInt( 10, Integer.parseInt( frm.getCancelLimitDate() ) );
            cancelLimitTime = ConvertTime.convTimeSS( Integer.parseInt( frm.getCancelLimitTimeHH() ), Integer.parseInt( frm.getCancelLimitTimeMM() ), 2 );
            prestate.setInt( 11, cancelLimitTime );
            prestate.setInt( 12, 0 );
            prestate.setString( 13, "" );
            prestate.setString( 14, "" );
            prestate.setString( 15, "" );
            prestate.setInt( 16, Integer.parseInt( frm.getDispIndexComm() ) );
            prestate.setString( 17, frm.getOwnerHotelID() );
            prestate.setInt( 18, frm.getUserId() );
            prestate.setInt( 19, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 20, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.executeUpdate();

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvOption.insertCommOption] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( conn );
        }
    }

    /**
     * �ʏ�I�v�V�������X�V
     * 
     * @param �Ȃ�
     */
    private void updCommOption() throws Exception
    {
        String query = "";
        Connection conn = null;
        PreparedStatement prestate = null;
        int cancelLimitTime = 0;

        query = query + "UPDATE hh_rsv_option SET ";
        query = query + "option_name = ?, ";
        query = query + "search_option_name = ?, ";
        query = query + "option_charge = ?, ";
        query = query + "max_quantity = ?, ";
        query = query + "input_max_quantity = ?, ";
        query = query + "cancel_limit_date = ?, ";
        query = query + "cancel_limit_time = ?, ";
        query = query + "disp_index = ?, ";
        query = query + "hotel_id = ?,";
        query = query + "user_id = ?, ";
        query = query + "last_update = ?, ";
        query = query + "last_uptime = ?  ";
        query = query + "WHERE id = ? ";
        query = query + "  AND option_id = ? ";
        query = query + "  AND option_sub_id = ? ";

        try
        {
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );
            prestate.setString( 1, ConvertCharacterSet.convForm2Db( frm.getOptionNm_Comm() ) );
            prestate.setString( 2, ConvertCharacterSet.convForm2Db( frm.getOptionNm_CommSearch() ) );
            prestate.setInt( 3, Integer.parseInt( frm.getOptCharge() ) );
            prestate.setInt( 4, Integer.parseInt( frm.getMaxQuantity() ) );
            prestate.setInt( 5, Integer.parseInt( frm.getInpMaxQuantity() ) );
            prestate.setInt( 6, Integer.parseInt( frm.getCancelLimitDate() ) );
            cancelLimitTime = ConvertTime.convTimeSS( Integer.parseInt( frm.getCancelLimitTimeHH() ), Integer.parseInt( frm.getCancelLimitTimeMM() ), 2 );
            prestate.setInt( 7, cancelLimitTime );
            prestate.setInt( 8, Integer.parseInt( frm.getDispIndexComm() ) );
            prestate.setString( 9, frm.getOwnerHotelID() );
            prestate.setInt( 10, frm.getUserId() );
            prestate.setInt( 11, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 12, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 13, frm.getSelHotelId() );
            prestate.setInt( 14, frm.getSelOptId() );
            prestate.setInt( 15, 1 );
            prestate.executeUpdate();

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvOption.updCommOption] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( conn );
        }
    }

    /**
     * �I�v�V�������
     * 
     * @param int hotelId �z�e��ID
     * @param int optId �I�v�V����ID
     */
    public void deleteOption(int hotelId, int optId) throws Exception
    {
        String query = "";
        Connection conn = null;
        PreparedStatement prestate = null;
        query = query + "DELETE FROM hh_rsv_option ";
        query = query + "WHERE id = ? ";
        query = query + "  AND option_id = ? ";

        try
        {
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, optId );
            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvOption.deleteOption] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( conn );
        }
    }

    /**
     * �\��f�[�^�����݂��邩
     * �X�e�[�^�X�͊֌W�Ȃ�
     * 
     * @param int hotelId �z�e��ID
     * @param int optId �I�v�V����ID
     * @return true:���݂���Afalse:���݂��Ȃ�
     */
    public boolean existsReserve(int hotelId, int optId) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = false;

        query = query + "SELECT COUNT(*) AS CNT FROM hh_rsv_rel_reserve_option ";
        query = query + "WHERE id = ? ";
        query = query + "  AND option_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, optId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                if ( result.getInt( "CNT" ) > 0 )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvOption.existsReserve] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * �v�����ɃI�v�V�������o�^����Ă��邩
     * 
     * @param int hotelId �z�e��ID
     * @param int optId �I�v�V����ID
     * @param int selKbn �����敪(1:�v�����A2:�v����������)
     * @return true:���݂���Afalse:���݂��Ȃ�
     */
    public boolean existsPlanOption(int hotelId, int optId, int selKbn) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = false;

        query = query + "SELECT COUNT(*) AS CNT FROM ";
        if ( selKbn == 1 )
        {
            query = query + " hh_rsv_rel_plan_option ";
        }
        else
        {
            query = query + " hh_rsv_rel_plan_option_draft ";
        }
        query = query + "WHERE id = ? ";
        query = query + "  AND option_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, optId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                if ( result.getInt( "CNT" ) > 0 )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvOption.existsPlanOption] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }
}
