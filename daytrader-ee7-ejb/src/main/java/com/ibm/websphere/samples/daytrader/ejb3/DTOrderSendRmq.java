package com.ibm.websphere.samples.daytrader.ejb3;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.SerializationUtils;

@ApplicationScoped
public class DTOrderSendRmq extends RmqEndpoint {

	public DTOrderSendRmq(String endpointName) throws IOException, TimeoutException {
		super(endpointName);
		// TODO Auto-generated constructor stub
	}
	
	public DTOrderSendRmq() throws IOException, TimeoutException {
		super("broker-temp");
	}
	public void sendMessage(Serializable object) throws IOException {
	    channel.basicPublish("",endPointName, null, SerializationUtils.serialize(object));
	}
}
