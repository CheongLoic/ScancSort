package scansort.fr;

public class Member {
    private String LastName;
    private String FirstName;
    private String Password;
    private String Email;
    private String Score;

    public Member() {}

    public Member(String lastName, String firstName, String email) {
        LastName = lastName;
        FirstName = firstName;
        Email = email;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
