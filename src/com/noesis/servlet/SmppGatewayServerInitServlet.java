package com.noesis.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.cloudhopper.smpp.type.SmppChannelException;
import com.noesis.server.SmppServer;

public class SmppGatewayServerInitServlet extends HttpServlet
{

    private static final long serialVersionUID = 1L;
    static Logger logger = Logger.getLogger(SmppGatewayServerInitServlet.class);

    public SmppGatewayServerInitServlet()
    {
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        if(logger.isInfoEnabled())
        {
            logger.info("Init called....");
        }
        try 
        {
            SmppServer.getInstance().start();
            boolean sessionThreadEnabled = false;
            logger.info((new StringBuilder()).append("sessionThreadEnabled>>>").append(sessionThreadEnabled).toString());
            /*if(!sessionThreadEnabled)
            {
                DnReaperTon.INST.start();
            }*/
            /*if(Utility.isChronicleEnabled())
            {
                if(logger.isInfoEnabled())
                {
                    logger.info("Starting message/dn post log chronicle reader/writer...");
                }
                ChronicleDNReader.DNPOST.start();
                DeleteChronicleStoreTask.getInstance();
                logger.info("session tracker register to db started...");
            } else
            {
                logger.info("In memory message reaper starting...");
                Thread reaper = new Thread(new QRepear(), "InMemQReaper-Thread");
                reaper.start();
            }*/
           /* new SessionCountUpdateTask();
            logger.info("Session Count Update Task starting...");
            new IdleSessionRemoverTask();
            logger.info("Idle session remover task starting...");*/
            /*new ResetSessionCounterTask();
            logger.info("Day wise counter resetting task starting...");*/
           // new AccountReloadTask();
            /*logger.info("Periodic account reloading task starting...");
            new DisabledAccountCheckTask();*/
            /*logger.info("Disabled account check task starting...");
            AdminServer adminServer = new AdminServer();
            Thread adminThread = new Thread(adminServer, "AdminMonitoringServer-Thread");
            adminThread.start();*/
            /*PropertiesConfiguration prop = PropertyFileLoaderTon.getInstance().getPropertiesConfiguration("ng3.properties.loc");
            (new DNRedis()).removeAllBindInfo(prop.getProperty("instance.id").toString());*/
            logger.info("VSMSC Server initialization completed...%%%");
        }
        catch(SmppChannelException e)
        {
            logger.error("Problem starting server...", e);
        }
        catch(Exception exp)
        {
            logger.error("Problem starting server...", exp);
        }
    }

}
