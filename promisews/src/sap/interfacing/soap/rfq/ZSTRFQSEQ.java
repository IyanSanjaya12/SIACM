
package sap.interfacing.soap.rfq;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Structure for RFQ Seq Number
 * 
 * <p>Java class for ZST_RFQ_SEQ complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZST_RFQ_SEQ">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BANFN" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="BNFPO" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="5"/>
 *               &lt;pattern value="\d+"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="NETPR" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="11"/>
 *               &lt;fractionDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="WAERS" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PACKNO" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *               &lt;pattern value="\d+"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="EXTROW" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *               &lt;pattern value="\d+"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="EBELP" minOccurs="0">
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
@XmlType(name = "ZST_RFQ_SEQ", propOrder = {
    "banfn",
    "bnfpo",
    "netpr",
    "waers",
    "packno",
    "extrow",
    "ebelp"
})
public class ZSTRFQSEQ {

    @XmlElement(name = "BANFN")
    protected String banfn;
    @XmlElement(name = "BNFPO")
    protected String bnfpo;
    @XmlElement(name = "NETPR")
    protected BigDecimal netpr;
    @XmlElement(name = "WAERS")
    protected String waers;
    @XmlElement(name = "PACKNO")
    protected String packno;
    @XmlElement(name = "EXTROW")
    protected String extrow;
    @XmlElement(name = "EBELP")
    protected String ebelp;

    /**
     * Gets the value of the banfn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBANFN() {
        return banfn;
    }

    /**
     * Sets the value of the banfn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBANFN(String value) {
        this.banfn = value;
    }

    /**
     * Gets the value of the bnfpo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBNFPO() {
        return bnfpo;
    }

    /**
     * Sets the value of the bnfpo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBNFPO(String value) {
        this.bnfpo = value;
    }

    /**
     * Gets the value of the netpr property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNETPR() {
        return netpr;
    }

    /**
     * Sets the value of the netpr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNETPR(BigDecimal value) {
        this.netpr = value;
    }

    /**
     * Gets the value of the waers property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWAERS() {
        return waers;
    }

    /**
     * Sets the value of the waers property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWAERS(String value) {
        this.waers = value;
    }

    /**
     * Gets the value of the packno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPACKNO() {
        return packno;
    }

    /**
     * Sets the value of the packno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPACKNO(String value) {
        this.packno = value;
    }

    /**
     * Gets the value of the extrow property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEXTROW() {
        return extrow;
    }

    /**
     * Sets the value of the extrow property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEXTROW(String value) {
        this.extrow = value;
    }

    /**
     * Gets the value of the ebelp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEBELP() {
        return ebelp;
    }

    /**
     * Sets the value of the ebelp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEBELP(String value) {
        this.ebelp = value;
    }

}
