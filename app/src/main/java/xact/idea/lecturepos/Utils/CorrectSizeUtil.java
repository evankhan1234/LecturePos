/**
 * Desciption:Class use for correct size of view to mobile screen<br>
 *
 * @author L.Hien
 * @date Dec 11, 2014
 */
package xact.idea.lecturepos.Utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * The Class CorrectSizeUtil.
 */

public class CorrectSizeUtil {

    /**
     * The m activity.
     */
    private Activity mActivity;

    private Context ctx;
    /**
     * xxx The screen width original.
     */
    private int screenWidthOriginal = 1080;

    /**
     * The screen rate.
     */
    public static float screenRate = 0;

    private boolean isUseTag = true;

    private static CorrectSizeUtil sCorrectSizeUtil;

    public CorrectSizeUtil(Context ctx) {
        this.ctx = ctx;
    }

    public CorrectSizeUtil(Activity activity) {
        this.mActivity = activity;
        this.ctx = activity.getApplicationContext();
        if (sCorrectSizeUtil != null) {
            if (sCorrectSizeUtil.mActivity != activity) {
                sCorrectSizeUtil = this;
            }
        } else {
            sCorrectSizeUtil = this;
        }
    }

    public static CorrectSizeUtil getInstance(Activity activity) {
        if (sCorrectSizeUtil == null) {
            sCorrectSizeUtil = new CorrectSizeUtil(activity);
        }
        if (sCorrectSizeUtil.mActivity == null) {
            sCorrectSizeUtil = new CorrectSizeUtil(activity);
        }
        if (sCorrectSizeUtil.mActivity != activity) {
            sCorrectSizeUtil = new CorrectSizeUtil(activity);
        }
        return sCorrectSizeUtil;
    }


    /**
     * TODO Function:xxx Sets the original width.<br>
     *
     * @param width the new width original
     * @author: L.Hien
     * @date: Dec 11, 2014
     */
    public void setWidthOriginal(int width) {
        screenWidthOriginal = width;
        screenRate = 0;
    }

    /**
     * TODO Function:xxx Correct size of current activity from root.<br>
     *
     * @author: L.Hien
     * @date: Dec 11, 2014
     */
    public void correctSize() {

//		correctSize(root); // xxxx change new function for context
        if (ctx instanceof Activity) {
            View root = ((Activity) ctx).findViewById(android.R.id.content);
            correctSize(root);
        }
    }

    /**
     * TODO Function:xxx Correct size of input view.<br>
     *
     * @param root the root view that need to correct size
     * @author: L.Hien
     * @date: Dec 11, 2014
     */
    public void correctSize(View root) {
        if (root == null) {
            return;
        }
        // xxx stop correct action when view is viewgroup and corrected size
        if (root instanceof ViewGroup) {
            if (isUseTag) {
                if (root.getTag() != null) {
                    String tag = "correctSize";
                    try {
                        tag = (String) root.getTag();
                    } catch (Exception e) {

                    }
                    if (tag.equals("correctSize")) {
                        return;
                    }
                }
            } else {
                if (root.getContentDescription() != null) {
                    String tag = "correctSize";
                    try {
                        tag = root.getContentDescription().toString();
                    } catch (Exception e) {

                    }
                    if (tag.equals("correctSize")) {
                        return;
                    }
                }
            }
        }

        // xxx correct size of current view
        correctSizeByLayout(root);

        // xxx correct size for each child view when current view is viewgroup
        if (root instanceof ViewGroup) {
            if (isUseTag) {
                root.setTag("correctSize");
            } else {
                root.setContentDescription("correctSize");
            }
            ViewGroup group = (ViewGroup) root;
            for (int i = 0; i < group.getChildCount(); i++) {
                correctSize(group.getChildAt(i));
            }
        }

    }

    /**
     * TODO Function:xxx Gets the screen rate.<br>
     *
     * @return the screen rate
     * @author: L.Hien
     * @date: Dec 11, 2014
     */
    @SuppressWarnings("deprecation")

    public float getScreenRateOld() {
        if (screenRate == 0) {
            Display display = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay();
            float scale = ctx.getResources().getDisplayMetrics().density;
            int width = display.getWidth();
            int height = display.getHeight();
            if (width > height) {
                int tmp = width;
                width = height;
                height = tmp;
            }
            screenWidth = width;
            screenRate = width * 1.0f / screenWidthOriginal;
        }

        // xxx calculate screen rate base on current screen size and original
        // screen size

//		mCurrentRatio = width / height;

//		screenRate = width  * 1.0f / screenWidthOriginal;

        return screenRate;
    }

    public float getScreenRateByMultiScreen() {
        if (screenRate == 0) {
            //---------------------------------
            DisplayMetrics metrics = new DisplayMetrics();
            (mActivity).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            //Log.e("Multi", "MultiWindowMode"+"_"+metrics.widthPixels);

            //---------------------------------
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
//            if (width > height) {
//                int tmp = width;
//                width = height;
//                height = tmp;
//            }
            screenWidth = width;
            screenRate = width * 1.0f / screenWidthOriginal;
        }

        // xxx calculate screen rate base on current screen size and original
        // screen size

//		mCurrentRatio = width / height;

//		screenRate = width  * 1.0f / screenWidthOriginal;

        return screenRate;
    }

    public static int screenWidth = 0;

    /**
     * TODO Function:Correct size by layout.<br>
     *
     * @param v the v
     * @author: L.Hien
     * @date: Dec 11, 2014
     */
    public void correctSizeByLayout(View v) {
        float screenRate = 0;
        screenRate = getScreenRateByMultiScreen();
        boolean isRoundDown = false;

        // xxx if view not attact to any other view , stop correct action
        if (v.getLayoutParams() == null) {
            return;
        }

        if (Build.VERSION.SDK_INT > 15) {

            int minHeight = v.getMinimumHeight();
            if (minHeight > 0) {
                if (isRoundDown) {
                    int newMinHeight = (int) Math.floor(minHeight * screenRate);
                    v.setMinimumHeight(newMinHeight);
                } else {
                    int newMinHeight = (int) Math.ceil(minHeight * screenRate);
                    v.setMinimumHeight(newMinHeight);
                }
            }

            int minWidth = v.getMinimumWidth();
            if (minWidth > 0) {
                if (isRoundDown) {
                    int newMinWidth = (int) Math.floor(minWidth * screenRate);
                    v.setMinimumWidth(newMinWidth);
                } else {
                    int newMinWidth = (int) Math.ceil(minWidth * screenRate);
                    v.setMinimumWidth(newMinWidth);
                }
            }
        }


        // xxx only do correct size for suitable width value
        if (v.getLayoutParams().width > 0) {
            // xxx for fiction value , increase to 1 or decrease to 0 base on
            // rounddown flag
            if (isRoundDown) {
                v.getLayoutParams().width = (int) Math.floor(v
                        .getLayoutParams().width * screenRate);
            } else {
                v.getLayoutParams().width = (int) Math
                        .ceil(v.getLayoutParams().width * screenRate);
            }
        }
        // xxx only do correct size for suitable height value
        if (v.getLayoutParams().height > 0) {
            // xxx for fiction value , increase to 1 or decrease to 0 base on
            // rounddown flag
            if (isRoundDown) {
                v.getLayoutParams().height = (int) Math.floor(v
                        .getLayoutParams().height * screenRate);
            } else {
                v.getLayoutParams().height = (int) Math.ceil(v
                        .getLayoutParams().height * screenRate);
            }
        }
        // xxx correct margin of LinearLayout
        if (v.getLayoutParams() instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) v
                    .getLayoutParams();
            lp.setMargins(Math.round(lp.leftMargin * screenRate),
                    Math.round(lp.topMargin * screenRate),
                    Math.round(lp.rightMargin * screenRate),
                    Math.round(lp.bottomMargin * screenRate));
            v.setLayoutParams(lp);
        } else {
            // xxx correct margin of RelativeLayout
            if (v.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) v
                        .getLayoutParams();
                lp.setMargins(Math.round(lp.leftMargin * screenRate),
                        Math.round(lp.topMargin * screenRate),
                        Math.round(lp.rightMargin * screenRate),
                        Math.round(lp.bottomMargin * screenRate));
                v.setLayoutParams(lp);
            } else {
                // xxx correct margin of FrameLayout
                if (v.getLayoutParams() instanceof FrameLayout.LayoutParams) {
                    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) v
                            .getLayoutParams();
                    lp.setMargins(Math.round(lp.leftMargin * screenRate),
                            Math.round(lp.topMargin * screenRate),
                            Math.round(lp.rightMargin * screenRate),
                            Math.round(lp.bottomMargin * screenRate));
                    v.setLayoutParams(lp);
                }
            }
        }

        // xxx correct Padding size
        v.setPadding(Math.round(v.getPaddingLeft() * screenRate),
                Math.round(v.getPaddingTop() * screenRate),
                Math.round(v.getPaddingRight() * screenRate),
                Math.round(v.getPaddingBottom() * screenRate));

        if (v instanceof ListView) {
            int height = Math.round(((ListView) v).getDividerHeight()
                    * screenRate);
            if (height < 2 && ((ListView) v).getDividerHeight() != 0) {
                height = 2;
            }
            ((ListView) v).setDividerHeight(height);
        }

//        if(v instanceof ArcLayout){
//            ((ArcLayout) v).setAxisRadius((int) (((ArcLayout) v).getAxisRadius()*screenRate));
//        }

        // xxx check exited tag for correct text size
        Object checkItem = null;
        if (isUseTag) {
            checkItem = v.getTag();
        } else {
            checkItem = v.getContentDescription();
        }
        if (checkItem != null) {
            String tag = null;
            // xxx check existed of size from tag
            if (isUseTag) {
                tag = (String) v.getTag();
            } else {
                tag = v.getContentDescription().toString();
            }
            if (tag == null || tag.equals("")) {
                return;
            }
            int tagSize = 0;
            try {
                tagSize = Integer.parseInt(tag);
            } catch (Exception e) {

            }

            // xxx change text size for TextView to suit mobile screen
            if (v instanceof TextView) {
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX, tagSize
                        * screenRate);

                if (Build.VERSION.SDK_INT >= 16) {
                    ((TextView) v).setShadowLayer(((TextView) v).getShadowRadius(),
                            ((TextView) v).getShadowDx() * screenRate,
                            ((TextView) v).getShadowDy() * screenRate,
                            ((TextView) v).getShadowColor());
                }
            }

            // xxx change text size for EditText to suit mobile screen
            if (v instanceof EditText) {
                ((EditText) v).setTextSize(TypedValue.COMPLEX_UNIT_PX, tagSize
                        * screenRate);
            }

            // xxx change text size for Button to suit mobile screen
            if (v instanceof Button) {
                ((Button) v).setTextSize(TypedValue.COMPLEX_UNIT_PX, tagSize
                        * screenRate);
            }
            if (v instanceof ImageView) {

            } else {
                // xxx remove tag
                if (isUseTag) {
                    v.setTag(null);
                } else {
                    v.setContentDescription("");
                }

            }
        }
    }

    /**
     * TODO Function:xxx Gets the value after resize.<br>
     *
     * @param value the value
     * @return the value after resize
     * @author: L.Hien
     * @date: Dec 11, 2014
     */
    public static int getValueAfterResize(int value) {
        int return_value = Math.round(value * screenRate);
        return return_value;
    }
}
