package com.murerz.camel;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpComponent;
import org.apache.camel.component.jetty8.JettyHttpComponent8;
import org.apache.camel.component.websocket.WebsocketComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	private static final Logger LOG = LoggerFactory.getLogger(Main.class);

	public static interface Poc {

		public String poc();

	}

	public static void main(String[] args) throws Exception {
		LOG.info("init");

		DefaultCamelContext ctx = new DefaultCamelContext();

		ctx.addComponent("http4", new HttpComponent());
		ctx.addComponent("jetty", new JettyHttpComponent8());
		ctx.addComponent("websocket", new WebsocketComponent());

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

		// ctx.addRoutes(new RouteBuilder() {
		// public void configure() {
		// RouteDefinition route = from(from);
		// route.marshal().json(JsonLibrary.Gson);
		// route.to("log:target");
		// }
		// });

		// ctx.addRoutes(new RouteBuilder() {
		// public void configure() {
		// RouteDefinition route =
		// from("jetty:http://localhost:5005/app?matchOnUriPrefix=true");
		// route.to("log:input?showAll=true");
		// route.unmarshal().json(JsonLibrary.Gson);
		// route.to("log:parsed?showAll=true");
		// route.process(new Processor() {
		//
		// public void process(Exchange exchange) throws Exception {
		// Map body = exchange.getIn().getBody(Map.class);
		// System.out.println("XXX: " + body);
		// HttpServletResponse resp =
		// exchange.getIn().getBody(HttpServletResponse.class);
		// resp.addHeader("X-TestHeader", "test value");
		// exchange.getOut().setBody(body);
		// }
		// });
		// route.to("log:result?showAll=true");
		// route.process(new Processor() {
		//
		// public void process(Exchange exchange) throws Exception {
		// exchange.getOut().setBody("fake!!!");
		// }
		// });
		// route.to("log:fake?showAll=true");
		// route.routingSlip().method(new Poc() {
		// public String poc() {
		// return "log:final?showAll=true";
		// }
		// }, "poc");
		// }
		// });

		ctx.addRoutes(new RouteBuilder() {
			public void configure() {
				RouteDefinition route = from("websocket://localhost:5006/");
				route.to("log:input?showAll=true");
				route.to("websocket://localhost:5006/");
				route.unmarshal().json(JsonLibrary.Gson);
				route.to("log:parsed?showAll=true");
				route.process(new Processor() {

					public void process(Exchange exchange) throws Exception {
						Map body = exchange.getIn().getBody(Map.class);
						System.out.println("XXX: " + body);
						HttpServletResponse resp = exchange.getIn().getBody(HttpServletResponse.class);
						// resp.addHeader("X-TestHeader", "test value");
						exchange.getOut().setBody(body);
					}
				});
				route.to("log:result?showAll=true");
				route.process(new Processor() {

					public void process(Exchange exchange) throws Exception {
						exchange.getOut().setBody("fake!!!");
					}
				});
				route.to("log:fake?showAll=true");
				route.routingSlip().method(new Poc() {
					public String poc() {
						return "log:final?showAll=true";
					}
				}, "poc");
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
