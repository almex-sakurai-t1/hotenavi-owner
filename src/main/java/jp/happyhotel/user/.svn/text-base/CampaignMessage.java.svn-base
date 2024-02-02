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
 * キャンペーンプッシュ配信情報クラス
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
    ArrayList<DataApPush> arrPush_a = new ArrayList<DataApPush>();   //1回分毎の送信トークンをセット(100件）
    ArrayList<DataApPush> arrPush_i = new ArrayList<DataApPush>();   //1回分毎の送信トークンをセット(100件）

    public CampaignMessage()
    {
        this.dataApPushData = null;
        this.pushCount = 0;
        this.dataApPushDataList = null;
        this.pushListCount = 0;
    }

    /***
     * プッシュ配信データを取得
     *
     * @param date 配信希望日付
     * @param time 配信希望時刻
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

        // PUSH配信希望日時のデータを取得

        query = "SELECT *, ACM.campaign_id, ACM.detail  FROM ap_push_data APD";
        query += " INNER JOIN ap_campaign_master ACM ON APD.campaign_id = ACM.campaign_id";
        query += " WHERE APD.status = 2 "; //配信承認
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
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.pushCount = result.getRow();
                }
                result.beforeFirst();

                // クラスの配列を用意し、初期化する。
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
     * キャンペーン用プッシュ配信データリストを取得
     *
     * @param push_seq プッシュ配信連番
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
            // ハピホテアプリに配信のとき
            query ="SELECT *  FROM ap_push_data_list as apdl";
            query += " INNER JOIN ap_token_user AS atu";
            query += " ON apdl.user_id = atu.user_id";
            query += " AND apdl.token = atu.token";
            query += " INNER JOIN ap_user_push_config AS aupc";
            query += " ON apdl.user_id = aupc.user_id";
            query += " WHERE apdl.type = 1 "; //キャンペーン
            query += "  AND apdl.status = 0 ";//未送信
            query += "  AND apdl.push_seq = ? ";
            query += "  ORDER BY atu.os_type, apdl.user_id DESC";
        }
        else
        {
            // 予約アプリに配信のとき
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
            query += " WHERE apdl.type = 1 "; //キャンペーン
            query += "  AND apdl.status = 0 ";//未送信
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
                // レコード件数取得
                if ( result.last() != false )
                {
                    count = result.getRow();
                    // クラスの配列を用意し、初期化する。
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
     * キャンペーン用PUSH通知
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

            // URL情報を取得
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
                Logging.error( "[CampaignMessage]confファイルからURL情報を取得に失敗しました Exception:" + e.toString() );
                return false;
            }

            do
            {
            try
            {
            //PUSH通知するトークンリスト取得
            if( getApPushData(date, time))
            {

                for( int i = 0 ; i < this.dataApPushData.length ; i++ )
                {

                    //変数初期化
                    target_PushSeq = this.dataApPushData[i].getPushSeq();
                    target_CampaignId = this.dataApPushData[i].getCampaignId();
                    target_ApliKind = this.dataApPushData[i].getApliKind();
                    this.pushListCount = 0;
                    arrPush_a.clear();
                    arrPush_i.clear();

                    //プッシュ配信データ(ap_push_data)配信日時更新
                    if ( !this.updatePushData( target_PushSeq, STATUS_IN_PROCECCING ))
                    {
                        Logging.error( "CampaignMessage[this.updatePushData()]" );
                        throw new Exception();
                    }

                    //キャンペーン情報マスタ（ap_campaign_master）からキャンペーン詳細取得
                    if ( !dacm.getData(target_CampaignId) )
                    {
                        Logging.error( "CampaignMessage[dacm.getData("+target_CampaignId+")]","CampaignMessage" );
                        throw new Exception();
                    }
                    msg = dacm.getDetail();

                    //URL作成（jspを開いたときにap_push_data_listのconfirmdate等を更新するため、キー項目を送る）
                    String param = "";
                    param  = "?cpid=" +  + target_CampaignId;
                    param += "&type=1";//常に1（キャンペーン）
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


                    //リストを取得し、OSごとに配信用トークン配列を作成する
                    if( getApPushDataList( target_PushSeq ,target_ApliKind))
                    {
                        if(this.dataApPushDataList != null)
                        {
                            //変数初期化
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
                                        //変数クリア
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
                                        //変数クリア
                                        cnt_a = 0;
                                        tmp_a = "";
                                        listDt_a = new ArrayList<DataApPushDataListExtend>();
                                    }
                                }
                            }

                            //MAXPUSH未満のデータをセット
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

                    //android送信
                    //Logging.info( "android送信" + DateEdit.getDate( 1 ) + DateEdit.getTime( 0 ) + " this.arrPush_a.size()=" +  +this.arrPush_a.size(),"CampaignMessage ");
                    for( int j = 0 ; j < this.arrPush_a.size() ; j++ )
                    {
                        p2a = new Push2Android();
                        GooglePushMapper gpm = new GooglePushMapper();
                        gpm.addRegId( this.arrPush_a.get(j).getTokens());
                        gpm.createData( msg, url );
                        p2a.setApliKind( target_ApliKind );
                        if(p2a.push( gpm ))
                        {
                            //Logging.info( "プッシュ通知完了(アンドロイド）"+ this.arrPush_a.get(j).getTokens()  ,"CampaignMessage sendCampaignMessage()");

                            //送信エラートークン配列取得
                            ArrayList<String> inactiveTokenTmp = new ArrayList<String>();
                            inactiveTokenTmp = p2a.getInactiveToken();
                            //不正エラートークン配列取得
                            ArrayList<String> invalidTokenTmp = new ArrayList<String>();
                            invalidTokenTmp = p2a.getInvalidToken();

                            //大文字変換
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

                            //ap_push_data_list更新
                            if(!this.updatePushDataList( this.arrPush_a.get(j), inactiveToken, invalidToken))
                            {
                                Logging.error( "CampaignMessage[this..updatePushDataList()]ANDROID","CampaignMessage" );
                                throw new Exception();
                            }
                            //ap_user_campaignデータ作成（または更新）
                            if( !updateUserCampaign( this.arrPush_a.get(j), target_CampaignId, target_ApliKind) )
                            {
                                Logging.error( "CampaignMessage[this.updateUserCampaign()]" );
                                throw new Exception();
                            }
                            //ap_token_user またはap_rsv_token_uuidのエラーフラグ更新
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


                    //iPhone送信
                    //Logging.info( "iPhone送信" + DateEdit.getDate( 1 ) + DateEdit.getTime( 0 ) + " this.arrPush_i.size()="+this.arrPush_i.size(),"CampaignMessage" );
                    for( int j = 0 ; j < this.arrPush_i.size() ; j++ )
                    {
                        p2i = new Push2Iphone();
                        //Logging.info( "this.arrPush_i.get(j).getTokens()="+this.arrPush_i.get(j).getTokens(),"CampaignMessage" );
                        p2i.setApliKind( target_ApliKind );
                        if(p2i.push(  this.arrPush_i.get(j).getTokens(), msg, url ))
                        {

                            //Logging.info( "プッシュ通知完了（iOS）"+this.arrPush_i.get(j).getTokens() ,"CampaignMessage sendCampaignMessage()");

                            //送信エラートークン配列取得
                            ArrayList<String> inactiveTokenTmp = new ArrayList<String>();
                            inactiveTokenTmp = p2i.getInactiveToken();
                            //不正エラートークン配列取得
                            ArrayList<String> invalidTokenTmp = new ArrayList<String>();
                            invalidTokenTmp = p2i.getInvalidToken();

                            //大文字変換
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

                            //ap_push_data_list更新
                            if(!this.updatePushDataList( this.arrPush_i.get(j), inactiveToken, invalidToken))
                            {
                                Logging.error( "CampaignMessage[this.updatePushDataList()]iOS","CampaignMessage" );
                                throw new Exception();
                            }
                            //ap_user_campaignデータ作成（または更新）
                            if( !updateUserCampaign( this.arrPush_i.get(j), target_CampaignId, target_ApliKind) )
                            {
                                Logging.error( "CampaignMessage[this.updateUserCampaign()]" );
                                throw new Exception();
                            }
                            //ap_token_user またはap_rsv_token_uuidのエラーフラグ更新
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

                    //プッシュ配信データ(ap_push_data)Status更新
                    //Logging.info( "プッシュ配信データ(ap_push_data)Status更新" + DateEdit.getDate( 1 ) + DateEdit.getTime( 0 ),"CampaignMessage" );
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

                //プッシュ配信データ(ap_push_data)配信エラーにする
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
     * PUSH配信データリスト更新
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

                    //トークンの状態を取得
                    int status = chkTokenStatus( listPl.getToken(), inactiveToken, invalidToken );

                    if(status == 0)
                    {
                        dapdl.setStatus( 1 );//トークンが正常のとき送信済みにする
                    }
                    else
                    {
                        dapdl.setStatus( status );//送信エラーまたは、トークン不正
                    }

                }
                else
                {
                    dapdl.setStatus( 9 );//PUSH通知拒否データはステータスを削除にする
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
     * PUSH配信データ更新（配信日時）
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
                //配信中のとき
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
     * PUSH配信データ更新(Status, Count)
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
            dapd.setStatus( 3 );//配信済
            dapd.setPushCount( count );
            dapd.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dapd.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            ret = dapd.updateData( pushSeq );
        }
        return ret;
    }

    /****
     * apUserCampaign更新
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
     * error_flag更新
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

            //トークンの状態を取得
            status = chkTokenStatus( listPl.getToken(), inactiveToken, invalidToken );


            if ( apli_kind == 1)
            {
                //ハピホテアプリ用トークンのときap_user_tokenのerror_flag更新
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
                //予約アプリのときap_rsv_token_uuidのerror_flag更新
                if( !updateApRsvTokenUuid( token, status))
                {
                    ret= false;
                }
            }
        }

        return ret;
    }

    /****
     * トークンの状態取得
     *
     * @param token
     * @param inactiveToken
     * @param invalidToken
     * @return
     */
    public int chkTokenStatus( String token, ArrayList<String> inactiveToken, ArrayList<String> invalidToken )
    {

        int status = 0;//正常

        //トークンの状態を取得
        if(inactiveToken.indexOf(token.toUpperCase()) != -1)
        {
            status = 2;//送信エラー
        }
        if(invalidToken.indexOf(token.toUpperCase()) != -1)
        {
            status = 3;//トークン不正
        }

        return status;
    }

    /**
     * トークンユーザー(ap_rsv_token_uuid)更新
     *
     * @see "値のセット後(setXXX)に行うこと"
     * @param token iOS:device token,Android:registration id（トークンは書き換わるときあり）
     * @param uuid ユーザーID
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

            //エラーフラグ取得
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


            //エラーフラグに変更があるなら、更新
            if( ret && errorFlag != status)
            {
                query = "UPDATE newRsvDB.ap_rsv_token_uuid SET ";
                query += "  update_date=?";
                query += ", update_time=?";
                query += ", error_flag=?";
                query += " WHERE token=?";


                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                // 更新対象の値をセットする
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
