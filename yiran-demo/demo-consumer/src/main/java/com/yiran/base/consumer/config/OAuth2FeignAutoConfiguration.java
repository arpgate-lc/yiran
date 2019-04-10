package com.yiran.base.consumer.config;

import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

import feign.RequestInterceptor;

@Configuration
public class OAuth2FeignAutoConfiguration {

	@Bean
	public RequestInterceptor oauth2FeignRequestInterceptor(OAuth2ClientContext oAuth2ClientContext,
			OAuth2ProtectedResourceDetails resource) {
		return new OAuth2FeignRequestInterceptor(oAuth2ClientContext, resource);
	}
}
