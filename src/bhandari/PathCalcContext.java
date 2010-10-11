/**
 * Copyright 2010 Russ Weeks rweeks@newbrightidea.com
 * Licensed under the GNU LGPL
 * License details here: http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package bhandari;

import graph.Device;
import graph.Link;
import graph.Utils;
import graph.Network;

import java.util.*;

public class PathCalcContext {

  private static final Double AVOID_LINK_COST = Math.sqrt(Float.MAX_VALUE);

  private final Map<Device, Link> predecessors;
  private final Map<Device,Double> knownDistances;

  private final Map<ArcAdjustment,Double> adjustedArcs;

  public PathCalcContext(Device src)
  {
    predecessors = new HashMap<Device,Link>();
    knownDistances = new HashMap<Device,Double>();
    knownDistances.put(src, 0.0d);
    adjustedArcs = new HashMap<ArcAdjustment,Double>();
  }

  public Double getKnownDistance(Device device)
  {
    Double distance = knownDistances.get(device);
    return distance == null ? Network.MAX_SIZE * AVOID_LINK_COST : distance;
  }

  public boolean updateDistance(Device incidentDevice, Link hop)
  {
    Double curCost = getKnownDistance( incidentDevice );
    Device emergentDevice = Utils.getOtherEndpoint(hop,incidentDevice);
    Double newCost = getKnownDistance( emergentDevice ) + getAdjustedCost(hop, emergentDevice);
    if ( (curCost == null) || (curCost > newCost) )
    {
      knownDistances.put( incidentDevice, newCost );
      predecessors.put( incidentDevice, hop );
      return true;
    }
    return false;
  }

  public List<Link> getPathFromPredecessors(Device dest)
  {
    List<Link> backPath = new LinkedList<Link>();
    Link hop = predecessors.get(dest);
    while ( hop != null )
    {
      backPath.add(hop);
      dest = Utils.getOtherEndpoint(hop, dest);
      hop = predecessors.get(dest);
    }
    Collections.reverse( backPath );
    return backPath;
  }

  public Double getAdjustedCost(Link link, Device emergentDevice)
  {
    Double adjustedCost = adjustedArcs.get(new ArcAdjustment(emergentDevice, link));
    return adjustedCost == null ? link.getTeMetric() : adjustedCost;
  }

  public void reset(Device src)
  {
    predecessors.clear();
    adjustedArcs.clear();
    knownDistances.clear();
    knownDistances.put(src,0.0d);
  }

  public void setShortestPath(List<Link> path, Device src)
  {
    reset(src);
    Device curDevice = src;
    for( Link hop: path )
    {
      adjustedArcs.put( new ArcAdjustment(curDevice, hop), AVOID_LINK_COST );
      curDevice = Utils.getOtherEndpoint(hop, curDevice);
      adjustedArcs.put( new ArcAdjustment(curDevice, hop), -1.0d * hop.getTeMetric() );
    }
  }

  private class ArcAdjustment
  {
    public final Device emergentDevice;
    public final Link link;

    private ArcAdjustment(Device emergentDevice, Link link) {
      this.emergentDevice = emergentDevice;
      this.link = link;
    }

    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      ArcAdjustment that = (ArcAdjustment) o;

      if (emergentDevice != null ? !emergentDevice.equals(that.emergentDevice) : that.emergentDevice != null)
        return false;
      if (link != null ? !link.equals(that.link) : that.link != null) return false;

      return true;
    }

    public int hashCode() {
      int result;
      result = (emergentDevice != null ? emergentDevice.hashCode() : 0);
      result = 31 * result + (link != null ? link.hashCode() : 0);
      return result;
    }

    public String toString()
    {
      return "Link: " + link + " Emergent Device: " + emergentDevice.getName();
    }
  }
}
