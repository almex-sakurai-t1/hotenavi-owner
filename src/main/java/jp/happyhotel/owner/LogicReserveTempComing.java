package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import jp.happyhotel.common.*;
import jp.happyhotel.reserve.FormReserveSheetPC;

public class LogicReserveTempComing implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = -6961606827031522404L;
    
    private FormReserveSheetPC frm;
    private LogicOwnerRsvCheckIn lgRsv;
    
    public boolean execute(Connection connection, int elapseDays) 
    {
        boolean returnSts = true;
        String query = "";
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            frm = new FormReserveSheetPC();
            lgRsv = new LogicOwnerRsvCheckIn();
            
            //�����\�莞�� hhmmss �́A24�����z���邱�Ƃ�����̂ŁADATE_ADD�œ��t���ɉ��Z���Ă���
            //�������́A�ʏ��hhmmss�̒l
            
            query = "SELECT r.id, r.reserve_no, r.reserve_sub_no, r.plan_id, r.seq, r.reserve_date, " +
                    "r.accept_date, r.accept_time " +
                    "FROM hh_rsv_reserve r, hh_rsv_reserve_basic b " +
                    "WHERE r.id = b.id and r.status = 1 AND r.temp_coming_flag = 0 " +
                    "AND (DATE_ADD(r.reserve_date, INTERVAL floor(r.est_time_arrival / 10000) HOUR) + (r.est_time_arrival % 10000))" +
                    " < (DATE_ADD(curdate(), INTERVAL floor(b.deadline_time / 10000) HOUR) + (b.deadline_time % 10000)) " +
                    "AND (DATE_ADD(r.reserve_date, INTERVAL 24 + floor(b.deadline_time / 10000) HOUR) + (b.deadline_time % 10000))" +
                    " < (curdate() * 1000000 + curtime())";

            //System.out.println( query );

            prestate = connection.prepareStatement( query );

            result = prestate.executeQuery();

            if ( result != null )
            {
                //�\��f�[�^���X�V���Ă���
                while( result.next() )
                {
                    frm.setSelHotelId( result.getInt( "id" ) );
                    frm.setRsvNo( result.getString( "reserve_no" ) );
                    frm.setRsvSubNo( result.getInt( "reserve_sub_no" ) );
                    frm.setSeq( result.getInt( "seq" ) );
                    frm.setRsvDate( result.getInt( "reserve_date" ) );
                    frm.setOrgRsvDate( result.getInt( "reserve_date" ) );
                    frm.setSelPlanId( result.getInt( "plan_id" ) );
                    frm.setTermKind( 1 );   //2011.05.16 �P�̃e�X�gP242
                    lgRsv.setFrm(this.frm);
                    
                    System.out.println( "[LogicReserveTempComing.excute] target id = " + frm.getSelHotelId() +
                            ", reserve_no = " + frm.getRsvNo() + ", reserve_sub_no = " + frm.getRsvSubNo() );
                    
                    boolean sts = execKariRaiten(connection);
                    if ( sts == false)
                    {
                        System.out.println( "[LogicReserveTempComing.excute] Update Error id = " + frm.getSelHotelId() +
                                ", reserve_no = " + frm.getRsvNo() );
                    }
                    
                }
            }
            
            
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);

            //���̏���
            //�E�����X����A���߂�ꂽ���Ԃ��߂����ꍇ�́A���X�Ɠ������������s����B
            //�E�����������X�t���O�̓N���A���Ȃ��B
            
            query = "SELECT r.id, r.reserve_no, r.reserve_sub_no, r.plan_id, r.seq, r.reserve_date, " +
                    "r.user_id, r.reminder_flag, r.add_point, r.est_time_arrival, r.accept_date, r.accept_time " +
                    "FROM hh_rsv_reserve r, hh_rsv_reserve_basic b " +
                    "WHERE r.id = b.id AND r.status = 1 AND r.temp_coming_flag = 1 " +
                    "AND (DATE_ADD(r.reserve_date, INTERVAL (" + elapseDays + " * 24) + floor(b.deadline_time / 10000) HOUR) + (b.deadline_time % 10000))" +
                    " < (curdate() * 1000000 + curtime())";

            //System.out.println( query );
                   
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            if ( result != null )
            {
                //�\��f�[�^���X�V���Ă���
                while( result.next() )
                {
                    frm.setSelHotelId( result.getInt( "id" ) );
                    frm.setRsvNo( result.getString( "reserve_no" ) );
                    frm.setRsvSubNo( result.getInt( "reserve_sub_no" ) );
                    frm.setSeq( result.getInt( "seq" ) );
                    frm.setRsvDate( result.getInt( "reserve_date" ) );
                    frm.setOrgRsvDate( result.getInt( "reserve_date" ) );
                    frm.setSelPlanId( result.getInt( "plan_id" ) );
                    frm.setUserId( result.getString( "user_id" ) );
                    frm.setReminder( result.getInt( "reminder_flag" ) );
                    frm.setAddPoint( result.getInt( "add_point" ) );
                    frm.setTermKind( 1 );   //2011.05.16 �P�̃e�X�gP242
                    
                    frm.setLoginUserId(result.getString( "user_id" ));
                    
                    //frm.setReflectDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    //���X�����Ɠ��l�ɂ��ċ��߂�
                    frm.setReflectDate( getReflectDate(connection, frm.getRsvDate(), result.getInt( "est_time_arrival" )) );
                    System.out.println( "ReflectDate = " + frm.getReflectDate() );
                    
                    lgRsv.setFrm(this.frm);
                    
                    System.out.println( "[LogicReserveTempComing.excute call execRaiten] target id = " + frm.getSelHotelId() +
                            ", reserve_no = " + frm.getRsvNo() + ", reserve_sub_no = " + frm.getRsvSubNo() +
                            ", reserve_date = " + frm.getRsvDate() );
                    
                    boolean sts = execRaiten(connection);
                    
                    if ( sts == false)
                    {
                        System.out.println( "[LogicReserveTempComing.excute] Update Error id = " + frm.getSelHotelId() +
                                ", reserve_no = " + frm.getRsvNo() );
                    }
                }
            }
        }
        catch ( Exception e )
        {
            System.out.println( e.toString() );
            returnSts = false;
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }

        return returnSts;
    }
    
    
    /**
    *
    * �\��҉����X����
    *
    * @return true:����Afalse:�ُ�
    */
   private boolean execKariRaiten(Connection connection)
   {
       boolean isResult = false;
       boolean blnRet = false;
       int subNo = 0;
       String query = "";

       ResultSet result = null;
       PreparedStatement prestate = null;

       try
       {

           // �擾�����\��ԍ��}�Ԃ�1�����Z����
           subNo = frm.getRsvSubNo() + 1;
           frm.setRsvSubNo( subNo );

           // �ύX�\��̓��e�Ń��b�N
           if ( LockReserve.Lock( connection, frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() ) == false )
           {
               // ���̐l�����b�N���Ă����ꍇ
               System.out.println("[LogicReserveTempComing] ���b�N�ł��܂���ł����B" +
                       String.format( "ID=%d, DATE=%s, SEQ=%d", frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() ));
               return(blnRet);
           }

           try
           {
               query = "START TRANSACTION ";
               prestate = connection.prepareStatement( query );
               result = prestate.executeQuery();

               // �\��f�[�^�̉����X�t���O��ON�ɂ���
               blnRet = updRaitenRsvData( connection, prestate, frm, 0 );
               System.out.println("updRaitenRsvData sts = " + blnRet);

               // �\�񗚗��̍쐬
               if ( blnRet )
               {
                   blnRet = lgRsv.createRsvHistory( connection, ReserveCommon.UPDKBN_UPDATE, frm.getRsvNo() );
                   System.out.println("lgRsv.createRsvHistory sts = " + blnRet);
               }
               if ( blnRet )
               {
                   // �\��E�I�v�V���������f�[�^�쐬
                   blnRet = lgRsv.createRsvOptionHistory( connection, prestate, frm.getRsvNo() );
                   System.out.println("lgRsv.createRsvOptionHistory sts = " + blnRet);
              }

               if ( blnRet )
               {
                   // ���[���𑗐M
                   lgRsv.sendMail( connection, frm.getRsvNo(), 0, ReserveCommon.TERM_KIND_PC, 7);
                   System.out.println("lgRsv.sendMail sts = " + blnRet);
               }

               if ( blnRet )
               {
                   query = "COMMIT ";
                   prestate = connection.prepareStatement( query );
                   result = prestate.executeQuery();
               }
               else
               {
                   query = "ROLLBACK";
                   prestate = connection.prepareStatement( query );
                   result = prestate.executeQuery();
               }

               isResult = true;
           }
           catch ( Exception e )
           {
               System.out.println( "[LogicReserveTempComing.execRaiten] Exception=" + e.toString() );
               
               query = "ROLLBACK";
               prestate = connection.prepareStatement( query );
               result = prestate.executeQuery();
           }
           finally
           {
               // ���b�N�̉���
               LockReserve.UnLock( connection, frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() );
           }
       }
       catch ( Exception e )
       {
           System.out.println( "[LogicReserveTempComing.execRaiten] Exception=" + e.toString() );
       }
       finally
       {
          DBConnection.releaseResources(result);
          DBConnection.releaseResources(prestate);
       }
       return isResult;
   }
   
   
   /**
    * �\��f�[�^�X�V
    *
    * @param conn Connection�I�u�W�F�N�g
    * @param prestate PreparedStatement�I�u�W�F�N�g
    * @param frm FormReserveSheetPC�I�u�W�F�N�g
    * @param kbn �敪 0:�����X�A1:���X
    * @return true:����Afalse:�ُ�
    */
   private boolean updRaitenRsvData(Connection conn, PreparedStatement prestate, FormReserveSheetPC frm, int kbn) throws Exception
   {
       String query = "";
       int retCnt = 0;
       boolean ret = false;

       if ( kbn == 0)
       {
           query = query + "UPDATE hh_rsv_reserve SET";
           query = query + "  reserve_sub_no = ? ";
           query = query + " ,temp_coming_flag = 1";
           query = query + " ,accept_date = ? ";
           query = query + " ,accept_time = ? ";
           query = query + " WHERE status = 1 and temp_coming_flag = 0";
           query = query + " AND id = ? AND reserve_no = ? AND reserve_sub_no = ?";
       }
       else
       {
           query = query + "UPDATE hh_rsv_reserve SET ";
           query = query + "  reserve_sub_no = ? ";
           query = query + " ,status = 2 ";
           query = query + " ,coming_flag = 1 ";
           query = query + " ,accept_date = ? ";
           query = query + " ,accept_time = ? ";
           query = query + " WHERE status = 1 and temp_coming_flag = 1";
           query = query + " AND id = ? AND reserve_no = ? AND reserve_sub_no = ?";
       }

       try
       {
           prestate = conn.prepareStatement( query );
           prestate.setInt( 1, frm.getRsvSubNo() );
           prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
           prestate.setInt( 3, Integer.parseInt( DateEdit.getTime( 1 ) ) );
           prestate.setInt( 4, frm.getSelHotelId() );
           prestate.setString( 5, frm.getRsvNo() );
           prestate.setInt( 6, frm.getRsvSubNo() - 1 ); //reserve_sub_no �������ɓ���Ă���

           retCnt = prestate.executeUpdate();

           if ( retCnt > 0 )
           {
               ret = true;
           }
       }
       catch ( Exception e )
       {
           System.out.println( "[LogicReserveTempComing.updRaitenRsvData] Exception=" + e.toString() );
           throw e;
       }
       return(ret);
   }


   /**
    * �����؂ꗈ�X����
    * 
    * @param connection
    * @return true:����Afalse:�ُ�
    * @throws Exception
    */
   private boolean execRaiten(Connection connection) throws Exception
   {
      boolean isResult = false;
      boolean blnRet = false;
      int subNo = 0;
      String query = "";

      ResultSet result = null;
      PreparedStatement prestate = null;

      try
      {
          // �擾�����\��ԍ��}�Ԃ�1�����Z����
          subNo = frm.getRsvSubNo() + 1;
          frm.setRsvSubNo( subNo );

          try
          {
              // �ύX�\��̓��e�Ń��b�N
              if ( LockReserve.Lock( connection, frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() ) == false )
              {
                  // ���̐l�����b�N���Ă����ꍇ
                  System.out.println("[LogicReserveTempComing] ���b�N�ł��܂���ł����B" +
                          String.format( "ID=%d, DATE=%s, SEQ=%d", frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() ));
                  return(blnRet);
              }

              query = "START TRANSACTION ";
              prestate = connection.prepareStatement( query );
              result = prestate.executeQuery();

              // �\��f�[�^�𗘗p�ς݁A���X�t���O��ON�ɂ���
              blnRet = updRaitenRsvData( connection, prestate, frm, 1 );

              // ���уf�[�^�̓o�^
              if ( blnRet )
              {
                  blnRet = lgRsv.registRsvResult( connection, ReserveCommon.RESULT_KIND_LIMIT, frm, frm.getSeq() );
              }

              // �����c���f�[�^�̍X�V
              if ( blnRet )
              {
                  // ��ʂőI�����ꂽ�����ԍ��Ń`�F�b�N�C��
                  blnRet = lgRsv.createRoomRemaindarRaiten( connection, frm.getRsvNo(), frm.getRsvDate(), frm.getSeq() );
              }

              // �\�񗚗��̍쐬
              if ( blnRet )
              {
                  blnRet = lgRsv.createRsvHistory( connection, ReserveCommon.UPDKBN_UPDATE, frm.getRsvNo() );
              }

              if ( blnRet )
              {
                  System.out.println("call lgRsv.createRsvOptionHistory()");
                  // �\��E�I�v�V���������f�[�^�쐬
                  blnRet = lgRsv.createRsvOptionHistory( connection, prestate, frm.getRsvNo() );
              }

              if ( blnRet )
              {
                  if ( frm.getReminder() == 1 )
                  {
                      System.out.println("call lgRsv.sendMail()");
                      // ���}�C���_�[��ON�̏ꍇ�A�T���L���[���[���𑗐M
                      lgRsv.sendMail( connection, frm.getRsvNo(), 0, ReserveCommon.TERM_KIND_PC, ReserveCommon.MAIL_RAITEN );
                  }
              }

              // �L�����[�U�|�C���g�ꎞ�f�[�^�쐬
              if ( frm.getAddPoint() != 0 )
              {
                  if ( blnRet )
                  {
                      System.out.println("call lgRsv.createUserPointPayTemp()");
                      blnRet = lgRsv.createUserPointPayTemp( connection, prestate );
                  }
              }

              if ( blnRet )
              {
                  System.out.println("call lgRsv.updateRsvUserBasic()");
                  // �\�񃆁[�U�[��{�f�[�^�X�V
                  blnRet = lgRsv.updateRsvUserBasic( connection, prestate, 2, frm.getUserId() );
              }

              if ( blnRet )
              {
                  System.out.println("COMMIT ");
                  query = "COMMIT ";
                  prestate = connection.prepareStatement( query );
                  result = prestate.executeQuery();
              }
              else
              {
                  System.out.println("ROLLBACK ");
                  query = "ROLLBACK";
                  prestate = connection.prepareStatement( query );
                  result = prestate.executeQuery();
              }
              isResult = true;
          }
          catch ( Exception e )
          {
              System.out.println("ROLLBACK by Exception");
              query = "ROLLBACK";
              prestate = connection.prepareStatement( query );
              result = prestate.executeQuery();
              throw e;
          }
          finally
          {
              // ���b�N�̉���
              LockReserve.UnLock( connection, frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() );
          }
      }
      catch ( Exception e )
      {
          System.out.println( "[LogicReserveTempComing.execRaiten] Exception=" + e.toString() );
          throw e;
      }
      finally
      {
          DBConnection.releaseResources(result);
          DBConnection.releaseResources(prestate);
      }
      return isResult;
  }

   
   /***** ActionOwnerRsvCheckIn ���R�s�[���Ă��� **********/
   /**
    * �L�����[�U�|�C���g�ꎞ�f�[�^�̔��f���擾
    *
    * @param int rsvDate �\���
    * @param int arrivalTime �����\�莞��
    * @return int ���f��
    * @throws Exception
    */
   private int getReflectDate(Connection connection, int rsvDate, int arrivalTime) throws Exception
   {
       int retDate = 0;
       int limitFlg = 0;
       int range = 0;
       String year = "";
       String month = "";
       String day = "";
       String rsvYear = "";
       String rsvMonth = "";
       String rsvDay = "";
       String rsvHour = "";
       String rsvMinutes = "";
       String rsvSecond = "";
       String arrivalTimeStr = "";
       Calendar calendar = Calendar.getInstance();

       //�|�C���g�Ǘ��}�X�^����f�[�^�擾
       limitFlg = OwnerRsvCommon.getInitHapyPoint( connection, 3 );
       range = OwnerRsvCommon.getInitHapyPoint( connection, 4 );

       //���t�ݒ�
       rsvYear = Integer.toString( rsvDate ).substring( 0, 4 );
       rsvMonth = Integer.toString( rsvDate ).substring( 4, 6 );
       rsvDay = Integer.toString( rsvDate ).substring( 6, 8 );

       arrivalTimeStr = ConvertTime.convTimeStr( arrivalTime, 0);
       rsvHour = arrivalTimeStr.substring( 0, 2 );
       rsvMinutes = arrivalTimeStr.substring( 2, 4 );
       rsvSecond = arrivalTimeStr.substring( 4 );
       calendar.set( Integer.parseInt( rsvYear ), Integer.parseInt( rsvMonth ) - 1, Integer.parseInt( rsvDay ),
                       Integer.parseInt( rsvHour ), Integer.parseInt( rsvMinutes ), Integer.parseInt( rsvSecond ) );

       switch( limitFlg ) {
           case OwnerRsvCommon.LIMIT_FLG_TIME:
               //���ԉ��Z
               calendar.add( Calendar.HOUR, range );
               break;

           case OwnerRsvCommon.LIMIT_FLG_DAY:
               //���t���Z
               calendar.add( Calendar.DATE, range );
               break;

           case OwnerRsvCommon.LIMIT_FLG_MONTH:
               //�����Z
               calendar.add( Calendar.MONTH, range );
               break;
       }

       year = Integer.toString( calendar.get( Calendar.YEAR ) );
       month = String.format( "%1$02d", calendar.get( Calendar.MONTH ) + 1 );
       day = String.format( "%1$02d", calendar.get( Calendar.DATE ));

       retDate  = Integer.parseInt( year + month + day );

       return (retDate);
   }
}
