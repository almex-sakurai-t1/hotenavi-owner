package jp.happyhotel.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.data.DataRsvMonthlyConfirm;
import jp.happyhotel.data.DataRsvPlan;
import jp.happyhotel.owner.FormOwnerRsvManageCalendar;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.FormOwnerRsvPlanCharge;
import jp.happyhotel.owner.FormOwnerRsvPlanChargeCalendar;
import jp.happyhotel.owner.FormOwnerRsvPlanChargeCalendarSub;
import jp.happyhotel.owner.LogicOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvPlanCharge;
import jp.happyhotel.owner.LogicOwnerRsvPlanChargeCalendar;

public class ActionOwnerRsvPlanChargeCalendar extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerRsvPlanChargeCalendar frmCalendar;
        FormOwnerBkoMenu frmMenu;
        LogicOwnerRsvPlanChargeCalendar logicCalendar;
        int paramHotelID = 0;
        int paramUserId = 0;
        int paramPlanId = -1;
        String paraOwnerHotelID = "";
        String paramTargetYM = "";
        int paramYYYY = 0;
        int paramMM = 0;
        String targetYYYYMM = "";
        Calendar orgCal;
        String planNm = "";
        String errMsg = "";
        ArrayList<Integer> chargeModeIdList = new ArrayList<Integer>();
        int disptype = 0;
        int imediaflag = 0;
        int adminflag = 0;
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();
        boolean ret = false;

        try
        {
            frmCalendar = new FormOwnerRsvPlanChargeCalendar();
            frmMenu = new FormOwnerBkoMenu();
            orgCal = Calendar.getInstance();

            // ��ʂ̒l���擾
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            paraOwnerHotelID = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
            paramTargetYM = request.getParameter( "currentYM" );

            logicMenu.setFrm( new FormOwnerBkoMenu() );
            disptype = logicMenu.getUserAuth( paraOwnerHotelID, paramUserId );
            adminflag = logicMenu.getAdminFlg( paraOwnerHotelID, paramUserId );
            imediaflag = OwnerRsvCommon.getImediaFlag( paraOwnerHotelID, paramUserId );

            if ( (imediaflag == 1 && adminflag == 1 && paraOwnerHotelID.equals( "happyhotel" )) == false && disptype != OwnerRsvCommon.USER_AUTH_OWNER && disptype != OwnerRsvCommon.USER_AUTH_DEMO && disptype != OwnerRsvCommon.USER_AUTH_CALLCENTER )
            {
                // �����̂Ȃ����[�U�̓G���[�y�[�W��\������
                errMsg = Message.getMessage( "erro.30001", "�y�[�W���{�����錠��" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            if ( request.getParameter( "updCalendar" ) != null && disptype == OwnerRsvCommon.USER_AUTH_CALLCENTER && (imediaflag == 1 && adminflag == 1 && paraOwnerHotelID.equals( "happyhotel" )) == false )
            {
                // �����̂Ȃ����[�U�̓G���[�y�[�W��\������
                errMsg = Message.getMessage( "erro.30001", "�X�e�[�^�X���X�V���錠��" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            if ( request.getParameter( "selPlanId" ) != null )
            {
                paramPlanId = Integer.parseInt( request.getParameter( "selPlanId" ) );
            }

            if ( request.getParameter( "preMonth" ) != null )
            {
                // ���O���{�^��
                paramYYYY = Integer.parseInt( paramTargetYM.substring( 0, 4 ) );
                paramMM = Integer.parseInt( paramTargetYM.substring( 4 ) );
                orgCal.set( paramYYYY, paramMM - 1, 1 );
                // 1�����O�擾
                orgCal.add( Calendar.MONTH, -1 );
            }
            else if ( request.getParameter( "nextMonth" ) != null )
            {
                // �������{�^��
                paramYYYY = Integer.parseInt( paramTargetYM.substring( 0, 4 ) );
                paramMM = Integer.parseInt( paramTargetYM.substring( 4 ) );
                orgCal.set( paramYYYY, paramMM - 1, 1 );

                // 1������擾
                orgCal.add( Calendar.MONTH, 1 );
            }
            else if ( request.getParameter( "updCalendar" ) != null )
            {
                // ���J�����_�[�ݒ�X�V�{�^��
                String[] ids = request.getParameterValues( "selChargeMode" );
                for( int i = 0 ; i < ids.length ; i++ )
                {
                    chargeModeIdList.add( Integer.parseInt( ids[i] ) );
                }

                // // ���I���̃J�����_�[�����݂�����G���[
                // for( int i = 0 ; i < chargeModeIdList.size() ; i++ )
                // {
                // if ( chargeModeIdList.get( i ) == 0 )
                // {
                // frmCalendar.setInptErrMsg( Message.getMessage( "warn.00002", "�������[�h" ) );
                // // �v�������擾
                // logicCalendar = new LogicOwnerRsvPlanChargeCalendar();
                // if ( paramPlanId == 0 )
                // {
                // // �z�e���S��
                // planNm = OwnerRsvCommon.PLAN_CAL_HOTELNM;
                // }
                // else
                // {
                // planNm = logicCalendar.getPlanNm( paramHotelID, paramPlanId );
                // }
                // frmCalendar.setSelPlanNm( planNm );
                //
                // // �J�����_�[���擾
                // targetYYYYMM = new SimpleDateFormat( "yyyyMM" ).format( orgCal.getTime() );
                // getMonth( frmCalendar, paramHotelID, targetYYYYMM, paramPlanId );
                // frmCalendar.setSelPlanId( paramPlanId );
                //
                // // ���j���[�A�{�ݏ��̐ݒ�
                // frmMenu.setUserId( paramUserId );
                // OwnerRsvCommon.setMenu( frmMenu, paramHotelID, 10, request.getCookies() );
                //
                // // ��ʑJ��
                // request.setAttribute( "FORM_Menu", frmMenu );
                // request.setAttribute( "FORM_Calendar", frmCalendar );
                // requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
                // requestDispatcher.forward( request, response );
                // return;
                // }
                // }

                // �f�[�^�X�V
                frmCalendar.setSelHotelId( paramHotelID );
                frmCalendar.setSelPlanId( paramPlanId );
                frmCalendar.setOwnerHotelID( paraOwnerHotelID );
                frmCalendar.setUserId( paramUserId );
                logicCalendar = new LogicOwnerRsvPlanChargeCalendar();
                logicCalendar.setFrm( frmCalendar );
                if ( paramPlanId == 0 )
                {
                    // �z�e���S�̂̃J�����_�[�X�V
                    logicCalendar.registHotelCalendar( chargeModeIdList, Integer.parseInt( paramTargetYM ) );

                    // �z�e���C������
                    String[] idOld = request.getParameterValues( "selChargeModeOld" );
                    for( int i = 0 ; i < ids.length ; i++ )
                    { // �ύX�����������𒲂ׂ�(edit_sub�́Acal_date)
                        if ( Integer.parseInt( ids[i] ) != Integer.parseInt( idOld[i] ) )
                        {
                            OwnerRsvCommon.addAdjustmentHistory( paramHotelID, OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                                    paramUserId, OwnerRsvCommon.ADJUST_EDIT_ID_CALENDAR,
                                    Integer.parseInt( paramTargetYM ) * 100 + i + 1, OwnerRsvCommon.ADJUST_MEMO_CALENDAR );
                        }
                    }
                }
                else
                {
                    // �v�����̓��ʗ������[�h�X�V
                    logicCalendar.registPlanCalendar( chargeModeIdList, Integer.parseInt( paramTargetYM ) );
                }

                paramYYYY = Integer.parseInt( paramTargetYM.substring( 0, 4 ) );
                paramMM = Integer.parseInt( paramTargetYM.substring( 4 ) );
                orgCal.set( paramYYYY, paramMM - 1, 1 );

            }
            else if ( request.getParameter( "addCalendar" ) != null )
            {
                LogicOwnerRsvPlanCharge logic;
                DataRsvPlan data = new DataRsvPlan();
                FormOwnerRsvPlanCharge frm;

                data.getData( paramHotelID, paramPlanId );

                frm = new FormOwnerRsvPlanCharge();
                logic = new LogicOwnerRsvPlanCharge();
                frm.setSelHotelId( paramHotelID );
                frm.setSelPlanId( paramPlanId );
                logic.setFrm( frm );
                // �f�[�^�o�^���Ƀv�����ʃJ�����_�[�̍쐬���s��
                logic.addPlanCalendar( data.getSalesStopWeekStatus() );

                paramYYYY = Integer.parseInt( paramTargetYM.substring( 0, 4 ) );
                paramMM = Integer.parseInt( paramTargetYM.substring( 4 ) );
                orgCal.set( paramYYYY, paramMM - 1, 1 );
            }
            else if ( request.getParameter( "calConfirm" ) != null )
            {
                DataRsvMonthlyConfirm drmc = new DataRsvMonthlyConfirm();
                ret = drmc.getData( paramHotelID, paramPlanId, Integer.parseInt( paramTargetYM ), paraOwnerHotelID, paramUserId );
                drmc.setConfirmDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                drmc.setConfirmTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );

                if ( ret == false )
                {
                    drmc.setId( paramHotelID );
                    drmc.setPlanId( paramPlanId );
                    drmc.setCalDate( Integer.parseInt( paramTargetYM ) );
                    drmc.setHotelId( paraOwnerHotelID );
                    drmc.setUserId( paramUserId );
                    drmc.setDispDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    drmc.setDispTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    drmc.insertData();
                }
                else
                {
                    drmc.updateData( paramHotelID, paramPlanId, Integer.parseInt( paramTargetYM ), paraOwnerHotelID, paramUserId );
                }

                paramYYYY = Integer.parseInt( paramTargetYM.substring( 0, 4 ) );
                paramMM = Integer.parseInt( paramTargetYM.substring( 4 ) );
                orgCal.set( paramYYYY, paramMM - 1, 1 );
            }

            // �v�������擾
            logicCalendar = new LogicOwnerRsvPlanChargeCalendar();
            if ( paramPlanId == 0 )
            {
                // �z�e���S��
                planNm = OwnerRsvCommon.PLAN_CAL_HOTELNM;
            }
            else
            {
                planNm = logicCalendar.getPlanNm( paramHotelID, paramPlanId );
            }
            frmCalendar.setSelPlanNm( planNm );

            // �J�����_�[���擾
            targetYYYYMM = new SimpleDateFormat( "yyyyMM" ).format( orgCal.getTime() );
            getMonth( frmCalendar, paramHotelID, targetYYYYMM, paramPlanId, imediaflag );
            frmCalendar.setSelPlanId( paramPlanId );

            // ���j���[�A�{�ݏ��̐ݒ�
            frmMenu.setUserId( paramUserId );
            OwnerRsvCommon.setMenu( frmMenu, paramHotelID, 10, request.getCookies() );

            // �\�񂪓����Ă��邩�`�F�b�N
            if ( OwnerRsvCommon.isExistsRsvPlan( paramHotelID, paramPlanId ) != false )
            {
                frmCalendar.setWarnMsg( Message.getMessage( "warn.30041", Integer.toString( paramHotelID ) ) );
            }

            // ��ʑJ��
            request.setAttribute( "FORM_Menu", frmMenu );
            request.setAttribute( "FORM_Calendar", frmCalendar );
            requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvPlanChargeCalendar.execute() ][hotelId = " + paramHotelID + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvPlanChargeCalendar.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            // logic = null;
        }
    }

    /**
     * �����A�����̃J�����_�[���擾
     * 
     * @param logic FormOwnerRsvPlanChargeCalendar�I�u�W�F�N�g
     * @para, hotelId �z�e��ID
     * @param planId �v����ID
     * @param salesFlg �̔��t���O
     * @param planId �v����ID(���w���-1)
     * @param imediaFlag �A�C���f�B�A�t���O
     * @return �Ȃ�
     */
    private void getMonth(FormOwnerRsvPlanChargeCalendar frm, int hotelId, String targetYM, int paramPlanId, int imediaFlag) throws Exception
    {
        int planId = 0;
        LogicOwnerRsvPlanChargeCalendar logic = new LogicOwnerRsvPlanChargeCalendar();
        ArrayList<ArrayList<FormOwnerRsvPlanChargeCalendarSub>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvPlanChargeCalendarSub>>();

        try
        {
            frm.setSelHotelId( hotelId );

            // �v�������擾
            logic.setFrm( frm );
            logic.getPlanList();
            frm = logic.getFrm();

            if ( paramPlanId == -1 )
            {
                // �擪�s�̃J�����_�[����\��
                planId = frm.getPlanIdList().get( 0 );
            }
            else
            {
                planId = paramPlanId;
            }

            // �J�����_�[���擾
            if ( paramPlanId == 0 )
            {
                // �z�e���S�̂̏ꍇ
                logic.getHotelCalendar( frm.getSelHotelId(), Integer.parseInt( targetYM ), imediaFlag );
            }
            else
            {
                logic.getCalendar( frm.getSelHotelId(), planId, Integer.parseInt( targetYM ), imediaFlag );
            }

            monthlyList = logic.getFrm().getMonthlyList();
            frm.setMonthlyList( monthlyList );

            // �t�H�[���ɃZ�b�g
            frm.setSelPlanId( planId );
            frm.setMonthlyList( logic.getFrm().getMonthlyList() );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvPlanChargeCalendar.getMonth() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvPlanChargeCalendar.getMonth] " + exception );
        }
    }

}
