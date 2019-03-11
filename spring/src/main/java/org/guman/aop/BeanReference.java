package org.guman.aop;

/**
 * @author duanhaoran
 * @since 2019/3/10 6:20 PM
 */
public class BeanReference<T> {

    private String name;

    private T ref;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getRef() {
        return ref;
    }

    public void setRef(T ref) {
        this.ref = ref;
    }

    public BeanReference(String name) {
        this.name = name;
    }
}