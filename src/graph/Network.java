/**
 * Licensed under the GNU LGPL
 * License details here: http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package graph;

import java.util.*;

public class Network {

  public static final Integer MAX_SIZE = 1<<20;

  private final Map<String,Device> devicesByName;

  private final Map<Device,Set<Link>> linksByDevice;

  public Network()
  {
    devicesByName = new HashMap<String,Device>();
    linksByDevice = new HashMap<Device,Set<Link>>();
  }

  public Set<Link> getLinksForDevice(Device device)
  {
    return linksByDevice.get(device);
  }
  
  public void addDevice(String name)
  {
    if ( devicesByName.containsKey(name) )
    {
      throw new IllegalArgumentException("Device " + name + " already exists.");
    }
    devicesByName.put( name, new Device(name) );
  }

  public Device getDevice(String name)
  {
    return devicesByName.get(name);
  }
  
  public void connectDevices(String endAName, String endZName, Double teMetric)
  {
    Device endA = devicesByName.get(endAName);
    Device endZ = devicesByName.get(endZName);

    NetworkInterface ifA = new NetworkInterface(endA, teMetric);
    NetworkInterface ifZ = new NetworkInterface(endZ, teMetric);
    Link link = new Link(ifA, ifZ);

    addLink(endA, link);
    addLink(endZ, link);
  }

  public void disconnectDevices(String endAName, String endZName, Double teMetric)
  {
    Device endA = devicesByName.get(endAName);
    Device endZ = devicesByName.get(endZName);
    Set<Link> endALinks = linksByDevice.get(endA);
    Set<Link> endZLinks = linksByDevice.get(endZ);
    if ( endALinks == null )
    {
      return;
    }
    Iterator<Link> linkIter = endALinks.iterator();
    while( linkIter.hasNext() )
    {
      Link link = linkIter.next();
      if (endZLinks.contains(link) &&
          (link.getEndpoints()[0].getTeMetric() == teMetric))
      {
        linkIter.remove();
        endZLinks.remove(link);
        break;
      }
    }
  }

  private void addLink(Device endpoint, Link link)
  {
    Set<Link> links = linksByDevice.get(endpoint);
    if ( links == null )
    {
      links = new HashSet<Link>();
      linksByDevice.put(endpoint,links);
    }
    links.add(link);
  }
}
