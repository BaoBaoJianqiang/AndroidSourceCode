public class BaseDexClassLoader extends ClassLoader {
    private final DexPathList pathList;

    public BaseDexClassLoader(String dexPath, File optimizedDirectory,
            String librarySearchPath, ClassLoader parent) {
        super(parent);

        this.pathList = new DexPathList(this, dexPath, 
            librarySearchPath, optimizedDirectory);
    }
}

public class DexPathList {
    private Element[] dexElements;

    public DexPathList(ClassLoader definingContext, String dexPath,
            String libraryPath, File optimizedDirectory) {
        this.dexElements = makeDexElements(splitDexPath(dexPath), optimizedDirectory);
    }

    private static List<File> splitDexPath(String path) {
        return splitPaths(path, false);
    }

    private static List<File> splitPaths(String searchPath, boolean directoriesOnly) {
        List<File> result = new ArrayList<>();

        if (searchPath != null) {
            for (String path : searchPath.split(File.pathSeparator)) {
                if (directoriesOnly) {
                    try {
                        StructStat sb = Libcore.os.stat(path);
                        if (!S_ISDIR(sb.st_mode)) {
                            continue;
                        }
                    } catch (ErrnoException ignored) {
                        continue;
                    }
                }
                result.add(new File(path));
            }
        }

        return result;
    }

    private static Element[] makeDexElements(ArrayList<File> files,
            File optimizedDirectory) {
        ArrayList<Element> elements = new ArrayList<Element>();
        for (File file : files) {
            ZipFile zip = null;
            DexFile dex = null;
            String name = file.getName();
            if (name.endsWith(DEX_SUFFIX)) {
                dex = loadDexFile(file, optimizedDirectory);
            } else if (name.endsWith(APK_SUFFIX) || name.endsWith(JAR_SUFFIX)
                    || name.endsWith(ZIP_SUFFIX)) {
                zip = new ZipFile(file);
            }
            ……
            if ((zip != null) || (dex != null)) {
                elements.add(new Element(file, zip, dex));
            }
        }
        return elements.toArray(new Element[elements.size()]);
    }

    private static DexFile loadDexFile(File file, File optimizedDirectory)
            throws IOException {
        if (optimizedDirectory == null) {
            return new DexFile(file);
        } else {
            String optimizedPath = optimizedPathFor(file, optimizedDirectory);
            return DexFile.loadDex(file.getPath(), optimizedPath, 0);
        }
    }

    /**
     * Converts a dex/jar file path and an output directory to an
     * output file path for an associated optimized dex file.
     */
    private static String optimizedPathFor(File path,
            File optimizedDirectory) {
        String fileName = path.getName();
        if (!fileName.endsWith(DEX_SUFFIX)) {
            int lastDot = fileName.lastIndexOf(".");
            if (lastDot < 0) {
                fileName += DEX_SUFFIX;
            } else {
                StringBuilder sb = new StringBuilder(lastDot + 4);
                sb.append(fileName, 0, lastDot);
                sb.append(DEX_SUFFIX);
                fileName = sb.toString();
            }
        }
        File result = new File(optimizedDirectory, fileName);
        return result.getPath();
    }
}