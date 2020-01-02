package org.springframework.prospring.ticket.db;

import java.util.Date;

public class Poster {
    int id;
    Date firstPerformance;
    byte[] posterImage;

    public Poster(int id, Date firstPerformance, byte[] posterImage) {
      this.id = id;
      this.firstPerformance = firstPerformance;
      this.posterImage = posterImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFirstPerformance() {
        return firstPerformance;
    }

    public void setFirstPerformance(Date firstPerformance) {
        this.firstPerformance = firstPerformance;
    }

    public byte[] getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(byte[] posterImage) {
        this.posterImage = posterImage;
    }
}
