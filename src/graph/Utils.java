/**
 * Copyright 2010 Russ Weeks rweeks@newbrightidea.com
 * Licensed under the GNU LGPL
 * License details here: http://www.gnu.org/licenses/lgpl-3.0.txt
 */package graph;

import java.util.LinkedList;
import java.util.Collection;

public class Utils {
  public static Device getOtherEndpoint(Link link, Device endpoint)
  {
    NetworkInterface[] ifs = link.getEndpoints();
    if (ifs[0].getDevice().equals(endpoint) )
    {
      return ifs[1].getDevice();
    }
    else if (ifs[1].getDevice().equals(endpoint))
    {
      return ifs[0].getDevice();
    }
    else
    {
      throw new IllegalArgumentException( "Endpoint does not belong to link." );
    }
  }

  public static LinkedList<Device> getOtherEndpoints(Collection<Link> links, Device endpoint)
  {
    LinkedList<Device> otherEndpoints = new LinkedList<Device>();
    for ( Link link: links )
    {
      otherEndpoints.add( getOtherEndpoint( link, endpoint ) );
    }
    return otherEndpoints;
  }
}
