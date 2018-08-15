public interface IActivityManager extends IInterface {
    public int startActivity(IApplicationThread caller, String callingPackage, Intent intent,
            String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags,
            ProfilerInfo profilerInfo, Bundle options) throws RemoteException;

    public void activityPaused(IBinder token) throws RemoteException;
    
    public ComponentName startService(IApplicationThread caller, Intent service,
            String resolvedType, String callingPackage, int userId) throws RemoteException;
    public int stopService(IApplicationThread caller, Intent service,
            String resolvedType, int userId) throws RemoteException;
    public int bindService(IApplicationThread caller, IBinder token, Intent service,
            String resolvedType, IServiceConnection connection, int flags,
            String callingPackage, int userId) throws RemoteException;

    public ContentProviderHolder getContentProvider(IApplicationThread caller,
            String name, int userId, boolean stable) throws RemoteException;

    public Intent registerReceiver(IApplicationThread caller, String callerPackage,
            IIntentReceiver receiver, IntentFilter filter,
            String requiredPermission, int userId) throws RemoteException;
    public void unregisterReceiver(IIntentReceiver receiver) throws RemoteException;
    public int broadcastIntent(IApplicationThread caller, Intent intent,
            String resolvedType, IIntentReceiver resultTo, int resultCode,
            String resultData, Bundle map, String[] requiredPermissions,
            int appOp, Bundle options, boolean serialized, boolean sticky, int userId) throws RemoteException;

}
