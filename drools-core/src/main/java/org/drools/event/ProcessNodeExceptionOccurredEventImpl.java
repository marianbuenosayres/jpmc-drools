package org.drools.event;

import org.drools.WorkingMemory;
import org.drools.event.process.ProcessNodeExceptionOccurredEvent;
import org.drools.runtime.KnowledgeRuntime;
import org.drools.runtime.process.NodeInstance;

/**
 * 
 * @author JBoss
 * @author nicolas.loriente
 *
 */
public class ProcessNodeExceptionOccurredEventImpl extends ProcessEvent implements ProcessNodeExceptionOccurredEvent {

    private Throwable error;
    private final NodeInstance nodeInstance;

    public ProcessNodeExceptionOccurredEventImpl(final NodeInstance nodeInstance, KnowledgeRuntime kruntime, Throwable error) {
        super( nodeInstance.getProcessInstance(), kruntime );
        this.nodeInstance = nodeInstance;
        this.error = error;
    }

    public Throwable getError() {
        return this.error;
    }

    @Override
    public NodeInstance getNodeInstance() {
        return this.nodeInstance;
    }

    @Override
    public String toString() {
            return "==>[ProcessNodeExceptionOccurredEventImpl: getNodeInstance()=" + getNodeInstance() + ", getProcessInstance()="
                            + getProcessInstance() + ", getKnowledgeRuntime()=" + getKnowledgeRuntime() + ", getError()= "+ getError() +"]";
    }
}
