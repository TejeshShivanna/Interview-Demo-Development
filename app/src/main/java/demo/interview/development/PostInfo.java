package demo.interview.development;

public class PostInfo {
    String postId, message, link, description, createdTime, fullPicture, name;
    int reach;

    public PostInfo(String postId, String message, String link, String name, String description, String createdTime, String fullPicture) {
        this.postId = postId;
        this.message = message;
        this.link = link;
        this.name = name;
        this.description = description;
        this.createdTime = createdTime;
        this.fullPicture = fullPicture;
        this.reach = 0;
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
}
