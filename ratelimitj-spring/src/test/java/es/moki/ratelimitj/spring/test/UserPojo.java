package es.moki.ratelimitj.spring.test;

/**
 * @author Enrico Costanzi
 */
public class UserPojo {

    private String name;

    public UserPojo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
