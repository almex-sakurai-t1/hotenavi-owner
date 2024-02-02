/*
 * @(#)UserPoint.java 1.00 2007/08/23 Copyright (C) ALMEX Inc. 2007 ���[�U�|�C���g�擾�E�X�V�N���X
 */

package jp.happyhotel.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataUserInquiryPay;

/**
 * �L���₢���킹�f�[�^
 * 
 * @author S.Tashiro
 * @version 1.00 2009/11/11
 */
public class UserInquiryPay implements Serializable
{
    /**
     *
     */
    private static final long    serialVersionUID = 4658546477538933384L;

    private int                  userInquiryCount;
    private DataUserInquiryPay[] userInquiry;

    /**
     * �f�[�^�����������܂��B
     */
    public UserInquiryPay()
    {
        userInquiryCount = 0;
    }

    public DataUserInquiryPay[] getUserInquiry()
    {
        return userInquiry;
    }

    public int getUserInquiryCount()
    {
        return userInquiryCount;
    }

    /**
     * �₢���킹���e�d���`�F�b�N
     * 
     * @param userId ���[�UID
     * @param inquiry �₢���킹���e
     */
    public boolean checkDuplication(String userId, String inquiry)
    {
        int i;
        int count;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        ret = false;
        count = 0;

        if ( userId.compareTo( "" ) == 0 )
        {
            return(ret);
        }

        query = "SELECT * FROM hh_user_inquiry_pay";

        query = query + " WHERE user_id = ?";
        query = query + " AND inquiry = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( userId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, userId );
                prestate.setString( 2, inquiry );
            }

            result = prestate.executeQuery();

            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    userInquiryCount = result.getRow();
                }

                // �N���X�̔z���p�ӂ��A����������B
                this.userInquiry = new DataUserInquiryPay[this.userInquiryCount];
                for( i = 0 ; i < userInquiryCount ; i++ )
                {
                    userInquiry[i] = new DataUserInquiryPay();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ���[�U�|�C���g���̎擾
                    this.userInquiry[count++].setData( result );
                }
            }
            // ���ʂ��擾
            if ( userInquiryCount > 0 )
            {
                ret = true;
            }
            else
            {
                ret = false;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserInquiryPay.checkDuplication] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }
}
