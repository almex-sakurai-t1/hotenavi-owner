/*
 * @(#)HapiTouch.java
 * 1.00 2011/01/12 Copyright (C) ALMEX Inc. 2007
 * �n�s�^�b�`����N���X
 */
package jp.happyhotel.others;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApCiError;

/**
 * �n�s�z�e�^�b�` �`�F�b�N�C���G���[�f�[�^�̔��f
 * 
 * @author T.Sakurai
 * @version 1.00 2019/04/08
 */
public class CiError
{
    private static final String  RESULT_OK    = "OK";
    private static final String  RESULT_NG    = "NG";
    private static final String  CONTENT_TYPE = "text/xml; charset=UTF-8";
    private static final String  ENCODE       = "UTF-8";
    private int                  ciCode       = 0;
    private int                  price        = 0;
    private int                  errorCode    = 0;
    static GenerateXmlGetCiError gxCiError;
    static GenerateXmlReflectCi  gxReflectCi;

    /**
     * �n�s�z�e�`�F�b�N�C���G���[�f�[�^�擾
     * 
     * @param hotelId �n�s�z�eID
     * @param response ���X�|���X
     * 
     */
    public void getCiError(int hotelId, HttpServletResponse response)
    {

        ServletOutputStream stream = null;
        gxCiError = new GenerateXmlGetCiError();

        try
        {

            stream = response.getOutputStream();
            getCiError( hotelId );

            // XML�̏o��
            String xmlOut = gxCiError.createXml();
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            Logging.info( xmlOut );
            out.write( xmlOut.getBytes( ENCODE ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[CiError getCiError]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[CiError getCiError]Exception:" + e.toString() );
                }
            }
        }
    }

    /**
     * �����Ɉ�v����v����ID�̃��X�g���擾����
     * 
     * @param hotelId
     * @return
     * @throws Exception
     */
    public static void getCiError(int hotelId) throws Exception
    {
        GenerateXmlGetCiErrorSub gxCiErrorSub;

        StringBuilder query = new StringBuilder();
        query.append( " SELECT id,seq,front_ip,ci_date,ci_time,room_no,rsv_no,use_point " );
        if ( hotelId == 0 )
        {
            query.append( " FROM ap_ci_error WHERE reflect_flag = ? " );
        }
        else
        {
            query.append( " FROM ap_ci_error WHERE id = ? AND reflect_flag=? " );
        }
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query.toString() );
            int i = 1;
            if ( hotelId != 0 )
            {
                prestate.setInt( i++, hotelId );
            }
            prestate.setInt( i++, 0 );
            result = prestate.executeQuery();
            while( result.next() )
            {
                gxCiErrorSub = new GenerateXmlGetCiErrorSub();
                gxCiErrorSub.setFrontIp( result.getString( "front_ip" ) );
                gxCiErrorSub.setHotelId( result.getInt( "id" ) );
                gxCiErrorSub.setCiCode( result.getInt( "seq" ) );
                gxCiErrorSub.setCiDate( result.getInt( "ci_date" ) );
                gxCiErrorSub.setCiTime( result.getInt( "ci_time" ) );
                gxCiErrorSub.setRoomNo( result.getString( "room_no" ) );
                gxCiErrorSub.setRsvNo( result.getString( "rsv_no" ) );
                gxCiError.addData( gxCiErrorSub );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[CiError.getCiError] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    public void reflectCi(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {

        ServletOutputStream stream = null;
        gxReflectCi = new GenerateXmlReflectCi();

        DataApCiError dace = new DataApCiError();
        boolean ret = true;
        String paramSeq = request.getParameter( "seq" );
        if ( (paramSeq == null) || (paramSeq.compareTo( "" ) == 0) || CheckString.numCheck( paramSeq ) == false )
        {
            paramSeq = "0";
        }
        ciCode = Integer.parseInt( paramSeq );
        String paramPrice = request.getParameter( "price" );
        if ( (paramPrice == null) || (paramPrice.compareTo( "" ) == 0) || CheckString.numCheck( paramPrice ) == false )
        {
            paramPrice = "0";
        }
        price = Integer.parseInt( paramPrice );
        String paramErrorCode = request.getParameter( "ErrorCode" );
        if ( (paramErrorCode == null) || (paramErrorCode.compareTo( "" ) == 0) || CheckString.numCheck( paramErrorCode ) == false )
        {
            paramErrorCode = "0";
        }
        errorCode = Integer.parseInt( paramErrorCode );

        try
        {
             stream = response.getOutputStream();
            if ( dace.getData( hotelId, ciCode ) != false )
            {
                dace.setAmount( price < 0 ? 0 : price );
                if ( price < 0 )
                {
                    dace.setReflectFlag( 2 );
                }
                else if ( price == 0 )
                {
                    dace.setReflectFlag( 1 );
                }
                dace.setErrorCode( errorCode );

                ret = dace.updateData( hotelId, ciCode );
             }
            else
            {
                ret = false;
            }

            // xml�o�̓N���X�Ƀm�[�h���Z�b�g
            if ( ret != false )
            {
                // xml�o�̓N���X�ɒl���Z�b�g
                gxReflectCi.setResult( RESULT_OK );
            }
            else
            {
                // xml�o�̓N���X�ɒl���Z�b�g
                gxReflectCi.setResult( RESULT_NG );
            }

            // XML�̏o��
            String xmlOut = gxReflectCi.createXml();
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            Logging.info( xmlOut );
            out.write( xmlOut.getBytes( ENCODE ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[CiError reflectCi]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[CiError reflectCi]Exception:" + e.toString() );
                }
            }
        }
    }
}
