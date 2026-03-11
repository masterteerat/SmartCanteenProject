public class Feedback {
    private String feedbackID;
    private Customer cus;
    private int score;
    private String comment;
    private Reservation reservation;

    public Feedback(String feedbackID, Customer cus, int score, String comment, Reservation reservation) {
        this.feedbackID = feedbackID;
        this.cus = cus;
        this.score = score;
        this.comment = comment;
        this.reservation = reservation;
    }

    public String getFeedbackID() { return feedbackID; }
    public void setFeedbackID(String feedbackID) { this.feedbackID = feedbackID; }
    public Customer getCustomer() { return cus; }
    public void setCustomer(Customer cus) { this.cus = cus; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public Reservation getReservation() { return reservation; }
    public void setReservation(Reservation reservation) { this.reservation = reservation; }
}