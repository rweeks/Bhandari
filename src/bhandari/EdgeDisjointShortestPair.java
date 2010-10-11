/**
 * Licensed under the GNU LGPL
 * License details here: http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package bhandari;

import graph.Link;
import graph.Network;
import graph.Device;
import graph.Utils;

import java.util.*;

import static bhandari.BreadthFirstSearch.*;

public class EdgeDisjointShortestPair
{
  /**
   * Finds the shortest pair of edge-disjoint paths between devices 'src' and 'dest' in
   * the given network.  If a pair of edge-disjoint paths does not exist, the returned
   * paths will contain the minimal overlap.
   */
  public static List<List<Link>> findDisjointPair(Network nw, Device src, Device dest)
  {
    PathCalcContext pcc = new PathCalcContext(src);
    // First find the shortest path
    LinkedList<Link> shortestPath = new LinkedList<Link>();
    shortestPath.addAll( findPath(nw, src, dest, pcc ) );
    // Set the shortest path in the path calc context, this will define all
    // needed arc adjustments
    pcc.setShortestPath(shortestPath, src);
    LinkedList<Link> secondPath = new LinkedList<Link>();
    // Find the second path
    secondPath.addAll( findPath(nw, src, dest, pcc ) );
    // Remove any links that are in both paths
    Iterator<Link> linkIter = shortestPath.iterator();
    while ( linkIter.hasNext() )
    {
      Link link = linkIter.next();
      if ( secondPath.contains(link) )
      {
        secondPath.remove(link);
        linkIter.remove();
      }
    }
    List<Link> primaryPath = mergePaths(src, dest, shortestPath, secondPath);
    List<Link> backupPath = mergePaths(src, dest, shortestPath, secondPath);

    return Arrays.asList(primaryPath, backupPath);
  }

  /**
   * Builds a path from src to dest using the links in pathFragsToChoose.  Returns
   * the path.  Links that are used in the path are removed from pathFragsToChoose.
   * If a path can't be built, throws IllegalArgumentException, and pathFragsToChoose
   * is left in an unpredictable state.
   */
  private static List<Link> mergePaths( Device src, Device dest,
                                        LinkedList<Link>... pathFragsToChoose )
  {
    LinkedList<Link> mergedPath = new LinkedList<Link>();
    while ( src != dest )
    {
      Link nextHop = null;
foundHop:
      for (LinkedList<Link> path: pathFragsToChoose)
      {
        Iterator<Link> hops = path.iterator();
        while ( hops.hasNext() )
        {
          Link hop = hops.next();
          if ( hop.containsEndpoint(src) )
          {
            nextHop = hop;
            hops.remove();
            break foundHop;
          }
        }
      }
      if ( nextHop == null )
      {
        throw new IllegalArgumentException("Provided paths do not connect endpoints" );
      }
      mergedPath.add( nextHop );
      src = Utils.getOtherEndpoint(nextHop, src);
    }
    return mergedPath;
  }
}
