public class Feedback {
    private Customer cus;
    private int score;
    private String comment;
    private Reservation reservation;

    public Feedback(Customer cus, int score, String comment, Reservation reservation) {
        this.cus = cus;
        this.score = score;
        this.comment = comment;
        this.reservation = reservation;
    }

    public Customer getCustomer() { return cus; }
    public void setCustomer(Customer cus) { this.cus = cus; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public Reservation getReservation() { return reservation; }
    public void setReservation(Reservation reservation) { this.reservation = reservation; }
}