
package sap.interfacing.soap.po;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Structure of Purchase Order Schedules
 * 
 * <p>Java class for ZST_PO_SCHEDULES complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZST_PO_SCHEDULES">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PO_ITEM" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="5"/>
 *               &lt;pattern value="\d+"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SERIAL_NO" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *               &lt;pattern value="\d+"/>
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
 *         &lt;element name="PREQ_NO" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PREQ_ITEM" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="5"/>
 *               &lt;pattern value="\d+"/>
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
@XmlType(name = "ZST_PO_SCHEDULES", propOrder = {
    "poitem",
    "serialno",
    "quantity",
    "preqno",
    "preqitem"
})
public class ZSTPOSCHEDULES {

    @XmlElement(name = "PO_ITEM")
    protected String poitem;
    @XmlElement(name = "SERIAL_NO")
    protected String serialno;
    @XmlElement(name = "QUANTITY")
    protected BigDecimal quantity;
    @XmlElement(name = "PREQ_NO")
    protected String preqno;
    @XmlElement(name = "PREQ_ITEM")
    protected String preqitem;

    /**
     * Gets the value of the poitem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOITEM() {
        return poitem;
    }

    /**
     * Sets the value of the poitem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOITEM(String value) {
        this.poitem = value;
    }

    /**
     * Gets the value of the serialno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSERIALNO() {
        return serialno;
    }

    /**
     * Sets the value of the serialno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSERIALNO(String value) {
        this.serialno = value;
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
     * Gets the value of the preqno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPREQNO() {
        return preqno;
    }

    /**
     * Sets the value of the preqno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPREQNO(String value) {
        this.preqno = value;
    }

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

}
