// Generated code from Butter Knife. Do not modify!
package proyecto.codigo.acceso;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LogIn_ViewBinding implements Unbinder {
  private LogIn target;

  @UiThread
  public LogIn_ViewBinding(LogIn target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LogIn_ViewBinding(LogIn target, View source) {
    this.target = target;

    target._emailText = Utils.findRequiredViewAsType(source, R.id.input_login_username, "field '_emailText'", EditText.class);
    target._passwordText = Utils.findRequiredViewAsType(source, R.id.input_login_password, "field '_passwordText'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LogIn target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target._emailText = null;
    target._passwordText = null;
  }
}
