public final class AssetManager implements AutoCloseable {
	final String[] getResourceStringArray(final int id) {
        String[] retArray = getArrayStringResource(id);
        return retArray;
    }

    public final int addAssetPath(String path) {
        synchronized (this) {
            int res = addAssetPathNative(path);
            makeStringBlocks(mStringBlocks);
            return res;
        }
    }