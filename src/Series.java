import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Series {
    String seriesTitle;
    ArrayList<Episode> episodes;

    public Series() {
        this.episodes = new ArrayList<Episode>();
    }

    public void addEpisode(Episode episode) {
        this.episodes.add(episode);
    }

    public void addSubtitleFile(String subtitleFilename) {
        FilenameParser parser = new FilenameParser(subtitleFilename);
        EpisodeIdentifier subtitleEpisodeIdentifier = parser.getEpisodeIdentifier();
        for (int i = 0; i < this.episodes.size(); i++) {
            if ((this.episodes.get(i).episodeIdentifier.season == subtitleEpisodeIdentifier.season) && (this.episodes.get(i).episodeIdentifier.episode.equals(subtitleEpisodeIdentifier.episode))) {
                this.episodes.get(i).subtitleFileName = subtitleFilename;
            }
        }
    }
    public void findSeriesTitle() {
        HashMap<String, Integer> possibilities = new HashMap<String, Integer>();
        for (int i = 0; i < this.episodes.size(); i++) {
            Episode episode = this.episodes.get(i);
            int identifierIndex = episode.videoFilename.indexOf(episode.episodeIdentifierString);
            String namePart = episode.videoFilename.substring(0, identifierIndex);
            String possibleName = namePart.replaceAll("\\.|\\s|\\_", " ");
            if (possibilities.containsKey(possibleName)) {
                possibilities.put(possibleName, possibilities.get(possibleName) + 1);
            } else {
                possibilities.put(possibleName, 1);
            }
        }
        String bestChoice = new String();
        int bestChoiceScore = 0;
        for (Map.Entry<String, Integer> entry: possibilities.entrySet()) {
            if (bestChoice.equals("")) {
                bestChoice = entry.getKey();
                bestChoiceScore = entry.getValue();
            } else {
                if (entry.getValue() > bestChoiceScore) {
                    bestChoice = entry.getKey();
                    bestChoiceScore = entry.getValue();
                }
            }
        }
        this.seriesTitle = bestChoice;
    }

    public String proposeFilename(int episodeIndex) {
        Episode episode = this.episodes.get(episodeIndex);
        String result = this.seriesTitle.concat(" - ")
                .concat(episode.episodeIdentifier.getStandardEpisodeIdentifierString())
                .concat(".")
                .concat(DirectoryParser.getFileExtension(episode.videoFilename));

        return result;
    }

    public HashMap<String, String> getTableData() {
        HashMap<String, String> result = new HashMap<String, String>();
        for (int i = 0; i < this.episodes.size(); i++) {
            result.put(this.episodes.get(i).videoFilename, this.proposeFilename(i));
        }

        return result;
    }
}
