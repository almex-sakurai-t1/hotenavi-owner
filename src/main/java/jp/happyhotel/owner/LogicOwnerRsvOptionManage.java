package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.ReplaceString;

/**
 * �I�v�V�����Ǘ��N���X
 */
public class LogicOwnerRsvOptionManage implements Serializable
{
    private static final long        serialVersionUID = 8963958152794029979L;
    private FormOwnerRsvOptionManage frm;

    /* �t�H�[���I�u�W�F�N�g */
    public FormOwnerRsvOptionManage getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerRsvOptionManage frm)
    {
        this.frm = frm;
    }

    /**
     * �I�v�V�������擾
     * 
     * @param �Ȃ�
     * @return
     */
    public void getOption() throws Exception
    {
        try
        {
            // �K�{�I�v�V�������擾
            getMustOption();

            // �ʏ�I�v�V�������擾
            getCommOption();
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvOptionManage.getOption] Exception=" + e.toString() );
            throw new Exception( e );
        }
    }

    /**
     * �K�{�I�v�V�������擾
     * 
     * @param �Ȃ�
     * @return
     */
    private void getMustOption() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;
        ArrayList<Integer> optionFlagList = new ArrayList<Integer>();
        ArrayList<Integer> optionIdList = new ArrayList<Integer>();
        ArrayList<Integer> optionSubIdList = new ArrayList<Integer>();
        ArrayList<String> optionNmList = new ArrayList<String>();
        ArrayList<String> optionSubNmList = new ArrayList<String>();
        ArrayList<Integer> optionChargeList = new ArrayList<Integer>();
        ArrayList<String> dispIndexList = new ArrayList<String>();
        ArrayList<Integer> newOptionFlagList = new ArrayList<Integer>();
        ArrayList<Integer> newOptionIdList = new ArrayList<Integer>();
        ArrayList<String> newOptionNmList = new ArrayList<String>();
        ArrayList<String> newOptionSubNmStrList = new ArrayList<String>();
        ArrayList<String> newOptionSubNmList = new ArrayList<String>();
        ArrayList<String> newDispIndexList = new ArrayList<String>();
        int newOptId = 0;
        int orgOptId = 0;
        String subOptNm = "";
        String dispIdx = "";
        String orgOptNm = "";

        query = createSelSQL();
        query = query + " AND option_flag = 1 ";
        query = query + " ORDER BY disp_index, option_id, option_sub_id";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                optionFlagList.add( result.getInt( "option_flag" ) );
                optionIdList.add( result.getInt( "option_id" ) );
                optionSubIdList.add( result.getInt( "option_sub_id" ) );
                optionNmList.add( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( (CheckString.checkStringForNull( result.getString( "option_name" ) )) ) ) );
                optionSubNmList.add( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( (CheckString.checkStringForNull( result.getString( "option_sub_name" ) )) ) ) );
                optionChargeList.add( result.getInt( "option_charge" ) );
                dispIndexList.add( result.getString( "disp_index" ) );
            }

            // ���R�[�h�����擾
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // �T�u�I�v�V�������f�[�^�𐮌`
            for( int i = 0 ; i < optionIdList.size() ; i++ )
            {
                newOptId = optionIdList.get( i );

                if ( i == 0 )
                {
                    orgOptId = optionIdList.get( i );
                    orgOptNm = optionNmList.get( i );
                    dispIdx = dispIndexList.get( i );
                }

                if ( orgOptId == newOptId )
                {
                    subOptNm = subOptNm + optionSubNmList.get( i ) + "<br />";
                }
                else
                {
                    // �O��̃I�v�V�������e���i�[
                    newOptionIdList.add( orgOptId );
                    newOptionNmList.add( orgOptNm );
                    newOptionSubNmStrList.add( subOptNm );
                    newDispIndexList.add( dispIdx );

                    // ���̃I�v�V������ݒ�
                    orgOptId = newOptId;
                    subOptNm = "";
                    subOptNm = subOptNm + optionSubNmList.get( i ) + "<br />";
                    orgOptNm = optionNmList.get( i );
                    dispIdx = dispIndexList.get( i );
                }
            }
            newOptionIdList.add( orgOptId );
            newOptionNmList.add( orgOptNm );
            newOptionSubNmStrList.add( subOptNm );
            newDispIndexList.add( dispIdx );

            // �I�v�V�����f�[�^�𐮌`
            orgOptId = 0;
            newOptId = 0;
            for( int i = 0 ; i < newOptionIdList.size() ; i++ )
            {
                orgOptId = newOptionIdList.get( i );

                newOptionFlagList.add( 1 );
                newOptionNmList.add( newOptionNmList.get( i ) );
                newOptionSubNmList.add( newOptionSubNmStrList.get( i ) );
            }

            // �t�H�[���ɃZ�b�g
            frm.setOptionFlagList( optionFlagList );
            frm.setOptionIdList( newOptionIdList );
            frm.setOptionNmList( newOptionNmList );
            frm.setOptionSubNmList( newOptionSubNmList );
            frm.setOptionChargeList( optionChargeList );
            frm.setDispIndexList( newDispIndexList );

            // �Y���f�[�^���Ȃ��ꍇ
            if ( count == 0 )
            {
                frm.setMustErrMsg( Message.getMessage( "erro.30001", "�K�{�I�v�V����" ) );
                return;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvOptionManage.getMustOption] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * �I�v�V�������擾
     * 
     * @param �Ȃ�
     * @return
     */
    private void getCommOption() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;
        ArrayList<Integer> optionFlagList = new ArrayList<Integer>();
        ArrayList<Integer> optionIdList = new ArrayList<Integer>();
        ArrayList<Integer> optionSubIdList = new ArrayList<Integer>();
        ArrayList<String> optionNmList = new ArrayList<String>();
        ArrayList<String> optionSubNmList = new ArrayList<String>();
        ArrayList<String> optionChargeList = new ArrayList<String>();
        ArrayList<String> dispIndexList = new ArrayList<String>();

        query = createSelSQL();
        query = query + " AND option_flag = 0 ";
        query = query + " ORDER BY disp_index, option_id, option_sub_id";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                optionFlagList.add( result.getInt( "option_flag" ) );
                optionIdList.add( result.getInt( "option_id" ) );
                optionSubIdList.add( result.getInt( "option_sub_id" ) );
                optionNmList.add( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( (CheckString.checkStringForNull( result.getString( "option_name" ) )) ) ) );
                optionSubNmList.add( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( (CheckString.checkStringForNull( result.getString( "option_sub_name" ) )) ) ) );
                optionChargeList.add( String.format( "%1$,3d", result.getInt( "option_charge" ) ) );
                dispIndexList.add( result.getString( "disp_index" ) );
            }

            // ���R�[�h�����擾
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // �t�H�[���ɃZ�b�g
            frm.setComOptionFlagList( optionFlagList );
            frm.setComOptionIdList( optionIdList );
            frm.setComOptionSubIdList( optionSubIdList );
            frm.setComOptionNmList( optionNmList );
            frm.setComOptionSubNmList( optionSubNmList );
            frm.setComOptionChargeList( optionChargeList );
            frm.setComDispIndexList( dispIndexList );

            // �Y���f�[�^���Ȃ��ꍇ
            if ( count == 0 )
            {
                frm.setCommErrMsg( Message.getMessage( "erro.30001", "�ʏ�I�v�V����" ) );
                return;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvOptionManage.getCommOption] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * �I�v�V�����擾�pSQL�쐬
     * 
     * @param �Ȃ�
     * @return SQL��
     */
    private String createSelSQL()
    {
        String query = "";

        query = query + "SELECT ";
        query = query + "option_id, option_sub_id, option_name, option_sub_name, option_charge, option_flag, disp_index ";
        query = query + "FROM hh_rsv_option ";
        query = query + " WHERE id = ? ";

        return(query);
    }

    /**
     * �\�����X�V
     * 
     * @param dispIdx �\����
     * @param optId �I�v�V����ID
     */
    public boolean updDispIndex(int dispIdx, int optId) throws Exception
    {
        String query = "";
        Connection conn = null;
        PreparedStatement prestate = null;
        boolean ret = false;
        int result = 0;

        query = query + "UPDATE hh_rsv_option SET ";
        query = query + "disp_index = ?, ";
        query = query + "user_id = ?, ";
        query = query + "last_update = ?, ";
        query = query + "last_uptime = ?  ";
        query = query + "WHERE id = ? ";
        query = query + "  AND option_id = ? ";

        try
        {
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, dispIdx );
            prestate.setInt( 2, frm.getUserId() );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 4, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 5, frm.getSelHotelId() );
            prestate.setInt( 6, optId );
            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvOption.updDispIndex] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( conn );
        }

        return(ret);
    }
}
