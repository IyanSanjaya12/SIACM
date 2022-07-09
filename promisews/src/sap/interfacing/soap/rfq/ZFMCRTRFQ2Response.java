
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
 *         &lt;element name="GV_RETURN_TEXT" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="73"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GV_RFQNUM" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
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
@XmlRootElement(name = "ZFM_CRT_RFQ_2.Response")
public class ZFMCRTRFQ2Response {

    @XmlElement(name = "GV_RETURN_TEXT")
    protected String gvreturntext;
    @XmlElement(name = "GV_RFQNUM")
    protected String gvrfqnum;
    @XmlElement(name = "GT_RETURN", required = true)
    protected ZFMCRTRFQ2Response.GTRETURN gtreturn;
    @XmlElement(name = "GT_SEQ_NUM", required = true)
    protected ZFMCRTRFQ2Response.GTSEQNUM gtseqnum;

    /**
     * Gets the value of the gvreturntext property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGVRETURNTEXT() {
        return gvreturntext;
    }

    /**
     * Sets the value of the gvreturntext property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGVRETURNTEXT(String value) {
        this.gvreturntext = value;
    }

    /**
     * Gets the value of the gvrfqnum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGVRFQNUM() {
        return gvrfqnum;
    }

    /**
     * Sets the value of the gvrfqnum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGVRFQNUM(String value) {
        this.gvrfqnum = value;
    }

    /**
     * Gets the value of the gtreturn property.
     * 
     * @return
     *     possible object is
     *     {@link ZFMCRTRFQ2Response.GTRETURN }
     *     
     */
    public ZFMCRTRFQ2Response.GTRETURN getGTRETURN() {
        return gtreturn;
    }

    /**
     * Sets the value of the gtreturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZFMCRTRFQ2Response.GTRETURN }
     *     
     */
    public void setGTRETURN(ZFMCRTRFQ2Response.GTRETURN value) {
        this.gtreturn = value;
    }

    /**
     * Gets the value of the gtseqnum property.
     * 
     * @return
     *     possible object is
     *     {@link ZFMCRTRFQ2Response.GTSEQNUM }
     *     
     */
    public ZFMCRTRFQ2Response.GTSEQNUM getGTSEQNUM() {
        return gtseqnum;
    }

    /**
     * Sets the value of the gtseqnum property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZFMCRTRFQ2Response.GTSEQNUM }
     *     
     */
    public void setGTSEQNUM(ZFMCRTRFQ2Response.GTSEQNUM value) {
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
