package com.masm.map;

/**
 * Created by simple on 2017/8/24.
 */
public class MyList {

    private Object[] value;
    private int size;

    public MyList() {
        this(10);
    }

    public MyList(int initialCapacity) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal Capacity: "+
                    initialCapacity);
        value = new Object[initialCapacity];
    }

    public void add(Object object) {
        value[size] = object;
        size++;
        if (size >= value.length) {//扩容
            int newCapacity = value.length * 2;
            Object[] newList = new Object[newCapacity];
            //System.arraycopy(value, 0, newList, 0, value.length);
            for (int i=0;i<value.length;i++) {
                newList[i] = value[i];
            }
            value = newList;
        }
    }

    public Object get(int index) {
        if (index>=size || index<0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        return value[index];
    }

    public int size() {
        return size;
    }

    public int indexOf(Object obj) {
        if (obj == null) {
            for (int i=0;i<size;i++) {
                if (value[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i=0;i<size;i++) {
                if (value[i].equals(obj)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public Object set(int index, Object o) {
        if (index>=size || index<0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        Object oldValue = value[index];
        value[index] = o;
        return oldValue;
    }

    public int lastIndexOf(Object obj) {
        if (obj == null) {
            for (int i=0;i<size;i++) {
                if (value[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i=size-1;i>=0;i--) {
                if (value[i].equals(obj)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }
}
