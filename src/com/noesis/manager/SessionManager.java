package com.noesis.manager;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

//import com.a2wi.ng.util.misc.DBConfig;
//import com.a2wi.ng.util.misc.DBConfig;
//import com.a2wi.ng.vsmsc.dn.workers.CustomerRedisQWorker;
//import com.a2wi.ng.vsmsc.dn.workers.SessionRedisQWorker;
//import com.a2wi.ng.vsmsc.manager.util.SessionRoundRobin;
//import com.a2wi.ng.vsmsc.util.InMemAccInfo;
import com.cloudhopper.smpp.SmppBindType;
import com.cloudhopper.smpp.SmppServerSession;
import com.noesis.event.handlers.SessionEventHandler;
import com.noesis.manager.util.SessionRoundRobin;

public class SessionManager
{
    /* member class not found */
	private static class SingletonHolder
	{
	    public static final SessionManager INSTANCE = new SessionManager();
	    private SingletonHolder ()
	    {
	    }
	}

    ConcurrentHashMap txSessionsMap;
    ConcurrentHashMap rxTrxSessionsMap;
    private static Logger logger = Logger.getLogger(SessionManager.class);
    boolean sessionThreadEnabled;
    Map dnWorkerMap;

    public Map getDnWorkerMap()
    {
        return dnWorkerMap;
    }

    private SessionManager()
    {
        txSessionsMap = new ConcurrentHashMap();
        rxTrxSessionsMap = new ConcurrentHashMap();
        sessionThreadEnabled = false;
        dnWorkerMap = new ConcurrentHashMap();
        try
        {
            //sessionThreadEnabled = Integer.parseInt(DBConfig.instance().getProperty("dn.session.thread.enabled")) == 1;
        	sessionThreadEnabled = true;
            logger.info((new StringBuilder()).append("sessionThreadEnabled==").append(sessionThreadEnabled).toString());
        }
        catch(Exception exp)
        {
            logger.error("problem checking sessionThreadEnabled due to", exp);
        }
    }

    public static SessionManager getInstance()
    {
        return SingletonHolder.INSTANCE;
    }

    public synchronized int getTotalBindCount()
    {
        Collection txVals = txSessionsMap.values();
        int totalBinds = 0;
        for(Iterator i$ = txVals.iterator(); i$.hasNext();)
        {
            SessionRoundRobin arr = (SessionRoundRobin)i$.next();
            totalBinds += arr.getHandlersCount();
        }

        Collection rxtrxVals = rxTrxSessionsMap.values();
        for(Iterator i$ = rxtrxVals.iterator(); i$.hasNext();)
        {
            SessionRoundRobin arr = (SessionRoundRobin)i$.next();
            totalBinds += arr.getHandlersCount();
        }

        return totalBinds;
    }

    public synchronized void addSession(SessionEventHandler aSessionHandler)
        throws Exception
    {
        String systemId = aSessionHandler.getSystemId();
        ConcurrentHashMap sessionsMap = null;
        if(aSessionHandler.getSession().getBindType().equals(SmppBindType.TRANSMITTER))
        {
            sessionsMap = txSessionsMap;
        } else
        {
            sessionsMap = rxTrxSessionsMap;
        }
        SessionRoundRobin srrObj = (SessionRoundRobin)sessionsMap.get(systemId);
        logger.info("Bind Type: "+aSessionHandler.getSession().getBindType() + " AND sessionThreadEnabled is : "+sessionThreadEnabled);
        if(!aSessionHandler.getSession().getBindType().equals(SmppBindType.TRANSMITTER) && sessionThreadEnabled)
        {
        	
          /*  Map accInfo = InMemAccInfo.getInstance().getAccountDetail(systemId, 1);
            if(accInfo == null)
            {
                accInfo = InMemAccInfo.getInstance().getAccountDetail(systemId, 9);
            }
            SessionRedisQWorker sworker = new SessionRedisQWorker(accInfo.get("AID").toString(), systemId, accInfo.get("PID").toString(), accInfo.get("PCODE").toString(), aSessionHandler);
           */ 
        	/*aSessionHandler.setInUse(true);
            LinkedList list = (LinkedList)dnWorkerMap.get(systemId);
            if(list == null)
            {
                list = new LinkedList();
                dnWorkerMap.put(systemId, list);
            }*/
            //list.add(sworker);
            //sworker.start();
        }
        logger.info("Srr Object: "+srrObj);
        if(srrObj != null)
        {
        	logger.info("Srr Object: "+srrObj);
            srrObj.addSession(aSessionHandler);
        } else
        {
        	logger.info("Srr Object: "+srrObj);
        	srrObj = new SessionRoundRobin();
            srrObj.addSession(aSessionHandler);
            logger.info("Adding Session into Map: "+systemId+" And obj is: "+srrObj);
            sessionsMap.put(systemId, srrObj);
            if(!aSessionHandler.getSession().getBindType().equals(SmppBindType.TRANSMITTER) && !sessionThreadEnabled)
            {
               /* Map accInfo = InMemAccInfo.getInstance().getAccountDetail(systemId, 1);
                if(accInfo == null)
                {
                    accInfo = InMemAccInfo.getInstance().getAccountDetail(systemId, 9);
                }
                CustomerRedisQWorker worker = new CustomerRedisQWorker(accInfo.get("AID").toString(), systemId, accInfo.get("PID").toString(), accInfo.get("PCODE").toString());
                LinkedList list = (LinkedList)dnWorkerMap.get(systemId);
                if(list == null)
                {
                    list = new LinkedList();
                }
                list.add(worker);
                dnWorkerMap.put(systemId, list);
                worker.start();*/
            }
        }
    }

    public synchronized void removeSession(SmppServerSession session)
    {
        String systemId = session.getConfiguration().getSystemId();
        ConcurrentHashMap sessionsMap = null;
        if(session.getBindType().equals(SmppBindType.TRANSMITTER))
        {
            sessionsMap = txSessionsMap;
        } else
        {
            sessionsMap = rxTrxSessionsMap;
        }
        SessionRoundRobin srrObj = (SessionRoundRobin)sessionsMap.get(systemId);
        if(srrObj != null)
        {
            logger.info((new StringBuilder()).append("session removed for systemid=").append(systemId).append(" is ").append(srrObj.removeSession(session)).toString());
            if(srrObj.getHandlersCount() == 0)
            {
                sessionsMap.remove(systemId);
                if(dnWorkerMap.containsKey(systemId) && !sessionThreadEnabled)
                {
                   // ((CustomerRedisQWorker)((LinkedList)dnWorkerMap.get(systemId)).get(0)).setDone(true);
                    dnWorkerMap.remove(systemId);
                }
            }
        }
    }

    public SessionEventHandler getSession(String systemId)
    {
        SessionRoundRobin srrObj = (SessionRoundRobin)rxTrxSessionsMap.get(systemId);
        SessionEventHandler aSessionHandler = null;
        if(srrObj != null)
        {
            aSessionHandler = srrObj.getNextSession();
            if(aSessionHandler != null)
            {
                aSessionHandler.updateLastUsedTime();
            }
        }
        return aSessionHandler;
    }

    public synchronized SessionEventHandler getAvailableSession(String systemId)
        throws Exception
    {
        SessionRoundRobin srrObj = (SessionRoundRobin)rxTrxSessionsMap.get(systemId);
        SessionEventHandler aSessionHandler = null;
        if(srrObj != null)
        {
            aSessionHandler = srrObj.getAvailableSession();
        } else
        {
            throw new Exception("no bind exists");
        }
        return aSessionHandler;
    }

    public int getRxHandlersCount(String systemId)
    {
        int count = -1;
        SessionRoundRobin srrObj = (SessionRoundRobin)rxTrxSessionsMap.get(systemId);
        if(srrObj != null)
        {
            count = srrObj.getHandlersCount();
        }
        return count;
    }

    public int removeExpiredSessions(long expiryMillis)
    {
        int totalRemovedSession = 0;
        Collection col1 = txSessionsMap.values();
        for(Iterator i$ = col1.iterator(); i$.hasNext();)
        {
            SessionRoundRobin srr = (SessionRoundRobin)i$.next();
            totalRemovedSession += srr.unbindExpired(expiryMillis);
        }

        Collection col2 = rxTrxSessionsMap.values();
        for(Iterator i$ = col2.iterator(); i$.hasNext();)
        {
            SessionRoundRobin srr = (SessionRoundRobin)i$.next();
            totalRemovedSession += srr.unbindExpired(expiryMillis);
        }

        return totalRemovedSession;
    }

    public ConcurrentHashMap getTxSessionsMap()
    {
        return txSessionsMap;
    }

    public ConcurrentHashMap getRxTrxSessionsMap()
    {
        return rxTrxSessionsMap;
    }

    public int getTotalSessionCount(String systemId, int commandId)
    {
        int sessionCount = 0;
        if(commandId == 2)
        {
            SessionRoundRobin srr = (SessionRoundRobin)txSessionsMap.get(systemId);
            if(srr != null)
            {
                sessionCount = srr.getHandlersCount();
            }
        } else
        {
            SessionRoundRobin srr2 = (SessionRoundRobin)rxTrxSessionsMap.get(systemId);
            if(srr2 != null)
            {
                sessionCount = srr2.getHandlersCount();
            }
        }
        return sessionCount;
    }

    public void resetCounters()
    {
        Collection col1 = txSessionsMap.values();
        SessionRoundRobin srr;
        for(Iterator i$ = col1.iterator(); i$.hasNext(); srr.resetCounters())
        {
            srr = (SessionRoundRobin)i$.next();
        }

        Collection col2 = rxTrxSessionsMap.values();
        for(Iterator i$ = col2.iterator(); i$.hasNext(); srr.resetCounters())
        {
            srr = (SessionRoundRobin)i$.next();
        }

    }

    SessionManager(Object obj)
    {
        this();
    }

    
}
