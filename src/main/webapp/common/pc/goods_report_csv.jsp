<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.ReplaceString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%

    NumberFormat nf2      = new DecimalFormat("00");
    NumberFormat nf6      = new DecimalFormat("000000");
    StringFormat sf;
    sf = new StringFormat();

    String loginHotelId =  (String)session.getAttribute("LoginHotelId");
    String hotelid = (String)session.getAttribute("SelectHotel");


    DateEdit dateedit = new DateEdit();

    int nowdate   =  Integer.parseInt(dateedit.getDate(2));
    int nowtime   =  Integer.parseInt(dateedit.getTime(1));
    String nowdate_s = nf2.format(nowdate / 10000 ) + "/" + nf2.format(nowdate / 100 % 100 ) + "/" + nf2.format(nowdate % 100 );
    String nowtime_s = nf2.format(nowtime / 10000 ) + "/" + nf2.format(nowtime / 100 % 100 ) + "/" + nf2.format(nowtime % 100 );
    int    count        = 0;
    int    total_count  = 0;
    int    inquiry_no   = 0;
    String            query = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    Connection        connection_sub  = null;
    PreparedStatement prestate_sub    = null;
    ResultSet         result_sub      = null;
    connection     = DBConnection.getConnection();
    connection_sub = DBConnection.getConnection();
    try
    {
        query = "SELECT goods_entry.inquiry_no FROM goods_entry WHERE goods_entry.hotelid=?";
        query = query + " ORDER BY goods_entry.inquiry_no DESC";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery(query);
        if( result != null )
        {
            if(result.next())
            {
                inquiry_no = result.getInt("inquiry_no");
            }
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    try
    {
        query = "SELECT count(*) FROM goods_entry,hh_user_basic WHERE goods_entryhotelid=?";
        query = query + " AND goods_entry.user_id=hh_user_basic.user_id";
        query = query + " AND goods_entry.count!=0";
        query = query + " AND goods_entry.status=1";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery(query);
        if( result != null )
        {
            if(result.next())
            {
                total_count = result.getInt(1);
            }
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

//�P�D�_�E�����[�h���鎞�̌��܂育�ƂƂ��āA�ȉ��̃\�[�X�������B
    response.setContentType("application/octetstream");

//�Q�D�_�E�����[�h����t�@�C�������w��(�g���q��csv�ɂ���)
    String filename = "order_100_" + nowdate + "000001";
    filename = filename + ".csv";
    response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
//�R�D�_�E�����[�h����t�@�C�����X�g���[��������
    PrintWriter outstream = response.getWriter();

    try
    {
        query = "SELECT * FROM goods_entry";
        query = query + " INNER JOIN hh_user_basic ON goods_entry.user_id=hh_user_basic.user_id";
        query = query + " INNER JOIN goods ON goods_entry.hotelid=goods.hotelid";
        query = query + " AND goods_entry.category_id=goods.category_id";
        query = query + " AND goods_entry.seq=goods.seq";
        query = query + " AND goods_entry.input_date  >= goods.disp_from";
        query = query + " AND goods_entry.input_date  <= goods.disp_to";
        query = query + " WHERE goods_entry.hotelid=?";
        query = query + " AND goods_entry.count!=0";
        query = query + " AND goods_entry.status=1";
        query = query + " ORDER BY entry_id , entry_branch";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery(query);
        int  input_date = 0;
        int  input_time = 0;
        if( result != null )
        {
            while( result.next() != false )
            {
                count++;
                
                input_date = result.getInt("input_date");
                input_time = result.getInt("input_time");
                outstream.print(inquiry_no + count);        //�⍇�ԍ�
                outstream.print(",");
                outstream.print(input_date);             //������
                outstream.print(",,,,");
                outstream.print(result.getString("hh_user_basic.name_last")+"�@"+ result.getString("hh_user_basic.name_first"));//�\���Ҏ���
                outstream.print(",");
                outstream.print(result.getString("hh_user_basic.name_last_kana")+"�@"+ result.getString("hh_user_basic.name_first_kana"));//�\���҃J�i
                outstream.print(",");
                outstream.print(",");//�\���ҔN��
                outstream.print(result.getString("goods_entry.zip_code"));//�\���җX�֔ԍ�
                outstream.print(",");//
                outstream.print(result.getString("goods_entry.pref_name"));//�s���{��
                outstream.print(",");//
                outstream.print(result.getString("goods_entry.address1"));//�Z��2
                outstream.print(",");
                outstream.print(ReplaceString.replaceNumAlphaFull(result.getString("goods_entry.address2")));//�Z��3
                outstream.print(",");
//                outstream.print(result.getString("hh_user_basic.zip_code"));//�\���җX�֔ԍ�
//                outstream.print(",");//
//                query = "SELECT * FROM hh_master_pref WHERE pref_id = " + result.getInt("hh_user_basic.pref_code");
//                prestate_sub    = connection_sub.prepareStatement(query);
//                result_sub      = prestate_sub.executeQuery(query);
//                String pref_name = "";
//                if(result_sub.next()) pref_name = result_sub.getString("name");
//                DBConnection.releaseResources(result_sub);
//                DBConnection.releaseResources(prestate_sub);
//                outstream.print(pref_name);//�s���{��
//                outstream.print(",");//
//                outstream.print(result.getString("hh_user_basic.address1"));//�Z��2
//                outstream.print(",");
//                outstream.print(result.getString("hh_user_basic.address2"));//�Z��3
                outstream.print(",");//�Z��4
                outstream.print(result.getString("hh_user_basic.tel1"));//�\���ғd�b�ԍ�
                outstream.print(",");
                outstream.print(",,,,");
                outstream.print(result.getString("goods_entry.send_name"));//�͂��掁��
                outstream.print(",");
                outstream.print(result.getString("hh_user_basic.name_last_kana")+"�@"+ result.getString("hh_user_basic.name_first_kana"));//�\���҃J�i
                outstream.print(",");//�͂���N��
                outstream.print(result.getString("goods_entry.zip_code"));//�\���җX�֔ԍ�
                outstream.print(",");//
                outstream.print(result.getString("goods_entry.pref_name"));//�s���{��
                outstream.print(",");//
                outstream.print(result.getString("goods_entry.address1"));//�Z��2
                outstream.print(",");
                outstream.print(ReplaceString.replaceNumAlphaFull(result.getString("goods_entry.address2")));//�Z��3
                outstream.print(",");
                outstream.print(ReplaceString.replaceNumAlphaFull(result.getString("goods_entry.address3")));//�Z��4
                outstream.print(",");//�Z��4
                outstream.print(result.getString("goods_entry.tel1"));//�\���ғd�b�ԍ�
                outstream.print(",");
                outstream.print(",");
                outstream.print(result.getInt("goods_entry.seq"));//���i�R�[�h
                outstream.print(",");
                outstream.print(result.getInt("goods_entry.count"));//����
                outstream.print(",");
                outstream.print(",");
                outstream.print(",");
                outstream.print(",");
                outstream.print("0,");
                outstream.print(result.getString("goods_entry.mail_address").replace(","," "));//���[���A�h���X
                outstream.print(",");
                outstream.print(input_date + nf6.format(result.getInt("goods_entry.entry_id")) + nf2.format(result.getInt("goods_entry.entry_branch")));
                outstream.print(",");
                if (count == total_count)
                {
                    outstream.print("9");
                }
                else
                {
                    outstream.print("1");
                }
                outstream.print(",");
                outstream.println("");
           }
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(connection_sub);
        DBConnection.releaseResources(result,prestate,connection);
    }
// �N���[�Y
    outstream.close();
%>
<%=query%>