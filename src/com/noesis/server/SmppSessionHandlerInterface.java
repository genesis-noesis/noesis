package com.noesis.server;

import com.cloudhopper.smpp.SmppServerSession;
import com.cloudhopper.smpp.SmppSessionHandler;
import com.cloudhopper.smpp.pdu.BaseBindResp;
import com.cloudhopper.smpp.type.SmppProcessingException;

public interface SmppSessionHandlerInterface
{

    public abstract SmppSessionHandler sessionCreated(Long long1, SmppServerSession smppserversession, BaseBindResp basebindresp)
        throws SmppProcessingException;

    public abstract void sessionDestroyed(Long long1, SmppServerSession smppserversession);
}
