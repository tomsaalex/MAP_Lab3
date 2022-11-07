package domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Friendship extends Entity<Long>{
    Long u1ID, u2ID;
    LocalDateTime friendsFrom;

    public Long getU1ID() {
        return u1ID;
    }

    public Friendship(Long u1ID, Long u2ID, LocalDateTime friendsFrom) {
        this.u1ID = u1ID;
        this.u2ID = u2ID;
        this.friendsFrom = friendsFrom;
    }

    public Long getU2ID() {
        return u2ID;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public Friendship(Long u1ID, Long u2ID) {
        this.u1ID = u1ID;
        this.u2ID = u2ID;
        this.friendsFrom = LocalDateTime.now(); // TODO I hope this works
    }
    /**
     * Converts an object of type Friendship to a string, displaying its ID, firstName, lastName and the list of its friends' IDs.
     */
    @Override
    public String toString()
    {
        return "ID User1: " + getU1ID() + " | ID User2: " + getU2ID() + " | Friends From: " + getFriendsFrom();
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(!(o instanceof Friendship))
            return false;

        Friendship f = (Friendship) o;

        boolean equals1 = Objects.equals(f.getU1ID(), this.getU1ID()) && Objects.equals(f.getU2ID(), this.getU2ID());
        boolean equals2 = Objects.equals(f.getU2ID(), this.getU1ID()) && Objects.equals(f.getU1ID(), this.getU2ID());

        return equals1 || equals2;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getU1ID(), getU2ID());
    }
}
