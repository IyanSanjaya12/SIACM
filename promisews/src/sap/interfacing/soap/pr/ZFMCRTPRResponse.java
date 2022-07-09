
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
 *         &lt;element name="GV_NUMBER" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GV_RETURN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlRootElement(name = "ZFM_CRT_PR.Response")
public class ZFMCRTPRResponse {

    @XmlElement(name = "GV_NUMBER")
    protected String gvnumber;
    @XmlElement(name = "GV_RETURN")
    protected String gvreturn;
    @XmlElement(name = "GT_ACCOUNTS")
    protected ZFMCRTPRResponse.GTACCOUNTS gtaccounts;
    @XmlElement(name = "GT_ITEM", required = true)
    protected ZFMCRTPRResponse.GTITEM gtitem;

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
     * Gets the value of the gtaccounts property.
     * 
     * @return
     *     possible object is
     *     {@link ZFMCRTPRResponse.GTACCOUNTS }
     *     
     */
    public ZFMCRTPRResponse.GTACCOUNTS getGTACCOUNTS() {
        return gtaccounts;
    }

    /**
     * Sets the value of the gtaccounts property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZFMCRTPRResponse.GTACCOUNTS }
     *     
     */
    public void setGTACCOUNTS(ZFMCRTPRResponse.GTACCOUNTS value) {
        this.gtaccounts = value;
    }

    /**
     * Gets the value of the gtitem property.
     * 
     * @return
     *     possible object is
     *     {@link ZFMCRTPRResponse.GTITEM }
     *     
     */
    public ZFMCRTPRResponse.GTITEM getGTITEM() {
        return gtitem;
    }

    /**
     * Sets the value of the gtitem property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZFMCRTPRResponse.GTITEM }
     *     
     */
    public void setGTITEM(ZFMCRTPRResponse.GTITEM value) {
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
