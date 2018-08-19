public class EpisodeIdentifier {
    int season;
    String episode;

    public String getStandardEpisodeIdentifierString() {
        return String.format("S%02dE", this.season).concat(this.episode);
    }
}
