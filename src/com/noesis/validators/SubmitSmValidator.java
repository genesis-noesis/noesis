package com.noesis.validators;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.pdu.SubmitSmResp;
import com.noesis.dto.RequestObject;
import com.noesis.dto.SubmitObject;

public class SubmitSmValidator
{

    private static Logger logger = Logger.getLogger(SubmitSmValidator.class);
    private static PropertiesConfiguration pc;
    private String instanceId;
    private static final String className = "[MessageValidator]";
    //private Utility utility;
    com.a2wi.ng.util.misc.Utility util;
    private String messageId = null;

    public SubmitSmValidator()
    {
       /* utility = new Utility();
        util = new com.a2wi.ng.util.misc.Utility();
        try
        {
            pc = PropertyFileLoaderTon.getInstance().getPropertiesConfiguration("ng3.properties.loc");
            instanceId = pc.getString("instance.id");
        }
        catch(Exception e)
        {
            logger.error("Error initializing SubmitSmValidator due to ", e);
        }*/
    }

    public void validate(SubmitSm request, SubmitSmResp submitResponse, String systemId, String systemType, HashMap partnerDetails)
    {
        RequestObject reqObj = null;
       
        String bindType = "TRX"; //String.valueOf(partnerDetails.get("BINDTYPE")).trim();
        if("TX".equals(bindType) || "TRX".equals(bindType))
        {
        	String strDestAddr = request.getDestAddress().getAddress();
            strDestAddr = validateDestinationAddress(strDestAddr, submitResponse, partnerDetails);
        	//TODO: Generate Message Id 
            
        	messageId = strDestAddr+"SMPP"+getRandomNumberInRange(100000, 1000000);
        	submitResponse.setMessageId(messageId);
            logger.debug("Message Id to be returned: "+submitResponse.getMessageId());
            /* Integer online_dnd_yn = (Integer)partnerDetails.get("ONLINE_DND_CHECK_YN");
            if(logger.isDebugEnabled())
            {
                logger.debug((new StringBuilder()).append("ONLINE_DND_CHECK_YN - ").append(online_dnd_yn).toString());
            }
            if("PRE".equals(partnerDetails.get("BILL_TYPE")) && !doProcessPrepaid(partnerDetails.get("AID").toString()))
            {
                if(logger.isInfoEnabled())
                {
                    logger.info((new StringBuilder()).append("@@START PREPAID UPDATE - ").append(partnerDetails.get("AID")).append(" - ").append(new Date()).toString());
                }
                submitResponse.setCommandStatus(69);
            }
            if("CR".equals(partnerDetails.get("BILL_TYPE")) && !doProcessCredit(partnerDetails.get("AID").toString()))
            {
                if(logger.isInfoEnabled())
                {
                    logger.info((new StringBuilder()).append("@@START CREDIT UPDATE - ").append(partnerDetails.get("AID")).append(" - ").append(new Date()).toString());
                }
                submitResponse.setCommandStatus(69);
            }*/
            logger.info((new StringBuilder()).append("CommandStatus : ").append(submitResponse.getCommandStatus()).toString());
            if(submitResponse.getCommandStatus() == 0)
            {
                //canProcessDND(strDestAddr, submitResponse, online_dnd_yn.intValue());
                if(submitResponse.getCommandStatus() == 0)
                {
                    SubmitObject so = new SubmitObject(request, submitResponse, submitResponse.getMessageId(), systemId, systemType, partnerDetails);
                    reqObj = so.getRequestObject();
                    reqObj.setInstanceid(instanceId);
                    //checkTimeOfDelivery(reqObj, submitResponse, partnerDetails);*/
                    if(submitResponse.getCommandStatus() == 0)
                    {
                    	logger.info("Calling Deliver SM");
                        deliverSubmitSm(reqObj, submitResponse, systemId);
                    } else
                    {
                        logger.info((new StringBuilder()).append("Message will not processed with command status=").append(submitResponse.getCommandStatus()).toString());
                    }
                }
            }
        } else
        {
            submitResponse.setCommandStatus(3);
        }
    }

    /*private boolean doProcessPrepaid(String aid)
    {
        if(logger.isDebugEnabled())
        {
            logger.debug("---------It's PREPADID ACCOUNT----------");
        }
        if(logger.isDebugEnabled())
        {
            logger.debug((new StringBuilder()).append("@@START PREPAID CHECK - ").append(aid).append(" - ").append(new Date()).toString());
        }
        Segment segment = util.getSegment();
        CreditProcessor crprocess = new CreditProcessor();
        boolean isCreditAvailable = false;
        try
        {
            isCreditAvailable = crprocess.hasBalanceForPrepaidAccount(segment, aid);
            if(logger.isInfoEnabled())
            {
                logger.info((new StringBuilder()).append("@@START PREPAID CHECK - ").append(aid).append(" - ").append(new Date()).append(" hasPrepaid Bal -").append(isCreditAvailable).toString());
            }
        }
        catch(Exception e)
        {
            isCreditAvailable = true;
        }
        return isCreditAvailable;
    }*/

   /* private boolean doProcessCredit(String aid)
    {
        if(logger.isDebugEnabled())
        {
            logger.debug("---------It's CREDIT ACCOUNT----------");
        }
        if(logger.isDebugEnabled())
        {
            logger.debug((new StringBuilder()).append("@@START CREDIT CHECK - ").append(aid).append(" - ").append(new Date()).toString());
        }
        Segment segment = util.getSegment();
        CreditProcessor crprocess = new CreditProcessor();
        boolean isCreditAvailable = false;
        try
        {
            isCreditAvailable = crprocess.hasBalanceForCreditAccount(segment, aid);
            if(logger.isInfoEnabled())
            {
                logger.info((new StringBuilder()).append("@@START CREDIT CHECK - ").append(aid).append(" - ").append(new Date()).append(" hasCredit Bal -").append(isCreditAvailable).toString());
            }
        }
        catch(Exception e)
        {
            isCreditAvailable = true;
        }
        return isCreditAvailable;
    }*/

    /*private void checkTimeOfDelivery(RequestObject reqObj, SubmitSmResp submitResponse, HashMap partnerDetails)
    {
        SMPPDeliveryTimeCheck sdtc = new SMPPDeliveryTimeCheck();
        try
        {
            if(sdtc.isAcceptedTRAITime(reqObj))
            {
                String status = "";
                if(reqObj.getScheTime() != null)
                {
                    Integer schYN = (Integer)partnerDetails.get("SCHEDULE_YN");
                    if(schYN != null && schYN.intValue() == 1)
                    {
                        status = sdtc.isValidScheduleTime(reqObj);
                        if("EXCEPTION".equals(status))
                        {
                            submitResponse.setCommandStatus(97);
                        } else
                        if("CURRENT".equalsIgnoreCase(status))
                        {
                            reqObj.setScheTime(null);
                        } else
                        if("INVALID".equalsIgnoreCase(status))
                        {
                            submitResponse.setCommandStatus(97);
                        }
                    } else
                    {
                        submitResponse.setCommandStatus(97);
                    }
                }
                if(!status.equals("SCHEDULE"))
                {
                    partnerDetails = (HashMap)PushAccount.instance().getPushAccount(reqObj.getPid(), reqObj.getAid());
                    status = sdtc.isValidBlockOut(reqObj);
                    if("INVALID".equalsIgnoreCase(status))
                    {
                        submitResponse.setCommandStatus(97);
                    }
                }
            } else
            {
                if(logger.isDebugEnabled())
                {
                    logger.debug("TRAI Rejected .... : 1026");
                }
                submitResponse.setCommandStatus(1026);
            }
        }
        catch(Exception e)
        {
            submitResponse.setCommandStatus(8);
            logger.error("Problem processing pdu scheduled time due to ...", e);
        }
    }*/

   /* private void canProcessDND(String strDestAddr, SubmitSmResp submitResponse, int online_dnd_yn)
    {
        if(logger.isDebugEnabled())
        {
            logger.debug((new StringBuilder()).append("Before process DND check DestAddr - ").append(strDestAddr).toString());
        }
        boolean isdndreject = false;
        if(StringUtils.startsWith(strDestAddr, "91") && online_dnd_yn == 1)
        {
            ArrayList mobilelist = new ArrayList();
            mobilelist.add(strDestAddr);
            try
            {
                DNDInterface dndCheck = DAOFactory.getDNDDAO();
                List dndlist = dndCheck.getDNDNumber(mobilelist);
                if(dndlist.size() > 0)
                {
                    isdndreject = true;
                }
            }
            catch(Exception e)
            {
                logger.error("[MessageValidator]canProcessDND() DND Check err", e);
            }
        }
        if(isdndreject)
        {
            if(logger.isInfoEnabled())
            {
                logger.error("Message DND rejeceted...");
            }
            submitResponse.setCommandStatus(11);
        }
    }*/

    private String validateDestinationAddress(String strDestAddr, SubmitSmResp submitResponse, HashMap partnerDetails)
    {
        if(strDestAddr == null || strDestAddr.trim().length() < 8 || strDestAddr.trim().length() > 15 || !StringUtils.isNumeric(StringUtils.stripStart(strDestAddr, "+")))
        {
            if(logger.isInfoEnabled())
            {
                logger.info("Invalid destination address message rejected...");
            }
            submitResponse.setCommandStatus(11);
        } else
        {
            strDestAddr = StringUtils.stripStart(strDestAddr, "+");
            strDestAddr = StringUtils.stripStart(strDestAddr, "0");
           /* if(logger.isDebugEnabled())
            {
                logger.debug((new StringBuilder()).append("After strip the destAddr - ").append(strDestAddr).toString());
            }
            if(strDestAddr.length() == 10 && Integer.parseInt(partnerDetails.get("INTL_DELIVERY").toString()) == 0 && Integer.parseInt(partnerDetails.get("LANDLINE_DELIVERY").toString()) == 0)
            {
                strDestAddr = (new StringBuilder()).append("91").append(strDestAddr).toString();
                if(logger.isDebugEnabled())
                {
                    logger.debug((new StringBuilder()).append("Prefix 91 DestAddr -  - ").append(strDestAddr).toString());
                }
            } else if(strDestAddr.length() == 10 && Integer.parseInt(partnerDetails.get("INTL_DELIVERY").toString()) == 1 && Integer.parseInt(partnerDetails.get("PREFIX91_YN").toString()) == 1 && startWith91uppeder(strDestAddr))
            {
                if(logger.isDebugEnabled())
                {
                    logger.debug("prefix91() *** Prefix 91 for the mobile number****");
                }
                strDestAddr = (new StringBuilder()).append("91").append(strDestAddr).toString();
                if(logger.isDebugEnabled())
                {
                    logger.debug((new StringBuilder()).append("Prefix 91 DestAddr -  - ").append(strDestAddr).toString());
                }
            }*/
        }
        logger.info("Final Destination Address: "+strDestAddr);
        return strDestAddr;
    }

    private void deliverSubmitSm(RequestObject reqObj, SubmitSmResp submitResponse, String systemId)
    {
        //boolean chronicleEnabled = Utility.isChronicleEnabled();
        //setTypeOfRequest(reqObj);
        boolean isconcatmsgindicator = false;
        logger.debug((new StringBuilder()).append("Acode  ").append(reqObj.getAcode()).append(" udh : ").append(reqObj.getUdh()).append(" msgclass ").append(reqObj.getMsgclass()).toString());
        if(reqObj.getUdh() != null && reqObj.getUdh().length() > 5 && (reqObj.getUdh().substring(0, 6).equals("050003") || reqObj.getUdh().substring(0, 6).equals("060804")))
        {
            isconcatmsgindicator = true;
        }
        String concatTemplateCheck = "0";
        if(logger.isDebugEnabled())
        {
            /*logger.debug((new StringBuilder()).append("isconcatmsgindicator=").append(isconcatmsgindicator).toString());
            logger.debug((new StringBuilder()).append("PushAccount.instance().getPushAccount(reqObj.getPid(), reqObj.getAid()).get(CONC" +"AT_TEMPLATE_CHECK_YN)"
).append(PushAccount.instance().getPushAccount(reqObj.getPid(), reqObj.getAid()).get("CONCAT_TEMPLATE_CHECK_YN")).toString());
        }
        if(PushAccount.instance().getPushAccount(reqObj.getPid(), reqObj.getAid()).get("CONCAT_TEMPLATE_CHECK_YN") != null)
        {
            concatTemplateCheck = PushAccount.instance().getPushAccount(reqObj.getPid(), reqObj.getAid()).get("CONCAT_TEMPLATE_CHECK_YN").toString();
        }
        String advtFooterYN = "0";
        if(PushAccount.instance().getPushAccount(reqObj.getPid(), reqObj.getAid()).get("ADVT_FOOTER_YN") != null)
        {
            advtFooterYN = PushAccount.instance().getPushAccount(reqObj.getPid(), reqObj.getAid()).get("ADVT_FOOTER_YN").toString();
        }
        String jsonObj = (new Utility()).getAsJsonString(reqObj, instanceId);*/
        try
        {
            /*if(logger.isDebugEnabled())
            {
                logger.debug((new StringBuilder()).append("Request : ").append(reqObj).toString());
            }
            logger.debug((new StringBuilder()).append("JSON OBJ-").append(jsonObj).toString());*/
            boolean pushed = true;
        	
        	//TODO: Send MEssage to Redis
            //pushed = utility.push2Redis(reqObj);
            logger.info((new StringBuilder()).append("Message pushed to redis TODO : ").append(pushed).toString());
            if(logger.isDebugEnabled())
            {
                logger.debug((new StringBuilder()).append("pushed to redis : ").append(pushed).toString());
            }
            if(!pushed)
            {
                submitResponse.setCommandStatus(8);
            }
            
            // TODO: Sending message to Kannel by URL.
            String responseFromKannal = sendMessageToKannel(reqObj);
            logger.debug("Response from Kannel for Number : "+reqObj.getMnumber() +" :is : "+responseFromKannal);
            
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder()).append(systemId).append("[MessageValidator]").append(" Exception while pushing the message to Redis").toString(), e);
            submitResponse.setCommandStatus(8);
        }
    }
    }      

    /*private boolean startWith91uppeder(String mobile)
    {
        StringTokenizer st = new StringTokenizer(DBConfig.instance().getProperty("91prefix.start.number"), "~");
_L1:
        String startnumber;
        if(!st.hasMoreTokens())
        {
            break MISSING_BLOCK_LABEL_56;
        }
        startnumber = st.nextToken();
        if(mobile.startsWith(startnumber))
        {
            return true;
        }
          goto _L1
        Exception ignore;
        ignore;
        logger.error("startWithInternational ", ignore);
        return false;
    }*/

   /* private void setTypeOfRequest(RequestObject requestObject)
    {
        if(StringUtils.isNotBlank(requestObject.getPrty()) && Integer.parseInt(requestObject.getPrty()) > 0)
        {
            requestObject.setTypeOfRequest("PRIORITYMSG");
        } else
        if(StringUtils.isNotBlank(requestObject.getOdreqid()))
        {
            requestObject.setTypeOfRequest("PULLRESP");
        } else
        if(StringUtils.contains(requestObject.getMnumber(), "@"))
        {
            requestObject.setTypeOfRequest("EMAIL");
        } else
        if(StringUtils.isNotBlank(requestObject.getMsgclass()) && requestObject.getMsgclass().equals("1"))
        {
            requestObject.setTypeOfRequest("TRANSMSG");
        } else
        {
            requestObject.setTypeOfRequest("PROMOMSG");
        }
    }*/

    
    public String sendMessageToKannel (RequestObject requestObject) throws IOException {
    	// HTTP GET request
    		//52.66.156.150
    		String systemIp1 = "localhost";
    		String systemIp2 = "52.66.156.150";
    		String dlrUrl = "http://52.66.156.150:8080/noesis-smpp/DLRRequestListener?dr=%a&smscid=%i&statuscd=%d&uniqID="+messageId+"&customerref=ashish"+
    					"&recivetime="+System.currentTimeMillis()+"&dlrtype=9&mobile="+requestObject.getMnumber()+"&submittime="+System.currentTimeMillis()
    					+"&expiry=12senderid=NOESIS&carrierid=TELCONAME&circleid=DELHI&routeid=DELHI&systemid=%o";

    		String url = "http://52.66.156.150:13013/cgi-bin/sendsms?username=test&password=test&smscid=test&from=NOESIS&to="+URLEncoder.encode(requestObject.getMnumber(),"UTF-8")+
    				"&text="+URLEncoder.encode(requestObject.getMessage(),"UTF-8")+"&coding=0&dlr-mask=23&dlr-url="+URLEncoder.encode(dlrUrl, "UTF-8");
    	
    		logger.info("DLR URL Without Encoding: "+dlrUrl);
    		logger.info("final kannel URL Without Encoding: "+url);
    		logger.info("Final Kannel URL for message sending : "+url);
    		URL obj = new URL(url);
    		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    	
    		// optional default is GET
    		con.setRequestMethod("GET");

    		//add request header
    		//con.setRequestProperty("User-Agent", USER_AGENT);

    		int responseCode = con.getResponseCode();
    		System.out.println("\nSending 'GET' request to URL : " + url);
    		System.out.println("Response Code : " + responseCode);

    		BufferedReader in = new BufferedReader(
    		        new InputStreamReader(con.getInputStream()));
    		String inputLine;
    		StringBuffer response = new StringBuffer();

    		while ((inputLine = in.readLine()) != null) {
    			response.append(inputLine);
    		}
    		in.close();

    		//print result
    		return response.toString();
    	}
    
    private static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
}
