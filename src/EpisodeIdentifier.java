import java.util.ArrayList;
import java.util.StringJoiner;

public class EpisodeIdentifier {
    int season;
    String episode;

    public String getStandardEpisodeIdentifierString() {
        String episodeString = "";
        String[] episodePieces = this.episode.split("\\-");
        StringJoiner stringJoiner = new StringJoiner("-");
        for (int i = 0; i < episodePieces.length; i++) {
            stringJoiner.add(String.format("%02d", Integer.parseInt(episodePieces[i].replaceAll("e", ""))));
        }
        return String.format("S%02dE", this.season).concat(stringJoiner.toString());
    }
}
