package com.walhalla.vibro.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.walhalla.ui.DLog;
import com.walhalla.vibro.LStorage;

import com.walhalla.vibro.PlayerManager;
import com.walhalla.vibro.R;
import com.walhalla.vibro.Utils;
import com.walhalla.vibro.activity.VibrationMode;
import com.walhalla.vibro.databinding.FragmentMainBinding;

import io.feeeei.circleseekbar.CSeekBar;

public class VibrationModeFragment extends Fragment {

    private static final String KEY_MODE = "app_key_mode";

    private Callback callback;
    private Button action;
    private VibrationMode mode;

    private TextView wait_time;
    private TextView vibration_time;

    private VibrationMode mode1 = null;
    private VibrationMode mode2 = null;


    private LStorage storage;

    private int middle_speed_begin;
    private int middle_speed_end;
    private String modeName;

    private ImageView imgAnim, imgAnim2;
    private State state = State.STOP;
    private FragmentMainBinding binding;

    public static Fragment newInstance(VibrationMode mode) {
        VibrationModeFragment fragment = new VibrationModeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_MODE, mode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            this.callback = (Callback) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement Callback");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }


    public interface Callback {
        void showToolbar(boolean visible);

        void makeToast(String message);


        //Start/Stop
        void onClickVibratorTestBtn(String status);

        //Navigation
        //void changeMode(VibrationMode mode);


        void waitTimeProgress(int curProcess);

        void vibroTimeProgress(int curProcess);

        void randomTime(int i);

        void onClickVibratorStop();
    }


    //private static final int LAUNCHES_UNTIL_PROMPT = 2;
    //public static boolean isSubDialogShown = false;


    //Billing
//    private BillingClient mBillingClient;
//    public Map<String, SkuDetails> mSkuDetailsMap = new HashMap();


    //public String mSkuId = "podpis_sku_id_1";


    /* renamed from: sP */
    //public ShineAnimator f61sP = null;
    //private int selectedTest;
    //Intent sharingIntent = new Intent("android.intent.action.SEND");

    //private Button sub_button;

    private CSeekBar sb1;
    private CSeekBar sb2;


    private final CSeekBar.OnSeekBarChangeListener listener = new CSeekBar.OnSeekBarChangeListener() {
        @Override
        public void onChanged(CSeekBar seekBar, int curValue) {


//            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(
//                    "@" + "" + "\n"
//                            + sb2.getCurProcess() + "/" + "@" + sb2.getMaxProcess() + "\n"
//            );
            switch (mode) {

                case RANDOM:
//                    Log.d(TAG, "@: " + sb1.getCurProcess() + "\t" + curValue);
//                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(
//                            "@" + sb1.getCurProcess() + "/" + sb1.getMaxProcess() + "\n"
//                    );

                    wait_time.setText(getString(R.string.randomness_rate, sb1.getCurProcess(), sb1.getMaxProcess()));
                    if (sb1.getCurProcess() == 0) {
                        callback.randomTime(100);
                    } else {
                        callback.randomTime(sb1.getCurProcess() * 1000);
                    }
                    break;
                /**
                 *
                 *
                 *
                 */
                case SIMPLE:
//                    Log.d(TAG, "@: " + sb1.getCurProcess() + "\t" + curValue);
//                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(
//                            "@" + sb1.getCurProcess() + "/" + sb1.getMaxProcess() + "\n");

                    callback.waitTimeProgress(500 - seekBar.getCurProcess());
                    wait_time.setText(genLevel(seekBar.getCurProcess()));
                    break;

                case EXTENDED:
                default:
                    int id = seekBar.getId();
                    if (id == R.id.sb1) {
                        wait_time.setText(getString(R.string.wait_time, seekBar.getCurProcess(), seekBar.getMaxProcess()));
                        callback.waitTimeProgress(seekBar.getCurProcess());
                        return;
                    } else if (id == R.id.sb2) {
                        vibration_time.setText(getString(R.string.vibration_time, seekBar.getCurProcess(), seekBar.getMaxProcess()));
                        callback.vibroTimeProgress(seekBar.getCurProcess());
                        return;
                    } else {
                        DLog.d("none: ");
                    }
                    break;
            }
        }
    };

//Billing
//    public void querySkuDetails() {
//        SkuDetailsParams.Builder newBuilder = SkuDetailsParams.newBuilder();
//        ArrayList arrayList = new ArrayList();
//        arrayList.add(this.mSkuId);
//        newBuilder.setSkusList(arrayList).setType(SkuType.SUBS);
//        this.mBillingClient.querySkuDetailsAsync(newBuilder.build(), new SkuDetailsResponseListener() {
//            public void onSkuDetailsResponse(int i, List<SkuDetails> list) {
//                if (i == 0) {
//                    for (SkuDetails skuDetails : list) {
//                        mSkuDetailsMap.put(skuDetails.getSku(), skuDetails);
//                    }
//                }
//            }
//        });
//    }


//    public void unPay() {
//        Editor edit = getSharedPreferences("subads", 0).edit();
//        edit.putBoolean("dontshowagain", false);
//        edit.apply();
//        this.linin.setVisibility(View.VISIBLE);
//        this.sub_button.setVisibility(View.VISIBLE);
//    }


//    public List<Purchase> queryPurchases() {
//        return this.mBillingClient.queryPurchases(SkuType.SUBS).getPurchasesList();
//    }


//    public void showCloseBtn(final ImageView imageView) {
//        new Thread() {
//            public void run() {
//                try {
//                    Thread.sleep(3000);
//                    getActivity().runOnUiThread(new Runnable() {
//                        public void run() {
//                            imageView.setVisibility(View.VISIBLE);
//                        }
//                    });
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //LinearLayout linin = view.findViewById(R.id.linin);
        this.storage = LStorage.getInstance(getActivity());

//        this.sub_button = findViewById(R.id.sub);
//        this.mBillingClient = BillingClient.newBuilder(this).setListener(new PurchasesUpdatedListener() {
//            public void onPurchasesUpdated(int i, @Nullable List<Purchase> list) {
//                if (i == 0 && list != null) {
//                    payComplete();
//                }
//            }
//        }).build();
//        this.mBillingClient.startConnection(new BillingClientStateListener() {
//            public void onBillingServiceDisconnected() {
//            }
//
//            public void onBillingSetupFinished(int i) {
//                if (i == 0) {
//                    querySkuDetails();
//                    List access$300 = queryPurchases();
//                    for (int i2 = 0; i2 < access$300.size(); i2++) {
//                        if (((Purchase) access$300.get(i2)).getSku().equals(mSkuId)) {
//                            payComplete();
//                        } else {
//                            unPay();
//                        }
//                    }
//                    if (access$300.isEmpty()) {
//                        unPay();
//                    }
//                }
//            }
//        });
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("subads", 0);
//        sharedPreferences.edit();
//        if (sharedPreferences.getBoolean("dontshow_again", true) || MainActivity.isSub) {
//            linin.setVisibility(View.GONE);
//            //this.sub_button.setVisibility(View.GONE);
//        }
//        if (!sharedPreferences.getBoolean("dontshow_again", false) && !isSubDialogShown) {
//            isSubDialogShown = true;
//            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), 16974122);
//            builder.setCancelable(false);
//            final View inflate = ((LayoutInflater) getActivity().getSystemService("layout_inflater")).inflate(R.layout.sub_lay, null);
//            builder.setView(inflate);
//            inflate.findViewById(R.id.buy).setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) {
////                    launchBilling(mSkuId);
//                }
//            });
//            final AlertDialog create = builder.create();
//            TextView textView = inflate.findViewById(R.id.terms);
//            textView.setText(fromHtml(getString(R.string.terms)));
//            textView.setMovementMethod(LinkMovementMethod.getInstance());
//            final ImageView imageView = inflate.findViewById(R.id.close);
//            imageView.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) {
//                    create.cancel();
//                }
//            });
//            create.setOnShowListener(new DialogInterface.OnShowListener() {
//                public void onShow(DialogInterface dialogInterface) {
//                    showCloseBtn(imageView);
//                    f61sP = new ShineAnimator().setShineView((ShineView) inflate.findViewById(R.id.shine))
//                            .setDelayEvery(2000, -500).setInterpolator(new LinearInterpolator()).start();
//                }
//            });
//            create.show();
//        }
        //getActivity().getApplicationContext();

        //this.isWork = false;
        //this.fiveStops = 0;

        sb1 = view.findViewById(R.id.sb1);
        sb2 = view.findViewById(R.id.sb2);

        wait_time = view.findViewById(R.id.label1);
        vibration_time = view.findViewById(R.id.text2);

        //appRater();
        Integer action_button_1;
        Integer action_button_2;

        String label1 = null;
        String label2 = null;


        //@ int sub_title;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        modeName = mode.name().toLowerCase();

        switch (mode) {

            case RANDOM:

                //mainContainer.setBackground(getResources().getDrawable(R.drawable.gradient_blue));

                //@ sub_title = R.string.title_mode_random;
                action_button_1 = R.string.mode_simple;
                action_button_2 = R.string.mode_manual;

                mode1 = VibrationMode.SIMPLE;
                mode2 = VibrationMode.EXTENDED;

                sb2.setVisibility(View.GONE);


                int _max = Integer.parseInt(storage.modeRandomMax());
                int _min = Integer.parseInt(storage.modeRandomMin());


                sb1.setMaxProcess(_max);

                //Current
                int current1 = preferences.getInt(modeName + R.id.sb1, _min);
                sb1.setCurProcess(current1);

                callback.waitTimeProgress(80);
                callback.vibroTimeProgress(65);
                label1 = getString(R.string.randomness_rate, _min, _max);
                break;

            case SIMPLE:

                //mainContainer.setBackground(getResources().getDrawable(R.drawable.gradient_default));

                //@ sub_title = R.string.title_mode_simple;
                action_button_1 = R.string.mode_random;
                action_button_2 = R.string.mode_manual;
                mode1 = VibrationMode.RANDOM;
                mode2 = VibrationMode.EXTENDED;

                DLog.d("@@@@@@@@@@@" + storage.modeSimpleMaxSpeed());
                int _max_speed = Integer.parseInt(storage.modeSimpleMaxSpeed());
                int _min_speed = Integer.parseInt(storage.modeSimpleMinSpeed());

                this.middle_speed_begin = _max_speed / 4;
                this.middle_speed_end = _max_speed - middle_speed_begin;

                sb1.setMaxProcess(_max_speed);

                //Current
                int current10 = preferences.getInt(modeName + R.id.sb1, _min_speed);
                sb1.setCurProcess(current10);

                callback.waitTimeProgress(500);
                callback.vibroTimeProgress(50);
                sb2.setVisibility(View.GONE);
                wait_time.setText(genLevel(_min_speed));
                //wait_time.setVisibility(View.GONE);
                vibration_time.setVisibility(View.GONE);
                break;


            case EXTENDED:
            default:

                //mainContainer.setBackground(getResources().getDrawable(R.drawable.gradient_orange));

                //sub_title = R.string.title_mode_manual;
                action_button_1 = R.string.mode_random;
                action_button_2 = R.string.mode_simple;

                mode1 = VibrationMode.RANDOM;
                mode2 = VibrationMode.SIMPLE;
//                app:wheel_cur_process="65"
//                app:wheel_max_process="1000"


                int minTimeOut = Integer.parseInt(storage.modeExtendedTimeoutMin());
                int maxTimeOut = Integer.parseInt(storage.modeExtendedTimeoutMax());

                int minDuration = Integer.parseInt(storage.modeExtendedDurationMin());
                int maxDuration = Integer.parseInt(storage.modeExtendedDurationMax());

                sb1.setMaxProcess(maxTimeOut);

                //Current
                int current100 = preferences.getInt(modeName + R.id.sb1, minTimeOut);
                sb1.setCurProcess(current100);


                label1 = getString(R.string.wait_time, minTimeOut, maxTimeOut);
                sb2.setMaxProcess(maxDuration);

                //Current
                int current1000 = preferences.getInt(modeName + R.id.sb2, minDuration);
                sb2.setCurProcess(current1000);

                label2 = getString(R.string.vibration_time, minDuration, maxDuration);

                //Init
                this.callback.waitTimeProgress(minTimeOut);//80-1000
                this.callback.vibroTimeProgress(minDuration);//65-1000
                break;
        }


        if (label1 != null) {
            wait_time.setText(label1);
        }
        if (label2 != null) {
            vibration_time.setText(label2);
        }


        // @@@       ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        // @@@       if (ab != null) {
        // @@@           ab.setSubtitle(sub_title);
        // @@@       }


//@@@ Переключение мода кнопками
//@@@       Button button = view.findViewById(R.id.action_simple_mode);
// @@@       if (action_button_1 != null) {
// @@@           button.setText(action_button_1);
// @@@           button.setOnClickListener(view13 -> {
// @@@               try {
// @@@                   switchMode(mode1);
// @@@               } catch (Exception e) {
// @@@                   //e.printStackTrace();
// @@@               }
// @@@           });
// @@@       }


// @@@       Button button2 = view.findViewById(R.id.b2);
// @@@       if (action_button_2 != null) {
// @@@           button2.setText(action_button_2);
// @@@           button2.setOnClickListener(view1 -> switchMode(mode2));
// @@@       }

        action = view.findViewById(R.id.vibratorTestBtn);
        action.setOnClickListener(this::runVibration);


        wait_time.setTextColor(-1);
        vibration_time.setTextColor(-1);

        sb1.setOnSeekBarChangeListener(listener);
//        sb1.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(-1, Mode.MULTIPLY));
//        if (VERSION.SDK_INT >= 16) {
//            sb1.getThumb().setColorFilter(Color.parseColor("#FF4364"), Mode.SRC_IN);
//        }

        sb2.setOnSeekBarChangeListener(listener);
//        sb2.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(-1, Mode.MULTIPLY));
//        if (VERSION.SDK_INT >= 16) {
//            sb2.getThumb().setColorFilter(Color.parseColor("#FF4364"), Mode.SRC_IN);
//        }

        imgAnim = view.findViewById(R.id.imgAnim);
        imgAnim2 = view.findViewById(R.id.imgAnim2);


//        Glide.with(getActivity().getBaseContext())
//                .load(R.drawable.circle_img1)
//                .apply(new RequestOptions().centerCrop())
//                .into(imgAnim);

        handlerAnimation = new Handler();
    }

    private Handler handlerAnimation;
    private Runnable runnableAnimation = new Runnable() {

        @Override
        public void run() {

            imgAnim.animate().scaleX(4f).scaleY(4f).alpha(0f)
                    .setDuration(900).withEndAction(() -> {
                        imgAnim.setScaleX(1f);
                        imgAnim.setScaleY(1f);
                        imgAnim.setAlpha(1f);
                    });


            imgAnim2.animate().scaleX(4f).scaleY(4f).alpha(0f)
                    .setDuration(800).withEndAction(() -> {
                        imgAnim2.setScaleX(1f);
                        imgAnim2.setScaleY(1f);
                        imgAnim2.setAlpha(1f);

                    });

            handlerAnimation.postDelayed(runnableAnimation, 1500);
        }
    };

    private int genLevel(int current_speed) {
        //slower ----> middle ---> faster
        if (current_speed > middle_speed_end) {
            return R.string.simple_level_faster;
        } else if (current_speed > middle_speed_begin) {
            return R.string.simple_level_middle;
        }
        return R.string.simple_level_slower;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.mode = (VibrationMode) getArguments().getSerializable(KEY_MODE);
        }
    }

//    public void appRater() {
//        SharedPreferences sharedPreferences = getSharedPreferences("apprater", 0);
//        if (!sharedPreferences.getBoolean("dontshowagain", false)) {
//            Editor edit = sharedPreferences.edit();
//            long j = sharedPreferences.getLong("launch_count", 0) + 1;
//            edit.putLong("launch_count", j);
//            if (Long.valueOf(sharedPreferences.getLong("date_firstlaunch", 0)).longValue() == 0) {
//                edit.putLong("date_firstlaunch", Long.valueOf(System.currentTimeMillis()).longValue());
//            }
//            Long valueOf = Long.valueOf(System.currentTimeMillis());
//            Long valueOf2 = Long.valueOf(sharedPreferences.getLong("last_show_dialod", 0));
//            if (j >= 2 && (j == 2 || valueOf.longValue() >= valueOf2.longValue() + 86400000)) {
//                edit.putLong("last_show_dialod", System.currentTimeMillis());
//                showRateDialog(edit);
//            }
//            edit.apply();
//        }
//    }


//    public void launchBilling(String str) {
//        this.mBillingClient.launchBillingFlow(this, BillingFlowParams.newBuilder().setSkuDetails((SkuDetails) this.mSkuDetailsMap.get(str)).build());
//    }


//    private void switchMode(VibrationMode mode) {
//        action.setText(R.string.action_start);
//        if (callback != null) {
//            callback.changeMode(mode);
//        }
//    }

    //android.hardware.vibrator-service.example

    private void runVibration(View view) {
        Button button = (Button) view;
        String status = button.getText().toString();
        if (callback != null) {
            callback.onClickVibratorTestBtn(status);
            if (status.equals(getString(R.string.action_start))) {
                switchSm(State.START);
            } else if (status.equals(getString(R.string.action_stop))) {
                switchSm(State.STOP);
            }
        }
    }

    public void switchSm(State start) {

        switch (start) {

            case START:
                if (getActivity() != null) {
                    Vibrator tmp = PlayerManager.getInstance(getActivity()).mVibrator;
                    if (tmp != null) {
                        action.setText(R.string.action_stop);
                    }
                }
                this.imgAnim.setVisibility(View.VISIBLE);
                this.imgAnim2.setVisibility(View.VISIBLE);
                this.runnableAnimation.run();
                state = start;
                break;

            case STOP:
                action.setText(R.string.action_start);
                this.handlerAnimation.removeCallbacks(runnableAnimation);
                this.callback.onClickVibratorStop();
                state = start;
                break;

            default:
                break;
        }
    }

    public enum State {
        START, STOP
    }
//    public void onClickRadioSelectTest(View view) {
//        this.selectedTest = view.getId();
//    }

    //    @NonNull
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.main, menu);
//        menu.findItem(R.id.main_share).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                sharingIntent.setFlags(67108864);
//                sharingIntent.putExtra("android.intent.extra.TEXT", "Look at this https://play.google.com/store/apps/details?id=com.vibrator.massage.relax");
//                sharingIntent.setType("text/plain");
//                startActivityForResult(sharingIntent, 0);
//                return false;
//            }
//        });
//        menu.findItem(R.id.more).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                try {
//                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://apps/developer?id=gree+games")));
//                } catch (ActivityNotFoundException e) {
//                    e.getMessage();
//                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/developer?id=gree+games")));
//                }
//                return false;
//            }
//        });
//
//    }

    public void onTabUnselected() {
        DLog.d("-------------");
        try {
            callback.onClickVibratorStop();
            Utils.onDestroyMode(getActivity(), sb1.getCurProcess(), sb2.getCurProcess(), mode);
        } catch (Exception e) {
            DLog.handleException(e);
        }
    }

    @Override
    public void onDestroy() {
        try {
            Utils.onDestroyMode(getActivity(), sb1.getCurProcess(), sb2.getCurProcess(), mode);
        } catch (Exception e) {
            DLog.handleException(e);
        }
        super.onDestroy();
    }

//    @Override
//    public void onPause() {
//        callback.stop();
//        Utils.onDestroyMode(getActivity(), sb1.getCurProcess(), sb2.getCurProcess(), mode);
//        super.onPause();
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//    }

}