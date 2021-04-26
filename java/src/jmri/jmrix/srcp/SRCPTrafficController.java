package jmri.jmrix.srcp;

import java.lang.reflect.InvocationTargetException;
import java.util.Vector;
import jmri.InstanceManager;
import jmri.ShutDownManager;
import jmri.jmrix.AbstractMRListener;
import jmri.jmrix.AbstractMRMessage;
import jmri.jmrix.AbstractMRReply;
import jmri.jmrix.AbstractMRTrafficController;
import jmri.jmrix.srcp.parser.ParseException;
import jmri.jmrix.srcp.parser.SRCPClientParser;
import jmri.jmrix.srcp.parser.SRCPClientVisitor;
import jmri.jmrix.srcp.parser.SimpleNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converts Stream-based I/O to/from SRCP messages. The "SRCPInterface" side
 * sends/receives message objects.
 * <p>
 * The connection to a SRCPPortController is via a pair of *Streams, which then
 * carry sequences of characters for transmission. Note that this processing is
 * handled in an independent thread, which we need to clean up in tests.
 * <p>
 * This handles the state transitions, based on the necessary state in each
 * message.
 *
 * @author Bob Jacobsen Copyright (C) 2001
 */
public class SRCPTrafficController extends AbstractMRTrafficController
        implements SRCPInterface {

    protected SRCPSystemConnectionMemo _memo = null;
    final Runnable shutDownTask = () -> sendSRCPMessage(new SRCPMessage("TERM 0 SESSION"), null);

    /**
     * Create a new SRCPTrafficController instance.
     */
    public SRCPTrafficController() {
        super();
        InstanceManager.getDefault(ShutDownManager.class).register(shutDownTask);
    }

    // The methods to implement the SRCPInterface
    @Override
    public synchronized void addSRCPListener(SRCPListener l) {
        this.addListener(l);
    }

    @Override
    public synchronized void removeSRCPListener(SRCPListener l) {
        this.removeListener(l);
    }

    /*
     * Set the system connection memo associated with the traffic
     * controller
     */
    void setSystemConnectionMemo(SRCPSystemConnectionMemo memo) {
        _memo = memo;
    }

    /*
     * Get the system connection memo associated with the traffic
     * controller
     */
    SRCPSystemConnectionMemo getSystemConnectionMemo() {
        return _memo;
    }

    public static int HANDSHAKEMODE = 0;
    public static int RUNMODE = 1;
    private int mode = HANDSHAKEMODE;

    /*
     * We are going to override the receiveLoop() function so that we can
     * handle messages received by the system using the SRCP parser.
     */
    @Override
    public void receiveLoop() {
        log.debug("SRCP receiveLoop starts");
        SRCPClientParser parser = new SRCPClientParser(istream);
        while (true) {
            try {
                SimpleNode e;
                if (_memo.getMode() == HANDSHAKEMODE) {
                    e = parser.handshakeresponse();
                } else {
                    e = parser.commandresponse();
                }

                // forward the message to the registered recipients,
                // which includes the communications monitor.
                // return a notification via the Swing event queue to ensure proper thread
                Runnable r = new SRCPRcvNotifier(e, mLastSender, this);
                try {
                    javax.swing.SwingUtilities.invokeAndWait(r);
                } catch (InterruptedException | InvocationTargetException ex) {
                    log.error("Unexpected exception in invokeAndWait:", ex);
                }
                log.debug("dispatch thread invoked");

                log.debug("Mode {} child contains {}", mode, ((SimpleNode) e.jjtGetChild(1)).jjtGetValue());
                //if (mode==HANDSHAKEMODE && ((String)((SimpleNode)e.jjtGetChild(1)).jjtGetValue()).contains("GO")) mode=RUNMODE;

                SRCPClientVisitor v = new SRCPClientVisitor();
                e.jjtAccept(v, _memo);

                // we need to re-write the switch below so that it uses the
                // SimpleNode values instead of the reply message.
                //SRCPReply msg = new SRCPReply((SimpleNode)e.jjtGetChild(1));
                switch (mCurrentState) {
                    case WAITMSGREPLYSTATE:
                        // update state, and notify to continue
                        synchronized (xmtRunnable) {
                            mCurrentState = NOTIFIEDSTATE;
                            replyInDispatch = false;
                            xmtRunnable.notify();
                        }
                        break;
                    case WAITREPLYINPROGMODESTATE:
                        // entering programming mode
                        mCurrentMode = PROGRAMINGMODE;
                        replyInDispatch = false;

                        // check to see if we need to delay to allow decoders
                        // to become responsive
                        int warmUpDelay = enterProgModeDelayTime();
                        if (warmUpDelay != 0) {
                            try {
                                synchronized (xmtRunnable) {
                                    xmtRunnable.wait(warmUpDelay);
                                }
                            } catch (InterruptedException ex) {
                                Thread.currentThread().interrupt(); // retain if needed later
                            }
                        }
                        // update state, and notify to continue
                        synchronized (xmtRunnable) {
                            mCurrentState = OKSENDMSGSTATE;
                            xmtRunnable.notify();
                        }
                        break;
                    case WAITREPLYINNORMMODESTATE:
                        // entering normal mode
                        mCurrentMode = NORMALMODE;
                        replyInDispatch = false;
                        // update state, and notify to continue
                        synchronized (xmtRunnable) {
                            mCurrentState = OKSENDMSGSTATE;
                            xmtRunnable.notify();
                        }
                        break;
                    default:
                        replyInDispatch = false;
                        if (allowUnexpectedReply == true) {
                            log.debug("Allowed unexpected reply received in state: {} was {}", mCurrentState, e);

                            synchronized (xmtRunnable) {
                                // The transmit thread sometimes gets stuck
                                // when unexpected replies are received.  Notify
                                // it to clear the block without a timeout.
                                // (do not change the current state)
                                xmtRunnable.notify();
                            }
                        } else {
                            unexpectedReplyStateError(mCurrentState, e.toString());
                        }
                }
            }
            catch (ParseException pe) {
                rcvException = true;
                reportReceiveLoopException(pe);
                break;
            }
        }
    }

    /**
     * Terminate the SRCP extra threads.
     * <p>
     * This is intended to be used only by testing subclasses.
     */
    @Override
    public void terminateThreads() {
        sendSRCPMessage(new SRCPMessage("TERM 0 SESSION"), null);
        // we also need to remove the shutdown task.
        jmri.InstanceManager.getDefault(jmri.ShutDownManager.class).deregister(shutDownTask);
        // usual stop process
        super.terminateThreads();
    }

    /**
     * Forward a SRCPMessage to all registered SRCPInterface listeners.
     */
    @Override
    protected void forwardMessage(AbstractMRListener client, AbstractMRMessage m) {
        ((SRCPListener) client).message((SRCPMessage) m);
    }

    /**
     * Forward a SRCPReply to all registered SRCPInterface listeners.
     */
    @Override
    protected void forwardReply(AbstractMRListener client, AbstractMRReply m) {
        ((SRCPListener) client).reply((SRCPReply) m);
    }

    /**
     * Forward a SRCPReply to all registered SRCPInterface listeners.
     *
     * @param client WHo should receive the reply
     * @param n      relevant node
     */
    protected void forwardReply(AbstractMRListener client, SimpleNode n) {
        ((SRCPListener) client).reply(n);
    }

    public void setSensorManager(jmri.SensorManager m) {
    }

    @Override
    protected AbstractMRMessage pollMessage() {
        return null;
    }

    @Override
    protected AbstractMRListener pollReplyHandler() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendSRCPMessage(SRCPMessage m, SRCPListener reply) {
        sendMessage(m, reply);
    }

    @Override
    protected AbstractMRMessage enterProgMode() {
        // we need to find the right bus number!
        return SRCPMessage.getProgMode(1);
    }

    @Override
    protected AbstractMRMessage enterNormalMode() {
        // we need to find the right bus number!
        return SRCPMessage.getExitProgMode(1);
    }

    @Override
    protected AbstractMRReply newReply() {
        return new SRCPReply();
    }

    @Override
    protected boolean endOfMessage(AbstractMRReply msg) {
        int index = msg.getNumDataElements() - 1;
        switch (msg.getElement(index)) {
            case 0x0D:
            case 0x0A:
                return true;
            default:
                return false;
        }
    }

    /**
     * Forward a "Reply" from layout to registered listeners.
     *
     * @param r    Reply to be forwarded intact
     * @param dest One (optional) listener to be skipped, usually because it's
     *             the originating object.
     */
    @SuppressWarnings("unchecked")
    protected void notifyReply(SimpleNode r, AbstractMRListener dest) {
        // make a copy of the listener vector to synchronized (not needed for transmit?)
        Vector<AbstractMRListener> v;
        synchronized (this) {
            // FIXME: unnecessary synchronized; the Vector IS already thread-safe.
            v = (Vector<AbstractMRListener>) cmdListeners.clone();
        }
        // forward to all listeners
        int cnt = v.size();
        for (int i = 0; i < cnt; i++) {
            AbstractMRListener client = v.elementAt(i);
            log.debug("notify client: {}", client);
            try {
                //skip dest for now, we'll send the message to there last.
                if (dest != client) {
                    forwardReply(client, r);
                }
            } catch (Exception ex) {
                log.warn("notify: During reply dispatch to {}", client, ex);
            }
            // forward to the last listener who send a message
            // this is done _second_ so monitoring can have already stored the reply
            // before a response is sent
            if (dest != null) {
                forwardReply(dest, r);
            }
        }
    }

    /**
     * Internal class to remember the Reply object and destination listener with
     * a reply is received.
     */
    protected static class SRCPRcvNotifier implements Runnable {

        SimpleNode e;
        SRCPListener mDest;
        SRCPTrafficController mTC;

        SRCPRcvNotifier(SimpleNode n, AbstractMRListener pDest,
                AbstractMRTrafficController pTC) {
            // the first child of n in the parse tree is
            // the response, without the timestamp
            e = (SimpleNode) n.jjtGetChild(1);
            mDest = (SRCPListener) pDest;
            mTC = (SRCPTrafficController) pTC;
        }

        @Override
        public void run() {
            log.debug("Delayed rcv notify starts");
            mTC.notifyReply(e, mDest);
        }
    }

    private final static Logger log = LoggerFactory.getLogger(SRCPTrafficController.class);

}
