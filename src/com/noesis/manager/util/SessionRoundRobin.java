package com.noesis.manager.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

//import com.a2wi.ng.vsmsc.util.ServerRequestCounter;
import com.cloudhopper.smpp.SmppServerSession;
import com.cloudhopper.smpp.impl.DefaultSmppSession;
import com.noesis.event.handlers.SessionEventHandler;
import com.noesis.utils.ServerRequestCounter;

public class SessionRoundRobin
{

    AtomicInteger ai;
    AtomicInteger availableIndex;
    List sessionHandlers;
    int sleepIncFact;
    int minSleep;
    String systemId;
    Logger logger;

    public SessionRoundRobin()
    {
        ai = new AtomicInteger(-1);
        availableIndex = new AtomicInteger(-1);
        sessionHandlers = Collections.synchronizedList(new LinkedList());
        sleepIncFact = 2;
        minSleep = 25;
        logger = Logger.getLogger(SessionRoundRobin.class);
    }

    public List getSessionHandlers()
    {
        return sessionHandlers;
    }

    public void setSessionHandlers(LinkedList sessionHandlers)
    {
        this.sessionHandlers = sessionHandlers;
    }

    public boolean addSession(SessionEventHandler aSessionHandler)
        throws Exception
    {
        boolean added = false;
        if(aSessionHandler != null)
        {
            if(systemId == null || systemId.equals(aSessionHandler.getSession().getConfiguration().getSystemId()))
            {
                added = sessionHandlers.add(aSessionHandler);
            } else
            {
                throw new Exception((new StringBuilder()).append(systemId).append(" round robin won't fit ").append(aSessionHandler.getSession().getConfiguration().getSystemId()).toString());
            }
        }
        return added;
    }

    public synchronized SessionEventHandler getAvailableSession()
        throws Exception
    {
        int index = availableIndex.addAndGet(1);
        SessionEventHandler aSessionHandler = null;
        if(sessionHandlers.size() == 0)
        {
            throw new Exception("no bind exists...");
        }
        if(index > sessionHandlers.size() - 1)
        {
            availableIndex.set(0);
            index = 0;
        }
        if(logger.isInfoEnabled())
        {
            logger.info((new StringBuilder()).append("returning session from index>>>").append(index).append(" for size=").append(sessionHandlers.size()).toString());
        }
        aSessionHandler = (SessionEventHandler)sessionHandlers.get(index);
        if(!aSessionHandler.isInUse())
        {
            aSessionHandler.setInUse(true);
            return aSessionHandler;
        }
        aSessionHandler = null;
        Iterator i$ = sessionHandlers.iterator();
        do
        {
            if(!i$.hasNext())
            {
                break;
            }
            SessionEventHandler handler = (SessionEventHandler)i$.next();
            if(handler.isInUse())
            {
                continue;
            }
            aSessionHandler = handler;
            handler.setInUse(true);
            break;
        } while(true);
        return aSessionHandler;
    }

    public synchronized SessionEventHandler getNextSession()
    {
        int index = ai.addAndGet(1);
        SessionEventHandler aSessionHandler = null;
        if(index > sessionHandlers.size() - 1)
        {
            ai.set(0);
            index = 0;
        }
        if(logger.isDebugEnabled())
        {
            logger.debug((new StringBuilder()).append("sessions size=").append(sessionHandlers.size()).toString());
            logger.debug((new StringBuilder()).append("sessions index=").append(index).toString());
        }
        try
        {
            aSessionHandler = (SessionEventHandler)sessionHandlers.get(index);
        }
        catch(IndexOutOfBoundsException aie)
        {	aie.printStackTrace();
            try
            {
                ai.set(0);
                index = 0;
                aSessionHandler = (SessionEventHandler)sessionHandlers.get(0);
            }
            catch(IndexOutOfBoundsException ignore) { }
        }
        if(aSessionHandler != null)
        {
            if(!aSessionHandler.isInUse())
            {
                if(logger.isDebugEnabled())
                {
                    logger.debug((new StringBuilder()).append("**used index=").append(index).toString());
                }
                aSessionHandler.setInUse(true);
            } else
            {
                try
                {
                    int indx = ai.addAndGet(1);
                    do
                    {
                        if(indx >= sessionHandlers.size())
                        {
                            break;
                        }
                        aSessionHandler = (SessionEventHandler)sessionHandlers.get(indx);
                        if(!aSessionHandler.isInUse())
                        {
                            if(logger.isDebugEnabled())
                            {
                                logger.debug((new StringBuilder()).append("used index=").append(indx).toString());
                            }
                            break;
                        }
                        indx = ai.addAndGet(1);
                    } while(true);
                }
                catch(IndexOutOfBoundsException ignore) { }
                if(!aSessionHandler.isInUse())
                {
                    aSessionHandler.setInUse(true);
                } else
                {
                    try
                    {
                        minSleep *= sleepIncFact;
                        logger.warn((new StringBuilder()).append("going for sleep as all sessions are busy...").append(minSleep).toString());
                        Thread.sleep(minSleep);
                        ai.set(0);
                        aSessionHandler = getNextSession();
                    }
                    catch(InterruptedException ignore) { }
                }
            }
        }
        if(aSessionHandler == null && logger.isInfoEnabled())
        {
            logger.error("Null session found not able to return session to send deliver sm!!!!");
        }
        minSleep = 25;
        return aSessionHandler;
    }

    public boolean removeSession(SmppServerSession session)
    {
        boolean removed;
label0:
        {
            removed = false;
            if(session == null)
            {
                break label0;
            }
            Iterator i$ = sessionHandlers.iterator();
            SessionEventHandler aSessionHandler;
            do
            {
                if(!i$.hasNext())
                {
                    break label0;
                }
                aSessionHandler = (SessionEventHandler)i$.next();
            } while(aSessionHandler.getSession() != session);
            removed = sessionHandlers.remove(aSessionHandler);
        }
        return removed;
    }

    public synchronized int unbindExpired(long expiryTime)
    {
        int unboundCount = 0;
        LinkedList unbindList = new LinkedList();
        Iterator i$ = sessionHandlers.iterator();
        do
        {
            if(!i$.hasNext())
            {
                break;
            }
            SessionEventHandler anHandler = (SessionEventHandler)i$.next();
            if(logger.isDebugEnabled())
            {
                logger.debug((new StringBuilder()).append("looping systemid=").append(anHandler.getSystemId()).toString());
                logger.debug((new StringBuilder()).append("for expiryTime=").append(expiryTime).append(" :  lastUsed=").append(anHandler.getLastUsedTime()).toString());
                logger.debug((new StringBuilder()).append("System.currentTimeMillis()-anHandler.getLastUsedTime().getTime()=").append(System.currentTimeMillis() - anHandler.getLastUsedTime().getTime()).toString());
            }
            if(anHandler.getSession().getStateName().equals("CLOSED") || expiryTime <= System.currentTimeMillis() - anHandler.getLastUsedTime().getTime())
            {
                unbindList.add(anHandler);
            }
        } while(true);
        SessionEventHandler anHandler;
        for(i$ = unbindList.iterator(); i$.hasNext(); anHandler.getSession().destroy())
        {
            anHandler = (SessionEventHandler)i$.next();
            anHandler.setExpired(true);
            DefaultSmppSession session = (DefaultSmppSession)anHandler.getSession();
            ServerRequestCounter counter = ServerRequestCounter.getInstance();
            counter.setDeliverSm(counter.getDeliverSm() + (long)session.getCounters().getTxDeliverSM().getRequest());
            counter.setDeliverSmResp(counter.getDeliverSmResp() + (long)session.getCounters().getTxDeliverSM().getResponse());
            counter.setEnquireLink(counter.getEnquireLink() + (long)session.getCounters().getRxEnquireLink().getResponse());
            counter.setSubmitSm(counter.getSubmitSm() + (long)session.getCounters().getRxSubmitSM().getRequest());
            counter.setSubmitSmResp(counter.getSubmitSmResp() + (long)session.getCounters().getRxSubmitSM().getResponse());
            session.resetCounters();
            anHandler.getSession().close();
        }

        unboundCount = unbindList.size();
        return unboundCount;
    }

    public void resetCounters()
    {
        SessionEventHandler anHandler;
        for(Iterator i$ = sessionHandlers.iterator(); i$.hasNext(); anHandler.getSession().getCounters().reset())
        {
            anHandler = (SessionEventHandler)i$.next();
        }

    }

    public int getHandlersCount()
    {
        return sessionHandlers.size();
    }
}
