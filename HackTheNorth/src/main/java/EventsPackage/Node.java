package EventsPackage;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public class Node {

    private Member member;
    private User user;
    private Node next;

    public Node(Member member) {
        this.member = member;
        this.user = member.getUser();
        this.next = null;
    }

    public User getUser() {
        return user;
    }

    public Member getMember() {
        return member;
    }

    public String getName() {
        return user.getName();
    }

    public Node getNext() {
        return next;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
