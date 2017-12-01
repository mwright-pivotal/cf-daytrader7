package com.ibm.websphere.samples.daytrader.ejb3;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jms.TextMessage;

import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.Property;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public abstract class RmqEndpoint {
	protected Channel channel;
    protected Connection connection;
    protected String endPointName;
    
    /**
     * Expecting cloud.services.<service-name>.connection.<property> when PCF
     */
    @Inject
    @Property("cloud.services.broker-messaging.connection.host")
    private String rabbitHost;
    
    @Inject
    @Property("cloud.services.broker-messaging.connection.username")
    private String rabbitUser;
    
    @Inject
    @Property("cloud.services.broker-messaging.connection.password")
    private String rabbitPassword;
	
    public RmqEndpoint(String endpointName) {
         this.endPointName = endpointName;
    }
    
    @PostConstruct
    public void init() {
         //Create a connection factory
         ConnectionFactory factory = new ConnectionFactory();
         System.out.println("Rabbit Host is "+ rabbitHost);
         
         if (Log.doTrace()) {
             Log.trace("Using RabbitMQ at " + rabbitHost);
         }
	    
         //hostname of your rabbitmq server
         factory.setHost(rabbitHost);
         factory.setUsername(rabbitUser);
         factory.setPassword(rabbitPassword);
		
         //getting a connection
         try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			//declaring a queue for this channel. If queue does not exist,
	         //it will be created on the server.
	         channel.queueDeclare(this.endPointName, false, false, false, null);
		} catch (IOException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
         
    }
	
	
    /**
     * Close channel and connection. Not necessary as it happens implicitly any way. 
     * @throws IOException
     * @throws TimeoutException 
     */
     public void close() throws IOException, TimeoutException{
         this.channel.close();
         this.connection.close();
     }
}
