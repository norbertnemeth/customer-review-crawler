import java.util.Date;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class Review {
	String itemID;
	String reviewID;
	String customerName;
	String customerID;
	String title;
	double rating;
	double fullRating;
	int helpfulVotes;
	int totalVotes;
	boolean verifiedPurchase;
	String realName;
	Date reviewDate;
	String content;

	/**
	 * All the pieces in a review on Amazon.com
	 * 
	 * @param aitemID
	 *            the unique item ID of a product
	 * @param areviewID
	 *            the unique review ID
	 * @param acustomerName
	 *            displayed name of a customer
	 * @param acustomerID
	 *            unique customer ID
	 * @param atitle
	 *            title of the review
	 * @param arating
	 *            star rating out of 5 given by the customer
	 * @param afullRating
	 *            max rating can be given (5 for now)
	 * @param ahelpfulVotes
	 *            number of readers who rated the review as helpful
	 * @param atotalVotes
	 *            total number of readers who voted usefulness of the review
	 * @param verifiedornot
	 *            whether the review is from a verified purchase
	 * @param realnameornot
	 *            whether the customer is using his real name when writing the
	 *            review (obsolete, amazon no longer displays the badge)
	 * @param aReviewDate
	 *            date of the review
	 * @param acontent
	 *            textual content of the review
	 */
	public Review(String aitemID, String areviewID, String acustomerName, String acustomerID, String atitle,
			int arating, int afullRating, int ahelpfulVotes, int atotalVotes, boolean verifiedornot,
			String realnameornot, Date aReviewDate, String acontent) {
		itemID = aitemID;
		reviewID = areviewID;
		customerName = acustomerName;
		customerID = acustomerID;
		title = atitle;
		rating = arating;
		fullRating = afullRating;
		helpfulVotes = ahelpfulVotes;
		totalVotes = atotalVotes;
		verifiedPurchase = verifiedornot;
		realName = realnameornot;
		reviewDate = aReviewDate;
		content = acontent;
	}

	public void updateReview(String aitemid, String areviewid, String acustomername, String acustomerID, String atitle,
			double arating, double afullRating, int ahelpfulVotes, int atotalVotes, boolean verified, String realname,
			Date areviewDate, String acontent) {
		this.itemID = aitemid;
		this.reviewID = areviewid;
		this.customerName = acustomername;
		this.customerID = acustomerID;
		this.title = atitle;
		this.rating = arating;
		this.fullRating = afullRating;
		this.helpfulVotes = ahelpfulVotes;
		this.totalVotes = atotalVotes;
		this.verifiedPurchase = verified;
		this.realName = realname;
		this.reviewDate = areviewDate;
		this.content = acontent;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
