package Models;

public class Review {
    private int reviewId;
    private int userId;
    private int courtId;
    private int rating;
    private String content;
    private String createAt;
    private String status;

    public Review(int reviewId, int userId, int courtId, int rating, String content, String createAt, String status) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.courtId = courtId;
        this.rating = rating;
        this.content = content;
        this.createAt = createAt;
        this.status = status;
    }

    public int getReviewId() {
        return reviewId;
    }

    public int getUserId() {
        return userId;
    }

    public int getCourtId() {
        return courtId;
    }

    public int getRating() {
        return rating;
    }

    public String getContent() {
        return content;
    }

    public String getCreateAt() {
        return createAt;
    }

    public String getStatus() {
        return status;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCourtId(int courtId) {
        this.courtId = courtId;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

