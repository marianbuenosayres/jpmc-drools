package org.drools.persistence.info;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.drools.persistence.SessionMarshallingHelper;

@Entity
@SequenceGenerator(name="sessionInfoIdSeq", sequenceName="SESSIONINFO_ID_SEQ")
public class SessionInfo implements Serializable {
    
    private static final long serialVersionUID = 540417l;

    private @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="sessionInfoIdSeq")
    Integer                        id;

    @Version
    @Column(name = "OPTLOCK")     
    private int                version;

    private Date               startDate;
    private Date               lastModificationDate;
    
    @Lob
    @Column(length=2147483647)
    private byte[]             rulesByteArray;

    @Transient
    transient SessionMarshallingHelper helper;
    
    public SessionInfo() {
        this.startDate = new Date();
    }

    public Integer getId() {
        return this.id;
    }
    
    public int getVersion() {
        return this.version;
    }

    public void setJPASessionMashallingHelper(SessionMarshallingHelper helper) {
        this.helper = helper;
    }

    public SessionMarshallingHelper getJPASessionMashallingHelper() {
        return helper;
    }
    
    public void setData( byte[] data) {
        this.rulesByteArray = data;
    }
    
    public byte[] getData() {
        return this.rulesByteArray;
    }
    
    public Date getStartDate() {
        return this.startDate;
    }

    public Date getLastModificationDate() {
        return this.lastModificationDate;
    }

    public void setLastModificationDate(Date date) {
        this.lastModificationDate = date;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastModificationDate == null) ? 0 : lastModificationDate.hashCode());
		result = prime * result + Arrays.hashCode(rulesByteArray);
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + version;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		SessionInfo other = (SessionInfo) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		if (lastModificationDate == null) {
			if (other.lastModificationDate != null) return false;
		} else if (!lastModificationDate.equals(other.lastModificationDate)) return false;
		if (!Arrays.equals(rulesByteArray, other.rulesByteArray)) return false;
		if (startDate == null) {
			if (other.startDate != null) return false;
		} else if (!startDate.equals(other.startDate)) return false;
		if (version != other.version) return false;
		return true;
	}

	@PrePersist 
    @PreUpdate 
    public void update() {
        this.rulesByteArray  = this.helper.getSnapshot();
    }

    public void setId(Integer ksessionId) {
        this.id = ksessionId;
    }

}
