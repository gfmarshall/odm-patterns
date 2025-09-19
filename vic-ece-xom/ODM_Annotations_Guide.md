# ODM XOM Annotations Guide

This guide documents the key annotations needed for proper ODM XOM integration, particularly for ODM 9.x.

## Essential Annotations

### JAXB Annotations
These annotations enable XML binding for web services and decision services:

- `@XmlAccessorType(XmlAccessType.FIELD)` - Class-level annotation to specify how JAXB accesses the fields
- `@XmlElement` - Field-level annotation to mark fields for XML serialization
- `@XmlTransient` - Exclude a field from XML serialization

### ODM BOM Annotations
These annotations control how the XOM classes are exposed in the BOM:

- `@BusinessName("name")` - Provide a business-friendly name for parameters or methods
- `@CustomProperty(name = "dataio.default", value = "true")` - Mark a constructor as the default for DVS
- `@CustomProperty(name = "factory.ignore", value = "true")` - Exclude a method from the BOM

## Best Practices

1. **Implement both approaches**:
   - Implement `Serializable` for Java-based persistence and state management
   - Use JAXB annotations for XML binding when exposing rules through web services

2. **Constructor annotations**:
   - Use `@BusinessName` for constructor parameters
   - Use `@CustomProperty(name = "dataio.default", value = "true")` on the primary constructor

3. **Documentation**:
   - Always include proper JavaDoc on classes and methods
   - Use `@param`, `@return` tags in method documentation

## Example Usage

```java
@XmlAccessorType(XmlAccessType.FIELD)
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @XmlElement
    private String name;
    
    @XmlElement
    private int age;
    
    // Transient fields won't be serialized
    @XmlTransient
    private transient List<String> temporaryData;
    
    public Customer() {
        // Default constructor
    }
    
    @CustomProperty(name = "dataio.default", value = "true")
    public Customer(@BusinessName("customerName") String name, 
                   @BusinessName("customerAge") int age) {
        this.name = name;
        this.age = age;
    }
    
    // Method to ignore in BOM
    @CustomProperty(name = "factory.ignore", value = "true")
    public void internalProcessing() {
        // Implementation
    }
}
```

## Important Notes for ODM 9.x

1. The Serializable interface is recommended for all XOM classes when using RES or Decision Services.
2. When updating existing XOM classes, be careful not to introduce breaking changes to rule execution.
3. In Rule Designer, these dependencies will be automatically available.
