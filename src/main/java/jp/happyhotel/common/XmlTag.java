package jp.happyhotel.common;

import java.util.ArrayList;

public class XmlTag
{
    // コンストラクタ
    private XmlTag()
    {
        return;
    }

    private XmlTag                  parent;
    private String                  name;
    private String                  value;
    private boolean                 hasValue = false;

    private ArrayList<XmlTag>       children;
    private ArrayList<XmlAttribute> attributes;

    private void setParent(XmlTag parent)
    {
        this.parent = parent;
        parent.addChildren( this );
        return;
    }

    public String getName()
    {
        return name;
    }

    public String getValue()
    {
        return(this.value);
    }

    public void setValue(String value)
    {
        this.value = value.toString();
        hasValue = true;
        return;
    }

    public void setNullValue()
    {
        hasValue = false;
        return;
    }

    public boolean getHasValue()
    {
        return(hasValue);
    }

    public boolean hasChild()
    {
        boolean ret = false;
        while( true )
        {
            if ( children == null )
            {
                break;
            }
            if ( children.size() <= 0 )
            {
                break;
            }
            ret = true;
            break;
        }
        return(ret);
    }

    public ArrayList<XmlTag> getChildren()
    {
        return(children);
    }

    public void addChildren(XmlTag xmlTag)
    {
        if ( children == null )
        {
            children = new ArrayList<XmlTag>();
        }
        children.add( xmlTag );
        return;
    }

    public boolean hasAttribte()
    {
        boolean ret = false;
        while( true )
        {
            if ( attributes == null )
            {
                break;
            }
            if ( attributes.size() <= 0 )
            {
                break;
            }
            ret = true;
            break;
        }
        return(ret);
    }

    public ArrayList<XmlAttribute> getAttributes()
    {
        return(attributes);
    }

    public void addAttribute(XmlAttribute attribute)
    {
        if ( attributes == null )
        {
            attributes = new ArrayList<XmlAttribute>();
        }
        attributes.add( attribute );
        return;
    }

    // 有効なタグか判定する処理
    // 値無・子要素なし・属性なしの場合は無効なタグ
    public boolean isValidTag()
    {
        boolean ret = true;

        while( true )
        {
            // 親要素がない = 自分がルートノードなので、必ず有効
            if ( parent == null )
            {
                break;
            }
            if ( hasValue )
            {
                break;
            }
            if ( hasChild() )
            {
                break;
            }
            if ( hasAttribte() )
            {
                break;
            }
            ret = false;
            break;
        }
        return(ret);
    }

    // XMLタグインスタンスファクトリ
    static public XmlTag createXmlTag(String tagName)
    {
        return(createXmlTag( tagName, "" ));
    }

    // XMLタグインスタンスファクトリ
    static public XmlTag createXmlTag(String tagName, int tagValue)
    {
        return(createXmlTag( tagName, Integer.toString( tagValue ) ));
    }

    // XMLタグインスタンスファクトリ
    static public XmlTag createXmlTag(String tagName, double tagValue)
    {
        return(createXmlTag( tagName, String.valueOf( tagValue ) ));
    }

    // XMLタグインスタンスファクトリ
    static public XmlTag createXmlTag(String tagName, String tagValue)
    {
        XmlTag ret = new XmlTag();

        ret.name = tagName;

        if ( tagValue != null && !tagValue.equals( "" ) )
        {
            ret.setValue( tagValue );
        }

        return(ret);
    }

    // XMLタグインスタンスファクトリ
    static public XmlTag createXmlTagNoCheck(String tagName, String tagValue)
    {
        XmlTag ret = new XmlTag();

        ret.name = tagName;
        if ( tagValue != null )
        {
            ret.setValue( tagValue );
        }

        return(ret);
    }

    // 親要素セット
    static public void setParent(XmlTag parent, XmlTag child)
    {
        while( true )
        {
            if ( child == null )
            {
                break;
            }
            child.setParent( parent );

            break;
        }
        return;
    }

}
