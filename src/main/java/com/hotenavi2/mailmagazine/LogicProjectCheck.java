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
            disp_message += "yProject Get statusz" + projectGet.getResponseCode() + "<br>";
            disp_message += StringEscapeUtils.escapeHtml4( projectGet.getErrorMessage() );
            return false;
        }
        disp_message += "y–¼Ìz" + StringEscapeUtils.escapeHtml4( projectGet.getName() ) + "<br>";
        disp_message += "y“o˜^ƒ[ƒ‹ƒAƒhƒŒƒXz" + StringEscapeUtils.escapeHtml4( projectGet.getEmail() ) + "<br>";

        ProjectRolesGet projectRolesGet = new ProjectRolesGet();
        projectRolesGet.execute( hotelId );
        if ( projectRolesGet.getResponseCode() != NomalResponse )
        {
            disp_message += "yProject Role Get statusz" + projectRolesGet.getResponseCode() + "<br>";
            return false;
        }
        disp_message += "yƒ[ƒ‹IDzmagazine.caller<br>";
        disp_message += "yƒ[ƒ‹‚Ìà–¾z" + StringEscapeUtils.escapeHtml4( projectRolesGet.getDescription() ) + "<br>";

        ProjectApiKeysGet projectApiKeysGet = new ProjectApiKeysGet();
        projectApiKeysGet.execute( hotelId );
        if ( projectApiKeysGet.getResponseCode() != NomalResponse )
        {
            disp_message += "yProject Api Key Get statusz" + projectApiKeysGet.getResponseCode() + "<br>";
            return false;
        }

        MagazineSenderGet magazineSenderGet = new MagazineSenderGet();
        magazineSenderGet.execute( hotelId );
        if ( magazineSenderGet.getResponseCode() != NomalResponse )
        {
            disp_message += "yMagazine Sender Get statusz" + magazineSenderGet.getResponseCode() + "<br>";
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

        disp_message += "yfrom emailz" + StringEscapeUtils.escapeHtml4( magazineSenderGet.getFromEmail() ) + "<br>";
        if ( !dataFromEmail.equals( magazineSenderGet.getFromEmail() ) )
        {
            disp_message += "<font color=red>ƒ[ƒ‹ƒ}ƒKƒWƒ“‘—MŒ³ƒ[ƒ‹ƒAƒhƒŒƒX‚ªXV‚³‚ê‚Ü‚µ‚½</font><br>";
            ret = false;
        }
        disp_message += "yfrom namez" + StringEscapeUtils.escapeHtml4( magazineSenderGet.getFromName() ) + "<br>";
        disp_message += "yreply_to emailz" + StringEscapeUtils.escapeHtml4( magazineSenderGet.getReplyToEmail() ) + "<br>";
        if ( !dataReplyToEmail.equals( magazineSenderGet.getReplyToEmail() ) )
        {
            disp_message += "<font color=red>ƒ[ƒ‹ƒ}ƒKƒWƒ“•ÔMæƒ[ƒ‹ƒAƒhƒŒƒX‚ªXV‚³‚ê‚Ü‚µ‚½</font><br>";
            ret = false;
        }
        disp_message += "yreply_to namez" + StringEscapeUtils.escapeHtml4( magazineSenderGet.getReplyToName() ) + "<br>";
        disp_message += "yaddressz" + StringEscapeUtils.escapeHtml4( magazineSenderGet.getAddress() ) + "<br>";
        disp_message += "ycityz" + StringEscapeUtils.escapeHtml4( magazineSenderGet.getCity() ) + "<br>";
        disp_message += "yzip_codez" + StringEscapeUtils.escapeHtml4( magazineSenderGet.getZipCode() ) + "<br>";
        disp_message += "yrevisionz" + magazineSenderGet.getRevision() + "<br>";

        return ret;
    }
}
