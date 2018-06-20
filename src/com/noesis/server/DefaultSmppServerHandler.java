package com.noesis.server;

import java.io.Serializable;

import javax.management.MBeanServer;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import com.cloudhopper.smpp.SmppServerHandler;
import com.cloudhopper.smpp.SmppServerSession;
import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.cloudhopper.smpp.pdu.BaseBind;
import com.cloudhopper.smpp.pdu.BaseBindResp;
import com.cloudhopper.smpp.type.SmppProcessingException;

public class DefaultSmppServerHandler implements SmppServerHandler, Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(DefaultSmppServerHandler.class);
	private transient SmppSessionHandlerInterface smppSessionHandlerInterface;
	//PropertiesConfiguration pc;
	private MBeanServer mbeanServer;

	public DefaultSmppServerHandler() {
		smppSessionHandlerInterface = null;
		mbeanServer = null;
		try {
		//pc = PropertyFileLoaderTon.getInstance().getPropertiesConfiguration("ng3.properties.loc");
		} catch (Exception e) {
			logger.error("problem initializing NG properties", e);
		}
	}

	public SmppSessionHandlerInterface getSmppSessionHandlerInterface() {
		return smppSessionHandlerInterface;
	}

	public void setSmppSessionHandlerInterface(SmppSessionHandlerInterface smppSessionHandlerInterface) {
		this.smppSessionHandlerInterface = smppSessionHandlerInterface;
	}

	public synchronized void sessionBindRequested(Long sessionId, SmppSessionConfiguration sessionConfiguration,
			BaseBind bindRequest) throws SmppProcessingException {
		if (logger.isInfoEnabled()) {
			logger.info("sessionBindRequested##################");
		}
		if (smppSessionHandlerInterface == null) {
			logger.error("No SmppSessionHandlerInterface registered yet! Will close SmppServerSession");
			throw new SmppProcessingException(13);
		}
		String systemId = bindRequest.getSystemId();
		if (logger.isInfoEnabled()) {
			logger.info((new StringBuilder()).append("bind get systemid##################")
					.append(bindRequest.getSystemId()).toString());
		}
		sessionConfiguration.setConnectTimeout(30000L);
		sessionConfiguration.setBindTimeout(30000L);
		sessionConfiguration.setWindowSize(1);
		bindRequest.getSystemType();
		String allowedBindTypes = "TRX,RX,TX"; //DBConfig.instance().getProperty("bind.type.allowed");
		if (logger.isInfoEnabled()) {
			logger.info((new StringBuilder()).append("Allowed bind types=").append(allowedBindTypes).toString());
		}
		int iBindType = bindRequest.getCommandId();
		System.out.println("iBindType: "+iBindType);
		if (iBindType == 2) {
			if (allowedBindTypes.indexOf("TX") == -1) {
				logger.error((new StringBuilder()).append("TX bind not allowed in this instance disallowed systemid=")
						.append(systemId).toString());
				throw new SmppProcessingException(13);
			}
		} else if (iBindType == 1) {
			String bt[] = allowedBindTypes.split(",");
			boolean rxAllowed = false;
			String arr$[] = bt;
			int len$ = arr$.length;
			int i$ = 0;
			do {
				if (i$ >= len$) {
					break;
				}
				String bindType = arr$[i$];
				if (bindType.equals("RX")) {
					rxAllowed = true;
					break;
				}
				i$++;
			} while (true);
			if (!rxAllowed) {
				logger.error((new StringBuilder()).append("RX bind not allowed in this instance disallowed systemid=")
						.append(systemId).toString());
				throw new SmppProcessingException(13);
			}
		} else if (iBindType == 9 && allowedBindTypes.indexOf("TRX") == -1) {
			logger.error((new StringBuilder()).append("TRX bind not allowed in this instance disallowed systemid=")
					.append(systemId).toString());
			throw new SmppProcessingException(13);
		}
		//(new BindValidator()).validate(bindRequest);
	}

	public void sessionCreated(Long sessionId, SmppServerSession session, BaseBindResp preparedBindResponse)
			throws SmppProcessingException {
		if (logger.isInfoEnabled()) {
			logger.info(
					String.format("Session created: %s", new Object[] { session.getConfiguration().getSystemId() }));
		}
		if (smppSessionHandlerInterface == null) {
			logger.error("No SmppSessionHandlerInterface registered yet! Will close SmppServerSession");
			throw new SmppProcessingException(13);
		}
		if (!logger.isDebugEnabled()) {
			session.getConfiguration().getLoggingOptions().setLogBytes(true);
			session.getConfiguration().getLoggingOptions().setLogPdu(true);
			session.getConfiguration().setName(session.getConfiguration().getSystemId());
		}
		com.cloudhopper.smpp.SmppSessionHandler smppSessionHandler = smppSessionHandlerInterface
				.sessionCreated(sessionId, session, preparedBindResponse);
		session.serverReady(smppSessionHandler);
		registerMBean(sessionId, session);
	}

	public synchronized void sessionDestroyed(Long sessionId, SmppServerSession session) {
		if (logger.isInfoEnabled()) {
			logger.info(
					String.format("Session destroyed: %s", new Object[] { session.getConfiguration().getSystemId() }));
		}
		if (smppSessionHandlerInterface != null) {
			smppSessionHandlerInterface.sessionDestroyed(sessionId, session);
		}
		if (session.hasCounters() && logger.isInfoEnabled()) {
			logger.info(String.format("final session rx-submitSM: %s",
					new Object[] { session.getCounters().getRxSubmitSM() }));
			logger.info(String.format("final session Tx-deliverSM: %s",
					new Object[] { session.getCounters().getTxDeliverSM() }));
			logger.info(String.format("final session Tx-EnquireLink: %s",
					new Object[] { session.getCounters().getRxEnquireLink() }));
		}
		session.destroy();
		unregisterMBean(sessionId, session);
	}

	private void registerMBean(Long long1, SmppServerSession smppserversession) {
	}

	private void unregisterMBean(Long long1, SmppServerSession smppserversession) {
	}

}
