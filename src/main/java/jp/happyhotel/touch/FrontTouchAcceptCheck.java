package jp.happyhotel.touch;

import java.io.Serializable;

import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApHotelTerminal;

/**
 * �t�����g�^�b�`��t���N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2011/05/19
 */
public class FrontTouchAcceptCheck implements Serializable
{
    static final int          FRONT_TOUCH_OK   = 1;                   // �t�����g�^�b�`OK
    static final int          FRONT_TOUCH_NG   = 0;                   // �t�����g�^�b�`NG

    /**
     * �[���̎�t�����`�F�b�N����
     */
    private static final long serialVersionUID = 8597109204671730950L;

    public boolean check(int id)
    {
        DataApHotelTerminal daht = new DataApHotelTerminal();
        boolean ret = false;

        ret = daht.getFrontTerminal( id );
        if ( ret != false )
        {
            ret = this.check( daht );
        }

        return ret;
    }

    /****
     * 
     * @param daht
     * @return
     */
    public boolean check(DataApHotelTerminal daht)
    {
        int limitDate = 0;
        int limitTime = 0;
        int frontTouchStat = 0;
        int nowDate = 0;
        int nowTime = 0;
        boolean ret = false;

        // �z�e���̏�񂪓����Ă���΃f�[�^�������Ă���̂Œ��g���`�F�b�N
        if ( daht.getId() > 0 )
        {
            frontTouchStat = daht.getFrontTouchState();
            limitDate = daht.getLimitDate();
            limitTime = daht.getLimitTime();
            nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );

            //
            if ( frontTouchStat == FRONT_TOUCH_OK )
            {
                // ���ݎ����Ɛ������A�����������Z�b�g���Ĕ�r���ėL���������؂�Ă�����G���[
                if ( DateEdit.isValidDate( limitDate, limitTime, nowDate, nowTime, 0, 0 ) != false )
                {
                    ret = true;
                }
                else
                {
                    daht.setLastUpdate( nowDate );
                    daht.setLastUptime( nowTime );
                    daht.setFrontTouchState( FRONT_TOUCH_NG );
                    // �f�[�^���X�V
                    daht.updateData( daht.getId(), daht.getTerminalId() );

                    ret = false;
                }
            }
            else
            {
                ret = false;
            }

        }

        return ret;
    }

    /**
     * �t�����g�̒[���f�[�^���g�p�s�ɂ���
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param id �n�s�z�eID
     * @return
     */
    public boolean updateFrontTouchDisable(int id)
    {
        DataApHotelTerminal daht = new DataApHotelTerminal();
        boolean ret;
        ret = false;

        try
        {
            ret = daht.getFrontTerminal( id );
            if ( ret != false )
            {
                daht.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daht.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daht.setFrontTouchState( FRONT_TOUCH_NG );
                // �f�[�^���X�V
                ret = daht.updateFrontData( daht.getId() );
                // ret = daht.updateData( daht.getId(), daht.getTerminalId() );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelTerminal.updateFrontTouchDisable(id)] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
        }
        return(ret);
    }

    /**
     * �t�����g�̒[���f�[�^���g�p�ɂ���
     * 
     * @param id �n�s�z�eID
     * @param frontTouchLimit ��������
     * @return
     */
    public boolean updateFrontTouchEnable(int id, int frontTouchLimit)
    {
        DataApHotelTerminal daht = new DataApHotelTerminal();
        int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
        int limitTime = Integer.parseInt( DateEdit.elapsedTime( nowDate, nowTime, 4, frontTouchLimit ) );
        int limitDate = nowDate;
        if ( limitTime < nowTime )
        {
            limitDate = DateEdit.addDay( nowDate, 1 );
        }

        boolean ret;
        ret = false;

        try
        {
            ret = daht.getFrontTerminal( id );
            if ( ret != false )
            {
                daht.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daht.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daht.setLimitDate( limitDate );
                daht.setLimitTime( limitTime );

                // DateEdit.elapsedTime( date, time, kind, elapsedTime )
                daht.setFrontTouchState( FRONT_TOUCH_OK );
                // �f�[�^���X�V
                ret = daht.updateFrontData( daht.getId() );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelTerminal.updateFrontTouchEnable(id)] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
        }
        return(ret);
    }
}
