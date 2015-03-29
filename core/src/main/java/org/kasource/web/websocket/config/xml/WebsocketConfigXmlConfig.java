//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.14 at 02:25:54 PM CET 
//


package org.kasource.web.websocket.config.xml;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *       &lt;sequence>
 *         &lt;element ref="{http://kasource.org/schema/websocket}websocket" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://kasource.org/schema/websocket}orginWhitelist" minOccurs="0"/>
 *         &lt;element ref="{http://kasource.org/schema/websocket}clientIdGenerator" minOccurs="0"/>
 *         &lt;element ref="{http://kasource.org/schema/websocket}authenticationProvider" minOccurs="0"/>
 *         &lt;element ref="{http://kasource.org/schema/websocket}textProtocolHandler" minOccurs="0"/>
 *         &lt;element ref="{http://kasource.org/schema/websocket}binaryProtocolHandler" minOccurs="0"/>
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
    "websocket",
    "originWhitelist",
    "clientIdGenerator",
    "authenticationProvider",
    "textProtocolHandler",
    "binaryProtocolHandler"
})
@XmlRootElement(name = "websocket-config")
public class WebsocketConfigXmlConfig {

    protected List<WebsocketXmlConfig> websocket;
    protected OriginWhitelistXmlConfig originWhitelist;
    protected ClientIdGeneratorXmlConfig clientIdGenerator;
    protected AuthenticationProviderXmlConfig authenticationProvider;
    protected ProtocolHandlerXmlConfig textProtocolHandler;
    protected ProtocolHandlerXmlConfig binaryProtocolHandler;

    /**
     * Gets the value of the websocket property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the websocket property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWebsocket().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WebsocketXmlConfig }
     * 
     * 
     */
    public List<WebsocketXmlConfig> getWebsocket() {
        if (websocket == null) {
            websocket = new ArrayList<WebsocketXmlConfig>();
        }
        return this.websocket;
    }

    /**
     * Gets the value of the orginWhitelist property.
     * 
     * @return
     *     possible object is
     *     {@link OrginWhitelist }
     *     
     */
    public OriginWhitelistXmlConfig getOriginWhitelist() {
        return originWhitelist;
    }

    /**
     * Sets the value of the orginWhitelist property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrginWhitelist }
     *     
     */
    public void setOriginWhitelist(OriginWhitelistXmlConfig value) {
        this.originWhitelist = value;
    }

    /**
     * Gets the value of the clientIdGenerator property.
     * 
     * @return
     *     possible object is
     *     {@link ClientIdGeneratorXmlConfig }
     *     
     */
    public ClientIdGeneratorXmlConfig getClientIdGenerator() {
        return clientIdGenerator;
    }

    /**
     * Sets the value of the clientIdGenerator property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClientIdGeneratorXmlConfig }
     *     
     */
    public void setClientIdGenerator(ClientIdGeneratorXmlConfig value) {
        this.clientIdGenerator = value;
    }

    /**
     * Gets the value of the authenticationProvider property.
     * 
     * @return
     *     possible object is
     *     {@link AuthenticationProviderXmlConfig }
     *     
     */
    public AuthenticationProviderXmlConfig getAuthenticationProvider() {
        return authenticationProvider;
    }

    /**
     * Sets the value of the authenticationProvider property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthenticationProviderXmlConfig }
     *     
     */
    public void setAuthenticationProvider(AuthenticationProviderXmlConfig value) {
        this.authenticationProvider = value;
    }

    /**
     * Gets the value of the textProtocolHandler property.
     * 
     * @return
     *     possible object is
     *     {@link ProtocolHandlerXmlConfig }
     *     
     */
    public ProtocolHandlerXmlConfig getTextProtocolHandler() {
        return textProtocolHandler;
    }

    /**
     * Sets the value of the textProtocolHandler property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProtocolHandlerXmlConfig }
     *     
     */
    public void setTextProtocolHandler(ProtocolHandlerXmlConfig value) {
        this.textProtocolHandler = value;
    }

    /**
     * Gets the value of the binaryProtocolHandler property.
     * 
     * @return
     *     possible object is
     *     {@link ProtocolHandlerXmlConfig }
     *     
     */
    public ProtocolHandlerXmlConfig getBinaryProtocolHandler() {
        return binaryProtocolHandler;
    }

    /**
     * Sets the value of the binaryProtocolHandler property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProtocolHandlerXmlConfig }
     *     
     */
    public void setBinaryProtocolHandler(ProtocolHandlerXmlConfig value) {
        this.binaryProtocolHandler = value;
    }

}