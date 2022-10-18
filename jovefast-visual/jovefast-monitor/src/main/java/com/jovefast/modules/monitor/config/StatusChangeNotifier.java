package com.jovefast.modules.monitor.config;

import com.alibaba.fastjson2.JSONObject;
import com.jovefast.common.core.qiwx.WeixinApiConfig;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;

/**
 * 通知发送配置
 *
 * @author Acechengui
 */
@Component
public class StatusChangeNotifier extends AbstractStatusChangeNotifier
{
    private final Logger log = LoggerFactory.getLogger(StatusChangeNotifier.class);

    /**
     * 企业微信群机器人KEY
     */
    private final String robotID = "企业微信群机器人KEY";

    /**
     * 消息模板
     */
    private static final String TEMPLATE = "<<<%s>>> \n 【服务名】: %s(%s) \n 【状态】: %s(%s) \n 【服务ip】: %s \n 【详情】: %s";

    private final String titleAlarm = "系统告警";

    private final String titleNotice = "系统通知";

    private final String[] ignoreChanges = new String[]{"UNKNOWN:UP","DOWN:UP","OFFLINE:UP"};

    public StatusChangeNotifier(InstanceRepository repository) {
        super(repository);
    }

    @Override
    protected boolean shouldNotify(InstanceEvent event, Instance instance) {
        if (!(event instanceof InstanceStatusChangedEvent)) {
            return false;
        } else {
            InstanceStatusChangedEvent statusChange = (InstanceStatusChangedEvent)event;
            String from = this.getLastStatus(event.getInstance());
            String to = statusChange.getStatusInfo().getStatus();
            return Arrays.binarySearch(this.ignoreChanges, from + ":" + to) < 0 && Arrays.binarySearch(this.ignoreChanges, "*:" + to) < 0 && Arrays.binarySearch(this.ignoreChanges, from + ":*") < 0;
        }
    }


    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {

        return Mono.fromRunnable(() -> {

            if (event instanceof InstanceStatusChangedEvent) {
                if(instance.getRegistration().getName().contains("prod")){
                    log.info("Instance {} ({}) is {}", instance.getRegistration().getName(),
                            event.getInstance(),
                            ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus());

                    String status = ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus();
                    String messageText = null;
                    switch (status) {
                        // 健康检查没通过
                        case "DOWN":
                            log.info("发送 健康检查没通过 的通知！");
                            messageText = String
                                    .format(TEMPLATE,titleAlarm, instance.getRegistration().getName(), event.getInstance(),
                                            ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus(), "健康检查没通过通知",
                                            instance.getRegistration().getServiceUrl(), JSONObject.toJSONString(instance.getStatusInfo().getDetails()));
                            break;
                        // 服务离线
                        case "OFFLINE":
                            log.info("发送 服务离线 的通知！");
                            messageText = String
                                    .format(TEMPLATE,titleAlarm, instance.getRegistration().getName(), event.getInstance(),
                                            ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus(), "服务离线通知",
                                            instance.getRegistration().getServiceUrl(), JSONObject.toJSONString(instance.getStatusInfo().getDetails()));
                            System.out.println(messageText);
                            break;
                        //服务上线
                        case "UP":
                            log.info("发送 服务上线 的通知！");
                            messageText = String
                                    .format(TEMPLATE,titleNotice, instance.getRegistration().getName(), event.getInstance(),
                                            ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus(), "服务上线通知",
                                            instance.getRegistration().getServiceUrl(), JSONObject.toJSONString(instance.getStatusInfo().getDetails()));
                            break;
                        // 服务未知异常
                        case "UNKNOWN":
                            log.info("发送 服务未知异常 的通知！");
                            messageText = String
                                    .format(TEMPLATE,titleAlarm, instance.getRegistration().getName(), event.getInstance(),
                                            ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus(), "服务未知异常通知",
                                            instance.getRegistration().getServiceUrl(), JSONObject.toJSONString(instance.getStatusInfo().getDetails()));
                            break;
                        default:
                            break;
                    }
                    //向群发送消息
                    if(messageText != null){
                        String text = "{\n" +
                                "    \"msgtype\": \"text\",\n" +
                                "    \"text\": {\n" +
                                "        \"content\": \"" + messageText + "\"" +
                                "}";
                        WeixinApiConfig.rotSendInfo(robotID, text, 0);
                    }
                }
            } else {
                log.info("Instance {} ({}) {}", instance.getRegistration().getName(), event.getInstance(),
                        event.getType());
            }
        });
    }
}