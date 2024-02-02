package jp.happyhotel.common;

public class XmlAttribute
{
    private String name;
    private String value;

    public XmlAttribute(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

    public String getName()
    {
        return name;
    }

    public String getValue()
    {
        return value;
    }

}
