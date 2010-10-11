/**
 * Licensed under the GNU LGPL
 * License details here: http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package graph;

public class Link {
  private final NetworkInterface[] endpoints;

  public Link(NetworkInterface endA, NetworkInterface endZ)
  {
    if (endA.equals(endZ))
    {
      throw new IllegalArgumentException("Link endpoints must differ");
    }
    if (endA.getTeMetric() != endZ.getTeMetric())
    {
      throw new IllegalArgumentException("Link interface TE metrics must match" );
    }
    endpoints = new NetworkInterface[] {endA, endZ};
  }

  public Double getTeMetric()
  {
    return endpoints[0].getTeMetric();
  }
  
  public NetworkInterface[] getEndpoints() {
    return endpoints;
  }

  public boolean containsEndpoint( Device device )
  {
    return ( endpoints[0].getDevice().equals(device) ||
             endpoints[1].getDevice().equals(device) );
  }

  public String toString()
  {
    return endpoints[0] + " <-> " + endpoints[1];
  }
}
