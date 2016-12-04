import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * A product and all of its reviews
 * 
 * @author Feng Mai
 * 
 */

public class Item {

	public String itemID;
	public ArrayList<Review> reviews;

	public Item(String theitemid) {
		itemID = theitemid;
		reviews = new ArrayList<Review>();
	}

	public void addReview(Review thereview) {
		reviews.add(thereview);
	}

	/**
	 * retry the Jsoup connection
	 * 
	 * @param url
	 * @return
	 */
	public org.jsoup.nodes.Document jsoup_load_with_retry(String url) throws IOException {
		int max_retry = 10;
		int retry = 1;
		int sleep_sec = 2;
		org.jsoup.nodes.Document content = null;

		while (retry <= max_retry) {
			try {
				System.out.println("GET: " + url);
				content = Jsoup.connect(url).timeout(30000).userAgent("Mozilla/17.0").get();
				break;
			} catch (Exception ex) {
				// wait before retry
				System.out.println(ex.getMessage() + " retrying..");
				try {
					TimeUnit.SECONDS.sleep(sleep_sec);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			retry++;
		}
		return content;
	}

	/**
	 * Fetch all reviews for the item from Amazon.com
	 */
	public void fetchReview() {
		String url = "http://www.amazon.com/product-reviews/" + itemID
				+ "/?showViewpoints=0&sortBy=byRankDescending&pageNumber=" + 1;
		try {
			// Get the max number of review pages;
			org.jsoup.nodes.Document reviewpage1 = null;
			// reviewpage1 = Jsoup.connect(url).timeout(10*1000).get();
			reviewpage1 = jsoup_load_with_retry(url);
			int maxpage = 1;
			Elements pagelinks = reviewpage1.select("a[href*=pageNumber=]");
			if (pagelinks.size() != 0) {
				ArrayList<Integer> pagenum = new ArrayList<Integer>();
				for (Element link : pagelinks) {
					try {
						pagenum.add(Integer.parseInt(link.text()));
					} catch (NumberFormatException nfe) {
					}
				}
				maxpage = Collections.max(pagenum);
			}
			// collect review from each of the review pages;
			for (int p = 1; p <= maxpage; p = p + 1) {
				url = "http://www.amazon.com/product-reviews/" + itemID + "/?sortBy=helpful&pageNumber=" + p;
				org.jsoup.nodes.Document reviewpage = null;
				// reviewpage = Jsoup.connect(url).timeout(10*1000).get();
				reviewpage = jsoup_load_with_retry(url);
				if (reviewpage.select("div.a-section.review").isEmpty()) {
					System.out.println(itemID + " " + "no reivew");
				} else {
					Elements reviewsHTMLs = reviewpage.select("div.a-section.review");
					for (Element reviewBlock : reviewsHTMLs) {
						Review theReview = cleanReviewBlock(reviewBlock);
						this.addReview(theReview);
					}
				}

			}

		} catch (Exception e) {
			System.out.println(itemID + " " + "Exception" + " " + e.toString());
		}

	}

	/**
	 * cleans the html block that contains a review
	 * 
	 * @param reviewBlock
	 *            a html review block (Jsoup Element)
	 * @return
	 * @throws ParseException
	 */
	public Review cleanReviewBlock(Element reviewBlock) throws ParseException {
		String theitemID = this.itemID;
		String reviewID = "";
		String customerName = "";
		String customerID = "";
		String title = "";
		int rating = 0;
		int fullRating = 5;
		int helpfulVotes = 0;
		int totalVotes = -1; // Note: 2016-06-25: Amazon no longer shows the
								// total helpful votes
		boolean verifiedPurchase = false;
		String realName = "N/A"; // Note 2015-11-14 : Amazon no longer shows the
									// real name badge
		Date reviewDate = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse("January 1, 1900");
		String content = "";

		// review id
		reviewID = reviewBlock.id();
		try {
			// customer name and id
			Elements customerIDs = reviewBlock.getElementsByAttributeValueContaining("href", "/gp/pdp/profile/");
			if (customerIDs.size() > 0) {
				Element customer = customerIDs.first();
				String customerhref = customer.attr("href");
				String patternString = "(/gp/pdp/profile/)(.+)(/)";
				Pattern pattern = Pattern.compile(patternString);
				Matcher matcher = pattern.matcher(customerhref);
				while (matcher.find()) {
					// cutomer id;
					customerID = matcher.group(2);
					// customer name;
					customerName = customer.text();
				}
			}
			// title
			Element reviewTitle = reviewBlock.select("a.review-title").first();
			title = reviewTitle.text();

			// rating
			Element star = reviewBlock.select("i.a-icon-star").first();
			String starinfo = star.text();
			rating = Integer.parseInt(starinfo.substring(0, 1));

			// usefulness voting
			Elements votes = reviewBlock.select("span.review-votes");
			if (votes.size() > 0) {
				String votingtext = votes.first().text();
				Pattern pattern2 = Pattern.compile("(\\S+)( people found this helpful)");
				Matcher matcher2 = pattern2.matcher(votingtext);
				matcher2.find();
				while (matcher2.find()) {
					helpfulVotes = Integer.parseInt(matcher2.group(1).replaceAll(",", ""));
				}
				// totalVotes =
				// Integer.parseInt(matcher2.group(3).replaceAll(",", ""));
			}

			// verified purchase
			Elements verified = reviewBlock.select("span.a-size-mini:contains(Verified Purchase)");
			if (verified.size() > 0) {
				verifiedPurchase = true;
			}

			// review date
			Elements date = reviewBlock.select("span.review-date");
			String datetext = date.first().text();
			datetext = datetext.substring(3); // remove "On "
			reviewDate = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(datetext);

			// review content
			Element contentDoc = reviewBlock.select("span.review-text").first();
			content = contentDoc.text();
		} catch (Exception e) {
			System.out.println(reviewID + " " + "Exception" + " " + e.toString());
		}
		Review thereview = new Review(theitemID, reviewID, customerName, customerID, title, rating, fullRating,
				helpfulVotes, totalVotes, verifiedPurchase, realName, reviewDate, content);
		return thereview;
	}
	
	public ArrayList<Review> getReviews() {
		return reviews;
	}
}
