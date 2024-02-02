package jp.happyhotel.common;

public class HotelInformation
{

    private int    id;
    private int    edit_id;
    private int    open_date;
    private int    renewal_date;
    private int    input_date;
    private String name;
    private String pref_name;
    private String address1;
    private String user_name;
    private String message;
    private String newmessage;
    private String contribution_dt;
    private String start_dt;
    private String title;
    private String link;

    public HotelInformation()
    {

        id = 0;
        edit_id = 0;
        open_date = 0;
        renewal_date = 0;
        name = null;
        pref_name = null;
        address1 = null;
        user_name = null;
        message = null;
        newmessage = null;
        contribution_dt = null;
        start_dt = null;
        title = null;
        link = null;

    }

    /**
     * @return the id
     */
    public int getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * @return the edit_id
     */
    public int getEdit_id()
    {
        return edit_id;
    }

    /**
     * @param edit_id the edit_id to set
     */
    public void setEdit_id(int edit_id)
    {
        this.edit_id = edit_id;
    }

    /**
     * @return the open_date
     */
    public int getOpen_date()
    {
        return open_date;
    }

    /**
     * @param open_date the open_date to set
     */
    public void setOpen_date(int open_date)
    {
        this.open_date = open_date;
    }

    /**
     * @return the renewal_date
     */
    public int getRenewal_date()
    {
        return renewal_date;
    }

    /**
     * @param renewal_date the renewal_date to set
     */
    public void setRenewal_date(int renewal_date)
    {
        this.renewal_date = renewal_date;
    }

    /**
     * @return the contribution_date
     */
    public String getContribution_date()
    {

        return contribution_dt;
    }

    /**
     * This method return the date in yyyy/mm/dd format
     * 
     * @param contribution_date the contribution_date to set
     */
    public void setContribution_date(int contribution_date)
    {
        int y = contribution_date / 10000;
        int m = (contribution_date / 100) % 100;
        int d = (contribution_date % 100);
        String mm = Integer.toString( m );
        String dd = Integer.toString( d );
        String yr = Integer.toString( y );
        contribution_dt = yr + "/" + (m < 10 ? "0" + mm : mm) + "/" + (d < 10 ? "0" + dd : dd);
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the pref_name
     */
    public String getPref_name()
    {
        return pref_name;
    }

    /**
     * @param pref_name the pref_name to set
     */
    public void setPref_name(String pref_name)
    {
        this.pref_name = pref_name;
    }

    /**
     * @return the address1
     */
    public String getAddress1()
    {
        return address1;
    }

    /**
     * @param address1 the address1 to set
     */
    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    /**
     * @return the user_name
     */
    public String getUser_name()
    {
        return user_name;
    }

    /**
     * @param user_name the user_name to set
     */
    public void setUser_name(String user_name)
    {
        user_name = user_name.replaceAll( "&", "&amp;" );
        user_name = user_name.replaceAll( "<", "&lt;" );
        user_name = user_name.replaceAll( ">", "&gt;" );

        this.user_name = user_name;
    }

    /**
     * @return the message
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * This method truncate the message length up to 60 characters
     * 
     * @param message the message to set
     */
    public void setMessage(String message)
    {
        if ( message.length() > 60 )
        {
            String paramMessage = message.substring( 0, 59 );

            paramMessage = paramMessage.replaceAll( "<BR>", "" );
            paramMessage = paramMessage.replaceAll( "<br>", "" );
            paramMessage = paramMessage.replaceAll( "&", "&amp;" );
            paramMessage = paramMessage.replaceAll( "<", "&lt;" );
            paramMessage = paramMessage.replaceAll( ">", "&gt;" );

            paramMessage = paramMessage + "...";
            this.message = paramMessage;
        }
        else
        {
            this.message = message;
        }
    }

    /**
     * @return the message
     */
    public String getNewMessage()
    {
        return newmessage;
    }

    /**
     * This method truncate the message length up to 60 characters
     * 
     * @param message the message to set
     */
    public void setNewMessage(String message)
    {
        message = message.replaceAll( "<BR>", "" );
        message = message.replaceAll( "<br>", "" );
        message = message.replaceAll( "&", "&amp;" );
        message = message.replaceAll( "<", "&lt;" );
        message = message.replaceAll( ">", "&gt;" );

        this.newmessage = message;

    }

    /**
     * @return the input_date
     */
    public int getInput_date()
    {
        return input_date;
    }

    /**
     * @param input_date the input_date to set
     */
    public void setInput_date(int input_date)
    {
        this.input_date = input_date;
    }

    public String getStart_date()
    {
        return start_dt;
    }

    public void setStart_date(int start_date)
    {
        int y = start_date / 10000;
        int m = (start_date / 100) % 100;
        int d = (start_date % 100);
        String mm = Integer.toString( m );
        String dd = Integer.toString( d );
        String yr = Integer.toString( y );
        start_dt = " - " + yr + "/" + (m < 10 ? "0" + mm : mm) + "/" + (d < 10 ? "0" + dd : dd) + " - ";

    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        title = title.replaceAll( "&", "&amp;" );
        title = title.replaceAll( "<", "&lt;" );
        title = title.replaceAll( ">", "&gt;" );
        this.title = title;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        link = link.replaceAll( "&", "&amp;" );
        link = link.replaceAll( "<", "&lt;" );
        link = link.replaceAll( ">", "&gt;" );

        this.link = link;
    }

}
