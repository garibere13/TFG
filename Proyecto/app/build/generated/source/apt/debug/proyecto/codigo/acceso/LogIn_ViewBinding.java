// Generated code from Butter Knife. Do not modify!
package proyecto.codigo.acceso;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

    target.username = Utils.findRequiredViewAsType(source, R.id.input_login_username, "field 'username'", EditText.class);
    target.password = Utils.findRequiredViewAsType(source, R.id.input_login_password, "field 'password'", EditText.class);
    target.loginButton = Utils.findRequiredViewAsType(source, R.id.btn_login, "field 'loginButton'", Button.class);
    target.signupLink = Utils.findRequiredViewAsType(source, R.id.login_link_signup, "field 'signupLink'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LogIn target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.username = null;
    target.password = null;
    target.loginButton = null;
    target.signupLink = null;
  }
}
