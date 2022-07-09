
package sap.interfacing.soap.pr;

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
 *         &lt;element name="GV_ATTACHMENT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GV_DOCTYPE" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GV_HEADERNOTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GV_INTERMSOF" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GV_REQUISITIONER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GV_TESTRUN" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GT_ACCOUNTS" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZST_PR_ACC" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="GT_ITEM">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZST_PR_ITEM" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlRootElement(name = "ZFM_CRT_PR")
public class ZFMCRTPR {

    @XmlElement(name = "GV_ATTACHMENT")
    protected String gvattachment;
    @XmlElement(name = "GV_DOCTYPE")
    protected String gvdoctype;
    @XmlElement(name = "GV_HEADERNOTE")
    protected String gvheadernote;
    @XmlElement(name = "GV_INTERMSOF")
    protected String gvintermsof;
    @XmlElement(name = "GV_REQUISITIONER")
    protected String gvrequisitioner;
    @XmlElement(name = "GV_TESTRUN")
    protected String gvtestrun;
    @XmlElement(name = "GT_ACCOUNTS")
    protected ZFMCRTPR.GTACCOUNTS gtaccounts;
    @XmlElement(name = "GT_ITEM", required = true)
    protected ZFMCRTPR.GTITEM gtitem;

    /**
     * Gets the value of the gvattachment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGVATTACHMENT() {
        return gvattachment;
    }

    /**
     * Sets the value of the gvattachment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGVATTACHMENT(String value) {
        this.gvattachment = value;
    }

    /**
     * Gets the value of the gvdoctype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGVDOCTYPE() {
        return gvdoctype;
    }

    /**
     * Sets the value of the gvdoctype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGVDOCTYPE(String value) {
        this.gvdoctype = value;
    }

    /**
     * Gets the value of the gvheadernote property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGVHEADERNOTE() {
        return gvheadernote;
    }

    /**
     * Sets the value of the gvheadernote property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGVHEADERNOTE(String value) {
        this.gvheadernote = value;
    }

    /**
     * Gets the value of the gvintermsof property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGVINTERMSOF() {
        return gvintermsof;
    }

    /**
     * Sets the value of the gvintermsof property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGVINTERMSOF(String value) {
        this.gvintermsof = value;
    }

    /**
     * Gets the value of the gvrequisitioner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGVREQUISITIONER() {
        return gvrequisitioner;
    }

    /**
     * Sets the value of the gvrequisitioner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGVREQUISITIONER(String value) {
        this.gvrequisitioner = value;
    }

    /**
     * Gets the value of the gvtestrun property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGVTESTRUN() {
        return gvtestrun;
    }

    /**
     * Sets the value of the gvtestrun property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGVTESTRUN(String value) {
        this.gvtestrun = value;
    }

    /**
     * Gets the value of the gtaccounts property.
     * 
     * @return
     *     possible object is
     *     {@link ZFMCRTPR.GTACCOUNTS }
     *     
     */
    public ZFMCRTPR.GTACCOUNTS getGTACCOUNTS() {
        return gtaccounts;
    }

    /**
     * Sets the value of the gtaccounts property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZFMCRTPR.GTACCOUNTS }
     *     
     */
    public void setGTACCOUNTS(ZFMCRTPR.GTACCOUNTS value) {
        this.gtaccounts = value;
    }

    /**
     * Gets the value of the gtitem property.
     * 
     * @return
     *     possible object is
     *     {@link ZFMCRTPR.GTITEM }
     *     
     */
    public ZFMCRTPR.GTITEM getGTITEM() {
        return gtitem;
    }

    /**
     * Sets the value of the gtitem property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZFMCRTPR.GTITEM }
     *     
     */
    public void setGTITEM(ZFMCRTPR.GTITEM value) {
        this.gtitem = value;
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
     *         &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZST_PR_ACC" maxOccurs="unbounded" minOccurs="0"/>
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
    public static class GTACCOUNTS {

        protected List<ZSTPRACC> item;

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
         * {@link ZSTPRACC }
         * 
         * 
         */
        public List<ZSTPRACC> getItem() {
            if (item == null) {
                item = new ArrayList<ZSTPRACC>();
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
     *         &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZST_PR_ITEM" maxOccurs="unbounded" minOccurs="0"/>
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
    public static class GTITEM {

        protected List<ZSTPRITEM> item;

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
         * {@link ZSTPRITEM }
         * 
         * 
         */
        public List<ZSTPRITEM> getItem() {
            if (item == null) {
                item = new ArrayList<ZSTPRITEM>();
            }
            return this.item;
        }

    }

}
