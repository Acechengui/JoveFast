package com.jovefast.flowable.listener;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

/**
 * @author Acechengui
 */
public class UserTaskListener implements TaskListener{

    private static final long serialVersionUID = -6251879352067300331L;

    @Override
    public void notify(DelegateTask delegateTask) {

    }

}
