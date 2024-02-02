/*
 * @(#)UserMap.java 1.00
 * 2007/07/31 Copyright (C) ALMEX Inc. 2007
 * ���[�U�[�}�b�v�擾�E�X�V�N���X
 */

package jp.happyhotel.user;

import java.io.Serializable;

import jp.happyhotel.common.HttpConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataUserMap;

/**
 * ���[�U�[�ɕ\������n�}���擾����B
 * 
 * @author S.Tashiro
 * @version 1.00 2009/07/30
 */
public class UserMap implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -1475978782029948447L;

    private DataUserMap       dum;

    /**
     * �f�[�^�����������܂��B
     */
    public UserMap()
    {
    }

    public DataUserMap getUserMapInfo()
    {
        return(dum);
    }

    /***
     * 
     * @param userId ���[�U�[ID
     * @return ��������(TRUE:����,FALSE:���s)
     */
    public boolean getData(String userId)
    {
        boolean ret;
        ret = false;

        this.dum = new DataUserMap();
        ret = this.dum.getData( userId );
        // �Ԃ��Ă����f�[�^�����������`�F�b�N���s���B
        if ( ret != false )
        {
            if ( this.dum.getUserId().compareTo( userId ) == 0 )
            {
                ret = true;
            }
            else
            {
                ret = false;
            }
        }
        return(ret);
    }

    /**
     * ���X�|���X��hh_user_map�ɓo�^����
     * 
     * @param userId ���[�U�[ID
     * @param con HttpConnection�N���X
     * @return
     */
    public boolean registUserMap(String userId, HttpConnection con)
    {
        boolean ret;

        ret = false;
        this.dum = new DataUserMap();

        try
        {
            if ( userId.compareTo( "" ) != 0 )
            {
                ret = this.dum.getData( userId );
                if ( ret != false && this.dum.getUserId().compareTo( userId ) == 0 )
                {
                    ret = true;
                }
                else
                {
                    ret = false;
                }
                this.dum.setUserId( userId );
                this.dum.setCoordinate( con.getCoordinate() );
                this.dum.setCoordinateDL( con.getCoordinateDL() );
                this.dum.setCoordinateDR( con.getCoordinateDR() );
                this.dum.setCoordinateUL( con.getCoordinateUL() );
                this.dum.setCoordinateUR( con.getCoordinateUR() );
                this.dum.setScale( con.getSclale() );
                this.dum.setImage( con.getImage() );
                this.dum.setContentType( con.getContentType() );
                if ( ret != false )
                {
                    ret = this.dum.updateData( userId );
                }
                else
                {
                    ret = this.dum.insertData();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionEmptySearch.registUserMap] Exception:" + e.toString() );
            ret = false;
        }

        return(ret);
    }

    /**
     * �Z����hh_user_map�ɓo�^����
     * 
     * @param userId ���[�U�[ID
     * @param address �Z��
     * @return
     */
    public boolean registAddress(String userId, String address)
    {
        boolean ret;

        ret = false;
        if ( this.dum == null )
        {
            this.dum = new DataUserMap();
        }

        try
        {
            if ( userId.compareTo( "" ) != 0 )
            {
                ret = this.dum.getData( userId );
                if ( ret != false && this.dum.getUserId().compareTo( userId ) == 0 )
                {
                    ret = true;
                }
                else
                {
                    ret = false;
                }
                this.dum.setUserId( userId );
                this.dum.setAddress( address );
                if ( ret != false )
                {
                    ret = this.dum.updateData( userId );
                }
                else
                {
                    ret = this.dum.insertData();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionEmptySearch.registAddress] Exception:" + e.toString() );
            ret = false;
        }
        return(ret);
    }

    /**
     * �摜�݂̂�hh_user_map�ɓo�^����
     * 
     * @param userId ���[�U�[ID
     * @param con HttpConnection�N���X
     * @return
     */
    public boolean registImage(String userId, HttpConnection con)
    {
        boolean ret;

        ret = false;
        this.dum = new DataUserMap();

        try
        {
            if ( userId.compareTo( "" ) != 0 )
            {
                ret = this.dum.getData( userId );
                if ( ret != false && this.dum.getUserId().compareTo( userId ) == 0 )
                {
                    ret = true;
                }
                else
                {
                    ret = false;
                }
                this.dum.setUserId( userId );
                this.dum.setImage( con.getImage() );
                this.dum.setContentType( con.getContentType() );
                if ( ret != false )
                {
                    ret = this.dum.updateData( userId );
                }
                else
                {
                    ret = this.dum.insertData();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionEmptySearch.registUserMap] Exception:" + e.toString() );
            ret = false;
        }

        return(ret);
    }

}
