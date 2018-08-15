private class H extends Handler {
	public void handleMessage(Message msg) {
        switch (msg.what) {
            case LAUNCH_ACTIVITY: {
                final ActivityClientRecord r = (ActivityClientRecord) msg.obj;

                r.packageInfo = getPackageInfoNoCheck(
                       r.activityInfo.applicationInfo, r.compatInfo);
                handleLaunchActivity(r, null);
            } break;
         }
    }
}