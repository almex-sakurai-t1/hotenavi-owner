package com.hotenavi2.mailmagazine;

import org.apache.commons.lang3.StringEscapeUtils;

import com.hotenavi2.data.DataMagHotel;

public class LogicProjectCheck
{
    private final String className     = "LogicProjectCheck";
    private String       disp_message;
    private int          response_code;
    private final int    NomalResponse = 200;

    public LogicProjectCheck()
    {
        this.disp_message = "";
        this.response_code = 0;
    }

    public int getResponseCode()
    {
        return this.response_code;
    }

    public void setResponseCode(int responsCode)
    {
        this.response_code = responsCode;
    }

    public String getDispMssage()
    {
        return this.disp_message;
    }

    public void setDispMssage(String dispMessage)
    {
        this.disp_message = dispMessage;
    }

    public boolean check(String hotelId)
    {

        ProjectGet projectGet = new ProjectGet();
        projectGet.execute( hotelId );
        this.setResponseCode( projectGet.getResponseCode() );

        if ( projectGet.getResponseCode() != NomalResponse )
        {
            disp_message += "【Project Get status】" + projectGet.getResponseCode() + "<br>";
            disp_message += StringEscapeUtils.escapeHtml4( projectGet.getErrorMessage() );
            return false;
        }
        disp_message += "【名称】" + StringEscapeUtils.escapeHtml4( projectGet.getName() ) + "<br>";
        disp_message += "【登録メールアドレス】" + StringEscapeUtils.escapeHtml4( projectGet.getEmail() ) + "<br>";

        ProjectRolesGet projectRolesGet = new ProjectRolesGet();
        projectRolesGet.execute( hotelId );
        if ( projectRolesGet.getResponseCode() != NomalResponse )
        {
            disp_message += "【Project Role Get status】" + projectRolesGet.getResponseCode() + "<br>";
            return false;
        }
        disp_message += "【ロールID】magazine.caller<br>";
        disp_message += "【ロールの説明】" + StringEscapeUtils.escapeHtml4( projectRolesGet.getDescription() ) + "<br>";

        ProjectApiKeysGet projectApiKeysGet = new ProjectApiKeysGet();
        projectApiKeysGet.execute( hotelId );
        if ( projectApiKeysGet.getResponseCode() != NomalResponse )
        {
            disp_message += "【Project Api Key Get status】" + projectApiKeysGet.getResponseCode() + "<br>";
            return false;
        }

        MagazineSenderGet magazineSenderGet = new MagazineSenderGet();
        magazineSenderGet.execute( hotelId );
        if ( magazineSenderGet.getResponseCode() != NomalResponse )
        {
            disp_message += "【Magazine Sender Get status】" + magazineSenderGet.getResponseCode() + "<br>";
            return false;
        }

        String dataFromEmail = "";
        String dataReplyToEmail = "";
        DataMagHotel dmh = new DataMagHotel();
        if ( dmh.getData( hotelId ) )
        {
            dataFromEmail = dmh.getFromEmail();
            dataReplyToEmail = dmh.getReplyToEmail();
        }

        boolean ret = true;

        disp_message += "【from email】" + StringEscapeUtils.escapeHtml4( magazineSenderGet.getFromEmail() ) + "<br>";
        if ( !dataFromEmail.equals( magazineSenderGet.getFromEmail() ) )
        {
            disp_message += "<font color=red>メールマガジン送信元メールアドレスが更新されました</font><br>";
            ret = false;
        }
        disp_message += "【from name】" + StringEscapeUtils.escapeHtml4( magazineSenderGet.getFromName() ) + "<br>";
        disp_message += "【reply_to email】" + StringEscapeUtils.escapeHtml4( magazineSenderGet.getReplyToEmail() ) + "<br>";
        if ( !dataReplyToEmail.equals( magazineSenderGet.getReplyToEmail() ) )
        {
            disp_message += "<font color=red>メールマガジン返信先メールアドレスが更新されました</font><br>";
            ret = false;
        }
        disp_message += "【reply_to name】" + StringEscapeUtils.escapeHtml4( magazineSenderGet.getReplyToName() ) + "<br>";
        disp_message += "【address】" + StringEscapeUtils.escapeHtml4( magazineSenderGet.getAddress() ) + "<br>";
        disp_message += "【city】" + StringEscapeUtils.escapeHtml4( magazineSenderGet.getCity() ) + "<br>";
        disp_message += "【zip_code】" + StringEscapeUtils.escapeHtml4( magazineSenderGet.getZipCode() ) + "<br>";
        disp_message += "【revision】" + magazineSenderGet.getRevision() + "<br>";

        return ret;
    }
}
