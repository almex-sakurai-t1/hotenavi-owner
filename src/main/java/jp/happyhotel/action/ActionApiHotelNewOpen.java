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
import jp.happyhotel.data.DataMasterPref;
import jp.happyhotel.others.GenerateXmlContents;
import jp.happyhotel.others.GenerateXmlContentsHotel;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.search.SearchHotelNewOpen;

/**
 *
 * �V�K�z�e���\���N���X
 *
 * @author S.Tashiro
 * @version 1.0 2011/05/02
 */

public class ActionApiHotelNewOpen extends BaseAction
{

    final private static int DISP_MAX_NUMBER = 20;

    /**
     *
     *
     * @param request �N���C�A���g����T�[�o�ւ̃��N�G�X�g
     * @param response �T�[�o����N���C�A���g�ւ̃��X�|���X
     * @see "/�L�����A�̃t�H���_/search/hotelmap_M2.jsp ���܂��������ꍇ�ɑJ�ڂ���"
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        boolean ret;
        int lastday;
        String paramLocalId;
        String paramPrefId;
        String paramPage;
        String paramMethod;
        String prefName = "�S��";
        String name;
        String strDate;
        int kind;
        DataHotelBasic dhb;
        DataMasterPref dmp;
        SearchHotelNewOpen shno;

        dhb = new DataHotelBasic();
        dmp = new DataMasterPref();
        shno = new SearchHotelNewOpen();

        paramLocalId = request.getParameter( "local_id" );
        paramPrefId = request.getParameter( "pref_id" );
        paramPage = request.getParameter( "page" );
        paramMethod = request.getParameter( "method" );

        if ( paramPage == null || paramPage.equals( "" ) != false || CheckString.numCheck( paramPage ) == false )
        {
            paramPage = "0";
        }
        if ( paramLocalId == null || paramLocalId.equals( "" ) != false || CheckString.numCheck( paramLocalId ) == false )
        {
            paramLocalId = "0";
        }
        if ( paramPrefId == null || paramPrefId.equals( "" ) != false || CheckString.numCheck( paramPrefId ) == false )
        {
            paramPrefId = "0";
        }
        if ( paramMethod == null )
        {
            paramMethod = "";
        }

        try
        {
            ret = shno.getHotelListByPref( Integer.parseInt( paramPrefId ), 0, Integer.parseInt( DateEdit.getDate( 2 ) ), DISP_MAX_NUMBER, Integer.parseInt( paramPage ) );
            // �n��ID�A�s���{��ID
            if ( paramPrefId.equals( "0" ) == false )
            {
                dmp.getData( Integer.parseInt( paramPrefId ) );
                prefName = dmp.getName();
            }

            if ( ret != false )
            {
                GenerateXmlContents contents = new GenerateXmlContents();
                contents.setError( "" );
                contents.setErrorCode( 0 );
                contents.setResultCount( shno.getCount() );

                // �z�e���̃f�[�^���Z�b�g
                if ( shno.getCount() > 0 )
                {
                    for( int i = 0 ; i < shno.getCount() ; i++ )
                    {
                        name = "";
                        kind = 0;

                        GenerateXmlContentsHotel hotel = new GenerateXmlContentsHotel();

                        // ���t
                        if ( shno.getHotelInfo()[i].getRenewalDateText().compareTo( "" ) != 0 )
                        {
                            strDate = shno.getHotelInfo()[i].getRenewalDateText();
                        }
                        else
                        {
                            strDate = Integer.toString( shno.getHotelInfo()[i].getRenewalDate() / 10000 ) + "."
                                    + String.format( "%1$02d", shno.getHotelInfo()[i].getRenewalDate() / 100 % 100 ) + "."
                                    + String.format( "%1$02d", shno.getHotelInfo()[i].getRenewalDate() % 100 );
                        }
                        hotel.setDate( strDate );

                        // �V�z�E���j���[�A������
                        if ( shno.getHotelInfo()[i].getOpenDate() == shno.getHotelInfo()[i].getRenewalDate() )
                        {
                            kind = 1;
                        }
                        else
                        {
                            kind = 2;
                        }
                        hotel.setNew( kind );
                        name = shno.getHotelInfo()[i].getName();
                        hotel.setName( name );

                        hotel.setAddress( shno.getHotelInfo()[i].getPrefName() + shno.getHotelInfo()[i].getAddress1() );
                        hotel.setId( shno.getHotelInfo()[i].getId() );

                        contents.addHotel( hotel );
                    }
                }
                else
                {
                    contents.setError( Constants.ERROR_MSG_API6 );
                    contents.setErrorCode( Constants.ERROR_CODE_API6 );
                    contents.setResultCount( shno.getCount() );
                }

                GenerateXmlHeader header = new GenerateXmlHeader();
                header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                header.setMethod( paramMethod );
                header.setName( prefName );
                header.setCount( shno.getAllCount() );
                // �N�`�R�~�ڍׂ�ǉ�
                header.setContents( contents );

                // �o�͂��w�b�_�[����
                String xmlOut = header.createXml();
                ServletOutputStream out = null;

                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionApiHotelNewOpen]Exception:" + e.toString() );
        }
    }
}
