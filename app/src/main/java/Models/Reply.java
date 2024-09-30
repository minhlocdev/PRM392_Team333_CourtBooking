package Models;

public class Reply {
    private int replyId;
    private int reviewId;
    private String content;
    private String createAt;
    private String status;

    public Reply(int replyId, int reviewId, String content, String createAt, String status) {
        this.replyId = replyId;
        this.reviewId = reviewId;
        this.content = content;
        this.createAt = createAt;
        this.status = status;
    }

    public int getReplyId() {
        return replyId;
    }

    public int getReviewId() {
        return reviewId;
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

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
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

