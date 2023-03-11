package stijn.dev.data.importing.util;

import org.apache.commons.lang3.*;
import stijn.dev.records.*;

public class GameTitleMatcher {
    public static boolean isContainedInTitle(String gameTitle, RomImportRecord rom) {
        return StringUtils.stripAccents(gameTitle).toLowerCase().contains(StringUtils.stripAccents(rom.title().getValue().toLowerCase()));
    }

    public static boolean isExactMatch(String gameTitle, RomImportRecord rom) {
        return StringUtils.stripAccents(gameTitle).toLowerCase().matches(StringUtils.stripAccents(rom.title().getValue().toLowerCase()));
    }

    public static boolean segmentMatchesPartOfGameTitle(String gameTitle, String platform, String segment, RomImportRecord rom) {
        return StringUtils.stripAccents(gameTitle).toLowerCase().contains(StringUtils.stripAccents(segment.toLowerCase()))
                && platform.equals(rom.scrapeAsPlatform().getValue());
    }
}
