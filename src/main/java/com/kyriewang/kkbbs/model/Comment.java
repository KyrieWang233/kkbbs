package com.kyriewang.kkbbs.model;

public class Comment {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.id
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.parent_id
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    private Long parent_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.type
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    private Integer type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.comment_creator
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    private Long comment_creator;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.gmt_create
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    private Long gmt_create;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.gmt_modified
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    private Long gmt_modified;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.like_count
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    private Long like_count;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.comment_count
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    private Integer comment_count;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.content
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    private String content;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.id
     *
     * @return the value of comment.id
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.id
     *
     * @param id the value for comment.id
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.parent_id
     *
     * @return the value of comment.parent_id
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    public Long getParent_id() {
        return parent_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.parent_id
     *
     * @param parent_id the value for comment.parent_id
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.type
     *
     * @return the value of comment.type
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.type
     *
     * @param type the value for comment.type
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.comment_creator
     *
     * @return the value of comment.comment_creator
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    public Long getComment_creator() {
        return comment_creator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.comment_creator
     *
     * @param comment_creator the value for comment.comment_creator
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    public void setComment_creator(Long comment_creator) {
        this.comment_creator = comment_creator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.gmt_create
     *
     * @return the value of comment.gmt_create
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    public Long getGmt_create() {
        return gmt_create;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.gmt_create
     *
     * @param gmt_create the value for comment.gmt_create
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    public void setGmt_create(Long gmt_create) {
        this.gmt_create = gmt_create;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.gmt_modified
     *
     * @return the value of comment.gmt_modified
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    public Long getGmt_modified() {
        return gmt_modified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.gmt_modified
     *
     * @param gmt_modified the value for comment.gmt_modified
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    public void setGmt_modified(Long gmt_modified) {
        this.gmt_modified = gmt_modified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.like_count
     *
     * @return the value of comment.like_count
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    public Long getLike_count() {
        return like_count;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.like_count
     *
     * @param like_count the value for comment.like_count
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    public void setLike_count(Long like_count) {
        this.like_count = like_count;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.comment_count
     *
     * @return the value of comment.comment_count
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    public Integer getComment_count() {
        return comment_count;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.comment_count
     *
     * @param comment_count the value for comment.comment_count
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    public void setComment_count(Integer comment_count) {
        this.comment_count = comment_count;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.content
     *
     * @return the value of comment.content
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.content
     *
     * @param content the value for comment.content
     *
     * @mbg.generated Mon Nov 23 21:59:58 CST 2020
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}