package com.hotenavi2.mailmagazine;

import com.hotenavi2.data.DataMagHotel;

public class LogicProjectStart
{
    private final String className       = "LogicProjectStart";
    private String       error_message;
    private int          response_code;
    private final int    NomalResponse   = 200;
    private final int    CreatedResponse = 201;                // 作成成功
    private final int    DeleteResponse  = 204;                // 削除成功

    public LogicProjectStart()
    {
        this.error_message = "";
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

    public String getErrorMessage()
    {
        return this.error_message;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.error_message = errorMessage;
    }

    public void execute(String hotelId)
    {
        /* プロジェクトが登録されていなければ作成する */
        ProjectGet projectGet = new ProjectGet();
        projectGet.execute( hotelId );
        if ( projectGet.getResponseCode() != NomalResponse )
        {
            ProjectAdd projectAdd = new ProjectAdd();
            projectAdd.execute( hotelId );
            if ( projectAdd.getResponseCode() != CreatedResponse )
            {
                setResponseCode( projectAdd.getResponseCode() );
                setErrorMessage( "project add error" );
                return;
            }
        }
        ProjectRolesGet projectRolesGet = new ProjectRolesGet();
        projectRolesGet.execute( hotelId );
        if ( projectRolesGet.getResponseCode() != NomalResponse )
        {
            ProjectRoleAdd projectRoleAdd = new ProjectRoleAdd();
            projectRoleAdd.execute( hotelId );
            if ( projectRoleAdd.getResponseCode() != CreatedResponse )
            {
                setResponseCode( projectRoleAdd.getResponseCode() );
                setErrorMessage( "project role add error" );
                return;
            }
        }

        ProjectApiKeysGet projectApiKeysGet = new ProjectApiKeysGet();
        projectApiKeysGet.execute( hotelId );
        if ( projectApiKeysGet.getResponseCode() != NomalResponse )
        {
            ProjectApiKeyAdd projectApiKeyAdd = new ProjectApiKeyAdd();
            projectApiKeyAdd.execute( hotelId );
            if ( projectApiKeyAdd.getResponseCode() != CreatedResponse )
            {
                setResponseCode( projectApiKeyAdd.getResponseCode() );
                setErrorMessage( "project api-key add error" );
                return;
            }
        }

        String dataFromEmail = "";
        String dataReplyToEmail = "";
        DataMagHotel dmh = new DataMagHotel();
        if ( dmh.getData( hotelId ) )
        {
            dataFromEmail = dmh.getFromEmail();
            dataReplyToEmail = dmh.getReplyToEmail();
        }
        MagazineSenderGet magazineSenderGet = new MagazineSenderGet();
        magazineSenderGet.execute( hotelId );
        if ( magazineSenderGet.getResponseCode() != NomalResponse )
        {
            MagazineSenderAdd magazineSenderAdd = new MagazineSenderAdd();
            magazineSenderAdd.execute( hotelId );
            if ( magazineSenderAdd.getResponseCode() != CreatedResponse )
            {
                setResponseCode( magazineSenderAdd.getResponseCode() );
                setErrorMessage( "magazine sender add error" );
                return;
            }
        }
        else if ( !dataFromEmail.equals( magazineSenderGet.getFromEmail() ) || !dataReplyToEmail.equals( magazineSenderGet.getReplyToEmail() ) )
        {
            MagazineSenderDelete magazineSenderDelete = new MagazineSenderDelete();
            magazineSenderDelete.execute( hotelId );
            if ( magazineSenderDelete.getResponseCode() != DeleteResponse )
            {
                setResponseCode( magazineSenderDelete.getResponseCode() );
                setErrorMessage( "magazine sender delete error" );
                return;
            }
            MagazineSenderAdd magazineSenderAdd = new MagazineSenderAdd();
            magazineSenderAdd.execute( hotelId );
            if ( magazineSenderAdd.getResponseCode() != CreatedResponse )
            {
                setResponseCode( magazineSenderAdd.getResponseCode() );
                setErrorMessage( "magazine sender add error" );
                return;
            }
            /*
             * MagazineSenderPatch magazineSenderPatch = new MagazineSenderPatch();
             * magazineSenderPatch.setRevision( magazineSenderGet.getRevision() );
             * magazineSenderPatch.execute( hotelId );
             * if ( magazineSenderPatch.getResponseCode() != NomalResponse )
             * {
             * setResponseCode( magazineSenderPatch.getResponseCode() );
             * setErrorMessage( "magazine sender patch error" );
             * return;
             * }
             */
        }
    }
}
