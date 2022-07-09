
package sap.interfacing.soap.po;

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
 *         &lt;element name="GV_CURRENCY" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GV_NOMORKONTRAK" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="GV_PURCHORG">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GV_PUR_GROUP" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GV_TGLKONTRAK" type="{urn:sap-com:document:sap:rfc:functions}date"/>
 *         &lt;element name="GV_VENDOR">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GT_ITEMS">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZST_PO_ITEM" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="GT_SCHEDULES">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZST_PO_SCHEDULES" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlRootElement(name = "ZFM_CRT_PO")
public class ZFMCRTPO {

    @XmlElement(name = "GV_CURRENCY")
    protected String gvcurrency;
    @XmlElement(name = "GV_NOMORKONTRAK", required = true)
    protected String gvnomorkontrak;
    @XmlElement(name = "GV_PURCHORG", required = true)
    protected String gvpurchorg;
    @XmlElement(name = "GV_PUR_GROUP")
    protected String gvpurgroup;
    @XmlElement(name = "GV_TGLKONTRAK", required = true)
    protected String gvtglkontrak;
    @XmlElement(name = "GV_VENDOR", required = true)
    protected String gvvendor;
    @XmlElement(name = "GT_ITEMS", required = true)
    protected ZFMCRTPO.GTITEMS gtitems;
    @XmlElement(name = "GT_SCHEDULES", required = true)
    protected ZFMCRTPO.GTSCHEDULES gtschedules;

    /**
     * Gets the value of the gvcurrency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGVCURRENCY() {
        return gvcurrency;
    }

    /**
     * Sets the value of the gvcurrency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGVCURRENCY(String value) {
        this.gvcurrency = value;
    }

    /**
     * Gets the value of the gvnomorkontrak property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGVNOMORKONTRAK() {
        return gvnomorkontrak;
    }

    /**
     * Sets the value of the gvnomorkontrak property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGVNOMORKONTRAK(String value) {
        this.gvnomorkontrak = value;
    }

    /**
     * Gets the value of the gvpurchorg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGVPURCHORG() {
        return gvpurchorg;
    }

    /**
     * Sets the value of the gvpurchorg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGVPURCHORG(String value) {
        this.gvpurchorg = value;
    }

    /**
     * Gets the value of the gvpurgroup property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGVPURGROUP() {
        return gvpurgroup;
    }

    /**
     * Sets the value of the gvpurgroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGVPURGROUP(String value) {
        this.gvpurgroup = value;
    }

    /**
     * Gets the value of the gvtglkontrak property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGVTGLKONTRAK() {
        return gvtglkontrak;
    }

    /**
     * Sets the value of the gvtglkontrak property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGVTGLKONTRAK(String value) {
        this.gvtglkontrak = value;
    }

    /**
     * Gets the value of the gvvendor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGVVENDOR() {
        return gvvendor;
    }

    /**
     * Sets the value of the gvvendor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGVVENDOR(String value) {
        this.gvvendor = value;
    }

    /**
     * Gets the value of the gtitems property.
     * 
     * @return
     *     possible object is
     *     {@link ZFMCRTPO.GTITEMS }
     *     
     */
    public ZFMCRTPO.GTITEMS getGTITEMS() {
        return gtitems;
    }

    /**
     * Sets the value of the gtitems property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZFMCRTPO.GTITEMS }
     *     
     */
    public void setGTITEMS(ZFMCRTPO.GTITEMS value) {
        this.gtitems = value;
    }

    /**
     * Gets the value of the gtschedules property.
     * 
     * @return
     *     possible object is
     *     {@link ZFMCRTPO.GTSCHEDULES }
     *     
     */
    public ZFMCRTPO.GTSCHEDULES getGTSCHEDULES() {
        return gtschedules;
    }

    /**
     * Sets the value of the gtschedules property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZFMCRTPO.GTSCHEDULES }
     *     
     */
    public void setGTSCHEDULES(ZFMCRTPO.GTSCHEDULES value) {
        this.gtschedules = value;
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
     *         &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZST_PO_ITEM" maxOccurs="unbounded" minOccurs="0"/>
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
    public static class GTITEMS {

        protected List<ZSTPOITEM> item;

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
         * {@link ZSTPOITEM }
         * 
         * 
         */
        public List<ZSTPOITEM> getItem() {
            if (item == null) {
                item = new ArrayList<ZSTPOITEM>();
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
     *         &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZST_PO_SCHEDULES" maxOccurs="unbounded" minOccurs="0"/>
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
    public static class GTSCHEDULES {

        protected List<ZSTPOSCHEDULES> item;

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
         * {@link ZSTPOSCHEDULES }
         * 
         * 
         */
        public List<ZSTPOSCHEDULES> getItem() {
            if (item == null) {
                item = new ArrayList<ZSTPOSCHEDULES>();
            }
            return this.item;
        }

    }

}
