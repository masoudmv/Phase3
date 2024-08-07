package shared.response.game;

import shared.Model.Match;
import shared.response.Response;
import shared.response.ResponseHandler;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.List;


@JsonTypeName("EndOfGameResponse")

public class EndOfGameResponse implements Response {
    private Match match;
    private List<Match> matches;
    private boolean defeated;

    public EndOfGameResponse(Match match, List<Match> matches, boolean defeated) {
        this.match = match;
        this.matches = matches;
        this.defeated = defeated;
    }

    public EndOfGameResponse() {
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public boolean isDefeated() {
        return defeated;
    }

    public void setDefeated(boolean defeated) {
        this.defeated = defeated;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleEndOfGameResponse(this);
    }
}
