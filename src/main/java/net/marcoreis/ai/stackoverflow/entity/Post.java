package net.marcoreis.ai.stackoverflow.entity;

@lombok.Data
public class Post {
	private String id;
	private String parentId;
	private String ownerDisplayName;
	private String closedDate;
	private String communityOwnedDate;
	private String lastEditorDisplayName;
	private String postTypeId;
	private String acceptedAnswerId;
	private String creationDate;
	private String score;
	private String viewCount;
	private String body;
	private String ownerUserId;
	private String lastEditorUserId;
	private String lastEditDate;
	private String lastActivityDate;
	private String title;
	private String tags;
	private String answerCount;
	private String commentCount;
	private String favoriteCount;
}
