package com.jovefast.modules.monitor.config;

import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.notify.CompositeNotifier;
import de.codecentric.boot.admin.server.notify.Notifier;
import de.codecentric.boot.admin.server.notify.RemindingNotifier;
import de.codecentric.boot.admin.server.notify.filter.FilteringNotifier;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Configuration
public class NotificationConfig {

    private final InstanceRepository instanceRepository;
    private final ObjectProvider<List<Notifier>> provider;

    public NotificationConfig(InstanceRepository instanceRepository, ObjectProvider<List<Notifier>> provider) {
        this.instanceRepository = instanceRepository;
        this.provider = provider;
    }

    @Bean
    public FilteringNotifier filteringNotifier() {
        CompositeNotifier compositeNotifier = new CompositeNotifier(this.provider.getIfAvailable(Collections::emptyList));
        return new FilteringNotifier(compositeNotifier, this.instanceRepository);
    }

    @Bean
    @Primary
    public RemindingNotifier remindingNotifier() {
        RemindingNotifier remindingNotifier = new RemindingNotifier(filteringNotifier(), this.instanceRepository);
        //配置每隔多久提示
        remindingNotifier.setReminderPeriod(Duration.ofMinutes(30));
        //配置每隔多久检查
        remindingNotifier.setCheckReminderInverval(Duration.ofSeconds(120));
        return remindingNotifier;
    }
}
