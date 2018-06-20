package com.noesis.utils;

public class ServerRequestCounter {

    /* member class not found */
	private static class SingletonHolder
	{
	    public static final ServerRequestCounter INSTANCE = new ServerRequestCounter(null);
	    private SingletonHolder()
	    {
	    }
	}



    long submitSm;
    long submitSmResp;
    long enquireLink;
    long deliverSm;
    long deliverSmResp;

    public long getSubmitSm()
    {
        return submitSm;
    }

    public void setSubmitSm(long submitSm)
    {
        this.submitSm = submitSm;
    }

    public long getSubmitSmResp()
    {
        return submitSmResp;
    }

    public void setSubmitSmResp(long submitSmResp)
    {
        this.submitSmResp = submitSmResp;
    }

    public long getEnquireLink()
    {
        return enquireLink;
    }

    public void setEnquireLink(long enquireLink)
    {
        this.enquireLink = enquireLink;
    }

    public long getDeliverSm()
    {
        return deliverSm;
    }

    public void setDeliverSm(long deliverSm)
    {
        this.deliverSm = deliverSm;
    }

    public long getDeliverSmResp()
    {
        return deliverSmResp;
    }

    public void setDeliverSmResp(long deliverSmResp)
    {
        this.deliverSmResp = deliverSmResp;
    }

    private ServerRequestCounter()
    {
    }

    public static ServerRequestCounter getInstance()
    {
        return SingletonHolder.INSTANCE;
    }

    public void reset()
    {
        submitSm = 0L;
        submitSmResp = 0L;
        enquireLink = 0L;
        deliverSm = 0L;
        deliverSmResp = 0L;
    }

    ServerRequestCounter(Object obj)
    {
        this();
    }
}
