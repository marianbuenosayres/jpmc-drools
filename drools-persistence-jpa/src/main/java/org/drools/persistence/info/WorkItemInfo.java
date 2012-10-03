package org.drools.persistence.info;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.drools.common.InternalRuleBase;
import org.drools.common.Scheduler.ActivationTimerInputMarshaller;
import org.drools.marshalling.impl.InputMarshaller;
import org.drools.marshalling.impl.MarshallerReaderContext;
import org.drools.marshalling.impl.MarshallerWriteContext;
import org.drools.marshalling.impl.OutputMarshaller;
import org.drools.marshalling.impl.PersisterEnums;
import org.drools.marshalling.impl.TimersInputMarshaller;
import org.drools.process.instance.WorkItem;
import org.drools.reteoo.ObjectTypeNode.ExpireJobContextTimerInputMarshaller;
import org.drools.rule.SlidingTimeWindow.BehaviorJobContextTimerInputMarshaller;
import org.drools.runtime.Environment;

@Entity
@SequenceGenerator(name="workItemInfoIdSeq", sequenceName="WORKITEMINFO_ID_SEQ")
public class WorkItemInfo implements Serializable {
    
    private static final long serialVersionUID = 540417l;
   
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="workItemInfoIdSeq")
    private Long   workItemId;

    @Version
    @Column(name = "OPTLOCK")
    private int    version;

    private String name;
    private Date   creationDate;
    private long   processInstanceId;
    private long   state;
    
    @Lob
    @Column(length=2147483647)
    private byte[] workItemByteArray;
    
    private @Transient
    WorkItem       workItem;

    private @Transient
    transient Environment                       env;
    
    protected WorkItemInfo() {
    }

    public WorkItemInfo(WorkItem workItem, Environment env) {
        this.workItem = workItem;
        this.name = workItem.getName();
        this.creationDate = new Date();
        this.processInstanceId = workItem.getProcessInstanceId();
        this.env = env;
    }

    public Long getId() {
        return workItemId;
    }
    
    public int getVersion() {
        return this.version;
    }

    public String getName() {
        return name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public long getProcessInstanceId() {
        return processInstanceId;
    }

    public long getState() {
        return state;
    }
    
    public byte [] getWorkItemByteArray() { 
       return workItemByteArray;
    }
    
    public WorkItem getWorkItem(Environment env, InternalRuleBase ruleBase) {
        this.env = env;
        if ( workItem == null ) {
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream( workItemByteArray );
                MarshallerReaderContext context = new MarshallerReaderContext( bais,
                                                                               ruleBase,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               env);
                workItem = InputMarshaller.readWorkItem( context );
                context.close();
            } catch ( IOException e ) {
                e.printStackTrace();
                throw new IllegalArgumentException( "IOException while loading process instance: " + e.getMessage() );
            }
        }
        return workItem;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (processInstanceId ^ (processInstanceId >>> 32));
		result = prime * result + (int) (state ^ (state >>> 32));
		result = prime * result + version;
		result = prime * result + ((workItem == null) ? 0 : workItem.hashCode());
		result = prime * result + Arrays.hashCode(workItemByteArray);
		result = prime * result + ((workItemId == null) ? 0 : workItemId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		WorkItemInfo other = (WorkItemInfo) obj;
		if (creationDate == null) {
			if (other.creationDate != null) return false;
		} else if (!creationDate.equals(other.creationDate)) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		if (processInstanceId != other.processInstanceId) return false;
		if (state != other.state) return false;
		if (version != other.version) return false;
		if (workItem == null) {
			if (other.workItem != null) return false;
		} else if (!workItem.equals(other.workItem)) return false;
		if (!Arrays.equals(workItemByteArray, other.workItemByteArray)) return false;
		if (workItemId == null) {
			if (other.workItemId != null) return false;
		} else if (!workItemId.equals(other.workItemId)) return false;
		return true;
	}

	@PreUpdate
    public void update() {
        this.state = workItem.getState();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            MarshallerWriteContext context = new MarshallerWriteContext( baos,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         this.env);
            
            OutputMarshaller.writeWorkItem( context,
                                                 workItem );

            context.close();
            this.workItemByteArray = baos.toByteArray();
        } catch ( IOException e ) {
            throw new IllegalArgumentException( "IOException while storing workItem " + workItem.getId() + ": " + e.getMessage() );
        }
    }
    
    public void setId(Long id){
        this.workItemId = id;
    }
}
