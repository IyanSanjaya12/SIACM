
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
 *         &lt;element name="GV_NUMBER" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GV_RETURN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlRootElement(name = "ZFM_CRT_PO.Response")
public class ZFMCRTPOResponse {

    @XmlElement(name = "GV_NUMBER")
    protected String gvnumber;
    @XmlElement(name = "GV_RETURN")
    protected String gvreturn;
    @XmlElement(name = "GT_ITEMS", required = true)
    protected ZFMCRTPOResponse.GTITEMS gtitems;
    @XmlElement(name = "GT_SCHEDULES", required = true)
    protected ZFMCRTPOResponse.GTSCHEDULES gtschedules;

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
     * Gets the value of the gvreturn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGVRETURN() {
        return gvreturn;
    }

    /**
     * Sets the value of the gvreturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGVRETURN(String value) {
        this.gvreturn = value;
    }

    /**
     * Gets the value of the gtitems property.
     * 
     * @return
     *     possible object is
     *     {@link ZFMCRTPOResponse.GTITEMS }
     *     
     */
    public ZFMCRTPOResponse.GTITEMS getGTITEMS() {
        return gtitems;
    }

    /**
     * Sets the value of the gtitems property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZFMCRTPOResponse.GTITEMS }
     *     
     */
    public void setGTITEMS(ZFMCRTPOResponse.GTITEMS value) {
        this.gtitems = value;
    }

    /**
     * Gets the value of the gtschedules property.
     * 
     * @return
     *     possible object is
     *     {@link ZFMCRTPOResponse.GTSCHEDULES }
     *     
     */
    public ZFMCRTPOResponse.GTSCHEDULES getGTSCHEDULES() {
        return gtschedules;
    }

    /**
     * Sets the value of the gtschedules property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZFMCRTPOResponse.GTSCHEDULES }
     *     
     */
    public void setGTSCHEDULES(ZFMCRTPOResponse.GTSCHEDULES value) {
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
