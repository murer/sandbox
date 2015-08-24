package com.murerz.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.ComponentConfiguration;
import org.apache.camel.Endpoint;
import org.apache.camel.EndpointConfiguration;

public class FooComponent implements Component {

	private CamelContext camelContext;

	public void setCamelContext(CamelContext camelContext) {
		this.camelContext = camelContext;
	}

	public CamelContext getCamelContext() {
		return camelContext;
	}

	public Endpoint createEndpoint(String uri) throws Exception {
		return new FooEndpoint().setUri(uri);
	}

	public boolean useRawUri() {
		return false;
	}

	public EndpointConfiguration createConfiguration(String uri) throws Exception {
		throw new RuntimeException("createConfiguration: " + uri);
	}

	public ComponentConfiguration createComponentConfiguration() {
		throw new RuntimeException("createComponentConfiguration");
	}

}
