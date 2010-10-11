/**
 * Licensed under the GNU LGPL
 * License details here: http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package bhandari;

import graph.Link;
import graph.Device;
import graph.Network;
import graph.Utils;

import java.util.*;

public class BreadthFirstSearch {

  public static List<Link> findPath(Network nw, Device src, Device dest)
  {
    final PathCalcContext pcc = new PathCalcContext(src);
    return findPath(nw, src, dest, pcc);
  }

  public static List<Link> findPath(Network nw, Device src, Device dest, PathCalcContext pcc)
  {
    final LinkedList<Device> devicesToCheck = new LinkedList<Device>();
    devicesToCheck.add(src);
    while ( !devicesToCheck.isEmpty() )
    {
      Device toCheck = devicesToCheck.pop();
      Set<Link> linksToCheck = nw.getLinksForDevice(toCheck);
      Set<Device> neighboursToCheck = new HashSet<Device>();
      for ( Link link: linksToCheck )
      {
        Device otherEnd = Utils.getOtherEndpoint( link, toCheck );
        if ( pcc.updateDistance( otherEnd, link ) && (otherEnd != dest) )
        {
          neighboursToCheck.add(otherEnd);
        }
      }
      devicesToCheck.addAll(neighboursToCheck);
    }
    return pcc.getPathFromPredecessors(dest);
  }
}
