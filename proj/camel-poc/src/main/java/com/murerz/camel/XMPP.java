package com.murerz.camel;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.xmpp.XmppComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XMPP {

	private static final Logger LOG = LoggerFactory.getLogger(XMPP.class);

	public static interface Poc {

		public String poc();

	}

	public static void main(String[] args) throws Exception {
		LOG.info("init");

		DefaultCamelContext ctx = new DefaultCamelContext();

		ctx.addComponent("xmpp", new XmppComponent());

		ctx.addRoutes(new RouteBuilder() {
			public void configure() {
			    from("xmpp://talk.google.com:5222/pyrata@fuweweu.com?serviceName=fuweweu.com&user=pyrata&password=5t6y7u8i")
			    .setBody(simple("aaa: ${body} :aaa"))
			    .log(LoggingLevel.INFO, "IN: ${body}")
			    .to("xmpp://talk.google.com:5222/fuweweu@gmail.com?serviceName=fuweweu.com&user=pyrata&password=5t6y7u8i");
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
