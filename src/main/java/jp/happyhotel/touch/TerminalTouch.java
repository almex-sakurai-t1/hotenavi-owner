package jp.happyhotel.touch;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApHotelTerminal;
import jp.happyhotel.data.DataApTerminalTouch;
import jp.happyhotel.hotel.HotelCi;

/**
 * �[���^�b�`�������N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2011/05/19
 */
public class TerminalTouch implements Serializable
{

    /**
     *
     */
    private static final long   serialVersionUID = -6199069863331936331L;
    // �[�����f�[�^
    private DataApTerminalTouch terminal         = new DataApTerminalTouch();
    private static int          KIND_DATE        = 2;
    private static int          KIND_TIME        = 1;

    //
    public DataApTerminalTouch getTerminal()
    {
        return terminal;
    }

    public void setTerminal(DataApTerminalTouch terminal)
    {
        this.terminal = terminal;
    }

    /**
     * 
     * @param id
     * @param terminalId
     * @return
     */
    public boolean getTerminalTouch(int id, int terminalId)
    {
        boolean ret = false;

        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM ap_terminal_touch WHERE id = ? AND terminal_id = ? ORDER BY touch_seq DESC";
        query += " LIMIT 0,1";
        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, terminalId );
            result = prestate.executeQuery();

            if ( result != null )
            {

                if ( result.next() != false )
                {
                    this.terminal.setData( result );
                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
            if ( count > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelTerminal.getHotelTerminal()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

    public boolean getTerminalTouchFromRoomNo(int id, int roomNo)
    {
        boolean ret = false;

        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM ap_terminal_touch WHERE id = ? AND room_no = ? ORDER BY touch_date DESC,touch_time DESC ";
        query += " LIMIT 0,1";
        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, roomNo );
            result = prestate.executeQuery();

            if ( result != null )
            {

                if ( result.next() != false )
                {
                    this.terminal.setData( result );
                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
            if ( count > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelTerminal.getHotelTerminalFromRoomNo()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

    /***
     * �[���^�b�`�f�[�^�o�^
     * 
     * @param tt
     * @param hotelId �z�e��ID
     * @param termId �[��ID
     * @param userId ���[�UID�i�����̏ꍇ�͋󔒁j
     * @param idm �f�o�C�X�g�[�N���AFelicaID
     * @param roomNo �����ԍ�
     * @param touchIp IP�A�h���X
     * @param touchUseragent ���[�U�G�[�W�F���g
     * @return
     */
    public boolean registTerminalTouch(DataApTerminalTouch tt, int hotelId, int termId, String userId, String idm, int roomNo, String touchIp, String touchUseragent, int ciSeq)
    {
        boolean ret = false;

        tt.setId( hotelId );
        tt.setTerminalId( termId );
        tt.setTouchSeq( 0 );
        tt.setUserId( userId );
        tt.setIdm( idm );
        tt.setRoomNo( roomNo );
        tt.setCiSeq( ciSeq );
        // tt.setTerminalNo(0);
        tt.setTouchDate( Integer.parseInt( DateEdit.getDate( KIND_DATE ) ) );
        tt.setTouchTime( Integer.parseInt( DateEdit.getTime( KIND_TIME ) ) );
        tt.setRegistDate( Integer.parseInt( DateEdit.getDate( KIND_DATE ) ) );
        tt.setRegistTime( Integer.parseInt( DateEdit.getTime( KIND_TIME ) ) );
        tt.setTouchIp( touchIp );
        tt.setTouchUseragent( touchUseragent );
        ret = tt.insertData();

        return ret;
    }

    /**
     * 
     * @param id
     * @param terminalId
     * @return
     */
    public boolean registData(int id, HotelCi hc, int roomNo)
    {
        boolean ret = false;
        DataApHotelTerminal daht = new DataApHotelTerminal();
        TerminalTouch tt = new TerminalTouch();
        int termId = 0;

        ret = daht.getDataByRoomNo( id, roomNo );
        // �����̒[���f�[�^���擾�ł���
        if ( ret != false )
        {
            termId = daht.getTerminalId();
            // �[���ԍ��ƃz�e��ID����^�b�`�f�[�^���擾
            tt.getTerminalTouch( id, termId );
            // �\��ō��ꂽ�`�F�b�N�C���f�[�^���A�[���f�[�^�����V�����ꍇ�͍X�V����B
            if ( hc.getHotelCi().getSeq() > tt.getTerminal().getCiSeq() )
            {
                tt.getTerminal().setCiSeq( hc.getHotelCi().getSeq() );
                tt.getTerminal().setTouchDate( hc.getHotelCi().getVisitDate() );
                tt.getTerminal().setTouchTime( hc.getHotelCi().getVisitTime() );
                tt.getTerminal().setTouchDate( Integer.parseInt( DateEdit.getDate( KIND_DATE ) ) );
                tt.getTerminal().setTouchTime( Integer.parseInt( DateEdit.getTime( KIND_TIME ) ) );
                ret = tt.getTerminal().insertData();
            }

        }

        return ret;

    }

}
