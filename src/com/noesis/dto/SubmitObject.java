package com.noesis.dto;

import com.cloudhopper.commons.util.HexUtil;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.pdu.SubmitSmResp;
import com.cloudhopper.smpp.tlv.Tlv;
import com.cloudhopper.smpp.type.Address;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// Referenced classes of package com.a2wi.ng.vsmsc.util:
//            SmppUtility

public class SubmitObject  implements Serializable //, Constants
{

    private transient SubmitSm sm;
    private transient SubmitSmResp _sr;
    private String systemId;
    private String pCode;
    private String systemType;
    private String pin;
    private String phoneNumber;
    private String message;
    private String fromAddress;
    private String lang;
    private String deliveryTime;
    private String odRequestId;
    private String priority;
    private String udh;
    private String cntry;
    private String dlr;
    private String msgType;
    private String expiry;
    private String splitAlgm;
    private String refid;
    private String pid;
    private String aid;
    private String queryKey;
    private String msgAcceptedDateTime;
    private String msgsrc;
    private String billRef;
    private String msgclass;
    private String defertype;
    private String instanceid;
    private int proiority;
    private static final long serialVersionUID = 0xb2d7904043318516L;
    private static final String className = "[SubmitObject]";
    private static Log logger = LogFactory.getLog(SubmitObject.class);
    //private SmppUtility smppUtility;

    public SubmitObject()
    {
        sm = null;
        _sr = null;
        systemId = null;
        pCode = null;
        systemType = null;
        pin = null;
        phoneNumber = null;
        message = null;
        fromAddress = null;
        lang = null;
        deliveryTime = null;
        odRequestId = null;
        priority = null;
        udh = null;
        cntry = null;
        dlr = null;
        msgType = null;
        expiry = null;
        splitAlgm = null;
        refid = null;
        pid = null;
        aid = null;
        queryKey = null;
        msgAcceptedDateTime = null;
        msgsrc = null;
        billRef = null;
        msgclass = null;
        defertype = null;
        instanceid = null;
        proiority = 3;
       // smppUtility = new SmppUtility();
    }

    public RequestObject getRequestObject()
    {
        RequestObject requestObject = new RequestObject(pCode, systemId, pin, phoneNumber, message, fromAddress, lang, deliveryTime, odRequestId, priority, msgType, udh, null, cntry, dlr, expiry, splitAlgm, refid, pid, aid, queryKey, msgAcceptedDateTime, msgsrc, billRef, null, msgclass, defertype, proiority, null, instanceid);
        return requestObject;
    }

    public SubmitObject(SubmitSm _sm, SubmitSmResp _sr, String _queryKey, String _systemId, String _systemType, HashMap partnerDetails)
    {
        sm = null;
        this._sr = null;
        systemId = null;
        pCode = null;
        systemType = null;
        pin = null;
        phoneNumber = null;
        message = null;
        fromAddress = null;
        lang = null;
        deliveryTime = null;
        odRequestId = null;
        priority = null;
        udh = null;
        cntry = null;
        dlr = null;
        msgType = null;
        expiry = null;
        splitAlgm = null;
        refid = null;
        pid = null;
        aid = null;
        queryKey = null;
        msgAcceptedDateTime = null;
        msgsrc = null;
        billRef = null;
        msgclass = null;
        defertype = null;
        instanceid = null;
        proiority = 3;
        //smppUtility = new SmppUtility();
        if(logger.isDebugEnabled())
        {
            logger.debug((new StringBuilder()).append(_systemId).append(" [SubmitObject] SubmitObject()..").toString());
           // logger.debug((new StringBuilder()).append(_systemId).append(" [SubmitObject] SubmitObject() partnerDetails - ").append(partnerDetails).toString());
        }
        String dataCoding = null;
        String esmClass = null;
        String msgWithHeader = null;
        String validityPeriod = null;
        String priorityFlag = null;
        boolean clientPriorityEnabled = false;
        String sourceTON = null;
        String sourceNPI = null;
        String destinationTON = null;
        String destinationNPI = null;
        String serviceType = null;
        msgAcceptedDateTime = (new Long(System.currentTimeMillis() )).toString();// + Long.parseLong(DBConfig.instance().getProperty("database.time.difference")))).toString();
        sm = _sm;
        this._sr = _sr;
        queryKey = _queryKey;
        systemId = _systemId;
        systemType = _systemType;
        pCode = null; //(String)partnerDetails.get("PCODE");
        serviceType = _sm.getServiceType();
        phoneNumber = _sm.getDestAddress().getAddress() != null ? _sm.getDestAddress().getAddress().trim() : "";
        phoneNumber = StringUtils.stripStart(phoneNumber, "+");
        phoneNumber = StringUtils.stripStart(phoneNumber, "0");
        if(logger.isDebugEnabled())
        {
            logger.debug((new StringBuilder()).append(systemId).append(" [SubmitObject] SubmitObject() phoneNumber - ").append(phoneNumber).toString());
        }
        destinationTON = String.valueOf(_sm.getDestAddress().getTon());
        destinationNPI = String.valueOf(_sm.getDestAddress().getNpi());
        sourceTON = String.valueOf(_sm.getSourceAddress().getTon());
        sourceNPI = String.valueOf(_sm.getSourceAddress().getNpi());
        try
        {
            if(_sm.getSourceAddress() == null || _sm.getSourceAddress().getAddress().length() == 0)
            {
               // Integer dynaSender = (Integer)partnerDetails.get("DYNA_SENDER");
               // Integer mulSender = (Integer)partnerDetails.get("MUL_SENDER");
               /* if(dynaSender.intValue() == 1 || mulSender.intValue() == 1)
                {
                    Map accMap = PushAccount.instance().getPushAccount(partnerDetails.get("PID").toString(), partnerDetails.get("AID").toString());
                    String rejectsenderid = (String)accMap.get("REJECT_ON_SENDERID");
                    if(rejectsenderid != null && rejectsenderid.trim().length() > 0)
                    {
                        fromAddress = rejectsenderid.trim();
                    } else
                    {
                        _sr.setCommandStatus(10);
                    }
                    logger.info((new StringBuilder()).append(systemId).append(" [SubmitObject] Source address (senderid) is null or empty").toString());
                } else
                {
                    Object staticSenderId = partnerDetails.get("STATICSENDERID");
                    if(staticSenderId != null)
                    {
                        fromAddress = (String)staticSenderId;
                    } else
                    {
                        fromAddress = "";
                    }
                }*/
            } else
            {
                fromAddress = _sm.getSourceAddress().getAddress().trim();
            }
            if(logger.isDebugEnabled())
            {
                logger.debug((new StringBuilder()).append(systemId).append(" [SubmitObject] SubmitObject() fromAddress(senderid) - ").append(fromAddress).toString());
            }
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] Error while checking source address id. Error: ").toString(), e);
        }
        pin = null; //partnerDetails.get("PIN").toString();
        if(logger.isDebugEnabled())
        {
            logger.debug((new StringBuilder()).append(systemId).append(" [SubmitObject] SubmitObject() pin - ").append(pin).toString());
        }
        pid = null; // partnerDetails.get("PID").toString();
        if(logger.isDebugEnabled())
        {
            logger.debug((new StringBuilder()).append(systemId).append(" [SubmitObject] SubmitObject() pid - ").append(pid).toString());
        }
        aid = null; //partnerDetails.get("AID").toString();
        if(logger.isDebugEnabled())
        {
            logger.debug((new StringBuilder()).append(systemId).append(" [SubmitObject] SubmitObject() aid - ").append(aid).toString());
        }
        msgsrc = String.valueOf(1);
        phoneNumber = prefix91(pid, aid, phoneNumber);
        if(logger.isDebugEnabled())
        {
            logger.debug((new StringBuilder()).append(systemId).append(" [SubmitObject] SubmitObject() Prefix 91 phoneNumber - ").append(phoneNumber).toString());
        }
       // msgclass = PushAccount.instance().getPushAccount(pid, aid).get("MSGCLASS").toString();
        if(logger.isDebugEnabled())
        {
            logger.debug((new StringBuilder()).append(systemId).append(" Message Class : ").append(msgclass).toString());
        }
        try
        {
            dataCoding = Byte.toString(_sm.getDataCoding());
            if(dataCoding != null && dataCoding.length() > 0)
            {
                logger.info((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] dataCoding : ").append(dataCoding).toString());
            } else
            {
                dataCoding = null;
            }
        }
        catch(Exception e)
        {
          //  TrapSender.sendTrap("VSMSC", null, 119, "Finding dataCoding", e);
            logger.error((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] Error while finding dataCoding. Error: ").toString(), e);
        }
        try
        {
            esmClass = Byte.toString(_sm.getEsmClass());
            if(esmClass != null && esmClass.length() > 0)
            {
                logger.info((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] esmClass : ").append(esmClass).toString());
              //  esmClass = smppUtility.getHexStr(esmClass);
                logger.info((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] esmClass (HEX): ").append(esmClass).toString());
            } else
            {
                esmClass = null;
            }
        }
        catch(Exception e)
        {
           // TrapSender.sendTrap("VSMSC", null, 119, "Finding esmClass", e);
            logger.error((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] Error while finding esmClass. Error: ").toString(), e);
        }
        if(dataCoding != null)
        {
            checkMsgType(dataCoding);
        } else
        {
            msgType = "PM";
        }
        try
        {
            if(esmClass != null && (esmClass.equals("40") || esmClass.equals("43")))
            {
                msgWithHeader = HexUtil.toHexString(_sm.getShortMessage());
                logger.info((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] UDH+message : ").append(msgWithHeader).toString());
                spiltMsgWithHeader(msgWithHeader);
            } else
            if(dataCoding.trim().equals("8") || dataCoding.trim().equals("18") || dataCoding.trim().equals("24"))
            {
                message = HexUtil.toHexString(_sm.getShortMessage());
                logger.info((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] Unicode Msg : ").append(message).toString());
            } else
            {
                message = new String(_sm.getShortMessage());
                logger.info((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] message : ").append(message).toString());
                if(message == null || message.trim().length() == 0)
                {
                    Tlv messagePayloadTlv = _sm.getOptionalParameter((short)1060);
                    if(messagePayloadTlv != null)
                    {
                        message = messagePayloadTlv.getValueAsString();
                    }
                    logger.info((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] message Payload : ").append(message).toString());
                }
            }
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] Error while finding the message. Error: ").toString(), e);
        }
        message = message != null ? message.trim() : "";
        try
        {
            String origDelTS = _sm.getScheduleDeliveryTime();
            if(logger.isDebugEnabled())
            {
                logger.debug((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] origDelTS: ").append(origDelTS).toString());
            }
            if(origDelTS != null && !origDelTS.isEmpty() && origDelTS.length() > 6)
            {
                String deliveryTS = origDelTS.substring(0, origDelTS.length() - 6);
                if(logger.isDebugEnabled())
                {
                    logger.debug((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] deliveryTS: ").append(deliveryTS).toString());
                }
                SimpleDateFormat sdf_1 = new SimpleDateFormat("yyMMddHHmm");
                SimpleDateFormat sdf_2 = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
                deliveryTime = sdf_2.format(sdf_1.parse(deliveryTS));
                logger.info((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] Scheduled Delivery Time: ").append(deliveryTime).toString());
            } else
            {
                deliveryTime = null;
            }
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] Error while finding scheduled delivery time. Error" +
": "
).toString(), e);
        }
        try
        {
            String origValPeriod = _sm.getValidityPeriod();
            logger.info((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] origValPeriod :").append(origValPeriod).toString());
            if(origValPeriod != null && !origValPeriod.isEmpty() && origValPeriod.length() > 4)
            {
                validityPeriod = origValPeriod.substring(0, origValPeriod.length() - 4);
                logger.info((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] validityPeriod:").append(validityPeriod).toString());
                SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
                Date current_date = new Date();
                Date valid_date = format.parse(validityPeriod);
                long validMins = valid_date.getTime() / 60000L - current_date.getTime() / 60000L;
              //  int MAX_EXPIRY = Integer.parseInt(DBConfig.instance().getProperty("max.expiry.minutes.allowed"));
               /* if(validMins > (long)MAX_EXPIRY)
                {
                    _sr.setCommandStatus(98);
                    logger.info((new StringBuilder()).append(systemId).append("[SubmitObejct] invalid validity period").toString());
                } else
                {
                    if(validMins <= 0L)
                    {
                        expiry = null;
                    } else
                    {
                        expiry = String.valueOf(validMins);
                    }
                    logger.info((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] validMins :").append(validMins).toString());
                }*/
            }
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] Error while finding validity time. Error: ").toString(), e);
        }
        try
        {
            //clientPriorityEnabled = Integer.parseInt(partnerDetails.get("PRIORITY_YN").toString()) == 1;
            //priorityFlag = Byte.toString(_sm.getPriority());
            /*if(priorityFlag != null && priorityFlag.length() > 0)
            {
                logger.info((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] priorityFlag : ").append(priorityFlag).toString());
                if(clientPriorityEnabled && Integer.parseInt(priorityFlag) > 0)
                {
                    priority = "1";
                } else
                {
                    priority = "0";
                }
            } else
            {
                priority = "0";
            }*/
        	
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] Error while finding dataCoding. Error: ").toString(), e);
        }
        try
        {
            dlr = new String("0");
            Integer dbDlr = null ; //(Integer)partnerDetails.get("DLR_TYPE");
            if(dbDlr != null && _sm.getRegisteredDelivery() > 0 && dbDlr.intValue() > 0)
            {
                dlr = new String(String.valueOf(dbDlr));
            }
            logger.info((new StringBuilder()).append(systemId).append(" [SubmitObject] delivery type : ").append(dlr).toString());
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] Error while finding register delivery. Error: ").toString(), e);
        }
        try
        {
            if(_sm.hasOptionalParameter((short)771) && _sm.getOptionalParameter((short)771) != null)
            {
                refid = _sm.getOptionalParameter((short)771).getValueAsString();
                logger.info((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] Customer reference id : ").append(refid).toString());
            }
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] Error while finding Customer reference id. Error: ").toString(), e);
        }
        try
        {
            if(_sm.hasOptionalParameter((short)514) && _sm.getOptionalParameter((short)514) != null)
            {
                billRef = _sm.getOptionalParameter((short)514).getValueAsString();
                logger.info((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] bill reference id : ").append(billRef).toString());
            }
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder()).append(systemId).append(" [SubmitObject][SubmitObejct] Error while finding Customer Bill Reference id. Er" +
"ror: "
).toString(), e);
        }
    }

    private boolean startWith91uppeder(String mobile)
    {
       /* StringTokenizer st = new StringTokenizer(DBConfig.instance().getProperty("91prefix.start.number"), "~");

        String startnumber;
        if(!st.hasMoreTokens())
        {
            break MISSING_BLOCK_LABEL_58;
        }
        startnumber = st.nextToken();
        if(mobile.startsWith(startnumber))
        {
            return true;
        }
          goto _L1
        Exception ignore;
        ignore;
        logger.error("startWithInternational ", ignore);*/
        return true;
    }

    private void spiltMsgWithHeader(String msgWithHeader)
    {
        String headerLen = msgWithHeader.substring(0, 2);
        int totLen = Integer.parseInt(headerLen, 16);
        if(logger.isDebugEnabled())
        {
            logger.debug((new StringBuilder()).append("[SubmitObject][spiltMsgWithHeader] Header Length :").append(totLen).toString());
        }
        udh = msgWithHeader.substring(0, totLen * 2 + 2);
        logger.info((new StringBuilder()).append("[SubmitObject][spiltMsgWithHeader] UDH :").append(udh).toString());
        //int maxudhlength = Integer.parseInt(DBConfig.instance().getProperty("max.udh.length.allowed"));
       /* if(udh != null && udh.length() > maxudhlength)
        {
            udh = udh.substring(0, maxudhlength);
        }*/
        message = msgWithHeader.substring(udh.length(), msgWithHeader.length());
        logger.info((new StringBuilder()).append("[SubmitObject][spiltMsgWithHeader] message:").append(message).toString());
    }

    private void checkMsgType(String dcs)
    {
        int _dcs = Integer.parseInt(dcs);
        switch(_dcs)
        {
        case 11: // '\013'
            msgType = "BM";
            break;

        case -11: 
            msgType = "BM";
            break;

        case 4: // '\004'
            msgType = "BM";
            break;

        case 12: // '\f'
            msgType = "SP";
            break;

        case 8: // '\b'
            msgType = "UC";
            break;

        case 16: // '\020'
            msgType = "FL";
            break;

        case -16: 
            msgType = "FL";
            break;

        case 18: // '\022'
            msgType = "FU";
            break;

        case 24: // '\030'
            msgType = "FU";
            break;

        case 0: // '\0'
            msgType = "PM";
            break;

        default:
            msgType = "PM";
            break;
        }
    }

    public String getOdRequestId()
    {
        return odRequestId;
    }

    public void setOdRequestId(String odRequestId)
    {
        this.odRequestId = odRequestId;
    }

    public String getPriority()
    {
        return priority;
    }

    public void setPriority(String priority)
    {
        this.priority = priority;
    }

    public String getInstanceid()
    {
        return instanceid;
    }

    public void setInstanceid(String instanceid)
    {
        this.instanceid = instanceid;
    }

    public String prefix91(String pid, String aid, String mobile)
    {
        if(logger.isDebugEnabled())
        {
            logger.debug((new StringBuilder()).append("prefix91()...pid : ").append(pid).append(" aid : ").append(aid).append(" mobile : ").append(mobile).toString());
        }
       // Map partnerDetails = PushAccount.instance().getPushAccount(pid, aid);
      /*  if(mobile.length() == 10 && Integer.parseInt(partnerDetails.get("INTL_DELIVERY").toString()) == 0 && Integer.parseInt(partnerDetails.get("LANDLINE_DELIVERY").toString()) == 0)
        {
            if(logger.isDebugEnabled())
            {
                logger.debug("prefix91() *** Prefix 91 for the mobile number****");
            }
            mobile = (new StringBuilder()).append("91").append(mobile).toString();
        } else
        if(mobile.length() == 10 && Integer.parseInt(partnerDetails.get("INTL_DELIVERY").toString()) == 1 && Integer.parseInt(partnerDetails.get("PREFIX91_YN").toString()) == 1 && startWith91uppeder(mobile))
        {
            logger.debug("prefix91() *** Prefix 91 for the mobile number****");
            mobile = (new StringBuilder()).append("91").append(mobile).toString();
            if(logger.isDebugEnabled())
            {
                logger.debug((new StringBuilder()).append("Prefix 91 DestAddr -  - ").append(mobile).toString());
            }
        }*/
        return mobile;
    }

    public String decodeHexString(String hexText)
        throws Exception
    {
        String decodedText = null;
        String chunk = null;
        PropertiesConfiguration pc = null;
        String identifier = "[SubmitObject] decodeHexString ";
        try
        {
          //  pc = PropertyFileLoaderTon.getInstance().getPropertiesConfiguration("ng3.properties.loc");
            if(hexText != null && hexText.length() > 0)
            {
                int numBytes = hexText.length() / 2;
                byte rawToByte[] = new byte[numBytes];
                int offset = 0;
                int bCounter = 0;
                for(int i = 0; i < numBytes; i++)
                {
                    chunk = hexText.substring(offset, offset + 2);
                    offset += 2;
                    rawToByte[i] = (byte)(Integer.parseInt(chunk, 16) & 0xff);
                }

                decodedText = new String(rawToByte);
            }
        }
        catch(Exception e)
        {
            logger.error("[SubmitObject]Exception - ", e);
          //  (new ERRLogger()).insert(e, 0, "", "DECODE HEX STRING", pc.getString("instance.id"));
            decodedText = hexText;
          //  TrapSender.sendTrap("PREROUTER", null, 131, (new StringBuilder()).append("Classes MsgUtils decodeHexString module:Not able to decode hexstring: ").append(hexText).toString(), e);
        }
        return decodedText;
    }

}
