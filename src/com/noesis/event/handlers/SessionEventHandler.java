package com.noesis.event.handlers;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import com.cloudhopper.smpp.PduAsyncResponse;
import com.cloudhopper.smpp.SmppBindType;
import com.cloudhopper.smpp.SmppServerSession;
import com.cloudhopper.smpp.SmppSessionHandler;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.PduResponse;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.pdu.SubmitSmResp;
import com.cloudhopper.smpp.type.RecoverablePduException;
import com.cloudhopper.smpp.type.UnrecoverablePduException;
import com.noesis.validators.SubmitSmValidator;

public class SessionEventHandler implements SmppSessionHandler {

	private String systemId;
	private SmppServerSession session;
	private String sessionId;
	private Date lastUsedTime;
	private Date prevUsedTime;
	private String systemType;
	private String bindType;
	private int bindCommandId;
	private static Logger logger = Logger.getLogger(SessionEventHandler.class);
	private SubmitSmValidator validator;
	private HashMap partnerDetails;
	private boolean expired;
	// private VSMSCDBImpl dbImpl;
	private String instanceId;
	private PropertiesConfiguration pc;
	private boolean isInUse;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}
	
	static class _cls1
    {
        static final int $SwitchMap$com$cloudhopper$smpp$SmppBindType[];
        static {
            $SwitchMap$com$cloudhopper$smpp$SmppBindType = new int[SmppBindType.values().length];
            try
            {
                $SwitchMap$com$cloudhopper$smpp$SmppBindType[SmppBindType.TRANSMITTER.ordinal()] = 1;
            }
            catch(NoSuchFieldError ex) { }
            try
            {
                $SwitchMap$com$cloudhopper$smpp$SmppBindType[SmppBindType.TRANSCEIVER.ordinal()] = 2;
            }
            catch(NoSuchFieldError ex) { }
            try
            {
                $SwitchMap$com$cloudhopper$smpp$SmppBindType[SmppBindType.RECEIVER.ordinal()] = 3;
            }
            catch(NoSuchFieldError ex) { }
        }
    }


	public SessionEventHandler(String systemId, SmppServerSession session, SmppBindType bindType, String sessionId)
        throws Exception
    {
        lastUsedTime = new Date();
        validator = new SubmitSmValidator();
        //dbImpl = new VSMSCDBImpl();
        this.systemId = systemId;
        this.session = session;
        this.sessionId = sessionId;
        systemType = session.getConfiguration().getSystemType();
        //pc = PropertyFileLoaderTon.getInstance().getPropertiesConfiguration("ng3.properties.loc");
        //instanceId = pc.getString("instance.id");
        
    }
    
	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public SmppServerSession getSession() {
		return session;
	}

	public void setSession(SmppServerSession session) {
		this.session = session;
	}

	public Date getLastUsedTime() {
		return lastUsedTime;
	}

	public void setLastUsedTime(Date lastUsedTime) {
		this.lastUsedTime = lastUsedTime;
	}

	public String getSystemType() {
		return systemType;
	}

	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}

	public HashMap getPartnerDetails() {
		return partnerDetails;
	}

	public void setPartnerDetails(HashMap partnerDetails) {
		this.partnerDetails = partnerDetails;
	}

	public boolean isInUse() {
		return isInUse;
	}

	public void setInUse(boolean isInUse) {
		this.isInUse = isInUse;
	}

	public String lookupResultMessage(int commandStatus) {
		System.out.println("lookupResultMessage*********************");
		return null;
	}

	public String lookupTlvTagName(short tag) {
		logger.error((new StringBuilder()).append("lookupTlvTagName not implemented*********************").append(tag)
				.toString());
		return null;
	}

	public void fireChannelUnexpectedlyClosed()
    {
        System.out.println("fireChannelUnexpectedlyClosed*********************");
        String sBindType = null;
        SmppBindType bindType = session.getBindType();
        switch (_cls1.$SwitchMap$com$cloudhopper$smpp$SmppBindType[bindType.ordinal()]) 
        {
        case 1: // '\001'
            sBindType = "TX";
            break;

        case 2: // '\002'
            sBindType = "TRX";
            break;

        case 3: // '\003'
            sBindType = "RX";
            break;
        }
        //dbImpl.insertUnbindRequest(systemId, sBindType, instanceId, true);
    }

	public PduResponse firePduRequestReceived(PduRequest pduRequest) {
		PduResponse response = pduRequest.createResponse();
		int commandId = pduRequest.getCommandId();
		switch (commandId) {
		default:
			break;

		case 4: // '\004'
			try {
				//partnerDetails = InMemAccInfo.getInstance().getAccountDetail(systemId, bindCommandId);
				validator.validate((SubmitSm) pduRequest, (SubmitSmResp) response, systemId, systemType,partnerDetails);
				break;
			} catch (Exception e) {
				logger.error(
						(new StringBuilder()).append("firePduRequestReceived event failed returning system error for ")
								.append(systemId).append(" due to ").toString(),
						e);
			}
			response.setCommandStatus(8);
			break;

		case 33: // '!'
			response.setCommandStatus(3);
			break;

		case 5: // '\005'
			response.setCommandStatus(3);
			break;

		case 259:
			response.setCommandStatus(3);
			break;

		case 3: // '\003'
			response.setCommandStatus(3);
			break;

		case 8: // '\b'
			response.setCommandStatus(3);
			break;

		case 7: // '\007'
			response.setCommandStatus(3);
			break;
		}
		lastUsedTime = new Date();
		return response;
	}

	public void firePduRequestExpired(PduRequest pduRequest) {
		logger.error((new StringBuilder())
				.append("firePduRequestExpired not implemented possible unhandled request****************" + "*****\n")
				.append(pduRequest).toString());
		lastUsedTime = new Date();
	}

	public void fireExpectedPduResponseReceived(PduAsyncResponse pduAsyncResponse) {
		logger.error((new StringBuilder())
				.append("fireExpectedPduResponseReceived not implemented possible unhandled request******"
						+ "***************\n")
				.append(pduAsyncResponse).toString());
		lastUsedTime = new Date();
	}

	public void fireUnexpectedPduResponseReceived(PduResponse pduResponse) {
		logger.error((new StringBuilder())
				.append("fireUnexpectedPduResponseReceived possible unhandled response*******************" + "**\n")
				.append(pduResponse).toString());
		lastUsedTime = new Date();
	}

	public void fireUnrecoverablePduException(UnrecoverablePduException e) {
		logger.error("fireUnrecoverablePduException*********************", e);
		e.printStackTrace();
	}

	public void fireRecoverablePduException(RecoverablePduException e) {
		logger.error("fireRecoverablePduException*********************", e);
		e.printStackTrace();
	}

	public void fireUnknownThrowable(Throwable t) {
		logger.error("fireUnknownThrowable*********************closing the session...", t);
		try {
			session.close();
			session.destroy();
		} catch (Exception error) {
			// SessionManager.getInstance().removeSession(session);
			logger.error("session close/destroy failed session handler removed>>>", error);
		}
	}

	public void updateLastUsedTime() {
		prevUsedTime = lastUsedTime;
		lastUsedTime = new Date();
	}

	public void resetLastUsedTime() {
		lastUsedTime = prevUsedTime;
	}

}
