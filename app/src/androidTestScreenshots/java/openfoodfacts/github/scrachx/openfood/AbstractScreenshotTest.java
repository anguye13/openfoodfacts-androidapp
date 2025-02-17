package openfoodfacts.github.scrachx.openfood;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Locale;

import openfoodfacts.github.scrachx.openfood.test.ScreenshotActivityTestRule;
import openfoodfacts.github.scrachx.openfood.test.ScreenshotParameter;
import openfoodfacts.github.scrachx.openfood.test.ScreenshotsLocaleProvider;
import openfoodfacts.github.scrachx.openfood.utils.LocaleHelper;
import openfoodfacts.github.scrachx.openfood.views.OFFApplication;

/**
 * Take screenshots...
 */
@RunWith(AndroidJUnit4.class)
public abstract class AbstractScreenshotTest {
    public static final String ACTION_NAME = "actionName";
    private static final String LOG_TAG = AbstractScreenshotTest.class.getSimpleName();
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CHANGE_CONFIGURATION);
    private static Locale initLocale;
    ScreenshotsLocaleProvider localeProvider = new ScreenshotsLocaleProvider();

    @SafeVarargs
    protected final void startScreenshotActivityTestRules(ScreenshotParameter screenshotParameter, ScreenshotActivityTestRule<? extends Activity>... activityRules) {
        changeLocale(screenshotParameter);
        for (ScreenshotActivityTestRule<? extends Activity> activityRule : activityRules) {
            activityRule.finishActivity();
            activityRule.setScreenshotParameter(screenshotParameter);
            activityRule.launchActivity(null);
        }
    }

    protected void startScreenshotActivityTestRules(ScreenshotParameter screenshotParameter, ScreenshotActivityTestRule<? extends Activity> activityRule,
                                                    Collection<Intent> intents) {

        changeLocale(screenshotParameter);
        for (Intent intent : intents) {
            activityRule.finishActivity();
            String title = intent.getStringExtra(ACTION_NAME);
            if (title != null) {
                activityRule.setName(title);
            }
            activityRule.setActivityIntent(intent);
            activityRule.setScreenshotParameter(screenshotParameter);
            activityRule.launchActivity(null);
        }
    }

    protected void changeLocale(ScreenshotParameter parameter) {
        changeLocale(parameter, OFFApplication.getInstance());
    }

    protected void changeLocale(ScreenshotParameter parameter, Context context) {
        Log.d(LOG_TAG, "Change parameters to " + parameter);
        LocaleHelper.setLocale(context, parameter.getLocale());
        final String countryName = parameter.getCountryTag();
    }

    @SafeVarargs
    public final void startForAllLocales(ScreenshotActivityTestRule<? extends Activity>... activityRule) {
        for (ScreenshotParameter screenshotParameter : localeProvider.getParameters()) {
            startScreenshotActivityTestRules(screenshotParameter, activityRule);
        }
    }

    @AfterClass
    public static void resetLanguage() {
        LocaleHelper.setLocale(initLocale);
    }

    @BeforeClass
    public static void initLanguage() {
        initLocale = LocaleHelper.getLocale();
    }
}
