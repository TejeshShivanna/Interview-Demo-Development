package demo.interview.development;

public class PostInfo {
    private String postId, message, link, description, createdTime, fullPicture, name;
    int reach;

    boolean isPublished;

    public PostInfo(String postId, String message, String link, String name, String description, String createdTime, String fullPicture, boolean isPublished) {
        this.postId = postId;
        this.message = message;
        this.link = link;
        this.name = name;
        this.description = description;
        this.createdTime = createdTime;
        this.fullPicture = fullPicture;
        this.reach = 0;
        this.isPublished = isPublished;
    }

    public int getReach() {
        return reach;
    }

    public void setReach(int reach) {
        this.reach = reach;
    }

    public String getPostId() {
        return postId;
    }

    public String getMessage() {
        return message;
    }

    public boolean getIsPublished() {
        return isPublished;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getName() {
        return name;
    }
}