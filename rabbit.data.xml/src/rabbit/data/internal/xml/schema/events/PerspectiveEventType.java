//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.04.19 at 09:19:36 PM NZST 
//


package rabbit.data.internal.xml.schema.events;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for perspectiveEventType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="perspectiveEventType">
 *   &lt;complexContent>
 *     &lt;extension base="{}durationEventType">
 *       &lt;attribute name="perspectiveId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "perspectiveEventType")
public class PerspectiveEventType
    extends DurationEventType
{

    @XmlAttribute(required = true)
    protected String perspectiveId;

    /**
     * Gets the value of the perspectiveId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPerspectiveId() {
        return perspectiveId;
    }

    /**
     * Sets the value of the perspectiveId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPerspectiveId(String value) {
        this.perspectiveId = value;
    }

}
