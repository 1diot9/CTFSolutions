package com.example.jseri.entity;

/* loaded from: app(1).jar:BOOT-INF/classes/com/example/jseri/entity/DataVo.class */
public class DataVo {
    private String url;
    private String type;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DataVo)) {
            return false;
        }
        DataVo other = (DataVo) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$url = getUrl();
        Object other$url = other.getUrl();
        if (this$url == null) {
            if (other$url != null) {
                return false;
            }
        } else if (!this$url.equals(other$url)) {
            return false;
        }
        Object this$type = getType();
        Object other$type = other.getType();
        return this$type == null ? other$type == null : this$type.equals(other$type);
    }

    protected boolean canEqual(Object other) {
        return other instanceof DataVo;
    }

    public int hashCode() {
        Object $url = getUrl();
        int result = (1 * 59) + ($url == null ? 43 : $url.hashCode());
        Object $type = getType();
        return (result * 59) + ($type == null ? 43 : $type.hashCode());
    }

    public String toString() {
        return "DataVo(url=" + getUrl() + ", type=" + getType() + ")";
    }

    public String getUrl() {
        return this.url;
    }

    public String getType() {
        return this.type;
    }
}