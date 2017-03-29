package net.liuxuan.msgdelete.model;

/**
 * Created by hanj on 14-10-30.
 */
public class SmsBean {
    private int id;
    private String fromNum;
    private String content;
    private long time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFromNum() {
        return fromNum;
    }

    public void setFromNum(String fromNum) {
        this.fromNum = fromNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "SmsBean{" +
                "id=" + id +
                ", fromNum='" + fromNum + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SmsBean) {
            SmsBean other = (SmsBean) o;
            return this.id == other.id;
        }

        return false;
    }
}
