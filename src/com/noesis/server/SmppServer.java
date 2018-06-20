package com.noesis.server;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import javax.management.MBeanServer;

import org.apache.log4j.Logger;

import com.cloudhopper.smpp.SmppServerConfiguration;
import com.cloudhopper.smpp.SmppServerHandler;
import com.cloudhopper.smpp.impl.DefaultSmppServer;
import com.cloudhopper.smpp.type.SmppChannelException;
import com.noesis.event.handlers.SmppSessionBindUnbindHandler;

// Referenced classes of package com.a2wi.ng.vsmsc.server:
//            DefaultSmppServerHandler

public class SmppServer
{
	private static class SingletonHolder
	{
	    public static final SmppServer INSTANCE = new SmppServer();
	    private SingletonHolder() {}
	}



    private static final Logger logger = Logger.getLogger(SmppServer.class);
    private String name;
    private int port;
    private long bindTimeout;
    private String systemId;
    private boolean autoNegotiateInterfaceVersion;
    private double interfaceVersion;
    private int maxConnectionSize;
    private int defaultWindowSize;
    private long defaultWindowWaitTimeout;
    private long defaultRequestExpiryTimeout;
    private long defaultWindowMonitorInterval;
    private boolean defaultSessionCountersEnabled;
    private long startUpTime;
    private DefaultSmppServer defaultSmppServer;
    private SmppServerHandler smppServerHandler;
    private MBeanServer mbeanServer;

    private SmppServer()
    {
        name = "SmppServer";
        port = 2776;
        bindTimeout = 30000L;
        systemId = "Noesis SMSC";
        autoNegotiateInterfaceVersion = true;
        interfaceVersion = 3.3999999999999999D;
        maxConnectionSize = 100;
        defaultWindowSize = 1;
        defaultWindowWaitTimeout = 60000L;
        defaultRequestExpiryTimeout = -1L;
        defaultWindowMonitorInterval = -1L;
        defaultSessionCountersEnabled = true;
        startUpTime = 0L;
        defaultSmppServer = null;
        smppServerHandler = null;
        mbeanServer = null;
    }

    public static SmppServer getInstance()
    {
        return SingletonHolder.INSTANCE;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    protected String getName()
    {
        return name;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public void setBindTimeout(long bindTimeout)
    {
        this.bindTimeout = bindTimeout;
    }

    public void setSystemId(String systemId)
    {
        this.systemId = systemId;
    }

    public void setAutoNegotiateInterfaceVersion(boolean autoNegotiateInterfaceVersion)
    {
        this.autoNegotiateInterfaceVersion = autoNegotiateInterfaceVersion;
    }

    public void setInterfaceVersion(double interfaceVersion)
        throws Exception
    {
        if(interfaceVersion != 3.3999999999999999D && interfaceVersion != 3.2999999999999998D)
        {
            throw new Exception("Only SMPP version 3.4 or 3.3 is supported");
        } else
        {
            this.interfaceVersion = interfaceVersion;
            return;
        }
    }

    public void setMaxConnectionSize(int maxConnectionSize)
    {
        this.maxConnectionSize = maxConnectionSize;
    }

    public void setDefaultWindowSize(int defaultWindowSize)
    {
        this.defaultWindowSize = defaultWindowSize;
    }

    public void setDefaultWindowWaitTimeout(long defaultWindowWaitTimeout)
    {
        this.defaultWindowWaitTimeout = defaultWindowWaitTimeout;
    }

    public void setDefaultRequestExpiryTimeout(long defaultRequestExpiryTimeout)
    {
        this.defaultRequestExpiryTimeout = defaultRequestExpiryTimeout;
    }

    public void setDefaultWindowMonitorInterval(long defaultWindowMonitorInterval)
    {
        this.defaultWindowMonitorInterval = defaultWindowMonitorInterval;
    }

    public void setDefaultSessionCountersEnabled(boolean defaultSessionCountersEnabled)
    {
        this.defaultSessionCountersEnabled = defaultSessionCountersEnabled;
    }

    public long getStartUpTime()
    {
        return startUpTime;
    }

    public void setStartUpTime(long startUpTime)
    {
        this.startUpTime = startUpTime;
    }

    public void start() throws SmppChannelException
    {
        setStartUpTime(System.currentTimeMillis());
        ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
        /*port = Integer.parseInt(DBConfig.instance().getProperty("smpp.interface.vsmsc.listen.port"));
        bindTimeout = Integer.parseInt(DBConfig.instance().getProperty("vsmsc.bind.timeout"));
        maxConnectionSize = Integer.parseInt(DBConfig.instance().getProperty("vsmsc.max.binds"));
        defaultWindowSize = Integer.parseInt(DBConfig.instance().getProperty("vsmsc.window.size"));
        defaultRequestExpiryTimeout = Integer.parseInt(DBConfig.instance().getProperty("vsmsc.request.timeout"));*/
        if(logger.isInfoEnabled())
        {
            logger.info((new StringBuilder()).append("vsmsc listen port ").append(port).toString());
            logger.info((new StringBuilder()).append("vsmsc.bind.timeout in millis ").append(bindTimeout).toString());
            logger.info((new StringBuilder()).append("max binds allowed for this vsmsc ").append(maxConnectionSize).toString());
            logger.info((new StringBuilder()).append("max outstanding request allowed ").append(defaultWindowSize).toString());
            logger.info((new StringBuilder()).append("Default request expiry time in millis=").append(defaultRequestExpiryTimeout).toString());
        }
       /* ScheduledThreadPoolExecutor monitorExecutor = (ScheduledThreadPoolExecutor)Executors.newScheduledThreadPool(1, new  Object() ,class _anm1 {}

);*/
        SmppServerConfiguration configuration = new SmppServerConfiguration();
        configuration.setName(name);
        configuration.setPort(port);
        configuration.setBindTimeout(bindTimeout);
        configuration.setSystemId(systemId);
        configuration.setAutoNegotiateInterfaceVersion(autoNegotiateInterfaceVersion);
        if(interfaceVersion == 3.3999999999999999D)
        {
            configuration.setInterfaceVersion((byte)52);
        } else
        if(interfaceVersion == 3.2999999999999998D)
        {
            configuration.setInterfaceVersion((byte)51);
        }
        configuration.setMaxConnectionSize(maxConnectionSize);
        configuration.setNonBlockingSocketsEnabled(true);
        configuration.setDefaultRequestExpiryTimeout(defaultRequestExpiryTimeout);
        //int dnRequestTimeout = Integer.parseInt(DBConfig.instance().getProperty("vsmsc.dnrequest.timeout"));
       // configuration.setDefaultWindowMonitorInterval(dnRequestTimeout);
        configuration.setDefaultWindowSize(defaultWindowSize);
        configuration.setDefaultWindowWaitTimeout(defaultRequestExpiryTimeout * 2L);
        configuration.setDefaultSessionCountersEnabled(defaultSessionCountersEnabled);
        configuration.setJmxEnabled(false);
        configuration.setJmxDomain("mgage.vsmsc");
        DefaultSmppServerHandler serverHandler = new DefaultSmppServerHandler();
        serverHandler.setSmppSessionHandlerInterface( new SmppSessionBindUnbindHandler());
        smppServerHandler = serverHandler;
        //defaultSmppServer = new DefaultSmppServer(configuration, smppServerHandler, executor, monitorExecutor);
        defaultSmppServer = new DefaultSmppServer(configuration, smppServerHandler);
        registerMBean();
        logger.info("Starting SMPP server...");
        defaultSmppServer.start();
        logger.info("SMPP server started");
    }

    public DefaultSmppServer getDefaultSmppServer()
    {
        return defaultSmppServer;
    }

    public void setDefaultSmppServer(DefaultSmppServer defaultSmppServer)
    {
        this.defaultSmppServer = defaultSmppServer;
    }

    public void stop()
    {
        logger.info("Stopping SMPP server...");
        defaultSmppServer.stop();
        logger.info("SMPP server stopped");
        logger.info(String.format("Server counters: %s", new Object[] {
            defaultSmppServer.getCounters()
        }));
    }

    public void destroy()
    {
        unregisterMBean();
    }

    private void registerMBean()
    {
    }

    private void unregisterMBean()
    {
    }

    public DefaultSmppServerHandler getDefaultSmppServerHandler()
    {
        return (DefaultSmppServerHandler)smppServerHandler;
    }
    
    private void Smpp() {
	}
    
    
	class ger implements ThreadFactory {

		private AtomicInteger sequence;
		final SmppServer smppServer;

		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setName((new StringBuilder()).append("SmppServerSessionWindowMonitorPool-")
					.append(sequence.getAndIncrement()).toString());
			return t;
		}

		ger() {
			super();
			smppServer = SmppServer.this;
			sequence = new AtomicInteger(0);
		}
	}

}


