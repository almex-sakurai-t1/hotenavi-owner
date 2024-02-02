/*
 * @(#)FindPrefId.java 1.00 2009/12/08
 * Copyright (C) ALMEX Inc. 2009
 * �s���{��ID�擾�N���X
 */

package jp.happyhotel.common;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import jp.happyhotel.data.DataMapPoint;
import jp.happyhotel.data.DataMasterArea;
import jp.happyhotel.data.DataMasterCity;

;

/**
 * �s���{��ID�擾�N���X
 * �s���{��ID�����낢��ȏ�������擾����
 * 
 * @author N.Ide
 * @version 1.00 2009/12/08
 */
public class FindPrefId implements Serializable
{
    private static final long serialVersionUID = -3685059929468200562L;

    /**
     * �s���{��ID���擾����
     * 
     * @param request ���N�G�X�g
     * @return ��������(0:�Y�����Ȃ��A����ȊO:�Y������s���{��ID)
     * @see "�ȉ��̃p�����[�^������Γs���{��ID���擾�ł���<br>
     *      prefId:�s���{��ID<br>
     *      jisCode:�s�撬��ID<br>
     *      areaId:�z�e���XID<br>
     *      routeId:���[�gID"
     */
    static public int getPrefId(HttpServletRequest request)
    {
        int prefId;
        prefId = 0;

        String paramAreaId = "";
        String paramJisCode = "";
        String paramPrefId = "";
        String paramIcId;

        int nAreaId = 0;
        int nJisCode = 0;
        boolean ret = false;

        DataMasterCity dmc;
        DataMasterArea dma;
        DataMapPoint dmap;

        dmc = new DataMasterCity();

        paramAreaId = request.getParameter( "area_id" );
        if ( paramAreaId == null )
        {
            paramAreaId = "";
        }
        else if ( CheckString.numCheck( paramAreaId ) == false )
        {
            paramAreaId = "";
        }

        paramJisCode = request.getParameter( "jis_code" );
        if ( paramJisCode == null )
        {
            paramJisCode = "";
        }
        else if ( CheckString.numCheck( paramJisCode ) == false )
        {
            paramJisCode = "";
        }

        paramPrefId = request.getParameter( "pref_id" );
        if ( paramPrefId == null )
        {
            paramPrefId = "";
        }
        else if ( CheckString.numCheck( paramPrefId ) == false )
        {
            paramPrefId = "";
        }

        paramIcId = request.getParameter( "route_id" );
        if ( paramIcId == null )
        {
            paramIcId = "";
        }

        // �p�����[�^��ID�̗D�揇�ʂɂ��킹�āA�s���{��ID���擾����
        //
        // �@1.�z�e���O�R�[�h�iareaId�j
        // �@2.�s�撬���R�[�h�iJisCode�j
        // �@3.�s���{��ID �iprefId�j
        // �@4.���[�gID�irouteId�j
        try
        {
            if ( paramAreaId.compareTo( "" ) != 0 )
            {
                dma = new DataMasterArea();

                nAreaId = Integer.parseInt( paramAreaId );
                ret = dma.getData( nAreaId );
                if ( ret != false )
                {
                    prefId = dma.getPrefId();
                }
            }
            else if ( paramJisCode.compareTo( "" ) != 0 )
            {
                nJisCode = Integer.parseInt( paramJisCode );
                ret = dmc.getData( nJisCode );
                if ( ret != false )
                {
                    prefId = dmc.getPrefId();
                }
            }
            else if ( paramPrefId.compareTo( "" ) != 0 )
            {
                prefId = Integer.parseInt( paramPrefId );
            }
            else if ( paramIcId.compareTo( "" ) != 0 )
            {
                dmap = new DataMapPoint();
                dmap.getData( paramIcId );

                // JIS�R�[�h���pref_id���擾����
                if ( dmap.getJisCode() != 0 )
                {
                    ret = dmc.getData( dmap.getJisCode() );
                    if ( ret != false )
                    {
                        prefId = dmc.getPrefId();
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[ FindPrefId.getPrefId() ] Exception:" + e.toString() );
        }
        if ( prefId < 0 )
        {
            prefId = 0;
        }
        return(prefId);
    }
}
