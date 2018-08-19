import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilenameParser {
    String filename;
    final String episodeIdentifierRegex = "(?i)(\\.|\\-|\\_|\\s)s{0,1}\\d{1,2}(e|x){0,1}\\d{1,2}(\\-e\\d{1,2}){0,1}(\\.|\\-|\\_|\\s)";

    public FilenameParser(String filename) {
        this.filename = filename;
    }

    public String getEpisodeIdentifierString() {
        return getFirstRegexMatchFromFilename(this.episodeIdentifierRegex);
    }

    public EpisodeIdentifier getEpisodeIdentifier()
    {
        EpisodeIdentifier result = new EpisodeIdentifier();
        String identifierString = getEpisodeIdentifierString();
        if (identifierString != null) {
            result.season = getSeasonFromEpisodeIdentifierString(identifierString);
            result.episode = getEpisodeFromEpisodeIdentifierString(identifierString);
        }
        return result;
    }

    private String getFirstRegexMatchFromFilename(String regexExpression) {
        Pattern pattern = Pattern.compile(regexExpression);
        Matcher matcher = pattern.matcher(this.filename);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

    private int getSeasonFromEpisodeIdentifierString(String episodeIdentifierString) {
        String lowerCaseSubject = episodeIdentifierString.toLowerCase().replaceAll("(\\.|\\_)", "").trim();
        if (lowerCaseSubject.indexOf('s') > -1) {
            int startIndex = lowerCaseSubject.indexOf('s');
            int endIndex = lowerCaseSubject.indexOf('e', startIndex);
            return Integer.parseInt(lowerCaseSubject.substring(startIndex + 1, endIndex));
        }
        if (lowerCaseSubject.indexOf('x') > -1) {
            String[] pieces = lowerCaseSubject.split("x");
            return Integer.parseInt(pieces[0]);
        }
        if (lowerCaseSubject.length() == 4) {
            return Integer.parseInt(lowerCaseSubject.substring(0, 2));
        }
        if (lowerCaseSubject.length() == 3) {
            return Integer.parseInt(lowerCaseSubject.substring(0, 1));
        }

        return 0;
    }

    private String getEpisodeFromEpisodeIdentifierString(String episodeIdentifierString) {
        String lowerCaseSubject = episodeIdentifierString.toLowerCase().replaceAll("(\\.|\\_)", "").trim();
        String result = "0";
        if (lowerCaseSubject.indexOf('e') > -1) {
            int startIndex = lowerCaseSubject.indexOf('e');
            result = lowerCaseSubject.substring(startIndex + 1);
        }
        if (lowerCaseSubject.indexOf('x') > -1) {
            String[] pieces = lowerCaseSubject.split("x");
            result = pieces[1];
        }
        if (lowerCaseSubject.length() == 4) {
            result = lowerCaseSubject.substring(2);
        }
        if (lowerCaseSubject.length() == 3) {
            result = lowerCaseSubject.substring(1);
        }

        return result;
    }
}
