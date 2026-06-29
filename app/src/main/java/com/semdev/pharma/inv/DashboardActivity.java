package com.semdev.pharma.inv;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.*;
import android.content.DialogInterface;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.*;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.semdev.pharma.inv.databinding.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;

public class DashboardActivity extends Activity {
	
	private DashboardBinding binding;
	private double show = 0;
	
	private ArrayList<HashMap<String, Object>> pharma = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> pharma2 = new ArrayList<>();
	
	private RequestNetwork data;
	private RequestNetwork.RequestListener _data_request_listener;
	private AlertDialog.Builder about;
	private String cacheFilePath;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		binding = DashboardBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		data = new RequestNetwork(this);
		about = new AlertDialog.Builder(this);
		cacheFilePath = getFilesDir() + "/pharma_cache.json";
		
		binding.webview1.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView _param1, String _param2, Bitmap _param3) {
				final String _url = _param2;
				
				super.onPageStarted(_param1, _param2, _param3);
			}
			
			@Override
			public void onPageFinished(WebView _param1, String _param2) {
				final String _url = _param2;
				
				super.onPageFinished(_param1, _param2);
			}
		});
		
		binding.options.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				PopupMenu popup = new PopupMenu(DashboardActivity.this, binding.options);
				
				Menu menu = popup.getMenu();
				menu.add("Refresh");
				menu.add("About");
				menu.add("Exit");
				
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					
					public boolean onMenuItemClick(MenuItem item) {
						switch (item.getTitle().toString()) {
							case "Refresh":
							data.startRequestNetwork(RequestNetworkController.GET, "https://api.jsonsilo.com/public/5b34b860-5791-454a-9838-c30b35d3883f", "Get Data List", _data_request_listener);
							binding.webview1.loadUrl(binding.webview1.getUrl());
							return true;
							
							case "About":
							about.setTitle("ABOUT THE APPLICATION");
							about.setIcon(R.drawable.icon_sticky_note_2_round);
							about.setMessage("This application allows pharmacy customers to easily view current medicine prices and details.\n\nKey features include:\n\n• Comprehensive Medicine Listing: Browse a complete list of available medications with their current prices.\n• Detailed Medicine Information: View detailed information about each medicine, including description, dosage, and other relevant details, simply by clicking on its name.\n• Up-to-Date Pricing: Rest assured that all prices displayed are current and reflect the pharmacy's most recent updates.\n\nThis application is ideal for pharmacy customers who want to quickly check prices and access detailed information about medications before visiting the pharmacy. It provides a convenient way to stay informed about medication costs and details.");
							about.setPositiveButton("Okay!", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface _dialog, int _which) {
									
								}
							});
							about.create().show();
							return true;
							
							case "Exit":
							finishAffinity();
							return true;
							
							default: return false;
						}
					}
				});
				
				
				popup.show();
			}
		});
		
		binding.edittext1.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				String query = _charSeq.toString().toLowerCase();
				ArrayList<HashMap<String, Object>> filteredList = new ArrayList<>();
				if (query.isEmpty()) {
					filteredList = pharma;
				} else {
					for (HashMap<String, Object> map : pharma) {
						if (map.containsKey("Name")) {
							String name = map.get("Name").toString().toLowerCase();
							if (name.contains(query)) {
								filteredList.add(map);
							}
						}
					}
				}
				pharma2 = filteredList;
				Listview1Adapter adapter = new Listview1Adapter(pharma2);
				binding.listview1.setAdapter(adapter);
				((BaseAdapter)binding.listview1.getAdapter()).notifyDataSetChanged();
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		_data_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				FileUtil.writeFile(cacheFilePath, _response);
				pharma = new Gson().fromJson(_response, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
				pharma2 = new Gson().fromJson(_response, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
				binding.listview1.setAdapter(new Listview1Adapter(pharma));
				((BaseAdapter)binding.listview1.getAdapter()).notifyDataSetChanged();
				binding.edittext1.setText("");
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				String cachedData = FileUtil.readFile(cacheFilePath);
				if (!cachedData.isEmpty()) {
					pharma = new Gson().fromJson(cachedData, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
					pharma2 = new Gson().fromJson(cachedData, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
					binding.listview1.setAdapter(new Listview1Adapter(pharma));
					((BaseAdapter)binding.listview1.getAdapter()).notifyDataSetChanged();
					SketchwareUtil.showMessage(getApplicationContext(), "Loaded from offline cache");
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), _message);
				}
			}
		};
	}
	
	private void initializeLogic() {
		String cachedData = FileUtil.readFile(cacheFilePath);
		if (!cachedData.isEmpty()) {
			pharma = new Gson().fromJson(cachedData, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
			pharma2 = new Gson().fromJson(cachedData, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
			binding.listview1.setAdapter(new Listview1Adapter(pharma));
			((BaseAdapter)binding.listview1.getAdapter()).notifyDataSetChanged();
		}
		data.startRequestNetwork(RequestNetworkController.GET, "https://api.jsonsilo.com/public/5b34b860-5791-454a-9838-c30b35d3883f", "Get Data List", _data_request_listener);
		binding.box.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)15, 0xFFFFFFFF));
		
		//Webview Settings
		binding.webview1.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		binding.webview1.setWebViewClient(new WebViewClient());
		binding.webview1.getSettings().setJavaScriptEnabled(true);
		binding.webview1.loadUrl("https://scabpharmacy.com/");
		binding.webview1.setVisibility(View.GONE);
	}
	
	@Override
	public void onBackPressed() {
		show++;
		if (show == 1) {
			binding.linBase.setVisibility(View.GONE);
			binding.webview1.setVisibility(View.VISIBLE);
			SketchwareUtil.showMessage(getApplicationContext(), "WebView");
		} else {
			if (show == 2) {
				binding.linBase.setVisibility(View.VISIBLE);
				binding.webview1.setVisibility(View.GONE);
				show = 0;
				SketchwareUtil.showMessage(getApplicationContext(), "Dashboard");
			}
		}
	}
	public class Listview1Adapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		
		@Override
		public View getView(final int _position, View _v, ViewGroup _container) {
			ItemsBinding binding = ItemsBinding.inflate(getLayoutInflater());
			View _view = binding.getRoot();
			
			binding.textview1.setText(_data.get((int)_position).get("Name").toString());
			binding.textview2.setText(_data.get((int)_position).get("Sales Price").toString());
			
			return _view;
		}
	}
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}