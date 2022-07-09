
package sap.interfacing.soap.pr;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Structure of Purchase Requisition
 * 
 * <p>Java class for ZST_PR_ITEM complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZST_PR_ITEM">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PREQ_ITEM" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="5"/>
 *               &lt;pattern value="\d+"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ACCTASSCAT" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PUR_GROUP" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PREQ_NAME" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="MATERIAL" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="18"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SHORT_TEXT" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="QUANTITY" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="13"/>
 *               &lt;fractionDigits value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="UNIT" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PREQ_PRICE" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="28"/>
 *               &lt;fractionDigits value="9"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PREQ_DATE" type="{urn:sap-com:document:sap:rfc:functions}date" minOccurs="0"/>
 *         &lt;element name="DELIV_DATE" type="{urn:sap-com:document:sap:rfc:functions}date" minOccurs="0"/>
 *         &lt;element name="PLANT" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="STORE_LOC" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="MATL_GROUP" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="9"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="TRACKINGNO" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="CURRENCY" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZST_PR_ITEM", propOrder = {
    "preqitem",
    "acctasscat",
    "purgroup",
    "preqname",
    "material",
    "shorttext",
    "quantity",
    "unit",
    "preqprice",
    "preqdate",
    "delivdate",
    "plant",
    "storeloc",
    "matlgroup",
    "trackingno",
    "currency"
})
public class ZSTPRITEM {

    @XmlElement(name = "PREQ_ITEM")
    protected String preqitem;
    @XmlElement(name = "ACCTASSCAT")
    protected String acctasscat;
    @XmlElement(name = "PUR_GROUP")
    protected String purgroup;
    @XmlElement(name = "PREQ_NAME")
    protected String preqname;
    @XmlElement(name = "MATERIAL")
    protected String material;
    @XmlElement(name = "SHORT_TEXT")
    protected String shorttext;
    @XmlElement(name = "QUANTITY")
    protected BigDecimal quantity;
    @XmlElement(name = "UNIT")
    protected String unit;
    @XmlElement(name = "PREQ_PRICE")
    protected BigDecimal preqprice;
    @XmlElement(name = "PREQ_DATE")
    protected String preqdate;
    @XmlElement(name = "DELIV_DATE")
    protected String delivdate;
    @XmlElement(name = "PLANT")
    protected String plant;
    @XmlElement(name = "STORE_LOC")
    protected String storeloc;
    @XmlElement(name = "MATL_GROUP")
    protected String matlgroup;
    @XmlElement(name = "TRACKINGNO")
    protected String trackingno;
    @XmlElement(name = "CURRENCY")
    protected String currency;

    /**
     * Gets the value of the preqitem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPREQITEM() {
        return preqitem;
    }

    /**
     * Sets the value of the preqitem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPREQITEM(String value) {
        this.preqitem = value;
    }

    /**
     * Gets the value of the acctasscat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getACCTASSCAT() {
        return acctasscat;
    }

    /**
     * Sets the value of the acctasscat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setACCTASSCAT(String value) {
        this.acctasscat = value;
    }

    /**
     * Gets the value of the purgroup property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPURGROUP() {
        return purgroup;
    }

    /**
     * Sets the value of the purgroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPURGROUP(String value) {
        this.purgroup = value;
    }

    /**
     * Gets the value of the preqname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPREQNAME() {
        return preqname;
    }

    /**
     * Sets the value of the preqname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPREQNAME(String value) {
        this.preqname = value;
    }

    /**
     * Gets the value of the material property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATERIAL() {
        return material;
    }

    /**
     * Sets the value of the material property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATERIAL(String value) {
        this.material = value;
    }

    /**
     * Gets the value of the shorttext property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSHORTTEXT() {
        return shorttext;
    }

    /**
     * Sets the value of the shorttext property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSHORTTEXT(String value) {
        this.shorttext = value;
    }

    /**
     * Gets the value of the quantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQUANTITY() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQUANTITY(BigDecimal value) {
        this.quantity = value;
    }

    /**
     * Gets the value of the unit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUNIT() {
        return unit;
    }

    /**
     * Sets the value of the unit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUNIT(String value) {
        this.unit = value;
    }

    /**
     * Gets the value of the preqprice property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPREQPRICE() {
        return preqprice;
    }

    /**
     * Sets the value of the preqprice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPREQPRICE(BigDecimal value) {
        this.preqprice = value;
    }

    /**
     * Gets the value of the preqdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPREQDATE() {
        return preqdate;
    }

    /**
     * Sets the value of the preqdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPREQDATE(String value) {
        this.preqdate = value;
    }

    /**
     * Gets the value of the delivdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDELIVDATE() {
        return delivdate;
    }

    /**
     * Sets the value of the delivdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDELIVDATE(String value) {
        this.delivdate = value;
    }

    /**
     * Gets the value of the plant property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPLANT() {
        return plant;
    }

    /**
     * Sets the value of the plant property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPLANT(String value) {
        this.plant = value;
    }

    /**
     * Gets the value of the storeloc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTORELOC() {
        return storeloc;
    }

    /**
     * Sets the value of the storeloc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTORELOC(String value) {
        this.storeloc = value;
    }

    /**
     * Gets the value of the matlgroup property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATLGROUP() {
        return matlgroup;
    }

    /**
     * Sets the value of the matlgroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATLGROUP(String value) {
        this.matlgroup = value;
    }

    /**
     * Gets the value of the trackingno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRACKINGNO() {
        return trackingno;
    }

    /**
     * Sets the value of the trackingno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRACKINGNO(String value) {
        this.trackingno = value;
    }

    /**
     * Gets the value of the currency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCURRENCY() {
        return currency;
    }

    /**
     * Sets the value of the currency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCURRENCY(String value) {
        this.currency = value;
    }

}
