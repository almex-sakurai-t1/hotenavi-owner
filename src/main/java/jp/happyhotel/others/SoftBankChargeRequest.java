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
 *         &lt;element ref="{}amount"/>
 *         &lt;element ref="{}cpNote"/>
 *         &lt;element ref="{}okURL"/>
 *         &lt;element ref="{}ngURL"/>
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
    "amount",
    "cpNote",
    "okURL",
    "ngURL"
})
@XmlRootElement(name = "BeginningBillingDemand")
public class SoftBankChargeRequest {

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
    protected BigInteger amount;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String cpNote;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String okURL;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String ngURL;

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
     * Gets the value of the amount property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setAmount(BigInteger value) {
        this.amount = value;
    }

    /**
     * Gets the value of the cpNote property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCpNote() {
        return cpNote;
    }

    /**
     * Sets the value of the cpNote property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCpNote(String value) {
        this.cpNote = value;
    }

    /**
     * Gets the value of the okURL property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOkURL() {
        return okURL;
    }

    /**
     * Sets the value of the okURL property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOkURL(String value) {
        this.okURL = value;
    }

    /**
     * Gets the value of the ngURL property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNgURL() {
        return ngURL;
    }

    /**
     * Sets the value of the ngURL property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNgURL(String value) {
        this.ngURL = value;
    }

}
