package custom;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;

/**
 * Created by xieyingwu on 2017/5/6.
 */

public class ExpandImageSwitcher extends ImageSwitcher {
    public ExpandImageSwitcher(Context context) {
        super(context);
    }

    public ExpandImageSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImageByGlide(int placeHolderRes, int errorRes, String url) {
        GlideUrl glideUrl = null;
        if (!TextUtils.isEmpty(url)) {
            glideUrl = new GlideUrl(url);
        }
        ImageView image = (ImageView) this.getNextView();
        Glide.with(this.getContext()).load(glideUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(placeHolderRes).error(errorRes).dontAnimate().into(image);
        showNext();
    }
}
