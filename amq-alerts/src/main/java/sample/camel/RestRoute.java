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

from("timer:hello?period={{timer.period}}")
    //.setHeader("id", simple("${random(1,3)}"))
.to("exec:{{management.endpoints.config}}")
.process(new Processor() {

   public void process(Exchange exchange) throws Exception {

      List<Integer> propList = new ArrayList<Integer>();

      String myBody = exchange.getIn().getBody(String.class);
      System.out.println("$$$$$$$$$$ myBody: "+myBody);
      String totalMsg = exchange.getContext().resolvePropertyPlaceholders("{{total.message.count.threshold}}");
      propList.add(Integer.parseInt(totalMsg));
      String totalThread = exchange.getContext().resolvePropertyPlaceholders("{{peak.thread.count.threshold}}");
      propList.add(Integer.parseInt(totalThread));
      String totalConn = exchange.getContext().resolvePropertyPlaceholders("{{connection.count.threshold}}");
      propList.add(Integer.parseInt(totalConn));
      String totalQueueMsg = exchange.getContext().resolvePropertyPlaceholders("{{queue.message.count.threshold}}");
      propList.add(Integer.parseInt(totalQueueMsg));
      String totalMem = exchange.getContext().resolvePropertyPlaceholders("{{address.memory.threshold}}");
      float memPercent = Float.parseFloat(totalMem);
      String totalDisk = exchange.getContext().resolvePropertyPlaceholders("{{diskstore.usage.threshold}}");
      float diskPercent = Float.parseFloat(totalDisk);
      String totalCpu = exchange.getContext().resolvePropertyPlaceholders("{{process.cpu.load.threshold}}");
      float cpuPercent = Float.parseFloat(totalCpu);
      
      String emailAddress = exchange.getContext().resolvePropertyPlaceholders("{{application.email.emailAddress}}"); 
      exchange.getIn().setBody("{\n"
 	+ "  \"body\": \"" + myBody + "\" ,\n"
    	+ "  \"emailAddress\": \"" + emailAddress + "\",\n"
    	+ "  \"subject\": \"Manual Processing queue\"\n"
    	+ "}");
    
      List<String> matchList = new ArrayList<String>();
      Pattern pattern = Pattern.compile("(?<=value\":).*?(?=,)");
      Matcher matcher = pattern.matcher(myBody);
      while ( matcher.find( ) ) {
         matchList.add(matcher.group());
      }
      int temp = 0;
      Map<Integer,Integer> alertMap = new LinkedHashMap<Integer,Integer>(20,0.75f,false);
      for (int i=0; i<matchList.size(); i++) {
         temp = Integer.parseInt(matchList.get(i));
         if ( temp >= propList.get(i) ) {
            alertMap.put(i,temp);
         }
      }
      Set entrySet = alertMap.entrySet();
      Iterator it = entrySet.iterator();
      System.out.println("LinkedHashMap entries : ");
  
      boolean alert = false;
        while (it.hasNext()) {
            System.out.println(it.next());
            alert = true;
        }
   } //end process
})
.log("@@@@@"+"${headers}")
//.log("!!!!!"+"${body}")
.log("*** Sending email ***")
.to("smtps://smtp.gmail.com:465?username=foo&password=bar&debugMode=true&mail.smtp.auth=true&mail.smtp.starttls.enable=true");
	
//.to("smtp://{{smtp.host}}?username={{smtp.username}}&password={{smtp.password}}&from={{smtp.from.email}}&contentType={{smtp.contentType}}")
    }

}
