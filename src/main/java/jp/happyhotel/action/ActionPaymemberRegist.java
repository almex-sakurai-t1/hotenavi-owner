package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.AuAuthCheck;
import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.ConvertString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.RandomString;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.data.DataUserBasic;
import jp.happyhotel.others.FindConstellation;
import jp.happyhotel.user.UserBasicInfo;
import jp.happyhotel.user.UserCreditInfo;
import jp.happyhotel.user.UserPoint;
import jp.happyhotel.user.UserPointPay;

/**
 * 
 * 有料会員入会処理クラス
 * 
 * @author N.Ide
 * @version 1.0 2009/07/**
 * @see "about_premium.jsp TOPﾍﾟｰｼﾞなどから最初にｱｸｾｽした際(ﾌﾟﾚﾐｱﾑｺｰｽ説明画面)"
 * @see "paymember_index.jsp ﾌﾟﾚﾐｱﾑｺｰｽ説明画面で会員登録ﾘﾝｸを選択した際"
 * @see "paymember_registration.jsp ｷｬﾘｱ登録が完了した際(非会員から入会の場合のみ)"
 * @see "paymember_complete.jsp 有料会員登録が完了した際"
 */

public class ActionPaymemberRegist extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;
    public static final int   RS_PAY_TEMPMEMBER = 1;      // 非会員が有料会員になる際にｷｬﾘｱからOKが帰って来た時のRegistStatusPayの値
    public static final int   RS_OLD_NOTMEMBER  = 8;      // 非会員が有料会員になる際のRegistStatusOldの値
    public static final int   RS_MEMBER         = 9;      // 正会員のRegistStatusの値
    public static final int   USERID_NUM        = 10;     // ランダム生成する会員IDの桁数
    public static final int   PASSWORD_NUM      = 4;      // ランダム生成するﾊﾟｽﾜｰﾄﾞの桁数
    public static final int   SEX_UNKNOWN       = 2;      // 非会員から有料会員になる場合、性別にセットする値
    public static final int   REGIST_POINT      = 1000001; // 有料入会ポイントのポイントコード

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String paramStep = "1";
        String paramUserId = "";
        String paramPassword = "";
        String paramRegeneration = "";
        String termNo = "";
        String tempUserId;
        String tempPass = "";
        String forwardUrl = "";
        String strError = "";
        String paramUidLink;
        String paramAcRead;
        String paramMail = "";
        int stepNum = 1;
        int carrierFlag = 0;
        int nowDate;
        int nowTime;
        int docomoFlag = 0;
        boolean ret = false;
        boolean pcUserFlag = false;

        DataLoginInfo_M2 dli;
        DataUserBasic dub;
        AuAuthCheck auCheck;
        UserBasicInfo ubi;
        UserPointPay upp;
        UserPoint up;
        UserCreditInfo userCredit;

        dli = new DataLoginInfo_M2();
        dub = new DataUserBasic();
        ubi = new UserBasicInfo();
        upp = new UserPointPay();
        userCredit = new UserCreditInfo();

        paramStep = request.getParameter( "step" );
        paramUidLink = (String)request.getAttribute( "UID-LINK" );
        if ( paramStep == null || CheckString.numCheck( paramStep ) == false )
        {
            paramStep = "1";
        }
        stepNum = Integer.parseInt( paramStep );
        paramUserId = request.getParameter( "user_id" );
        paramPassword = request.getParameter( "password" );
        paramMail = request.getParameter( "mail" );
        paramRegeneration = request.getParameter( "regeneration" );
        if ( paramRegeneration == null )
        {
            paramRegeneration = "false";
        }

        // termNoの取得
        carrierFlag = UserAgent.getUserAgentType( request );
        if ( carrierFlag == UserAgent.USERAGENT_AU )
        {
            termNo = request.getHeader( "x-up-subno" );
        }
        else if ( carrierFlag == UserAgent.USERAGENT_VODAFONE )
        {
            termNo = request.getHeader( "x-jphone-uid" );
            if ( termNo != null )
            {
                termNo = termNo.substring( 1 );
            }
        }
        else if ( carrierFlag == UserAgent.USERAGENT_DOCOMO )
        {
            termNo = request.getParameter( "uid" );
            docomoFlag = 1;
        }

        // 端末番号が取得できなかったらエラーページへ飛ばす
        if ( termNo == null )
        {
            try
            {
                response.sendRedirect( "paymember_uid_error.jsp?" + paramUidLink );
            }
            catch ( Exception e )
            {
                Logging.info( "[ActionPaymemberRegist.sendRedirect] Exception:" + e.toString() );
            }
        }

        // 日時取得
        nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );

        try
        {
            dli = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );

            // auだったらアクセスチケットをチェックする
            paramAcRead = request.getParameter( "acread" );
            // carrierFlag = UserAgent.getUserAgentType( request );
            if ( (paramAcRead == null) && (carrierFlag == DataMasterUseragent.CARRIER_AU) )
            {
                try
                {
                    auCheck = new AuAuthCheck();
                    // DataLoginInfo_M2にデータを入れないために、JSP側のメソッドを呼ぶ
                    ret = auCheck.authCheckForClass( request, false );
                    // アクセスチケット確認の結果 falseだったらリダイレクト
                    if ( ret == false )
                    {
                        response.sendRedirect( auCheck.getResultData() );
                        return;
                    }
                    // アクセスチケット確認の結果 trueだったら情報を取得
                    else
                    {
                        // DataLoginInfo_M2を取得する
                        if ( auCheck.getDataLoginInfo() != null )
                        {
                            // dli = auCheck.getDataLoginInfo();
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[ActionEmptySearch AuAuthCheck] Exception:" + e.toString() );
                }
            }
            // アクセスチケットがNGの場合、再入会の意思があるということなので、データを削除する
            // キャリアチェックの際はコメントをはずす。
            /*
             * if ( paramAcRead != null )
             * {
             * if ( dli != null )
             * {
             * // 有料会員だったら削除
             * if ( dli.getRegistStatusPay() == 9 )
             * {
             * if ( dub.getData( dli.getUserId() ) != false || dub != null )
             * {
             * dub.setDelDatePay( nowDate );
             * dub.setDelTimePay( nowTime );
             * dub.setPointPay( 0 );
             * dub.setPointPayUpdate( nowDate );
             * // 非会員から有料会員になった場合、del_flagを1にする。
             * if ( dub.getRegistStatusOld() == 8 )
             * {
             * dub.setDelFlag( 1 );
             * }
             * // 無料会員から有料会員になった場合、有料会員になった時点のregist_statusに戻す
             * else
             * {
             * // regist_status_oldがとりえる値以外の場合は処理を行わない
             * if ( dub.getRegistStatusOld() == 2 || dub.getRegistStatusOld() == 3 || dub.getRegistStatusOld() == 9 )
             * {
             * dub.setRegistStatus( dub.getRegistStatusOld() );
             * }
             * }
             * dub.setRegistStatusPay( 0 );
             * dub.updateData( dli.getUserId() );
             * dli = null;
             * dub = null;
             * // 再度インスタンス化する
             * dub = new DataUserBasic();
             * }
             * }
             * }
             * // dliガ無くて端末番号を取得→有料会員途中の場合
             * else if ( ubi.getUserBasicByTermnoNoCheck( termNo ) != false )
             * {
             * if ( ubi.getUserInfo().getRegistStatusPay() == 1 )
             * {
             * ubi.getUserInfo().deleteData( ubi.getUserInfo().getUserId() );
             * }
             * ubi = null;
             * ubi = new UserBasicInfo();
             * }
             * }
             */

            // 非会員から入会（PCのみユーザ含む）またはdocomoﾏｲﾒﾆｭｰ登録のみでID,passのないユーザの場合
            if ( dli == null || dli.getRegistStatus() == 1 )
            {
                if ( ubi.getUserBasicByTermnoNoCheck( termNo ) != false && ubi != null )
                {
                    if ( dub.getData( ubi.getUserInfo().getUserId() ) != false && dub != null )
                    {
                        // 引継ぎ入力画面へ遷移
                        if ( stepNum == 5 && dub.getRegistStatusPay() == 1 )
                        {
                            forwardUrl = "paymember_registration_handover.jsp";
                        }
                        // 引継ぎ確認画面へ
                        else if ( stepNum == 6 && dub.getRegistStatusPay() == 1 )
                        {
                            ubi.getUserBasicByAll( paramUserId );

                            strError = this.checkUserInfo( ubi.getUserInfo(), paramUserId, paramPassword, paramMail );
                            if ( strError.equals( "" ) != false )
                            {
                                // 引継ぐ情報をセットする
                                dub = ubi.getUserInfo();

                                // 引継ぎを完了させる
                                dub.setUserId( paramUserId );
                                dub.setPasswd( paramPassword );
                                if ( dub.getDelFlag() == 1 )
                                {
                                    dub.setDelFlag( 0 );
                                }
                                dub.setRegistStatus( RS_MEMBER );
                                dub.setRegistStatusPay( RS_MEMBER );
                                dub.setRegistDatePay( nowDate );
                                dub.setRegistTimePay( nowTime );
                                dub.setDocomoFlag( docomoFlag );
                                // キャリア変更時に前の端末でのログインを防ぐため、端末番号でMD5を更新しておく(docomo対応)
                                dub.setMailAddrMobileMd5( ConvertString.convert2md5( termNo ) );

                                dub.setMobileTermNo( termNo );
                                dub.setRegistStatusOld( dub.getRegistStatus() );

                                ret = dub.updateData( paramUserId );
                                if ( ret != false )
                                {

                                    forwardUrl = "paymember_complete.jsp";
                                    // 成功したので、有料ポイントを付与
                                    upp.setPoint( dub.getUserId(), REGIST_POINT, 0, "" );
                                    // 無料入会していないユーザに無料入会ポイントを付与する
                                    if ( dub.getRegistStatusOld() == 2 || dub.getRegistStatusOld() == 3 )
                                    {
                                        // 無料ポイントを付与
                                        up = new UserPoint();
                                        up.setPointJoin( dub.getUserId() );
                                    }

                                    // 最後に端末番号のユーザIDの情報を削除
                                    ret = dub.deleteData( termNo );
                                    Logging.info( "[ActionPaymemberRegist.execute] deleteData=" + ret + " 非会員から有料会員(仮ﾃﾞｰﾀ削除)" );
                                }
                                else
                                {
                                    strError += "ﾕｰｻﾞ情報の引継ぎができませんでした。<br>";
                                    forwardUrl = "paymember_registration_handover.jsp";
                                    request.setAttribute( "ERROR", strError );
                                    request.setAttribute( "USER-ID", paramUserId );
                                    request.setAttribute( "PASSWORD", paramPassword );
                                    request.setAttribute( "MAIL", paramMail );
                                }

                            }
                            else
                            {
                                forwardUrl = "paymember_registration_handover.jsp";
                                request.setAttribute( "ERROR", strError );
                                request.setAttribute( "USER-ID", paramUserId );
                                request.setAttribute( "PASSWORD", paramPassword );
                                request.setAttribute( "MAIL", paramMail );
                            }

                        }
                        // userIdとPassが入力されている場合、内容をﾁｪｯｸ
                        else if ( stepNum == 4 && dub.getRegistStatusPay() == 1 )
                        {
                            // IDとpassのﾁｪｯｸ
                            strError = checkIdPass( paramUserId, paramPassword );

                            if ( strError.compareTo( "true" ) == 0 )
                            {
                                // カード会員かどうかをチェック
                                if ( (userCredit.getPayMemberFlag( paramUserId ) != false) || (userCredit.getNgMemberFlag( paramUserId ) != false) )
                                {
                                    strError = "そのIDはPCまたはスマートフォンで登録しているため、使用できません。<br>";
                                }
                            }
                            if ( strError.compareTo( "true" ) != 0 )
                            {
                                request.setAttribute( "ERROR-WORD", strError );
                                forwardUrl = "paymember_registration.jsp";
                                tempUserId = getUserId();
                                tempPass = RandomString.getRandomNumber( PASSWORD_NUM );
                                request.setAttribute( "USER-ID", tempUserId );
                                request.setAttribute( "PASSWORD", tempPass );
                            }
                            // ID,pass自体に問題がなければ会員登録処理
                            else
                            {
                                ret = false;

                                // 既存ﾕｰｻﾞﾁｪｯｸ
                                if ( ubi.getUserBasicByAll( paramUserId ) != false && ubi != null )
                                {
                                    dub = new DataUserBasic();
                                    if ( dub.getData( paramUserId ) != false && dub != null && dub.getUserId().compareTo( "" ) != 0 )
                                    {
                                        // ｱｸｾｽしている携帯のtermNoとDBのtermNoが異なったらｴﾗｰにする
                                        // ID,passともに一致したらPCユーザ
                                        if ( dub.getPasswd().compareTo( paramPassword ) == 0 )
                                        {
                                            if ( dub.getMobileTermNo().compareTo( "" ) != 0 )
                                            {
                                                strError = "ご入力いただいたIDではﾊﾋﾟﾎﾃﾌﾟﾚﾐｱﾑｺｰｽのご登録は出来ません。<br>";
                                                strError += "携帯電話会社変更を行われたお客様で情報引継ぎを希望されるお客様は、先に無料会員登録が必要になります";
                                                request.setAttribute( "CARRIER_CHANGE", "1" );
                                            }
                                            else
                                            {
                                                pcUserFlag = true;
                                            }
                                        }
                                        // passが一致しない場合は使用済みｴﾗｰ
                                        else
                                        {
                                            strError = "入力したIDは使用済みIDとなっております。お手数ですが他のIDをご登録下さい。<br>";
                                        }
                                    }
                                    // 削除済みユーザだった場合はｴﾗｰ
                                    else
                                    {
                                        strError = "そのIDはご使用できないﾕｰｻﾞｰIDのため登録できません。お手数ですが他のIDをご登録下さい。<br>";
                                    }
                                }

                                // 登録済みIDかつpassが正しくない場合
                                if ( strError.compareTo( "true" ) != 0 )
                                {
                                    request.setAttribute( "ERROR-WORD", strError );
                                    tempUserId = getUserId();
                                    tempPass = RandomString.getRandomNumber( PASSWORD_NUM );
                                    request.setAttribute( "USER-ID", tempUserId );
                                    request.setAttribute( "PASSWORD", tempPass );
                                    forwardUrl = "paymember_registration.jsp";
                                }
                                else
                                // 未登録IDの場合またはID,Passともに正しい場合
                                {
                                    dub.setUserId( paramUserId );
                                    dub.setPasswd( paramPassword );
                                    dub.setRegistStatus( RS_MEMBER );
                                    dub.setRegistStatusPay( RS_MEMBER );
                                    dub.setRegistDatePay( nowDate );
                                    dub.setRegistTimePay( nowTime );
                                    dub.setDocomoFlag( docomoFlag );
                                    // キャリア変更時に前の端末でのログインを防ぐため、端末番号でMD5を更新しておく(docomo対応)
                                    dub.setMailAddrMobileMd5( ConvertString.convert2md5( termNo ) );

                                    // PCユーザの場合、そのIDのデータをupdate
                                    if ( pcUserFlag != false )
                                    {
                                        dub.setMobileTermNo( termNo );
                                        dub.setRegistStatusOld( dub.getRegistStatus() );
                                        ret = dub.updateData( paramUserId );
                                        Logging.info( "[ActionPaymemberRegist.execute] updateData = " + ret + " 非会員から有料会員(ID,Pass登録)" );
                                    }
                                    // 新規ユーザの場合、insert
                                    else
                                    {
                                        dub.setSex( SEX_UNKNOWN );
                                        // 会員データがない場合
                                        if ( dli == null )
                                        {
                                            dub.setRegistStatusOld( RS_OLD_NOTMEMBER );
                                        }
                                        else
                                        {
                                            if ( dli.getRegistStatus() > 0 )
                                            {
                                                dub.setRegistStatusOld( dli.getRegistStatus() );
                                            }
                                            else
                                            {
                                                dub.setRegistStatusOld( RS_OLD_NOTMEMBER );
                                            }
                                        }
                                        ret = dub.insertData();
                                        Logging.info( "[ActionPaymemberRegist.execute] insertData = " + ret + " 非会員から有料会員(ID,Pass登録)" );
                                    }
                                    // ID=termNoで保存した仮データを削除
                                    if ( ret != false )
                                    {
                                        ret = dub.deleteData( termNo );
                                        Logging.info( "[ActionPaymemberRegist.execute] deleteData=" + ret + " 非会員から有料会員(仮ﾃﾞｰﾀ削除)" );
                                    }

                                    // updateに失敗した際の処理(ｷｬﾘｱ登録済のため要注意、ただし通常では起こり得ない)
                                    if ( ret == false )
                                    {
                                        forwardUrl = "paymember_error.jsp";
                                    }
                                    // updateに成功した際の処理
                                    else
                                    {
                                        forwardUrl = "paymember_complete.jsp";
                                        // 成功したので、有料ポイントを付与
                                        upp.setRegistPoint( paramUserId, REGIST_POINT, 0, "" );
                                        // 無料ポイントを付与
                                        up = new UserPoint();
                                        up.setPointJoin( paramUserId );
                                    }
                                }
                            }
                        }
                        // regist_status_payが"1"のユーザはｷｬﾘｱ登録済みでﾊﾋﾟﾎﾃ未登録のため、userIdとPassを打たせる
                        else if ( dub.getRegistStatusPay() == 1 )
                        {
                            // 会員IDをランダム生成する(この時点ではDBには書き込まない)
                            tempUserId = getUserId();
                            tempPass = RandomString.getRandomNumber( PASSWORD_NUM );
                            request.setAttribute( "USER-ID", tempUserId );
                            request.setAttribute( "PASSWORD", tempPass );
                            stepNum = 4;
                            forwardUrl = "paymember_registration.jsp";
                        }
                    }
                }
                // ﾌﾟﾚﾐｱﾑｺｰｽ説明ﾍﾟｰｼﾞへ
                if ( stepNum == 1 )
                {
                    forwardUrl = "about_premium.jsp";
                }
                // 規約同意確認ﾍﾟｰｼﾞへ
                else if ( stepNum == 2 )
                {
                    forwardUrl = "paymember_index.jsp";
                }
                // ｷｬﾘｱ登録から戻ってきた場合の処理
                else if ( stepNum == 3 && paramRegeneration.compareTo( "true" ) != 0 )
                {
                    ret = false;
                    // 会員IDをランダム生成する(この時点ではDBには書き込まない)
                    tempUserId = getUserId();
                    tempPass = RandomString.getRandomNumber( PASSWORD_NUM );
                    request.setAttribute( "USER-ID", tempUserId );
                    request.setAttribute( "PASSWORD", tempPass );

                    // termNoで一時的に会員登録
                    dub.setUserId( termNo );
                    dub.setMobileTermNo( termNo );
                    dub.setRegistStatusPay( RS_PAY_TEMPMEMBER );
                    dub.setRegistStatusOld( RS_OLD_NOTMEMBER );
                    dub.setRegistDatePay( nowDate );
                    dub.setRegistTimePay( nowTime );
                    dub.setDocomoFlag( docomoFlag );
                    // キャリア変更時に前の端末でのログインを防ぐため、端末番号でMD5を更新しておく(docomo対応)
                    dub.setMailAddrMobileMd5( ConvertString.convert2md5( termNo ) );
                    ret = dub.insertData();
                    // insertに失敗した際の処理(ｷｬﾘｱ登録済のため要注意)
                    if ( ret == false )
                    {
                        Logging.error( "[ActionPaymemberRegist.execute] insertData=" + ret + " 非会員から有料会員(仮ﾃﾞｰﾀ登録)" );
                        forwardUrl = "paymember_error.jsp";
                    }
                    // insertに成功した場合
                    else
                    {
                        forwardUrl = "paymember_registration.jsp";
                    }
                }
            }
            // 無料会員から入会の場合(dli!=null)
            else
            {
                // Logging.info( "[ActionPaymemberRegist.execute] RegistStatus=" + dli.getRegistStatus() + " RegistStatusPay=" + dli.getRegistStatusPay() + " RegistStatusOld=" + dli.getRegistStatusOld() ); // test log
                // ﾌﾟﾚﾐｱﾑｺｰｽ説明ﾍﾟｰｼﾞへ
                if ( stepNum == 1 )
                {
                    forwardUrl = "about_premium.jsp";
                }
                // 規約同意確認ﾍﾟｰｼﾞへ
                else if ( stepNum == 2 )
                {
                    forwardUrl = "paymember_index.jsp";
                }
                // 各ｷｬﾘｱからOKが帰ってきた場合
                else if ( stepNum == 3 && dli.getRegistStatusPay() != 9 )
                {
                    // 元から無料会員だった場合は登録完了画面へ（カードメンバー登録されていない場合のみ）
                    if ( dli != null && dli.getCardmemberFlag() == false && dli.getCardmemberNgFlag() == false )
                    {
                        ret = false;
                        if ( dub.getData( dli.getUserId() ) != false && dub != null )
                        {
                            // 性別が登録されていない場合は｢不明｣で登録
                            if ( dub.getRegistStatus() == 1 || dub.getRegistStatus() == 2 )
                            {
                                dub.setSex( SEX_UNKNOWN );
                            }
                            dub.setRegistStatusOld( dli.getRegistStatus() );
                            dub.setRegistStatus( RS_MEMBER );
                            dub.setRegistStatusPay( RS_MEMBER );
                            dub.setRegistDatePay( nowDate );
                            dub.setRegistTimePay( nowTime );
                            dub.setDocomoFlag( docomoFlag );
                            // キャリア変更時に前の端末でのログインを防ぐため、端末番号でMD5を更新しておく(docomo対応)
                            dub.setMailAddrMobileMd5( ConvertString.convert2md5( termNo ) );
                            if ( dub.getBirthdayMonth() > 0 && dub.getBirthdayDay() > 0 )
                            {
                                dub.setConstellation( FindConstellation.getConstellation( dub.getBirthdayMonth() * 100 + dub.getBirthdayDay() ) );
                            }
                            ret = dub.updateData( dli.getUserId() );
                        }
                        // updateに失敗した際の処理(ｷｬﾘｱ登録済のため要注意、ただし通常では起こり得ない)
                        if ( ret == false )
                        {
                            Logging.error( "[ActionPaymemberRegist.execute] updateData=" + ret + " 無料会員から有料会員" );
                            forwardUrl = "paymember_error.jsp";
                        }
                        // updateに成功した際の処理
                        else
                        {
                            forwardUrl = "paymember_complete.jsp";
                            // 成功したので、有料ポイントを付与
                            upp.setPoint( dli.getUserId(), REGIST_POINT, 0, "" );
                            // 無料入会していないユーザに無料入会ポイントを付与する
                            if ( dub.getRegistStatusOld() == 2 || dub.getRegistStatusOld() == 3 )
                            {
                                // 無料ポイントを付与
                                up = new UserPoint();
                                up.setPointJoin( dli.getUserId() );
                            }
                        }
                    }
                    else
                    {
                        ret = true;
                    }
                }
                // auでアクセスチケットからデータ更新されたユーザにのみ
                else if ( (paramAcRead == null) && (carrierFlag == DataMasterUseragent.CARRIER_AU) && stepNum == 3 && dli.getRegistStatusPay() == 9 )
                {
                    forwardUrl = "paymember_complete.jsp";
                }
            }
            // forwardUrlが入っていない場合はTOPにリダイレクト
            if ( forwardUrl.compareTo( "" ) == 0 )
            {
                response.sendRedirect( "../../index.jsp?" + paramUidLink );
                return;
            }
            Logging.info( "[ActionPaymemberRegist.execute] forwardUrl=" + forwardUrl );
            requestDispatcher = request.getRequestDispatcher( forwardUrl );
            requestDispatcher.forward( request, response );
        }
        catch ( Exception e )
        {
            Logging.error( "ActionPaymemberRegist.execute() Exception=" + e.toString() );
        }
        finally
        {
            pcUserFlag = false;
            strError = "";
            forwardUrl = "";
            dli = null;
            dub = null;
            ubi = null;
        }
    }

    /**
     * 英字を含む未登録の会員IDを返す
     * 
     * @return 処理結果("":失敗)
     */
    public static String getUserId()
    {
        String userId;
        UserBasicInfo ubi;

        ubi = new UserBasicInfo();

        // 問題のない会員IDが生成されたらbreak
        while( true )
        {
            // userId = RandomString.getRandomString( USERID_NUM );
            userId = RandomString.getRandomString( USERID_NUM );
            // ユーザーID数値チェック(数字以外が含まれていればOK)
            if ( CheckString.numCheck( userId ) == false )
            {
                // 同じIDが登録されていなければOK
                if ( ubi.getUserBasicByAll( userId ) == false )
                {
                    break;
                }
            }
        }
        return(userId);
    }

    /**
     * IDとﾊﾟｽﾜｰﾄﾞのﾁｪｯｸ(重複ﾁｪｯｸなし)
     * 
     * @param userId ユーザID
     * @param password ﾊﾟｽﾜｰﾄﾞ
     * @return エラー文言（エラーなし："true"）
     */

    public static String checkIdPass(String userId, String password)
    {
        String strError = "";
        boolean ret = true;

        // IDに半角英数以外がある場合
        if ( CheckString.numAlphaCheck( userId ) == false )
        {
            strError += "IDは半角英数字で入力して下さい。<br>";
            ret = false;
        }
        // IDが4文字未満の場合
        if ( userId.length() < 4 )
        {
            strError += "IDは4文字以上入力して下さい。<br>";
            ret = false;
        }
        // IDが10文字より多い場合
        if ( userId.length() > 11 )
        {
            strError += "IDは10文字以内で入力して下さい。<br>";
            ret = false;
        }
        // IDが数字のみの場合
        if ( CheckString.numCheck( userId ) != false )
        {
            strError += "IDは数字だけでは登録できません。必ず英字を1文字以上入れて下さい。<br>";
            ret = false;
        }
        // IDとPassがまったく同じ場合
        if ( userId.compareTo( password ) == 0 )
        {
            strError += "IDと同じﾊﾟｽﾜｰﾄﾞの設定はできません。<br>";
            ret = false;
        }
        // passに半角英数以外がある場合
        if ( CheckString.numAlphaCheck( password ) == false )
        {
            strError += "ﾊﾟｽﾜｰﾄﾞは半角英数字で入力して下さい。<br>";
            ret = false;
        }
        // passが4文字未満の場合
        if ( password.length() < 4 )
        {
            strError += "ﾊﾟｽﾜｰﾄﾞは4文字以上入力して下さい。<br>";
            ret = false;
        }
        // passが16文字より多い場合
        if ( password.length() > 17 )
        {
            strError += "ﾊﾟｽﾜｰﾄﾞは16文字以内で入力して下さい。<br>";
            ret = false;
        }

        if ( ret == false )
        {
            return(strError);
        }
        else
        {
            return("true");
        }
    }

    /***
     * 引継ぎ確認
     * 
     * @param dub ユーザ基本情報クラス
     * @param userId ユーザID
     * @param pass パスワード
     * @param mail メールアドレス
     * @return
     */
    public String checkUserInfo(DataUserBasic dub, String userId, String pass, String mail)
    {
        String strError = "";
        UserCreditInfo userCredit;

        if ( userId.equals( "" ) != false )
        {
            strError += "ﾕｰｻﾞIDを入力してください。<br>";
        }
        if ( pass.equals( "" ) != false )
        {
            strError += "ﾊﾟｽﾜｰﾄﾞを入力してください。<br>";
        }
        if ( mail.equals( "" ) != false )
        {
            strError += "登録ﾒｰﾙｱﾄﾞﾚｽを入力してください。<br>";
        }

        // 初歩的なエラーがなかったら中身をチェック
        if ( strError.equals( "" ) != false )
        {
            if ( dub.getUserId().equals( "" ) != false )
            {
                strError += "ﾕｰｻﾞ情報が取得できませんでした。<br>";
            }

            if ( dub.getPasswd().equals( pass ) == false )
            {
                strError += "ﾊﾟｽﾜｰﾄﾞが一致しません。<br>";
            }

            if ( dub.getMailAddr().equals( mail ) == false && dub.getMailAddrMobile().equals( mail ) == false )
            {
                strError += "ﾒｰﾙｱﾄﾞﾚｽが一致しません。<br>";
            }
        }
        // 本人確認ができたらさらに中身をチェック
        if ( strError.equals( "" ) != false )
        {
            int today = Integer.parseInt( DateEdit.getDate( 2 ) );
            int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
            // 既に退会済みの場合は有料会員のみ引継げるようにする
            if ( dub.getDelFlag() == 1 && dub.getRegistStatusOld() > 0 )
            {
                // 有効期限が過ぎていたら引継ぎ不可能
                if ( DateEdit.isValidDate( dub.getDelDatePay(), dub.getDelTimePay(), 2, 90 ) == false )
                {
                    strError += "そのIDは引継ぎ期限切れです。登録できません。<br>";
                }
            }

            userCredit = new UserCreditInfo();
            // カード会員かどうかをチェック
            if ( (userCredit.getPayMemberFlag( dub.getUserId() ) != false) || (userCredit.getNgMemberFlag( dub.getUserId() ) != false) )
            {
                strError += "そのIDはPCまたはスマートフォンで登録しているため、使用できません。<br>";
            }
        }

        return strError;
    }
}
