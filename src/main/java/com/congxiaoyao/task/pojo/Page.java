package com.congxiaoyao.task.pojo;

public class Page {
    private Integer current;
    private Integer next;
    private Integer size;
    private Long total;

    public Page(Integer current, Integer next, Integer size, Long total) {
        this.current = current;
        this.next = next;
        this.size = size;
        this.total = total;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getNext() {
        return next;
    }

    public void setNext(Integer next) {
        this.next = next;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Page{" +
                "current=" + current +
                ", next=" + next +
                ", size=" + size +
                ", total=" + total +
                '}';
    }
}