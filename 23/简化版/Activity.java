public class Activity extends ContextThemeWrapper {

	public void startActivity(Intent intent, @Nullable Bundle options) {
        if (options != null) {
            startActivityForResult(intent, -1, options);
        } else {
            startActivityForResult(intent, -1);
        }
    }

	private Instrumentation mInstrumentation;	

	public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        //前后省略一些代码
        Instrumentation.ActivityResult ar =
                mInstrumentation.execStartActivity(
                    this, mMainThread.getApplicationThread(), mToken, this,
                    intent, requestCode, options);
    }
}
