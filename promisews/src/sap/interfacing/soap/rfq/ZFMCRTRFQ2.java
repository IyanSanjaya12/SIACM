
package sap.interfacing.soap.rfq;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="GV_EKORG" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GV_LIFNR">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GV_NUMBER">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GV_QUO_DEADLINE" type="{urn:sap-com:document:sap:rfc:functions}date" minOccurs="0"/>
 *         &lt;element name="GV_RFQ_DATE" type="{urn:sap-com:document:sap:rfc:functions}date" minOccurs="0"/>
 *         &lt;element name="GT_RETURN">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}BAPIRET2" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="GT_SEQ_NUM">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZST_RFQ_SEQ" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "ZFM_CRT_RFQ_2")
public class ZFMCRTRFQ2 {

    @XmlElement(name = "GV_EKORG")
    protected String gvekorg;
    @XmlElement(name = "GV_LIFNR", required = true)
    protected String gvlifnr;
    @XmlElement(name = "GV_NUMBER", required = true)
    protected String gvnumber;
    @XmlElement(name = "GV_QUO_DEADLINE")
    protected String gvquodeadline;
    @XmlElement(name = "GV_RFQ_DATE")
    protected String gvrfqdate;
    @XmlElement(name = "GT_RETURN", required = true)
    protected ZFMCRTRFQ2 .GTRETURN gtreturn;
    @XmlElement(name = "GT_SEQ_NUM", required = true)
    protected ZFMCRTRFQ2 .GTSEQNUM gtseqnum;

    /**
     * Gets the value of the gvekorg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGVEKORG() {
        return gvekorg;
    }

    /**
     * Sets the value of the gvekorg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGVEKORG(String value) {
        this.gvekorg = value;
    }

    /**
     * Gets the value of the gvlifnr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGVLIFNR() {
        return gvlifnr;
    }

    /**
     * Sets the value of the gvlifnr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGVLIFNR(String value) {
        this.gvlifnr = value;
    }

    /**
     * Gets the value of the gvnumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGVNUMBER() {
        return gvnumber;
    }

    /**
     * Sets the value of the gvnumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGVNUMBER(String value) {
        this.gvnumber = value;
    }

    /**
     * Gets the value of the gvquodeadline property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGVQUODEADLINE() {
        return gvquodeadline;
    }

    /**
     * Sets the value of the gvquodeadline property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGVQUODEADLINE(String value) {
        this.gvquodeadline = value;
    }

    /**
     * Gets the value of the gvrfqdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGVRFQDATE() {
        return gvrfqdate;
    }

    /**
     * Sets the value of the gvrfqdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGVRFQDATE(String value) {
        this.gvrfqdate = value;
    }

    /**
     * Gets the value of the gtreturn property.
     * 
     * @return
     *     possible object is
     *     {@link ZFMCRTRFQ2 .GTRETURN }
     *     
     */
    public ZFMCRTRFQ2 .GTRETURN getGTRETURN() {
        return gtreturn;
    }

    /**
     * Sets the value of the gtreturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZFMCRTRFQ2 .GTRETURN }
     *     
     */
    public void setGTRETURN(ZFMCRTRFQ2 .GTRETURN value) {
        this.gtreturn = value;
    }

    /**
     * Gets the value of the gtseqnum property.
     * 
     * @return
     *     possible object is
     *     {@link ZFMCRTRFQ2 .GTSEQNUM }
     *     
     */
    public ZFMCRTRFQ2 .GTSEQNUM getGTSEQNUM() {
        return gtseqnum;
    }

    /**
     * Sets the value of the gtseqnum property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZFMCRTRFQ2 .GTSEQNUM }
     *     
     */
    public void setGTSEQNUM(ZFMCRTRFQ2 .GTSEQNUM value) {
        this.gtseqnum = value;
    }


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
     *         &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}BAPIRET2" maxOccurs="unbounded" minOccurs="0"/>
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
        "item"
    })
    public static class GTRETURN {

        protected List<BAPIRET2> item;

        /**
         * Gets the value of the item property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the item property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getItem().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link BAPIRET2 }
         * 
         * 
         */
        public List<BAPIRET2> getItem() {
            if (item == null) {
                item = new ArrayList<BAPIRET2>();
            }
            return this.item;
        }

    }


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
     *         &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZST_RFQ_SEQ" maxOccurs="unbounded" minOccurs="0"/>
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
        "item"
    })
    public static class GTSEQNUM {

        protected List<ZSTRFQSEQ> item;

        /**
         * Gets the value of the item property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the item property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getItem().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ZSTRFQSEQ }
         * 
         * 
         */
        public List<ZSTRFQSEQ> getItem() {
            if (item == null) {
                item = new ArrayList<ZSTRFQSEQ>();
            }
            return this.item;
        }

    }

}
