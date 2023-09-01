package com.jovefast.flowable.listener;

import org.flowable.common.engine.impl.el.FixedValue;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

/**
 * @Description 表单是否允许编辑流程变量增加
 * @Author Acechengui
 * @Date Created in 2023/2/24
 */
public class ProcessVariableAddlistener  implements TaskListener {

    private static final long serialVersionUID = -7104193455938349438L;

    private FixedValue whetherWritable;

    @Override
    public void notify(DelegateTask delegateTask) {
        if(whetherWritable.getExpressionText().equals("Y")){
            //是否可写 默认应该为不可写,特定流程需要可写
            delegateTask.setVariable("whetherWritable",true);
        }else {
            delegateTask.setVariable("whetherWritable",false);
        }
    }
}
