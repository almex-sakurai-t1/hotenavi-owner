package jp.happyhotel.others;

import java.io.OutputStream;
import java.io.Serializable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import jp.happyhotel.common.Logging;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;

public class GenerateXmlTouch implements Serializable
{
    String                 encode        = "";
    DocumentBuilderFactory factory;
    DocumentBuilder        builder;
    Document               document;
    Element                rootNode      = null;
    Element                resultNode    = null;
    Element                pointNode     = null;
    Element                messageNode   = null;
    Element                registUrlNode = null;
    Element                fileUrlNode1  = null;
    Element                fileUrlNode2  = null;

    public GenerateXmlTouch()
    {
        try
        {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder(); // DocumentBuilder�C���X�^���X
            document = builder.newDocument(); // Document�̐���
            encode = "";
        }
        catch ( Exception e )
        {
            Logging.error( "[GenerateXmlTouch] Exception=" + e.toString() );
        }
    }

    public String getEncode()
    {
        return this.encode;
    }

    public void setEncode(String encode)
    {
        this.encode = encode;
    }

    /** ���[�g�m�[�h�쐬 **/
    public void setRootName(String methodName)
    {
        this.rootNode = document.createElement( methodName );
    }

    /** ���[�g�m�[�h�̒l���Z�b�g **/
    public void setRootValue(String methodValue)
    {
        this.rootNode.appendChild( document.createTextNode( methodValue ) );
    }

    /** ���ʃm�[�h�쐬 **/
    public void setResultName(String resultName)
    {
        this.resultNode = document.createElement( resultName );
    }

    /** ���ʃm�[�h�̒l���Z�b�g **/
    public void setResultValue(String resultValue)
    {
        this.resultNode.appendChild( document.createTextNode( resultValue ) );
    }

    /** �|�C���g�m�[�h�쐬 **/
    public void setPointName(String pointName)
    {
        this.pointNode = document.createElement( pointName );
    }

    /** ���b�Z�[�W�|�C���g�m�[�h�̒l���Z�b�g **/
    public void setPointValue(String pointValue)
    {
        this.pointNode.appendChild( document.createTextNode( pointValue ) );
    }

    /** ���b�Z�[�W�m�[�h�쐬 **/
    public void setMessageName(String messageName)
    {
        this.messageNode = document.createElement( messageName );
    }

    /** ���b�Z�[�W�|�C���g�m�[�h�̒l���Z�b�g **/
    public void setMessageValue(String messageValue)
    {
        this.messageNode.appendChild( document.createTextNode( messageValue ) );
    }

    /** �o�^URL�m�[�h�쐬 **/
    public void setRegistUrlName(String registUrlName)
    {
        this.registUrlNode = document.createElement( registUrlName );
    }

    /** �o�^URL�m�[�h�̒l���Z�b�g **/
    public void setRegistUrlValue(String registUrlValue)
    {
        this.registUrlNode.appendChild( document.createTextNode( registUrlValue ) );
    }

    /** �t�@�C��URL�m�[�h�쐬 **/
    public void setFileUrl1Name(String fileUrlName1)
    {
        this.fileUrlNode1 = document.createElement( fileUrlName1 );
    }

    /** �t�@�C��URL�m�[�h�̒l���Z�b�g **/
    public void setFileUrl1Value(String fileUrlValue1)
    {
        this.fileUrlNode1.appendChild( document.createTextNode( fileUrlValue1 ) );
    }

    /** �t�@�C��URL�m�[�h�쐬 **/
    public void setFileUrl2Name(String fileUrlName2)
    {
        this.fileUrlNode2 = document.createElement( fileUrlName2 );
    }

    /** �t�@�C��URL�m�[�h�̒l���Z�b�g **/
    public void setFileUrl2Value(String fileUrlValue2)
    {
        this.fileUrlNode2.appendChild( document.createTextNode( fileUrlValue2 ) );
    }

    /**
     * XML�o�̓��\�b�h
     * 
     * @param response HttpServletResponse
     * @see �K��rootNode���Z�b�g���Ă�������
     * */
    public void xmlOutput(OutputStream response)
    {
        if ( this.rootNode != null )
        {
            document.appendChild( rootNode );
            if ( this.resultNode != null )
            {
                this.rootNode.appendChild( this.resultNode );
            }
            if ( this.pointNode != null )
            {
                this.rootNode.appendChild( this.pointNode );
            }
            if ( this.messageNode != null )
            {
                this.rootNode.appendChild( this.messageNode );
            }
            if ( this.registUrlNode != null )
            {
                this.rootNode.appendChild( this.registUrlNode );
            }

            if ( this.fileUrlNode1 != null )
            {
                this.rootNode.appendChild( this.fileUrlNode1 );
            }
            if ( this.fileUrlNode2 != null )
            {
                this.rootNode.appendChild( this.fileUrlNode2 );
            }

            try
            {
                TransformerFactory tff = TransformerFactory.newInstance();
                Transformer tf = tff.newTransformer();
                tf.setOutputProperty( "encoding", this.encode );

                // �C���f���g���s��
                tf.setOutputProperty( OutputKeys.INDENT, "yes" );
                tf.setOutputProperty( OutputKeys.METHOD, "xml" );

                // �C���f���g�̕�����
                tf.setOutputProperty( OutputPropertiesFactory.S_KEY_INDENT_AMOUNT, "4" );
                // xml�t�@�C�����o��
                tf.transform( new DOMSource( document ), new StreamResult( response ) );

            }
            catch ( Exception e )
            {
                Logging.error( "[GenerateXmlTouch.xmlOutput()] Exception=" + e.toString() );
            }
        }
        else
        {
        }

    }
}
