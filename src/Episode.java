public class Episode {
    String videoFilename;
    String subtitleFileName;
    EpisodeIdentifier episodeIdentifier;
    String episodeIdentifierString;

    public Episode(String filename) {
        this.videoFilename = filename;
        this.subtitleFileName = null;
        FilenameParser parser = new FilenameParser(this.videoFilename);
        this.episodeIdentifier = parser.getEpisodeIdentifier();
        this.episodeIdentifierString = parser.getEpisodeIdentifierString();
    }
}
