package jp.happyhotel.user;

import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApCampaignMaster;
import jp.happyhotel.data.DataApPush;
import jp.happyhotel.data.DataApPushData;
import jp.happyhotel.data.DataApPushDataList;
import jp.happyhotel.data.DataApPushDataListExtend;
import jp.happyhotel.data.DataApTokenUser;
import jp.happyhotel.data.DataApUserCampaign;
import jp.happyhotel.touch.GooglePushMapper;
import jp.happyhotel.touch.Push2Android;
import jp.happyhotel.touch.Push2Iphone;
//import jp.newrsv.data.DataApRsvTokenUuid;
//import jp.happyhotel.data.DataApPushDataList;

/**
 * �L�����y�[���v�b�V���z�M���N���X
 *
 * @author S.Tashiro
 * @version 1.00 2011/05/19
 */
public class CampaignMessage implements Serializable
{
    final int         ANDROID = 2;
    final int         PUSH    = 1;
    final int         MAXPUSH_a = 100;
    final int         MAXPUSH_i = 100;
    final int         STATUS_IN_PROCECCING = 9;
    final int         STATUS_ERROR = 11;
    final String      configFilePath = "//etc//happyhotel//push.conf";

    DataApPushDataList dapdl = new DataApPushDataList();
    DataApPushData[] dataApPushData;
    DataApPushDataListExtend[] dataApPushDataList;
    DataApPush[] dataApPush;
    int               pushCount;
    int               pushListCount;
    ArrayList<DataApPush> arrPush_a = new ArrayList<DataApPush>();   //1�񕪖��̑��M�g�[�N�����Z�b�g(100���j
    ArrayList<DataApPush> arrPush_i = new ArrayList<DataApPush>();   //1�񕪖��̑��M�g�[�N�����Z�b�g(100���j

    public CampaignMessage()
    {
        this.dataApPushData = null;
        this.pushCount = 0;
        this.dataApPushDataList = null;
        this.pushListCount = 0;
    }

    /***
     * �v�b�V���z�M�f�[�^���擾
     *
     * @param date �z�M��]���t
     * @param time �z�M��]����
     * @return
     */
    public boolean getApPushData(int date, int time) throws Exception
    {

        boolean ret = false;
        int count;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String query;

        this.pushCount = 0;

        // PUSH�z�M��]�����̃f�[�^���擾

        query = "SELECT *, ACM.campaign_id, ACM.detail  FROM ap_push_data APD";
        query += " INNER JOIN ap_campaign_master ACM ON APD.campaign_id = ACM.campaign_id";
        query += " WHERE APD.status = 2 "; //�z�M���F
        query += "  AND APD.del_flag = 0 ";
        query += "  AND ACM.del_flag = 0 ";
        query += "  AND ( APD.desired_date < ? OR ";
        query += "  ( APD.desired_date = ?";
        query += "  AND APD.desired_time <= ? ))";
        query += "  ORDER BY APD.push_seq";
        query += "  LIMIT 1";
        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, date );
            prestate.setInt( 2, date );
            prestate.setInt( 3, time );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    this.pushCount = result.getRow();
                }
                result.beforeFirst();

                // �N���X�̔z���p�ӂ��A����������B
                this.dataApPushData = new DataApPushData[this.pushCount];
                result.beforeFirst();
                while( result.next() != false )
                {
                    this.dataApPushData[count] = new DataApPushData();
                    this.dataApPushData[count].setData( result );
                    count++;
                }
            }
            ret = true;
        }
        catch ( Exception e )
        {
            Logging.error( "[CampaignMessage.getApPushData()] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /***
     * �L�����y�[���p�v�b�V���z�M�f�[�^���X�g���擾
     *
     * @param push_seq �v�b�V���z�M�A��
     * @return
     */
    public boolean getApPushDataList(int push_seq, int apli_kind) throws Exception
    {

        boolean ret = false;
        int count = 0;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String query = "";

        if( apli_kind == 1 )
        {
            // �n�s�z�e�A�v���ɔz�M�̂Ƃ�
            query ="SELECT *  FROM ap_push_data_list as apdl";
            query += " INNER JOIN ap_token_user AS atu";
            query += " ON apdl.user_id = atu.user_id";
            query += " AND apdl.token = atu.token";
            query += " INNER JOIN ap_user_push_config AS aupc";
            query += " ON apdl.user_id = aupc.user_id";
            query += " WHERE apdl.type = 1 "; //�L�����y�[��
            query += "  AND apdl.status = 0 ";//�����M
            query += "  AND apdl.push_seq = ? ";
            query += "  ORDER BY atu.os_type, apdl.user_id DESC";
        }
        else
        {
            // �\��A�v���ɔz�M�̂Ƃ�
            query ="SELECT *  FROM ap_push_data_list AS apdl";
            query += " INNER JOIN ( SELECT token, device_type AS os_type FROM newRsvDB.ap_rsv_token_uuid AS artu ";
            query += "              INNER JOIN newRsvDB.ap_rsv_uuid AS aru";
            query += "               ON artu.uuid = aru.uuid ) AS tkn";
            query += " ON apdl.token = tkn.token";
            query += " INNER JOIN  ";
            query += "( SELECT aruu.user_id AS user_id, arupc.push_flag AS push_flag, arupc.campaign_flag AS campaign_flag, 0 AS co_flag FROM newRsvDB.ap_rsv_uuid_user AS aruu ";
            query += "INNER JOIN  newRsvDB.ap_rsv_user_push_config AS arupc ";
            query += "ON aruu.uuid = arupc.uuid ) AS config";
            query += " ON config.user_id = apdl.user_id";
            query += " WHERE apdl.type = 1 "; //�L�����y�[��
            query += "  AND apdl.status = 0 ";//�����M
            query += "  AND apdl.push_seq = ? ";
            query += "  ORDER BY tkn.os_type, apdl.user_id DESC";
        }
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, push_seq );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    count = result.getRow();
                    // �N���X�̔z���p�ӂ��A����������B
                    dataApPushDataList = new DataApPushDataListExtend[count];
                }
                result.beforeFirst();
                count = 0;
                while( result.next() != false )
                {
                    this.dataApPushDataList[count] = new DataApPushDataListExtend();
                    this.dataApPushDataList[count].setData( result );
                    count++;
                }
            }
            ret = true;
        }
        catch ( Exception e )
        {
            Logging.error( "[CampaignMessage.getApPushDataList()] Exception=" + e.toString() ,"CampaignMessage");
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /***
     * �L�����y�[���pPUSH�ʒm
     *
     * @param userId
     * @param id
     * @return
     */
    public boolean sendCampaignMessage(int date, int time)
    {
        boolean ret = true;
        String msg = "";

        DataApCampaignMaster dacm = new DataApCampaignMaster();
        Push2Android p2a = new Push2Android();
        Push2Iphone p2i = new Push2Iphone();

        int target_PushSeq = 0;
        int target_CampaignId = 0;
        int target_ApliKind = 0;

            // URL�����擾
            FileInputStream propfile = null;
            Properties config;
            String      Url="";
            String      UrlRsv="";
            try
            {
                propfile = new FileInputStream( configFilePath );
                config = new Properties();
                config.load( propfile );
                Url = String.valueOf( config.getProperty( "url" ) );
                UrlRsv = String.valueOf( config.getProperty( "urlrsv" ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[CampaignMessage]conf�t�@�C������URL�����擾�Ɏ��s���܂��� Exception:" + e.toString() );
                return false;
            }

            do
            {
            try
            {
            //PUSH�ʒm����g�[�N�����X�g�擾
            if( getApPushData(date, time))
            {

                for( int i = 0 ; i < this.dataApPushData.length ; i++ )
                {

                    //�ϐ�������
                    target_PushSeq = this.dataApPushData[i].getPushSeq();
                    target_CampaignId = this.dataApPushData[i].getCampaignId();
                    target_ApliKind = this.dataApPushData[i].getApliKind();
                    this.pushListCount = 0;
                    arrPush_a.clear();
                    arrPush_i.clear();

                    //�v�b�V���z�M�f�[�^(ap_push_data)�z�M�����X�V
                    if ( !this.updatePushData( target_PushSeq, STATUS_IN_PROCECCING ))
                    {
                        Logging.error( "CampaignMessage[this.updatePushData()]" );
                        throw new Exception();
                    }

                    //�L�����y�[�����}�X�^�iap_campaign_master�j����L�����y�[���ڍ׎擾
                    if ( !dacm.getData(target_CampaignId) )
                    {
                        Logging.error( "CampaignMessage[dacm.getData("+target_CampaignId+")]","CampaignMessage" );
                        throw new Exception();
                    }
                    msg = dacm.getDetail();

                    //URL�쐬�ijsp���J�����Ƃ���ap_push_data_list��confirmdate�����X�V���邽�߁A�L�[���ڂ𑗂�j
                    String param = "";
                    param  = "?cpid=" +  + target_CampaignId;
                    param += "&type=1";//���1�i�L�����y�[���j
                    param += "&push_seq=" + target_PushSeq;
                    String url = "";
                    if( target_ApliKind == 1 )
                    {
                        url = Url + param;
                    }
                    else
                    {
                        url = UrlRsv + param;
                    }


                    //���X�g���擾���AOS���Ƃɔz�M�p�g�[�N���z����쐬����
                    if( getApPushDataList( target_PushSeq ,target_ApliKind))
                    {
                        if(this.dataApPushDataList != null)
                        {
                            //�ϐ�������
                            int cnt_i=0;
                            int cnt_a=0;
                            String tmp_i="";
                            String tmp_a="";
                            ArrayList<DataApPushDataListExtend> listDt_a = new ArrayList<DataApPushDataListExtend>();
                            ArrayList<DataApPushDataListExtend> listDt_i = new ArrayList<DataApPushDataListExtend>();

                            for( int j = 0 ; j < this.dataApPushDataList.length ; j++ )
                            {
                                //iOS
                                if(this.dataApPushDataList[j].getOsType() == 1)
                                {
                                    if(this.dataApPushDataList[j].getPushFlag() == 1 && this.dataApPushDataList[j].getCampaignFlag() == 1 )
                                    {
                                        tmp_i += "," + this.dataApPushDataList[j].getToken();
                                    }
                                    listDt_i.add( this.dataApPushDataList[j] );
                                    cnt_i ++;
                                    if(cnt_i >= MAXPUSH_i)
                                    {
                                        DataApPush dp = new DataApPush();
                                        if(tmp_i.length() > 0)
                                        {
                                            dp.setTokens( tmp_i.substring(1,tmp_i.length()) );
                                        }
                                        dp.setListPl(listDt_i);
                                        arrPush_i.add( dp );
                                        //�ϐ��N���A
                                        cnt_i = 0;
                                        tmp_i = "";
                                        listDt_i = new ArrayList<DataApPushDataListExtend>();
                                    }
                                }
                                //android
                                else
                                {
                                    if(this.dataApPushDataList[j].getPushFlag() == 1 && this.dataApPushDataList[j].getCampaignFlag() == 1 )
                                    {
                                        tmp_a += "," + this.dataApPushDataList[j].getToken();
                                    }
                                    listDt_a.add( this.dataApPushDataList[j] );
                                    cnt_a ++;
                                    if(cnt_a >= MAXPUSH_a)
                                    {
                                        DataApPush dp = new DataApPush();
                                        if(tmp_a.length() > 0)
                                        {
                                            dp.setTokens( tmp_a.substring(1,tmp_a.length()) );
                                        }
                                        dp.setListPl(listDt_a);
                                        arrPush_a.add( dp );
                                        //�ϐ��N���A
                                        cnt_a = 0;
                                        tmp_a = "";
                                        listDt_a = new ArrayList<DataApPushDataListExtend>();
                                    }
                                }
                            }

                            //MAXPUSH�����̃f�[�^���Z�b�g
                            DataApPush dpi = new DataApPush();
                            if(tmp_i.length() > 0)
                            {
                                dpi.setTokens( tmp_i.substring(1,tmp_i.length()) );
                            }
                            if(listDt_i.size()>0)
                            {
                                dpi.setListPl(listDt_i);
                                arrPush_i.add( dpi );
                            }
                            DataApPush dpa = new DataApPush();
                            if(tmp_a.length() > 0)
                            {
                                dpa.setTokens( tmp_a.substring(1,tmp_a.length()) );
                            }
                            if(listDt_a.size()>0)
                            {
                                dpa.setListPl(listDt_a);
                                arrPush_a.add( dpa );
                            }

                        }

                    }
                    else
                    {
                        Logging.error( "CampaignMessage[getApPushDataList("+this.dataApPushData[i].getPushSeq()+")]" ,"CampaignMessage");
                        throw new Exception();
                    }

                    //android���M
                    //Logging.info( "android���M" + DateEdit.getDate( 1 ) + DateEdit.getTime( 0 ) + " this.arrPush_a.size()=" +  +this.arrPush_a.size(),"CampaignMessage ");
                    for( int j = 0 ; j < this.arrPush_a.size() ; j++ )
                    {
                        p2a = new Push2Android();
                        GooglePushMapper gpm = new GooglePushMapper();
                        gpm.addRegId( this.arrPush_a.get(j).getTokens());
                        gpm.createData( msg, url );
                        p2a.setApliKind( target_ApliKind );
                        if(p2a.push( gpm ))
                        {
                            //Logging.info( "�v�b�V���ʒm����(�A���h���C�h�j"+ this.arrPush_a.get(j).getTokens()  ,"CampaignMessage sendCampaignMessage()");

                            //���M�G���[�g�[�N���z��擾
                            ArrayList<String> inactiveTokenTmp = new ArrayList<String>();
                            inactiveTokenTmp = p2a.getInactiveToken();
                            //�s���G���[�g�[�N���z��擾
                            ArrayList<String> invalidTokenTmp = new ArrayList<String>();
                            invalidTokenTmp = p2a.getInvalidToken();

                            //�啶���ϊ�
                            ArrayList<String> inactiveToken = new ArrayList<String>();
                            if( inactiveTokenTmp != null)
                            {
                                for( int m = 0 ; m < inactiveTokenTmp.size() ; m++ )
                                {
                                    inactiveToken.add( inactiveTokenTmp.get( m ).toUpperCase() );
                                }
                            }
                            ArrayList<String> invalidToken = new ArrayList<String>();
                            if( invalidTokenTmp != null)
                            {
                                for( int m = 0 ; m < invalidTokenTmp.size() ; m++ )
                                {
                                    invalidToken.add( invalidTokenTmp.get( m ).toUpperCase() );
                                }
                            }

                            //ap_push_data_list�X�V
                            if(!this.updatePushDataList( this.arrPush_a.get(j), inactiveToken, invalidToken))
                            {
                                Logging.error( "CampaignMessage[this..updatePushDataList()]ANDROID","CampaignMessage" );
                                throw new Exception();
                            }
                            //ap_user_campaign�f�[�^�쐬�i�܂��͍X�V�j
                            if( !updateUserCampaign( this.arrPush_a.get(j), target_CampaignId, target_ApliKind) )
                            {
                                Logging.error( "CampaignMessage[this.updateUserCampaign()]" );
                                throw new Exception();
                            }
                            //ap_token_user �܂���ap_rsv_token_uuid�̃G���[�t���O�X�V
                            if(!this.updateTokenErrorFlag( this.arrPush_a.get(j), inactiveToken, invalidToken, target_ApliKind))
                            {
                                Logging.error( "CampaignMessage[this.updateTokenErrorFlag()]ANDROID","CampaignMessage" );
                                throw new Exception();
                            }
                        }
                        else
                        {
                            Logging.error( "ANDROID push() Error","CampaignMessage" );
                            throw new Exception();
                        }
                    }


                    //iPhone���M
                    //Logging.info( "iPhone���M" + DateEdit.getDate( 1 ) + DateEdit.getTime( 0 ) + " this.arrPush_i.size()="+this.arrPush_i.size(),"CampaignMessage" );
                    for( int j = 0 ; j < this.arrPush_i.size() ; j++ )
                    {
                        p2i = new Push2Iphone();
                        //Logging.info( "this.arrPush_i.get(j).getTokens()="+this.arrPush_i.get(j).getTokens(),"CampaignMessage" );
                        p2i.setApliKind( target_ApliKind );
                        if(p2i.push(  this.arrPush_i.get(j).getTokens(), msg, url ))
                        {

                            //Logging.info( "�v�b�V���ʒm�����iiOS�j"+this.arrPush_i.get(j).getTokens() ,"CampaignMessage sendCampaignMessage()");

                            //���M�G���[�g�[�N���z��擾
                            ArrayList<String> inactiveTokenTmp = new ArrayList<String>();
                            inactiveTokenTmp = p2i.getInactiveToken();
                            //�s���G���[�g�[�N���z��擾
                            ArrayList<String> invalidTokenTmp = new ArrayList<String>();
                            invalidTokenTmp = p2i.getInvalidToken();

                            //�啶���ϊ�
                            ArrayList<String> inactiveToken = new ArrayList<String>();
                            if( inactiveTokenTmp != null)
                            {
                                for( int m = 0 ; m < inactiveTokenTmp.size() ; m++ )
                                {
                                    inactiveToken.add( inactiveTokenTmp.get( m ).toUpperCase() );
                                }
                            }
                            ArrayList<String> invalidToken = new ArrayList<String>();
                            if( invalidTokenTmp != null)
                            {
                                for( int m = 0 ; m < invalidTokenTmp.size() ; m++ )
                                {
                                    invalidToken.add( invalidTokenTmp.get( m ).toUpperCase() );
                                }
                            }
                            //Logging.info( "inactiveToken.size()="+inactiveToken.size() + " invalidToken.size()="+invalidToken.size(),"CampaignMessage" );

                            //ap_push_data_list�X�V
                            if(!this.updatePushDataList( this.arrPush_i.get(j), inactiveToken, invalidToken))
                            {
                                Logging.error( "CampaignMessage[this.updatePushDataList()]iOS","CampaignMessage" );
                                throw new Exception();
                            }
                            //ap_user_campaign�f�[�^�쐬�i�܂��͍X�V�j
                            if( !updateUserCampaign( this.arrPush_i.get(j), target_CampaignId, target_ApliKind) )
                            {
                                Logging.error( "CampaignMessage[this.updateUserCampaign()]" );
                                throw new Exception();
                            }
                            //ap_token_user �܂���ap_rsv_token_uuid�̃G���[�t���O�X�V
                            if(!this.updateTokenErrorFlag( this.arrPush_i.get(j), inactiveToken, invalidToken, target_ApliKind))
                            {
                                Logging.error( "CampaignMessage[this.updateTokenErrorFlag()]iOS","CampaignMessage" );
                                throw new Exception();
                            }
                        }
                        else
                        {
                            Logging.error( "iOS push() Error","CampaignMessage" );
                            throw new Exception();
                        }
                    }

                    //�v�b�V���z�M�f�[�^(ap_push_data)Status�X�V
                    //Logging.info( "�v�b�V���z�M�f�[�^(ap_push_data)Status�X�V" + DateEdit.getDate( 1 ) + DateEdit.getTime( 0 ),"CampaignMessage" );
                    int listCnt = 0;
                    if(dapdl.getPushDataListCount(1, target_PushSeq, 1))
                    {
                        listCnt = dapdl.getPushCount();
                    }
                    if ( !this.updatePushDataStatus( target_PushSeq ,listCnt))
                    {
                        Logging.error( "CampaignMessage[this.updatePushDataStatus()]","CampaignMessage" );
                        throw new Exception();
                    }
                }
            }

            }
            catch ( Exception e )
            {
                Logging.error( "[CampaignMessage.sendCampaignMessage()] Exception=" + e.toString(),"CampaignMessage" );

                //�v�b�V���z�M�f�[�^(ap_push_data)�z�M�G���[�ɂ���
                if ( !this.updatePushData( target_PushSeq, STATUS_ERROR ))
                {
                    Logging.error( "CampaignMessage[this.updatePushData()]status=10" );
                }
                ret=false;
            }

            }
            while(this.pushCount > 0);

        return ret;
    }

    /****
     * PUSH�z�M�f�[�^���X�g�X�V
     *
     * @param id
     * @param pushSeq
     * @param userId
     * @param token
     * @return
     */
    public boolean updatePushDataList(DataApPush dp, ArrayList<String> inactiveToken, ArrayList<String> invalidToken )
    {
        //Logging.error( "[CampaignMessage.updatePushDataList()] dp.getListPl().size()="+dp.getListPl().size(),"CampaignMessage" );
        boolean ret = true;

        for( int i = 0 ; i < dp.getListPl().size() ; i++ )
        {
            DataApPushDataListExtend listPl = new DataApPushDataListExtend();
            listPl = dp.getListPl().get(i);
            DataApPushDataList dapdl = new DataApPushDataList();
            if(dapdl.getData( listPl.getType(), listPl.getId(), listPl.getPushSeq(), listPl.getUserId(), listPl.getToken() ))
            {
                if(listPl.getPushFlag() == 1 && listPl.getCampaignFlag() == 1)
                {

                    //�g�[�N���̏�Ԃ��擾
                    int status = chkTokenStatus( listPl.getToken(), inactiveToken, invalidToken );

                    if(status == 0)
                    {
                        dapdl.setStatus( 1 );//�g�[�N��������̂Ƃ����M�ς݂ɂ���
                    }
                    else
                    {
                        dapdl.setStatus( status );//���M�G���[�܂��́A�g�[�N���s��
                    }

                }
                else
                {
                    dapdl.setStatus( 9 );//PUSH�ʒm���ۃf�[�^�̓X�e�[�^�X���폜�ɂ���
                }
                dapdl.setSendDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dapdl.setSendTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                if(dapdl.updateData( listPl.getType(), listPl.getId(), listPl.getPushSeq(), listPl.getUserId(), listPl.getToken() ))
                {
                    pushListCount ++;
                }
                else
                {
                    ret=false;
                }
            }
            else
            {
                ret=false;
            }
        }
        return ret;
    }

    /****
     * PUSH�z�M�f�[�^�X�V�i�z�M�����j
     *
     * @param pushSeq
     * @return
     */
    public boolean updatePushData( int pushSeq, int status)
    {
        boolean ret = false;
        DataApPushData dapd = new DataApPushData();
        if( dapd.getData( pushSeq ))
        {
            dapd.setStatus( status );
            if( status == 9)
            {
                //�z�M���̂Ƃ�
                dapd.setPushDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dapd.setPushTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            }
            dapd.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dapd.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            ret = dapd.updateData( pushSeq );
        }
        return ret;
    }

    /****
     * PUSH�z�M�f�[�^�X�V(Status, Count)
     *
     * @param pushSeq
     * @param count
     * @return
     */
    public boolean updatePushDataStatus( int pushSeq, int count )
    {
        boolean ret = false;
        DataApPushData dapd = new DataApPushData();
        if(  dapd.getData( pushSeq ))
        {
            dapd.setStatus( 3 );//�z�M��
            dapd.setPushCount( count );
            dapd.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dapd.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            ret = dapd.updateData( pushSeq );
        }
        return ret;
    }

    /****
     * apUserCampaign�X�V
     *
     * @param userId
     * @param campaign_id
     * @return
     */
    public boolean updateUserCampaign( DataApPush dp, int campaign_id, int apli_kind)
    {
        boolean ret = true;

        for( int i = 0 ; i < dp.getListPl().size() ; i++ )
        {
            DataApPushDataListExtend listPl = new DataApPushDataListExtend();
            listPl = dp.getListPl().get(i);
            DataApUserCampaign dauc = new DataApUserCampaign();

            if(dauc.getData(  listPl.getUserId(), campaign_id ,apli_kind ))
            {
                //update
                dauc.setPushDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dauc.setPushTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                if(!dauc.updateData( listPl.getUserId(), campaign_id, apli_kind))
                {
                    ret = false;
                }
            }
            else
            {
                //insert
                dauc.setUserId( listPl.getUserId() );
                dauc.setCampaignId( campaign_id );
                dauc.setApliKind( apli_kind );
                dauc.setPushDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dauc.setPushTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                if(!dauc.insertData())
                {
                    ret = false;
                }
            }
        }
        return ret;
    }
    /****
     * error_flag�X�V
     *
     * @param DataApPush
     * @param inactiveToken
     * @param invalidToken
     * @param apli_kind
     * @return
     */
    public boolean updateTokenErrorFlag( DataApPush dp, ArrayList<String> inactiveToken, ArrayList<String> invalidToken, int apli_kind)
    {
        boolean ret = true;
        DataApTokenUser datu = new DataApTokenUser();
        //DataApRsvTokenUuid dartu = new DataApRsvTokenUuid();

        for( int i = 0 ; i < dp.getListPl().size() ; i++ )
        {
            DataApPushDataListExtend listPl = new DataApPushDataListExtend();
            listPl = dp.getListPl().get(i);
            DataApPushDataList dapdl = new DataApPushDataList();
            String userId =listPl.getUserId();
            String token = listPl.getToken();
            int status = 0;

            //�g�[�N���̏�Ԃ��擾
            status = chkTokenStatus( listPl.getToken(), inactiveToken, invalidToken );


            if ( apli_kind == 1)
            {
                //�n�s�z�e�A�v���p�g�[�N���̂Ƃ�ap_user_token��error_flag�X�V
                if(datu.getData( token, userId ))
                {
                    if( datu.getErrorFlag() != status)
                    {
                        datu.setErrorFlag( status );
                        datu.setUpdateDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        datu.setUpdateTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        if(!datu.updateData( token, userId ))
                        {
                            ret=false;
                        }
                    }
                }
                else
                {
                    ret=false;
                }
            }
            else
            {
                //�\��A�v���̂Ƃ�ap_rsv_token_uuid��error_flag�X�V
                if( !updateApRsvTokenUuid( token, status))
                {
                    ret= false;
                }
            }
        }

        return ret;
    }

    /****
     * �g�[�N���̏�Ԏ擾
     *
     * @param token
     * @param inactiveToken
     * @param invalidToken
     * @return
     */
    public int chkTokenStatus( String token, ArrayList<String> inactiveToken, ArrayList<String> invalidToken )
    {

        int status = 0;//����

        //�g�[�N���̏�Ԃ��擾
        if(inactiveToken.indexOf(token.toUpperCase()) != -1)
        {
            status = 2;//���M�G���[
        }
        if(invalidToken.indexOf(token.toUpperCase()) != -1)
        {
            status = 3;//�g�[�N���s��
        }

        return status;
    }

    /**
     * �g�[�N�����[�U�[(ap_rsv_token_uuid)�X�V
     *
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param token iOS:device token,Android:registration id�i�g�[�N���͏��������Ƃ�����j
     * @param uuid ���[�U�[ID
     * @return
     */
    public boolean updateApRsvTokenUuid(String token, int status)
    {
        int i = 1;
        int result;
        ResultSet resultset = null;
        int errorFlag = -1;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;


        try
        {
            connection = DBConnection.getConnection();

            //�G���[�t���O�擾
            query = "SELECT * FROM newRsvDB.ap_rsv_token_uuid WHERE token = ?  ";
            prestate = connection.prepareStatement( query );
            prestate.setString( i++, token );
            resultset = prestate.executeQuery();
            if ( resultset != null )
            {
                if ( resultset.next() != false )
                {
                    errorFlag = resultset.getInt( "error_flag" );
                    ret = true;
                }
            }


            //�G���[�t���O�ɕύX������Ȃ�A�X�V
            if( ret && errorFlag != status)
            {
                query = "UPDATE newRsvDB.ap_rsv_token_uuid SET ";
                query += "  update_date=?";
                query += ", update_time=?";
                query += ", error_flag=?";
                query += " WHERE token=?";


                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                // �X�V�Ώۂ̒l���Z�b�g����
                prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
                prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
                prestate.setInt( i++, status );
                prestate.setString( i++, token );
                result = prestate.executeUpdate();
                if ( result > 0 )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApRsvTokenUuid.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

}
