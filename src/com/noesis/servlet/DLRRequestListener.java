package com.noesis.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.cloudhopper.commons.util.windowing.WindowFuture;
import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.pdu.DeliverSm;
import com.cloudhopper.smpp.pdu.DeliverSmResp;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.PduResponse;
import com.cloudhopper.smpp.type.Address;
import com.cloudhopper.smpp.util.DeliveryReceipt;
import com.noesis.event.handlers.SessionEventHandler;
import com.noesis.manager.SessionManager;

public class DLRRequestListener extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String CONTENT_TYPE = "text/html";
	static Logger logger = Logger.getLogger(DLRRequestListener.class);
	// Initialize global variables
	public void init() throws ServletException {
	}

	// Process the HTTP Get request
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		SessionEventHandler sessionEventHandler = null;
		
		PrintWriter out = response.getWriter();
		logger.info("DLR Message Received from Kannel: ");
		String dr = request.getParameter("dr");
		String smscid = request.getParameter("smscid");
		String statuscd = request.getParameter("statuscd");
		String messageId = request.getParameter("uniqID");
		String systemId = request.getParameter("systemid");
		String mobile = request.getParameter("mobile");
		logger.info("Received DLR from Kannel for messageID : "+messageId+ "And DR is: "+dr+ " And status Code is : "+statuscd+" And System Id : "+systemId);

		//TODO: Send DLR Back to SMPP
		try {
			/*Thread t = new SendDLRPDU(responseid, mobile, status, senderid,
					errcode, errdesc, dlrtime);
			t.start();
			Thread.sleep(Integer.parseInt(CoreProperties.getInstance()
					.getValue("dlr_send_sleep")));
			t.interrupt();*/
			
			//TODO : Testing
			sessionEventHandler = SessionManager.getInstance().getSession("ashish");
			if (sessionEventHandler!=null) {
				logger.info("User Session found successfully: "+sessionEventHandler.getSessionId());
			}
			String[] address = messageId.split("SMPP");
			DeliverSm pdu = new DeliverSm();
			pdu.setEsmClass((byte)4);
			byte b = 0;
			byte c = 0;
			pdu.setDestAddress(new Address(b,c,address[0]));
			
			
			DeliveryReceipt reciept = new DeliveryReceipt(messageId, 1, 1, new DateTime(), new DateTime(), new Byte("1"), 1, "Hello");
			pdu.setShortMessage(reciept.toShortMessage().getBytes());
			logger.info("Delivery Receipt is: "+reciept.toShortMessage());
			logger.info("Deliver SM PDU is: "+pdu.toString());
			
			
			WindowFuture<Integer,PduRequest,PduResponse> future = sessionEventHandler.getSession().sendRequestPdu(pdu, 10000, false);
		        if (!future.await()) {
		        	logger.error("Failed to receive deliver_sm_resp within specified time");
		        } else if (future.isSuccess()) {
		            DeliverSmResp deliverSmResp = (DeliverSmResp)future.getResponse();
		            logger.info("deliver_sm_resp: commandStatus [" + deliverSmResp.getCommandStatus() + "=" + deliverSmResp.getResultMessage() + "]");
		        } else {
		        	logger.error("Failed to properly receive deliver_sm_resp: " + future.getCause());
		        }
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			if (sessionEventHandler!=null) {
				logger.info("DLR Sent to Kannel. Now release session.");
				sessionEventHandler.setInUse(false);
			}
		}
		
		out.write("RECEIVED DLR");
	}

	// Process the HTTP Post request
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	// Clean up resources
	public void destroy() {
	}
}