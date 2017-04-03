package ru.yandex.alexey.mylauncher;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.badoualy.stepperindicator.StepperIndicator;

import ru.yandex.alexey.mylauncher.pages.Page0;
import ru.yandex.alexey.mylauncher.pages.Page1;
import ru.yandex.alexey.mylauncher.pages.Page2;

public class WelcomActivity extends AppCompatActivity implements View.OnClickListener {
    private int curPage = 0;
    private int idColumnCount = R.id.btn_standard_size;
    private int idTheme = R.style.AppLightTheme_NoActionBar;
    private StepperIndicator stepperIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);

        stepperIndicator = (StepperIndicator) findViewById(R.id.indicator);
        stepperIndicator.setCurrentStep(curPage);
        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.page, new Page0());
            fragmentTransaction.commit();
        } else {
            curPage = savedInstanceState.getInt("cur_page");
            idColumnCount = savedInstanceState.getInt("id_column_count");
            idTheme = savedInstanceState.getInt("id_theme");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("cur_page", curPage);
        outState.putInt("id_column_count", idColumnCount);
        outState.putInt("id_theme", idTheme);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (curPage) {
            case 0:
                ++curPage;
                fragment = new Page1();
                break;
            case 1:
                idColumnCount = ((RadioButton)findViewById(R.id.btn_standard_size)).isChecked()
                        ? R.integer.column_count_standard_size
                        : R.integer.column_count_big_size;
                ++curPage;
                fragment = new Page2();
                break;
            case 2:
                idTheme = ((RadioButton)findViewById(R.id.light_theme)).isChecked()
                        ? R.style.AppLightTheme_NoActionBar
                        : R.style.AppDarkTheme_NoActionBar;
                ++curPage;
                break;
        }
        stepperIndicator.setCurrentStep(curPage);
        if (curPage == getResources().getInteger(R.integer.step_count)) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("id_column_count", idColumnCount);
            intent.putExtra("id_theme", idTheme);
            startActivity(intent);
            finish();
        } else {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.page, fragment);
            fragmentTransaction.commit();
        }
    }
}
