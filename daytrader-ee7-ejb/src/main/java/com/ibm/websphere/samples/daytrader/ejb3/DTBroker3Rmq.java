package com.ibm.websphere.samples.daytrader.ejb3;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.ejb.EJB;
import javax.jms.TextMessage;

import org.apache.commons.lang.SerializationUtils;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.MDBStats;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class DTBroker3Rmq extends RmqEndpoint implements Runnable, Consumer {
	private final MDBStats mdbStats;
    private int statInterval = 10000;

    
    // TODO: Using local interface, make it configurable to use remote?
    @EJB
    private TradeSLSBLocal tradeSLSB;
    
	public DTBroker3Rmq(String endpointName) throws IOException, TimeoutException {
		super(endpointName);
		if (Log.doTrace()) {
            Log.trace("DTBroker3MDB:DTBroker3MDB()");
        }
        if (statInterval <= 0) {
            statInterval = 10000;
        }
        mdbStats = MDBStats.getInstance();
	}

	@Override
	public void handleCancel(String arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleCancelOk(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleConsumeOk(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleDelivery(String arg0, Envelope arg1, BasicProperties arg2, byte[] body) throws IOException {
		Map message = (HashMap)SerializationUtils.deserialize(body);
		if (Log.doTrace()) {
            Log.trace("TradeBroker:onMessage -- received message -->" + message.toString() + "command-->"
                    + message.get("command") + "<--");
        }
		
		String command = (String)message.get("command");
		String data = (String)message.get("data");
		
	}

	@Override
	public void handleRecoverOk(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleShutdownSignal(String arg0, ShutdownSignalException arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		try {
			//start consuming messages. Auto acknowledge messages.
			channel.basicConsume(endPointName, true,this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
