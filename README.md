customer-review-crawler - pure
===============================

A crawler to collect reviews and product infomation on Amazon.com.

# Quick Start Guide


## Get reviews from one product
To get all reviews for a product, first get the Amazon Standard Identification Number (ASIN) of the product. It is the 10-character alphanumeric ID followed by /product/ in the url.
Then add the following code to the main function
```java
	Item samsungTab3 = new Item("B00D02AGU4");
	samsungTab3.fetchReview();
	System.out.println(an_item.getReviews());
```

# Common Exceptions
1. java.io.IOException means that the item no longer exist on Amazon.com. You do not have to do anything with that item.
2. java.net.SocketTimeoutException means that connection to the website is taking too long. Rerun the crawler on the items with this exception.

# Original Repo

[Link](https://github.com/maifeng/customer-review-crawler)

# Licence
The code is released into public domain. If you find the code useful in your research work, I appreciate if you can cite
"Market Dynamics and User-Generated Content about Tablet Computers" by Xin (Shane) Wang, Feng Mai and Roger H.L. Chiang, Marketing Science 33.3 (2014): 449-458
