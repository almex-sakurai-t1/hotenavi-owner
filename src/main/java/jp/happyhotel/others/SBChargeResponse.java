package jp.happyhotel.others;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}providerCode"/>
 *         &lt;element ref="{}serviceCode"/>
 *         &lt;element ref="{}serviceUserId"/>
 *         &lt;element ref="{}servicerManageNo"/>
 *         &lt;element ref="{}transactionId"/>
 *         &lt;element ref="{}redirectURL"/>
 *         &lt;element ref="{}resultStatus"/>
 *         &lt;element ref="{}statusCode"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "providerCode",
    "serviceCode",
    "serviceUserId",
    "servicerManageNo",
    "transactionId",
    "redirectURL",
    "resultStatus",
    "statusCode"
})
@XmlRootElement(name = "BeginningBillingDemandResponse")
public class SBChargeResponse {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String providerCode;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String serviceCode;
    @XmlElement(required = true)
    protected String serviceUserId;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String servicerManageNo;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String transactionId;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String redirectURL;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String resultStatus;
    @XmlElement(required = true)
    protected BigInteger statusCode;

    /**
     * Gets the value of the providerCode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getProviderCode() {
        return providerCode;
    }

    /**
     * Sets the value of the providerCode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setProviderCode(String value) {
        this.providerCode = value;
    }

    /**
     * Gets the value of the serviceCode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getServiceCode() {
        return serviceCode;
    }

    /**
     * Sets the value of the serviceCode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setServiceCode(String value) {
        this.serviceCode = value;
    }

    /**
     * Gets the value of the serviceUserId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getServiceUserId() {
        return serviceUserId;
    }

    /**
     * Sets the value of the serviceUserId property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setServiceUserId(String value) {
        this.serviceUserId = value;
    }

    /**
     * Gets the value of the servicerManageNo property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getServicerManageNo() {
        return servicerManageNo;
    }

    /**
     * Sets the value of the servicerManageNo property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setServicerManageNo(String value) {
        this.servicerManageNo = value;
    }

    /**
     * Gets the value of the transactionId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Sets the value of the transactionId property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTransactionId(String value) {
        this.transactionId = value;
    }

    /**
     * Gets the value of the redirectURL property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRedirectURL() {
        return redirectURL;
    }

    /**
     * Sets the value of the redirectURL property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRedirectURL(String value) {
        this.redirectURL = value;
    }

    /**
     * Gets the value of the resultStatus property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getResultStatus() {
        return resultStatus;
    }

    /**
     * Sets the value of the resultStatus property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setResultStatus(String value) {
        this.resultStatus = value;
    }

    /**
     * Gets the value of the statusCode property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the value of the statusCode property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setStatusCode(BigInteger value) {
        this.statusCode = value;
    }

}
