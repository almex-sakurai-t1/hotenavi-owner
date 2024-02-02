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
import jp.happyhotel.hotel.HotelPv;
import jp.happyhotel.hotel.HotelUniquePv;
import jp.happyhotel.others.GenerateXmlContents;
import jp.happyhotel.others.GenerateXmlContentsHotel;
import jp.happyhotel.others.GenerateXmlHeader;

/**
 * 
 * PV�����L���O�\���N���X
 * 
 * @author S.Tashiro
 * @version 1.0 2009/07/07
 */

public class ActionApiHotelPvRank extends BaseAction
{

    final private static int DISP_MAX_NUMBER = 30;

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
        DataHotelBasic dhb;
        DataMasterPref dmp;
        HotelPv hotelPv;
        HotelUniquePv hotelUuPv;

        dhb = new DataHotelBasic();
        dmp = new DataMasterPref();
        hotelPv = new HotelPv();
        hotelUuPv = new HotelUniquePv();

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
            lastday = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -1 );
            lastday = 0;
            // �n��ID�A�s���{��ID
            if ( paramPrefId.equals( "0" ) == false )
            {
                dmp.getData( Integer.parseInt( paramPrefId ) );
                prefName = dmp.getName();
                // ret = hotelPv.getPvPref( Integer.parseInt( paramPrefId ), lastday, DISP_MAX_NUMBER );
                ret = hotelUuPv.getPvPref( Integer.parseInt( paramPrefId ), lastday, DISP_MAX_NUMBER );
            }
            else
            {
                // ret = hotelPv.getPvAllByRank( lastday, DISP_MAX_NUMBER, Integer.parseInt( paramPage ) );
                ret = hotelUuPv.getPvAllByRank( lastday, DISP_MAX_NUMBER, Integer.parseInt( paramPage ) );
            }

            if ( ret != false )
            {
                hotelPv.setHotelPvCount( hotelUuPv.getHotelPvCount() );
                hotelPv.setHotelPv( hotelUuPv.getHotelPv() );

                GenerateXmlContents contents = new GenerateXmlContents();
                contents.setError( "" );
                contents.setErrorCode( 0 );
                contents.setResultCount( hotelPv.getHotelPvCount() );

                // �z�e����PV�f�[�^���Z�b�g
                if ( hotelPv.getHotelPvCount() > 0 )
                {
                    for( int i = 0 ; i < hotelPv.getHotelPvCount() ; i++ )
                    {
                        GenerateXmlContentsHotel hotel = new GenerateXmlContentsHotel();
                        // �z�e��ID���Z�b�g���A�z�e�������擾
                        dhb.getData( hotelPv.getHotelPv()[i].getId() );
                        String name = dhb.getName();
                        hotel.setName( name );
                        hotel.setAddress( dhb.getPrefName() + dhb.getAddress1() );
                        hotel.setHotelId( hotelPv.getHotelPv()[i].getId() );
                        hotel.setRanking( Integer.toString( i + 1 ) );
                        contents.addHotel( hotel );
                    }
                }

                GenerateXmlHeader header = new GenerateXmlHeader();
                header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                header.setMethod( paramMethod );
                header.setName( prefName );
                header.setCount( hotelPv.getHotelPvCount() );
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
        catch ( Exception exception )
        {
            Logging.error( "[ActionApiHotelPvRank.execute ]Exception:" + exception.toString() );

            // �G���[���o��
            GenerateXmlHeader header = new GenerateXmlHeader();
            GenerateXmlContents contents = new GenerateXmlContents();
            contents.setError( Constants.ERROR_MSG_API10 );
            contents.setErrorCode( Constants.ERROR_CODE_API10 );

            // �������ʃw�b�_�쐬
            header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            header.setMethod( paramMethod );
            header.setName( "�����L���O" );
            header.setCount( 0 );
            // �z�e���ڍׂ�ǉ�
            header.setContents( contents );

            String xmlOut = header.createXml();
            ServletOutputStream out = null;

            try
            {
                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionApiHotelPvRank response]Exception:" + e.toString() );
            }
        }
    }
}
