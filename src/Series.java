import java.util.*;

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
        this.seriesTitle = bestChoice.substring(0, 1).toUpperCase().concat(bestChoice.substring(1));
    }

    public String proposeFilename(int episodeIndex) {
        Episode episode = this.episodes.get(episodeIndex);
        String extension = DirectoryParser.getFileExtension(episode.videoFilename);
        return proposeFilename(episodeIndex, extension);
    }

    public String proposeVideoFilename(int episodeIndex) {
        Episode episode = this.episodes.get(episodeIndex);
        String extension = DirectoryParser.getFileExtension(episode.videoFilename);
        return proposeFilename(episodeIndex, extension);
    }

    public String proposeSubtitleFilename(int episodeIndex) {
        Episode episode = this.episodes.get(episodeIndex);
        if (episode.subtitleFileName == null) {
            return "";
        }
        String extension = DirectoryParser.getFileExtension(episode.subtitleFileName);
        return proposeFilename(episodeIndex, extension);
    }

    public String proposeFilename(int episodeIndex, String extension) {
        Episode episode = this.episodes.get(episodeIndex);
        String result = this.seriesTitle
                .replaceAll("-", "")
                .replaceAll("\\s{2,}", "\\s")
                .trim()
                .replaceAll("\\s", "\\.")
                .replaceAll("\\.{2,}", "\\.")
                .trim()
                .concat(".")
                .concat(episode.episodeIdentifier.getStandardEpisodeIdentifierString())
                .concat(".")
                .concat(extension);

        return result;
    }

    public void setSeriesTitle(String seriesTitle) {
        this.seriesTitle = seriesTitle;
    }

    public ArrayList<String[]> getTableData() {
        ArrayList<String[]> result = new ArrayList<String[]>();
        for (int i = 0; i < this.episodes.size(); i++) {
            String[] data = {
                    this.episodes.get(i).videoFilename,
                    this.episodes.get(i).subtitleFileName,
                    this.proposeVideoFilename(i),
                    this.proposeSubtitleFilename(i)};
            result.add(data);
        }

        return result;
    }

    public void sortEpisodesByEpisodeIdentifier() {
        Collections.sort(this.episodes, new Comparator<Episode>() {
            @Override
            public int compare(Episode o1, Episode o2) {
                int result = o1.episodeIdentifier.episode.compareToIgnoreCase(o2.episodeIdentifier.episode);
                return result;
            }
        });
    }
}
