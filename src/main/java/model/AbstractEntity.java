package model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

@MappedSuperclass
public abstract class AbstractEntity {
    @Id
    @GeneratedValue()
    protected long id;

    @Version
    protected long version;

    public long getVersion() {
        return version;
    }

    public long getId() {
        return id;
    }
}
