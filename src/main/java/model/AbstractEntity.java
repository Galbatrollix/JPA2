package model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

@MappedSuperclass
public class AbstractEntity {
    @Id
    @GeneratedValue()
    private long id;

    @Version
    private long version;

    public long getVersion() {
        return version;
    }
}
