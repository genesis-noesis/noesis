package com.noesis.event.handlers;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import com.cloudhopper.smpp.SmppBindType;
import com.cloudhopper.smpp.SmppServerSession;
import com.cloudhopper.smpp.SmppSessionHandler;
import com.cloudhopper.smpp.pdu.BaseBindResp;
import com.cloudhopper.smpp.type.SmppProcessingException;
import com.noesis.manager.SessionManager;
import com.noesis.server.SmppSessionHandlerInterface;

// Referenced classes of package com.a2wi.ng.vsmsc.event.handlers:
//            SessionEventHandler

public class SmppSessionBindUnbindHandler implements SmppSessionHandlerInterface {

	private static final Logger logger = Logger.getLogger(SmppSessionBindUnbindHandler.class);
	// private VSMSCDBImpl vsmscDbImpl;
	// DNRedis dnRedis;
	private String instanceId = "local";
	private PropertiesConfiguration pc;

	public SmppSessionBindUnbindHandler() {
		// vsmscDbImpl = new VSMSCDBImpl();
		// dnRedis = new DNRedis();
		try {
			// pc = PropertyFileLoaderTon.getInstance().getPropertiesConfiguration("ng3.properties.loc");
			// instanceId = pc.getString("instance.id");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* anonymous class not found */
	static class _cls1 {
		static int $SwitchMap$com$cloudhopper$smpp$SmppBindType[];
		static {
			$SwitchMap$com$cloudhopper$smpp$SmppBindType = new int[SmppBindType.values().length];
			try {
				$SwitchMap$com$cloudhopper$smpp$SmppBindType[SmppBindType.TRANSMITTER.ordinal()] = 1;
			} catch (NoSuchFieldError ex) {
			}
			try {
				$SwitchMap$com$cloudhopper$smpp$SmppBindType[SmppBindType.TRANSCEIVER.ordinal()] = 2;
			} catch (NoSuchFieldError ex) {
			}
			try {
				$SwitchMap$com$cloudhopper$smpp$SmppBindType[SmppBindType.RECEIVER.ordinal()] = 3;
			} catch (NoSuchFieldError ex) {
			}
		}
	}

	public synchronized SmppSessionHandler sessionCreated(Long sessionId, SmppServerSession session,
			BaseBindResp preparedBindResponse) throws SmppProcessingException {
		if (logger.isInfoEnabled()) {
			logger.info((new StringBuilder()).append("sessionCreated************ acode=")
					.append(session.getConfiguration().getSystemId()).append(" host=")
					.append(session.getConfiguration().getHost()).toString());
		}
		String remoteIPAddr = session.getConfiguration().getHost();
		if (logger.isInfoEnabled()) {
			logger.info((new StringBuilder()).append("Remote Host: ").append(remoteIPAddr).toString());
		}
		/*
		 * if(!AuthorizedIPs.getInstance().isIPAuthorized(remoteIPAddr)) {
		 * logger.error("ip validation failed..."); throw new
		 * SmppProcessingException(13); }
		 */
		SmppBindType bindType = session.getBindType();
		String sBindType = null;
		SessionEventHandler aSessionHandler = null;

		switch (_cls1.$SwitchMap$com$cloudhopper$smpp$SmppBindType[bindType.ordinal()]) {
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
		if (logger.isInfoEnabled()) {
			logger.info((new StringBuilder()).append("BindType :").append(sBindType).toString());
		}
		try {
			String systemId = session.getConfiguration().getSystemId();
			if (logger.isInfoEnabled()) {
				logger.info((new StringBuilder()).append("SystemId :").append(systemId).toString());
			}
			logger.info("systemID : "+systemId);
			logger.info("session : "+session);
			logger.info("session.getBindType() : "+session.getBindType());
			logger.info("sessionId : "+sessionId);
			
			aSessionHandler = new SessionEventHandler(systemId, session, session.getBindType(), sessionId.toString());
			if (logger.isInfoEnabled()) {
				logger.info("Created Session Handler...");
			}
			// BindUnbindCounter.getInstance().addBind(systemId);
			int bindTypeId = 0;
			if (session.getBindType().equals(SmppBindType.RECEIVER)) {
				bindTypeId = 1;
			} else if (session.getBindType().equals(SmppBindType.TRANSMITTER)) {
				bindTypeId = 2;
			} else {
				bindTypeId = 9;
			}
			if (logger.isInfoEnabled()) {
				logger.info((new StringBuilder()).append("new Bind type =").append(session.getBindType()).toString());
			}
			SessionManager.getInstance().addSession(aSessionHandler);
			// HashMap partnerDetails =
			// InMemAccInfo.getInstance().getAccountDetail(systemId, bindTypeId);
			// int maxbind = Integer.parseInt(partnerDetails.get("MAXBIND").toString());
			// int maxConnectionSize =
			// Integer.parseInt(DBConfig.instance().getProperty("vsmsc.max.binds"));
			/*
			 * if(session.getBindType().equals(SmppBindType.TRANSMITTER)) {
			 * SessionRoundRobin srr =
			 * (SessionRoundRobin)SessionManager.getInstance().getTxSessionsMap().get(
			 * systemId); logger.info((new
			 * StringBuilder()).append("maxbind=").append(maxbind).append(" handlerscount=")
			 * .append(srr == null ? 0 : srr.getHandlersCount()).toString()); if(srr != null
			 * && srr.getHandlersCount() >= maxbind) { throw new SmppProcessingException(5);
			 * } } else { SessionRoundRobin srr =
			 * (SessionRoundRobin)SessionManager.getInstance().getRxTrxSessionsMap().get(
			 * systemId); logger.info((new
			 * StringBuilder()).append("maxbind=").append(maxbind).
			 * append(" rxhandlerscount=").append(srr == null ? 0 :
			 * srr.getHandlersCount()).toString()); if(srr != null && srr.getHandlersCount()
			 * >= maxbind) { throw new SmppProcessingException(5); } }
			 */
			/*
			 * if(SessionManager.getInstance().getTotalBindCount() < maxConnectionSize) {
			 * SessionManager.getInstance().addSession(asessionHandler); } else { throw new
			 * SmppProcessingException(5); }
			 * vsmscDbImpl.insertBindRequest(asessionHandler.getPartnerDetails().get("AID").
			 * toString(), sBindType, instanceId);
			 * if((session.getBindType().equals(SmppBindType.TRANSCEIVER) ||
			 * session.getBindType().equals(SmppBindType.RECEIVER)) &&
			 * !dnRedis.addBindCount(systemId, instanceId)) { throw new
			 * SmppProcessingException(8); }
			 */
			preparedBindResponse.setSystemId(aSessionHandler.getSystemId());
		} catch (Exception exp) {
			logger.error("problem creating bind request ", exp);
			if (!(exp instanceof SmppProcessingException)) {
				throw new SmppProcessingException(8);
			} else {
				throw (SmppProcessingException) exp;
			}
		}
		return aSessionHandler;
	}

	public synchronized void sessionDestroyed(Long sessionId, SmppServerSession session)
    {
      /*  try
        {
            //BindUnbindCounter.getInstance().addUnBind(session.getConfiguration().getSystemId());
            try
            {
              //  LiveSessionsCounter.INST.removeCounter(sessionId.longValue());
            }
            catch(Exception error)
            {
                logger.error("problem removing session counter...", error);
            }
            //ServerRequestCounter counter = ServerRequestCounter.getInstance();
            counter.setDeliverSm(counter.getDeliverSm() + (long)session.getCounters().getTxDeliverSM().getRequest());
            counter.setDeliverSmResp(counter.getDeliverSmResp() + (long)session.getCounters().getTxDeliverSM().getResponse());
            counter.setEnquireLink(counter.getEnquireLink() + (long)session.getCounters().getRxEnquireLink().getResponse());
            counter.setSubmitSm(counter.getSubmitSm() + (long)session.getCounters().getRxSubmitSM().getRequest());
            counter.setSubmitSmResp(counter.getSubmitSmResp() + (long)session.getCounters().getRxSubmitSM().getResponse());
            if(logger.isInfoEnabled())
            {
                logger.info((new StringBuilder()).append("Session destroyed ").append(sessionId).toString());
            }
            SessionManager.getInstance().removeSession(session);
            SmppBindType bindType = session.getBindType();
            String sBindType = null;
            switch(_cls1.SwitchMap.com.cloudhopper.smpp.SmppBindType[bindType.ordinal()])
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
            if(session.getBindType().equals(SmppBindType.TRANSCEIVER) || session.getBindType().equals(SmppBindType.RECEIVER))
            {
                try
                {
                    dnRedis.removeBindCount(session.getConfiguration().getSystemId(), instanceId);
                }
                catch(Exception exp)
                {
                    logger.error("problem adding unbind info to redis...", exp);
                }
            }
            int bindCommandId = 0;
            switch(_cls1..SwitchMap.com.cloudhopper.smpp.SmppBindType[bindType.ordinal()])
            {
            case 1: // '\001'
                bindCommandId = 2;
                break;

            case 2: // '\002'
                bindCommandId = 9;
                break;

            case 3: // '\003'
                bindCommandId = 1;
                break;
            }
           // HashMap partnerDetails = InMemAccInfo.getInstance().getAccountDetail(session.getConfiguration().getSystemId(), bindCommandId);
           // vsmscDbImpl.insertUnbindRequest(partnerDetails.get("AID").toString(), sBindType, instanceId, false);
        }
        catch(Exception exp)
        {
            logger.error("Problem in removing session during unbind due to ", exp);
        }*/
        logger.info("sessionDestroyed********************");
    }

}