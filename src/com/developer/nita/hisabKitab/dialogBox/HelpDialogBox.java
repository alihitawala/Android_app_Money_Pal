package com.developer.nita.hisabKitab.dialogBox;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.developer.nita.hisabKitab.R;

/**
 * Created by aliHitawala on 6/14/13.
 */
public class HelpDialogBox extends Dialog {
    private String helpText;

    public HelpDialogBox(Context context, String helpText) {
        super(context, R.style.CustomDialogTheme);
        this.helpText = helpText;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.help_dialog);
        this.setCanceledOnTouchOutside(true);
        this.setValueOnDialogBox();
    }

    public void setValueOnDialogBox()
    {
        WebView mWebView = (WebView) findViewById(R.id.helptext);
        mWebView.loadData(this.helpText, "text/html", "utf-8");
    }

}
