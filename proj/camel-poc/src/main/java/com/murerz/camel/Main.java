package com.murerz.camel;

import java.io.File;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	private static final Logger LOG = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws Exception {
		LOG.info("init");

		DefaultCamelContext ctx = new DefaultCamelContext();

		ctx.addComponent("http4", new HttpComponent());

		String current = new File(".").getAbsolutePath();
		final String from = "file://" + current + "/target/poc";
		System.out.println("f: " + from);

		// ctx.addRoutes(new RouteBuilder() {
		// public void configure() {
		// RouteDefinition route = from(from);
		// route.to("log:first");
		// route.transform().simple("chec !!! ${in.body} !!! extra data!");
		// route.to("log:second");
		// }
		// });
		//
		// ctx.addRoutes(new RouteBuilder() {
		// public void configure() {
		// RouteDefinition route = from("timer:foo?period=5000");
		// route.to("http4://qunitpage.org/README.md");
		// route.to("file:///tmp/poc/c3");
		// }
		// });

		ctx.addRoutes(new RouteBuilder() {
			public void configure() {
				RouteDefinition route = from(from);
				route.marshal().json(JsonLibrary.Gson);
				route.to("log:target");
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
