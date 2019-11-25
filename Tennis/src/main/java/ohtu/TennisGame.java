package ohtu;

public class TennisGame {

    private int m_score1 = 0;
    private int m_score2 = 0;
    private String player1Name;
    private String player2Name;

    public TennisGame(String player1Name, String player2Name) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
    }

    public void wonPoint(String playerName) {
        if (playerName == "player1")
            m_score1 += 1;
        else
            m_score2 += 1;
    }

    public String getScore() {
        String score = "";
        if (m_score1 == m_score2) {
            score = getEvenStanding();
        } else if (m_score1 >= 4 || m_score2 >= 4) {
            score = getMatchPoint();
        } else {
            score = getStanding();
        }
        return score;
    }

    private String getStanding() {
        return getPart(m_score1) + "-" + getPart(m_score2);
    }

    private String getPart(int score) {
        switch (score) {
        case 0:
            return "Love";
        case 1:
            return "Fifteen";
        case 2:
            return "Thirty";
        default:
            return "Forty";
        }
    }

    private String getMatchPoint() {
        int minusResult = m_score1 - m_score2;
        if (minusResult == 1)
            return "Advantage player1";
        else if (minusResult == -1)
            return "Advantage player2";
        else if (minusResult >= 2)
            return "Win for player1";
        else
            return "Win for player2";
    }

    private String getEvenStanding() {
        switch (m_score1) {
        case 0:
            return "Love-All";
        case 1:
            return "Fifteen-All";
        case 2:
            return "Thirty-All";
        case 3:
            return "Forty-All";
        default:
            return "Deuce";

        }
    }
}