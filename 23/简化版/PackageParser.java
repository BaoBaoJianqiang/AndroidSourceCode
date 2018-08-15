public class PackageParser {

	public Package parsePackage(File packageFile, int flags) throws PackageParserException {
        if (packageFile.isDirectory()) {
        	//Multi App
            return parseClusterPackage(packageFile, flags);
        } else {
        	//Single App
            return parseMonolithicPackage(packageFile, flags);
        }
    }

    @Deprecated
    public Package parseMonolithicPackage(File apkFile, int flags) throws PackageParserException {
        if (mOnlyCoreApps) {
            final PackageLite lite = parseMonolithicPackageLite(apkFile, flags);
            if (!lite.coreApp) {
                throw new PackageParserException(INSTALL_PARSE_FAILED_MANIFEST_MALFORMED,
                        "Not a coreApp: " + apkFile);
            }
        }

        final AssetManager assets = new AssetManager();
        try {
            final Package pkg = parseBaseApk(apkFile, assets, flags);
            pkg.codePath = apkFile.getAbsolutePath();
            return pkg;
        } finally {
            IoUtils.closeQuietly(assets);
        }
    }

    private Package parseBaseApk(File apkFile, AssetManager assets, int flags)
            throws PackageParserException {
		final Package pkg = parseBaseApk(res, parser, flags, outError);
        return pkg;    
    }

    private Package parseBaseApk(Resources res, XmlResourceParser parser, int flags,
            String[] outError) throws XmlPullParserException, IOException {
  		//循环处理manifest中的标签
		if (tagName.equals("application")) {
                if (!parseBaseApplication(pkg, res, parser, attrs, flags, outError)) {
                    return null;
                }
            } else if (tagName.equals("overlay")) {
          	}
        }
  	}

  	private boolean parseBaseApplication(Package owner, Resources res,
            XmlPullParser parser, AttributeSet attrs, int flags, String[] outError)
        throws XmlPullParserException, IOException {

        //....省略对Application元素属性解析多行代码....
        
	    //解析Application下的子元素结点,如activity,receiver,service等    
    	while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
                && (type != XmlPullParser.END_TAG || parser.getDepth() > innerDepth)) {
             String tagName = parser.getName();
            if (tagName.equals("activity")) {
                Activity a = parseActivity(owner, res, parser, attrs, flags, outError, false,
                        owner.baseHardwareAccelerated);
                if (a == null) {
                    mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                    return false;
                }

                owner.activities.add(a);

            } else if (tagName.equals("receiver")) {
                Activity a = parseActivity(owner, res, parser, attrs, flags, outError, true, false);
                if (a == null) {
                    mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                    return false;
                }

                owner.receivers.add(a);

            } else if (tagName.equals("service")) {
                Service s = parseService(owner, res, parser, attrs, flags, outError);
                if (s == null) {
                    mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                    return false;
                }

                owner.services.add(s);

            } else if (tagName.equals("provider")) {
                Provider p = parseProvider(owner, res, parser, attrs, flags, outError);
                if (p == null) {
                    mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                    return false;
                }

                owner.providers.add(p);
            }                
         }
        
       return true;      
	}  


	public static PackageInfo generatePackageInfo(PackageParser.Package p,
            int gids[], int flags, long firstInstallTime, long lastUpdateTime,
            Set<String> grantedPermissions, PackageUserState state) {

        return generatePackageInfo(p, gids, flags, firstInstallTime, lastUpdateTime,
                grantedPermissions, state, UserHandle.getCallingUserId());
    }

    public static PackageInfo generatePackageInfo(PackageParser.Package p,
            int gids[], int flags, long firstInstallTime, long lastUpdateTime,
            Set<String> grantedPermissions, PackageUserState state, int userId) {

        PackageInfo pi = new PackageInfo();
        pi.packageName = p.packageName;
        //设置各种pi的各种属性

		if ((flags&PackageManager.GET_ACTIVITIES) != 0) {
            //....省略多行代码,关注generateActivityInfo()....
        }
        
        if ((flags&PackageManager.GET_RECEIVERS) != 0) {
           //....省略多行代码,关注generateActivityInfo()....
        }
        
        if ((flags&PackageManager.GET_SERVICES) != 0) {
            //....省略多行代码,关注generateServiceInfo()....
        }
        
        if ((flags&PackageManager.GET_PROVIDERS) != 0) {
            //....省略多行代码,关注generateProviderInfo()....
        }

    }      
}