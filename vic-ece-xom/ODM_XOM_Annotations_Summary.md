# ODM XOM Class Annotations Summary

This document summarizes the annotations added to the XOM classes for use with IBM ODM Rule Designer.

## Annotations Added

Every class has been updated with the following annotations:

1. **Class Level**:
   - `@XmlAccessorType(XmlAccessType.FIELD)` - JAXB annotation for XML binding

2. **Fields**:
   - `@XmlElement` - JAXB annotation for each field to be serialized

3. **Main Constructors**:
   - `@CustomProperty(name = "dataio.default", value = "true")` - ODM annotation to mark the constructor as default for DVS
   - `@BusinessName` - ODM annotation for each parameter to provide business-friendly names

## Updated Classes

The following classes have been fully annotated:

1. **Core Request/Response Classes**:
   - `CalculationRequest.java`
   - `CalculationResponse.java`
   - `EligibilityRequest.java`
   - `EligibilityResponse.java`

2. **Domain Model Classes**:
   - `Person.java` (base class)
   - `Child.java` (extends Person)
   - `Guardian.java` (extends Person)
   - `Family.java`
   - `Service.java`
   - `ServiceProvider.java`
   - `FundingDetermination.java`

## Annotation Pattern

All classes follow this pattern:

```java
@XmlAccessorType(XmlAccessType.FIELD)
public class Example implements Serializable {
    
    @XmlElement
    private String field1;
    
    @XmlElement
    private int field2;
    
    // Default constructor required for JAXB
    public Example() {
    }
    
    // Main constructor with ODM annotations
    @CustomProperty(name = "dataio.default", value = "true")
    public Example(
            @BusinessName("field1") String field1,
            @BusinessName("field2") int field2) {
        this.field1 = field1;
        this.field2 = field2;
    }
    
    // Getters and setters...
}
```

## Important Notes

1. These annotations will work in IBM ODM Rule Designer, which includes the necessary libraries.

2. The annotations serve two main purposes:
   - JAXB annotations enable XML binding for web services
   - ODM annotations improve business rule authoring experience

3. The `Serializable` interface has been maintained for compatibility with IBM ODM 9.x.

4. When updating in Rule Designer, all compilation errors related to missing annotations will be resolved since the environment includes the necessary libraries.
