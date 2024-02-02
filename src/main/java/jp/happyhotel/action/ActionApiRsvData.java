package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.others.GenerateXmlRsvData;
import jp.happyhotel.others.GenerateXmlRsvDataSub;
import jp.happyhotel.others.GenerateXmlSearchResult;
import jp.happyhotel.user.FormUserRsvList;
import jp.happyhotel.user.LogicUserRsvList;
import jp.happyhotel.user.UserLoginInfo;

public class ActionApiRsvData extends BaseAction
{
    static int    pageRecords         = Constants.pageLimitRecords;
    static String recordsNotFoundMsg2 = Constants.errorRecordsNotFound2;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        final int dispDataNum = 100; // 1�y�[�W�ɕ\�����闚���̌���
        final int dispMaxPage = 10; // �\������y�[�W�����N�̍ő吔
        int maxPage = 10; // �\������ő�y�[�W��
        int nPage;
        int i;
        int dispDate = 0;
        int startDate;
        int endDate;
        int rsvCount = 0;
        int errorCode = 0;
        boolean ret = false;
        String paramPage;
        String pageRecords;
        String pageLinks;
        String paramRsvNo = "";
        String paramMethod = null;
        String errorMsg = "";
        FormUserRsvList formUser;
        LogicUserRsvList logicUser;
        DataHotelBasic dhb = new DataHotelBasic();
        UserLoginInfo uli;
        GenerateXmlHeader xmlHeader = new GenerateXmlHeader();
        GenerateXmlRsvData xmlRsvData = new GenerateXmlRsvData();
        GenerateXmlRsvDataSub xmlRsvDataSub = new GenerateXmlRsvDataSub();

        formUser = new FormUserRsvList();
        logicUser = new LogicUserRsvList();
        uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
        paramMethod = request.getParameter( "method" );
        paramPage = request.getParameter( "page" );
        paramRsvNo = request.getParameter( "rsv_no" );

        if ( uli == null )
        {
            uli = new UserLoginInfo();
        }
        if ( paramPage != null )
        {
            if ( CheckString.numCheck( paramPage ) != false )
            {
                nPage = Integer.parseInt( paramPage );
                // ����y�[�W�ȍ~���v�����ꂽ�ꍇ�A����y�[�W�ɔ�΂�
                if ( nPage >= maxPage )
                {
                    nPage = maxPage - 1;
                }
            }
            else
            {
                nPage = 0;
            }
        }
        else
        {
            nPage = 0;
        }
        if ( (paramRsvNo == null) || (paramRsvNo.length() == 0) )
        {
            paramRsvNo = "";
        }
        try
        {
            if ( uli.getUserInfo() != null && uli.isMemberFlag() != false )
            {

                startDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                dispDate = startDate;
                endDate = DateEdit.addMonth( Integer.parseInt( DateEdit.getDate( 2 ) ), 2 );
                formUser.setUserId( uli.getUserInfo().getUserId() );
                formUser.setDateFrom( startDate );
                formUser.setDateTo( endDate );
                formUser.setPageAct( nPage );
                logicUser.setUserId( uli.getUserInfo().getUserId() );
                logicUser.setRsvNo( paramRsvNo );

                /*
                 * �\����̎擾
                 * 2015.4.28 �\����݂̂ɕύX hh_rsv_reserve.status�@5��1
                 */
                ret = logicUser.getData( formUser, 1, nPage, dispDataNum, "PARTS", 1 );

                if ( ret != false )
                {
                    rsvCount = formUser.getIdList().size();
                    for( i = 0 ; i < formUser.getIdList().size() ; i++ )
                    {
                        xmlRsvDataSub = new GenerateXmlRsvDataSub();
                        dhb.getData( formUser.getIdList().get( i ) );
                        xmlRsvDataSub.setDate( formUser.getRsvDateValList().get( i ) );
                        xmlRsvDataSub.setName( dhb.getName() );
                        xmlRsvDataSub.setRsvNo( formUser.getReserveNoList().get( i ) );
                        xmlRsvData.setRsvDataSub( xmlRsvDataSub );
                    }
                }
            }
            else
            {
                rsvCount = 0;
                // ���[�U��{���擾��null�̏ꍇ
                errorCode = Constants.ERROR_CODE_API16;
                errorMsg = Constants.ERROR_MSG_API16;
            }

            xmlRsvData.setErrorCode( errorCode );
            xmlRsvData.setError( errorMsg );

            // �������ʃw�b�_�쐬
            GenerateXmlHeader header = new GenerateXmlHeader();
            header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            header.setMethod( paramMethod );
            header.setName( "�\��ꗗ" );
            header.setCount( rsvCount );

            // �ǉ�����XML��ǉ�����
            header.setRsvData( xmlRsvData );

            String xmlOut = header.createXml();
            ServletOutputStream out = null;

            out = response.getOutputStream();
            response.setContentType( "text/xml; charset=UTF-8" );
            response.setContentLength( xmlOut.getBytes().length ); // �d������ǉ�5/11
            out.write( xmlOut.getBytes( "UTF-8" ) );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionApiRsvData()] Exception:" + exception.toString() );

            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( Constants.ERROR_MSG_API10 );
            searchResult.setResultCount( 0 );

            // �������ʃw�b�_�쐬
            xmlHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            xmlHeader.setMethod( paramMethod );
            xmlHeader.setName( "�\��ꗗ" );
            xmlHeader.setCount( 0 );
            // �z�e���ڍׂ�ǉ�
            xmlHeader.setSearchResult( searchResult );

            String xmlOut = xmlHeader.createXml();
            ServletOutputStream out = null;

            try
            {
                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionApiRsvData response]Exception:" + e.toString() );
            }
        }
        finally
        {
        }

    }
}
