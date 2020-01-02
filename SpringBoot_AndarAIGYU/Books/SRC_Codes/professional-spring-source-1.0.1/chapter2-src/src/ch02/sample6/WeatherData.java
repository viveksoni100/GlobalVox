package ch02.sample6;

import java.util.Date;

/**
 * Represents a daily weather record
 */
public class WeatherData {

  Date date;

  double low;

  double high;

  /**
   * @return Returns the date.
   */
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  /**
   * @return Returns the low.
   */
  public double getLow() {
    return low;
  }

  public void setLow(double low) {
    this.low = low;
  }

  /**
   * @return Returns the high.
   */
  public double getHigh() {
    return high;
  }

  public void setHigh(double high) {
    this.high = high;
  }
}