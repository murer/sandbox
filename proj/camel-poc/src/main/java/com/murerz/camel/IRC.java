package com.murerz.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.irc.IrcComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IRC {

	private static final Logger LOG = LoggerFactory.getLogger(IRC.class);

	public static interface Poc {

		public String poc();

	}

	public static void main(String[] args) throws Exception {
		LOG.info("init");

		DefaultCamelContext ctx = new DefaultCamelContext();
		ctx.addComponent("irc", new IrcComponent());

		ctx.addRoutes(new RouteBuilder() {
			public void configure() {
				RouteDefinition route = from("irc:pyrata@irc.freenode.net?channels=#mycontainer");
				route.to("log:input?showAll=true");
				route.to("irc:pyrata@irc.freenode.net?channels=#mycontainer");
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
