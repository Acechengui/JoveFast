package com.jovefast.flowable.listener;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

/**
 * @author Acechengui
 * @version 1.0
 * @description: 表单是否允许编辑流程变量清除
 * @date 2022/12/12 20:27
 */
public class ProcessVariableClearlistener  implements TaskListener {

    private static final long serialVersionUID = -1844982826465464304L;

    @Override
    public void notify(DelegateTask delegateTask) {
        if(delegateTask.hasVariable("whetherWritable")){
            delegateTask.removeVariable("whetherWritable");
        }
    }
}
