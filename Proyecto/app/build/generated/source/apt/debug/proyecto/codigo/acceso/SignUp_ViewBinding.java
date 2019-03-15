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

public class SignUp_ViewBinding implements Unbinder {
  private SignUp target;

  @UiThread
  public SignUp_ViewBinding(SignUp target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SignUp_ViewBinding(SignUp target, View source) {
    this.target = target;

    target.nombre = Utils.findRequiredViewAsType(source, R.id.signup_input_name, "field 'nombre'", EditText.class);
    target.ape1 = Utils.findRequiredViewAsType(source, R.id.signup_input_ape1, "field 'ape1'", EditText.class);
    target.ape2 = Utils.findRequiredViewAsType(source, R.id.signup_input_ape2, "field 'ape2'", EditText.class);
    target.username = Utils.findRequiredViewAsType(source, R.id.signup_input_username, "field 'username'", EditText.class);
    target.handicap = Utils.findRequiredViewAsType(source, R.id.signup_input_handicap, "field 'handicap'", EditText.class);
    target.pass1 = Utils.findRequiredViewAsType(source, R.id.signup_input_pass1, "field 'pass1'", EditText.class);
    target.pass2 = Utils.findRequiredViewAsType(source, R.id.signup_input_pass2, "field 'pass2'", EditText.class);
    target.signupButton = Utils.findRequiredViewAsType(source, R.id.btn_signup, "field 'signupButton'", Button.class);
    target.loginLink = Utils.findRequiredViewAsType(source, R.id.link_login, "field 'loginLink'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SignUp target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.nombre = null;
    target.ape1 = null;
    target.ape2 = null;
    target.username = null;
    target.handicap = null;
    target.pass1 = null;
    target.pass2 = null;
    target.signupButton = null;
    target.loginLink = null;
  }
}
