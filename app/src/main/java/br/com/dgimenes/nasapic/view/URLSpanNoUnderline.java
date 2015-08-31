package br.com.dgimenes.nasapic.view;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannedString;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.widget.TextView;

public class URLSpanNoUnderline extends URLSpan {
    public URLSpanNoUnderline(String url) {
        super(url);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }

    public static Spannable removeUrlUnderline(CharSequence text) {
        Spannable spannable = new SpannableString(text);
        for (URLSpan span : spannable.getSpans(0, text.length(), URLSpan.class)) {
            int start = spannable.getSpanStart(span);
            int end = spannable.getSpanEnd(span);
            spannable.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            spannable.setSpan(span, start, end, 0);
        }
        return spannable;
    }
}
