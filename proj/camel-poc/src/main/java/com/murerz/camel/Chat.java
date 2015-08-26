package com.murerz.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.websocket.WebsocketComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Chat {

	private static final Logger LOG = LoggerFactory.getLogger(Chat.class);

	public static interface Poc {

		public String poc();

	}

	public static void main(String[] args) throws Exception {
		LOG.info("init");

		DefaultCamelContext ctx = new DefaultCamelContext();
		ctx.addComponent("websocket", new WebsocketComponent());

		ctx.addRoutes(new RouteBuilder() {
			public void configure() {
				RouteDefinition route = from("websocket://0.0.0.0:5006/");
				route.to("log:input?showAll=true");
				route.to("websocket://0.0.0.0:5006/?sendToAll=true");
			}
		});

		try {
			LOG.info("Starting");
			ctx.start();
			System.in.read();
		} finally {
			try {
				LOG.info("Stopping");
				ctx.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
