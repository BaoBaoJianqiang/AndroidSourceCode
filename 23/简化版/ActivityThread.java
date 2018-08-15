public final class ActivityThread {
	private static ActivityThread sCurrentActivityThread;

    public static ActivityThread currentActivityThread() {
        return sCurrentActivityThread;
    }

    final Looper mLooper = Looper.myLooper();
    final H mH = new H();

    static IPackageManager sPackageManager;

    public static IPackageManager getPackageManager() {
        if (sPackageManager != null) {
            return sPackageManager;
        }
        IBinder b = ServiceManager.getService("package");
        sPackageManager = IPackageManager.Stub.asInterface(b);
        return sPackageManager;
    }


    private Activity performLaunchActivity(ActivityClientRecord r, Intent customIntent) 
        Activity activity = null;

        java.lang.ClassLoader cl = r.packageInfo.getClassLoader();
        activity = mInstrumentation.newActivity(
                    cl, component.getClassName(), r.intent);
        return activity;
    }

    private void handleLaunchActivity(ActivityClientRecord r, Intent customIntent) {
        Activity a = performLaunchActivity(r, customIntent);
    }

    public final LoadedApk getPackageInfoNoCheck(ApplicationInfo ai,
            CompatibilityInfo compatInfo) {
        return getPackageInfo(ai, compatInfo, null, false, true, false);
    }

    private LoadedApk getPackageInfo(ApplicationInfo aInfo, CompatibilityInfo compatInfo,
            ClassLoader baseLoader, boolean securityViolation, boolean includeCode,
            boolean registerPackage) {
        final boolean differentUser = (UserHandle.myUserId() != UserHandle.getUserId(aInfo.uid));
        synchronized (mResourcesManager) {
            WeakReference<LoadedApk> ref;
            if (differentUser) {
                ref = null;
            } else if (includeCode) {
                ref = mPackages.get(aInfo.packageName);
            } else {
                ref = mResourcePackages.get(aInfo.packageName);
            }

            LoadedApk packageInfo = ref != null ? ref.get() : null;
            if (packageInfo == null || (packageInfo.mResources != null
                    && !packageInfo.mResources.getAssets().isUpToDate())) {                
                packageInfo =
                    new LoadedApk(this, aInfo, compatInfo, baseLoader,
                            securityViolation, includeCode &&
                            (aInfo.flags&ApplicationInfo.FLAG_HAS_CODE) != 0, registerPackage);
                mPackages.put(aInfo.packageName,
                            new WeakReference<LoadedApk>(packageInfo));
            }

            return packageInfo;
        }
    }

    private void installContentProviders(
            Context context, List<ProviderInfo> providers) {
        final ArrayList<IActivityManager.ContentProviderHolder> results =
            new ArrayList<IActivityManager.ContentProviderHolder>();

        for (ProviderInfo cpi : providers) {
            IActivityManager.ContentProviderHolder cph = installProvider(context, null, cpi,
                    false /*noisy*/, true /*noReleaseNeeded*/, true /*stable*/);
            if (cph != null) {
                cph.noReleaseNeeded = true;
                results.add(cph);
            }
        }

        try {
            ActivityManagerNative.getDefault().publishContentProviders(
                getApplicationThread(), results);
        } catch (RemoteException ex) {
        }
    }
}