/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sample.camel;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.exec.ExecResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * A Camel route that calls the REST service using a timer
 * <p/>
 * Use <tt>@Component</tt> to make Camel auto detect this route when starting.
 */
@Component
public class RestRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

restConfiguration().host("{{amq.host}}").port("{{amq.port}}");

from("timer:hello?period={{timer.period}}")
.process(new Processor() {
   public void process(Exchange exchange) throws Exception {
   
      exchange.getIn().setHeader("Origin", "http://localhost");
      exchange.getIn().setHeader("Authorization", "Basic dGVzdDpjYXRkb2cxMjM=");
      exchange.getIn().setHeader("Accept", "application/json");
   } //end process
}) //end processor
.to("{{amq.heap.memory.endpoint}}")
.log("---------->>>> ${body}")
.choice()
    //.when().jsonpath("$.value.used", String.class).equals("foobar")
    .when().jsonpath("$.value[?(@.used > 200000000)]")
      .log("Heap Memory Alert!")
      .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
      .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
      .to("{{application.webhook.endpoint}}")
      .process(new Processor() {
          public void process(Exchange exchange) throws Exception {

             String myBody = exchange.getIn().getBody(String.class);
             exchange.getIn().setBody(myBody);
          } //end process

      })
    .otherwise()
      //.to("jms:queue:DLQ")
      .log("No Alert")
.end()
.to("exec:{{management.endpoints.config}}")
.process(new Processor() {

   public void process(Exchange exchange) throws Exception {

      List<Integer> propList = new ArrayList<Integer>();

      String myBody = exchange.getIn().getBody(String.class);
      exchange.getIn().setBody(myBody);
    
    
   } //end process
})
.log("${headers}")
.log("${body}")
.log("*** Sending to Webhook ***")
.setHeader(Exchange.HTTP_METHOD, constant("POST"))
//.to("https://webhook.site/3aea0c2f-8b7a-4f8c-88f8-7fce277e96af");
.to("{{application.webhook.endpoint}}");
	
    }

}
