package com.murerz.camel;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.EndpointConfiguration;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.PollingConsumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FooEndpoint implements Endpoint {

	private static final Logger LOG = LoggerFactory.getLogger(FooEndpoint.class);

	private String uri;

	public boolean isSingleton() {
		return false;
	}

	public void start() throws Exception {
		LOG.info("started");
	}

	public void stop() throws Exception {
		LOG.info("stoped");
	}

	public String getEndpointUri() {
		return uri;
	}

	public EndpointConfiguration getEndpointConfiguration() {
		throw new RuntimeException("getEndpointConfiguration");
	}

	public String getEndpointKey() {
		throw new RuntimeException("getEndpointKey");
	}

	public Exchange createExchange() {
		throw new RuntimeException("createExchange");
	}

	public Exchange createExchange(ExchangePattern pattern) {
		throw new RuntimeException("createExchange");
	}

	public Exchange createExchange(Exchange exchange) {
		throw new RuntimeException("createExchange");
	}

	public CamelContext getCamelContext() {
		throw new RuntimeException("getCamelContext");
	}

	public Producer createProducer() throws Exception {
		throw new RuntimeException("createProducer");
	}

	public Consumer createConsumer(Processor processor) throws Exception {
		throw new RuntimeException("createConsumer");
	}

	public PollingConsumer createPollingConsumer() throws Exception {
		throw new RuntimeException("createPollingConsumer");
	}

	public void configureProperties(Map<String, Object> options) {
		throw new RuntimeException("configureProperties");
	}

	public void setCamelContext(CamelContext context) {
		throw new RuntimeException("setCamelContext");
	}

	public boolean isLenientProperties() {
		throw new RuntimeException("isLenientProperties");
	}

	public Endpoint setUri(String uri) {
		this.uri = uri;
		return this;
	}

}
