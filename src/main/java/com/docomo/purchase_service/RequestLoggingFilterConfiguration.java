package com.docomo.purchase_service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@SuppressWarnings("unused")
@Slf4j
@Configuration
public class RequestLoggingFilterConfiguration {

    @Bean
    public Filter requestLoggingFilter() {
        AbstractRequestLoggingFilter filter = new InfoLevelRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        return filter;
    }

    class InfoLevelRequestLoggingFilter extends AbstractRequestLoggingFilter {
        private static final String UUID_KEY = "UUID";

        protected boolean shouldLog(HttpServletRequest request) {
            return this.logger.isInfoEnabled();
        }

        @Override
        protected void beforeRequest(HttpServletRequest httpServletRequest, String s) {
            String uuid = UUID.randomUUID().toString();
            MDC.put(UUID_KEY, uuid);
            log.info("[{}] {}", uuid, s);
        }

        @Override
        protected void afterRequest(HttpServletRequest httpServletRequest, String s) {
            String uuid = MDC.get(UUID_KEY);
            MDC.remove(UUID_KEY);
            log.info("[{}] {}", uuid, s);
        }
    }
}
