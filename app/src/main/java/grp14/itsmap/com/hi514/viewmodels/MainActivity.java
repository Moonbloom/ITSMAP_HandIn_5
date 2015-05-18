package grp14.itsmap.com.hi514.viewmodels;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

import com.moonbloom.boast.BStyle;
import com.moonbloom.boast.Boast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnTextChanged;
import grp14.itsmap.com.hi514.R;
import grp14.itsmap.com.hi514.adapters.TrainListAdapter;
import grp14.itsmap.com.hi514.models.Train;
import grp14.itsmap.com.hi514.services.TrainDownloadService;
import grp14.itsmap.com.hi514.utilities.NetworkHelper;

@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends ListActivity {

    //region Variables
    //Debug TAG
    private final transient String TAG = ((Object)this).getClass().getSimpleName();

    public static final String localBroadcastUpdateMsg = "local_broadcast_update_msg";
    public static final String listExtraTag = "list_extra_tag";

    private TrainListAdapter adapter;

    private List<Train> trainList;

    private TrainDownloadService service;
    private ServiceConnection serviceConnection;

    private String secretInput = "moon";
    private boolean secretUnlocked = false;
    //endregion

    //region Injects
    @InjectView(R.id.main_activity_filter_edittext) EditText filterEditText;

    @OnTextChanged(R.id.main_activity_filter_edittext)
    public void filterTextChanged(CharSequence text) {
        adapter.getFilter().filter(text);

        if(text.toString().toLowerCase().equals(secretInput)) {
            Boast.makeText(this, R.string.secret_unlocked);
            secretUnlocked = true;
        }
    }
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if(NetworkHelper.isNetworkAvailable()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            Boast.makeText(this, R.string.no_network_error, BStyle.ALERT);
        }

        ButterKnife.inject(this);

        trainList = new ArrayList<>();
        adapter = new TrainListAdapter(this, R.layout.train_list_tile, trainList);
        setListAdapter(adapter);

        registerLocalReceiver();

        setupBoundService();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent(this, TrainDownloadService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unbindService(serviceConnection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_update_list:
                if(NetworkHelper.isNetworkAvailable()) {
                    if(!secretUnlocked) {
                        updateList(new ArrayList<Train>());
                    }
                    Boast.makeText(this, R.string.main_activity_update_train_list_toast);
                    downloadTrainList();
                } else {
                    Boast.makeText(this, R.string.no_network_error, BStyle.ALERT);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupBoundService() {
        serviceConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                // We've bound to TrainBinder, cast the IBinder and get LocalService instance
                TrainDownloadService.TrainBinder trainBinder = (TrainDownloadService.TrainBinder) iBinder;
                service = trainBinder.getService();

                downloadTrainList();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {}
        };
    }

    private void downloadTrainList() {
        if(NetworkHelper.isNetworkAvailable()) {
            service.downloadList();
        }
    }

    private void registerLocalReceiver() {
        BroadcastReceiver messageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateList((ArrayList<Train>)intent.getSerializableExtra(listExtraTag));
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter(localBroadcastUpdateMsg));
    }

    private void updateList(ArrayList<Train> list) {
        trainList.clear();
        trainList.addAll(list);
        adapter.updateOriginalList(list);
        adapter.notifyDataSetChanged();
        filterEditText.setEnabled(true);
        adapter.getFilter().filter(filterEditText.getText().toString());
    }
}