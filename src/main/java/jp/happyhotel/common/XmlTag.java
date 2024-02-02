package jp.happyhotel.common;

import java.util.ArrayList;

public class XmlTag
{
    // �R���X�g���N�^
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

    // �L���ȃ^�O�����肷�鏈��
    // �l���E�q�v�f�Ȃ��E�����Ȃ��̏ꍇ�͖����ȃ^�O
    public boolean isValidTag()
    {
        boolean ret = true;

        while( true )
        {
            // �e�v�f���Ȃ� = ���������[�g�m�[�h�Ȃ̂ŁA�K���L��
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

    // XML�^�O�C���X�^���X�t�@�N�g��
    static public XmlTag createXmlTag(String tagName)
    {
        return(createXmlTag( tagName, "" ));
    }

    // XML�^�O�C���X�^���X�t�@�N�g��
    static public XmlTag createXmlTag(String tagName, int tagValue)
    {
        return(createXmlTag( tagName, Integer.toString( tagValue ) ));
    }

    // XML�^�O�C���X�^���X�t�@�N�g��
    static public XmlTag createXmlTag(String tagName, double tagValue)
    {
        return(createXmlTag( tagName, String.valueOf( tagValue ) ));
    }

    // XML�^�O�C���X�^���X�t�@�N�g��
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

    // XML�^�O�C���X�^���X�t�@�N�g��
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

    // �e�v�f�Z�b�g
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
