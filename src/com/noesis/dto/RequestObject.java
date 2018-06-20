package com.noesis.dto;

import java.io.Serializable;
import java.util.Date;

public class RequestObject implements Serializable
{

private static final long serialVersionUID = 1L;
String pcode;
String acode;
String pin;
String mnumber;
String message;
String signature;
String lang;
String scheTime;
String odreqid;
String prty;
String msgType;
String udh;
String port;
String cntry;
String dlr;
String expiry;
String splitAlgm;
String refid;
String pid;
String aid;
String ackid;
String rts;
String msgsrc;
String billRef;
String custIP;
String sdTableName;
String msgclass;
String defertype;
String requestId;
String typeOfRequest;
boolean dndCheckRequired;
String msgDeliveryType;
String typeOfCountry;
int carrierId;
int circleId;
String billingType;
String featureCd;
String iciciXML;
boolean isBlockout;
int priority;
Date priorityTime;
int countrycd;
int servicetype;
String connectorid;
String tablename;
String instanceid;

public RequestObject()
{
    pcode = null;
    acode = null;
    pin = null;
    mnumber = null;
    message = null;
    signature = null;
    lang = null;
    scheTime = null;
    odreqid = null;
    prty = null;
    msgType = null;
    udh = null;
    port = null;
    cntry = null;
    dlr = null;
    expiry = null;
    splitAlgm = null;
    refid = null;
    pid = null;
    aid = null;
    ackid = null;
    rts = null;
    msgsrc = null;
    billRef = null;
    custIP = null;
    sdTableName = null;
    msgclass = null;
    defertype = null;
    requestId = null;
    typeOfRequest = null;
    dndCheckRequired = false;
    msgDeliveryType = null;
    typeOfCountry = null;
    carrierId = 0;
    circleId = 0;
    billingType = null;
    featureCd = null;
    iciciXML = null;
    isBlockout = false;
    priority = 0;
}

public RequestObject(String ackid, String mnumber, String message, String rts, String pin, String refid)
{
    pcode = null;
    acode = null;
    this.pin = null;
    this.mnumber = null;
    this.message = null;
    signature = null;
    lang = null;
    scheTime = null;
    odreqid = null;
    prty = null;
    msgType = null;
    udh = null;
    port = null;
    cntry = null;
    dlr = null;
    expiry = null;
    splitAlgm = null;
    this.refid = null;
    pid = null;
    aid = null;
    this.ackid = null;
    this.rts = null;
    msgsrc = null;
    billRef = null;
    custIP = null;
    sdTableName = null;
    msgclass = null;
    defertype = null;
    requestId = null;
    typeOfRequest = null;
    dndCheckRequired = false;
    msgDeliveryType = null;
    typeOfCountry = null;
    carrierId = 0;
    circleId = 0;
    billingType = null;
    featureCd = null;
    iciciXML = null;
    isBlockout = false;
    priority = 0;
    this.ackid = ackid;
    this.mnumber = mnumber;
    this.message = message;
    this.rts = rts;
    this.pin = pin;
    this.refid = refid;
}

public String getIciciXML()
{
    return iciciXML;
}

public void setIciciXML(String iciciXML)
{
    this.iciciXML = iciciXML;
}

public RequestObject(String pcode, String acode, String pin, String mnumber, String message, String signature, String lang, 
        String scheTime, String odreqid, String prty, String msgType, String udh, String port, String cntry, 
        String dlr, String expiry, String splitAlgm, String refid, String pid, String aid, String ackid, 
        String rts, String msgsrc, String billRef, String custIP, String msgclass, String defertype, int priority, 
        Date priorityTime, String instanceid)
{
    this.pcode = null;
    this.acode = null;
    this.pin = null;
    this.mnumber = null;
    this.message = null;
    this.signature = null;
    this.lang = null;
    this.scheTime = null;
    this.odreqid = null;
    this.prty = null;
    this.msgType = null;
    this.udh = null;
    this.port = null;
    this.cntry = null;
    this.dlr = null;
    this.expiry = null;
    this.splitAlgm = null;
    this.refid = null;
    this.pid = null;
    this.aid = null;
    this.ackid = null;
    this.rts = null;
    this.msgsrc = null;
    this.billRef = null;
    this.custIP = null;
    sdTableName = null;
    this.msgclass = null;
    this.defertype = null;
    requestId = null;
    typeOfRequest = null;
    dndCheckRequired = false;
    msgDeliveryType = null;
    typeOfCountry = null;
    carrierId = 0;
    circleId = 0;
    billingType = null;
    featureCd = null;
    iciciXML = null;
    isBlockout = false;
    this.priority = 0;
    this.pcode = pcode;
    this.acode = acode;
    this.pin = pin;
    this.mnumber = mnumber;
    this.message = message;
    this.signature = signature;
    this.lang = lang;
    this.scheTime = scheTime;
    this.odreqid = odreqid;
    this.prty = prty;
    this.msgType = msgType;
    this.udh = udh;
    this.port = port;
    this.cntry = cntry;
    this.dlr = dlr;
    this.expiry = expiry;
    this.splitAlgm = splitAlgm;
    this.refid = refid;
    this.pid = pid;
    this.aid = aid;
    this.ackid = ackid;
    this.rts = rts;
    this.msgsrc = msgsrc;
    this.billRef = billRef;
    this.custIP = custIP;
    this.msgclass = msgclass;
    this.defertype = defertype;
    this.priority = priority;
    this.priorityTime = priorityTime;
    this.instanceid = instanceid;
}

public RequestObject(String mnumber, String message, String signature, String odreqid, String msgType, String pid, String aid, 
        String ackid, String rts, String msgsrc)
{
    pcode = null;
    acode = null;
    pin = null;
    this.mnumber = null;
    this.message = null;
    this.signature = null;
    lang = null;
    scheTime = null;
    this.odreqid = null;
    prty = null;
    this.msgType = null;
    udh = null;
    port = null;
    cntry = null;
    dlr = null;
    expiry = null;
    splitAlgm = null;
    refid = null;
    this.pid = null;
    this.aid = null;
    this.ackid = null;
    this.rts = null;
    this.msgsrc = null;
    billRef = null;
    custIP = null;
    sdTableName = null;
    msgclass = null;
    defertype = null;
    requestId = null;
    typeOfRequest = null;
    dndCheckRequired = false;
    msgDeliveryType = null;
    typeOfCountry = null;
    carrierId = 0;
    circleId = 0;
    billingType = null;
    featureCd = null;
    iciciXML = null;
    isBlockout = false;
    priority = 0;
    this.mnumber = mnumber;
    this.message = message;
    this.signature = signature;
    this.odreqid = odreqid;
    this.msgType = msgType;
    this.pid = pid;
    this.aid = aid;
    this.ackid = ackid;
    this.rts = rts;
    this.msgsrc = msgsrc;
}

public RequestObject(String mnumber, String message, String signature, String odreqid, String msgType, String pid, String aid, 
        String ackid, String rts, String msgsrc, int carrierId, int circleId, String typeOfCountry, String featureCd, 
        String splitAlgm, String requestId, int countrycd)
{
    pcode = null;
    acode = null;
    pin = null;
    this.mnumber = null;
    this.message = null;
    this.signature = null;
    lang = null;
    scheTime = null;
    this.odreqid = null;
    prty = null;
    this.msgType = null;
    udh = null;
    port = null;
    cntry = null;
    dlr = null;
    expiry = null;
    this.splitAlgm = null;
    refid = null;
    this.pid = null;
    this.aid = null;
    this.ackid = null;
    this.rts = null;
    this.msgsrc = null;
    billRef = null;
    custIP = null;
    sdTableName = null;
    msgclass = null;
    defertype = null;
    this.requestId = null;
    typeOfRequest = null;
    dndCheckRequired = false;
    msgDeliveryType = null;
    this.typeOfCountry = null;
    this.carrierId = 0;
    this.circleId = 0;
    billingType = null;
    this.featureCd = null;
    iciciXML = null;
    isBlockout = false;
    priority = 0;
    this.mnumber = mnumber;
    this.message = message;
    this.signature = signature;
    this.odreqid = odreqid;
    this.msgType = msgType;
    this.pid = pid;
    this.aid = aid;
    this.ackid = ackid;
    this.rts = rts;
    this.msgsrc = msgsrc;
    this.carrierId = carrierId;
    this.circleId = circleId;
    this.typeOfCountry = typeOfCountry;
    this.featureCd = featureCd;
    this.splitAlgm = splitAlgm;
    this.requestId = requestId;
    this.countrycd = countrycd;
}

public String getPcode()
{
    return pcode;
}

public String getAcode()
{
    return acode;
}

public String getPin()
{
    return pin;
}

public String getMnumber()
{
    return mnumber;
}

public String getMessage()
{
    return message;
}

public String getSignature()
{
    return signature;
}

public String getLang()
{
    return lang;
}

public String getScheTime()
{
    return scheTime;
}

public String getOdreqid()
{
    return odreqid;
}

public String getPrty()
{
    return prty;
}

public String getMsgType()
{
    return msgType;
}

public String getUdh()
{
    return udh;
}

public String getPort()
{
    return port;
}

public String getCntry()
{
    return cntry;
}

public String getDlr()
{
    return dlr;
}

public String getExpiry()
{
    return expiry;
}

public String getSplitAlgm()
{
    return splitAlgm;
}

public String getRefid()
{
    return refid;
}

public String getPid()
{
    return pid;
}

public String getAid()
{
    return aid;
}

public String getAckid()
{
    return ackid;
}

public String getRts()
{
    return rts;
}

public String getMsgsrc()
{
    return msgsrc;
}

public String getBillRef()
{
    return billRef;
}

public String getCustIP()
{
    return custIP;
}

public void setAckid(String ackid)
{
    this.ackid = ackid;
}

public void setAcode(String acode)
{
    this.acode = acode;
}

public void setAid(String aid)
{
    this.aid = aid;
}

public void setBillRef(String billRef)
{
    this.billRef = billRef;
}

public void setCntry(String cntry)
{
    this.cntry = cntry;
}

public void setCustIP(String custIP)
{
    this.custIP = custIP;
}

public void setDlr(String dlr)
{
    this.dlr = dlr;
}

public void setExpiry(String expiry)
{
    this.expiry = expiry;
}

public void setLang(String lang)
{
    this.lang = lang;
}

public void setMessage(String message)
{
    this.message = message;
}

public void setMnumber(String mnumber)
{
    this.mnumber = mnumber;
}

public void setMsgsrc(String msgsrc)
{
    this.msgsrc = msgsrc;
}

public void setMsgType(String msgType)
{
    this.msgType = msgType;
}

public void setOdreqid(String odreqid)
{
    this.odreqid = odreqid;
}

public void setPcode(String pcode)
{
    this.pcode = pcode;
}

public void setPid(String pid)
{
    this.pid = pid;
}

public void setPin(String pin)
{
    this.pin = pin;
}

public void setPort(String port)
{
    this.port = port;
}

public void setPrty(String prty)
{
    this.prty = prty;
}

public void setRefid(String refid)
{
    this.refid = refid;
}

public void setRts(String rts)
{
    this.rts = rts;
}

public void setScheTime(String scheTime)
{
    this.scheTime = scheTime;
}

public void setSignature(String signature)
{
    this.signature = signature;
}

public void setSplitAlgm(String splitAlgm)
{
    this.splitAlgm = splitAlgm;
}

public void setUdh(String udh)
{
    this.udh = udh;
}

public String getSdTableName()
{
    return sdTableName;
}

public void setSdTableName(String sdTableName)
{
    this.sdTableName = sdTableName;
}

public String getBillingType()
{
    return billingType;
}

public void setBillingType(String billingType)
{
    this.billingType = billingType;
}

public int getCarrierId()
{
    return carrierId;
}

public void setCarrierId(int carrierId)
{
    this.carrierId = carrierId;
}

public int getCircleId()
{
    return circleId;
}

public void setCircleId(int circleId)
{
    this.circleId = circleId;
}

public boolean isDndCheckRequired()
{
    return dndCheckRequired;
}

public void setDndCheckRequired(boolean dndCheckRequired)
{
    this.dndCheckRequired = dndCheckRequired;
}

public String getMsgDeliveryType()
{
    return msgDeliveryType;
}

public void setMsgDeliveryType(String msgDeliveryType)
{
    this.msgDeliveryType = msgDeliveryType;
}

public String getRequestId()
{
    return requestId;
}

public void setRequestId(String requestId)
{
    this.requestId = requestId;
}

public String getTypeOfCountry()
{
    return typeOfCountry;
}

public void setTypeOfCountry(String typeOfCountry)
{
    this.typeOfCountry = typeOfCountry;
}

public String getTypeOfRequest()
{
    return typeOfRequest;
}

public void setTypeOfRequest(String typeOfRequest)
{
    this.typeOfRequest = typeOfRequest;
}

public String getFeatureCd()
{
    return featureCd;
}

public void setFeatureCd(String featureCd)
{
    this.featureCd = featureCd;
}

public String toString()
{
    StringBuffer buffer = new StringBuffer();
    buffer.append((new StringBuilder()).append("pcode - ").append(pcode).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("acode - ").append(acode).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("pin\t - ").append(pin).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("mnumber - ").append(mnumber).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("message - ").append(message).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("signature - ").append(signature).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("lang - ").append(lang).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("scheTime - ").append(scheTime).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("odreqid - ").append(odreqid).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("prty - ").append(prty).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("msgType - ").append(msgType).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("udh - ").append(udh).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("port - ").append(port).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("cntry - ").append(cntry).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("dlr - ").append(dlr).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("expiry - ").append(expiry).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("splitAlgm - ").append(splitAlgm).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("refid - ").append(refid).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("pid - ").append(pid).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("aid - ").append(aid).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("ackid - ").append(ackid).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("rts - ").append(rts).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("msgsrc - ").append(msgsrc).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("billRef - ").append(billRef).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("custIP - ").append(custIP).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("sdTableName - ").append(sdTableName).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("requestId - ").append(requestId).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("typeOfRequest - ").append(typeOfRequest).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("dndCheckRequired - ").append(dndCheckRequired).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("msgDeliveryType - ").append(msgDeliveryType).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("typeOfCountry - ").append(typeOfCountry).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("carrierId - ").append(carrierId).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("circleId - ").append(circleId).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("billingType - ").append(billingType).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("featureCd - ").append(featureCd).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("iciciXML - ").append(iciciXML).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("msgclass - ").append(msgclass).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("defertype - ").append(defertype).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("priority - ").append(priority).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("Priority Time - ").append(priorityTime).toString());
    buffer.append("\t");
    buffer.append((new StringBuilder()).append("Instance Id - ").append(instanceid).toString());
    return buffer.toString();
}

public boolean isBlockout()
{
    return isBlockout;
}

public void setBlockout(boolean isBlockout)
{
    this.isBlockout = isBlockout;
}

public int getCountrycd()
{
    return countrycd;
}

public void setCountrycd(int countrycd)
{
    this.countrycd = countrycd;
}

public int getServicetype()
{
    return servicetype;
}

public void setServicetype(int servicetype)
{
    this.servicetype = servicetype;
}

public String getMsgclass()
{
    return msgclass;
}

public void setMsgclass(String msgclass)
{
    this.msgclass = msgclass;
}

public String getDefertype()
{
    return defertype;
}

public void setDefertype(String defertype)
{
    this.defertype = defertype;
}

public int getPriority()
{
    return priority;
}

public void setPriority(int priority)
{
    this.priority = priority;
}

public Date getPriorityTime()
{
    return priorityTime;
}

public void setPriorityTime(Date priorityTime)
{
    this.priorityTime = priorityTime;
}

public String getConnectorid()
{
    return connectorid;
}

public void setConnectorid(String connectorid)
{
    this.connectorid = connectorid;
}

public String getTablename()
{
    return tablename;
}

public void setTablename(String tablename)
{
    this.tablename = tablename;
}

public String getInstanceid()
{
    return instanceid;
}

public void setInstanceid(String instanceid)
{
    this.instanceid = instanceid;
}
}
