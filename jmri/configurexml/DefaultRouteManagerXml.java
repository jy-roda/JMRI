// DefaultRouteManagerConfigXML.java

package jmri.configurexml;

import jmri.InstanceManager;
import jmri.Route;
import jmri.RouteManager;
import jmri.Turnout;
import jmri.Sensor;
import jmri.DefaultRouteManager;
import java.util.List;
import org.jdom.Element;

/**
 * Provides the functionality for
 * configuring RouteManagers
 * <P>
 *
 * @author Dave Duchamp Copyright (c) 2004
 * @author Daniel Boudreau Copyright (c) 2007
 * @author Simon Reader Copyright (C) 2008
 *
 * @version $Revision: 1.19 $
 */
public class DefaultRouteManagerXml extends AbstractNamedBeanManagerConfigXML {

    public DefaultRouteManagerXml() {
    }

    /**
     * Default implementation for storing the contents of a
     * RouteManager
     * @param o Object to store, of type RouteManager
     * @return Element containing the complete info
     */
    public Element store(Object o) {
        Element routes = new Element("routes");
        setStoreElementClass(routes);
        RouteManager tm = (RouteManager) o;
        if (tm!=null) {
            java.util.Iterator iter =
                                    tm.getSystemNameList().iterator();

            // don't return an element if there are not routes to include
            if (!iter.hasNext()) return null;
            
            // store the routes
            while (iter.hasNext()) {
                String sname = (String)iter.next();
                if (sname==null) log.error("System name null during store");
                log.debug("system name is "+sname);
                Route r = tm.getBySystemName(sname);
                String cTurnout = r.getControlTurnout();
				int addedDelay = r.getRouteCommandDelay();
				boolean routeLocked = r.getLocked();
				String cLockTurnout = r.getLockControlTurnout();
				
                Element elem = new Element("route")
                            .setAttribute("systemName", sname);
                
                // store common parts
                storeCommon(r, elem);

                if (cTurnout!=null && cTurnout!="") {
                    elem.setAttribute("controlTurnout", cTurnout);
                    int state = r.getControlTurnoutState();
                    if (state == Route.ONTHROWN) {
                        elem.setAttribute("controlTurnoutState","THROWN");
                    }else if (state == Route.ONCHANGE) {
                    	elem.setAttribute("controlTurnoutState","CHANGE");
                    }else if (state == Route.VETOCLOSED) {
                    	elem.setAttribute("controlTurnoutState","VETOCLOSED");
                    }else if (state == Route.VETOTHROWN) {
                    	elem.setAttribute("controlTurnoutState","VETOTHROWN");
                    }else {
                        elem.setAttribute("controlTurnoutState","CLOSED");
                    }
                }
                if (cLockTurnout!=null && cLockTurnout!="") {
                    elem.setAttribute("controlLockTurnout", cLockTurnout);
                    int state = r.getLockControlTurnoutState();
                    if (state == Route.ONTHROWN) {
                        elem.setAttribute("controlLockTurnoutState","THROWN");
                    }else if (state == Route.ONCHANGE) {
                    	elem.setAttribute("controlLockTurnoutState","CHANGE");
                    }else {
                        elem.setAttribute("controlLockTurnoutState","CLOSED");
                    }
                }
				if (addedDelay>0) {
					elem.setAttribute("addedDelay",Integer.toString(addedDelay));
				}
				
				if (routeLocked){
					elem.setAttribute("routeLocked","True");
				}
                // add route output Turnouts, if any
                int index = 0;
                String rTurnout = null;
                while ( (rTurnout = r.getOutputTurnoutByIndex(index)) != null) {
                    Element rElem = new Element("routeOutputTurnout")
                                    .setAttribute("systemName", rTurnout);
                    String sState = "CLOSED";
                    if (r.getOutputTurnoutSetState(rTurnout)==Turnout.THROWN) {
                        sState = "THROWN";
                    }
                    else if (r.getOutputTurnoutSetState(rTurnout)==Route.TOGGLE) {
                        sState = "TOGGLE";
                    }
                   rElem.setAttribute("state", sState);
                    elem.addContent(rElem);
                    index ++;
                }
                // add route output Sensors, if any
                index = 0;
                String rSensor = null;
                while ( (rSensor = r.getOutputSensorByIndex(index)) != null) {
                    Element rElem = new Element("routeOutputSensor")
                                    .setAttribute("systemName", rSensor);
                    String sState = "INACTIVE";
                    if (r.getOutputSensorSetState(rSensor)==Sensor.ACTIVE) {
                        sState = "ACTIVE";
                    }
                    else if (r.getOutputSensorSetState(rSensor)==Route.TOGGLE) {
                        sState = "TOGGLE";
                    }
                    rElem.setAttribute("state", sState);
                    elem.addContent(rElem);
                    index ++;
                }
                // add route control Sensors, if any
                index = 0;
                rSensor = null;
                while ( (rSensor = r.getRouteSensorName(index)) != null) {
                    Element rsElem = new Element("routeSensor")
                                    .setAttribute("systemName", rSensor);
                    int mode = r.getRouteSensorMode(index);
                    String modeName;
                    switch (mode) {
                    case Route.ONACTIVE:
                        modeName = "onActive";
                        break;
                    case Route.ONINACTIVE:
                        modeName = "onInactive";
                        break;
                    case Route.ONCHANGE:
                        modeName = "onChange";
                        break;
                    case Route.VETOACTIVE:
                        modeName = "vetoActive";
                        break;
                    case Route.VETOINACTIVE:
                        modeName = "vetoInactive";
                        break;
                    default:
                        modeName = null;
                    }
                    if (modeName!=null) rsElem.setAttribute("mode", modeName);
                    elem.addContent(rsElem);
                    index ++;
                }
                // add sound and script file elements if needed
                if (r.getOutputSoundName()!=null && !r.getOutputSoundName().equals("")) {
                    Element rsElem = new Element("routeSoundFile")
                                    .setAttribute("name", r.getOutputSoundName());
                    elem.addContent(rsElem);
                }
                if (r.getOutputScriptName()!=null && !r.getOutputScriptName().equals("")) {
                    Element rsElem = new Element("routeScriptFile")
                                    .setAttribute("name", r.getOutputScriptName());
                    elem.addContent(rsElem);
                }
                
                // add turnouts aligned sensor if there is one
                if (!r.getTurnoutsAlignedSensor().equals("")) {
                    Element rsElem = new Element("turnoutsAlignedSensor")
                                    .setAttribute("name", r.getTurnoutsAlignedSensor());
                    elem.addContent(rsElem);
                }
                
                log.debug("store route "+sname);
                routes.addContent(elem);
            }
        }
        return routes;
    }

    /**
     * Subclass provides implementation to create the correct top
     * element, including the type information.
     * Default implementation is to use the local class here.
     * @param routes The top-level element being created
     */
    public void setStoreElementClass(Element routes) {
        routes.setAttribute("class","jmri.configurexml.DefaultRouteManagerXml");
    }

    public void load(Element element, Object o) {
        log.error("Invalid method called");
    }

    /**
     * Create a RouteManager object of the correct class, then
     * register and fill it.
     * @param routes Top level Element to unpack.
     */
    public void load(Element routes) {
        // create the master object
        replaceRouteManager();
        // load individual routes
        loadRoutes(routes);
    }


    /**
     * Utility method to load the individual Route objects.
     * If there's no additional info needed for a specific route type,
     * invoke this with the parent of the set of Route elements.
     * @param routes Element containing the Route elements to load.
     */
    public void loadRoutes(Element routes) {
        List routeList = routes.getChildren("route");
        if (log.isDebugEnabled()) log.debug("Found "+routeList.size()+" routes");
        RouteManager tm = InstanceManager.routeManagerInstance();

        for (int i=0; i<routeList.size(); i++) {
            if ( ((Element)(routeList.get(i))).getAttribute("systemName") == null) {
                log.warn("unexpected null in systemName "+((Element)(routeList.get(i)))+" "+
                                                        ((Element)(routeList.get(i))).getAttributes());
                break;
            }
            String sysName = ((Element)(routeList.get(i))).getAttribute("systemName").getValue();
            String userName = null;
            String cTurnout = null;
            String cTurnoutState = null;
			String addedDelayTxt = null;
			String routeLockedTxt = null;
			String cLockTurnout = null;
			String cLockTurnoutState = null;
			int addedDelay = 0;
            if ( ((Element)(routeList.get(i))).getAttribute("userName") != null)
                userName = ((Element)(routeList.get(i))).getAttribute("userName").getValue();

            if ( ((Element)(routeList.get(i))).getAttribute("controlTurnout") != null)
                cTurnout = ((Element)(routeList.get(i))).getAttribute("controlTurnout").getValue();
            if ( ((Element)(routeList.get(i))).getAttribute("controlTurnoutState") != null)
                cTurnoutState = ((Element)(routeList.get(i))).getAttribute("controlTurnoutState").getValue();
            if ( ((Element)(routeList.get(i))).getAttribute("controlLockTurnout") != null)
                cLockTurnout = ((Element)(routeList.get(i))).getAttribute("controlLockTurnout").getValue();
            if ( ((Element)(routeList.get(i))).getAttribute("controlLockTurnoutState") != null)
                cLockTurnoutState = ((Element)(routeList.get(i))).getAttribute("controlLockTurnoutState").getValue();
            if ( ((Element)(routeList.get(i))).getAttribute("addedDelay") != null) {
                addedDelayTxt = ((Element)(routeList.get(i))).getAttribute("addedDelay").getValue();
				if (addedDelayTxt != null) {
					addedDelay = Integer.parseInt(addedDelayTxt);
				}
			}
            if ( ((Element)(routeList.get(i))).getAttribute("routeLocked") != null)
            	routeLockedTxt = ((Element)(routeList.get(i))).getAttribute("routeLocked").getValue();
            
            if (log.isDebugEnabled()) log.debug("create route: ("+sysName+")("+
                                                            (userName==null?"<null>":userName)+")");
            Route r = tm.provideRoute(sysName, userName);

            // load common parts
            loadCommon(r, ((Element)(routeList.get(i))));

            if (r!=null) {
				// add control turnout if there is one
				if (cTurnout != null) {
					r.setControlTurnout(cTurnout);
					if (cTurnoutState.equals("THROWN")) {
						r.setControlTurnoutState(Route.ONTHROWN);
					} else if (cTurnoutState.equals("CHANGE"))	{
						r.setControlTurnoutState(Route.ONCHANGE);
					} else if (cTurnoutState.equals("VETOCLOSED"))	{
						r.setControlTurnoutState(Route.VETOCLOSED);
					} else if (cTurnoutState.equals("VETOTHROWN"))	{
						r.setControlTurnoutState(Route.VETOTHROWN);
					} else {
						r.setControlTurnoutState(Route.ONCLOSED);
					}
				}
				// set added delay
				r.setRouteCommandDelay(addedDelay);

				// determine if route locked
				if (routeLockedTxt != null && routeLockedTxt.equals("True"))
					r.setLocked(true);
				
				//add lock control turout if there is one
				if (cLockTurnout != null) {
					r.setLockControlTurnout(cLockTurnout);
					if (cLockTurnoutState.equals("THROWN")) {
						r.setLockControlTurnoutState(Route.ONTHROWN);
					} else if (cLockTurnoutState.equals("CHANGE"))	{
						r.setLockControlTurnoutState(Route.ONCHANGE);
					} else {
						r.setLockControlTurnoutState(Route.ONCLOSED);
					}
				}
                
                // load output turnouts if there are any - old format first (1.7.6 and before)
                List routeTurnoutList = ((Element)(routeList.get(i))).getChildren("routeTurnout");
                if (routeTurnoutList.size() > 0) {
                    // This route has turnouts
                    for (int k=0; k<routeTurnoutList.size(); k++) {
                        if ( ((Element)(routeTurnoutList.get(k))).getAttribute("systemName") == null) {
                            log.warn("unexpected null in systemName "+((Element)(routeTurnoutList.get(k)))+
                                                " "+((Element)(routeTurnoutList.get(k))).getAttributes());
                            break;
                        }
                        String tSysName = ((Element)(routeTurnoutList.get(k)))
                                                            .getAttribute("systemName").getValue();
                        String rState = ((Element)(routeTurnoutList.get(k)))
                                                            .getAttribute("state").getValue();
                        int tSetState = Turnout.CLOSED;
                        if (rState.equals("THROWN")) {
                            tSetState = Turnout.THROWN;
                        }
						else if (rState.equals("TOGGLE")) {
							tSetState = Route.TOGGLE;
						}
                        // Add turnout to route
                        r.addOutputTurnout(tSysName, tSetState);
                    }
                }
                // load output turnouts if there are any - new format
                routeTurnoutList = ((Element)(routeList.get(i))).getChildren("routeOutputTurnout");
                if (routeTurnoutList.size() > 0) {
                    // This route has turnouts
                    for (int k=0; k<routeTurnoutList.size(); k++) {
                        if ( ((Element)(routeTurnoutList.get(k))).getAttribute("systemName") == null) {
                            log.warn("unexpected null in systemName "+((Element)(routeTurnoutList.get(k)))+
                                                " "+((Element)(routeTurnoutList.get(k))).getAttributes());
                            break;
                        }
                        String tSysName = ((Element)(routeTurnoutList.get(k)))
                                                            .getAttribute("systemName").getValue();
                        String rState = ((Element)(routeTurnoutList.get(k)))
                                                            .getAttribute("state").getValue();
                        int tSetState = Turnout.CLOSED;
                        if (rState.equals("THROWN")) {
                            tSetState = Turnout.THROWN;
                        }
						else if (rState.equals("TOGGLE")) {
							tSetState = Route.TOGGLE;
						}
                        // Add turnout to route
                        r.addOutputTurnout(tSysName, tSetState);
                        
                        // determine if turnout should be locked
                        Turnout t = r.getOutputTurnout(k);
						if (r.getLocked()) {
							t.setLocked(Turnout.CABLOCKOUT + Turnout.PUSHBUTTONLOCKOUT, true);
						}
                	}
                }
                // load output sensors if there are any - new format
                routeTurnoutList = ((Element)(routeList.get(i))).getChildren("routeOutputSensor");
                if (routeTurnoutList.size() > 0) {
                    // This route has turnouts
                    for (int k=0; k<routeTurnoutList.size(); k++) {
                        if ( ((Element)(routeTurnoutList.get(k))).getAttribute("systemName") == null) {
                            log.warn("unexpected null in systemName "+((Element)(routeTurnoutList.get(k)))+
                                                " "+((Element)(routeTurnoutList.get(k))).getAttributes());
                            break;
                        }
                        String tSysName = ((Element)(routeTurnoutList.get(k)))
                                                            .getAttribute("systemName").getValue();
                        String rState = ((Element)(routeTurnoutList.get(k)))
                                                            .getAttribute("state").getValue();
                        int tSetState = Sensor.INACTIVE;
                        if (rState.equals("ACTIVE")) {
                            tSetState = Sensor.ACTIVE;
                        }
						else if (rState.equals("TOGGLE")) {
							tSetState = Route.TOGGLE;
						}
                        // Add turnout to route
                        r.addOutputSensor(tSysName, tSetState);
                    }
                }
                // load sound, script files if present
                Element fileElement = ((Element)(routeList.get(i))).getChild("routeSoundFile");
                if (fileElement != null) {
                    r.setOutputSoundName(fileElement.getAttribute("name").getValue());
                }
                fileElement = ((Element)(routeList.get(i))).getChild("routeScriptFile");
                if (fileElement != null) {
                    r.setOutputScriptName(fileElement.getAttribute("name").getValue());
                }
                // load turnouts aligned sensor if there is one
                fileElement = ((Element)(routeList.get(i))).getChild("turnoutsAlignedSensor");
                if (fileElement != null) {
                    r.setTurnoutsAlignedSensor(fileElement.getAttribute("name").getValue());
                }
                
                // load route control sensors, if there are any
                List routeSensorList = ((Element)(routeList.get(i))).getChildren("routeSensor");
                if (routeSensorList.size() > 0) {
                    // This route has sensors
                    for (int k=0; k<routeSensorList.size(); k++) {
                        if ( ((Element)(routeSensorList.get(k))).getAttribute("systemName") == null) {
                            log.warn("unexpected null in systemName "+((Element)(routeSensorList.get(k)))+
                                                " "+((Element)(routeSensorList.get(k))).getAttributes());
                            break;
                        }
                        int mode = Route.ONACTIVE;  // default mode
                        if ( ((Element)(routeSensorList.get(k))).getAttribute("mode") != null) {
                            String sm = ((Element)(routeSensorList.get(k))).getAttribute("mode").getValue();
                            if (sm.equals("onActive"))
                                mode = Route.ONACTIVE;
                            else if (sm.equals("onInactive"))
                                mode = Route.ONINACTIVE;
                            else if (sm.equals("onChange"))
                                mode = Route.ONCHANGE;
                            else if (sm.equals("vetoActive"))
                                mode = Route.VETOACTIVE;
                            else if (sm.equals("vetoInactive"))
                                mode = Route.VETOINACTIVE;
                            else
                                log.warn("unexpected sensor mode in route "+sysName+" was "+sm);
                        }

                        // Add Sensor to route
                        r.addSensorToRoute(((Element)(routeSensorList.get(k)))
                                                        .getAttribute("systemName").getValue(), mode);
                    }
                }
            // and start it working
            r.activateRoute();
            
            }
            else {
                log.error ("failed to create Route: "+sysName);
            }
        }
    }

    /**
     * Replace the current RouteManager, if there is one, with
     * one newly created during a load operation. This is skipped
     * if they are of the same absolute type.
     */
    protected void replaceRouteManager() {
        if (InstanceManager.routeManagerInstance().getClass().getName()
                .equals(DefaultRouteManager.class.getName()))
            return;
        // if old manager exists, remove it from configuration process
        if (InstanceManager.routeManagerInstance() != null)
            InstanceManager.configureManagerInstance().deregister(
                InstanceManager.routeManagerInstance() );

        // register new one with InstanceManager
        DefaultRouteManager pManager = DefaultRouteManager.instance();
        InstanceManager.setRouteManager(pManager);
        // register new one for configuration
        InstanceManager.configureManagerInstance().registerConfig(pManager);
    }

    static org.apache.log4j.Category log = org.apache.log4j.Category.getInstance(DefaultRouteManagerXml.class.getName());
}
