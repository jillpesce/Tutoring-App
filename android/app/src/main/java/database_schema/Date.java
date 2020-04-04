package database_schema;

public class Date implements Comparable {
    private int year;
    private int month;
    private int day;
    private int hour;
    private String code;

    public Date(int y, int m, int d, int h) {
        year = y;
        month = m;
        day = d;
        hour = h;
    }

    public Date(String s) {
        code = s;
        year = Integer.parseInt(s.substring(0,4));
        month = Integer.parseInt(s.substring(4,6));
        day = Integer.parseInt(s.substring(6,8));
        hour = Integer.parseInt(s.substring(8,10));
    }

    public int getYearInt() {
        return year;
    }

    public int getMonthInt() {
        return month;
    }

    public int getDayInt() {
        return day;
    }

    public int getHourInt() {
        return hour;
    }

    public String getMonthString() {
        switch (month) {
            case 1: return "January";
            case 2: return "February";
            case 3: return "March";
            case 4: return "April";
            case 5: return "May";
            case 6: return "June";
            case 7: return "July";
            case 8: return "August";
            case 9: return "September";
            case 10: return "October";
            case 11: return "November";
            case 12: return "December";
            default: return "default Month";
        }
    }

    public String getTimeString() {
        switch (hour) {
            case 8: return "8:00 AM";
            case 9: return "9:00 AM";
            case 10: return "10:00 AM";
            case 11: return "11:00 AM";
            case 12: return "12:00 PM";
            case 13: return "1:00 PM";
            case 14: return "2:00 PM";
            case 15: return "3:00 PM";
            case 16: return "4:00 PM";
            case 17: return "5:00 PM";
            case 18: return "6:00 PM";
            case 19: return "7:00 PM";
            case 20: return "8:00 PM";
            case 21: return "9:00 PM";
            case 22: return "10:00 PM";
            case 23: return "11:00 PM";
            default: return "default Month";
        }
    }

    public String getFullDescription() {
        return (this.getMonthString() + " " + day +", "+year +" at " +getTimeString());
    }

    public String getDateDescription() {
        return (this.getMonthString() + " " + day +", "+year);
    }

    @Override
    public int compareTo(Object o) {
        Date d2 = (Date) o;

        if (d2.getYearInt() < this.year) {
            return -1;
        } else if (d2.getYearInt() > this.year) {
            return 1;
        } else {
            if (d2.getMonthInt() < this.month) {
                return -1;
            } else if (d2.getMonthInt() > this.month) {
                return 1;
            } else {
                if (d2.getDayInt() < this.day) {
                    return -1;
                } else if (d2.getDayInt() > this.day) {
                    return 1;
                } else {
                    if (d2.getHourInt() < this.hour) {
                        return -1;
                    } else if (d2.getHourInt() > this.hour) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        }
    }
}
