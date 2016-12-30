package com.kratav.tinySurprise.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.adapter.TabPagerAdapter;

public class CategoryActivity extends AppCompatActivity implements OnClickListener {
	private ViewPager mPager;
	private ActionBar mActionbar;
	private ImageView backButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.setTheme(R.style.CategoryTheme);
		super.onCreate(savedInstanceState);
		mActionbar = getSupportActionBar();
		mActionbar.setDisplayShowTitleEnabled(false);
		mActionbar.setDisplayShowHomeEnabled(false);
		LayoutInflater li = LayoutInflater.from(this);
		View customView = li.inflate(R.layout.actionbar_category, null);
		LayoutParams l = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		backButton = (ImageView) customView
				.findViewById(R.id.backButtonCategory);
		backButton.setOnClickListener(this);
		mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME);
		mActionbar.setCustomView(customView, l);
		Toolbar parent =(Toolbar) customView.getParent();//first get parent toolbar of current action bar
		parent.setContentInsetsAbsolute(0,0);//
		/** Set tab navigation mode */
		mActionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		setContentView(R.layout.view_pager);
		/** Getting a reference to ViewPager from the layout */
		mPager = (ViewPager) findViewById(R.id.pager);

		/** Getting a reference to FragmentManager */
		FragmentManager fm = getSupportFragmentManager();

		/** Defining a listener for pageChange */
		ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				mActionbar.setSelectedNavigationItem(position);
			}
		};

		/** Setting the pageChange listener to the viewPager */
		mPager.setOnPageChangeListener(pageChangeListener);

		/** Creating an instance of FragmentPagerAdapter */
		TabPagerAdapter fragmentPagerAdapter = new TabPagerAdapter(fm);

		/** Setting the FragmentPagerAdapter object to the viewPager object */
		mPager.setAdapter(fragmentPagerAdapter);

		/** Defining tab listener */
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			}

			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				mPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
			}
		};

		/** Creating fragment1 Tab */
		Tab tab = mActionbar.newTab().setText("Products")
				.setTabListener(tabListener);

		mActionbar.addTab(tab);

		/** Creating fragment2 Tab */
		tab = mActionbar.newTab().setText("Relation")
				.setTabListener(tabListener);

		mActionbar.addTab(tab);

		tab = mActionbar.newTab().setText("Location")
				.setTabListener(tabListener);

		mActionbar.addTab(tab);
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.equals(backButton))
			finish();
	}
}
