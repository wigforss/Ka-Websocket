//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.04.29 at 11:53:24 AM CEST 
//


package org.kasource.web.websocket.config.xml.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *       &lt;attribute name="provider" use="required" type="{http://kasource.org/schema/websocket}javaClass" />
 *       &lt;attribute name="headerAuthentication" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="usernameKey" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="passwordKey" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "authenticationProvider")
public class AuthenticationProviderXmlConfig {

    @XmlAttribute(required = true)
    protected String provider;
    @XmlAttribute
    protected Boolean headerAuthentication;
    @XmlAttribute
    protected String usernameKey;
    @XmlAttribute
    protected String passwordKey;

    /**
     * Gets the value of the provider property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvider() {
        return provider;
    }

    /**
     * Sets the value of the provider property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvider(String value) {
        this.provider = value;
    }

    /**
     * Gets the value of the headerAuthentication property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isHeaderAuthentication() {
        if (headerAuthentication == null) {
            return false;
        } else {
            return headerAuthentication;
        }
    }

    /**
     * Sets the value of the headerAuthentication property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHeaderAuthentication(Boolean value) {
        this.headerAuthentication = value;
    }

    /**
     * Gets the value of the usernameKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsernameKey() {
        return usernameKey;
    }

    /**
     * Sets the value of the usernameKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsernameKey(String value) {
        this.usernameKey = value;
    }

    /**
     * Gets the value of the passwordKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPasswordKey() {
        return passwordKey;
    }

    /**
     * Sets the value of the passwordKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPasswordKey(String value) {
        this.passwordKey = value;
    }

}
