/* 
 * BaseEntity.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package edu.tamu.framework.model;

import java.util.Objects;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Deprecated
public abstract class BaseEntity extends ValidatingBase implements Comparable<BaseEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        // if we're the same entity type
        if (obj != null && obj.getClass().equals(this.getClass())) {
            // and we have the same Id
            Long objId = ((BaseEntity) obj).getId();
            if (objId != null) {
                return objId.equals(this.getId());
            } else {
                return objId == this.getId();
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public int compareTo(BaseEntity o) {
        return this.getId().compareTo(o.getId());
    }

}
