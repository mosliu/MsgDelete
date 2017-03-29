package net.liuxuan.msgdelete.model;

/**
 * Created by hanj on 14-10-30.
 */
public class FilterBean {
    private int id;
    private String title;
    private String content;
    private int type;

    public FilterBean() {

    }

    public FilterBean(int id, String title, String content, int type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
