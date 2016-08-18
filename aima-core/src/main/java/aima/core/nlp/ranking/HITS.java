package aima.core.nlp.ranking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 871.<br>
 * <br>
 * 
 * <pre>
 * function HITS(query) returns pages (with hub and authority numbers)
 *   pages &larr; EXPAND-PAGES(RELEVANT-PAGES(query))
 *   for each p in pages do 
 *   	p.AUTHORITY &larr; 1
 *   	p.HUB &larr; 1
 *   repeat until convergence do
 *   	for each p in pages do
 *   		p.AUTHORITY &larr; &Sigma<sub>i</sub> INLINK<sub>i</sub>(p).HUB
 *   		p.HUB &larr; &Sigma<sub>i</sub> OUTLINK<sub>i</sub>(p).AUTHORITY
 *   	NORMALIZE(pages)
 *   return pages
 * </pre>
 * 
 * Figure 22.1 The HITS algorithm for computing hubs and authorities with respect to a query.
 * RELEVANT-PAGES fetches the pages that match the query, and EXPAND-PAGES add in every page
 * that links to or is linked from one of the relevant pages. NORMALIZE divides each page's
 * score by the sum of the squares of all pages' scores (separately for both the authority
 * and hubs scores.<br>
 * <br>
 * 
 * @author Jonathon Belotti (thundergolfer)
 *
 */
public class HITS {

	final int RANK_HISTORY_DEPTH;
	final double DELTA_TOLERANCE; // somewhat arbitrary
	Map<String,Page> pTable; 
	// DETECT CONVERGENCE VARS
    double[] prevAuthVals;
    double[] prevHubVals;
	double prevAveHubDelta = 0;
	double prevAveAuthDelta = 0; 
	////////////////////////////
    
    // TODO: Improve the convergence detection functionality
    public HITS( Map<String,Page> pTable, int rank_hist_depth, double delta_tolerance ) {
    	this.pTable = pTable;
    	this.RANK_HISTORY_DEPTH = rank_hist_depth;
    	this.DELTA_TOLERANCE = delta_tolerance;
    	
    }
    
    public HITS( Map<String, Page> pTable ) {
    	this( pTable, 3, 0.05);
    }
	
    // function HITS(query) returns pages with hub and authority number
	public List<Page> hits( String query ) { 
		List<Page> pages;
		pages = expandPages( relevantPages( query ));
		// for each p in pages
		for( Page p: pages ) { 
			p.authority = 1;
			p.hub = 1; 
		}
		while( !convergence( pages ) ) {
			// for each p in pages do
			for( Page p : pages ) {
				p.authority = SumInlinkHubScore( p );
				p.hub       = SumOutlinkAuthorityScore( p );
			}
			normalize(pages);
		}
		return pages;
		
	}
	
	/**
	 * Fetches and returns all pages that match the query
	 * @param query
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public List<Page> relevantPages( String query ) {
		List<Page> relevantPages = new ArrayList<Page>();
		for( Page p: pTable.values()) {
			if( matches( query, p.getContent() )) {
				relevantPages.add(p);
			}
		}
		return relevantPages;
	}
	
	/**
	 * Simple check if query string is a substring of a block 
	 * of text. 
	 * @param query
	 * @param text
	 * @return
	 */
	public boolean matches( String query, String text ) {
		return text.contains(query);
	}
		
	/**
	 * Adds pages that are linked to or is linked from one of the pages 
	 * passed as argument.
	 * 
	 * @param pages
	 * @return
	 */
	public List<Page> expandPages( List<Page> pages ) {
		
		List<Page> expandedPages = new ArrayList<Page>();
		Set<String> inAndOutLinks = new HashSet<String>();
		// Go through all pages an build a list of String links
		for( int i=0; i < pages.size(); i++ ) {
			Page currP = pages.get(i);
			if( !expandedPages.contains(currP)) {
				expandedPages.add(currP);
			}
			List<String> currInlinks = currP.getInlinks();
			for( int j=0; j < currInlinks.size(); j++ ) {
				inAndOutLinks.add(currInlinks.get(i));
			}
			List<String> currOutlinks = currP.getOutlinks();
			for( int j=0; j < currOutlinks.size(); j++ ) {
				inAndOutLinks.add(currOutlinks.get(i));
			}
		}
		// go through String links and add their respective pages to our return list
		Iterator<String> it = inAndOutLinks.iterator();
		while( it.hasNext() ) {
			String addr = it.next();
			Page p = pTable.get(addr);
			if( p != null && !expandedPages.contains(p)) { // a valid link may not have an associated page in our table
				expandedPages.add(p); 
			}
		}
		return expandedPages;
	} // end expandPages();
	
	/**
	 * Divides each page's score by the sum of the squares of all pages' scores 
	 * (separately for both the authority and hubs scores
	 * @param pages
	 * @return
	 */
	public List<Page> normalize( List<Page> pages ) {
		double hubTotal = 0; double authTotal = 0;
		for( Page p: pages) {
			// Sum Hub scores over all pages
			hubTotal += Math.pow(p.hub, 2);
			// Sum Authority scores over all pages
			authTotal += Math.pow(p.authority, 2);		}
		// divide all hub and authority scores for all pages
		for( Page p: pages ) {
			if( hubTotal > 0 ) { p.hub /= hubTotal; } 
			else { p.hub = 0; }
			if( authTotal > 0 ) { p.authority /= authTotal; }
			else { p.authority = 0; }
		}
		return pages; // with normalised scores now
	} // end normalize()
	
	/**
	 * Calculate the Authority score of a page by summing the 
	 * Hub scores of that page's inlinks.
	 * @param page
	 * @param pagesTable
	 * @return
	 */
	public double SumInlinkHubScore( Page page ) {
		List<String> inLinks = page.getInlinks();
		double hubScore = 0;
		for( int i=0; i < inLinks.size(); i++ ) {
			Page inLink = pTable.get(inLinks.get(i));
			if( inLink != null ) {
				hubScore += inLink.hub;
			}
			else {
				// page is linked to by a Page not in our table
				continue;
			}
		}
		return hubScore;
	} // end SumInlinkHubScore() 
	
	/**
	 * Calculate the Hub score of a page by summing the 
	 * Authority scores of that page's outlinks.
	 * @param page
	 * @param pagesTable
	 * @return
	 */
	public double SumOutlinkAuthorityScore( Page page ) {
		List<String> outLinks = page.getOutlinks();
		double authScore = 0;
		for( int i=0; i < outLinks.size(); i++ ) {
			Page outLink = pTable.get(outLinks.get(i));
			if( outLink != null ) {
				authScore += outLink.authority;
			}
		}
		return authScore;
	}
	
	/**
	 * pg. 872 : "If we then normalize the scores and repeat k times
	 * the process will converge"
	 * @return
	 */
	private boolean convergence( List<Page> pages ) {
		double aveHubDelta = 100; double aveAuthDelta = 100;
		if( pages == null ) {
			return true;
		} 
		
		// get current values from pages
		double[] currHubVals = new double[pages.size()];
		double[] currAuthVals = new double[pages.size()];
		for( int i=0; i < pages.size(); i++ ) {
			Page currPage = pages.get(i);
			currHubVals[i] = currPage.hub;
			currHubVals[i] = currPage.authority;
		}
		if( prevHubVals == null || prevAuthVals == null ) {
			prevHubVals = currHubVals;
			prevAuthVals = currAuthVals;
			return false;
		}
		// compare to past values
		aveHubDelta = getAveDelta( currHubVals, prevHubVals );
		aveAuthDelta = getAveDelta( currAuthVals, prevAuthVals );
		if( aveHubDelta + aveAuthDelta < DELTA_TOLERANCE || 
			(Math.abs(prevAveHubDelta-aveHubDelta) < 0.01 && Math.abs(prevAveAuthDelta-aveAuthDelta) < 0.01)) {
			return true;
		}
		else {
			prevHubVals = currHubVals;
			prevAuthVals = currAuthVals;
			prevAveHubDelta = aveHubDelta; 
			prevAveAuthDelta = aveAuthDelta;
			return false;
		}
	}
	
	/**
	 * Determine how much values in a list are changing. Useful for detecting 
	 * convergence of data values.
	 * @param r
	 * @return
	 */
	public double getAveDelta( double[] curr, double[] prev ) {
		double aveDelta = 0;
		assert(curr.length == prev.length);
		for( int j=0; j < curr.length; j++ ) {
			aveDelta += Math.abs(curr[j] - prev[j]);
		}
		aveDelta /= curr.length;
		return aveDelta;
	}
	
	/**
	 * Return from a set of Pages the Page with the greatest Hub value
	 * @param pageTable
	 * @return
	 */
	public Page getMaxHub( List<Page> result ) {
		Page maxHub = result.get(0);
		for( int i=1; i < result.size(); i++ ) {
			Page currPage = result.get(i);
			if( currPage.hub > maxHub.hub ) { maxHub = currPage; }
		}
		return maxHub;
	}
	
	/**
	 * Return from a set of Pages the Page with the greatest Authority value
	 * @param pageTable
	 * @return
	 */
	public Page getMaxAuthority( List<Page> result ) {
		Page maxAuthority = result.get(0);
		for( int i=1; i < result.size(); i++ ) {
			Page currPage = result.get(i);
			if( currPage.authority > maxAuthority.authority ) {	maxAuthority = currPage; }
		}
		return maxAuthority;
	}
	
	/**
	 * Organize the list of pages according to their descending Hub scores.
	 * @param result
	 */
	public void sortHub( List<Page> result ) {
		Collections.sort(result, new Comparator<Page>() {
	        public int compare(Page p1, Page p2) {
	            //Sorts by 'TimeStarted' property
	            return p1.hub<p2.hub? -1:p1.hub>p2.hub ? 1 : secondaryOrderSort(p1,p2);
	        }

	        //If 'TimeStarted' property is equal sorts by 'TimeEnded' property
	        public int secondaryOrderSort(Page p1,Page p2) {
	            return p1.getLocation().compareToIgnoreCase(p2.getLocation()) < 1?-1:p1.getLocation().compareToIgnoreCase(p2.getLocation()) > 1?1:0;
	        }
	    });
	}
	
	/**
	 * Organize the list of pages according to their descending Authority Scores
	 * @param result
	 */
	public void sortAuthority( List<Page> result ) {
		Collections.sort(result, new Comparator<Page>() {
	        public int compare(Page p1, Page p2) {
	            //Sorts by 'TimeStarted' property
	            return p1.hub<p2.hub? -1:p1.hub>p2.hub ? 1 : secondaryOrderSort(p1,p2);
	        }

	        //If 'TimeStarted' property is equal sorts by 'TimeEnded' property
	        public int secondaryOrderSort(Page p1,Page p2) {
	            return p1.getLocation().compareToIgnoreCase(p2.getLocation()) < 1?-1:p1.getLocation().compareToIgnoreCase(p2.getLocation()) > 1?1:0;
	        }
	    });
	}
	
	/**
	 * Simple console display of HITS Algorithm results.
	 * @param result
	 */
	public void report( List<Page> result ) {
		
		// Print Pages out ranked by highest authority
		sortAuthority(result);
		System.out.println("AUTHORITY RANKINGS : ");
		for( int i=0; i < result.size(); i++ ) {
			Page currP = result.get(i);
			System.out.printf(currP.getLocation() + ": " + "%.5f" + '\n', currP.authority);
		}
		System.out.println();
		// Print Pages out ranked by highest hub 
		sortHub(result);
		System.out.println("HUB RANKINGS : ");
		for( int i=0; i < result.size(); i++ ) {
			Page currP = result.get(i);
			System.out.printf(currP.getLocation() + ": " + "%.5f" + '\n', currP.hub);
		}
		System.out.println();
		// Print Max Authority
		System.out.println("Page with highest Authority score: " + getMaxAuthority(result).getLocation());
		// Print Max Authority
		System.out.println("Page with highest Hub score: " + getMaxAuthority(result).getLocation());
	}
	
}
