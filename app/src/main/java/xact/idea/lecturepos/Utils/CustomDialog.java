package xact.idea.lecturepos.Utils;

import android.app.Dialog;
import android.content.Context;

public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.setCancelable(false);
    }

    public CustomDialog(Context context, int style) {
        super(context, style);
        // TODO Auto-generated constructor stub
        this.setCancelable(false);
    }

    @Override
    public void onBackPressed() {
        this.dismiss();
    }


}