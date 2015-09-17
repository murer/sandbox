package com.murerz.camel;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.DescriptorProtos.FileDescriptorProto;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.DynamicMessage;
import com.murerz.camel.proto.SendSMSMessageBeanProtocolBuffer;
import com.murerz.camel.proto.SendSMSMessageBeanProtocolBuffer.SendSMSMessageBeanProtocolBufferMessage;

public class ProtoBufClassMain {

    private static final Logger LOG = LoggerFactory.getLogger(ProtoBufClassMain.class);

    public static interface Poc {

        public String poc();

    }

    public static void main(String[] args) throws Exception {
        LOG.info("init");

        DefaultCamelContext ctx = new DefaultCamelContext();

        Map<String, String> mapping = new HashMap<String, String>();
        FileInputStream fin = new FileInputStream("/tmp/sender.desc");
        DescriptorProtos.FileDescriptorSet set = DescriptorProtos.FileDescriptorSet.parseFrom(fin);
        FileDescriptorProto dep = set.getFile(0);
        FileDescriptorProto f = set.getFile(1);
        FileDescriptor fd = FileDescriptor.buildFrom(dep, new FileDescriptor[] {});
        fd = FileDescriptor.buildFrom(f, new FileDescriptor[] { fd });
        System.out.println(fd);
        final Descriptor x = fd.findMessageTypeByName("SendSMSMessageBeanProtocolBufferMessage");

        ctx.addRoutes(new RouteBuilder() {
            public void configure() {
                from("file:/tmp/test").unmarshal().protobuf(SendSMSMessageBeanProtocolBufferMessage.getDefaultInstance()).process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        SendSMSMessageBeanProtocolBufferMessage body = exchange.getIn().getBody(SendSMSMessageBeanProtocolBufferMessage.class);
                        System.out.println("i: " + body.getCarrierId());
                        System.out.println("p: " + body.getPriority());
                        System.out.println("desc: " + body.getDescription());
                        Thread.sleep(1000l);
                        System.out.println("Xxxx");
                        System.exit(0);
                    }
                });
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
