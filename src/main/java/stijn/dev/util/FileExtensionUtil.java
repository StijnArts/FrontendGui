package stijn.dev.util;

import stijn.dev.datasource.objects.data.enums.*;

import java.util.*;

public class FileExtensionUtil {
    public static final String MULTIPLE = "Multiple";

    public static final String NOT_RECOGNIZED = "Not Recognized";

    private static HashMap<String,String> extensionsForPlatforms = new HashMap<>();
    private static boolean extensionsForPlatformsHasBeenFilled = false;

    private static boolean hasBeenFilled = false;
    private static ArrayList<String> fileExtensions = new ArrayList<>();
    private static HashMap<String, MediaTypes> mediaFileExtensions = new HashMap<>();
    private static boolean mediaFileExtensionsHasBeenFilled = false;

    public static HashMap<String,String> getExtensionsForPlatforms() {
        if(!extensionsForPlatformsHasBeenFilled) {
            extensionsForPlatforms.put("rar", MULTIPLE);
            extensionsForPlatforms.put("7z", MULTIPLE);
            extensionsForPlatforms.put("7zip", MULTIPLE);
            extensionsForPlatforms.put("tar.gz.gz", MULTIPLE);
            extensionsForPlatforms.put("tar", MULTIPLE);
            extensionsForPlatforms.put("001", MULTIPLE);
            extensionsForPlatforms.put("part1", MULTIPLE);
            extensionsForPlatforms.put("r01", MULTIPLE);
            extensionsForPlatforms.put("gzip", MULTIPLE);
            extensionsForPlatforms.put("iso", MULTIPLE);
            extensionsForPlatforms.put("img", MULTIPLE);
            extensionsForPlatforms.put("ccd", MULTIPLE);
            extensionsForPlatforms.put("bin", MULTIPLE);
            extensionsForPlatforms.put("cue", MULTIPLE);
            extensionsForPlatforms.put("chd", MULTIPLE);
            extensionsForPlatforms.put("mdf", MULTIPLE);
            extensionsForPlatforms.put("mds", MULTIPLE);
            extensionsForPlatforms.put("ecm", "Sony Playstation");
            extensionsForPlatforms.put("cso", MULTIPLE);
            extensionsForPlatforms.put("gcz", MULTIPLE);
            extensionsForPlatforms.put("rvz", MULTIPLE);
            extensionsForPlatforms.put("cdi", "Sega Dreamcast");
            extensionsForPlatforms.put("gdi", "Sega Dreamcast");
            extensionsForPlatforms.put("sbl", MULTIPLE);
            extensionsForPlatforms.put("fds", "Nintendo Entertainment System");
            extensionsForPlatforms.put("ndd", "Nintendo 64");
            extensionsForPlatforms.put("wav", MULTIPLE);
            extensionsForPlatforms.put("tap", MULTIPLE);
            extensionsForPlatforms.put("tzx", MULTIPLE);
            extensionsForPlatforms.put("cdc", "Amstrad CPC");
            extensionsForPlatforms.put("cas", MULTIPLE);
            extensionsForPlatforms.put("nes", "Nintendo Entertainment System");
            extensionsForPlatforms.put("nez", "Nintendo Entertainment System");
            extensionsForPlatforms.put("unf", "Nintendo Entertainment System");
            extensionsForPlatforms.put("unif", "Nintendo Entertainment System");
            extensionsForPlatforms.put("smc", "Super Nintendo Entertainment System");
            extensionsForPlatforms.put("sfc", "Super Nintendo Entertainment System");
            extensionsForPlatforms.put("md", "Sega Genesis");
            extensionsForPlatforms.put("smd", "Sega Genesis");
            extensionsForPlatforms.put("gen", "Sega Genesis");
            extensionsForPlatforms.put("gg", "Sega Game Gear");
            extensionsForPlatforms.put("z64", "Nintendo 64");
            extensionsForPlatforms.put("v64", "Nintendo 64");
            extensionsForPlatforms.put("n64", "Nintendo 64");
            extensionsForPlatforms.put("gb", "Nintendo Game Boy");
            extensionsForPlatforms.put("gbc", "Nintendo Game Boy Color");
            extensionsForPlatforms.put("gba", "Nintendo Game Boy Advance");
            extensionsForPlatforms.put("gcm", "Nintendo GameCube");
            extensionsForPlatforms.put("nkit.iso", MULTIPLE);
            extensionsForPlatforms.put("nds", "Nintendo DS");
            extensionsForPlatforms.put("srl", MULTIPLE);
            extensionsForPlatforms.put("dsi", "Nintendo DS");
            extensionsForPlatforms.put("app", "Nintendo DS");
            extensionsForPlatforms.put("ids", "Nintendo DS");
            extensionsForPlatforms.put("wbfs", "Nintendo Wii");
            extensionsForPlatforms.put("wad", MULTIPLE);
            extensionsForPlatforms.put("cia", "Nintendo 3DS");
            extensionsForPlatforms.put("3ds", "Nintendo 3DS");
            extensionsForPlatforms.put("nsp", "Nintendo Switch");
            extensionsForPlatforms.put("xci", "Nintendo Switch");
            extensionsForPlatforms.put("ngp", MULTIPLE);
            extensionsForPlatforms.put("ngc", MULTIPLE);
            extensionsForPlatforms.put("pce", MULTIPLE);
            extensionsForPlatforms.put("vpk", "Sony Playstation Vita");
            extensionsForPlatforms.put("vb", "Nintendo Virtual Boy");
            extensionsForPlatforms.put("ws", "WonderSwan");
            extensionsForPlatforms.put("wsc", "WonderSwanColor");
            extensionsForPlatforms.put("ipa", "Apple iOS");
            extensionsForPlatforms.put("apk", "Android");
            extensionsForPlatforms.put("obb", "Android");
            extensionsForPlatforms.put("elf", MULTIPLE);
            extensionsForPlatforms.put("pbp", MULTIPLE);
            extensionsForPlatforms.put("dol", MULTIPLE);
            extensionsForPlatforms.put("xbe", MULTIPLE);
            extensionsForPlatforms.put("xex", MULTIPLE);
            extensionsForPlatforms.put("xml", MULTIPLE);
            extensionsForPlatforms.put("hsi", "MESS");
            extensionsForPlatforms.put("lay", MULTIPLE);
            extensionsForPlatforms.put("nv", MULTIPLE);
            extensionsForPlatforms.put("m3u", MULTIPLE);
            extensionsForPlatforms.put("exe", "Windows");
            extensionsForPlatforms.put("lnk", "Windows");
            extensionsForPlatforms.put("url", "Windows");
            extensionsForPlatforms.put("cda", "Windows");
            extensionsForPlatforms.put("rpx", "Nintendo Wii U");
            extensionsForPlatformsHasBeenFilled = true;
        }
        return extensionsForPlatforms;
    }

    public static ArrayList<String> getFileExtensions(){
        if(!hasBeenFilled) {
            fileExtensions.add("rar");
            fileExtensions.add("7z");
            fileExtensions.add("7zip");
            fileExtensions.add("gz");
            fileExtensions.add("tar");
            fileExtensions.add("001");
            fileExtensions.add("part1");
            fileExtensions.add("r01");
            fileExtensions.add("gzip");
            fileExtensions.add("iso");
            fileExtensions.add("img");
            fileExtensions.add("ccd");
            fileExtensions.add("bin");
            fileExtensions.add("cue");
            fileExtensions.add("chd");
            fileExtensions.add("mdf");
            fileExtensions.add("mds");
            fileExtensions.add("ecm");
            fileExtensions.add("cso");
            fileExtensions.add("gcz");
            fileExtensions.add("rvz");
            fileExtensions.add("cdi");
            fileExtensions.add("gdi");
            fileExtensions.add("sbl");
            fileExtensions.add("fds");
            fileExtensions.add("ndd");
            fileExtensions.add("wav");
            fileExtensions.add("tap");
            fileExtensions.add("tzx");
            fileExtensions.add("cdc");
            fileExtensions.add("cas");
            fileExtensions.add("nes");
            fileExtensions.add("nez");
            fileExtensions.add("unf");
            fileExtensions.add("unif");
            fileExtensions.add("smc");
            fileExtensions.add("sfc");
            fileExtensions.add("md");
            fileExtensions.add("smd");
            fileExtensions.add("gen");
            fileExtensions.add("gg");
            fileExtensions.add("z64");
            fileExtensions.add("v64");
            fileExtensions.add("n64");
            fileExtensions.add("gb");
            fileExtensions.add("gbc");
            fileExtensions.add("gba");
            fileExtensions.add("gcm");
            fileExtensions.add("nkit");
            fileExtensions.add("nds");
            fileExtensions.add("srl");
            fileExtensions.add("dsi");
            fileExtensions.add("app");
            fileExtensions.add("ids");
            fileExtensions.add("wbfs");
            fileExtensions.add("wad");
            fileExtensions.add("cia");
            fileExtensions.add("3ds");
            fileExtensions.add("nsp");
            fileExtensions.add("xci");
            fileExtensions.add("ngp");
            fileExtensions.add("ngc");
            fileExtensions.add("pce");
            fileExtensions.add("vpk");
            fileExtensions.add("vb");
            fileExtensions.add("ws");
            fileExtensions.add("wsc");
            fileExtensions.add("ipa");
            fileExtensions.add("apk");
            fileExtensions.add("obb");
            fileExtensions.add("elf");
            fileExtensions.add("pbp");
            fileExtensions.add("dol");
            fileExtensions.add("xbe");
            fileExtensions.add("xex");
            fileExtensions.add("xml");
            fileExtensions.add("hsi");
            fileExtensions.add("lay");
            fileExtensions.add("nv");
            fileExtensions.add("m3u");
            fileExtensions.add("exe");
            fileExtensions.add("lnk");
            fileExtensions.add("url");
            fileExtensions.add("cda");
            fileExtensions.add("rpx");
            hasBeenFilled =true;
        }
        return fileExtensions;
    }

    public static HashMap<String, MediaTypes> getMediaFileExtensions() {
        if(!mediaFileExtensionsHasBeenFilled) {
            mediaFileExtensions.put("png", MediaTypes.IMAGE);
            mediaFileExtensions.put("jpeg", MediaTypes.IMAGE);
            mediaFileExtensions.put("jpg", MediaTypes.IMAGE);
            mediaFileExtensions.put("jpe", MediaTypes.IMAGE);
            mediaFileExtensions.put("jif", MediaTypes.IMAGE);
            mediaFileExtensions.put("jfif", MediaTypes.IMAGE);
            mediaFileExtensions.put("jfi", MediaTypes.IMAGE);
            mediaFileExtensions.put("jp2", MediaTypes.IMAGE);
            mediaFileExtensions.put("j2k", MediaTypes.IMAGE);
            mediaFileExtensions.put("jpf", MediaTypes.IMAGE);
            mediaFileExtensions.put("jpm", MediaTypes.IMAGE);
            mediaFileExtensions.put("jpg2", MediaTypes.IMAGE);
            mediaFileExtensions.put("j2c", MediaTypes.IMAGE);
            mediaFileExtensions.put("jpc", MediaTypes.IMAGE);
            mediaFileExtensions.put("jpx", MediaTypes.IMAGE);
            mediaFileExtensions.put("mj2", MediaTypes.IMAGE);
            mediaFileExtensions.put("webp", MediaTypes.IMAGE);
            mediaFileExtensions.put("hdr", MediaTypes.IMAGE);
            mediaFileExtensions.put("heif", MediaTypes.IMAGE);
            mediaFileExtensions.put("heifs", MediaTypes.IMAGE);
            mediaFileExtensions.put("heic", MediaTypes.IMAGE);
            mediaFileExtensions.put("heics", MediaTypes.IMAGE);
            mediaFileExtensions.put("avci", MediaTypes.IMAGE);
            mediaFileExtensions.put("avcs", MediaTypes.IMAGE);
            mediaFileExtensions.put("avif", MediaTypes.IMAGE);
            mediaFileExtensions.put("jxl", MediaTypes.IMAGE);
            mediaFileExtensions.put("tiff", MediaTypes.IMAGE);
            mediaFileExtensions.put("tif", MediaTypes.IMAGE);
            mediaFileExtensions.put("bmp", MediaTypes.IMAGE);
            mediaFileExtensions.put("dib", MediaTypes.IMAGE);
            mediaFileExtensions.put("pbm", MediaTypes.IMAGE);
            mediaFileExtensions.put("pgm", MediaTypes.IMAGE);
            mediaFileExtensions.put("ppm", MediaTypes.IMAGE);
            mediaFileExtensions.put("pnm", MediaTypes.IMAGE);
            mediaFileExtensions.put("svg", MediaTypes.IMAGE);
            mediaFileExtensions.put("svgz", MediaTypes.IMAGE);
            mediaFileExtensions.put("gif", MediaTypes.IMAGE);
            mediaFileExtensionsHasBeenFilled = true;
        }
        return mediaFileExtensions;
    }

    public static void setMediaFileExtensions(HashMap<String, MediaTypes> mediaFileExtensions) {
        FileExtensionUtil.mediaFileExtensions = mediaFileExtensions;
    }

    public static MediaTypes getMediaType(String fileExtension) {
        return getMediaFileExtensions().get(fileExtension.toLowerCase());
    }
}
