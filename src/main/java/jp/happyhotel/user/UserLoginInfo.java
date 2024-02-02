/*
 * @(#)UserBasicInfo.java 1.00 2007/07/31 Copyright (C) ALMEX Inc. 2007 ���[�U��{���擾�N���X
 */

package jp.happyhotel.user;

import java.io.Serializable;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApUuidUser;
import jp.happyhotel.data.DataUserBasic;
import jp.happyhotel.data.DataUserFelica;

/**
 * ���[�U��{���擾�N���X�B ���[�U�̊�{�����擾����@�\��񋟂���
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/31
 * @version 1.1 2007/11/26
 */
public class UserLoginInfo implements Serializable
{
    /** �����O�C�����[�U�ł��邱�Ƃ��������[�UID */
    public static final String NON_MEMBER_USER_ID = "tFA7EFMXMFbY4bTjbuArTUARkSPUBsFN3tsWQi2E2Tebam38WmuNDce8u4LSQXpf";
    /** �����O�C�����[�U�ł��邱�Ƃ��������[�U�̃p�X���[�h */
    public static final String NON_MEMBER_PASSWD  = "R6rZ7aNcPhUEtDTJwWsSPtmw8WjZWWeYNVMTegktYXzEjr9JKrTx5m75iTh97XQY";

    /**
     *
     */
    private static final long  serialVersionUID   = -3924291538252750667L;

    private int                userBasicCount;
    private DataUserBasic      userBasic;
    private boolean            nonmemberFlag;                                                                          // �����i�A�v������A�N�Z�X���ꂽ���[�U�j
    private boolean            memberFlag;                                                                             // ���(�r��������܂�)
    private boolean            paymemberFlag;                                                                          // �L�����
    private boolean            paymemberTempFlag;                                                                      // �L���r�����
    private String             errorMessage;

    /**
     * �f�[�^�����������܂��B
     */
    public UserLoginInfo()
    {
        userBasicCount = 0;
        nonmemberFlag = false;
        memberFlag = false;
        paymemberFlag = false;
        paymemberTempFlag = false;
        errorMessage = "";
    }

    /** ���[�U��{��񌏐��擾 **/
    public int getCount()
    {
        return(userBasicCount);
    }

    /** ���[�U��{���擾 **/
    public DataUserBasic getUserInfo()
    {
        return(userBasic);
    }

    public void setUserBasic(DataUserBasic userBasic)
    {
        this.userBasic = userBasic;
    }

    /** �����t���O **/
    public boolean isNonmemberFlag()
    {
        return(nonmemberFlag);
    }

    /** ����t���O **/
    public boolean isMemberFlag()
    {
        return(memberFlag);
    }

    /** �L������t���O **/
    public boolean isPaymemberFlag()
    {
        return(paymemberFlag);
    }

    /** �L���r������t���O **/
    public boolean isPaymemberTempFlag()
    {
        return(paymemberTempFlag);
    }

    public String getErrorMessage()
    {
        return(errorMessage);
    }

    /**
     * ���[�U��{�����擾����iID,pass����j
     * 
     * @param userId ���[�UID
     * @param passwd �p�X���[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public void getUserLoginInfo(String userId, String passwd)
    {
        int kind = 0;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( userId == null )
        {
            return;
        }

        // �����̃`�F�b�N
        ret = this.checkNonMember( userId, passwd );
        if ( ret != false )
        {
            nonmemberFlag = true;
            return;
        }

        query = "SELECT * FROM hh_user_basic";
        // ���[���A�h���X�`�F�b�N���s��
        if ( CheckString.mailaddrCheck( userId ) != false )
        {
            kind = 1;
            query += " WHERE mail_addr = ? ";
            query += " AND regist_status > 0";
            query += " AND del_flag = 0";
            // �g�т̃��[���A�h���X����f�[�^���擾
            query += " UNION SELECT * FROM hh_user_basic";
            query += " WHERE mail_addr_mobile = ?";
            query += " AND regist_status > 0";
            query += " AND del_flag = 0";

        }
        else if ( userId.compareTo( "" ) != 0 )
        {
            kind = 2;
            query += " WHERE user_id = ?";
            query += " AND regist_status > 0";
            query += " AND del_flag = 0";
        }
        else
        {
            errorMessage = Constants.ERROR_MSG_API1;
            return;
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( kind == 1 )
            {
                prestate.setString( 1, userId );
                prestate.setString( 2, userId );
            }
            else if ( kind == 2 )
            {
                prestate.setString( 1, userId );
            }

            ret = getUserLoginInfoSub( prestate );
            if ( ret != false )
            {
                // ���[���A�h���X�Ŏ󂯂Ă��邩������Ȃ��̂ŁAuserId���擾���Ȃ����B
                userId = this.userBasic.getUserId();

                // �p�����[�^���Í��������p�X���[�h�ƁADB�̃p�X���[�h���r
                if ( passwd.equals( UserLogin.decrypt( this.userBasic.getPasswd() ) ) != false )
                {
                    // ��v�����̂Ń����o�[�͊m��
                    memberFlag = true;
                    // �L���r������`�F�b�N
                    if ( this.userBasic.getRegistStatusPay() == 1 )
                    {
                        paymemberTempFlag = true;
                        paymemberFlag = false;
                    }
                    // �L������`�F�b�N
                    if ( this.userBasic.getRegistStatusPay() == 9 )
                    {
                        paymemberTempFlag = false;
                        paymemberFlag = true;
                    }
                    else
                    {
                        // �A�v�����ۋ��`�F�b�N

                        query = "SELECT A.user_id, C.regist_status_pay FROM hh_user_basic A";
                        query += " INNER JOIN ap_uuid_user B ON A.user_id = B.user_id";
                        query += " INNER JOIN ap_uuid C ON B.uuid = C.uuid AND C.regist_status_pay = 2";
                        query += " WHERE A.user_id = ?";

                        prestate = connection.prepareStatement( query );
                        prestate.setString( 1, userId );

                        ResultSet result = prestate.executeQuery();

                        if ( result.next() )
                        {
                            // �L�����(�A�v�����ۋ�)
                            paymemberTempFlag = false;
                            paymemberFlag = true;
                        }
                        else
                        {
                            // �������
                            paymemberTempFlag = false;
                            paymemberFlag = false;
                        }
                    }
                }
                else
                {
                    memberFlag = false;
                    paymemberTempFlag = false;
                    paymemberFlag = false;
                    errorMessage = Constants.ERROR_MSG_API2;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserLoginInfo.getUserLoginInfo()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return;
    }

    /**
     * ���[�U��{�����擾����iuserId����j
     * 
     * @param userId ���[�UID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public void getUserLoginInfo(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( userId == null )
        {
            return;
        }

        query = "SELECT * FROM hh_user_basic";
        if ( userId.compareTo( "" ) != 0 )
        {
            query += " WHERE user_id = ?";
            query += " AND regist_status > 0";
            query += " AND del_flag = 0";
        }
        else
        {
            errorMessage = Constants.ERROR_MSG_API1;
            return;
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );

            ret = getUserLoginInfoSub( prestate );
            if ( ret != false )
            {
                // ��v�����̂Ń����o�[�͊m��
                memberFlag = true;
                // �L���r������`�F�b�N
                if ( this.userBasic.getRegistStatusPay() == 1 )
                {
                    paymemberTempFlag = true;
                    paymemberFlag = false;
                }
                // �L������`�F�b�N
                if ( this.userBasic.getRegistStatusPay() == 9 )
                {
                    paymemberTempFlag = false;
                    paymemberFlag = true;
                }
                else
                {
                    // �A�v�����ۋ��`�F�b�N

                    query = "SELECT A.user_id, C.regist_status_pay FROM hh_user_basic A";
                    query += " INNER JOIN ap_uuid_user B ON A.user_id = B.user_id";
                    query += " INNER JOIN ap_uuid C ON B.uuid = C.uuid AND C.regist_status_pay = 2";
                    query += " WHERE A.user_id = ?";

                    prestate = connection.prepareStatement( query );
                    prestate.setString( 1, userId );

                    ResultSet result = prestate.executeQuery();

                    if ( result.next() )
                    {
                        // �L�����(�A�v�����ۋ�)
                        paymemberTempFlag = false;
                        paymemberFlag = true;
                    }
                    else
                    {
                        // �������
                        paymemberTempFlag = false;
                        paymemberFlag = false;
                    }
                }
            }
            else
            {
                memberFlag = false;
                paymemberTempFlag = false;
                paymemberFlag = false;
                errorMessage = Constants.ERROR_MSG_API2;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserLoginInfo.getUserLoginInfo()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return;
    }

    public static boolean checkMileUser(String userId)
    {
        return checkMileUser( userId, 0 );
    }

    public static boolean checkMileUser(String userId, int memberNo)
    {
        boolean ret = false;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        if ( userId != null )
        {
            query = "SELECT user_id FROM hh_user_basic";
            query += " WHERE user_id = ?";
            query += " AND regist_status > 0";
            try
            {
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, userId );
                result = prestate.executeQuery();
                if ( result.next() )
                {
                    ret = true;
                }
            }
            catch ( Exception e )
            {
                Logging.info( "[UserLoginInfo.checkMileUser()] Exception=" + e.toString() );
            }
            finally
            {
                DBConnection.releaseResources( result, prestate, connection );
            }
            if ( userId.indexOf( "ip_" ) == -1 && userId.indexOf( "ad_" ) == -1 )
            {
            }
            else if ( ret )
            {

                // �������s���[�U�̔��f���Ȃ����B�������s���[�U�ł��^�b�`���󂯕t����B2015.06.02
                int appStatus = 1; // 0:�����O�C�����,1:���O�C�����
                DataApUuidUser dauu = new DataApUuidUser();
                if ( dauu.getAppData( userId ) != false )
                {
                    appStatus = dauu.getAppStatus();
                }
                if ( appStatus == 0 )
                {
                    ret = false;
                }
                Logging.info( "user_id=" + userId + ",appStatus=" + appStatus, "UserLoginInfo.checkMileUser()" );

                if ( ret == false ) // �����O�C�����[�U�ł�felica�ɕR�Â��Ă����OK�Ƃ���
                {
                    DataUserFelica duf = new DataUserFelica();
                    if ( duf.getData( userId ) != false )
                    {
                        ret = true;
                    }
                    Logging.info( "user_id=" + userId + ",felica=" + ret, "UserLoginInfo.checkMileUser()" );
                }

                if ( ret == false ) // �����O�C�����[�U�ł��L������������胁�A�h���o�^����Ă����OK�Ƃ���
                {
                    DataUserBasic dub = new DataUserBasic();
                    if ( dub.getData( userId ) != false )
                    {
                        if ( dub.getRegistStatusPay() == 9 )// �L�����
                        {
                            ret = true;
                        }
                        else if ( !dub.getMailAddr().equals( "" ) )
                        {
                            ret = true;
                        }
                        else if ( !dub.getMailAddrMobile().equals( "" ) )
                        {
                            ret = true;
                        }
                    }
                    Logging.info( "user_id=" + userId + ",UserBasic=" + ret, "UserLoginInfo.checkMileUser()" );
                }

            }
        }
        return ret;
    }

    /**
     * ���[�U��{���̃f�[�^���Z�b�g
     * 
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getUserLoginInfoSub(PreparedStatement prestate)
    {
        ResultSet result = null;

        try
        {
            result = prestate.executeQuery();

            if ( result != null )
            {
                userBasic = new DataUserBasic();

                if ( result.next() != false )
                {
                    userBasicCount = 1;
                    // ���[�U��{�f�[�^���̐ݒ�
                    this.userBasic.setData( result );
                }
                else
                {
                    userBasicCount = 0;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserLoginInfo.getUserLoginInfoSub()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( userBasicCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ���[�U��{���̃f�[�^���Z�b�g
     * 
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getAppPurchaseInfo(PreparedStatement prestate)
    {
        ResultSet result = null;
        int flag = 0;

        try
        {
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    flag = 1;
                }
                else
                {
                    flag = 0;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserLoginInfo.getUserLoginInfoSub()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( flag != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ���[�U�����`�F�b�N
     * 
     * @param userId ���[�UID
     * @param passwd �p�X���[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     **/
    private boolean checkNonMember(String userId, String passwd)
    {
        if ( (NON_MEMBER_USER_ID.equals( userId ) != false) && (NON_MEMBER_PASSWD.equals( passwd ) != false) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * MD5�ϊ�����
     * 
     * @param passwd �p�X���[�h
     * @return ��������(null�FMD5�ϊ����s)
     **/
    private String getPasswdMd5(String passwd)
    {
        byte[] bytePass;
        String strReturn = null;

        try
        {
            MessageDigest md = MessageDigest.getInstance( "MD5" );
            bytePass = passwd.getBytes();
            md.update( bytePass );
            bytePass = md.digest();

            StringBuffer buff = new StringBuffer();
            for( int i = 0 ; i < bytePass.length ; i++ )
            {
                int val = bytePass[i] & 0xff;
                if ( val < 16 )
                {
                    buff.append( "0" );
                }
                buff.append( Integer.toString( val, 16 ) );
            }
            strReturn = buff.toString();
        }
        catch ( Exception e )
        {
            Logging.error( "[getPasswdMd5]Exception:" + e.toString() );
        }

        return(strReturn);
    }
}