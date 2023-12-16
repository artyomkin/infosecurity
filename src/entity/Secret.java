package entity;

public class Secret {
    private Integer id;
    private String key;
    private String value;
    private Integer namespace_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNamespace_id() {
        return namespace_id;
    }

    public void setNamespace_id(Integer namespace_id) {
        this.namespace_id = namespace_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
