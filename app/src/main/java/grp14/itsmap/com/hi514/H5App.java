package grp14.itsmap.com.hi514;

import android.app.Application;
import android.content.Context;

import com.moonbloom.boast.BStyle;
import com.moonbloom.boast.Boast;

public class H5App extends Application {

    //region Variables
    //Only used for POJO classes that needs any kind of context to access system variables (resources, shared preferences, etc)
    public static Context context;
    //endregion

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        Boast.setDefaultBStyle(BStyle.INFO);
    }
}