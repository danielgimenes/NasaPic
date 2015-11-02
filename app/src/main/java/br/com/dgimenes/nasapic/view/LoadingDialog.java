package br.com.dgimenes.nasapic.view;

import android.app.ProgressDialog;
import android.content.Context;

import br.com.dgimenes.nasapic.R;

public class LoadingDialog {

    private ProgressDialog loadingDialog;
    private Context context;

    public LoadingDialog(Context context) {
        this.context = context;
    }

    public void dismiss() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    public void show() {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(context);
            loadingDialog.setIndeterminate(false);
            loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loadingDialog.setMessage(context.getResources().getString(R.string.wait));
            loadingDialog.setCancelable(false);
        }
        loadingDialog.show();
    }
}
