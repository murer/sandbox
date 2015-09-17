package com.murerz.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.irc.IrcComponent;
import org.apache.camel.component.xmpp.XmppComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Irc2Xmpp {

	private static final Logger LOG = LoggerFactory.getLogger(Irc2Xmpp.class);

	public static interface Poc {

		public String poc();

	}

	public static void main(String[] args) throws Exception {
		LOG.info("init");

		DefaultCamelContext ctx = new DefaultCamelContext();
		ctx.addComponent("irc", new IrcComponent());
		ctx.addComponent("xmpp", new XmppComponent());

		ctx.addRoutes(new RouteBuilder() {
			public void configure() {
				RouteDefinition route = from("irc:pyrata@irc.freenode.net?channels=#mycontainer");
				route.to("log:input?showAll=true");
				route.to("xmpp://talk.google.com:5222/fuweweu@gmail.com?serviceName=fuweweu.com&user=pyrata&password=5t6y7u8i");
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
