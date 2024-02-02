/*
 * @(#)UserTermInfo.java 1.00 2007/07/31 Copyright (C) ALMEX Inc. 2007 ���[�U�[�����擾�E�X�V�N���X
 */

package jp.happyhotel.user;

import java.io.*;
import java.sql.*;

import javax.servlet.http.*;
import jp.happyhotel.data.*;
import jp.happyhotel.common.*;

/**
 * ���[�U�[�����擾�E�X�V�N���X�B ���[�U�̒[�������擾����@�\��񋟂���
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/31
 * @version 1.1 2007/11/27
 */
public class UserTermInfo implements Serializable
{
    /**
	 *
	 */
    private static final long   serialVersionUID = 442143587628568787L;

    private DataMasterUseragent userTerm;

    /**
     * �f�[�^�����������܂��B
     */
    public UserTermInfo()
    {

    }

    /** ���[�U�[�����擾 **/
    public DataMasterUseragent getTerm()
    {
        return(userTerm);
    }

    /**
     * �[�������擾����
     * 
     * @param request HTTP���N�G�X�g
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getTermInfo(HttpServletRequest request)
    {
        boolean ret;
        String query;
        String userAgent;
        String cutAgent;
        Connection connection = null;
        PreparedStatement prestate = null;

        userAgent = request.getHeader( "user-agent" );
        if ( userAgent == null )
        {
            return(false);
        }

        // ���[�U�G�[�W�F���g���J�b�g����
        cutAgent = getCutUserAgent( userAgent );

        query = "SELECT * FROM hh_master_useragent";

        if ( userAgent.compareTo( "" ) != 0 )
        {
            query = query + " WHERE useragent LIKE ?";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( userAgent.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, cutAgent + "%" );
            }

            ret = getTermInfoSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[UserTermInfo.getMyHotelList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �[�����̃f�[�^���Z�b�g
     * 
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getTermInfoSub(PreparedStatement prestate)
    {
        boolean ret;
        ResultSet result = null;

        ret = false;

        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                userTerm = new DataMasterUseragent();

                result.beforeFirst();
                if ( result.next() != false )
                {
                    // ���[�U���̎擾
                    this.userTerm.setData( result );
                }

                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserTermInfo.getTermInfoSub] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        return(ret);
    }

    /**
     * ���[�U�G�[�W�F���g�؏o����
     * 
     * @param userAgent ���[�U�G�[�W�F���g
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private String getCutUserAgent(String userAgent)
    {
        int i;
        int count;
        int cutStart;
        StringBuffer cutOrgData;
        StringBuffer cutData;

        count = 0;
        cutOrgData = new StringBuffer( userAgent );
        cutData = new StringBuffer();

        // DoCoMo�p
        if ( userAgent.indexOf( "DoCoMo" ) >= 0 )
        {
            // �o�[�W�����`�F�b�N
            if ( userAgent.indexOf( "1.0" ) >= 0 )
            {
                // ���[�U�G�[�W�F���g�̐؏o�i"/"���R���o�ŏI���j
                for( i = 0 ; i < cutOrgData.length() ; i++ )
                {
                    if ( cutOrgData.charAt( i ) == '/' )
                    {
                        count++;
                        if ( count >= 3 )
                        {
                            break;
                        }
                    }
                    cutData.append( cutOrgData.charAt( i ) );
                }
            }
            else if ( userAgent.indexOf( "2.0" ) >= 0 )
            {
                // ���[�U�G�[�W�F���g�̐؏o�i"("�����o�ŏI���j
                for( i = 0 ; i < cutOrgData.length() ; i++ )
                {
                    if ( cutOrgData.charAt( i ) == '(' )
                    {
                        break;
                    }
                    cutData.append( cutOrgData.charAt( i ) );
                }
            }
        }
        // SoftBank�p
        else if ( userAgent.indexOf( "SoftBank" ) >= 0 || userAgent.indexOf( "Vodafone" ) >= 0 || userAgent.indexOf( "J-PHONE" ) >= 0 )
        {
            // ���[�U�G�[�W�F���g�̐؏o�i"/"���R���o�ŏI���j
            for( i = 0 ; i < cutOrgData.length() ; i++ )
            {
                if ( cutOrgData.charAt( i ) == '/' || cutOrgData.charAt( i ) == ' ' )
                {
                    count++;
                    if ( count >= 3 && userAgent.indexOf( "J-PHONE" ) >= 0 )
                    {
                        // J-PHONE�Ɋւ��Ă�3�܂�
                        break;
                    }
                    if ( count >= 4 )
                    {
                        // ���̑���4�܂�
                        break;
                    }
                }
                cutData.append( cutOrgData.charAt( i ) );
            }
        }
        // au�p
        else if ( userAgent.indexOf( "KDDI" ) >= 0 || userAgent.indexOf( "UP.Browser" ) >= 0 )
        {
            // ���[�U�G�[�W�F���g�̐؏o�i" "�����o�ŏI���j
            for( i = 0 ; i < cutOrgData.length() ; i++ )
            {
                if ( cutOrgData.charAt( i ) == ' ' )
                {
                    break;
                }
                cutData.append( cutOrgData.charAt( i ) );
            }
            // '-'�����o���f�o�C�XID���擾����
            cutStart = cutData.indexOf( "-" );
            if ( cutStart >= 0 )
            {
                cutData = new StringBuffer( cutData.substring( cutStart + 1 ) );
            }
        }
        else
        {
            cutData = cutOrgData;
        }

        return(cutData.toString());
    }

}
